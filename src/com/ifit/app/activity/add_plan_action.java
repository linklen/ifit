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
	
	
	private String[] left = new String[]{"家庭健身","器械健身","有氧运动","拉伸"};
	private String[] center_home = new String[]{"背部","胸部","肩部","核心","手臂",
			"臀部","小腿"};
	private String[] right_home_beibu= new String[]{"全部"};
	
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
		
		
		
		//下面的都是VM的监听
		WV_left.setOnWheelViewListener(new WheelView_small.OnWheelViewListener(){
			
            @Override
            public void onSelected(int selectedIndex, String item) {
                //Log.d("xxx", "selectedIndex: " + selectedIndex + ", item: " + item);
            	
            	
            	
            	switch(item){
            	case "家庭健身":
            		//Log.d("xxx", "111");
            		pingyinPath_left = "homefit";
            		WV_center.Dynamic_refresh(Arrays.asList(center_home));
            		//Log.d("xxx", Arrays.asList(center_home).toString());
            		
            		break;
            	case "器械健身":
            		
            		pingyinPath_left = "gymfit";
            		String[] center_gym = new String[]{"背部","胸部"};
            		WV_center.Dynamic_refresh(Arrays.asList(center_gym));
            		
            		break;
            	case "有氧运动":
            		
            		pingyinPath_left = "youyangyundong";
            		String[] center_O = new String[]{"全部"};
            		WV_center.Dynamic_refresh(Arrays.asList(center_O));
            		
            		break;
            	case "拉伸":
            		
            		pingyinPath_left = "lashen";
            		String[] center_extent = new String[]{"全部"};
            		WV_center.Dynamic_refresh(Arrays.asList(center_extent));
            		
            		break;
            	}
            }
        });
		
		
		WV_center.setOnWheelViewListener(new WheelView_small.OnWheelViewListener(){
			@Override
            public void onSelected(int selectedIndex, String item) {
				
				switch(WV_left.getSeletedItem()){
				case "家庭健身":
					switch(item){
	            	case "背部":
	            		
	            		pingyinPath_center = "beibu";
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_beibu));
	            		
	            		break;
	            	case "胸部":
	            		
	            		pingyinPath_center = "xiongbu";
	            		String[] right_home_xiongbu = new String[]{"全部","胸肌整体","胸肌上部","胸肌中缝","胸肌下部"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_xiongbu));
	            		
	            		break;
	            	case "肩部":
	            		
	            		pingyinPath_center = "jianbu";
	            		String[] right_home_jianbu = new String[]{"全部","三角肌整体","三角肌前束","三角肌中束","三角肌后束"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_jianbu));
	            		
	            		break;
	            	case "核心":
	            		
	            		pingyinPath_center = "hexin";
	            		String[] right_home_hexin = new String[]{"全部","腹肌","下背肌群"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_hexin));
	            		
	            		break;
	            	case "手臂":
	            		
	            		pingyinPath_center = "shoubi";
	            		String[] right_home_shoubi = new String[]{"全部","肱二头肌","肱三头肌"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_shoubi));
	            		
	            		break;
	            	case "臀部":
	            		
	            		pingyinPath_center = "tunbu";
	            		String[] right_home_tunbu = new String[]{"全部","臀腿肌群","大腿前侧","大腿内侧","大腿后侧","臀肌"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_tunbu));
	            		
	            		break;
	            	case "小腿":
	            		
	            		pingyinPath_center = "xiaotui";
	            		String[] right_home_xiaotui = new String[]{"全部"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_xiaotui));
	            		
	            		break;
	            	}
					break;
				case "器械健身":
					switch(item){
	            	case "背部":
	            		
	            		pingyinPath_center = "beibu";
	            		String[] right_gym_beibu = new String[]{"全部","背阔肌","上背肌群","下背肌群"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_gym_beibu));
	            		
	            		break;
	            	case "胸部":
	            		
	            		pingyinPath_center = "xiongbu";
	            		String[] right_home_xiongbu = new String[]{"全部","胸肌整体","胸肌上部","胸肌中缝","胸肌下部"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_home_xiongbu));
	            		
	            		break;
	            	}
					break;
				case "有氧运动":
					switch(item){
	            	case "全部":
	            		
	            		pingyinPath_center = "";
	            		String[] right_O = new String[]{"全部"};
	            		WV_right.Dynamic_refresh(Arrays.asList(right_O));
	            		
	            		break;
	            	}
					break;
				case "拉伸":
					switch(item){
	            	case "全部":
	            		
	            		pingyinPath_center = "";
	            		String[] right_extent = new String[]{"全部"};
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
				imgpath = file.toString();// 查到文件则进行路径赋值
				imgFilename = file.getName();
				//Log.d("xxx", imgFilename);
				if(imgFilename.substring(imgFilename.length()-4).equals(".png")){ //如果是png格式的获取来获取名称
				//getItem_name(imgpath);
					//Log.d("xxx", getname+"xxx1111111111111111");
					//Log.d("xxx", "111");
				addItem_to_list(imgpath,getItem_name(imgFilename));//每次查到文件，就进行动作名称查询，并且直接添加到list
				}//这里进行查询json来获得图片对应的名称
				//Log.d("xxx", imgpath);
				}
			else if (file.isDirectory()){
				serch_imgpath(file.toString()); }// 递规的方式查找里面的文件
		}
		
	}
	
	
	public void addItem_to_list(String path,String name){
		
			list_item = new add_action_listItem(path, name);
			list.add(list_item);
		
	}
	//获取文件对应名字
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
	
	//获取json文本并且实例化
	public void getJson_file() {
		File jsonpath = new File(directory,"describe.json");
		
		// 用scanner扫描文件，之前都是用bufferreader
		
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
		case "全部":
			pingyinPath_right = "";
			break;
		case "胸肌整体":
			pingyinPath_right = "xiongjizhengti";
			break;
		case "胸肌上部":
			pingyinPath_right = "xiongjishangbu";
			break;
		case "胸肌中缝":
			pingyinPath_right = "xiongjizhongfeng";
			break;
		case "胸肌下部":
			pingyinPath_right = "xiongjixiabu";
			break;
		case "三角肌整体":
			pingyinPath_right = "sanjiaojizhengti";
			break;
		case "三角肌前束":
			pingyinPath_right = "sanjiaojiqianshu";
			break;
		case "三角肌中束":
			pingyinPath_right = "sanjiaojizhongshu";
			break;
		case "三角肌后束":
			pingyinPath_right = "sanjiaojihoushu";
			break;
		case "腹肌":
			pingyinPath_right = "fuji";
			break;
		case "下背肌群":
			pingyinPath_right = "xiabeijiqun";
			break;
		case "肱二头肌":
			pingyinPath_right = "gongertouji";
			break;
		case "肱三头肌":
			pingyinPath_right = "gongsantouji";
			break;
		case "臀腿肌群":
			pingyinPath_right = "tuntuijiqun";
			break;
		case "大腿前侧":
			pingyinPath_right = "datuiqiance";
			break;
		case "大腿内侧":
			pingyinPath_right = "datuineice";
			break;
		case "大腿后侧":
			pingyinPath_right = "datuihouce";
			break;
		case "背阔肌":
			pingyinPath_right = "beikuoji";
			break;
		case "上背肌群":
			pingyinPath_right = "shangbeijiqun";
			break;
		}
	}

}
