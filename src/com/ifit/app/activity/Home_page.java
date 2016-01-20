package com.ifit.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.MyFragPagerAdapter;
import com.ifit.app.fragment.frag_news;
import com.ifit.app.fragment.frag_search;
import com.ifit.app.fragment.frag_learn;
import com.ifit.app.other.Exit_dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
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
import android.widget.PopupWindow;
import android.widget.Toast;

public class Home_page extends FragmentActivity {

	public static Home_page Home_page_instance = null; 
	ViewPager viewpager;
	private  boolean menu_display = false;
	private PopupWindow menuWindow;
	private View menuview;
	private boolean isclickedmenu= false;
	private Button btn_change_user,btn_menu_cancel,btn_menu_exit;//popuwindow布局中的按键
	private ImageView btn_news,btn_learn,btn_search;//底部按钮的按键
	private int before = 0;//上一个页面编号
	 
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
				SharedPreferences.Editor clear = getSharedPreferences("islogin", MODE_PRIVATE)
						.edit();
				clear.clear();
				clear.commit();
				Intent turn_login = new Intent (Home_page.this,login.class);
				startActivity(turn_login);
				finish();
				menuWindow.dismiss();
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

}
