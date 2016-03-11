package com.ifit.app.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifit.app.R;
import com.ifit.app.activity.Custom_plan;
import com.ifit.app.activity.Personal_center;
import com.ifit.app.activity.news_details;
import com.ifit.app.activity.report;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.custom_plan_in_item;
import com.ifit.app.other.newsItem;
import com.ifit.app.other.newspic_loadImg;
import com.ifit.app.other.newspic_loadImg.ImgCallback;

public class newsItemAdapter extends ArrayAdapter<newsItem> {

	private int resourceId;
	private final int Type_0=0,
					  Type_1=1,
					  Type_2=2,
					  Type_3=3,
					  Type_4=4;
	//private Bitmap bm1,bm2,bm3,bm4;
	//private InputStream Is = null;
	//private BitmapFactory.Options opts;
	
	ViewHolder_0 holder_0 = null;
	ViewHolder_1 holder_1 = null;
	ViewHolder_2 holder_2 = null;
	ViewHolder_3 holder_3 = null;
	ViewHolder_4 holder_4 = null;
	
	private newspic_loadImg loadimg = new newspic_loadImg();
	
	private static String getClassname;
	String compare_name = "class com.ifit.app.activity.Home_page";
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	String like_people;
	Cursor cursor;
	String[] like_name;
	//int likecount;
	//List list=null;
	//boolean islike=false;
	String UsingName;
	
	// 引入线程池，并引入内存缓存功能,并对外部调用封装了接口，简化调用过程
	private void pic_loading(String imgpath,int position,int number,final View convertView){//number为编号

		Bitmap cacheimg =loadimg.loadBitmap(imgpath, position, number,
				new ImgCallback() {
					
					@Override
					public void imageLoaded(Bitmap bm, int position, int number) {
						// TODO Auto-generated method stub
						setImg(bm,position,number,convertView);
					}
				});
		if(cacheimg != null){
			setImg(cacheimg,position,number,convertView);
		}
		
	}
	
	
	
	public void ClosePoll(){
		loadimg.ClosePoll();
	}
	
	
	//根据布局类型设置图片
	public void setImg(Bitmap bm,int position,int number,View convertView){
		
		int Type = getItemViewType(position);
		
		switch (Type) {
		case 1:
			 ((ViewHolder_1)convertView.getTag()).one_Onepic.setImageBitmap(bm);
			
			//Log.d("xxx", bm.toString());
			break;
		case 2:
			switch(number){
			case 1:
				((ViewHolder_2)convertView.getTag()).two_Onepic.setImageBitmap(bm);
				break;
			case 2:
				((ViewHolder_2)convertView.getTag()).two_Twopic.setImageBitmap(bm);
				break;
			}
			break;
		case 3:
			switch(number){
			case 1:
				((ViewHolder_3)convertView.getTag()).three_Onepic.setImageBitmap(bm);
				break;
			case 2:
				((ViewHolder_3)convertView.getTag()).three_Twopic.setImageBitmap(bm);
				break;
			case 3:
				((ViewHolder_3)convertView.getTag()).three_Threepic.setImageBitmap(bm);
				break;
			}
			//Log.d("xxx", "3");
			break;
		case 4:
			switch(number){
			case 1:
				((ViewHolder_4)convertView.getTag()).four_Onepic.setImageBitmap(bm);
				break;
			case 2:
				((ViewHolder_4)convertView.getTag()).four_Twopic.setImageBitmap(bm);
				break;
			case 3:
				((ViewHolder_4)convertView.getTag()).four_Threepic.setImageBitmap(bm);
				break;
			case 4:
				((ViewHolder_4)convertView.getTag()).four_Fourpic.setImageBitmap(bm);
				break;
			}
			break;
		}
	}
	
	public void getclassname(){
		getClassname = getContext().getClass().toString();
	}
	
