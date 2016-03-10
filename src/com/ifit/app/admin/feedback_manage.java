package com.ifit.app.admin;

import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.feedback_manage_adapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.feedback_manage_item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class feedback_manage extends Activity {
	
	private ListView listview;
	private List<feedback_manage_item> list = new ArrayList<feedback_manage_item>();
	private feedback_manage_adapter adapter;
	private feedback_manage_item item;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_manage);
		
		listview = (ListView)findViewById(R.id.listview);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null,1);
		db = usedb.getWritableDatabase();
		
		Cursor cr = db.query("User_feedback_table", null,
				null, null, null,null,null);
		
		if(cr.moveToFirst()){
			do{
				String content = cr.getString(cr.getColumnIndex("content"));
				String contact = cr.getString(cr.getColumnIndex("contact"));
				int id = cr.getInt(cr.getColumnIndex("id"));
				item = new feedback_manage_item(content, contact,id);
				list.add(item);
			}while(cr.moveToNext());
		}
		
		adapter = new feedback_manage_adapter(this,R.layout.feedback_manage_item,list);
		listview.setAdapter(adapter);
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				feedback_manage_item item = adapter.getItem(position);
				showdialog(item.getid(),item);
				
				
				return false;
			}
		});
		cr.close();
	}

	public void showdialog(final int id,final feedback_manage_item item){
		new AlertDialog.Builder(this).setMessage("确定删除这条反馈？")
		.setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				db.delete("User_feedback_table", "id = ?", new String[]{(id+"")});
				
				
				list.remove(item);
				adapter.notifyDataSetChanged();
				
				Toast.makeText(feedback_manage.this, "删除成功",Toast.LENGTH_SHORT).show();
				
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
