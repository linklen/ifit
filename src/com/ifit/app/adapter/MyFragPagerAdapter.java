package com.ifit.app.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> FragmentList;
	public MyFragPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		FragmentList = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return FragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return FragmentList.size();
	}

}
