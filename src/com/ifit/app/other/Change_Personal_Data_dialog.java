package com.ifit.app.other;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifit.app.R;
import com.ifit.app.other.MyPickView.onSelectListener;

public class Change_Personal_Data_dialog extends Activity {

	private MyPickView mPickView;
	private TextView cancle,complete,tips;
	private RelativeLayout pickView_layout,editText_layout;
	private LinearLayout Btn_layout;
	private Button btn_first,btn_second,btn_third;
	
	private EditText edit_nick,edit_region,edit_introduction;
	private ImageView getLocationImage;
	private TextView editText_check;
	
	public int setMinNumber=0,setMaxNumber;
	public int selectposition=0;
	public String getString = "";
	private int get_intent_id;
	private String getType;
	private String return_data;
	
	private LocationManager locationManager;
	private ProgressDialog positioning;
	private String ak ="01PX6GxUQL8kkvdnZxgQ2NcV";
	private String mcode = "AF:3E:0F:A3:2D:75:6A:88:73:5E:98:D2:55:DE:E9:B6:C3:04:0B:61;" +
			"com.ifit.app";

	public SharedPreferences locationdata;
	public static final int SHOW_LOCATION = 0;
	public static final int TIME_OUT = 1;
    private Thread position_Thread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_personal_data_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//让弹窗全屏，在样式里设不设置都可以<item name="android:windowFullscreen">true</item>  
		locationdata = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		//必要控件
		cancle = (TextView)findViewById(R.id.select_personal_data_cancel);
		complete = (TextView)findViewById(R.id.select_personal_data_complete);
		tips = (TextView)findViewById(R.id.select_personal_data_tip);
		

