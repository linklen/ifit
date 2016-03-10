package com.ifit.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	public static String CREATE_USER_TABLE = "create table User_table(" +
			"name text primary key, " +
			"password text, " +
			"phone text, " +
			"isadmin text, " +
			"isnew text)";
	public static String CREATE_USER_PERSONAL_INFO_TABLE = "create table User_personal_info_table(" +
			"name text primary key, " +
			"nickname text, " +
			"sex text, " +
			"age integer, " +
			"region text, " +
			"introduction text)";
	public static String CREATE_USER_BODY_DATA_TABLE ="create table User_body_data_table(" +
			"name text primary key, " +
			"height integer, " +
			"weight integer, " +
			"waist integer, " +
			"experience text, " +
			"purpose text)";
	public static String CREATE_USER_HEADIMAGE_TABLE ="create table User_headImage_table(" +
			"name text primary key, " +
			"user_head_img blob)";
	public static String CREATE_USER_FEEDBACK_TABLE = "create table User_feedback_table(" +
			"id integer primary key autoincrement, " +
			"content text, " +
			"contact text)";
	public static String CREATE_USER_NEWS_TABLE = "create table User_news_table(" +
			"news_id integer primary key autoincrement, " +
			"time text, " +
			"imagepath_1 text, " +
			"imagepath_2 text, " +
			"imagepath_3 text, " +
			"imagepath_4 text, " +
			"words text, " +
			"name text)";
	
	public static String CREATE_USER_NEWS_GOOD_TABLE = "create table User_news_good_table(" +
			"news_id integer, " +
			"goodname text)";//赞的人，用分隔号分开，读取的时候解析
			//评论的内容，包括时间，人名
			
	public static String CREATE_USER_NEWS_MARK_TABLE = "create table User_news_mark_table(" +
			"news_id integer, " +
			"marktitle text, " +
			"marktext text, " +
			"markname text, " +
			"marktime text)";
	
	public static String CREATE_LEARN_TABLE = "create table learn_table(" +
			"learn_id integer primary key autoincrement, " +
			"learn_title text, " +
			"learn_imagepath text, " +
			"learn_url text)";
	
	public static String CREATE_USER_TRAIN_RECORD_TABLE = "create table User_train_record_table(" +
			"name text, " +
			"Daytime integer, " +
			"Dayfire integer, " +
			"Dayday text, " +
			"mondata text, " +
			"tuedata text, " +
			"weddata text, " +
			"thurdata text, " +
			"fridata text, " +
			"satdata text, " +
			"sundata text)";
	
	public static String CREATE_USER_ORDER_TABLE = " create table User_order_table(" +
			"name text, " +
			"weektraintime integer, " +
			"rank text, "+
			"mondata text, " +
			"tuedata text, " +
			"weddata text, " +
			"thurdata text, " +
			"fridata text, " +
			"satdata text, " +
			"sundata text)";
	
	public static String CREATE_USER_REPORT_TABLE = "create table User_report_table(" +
			"news_id text, " +
			"type text, " +
			"content text)";
	
	private Context mContext;
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_USER_PERSONAL_INFO_TABLE);
		db.execSQL(CREATE_USER_BODY_DATA_TABLE);
		db.execSQL(CREATE_USER_HEADIMAGE_TABLE);
		db.execSQL(CREATE_USER_FEEDBACK_TABLE);
		db.execSQL(CREATE_USER_NEWS_TABLE);
		db.execSQL(CREATE_USER_NEWS_GOOD_TABLE);
		db.execSQL(CREATE_USER_NEWS_MARK_TABLE);
		db.execSQL(CREATE_USER_TRAIN_RECORD_TABLE);
		db.execSQL(CREATE_USER_ORDER_TABLE);
		db.execSQL(CREATE_LEARN_TABLE);
		db.execSQL(CREATE_USER_REPORT_TABLE);
		db.execSQL("insert into User_table(name,password,phone,isadmin)values(?,?,?,?)"
				,new String[]{"admin","admin","","true"});
		Toast.makeText(mContext, "用户表创建成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		switch(oldVersion){
		case 1:
			db.execSQL(CREATE_LEARN_TABLE);
		}
	}

}
