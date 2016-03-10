package com.ifit.app.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.ifit.app.R;
import com.ifit.app.adapter.search_plan_listview_adpater;
import com.ifit.app.other.search_listview_item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class plan_library extends Activity {

	private ImageView btn_back;
	private Button search_place,search_purpose;
	private TextView save_btn;
	private ListView plan_listview; 
	private search_listview_item item;
	private List<search_listview_item> list = new ArrayList<search_listview_item>();
	private search_plan_listview_adpater adapter;
	
	
	private File sdpath,librarypath;
	private String weekday;
	
	private String[] place={"ȫ��","����","��"},purpose={"ȫ��","����","��֬","����"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.serch_plan_layout);
		
		//�ƻ����ʹ�ã�ֻ��Ҫ�ڶ�Ӧ�ļ��£����������ӵļƻ��������������棬���ļƻ���ӣ�ֻҪ�ڼ���ļ�������
		//��������ļ��У���������ļ����¼��������ƻ��ļ��ͺ�
		Intent getdata = getIntent();
		weekday = getdata.getStringExtra("weekday");
		initContral();
		setbtnlisten();
		initpath();
		getplan(librarypath);//������ʼ������
		initadapter();
	}
	
	public void initadapter(){
		adapter = new search_plan_listview_adpater(this, R.layout.serch_plan_listitem, list);
		plan_listview.setAdapter(adapter);
	}

	public void initpath(){
		sdpath = Environment.getExternalStorageDirectory();
		librarypath = new File(sdpath.getAbsoluteFile()+"/ifit/temp/plan_library/");
	}
	
	//��һ�ν�������ļ��л�ȡ�ļ����е�basic���ҽ�������list
	public void getplan(File getpath){
		
		if(!getpath.exists()){
			getpath.mkdirs();
		}
		for(File file : getpath.listFiles()){
			if (file.isFile()){
				
				//Log.d("xxx", "123");
				if(file.getName().equals("basic_information.txt")){
					setlist(readtxt(file),file);
					
				}
					
			}else if(file.isDirectory()){
				 getplan(file);
			}
			
		}
	}
	
	//��ȡ�ı��ļ�
	public String readtxt(File file){
		String result = "";
		
		try {
			//����scnaner����Ϊscanner��������ո���ַ�
			//����һ��bufferedReader��ȡ�ļ�
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while((s=br.readLine())!= null){
				result = result + "\n" +s;
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//������ȡ�����ı�,�������list���Ҽ��뵽map����
	
	public void setlist(String data,File file){
		String[] info = data.split("---");
		item = new search_listview_item(info[0], info[4], info[2], info[1], file.toString());
		//Log.d("xxx", info[0]);
		list.add(item);
	}
	
	
	private void initContral(){
		btn_back = (ImageView)findViewById(R.id.search_plan_btn_back);
		search_purpose = (Button)findViewById(R.id.search_purpose);
		search_place = (Button)findViewById(R.id.search_place);
		save_btn = (TextView)findViewById(R.id.save_btn);
		plan_listview = (ListView)findViewById(R.id.search_plan_listview);
	}
	private void setbtnlisten(){
		btn_back.setOnClickListener(new MyOnClickListen());
		search_purpose.setOnClickListener(new MyOnClickListen());
		search_place.setOnClickListener(new MyOnClickListen());
		save_btn.setOnClickListener(new MyOnClickListen());
		
		search_purpose.addTextChangedListener(new MyTextChangeListen());
	}
	
	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.search_plan_btn_back:
				finish();
				break;
			case R.id.search_place:
				new AlertDialog.Builder(plan_library.this).setItems(place, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							search_place.setText("ȫ��");
							search_purpose.setText("ȫ��");
							break;
						case 1:
							search_place.setText("����");
							search_purpose.setText("ȫ��");
							break;
						case 2:
							search_place.setText("��");
							search_purpose.setText("ȫ��");
							break;
						}
					}
				}).show();
				
				
				break;
			case R.id.search_purpose:
				new AlertDialog.Builder(plan_library.this).setItems(purpose,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								switch (which) {
								case 0:
									search_purpose.setText("ȫ��");
									break;
								case 1:
									search_purpose.setText("����");
									break;
								case 2:
									search_purpose.setText("��֬");
									break;
								case 3:
									search_purpose.setText("����");
									break;
								}
							}
						}).show();
				break;
			case R.id.save_btn:
				int positon=0;
				for(int key : adapter.ischeck.keySet()){
					if(adapter.ischeck.get(key)){
						positon = key;
						break;
					}
				}
				item = adapter.getItem(positon);
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							//Log.d("xxx", "111");
							saveplan(item.getPlanpath());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				
				finish();
				
				break;
			}
		}
		
	}
	
	//���ƻ�������ļƻ����浽�û���������
	public void saveplan(String planpath) throws Exception{
		
		String action = planpath.substring(0, planpath.length()-21)+"action_information.txt";
		
		File basic_file = new File(planpath);
		File action_file = new File(action);
		
		String basictxt = "";
		String actintxt ="";
		
		BufferedReader bbr = new BufferedReader(new FileReader(basic_file));
		String s = null;
		while((s=bbr.readLine())!= null){
			basictxt = basictxt+s;
		}
		bbr.close();
		
		BufferedReader abr = new BufferedReader(new FileReader(action_file));
		s=null;
		while((s=abr.readLine())!= null){
			actintxt = actintxt+s;
		}
		abr.close();
		
		//�˴�Ӧ��Ϊ�ϴ���������������ֱ��������������ݿ��ļ���
		
		File directory = new File(sdpath.getAbsolutePath()+
				 "/ifit/dbfile/User_WeekTrain/"+Home_page.UsingName+"/"+weekday+"/");
		
		File save_action = new File(directory,"action_information.txt");
		File save_basic = new File(directory,"basic_information.txt");
		
		FileOutputStream saveaction = new FileOutputStream(save_action);
		saveaction.write(actintxt.getBytes());
		saveaction.close();
		
		FileOutputStream savebasic = new FileOutputStream(save_basic);
		savebasic.write(basictxt.getBytes());
		savebasic.close();
	}
	
	
	class MyTextChangeListen implements TextWatcher{
		

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(list !=null && adapter !=null){
				list.clear();
				adapter.clear();
				for(int key : adapter.ischeck.keySet()){
					adapter.ischeck.put(key, false);
				}
			}
			
			
			String lastpath = librarypath.toString()+getpinying(search_place.getText().toString())+
					getpinying(search_purpose.getText().toString());
			
			//Log.d("xxx", getpinying(search_place.getText().toString()).equals("")+"");
			if(getpinying(search_place.getText().toString()).equals("/")
					&&!getpinying(search_purpose.getText().toString()).equals("/")){
				
				another_getpath(librarypath,getpinying(search_purpose.getText().toString()));
				
			}else{
				File path = new File(lastpath);
				getplan(path);
			}
			
			adapter.notifyDataSetChanged();
			
		}
		
	}
	
	
	
	
	public void another_getpath(File path,String name){
		
		String lastname = name.substring(1, name.length()-1);
		
		
		for(File file : path.listFiles()){
			if (file.isFile()){
				if(file.getName().equals("basic_information.txt")&&(
						file.getParentFile().getParentFile().getName().equals(lastname)||
						file.getParentFile().getParentFile().getParentFile().getName().equals(lastname))){
					setlist(readtxt(file),file);
				}
			}else if(file.isDirectory()){
				//Log.d("xxx", file.getName());
				another_getpath(file,name);
			}
			
		}
	}
	
	public String getpinying(String name){
		
		String pinyin = "/";
		
		switch(name){
		case "ȫ��":
			break;
		case "����":
			pinyin = "/gymfit/";
			break;
		case "��":
			pinyin = "/homefit/";
			break;
		case "����":
			pinyin = "/zengji/";
			break;
		case "��֬":
			pinyin = "/jianzhi/";
			break;
		case "����":
			pinyin = "/suxing/";
			break;
		}
		return pinyin;
	}
}
