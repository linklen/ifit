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
	private Button btn_change_user,btn_menu_cancel,btn_menu_exit;//popuwindow�����еİ���
	private ImageView btn_news,btn_learn,btn_search;//�ײ���ť�İ���
	private int before = 0;//��һ��ҳ����
	//private LinearLayout view_pager_container;
	
	
	//����title���������
	public File sdCard,directory,location_image;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private SharedPreferences locationdate,getUser_name;//��ȡ�ͽ����ļ�
	private String login_name;//������������˻���
	private boolean isFirst = true;//��¼�Ƿ�Ϊ��һ�ο�����
	private ImageView title_user_headimg;
	private TextView title_user_nickname;
	private Bitmap mbitmap;
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_page);
		//��ʼ��instance
		Home_page_instance = this;
		//��ȡLoadActivity������������
		Intent getIntent = getIntent();
		Boolean is_send = getIntent.getBooleanExtra("is_send", false);
		String get_user_name = getIntent.getStringExtra("the_user_name");
		Boolean get_is_new = getIntent.getBooleanExtra("is_new", false);
		if(is_send){
					if(get_is_new){
									Toast.makeText(this,  
									"��ӭ�������û�"+get_user_name,
									Toast.LENGTH_SHORT).show();
									}else{Toast.makeText(this,
										  "��ӭ������"+ get_user_name, 
										  Toast.LENGTH_SHORT).show();}
					}
		
		
		//���ݿ�Ľ�������pref�Ķ�ȡ
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getReadableDatabase();
		
		getUser_name = getSharedPreferences("islogin", 0);
		login_name = getUser_name.getString("user", "");
		UsingName = login_name;
		
		locationdate = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		
		
		
		
		//����ļ��к��ļ�
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/UserHeadImage/");
		if(!directory.exists()){
		directory.mkdirs();}
		location_image = new File(directory,"location_image.jpg");
		
		
		//��ȡ������ͼ
		
		title_user_nickname = (TextView)findViewById(R.id.title_user_name);
		title_user_headimg = (ImageView)findViewById(R.id.title_user_center);
		
		
		
		
		
		
		// ����������
		List<Fragment> FragmentList = new ArrayList<Fragment>();
		FragmentList.add(new frag_news());
		FragmentList.add(new frag_learn());
		FragmentList.add(new frag_search());
		MyFragPagerAdapter adapter = new MyFragPagerAdapter
				(getSupportFragmentManager(), FragmentList);
		
		//�趨viewpager������
		viewpager = (ViewPager)findViewById(R.id.viewpager);
		viewpager.setAdapter(adapter);
		viewpager.addOnPageChangeListener(new MyPagerChangeListener());//setOnPageChangeListener��ʱ��
		/*view_pager_container.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return viewpager.dispatchTouchEvent(event);
				
			}
		});*/
		
		//��ʼ�任��ť��ɫ
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
				btn_news.setImageResource(R.drawable.news_green);//ʹ��setImageResource����ͼƬ���£���˵Ч���������������ǵ���setImageDrawable
				if(before == 1){
					btn_learn.setImageResource(R.drawable.learn);
				}else if(before == 2){
					btn_search.setImageResource(R.drawable.search);
				}
				break;
			case 1:
				btn_learn.setImageDrawable(getResources().getDrawable(R.drawable.learn_green));//��setImageDrawable����ͼƬ���£���˵Ч�ʸ�
				if(before == 0){
					btn_news.setImageDrawable(getResources().getDrawable(R.drawable.news));
				}else if(before == 2){
					btn_search.setImageDrawable(getResources().getDrawable(R.drawable.search));//����ӿڷ�������Ϊ�����������ڶ�������Ϊ����,���������ÿճ���
				}
				break;
			case 2:
				btn_search.setImageResource(R.drawable.search_green);//����������㣬������Ч��
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
				menuWindow = new PopupWindow(menuview,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//������������������ǿ�͸�
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
				//�����ǰ�Ѿ�Ϊ��ʾ״̬������������
				menuWindow.dismiss();
				menu_display = false;
			}
		}
		return  true;     
	}
	
	public boolean confirm_change_user(){
		new AlertDialog.Builder(Home_page.this).setTitle("ȷ��ע����").setMessage("��ȷ��Ҫע���û���")
		.setPositiveButton("�ֶ���",  new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				menu_display = false;
				menuWindow.dismiss();
			}
			
		}).setNegativeButton("�ǵ�", new DialogInterface.OnClickListener(){

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
				//����û����ݿ��д���ͷ�����������
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
		
		// ��ȡͷ��
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
				Outputimg.flush();// ��ջ�������
				Outputimg.close();
				// Log.d("xxx", "�Ѿ�����");
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
