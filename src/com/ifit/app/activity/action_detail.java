package com.ifit.app.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ant.liao.GifView;
import com.ifit.app.R;
import com.ifit.app.other.GifView_only_myphone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;



public class action_detail extends Activity {

	private ImageView back_btn;
	private TextView action_name;
	
	private GifView gif;
	
	private TextView Training_site;
	
	private TextView main_muscle;
	
	private TextView secondary_muscle;
	
	private TextView action_describe;
	
	private  File sdCard,directory;
	
	private JSONArray jsonArray;
	
	private String GifPath;
	
	
	String getAction_name;
	String getAction_Train_site;
	String getAction_mian_mucle;
	String getAction_secondary_muscle;
	String getAction_describr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.action_detail);
		
		getIntentData();//获得png路径进而解析gif路劲
		
		
		initpath();
		initControls();
		
		getJsonObject();
		getData();
		
		setControls_data();
	}
	

	private void setControls_data(){
		
		//设置动作名称
		action_name.setText(getAction_name);
		
		//设置gif图片
		File gifpath = new File(GifPath);
		FileInputStream fis;
		try {
			fis = new FileInputStream(gifpath);
			gif.setGifImage(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//设置训练部位
		Training_site.setText(getAction_Train_site);
		
		//设置主要肌群
		main_muscle.setText(getAction_mian_mucle);
		
		//设置次要肌群
		secondary_muscle.setText(getAction_secondary_muscle);
		
		//设置描述
		action_describe.setText(getAction_describr);
		
		//设置监听
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	private void getIntentData(){
		Intent getIntent_data = getIntent();
		
		String getPngPath = getIntent_data.getStringExtra("pngPath");
		
		GifPath = getPngPath.substring(0, getPngPath.length()-11)+".gif";
		
		//Log.d("xxx", GifPath);
		
	}
	
	
	private void getData(){
		
		File gifpath = new File(GifPath);
		
		String filename = gifpath.getName().substring(0, gifpath.getName().length()-4);
		try {
			for(int i =0;i<jsonArray.length();i++){
				
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String pinyin_name = jsonObject.getString("pingyin_name");//用拼音查找
					
					if(pinyin_name.equals(filename)){
						getAction_name = jsonObject.getString("name");
						getAction_Train_site = jsonObject.getString("Training_site");
						getAction_mian_mucle = jsonObject.getString("main_muscle");
						getAction_secondary_muscle = jsonObject.getString("secondary_muscle");
						getAction_describr = jsonObject.getString("describe");
						i = jsonArray.length();
					}
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	private void getJsonObject(){
		File jsonpath = new File(directory,"describe.json");
		
		Scanner scanner = null;
		StringBuilder jsondata = new StringBuilder();
		
		try {
			scanner = new Scanner(jsonpath,"GB2312");
			while(scanner.hasNextLine()){
				jsondata.append(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(scanner != null){
				scanner.close();
			}
		}
		
		//Log.d("XXX", jsondata.toString());
		
		try {
			jsonArray = new JSONArray(jsondata.toString());
			//Log.d("xxx", jsonArray.length()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initControls(){
		
		back_btn = (ImageView)findViewById(R.id.action_detail_title_back);
		action_name = (TextView)findViewById(R.id.action_detail_title_action_name);
		
		gif = (GifView)findViewById(R.id.gif);
		
		Training_site = (TextView)findViewById(R.id.Training_site);
		main_muscle = (TextView)findViewById(R.id.main_muscle);
		secondary_muscle = (TextView)findViewById(R.id.secondary_muscle);
		
		action_describe = (TextView)findViewById(R.id.action_describe);
		
	}
	
	private void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/Unzip/");
	}
	
}
