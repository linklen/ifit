package com.ifit.app.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ifit.app.R;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.PickView_dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Personal_data extends Activity {

	private TextView btn_complete,
					 edit_age,edit_nickname,
					 edit_sex,edit_region,edit_introduction,
					 edit_height,edit_weight,edit_waist,
					 edit_experience,edit_purpose;
	private ImageView backImage,headImage;
	
	private AlertDialog builder;
	private String[] dialog_item;
	private Bitmap mbitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editor_personal_data);
		
		dialog_item = new String[]{"ѡ�񱾵�ͼƬ","����"};
		builder = new AlertDialog.Builder(Personal_data.this)
								 .setTitle("����ͷ��")
								 .setItems(dialog_item, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										switch(which){
										case 0:
											finish();
											break;
										case 1:
											finish();
											break;
										}
									}
								})
								 .setPositiveButton("ȡ��", null)
								 .create();
		
		
		backImage = (ImageView)findViewById(R.id.editor_personal_data_title_btn_back);
		btn_complete = (TextView)findViewById(R.id.editor_personal_data_title_btn_complete);
		headImage =(ImageView)findViewById(R.id.editor_personal_data_headImage);
		edit_nickname = (TextView)findViewById(R.id.editor_personal_data_nickname);
		edit_sex = (TextView)findViewById(R.id.editor_personal_data_sex);
		edit_age = (TextView)findViewById(R.id.editor_personal_data_age);
		edit_region = (TextView)findViewById(R.id.editor_personal_data_region);
		edit_introduction = (TextView)findViewById(R.id.editor_personal_data_introduction);
		edit_height = (TextView)findViewById(R.id.editor_personal_data_body_height);
		edit_weight = (TextView)findViewById(R.id.editor_personal_data_body_weight);
		edit_waist = (TextView)findViewById(R.id.editor_personal_data_Waist_circumference);
		edit_experience = (TextView)findViewById(R.id.editor_personal_data_training_experience);
		edit_purpose = (TextView)findViewById(R.id.editor_personal_data_training_purpose);
		
		if(false){
			//�����û�ͷ���Ƿ���ڴ��ڣ���������
			//����ֱ������mbitmap
		}else{
		//headImage.setDrawingCacheEnabled(true);//��ͼ����ڻ�������
		//mbitmap = headImage.getDrawingCache();
		//headImage.setDrawingCacheEnabled(false);//��ͼ��ӻ�����ɾ��
		mbitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);}
		headImage.setImageDrawable(new CircleImageDrawable(mbitmap));

				
				
		backImage.setOnClickListener(new MyOnClickListener());
		btn_complete.setOnClickListener(new MyOnClickListener());
		headImage.setOnClickListener(new MyOnClickListener());
		edit_nickname.setOnClickListener(new MyOnClickListener());
		edit_sex.setOnClickListener(new MyOnClickListener());
		edit_age.setOnClickListener(new MyOnClickListener());
		edit_region.setOnClickListener(new MyOnClickListener());
		edit_introduction.setOnClickListener(new MyOnClickListener());
		edit_height.setOnClickListener(new MyOnClickListener());
		edit_weight.setOnClickListener(new MyOnClickListener());
		edit_waist.setOnClickListener(new MyOnClickListener());
		edit_experience.setOnClickListener(new MyOnClickListener());
		edit_purpose.setOnClickListener(new MyOnClickListener());
		
		
	}

	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.editor_personal_data_title_btn_back:
				finish();//����Ҫ�����ж��Ƿ��Ѿ�д������
				break;
			case R.id.editor_personal_data_title_btn_complete:
				finish();//����Ҫ����д�����ݿ����
				break;
			case R.id.editor_personal_data_headImage:
				 builder.show();
				//����������ͷ�񣬵�����ʾ��
				break;
			case R.id.editor_personal_data_nickname:
				set_Intent(R.id.editor_personal_data_nickname, edit_nickname.getText().toString(), "EditText");//�����������ǳƵĴ���
				break;
			case R.id.editor_personal_data_sex:
				set_Intent(R.id.editor_personal_data_sex,edit_sex.getText().toString(),"Button");
				break;
			case R.id.editor_personal_data_age:
				set_Intent(R.id.editor_personal_data_age,edit_age.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_region:
				set_Intent(R.id.editor_personal_data_region, edit_region.getText().toString(), "EditText");//������д������
				break;
			case R.id.editor_personal_data_introduction:
				set_Intent(R.id.editor_personal_data_introduction, edit_introduction.getText().toString(), "EditText");//������д����
				break;
			case R.id.editor_personal_data_body_height:
				set_Intent(R.id.editor_personal_data_body_height,edit_height.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_body_weight:
				set_Intent(R.id.editor_personal_data_body_weight,edit_weight.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_Waist_circumference:
				set_Intent(R.id.editor_personal_data_Waist_circumference,edit_weight.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_training_experience:
				set_Intent(R.id.editor_personal_data_training_experience,edit_experience.getText().toString(),"Button");//ѡ��ѵ������
				break;
			case R.id.editor_personal_data_training_purpose:
				set_Intent(R.id.editor_personal_data_training_purpose,edit_purpose.getText().toString(),"Button");//ѡ��ѵ��Ŀ��
				break;
			default:
				break;
			}
		}
		
	}
	
	public void set_Intent (int id,String values,String type){
		
		
		Intent open_pickView = new Intent(this,PickView_dialog.class);
		open_pickView.putExtra("getId", id);
		
		if(type.equals("Number")){
		//��ȡ�ַ���������
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(values);
		String getNumber = m.replaceAll("").trim();
		open_pickView.putExtra("getNumber",getNumber);
		open_pickView.putExtra("type", "Number");
		}else if(type.equals("Button")){
			open_pickView.putExtra("type", "Button");
			open_pickView.putExtra("getString", values);
		}else{
			open_pickView.putExtra("type", "EditText");
			open_pickView.putExtra("getString", values);
		}
		
		startActivityForResult(open_pickView, 1);
		overridePendingTransition(R.anim.slide_downtoin,0);
		
	}
}
