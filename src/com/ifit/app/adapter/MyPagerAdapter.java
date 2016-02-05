package com.ifit.app.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {
	
	private List<View> mlist_View;
	
	public MyPagerAdapter (List<View> list_View){
		mlist_View = list_View;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist_View.size();//返回要滑动页面的个数
		
	}

	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mlist_View.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mlist_View.get(position));
		return mlist_View.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 ==arg1;
	}
	
	

}
