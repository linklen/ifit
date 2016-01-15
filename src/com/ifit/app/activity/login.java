package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.db.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends Activity implements OnClickListener{

	private EditText Einputname,Einputkey;
	private user usedb;
	private SQLiteDatabase db;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		//数据库实例化
		usedb = new user(this,"User_table.db",null,1);
		db = usedb.getWritableDatabase();
		//发现控件
		TextView turn_findpassword = (TextView)findViewById(R.id.turn_findpassword);
		TextView turn_regist = (TextView)findViewById(R.id.turn_regist);
		Button login_btn = (Button) findViewById(R.id.login_button);
		Einputname = (EditText)findViewById(R.id.login_edit_user);
		Einputkey = (EditText)findViewById(R.id.login_edit_key);
		//控件监听
		turn_findpassword.setOnClickListener(this);
		turn_regist.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.turn_findpassword:
			Intent turnfindpassword = new Intent(this,findpassword.class);
			startActivityForResult(turnfindpassword,0);
			break;
		case R.id.turn_regist :
			Intent turnregist = new Intent(this,registe.class);
			startActivity(turnregist);
			finish();
			break;
		case R.id.login_button:
			boolean i=false;
			String Sinputname = Einputname.getText().toString();
			String Sinputkey = Einputkey.getText().toString();
			if(Sinputname.equals("")){
				new AlertDialog.Builder(this).setTitle("注意")
				.setMessage("请输入用户名").setPositiveButton("确定", null).show();}
			else{
				if( Sinputkey.equals("") ){
					new AlertDialog.Builder(this).setTitle("注意")
					.setMessage("请输入密码").setPositiveButton("确定", null).show();
				}
				else{
					  isUserInfo(Sinputname,Sinputkey);
				}
				
			}
			break;
	    default:
			break;
			
		}
		
	}
	
	public  void isUserInfo(String name, String key){
		Cursor cursor =  db.query("User_table", 
		new String[]{"name,password,isadmin"}, "name = ?", 
		new String[]{name}, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount()<1){
			new AlertDialog.Builder(this).setTitle("注意").setMessage("对不起，该用户不存在")
			.setPositiveButton("确定", null).show();
			cursor.close();
			}else{
				if(!cursor.getString(cursor.getColumnIndex("password")).equals(key)){
					new AlertDialog.Builder(this).setTitle("注意").setMessage("密码不正确")
					.setPositiveButton("确定", null).show();
					cursor.close();
				}else{
					if(cursor.getString(cursor.getColumnIndex("isadmin")).equals("true")){
						Intent adminlogin = new Intent(this,registe.class);
						startActivity(adminlogin);
						finish();
					}else{
						Intent userlogin = new Intent(this,findpassword.class);
						startActivity(userlogin);
					}
				}
			}
		}	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}
	
	
}
