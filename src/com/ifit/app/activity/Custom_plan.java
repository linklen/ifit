package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.ifit.app.R;
import com.ifit.app.adapter.custom_plan_in_ItemAdapter;
import com.ifit.app.other.custom_plan_in_item;
import com.ifit.app.other.newsItem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Custom_plan extends Activity {

	private ImageView image_back,add_plan;
	private ListView listview;
	private custom_plan_in_item item;
	//private List<custom_plan_in_item> itemlist = new LinkedList<custom_plan_in_item>();
	public LinkedList<custom_plan_in_item> itemlist = new LinkedList<custom_plan_in_item>();
	private custom_plan_in_ItemAdapter adapter;
	
	public File sdCard,directory;
	
	private SharedPreferences getUsername;
	private String User_name;
	
	private int filecount=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_plan_in);
		
		getUsername = getSharedPreferences("islogin", MODE_PRIVATE);
		User_name = getUsername.getString("user", "");
		
		
		initpath();
		image_back = (ImageView)findViewById(R.id.custom_plan_in_title_back);
		add_plan = (ImageView)findViewById(R.id.custom_plan_in_title_add_plan);
		listview = (ListView)findViewById(R.id.custom_plan_list);
		
		getPath(directory);//在里面直接初始化list
		adapter = new custom_plan_in_ItemAdapter(this, R.layout.custom_plan_in_listitem, itemlist);
		listview.setAdapter(adapter);
		
		image_back.setOnClickListener(new MyOnClickListen());
		add_plan.setOnClickListener(new MyOnClickListen());
		
		listview.setOnItemClickListener(new MyOnItemClickListen());
		
	}
	
	public void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/dbfile/User_plan/"+User_name+"/");
		if(!directory.exists()){
		directory.mkdirs();}
	}
	
	
	String imgpath=null;
	String name = null;
	String site = null;
	String place = null;
	String purpose = null;
	
	public void getPath(File dir){
		for (File file : dir.listFiles()) {
			if (file.isFile()){
				if(file.getName().equals("imgcard.jpg")){
					imgpath = file.toString(); //图片的地址
				}else if(file.getName().equals("basic_information.txt")){
					getTxt(file);
				}
				
				initlist();
				
			}else if (file.isDirectory()){
				filecount++;
				getPath(file); }
				
			
		}
	}
	
	public int getfilecount(File dir,int recount){
		
		
		
		for (File file : dir.listFiles()) {
			if (file.isFile()){
				
			}else if (file.isDirectory()){
				recount++;
				getfilecount(file,recount); }
				
			
		}
		
		return recount;
	}
	
	
	public void initlist(){
		if(imgpath!=null&&name!=null&&site!=null&&place!=null&&purpose!=null){
			item = new custom_plan_in_item(name, site, place, purpose, imgpath);
			itemlist.addFirst(item);
			imgpath=null;
			name = null;
			site = null;
			place = null;
			purpose = null;
		}
	}
	
	
	public void getTxt(File txtfile){
		
		Scanner scanner = null;
		StringBuilder data = new StringBuilder();
		
		try {
			scanner = new Scanner(txtfile,"utf-8");
			while(scanner.hasNextLine()){
				data.append(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(scanner != null){
				scanner.close();
			}
		}
		
		//String action = data.toString();
		getbasic_info(data.toString());
	}
	
	
	public void getbasic_info(String basic_data){ //只需要解析basic的文本文件
		
		String[] data = basic_data.split("---");
		
		name = data[0];
		site = data[1];
		place =data[2];
		purpose = data[3];
		
	}
	
	
	
	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.custom_plan_in_title_back:
				finish();
				break;
			case R.id.custom_plan_in_title_add_plan:
				Intent turn_edit_plan = new Intent(Custom_plan.this,edit_plan.class);
				startActivity(turn_edit_plan);
				break;
			}
		}
		
	}


	class MyOnItemClickListen implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Custom_plan.this,prepare_to_train.class);
			item = adapter.getItem(position);
			intent.putExtra("imgCardpath", item.getPlanImagepath());
			//Log.d("xxx", item.getPlanImagepath());
			startActivity(intent);
		}
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		int recount = 0;
		super.onRestart();
		
		if(getfilecount(directory,recount) != filecount){
			
			if(itemlist!=null&adapter!=null){
				itemlist.clear();
				adapter.clear();
				getPath(directory);//在里面直接初始化list
				//adapter = new custom_plan_in_ItemAdapter(this, R.layout.custom_plan_in_listitem, itemlist);
				adapter.notifyDataSetChanged();
			}
		}
		
		//getfilecount(directory,recount);
		//Log.d("xxx", ""+filecount);
		//Log.d("xxx", ""+);
		//if(itemlist!=null&&adapter!=null)
	}
	
	

}
