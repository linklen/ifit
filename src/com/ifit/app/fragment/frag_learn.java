package com.ifit.app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.other.Normal_ListView_PullDownAndUp.MyListViewPullDownAndUp;
import com.ifit.app.other.Normal_ListView_PullDownAndUp.MyListViewPullDownAndUp.RefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class frag_learn extends Fragment {

	
	private MyListViewPullDownAndUp MylistView;
	private List<String> data;
	int i=1;
	Handler handler=new Handler();
	private ArrayAdapter<String> adapter;
	
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.learn,container, false);
		
		MylistView = (MyListViewPullDownAndUp)view.findViewById(R.id.learn_list);
		
		
		data = new ArrayList<String>();
	       data.add("aaaaaaaa");
	       data.add("bbbbbbbbbbb");
	       data.add("ccccc");
	       data.add("dddddddd");
	       data.add("eeeeeeeeeeeee");
	       data.add("ffffffffffffffff");
	       data.add("gggg");
	       data.add("hhhhhhhhhhhhhhhh");
	       data.add("11111111111111111111111");
	       data.add("222222222222222222222222");
	       data.add("333333333333333333333333");
	       data.add("4444444444444444444444");
	       data.add("12111111111111111");
	       data.add("33333333333111111111111111");
	       data.add("11111111111111111111122222222222222");
	       data.add("aaaaaaaa");
	       data.add("bbbbbbbbbbb");
	       data.add("ccccc");
	       data.add("dddddddd");
	       data.add("eeeeeeeeeeeee");
	       data.add("ffffffffffffffff");
	       data.add("gggg");
	       data.add("hhhhhhhhhhhhhhhh");
	       data.add("11111111111111111111111");
	       data.add("222222222222222222222222");
	       data.add("333333333333333333333333");
	       data.add("4444444444444444444444");
	       data.add("12111111111111111");
	       data.add("33333333333111111111111111");
	       data.add("11111111111111111111122222222222222");
	       adapter = new ArrayAdapter<String>(getContext(),
	    		   android.R.layout.simple_expandable_list_item_1,data);
	       MylistView.setAdapter(adapter);
	       MylistView.setRefreshListener(new MyRefreshListener());
		return view;
	}
	
	class MyRefreshListener implements RefreshListener{ 
	       //处理下拉刷新
	           @Override
	           public void pullDownRefresh() { 
	               new Thread(new Runnable() { 
	                   @Override
	                   public void run() {
	                       SystemClock.sleep(2000);
	                       data.add(i+++"new下拉更新data……………………"); 
	                       
	                       handler.post(new Runnable() { 
	                           @Override
	                           public void run() {
	                               adapter.notifyDataSetChanged();
	                               MylistView.onPulldownRefreshComplete();
	                               Toast.makeText(getContext(), "数据添加完成",Toast.LENGTH_LONG).show();
	                               System.out.println(MylistView.getLastVisiblePosition()+"======="+adapter.getCount());
	                           }
	                       }); 
	                   }
	               }).start();
	           }
	   //处理上拉刷新
	           @Override
	           public void pullUpRefresh() {
	               new Thread(new Runnable() { 
	                   @Override
	                   public void run() {
	                       SystemClock.sleep(2000);
	                       data.add(i+++"new上拉更新data……………………"); 
	                       
	                       handler.post(new Runnable() { 
	                           @Override
	                           public void run() {
	                               adapter.notifyDataSetChanged();
	                               MylistView.onPullupRefreshComplete();
	                               Toast.makeText(getContext(), "数据添加完成",Toast.LENGTH_LONG).show();
	                               System.out.println(MylistView.getLastVisiblePosition()+"======="+adapter.getCount());
	                           }
	                       }); 
	                   }
	               }).start();
	           }
		
	       }

}
