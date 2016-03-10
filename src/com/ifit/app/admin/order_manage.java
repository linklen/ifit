package com.ifit.app.admin;

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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class order_manage extends Activity {

	private Button count;
	private Button order;
	private Button reset;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_manage);
		
		count = (Button)findViewById(R.id.count);
		order = (Button)findViewById(R.id.order);
		reset = (Button)findViewById(R.id.reset);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		count.setOnClickListener(new AdminClickListen());
		order.setOnClickListener(new AdminClickListen());
		reset.setOnClickListener(new AdminClickListen());
		
	}

	class AdminClickListen implements OnClickListener{
		
		
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.count:
				count();
				break;
			case R.id.order:
				new AlertDialog.Builder(order_manage.this).setMessage("是否已经总计过？")
				.setNegativeButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						order();
					}
				})
				.setPositiveButton("否", null).show();
				break;
			case R.id.reset:
				new AlertDialog.Builder(order_manage.this).setMessage("确认重置数据？")
				.setNegativeButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						reset();
					}
				})
				.setPositiveButton("否", null).show();
				break;
			}
		}
	}

	
	public void count(){
		Cursor cr = db.query("User_order_table",null,null, null, null, null, null);
		db.beginTransaction();
		if(cr.moveToFirst()){
			do{
				
				String name = cr.getString(cr.getColumnIndex("name"));
				
				String mondata = cr.getString(cr.getColumnIndex("mondata"));
				if(mondata==null){
					mondata = "0,0";
				}
				String montime = mondata.split(",")[0];
				String tuedata = cr.getString(cr.getColumnIndex("tuedata"));
				if(tuedata==null){
					tuedata = "0,0";
				}
				String tuetime = tuedata.split(",")[0];
				String weddata = cr.getString(cr.getColumnIndex("weddata"));
				if(weddata==null){
					weddata = "0,0";
				}
				String wedtime = weddata.split(",")[0];
				String thurdata = cr.getString(cr.getColumnIndex("thurdata"));
				if(thurdata==null){
					thurdata = "0,0";
				}
				String thurtime = thurdata.split(",")[0];
				String fridata = cr.getString(cr.getColumnIndex("fridata"));
				if(fridata==null){
					fridata = "0,0";
				}
				String fritime = fridata.split(",")[0];
				String satdata = cr.getString(cr.getColumnIndex("satdata"));
				if(satdata==null){
					satdata = "0,0";
				}
				String sattime = satdata.split(",")[0];
				String sundata = cr.getString(cr.getColumnIndex("sundata"));
				if(sundata==null){
					sundata = "0,0";
				}
				String suntime = sundata.split(",")[0];
				
				int weektime = Integer.parseInt(montime)+Integer.parseInt(tuetime)+
						Integer.parseInt(wedtime)+Integer.parseInt(thurtime)+
						Integer.parseInt(fritime)+Integer.parseInt(sattime)+
						Integer.parseInt(suntime);
				
				ContentValues values = new ContentValues();
				values.put("weektraintime", weektime);
				db.update("User_order_table", values, "name = ?", new String[]{name});
				values.clear();
			}while(cr.moveToNext());
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		Toast.makeText(this, "总计完成", Toast.LENGTH_SHORT).show();
		cr.close();
		
	}
	
	public void order(){
		Cursor cr = db.query("User_order_table",null,null, null, null, null, "weektraintime DESC");
		db.beginTransaction();
		if(cr.moveToFirst()){
			int i = 1;
			do{
				String name = cr.getString(cr.getColumnIndex("name"));
				//Log.d("xxx", name+"132");
				ContentValues values = new ContentValues();
				values.put("rank", i+"");
				//Log.d("xxx", values.toString());
				db.update("User_order_table", values, "name = ?",new String[]{name});
				i++;
				values.clear();
			}while(cr.moveToNext());
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		cr.close();
		Toast.makeText(this, "排序完成", Toast.LENGTH_SHORT).show();
	}
	
	public void reset(){
		
		db.beginTransaction();
		String x = "0,0";
		ContentValues values = new ContentValues();
		values.put("mondata", x);
		values.put("tuedata", x);
		values.put("weddata", x);
		values.put("thurdata", x);
		values.put("fridata", x);
		values.put("satdata", x);
		values.put("sundata", x);
		db.update("User_order_table", values, null, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		values.clear();
		Toast.makeText(this, "重置成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}
	
	
}
