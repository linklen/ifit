package com.ifit.app.activity;

import com.ifit.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class Personal_rank extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_rank);
		ImageView btn_exit = (ImageView)findViewById(R.id.personal_rank_exit);
		
		btn_exit.setOnClickListener(new MyListener());
	}

	
	public class MyListener implements OnClickListener{

		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.slide_uptoin, R.anim.slide_intodown);
	}
	
}