	public newsItemAdapter(Context context,
			int textViewResourceId, List<newsItem> objects) {
		super(context,textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
		
		getclassname();
		//Log.d("xxx", getClassname);
		
		usedb = new MyDatabaseHelper(getContext(),"DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
	}

	public void getUseingName (String UsingName){
		this.UsingName = UsingName;
	}
	


	@Override
	public int getPosition(newsItem item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
		
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		//return super.getItemViewType(position);
		newsItem item = getItem(position);
		int piccount = item.getPicCount();

		
		if(piccount == 0){
			return Type_0;
		}else if(piccount == 1){
			return Type_1;
		}else if(piccount == 2){
			return Type_2;
		}else if(piccount == 3){
			return Type_3;
		}else{
			return Type_4;
		}
		
		
	}





	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 5;
	}








	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		
		//Log.d("xxx", "getview");
		newsItem item = getItem(position);
		
		//String news_id = item.getNews_id();
		
		/*
		cursor = db.query("User_news_good_table",
				null, "news_id = ?", new String[]{news_id},null, null, null);
		
		cursor.moveToFirst();
		
		if(cursor.getCount()>0){
		like_people = cursor.getString(cursor.getColumnIndex("goodname"));
		like_name = like_people.split(",");
		likecount = like_name.length;
		}else{
			like_people = null;
			like_name =null;
			likecount = 0;
		}
		
		//Log.d("xxx", like_people);
		
		
		if(list != null && !list.isEmpty()){
		list=null;
		}
		if(like_name!=null && like_name.length>0){
		list = Arrays.asList(like_name);
		islike = list.contains(UsingName);}else{
			islike =  false;
		}*/
		
		
		int Type = getItemViewType(position);
		
		//按当前所需的样式，确定new的布局 
		if(convertView == null){
			
			switch(Type){
			case Type_0:
				convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
				holder_0 = new ViewHolder_0();
				holder_0.user_headimg = (ImageView)convertView.findViewById(R.id.listView_headimg);
				holder_0.username = (TextView)convertView.findViewById(R.id.listView_username);
				holder_0.time =  (TextView)convertView.findViewById(R.id.listView_time);
				holder_0.newstext = (TextView)convertView.findViewById(R.id.listView_newstext);
				holder_0.like_count = (TextView)convertView.findViewById(R.id.user_news_like_count);
				holder_0.mark_count = (TextView)convertView.findViewById(R.id.user_news_comments_count);
				holder_0.user_news_like = (ImageView)convertView.findViewById(R.id.user_news_like);
				holder_0.user_news_comments = (ImageView)convertView.findViewById(R.id.user_news_comments);
				holder_0.user_news_morebtn = (ImageView)convertView.findViewById(R.id.user_news_morebtn);
				convertView.setTag(holder_0);
				break;
			case Type_1:
				convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
				holder_1 = new ViewHolder_1();
				holder_1.user_headimg = (ImageView)convertView.findViewById(R.id.listView_headimg);
				holder_1.username = (TextView)convertView.findViewById(R.id.listView_username);
				holder_1.time =  (TextView)convertView.findViewById(R.id.listView_time);
				holder_1.newstext = (TextView)convertView.findViewById(R.id.listView_newstext);
				holder_1.like_count = (TextView)convertView.findViewById(R.id.user_news_like_count);
				holder_1.mark_count = (TextView)convertView.findViewById(R.id.user_news_comments_count);
				holder_1.onePic_layout = (RelativeLayout)convertView.findViewById(R.id.user_news_onepic_layout);
				holder_1.one_Onepic= (ImageView)convertView.findViewById(R.id.user_news_onepic_1);
				holder_1.user_news_like = (ImageView)convertView.findViewById(R.id.user_news_like);
				holder_1.user_news_comments = (ImageView)convertView.findViewById(R.id.user_news_comments);
				holder_1.user_news_morebtn = (ImageView)convertView.findViewById(R.id.user_news_morebtn);
				convertView.setTag(holder_1);
				break;
			case Type_2:
				convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
				holder_2 = new ViewHolder_2();
				holder_2.user_headimg = (ImageView)convertView.findViewById(R.id.listView_headimg);
				holder_2.username = (TextView)convertView.findViewById(R.id.listView_username);
				holder_2.time =  (TextView)convertView.findViewById(R.id.listView_time);
				holder_2.newstext = (TextView)convertView.findViewById(R.id.listView_newstext);
				holder_2.like_count = (TextView)convertView.findViewById(R.id.user_news_like_count);
				holder_2.mark_count = (TextView)convertView.findViewById(R.id.user_news_comments_count);
				holder_2.twoPic_layout = (LinearLayout)convertView.findViewById(R.id.user_news_twopic_layout);
				holder_2.two_Onepic = (ImageView)convertView.findViewById(R.id.user_news_twopic_1);
				holder_2.two_Twopic = (ImageView)convertView.findViewById(R.id.user_news_twopic_2);
				holder_2.user_news_like = (ImageView)convertView.findViewById(R.id.user_news_like);
				holder_2.user_news_comments = (ImageView)convertView.findViewById(R.id.user_news_comments);
				holder_2.user_news_morebtn = (ImageView)convertView.findViewById(R.id.user_news_morebtn);
				convertView.setTag(holder_2);
				break;
			case Type_3:
				convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
				holder_3 = new ViewHolder_3();
				holder_3.user_headimg = (ImageView)convertView.findViewById(R.id.listView_headimg);
				holder_3.username = (TextView)convertView.findViewById(R.id.listView_username);
				holder_3.time =  (TextView)convertView.findViewById(R.id.listView_time);
				holder_3.newstext = (TextView)convertView.findViewById(R.id.listView_newstext);
				holder_3.like_count = (TextView)convertView.findViewById(R.id.user_news_like_count);
				holder_3.mark_count = (TextView)convertView.findViewById(R.id.user_news_comments_count);
				holder_3.threePic_layout = (LinearLayout)convertView.findViewById(R.id.user_news_threepic_layout);
				holder_3.three_Onepic = (ImageView)convertView.findViewById(R.id.user_news_threepic_1);
				holder_3.three_Twopic = (ImageView)convertView.findViewById(R.id.user_news_threepic_2);
				holder_3.three_Threepic = (ImageView)convertView.findViewById(R.id.user_news_threepic_3);
				holder_3.user_news_like = (ImageView)convertView.findViewById(R.id.user_news_like);
				holder_3.user_news_comments = (ImageView)convertView.findViewById(R.id.user_news_comments);
				holder_3.user_news_morebtn = (ImageView)convertView.findViewById(R.id.user_news_morebtn);
				convertView.setTag(holder_3);
				break;
			case Type_4:
				convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
				holder_4 = new ViewHolder_4();
				holder_4.user_headimg = (ImageView)convertView.findViewById(R.id.listView_headimg);
				holder_4.username = (TextView)convertView.findViewById(R.id.listView_username);
				holder_4.time =  (TextView)convertView.findViewById(R.id.listView_time);
				holder_4.newstext = (TextView)convertView.findViewById(R.id.listView_newstext);
				holder_4.like_count = (TextView)convertView.findViewById(R.id.user_news_like_count);
				holder_4.mark_count = (TextView)convertView.findViewById(R.id.user_news_comments_count);
				holder_4.fourPic_layout = (LinearLayout)convertView.findViewById(R.id.user_news_fourpic_layout);
				holder_4.four_Onepic = (ImageView)convertView.findViewById(R.id.user_news_fourpic_1);
				holder_4.four_Twopic = (ImageView)convertView.findViewById(R.id.user_news_fourpic_2);
				holder_4.four_Threepic = (ImageView)convertView.findViewById(R.id.user_news_fourpic_3);
				holder_4.four_Fourpic = (ImageView)convertView.findViewById(R.id.user_news_fourpic_4);
				holder_4.user_news_like = (ImageView)convertView.findViewById(R.id.user_news_like);
				holder_4.user_news_comments = (ImageView)convertView.findViewById(R.id.user_news_comments);
				holder_4.user_news_morebtn = (ImageView)convertView.findViewById(R.id.user_news_morebtn);
				convertView.setTag(holder_4);
				break;
			}
			
		}else{
			switch(Type){
			case Type_0:
				holder_0 = (ViewHolder_0)convertView.getTag();
				break;
			case Type_1:
				holder_1 = (ViewHolder_1)convertView.getTag();
				break;
			case Type_2:
				holder_2 = (ViewHolder_2)convertView.getTag();
				break;
			case Type_3:
				holder_3 = (ViewHolder_3)convertView.getTag();
				break;
			case Type_4:
				holder_4 = (ViewHolder_4)convertView.getTag();
				break;
			}
		}
		
		
		
		/*
		opts = new BitmapFactory.Options();
		opts.inTempStorage = new byte[100*1024];
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inSampleSize = 2;
		opts.inInputShareable = true;*/
		
		
		switch(Type){
		case Type_0:
			
			if(item.getText().length()>0){
				File gettext = new File(item.getText());
				if(gettext.exists()){
				String line=null;
				String str = "";
				try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(gettext), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine())!=null){
						str = str + line;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				holder_0.newstext.setText(str);
				}else{
					holder_0.newstext.setText("");
				}
				}else{
					holder_0.newstext.setText("");
				}
				
				
			holder_0.user_headimg.setImageDrawable(new CircleImageDrawable(item.getBitmap()));
			holder_0.username.setText(item.getUsername().toString());
			holder_0.time.setText(item.getPublishTime().toString());
			holder_0.like_count.setText(""+item.getlike_count());
			holder_0.mark_count.setText(""+item.getMark());
			//Log.d("xxx", "123"+item.getislike());
			if(item.getislike()){
				holder_0.user_news_like.setImageResource(R.drawable.islike);
				holder_0.user_news_like.setFocusable(true);
			}else{
				holder_0.user_news_like.setImageResource(R.drawable.unlike);
				holder_0.user_news_like.setFocusable(false);
			}
			setlisten(Type,position);
			break;
		case Type_1:
			
			if(item.getText().length()>0){
				File gettext = new File(item.getText());
				if(gettext.exists()){
				String line=null;
				String str = "";
				try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(gettext), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine())!=null){
						str = str + line;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
					holder_1.newstext.setText(str);
				}else{
					holder_1.newstext.setText("");
				}
				}else{
					holder_1.newstext.setText("");
				}
				
				
			holder_1.user_headimg.setImageDrawable(new CircleImageDrawable(item.getBitmap()));
			holder_1.username.setText(item.getUsername().toString());
			holder_1.time.setText(item.getPublishTime().toString());
			holder_1.like_count.setText(""+item.getlike_count());
			holder_1.mark_count.setText(""+item.getMark());
			
