package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.MyFragPagerAdapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.fragment.frag_news;
import com.ifit.app.fragment.frag_search;
import com.ifit.app.fragment.frag_learn;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.Exit_dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Home_page extends FragmentActivity {

	public static Home_page Home_page_instance = null; 
	public static String UsingName = null;
	ViewPager viewpager;
	private  boolean menu_display = false;
	private PopupWindow menuWindow;
	private View menuview;
	private boolean isclickedmenu= false;
	private Button btn_change_user,btn_menu_cancel,btn_menu_exit;//popuwindow布局中的按键
	private ImageView btn_news,btn_learn,btn_search;//底部按钮的按键
	private int before = 0;//上一个页面编号
	//private LinearLayout view_pager_container;
	
	
	//设置title里面的数据
	public File sdCard,directory,location_image;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private SharedPreferences locationdate,getUser_name;//获取和建立文件
	private String login_name;//储存读进来的账户号
	private boolean isFirst = true;//记录是否为第一次开启来
	private ImageView title_user_headimg;
	private TextView title_user_nickname;
	private Bitmap mbitmap;
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_page);
		//初始化instance
		Home_page_instance = this;
		//获取LoadActivity传送来的数据
		Intent getIntent = getIntent();
		Boolean is_send = getIntent.getBooleanExtra("is_send", false);
		String get_user_name = getIntent.getStringExtra("the_user_name");
		Boolean get_is_new = getIntent.getBooleanExtra("is_new", false);
		if(is_send){
					if(get_is_new){
									Toast.makeText(this,  
									"欢迎您，新用户"+get_user_name,
									Toast.LENGTH_SHORT).show();
									}else{Toast.makeText(this,
										  "欢迎回来，"+ get_user_name, 
										  Toast.LENGTH_SHORT).show();}
					}
		
		
		//数据库的建立，和pref的读取
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getReadableDatabase();
		
		getUser_name = getSharedPreferences("islogin", 0);
		login_name = getUser_name.getString("user", "");
		UsingName = login_name;
		
		locationdate = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		
		
		
		
		//检测文件夹和文件
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/UserHeadImage/");
		if(!directory.exists()){
		directory.mkdirs();}
		location_image = new File(directory,"location_image.jpg");
		
		
		//获取顶部视图
		
		title_user_nickname = (TextView)findViewById(R.id.title_user_name);
		title_user_headimg = (ImageView)findViewById(R.id.title_user_center);
		
		
		
		
		
		
		// 构造适配器
		List<Fragment> FragmentList = new ArrayList<Fragment>();
		FragmentList.add(new frag_news());
		FragmentList.add(new frag_learn());
		FragmentList.add(new frag_search());
		MyFragPagerAdapter adapter = new MyFragPagerAdapter
				(getSupportFragmentManager(), FragmentList);
		
		//设定viewpager适配器
		viewpager = (ViewPager)findViewById(R.id.viewpager);
		viewpager.setAdapter(adapter);
		viewpager.addOnPageChangeListener(new MyPagerChangeListener());//setOnPageChangeListener过时了
		/*view_pager_container.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return viewpager.dispatchTouchEvent(event);
				
			}
		});*/
		
		//开始变换按钮颜色
		btn_news=(ImageView)findViewById(R.id.btn_news);
		btn_learn=(ImageView)findViewById(R.id.btn_learn);
		btn_search=(ImageView)findViewById(R.id.btn_search);
		
		btn_news.setOnClickListener(new MyClickListener(0));
		btn_learn.setOnClickListener(new MyClickListener(1));
		btn_search.setOnClickListener(new MyClickListener(2));
		
		
		
		
	}

	
	public class MyClickListener implements OnClickListener  {

		private int Index = 0;
		
		public MyClickListener(int i){
			Index = i;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewpager.setCurrentItem(Index);
		}
		
	}
	
	public class MyPagerChangeListener implements OnPageChangeListener{

		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0){
			case 0:
				btn_news.setImageResource(R.drawable.news_green);//使用setImageResource进行图片更新，据说效率慢，而且最终是调用setImageDrawable
				if(before == 1){
					btn_learn.setImageResource(R.drawable.learn);
				}else if(before == 2){
					btn_search.setImageResource(R.drawable.search);
				}
				break;
			case 1:
				btn_learn.setImageDrawable(getResources().getDrawable(R.drawable.learn_green));//用setImageDrawable进行图片更新，据说效率高
				if(before == 0){
					btn_news.setImageDrawable(getResources().getDrawable(R.drawable.news));
				}else if(before == 2){
					btn_search.setImageDrawable(getResources().getDrawable(R.drawable.search));//查过接口废弃，改为两个参数，第二个参数为主题,但是我设置空出错
				}
				break;
			case 2:
				btn_search.setImageResource(R.drawable.search_green);//还是这个方便，牺牲下效率
				if(before == 0){
					btn_news.setImageResource(R.drawable.news);
				}else if(before == 1){
					btn_learn.setImageResource(R.drawable.learn);
				}
				break;
			}
			before = arg0;
		}
		
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK  ){
			if(menu_display){
				menuWindow.dismiss();
				menu_display = false;
			}else{
			Intent open_exit_dialog = new Intent(Home_page.this,Exit_dialog.class);
			startActivity(open_exit_dialog);
			} 
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			if(!menu_display && !isclickedmenu){
				menuview = LayoutInflater.from(this).inflate(R.layout.menuview, null); 
				menuWindow = new PopupWindow(menuview,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//后面两个参数传入的是宽和高
				menuWindow.showAtLocation(this.findViewById(R.id.home_page),Gravity.BOTTOM, 0, 0);
				//menuWindow.setFocusable(true);
				//menuWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
				//menuWindow.setOutsideTouchable(false);
				//menuWindow.setBackgroundDrawable(new BitmapDrawable());
				menu_display = true;
				btn_change_user = (Button)menuview.findViewById(R.id.menu_btn_switch_user);
				btn_menu_cancel = (Button)menuview.findViewById(R.id.menu_btn_cancel);
				btn_menu_exit = (Button)menuview.findViewById(R.id.menu_btn_exit);
				btn_change_user.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						confirm_change_user();
					}
				});
				btn_menu_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						menuWindow.dismiss();
						menu_display = false;
						
					}
				});
				btn_menu_exit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent open_exit_dialog = new Intent(Home_page.this,Exit_dialog.class);
						startActivity(open_exit_dialog);
						menu_display = false;
						menuWindow.dismiss();
					}
				});
			}else{
				//如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
			}
		}
		return  true;     
	}
	
	public boolean confirm_change_user(){
		new AlertDialog.Builder(Home_page.this).setTitle("确定注销？").setMessage("您确定要注销用户吗？")
		.setPositiveButton("手抖了",  new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				menu_display = false;
				menuWindow.dismiss();
			}
			
		}).setNegativeButton("是的", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor clear_login,clear_data;
				clear_login = getSharedPreferences("islogin", MODE_PRIVATE)
						.edit();
				clear_data = getSharedPreferences("location_user_Data", MODE_PRIVATE)
						.edit();
				
				clear_login.clear();
				clear_data.clear();
				
				clear_login.commit();
				clear_data.commit();
				menuWindow.dismiss();
				
				Intent turn_login = new Intent (Home_page.this,login.class);
				//turn_login.putExtra("is_back", true);
				startActivity(turn_login);
				finish();
				
			}
			
		}).show();
		return true;
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(menu_display){
		menuWindow.dismiss();
		menu_display = false;
		}
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		set_data();
	}

	


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		frag_news.frag_news_instance.refreshlist();
	}

	public void set_data(){
		isFirst = locationdate.getBoolean("isFirst", true);
		
		if(!isFirst){
			
			String nickname = locationdate.getString("nickname", "");
			if(!nickname.equals("")){
				title_user_nickname.setText(nickname);
			}
			boolean isExist_img = true;
			if(location_image.exists()){
				//如果用户数据库中存在头像则进行设置
				mbitmap = BitmapFactory
						.decodeFile(location_image.toString());
				title_user_headimg.setImageDrawable(new CircleImageDrawable(mbitmap));
			}else{
				isExist_img = get_headimg(login_name);
				}
			if(!isExist_img){
				mbitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.default_headimage);
				title_user_headimg.setImageDrawable(new CircleImageDrawable(mbitmap));
			}
			
			
		}else{
			if(location_image.exists()){
				location_image.delete();
			}
			get_data(login_name);
		}
		
	}
	
	public void get_data(String getName){
		
		Cursor cursor = db.query("User_personal_info_table",
				 new String[]{"nickname,age,region"},
				 "name = ?", 
				 new String[]{getName}, null,null, null);
		
		cursor.moveToFirst();
		String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
		int age = cursor.getInt(cursor.getColumnIndex("age"));
		String region = cursor.getString(cursor.getColumnIndex("region"));
		
		SharedPreferences.Editor editor = locationdate.edit();
		editor.putString("nickname", nickname);
		editor.putInt("age", age);
		editor.putString("region", region);
		editor.putBoolean("isFirst", false);
		editor.commit();
		
		cursor.close();
		set_data();
		
	}
	
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
			title_user_headimg.setImageDrawable(new CircleImageDrawable(getbitmap));

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
	
	
}
