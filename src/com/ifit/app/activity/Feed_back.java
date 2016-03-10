package com.ifit.app.activity;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Feed_back extends Activity {

	private ImageView feed_back_btnback;
	private RelativeLayout getFocus,feed_back_submit;
	private EditText feed_back_edit_content,feed_back_edit_contact;
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feed_back);
		feed_back_btnback = (ImageView)findViewById(R.id.feed_back_title_btn_back);
		feed_back_btnback.setOnClickListener(new MyOnClickListener());
		getFocus = (RelativeLayout)findViewById(R.id.feed_back_big_EditText_layout);
		feed_back_submit = (RelativeLayout)findViewById(R.id.feed_back_submit);
		feed_back_edit_content= (EditText)findViewById(R.id.feed_back_edit_content_Text);
		feed_back_edit_contact = (EditText)findViewById(R.id.feed_back_edit_contact_Text);
		getFocus.setOnClickListener(new MyOnClickListener());
		feed_back_submit.setOnClickListener(new MyOnClickListener());
		
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
	}

	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.feed_back_title_btn_back:
				
				finish();
				break;
			case R.id.feed_back_big_EditText_layout:
				feed_back_edit_content.requestFocus();
				InputMethodManager imm = (InputMethodManager) 
						feed_back_edit_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.SHOW_FORCED); 
				break;
			
			case R.id.feed_back_submit:
				
				String content = feed_back_edit_content.getText().toString();
				String contact = feed_back_edit_contact.getText().toString();
				
				if(content.equals("")){
					//提示不能为空
					new AlertDialog.Builder(Feed_back.this).setTitle("提示").setMessage("您好，填写内容不能为空！")
					.setPositiveButton("确定", null).show();
				}else if(contact.equals("")){
					//提示填写联系方式
					new AlertDialog.Builder(Feed_back.this).setTitle("提示").setMessage("您好，请填写您的联系方式！")
					.setPositiveButton("确定", null).show();
				}else{
				ContentValues values = new ContentValues();
				values.put("content", content);
				values.put("contact", contact);
				db.insert("User_feedback_table", null, values);
				values.clear();
				
				Toast.makeText(Feed_back.this, "提交成功", Toast.LENGTH_SHORT).show();
				db.close();
				finish();}
				
				break;
			default:
				break;
			}
		}
		
	}
	
}
