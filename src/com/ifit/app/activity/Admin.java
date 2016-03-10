package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.admin.Backup_and_restore;
import com.ifit.app.admin.View_the_data;
import com.ifit.app.admin.feedback_manage;
import com.ifit.app.admin.learn_manage;
import com.ifit.app.admin.order_manage;
import com.ifit.app.admin.admin_manage;
import com.ifit.app.admin.user_manage;
import com.ifit.app.admin.user_report_manage;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Admin extends Activity {

	private RelativeLayout learn_manage;
	private RelativeLayout order_manage;
	private RelativeLayout admin_manage;
	private RelativeLayout user_manage;
	private RelativeLayout data_statistics;
	private RelativeLayout data_backup_an_restore;
	private RelativeLayout look_feedback;
	private RelativeLayout look_userreport;
	private TextView adminname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);
		
		Intent getname = getIntent();
		String name = getname.getStringExtra("adminname");
		
		adminname = (TextView)findViewById(R.id.admin_name);
		
		adminname.setText(name);
		
		learn_manage = (RelativeLayout)findViewById(R.id.learn_manage);
		learn_manage.setOnClickListener(new MyOnClickListen());
		
		order_manage = (RelativeLayout)findViewById(R.id.order_manage);
		order_manage.setOnClickListener(new MyOnClickListen());
		
		admin_manage = (RelativeLayout)findViewById(R.id.manage_admin);
		admin_manage.setOnClickListener(new MyOnClickListen());
		
		user_manage = (RelativeLayout)findViewById(R.id.manage_user);
		user_manage.setOnClickListener(new MyOnClickListen());
		
		data_statistics = (RelativeLayout)findViewById(R.id.data_statistics);
		data_statistics.setOnClickListener(new MyOnClickListen());
		
		data_backup_an_restore =(RelativeLayout)findViewById(R.id.data_backup_an_restore);
		data_backup_an_restore.setOnClickListener(new MyOnClickListen());
		
		look_feedback = (RelativeLayout)findViewById(R.id.look_feedback);
		look_feedback.setOnClickListener(new MyOnClickListen());
		
		look_userreport = (RelativeLayout)findViewById(R.id.look_userreport);
		look_userreport.setOnClickListener(new MyOnClickListen());
		
	}

	
	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.learn_manage: 
				Intent turn_learn_manage = new Intent(Admin.this,learn_manage.class);
				startActivity(turn_learn_manage);
				break;
			case R.id.order_manage:
				Intent turn_order_manage = new Intent(Admin.this,order_manage.class);
				startActivity(turn_order_manage);
				break;
			case R.id.manage_admin:
				Intent turn_admin_manage = new Intent(Admin.this,admin_manage.class);
				startActivity(turn_admin_manage);
				break;
			case R.id.manage_user:
				Intent turn_user_manage = new Intent(Admin.this,user_manage.class);
				startActivity(turn_user_manage);
				break;
			case R.id.data_statistics:
				Intent turn_view_the_data = new Intent(Admin.this,View_the_data.class);
				startActivity(turn_view_the_data);
				break;
			case R.id.data_backup_an_restore:
				Intent turn_Backup_and_restore = new Intent(Admin.this,Backup_and_restore.class);
				startActivity(turn_Backup_and_restore);
				break;
			case R.id.look_feedback:
				Intent turn_feedback_manage = new Intent(Admin.this,feedback_manage.class);
				startActivity(turn_feedback_manage);
				break;
			case R.id.look_userreport:
				Intent turn_report_manage = new Intent(Admin.this,user_report_manage.class);
				startActivity(turn_report_manage);
				break;
			
			}
		}
		
	}
}
