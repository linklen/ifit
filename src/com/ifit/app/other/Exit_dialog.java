package com.ifit.app.other;

import com.ifit.app.R;
import com.ifit.app.activity.Home_page;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;



public class Exit_dialog extends Activity {

	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog);
		layout=(LinearLayout)findViewById(R.id.exit_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	public void btn_exit_no(View v) {  
    	this.finish();    	
      }  
	public void btn_exit_yes(View v) {  
    	this.finish();
    	Home_page.Home_page_instance.finish();//Ô¶³Ì¹Ø±ÕActivity
      }  
	
}
