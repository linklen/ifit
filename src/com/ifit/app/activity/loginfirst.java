package com.ifit.app.activity;

import com.ifit.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class loginfirst extends Activity implements OnClickListener{

	private Button loginbtn,registebtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginfirst);
		
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
			Intent intent1 = new Intent(this,login.class);
			startActivity(intent1);
			break;
		case R.id.firstzc:
			Intent intent2 = new Intent(this,registe.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	
}
