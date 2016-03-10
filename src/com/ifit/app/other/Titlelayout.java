package com.ifit.app.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifit.app.R;
import com.ifit.app.activity.Personal_center;
import com.ifit.app.activity.Personal_fit;
import com.ifit.app.activity.Personal_rank;

public class Titlelayout extends RelativeLayout{

	
	private ImageView title_user_center,title_user_fit;
	
	public Titlelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		LayoutInflater.from(context).inflate(R.layout.titlelayout, this);
		title_user_center = (ImageView)findViewById(R.id.title_user_center);
		title_user_fit = (ImageView)findViewById(R.id.title_user_fit);
		//String con = getContext().toString();//���Context��ID��û���ô������
		String getname = getContext().getClass().toString();//�������������
		String compare_name = "class com.ifit.app.activity.Personal_fit";
		//String pak = getContext().getPackageName().toString();//��ð�������Ҫ�ڲ�ͬ���в��ܱ��
		//Log.d("con", con);
		//Log.d("cla", cla);
		//Log.d("pak", pak);
		
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
		
		
		
		
	}

}
