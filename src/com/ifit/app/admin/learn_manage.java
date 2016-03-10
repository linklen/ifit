package com.ifit.app.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.learn_manage_adapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.learn_manage_listitem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class learn_manage extends Activity {

	private RelativeLayout add_learn;
	
	private TextView article_count;
	
	private ListView listview;
	
	private List<learn_manage_listitem> list = new ArrayList<learn_manage_listitem>();
	
	private learn_manage_adapter adapter;
	
	private learn_manage_listitem item;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	
	private int artcount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learn_manage);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		
		listview = (ListView)findViewById(R.id.learn_manager_listview);
		article_count = (TextView)findViewById(R.id.art_count);
		initlist();
		initlistview();
		
		article_count.setText(artcount+"");
		
		add_learn = (RelativeLayout)findViewById(R.id.learn_manage_add_learn);
		add_learn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(learn_manage.this,add_learn.class);
				startActivity(intent);
			}
		});
	}
	
	public void initlistview(){
		adapter = new learn_manage_adapter(this, R.layout.learn_manager_listitem, list);
		listview.setAdapter(adapter);
	}

	public void initlist(){
		Cursor cr = db.query("learn_table", null, null, null, null, null,null);
		
		artcount = cr.getCount();
		
		if(cr.moveToFirst()){
			do{
				String arttitle = cr.getString(cr.getColumnIndex("learn_title"));
				String aetid = cr.getInt(cr.getColumnIndex("learn_id"))+"";
				String imgpath = cr.getString(cr.getColumnIndex("learn_imagepath"));
				
				item = new learn_manage_listitem(aetid, arttitle, imgpath);
				list.add(item);
				
			}while(cr.moveToNext());
		}
		cr.close();
	}

	public void delete_learn(learn_manage_listitem item){
		
		String imgpath = item.get_imgpath();
		
		File parent =null;
		File file = new File(imgpath);
		if(file.exists()){
			parent = file.getParentFile();
		}
		if(parent.exists()){
		for(File i:parent.listFiles()){
			if(i.isFile()){
				i.delete();
			}
		}
		parent.delete();
		}
		db.delete("learn_table", "learn_id = ?", new String[]{item.getid()});
		
		list.remove(item);
		adapter.notifyDataSetChanged();
		
		Toast.makeText(this, "É¾³ý³É¹¦", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(list!=null){
			list.clear();
		}
		initlist();
		adapter.notifyDataSetChanged();
		
	}
	
	
}
