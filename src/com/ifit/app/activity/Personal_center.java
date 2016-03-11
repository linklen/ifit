package com.ifit.app.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ifit.app.R;
import com.ifit.app.adapter.newsItemAdapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.newsItem;
import com.ifit.app.other.Rainbow_Bar_SwipeRefreshAndLoad.SwipeRefreshLayout;
import com.ifit.app.other.Rainbow_Bar_SwipeRefreshAndLoad.SwipeRefreshLayout.OnLoadListener;
import com.ifit.app.other.Rainbow_Bar_SwipeRefreshAndLoad.SwipeRefreshLayout.OnRefreshListener;

public class Personal_center extends Activity implements OnRefreshListener,OnLoadListener{

	private SwipeRefreshLayout mSwipeLayout;
	private ListView mListView;
	//private ArrayAdapter<String> mListAdapter;
	private newsItemAdapter adapter;
	private LayoutInflater layoutInflater;
	private ImageView btn_back,turn_setting,user_headImage,show_headimg;
	private Bitmap mbitmap;
	private AlertDialog show_headimg_dialog;
	private View show_headimg_View , listView_head;
	private TextView user_nickname,user_age,user_region,news_count,user_spottime;
	//private int headimg_change,nickname_change,age_change;
	
	public File sdCard,directory,location_image;
	public SharedPreferences locationdata,getUsername;//,data_change_mark;
	
	
	public List<newsItem> newsItemList = new ArrayList<newsItem>();
	public MyDatabaseHelper usedb;
	public SQLiteDatabase db;
	
