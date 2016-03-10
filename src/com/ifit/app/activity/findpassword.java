package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class findpassword extends Activity implements OnClickListener{

	private EditText Einputname,Einputphone;
	private String Sinputname,Sinputphone;
	private MyDatabaseHelper usedb;
	public SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.findpassword);
		//�������ݿ�ʵ��
		usedb = new MyDatabaseHelper(this,"DataBase.db",null,1);
		db = usedb.getReadableDatabase();
		//���ֿؼ�
		Button back_button = (Button)findViewById(R.id.back_button);
		Button complete_button = (Button)findViewById(R.id.complete_button);
		Einputname = (EditText) findViewById(R.id.find_edit_user);
		Einputphone = (EditText)findViewById(R.id.find_edit_phone);
		//��ذ�ť
		back_button.setOnClickListener(this);
		complete_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back_button:
			finish();
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.complete_button:
			Sinputname = Einputname.getText().toString();
			Sinputphone = Einputphone.getText().toString();
			if(Sinputname.equals("")){
				new AlertDialog.Builder(this).setTitle("ע��")
				.setMessage("�������û���").setPositiveButton("ȷ��", null).show();}
			else{
				if( Sinputphone.equals("") ){
					new AlertDialog.Builder(this).setTitle("ע��")
					.setMessage("������ע��ʱ�ֻ���").setPositiveButton("ȷ��", null).show();
				}
				else{
					returnkey(Sinputname,Sinputphone);
				}
				
			}
			
			break;
		default:
			break;
		}
	}

	public boolean returnkey(String name,String phone){
		Cursor cursor =  db.query("User_table", 
		new String[]{"name,password,phone"}, "name = ?", 
		new String[]{name}, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount()<1){
			new AlertDialog.Builder(this).setTitle("ע��")
			.setMessage("�û���������").setPositiveButton("ȷ��", null).show();
			cursor.close();
			return false;
		}else{
			if(! cursor.getString(cursor.getColumnIndex("phone")).equals(phone)){
				new AlertDialog.Builder(this).setTitle("ע��")
				.setMessage("������ֻ����벻��Ӧ").setPositiveButton("ȷ��", null).show();
				cursor.close();
				return false;
			}else{
				String rekey = cursor.getString(cursor.getColumnIndex("password"));
				new AlertDialog.Builder(this).setTitle("ע��")
				.setMessage("��������Ϊ" + rekey).setPositiveButton("ȷ��", null).show();
				cursor.close();
				return true;
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