			holder_1.onePic_layout.setVisibility(View.VISIBLE);
			pic_loading(item.getImage_one_id(),position,1,convertView);
			/*
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Is = new FileInputStream(Item.getImage_one_id());
						bm1 = BitmapFactory.decodeStream(Is, null, opts);
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						//bm.recycle();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();*/
			//holder_1.one_Onepic.setImageBitmap(bm);
			if(item.getislike()){
				holder_1.user_news_like.setImageResource(R.drawable.islike);
				holder_1.user_news_like.setFocusable(true);
			}else{
				holder_1.user_news_like.setImageResource(R.drawable.unlike);
				holder_1.user_news_like.setFocusable(false);
			}
			setlisten(Type,position);
			break;
		case Type_2:
			
			if(item.getText().length()>0){
				File gettext = new File(item.getText());
				if(gettext.exists()){
				String line=null;
				String str = "";
				try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(gettext), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine())!=null){
						str = str + line;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				holder_2.newstext.setText(str);
				}else{
					holder_2.newstext.setText("");
				}
				}else{
					holder_2.newstext.setText("");
				}
				
				
			holder_2.user_headimg.setImageDrawable(new CircleImageDrawable(item.getBitmap()));
			holder_2.username.setText(item.getUsername().toString());
			holder_2.time.setText(item.getPublishTime().toString());
			holder_2.like_count.setText(""+item.getlike_count());
			holder_2.mark_count.setText(""+item.getMark());
			
			holder_2.twoPic_layout.setVisibility(View.VISIBLE);
			
			pic_loading(item.getImage_one_id(),position,1,convertView);
			pic_loading(item.getImage_two_id(),position,2,convertView);
			
			
			/*
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Is = new FileInputStream(Item.getImage_one_id());
						bm1 = BitmapFactory.decodeStream(Is,null,opts);
						
						Is = new FileInputStream(Item.getImage_two_id());
						bm2 = BitmapFactory.decodeStream(Is, null, opts);
						Message message = new Message();
						message.what = 2;
						handler.sendMessage(message);
						//bm.recycle();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			*/
			if(item.getislike()){
				holder_2.user_news_like.setImageResource(R.drawable.islike);
				holder_2.user_news_like.setFocusable(true);
			}else{
				holder_2.user_news_like.setImageResource(R.drawable.unlike);
				holder_2.user_news_like.setFocusable(false);
			}
			setlisten(Type,position);
			break;
		case Type_3:
			
			if(item.getText().length()>0){
				File gettext = new File(item.getText());
				if(gettext.exists()){
				String line=null;
				String str = "";
				try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(gettext), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine())!=null){
						str = str + line;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				holder_3.newstext.setText(str);
				}else{
					holder_3.newstext.setText("");
				}
				}else{
					holder_3.newstext.setText("");
				}
				
				
			holder_3.user_headimg.setImageDrawable(new CircleImageDrawable(item.getBitmap()));
			holder_3.username.setText(item.getUsername().toString());
			holder_3.time.setText(item.getPublishTime().toString());
			holder_3.like_count.setText(""+item.getlike_count());
			holder_3.mark_count.setText(""+item.getMark());
			
			holder_3.threePic_layout.setVisibility(View.VISIBLE);
			
			pic_loading(item.getImage_one_id(),position,1,convertView);
			pic_loading(item.getImage_two_id(),position,2,convertView);
			pic_loading(item.getImage_three_id(),position,3,convertView);
			
			/*
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Is = new FileInputStream(Item.getImage_one_id());
						bm1 = BitmapFactory.decodeStream(Is,null,opts);
						
						Is = new FileInputStream(Item.getImage_two_id());
						bm2 = BitmapFactory.decodeStream(Is, null, opts);
						
						Is = new FileInputStream(Item.getImage_three_id());
						bm3 = BitmapFactory.decodeStream(Is, null, opts);
						
						Message message = new Message();
						message.what = 3;
						handler.sendMessage(message);
						//bm.recycle();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			*/
			if(item.getislike()){
				holder_3.user_news_like.setImageResource(R.drawable.islike);
				holder_3.user_news_like.setFocusable(true);
			}else{
				holder_3.user_news_like.setImageResource(R.drawable.unlike);
				holder_3.user_news_like.setFocusable(false);
			}
			setlisten(Type,position);
			break;
		case Type_4:
			
			if(item.getText().length()>0){
				File gettext = new File(item.getText());
				if(gettext.exists()){
				String line=null;
				String str = "";
				try {
					InputStreamReader isr = new InputStreamReader(new FileInputStream(gettext), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine())!=null){
						str = str + line;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
					holder_4.newstext.setText(str);
				}else{
					holder_4.newstext.setText("");
				}
				}else{
					holder_4.newstext.setText("");
				}
				
				
			holder_4.user_headimg.setImageDrawable(new CircleImageDrawable(item.getBitmap()));
			holder_4.username.setText(item.getUsername().toString());
			holder_4.time.setText(item.getPublishTime().toString());
			holder_4.like_count.setText(""+item.getlike_count());
			holder_4.mark_count.setText(""+item.getMark());
			
			holder_4.fourPic_layout.setVisibility(View.VISIBLE);
			
			pic_loading(item.getImage_one_id(),position,1,convertView);
			pic_loading(item.getImage_two_id(),position,2,convertView);
			pic_loading(item.getImage_three_id(),position,3,convertView);
			pic_loading(item.getImage_four_id(),position,4,convertView);
			
			/*
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Is = new FileInputStream(Item.getImage_one_id());
						bm1 = BitmapFactory.decodeStream(Is,null,opts);
						
						Is = new FileInputStream(Item.getImage_two_id());
						bm2 = BitmapFactory.decodeStream(Is, null, opts);
						
						Is = new FileInputStream(Item.getImage_three_id());
						bm3 = BitmapFactory.decodeStream(Is, null, opts);
						
						Is = new FileInputStream(Item.getImage_four_id());
						bm4 = BitmapFactory.decodeStream(Is, null, opts);
						
						Message message = new Message();
						message.what = 4;
						handler.sendMessage(message);
						//bm.recycle();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			*/
			if(item.getislike()){
				holder_4.user_news_like.setImageResource(R.drawable.islike);
				holder_4.user_news_like.setFocusable(true);
			}else{
				holder_4.user_news_like.setImageResource(R.drawable.unlike);
				holder_4.user_news_like.setFocusable(false);
			}
			setlisten(Type,position);
			break;
		}
		return convertView;
	}

	
	
	//所有的holder都是用来缓存的，避免重复做同样的事
	
	class ViewHolder_0{
		ImageView user_headimg;
		ImageView user_news_like;
		ImageView user_news_comments;
		ImageView user_news_morebtn;
		TextView username;
		TextView time;
		TextView newstext;
		TextView like_count;
		TextView mark_count;
	}
	
	class ViewHolder_1{
		
		ImageView user_headimg;
		ImageView user_news_like;
		ImageView user_news_comments;
		ImageView user_news_morebtn;
		TextView username;
		TextView time;
		TextView newstext;
		TextView like_count;
		TextView mark_count;
		
		RelativeLayout onePic_layout;
		ImageView one_Onepic;
		
	}
	
	class ViewHolder_2{
		ImageView user_headimg;
		ImageView user_news_like;
		ImageView user_news_comments;
		ImageView user_news_morebtn;
		TextView username;
		TextView time;
		TextView newstext;
		TextView like_count;
		TextView mark_count;
		
		LinearLayout twoPic_layout;
		ImageView two_Onepic;
		ImageView two_Twopic;
	}
	
	class ViewHolder_3{
		ImageView user_headimg;
		ImageView user_news_like;
		ImageView user_news_comments;
		ImageView user_news_morebtn;
		TextView username;
		TextView time;
		TextView newstext;
		TextView like_count;
		TextView mark_count;
		
		LinearLayout threePic_layout;
		ImageView three_Onepic;
		ImageView three_Twopic;
		ImageView three_Threepic;
	}
	
	class ViewHolder_4{
		ImageView user_headimg;
		ImageView user_news_like;
		ImageView user_news_comments;
		ImageView user_news_morebtn;
		TextView username;
		TextView time;
		TextView newstext;
		TextView like_count;
		TextView mark_count;
		
		LinearLayout fourPic_layout;
		ImageView four_Onepic;
		ImageView four_Twopic;
		ImageView four_Threepic;
		ImageView four_Fourpic;
	}
	
	
	public void setlisten(int Type,int position){
		
		
		
		
		switch(Type){
		case Type_0:
			if(getClassname.equals(compare_name)){
				holder_0.user_headimg.setOnClickListener(new MyOnClickListen(position));
			}
			holder_0.newstext.setOnClickListener(new MyOnClickListen(position));
			holder_0.user_news_like.setOnClickListener(new MyOnClickListen(position));
			holder_0.user_news_comments.setOnClickListener(new MyOnClickListen(position));
			holder_0.user_news_morebtn.setOnClickListener(new MyOnClickListen(position));
			
			break;
		case Type_1:
			if(getClassname.equals(compare_name)){
				holder_1.user_headimg.setOnClickListener(new MyOnClickListen(position));
			}
			holder_1.newstext.setOnClickListener(new MyOnClickListen(position));
			holder_1.user_news_like.setOnClickListener(new MyOnClickListen(position));
			holder_1.user_news_comments.setOnClickListener(new MyOnClickListen(position));
			holder_1.user_news_morebtn.setOnClickListener(new MyOnClickListen(position));
			holder_1.one_Onepic.setOnClickListener(new MyOnClickListen(position));
			
			break;
		case Type_2:
			if(getClassname.equals(compare_name)){
				holder_2.user_headimg.setOnClickListener(new MyOnClickListen(position));
			}
			holder_2.newstext.setOnClickListener(new MyOnClickListen(position));
			holder_2.user_news_like.setOnClickListener(new MyOnClickListen(position));
			holder_2.user_news_comments.setOnClickListener(new MyOnClickListen(position));
			holder_2.user_news_morebtn.setOnClickListener(new MyOnClickListen(position));
			holder_2.two_Onepic.setOnClickListener(new MyOnClickListen(position));
			holder_2.two_Twopic.setOnClickListener(new MyOnClickListen(position));
			
			break;
		case Type_3:
			if(getClassname.equals(compare_name)){
				holder_3.user_headimg.setOnClickListener(new MyOnClickListen(position));
			}
			holder_3.newstext.setOnClickListener(new MyOnClickListen(position));
			holder_3.user_news_like.setOnClickListener(new MyOnClickListen(position));
			holder_3.user_news_comments.setOnClickListener(new MyOnClickListen(position));
			holder_3.user_news_morebtn.setOnClickListener(new MyOnClickListen(position));
			holder_3.three_Onepic.setOnClickListener(new MyOnClickListen(position));
			holder_3.three_Twopic.setOnClickListener(new MyOnClickListen(position));
			holder_3.three_Threepic.setOnClickListener(new MyOnClickListen(position));
			break;
		case Type_4:
			if(getClassname.equals(compare_name)){
				holder_4.user_headimg.setOnClickListener(new MyOnClickListen(position));
			}
			holder_4.newstext.setOnClickListener(new MyOnClickListen(position));
			holder_4.user_news_like.setOnClickListener(new MyOnClickListen(position));
			holder_4.user_news_comments.setOnClickListener(new MyOnClickListen(position));
			holder_4.user_news_morebtn.setOnClickListener(new MyOnClickListen(position));
			holder_4.four_Onepic.setOnClickListener(new MyOnClickListen(position));
			holder_4.four_Twopic.setOnClickListener(new MyOnClickListen(position));
			holder_4.four_Threepic.setOnClickListener(new MyOnClickListen(position));
			holder_4.four_Fourpic.setOnClickListener(new MyOnClickListen(position));
			break;
		}
		
	}

	
	class MyOnClickListen implements OnClickListener{

		int position;
		
		public MyOnClickListen(int position){
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				
				switch(v.getId()){
			 		case R.id.listView_headimg:
			 			break;
			 		case R.id.listView_newstext:
			 			Intent turn_news_details = new Intent(getContext(),news_details.class);
			 			turn_news_details.putExtra("news_id", getItem(position).getNews_id());
			 			getContext().startActivity(turn_news_details);
			 			
			 			break;
			 		case R.id.user_news_like:
			 			
			 			setlike(position,v);
			 			
			 			break;
			 		case R.id.user_news_comments:
			 			
			 			//Log.d("xxx", "pic1");
			 			
			 			Intent turn_details = new Intent(getContext(),news_details.class);
			 			turn_details.putExtra("news_id", getItem(position).getNews_id());
			 			getContext().startActivity(turn_details);
			 			
			 			break;
			 		case R.id.user_news_morebtn:
			 			
			 			
			 			if(getClassname.equals(compare_name)){
			 				//举报
			 				popushow_report(v,position);
			 			}else{
			 				//删除
			 				popushow(v,position);
			 			}
			 			break;
			 		case R.id.user_news_onepic_1:
			 			break;
			 		case R.id.user_news_twopic_1:
			 			break;
			 		case R.id.user_news_twopic_2:
			 			break;
			 		case R.id.user_news_threepic_1:
			 			break;
			 		case R.id.user_news_threepic_2:
			 			break;
			 		case R.id.user_news_threepic_3:
			 			break;
			 		case R.id.user_news_fourpic_1:
			 			Log.d("xxx", "pic1");
			 			break;
			 		case R.id.user_news_fourpic_2:
			 			Log.d("xxx", "pic2");
			 			break;
			 		case R.id.user_news_fourpic_3:
			 			Log.d("xxx", "pic3");
			 			break;
			 		case R.id.user_news_fourpic_4:
			 			Log.d("xxx", "pic4");
			 			break;
				}
		}
	}
	
	
	public void setlike(int position,View v){

		newsItem setlike_item = getItem(position);
		
		String news_id = setlike_item.getNews_id();

 		cursor = db.query("User_news_good_table",
				null, "news_id = ?", new String[]{news_id},null, null, null);
		
		cursor.moveToFirst();
		
		int cursor_getCount = cursor.getCount();
		
		if(cursor_getCount>0){
			like_people = cursor.getString(cursor.getColumnIndex("goodname"));
			like_name = like_people.split(",");
			}else{
				like_name = null;
			}
			
		
			TextView like_count_textview = ((TextView)((View)(v.getParent())).findViewById(R.id.user_news_like_count));
			
			//int like_count=Integer.parseInt(like_count_textview.getText().toString());
			
			if(((ImageView)v).isFocusable()){
				((ImageView)v).setImageResource(R.drawable.unlike);
				((ImageView)v).setFocusable(false);
				setlike_item.islike = false;
				setlike_item.good = setlike_item.getlike_count()-1;
				like_count_textview.setText(""+setlike_item.getlike_count());
	 			
	 			
	 			deletelike(like_name,news_id);
	 			
	 			
	 			
	 		}else{
	 			((ImageView)v).setImageResource(R.drawable.islike);
	 			((ImageView)v).setFocusable(true);
	 			setlike_item.islike = true;
	 			setlike_item.good = setlike_item.getlike_count()+1;
	 			like_count_textview.setText(""+setlike_item.getlike_count());
	 			
	 			addlike(like_name,news_id,cursor_getCount);
	 			
	 		}

	}
	
	public void deletelike(String[] likestr,String news_id){
		if(likestr.length==1){
				db.delete("User_news_good_table", "news_id = ?", 
						new String[]{news_id});
			}else{
				
				
				
				
				String str = "";
				for(int i=0;i<likestr.length;i++){
					
					if(!likestr[i].equals(UsingName)){
					str = str+likestr[i]+",";
					}
					
				}
				
				ContentValues values = new ContentValues();
				
				values.put("goodname", str);
				
				db.update("User_news_good_table", values, 
						"news_id = ?", new String[]{news_id});
				values.clear();
			}
	}
	
	
	public void addlike(String[] likestr,String news_id,int cursor_getCount){
		
		ContentValues values = new ContentValues();
			
			if(cursor_getCount==0){
				
 			values.put("news_id", news_id);
 			values.put("goodname", UsingName+",");
 			db.insert("User_news_good_table", null, values);
 			values.clear();
			}else{
				String str = "";
				for(int i=0;i<likestr.length;i++){
					str = str + likestr[i]+",";
				}
				str = str + UsingName +",";
				values.put("goodname", str);
				db.update("User_news_good_table", values, 
						"news_id = ?", new String[]{news_id});
				values.clear();
			}
		
	}
	
	public void popushow_report(View v,final int position){
		View contentView = LayoutInflater.from(getContext()).
				inflate(R.layout.report_popu_menu, null);
		RelativeLayout btn_delete = (RelativeLayout)contentView.findViewById(R.id.report_popu_layout);
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		btn_delete.measure(w, h);
		int width = btn_delete.getMeasuredWidth();
		int height = btn_delete.getMeasuredHeight();
		final PopupWindow popu = new PopupWindow(contentView,
				width,height,true);
		btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getContext(), "show", 
						//Toast.LENGTH_SHORT).show();
				newsItem item = getItem(position);
				String id = item.getNews_id();
				Intent turn_report = new Intent(getContext(),report.class);
				turn_report.putExtra("news_id", id);
				getContext().startActivity(turn_report);
				popu.dismiss();
			}
		});
		
		
		
		popu.setTouchable(true);
		
		popu.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				
				return false;
				
				//这里如果返回true,touch事件将被拦截
				//拦截后popu的ontouchevent不被调用，这样点击外部区域无法dismis
			}
		});
		
		//如果不设置popu的背景，无论是点击外部区域还是back键都步法dissmiss弹窗
		popu.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.completely_transparent));

		popu.showAsDropDown(v,-100, -50);
				
	}
	
	
	public void popushow(View v,final int position){
		View contentView = LayoutInflater.from(getContext()).
				inflate(R.layout.delete_popu_menu, null);
		RelativeLayout btn_delete = (RelativeLayout)contentView.findViewById(R.id.delete_popu_layout);
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		btn_delete.measure(w, h);
		int width = btn_delete.getMeasuredWidth();
		int height = btn_delete.getMeasuredHeight();
		final PopupWindow popu = new PopupWindow(contentView,
				width,height,true);
		
		btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getContext(), "show", 
						//Toast.LENGTH_SHORT).show();
				showdiolog(position);
				popu.dismiss();
				
			}
		});
		
		
		
		popu.setTouchable(true);
		
		popu.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				
				return false;
				
				//这里如果返回true,touch事件将被拦截
				//拦截后popu的ontouchevent不被调用，这样点击外部区域无法dismis
			}
		});
		
		//如果不设置popu的背景，无论是点击外部区域还是back键都步法dissmiss弹窗
		popu.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.completely_transparent));

		popu.showAsDropDown(v,-100, -50);
		//popu.showAsDropDown(contentView);
		//popu.showAtLocation(v, Gravity.RIGHT,100, 110);
	}
	
	public void showdiolog(final int position){
		new AlertDialog.Builder(getContext()).setMessage("确认删除此条动态?")
		.setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				newsItem item = getItem(position);
				
				String id = item.getNews_id();
				
				int piccount = item.getPicCount();
				String path = null;
				if(piccount ==0){
					path = item.getText();
				}else{
					path = item.getImage_one_id();
				}
				
				if(path != null ){
					if(!path.equals("")){
				File file = new File (path);
				File parent =file.getParentFile();
				for(File i : parent.listFiles()){
					if(i.isFile()){
						i.delete();
					}
				}
				parent.delete();
					}
				}
				db.delete("User_news_table", "news_id = ?", new String[]{id});
				db.delete("User_news_good_table", "news_id = ?", new String[]{id});
				db.delete("User_news_mark_table", "news_id = ?", new String[]{id});
				//用户评论还没写完，先不需要删
	 			Personal_center activity = (Personal_center)getContext();
				activity.newsItemList.remove(item);
				
				notifyDataSetChanged();
				
				Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT);
				
			}
		}).setPositiveButton("否", null).show();
	}
	/*
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				holder_1.one_Onepic.setImageBitmap(bm1);
				break;
			case 2:
				holder_2.two_Onepic.setImageBitmap(bm1);
				holder_2.two_Twopic.setImageBitmap(bm2);
				break;
			case 3:
				holder_3.three_Onepic.setImageBitmap(bm1);
				holder_3.three_Twopic.setImageBitmap(bm2);
				holder_3.three_Threepic.setImageBitmap(bm3);
				break;
			case 4:
				holder_4.four_Onepic.setImageBitmap(bm1);
				holder_4.four_Twopic.setImageBitmap(bm2);
				holder_4.four_Threepic.setImageBitmap(bm3);
				holder_4.four_Fourpic.setImageBitmap(bm4);
				break;
			}
			super.handleMessage(msg);
		}
	};
	*/
}