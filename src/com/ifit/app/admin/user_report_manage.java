package com.ifit.app.admin;

import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.user_report_manage_adapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.user_report_manage_item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class user_report_manage extends Activity {
	
	private ListView listview;
	private List<user_report_manage_item> list = new ArrayList<user_report_manage_item>();
	private user_report_manage_adapter adapter;
	private user_report_manage_item item;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_report_manage);
		listview = (ListView)findViewById(R.id.listview);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null,1);
		db = usedb.getWritableDatabase();
		
		Cursor cr = db.query("User_report_table", null,
				null, null, null,null,null);
		
		if(cr.moveToFirst()){
			do{
				String content = cr.getString(cr.getColumnIndex("content"));
				String type = cr.getString(cr.getColumnIndex("type"));
				String id = cr.getString(cr.getColumnIndex("news_id"));
				item = new user_report_manage_item(content, type,id);
				list.add(item);
			}while(cr.moveToNext());
		}
		adapter = new user_report_manage_adapter(this, R.layout.user_report_manage_item, list);
		listview.setAdapter(adapter);
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				user_report_manage_item item = adapter.getItem(position);
				showdialog(item);
				
				
				return false;
			}
		});
		
		
		
		cr.close();
		
	}
	public void showdialog(final user_report_manage_item item){
		new AlertDialog.Builder(this).setMessage("确定删除这条举报？")
		.setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				String news_id = item.getid();
				String type = item.gettype();
				String content = item.getcontent();
				
				db.delete("User_report_table", "news_id = ? and type = ? and content =?", 
						new String[]{news_id,type,content});
				
				
				list.remove(item);
				adapter.notifyDataSetChanged();
				
				Toast.makeText(user_report_manage.this, "删除成功",Toast.LENGTH_SHORT).show();
				
			}
		}).setPositiveButton("否", null).show();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}
	

	
}
