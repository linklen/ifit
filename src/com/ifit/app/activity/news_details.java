package com.ifit.app.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.comment_create_view;
import com.ifit.app.other.learnpic_loadImg;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

public class news_details extends Activity {

	private String news_id;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	
	String[] like_name = null;
	int good_count;
	boolean islike;
	List list = null;
	String like_people = null;
	private String loginname = null;
	private String nickname;
	private String time;
	private int mark_count;
	private String news_text_path;
	
	
	private ImageView btn_back;
	private ImageView user_headimg;
	private TextView nickname_text;
	private TextView time_text;
	private TextView like_name_display;
	private TextView news_text;
	private ImageView islike_view;
	private TextView like_count;
	private TextView comment_count;
	private LinearLayout add_view_layout;
	
	public EditText edit_comment;
	private Button btn_send;
	
	private learnpic_loadImg loadimg = new learnpic_loadImg();
	private comment_create_view create_view = new comment_create_view();
	private void pic_loading(String imgpath,final ImageView Image){
		
		Bitmap cacheimg =loadimg.loadBitmap(imgpath,
				new ImgCallback() {
					
					@Override
					public void imageLoaded(Bitmap bm) {
						// TODO Auto-generated method stub
						Image.setImageBitmap(bm);
					}
				});
		if(cacheimg != null){
			Image.setImageBitmap(cacheimg);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_details_layout);
		
		usedb = new MyDatabaseHelper(this,"DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		Intent getdata = getIntent();
		news_id = getdata.getStringExtra("news_id");
		
		init_nomalContral();
		get_db_data();
		
		set_normal_data();
		
		add_comment_view();
		
	}
	
	public void init_nomalContral(){
		btn_back = (ImageView)findViewById(R.id.news_detail_title_btn_back);
		user_headimg = (ImageView)findViewById(R.id.listView_headimg);
		nickname_text = (TextView)findViewById(R.id.listView_username);
		time_text = (TextView)findViewById(R.id.listView_time);
		islike_view = (ImageView)findViewById(R.id.user_news_like);
		like_count = (TextView)findViewById(R.id.user_news_like_count);
		comment_count = (TextView)findViewById(R.id.user_news_comments_count);
		news_text = (TextView)findViewById(R.id.listView_newstext);
		like_name_display = (TextView)findViewById(R.id.like_name_display);
		edit_comment = (EditText)findViewById(R.id.edit_comment);
		btn_send = (Button)findViewById(R.id.btn_send);
		add_view_layout = (LinearLayout)findViewById(R.id.news_detail_add_view);
	}
	
	
	public void getlike(){
		Cursor good_cursor = db.query("User_news_good_table", 
				null, "news_id = ?", new String[]{news_id}, null, null, null);
		
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
		
			if(like_name!=null && like_name.length>0){
			list = Arrays.asList(like_name);
			islike = list.contains(Home_page.UsingName);
			}
			
			good_cursor.close();
			
			
	}
	
	
	public void get_db_data(){
		
		Cursor news_cursor = db.query("User_news_table", null,
				"news_id = ?",new String[]{news_id}, 
				null, null, null);
		
		news_cursor.moveToFirst();
		
		loginname = news_cursor.getString(news_cursor.getColumnIndex("name"));
		//Log.d("xxx", loginname);
		
		Cursor nickname_cursor = db.query("User_personal_info_table", 
				null, "name = ?", new String[]{loginname}, null, null, null);
		
		nickname_cursor.moveToFirst();
		
		String getnickname = nickname_cursor.getString(nickname_cursor.getColumnIndex("nickname"));
		
		if(getnickname == null){
			nickname = "未设置用户名";
		}else{
			nickname = getnickname;
		}
		
		
		
		String img1 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_1"));
		String img2 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_2"));
		String img3 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_3"));
		String img4 = news_cursor.getString(news_cursor.getColumnIndex("imagepath_4"));
		
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
		
		time = time_no_format.substring(0, 4)
					  +"-"+time_no_format.substring(4,6)
					  +"-"+time_no_format.substring(6, 8);
		//Log.d("xxx", time_format);
		news_text_path = news_cursor.getString(news_cursor.getColumnIndex("words"));
		if(news_text_path == null){
			news_text_path = "";
		}
		
		
		getlike();
		
			//Log.d("xxx", ""+islike);
		//int good_count = good_cursor.getCount();
		
		//Log.d("xxx", good_count+"123");
		
		Cursor mark_cursor = db.query("User_news_mark_table", 
				null, "news_id = ?", new String[]{news_id}, null, null, null);
		mark_count = mark_cursor.getCount();
		
		switch(picnumber){
		case 1:
			set_one_img(img1);
			break;
		case 2:
			set_two_img(img1,img2);
			break;
		case 3:
			set_three_img(img1,img2,img3);
			break;
		case 4:
			set_four_img(img1,img2,img3,img4);
			break;
		}
		
	}
	
	public void set_normal_data(){
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		//Log.d("xxx", loginname+"111");
		user_headimg.setImageDrawable(new CircleImageDrawable(getheadimg(loginname)));
		
		
		nickname_text.setText(nickname);
		
		time_text.setText(time);
		
		if(islike){
		islike_view.setImageResource(R.drawable.islike);
		}else{
		islike_view.setImageResource(R.drawable.unlike);
		}
		
		islike_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getlike();
				 like_click();
				 getlike();
				 if (like_name != null) {
						
						Cursor nickname_cursor = null;
						String like_display = "";
						String temp_nickname ="";
						
						for(int i=0;i<like_name.length;i++){
							nickname_cursor= db.query("User_personal_info_table", 
									new String[]{"nickname"}, "name = ?", new String[]{like_name[i]}, null, null, null);
							nickname_cursor.moveToFirst();
							String getnickname = nickname_cursor.getString(nickname_cursor.getColumnIndex("nickname"));
							if(getnickname == null){
								temp_nickname = "未填写昵称";
							}else{
								temp_nickname = getnickname;
							}
							like_display = like_display+temp_nickname+",";
						}

						like_name_display.setText( like_display + " " + good_count
								+ "人点了赞！");
						
						nickname_cursor.close();
					} else {
						like_name_display.setText("还未获赞！");
					}

			}
		});
		
		
		
		like_count.setText(good_count+"");
		
		
		if (like_name != null) {
			
			Cursor nickname_cursor = null;
			String like_display = "";
			String temp_nickname ="";
			
			for(int i=0;i<like_name.length;i++){
				nickname_cursor= db.query("User_personal_info_table", 
						new String[]{"nickname"}, "name = ?", new String[]{like_name[i]}, null, null, null);
				nickname_cursor.moveToFirst();
				String getnickname = nickname_cursor.getString(nickname_cursor.getColumnIndex("nickname"));
				if(getnickname == null){
					temp_nickname = "未填写昵称";
				}else{
					temp_nickname = getnickname;
				}
				like_display = like_display+temp_nickname+",";
			}

			like_name_display.setText( like_display + " " + good_count
					+ "人点了赞！");
			
			nickname_cursor.close();
		} else {
			like_name_display.setText("还未获赞！");
		}
		
		comment_count.setText(mark_count+"");
		
		
		if(news_text_path.equals("")){
		news_text.setText("");}
		else{
			File gettext = new File(news_text_path);
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
			news_text.setText(str);
			}
		}
		
		//edit_comment = (EditText)findViewById(R.id.edit_comment);
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edit_comment.getText().toString().length()>0){
					send_comment();
					edit_comment.setText("");
				}else{
					Toast.makeText(news_details.this, "请填写回复内容！", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
	}
	
	public void set_one_img(String one_pic_path){
		RelativeLayout onepiclayout = (RelativeLayout)findViewById(R.id.user_news_onepic_layout);
		onepiclayout.setVisibility(View.VISIBLE);
		ImageView onepic = (ImageView)findViewById(R.id.user_news_onepic_1);
		
		pic_loading(one_pic_path,onepic);
	}
	public void set_two_img(String one_pic_path,String two_pic_path){
		
		LinearLayout twopiclayout = (LinearLayout)findViewById(R.id.user_news_twopic_layout);
		twopiclayout.setVisibility(View.VISIBLE);
		ImageView onepic = (ImageView)findViewById(R.id.user_news_twopic_1);
		ImageView twopic = (ImageView)findViewById(R.id.user_news_twopic_2);
		
		pic_loading(one_pic_path,onepic);
		pic_loading(two_pic_path,twopic);
		
	}
	public void set_three_img(String one_pic_path,String two_pic_path,String three_pic_path){
		
		LinearLayout threepiclayout = (LinearLayout)findViewById(R.id.user_news_threepic_layout);
		threepiclayout.setVisibility(View.VISIBLE);
		ImageView onepic = (ImageView)findViewById(R.id.user_news_threepic_1);
		ImageView twopic = (ImageView)findViewById(R.id.user_news_threepic_2);
		ImageView threepic = (ImageView)findViewById(R.id.user_news_threepic_3);
		
		pic_loading(one_pic_path,onepic);
		pic_loading(two_pic_path,twopic);
		pic_loading(three_pic_path,threepic);
		
	}
	public void set_four_img(String one_pic_path,String two_pic_path,
			String three_pic_path,String four_pic_path){
		
		LinearLayout fourpiclayout = (LinearLayout)findViewById(R.id.user_news_fourpic_layout);
		fourpiclayout.setVisibility(View.VISIBLE);
		ImageView onepic = (ImageView)findViewById(R.id.user_news_fourpic_1);
		ImageView twopic = (ImageView)findViewById(R.id.user_news_fourpic_2);
		ImageView threepic = (ImageView)findViewById(R.id.user_news_fourpic_3);
		ImageView fourpic = (ImageView)findViewById(R.id.user_news_fourpic_4);
		
		pic_loading(one_pic_path,onepic);
		pic_loading(two_pic_path,twopic);
		pic_loading(three_pic_path,threepic);
		pic_loading(four_pic_path,fourpic);
		
	}
	
	public Bitmap getheadimg(String name){
		
		Bitmap	mbitmap=null;
		//Log.d("xxx", name);
		Cursor headimg_cursor = db.query("User_headImage_table", null, "name = ?",
				new String[] {loginname}, null, null, null);
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
		
		headimg_cursor.close();
		return mbitmap;
	}
	
	public void like_click(){
		
		if(islike){
			islike_view.setImageResource(R.drawable.unlike);
			good_count--;
			like_count.setText(good_count+"");
			islike = false;
			deletelike();
		}else{
			islike_view.setImageResource(R.drawable.islike);
			good_count++;
			like_count.setText(good_count+"");
			islike = true;
			addlike();
		}
		
		
	}
	
	
	public void deletelike(){
		if(like_name.length==1){
				db.delete("User_news_good_table", "news_id = ?", 
						new String[]{news_id});
			}else{
				
				String str = "";
				for(int i=0;i<like_name.length;i++){
					
					if(!like_name[i].equals(Home_page.UsingName)){
					str = str+like_name[i]+",";
					}
					
				}
				
				ContentValues values = new ContentValues();
				
				values.put("goodname", str);
				
				db.update("User_news_good_table", values, 
						"news_id = ?", new String[]{news_id});
				values.clear();
			}
	}
	
	
	public void addlike(){
		
		ContentValues values = new ContentValues();
			
			if(like_name==null){
				
 			values.put("news_id", news_id);
 			values.put("goodname", Home_page.UsingName+",");
 			db.insert("User_news_good_table", null, values);
 			values.clear();
			}else{
				String str = "";
				for(int i=0;i<like_name.length;i++){
					str = str + like_name[i]+",";
				}
				str = str + Home_page.UsingName +",";
				values.put("goodname", str);
				db.update("User_news_good_table", values, 
						"news_id = ?", new String[]{news_id});
				values.clear();
			}
		
	}
	
	public void send_comment(){
		
		Cursor nickname_cursor= db.query("User_personal_info_table", 
				new String[]{"nickname"}, "name = ?", new String[]{Home_page.UsingName}, null, null, null);
		nickname_cursor.moveToFirst();
		String getnickname = nickname_cursor.getString(nickname_cursor.getColumnIndex("nickname"));
		String temp_nickname = null;
		if(getnickname == null){
			temp_nickname = "未填写昵称";
		}else{
			temp_nickname = getnickname;
		}
		
		nickname_cursor.close();
		
		String comment_title = null;
		String comment_content = null;
		String comment_time = get_now_time();
		
		if(edit_comment.getHint().toString().equals("评论：")){
			comment_title = temp_nickname+":";
			comment_content = edit_comment.getText().toString();
		}else{
			String other_name = edit_comment.getHint().toString().substring(3);
			comment_title = temp_nickname + "回复"+other_name+":";
			comment_content = edit_comment.getText().toString();
		}
		//String i= "回复：xalkdas";
		//String x = i.substring(3);
		//Log.d("xxx", x);
		ContentValues values = new ContentValues();
		int id = Integer.parseInt(news_id);
		values.put("news_id", id);
		values.put("marktitle", comment_title);
		values.put("marktext", comment_content);
		values.put("marktime", comment_time);
		values.put("markname", Home_page.UsingName);
		db.insert("User_news_mark_table", null, values);
		values.clear();
		
		mark_count++;
		comment_count.setText(mark_count+"");
		add_one_comment_view(comment_title,comment_content,true,comment_time,Home_page.UsingName);
		Toast.makeText(news_details.this, "评论成功", Toast.LENGTH_SHORT).show();
	}
	
	public String get_now_time(){
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
		if(second.length() == 1){
			second = "0"+second;
		}
		
		String timedb = year+month+date+hour+minute+second;
		
		return timedb;
	}
	

	
	public void add_comment_view(){
		Cursor cr = db.query("User_news_mark_table",
				null, "news_id = ?", new String[]{news_id},null,null,null);
		cr.moveToFirst();
		//Log.d("xxx", cr.getCount()+"");
		if(cr.getCount()>0){
			
			do{
				//Log.d("xxx", "i");
				String comment_title = cr.getString(cr.getColumnIndex("marktitle"));
				String comment_content = cr.getString(cr.getColumnIndex("marktext"));
				String comment_time = cr.getString(cr.getColumnIndex("marktime"));
				String comment_name = cr.getString(cr.getColumnIndex("markname"));
				boolean mycomment = cr.getString(cr.getColumnIndex("markname")).equals(Home_page.UsingName);
				View view = new View(this);
				view = create_view.createView(comment_title, comment_content, 
						mycomment,comment_time,comment_name,news_details.this);
				add_view_layout.addView(view);
			}while(cr.moveToNext());
			
			
		}
		
		cr.close();
	}
	
	public void add_one_comment_view(String title,String content,boolean mycomment,String time,String name){
		View view = new View(this);
		view = create_view.createView(title, content, 
				mycomment,time,name,news_details.this);
		add_view_layout.addView(view);
	}
	public void delete_comment_view(final View v,final String time,final String name,final String title){
		new AlertDialog.Builder(this).setMessage("是否确认删掉该条回复?")
		.setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				add_view_layout.removeView(v);
				db.delete("User_news_mark_table","marktime=? and markname = ? and marktitle =?", new String[]{time,name,title});
				
				mark_count--;
				comment_count.setText(mark_count+"");
				Toast.makeText(news_details.this, "删除成功", Toast.LENGTH_SHORT).show();
			}
		}).setPositiveButton("否", null).show();
		
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
		loadimg.ClosePoll();
	}
	
	
}
