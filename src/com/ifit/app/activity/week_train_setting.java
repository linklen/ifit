package com.ifit.app.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ifit.app.R;
import com.ifit.app.other.week_train_create_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class week_train_setting extends Activity {

	private Button monday,tuesday,wednesday,thursday,friday,saturday,sunday;
	private File sdCard,directory;
	private boolean one,two,three,four,five,six,seven;
	private boolean[] x = {one,two,three,four,five,six,seven}; 
	private LinearLayout add_view_layout;
	private ImageView btn_back;
	private week_train_create_view cr=new week_train_create_view();
	private Map<String,View> Viewcache = new HashMap<String,View>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.week_train_setting);
		
		initContral();
		initpath();
		isSetselect();
		setImgSelect();
		setimglisten();
		
	}
	
	public void setimglisten(){
		monday.setOnClickListener(new MyOnClickListen());
		tuesday.setOnClickListener(new MyOnClickListen());
		wednesday.setOnClickListener(new MyOnClickListen());
		thursday.setOnClickListener(new MyOnClickListen());
		friday.setOnClickListener(new MyOnClickListen());
		saturday.setOnClickListener(new MyOnClickListen());
		sunday.setOnClickListener(new MyOnClickListen());
		btn_back.setOnClickListener(new MyOnClickListen());
	}
	
	public void setImgSelect(){
		
		String[] week = {"����һ","���ڶ�","������","������","������","������","������"};
		Button[] img = {monday,tuesday,wednesday,thursday,friday,saturday,sunday};
		
		for(int i= 0;i<7;i++){
			
			
			if(x[i]){
				img[i].setSelected(true);
				add_view(week[i]);
			}else{
				img[i].setSelected(false);
				
			}
		}
		
	}
	
	public void isSetselect(){
		String[] week = {"����һ","���ڶ�","������","������","������","������","������"};
		
		for(int i = 0;i<7;i++){
			File getpath = new File (directory+"/"+week[i]+"/");
			
			if(!getpath.exists()){
				getpath.mkdirs();}
			
			String[] m = getpath.list();
			if(m.length>0){
				x[i]=true;
			}else{
				x[i]=false;
			}
			
		}
		
	}

	public void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/dbfile/User_WeekTrain/"+Home_page.UsingName+"/");
	}
	public void initContral(){
		monday = (Button)findViewById(R.id.monday);
		tuesday = (Button)findViewById(R.id.tuesday);
		wednesday = (Button)findViewById(R.id.wednesday);
		thursday = (Button)findViewById(R.id.thursday);
		friday = (Button)findViewById(R.id.friday);
		saturday = (Button)findViewById(R.id.saturday);
		sunday = (Button)findViewById(R.id.sunday);
		add_view_layout = (LinearLayout)findViewById(R.id.week_train_add_view_layout);
		btn_back = (ImageView)findViewById(R.id.week_train_set_btnback);
	}
	
	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch(v.getId()){
			case R.id.monday:
				if(monday.isSelected()){
					//monday.setSelected(false);
					deleteplan("����һ",monday);
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					monday.setSelected(true);
					add_view("����һ");
					//�����ͼ
				}
				
				break;
			case R.id.tuesday:
				if(tuesday.isSelected()){
					deleteplan("���ڶ�",tuesday);
					//tuesday.setSelected(false);
					//add_view_layout.removeView(Viewcache.get("���ڶ�"));
					//Viewcache.remove("���ڶ�");
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					tuesday.setSelected(true);
					add_view("���ڶ�");
					//�����ͼ
				}
				break;
			case R.id.wednesday:
				if(wednesday.isSelected()){
					//wednesday.setSelected(false);
					//add_view_layout.removeView(Viewcache.get("������"));
					//Viewcache.remove("������");
					deleteplan("������",wednesday);
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					wednesday.setSelected(true);
					add_view("������");
					//�����ͼ
				}
				
				break;
			case R.id.thursday:
				if(thursday.isSelected()){
					//thursday.setSelected(false);
					//add_view_layout.removeView(Viewcache.get("������"));
					//Viewcache.remove("������");
					deleteplan("������",thursday);
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					thursday.setSelected(true);
					add_view("������");
					//�����ͼ
				}
				
				break;
			case R.id.friday:
				if(friday.isSelected()){
					//friday.setSelected(false);
					//add_view_layout.removeView(Viewcache.get("������"));
					//Viewcache.remove("������");
					deleteplan("������",friday);
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					friday.setSelected(true);
					add_view("������");
					//�����ͼ
				}
				
				break;
			case R.id.saturday:
				if(saturday.isSelected()){
					//saturday.setSelected(false);
					//add_view_layout.removeView(Viewcache.get("������"));
					//Viewcache.remove("������");
					deleteplan("������",saturday);
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					saturday.setSelected(true);
					add_view("������");
					//�����ͼ
				}
				
				break;
			case R.id.sunday:
				if(sunday.isSelected()){
					//sunday.setSelected(false);
					//add_view_layout.removeView(Viewcache.get("������"));
					//Viewcache.remove("������");
					deleteplan("������",sunday);
					//ɾ����Ӧ��ͼ
					//��ɾ���ļ����ڶ���
				}else{
					sunday.setSelected(true);
					add_view("������");
					//�����ͼ
				}
				
				break;
				
			case R.id.week_train_set_btnback:
				finish();
				break;
			}
			
		}
		
	}
	
	public void add_view(String weekday){
		
		Button[] img = {monday,tuesday,wednesday,thursday,friday,saturday,sunday};
		int x = 0;
		View view = new View(week_train_setting.this);
		switch(weekday){
		case "����һ":
			view = cr.createView(weekday, week_train_setting.this);
			add_view_layout.addView(view,0);
			Viewcache.put(weekday, view);
			break;
		case "���ڶ�":
			view = cr.createView(weekday, week_train_setting.this);
			for (int i = 0; i < 1; i++) {
				if (img[i].isSelected()) {
					x++;
				}
			}
			add_view_layout.addView(view, x);
			Viewcache.put(weekday, view);
			break;
		case "������":
			view = cr.createView(weekday, week_train_setting.this);
			for (int i = 0; i < 2; i++) {
				if (img[i].isSelected()) {
					x++;
				}
			}
			add_view_layout.addView(view, x);
			Viewcache.put(weekday, view);
			break;
		case "������":
			view = cr.createView(weekday, week_train_setting.this);
			for (int i = 0; i < 3; i++) {
				if (img[i].isSelected()) {
					x++;
				}
			}
			add_view_layout.addView(view, x);
			Viewcache.put(weekday, view);
			break;
		case "������":
			view = cr.createView(weekday, week_train_setting.this);
			for (int i = 0; i < 4; i++) {
				if (img[i].isSelected()) {
					x++;
				}
			}
			add_view_layout.addView(view, x);
			Viewcache.put(weekday, view);
			break;
		case "������":
			view = cr.createView(weekday, week_train_setting.this);
			for (int i = 0; i < 5; i++) {
				if (img[i].isSelected()) {
					x++;
				}
			}
			add_view_layout.addView(view, x);
			Viewcache.put(weekday, view);
			break;
		case "������":
			view = cr.createView(weekday, week_train_setting.this);
			for (int i = 0; i < 6; i++) {
				if (img[i].isSelected()) {
					x++;
				}
			}
			add_view_layout.addView(view, x);
			Viewcache.put(weekday, view);
			break;
		}
		
	}

	
	public void deleteplan(final String weekday,final Button btn){
		
		
		new AlertDialog.Builder(this).setMessage("���Ҫɾ��"+weekday+"�ļƻ���?")
		.setPositiveButton("ȡ��", null).setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				add_view_layout.removeView(Viewcache.get(weekday));
				Viewcache.remove(weekday);
				File getpath = new File (directory+"/"+weekday+"/");
				for(File file :getpath.listFiles()){
					if(file.isFile()){
						file.delete();
					}
				}
				btn.setSelected(false);
				
			}
		}).show();
	}
	
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(cr.whichclick!=null){
		add_view_layout.removeView(Viewcache.get(cr.whichclick));
		Viewcache.remove(cr.whichclick);
		add_view(cr.whichclick);
		cr.whichclick=null;}
	}
	
	
	
}
