package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class report extends Activity {

	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	
	private RelativeLayout submit_btn;
	private ImageView btn_back;
	
	private EditText edit_text;
	
	private RadioGroup radiogroup;
	private RadioButton btn1,btn2,btn3,btn4,btn5;
	private String type="";
	private String news_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report_layout);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		btn_back = (ImageView)findViewById(R.id.report_title_btn_back);
		submit_btn = (RelativeLayout) findViewById(R.id.report_submit);
		edit_text = (EditText)findViewById(R.id.report_edit_content_Text);
		radiogroup = (RadioGroup)findViewById(R.id.report_radiogroup);
		btn1 = (RadioButton)findViewById(R.id.radioButton1);
		btn2 = (RadioButton)findViewById(R.id.radioButton2);
		btn3 = (RadioButton)findViewById(R.id.radioButton3);
		btn4 = (RadioButton)findViewById(R.id.radioButton4);
		btn5 = (RadioButton)findViewById(R.id.radioButton5);
		
		Intent getdata = getIntent();
		news_id = getdata.getStringExtra("news_id");
		
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				switch(checkedId){
				case R.id.radioButton1:
					type = "色情";
					break;
				case R.id.radioButton2:
					type = "谣言";
					break;
				case R.id.radioButton3:
					type = "恶意营销";
					break;
				case R.id.radioButton4:
					type = "侮辱诋毁";
					break;
				case R.id.radioButton5:
					type = "其他";
					break;
				
				}
				
			}
		});
		
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		submit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String content = edit_text.getText().toString();
				
				if(content.equals("")||type.equals("")){
					new AlertDialog.Builder(report.this).setMessage("请填写完整！")
					.setPositiveButton("知道了", null).show();
				}else{
					ContentValues values = new ContentValues();
					values.put("news_id", news_id);
					values.put("type", type);
					values.put("content", content);
					db.insert("User_report_table", null, values);
					
					Toast.makeText(report.this, "举报成功!", Toast.LENGTH_SHORT).show();
					
					finish();
					
				}
			}
			
		});
	}
	
	
	
}
