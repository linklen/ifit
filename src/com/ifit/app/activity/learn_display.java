package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;

import com.ifit.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class learn_display extends Activity {

	private WebView webview;
	private RelativeLayout back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.learn_display);
		
		webview = (WebView)findViewById(R.id.learn_display_webview);
		back = (RelativeLayout)findViewById(R.id.learn_display_back);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		Intent getdata = getIntent();
		String url = getdata.getStringExtra("url");
		WebSettings setting = webview.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//和下面这一行一起是自适应手机屏幕的
		setting.setLoadWithOverviewMode(true);
		webview.setWebViewClient(new  WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;}//返回true是不调用第三方浏览器
            });
		
		webview.loadUrl(url);
	}

	
}
