package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.ifit.app.R;
import com.ifit.app.other.prepare_to_train_create_view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class prepare_to_train extends Activity {

	private ImageView btn_back;
	private TextView plan_name,plan_describe,plan_time,plan_place,plan_fire,plan_site,plan_purpose;
	private Button btn_to_train;
	private LinearLayout add_view_layout;
	private prepare_to_train_create_view cr = new prepare_to_train_create_view();
	private String cardImgpath;
	public static prepare_to_train prepare_to_train_instant = null;
	
	private String[] intentdata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.prepare_to_train);
		
		prepare_to_train_instant = this;
		
		initContral();
		
		getIntentdata();
		getfileData(); //已经包含了设置数据
		
		setListen();
	}
	
	
	public void setListen(){
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_to_train.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(prepare_to_train.this,training.class);
				intent.putExtra("action_data", intentdata);
				intent.putExtra("plan_name", plan_name.getText().toString());
				intent.putExtra("plan_fire", plan_fire.getText().toString());
				startActivity(intent);
			}
		});
		
	}
	
	
	
	public void initContral(){
		
		btn_back = (ImageView)findViewById(R.id.prepare_to_train_back);
		plan_name = (TextView)findViewById(R.id.prepare_to_train_name);
		plan_describe = (TextView)findViewById(R.id.prepare_to_train_describe);
		plan_time = (TextView)findViewById(R.id.prepare_to_train_time);
		plan_place = (TextView)findViewById(R.id.prepare_to_train_place);
		plan_site = (TextView)findViewById(R.id.prepare_to_train_site);
		plan_purpose = (TextView)findViewById(R.id.prepare_to_train_purpose);
		plan_fire = (TextView)findViewById(R.id.prepare_to_train_fire);
		btn_to_train = (Button)findViewById(R.id.prepare_to_train_btn_to_train);
		add_view_layout = (LinearLayout)findViewById(R.id.prepare_to_train_add_view_layout);
		
	}
	

	public void getIntentdata(){
		
		Intent getimgcard_path = getIntent();
		cardImgpath = getimgcard_path.getStringExtra("imgCardpath");
		
	}
	
	public void getfileData(){
		
		File getpath = new File(cardImgpath);
		
		String path = getpath.getParent();
		
		String action_info_path = path+"/action_information.txt";
		String basic_info_path =path + "/basic_information.txt";
		
		File action_file = new File(action_info_path);
		File basic_file = new File(basic_info_path);
		
		
		getString(basic_file,"basic");
		getString(action_file,"action");
		
		
	}
	
	public void getString(File txtfile,String name){
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
		
		
		if(name.equals("action")){
			getaction_info(data.toString());
		}else{
			set_basicData(data.toString());
		}
	}
	
	
	public void set_basicData(String basic_data) { // 只需要解析basic的文本文件

		String[] data = basic_data.split("---");
		
		plan_name.setText(data[0]);
		plan_site.setText(data[1]);
		plan_place.setText(data[2]);
		plan_purpose.setText(data[3]);
		plan_time.setText(data[4]);
		plan_describe.setText(data[5]);

	}
	

	
	public void getaction_info(String action_data) { // 只需要解析basic的文本文件

		String[] data = action_data.split("。");
		
		intentdata = data;
		
		for(int i = 0;i<data.length;i++){
			View view = new View(this);
			String[] datadetail = data[i].split("，");
			int order = Integer.parseInt(datadetail[0])+1;
			
			view = cr.createView(datadetail[5], datadetail[1], 
					datadetail[2]+"x"+datadetail[3],order+"", this);
			add_view_layout.addView(view);
		}

		plan_fire.setText((data.length*50)+"千卡");
		

	}
}
