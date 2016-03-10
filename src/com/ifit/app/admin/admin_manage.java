package com.ifit.app.admin;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class admin_manage extends Activity {

	
	private EditText add_new_admin_accunt,add_new_admin_password;
	private Button add_new_admin_btn;
	
	private EditText delete_admin_accunt;
	private Button delete_admin_btn;
	private TextView show_search_results;
	private Button search_admin_btn;
	
	private EditText change_password_admin_account;
	private EditText change_password_admin_new_word;
	private Button change_password_btn;
	
	private ListView listview;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	
	private String account = null;
	private String password = null;
	private String result = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_admin);
		initControl();
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		initlistview();
		
		add_new_admin_btn.setOnClickListener(new MyOnClickListen());
		search_admin_btn.setOnClickListener(new MyOnClickListen());
		delete_admin_btn.setOnClickListener(new MyOnClickListen());
		change_password_btn.setOnClickListener(new MyOnClickListen());
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String name = adapter.getItem(position);
				//Log.d("xxx", name);
				Cursor cr = db.query("User_table",
						new String[]{"password"}, "name = ?", new String[]{name},
						null, null, null);
				cr.moveToFirst();
				String password = cr.getString(cr.getColumnIndex("password"));
				
				new AlertDialog.Builder(admin_manage.this).setTitle("该管理员账号密码：")
				.setMessage("账号："+name+"\n"+"密码："+password)
				.setPositiveButton("OK", null).show();
				cr.close();
			}
		});
	}
	
	public void initlistview(){
		Cursor cr = db.query("User_table", new String[]{"name"},
				"isadmin = ?", new String[]{"true"}, null, null, null);
		if(cr.moveToFirst()){
			do{
				String admin = cr.getString(cr.getColumnIndex("name"));
				list.add(admin);
			}while(cr.moveToNext());
		}
		
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, list);
		listview.setAdapter(adapter);
		cr.close();
	}
	
	
	public void initControl(){
		add_new_admin_accunt = (EditText)findViewById(R.id.add_admin_accunt);
		add_new_admin_password = (EditText)findViewById(R.id.add_admin_password);
		add_new_admin_btn = (Button)findViewById(R.id.add_admin_btn);
		
		delete_admin_accunt = (EditText)findViewById(R.id.delete_admin_accunt);
		search_admin_btn = (Button)findViewById(R.id.search_admin_state_btn);
		show_search_results = (TextView)findViewById(R.id.show_result);
		delete_admin_btn = (Button)findViewById(R.id.delete_admin_btn);
		
		change_password_admin_account = (EditText)findViewById(R.id.change_admin_accunt);
		change_password_admin_new_word = (EditText)findViewById(R.id.admin_news_password);
		change_password_btn = (Button)findViewById(R.id.change_admin_password_btn);
		
		listview = (ListView)findViewById(R.id.show_admin_listview);
		
	}
	

	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.add_admin_btn:
				
				account = add_new_admin_accunt.getText().toString();
				password = add_new_admin_password.getText().toString();
				//Log.d("xxx", password);
				if(account.equals("")||password.equals("")){
					new AlertDialog.Builder(admin_manage.this).setMessage("请填写完整！")
					.setPositiveButton("OK", null).show();
				}else{
					
					if(list.contains(account)){
						new AlertDialog.Builder(admin_manage.this).setMessage("当前管理员已经存在，请尝试其他账号名！")
						.setPositiveButton("OK", null).show();
					}else{
						
						if(account.length()<5){
							new AlertDialog.Builder(admin_manage.this).setMessage("请使用固定格式,头五个字母为admin！")
							.setPositiveButton("OK", null).show();
						}else{
						
						if(!account.substring(0,5).equals("admin")){
							new AlertDialog.Builder(admin_manage.this).setMessage("请使用固定格式,头五个字母为admin！")
							.setPositiveButton("OK", null).show();
						}else{
						ContentValues values = new ContentValues();
						values.put("name",account);
						values.put("password",password);
						values.put("isadmin", "true");
						db.insert("User_table", null, values);
						values.clear();
						
						list.add(account);
						adapter.notifyDataSetChanged();
						
						add_new_admin_accunt.setText("");
						add_new_admin_password.setText("");
						
						Toast.makeText(admin_manage.this, "添加成功", Toast.LENGTH_SHORT).show();}}
					}
					
				}
				
				break;
			case R.id.search_admin_state_btn:
				
				account = delete_admin_accunt.getText().toString();
				
				if(account.equals("")){
					new AlertDialog.Builder(admin_manage.this).setMessage("请输入查询的对象!")
					.setPositiveButton("OK", null).show();
				}else{
				
				Cursor cr = db.query("User_table", 
						new String[]{"name"}, "name = ? and isadmin = ?", new String[]{account,"true"},
						null, null, null);
				if(cr.getCount()>0){
					show_search_results.setText("存在");
				}else{
					show_search_results.setText("不存在");
				}
				
				cr.close();}
				break;
			case R.id.delete_admin_btn:
				
				account = delete_admin_accunt.getText().toString();
				result = show_search_results.getText().toString();
				
				new AlertDialog.Builder(admin_manage.this).setTitle("警告！")
				.setMessage("确定要删除该管理员吗？").setNegativeButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(result.equals("")){
							new AlertDialog.Builder(admin_manage.this).setMessage("请先查询！")
							.setPositiveButton("OK", null).show();
						}else if(result.equals("不存在")){
							new AlertDialog.Builder(admin_manage.this).setMessage("请先查询！")
							.setPositiveButton("OK", null).show();
						}else{
							db.delete("User_table","name = ?",new String[]{account});
							
							list.remove(account);
							adapter.notifyDataSetChanged();
							
							delete_admin_accunt.setText("");
							show_search_results.setText("");
							
							Toast.makeText(admin_manage.this, "删除成功", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setPositiveButton("否", null).show();
				
				break;
			case R.id.change_admin_password_btn:
				
				new AlertDialog.Builder(admin_manage.this).setMessage("修改密码？")
				.setNegativeButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						account = change_password_admin_account.getText().toString();
						String newspassword = change_password_admin_new_word.getText().toString();
						
						
						if(account.equals("")||newspassword.equals("")){
							new AlertDialog.Builder(admin_manage.this).setMessage("请填写完整！")
							.setPositiveButton("OK", null).show();
						}else{
						
						Cursor cr = db.query("User_table",
								null, "name = ? and isadmin = ?", new String[]{account,"true"},
								null, null, null);
						
						if(cr.getCount()==0){
							new AlertDialog.Builder(admin_manage.this).setMessage("该管理员不存在！")
							.setPositiveButton("OK", null).show();
						}else{
							ContentValues values = new ContentValues();
							values.put("password", newspassword);
							db.update("User_table", values, "name = ?", new String[]{account});
							
							change_password_admin_account.setText("");
							change_password_admin_new_word.setText("");
							
							Toast.makeText(admin_manage.this, "修改成功", Toast.LENGTH_SHORT).show();
						}
						}
					}
				})
				.setPositiveButton("否", null).show();
				
				break;
			}
		}
		
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}
	
	
}
