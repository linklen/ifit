package com.ifit.app.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.jar.Attributes.Name;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ifit.app.R;
import com.ifit.app.activity.training;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class resting_frag extends Fragment {

	private View view=null;
	private ViewHolder viewholder;
	private TimeCount timer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(view==null){
			view = inflater.inflate(R.layout.train_rest_frag,container,false);
			viewholder = new ViewHolder();
			viewholder.timeText = (TextView)view.findViewById(R.id.train_rest_Timetext);
			viewholder.restlayout = (LinearLayout)view.findViewById(R.id.train_rest_frag_resting_layout);
			viewholder.btn_suspend = (TextView)view.findViewById(R.id.train_rest_time_suspended);
			viewholder.nextlayout = (LinearLayout)view.findViewById(R.id.train_rest_frag_next_layout);
			viewholder.know = (TextView) view.findViewById(R.id.train_rest_frag_resting_know);
			viewholder.know_refresh = (ImageView) view.findViewById(R.id.train_rest_frag_resting_refresh);
			viewholder.next_action_img = (ImageView)view.findViewById(R.id.train_rest_frag_next_action_img);
			viewholder.next_action_name = (TextView)view.findViewById(R.id.train_rest_frag_next_action_name);
			viewholder.activity = (training)getActivity();
			getJsonObject();
			view.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)view.getTag();
		}
		
		viewholder.btn_suspend.setSelected(false);
		viewholder.know.setText(random_get_know());
		viewholder.know_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewholder.know.setText(random_get_know());
			}
		});
		
		viewholder.btn_suspend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(viewholder.btn_suspend.isSelected()){
					
					viewholder.btn_suspend.setSelected(false);
					viewholder.btn_suspend.setText("暂停");
					timer_restart();
					
				}else{
					viewholder.btn_suspend.setSelected(true);
					viewholder.btn_suspend.setText("继续");
					timer_suspend();
				}
				
			}
		});
		
		
		return view;
	}
	
	
	
	public void layout_visible(String name){
		
		if(name.equals("rest")){
			viewholder.restlayout.setVisibility(View.VISIBLE);
			viewholder.nextlayout.setVisibility(View.GONE);
		}else{
			viewholder.restlayout.setVisibility(View.GONE);
			viewholder.nextlayout.setVisibility(View.VISIBLE);
		}
	}
	
	
	public void set_time(String resttime){
		
		int second = Integer.parseInt(resttime.substring(0,resttime.length()-1));
		
		timer = new TimeCount(second*1000, 1000);
		
	}
	
	public void timer_start(){
		timer.start();
	}
	
	public void timer_restart(){
		String Ssecond = viewholder.timeText.getText().toString();
		set_time(Ssecond);
		timer_start();
	}
	
	public void timer_suspend(){
		timer.cancel();
	}
	
		class ViewHolder{
			LinearLayout restlayout,nextlayout;
			TextView btn_suspend,timeText;
			TextView know;
			ImageView know_refresh;
			ImageView next_action_img;
			TextView next_action_name;
			training activity;
			JSONArray jsonArray;
		}
		
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer{
		
		public TimeCount(long millisInFuture,long countDownInterval){
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			viewholder.timeText.setText(millisUntilFinished/1000+"秒");
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			time_over();
		}
	}

	public void set_advance_data(String imgpath,String action_name){
		
		File imgfile = new File(imgpath);
		Uri uri = Uri.fromFile(imgfile);
		
		viewholder.next_action_img.setImageURI(uri);
		viewholder.next_action_name.setText(action_name);
		
	}
	
	public void time_over(){
		
		if(viewholder.restlayout.isShown()){
		viewholder.activity.change_frag("rest_over");
		viewholder.activity.add_process("totrain");
		}else{
			viewholder.activity.change_frag("next_over");
		}
		
		timer.cancel();
		
	}
	
	private void getJsonObject(){
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/Unzip/");
		File jsonpath = new File(directory,"knowledge.json");
		
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
			viewholder.jsonArray = new JSONArray(jsondata.toString());
			//Log.d("xxx", jsonArray.length()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String random_get_know(){
		
		int id=(int)(Math.random()*100);
		String know = null;
		
		try {
			for (int i = 0; i < viewholder.jsonArray.length(); i++) {

				JSONObject jsonObject = viewholder.jsonArray.getJSONObject(i);
				int get_id = jsonObject.getInt("Id");// 用拼音查找

				if (get_id == id) {
					know = jsonObject.getString("Content");
					i = viewholder.jsonArray.length();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return know;
	}
}
