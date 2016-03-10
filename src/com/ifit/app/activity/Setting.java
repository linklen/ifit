package com.ifit.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ifit.app.R;

public class Setting extends Activity {

	private ImageView btn_back;
	private RelativeLayout edit_layout,feedback_layout,about_layout,exit_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		btn_back = (ImageView)findViewById(R.id.setting_btn_back);
		edit_layout=(RelativeLayout)findViewById(R.id.setting_editor_item_layout);
		feedback_layout = (RelativeLayout)findViewById(R.id.setting_feedback_item_layout);
		about_layout = (RelativeLayout)findViewById(R.id.setting_about_item_layout);
		exit_layout = (RelativeLayout)findViewById(R.id.setting_exit_item_layout);
		
		
		btn_back.setOnClickListener(new MyclickListen());
		edit_layout.setOnClickListener(new MyclickListen());
		feedback_layout.setOnClickListener(new MyclickListen());
		about_layout.setOnClickListener(new MyclickListen());
		exit_layout.setOnClickListener(new MyclickListen());
	}
	
	class MyclickListen implements OnClickListener{

		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.setting_btn_back:
				finish();
				break;
			case R.id.setting_editor_item_layout:
				Intent turn_Personal_data = new Intent(Setting.this,Personal_data.class);
				startActivity(turn_Personal_data);
				break;
			case R.id.setting_feedback_item_layout:
				Intent turn_feed_back = new Intent(Setting.this,Feed_back.class);
				startActivity(turn_feed_back);
				break;
			case R.id.setting_about_item_layout:
				Intent turn_About = new Intent(Setting.this,About.class);
				startActivity(turn_About);
				break;
			case R.id.setting_exit_item_layout:
				confirm_change_user();
				break;
			default:
				break;
			}
		}
		
	}
	
	public void confirm_change_user(){
		new AlertDialog.Builder(this).setTitle("确定注销？").setMessage("您确定要注销用户吗？")
		.setPositiveButton("手抖了",null).setNegativeButton("是的", new DialogInterface.OnClickListener(){

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
				Intent turn_login = new Intent (Setting.this,login.class);
				//turn_login.putExtra("is_back", true);
				startActivity(turn_login);
				finish();
			}
			
		}).show();
	}
}