		cancle.setOnClickListener(new MyOnClickListener());
		complete.setOnClickListener(new MyOnClickListener());
		
		
		Intent intent = getIntent();
		get_intent_id = intent.getIntExtra("getId",0);
		getType = intent.getStringExtra("type");
		
		
		if(getType.equals("Number")){
			
			pickView_layout = (RelativeLayout)findViewById(R.id.select_personal_data_PickView_layout);

			mPickView = (MyPickView)findViewById(R.id.myPickView);
			
			//设置可见属性
			pickView_layout.setVisibility(View.VISIBLE);
			
			String get_text = intent.getStringExtra("getNumber");
			
			switch(get_intent_id){
			case R.id.editor_personal_data_age:
				tips.setText("年龄");
				setMinNumber = 15;
				setMaxNumber = 99;
				break;
			case R.id.editor_personal_data_body_height:
				tips.setText("身高");
				setMinNumber = 120;
				setMaxNumber = 250;
				break;
			case R.id.editor_personal_data_body_weight:
				tips.setText("体重");
				setMinNumber = 30;
				setMaxNumber = 200;
				break;
			case R.id.editor_personal_data_Waist_circumference:
				tips.setText("腰围");
				setMinNumber = 30;
				setMaxNumber = 200;
				break;
				}
			
			List<String> data = new ArrayList<String>();
			
			for (int i = 0; setMinNumber < setMaxNumber; setMinNumber++,i++)
			{
				data.add("" + setMinNumber);
				if(data.get(i).equals(get_text)){
					selectposition = i;
				}
			}
			
			mPickView.setData(data);
			if(selectposition!=0){
			mPickView.setSelected(selectposition);
			return_data =data.get(selectposition);}
			else if(get_intent_id == R.id.editor_personal_data_age){
				mPickView.setSelected(10);
				return_data =data.get(10);
			}else{
				return_data =data.get(data.size()/2);
			}
			
			
			
			mPickView.setOnSelectListener(new onSelectListener() {
				
				@Override
				public void onSelect(String text) {
					// TODO Auto-generated method stub
					//Toast.makeText(Change_Personal_Data_dialog.this, "当前的选择是" + text,
							//Toast.LENGTH_SHORT).show();
					return_data = text;
				}
			});
		}else if(getType.equals("Button")){
			
			Btn_layout = (LinearLayout)findViewById(R.id.select_personal_data_Btn_layout);
			
			btn_first = (Button)findViewById(R.id.select_personal_data_btn_first);
			btn_second = (Button)findViewById(R.id.select_personal_data_btn_second);
			btn_third = (Button)findViewById(R.id.select_personal_data_btn_third);
			
			getString = intent.getStringExtra("getString");
			//设置可见性 
			Btn_layout.setVisibility(View.VISIBLE);
			
			switch(get_intent_id){
			case R.id.editor_personal_data_sex:
				tips.setText("性别");
				btn_first.setText("男");
				btn_second.setVisibility(View.GONE);
				btn_third.setText("女");
				if(getString.equals("女")){
					btn_third.setSelected(true);
				}else{
					btn_first.setSelected(true);
				}
				break;
			case R.id.editor_personal_data_training_experience:
				tips.setText("训练经验");
				btn_first.setText("初级");
				btn_second.setText("中级");
				btn_third.setText("高级");
				if(getString.equals("高级")){
					btn_third.setSelected(true);
				}else if(getString.equals("中级")){
					btn_second.setSelected(true);
				}else{
					btn_first.setSelected(true);
				}
				break;
			case R.id.editor_personal_data_training_purpose:
				tips.setText("训练目的");
				btn_first.setText("减脂");
				btn_second.setText("增肌");
				btn_third.setText("塑形");
				if(getString.equals("塑形")){
					btn_third.setSelected(true);
				}else if(getString.equals("增肌")){
					btn_second.setSelected(true);
				}else{
					btn_first.setSelected(true);
				}
				break;
				}
			
			btn_first.setOnClickListener(new MyOnClickListener());
			btn_second.setOnClickListener(new MyOnClickListener());
			btn_third.setOnClickListener(new MyOnClickListener());
		}else{
			//当时为了设置不同的最大长度才分成3个
			editText_layout = (RelativeLayout)findViewById(R.id.select_personal_data_EditText_layout);
			
			editText_layout.setVisibility(View.VISIBLE);
			//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
			
			editText_check = (TextView)findViewById(R.id.select_personal_data_EditText_CheckNumber);
			
			getString = intent.getStringExtra("getString");
			
			int now_Number = 0;
			
			switch(get_intent_id){
			case R.id.editor_personal_data_nickname:
				edit_nick = (EditText)findViewById(R.id.select_personal_data_EditNick);
				edit_nick.setVisibility(View.VISIBLE);
				edit_nick.setText(getString);
				tips.setText("修改昵称");
				now_Number = 10-edit_nick.length();
				editText_check.setText(now_Number+"/10");
				edit_nick.addTextChangedListener(new MyTextWatcher("edit_nick",now_Number));
				break;
			case R.id.editor_personal_data_region:
				getLocationImage = (ImageView)findViewById(R.id.select_personal_data_getLocationImage);
				
				//positioning = (ProgressBar)findViewById(R.id.positioning);
				
				positioning = new ProgressDialog(Change_Personal_Data_dialog.this);
				positioning.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				positioning.setMessage("请稍等,需要一段时间用来定位...");
				positioning.setIndeterminate(false);//最大值和最小值之间来回滚动
				positioning.setCancelable(true);
				positioning.setOnCancelListener(new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						//Log.d("xxx", "okokoko");
						
						Toast.makeText(Change_Personal_Data_dialog.this, 
								"您取消了自动定位！", Toast.LENGTH_SHORT).show();
						if(locationManager != null){
							locationManager.removeUpdates(locationListener);
						}
					}
				});
				
				
				
