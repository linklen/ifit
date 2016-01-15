package com.ifit.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class user extends SQLiteOpenHelper {

	public static String CREATE_USER_TABLE = "create table User_table(" +
			"name text primary key, " +
			"password text, " +
			"phone text, " +
			"isadmin text)";
	private Context mContext;
	public user(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL("insert into User_table(name,password,phone,isadmin)values(?,?,?,?)"
				,new String[]{"admin","admin","","true"});
		Toast.makeText(mContext, "用户表创建成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
