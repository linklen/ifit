package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ifit.app.R;
import com.ifit.app.fragment.resting_frag;
import com.ifit.app.fragment.training_frag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class training extends Activity {

	private ImageView btn_exit_train;
	private TextView btn_skip;
	
	private TextView action_name;
	
	private ProgressBar train_progress;
	
	private String[] getData;
	private String plan_name;
	private String plan_fire;
	
	public  int totaltime = 0; 
	
	private training_frag train_frag;
	
	private resting_frag rest_frag;
	
	private FrameLayout training_layout,resting_layout;
	
	private JSONArray jsonArray;
	
	String order;
	String name;
	String group;
	String number;
	String rest_time;
	String gifpath;
	String describe;
	String imgpath;
	
	private  File sdCard,directory;
	
	int action_now_number=0;
	int action_count;
	int getgroup_NUM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.train);
		
		totaltime=0;
		
		initpath();
		initContral();
		getJsonObject();
		
		
		Intent getIntentdata = getIntent();
		getData = getIntentdata.getStringArrayExtra("action_data");
		plan_name = getIntentdata.getStringExtra("plan_name");
		plan_fire = getIntentdata.getStringExtra("plan_fire");
		action_count = getData.length; 
		
		analy_data(action_now_number);
		
		
		getCountprogress();
		setContral_data();
		
		setContral_listen();
		
	}

	
	private void initContral(){
		btn_exit_train = (ImageView)findViewById(R.id.btn_exit_train);
		btn_skip = (TextView)findViewById(R.id.training_action_skip);
		
		action_name = (TextView)findViewById(R.id.training_action_name);
		
		train_progress = (ProgressBar)findViewById(R.id.training_action_progressBar);
		
		train_frag = (training_frag)getFragmentManager().findFragmentById(R.id.training_action_frag);
		
		rest_frag = (resting_frag)getFragmentManager().findFragmentById(R.id.training_action_resting_frag);
		
		training_layout = (FrameLayout)findViewById(R.id.training_action_frag_layout);
		
		resting_layout = (FrameLayout)findViewById(R.id.training_action_resting_frag_layout);
	}
	
	
	
	
	public void setContral_data(){
		
		action_name.setText(name);
		
		train_progress.setMax(getgroup_NUM*2-(action_count-1));
		add_process("torest");
		init_trainfrag_data();
	}
	
	public void init_trainfrag_data(){
		
		train_frag.setControldata(gifpath, describe, number, group);
		train_frag.startTime();
	}
	
	public void setContral_listen(){
		
		btn_exit_train.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(training.this).setMessage("确定要退出锻炼吗！")
				.setPositiveButton("否", null).setNegativeButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).show();
			}
		});
		
		btn_skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(training_layout.isShown()){
					train_frag.complete();
				}else{
					rest_frag.time_over();
				}
				
			}
		});
		
		
	}
	
	
	
	public void analy_data(int n){
		
		String[] datadetail = getData[n].split("，");
		
		order = datadetail[0];
		name = datadetail[1];
		group = datadetail[2];
		number = datadetail[3];
		rest_time = datadetail[4];
		gifpath = datadetail[5].substring(0,datadetail[5].length()-11)+".gif";
		imgpath = datadetail[5];
		getDescribe(gifpath);
		
	}
	
	public void getCountprogress(){
		
		getgroup_NUM = 0;
		
		for(int i =0;i<getData.length;i++){
			
			String group = (getData[i].split("，"))[2];
			
			getgroup_NUM = getgroup_NUM+Integer.parseInt(group.substring(0,group.length()-1));
		}
	}

	private void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/Unzip/");
	}
	private void getJsonObject(){
		File jsonpath = new File(directory,"describe.json");
		
		Scanner scanner = null;
		StringBuilder jsondata = new StringBuilder();
		
		try {
			scanner = new Scanner(jsonpath,"GB2312");
			while(scanner.hasNextLine()){
				jsondata.append(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(scanner != null){
				scanner.close();
			}
		}
		
		//Log.d("XXX", jsondata.toString());
		
		try {
			jsonArray = new JSONArray(jsondata.toString());
			//Log.d("xxx", jsonArray.length()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getDescribe(String GifPath) {

		File gifpath = new File(GifPath);

		String filename = gifpath.getName().substring(0,
				gifpath.getName().length() - 4);
		try {
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String pinyin_name = jsonObject.getString("pingyin_name");// 用拼音查找

				if (pinyin_name.equals(filename)) {
					describe = jsonObject.getString("describe");
					i = jsonArray.length();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public void change_frag(String type){
		
		switch(type){
		case "resting":
			action_name.setText("休息");
			training_layout.setVisibility(View.GONE);
			rest_frag.layout_visible("rest");
			rest_frag.set_time(rest_time);
			rest_frag.timer_start();
			resting_layout.setVisibility(View.VISIBLE);
			break;
		case "next":
			action_count--;
			if(action_count!=0){
			action_name.setText("休息");
			training_layout.setVisibility(View.GONE);
			rest_frag.layout_visible("next");
			analy_data(++action_now_number);
			rest_frag.set_advance_data(imgpath,name);
			rest_frag.set_time(rest_time);
			rest_frag.timer_start();
			resting_layout.setVisibility(View.VISIBLE);}else{
				
				Intent turn_complete = new Intent(training.this,train_complete.class);
				turn_complete.putExtra("plan_name", plan_name);
				turn_complete.putExtra("plan_fire", plan_fire);
				turn_complete.putExtra("plan_time", totaltime);
				startActivity(turn_complete);
				 prepare_to_train.prepare_to_train_instant.finish();
				finish();
			}
			break;
		case "rest_over":
			action_name.setText(name);
			resting_layout.setVisibility(View.GONE);
			train_frag.changeGroup(group);
			training_layout.setVisibility(View.VISIBLE);
			break;
		case "next_over":
			action_name.setText(name);
			resting_layout.setVisibility(View.GONE);
			init_trainfrag_data();
			training_layout.setVisibility(View.VISIBLE);
			break;
		}
		
		
	}
	
	public void count_totaltime(int useTime){
		totaltime = totaltime + useTime;
	}
	
	int i = 1;
	
	public void add_process(String name){
		if(name.equals("torest")){
		train_progress.setProgress(i);}else{
			train_progress.setSecondaryProgress(i);
		}
		i++;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(training.this).setMessage("确定要退出锻炼吗！")
		.setPositiveButton("否", null).setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		}).show();
	}



	
	
}
