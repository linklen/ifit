package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ifit.app.R;
import com.ifit.app.adapter.add_plan_action_adapter;
import com.ifit.app.other.WheelView_small;
import com.ifit.app.other.add_action_listItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class add_plan_action extends Activity {

	private WheelView_small WV_left,WV_center,WV_right;
	private ImageView btn_back;
	private TextView btn_save;
	public File sdCard,directory;
	
	
	private String[] left = new String[]{"��ͥ����","��е����","�����˶�","����"};
	private String[] center_home = new String[]{"����","�ز�","�粿","����","�ֱ�",
			"�β�","С��"};
	private String[] right_home_beibu= new String[]{"ȫ��"};
	
	private String pingyinPath_left = "";
	private String pingyinPath_center = "";
	private String pingyinPath_right= "";
	
	private ListView action_display_list;
	private List<add_action_listItem> list = new ArrayList<add_action_listItem>();
	private add_plan_action_adapter adapter;
	private add_action_listItem list_item;
	
	private JSONArray jsonArray;
	
	public static Map<String,add_action_listItem> itemCache 
	= new HashMap<String,add_action_listItem>();
	public static List<String> MapKey = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_plan_action);
		
		initControls();
		initpath();
		getJson_file();
		initList();
		setVMlisten();
		setVMControlsData();
		
		setlisten();
		
	}
	
	
	public void initList(){
		adapter = new add_plan_action_adapter(this, R.layout.add_plan_action_listitem, list);
		action_display_list.setAdapter(adapter);
	}
	
	public void initControls(){
		
		btn_back = (ImageView)findViewById(R.id.add_plan_action_back);
		btn_save = (TextView)findViewById(R.id.add_plan_action_save);
		WV_left  = (WheelView_small)findViewById(R.id.add_plan_action_select_left);
		WV_center = (WheelView_small)findViewById(R.id.add_plan_action_select_center);
		WV_right = (WheelView_small)findViewById(R.id.add_plan_action_select_right);
		action_display_list = (ListView)findViewById(R.id.add_plan_action_listview);
		 
		 
		 
	}
	
	public void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/Unzip/");
	}
	
	public void setVMControlsData(){
		
		WV_left.setOffset(1);
		WV_left.setItems(Arrays.asList(left));
		pingyinPath_left = "homefit";
		
		WV_center.setOffset(1);
		WV_center.setItems(Arrays.asList(center_home));
		pingyinPath_center = "beibu";
		
		WV_right.setOffset(1);
		WV_right.setItems(Arrays.asList(right_home_beibu));
		WV_right.setSeletion(0);
		WV_right.onSeletedCallBack();
	}
	
	
	public void setlisten(){

		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemCache.clear();
				MapKey.clear();
				finish();
			}
		});
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		action_display_list.setOnItemClickListener(new MyOnItemClickListen());
	}
	
	
	class MyOnItemClickListen implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			list_item = adapter.getItem(position);
			Intent open_detail = new Intent(add_plan_action.this,action_detail.class);
			open_detail.putExtra("pngPath", list_item.getImgPath());
			startActivity(open_detail);
		}
		
	}
	
	
	
	
	public void setVMlisten(){
		
		
		
		//����Ķ���VM�ļ���
		WV_left.setOnWheelViewListener(new WheelView_small.OnWheelViewListener(){
			
            @Override
            public void onSelected(int selectedIndex, String item) {
                //Log.d("xxx", "selectedIndex: " + selectedIndex + ", item: " + item);
            	
            	
            	
            	switch(item){
            	case "��ͥ����":
            		//Log.d("xxx", "111");
            		pingyinPath_left = "homefit";
            		WV_center.Dynamic_refresh(Arrays.asList(center_home));
            		//Log.d("xxx", Arrays.asList(center_home).toString());
            		
            		break;
            	case "��е����":
            		
            		pingyinPath_left = "gymfit";
            		String[] center_gym = new String[]{"����","�ز�"};
            		WV_center.Dynamic_refresh(Arrays.asList(center_gym));
            		
            		break;
            	case "�����˶�":
            		
            		pingyinPath_left = "youyangyundong";
            		String[] center_O = new String[]{"ȫ��"};
            		WV_center.Dynamic_refresh(Arrays.asList(center_O));
            		
            		break;
            	case "����":
            		
            		pingyinPath_left = "lashen";
            		String[] center_extent = new String[]{"ȫ��"};
            		WV_center.Dynamic_refresh(Arrays.asList(center_extent));
            		
            		break;
            	}
            }
        });
		
		
		WV_center.setOnWheelViewListener(new WheelView_small.OnWheelViewListener(){
			@Override
            public void onSelected(int selectedIndex, String item) {
				
				switch(WV_left.getSeletedItem()){
				case "��ͥ����":
					switch(item){
	            	case "����":
	            		
	            		pingyinPath_center = "beibu";
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_beibu));
	            		
	            		break;
	            	case "�ز�":
	            		
	            		pingyinPath_center = "xiongbu";
	            		String[] right_home_xiongbu = new String[]{"ȫ��","�ؼ�����","�ؼ��ϲ�","�ؼ��з�","�ؼ��²�"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_xiongbu));
	            		
	            		break;
	            	case "�粿":
	            		
	            		pingyinPath_center = "jianbu";
	            		String[] right_home_jianbu = new String[]{"ȫ��","���Ǽ�����","���Ǽ�ǰ��","���Ǽ�����","���Ǽ�����"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_jianbu));
	            		
	            		break;
	            	case "����":
	            		
	            		pingyinPath_center = "hexin";
	            		String[] right_home_hexin = new String[]{"ȫ��","����","�±���Ⱥ"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_hexin));
	            		
	            		break;
	            	case "�ֱ�":
	            		
	            		pingyinPath_center = "shoubi";
	            		String[] right_home_shoubi = new String[]{"ȫ��","�Ŷ�ͷ��","����ͷ��"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_shoubi));
	            		
	            		break;
	            	case "�β�":
	            		
	            		pingyinPath_center = "tunbu";
	            		String[] right_home_tunbu = new String[]{"ȫ��","���ȼ�Ⱥ","����ǰ��","�����ڲ�","���Ⱥ��","�μ�"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_tunbu));
	            		
	            		break;
	            	case "С��":
	            		
	            		pingyinPath_center = "xiaotui";
	            		String[] right_home_xiaotui = new String[]{"ȫ��"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_xiaotui));
	            		
	            		break;
	            	}
					break;
				case "��е����":
					switch(item){
	            	case "����":
	            		
	            		pingyinPath_center = "beibu";
	            		String[] right_gym_beibu = new String[]{"ȫ��","������","�ϱ���Ⱥ","�±���Ⱥ"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_gym_beibu));
	            		
	            		break;
	            	case "�ز�":
	            		
	            		pingyinPath_center = "xiongbu";
	            		String[] right_home_xiongbu = new String[]{"ȫ��","�ؼ�����","�ؼ��ϲ�","�ؼ��з�","�ؼ��²�"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_xiongbu));
	            		
	            		break;
	            	}
					break;
				case "�����˶�":
					switch(item){
	            	case "ȫ��":
	            		
	            		pingyinPath_center = "";
	            		String[] right_O = new String[]{"ȫ��"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_O));
	            		
	            		break;
	            	}
					break;
				case "����":
					switch(item){
	            	case "ȫ��":
	            		
	            		pingyinPath_center = "";
	            		String[] right_extent = new String[]{"ȫ��"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_extent));
	            		
	            		break;
	            	}
					break;
				}
            	
            }
		});
		
		WV_right.setOnWheelViewListener(new WheelView_small.OnWheelViewListener(){
			@Override
            public void onSelected(int selectedIndex, String item) {
				getpingyin_left(item);
				String lastpath = directory + "/"+
					pingyinPath_left+"/"+pingyinPath_center+"/"+pingyinPath_right+"/";
					//Log.d("xxx", lastpath);
				if(list !=null && adapter !=null){
					list.clear();
					adapter.clear();
				}
					serch_imgpath(lastpath);
					//initList();
					adapter.notifyDataSetChanged();
					
			}
		});
	}
	
	
	public void serch_imgpath(String path){
		
		File lastpath = new File (path);
		String imgpath = "";
		String imgFilename = "";
		for (File file : lastpath.listFiles()) {
			if (file.isFile()){
				imgpath = file.toString();// �鵽�ļ������·����ֵ
				imgFilename = file.getName();
				//Log.d("xxx", imgFilename);
				if(imgFilename.substring(imgFilename.length()-4).equals(".png")){ //�����png��ʽ�Ļ�ȡ����ȡ����
				//getItem_name(imgpath);
					//Log.d("xxx", getname+"xxx1111111111111111");
					//Log.d("xxx", "111");
				addItem_to_list(imgpath,getItem_name(imgFilename));//ÿ�β鵽�ļ����ͽ��ж������Ʋ�ѯ������ֱ����ӵ�list
				}//������в�ѯjson�����ͼƬ��Ӧ������
				//Log.d("xxx", imgpath);
				}
			else if (file.isDirectory()){
				serch_imgpath(file.toString()); }// �ݹ�ķ�ʽ����������ļ�
		}
		
	}
	
	
	public void addItem_to_list(String path,String name){
		
			list_item = new add_action_listItem(path, name);
			list.add(list_item);
		
	}
	//��ȡ�ļ���Ӧ����
	public String getItem_name(String imgFilename){
		
		String imgname = imgFilename.substring(0, imgFilename.length()-11);
		//Log.d("xxx", imgname+"123");
		String name = "";
		try {
		for(int i =0;i<jsonArray.length();i++){
			
				//Log.d("xxx", i+"");
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String pinyin_name = jsonObject.getString("pingyin_name");
				
				//Log.d("xxx", pinyin_name);
				//Log.d("xxx", imgname+"123");
				if(pinyin_name.equals(imgname)){
					name = jsonObject.getString("name");
					//Log.d("xxx", name);
					i = jsonArray.length();
				}
			
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return name;
		
	}
	
	//��ȡjson�ı�����ʵ����
	public void getJson_file() {
		File jsonpath = new File(directory,"describe.json");
		
		// ��scannerɨ���ļ���֮ǰ������bufferreader
		
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
	public void getpingyin_left(String item){
		switch(item){
		case "ȫ��":
			pingyinPath_right = "";
			break;
		case "�ؼ�����":
			pingyinPath_right = "xiongjizhengti";
			break;
		case "�ؼ��ϲ�":
			pingyinPath_right = "xiongjishangbu";
			break;
		case "�ؼ��з�":
			pingyinPath_right = "xiongjizhongfeng";
			break;
		case "�ؼ��²�":
			pingyinPath_right = "xiongjixiabu";
			break;
		case "���Ǽ�����":
			pingyinPath_right = "sanjiaojizhengti";
			break;
		case "���Ǽ�ǰ��":
			pingyinPath_right = "sanjiaojiqianshu";
			break;
		case "���Ǽ�����":
			pingyinPath_right = "sanjiaojizhongshu";
			break;
		case "���Ǽ�����":
			pingyinPath_right = "sanjiaojihoushu";
			break;
		case "����":
			pingyinPath_right = "fuji";
			break;
		case "�±���Ⱥ":
			pingyinPath_right = "xiabeijiqun";
			break;
		case "�Ŷ�ͷ��":
			pingyinPath_right = "gongertouji";
			break;
		case "����ͷ��":
			pingyinPath_right = "gongsantouji";
			break;
		case "���ȼ�Ⱥ":
			pingyinPath_right = "tuntuijiqun";
			break;
		case "����ǰ��":
			pingyinPath_right = "datuiqiance";
			break;
		case "�����ڲ�":
			pingyinPath_right = "datuineice";
			break;
		case "���Ⱥ��":
			pingyinPath_right = "datuihouce";
			break;
		case "������":
			pingyinPath_right = "beikuoji";
			break;
		case "�ϱ���Ⱥ":
			pingyinPath_right = "shangbeijiqun";
			break;
		}
	}

}
