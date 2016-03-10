package com.ifit.app.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ifit.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Edit_Plan_ChangeData_dialog extends Activity {

	private TextView cancle,complete,tips;
	private WheelView WV;
	private WheelView_small WV_small_group,WV_small_number;
	private EditText edit_name,edit_describe;
	private TextView editText_check;
	private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,
				   btn14,btn15;
	private int btn_select_count = 0;
	private RelativeLayout EditText_layout,Training_site_layout;
	private LinearLayout WV_layout,WV_small_layout;
	
	private String getType,getValues;
	private int get_Id;
	
	Button[] btn = new Button[]{btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,
			   btn14,btn15};
	
	int[] id = new int[]{R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,
			R.id.button7,R.id.button8,R.id.button9,R.id.button10,R.id.button11,R.id.button12,
			R.id.button13,R.id.button14,R.id.button15};
	
	public List<String> btn_select_values = new ArrayList<String>();
	
	public List<String> WV_values = new ArrayList<String>();
	
	public List<String> WV_small_group_values = new ArrayList<String>();
	public List<String> WV_small_number_values = new ArrayList<String>();
	
	private String return_data="";
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_plan_changedata_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		Intent getIntent =getIntent();
		get_Id = getIntent.getIntExtra("Id",0);
		getType = getIntent.getStringExtra("type");
		getValues = getIntent.getStringExtra("getValues");
		
		initControls();
		setControlsData();
		setlisten();
	}

	public void setControlsData(){
		
		switch(getType){
		case "EditText":
			if(get_Id == R.id.edit_plan_Training_describe_layout){
				edit_describe.setText(getValues);
				editText_check.setText((100-edit_describe.length())+"/100");
			}else{
				edit_name.setText(getValues);
				editText_check.setText((10-edit_name.length())+"/10");
			}
			break;
		case "WheelView":
			
			
			
			switch(get_Id){
			case R.id.edit_plan_Training_place_layout:
				WV_values.add("健身房");
				WV_values.add("家");
				
				break;
			case R.id.edit_plan_Training_purpose_layout:
				WV_values.add("增肌");
				WV_values.add("减脂");
				WV_values.add("塑形");
				break;
			case R.id.edit_plan_Training_time_layout:
				String str = "";
				
				for(int i = 0;i<200;i++){
					str = i+"分钟";
					WV_values.add(str);
					
				}
				
				
				break;
			case R.id.edit_plan_addview_layout_rest_time:
				String st = "";
				for(int i = 0;i<200;i++){
					st = i+"秒";
					WV_values.add(st);
				}
				break;
			}
			
			
			WV.setOffset(1);
			WV.setItems(WV_values);
			int position = WV_values.lastIndexOf(getValues);
			if(get_Id ==R.id.edit_plan_Training_time_layout && position==(-1)){
			WV.setSeletion(45);}else if(position == (-1)){
				WV.setSeletion(0);
				}else{
				WV.setSeletion(position);
			}
			
			break;
			
		case "WheelView_small":
			String str_group;
			String str_number;
			for(int i = 1;i<=10;i++){
				str_group = i+"组";
				WV_small_group_values.add(str_group);
			}
			
			
			for(int i = 1;i<=30;i++){
				str_number= i+"个";
				WV_small_number_values.add(str_number);
			}
			
			String[] cmpvalues = getValues.split(",");//cmp是compare，就是用来对比的
			int position_group = WV_small_group_values.lastIndexOf(cmpvalues[0]);
			int position_number = WV_small_number_values.lastIndexOf(cmpvalues[1]);
			
			WV_small_group.setOffset(1);
			WV_small_group.setItems(WV_small_group_values);
			WV_small_group.setSeletion(position_group);
			
			WV_small_number.setOffset(1);
			WV_small_number.setItems(WV_small_number_values);
			WV_small_number.setSeletion(position_number);
			
			
			
			
			
			
			
			break;
		}
	}
	
	
	
	public void initControls(){
		
		cancle = (TextView)findViewById(R.id.edit_plan_data_cancel);
		complete = (TextView)findViewById(R.id.edit_plan_data_complete);
		tips = (TextView)findViewById(R.id.edit_plan_data_tip);
		
		
		switch(getType){
		
		case "EditText":
			
			EditText_layout = (RelativeLayout)findViewById(R.id.edit_plan_data_EditText_layout);
			EditText_layout.setVisibility(View.VISIBLE);
			
			if(get_Id == R.id.edit_plan_name_layout){
				edit_name = (EditText)findViewById(R.id.edit_plan_data_EditPlanName);
				edit_name.setVisibility(View.VISIBLE);
			}else{
				edit_describe = (EditText)findViewById(R.id.edit_plan_data_EditDescribe);
				edit_describe.setVisibility(View.VISIBLE);
			}
			
			editText_check = (TextView)findViewById(R.id.edit_plan_data_EditText_CheckNumber);
			break;
		case "Button":
			Training_site_layout = (RelativeLayout)findViewById(R.id.edit_plan_data_select_Training_site_layout);
			Training_site_layout.setVisibility(View.VISIBLE);
			
			for(int i=1;i<=15;i++){
				btn[i-1] = (Button)findViewById(id[i-1]);
			}
			break;
		case "WheelView":
			WV_layout = (LinearLayout)findViewById(R.id.edit_plan_data_select_WV_layout);
			WV_layout.setVisibility(View.VISIBLE);
			
			WV = (WheelView)findViewById(R.id.edit_plan_data_select_WV);
			break;
		case "WheelView_small":
			WV_small_layout = (LinearLayout)findViewById(R.id.edit_plan_data_select_WV_small_layout);
			WV_small_layout.setVisibility(View.VISIBLE);
			
			WV_small_group = (WheelView_small)findViewById(R.id.edit_plan_data_select_WV_small_group);
			WV_small_number = (WheelView_small)findViewById(R.id.edit_plan_data_select_WV_small_number);
			
			break;
		}
	}
	
	public void setlisten(){
		cancle.setOnClickListener(new MyOnClickListen());
		complete.setOnClickListener(new MyOnClickListen());
		
		switch(getType){
		case "EditText":
			
			if(get_Id == R.id.edit_plan_Training_describe_layout){
				edit_describe.addTextChangedListener(new MyTextWatcher(get_Id));
			}else{
				edit_name.addTextChangedListener(new MyTextWatcher(get_Id));
			}
			
			break;
		case "Button":
			
			
			for(int i=1;i<=15;i++){
				btn[i-1].setOnClickListener(new MyOnClickListen(true,i-1));
				
				//Log.d("xxx", i+"");
			}
			break;
		case "WheelView":
		/*	
			WV.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
	            @Override
	            public void onSelected(int selectedIndex, String item) {
	                //Log.d("xxx", "selectedIndex: " + selectedIndex + ", item: " + item);
	            	return_data = item;
	            }
	        });
			*/ //可以不用监听，直接用getSelectitem就行
			break;
		}
	}
	
	class MyOnClickListen implements OnClickListener{

		public boolean isbtn = false;
		public int position;
		public MyOnClickListen(){
			
		}
		
		public  MyOnClickListen(boolean isbtn,int position){
			this.isbtn = true;
			this.position = position;
			//Log.d("xxx", ""+position);
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(isbtn){
				
				if(btn[position].isSelected()){
					btn[position].setSelected(false);
					btn_select_count--;
					btn_select_values.remove(btn[position].getText().toString());
					//Log.d("xxx", ""+btn_select_values.size());
					//Log.d("xxx", btn_select_values.toString());
				}else{
					if(btn_select_count<2){
					btn[position].setSelected(true);
					btn_select_count++;
					btn_select_values.add(btn[position].getText().toString());
					//Log.d("xxx", ""+btn_select_values.size());
					}
				}
				
				
				
			}else{
			
			switch(v.getId()){
			case R.id.edit_plan_data_cancel:
				finish();
				break;
			case R.id.edit_plan_data_complete:
				Intent send_text_back = new Intent();
				send_text_back.putExtra("return_id", get_Id);
				switch(getType){
				case "EditText":
					if(get_Id == R.id.edit_plan_Training_describe_layout){
						return_data = edit_describe.getText().toString();
					}else{
						return_data = edit_name.getText().toString();
					}
					break;
				case "Button":
					
					 for(int i=0;i<btn_select_values.size();i++){
						return_data = return_data+" "+btn_select_values.get(i);
						}
				
					break;
				case "WheelView":
					
					return_data = WV.getSeletedItem();
					//Log.d("xxx", WV.getSeletedItem());
					//这里这样牵，前面获得位置中，必须进行设置，设置-1是获取不到的，所以如果position=-1，则进行置0设置
					break;
					
				case "WheelView_small":
					return_data = WV_small_group.getSeletedItem()+","+WV_small_number.getSeletedItem();
					break;
				}
				
				send_text_back.putExtra("return_data", return_data);
				setResult(RESULT_OK,send_text_back);
				finish();
				
				break;
			
			}
			
			
			}
		}
		
	}

	//监视字数改变
		class MyTextWatcher implements TextWatcher{
			
			private String Limit_Number;
			private int mNow_Number;
			private int id;
			public MyTextWatcher(int id){
				switch(id){
				case R.id.edit_plan_name_layout:
					Limit_Number = "/10";
					this.id = id;
					break;
				case R.id.edit_plan_Training_describe_layout:
					Limit_Number = "/100";
					this.id = id;
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
				switch(id){
				case R.id.edit_plan_name_layout:
					mNow_Number = 10-s.length();
					editText_check.setText(mNow_Number + Limit_Number);
					break;
				case R.id.edit_plan_Training_describe_layout:
					mNow_Number = 100-s.length();
					editText_check.setText(mNow_Number + Limit_Number);
					break;
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
			
		}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(0, R.anim.slide_intodown);
	}
	
	
}
