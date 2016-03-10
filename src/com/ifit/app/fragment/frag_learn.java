package com.ifit.app.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.activity.learn_display;
import com.ifit.app.adapter.learnItemAdapter;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.learnItem;
import com.ifit.app.other.newsItem;
import com.ifit.app.other.Normal_ListView_PullDownAndUp.MyListViewPullDownAndUp;
import com.ifit.app.other.Normal_ListView_PullDownAndUp.MyListViewPullDownAndUp.RefreshListener;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class frag_learn extends Fragment {

	
	private MyListViewPullDownAndUp MylistView;
	
	private LinkedList<learnItem> learnItemList = null;
	private learnItemAdapter adapter;
	
	Handler handler=new Handler();
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private Cursor learn_cursor;
	private static String lastlearn_id;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		
		usedb = new MyDatabaseHelper(getContext(), "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		
		View view = inflater.inflate(R.layout.learn,container, false);
		
		MylistView = (MyListViewPullDownAndUp)view.findViewById(R.id.learn_list);
		
		learnItemList = new LinkedList<learnItem>();
		initList();
	       adapter = new learnItemAdapter(getContext(), R.layout.learn_item, learnItemList);
	       MylistView.setAdapter(adapter);
	       MylistView.setRefreshListener(new MyRefreshListener());
	       MylistView.setOnItemClickListener(new MyOnItemClickListener());
		return view;
	}
	
	private void initList() {

		learn_cursor = db.query("learn_table",null, 
				null, null, null,null, null);
		
		learn_cursor.moveToLast();
		
		if(learn_cursor.getCount()>0){
			lastlearn_id = learn_cursor.getString(learn_cursor.getColumnIndex("learn_id"));
		}
		
		
		if (learn_cursor.getCount() >= 10) {

			for (int i = 0; i <  10; i++) {
				// 这些查询本应该在服务器上做的
				setList();
				
			}
			

		} else {
			for (int i = 0; i < learn_cursor.getCount(); i++) {
				setList();
				
			}
			
		}
		
	}

	private void setList() {
		
		String ImagePath = learn_cursor.getString(learn_cursor.getColumnIndex("learn_imagepath"));
		String Title = learn_cursor.getString(learn_cursor.getColumnIndex("learn_title"));
		String Url = learn_cursor.getString(learn_cursor.getColumnIndex("learn_url"));
		
		learnItem item = new learnItem(Title, ImagePath, Url);
		learnItemList.add(item);
		if(!learn_cursor.isFirst()){
			learn_cursor.moveToPrevious();
		}
	}

	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			learnItem item = adapter.getItem(position-1);
			String imagepath = item.getImagePath();
			String url = item.geturl();
			
			Intent turn_display = new Intent (getContext(),learn_display.class);
			turn_display.putExtra("imagepath", imagepath);
			turn_display.putExtra("url", url);
			startActivity(turn_display);
		}
		
	}
	
	
	
	
	
	class MyRefreshListener implements RefreshListener{ 
	       //处理下拉刷新
	           @Override
	           public void pullDownRefresh() { 
	               new Thread(new Runnable() { 
	                   @Override
	                   public void run() {
	                       SystemClock.sleep(1000);
	                       
	                       handler.post(new Runnable() { 
	                           @Override
	                           public void run() {
	                        	   
	                        	   
	                        	   Cursor cursor = db.query("learn_table",null, 
		       	               				"learn_id > ?", new String[]{lastlearn_id},null, null,null);
		                       		cursor.moveToLast();
		                       		
		                       		if(cursor.getCount()>0){
		                       			lastlearn_id = cursor.getString(cursor.getColumnIndex("learn_id"));
		                       		int listsize =learnItemList.size()+cursor.getCount();
		                       		learnItemList.clear();
		                       		adapter.clear();
		                       		   for(int i=0;i < listsize ;i++){
		                       			setList();
		                       		   }
		       	                    adapter.notifyDataSetChanged();
		       	                    Toast.makeText(getContext(), "更新成功",Toast.LENGTH_LONG).show();
		                       		}else{
		                       			Toast.makeText(getContext(), "无更多内容！",Toast.LENGTH_LONG).show();
		                       		}
		                            MylistView.onPulldownRefreshComplete();
		                            
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
	                       SystemClock.sleep(1000);
	                       
	                       handler.post(new Runnable() { 
	                           @Override
	                           public void run() {
	                        	   
	                        	   
	                        	   
	                        	   
	                        	   int rest_news_count = learn_cursor.getCount() - learnItemList.size();
		       	               		
		       	               		if(rest_news_count <= 0){
		       	               			Toast.makeText(getContext(), "无更多内容！", Toast.LENGTH_SHORT).show();
		       	               		}else{
		       	               			if(rest_news_count<=5){
		       	               				for(int i=0;i<rest_news_count;i++){
		       	               					setList();
		       	               				}
		       	               			}else{
		       	               				for(int i=0;i<5;i++){
		       	               					//这些查询本应该在服务器上做的
		       	               						setList();
		       	               					}
		       	               			}
		       	               			
		       	               		
		                             Toast.makeText(getContext(), "更新完成！",Toast.LENGTH_LONG).show();
		       	               		}
		                        	   
		                               adapter.notifyDataSetChanged();
		                               MylistView.onPullupRefreshComplete();
	                           }
	                       }); 
	                   }
	               }).start();
	           }
		
	       }

}
