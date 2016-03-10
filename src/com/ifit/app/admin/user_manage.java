package com.ifit.app.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class user_manage extends Activity {
	
	private EditText search_edit;
	private Button search_btn;
	
	private ListView listview;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	private EditText user_accunt_show;
	private EditText user_password_show;
	private EditText user_phone_show;
	private Button save_info_btn;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_user);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		initContral();
		intilistView();
		
		search_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = search_edit.getText().toString();
				
				if(list.contains(name)){
				listview.setSelection(adapter.getPosition(name));}
				else{
					Toast.makeText(user_manage.this, "该用户不存在！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		save_info_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String name = user_accunt_show.getText().toString();
				String password = user_password_show.getText().toString();
				String phone = user_phone_show.getText().toString();
				if(name.equals("")||password.equals("")||phone.equals("")){
					Toast.makeText(user_manage.this, "请填写完整信息！", Toast.LENGTH_SHORT).show();
				}else if(!list.contains(name)){
					Toast.makeText(user_manage.this, "该用户不存在，请核查！", Toast.LENGTH_SHORT).show();
				}else if(phone.length() != 11){
					Toast.makeText(user_manage.this, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
				}else{
					updata_info(name,password,phone);
				}
				
			}
		});
	}
	
	public void updata_info(final String name,final String password,final String phone){
		new AlertDialog.Builder(user_manage.this).setMessage("确认将该用户信息更新？")
		.setNegativeButton("是",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ContentValues values = new ContentValues();
				values.put("password", password);
				values.put("phone", phone);
				db.update("User_table", values, "name = ?", new String[]{name});
				Toast.makeText(user_manage.this, "更新成功！", Toast.LENGTH_SHORT).show();
			}
		}).setPositiveButton("再改改", null).show();
	}
	
	public void initContral(){
		search_edit = (EditText)findViewById(R.id.search_name);
		search_btn = (Button)findViewById(R.id.search_btn);
		
		listview = (ListView)findViewById(R.id.show_user_listview);
		
		user_accunt_show = (EditText)findViewById(R.id.show_user_account);
		user_password_show = (EditText)findViewById(R.id.show_user_password);
		user_phone_show = (EditText)findViewById(R.id.show_user_phone);
		save_info_btn = (Button)findViewById(R.id.save_info_btn);
	}
	

	public void intilistView(){
		Cursor cr = db.query("User_table", null,
				"isadmin = ?", new String[]{"false"},null, null, null);
		
		if(cr.moveToFirst()){
			do{
				String name = cr.getString(cr.getColumnIndex("name"));
				list.add(name);
			}while(cr.moveToNext());
		}
		
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, list);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String name = adapter.getItem(position);
				Cursor cr = db.query("User_table",
						null,"name = ?", new String[]{name}, null, null, null);
				cr.moveToFirst();
				String password = cr.getString(cr.getColumnIndex("password"));
				String phone = cr.getString(cr.getColumnIndex("phone"));
				
				user_accunt_show.setText(name);
				user_password_show.setText(password);
				user_phone_show.setText(phone);
				
				cr.close();
				
				
			}
		});
		
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String name = adapter.getItem(position);
				delete_all_info(name);
				
				return false;
			}
		});
		
		cr.close();
	}
	
	
	public void delete_all_info(final String name){
		new AlertDialog.Builder(user_manage.this).setMessage("确认删除该用户所有信息？")
		.setNegativeButton("是",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				db.beginTransaction();
				try{
				db.delete("User_body_data_table", "name = ?", new String[]{name});
				db.delete("User_headImage_table", "name = ?", new String[]{name});
				db.delete("User_news_table", "name = ?", new String[]{name});
				db.delete("User_order_table", "name = ?", new String[]{name});
				db.delete("User_personal_info_table", "name = ?", new String[]{name});
				db.delete("User_table", "name = ?", new String[]{name});
				db.delete("User_train_record_table", "name = ?", new String[]{name});
				
				//此处应该删除服务器上内容
				File sdcard = Environment.getExternalStorageDirectory();
				File directory = new File(sdcard.getAbsolutePath()+
						 "/ifit/dbfile/");
				
				findfile(directory,name); //查找文件夹后进行删除
				
				list.remove(name);
				adapter.notifyDataSetChanged();
				
				user_accunt_show.setText("");
				user_password_show.setText("");
				user_phone_show.setText("");
				
				db.setTransactionSuccessful();
				
				Toast.makeText(user_manage.this, "删除成功！", Toast.LENGTH_SHORT).show();
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(user_manage.this, "删除失败，请重试！", Toast.LENGTH_SHORT).show();
				}finally{
					db.endTransaction();
				}
			}
		}).setPositiveButton("否", null).show();
	}
	
	
	public void findfile(File file,String name){
		
			for(File i : file.listFiles()){
				if(i.isDirectory()){
					if(i.getName().equals(name)){
						deletefile(i);
					}else{
						findfile(i,name);
					}
				}
			}
		
		
		
	}
	
	public void deletefile(File file){
		
		for(File i :file.listFiles()){
			if(i.isFile()){
				i.delete();
			}else{
				deletefile(i);
			}
		}
		
		file.delete();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}
	
	
	
}