	private Cursor news_cursor,headimg_cursor;
	private String username;
	private String nickname;
	String[] like_name = null;
	int good_count;
	boolean islike;
	List list = null;
	String like_people = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_center_listview);
		
		
		layoutInflater = getLayoutInflater();
		
		show_headimg_View = layoutInflater.inflate(R.layout.show_headimg, null);
		
		show_headimg = (ImageView)show_headimg_View.findViewById(R.id.show_headimg);
		
		//显示动态总量
		//点击头像显示头像图
		show_headimg_dialog = new AlertDialog.Builder(Personal_center.this).create();
		
		show_headimg_dialog.setView(show_headimg_View);
		
		//show_headimg_dialog.create();
		
		
		mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.personal_center_swipe_container);
		mListView = (ListView)findViewById(R.id.personal_center_listview);
		listView_head = (LinearLayout)layoutInflater.inflate(R.layout.personal_center_listview_header, null);
		btn_back = (ImageView)findViewById(R.id.personal_center_exit);
		turn_setting = (ImageView)findViewById(R.id.personal_center_turn_setting);
		user_headImage = (ImageView)listView_head.findViewById(R.id.user_pic);
		user_nickname = (TextView)listView_head.findViewById(R.id.user_nickname);
		user_age = (TextView)listView_head.findViewById(R.id.user_age);
		user_region = (TextView)listView_head.findViewById(R.id.user_region);
		news_count = (TextView)listView_head.findViewById(R.id.user_number_personal_dynamic);
		user_spottime = (TextView)listView_head.findViewById(R.id.user_count_sport_time);
		//数据到175行，public void set_sportTime()函数里面获取并设置
		
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/UserHeadImage/");
		if(!directory.exists()){
		directory.mkdirs();}
		location_image = new File(directory,"location_image.jpg");
		
		locationdata = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		getUsername = getSharedPreferences("islogin", MODE_PRIVATE);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null,1);
		db = usedb.getWritableDatabase();
		//data_change_mark = getSharedPreferences("data_change_mark", MODE_PRIVATE);
		//用来判断是否进行过更新
		/*已经写在onstart里面
		if(location_image.exists()){
			//查看是否有头像，或者本地是否有，有就设置
			mbitmap = BitmapFactory
					.decodeFile(location_image.toString());
		}else{
			mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
		}
		user_headImage.setImageDrawable(new CircleImageDrawable(mbitmap));
		*/

		//set_data();
		
		btn_back.setOnClickListener(new MyOnClickListener());
		turn_setting.setOnClickListener(new MyOnClickListener());
		user_headImage.setOnClickListener(new MyOnClickListener());
		
		
		initList();
		set_sportTime();
		//mListAdapter = new ArrayAdapter<String>(this,
		//				android.R.layout.simple_expandable_list_item_1,values);
		
		adapter = new newsItemAdapter(this, R.layout.user_news_item, newsItemList);
		//mListView.setAdapter(mListAdapter);
		adapter.getUseingName(username);
		mListView.addHeaderView(listView_head);
		mListView.setAdapter(adapter);
		
		
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setOnLoadListener(this);
		mSwipeLayout.setColor(android.R.color.holo_blue_bright,
                			  android.R.color.holo_green_light,
                			  android.R.color.holo_orange_light,
                			  android.R.color.holo_red_light);
		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
		mSwipeLayout.setLoadNoFull(false);
		
		
	}
	
	/*ArrayList<String> values = new ArrayList<String>(){{
		add("value1");
		add("value2");
		add("value3");
		add("value4");
		add("value5");
		add("value6");
		add("value7");
	}};
	*/
	
	public void set_sportTime(){
		Cursor cr = db.query("User_order_table", 
				null, "name = ?", new String[]{Home_page.UsingName}, null, null, null);
		if(cr.moveToFirst()){
			
			db.beginTransaction();
			
			String mondata = cr.getString(cr.getColumnIndex("mondata"));
			if(mondata==null){
				mondata = "0,0";
			}
			String montime = mondata.split(",")[0];
			String tuedata = cr.getString(cr.getColumnIndex("tuedata"));
			if(tuedata==null){
				tuedata = "0,0";
			}
			String tuetime = tuedata.split(",")[0];
			String weddata = cr.getString(cr.getColumnIndex("weddata"));
			if(weddata==null){
				weddata = "0,0";
			}
			String wedtime = weddata.split(",")[0];
			String thurdata = cr.getString(cr.getColumnIndex("thurdata"));
			if(thurdata==null){
				thurdata = "0,0";
			}
			String thurtime = thurdata.split(",")[0];
			String fridata = cr.getString(cr.getColumnIndex("fridata"));
			if(fridata==null){
				fridata = "0,0";
			}
			String fritime = fridata.split(",")[0];
			String satdata = cr.getString(cr.getColumnIndex("satdata"));
			if(satdata==null){
				satdata = "0,0";
			}
			String sattime = satdata.split(",")[0];
			String sundata = cr.getString(cr.getColumnIndex("sundata"));
			if(sundata==null){
				sundata = "0,0";
			}
			String suntime = sundata.split(",")[0];
			
			db.setTransactionSuccessful();
			db.endTransaction();
			
			int temptime = Integer.parseInt(montime)+Integer.parseInt(tuetime)+
					Integer.parseInt(wedtime)+Integer.parseInt(thurtime)+
					Integer.parseInt(fritime)+Integer.parseInt(sattime)+
					Integer.parseInt(suntime);
			
			int sporttime = (int) Math.floor(temptime/60);
			user_spottime.setText(sporttime+"");
		}
	}
	
	public void initList(){
		
		//获取用户昵称
		username = getUsername.getString("user", "");
		nickname = locationdata.getString("nickname", "");
		
		
		//获取用户头像
		/*if(location_image.exists()){
			//查看是否有头像，或者本地是否有，有就设置
			mbitmap = BitmapFactory
					.decodeFile(location_image.toString());
			//out_bitmap = mbitmap;
		}else{
			mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
			//out_bitmap = mbitmap;
		}*/
		
		//获取头像
		
		
		headimg_cursor = db.query("User_headImage_table", null, "name = ?",
				new String[] { username }, null, null, null);
		headimg_cursor.moveToFirst();

		if (headimg_cursor.getBlob(headimg_cursor
				.getColumnIndex("user_head_img")) != null) {
			byte[] get_Headimg = headimg_cursor.getBlob(headimg_cursor
					.getColumnIndex("user_head_img"));
			mbitmap = BitmapFactory.decodeByteArray(get_Headimg, 0,
					get_Headimg.length);
		} else {
			mbitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_headimage);
		}
		
		
		
		news_cursor = db.query("User_news_table",
				null,"name = ?", new String[]{username}, null, null, null);
		
		
		String nuwscount = ""+news_cursor.getCount();
		news_count.setText(nuwscount);
		
		news_cursor.moveToLast();
		//Log.d("xxx", ""+news_cursor.getCount());
		//setList(username,news_cursor,good_cursor,mark_cursor);
		if(news_cursor.getCount()>=5){
			
			for(int i=0;i<5;i++){
			//这些查询本应该在服务器上做的
				setList();
				
			}
			
		}else{
			for(int i=0;i<news_cursor.getCount();i++){
				setList();
			}
		}
		

	}
	
	public void setList(){
		
		
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
		
		String time = time_no_format.substring(0, 4)
					  +"-"+time_no_format.substring(4,6)
					  +"-"+time_no_format.substring(6, 8);
		//Log.d("xxx", time_format);
		String news_text = news_cursor.getString(news_cursor.getColumnIndex("words"));
		if(news_text == null){
			news_text = "";
		}
		
		
		Cursor good_cursor = db.query("User_news_good_table", 
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
			islike = list.contains(username);
			}else{
				islike =  false;
			}
		
			//Log.d("xxx", ""+islike);
		//int good_count = good_cursor.getCount();
		
		//Log.d("xxx", good_count+"123");
		
		Cursor mark_cursor = db.query("User_news_mark_table", 
				null, "news_id = ?", new String[]{getnews_id}, null, null, null);
		int mark_count = mark_cursor.getCount();
		
		//Log.d("xxx", mark_count+"321");
		
		
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
	
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
		//Log.d("xxx", newsItemList.size()+"");
		//Log.d("xxx", news_cursor.getCount()+"");
		int rest_news_count = news_cursor.getCount() - newsItemList.size();
		
		if(rest_news_count <= 0){
			Toast.makeText(Personal_center.this, "已经全部加载完毕！", Toast.LENGTH_SHORT).show();
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
		}
		
		//Log.d("xxx", newsItemList.size()+"");
		
		// values.add("Add " + values.size());
	        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                mSwipeLayout.setLoading(false);
	                adapter.notifyDataSetChanged();
	            }
	        }, 1000);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		//values.add(0, "Add " + values.size());
		
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	
            	newsItemList.clear();
        		
        		adapter.clear();
        		
        		initList();
            	
            	Toast.makeText(Personal_center.this, "刷新成功", Toast.LENGTH_SHORT).show();
                mSwipeLayout.setRefreshing(false);
                //mListAdapter.notifyDataSetChanged();
            }
        }, 2000);
		
	}


	
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.personal_center_exit:
				finish();
				break;
			case R.id.personal_center_turn_setting:
				Intent turn_setting = new Intent(Personal_center.this,Setting.class);
				startActivity(turn_setting);
				break;
			case R.id.user_pic:
				
				
				if(location_image.exists()){
					//查看是否有头像，或者本地是否有，有就设置
					mbitmap = BitmapFactory
							.decodeFile(location_image.toString());
				}else{
					mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
				}
				//int height = mbitmap.getHeight();
				int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
				//int width = mbitmap.getWidth();
				show_headimg.setImageBitmap(mbitmap);
				show_headimg_dialog.show();
				WindowManager.LayoutParams params = show_headimg_dialog.getWindow().getAttributes();
				params.width = screenWidth;
				//Log.d("xxx", ""+height+","+width);
				
				params.height = screenWidth-32;
				
				//params.width = 600;
				//params.height = 600-32;
				show_headimg_dialog.getWindow().setAttributes(params);
				break;
			}
		}
		
	}
	
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//headimg_change = data_change_mark.getInt("headimg_change", 1);
		//nickname_change = data_change_mark.getInt("nickname_change", 1);
		//age_change = data_change_mark.getInt("age_change", 1);
		
		set_data();
	}

	


	public void set_data(){
		
		
		//设置头像
		if(location_image.exists()){
			//查看是否有头像，或者本地是否有，有就设置
			mbitmap = BitmapFactory
					.decodeFile(location_image.toString());
		}else{
			mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
		}
		user_headImage.setImageDrawable(new CircleImageDrawable(mbitmap));
		
		
		//设置年龄和昵称
		nickname = locationdata.getString("nickname", "");
		int age = locationdata.getInt("age", 0);
		String district = locationdata.getString("district", "");
		if(!nickname.equals("")){
			user_nickname.setText(nickname);
		}else{
			user_nickname.setText("");
		}
		if(age != 0){
			user_age.setText(""+age+"岁");
		}else{
			user_age.setText("");
		}
		if(!district.equals("")){
			user_region.setText(district);
		}else{
			user_region.setText("火星");
		}
		
		
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		mbitmap.recycle();
		overridePendingTransition(R.anim.slide_uptoin, R.anim.slide_intodown);
		db.close();
		news_cursor.close();
		adapter.ClosePoll();
	}



}
