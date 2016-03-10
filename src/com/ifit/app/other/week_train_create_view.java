package com.ifit.app.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifit.app.R;
import com.ifit.app.activity.Home_page;
import com.ifit.app.activity.plan_library;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

public class week_train_create_view {

	
	private View getView;
	//private learnpic_loadImg loadimg = new learnpic_loadImg();
	private File getSd,getpath;
	private String weekday;
	private ViewHolder_yes viewholder_yes;
	private ViewHolder_no viewholder_no;
	public String whichclick=null;
	/*
	private void pic_loading(String imgpath,final ViewHolder viewholder){
		
		Bitmap cacheimg =loadimg.loadBitmap(imgpath,
				new ImgCallback() {
					
					@Override
					public void imageLoaded(Bitmap bm) {
						// TODO Auto-generated method stub
						viewholder.action_img.setImageBitmap(bm);
					}
				});
		if(cacheimg != null){
			viewholder.action_img.setImageBitmap(cacheimg);
		}
	
	}	*/	
		
	public void init_getView(Context context){
		
		LayoutInflater inflater=LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.week_train_add_view_itemlayout,null);
		getView = view;
		
		}
	
	public boolean initpath(){
		getSd = Environment.getExternalStorageDirectory();
		getpath = new File(getSd.getAbsolutePath()+
				"/ifit/dbfile/User_WeekTrain/"+Home_page.UsingName+"/"+weekday+"/");
		if(!getpath.exists()){
			getpath.mkdirs();}

			String[] m = getpath.list();
			if(m.length>0){
				return true;
			}else{
				return false;
			}
			
			
	}
	
	//如果是服务器则在服务器文本读取文件
	public String[] getfiledata(File file){
		File basicfile = new File(file,"basic_information.txt");
		
		String result = "";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(basicfile));
			String s = null;
			while((s=br.readLine())!= null){
				result = result+s;
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] data = result.split("---");
		return data;
	}
	
	public  View createView(String weekday,Context context){
		
		this.weekday = weekday;
		
		init_getView(context);
		
		if(initpath()){
			viewholder_yes = new ViewHolder_yes();
			viewholder_yes.total_layout = (RelativeLayout)getView.findViewById(R.id.hava_train);
			viewholder_yes.weekday = (TextView)getView.findViewById(R.id.weekday);
			viewholder_yes.planname = (TextView)getView.findViewById(R.id.plan_name);
			viewholder_yes.plantime = (TextView)getView.findViewById(R.id.time_text);
			viewholder_yes.planplace = (TextView)getView.findViewById(R.id.location_set_text);
			viewholder_yes.planpurpose = (TextView)getView.findViewById(R.id.purpose_set_text);
			viewholder_yes.plansite = (TextView)getView.findViewById(R.id.site_set_text);
			
			viewholder_yes.total_layout.setVisibility(View.VISIBLE);
			
			String[] data = getfiledata(getpath);
			
			viewholder_yes.weekday.setText(weekday);
			viewholder_yes.planname.setText(data[0]);
			viewholder_yes.plantime.setText(data[4]);
			viewholder_yes.planplace.setText(data[2]);
			viewholder_yes.planpurpose.setText(data[3]);
			viewholder_yes.plansite.setText(data[1]);
			
		}else{
			viewholder_no = new ViewHolder_no();
			viewholder_no.total_layout = (RelativeLayout)getView.findViewById(R.id.no_train);
			viewholder_no.add_plan = (Button)getView.findViewById(R.id.serch_plan_library);
			viewholder_no.total_layout.setVisibility(View.VISIBLE);
			viewholder_no.add_plan.setOnClickListener(new MyOnClickListen(context));
		}
		
		
		return getView;
	
}
	
	
	
	
	class ViewHolder_yes{
		RelativeLayout total_layout;
		TextView weekday;
		TextView planname;
		TextView plantime;
		TextView planplace;
		TextView planpurpose;
		TextView plansite;
	}
	
	class ViewHolder_no{
		RelativeLayout total_layout;
		Button add_plan;
		
	}
	
	class MyOnClickListen implements OnClickListener{

		Context context;
		public MyOnClickListen(Context context){
			this.context = context;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.serch_plan_library:
				Intent intent = new Intent(context,plan_library.class);
				intent.putExtra("weekday", weekday);
				whichclick = weekday;
				((Activity) context).startActivity(intent);
				break;
			}
		}
		
	}
}
