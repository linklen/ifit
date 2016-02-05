package com.ifit.app.other;

import com.ifit.app.R;

import com.ifit.app.activity.Personal_center;
import com.ifit.app.activity.Personal_fit;

import com.ifit.app.activity.Personal_rank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Titlelayout extends RelativeLayout{

	private Bitmap mbitmap;
	public Titlelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.titlelayout, this);
		ImageView title_user_center = (ImageView)findViewById(R.id.title_user_center);
		TextView title_user_rank = (TextView)findViewById(R.id.title_user_rank);
		ImageView title_user_fit = (ImageView)findViewById(R.id.title_user_fit);
		//String con = getContext().toString();//获得Context的ID，没有用处，会变
		String getname = getContext().getClass().toString();//获得类名，可行
		String compare_name = "class com.ifit.app.activity.Personal_fit";
		//String pak = getContext().getPackageName().toString();//获得包名，需要在不同包中才能辨别
		//Log.d("con", con);
		//Log.d("cla", cla);
		//Log.d("pak", pak);
		if(false){
			//如果用户数据库中存在头像则进行设置
		}else{
		mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);}
		title_user_center.setImageDrawable(new CircleImageDrawable(mbitmap));
		
		
		if(getname.equals(compare_name)){
			title_user_fit.setImageResource(R.drawable.turn_home_page);
			
			title_user_fit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//Intent turn_Personal_fit = new Intent(getContext(),Home_page.class);
					//getContext().startActivity(turn_Personal_fit);
					//((Activity) getContext()).overridePendingTransition(0,0);
					//((Activity) getContext()).overridePendingTransition(R.anim.slide_downtoin, R.anim.slide_intoup);
					((Activity) getContext()).finish();
					
				}
			});
		}else{
			title_user_fit.setImageResource(R.drawable.turn_fit_page);
			
			title_user_fit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent turn_Personal_fit = new Intent(getContext(),Personal_fit.class);
					getContext().startActivity(turn_Personal_fit);
					((Activity) getContext()).overridePendingTransition(0,0);
					//((Activity) getContext()).overridePendingTransition(R.anim.slide_downtoin, R.anim.slide_intoup);
					
				}
			});
		}
		
		
		title_user_center.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent turn_Personal_center = new Intent(getContext(),Personal_center.class);
				getContext().startActivity(turn_Personal_center);
				((Activity) getContext()).overridePendingTransition(R.anim.slide_downtoin, R.anim.slide_intoup);
			}
		});
		
		
		title_user_rank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent turn_Personal_rank = new Intent(getContext(),Personal_rank.class);
				getContext().startActivity(turn_Personal_rank);
				((Activity) getContext()).overridePendingTransition(R.anim.slide_downtoin, R.anim.slide_intoup);
			}
		});
		
		
	}

}
