package com.ifit.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.adapter.MyPagerAdapter;
import com.ifit.app.other.RoundedCornersDrawable;
import com.ifit.app.other.MyPageTransformer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Personal_fit extends Activity {

	private ViewPager fit_viewpager;
	//public int viewpager_W,viewpager_H;
	public List<View> list_View;
	private View custom_plan,//custom_plan_second,
				 instant_train,
	             limit_challenge,limit_challenge_second,
	             sport_record,sport_record_second,
	             week_train,week_train_second;
	private ImageView personal_fit_cycle;
	private TextView personal_fit_text;
	//public  String getText;
	private RelativeLayout personal_fit_layout;
	private LayoutInflater inflater;
	private Bitmap bitmap;
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
		
		fit_viewpager.setPageTransformer(true, new MyPageTransformer());
	}
	
	
	public void findId(){
		inflater = getLayoutInflater();
		personal_fit_layout = (RelativeLayout)findViewById(R.id.personal_fit_layout);
		fit_viewpager = (ViewPager)findViewById(R.id.fit_viewpager);
		custom_plan = inflater.inflate(R.layout.custom_plan, null);
		//custom_plan_second = inflater.inflate(R.layout.custom_plan, null);
		instant_train = inflater.inflate(R.layout.instant_train, null);
		limit_challenge = inflater.inflate(R.layout.limit_challenge, null);
		limit_challenge_second = inflater.inflate(R.layout.limit_challenge, null);
		sport_record = inflater.inflate(R.layout.sport_record, null);
		sport_record_second = inflater.inflate(R.layout.sport_record, null); 
		week_train = inflater.inflate(R.layout.week_train, null);
		week_train_second = inflater.inflate(R.layout.week_train, null);
		personal_fit_cycle = (ImageView)findViewById(R.id.personal_fit_cycle);
		personal_fit_text = (TextView)findViewById(R.id.personal_fit_Text);
		
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
		week_train_second.setBackground(new RoundedCornersDrawable(bitmap));
		bitmap.recycle();
		//String x = name[i].toString();
		//Log.d("hh", x);
		
	}	
	
	
	public void Init_list_View(){
		list_View = new ArrayList<View>();
		list_View.add(week_train_second);
		list_View.add(limit_challenge_second);
		list_View.add(sport_record);
		list_View.add(custom_plan);
		list_View.add(instant_train);
		list_View.add(week_train);
		list_View.add(limit_challenge);
		list_View.add(sport_record_second);
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
		
		personal_fit_cycle.setOnClickListener(new MyImageViewListener());//设置图片监听
	}
	
	
	public class MyImageViewListener implements OnClickListener{
		
		
		
		//private int[] number = new int []{0,1,2,3,4};
		//int i=0;
		public MyImageViewListener(){
			//getText = text;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//String arrayText[]=new String[]{"训练","挑战","查看","添加","立即训练"};
			String getText =  (String) personal_fit_text.getText();
			switch(getText){
			case "训练" :
				Intent Week_train = new Intent (Personal_fit.this,Week_train.class);
				//getText =  (String) personal_fit_text.getText();
				startActivity(Week_train);
				break;
			case "挑战":
				Intent turn_Limit_challenge = new Intent (Personal_fit.this,Limit_challenge.class);
				//getText =  (String) personal_fit_text.getText();
				startActivity(turn_Limit_challenge);
				break;
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
			case "立即训练":
				Intent turn_Instant_train = new Intent (Personal_fit.this,Instant_train.class);
				//getText =  (String) personal_fit_text.getText();
				startActivity(turn_Instant_train);
				break;
			default:
				break;
			}
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
			if(currentPosition == 7){
				fit_viewpager.setCurrentItem(2,false);
			}else if(currentPosition == 0){
				fit_viewpager.setCurrentItem(5,false);
				}
			}
			
			switch (position){
			case 0:
				personal_fit_text.setText("训练");
				personal_fit_cycle.setImageResource(R.drawable.cycle_green);
				break;
			case 1:
				personal_fit_text.setText("挑战");
				personal_fit_cycle.setImageResource(R.drawable.cycle_red);
				break;
			case 2:
				personal_fit_text.setText("查看");
				personal_fit_cycle.setImageResource(R.drawable.cycle_yellow);
				break;
			case 3:
				personal_fit_text.setText("添加");
				personal_fit_cycle.setImageResource(R.drawable.cycle_blue);
				break;
			case 4:
				personal_fit_text.setText("立即训练");
				personal_fit_cycle.setImageResource(R.drawable.cycle_grey);
				break;
			case 5:
				personal_fit_text.setText("训练");
				personal_fit_cycle.setImageResource(R.drawable.cycle_green);
				break;
			case 6:
				personal_fit_text.setText("挑战");
				personal_fit_cycle.setImageResource(R.drawable.cycle_red);
				break;
			case 7:
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
			if(arg0 ==6 ){
				fit_viewpager.setCurrentItem(1,false);
				}else if(arg0==0){
				fit_viewpager.setCurrentItem(6,false);}
			//String i=""+arg0;
			//String x = ""+arg1;
			//Log.d("hh", i+","+x);
			
		}
	}
	
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(0, 0);
	}
	
}
