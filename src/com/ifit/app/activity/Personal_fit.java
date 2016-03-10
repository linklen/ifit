package com.ifit.app.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ifit.app.R;
import com.ifit.app.adapter.MyPagerAdapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.RoundedCornersDrawable;
import com.ifit.app.other.MyPageTransformer;
import com.ifit.app.other.UnzipAssets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Personal_fit extends Activity {

	private ViewPager fit_viewpager;
	//public int viewpager_W,viewpager_H;
	public List<View> list_View;
	private View custom_plan,custom_plan_second,
				 person_rank,person_rank_second,
	             //limit_challenge,limit_challenge_second,
	             sport_record,sport_record_second,
	             week_train,week_train_second;
	private ImageView personal_fit_cycle;
	private TextView personal_fit_text;
	//public  String getText;
	private RelativeLayout personal_fit_layout;
	private LayoutInflater inflater;
	private Bitmap bitmap;
	private static boolean istraverse = false;
	private boolean istrain = false;
	
	ImageView btn_setting;
	ImageView btn_setting_second;
	
	TextView weekday;
	TextView weekday_second;
	
	TextView train_info;
	TextView train_info_second;
	
	TextView dayfire;
	TextView dayfire_second;
	
	TextView daytime;
	TextView daytime_second;
	//设置头像昵称
	public File sdCard,directory,location_image;
	private ImageView title_user_headimg;
	private TextView title_user_nickname;
	private SharedPreferences locationdate;//获取和建立文件
	private Bitmap head_bitmap;
	
	public File getSd,getpath;
	
	private ExecutorService excutorService= 
			Executors.newFixedThreadPool(10);
	private final Handler handler = new Handler();
	private int recId[] = new int[]{R.drawable.custom_plan_bg,
									R.drawable.custom_plan_bg,
		    						R.drawable.person_rank_card_bg,
		    						R.drawable.person_rank_card_bg,
		    						//R.drawable.limit_challenge_bg,
		    						//R.drawable.limit_challenge_bg,
		    						R.drawable.sport_record_bg,
		    						R.drawable.sport_record_bg,
		    						R.drawable.week_train_bg,
		    						R.drawable.week_train_bg};
	
	//public static Map<String,SoftReference<Bitmap>> imgbgCache 
	//			= new HashMap<String,SoftReference<Bitmap>>();
	public static Map<String,Bitmap> imgbgCache 
	= new HashMap<String,Bitmap>();
	
	private int pagerW,pagerH;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_fit);
		
		findId();
		
		setListener();
		
		Init_background();
		
		Init_list_View();
		
		set_adapt();
		
		//set_title_data();
		
		Unzip();//解压图片文件
		
		fit_viewpager.setPageTransformer(true, new MyPageTransformer());
	}
	
	
	
	
	public void findId(){
		inflater = getLayoutInflater();
		personal_fit_layout = (RelativeLayout)findViewById(R.id.personal_fit_layout);
		fit_viewpager = (ViewPager)findViewById(R.id.fit_viewpager);
		custom_plan = inflater.inflate(R.layout.custom_plan, null);
		custom_plan_second = inflater.inflate(R.layout.custom_plan, null);
		person_rank = inflater.inflate(R.layout.person_rank_card, null);
		person_rank_second = inflater.inflate(R.layout.person_rank_card, null);
		//limit_challenge = inflater.inflate(R.layout.limit_challenge, null);
		//limit_challenge_second = inflater.inflate(R.layout.limit_challenge, null);
		sport_record = inflater.inflate(R.layout.sport_record, null);
		sport_record_second = inflater.inflate(R.layout.sport_record, null); 
		week_train = inflater.inflate(R.layout.week_train, null);
		week_train_second = inflater.inflate(R.layout.week_train, null);
		personal_fit_cycle = (ImageView)findViewById(R.id.personal_fit_cycle);
		personal_fit_text = (TextView)findViewById(R.id.personal_fit_Text);
		
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.for_measure);//与viewpager重叠的控件
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		layout.measure(w,h);
		pagerW = layout.getMeasuredWidth();
		pagerH = layout.getMeasuredHeight();
		
		
		set_weektrain_card_Contral();
		set_sportrecord_card_Contral();
		
	}
	
	public void set_sportrecord_card_Contral(){
		dayfire = (TextView)sport_record.findViewById(R.id.today_fire_count);
		daytime = (TextView)sport_record.findViewById(R.id.today_time_count);
		
		dayfire_second = (TextView)sport_record_second.findViewById(R.id.today_fire_count);
		daytime_second= (TextView)sport_record_second.findViewById(R.id.today_time_count);
		get_day_data();
	}
	
	public void get_day_data(){ //并设置textview   //检测今天的日期与当前日期对不对的上，对不上进行重置
		
		int dayfire_count = 0;
		int daytime_count = 0;
		
		MyDatabaseHelper usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		SQLiteDatabase db = usedb.getWritableDatabase();
		
		Cursor exist = db.query("User_train_record_table", 
				null, "name = ?", new String[]{Home_page.UsingName}, null, null, null);
		if(exist.getCount()==0){
			ContentValues v = new ContentValues();
			v.put("name", Home_page.UsingName);
			v.put("Dayday", "0");
			db.insert("User_train_record_table", null, v);
			v.clear();
			v.put("name", Home_page.UsingName);
			db.insert("User_order_table", null, v);
			v.clear();
		}
		
		
		Cursor cr = db.query("User_train_record_table", 
				null, "name = ?", new String[]{Home_page.UsingName}, null, null, null);
		
		cr.moveToFirst();
		Log.d("xxx", "1");
		String dayday = cr.getString(cr.getColumnIndex("Dayday"));
		Log.d("xxx", "12"+dayday);
		if(dayday .equals(getdaytime()) ){
			dayfire_count = cr.getInt(cr.getColumnIndex("Dayfire"));
			daytime_count = cr.getInt(cr.getColumnIndex("Daytime"));
		}else{
			
			ContentValues values = new ContentValues();
			checktime(db,cr,values);
			
			
			values.put("Dayday", getdaytime());
			values.put("Daytime", 0);
			values.put("Dayfire", 0);
			values.put(getWeekday_DB(), "0,0");
			db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
			values.clear();
		}
		
		//Log.d("xxx", dayfire_count+""+123);
		
		dayfire.setText(""+dayfire_count);
		dayfire_second.setText(""+dayfire_count);
		
		int minute = (int) Math.floor(daytime_count/60);
		
		daytime.setText(""+minute);
		daytime_second.setText(""+minute);
		
		
		cr.close();
		exist.close();
		db.close();
	}
	
	public void checktime(SQLiteDatabase db,Cursor cr,ContentValues values){//检测今天离前一次锻炼有多久
		String db_dayday = cr.getString(cr.getColumnIndex("Dayday"));
		String get_dayday = getdaytime();
		
		int x = Integer.parseInt(get_dayday)-Integer.parseInt(db_dayday);
		
		switch(x){
		case 1:
			break;
		case 2:
				switch(getWeekday()){
				case "星期一":
					values.put("sundata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				case "星期二":
					values.put("mondata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				case "星期三":
					values.put("tuedata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				case "星期四":
					values.put("weddata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				case "星期五":
					values.put("thurdata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				case "星期六":
					values.put("fridata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				case "星期日":
					values.put("satdata", "0,0");
					db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
					break;
				}
			break;
		case 3:
			switch(getWeekday()){
			case "星期一":
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期二":
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期三":
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期四":
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期五":
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期六":
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期日":
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			}
			break;
		case 4:
			switch(getWeekday()){
			case "星期一":
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期二":
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期三":
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期四":
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期五":
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期六":
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期日":
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			}
			break;
		case 5:
			switch(getWeekday()){
			case "星期一":
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期二":
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期三":
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期四":
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期五":
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期六":
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期日":
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			}
			break;
		case 6:
			switch(getWeekday()){
			case "星期一":
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期二":
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期三":
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期四":
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期五":
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期六":
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期日":
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			}
			break;
		case 7:
			switch(getWeekday()){
			case "星期一":
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期二":
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期三":
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期四":
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期五":
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期六":
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期日":
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			}
			break;
		default:
			switch(getWeekday()){
			case "星期一":
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期二":
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期三":
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期四":
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期五":
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期六":
				values.put("satdata", "0,0");
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			case "星期日":
				values.put("sundata", "0,0");
				values.put("mondata", "0,0");
				values.put("tuedata", "0,0");
				values.put("weddata", "0,0");
				values.put("thurdata", "0,0");
				values.put("fridata", "0,0");
				values.put("satdata", "0,0");
				db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
				break;
			}
			break;
		}
		
		values.clear();
	}
	
	public String getdaytime(){
		Date date = new Date();
		SimpleDateFormat dateDay = new SimpleDateFormat("yyyyMMdd");
		String daytime = dateDay.format(date);
		return daytime;
	}
	
	public void set_weektrain_card_Contral(){
		
		btn_setting = (ImageView)week_train.findViewById(R.id.btn_setting);
		btn_setting_second = (ImageView)week_train_second.findViewById(R.id.btn_setting);
		
		weekday = (TextView)week_train.findViewById(R.id.weekday);
		weekday_second = (TextView)week_train_second.findViewById(R.id.weekday);
		
		train_info = (TextView)week_train.findViewById(R.id.train_info);
		train_info_second = (TextView)week_train_second.findViewById(R.id.train_info);
		
		String to_day = getWeekday();
		weekday.setText(to_day);
		weekday_second.setText(to_day);
		btn_setting.setOnClickListener(new MyOnClickListener());
		btn_setting_second.setOnClickListener(new MyOnClickListener());
		istrain = getUserWeekTrainpath();
		if(istrain){
			gettrainData();
		}else{
			train_info.setText("今日无训练");
			train_info_second.setText("今日无训练");
		}
	}
	
	public String getWeekday_DB(){
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
	
	public String getWeekday(){
		Date date = new Date();

		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");


		String weekday = dateFm.format(date);
		

		return weekday;
		
	}
	
	public void gettrainData(){
		
		File file = new File(getpath,"basic_information.txt");
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s=null;
			while((s=br.readLine())!=null){
				result = result+s;
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] data = result.split("---");
		
		train_info.setText(data[0]);
		train_info_second.setText(data[0]);
	}
	
	public boolean getUserWeekTrainpath(){
		getSd = Environment.getExternalStorageDirectory();
		getpath = new File(getSd.getAbsolutePath()+
				"/ifit/dbfile/User_WeekTrain/"+Home_page.UsingName+"/"+getWeekday()+"/");
		if(!getpath.exists()){
			getpath.mkdirs();}
		
		if(getpath.listFiles().length>0){
			return true;
		}else{
			return false;
		}
	}
	public void Init_background(){
		/*ViewTreeObserver vto = fit_viewpager.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				fit_viewpager.getViewTreeObserver().
				removeGlobalOnLayoutListener(this);
				viewpager_H = fit_viewpager.getHeight();
				viewpager_W = fit_viewpager.getMeasuredWidth();
				String H = ""+viewpager_H;
				String W = ""+viewpager_W;
				Log.d("viewpager控件的高和宽", H+","+W);
				//获得空间高和宽
			}
		});*/
		//循环语句，担心开辟空间占内存
		/*View name[] = new View[]{custom_plan,instant_train,
	             				 limit_challenge,sport_record,week_train};
		int recId[] = new int[]{R.drawable.custom_plan_bg,R.drawable.instant_train_bg,
								R.drawable.limit_challenge_bg,R.drawable.sport_record_bg,
								R.drawable.week_train_bg};*/
		//for(int i=0;i<name.length;i++){
		//开启线程池来做
		/*
		for(count=0;count<recId.length;count++){
			
			Log.d("xxx", ""+count);
			excutorService.submit(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final Bitmap bm = getImage(count);
					final int i = count;
					Log.d("xxx", ""+i);
					Log.d("xxx", ""+count);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//name[i].setBackground(new RoundedCornersDrawable(bm));
						}
					});
				}
			});
			
		}
		
		*/
		
		View name[] = new View[]{custom_plan,
								 custom_plan_second,
								 person_rank,
								 person_rank_second,
				 				 //limit_challenge,
				 				 //limit_challenge_second,
				 				 sport_record,
				 				 sport_record_second,
				 				 week_train,
				 				 week_train_second};
		
		
		for(int i = 0;i<recId.length;i++){
			
			setImage(i,name);
			
		}
		
		
		
		
		
		//这样写内存使用节省的多
		/*
		InputStream Is = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTempStorage = new byte[200*1024];
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inSampleSize = 2;
		opts.inInputShareable = true;
		Is = getResources().openRawResource(R.drawable.custom_plan_bg);
		bitmap1 = BitmapFactory.decodeStream(Is, null, opts);
		custom_plan.setBackground(new RoundedCornersDrawable(bitmap1));
		
		Is = getResources().openRawResource(R.drawable.instant_train_bg);
		bitmap2 = BitmapFactory.decodeStream(Is, null, opts);
		instant_train.setBackground(new RoundedCornersDrawable(bitmap2));
		
		Is = getResources().openRawResource(R.drawable.limit_challenge_bg);
		bitmap3 = BitmapFactory.decodeStream(Is, null, opts);
		limit_challenge.setBackground(new RoundedCornersDrawable(bitmap3));
		limit_challenge_second.setBackground(new RoundedCornersDrawable(bitmap3));
		
		Is = getResources().openRawResource(R.drawable.sport_record_bg);
		bitmap4 = BitmapFactory.decodeStream(Is, null, opts);
		sport_record.setBackground(new RoundedCornersDrawable(bitmap4));
		sport_record_second.setBackground(new RoundedCornersDrawable(bitmap4));
		
		Is = getResources().openRawResource(R.drawable.week_train_bg);
		bitmap5 = BitmapFactory.decodeStream(Is, null, opts);
		week_train.setBackground(new RoundedCornersDrawable(bitmap5));
		week_train_second.setBackground(new RoundedCornersDrawable(bitmap5));
		*/
		/*
		bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.custom_plan_bg);
		custom_plan.setBackground(new RoundedCornersDrawable(bitmap));
		//custom_plan_second.setBackground(new RoundedCornersDrawable(bitmap));
		bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.instant_train_bg);
		instant_train.setBackground(new RoundedCornersDrawable(bitmap));
		bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.limit_challenge_bg);
		limit_challenge.setBackground(new RoundedCornersDrawable(bitmap));
		limit_challenge_second.setBackground(new RoundedCornersDrawable(bitmap));
		bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.sport_record_bg);
		sport_record.setBackground(new RoundedCornersDrawable(bitmap));
		sport_record_second.setBackground(new RoundedCornersDrawable(bitmap));
		bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.week_train_bg);
		week_train.setBackground(new RoundedCornersDrawable(bitmap));
		week_train_second.setBackground(new RoundedCornersDrawable(bitmap));*/
		//bitmap1.recycle();
		//bitmap2.recycle();
		//bitmap3.recycle();
		//bitmap4.recycle();
		//bitmap5.recycle();
		//String x = name[i].toString();
		//Log.d("hh", x);
		
	}	
	
	public Bitmap getImage(final int i){
		
		
		
		if(imgbgCache.containsKey(""+recId[i])){
			//SoftReference<Bitmap> softReference = imgbgCache.get(""+recId[i]);
			Bitmap bbm = imgbgCache.get(""+recId[i]);
			//Log.d("xxx", "11");
			//if(softReference.get() != null){
			if(bbm!=null){
				//return softReference.get();
				Log.d("xxx", "Map里面有");
				return bbm;
				
			}else{
				Bitmap bm;
				InputStream Is = null;
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inTempStorage = new byte[200*1024];
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inPurgeable = true;
				opts.inSampleSize = 2;
				opts.inInputShareable = true;
				Is = getResources().openRawResource(recId[i]);
				bm = BitmapFactory.decodeStream(Is, null, opts);
				//imgbgCache.put(""+recId[i], new SoftReference<Bitmap>(bm));
				imgbgCache.put(""+recId[i], bm);
				Log.d("xxx", "进行创建图像并存入map");
				return bm;
			}
		}else{
		Bitmap bm;
		InputStream Is = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTempStorage = new byte[200*1024];
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inSampleSize = 2;
		opts.inInputShareable = true;
		Is = getResources().openRawResource(recId[i]);
		bm = BitmapFactory.decodeStream(Is, null, opts);
		//imgbgCache.put(""+recId[i], new SoftReference<Bitmap>(bm));
		imgbgCache.put(""+recId[i], bm);
		return bm;
		}
		
	}
	
	public void setImage(final int i,final View name[]){
		
		
		excutorService.submit(new Runnable() {
			
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				
					final Bitmap bm = getImage(i);
					//Log.d("xxx", ""+i+","+bm.toString());
					
				
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						name[i].setBackground(new RoundedCornersDrawable(bm,pagerW,pagerH));
					}
				});
			}
		});
	}
	
	
	public void Init_list_View(){
		list_View = new ArrayList<View>();
		list_View.add(person_rank_second);
		list_View.add(week_train_second);
		//list_View.add(limit_challenge_second);
		list_View.add(sport_record);
		list_View.add(custom_plan);
		list_View.add(person_rank);
		list_View.add(week_train);
		//list_View.add(limit_challenge);
		list_View.add(sport_record_second);
		list_View.add(custom_plan_second);
		//list_View.add(custom_plan_second);
	}
	
	
	public void set_adapt(){
		MyPagerAdapter adapter = new MyPagerAdapter(list_View);	
		fit_viewpager.setAdapter(adapter);
		fit_viewpager.setCurrentItem(3,false);
		fit_viewpager.setOffscreenPageLimit(list_View.size());
		fit_viewpager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
		fit_viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	
	public void setListener(){
		personal_fit_layout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return fit_viewpager.dispatchTouchEvent(event);
            }
        });//分发触摸监听
		
		personal_fit_cycle.setOnClickListener(new MyOnClickListener());//设置图片监听
	}
	
	
	public class MyOnClickListener implements OnClickListener{
		
		
		
		//private int[] number = new int []{0,1,2,3,4};
		//int i=0;
		public MyOnClickListener(){
			//getText = text;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//String arrayText[]=new String[]{"训练","挑战","查看","添加","立即训练"};
			switch(v.getId()){
			case R.id.personal_fit_cycle:
				
				circle_img_get_text();
				
				break;
			case R.id.btn_setting:
				
				Intent intent = new Intent(Personal_fit.this,week_train_setting.class);
				startActivity(intent);
				
				break;
			
			}
			
		}
		
	}
	
	public void circle_img_get_text(){
		String getText = personal_fit_text.getText().toString();
		switch(getText){
		case "训练" :
			Intent prepare_to_train = new Intent (Personal_fit.this,prepare_to_train.class);
			prepare_to_train.putExtra("imgCardpath", getpath.toString()+"/11111111111");
			//prepare_to_train里面获得到数据，会进行删除末尾11位，获得文件夹，进而获得文件夹下的计划文件，这11位只是站位的。
			//Log.d("xxx", getpath.toString()+"/11111111111");
			//getText =  (String) personal_fit_text.getText();
			startActivity(prepare_to_train);
			break;
		/*case "挑战":
			Intent turn_Limit_challenge = new Intent (Personal_fit.this,Limit_challenge.class);
			//getText =  (String) personal_fit_text.getText();
			startActivity(turn_Limit_challenge);
			break;*/
		case "查看" :
			Intent turn_Sport_record = new Intent (Personal_fit.this,Sport_record.class);
			//getText =  (String) personal_fit_text.getText();
			startActivity(turn_Sport_record);
			break;
		case "添加" :
			Intent turn_Custom_plan = new Intent (Personal_fit.this,Custom_plan.class);
			//getText =  (String) personal_fit_text.getText();
			startActivity(turn_Custom_plan);
			break;
		case "我的排名":
			Intent turn_person_rank = new Intent (Personal_fit.this,Personal_rank.class);
			//getText =  (String) personal_fit_text.getText();
			startActivity(turn_person_rank);
			break;
		case "休息":
			new AlertDialog.Builder(Personal_fit.this).setMessage("记得要好好休息哦！")
			.setPositiveButton("知道了", null).show();
			break;
		default:
			break;
		}
		
	}
	public class MyOnPageChangeListener implements OnPageChangeListener{
		
		private int currentPosition;
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			/*if(list_View.size()>1){//多于1张，才会循环
				if(position<1){
					fit_viewpager.setCurrentItem(5,false);
				}else if(position>5){
					fit_viewpager.setCurrentItem(1,false);
				}
			}*/
			currentPosition = position;
			if(list_View.size()>1){
			if(currentPosition == 0){
				fit_viewpager.setCurrentItem(4,false);
			}else if(currentPosition == 7){
				fit_viewpager.setCurrentItem(3,false);
				}
			}
			
			switch (position){
			case 0:
				personal_fit_text.setText("我的排名");
				personal_fit_cycle.setImageResource(R.drawable.cycle_grey);
				break;
			case 1:
				if(istrain){
				personal_fit_text.setText("训练");}else{
					personal_fit_text.setText("休息");
				}
				personal_fit_cycle.setImageResource(R.drawable.cycle_green);
				break;
			/*case 1:
				personal_fit_text.setText("挑战");
				personal_fit_cycle.setImageResource(R.drawable.cycle_red);
				break;*/
			case 2:
				personal_fit_text.setText("查看");
				personal_fit_cycle.setImageResource(R.drawable.cycle_yellow);
				break;
			case 3:
				personal_fit_text.setText("添加");
				personal_fit_cycle.setImageResource(R.drawable.cycle_blue);
				break;
			case 4:
				personal_fit_text.setText("我的排名");
				personal_fit_cycle.setImageResource(R.drawable.cycle_grey);
				break;
			case 5:
				if(istrain){
					personal_fit_text.setText("训练");}else{
						personal_fit_text.setText("休息");
					}
				personal_fit_cycle.setImageResource(R.drawable.cycle_green);
				break;
			/*case 6:
				personal_fit_text.setText("挑战");
				personal_fit_cycle.setImageResource(R.drawable.cycle_red);
				break;*/
			case 6:
				personal_fit_text.setText("查看");
				personal_fit_cycle.setImageResource(R.drawable.cycle_yellow);
				break;
			default:
				break;
			}
			
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			/*if(state == fit_viewpager.SCROLL_STATE_IDLE){
				if(currentPosition == 6){
					fit_viewpager.setCurrentItem(1,false);
				}else if(currentPosition == 0){
					fit_viewpager.setCurrentItem(5,false);
				}
			}*/
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			if(arg0 ==0 ){
				fit_viewpager.setCurrentItem(5,false);
				}else if(arg0 ==6){
					fit_viewpager.setCurrentItem(2,false);
				}
			/*String i=""+arg0;
			String x = ""+arg1;
			Log.d("xxx", i+","+x);*/
			
		}
	}
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		set_title_data();
	}


	public void set_title_data(){
		
		
		locationdate = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		
		//检测文件夹和文件
		
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()
				+ "/ifit/temp/UserHeadImage/");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		location_image = new File(directory, "location_image.jpg");
		
		//获取顶部视图
		title_user_nickname = (TextView) findViewById(R.id.title_user_name);
		title_user_headimg = (ImageView) findViewById(R.id.title_user_center);
		
		
		//设置头像
		if(location_image.exists()){
			//如果用户数据库中存在头像则进行设置
			head_bitmap = BitmapFactory
					.decodeFile(location_image.toString());}
		else{
			head_bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_headimage);
		}
		title_user_headimg.setImageDrawable(new CircleImageDrawable(head_bitmap));
		
		
		//设置昵称
		String nickname = locationdate.getString("nickname", "");
		if(!nickname.equals("")){
			title_user_nickname.setText(nickname);
		}
		
	}
	
	public void Unzip(){
		
		if(!istraverse){
		
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()
				+ "/ifit/temp/");
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String outputAciton = directory.toString()+"/Unzip/";
				String outputPlan = directory.toString()+"/plan_library/";
				try {
					Log.d("xxx", "进入线程");
					UnzipAssets.unZip(Personal_fit.this,"myzip.zip", outputAciton);
					UnzipAssets.unZip(Personal_fit.this,"plan_library.zip", outputPlan);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("xxx", "出错");
					e.printStackTrace();
					Log.d("xxx", e.toString());
				}
			}
		}).start();
		
		istraverse = true;
		}
		
	}
	
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		get_day_data();//重新设置record
		
		if (!(getUserWeekTrainpath()==istrain)) {
			Log.d("xxx", "111");
			set_weektrain_card_Contral();
			if (istrain) {
				personal_fit_text.setText("训练");
			} else {
				personal_fit_text.setText("休息");
			}
		}
	}

	public void setdaydata(){
		MyDatabaseHelper usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		SQLiteDatabase db = usedb.getWritableDatabase();
		Cursor cr = db.query("User_train_record_table", 
				null, "name = ?", new String[]{Home_page.UsingName}, null, null, null);
		cr.moveToFirst();
		ContentValues values = new ContentValues();
			int dayfire_count = cr.getInt(cr.getColumnIndex("Dayfire"));
			int daytime_count = cr.getInt(cr.getColumnIndex("Daytime"));
			values.put(getWeekday_DB(), daytime_count+","+dayfire_count);
			db.update("User_train_record_table", values, "name = ?", new String[]{Home_page.UsingName});
		
		cr.close();
		db.close();
		values.clear();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(0, 0);
		excutorService.shutdown();
		setdaydata();
	}
	
}
