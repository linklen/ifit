package com.ifit.app.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ifit.app.R;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.Rainbow_Bar_SwipeRefreshAndLoad.SwipeRefreshLayout;
import com.ifit.app.other.Rainbow_Bar_SwipeRefreshAndLoad.SwipeRefreshLayout.OnLoadListener;
import com.ifit.app.other.Rainbow_Bar_SwipeRefreshAndLoad.SwipeRefreshLayout.OnRefreshListener;

public class Personal_center extends Activity implements OnRefreshListener,OnLoadListener{

	private SwipeRefreshLayout mSwipeLayout;
	private ListView mListView;
	private ArrayAdapter<String> mListAdapter;
	private LinearLayout listView_head;
	private LayoutInflater layoutInflater;
	private ImageView btn_back,turn_setting,user_headImage;
	private Bitmap mbitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_center_listview);
		
		layoutInflater = getLayoutInflater();
		mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.personal_center_swipe_container);
		mListView = (ListView)findViewById(R.id.personal_center_listview);
		listView_head = (LinearLayout)layoutInflater.inflate(R.layout.personal_center_listview_header, null);
		btn_back = (ImageView)findViewById(R.id.personal_center_exit);
		turn_setting = (ImageView)findViewById(R.id.personal_center_turn_setting);
		user_headImage = (ImageView)listView_head.findViewById(R.id.user_pic);
		
		if(false){
			//查看是否有头像，或者本地是否有，有就设置
		}else{
			mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
		}
		user_headImage.setImageDrawable(new CircleImageDrawable(mbitmap));
		
		
		btn_back.setOnClickListener(new MyOnClickListener());
		turn_setting.setOnClickListener(new MyOnClickListener());
		
		mListAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_expandable_list_item_1,values);
		mListView.setAdapter(mListAdapter);
		mListView.addHeaderView(listView_head);
		
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setOnLoadListener(this);
		mSwipeLayout.setColor(android.R.color.holo_blue_bright,
                			  android.R.color.holo_green_light,
                			  android.R.color.holo_orange_light,
                			  android.R.color.holo_red_light);
		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
		mSwipeLayout.setLoadNoFull(false);
		
	}
	
	ArrayList<String> values = new ArrayList<String>(){{
		add("value1");
		add("value2");
		add("value3");
		add("value4");
		add("value5");
		add("value6");
		add("value7");
	}};
	
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
		 values.add("Add " + values.size());
	        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                mSwipeLayout.setLoading(false);
	                mListAdapter.notifyDataSetChanged();
	            }
	        }, 1000);
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		values.add(0, "Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                mListAdapter.notifyDataSetChanged();
            }
        }, 2000);
		
	}


	
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.personal_center_exit:
				finish();
				break;
			case R.id.personal_center_turn_setting:
				Intent turn_setting = new Intent(Personal_center.this,Setting.class);
				startActivity(turn_setting);
				break;
			}
		}
		
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.slide_uptoin, R.anim.slide_intodown);
	}



}
