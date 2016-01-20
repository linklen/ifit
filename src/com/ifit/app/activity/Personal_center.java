package com.ifit.app.activity;

import com.ifit.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Personal_center extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_center);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.slide_uptoin, R.anim.slide_intodown);
	}
}
