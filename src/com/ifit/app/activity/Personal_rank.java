package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.rank_list_adapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.rank_list_item;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Personal_rank extends Activity {

	private ImageView userhead;
	private TextView myorder;
	public File sdCard,directory,location_image;
	public MyDatabaseHelper usedb;
	public SQLiteDatabase db;
	public Bitmap mbitmap;
	
	private ListView listview;
	private rank_list_item item;
	private List<rank_list_item> list = new ArrayList<rank_list_item>();
	private rank_list_adapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_rank);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		initpath();
		
		ImageView btn_exit = (ImageView)findViewById(R.id.personal_rank_exit);
		userhead = (ImageView)findViewById(R.id.user_head);
		myorder = (TextView)findViewById(R.id.myorder);
		listview = (ListView)findViewById(R.id.personal_rank_listview);
		btn_exit.setOnClickListener(new MyListener());
		
		set_myheadimg();
		myorder.setText(get_rank()+"");
		
		use_thread_initlistview();
	}
	
	
	public void use_thread_initlistview(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				get_other_rank();
				
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	
	public void get_other_rank(){
		Cursor cr = db.query("User_order_table", 
				new String[]{"name","weektraintime","rank"}, 
				null, null, null, null, "rank ASC");
		
		cr.moveToFirst();
		for(int i = 0;i<9;){
			String name = cr.getString(cr.getColumnIndex("name"));
			int weektime = (int) Math.floor(cr.getInt(cr.getColumnIndex("weektraintime"))/60);
			String rank = cr.getString(cr.getColumnIndex("rank"));
			if(rank != null){
			Bitmap getbitmap = null;
			Cursor cr_headimg = db.query("User_headImage_table", 
					null,"name = ?", new String[]{name}, null,null,null);
			cr_headimg.moveToFirst();
			if (cr_headimg.getBlob(cr_headimg.getColumnIndex("user_head_img")) != null) {
			//if (cr_headimg.getCount()>0) {
				byte[] get_Headimg = cr_headimg.getBlob(cr_headimg.getColumnIndex("user_head_img"));
				getbitmap = BitmapFactory.decodeByteArray(get_Headimg, 0,get_Headimg.length);
			}else{
				getbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
			}
			add_list(rank,name,weektime,getbitmap);
			cr_headimg.close();
			i++;
			}
			if(!cr.moveToNext()){
				break;
			}
		}
	}
	
	public void add_list(String rank,String nickname,int weektime,Bitmap getbitmap){
		item = new rank_list_item(rank, nickname, weektime+"", getbitmap);
		list.add(item);
	}
	
	public void init_adapter(){
		adapter = new rank_list_adapter(this, R.layout.rank_listitem, list);
		listview.setAdapter(adapter);
	}
	
	public String get_rank(){
		Cursor cr = db.query("User_order_table", new String[]{"rank"},"name = ?",new String[]{Home_page.UsingName},null,null,null);
		cr.moveToFirst();
		String rank = cr.getString(cr.getColumnIndex("rank"));
		if(rank == null){
			rank = "暂无排名";
		}
		return rank;
		
	}
	
	
	public void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/UserHeadImage/");
		if(!directory.exists()){
		directory.mkdirs();}
		location_image = new File(directory,"location_image.jpg");
	}
	
	
	public void set_myheadimg(){
		
		boolean isExist_img = true;
		
		if(location_image.exists()){
			//如果用户数据库中存在头像则进行设置
			mbitmap = BitmapFactory
					.decodeFile(location_image.toString());
			userhead.setImageDrawable(new CircleImageDrawable(mbitmap));
		}else{
			isExist_img = get_headimg(Home_page.UsingName);
			}
		if(!isExist_img){
			mbitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_headimage);
			userhead.setImageDrawable(new CircleImageDrawable(mbitmap));
		}
	}
	
	//获取数据库中的头像，数据库中有头像则返回true，没有则返回false；
		public boolean get_headimg(String getName){
		
		// 读取头像
		Cursor cursor = db.query("User_headImage_table",
				new String[] { "user_head_img" }, "name = ?",
				new String[] { getName }, null, null, null);
		cursor.moveToFirst();
		
		if (cursor.getBlob(cursor.getColumnIndex("user_head_img")) != null) {
			byte[] get_Headimg = cursor.getBlob(cursor
					.getColumnIndex("user_head_img"));
			Bitmap getbitmap = BitmapFactory.decodeByteArray(get_Headimg, 0,
					get_Headimg.length);
			userhead.setImageDrawable(new CircleImageDrawable(getbitmap));

			if (location_image.exists()) {
				location_image.delete();
			}
			try {
				FileOutputStream Outputimg = new FileOutputStream(
						location_image);
				getbitmap.compress(Bitmap.CompressFormat.JPEG, 100, Outputimg);
				Outputimg.flush();// 清空缓存区域
				Outputimg.close();
				// Log.d("xxx", "已经保存");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cursor.close();
			getbitmap.recycle();
			return true;
		}else{
			cursor.close();
			return false;
		}
		
	}
	public class MyListener implements OnClickListener{

		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1 :
				init_adapter();
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}

	
}
	
