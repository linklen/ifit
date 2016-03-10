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
		//�õ���ȫ��������ʽ���費���ö�����<item name="android:windowFullscreen">true</item>  
		locationdata = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		//��Ҫ�ؼ�
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
			
			//���ÿɼ�����
			pickView_layout.setVisibility(View.VISIBLE);
			
			String get_text = intent.getStringExtra("getNumber");
			
			switch(get_intent_id){
			case R.id.editor_personal_data_age:
				tips.setText("����");
				setMinNumber = 15;
				setMaxNumber = 99;
				break;
			case R.id.editor_personal_data_body_height:
				tips.setText("���");
				setMinNumber = 120;
				setMaxNumber = 250;
				break;
			case R.id.editor_personal_data_body_weight:
				tips.setText("����");
				setMinNumber = 30;
				setMaxNumber = 200;
				break;
			case R.id.editor_personal_data_Waist_circumference:
				tips.setText("��Χ");
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
					//Toast.makeText(Change_Personal_Data_dialog.this, "��ǰ��ѡ����" + text,
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
			//���ÿɼ��� 
			Btn_layout.setVisibility(View.VISIBLE);
			
			switch(get_intent_id){
			case R.id.editor_personal_data_sex:
				tips.setText("�Ա�");
				btn_first.setText("��");
				btn_second.setVisibility(View.GONE);
				btn_third.setText("Ů");
				if(getString.equals("Ů")){
					btn_third.setSelected(true);
				}else{
					btn_first.setSelected(true);
				}
				break;
			case R.id.editor_personal_data_training_experience:
				tips.setText("ѵ������");
				btn_first.setText("����");
				btn_second.setText("�м�");
				btn_third.setText("�߼�");
				if(getString.equals("�߼�")){
					btn_third.setSelected(true);
				}else if(getString.equals("�м�")){
					btn_second.setSelected(true);
				}else{
					btn_first.setSelected(true);
				}
				break;
			case R.id.editor_personal_data_training_purpose:
				tips.setText("ѵ��Ŀ��");
				btn_first.setText("��֬");
				btn_second.setText("����");
				btn_third.setText("����");
				if(getString.equals("����")){
					btn_third.setSelected(true);
				}else if(getString.equals("����")){
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
			//��ʱΪ�����ò�ͬ����󳤶Ȳŷֳ�3��
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
				tips.setText("�޸��ǳ�");
				now_Number = 10-edit_nick.length();
				editText_check.setText(now_Number+"/10");
				edit_nick.addTextChangedListener(new MyTextWatcher("edit_nick",now_Number));
				break;
			case R.id.editor_personal_data_region:
				getLocationImage = (ImageView)findViewById(R.id.select_personal_data_getLocationImage);
				
				//positioning = (ProgressBar)findViewById(R.id.positioning);
				
				positioning = new ProgressDialog(Change_Personal_Data_dialog.this);
				positioning.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				positioning.setMessage("���Ե�,��Ҫһ��ʱ��������λ...");
				positioning.setIndeterminate(false);//���ֵ����Сֵ֮�����ع���
				positioning.setCancelable(true);
				positioning.setOnCancelListener(new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						//Log.d("xxx", "okokoko");
						
						Toast.makeText(Change_Personal_Data_dialog.this, 
								"��ȡ�����Զ���λ��", Toast.LENGTH_SHORT).show();
						if(locationManager != null){
							locationManager.removeUpdates(locationListener);
						}
					}
				});
				
				
				
				edit_region = (EditText)findViewById(R.id.select_personal_data_Editregion);
				edit_region.setVisibility(View.VISIBLE);
				tips.setText("�޸ĵ���");
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
				tips.setText("�޸ļ��");
				edit_introduction.setText(getString);
				now_Number = 60-edit_introduction.length();
				editText_check.setText(now_Number+"/60");
				edit_introduction.addTextChangedListener(new MyTextWatcher("edit_introduction",now_Number));
				break;
			}
			
		}
		

		
		

		
		
		
	}

	
	//���������ı�
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
	
	//��ť���
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
			case R.id.select_personal_data_getLocationImage://��ȡ����λ�ð�ť�ӿ�
				positioning.show();
				//positioning.setVisibility(View.VISIBLE);
				locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
				String provider;
				//��ȡ���п��õ�λ���ṩ��
				List<String> providerList = locationManager.getProviders(true);
				if(providerList.contains(LocationManager.GPS_PROVIDER)){
					provider = LocationManager.GPS_PROVIDER;
				}else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
					provider = LocationManager.NETWORK_PROVIDER;
				}else{
					//��û�п��õ�λ���ṩ���ǣ�������ʾ��
					Toast.makeText(Change_Personal_Data_dialog.this, 
							"�뵽�������Ŀ�����λ����", Toast.LENGTH_SHORT).show();

					return;
				}
				
				Location location = locationManager.getLastKnownLocation(provider);
				
				if(location != null){
					//��ʾ��ǰ�豸��λ����Ϣ
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
						//��װ����������Ľӿڵ�ַ
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
						//��������Ϣͷ��ָ�����ԣ���֤�������᷵����������SS
						
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
								//JSONObject subObject = resultArray.getJSONObject(0);//��ȡjson�����еĵ�һ�� 
								//ȡ����ʽ�����λ����Ϣ
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
						//Log.d("xxx", "�쳣");
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
							"������������޷�������ַ��", Toast.LENGTH_SHORT).show();
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
