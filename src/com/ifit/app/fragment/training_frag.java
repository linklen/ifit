package com.ifit.app.fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.ant.liao.GifView;
import com.ifit.app.R;
import com.ifit.app.activity.training;
import com.ifit.app.other.GifView_only_myphone;

import android.app.Fragment;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class training_frag extends Fragment {

	private View view=null;
	private ViewHolder viewholder;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		if(view==null){
			view = inflater.inflate(R.layout.training_frag,container,false);
			viewholder = new ViewHolder();
			viewholder.gif = (GifView)view.findViewById(R.id.training_frag_action_gif);
			viewholder.action_describe = (TextView)view.findViewById(R.id.training_frag_action_describe);
			viewholder.action_number = (TextView)view.findViewById(R.id.training_frag_action_number);
			viewholder.action_group = (TextView)view.findViewById(R.id.training_frag_action_group);
			viewholder.btn_complete = (RelativeLayout)view.findViewById(R.id.action_achive_btn_layout);
			viewholder.mchronometer = (Chronometer)view.findViewById(R.id.training_frag_action_chronometer);
			viewholder.activity = (training)getActivity();
			view.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)view.getTag();
		}
		
		viewholder.btn_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				complete();
				
			}
		});
		
		return view;
	}

	
	public void complete(){
		
		int usetime = getcount_second(viewholder.mchronometer.getText().toString());
		viewholder.activity.count_totaltime(usetime);
		viewholder.activity.add_process("torest");
		if(viewholder.now!=viewholder.count){
			resetTime();
			viewholder.activity.change_frag("resting");
			viewholder.now++;}
		else{
				viewholder.activity.change_frag("next");
				viewholder.now = 1;
				resetTime();
			}
			
			
	}
	
	public int getcount_second(String gettime){
		String[] T = gettime.split(":");
		
		int a=Integer.parseInt(T[0]);
		int b = a*60;
		int c = Integer.parseInt(T[1]);
		int d = b+c;
		
		return d;
	}
	
	public void setControldata(String gifpath,
			String describe,String number,String group){
		
		 FileInputStream is;
		try {
			is = new FileInputStream(gifpath);
			viewholder.gif.setGifImage(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 viewholder.action_describe.setText(describe);
		 String num = number.substring(0, number.length()-1);
		 viewholder.action_number.setText(num);
		 String now = group.substring(0, group.length()-1);
		 viewholder.count = Integer.parseInt(now);
		 viewholder.action_group.setText(viewholder.now+"/"+now);
		 
	}
	
	public void changeGroup(String group){
		
		viewholder.action_group.setText(viewholder.now+"/"+viewholder.count);
	}
	
	public void startTime(){
		viewholder.mchronometer.start();
	}
	
	public void stopTime(){
		viewholder.mchronometer.start();
	}
	
	public void resetTime(){
		viewholder.mchronometer.setBase(SystemClock.elapsedRealtime());
	}
	
	class ViewHolder{
		GifView gif;
		TextView action_describe,action_number,action_group;
		Chronometer mchronometer;
		RelativeLayout btn_complete;
		training activity;
		int now=1;
		int count;
	}
}
