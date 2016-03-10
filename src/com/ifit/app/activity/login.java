package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Activity implements OnClickListener{

	private EditText Einputname,Einputkey;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	public int click_back_count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		//���ݿ�ʵ����
		usedb = new MyDatabaseHelper(this,"DataBase.db",null,1);
		db = usedb.getWritableDatabase();
		//���ֿؼ�
		TextView turn_findpassword = (TextView)findViewById(R.id.turn_findpassword);
		TextView turn_regist = (TextView)findViewById(R.id.turn_regist);
		Button login_btn = (Button) findViewById(R.id.login_button);
		Einputname = (EditText)findViewById(R.id.login_edit_user);
		Einputkey = (EditText)findViewById(R.id.login_edit_key);

		
		
		//�ؼ�����
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
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.turn_regist :
			Intent turnregist = new Intent(this,registe.class);
			startActivity(turnregist);
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			finish();
			break;
		case R.id.login_button:
			String Sinputname = Einputname.getText().toString();
			String Sinputkey = Einputkey.getText().toString();
			if(Sinputname.equals("")){
				new AlertDialog.Builder(this).setTitle("ע��")
				.setMessage("�������û���").setPositiveButton("ȷ��", null).show();}
			else{
				if( Sinputkey.equals("") ){
					new AlertDialog.Builder(this).setTitle("ע��")
					.setMessage("����������").setPositiveButton("ȷ��", null).show();
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
		
		//�ж��Ƿ���ע�����ص�
				Intent isback = getIntent();
				boolean is_back = isback.getBooleanExtra("is_back", true);
		
		Cursor cursor =  db.query("User_table", 
		new String[]{"name,password,isadmin,isnew"}, "name = ?", 
		new String[]{name}, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount()<1){
			new AlertDialog.Builder(this).setTitle("ע��").setMessage("�Բ��𣬸��û�������")
			.setPositiveButton("ȷ��", null).show();
			cursor.close();
			}else{
				if(!cursor.getString(cursor.getColumnIndex("password")).equals(key)){
					new AlertDialog.Builder(this).setTitle("ע��").setMessage("���벻��ȷ")
					.setPositiveButton("ȷ��", null).show();
					cursor.close();
				}else{
					if(cursor.getString(cursor.getColumnIndex("isadmin")).equals("true")){
						Intent adminlogin = new Intent(this,Admin.class);
						adminlogin.putExtra("adminname", name);
						startActivity(adminlogin);
						if(!is_back){
						loginfirst.loginfirst_instance.finish();
						}
						finish();
					}else{
						//�SharedPreference
						SharedPreferences.Editor islogin = 
								getSharedPreferences("islogin", MODE_PRIVATE).edit();
						islogin.putString("user",name);
						islogin.putBoolean("login_in", true);
						islogin.commit();
						//�Intent����������
						Intent userlogin = new Intent(this,Home_page.class);
						if(cursor.getString(cursor.getColumnIndex("isnew")).equals("true")){
							userlogin.putExtra("is_new", true);
							ContentValues values = new ContentValues();
							values.put("isnew", "false");
							db.update("User_table", values, "name = ?", new String[]{name});
						}
						userlogin.putExtra("the_user_name", name);
						userlogin.putExtra("is_send", true);
						startActivity(userlogin);
						if(!is_back){
						loginfirst.loginfirst_instance.finish();
						}
						finish();
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
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
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
