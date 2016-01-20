package com.ifit.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.ifit.app.R;
import com.ifit.app.adapter.MyPagerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class Personal_fit extends Activity {

	private ViewPager fit_viewpager;
	public List<View> list_View;
	private View custom_plan,instant_train,
	             limit_challenge,sport_record,week_train;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_fit);
		fit_viewpager = (ViewPager)findViewById(R.id.fit_viewpager);
		list_View = new ArrayList<View>();
		inflater = getLayoutInflater();
		findId();
		Init_list_View();
		MyPagerAdapter adapter = new MyPagerAdapter(list_View);
		fit_viewpager.setAdapter(adapter);
	}
	
	public void findId(){
		custom_plan =inflater.inflate(R.layout.custom_plan, null);
		instant_train = inflater.inflate(R.layout.instant_train, null);
		limit_challenge = inflater.inflate(R.layout.limit_challenge, null);
		sport_record = inflater.inflate(R.layout.sport_record, null);
		week_train = inflater.inflate(R.layout.week_train, null);
	}
	
	public void Init_list_View(){
		list_View.add(custom_plan);
		list_View.add(instant_train);
		list_View.add(limit_challenge);
		list_View.add(sport_record);
		list_View.add(week_train);
	}

	
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(0, 0);
	}
	
}