				edit_region = (EditText)findViewById(R.id.select_personal_data_Editregion);
				edit_region.setVisibility(View.VISIBLE);
				tips.setText("修改地区");
				edit_region.setText(getString);
				now_Number = 50-edit_region.length();
				editText_check.setText(now_Number+"/50");
				getLocationImage.setVisibility(View.VISIBLE);
				edit_region.addTextChangedListener(new MyTextWatcher("edit_region",now_Number));
				getLocationImage.setOnClickListener(new MyOnClickListener());
				break;
			case R.id.editor_personal_data_introduction:
				edit_introduction = (EditText)findViewById(R.id.select_personal_data_Editintroduction);
				edit_introduction.setVisibility(View.VISIBLE);
				tips.setText("修改简介");
				edit_introduction.setText(getString);
				now_Number = 60-edit_introduction.length();
				editText_check.setText(now_Number+"/60");
				edit_introduction.addTextChangedListener(new MyTextWatcher("edit_introduction",now_Number));
				break;
			}
			
		}
		

		
		

		
		
		
	}

	
	//监视字数改变
	class MyTextWatcher implements TextWatcher{
		
		
		private String mEditName,Limit_Number;
		private int mNow_Number;
		public MyTextWatcher(String EditName,int now_Number){
			mEditName = EditName;
			mNow_Number = now_Number;
			switch(EditName){
			case "edit_nick":
				Limit_Number = "/10";
				break;
			case "edit_region":
				Limit_Number = "/50";
				break;
			case "edit_introduction":
				Limit_Number = "/60";
				break;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			switch(mEditName){
			case "edit_nick":
				mNow_Number = 10-s.length();
				editText_check.setText(mNow_Number + Limit_Number);
				break;
			case "edit_region":
				mNow_Number = 50-s.length();
				editText_check.setText(mNow_Number + Limit_Number);
				break;
			case "edit_introduction":
				mNow_Number = 60-s.length();
				editText_check.setText(mNow_Number + Limit_Number);
				break;
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		
			
			
		}
		
	}
	
	//按钮监控
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.select_personal_data_cancel:
				finish();
				break;
			case R.id.select_personal_data_complete:
				Intent send_text_back = new Intent();
				send_text_back.putExtra("return_id", get_intent_id);
				switch(getType){
				case "Number":
					send_text_back.putExtra("return_data", return_data);
					break;
				case "Button":
					if(btn_first.isSelected()){
						return_data = btn_first.getText().toString();
					}else if(btn_second.isSelected()){
						return_data = btn_second.getText().toString();
					}else{
						return_data = btn_third.getText().toString();
					}
					send_text_back.putExtra("return_data", return_data);
					break;
				case "EditText":
					if(get_intent_id == R.id.editor_personal_data_nickname){
						if(!edit_nick.getText().toString().equals("")){
						return_data = edit_nick.getText().toString();}else{
							return_data = getString;
						}
					}else if(get_intent_id == R.id.editor_personal_data_region){
						return_data = edit_region.getText().toString();
					}else{
						
						return_data = edit_introduction.getText().toString();
						}
					
					send_text_back.putExtra("return_data", return_data);
					break;
				}
				
				setResult(RESULT_OK,send_text_back);
				finish();
				
				break;
			case R.id.select_personal_data_btn_first:
				btn_first.setSelected(true);
				btn_second.setSelected(false);
				btn_third.setSelected(false);
				break;
			case R.id.select_personal_data_btn_second:
				btn_first.setSelected(false);
				btn_second.setSelected(true);
				btn_third.setSelected(false);
				break;
			case R.id.select_personal_data_btn_third:
				btn_first.setSelected(false);
				btn_second.setSelected(false);
				btn_third.setSelected(true);
				break;
			case R.id.select_personal_data_getLocationImage://获取地理位置按钮接口
				positioning.show();
				//positioning.setVisibility(View.VISIBLE);
				locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
				String provider;
				//获取所有可用的位置提供器
				List<String> providerList = locationManager.getProviders(true);
				if(providerList.contains(LocationManager.GPS_PROVIDER)){
					provider = LocationManager.GPS_PROVIDER;
				}else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
					provider = LocationManager.NETWORK_PROVIDER;
				}else{
					//当没有可用的位置提供器是，弹出提示框
					Toast.makeText(Change_Personal_Data_dialog.this, 
							"请到设置中心开启定位服务", Toast.LENGTH_SHORT).show();

					return;
				}
				
				Location location = locationManager.getLastKnownLocation(provider);
				
				if(location != null){
					//显示当前设备的位置信息
					showLocation(location);
					
				}
				locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
				
				break;
				
			}
		}

	}
	
	LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			showLocation(location);
		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	
	private void showLocation(final Location location) {
		// TODO Auto-generated method stub
		/*String currentPosition = "latitude is " + location.getLatitude() +"\n"
				+ "longitude is "+location.getLongitude();
		edit_region.setText(currentPosition);
		*/
		
		position_Thread = new Thread(new Runnable(){

			
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//while(!Thread_EXIT){
					
					try{
						//组装反向地理编码的接口地址
						StringBuilder url = new StringBuilder();
						//url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
						url.append("http://api.map.baidu.com/geocoder/v2/?");
						url.append("ak=").append(ak);
						url.append("&mcode=").append(mcode);
						url.append("&location=");
						url.append(location.getLatitude()).append(",");
						url.append(location.getLongitude());
						url.append("&output=json&pois=0");
						//url.append("&sensor=false");
						HttpClient httpClient = new DefaultHttpClient();
						HttpGet httpGet = new HttpGet(url.toString());
						//在请求消息头中指定语言，保证服务器会返回中文数据SS
						
						//httpGet.addHeader("Accept-Language","zh-CN");
						HttpResponse httpResponse = httpClient.execute(httpGet);
						//String i = httpResponse.toString();
						//Log.d("xxx", i);
						//Log.d("xxx", ""+httpResponse.getStatusLine().getStatusCode());
						if(httpResponse.getStatusLine().getStatusCode() == 200){
							//Log.d("xxx", ""+httpResponse.getStatusLine().getStatusCode());
							HttpEntity entity = httpResponse.getEntity();
							String response = EntityUtils.toString(entity,"utf-8");
							//Log.d("xxx", response);
							JSONObject jsonObject = new JSONObject(response);
							String result = jsonObject.getString("result");
							JSONObject result_jsonObject = new JSONObject(result);
							
							//JSONArray resultArray = jsonObject.getJSONArray("results");
							
							String formatted_address = result_jsonObject.getString("formatted_address");
							
							//Log.d("xxx", formatted_address);
							//if(resultArray.length() > 0){
							if(!formatted_address.equals("")){
								//JSONObject subObject = resultArray.getJSONObject(0);//获取json数组中的第一项 
								//取出格式化后的位置信息
								String addressComponent = result_jsonObject.getString("addressComponent");
								JSONObject addressComponent_jsonObject = new JSONObject(addressComponent);
								
								String province = addressComponent_jsonObject.getString("province");
								String city = addressComponent_jsonObject.getString("city");
								String district = addressComponent_jsonObject.getString("district");
								String address =province+" "+city+" "+district;
								
								SharedPreferences.Editor editor = locationdata.edit();
								editor.putString("district", district);
								editor.commit();
								editor.clear();
								//Log.d("xxx", address);
								//JSONArray address_components_Array = subObject.getJSONArray("address_components");
								/*for(int i=4;i>=1;i--){
								JSONObject detailed = address_components_Array.getJSONObject(i);
								String temp_address = detailed.getString("long_name");
								//Log.d("xxx",temp_address );
								if(i==1){
								address = address+temp_address;
								}else{
									address = address+temp_address+" ";
								}
								}*/
								//String address = subObject.getString("formatted_address");
								Message message = new Message();
								message.what = SHOW_LOCATION;
								message.obj = address;
								handler.sendMessage(message);
							}
						}
					}catch(Exception e ){
					   
						e.printStackTrace();
						//Log.d("xxx", "异常");
						Message message = new Message();
						message.what = TIME_OUT;
						handler.sendMessage(message);
						
					}
				
				
			}
			
		});
		position_Thread.start();
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_LOCATION:
				
				if(positioning.isShowing()){
					
					positioning.dismiss();
					
					String currentPosition = (String)msg.obj;
				
					edit_region.setText(currentPosition);
					
					
				
					if(locationManager != null){
						locationManager.removeUpdates(locationListener);
					}
				}
				break;
			case TIME_OUT:
				if(positioning.isShowing()){
					positioning.dismiss();
					
					if(locationManager != null){
						locationManager.removeUpdates(locationListener);
					}
				
					Toast.makeText(Change_Personal_Data_dialog.this,
							"反向解析出错，无法解析地址！", Toast.LENGTH_SHORT).show();
				}
				
			default:
				break;
			}
		}
	};
	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(0, R.anim.slide_intodown);
		if(locationManager != null){
			locationManager.removeUpdates(locationListener);
		}
		
	}
}
