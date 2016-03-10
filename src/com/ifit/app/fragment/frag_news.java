package com.ifit.app.fragment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ifit.app.R;
import com.ifit.app.activity.Home_page;
import com.ifit.app.activity.News_PublishedActivity;
import com.ifit.app.adapter.newsItemAdapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.newsItem;
import com.ifit.app.other.Normal_ListView_PullDownAndUp.MyListViewPullDownAndUp;
import com.ifit.app.other.Normal_ListView_PullDownAndUp.MyListViewPullDownAndUp.RefreshListener;


public class frag_news extends Fragment{
	
	private MyListViewPullDownAndUp MylistView;
	//private LinkedList<String> data;
	public static frag_news frag_news_instance = null;
	private LinkedList<newsItem> newsItemList = null;
	//private List<newsItem> newsItemList = new LinkedList<newsItem>();
	private ImageView published_news;
	Handler handler=new Handler();
	//private ArrayAdapter<String> adapter;
	private newsItemAdapter adapter;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private static String lastnews_id;
	
	private Cursor news_cursor,mark_cursor,headimg_cursor,nickname_cursor,good_cursor;
	String username;
	String[] like_name = null;
	int good_count;
	boolean islike;
	List list = null;
	String like_people = null;
	String UsingName = Home_page.UsingName ;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		frag_news_instance = this;
		View view = inflater.inflate(R.layout.news, container, false);
		
		MylistView = (MyListViewPullDownAndUp)view.findViewById(R.id.news_list);
		published_news = (ImageView)view.findViewById(R.id.news_published);//加号按钮
		
