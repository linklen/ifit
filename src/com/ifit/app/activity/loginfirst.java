package com.ifit.app.activity;

import com.ifit.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class loginfirst extends Activity implements OnClickListener{

	public static loginfirst loginfirst_instance = null;
	private Button loginbtn,registebtn;
	public int click_back_count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginfirst);
		loginfirst_instance = this;
		loginbtn = (Button)findViewById(R.id.firstdl);
		registebtn= (Button)findViewById(R.id.firstzc);
		
		loginbtn.setOnClickListener(this);
		registebtn.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.firstdl:
			Intent login = new Intent(this,login.class);
			startActivity(login);
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			finish();
			break;
		case R.id.firstzc:
			Intent registe = new Intent(this,registe.class);
			startActivity(registe);
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override    
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	if(keyCode == KeyEvent.KEYCODE_BACK){
		double_click_exit();
		return false;
	}else{
	return  super.onKeyDown(keyCode, event);
		}
	}
	public void double_click_exit(){
		if(click_back_count == 0){
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			click_back_count = 1;
			mHandler.sendEmptyMessageDelayed(0, 1500);
		}else {
			finish();
		}
	}
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			click_back_count = 0;
		}
		
	};
}
