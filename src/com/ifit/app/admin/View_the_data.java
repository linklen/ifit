package com.ifit.app.admin;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class View_the_data extends Activity {

	private TextView user_count,feedback_count,report_count,dynamic_count;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_statistics);
		
		user_count = (TextView)findViewById(R.id.user_count);
		feedback_count = (TextView)findViewById(R.id.feedback_count);
		report_count = (TextView)findViewById(R.id.report_count);
		dynamic_count = (TextView)findViewById(R.id.dynamic_count);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		Cursor cr = db.query("User_table", null, null, null, null, null, null);
		
		int i = cr.getCount();
		user_count.setText(i+"");
		
		cr = db.query("User_feedback_table", null, null, null, null, null, null);
		
		int n = cr.getCount();
		feedback_count.setText(n+"");
		
		cr = db.query("User_report_table", null, null, null, null, null, null);
		
		int m = cr.getCount();
		report_count.setText(m+"");
		
		cr = db.query("User_news_table", null, null, null, null, null, null);
		int x = cr.getCount();
		dynamic_count.setText(x+"");
		
		cr.close();
		db.close();
		
		
	}
	
	

}