		published_news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent turn_publish = new Intent(getContext(),News_PublishedActivity.class);
				startActivity(turn_publish);
			}
		});
		
		usedb = new MyDatabaseHelper(getContext(), "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		newsItemList = new LinkedList<newsItem>();
		
		   initList();
	       adapter = new newsItemAdapter(getContext(), R.layout.user_news_item, newsItemList);
	       adapter.getUseingName(Home_page.UsingName);
	       MylistView.setAdapter(adapter);
	       MylistView.setRefreshListener(new MyRefreshListener());
	       
		return view;
	}
	
	
	public void initList(){
		
		news_cursor = db.query("User_news_table",null, 
				null, null, null,null, null);
		news_cursor.moveToLast();
		//Log.d("xxx", ""+news_cursor.getCount()+","+news_cursor.getPosition());
		
		
		if(news_cursor.getCount()>0){
			lastnews_id = news_cursor.getString(news_cursor.getColumnIndex("news_id"));
		}
		
		if (news_cursor.getCount() >= 5) {

			for (int i = 0; i <  10; i++) {
				// 这些查询本应该在服务器上做的
				setList();
				
			}
			

		} else {
			for (int i = 0; i < news_cursor.getCount(); i++) {
				setList();
				
			}
			
		}
		//Log.d("xxx", newsItemList.size()+"");
		
	}
	
	
	public void setList(){
		
		username = news_cursor.getString(news_cursor.getColumnIndex("name"));//这里获取的是发布者的账号，用于下面获取昵称用的
		
		String img1 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_1"));
		String img2 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_2"));
		String img3 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_3"));
		String img4 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_4"));
		
		//Log.d("xxx", "123"+img1+"123"+img2+"123"+img3+"123"+img4+"123");
		
		String getnews_id = ""+news_cursor.getInt(news_cursor.getColumnIndex("news_id"));
		
		int picnumber;
		if(img1==null){
			picnumber=0;
		}else if(img2==null){
			picnumber = 1;
		}else if(img3==null){
			picnumber = 2;
		}else if(img4 == null){
			picnumber = 3;
		}else{
			picnumber = 4;
		}
		
		String time_no_format = news_cursor.getString(news_cursor.getColumnIndex("time"));
		
		String time = getCompare_Time(time_no_format);
		
		//Log.d("xxx", time_format);
		String news_text = news_cursor.getString(news_cursor.getColumnIndex("words"));
		if(news_text == null){
			news_text = "";
		}
		
		
		//获取点赞数
		good_cursor = db.query("User_news_good_table", 
				null, "news_id = ?", new String[]{getnews_id}, null, null, null);
		
		good_cursor.moveToFirst();
		
		if(good_cursor.getCount()>0){
			like_people = good_cursor.getString(good_cursor.getColumnIndex("goodname"));
			like_name = like_people.split(",");
			good_count = like_name.length;
			}else{
				like_people = null;
				like_name =null;
				good_count = 0;
			}
		
		if(list != null && !list.isEmpty()){
			list=null;
			}
			if(like_name!=null && like_name.length>0){
			list = Arrays.asList(like_name);
			islike = list.contains(UsingName);
			}else{
				islike =  false;
			}
		
		
		//获取评论人数
		mark_cursor = db.query("User_news_mark_table", 
				null, "news_id = ?", new String[]{getnews_id}, null, null, null);
		int mark_count = mark_cursor.getCount();
		
		//Log.d("xxx", mark_count+"321");
		
		//获取用户昵称
		nickname_cursor = db.query("User_personal_info_table", 
				null, "name = ?", new String[]{username}, null, null, null);
		nickname_cursor.moveToFirst();
		String getnickname = nickname_cursor.getString(nickname_cursor.getColumnIndex("nickname"));
		String nickname = "";
		if(getnickname == null){
			nickname = "未设置用户名";
		}else{
			nickname = getnickname;
		}
		
		//获取头像
		Bitmap mbitmap;
		
		headimg_cursor = db.query("User_headImage_table", 
				null,"name = ?", new String[]{username}, null, null,null);
		headimg_cursor.moveToFirst();
		
		if (headimg_cursor.getBlob(headimg_cursor.getColumnIndex("user_head_img")) != null) {
			byte[] get_Headimg = headimg_cursor.getBlob(headimg_cursor
					.getColumnIndex("user_head_img"));
			mbitmap = BitmapFactory.decodeByteArray(get_Headimg, 0,
					get_Headimg.length);
		}else{
			mbitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_headimage);
		}
		
		
		
			if(picnumber == 0){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						mark_count,0,getnews_id,good_count,islike);
				newsItemList.add(item);
			}else if(picnumber == 1){
				
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,mark_count,1,getnews_id,good_count,islike);
				newsItemList.add(item);
			}else if(picnumber == 2){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,img2,mark_count,2,getnews_id,good_count,islike);
				newsItemList.add(item);
			}else if(picnumber == 3){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,img2,img3,mark_count,3,getnews_id,good_count,islike);
				newsItemList.add(item);
			}else if(picnumber == 4){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,img2,img3,img4,mark_count,4,getnews_id,good_count,islike);
				newsItemList.add(item);
			}
			if(!news_cursor.isFirst()){
				news_cursor.moveToPrevious();
			}
	}
	
	/*
	public void addfirstdata(Cursor cursor){
		username = cursor.getString(news_cursor.getColumnIndex("name"));
		
		String img1 = cursor.getString(news_cursor.getColumnIndex("imagepath_1"));
		String img2 = cursor.getString(news_cursor.getColumnIndex("imagepath_2"));
		String img3 = cursor.getString(news_cursor.getColumnIndex("imagepath_3"));
		String img4 = cursor.getString(news_cursor.getColumnIndex("imagepath_4"));
		
		//Log.d("xxx", "123"+img1+"123"+img2+"123"+img3+"123"+img4+"123");
		
		String getnews_id = ""+cursor.getInt(cursor.getColumnIndex("news_id"));
		
		int picnumber;
		if(img1==null){
			picnumber=0;
		}else if(img2==null){
			picnumber = 1;
		}else if(img3==null){
			picnumber = 2;
		}else if(img4 == null){
			picnumber = 3;
		}else{
			picnumber = 4;
		}
		
		String time_no_format = cursor.getString(cursor.getColumnIndex("time"));
		
		String time = getCompare_Time(time_no_format);
		
		//Log.d("xxx", time_format);
		String news_text = cursor.getString(cursor.getColumnIndex("words"));
		if(news_text == null){
			news_text = "";
		}
		
		
		//获取点赞数
		good_cursor = db.query("User_news_good_table", 
				null, "news_id = ?", new String[]{getnews_id}, null, null, null);
		int good_count = good_cursor.getCount();
		
		//Log.d("xxx", good_count+"123");
		
		
		//获取评论人数
		mark_cursor = db.query("User_news_mark_table", 
				null, "news_id = ?", new String[]{getnews_id}, null, null, null);
		int mark_count = mark_cursor.getCount();
		
		//Log.d("xxx", mark_count+"321");
		
		//获取用户昵称
		nickname_cursor = db.query("User_personal_info_table", 
				null, "name = ?", new String[]{username}, null, null, null);
		nickname_cursor.moveToFirst();
		String nickname = nickname_cursor.getString(nickname_cursor.getColumnIndex("nickname"));
		
		
		//获取头像
		Bitmap mbitmap;
		
		headimg_cursor = db.query("User_headImage_table", 
				null,"name = ?", new String[]{username}, null, null,null);
		headimg_cursor.moveToFirst();
		
		if (headimg_cursor.getBlob(headimg_cursor.getColumnIndex("user_head_img")) != null) {
			byte[] get_Headimg = headimg_cursor.getBlob(headimg_cursor
					.getColumnIndex("user_head_img"));
			mbitmap = BitmapFactory.decodeByteArray(get_Headimg, 0,
					get_Headimg.length);
		}else{
			mbitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_headimage);
		}
		
		
		
			if(picnumber == 0){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						good_count,mark_count,0);
				newsItemList.addFirst(item);
			}else if(picnumber == 1){
				
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,good_count,mark_count,1);
				newsItemList.addFirst(item);
			}else if(picnumber == 2){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,img2,good_count,mark_count,2);
				newsItemList.addFirst(item);
			}else if(picnumber == 3){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,img2,img3,good_count,mark_count,3);
				newsItemList.addFirst(item);
			}else if(picnumber == 4){
				newsItem item = new newsItem(mbitmap,nickname,
						time,news_text,
						img1,img2,img3,img4,good_count,mark_count,4);
				newsItemList.addFirst(item);
			}
	}
	*/
	
	class MyRefreshListener implements RefreshListener{ 
	       //处理下拉刷新
	           @Override
	           public void pullDownRefresh() { 
	               new Thread(new Runnable() { 
	                   @Override
	                   public void run() {
	                       SystemClock.sleep(1000);
	                       //data.addFirst(i+++"new下拉更新data……………………"); 
	                       
	                       
	                       handler.post(new Runnable() { 
	                           @Override
	                           public void run() {
	                        	   
	                        	   
	                        	   
	                        	
	                        	
	                       		Cursor cursor = db.query("User_news_table",null, 
	       	               				"news_id > ?", new String[]{lastnews_id},null, null,null);
	                       		cursor.moveToLast();
	                       		
	                       		if(cursor.getCount()>0){
	                       		lastnews_id = cursor.getString(cursor.getColumnIndex("news_id"));
	                       		int listsize = newsItemList.size()+cursor.getCount();
	                       		newsItemList.clear();
	                       		adapter.clear();
	                       		   for(int i=0;i < listsize ;i++){
	                       			setList();
	                       		   }
	       	                    adapter.notifyDataSetChanged();
	       	                    Toast.makeText(getContext(), "更新成功",Toast.LENGTH_LONG).show();
	                       		}else{
	                       			Toast.makeText(getContext(), "无更多内容！",Toast.LENGTH_LONG).show();
	                       		}
	                            MylistView.onPulldownRefreshComplete();
	                               
	                              // System.out.println(MylistView.getLastVisiblePosition()+"======="+adapter.getCount());
	                           }
	                       }); 
	                   }
	               }).start();
	           }
	   //处理上拉刷新
	           @Override
	           public void pullUpRefresh() {
	               new Thread(new Runnable() { 
	                   @Override
	                   public void run() {
	                       SystemClock.sleep(1000);
	                       //data.addLast(i+++"new上拉更新data……………………"); 
	                       
	                   
	                       
	                       handler.post(new Runnable() { 
	                           @Override
	                           public void run() {
	                        	   
	                        	   
	                        	int rest_news_count = news_cursor.getCount() - newsItemList.size();
	       	               		
	       	               		if(rest_news_count <= 0){
	       	               			Toast.makeText(getContext(), "无更多内容！", Toast.LENGTH_SHORT).show();
	       	               		}else{
	       	               			if(rest_news_count<=5){
	       	               				for(int i=0;i<rest_news_count;i++){
	       	               					setList();
	       	               				}
	       	               			}else{
	       	               				for(int i=0;i<5;i++){
	       	               					//这些查询本应该在服务器上做的
	       	               						setList();
	       	               					}
	       	               			}
	       	               			
	       	               		
	                             Toast.makeText(getContext(), "更新完成！",Toast.LENGTH_LONG).show();
	       	               		}
	                        	   
	                        	   
	                               adapter.notifyDataSetChanged();
	                               MylistView.onPullupRefreshComplete();
	                               //System.out.println(MylistView.getLastVisiblePosition()+"======="+adapter.getCount());
	                           }
	                       }); 
	                   }
	               }).start();
	           }
		
	       }

	public String getCompare_Time(String getpublishTime){
		
		String timeFormat;
		
		Time now_time = new Time();//这种方法获取时间虽然要写很多代码，但是好控制
		now_time.setToNow();
		String year = ""+now_time.year;
		String month = ""+(now_time.month+1);
		String date = ""+now_time.monthDay;
		String hour = ""+now_time.hour;
		String minute = ""+now_time.minute;
		String second = ""+now_time.second;
		
		if(month.length() == 1){
			month = "0"+month;
		}
		if(date.length() == 1){
			date = "0"+date;
		}
		if(hour.length() == 1){
			hour = "0"+hour;
		}
		if(minute.length() == 1){
			minute = "0"+minute;
		}
		
		timeFormat = getpublishTime.substring(0, 4)
				  +"-"+getpublishTime.substring(4,6)
				  +"-"+getpublishTime.substring(6, 8);
		
		int year_number = Integer.parseInt(year);
		
		int pub_year_number = Integer.parseInt(getpublishTime.substring(0, 4));
		
		if((year_number - pub_year_number)==0 ){
			int month_number = Integer.parseInt(month);
			int pub_month_number = Integer.parseInt(getpublishTime.substring(4, 6));
			if((month_number - pub_month_number)==0){
				int date_number = Integer.parseInt(date);
				int pub_date_number = Integer.parseInt(getpublishTime.substring(6, 8));
				if((date_number - pub_date_number) == 0){
					int hour_number = Integer.parseInt(hour);
					int pub_hour_number = Integer.parseInt(getpublishTime.substring(8, 10));
					if((hour_number - pub_hour_number) == 0){
						int minute_number = Integer.parseInt(minute);
						int pub_minute_number = Integer.parseInt(getpublishTime.substring(10));
						timeFormat = (minute_number-pub_minute_number)+"分钟前";
					}else {
						timeFormat = (hour_number - pub_hour_number)+"小时前";
					}
				}else{
					if(date_number - pub_date_number<3){
						timeFormat = (date_number - pub_date_number)+"天前";
					}else{
						timeFormat = getpublishTime.substring(0, 4)
								  +"-"+getpublishTime.substring(4,6)
								  +"-"+getpublishTime.substring(6, 8);
					}
				}
			}else{
				timeFormat = getpublishTime.substring(0, 4)
						  +"-"+getpublishTime.substring(4,6)
						  +"-"+getpublishTime.substring(6, 8);
			}
		}else{
			timeFormat = getpublishTime.substring(0, 4)
					  +"-"+getpublishTime.substring(4,6)
					  +"-"+getpublishTime.substring(6, 8);
		}
		
		return timeFormat;
		
	}
	
	public void refreshlist(){ //在homepage里面引用到的，在重新回问界面时调用
		
		int now_size = newsItemList.size();
		news_cursor = db.query("User_news_table",null, 
				null, null, null,null, null);
		news_cursor.moveToLast();
		//Log.d("xxx", ""+news_cursor.getCount()+","+news_cursor.getPosition());
		if(news_cursor.getCount()>0){
			lastnews_id = news_cursor.getString(news_cursor.getColumnIndex("news_id"));
		}
		adapter.clear();
		
			for (int i = 0; i <  now_size; i++) {
				// 这些查询本应该在服务器上做的
				setList();
				
			}
		adapter.getclassname();
		adapter.notifyDataSetChanged();
	}



	
	
}
	
