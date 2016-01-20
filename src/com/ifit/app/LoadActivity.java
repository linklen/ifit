package com.ifit.app;

import com.ifit.app.activity.Home_page;
import com.ifit.app.activity.loginfirst;
import com.ifit.app.db.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

public class LoadActivity extends Activity {

	private user dbcreate;
	public int time = 0;
	public boolean judge = false;
	public String user="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.startpic);
		dbcreate = new user(this,"User_table.db",null,1);
		dbcreate.getReadableDatabase();
		dbcreate.close();
		SharedPreferences islogin = getSharedPreferences("islogin", MODE_PRIVATE);
		judge = islogin.getBoolean("login_in", false);
		user = islogin.getString("user", "");
		if(judge){
			time = 1500;
		}else{
			time = 3000;
		}
		//调用handler方法延迟
		new Handler().postDelayed(new Runnable(){
			
			//获取是否已经登陆过文件
			
			
			@Override
			public void run(){
				if(judge){
					Intent turn_home_page = new Intent(LoadActivity.this,Home_page.class);
					turn_home_page.putExtra("the_user_name", user);
					turn_home_page.putExtra("is_send", true);
					startActivity(turn_home_page);
					finish();
				}else{
				Intent intent = new Intent (LoadActivity.this,loginfirst.class);
				startActivity(intent);
				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
				finish();}
			}
		}, time);
		//以下是创建线程进行延时
		/*Thread t1 = new Thread(time);
		t1.start();*/
	}
	/*Runnable time = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(3000);	
				Intent i1 = new Intent(LoadActivity.this, loginfirst.class);
				startActivity(i1);
				finish();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	};*/
	
	@Override    
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	if(keyCode == KeyEvent.KEYCODE_BACK){
		return  true;
		} 
	return  super.onKeyDown(keyCode, event);     
	}	
}


