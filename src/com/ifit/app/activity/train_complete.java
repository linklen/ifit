package com.ifit.app.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class train_complete extends Activity {
	
	private Button complete_btn;
	private TextView plan_name;
	private TextView plan_fire;
	private TextView plan_time;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private int now_time;
	private int now_fire;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.train_complete);
		
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		complete_btn = (Button)findViewById(R.id.train_complete_btn);
		plan_name = (TextView)findViewById(R.id.train_complete_plan_name);
		plan_fire = (TextView)findViewById(R.id.train_complete_plan_fire);
		plan_time = (TextView)findViewById(R.id.train_complete_timecount);
		
		Intent getdata = getIntent();
		String name = getdata.getStringExtra("plan_name");
		String fire = getdata.getStringExtra("plan_fire");
		int second = getdata.getIntExtra("plan_time",0);
		now_time = second;
		
		plan_time.setText("你完成了你的"+name+"计划，"+"燃烧了");
		String getfire = fire.substring(0,fire.length()-2);
		now_fire = Integer.parseInt(getfire);
		
		plan_fire.setText(getfire);
		
		if(second>0){
			if(second>60){
				int minute = (int) Math.floor(second/60);
				second = second-minute*60;
				if(minute > 60){
					int hour = (int) Math.floor(minute/60);
					minute = minute-hour*60;
					plan_time.setText("减去休息时间，共锻炼了"+hour+"小时"+minute+"分"+second+"秒");
				}else{
					plan_time.setText("减去休息时间，共锻炼了"+minute+"分"+second+"秒");
				}
				
			}else{
				plan_time.setText("减去休息时间，共锻炼了"+second+"秒");
			}
			
		}else{
			plan_time.setVisibility(View.GONE);
		}
		
		complete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				operate_db(now_time,now_fire);
				finish();
			}
		});
		
		
	}

	
	public void operate_db(int now_time,int now_fire){
		Cursor cursor = db.query("User_train_record_table",
				null, "name = ?", new String[]{Home_page.UsingName},null, null, null);
		
		ContentValues values = new ContentValues();
		cursor.moveToFirst();
		if(cursor.getCount()>0){
			
			String dayday = cursor.getString(cursor.getColumnIndex("Dayday"));
			
			int daytime=0;
			int dayfire=0;
			if(dayday.equals(getdaytime())){
			daytime = cursor.getInt(cursor.getColumnIndex("Daytime"));
			dayfire = cursor.getInt(cursor.getColumnIndex("Dayfire"));}
		
			int countdaytime = daytime+now_time;
			int countdayfire = dayfire+now_fire;
			
			values.put("Daytime", countdaytime);
			values.put("Dayfire", countdayfire);
			values.put("Dayday", getdaytime());
			
			values.put(getWeekday(), countdaytime+","+countdayfire);
			
			db.update("User_train_record_table",values,"name = ?",new String[]{Home_page.UsingName});
			values.clear();
			
		}else{
			values.put("name", Home_page.UsingName);
			values.put("Daytime", now_time);
			values.put("Dayfire", now_fire);
			values.put("Dayday", getdaytime());
			values.put(getWeekday(), now_time+","+now_fire);
			db.insert("User_train_record_table", null, values);
			values.clear();
		}
		cursor.close();
		put_order_db();
	}
	
	public void put_order_db(){
		Cursor cursor_to_order = db.query("User_order_table",
				null, "name = ?", new String[]{Home_page.UsingName},null, null, null);
		Cursor cursor = db.query("User_train_record_table",
				null, "name = ?", new String[]{Home_page.UsingName},null, null, null);
		cursor_to_order.moveToFirst();
		cursor.moveToFirst();
		ContentValues values = new ContentValues();
		if(cursor_to_order.getCount()>0){
			
			String data = cursor.getString(cursor.getColumnIndex(getWeekday()));
			//Log.d("xxx", data);
			values.put(getWeekday(), data);
			db.update("User_order_table", values,"name = ?",new String[]{Home_page.UsingName});
			
		}else{
			values.put("name", Home_page.UsingName);
			values.put(getWeekday(), now_time+","+now_fire);
			db.insert("User_order_table", null, values);
			values.clear();
		}
		
		cursor_to_order.close();
		cursor.close();
		values.clear();
	}
	
	public String getWeekday(){
		Date date = new Date();

		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");


		String temp_weekday = dateFm.format(date);
		
		String weekday= null;

		// Log.d("xxx", dateFm.format(date));
		switch(temp_weekday){
		case "星期一":
			weekday = "mondata";
			break;
		case "星期二":
			weekday = "tuedata";
			break;
		case "星期三":
			weekday = "weddata";
			break;
		case "星期四":
			weekday = "thurdata";
			break;
		case "星期五":
			weekday = "fridata";
			break;
		case "星期六":
			weekday = "satdata";
			break;
		case "星期日":
			weekday = "sundata";
			break;
		}
		
		return weekday;
		
	}
	
	public String getdaytime(){
		Date date = new Date();
		SimpleDateFormat dateDay = new SimpleDateFormat("yyyyMMdd");
		String daytime = dateDay.format(date);
		return daytime;
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		operate_db(now_time,now_fire);
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}
	
	
}
