package com.ifit.app.activity;

import com.ifit.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Feed_back extends Activity {

	private ImageView feed_back_btnback;
	private RelativeLayout getFocus;
	private EditText feed_back_edittext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feed_back);
		feed_back_btnback = (ImageView)findViewById(R.id.feed_back_title_btn_back);
		feed_back_btnback.setOnClickListener(new MyOnClickListener());
		getFocus = (RelativeLayout)findViewById(R.id.feed_back_big_EditText_layout);
		feed_back_edittext= (EditText)findViewById(R.id.feed_back_edit_content_Text);
		getFocus.setOnClickListener(new MyOnClickListener());
		
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
				feed_back_edittext.requestFocus();
				break;
			default:
				break;
			}
		}
		
	}
	
}
