package com.ifit.app;

import com.ifit.app.activity.loginfirst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class LoadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.startpic);
		
		Thread t1 = new Thread(time);
		t1.start();
	}
	Runnable time = new Runnable(){

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
		
	};
}
