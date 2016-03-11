package com.ifit.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Sport_record extends Activity {

	private ImageView btn_back;
	private TextView today_fair;
	
	private CombinedChart Chart;
	private CombinedData Data;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	
	int mondata_time,tuedata_time,weddata_time,thurdata_time,fridata_time,satdata_time,sundata_time;
	int mondata_fire,tuedata_fire,weddata_fire,thurdata_fire,fridata_fire,satdata_fire,sundata_fire;
	
	String[] week;
	int[] fire;
	int[] time;
	
	int today_fire_data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sport_record_in);
		getdb_data();
		getWeekday();
		showChart();
		
		btn_back = (ImageView)findViewById(R.id.sport_record_in_title_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		today_fair = (TextView)findViewById(R.id.today_fire);
		today_fair.setText(today_fire_data+"");
	}
	
	private void showChart() {
		Chart = (CombinedChart)findViewById(R.id.combinedChart);
		Chart.setDescription("");
		
		
		XAxis xaxis = Chart.getXAxis();
		xaxis.setPosition(XAxisPosition.BOTTOM);
		xaxis.setDrawGridLines(false);
		
		YAxis ylaxis = Chart.getAxisLeft();
		ylaxis.setAxisMaxValue(120f); 
		ylaxis.setDrawAxisLine(false);
		ylaxis.setLabelCount(3, true);
		
		YAxis yraxis = Chart.getAxisRight();
		yraxis.setAxisMaxValue(2000f); 
		yraxis.setDrawAxisLine(false);
		yraxis.setLabelCount(3, true);
		
		ArrayList<String> xvalue = new ArrayList<String>();
		for(int i =0;i<7;i++){
			xvalue.add(week[i]);
		}
		Data = new CombinedData(xvalue);
		Data.setData(getBarData());
		Data.setData(getLineData());
		Chart.setData(Data);
		Chart.animateY(2000);

	}


	public void getdb_data(){
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		String[] x;
		Cursor cr = db.query("User_train_record_table",
				null, "name = ?", new String[]{Home_page.UsingName}, null, null, null);
		cr.moveToFirst();
		
		today_fire_data = cr.getInt(cr.getColumnIndex("Dayfire"));
		
		//这里的时间应该转化为分钟，除去60，但是这是测试，除去取证都成0了，为了方便看图表变化，就放秒钟；
		String mondata = cr.getString(cr.getColumnIndex("mondata"));
		if(mondata == null){
			mondata= "0,0";
		}
		x = mondata.split(",");
		mondata_time = Integer.parseInt(x[0]);
		mondata_fire = Integer.parseInt(x[1]);
		
		String tuedata = cr.getString(cr.getColumnIndex("tuedata"));
		if(tuedata == null){
			tuedata= "0,0";
		}
		x = tuedata.split(",");
		tuedata_time = Integer.parseInt(x[0]);
		tuedata_fire = Integer.parseInt(x[1]);
		
		String weddata = cr.getString(cr.getColumnIndex("weddata"));
		if(weddata == null){
			weddata= "0,0";
		}
		x = weddata.split(",");
		weddata_time =Integer.parseInt(x[0]);
		weddata_fire =Integer.parseInt(x[1]);
		
		String thurdata = cr.getString(cr.getColumnIndex("thurdata"));
		if(thurdata == null){
			thurdata= "0,0";
		}
		x = thurdata.split(",");
		thurdata_time =Integer.parseInt(x[0]);
		thurdata_fire =Integer.parseInt(x[1]);
		String fridata = cr.getString(cr.getColumnIndex("fridata"));
		if(fridata == null){
			fridata= "0,0";
		}
		x = fridata.split(",");
		fridata_time = Integer.parseInt(x[0]);
		fridata_fire =Integer.parseInt(x[1]);
		
		String satdata = cr.getString(cr.getColumnIndex("satdata"));
		if(satdata == null){
			satdata= "0,0";
		}
		x = satdata.split(",");
		satdata_time =Integer.parseInt(x[0]);
		satdata_fire = Integer.parseInt(x[1]);
		
		String sundata = cr.getString(cr.getColumnIndex("sundata"));
		if(sundata == null){
			sundata= "0,0";
		}
		x = sundata.split(",");
		sundata_time = Integer.parseInt(x[0]);
		sundata_fire = Integer.parseInt(x[1]);
		
		db.close();
		cr.close();
		//Log.d("xxx",mondata+"|"+tuedata+"|"+weddata+"|"+thurdata+"|"+fridata+"|"+satdata+"|"+sundata);
	}
	
	
	private LineData getLineData() {

		LineData data = new LineData();
		ArrayList<Entry> entries = new ArrayList<Entry>();

		for(int i=0;i<7;i++){
		entries.add(new Entry(time[i], i));}

		LineDataSet lineDataSet = new LineDataSet(entries, "锻炼时间");

		lineDataSet.setLineWidth(1.75f);
		lineDataSet.setCircleSize(3f);
		// 一个lineDataSet是一条线

		// ArrayList<LineDataSet> lineLineDataSets = new
		// ArrayList<LineDataSet>();
		// lineLineDataSets.add(lineDataSet);
		lineDataSet.setDrawCubic(true);
		lineDataSet.setCubicIntensity(0);

		data.addDataSet(lineDataSet);
		return data;
	}

	private BarData getBarData() {

		BarData data = new BarData();

		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		for (int i = 0; i < 7; i++) {
			entries.add(new BarEntry(fire[i], i));
		}

		BarDataSet barDataSet = new BarDataSet(entries, "热量消耗");

		barDataSet.setColor(Color.rgb(114, 188, 223));
		barDataSet.setAxisDependency(AxisDependency.RIGHT);
		data.addDataSet(barDataSet);

		return data;
	}
	public String getWeekday(){
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		String weekday = dateFm.format(date);
		
		switch(weekday){
		case "星期一":
			week = new String[]{"星期二","星期三","星期四","星期五","星期六","星期日","星期一"};
			time = new int[]{tuedata_time,weddata_time,thurdata_time,fridata_time,satdata_time,sundata_time,mondata_time};
			fire = new int[]{tuedata_fire,weddata_fire,thurdata_fire,fridata_fire,satdata_fire,sundata_fire,mondata_fire};
			break;
		case "星期二":
			week = new String[]{"星期三","星期四","星期五","星期六","星期日","星期一","星期二"};
			time = new int[]{weddata_time,thurdata_time,fridata_time,satdata_time,sundata_time,mondata_time,tuedata_time};
			fire = new int[]{weddata_fire,thurdata_fire,fridata_fire,satdata_fire,sundata_fire,mondata_fire,tuedata_fire};
			break;
		case "星期三":
			week = new String[]{"星期四","星期五","星期六","星期日","星期一","星期二","星期三"};
			time = new int[]{thurdata_time,fridata_time,satdata_time,sundata_time,mondata_time,tuedata_time,weddata_time};
			fire = new int[]{thurdata_fire,fridata_fire,satdata_fire,sundata_fire,mondata_fire,tuedata_fire,weddata_fire};
			break;
		case "星期四":
			week = new String[]{"星期五","星期六","星期日","星期一","星期二","星期三","星期四"};
			time = new int[]{fridata_time,satdata_time,sundata_time,mondata_time,tuedata_time,weddata_time,thurdata_time};
			fire = new int[]{fridata_fire,satdata_fire,sundata_fire,mondata_fire,tuedata_fire,weddata_fire,thurdata_fire};
			break;
		case "星期五":
			week = new String[]{"星期六","星期日","星期一","星期二","星期三","星期四","星期五"};
			time = new int[]{satdata_time,sundata_time,mondata_time,tuedata_time,weddata_time,thurdata_time,fridata_time};
			fire = new int[]{satdata_fire,sundata_fire,mondata_fire,tuedata_fire,weddata_fire,thurdata_fire,fridata_fire};
			break;
		case "星期六":
			week = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
			time = new int[]{sundata_time,mondata_time,tuedata_time,weddata_time,thurdata_time,fridata_time,satdata_time};
			fire = new int[]{sundata_fire,mondata_fire,tuedata_fire,weddata_fire,thurdata_fire,fridata_fire,satdata_fire};
			break;
		case "星期日":
			week = new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
			time = new int[]{mondata_time,tuedata_time,weddata_time,thurdata_time,fridata_time,satdata_time,sundata_time};
			fire = new int[]{mondata_fire,tuedata_fire,weddata_fire,thurdata_fire,fridata_fire,satdata_fire,sundata_fire};
		}
		
		return weekday;
	}
}
