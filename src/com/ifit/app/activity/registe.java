package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.db.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
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


public class registe extends Activity implements OnClickListener {

	private EditText Einputname,Einputkey,Einputphone,Einputconkey;
	private String Sinputname,Sinputkey,Sinputconkey,Sinputphone;
	private user usedb;
	public SQLiteDatabase db;
	public int click_back_count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registered);
		//���ݿ�ʵ����
		usedb = new user(this,"User_table.db",null,1);
		db = usedb.getWritableDatabase();
		//�ҵ������齨
		TextView turn_login = (TextView)findViewById(R.id.turn_login);
		TextView turn_findpassword = (TextView)findViewById(R.id.turn_findpassword);
		Button registe = (Button)findViewById(R.id.regist_button);
		Einputname = (EditText)findViewById(R.id.registed_edit_user);
		Einputkey = (EditText)findViewById(R.id.registed_edit_key);
		Einputphone = (EditText)findViewById(R.id.registed_edit_phone);
		Einputconkey = (EditText)findViewById(R.id.registed_edit_conkey);
		//��ť���
		turn_login.setOnClickListener(this);
		registe.setOnClickListener(this);
		turn_findpassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.turn_login:
			Intent intent1 = new Intent (this,login.class);
			startActivity(intent1);
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			finish();
			break;
		case R.id.turn_findpassword:
			Intent intent2 = new Intent (this,findpassword.class);
			startActivityForResult(intent2,1);
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.regist_button:
			//������װ���ַ�����
			Sinputname = Einputname.getText().toString();
			Sinputkey = Einputkey.getText().toString();
			Sinputphone = Einputphone.getText().toString();
			Sinputconkey = Einputconkey.getText().toString();
			boolean i = false;
			if(Sinputname.equals("")){
				new AlertDialog.Builder(this).setTitle("ע��")
				.setMessage("�������û���").setPositiveButton("ȷ��", null).show();}
			else{
				if(Sinputphone.equals("") || Sinputphone.length() != 11){
					new AlertDialog.Builder(this).setTitle("ע��")
					.setMessage("��������ȷ���ֻ���").setPositiveButton("ȷ��", null).show();
				}else{
					if( Sinputkey.equals("") ){
						new AlertDialog.Builder(this).setTitle("ע��")
						.setMessage("����������").setPositiveButton("ȷ��", null).show();
					}
					else{
						if(Sinputconkey.equals("") || !Sinputconkey.equals(Sinputkey)){
							new AlertDialog.Builder(this).setTitle("ע��")
							.setMessage("�����������벻һ��").setPositiveButton("ȷ��", null).show();
						}
						else{
							 i = isUserexist(Sinputname,Sinputphone);
						    }
						
					}
				}
			}
			if(i){
				addUser(Sinputname,Sinputkey,Sinputphone);
			}
			break;
		default:
			break;
		}
	}

	public boolean isUserexist (String name,String phone){
		Cursor cursor = db.query("User_table", 
				new String[]{"name"}, "name = ?", new String[]{name}, null, null, null);
		if(cursor.getCount()>0){
			new AlertDialog.Builder(this).setTitle("ע��")
			.setMessage("�û����Ѿ���ע��").setPositiveButton("ȷ��", null).show();
			cursor.close();
			return false;
		}else{
			cursor = db.query("User_table", 
			new String[]{"phone"}, "phone = ?", new String[]{phone}, null, null, null);
			if(cursor.getCount()>0){
				new AlertDialog.Builder(this).setTitle("ע��")
				.setMessage("���ֻ��ѱ�ʹ��").setPositiveButton("ȷ��", null).show();
				cursor.close();
				return false;
			}else{
				new AlertDialog.Builder(this).setTitle("��ϲ")
				.setMessage("ע��ɹ�").setPositiveButton("ȷ��", null).show();
				return true;
			}
		}
			
	}
	
	public void addUser (String addname,String addpassword,String addphone){
		ContentValues values = new ContentValues();
		values.put("name", addname);
		values.put("password", addpassword);
		values.put("phone", addphone);
		values.put("isadmin", "false");
		values.put("isnew", "true");
		db.insert("User_table", null, values);
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
