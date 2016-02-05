package com.ifit.app.other;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifit.app.R;
import com.ifit.app.other.MyPickView.onSelectListener;

public class PickView_dialog extends Activity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_personal_data_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//让弹窗全屏，在样式里设不设置都可以<item name="android:windowFullscreen">true</item>  
		
		//必要控件
		cancle = (TextView)findViewById(R.id.select_personal_data_cancel);
		complete = (TextView)findViewById(R.id.select_personal_data_complete);
		tips = (TextView)findViewById(R.id.select_personal_data_tip);
		

		cancle.setOnClickListener(new MyOnClickListener());
		complete.setOnClickListener(new MyOnClickListener());
		
		
		Intent intent = getIntent();
		int get_intent_id = intent.getIntExtra("getId",0);
		String getType = intent.getStringExtra("type");
		
		
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
			mPickView.setSelected(selectposition);}
			else if(get_intent_id == R.id.editor_personal_data_age){
				mPickView.setSelected(10);
			}
			mPickView.setOnSelectListener(new onSelectListener() {
				
				@Override
				public void onSelect(String text) {
					// TODO Auto-generated method stub
					Toast.makeText(PickView_dialog.this, "当前的选择是" + text,
							Toast.LENGTH_SHORT).show();
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
				edit_region = (EditText)findViewById(R.id.select_personal_data_Editregion);
				edit_region.setVisibility(View.VISIBLE);
				tips.setText("修改地区");
				edit_region.setText(getString);
				now_Number = 20-edit_region.length();
				editText_check.setText(now_Number+"/20");
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
				Limit_Number = "/20";
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
				mNow_Number = 20-s.length();
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
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.select_personal_data_cancel:
				finish();
				break;
			case R.id.select_personal_data_complete:
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
			case R.id.select_personal_data_getLocationImage:
				break;
				
			}
		}
		
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(0, R.anim.slide_intodown);
	}
	
}
