package com.ifit.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.Change_Personal_Data_dialog;
import com.ifit.app.other.CircleImageDrawable;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Personal_data extends Activity {

	
	private TextView btn_complete,
					 edit_age,edit_nickname,
					 edit_sex,edit_region,edit_introduction,
					 edit_height,edit_weight,edit_waist,
					 edit_experience,edit_purpose;
	private ImageView backImage,headImage;
	
	
	private AlertDialog setHeadimage_dialog,btn_exit_dialog;
	private String[] dialog_item;
	private Bitmap mbitmap;

	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private SharedPreferences locationdata,getUser_name;//,data_change_mark;//获取和建立文件
	private boolean isRecord;
	private String login_name;//储存读进来的账户号
	
	private String nickname,sex,region,introduction,experience,purpose;
	private int age,height,weight,waist;
	
	private String getReturn_Data;
	private int getReturn_id;
	private boolean user_headimge_change = false,
					nickname_change = false,sex_change = false,
					region_change  = false,introduction_change  = false,
					experience_change  = false,purpose_change  = false,
					age_change  = false,height_change  = false,
					weight_change  = false,waist_change  = false;
	
	
	//相机用到
	public static final int TAKE_PHOTO = 2;
	public static final int CROP_PHOTO = 3;
	public static final int CHOOSE_PHOTO = 4;
	
	public Uri imageUri;
	public File sdCard,directory,location_image,temp_image;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editor_personal_data);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null,1);
		db = usedb.getWritableDatabase();
		
		getUser_name = getSharedPreferences("islogin", MODE_PRIVATE);
		login_name = getUser_name.getString("user", "");
		
		locationdata = getSharedPreferences("location_user_Data", MODE_PRIVATE);
		//data_change_mark = getSharedPreferences("data_change_mark", MODE_PRIVATE);
		
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/UserHeadImage/");
		if(!directory.exists()){
		directory.mkdirs();}
		location_image = new File(directory,"location_image.jpg");
		temp_image = new File(directory,"temp_image.jpg");
		
		
		
		dialog_item = new String[]{"选择本地图片","拍照"};
		setHeadimage_dialog = new AlertDialog.Builder(this)
								 .setTitle("设置头像")
								 .setItems(dialog_item, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										
										
										imageUri = Uri.fromFile(temp_image);
										
										// TODO Auto-generated method stub
										switch(which){
										case 0:
											Intent open_Photo_album = new Intent(
													"android.intent.action.GET_CONTENT");
											open_Photo_album.setType("image/*");
											startActivityForResult(open_Photo_album, CHOOSE_PHOTO);//打开相册

											break;
										case 1:
											
											
											//File outputImage = new File(Environment.getExternalStorageDirectory(),
											//		"output_image.jpg");
											/*try{
												if(location_image.exists()){
													location_image.delete();
												}
												location_image.createNewFile();
											}catch (IOException e) {
												// TODO: handle exception
												e.printStackTrace();
											}
											*/
											Intent open_camera = new Intent("android.media.action.IMAGE_CAPTURE");
											open_camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
											startActivityForResult(open_camera, TAKE_PHOTO);
											break;
										}
									}
								})
								 .setPositiveButton("取消", null)
								 .create();
		
		btn_exit_dialog = new AlertDialog.Builder(this).setTitle("提示")
										 .setMessage("个人信息已经修改，尚未保存，是否退出？")
										 .setPositiveButton("取消", null)
										 .setNegativeButton("确定", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												finish();
											}
										})
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
		
		
		setdata();
		
				
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
				if( nickname_change||sex_change||region_change||introduction_change||
					experience_change||
					purpose_change||age_change||height_change||weight_change||waist_change
					|| user_headimge_change){
					
					btn_exit_dialog.show();
				}else{
					finish();
				}
				
				break;
			case R.id.editor_personal_data_title_btn_complete:
				if( nickname_change||sex_change||region_change||introduction_change||
						experience_change||
						purpose_change||age_change||height_change||weight_change||waist_change
						|| user_headimge_change){
					
						//每一个都先进行本地写入在进行数据库录入
					
					
					SharedPreferences.Editor editor = locationdata.edit();
					//SharedPreferences.Editor mark_editor = data_change_mark.edit();
					ContentValues image_values = new ContentValues();
					ContentValues Info_values = new ContentValues();
					ContentValues Data_values = new ContentValues();
					
					
					if(user_headimge_change){
						
						location_image.delete();
						temp_image.renameTo(location_image);
						String imagePath =location_image.toString();
						Bitmap savebitmap = BitmapFactory.decodeFile(imagePath);
						//editor.putString("ImagePath", imagePath); //将地址保存到pref中没什么必要
						final ByteArrayOutputStream os = new ByteArrayOutputStream();
						savebitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
						image_values.put("user_head_img", os.toByteArray());
						db.update("User_headImage_table", image_values,
								"name = ?", new String[]{login_name});
						image_values.clear();
						
						//mark_editor.putInt("headimg_change", 2);
						
					}
					if(nickname_change){
						String nickname = edit_nickname.getText().toString();
						editor.putString("nickname", nickname);
						Info_values.put("nickname", nickname);
						//mark_editor.putInt("nickname_change", 2);
					}
					if(sex_change){
						String sex =edit_sex.getText().toString();
						editor.putString("sex", sex);
						Info_values.put("sex", sex);
					}
					if(age_change){
						int age = Integer.parseInt(edit_age.getText().toString());
						editor.putInt("age", age);
						Info_values.put("age", age);
						//mark_editor.putInt("age_change", 2);
					}
					if(region_change){
						String region = edit_region.getText().toString();
						editor.putString("region", region);
						Info_values.put("region", region);
					}
					if(introduction_change){
						String introduction = edit_introduction.getText().toString();
						editor.putString("introduction", introduction);
						Info_values.put("introduction", introduction);
					}
					if(height_change){
						int height = Integer.parseInt(edit_height.getText().toString());
						editor.putInt("height", height);
						Data_values.put("height", height);
					}
					if(weight_change){
						int weight = Integer.parseInt(edit_weight.getText().toString());
						editor.putInt("weight", weight);
						Data_values.put("weight", weight);
					}
					if(waist_change){
						int waist = Integer.parseInt(edit_waist.getText().toString());
						editor.putInt("waist", waist);
						Data_values.put("waist", waist);
					}
					if(experience_change){
						String experience = edit_experience.getText().toString();
						editor.putString("experience", experience);
						Data_values.put("experience", experience);
					}
					if(purpose_change){
						String purpose = edit_purpose.getText().toString();
						editor.putString("purpose", purpose);
						Data_values.put("purpose", purpose);
					}
					
					editor.commit();
					//mark_editor.commit();
					if(Info_values.size() != 0){
					db.update("User_personal_info_table", Info_values, 
							"name = ?", new String[]{login_name});
							Info_values.clear();
					}
					if(Data_values.size() != 0){
					db.update("User_body_data_table", Data_values, 
							"name = ?", new String[]{login_name});
							Data_values.clear();
					}
					
					
					Toast.makeText(Personal_data.this, "更新成功", Toast.LENGTH_SHORT).show();
					finish();
					
					}else{
						finish();
					}
				break;
			case R.id.editor_personal_data_headImage:
				setHeadimage_dialog.show();
				//这里是设置头像，弹出提示框
				break;
			case R.id.editor_personal_data_nickname:
				set_Intent(R.id.editor_personal_data_nickname, edit_nickname.getText().toString(), "EditText");//这里是设置昵称的窗口
				break;
			case R.id.editor_personal_data_sex:
				set_Intent(R.id.editor_personal_data_sex,edit_sex.getText().toString(),"Button");
				break;
			case R.id.editor_personal_data_age:
				set_Intent(R.id.editor_personal_data_age,edit_age.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_region:
				set_Intent(R.id.editor_personal_data_region, edit_region.getText().toString(), "EditText");//这里是写地区的
				break;
			case R.id.editor_personal_data_introduction:
				set_Intent(R.id.editor_personal_data_introduction, edit_introduction.getText().toString(), "EditText");//这里是写简介的
				break;
			case R.id.editor_personal_data_body_height:
				set_Intent(R.id.editor_personal_data_body_height,edit_height.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_body_weight:
				set_Intent(R.id.editor_personal_data_body_weight,edit_weight.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_Waist_circumference:
				set_Intent(R.id.editor_personal_data_Waist_circumference,edit_waist.getText().toString(),"Number");
				break;
			case R.id.editor_personal_data_training_experience:
				set_Intent(R.id.editor_personal_data_training_experience,edit_experience.getText().toString(),"Button");//选择训练经验
				break;
			case R.id.editor_personal_data_training_purpose:
				set_Intent(R.id.editor_personal_data_training_purpose,edit_purpose.getText().toString(),"Button");//选择训练目的
				break;
			default:
				break;
			}
		}
		
	}
	
	public void set_Intent (int id,String values,String type){
		
		
		Intent open_pickView = new Intent(this,Change_Personal_Data_dialog.class);
		open_pickView.putExtra("getId", id);
		
		if(type.equals("Number")){
		//获取字符串中数字
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
	
	
	public void setdata(){
		
		isRecord = locationdata.getBoolean("isRecord", false);
		
		if(isRecord){
			nickname = locationdata.getString("nickname", "");
			sex = locationdata.getString("sex", "");
			age = locationdata.getInt("age",0);
			region = locationdata.getString("region", "");
			introduction = locationdata.getString("introduction", "");
			experience = locationdata.getString("experience", "");
			purpose = locationdata.getString("purpose", "");
			height = locationdata.getInt("height", 0);
			weight = locationdata.getInt("weight", 0);
			waist = locationdata.getInt("waist", 0);
			
			
			
			if(location_image.exists()){
				//查找用户头像是否存在存在，进行设置
				//存在直接设置mbitmap
				//imageUri = Uri.fromFile(location_image);
				mbitmap = BitmapFactory
						.decodeFile(location_image.toString());
			}else{
			//headImage.setDrawingCacheEnabled(true);//将图像放在缓冲里面
			//mbitmap = headImage.getDrawingCache();
			//headImage.setDrawingCacheEnabled(false);//将图像从缓冲中删除
			mbitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_headimage);
			}
			headImage.setImageDrawable(new CircleImageDrawable(mbitmap));
			
			
			
			
			if(!nickname.equals("")){
				edit_nickname.setText(nickname);
			}
			if(!sex.equals("")){
				edit_sex.setText(sex);
			}
			if(age != 0){
				edit_age.setText(""+age);
			}
			if(!region.equals("")){
				edit_region.setText(region);
			}
			if(!introduction.equals("")){
				edit_introduction.setText(introduction);
			}
			if(!purpose.equals("")){
				edit_purpose.setText(purpose);
			}
			if(!experience.equals("")){
				edit_experience.setText(experience);
			}
			if(height != 0){
				edit_height.setText(""+height);
			}
			if(weight != 0){
				edit_weight.setText(""+weight);
			}
			if(waist != 0){
				edit_waist.setText(""+waist);
			}
		}else{
			getData(login_name);
		}
	}
	
	
	
	public void getData(String getName){
		
		Cursor cursor = db.query("User_personal_info_table",
								 new String[]{"nickname,sex,age,region,introduction"},
								 "name = ?", 
								 new String[]{getName}, null,null, null);
		cursor.moveToFirst();
		nickname = cursor.getString(cursor.getColumnIndex("nickname"));
		sex = cursor.getString(cursor.getColumnIndex("sex"));
		age = cursor.getInt(cursor.getColumnIndex("age"));
		region = cursor.getString(cursor.getColumnIndex("region"));
		introduction = cursor.getString(cursor.getColumnIndex("introduction"));
		
		SharedPreferences.Editor editor = locationdata.edit();
			
		//editor.putString("nickname", nickname);
		editor.putString("sex", sex);
		//editor.putInt("age", age);
		//editor.putString("region", region);
		editor.putString("introduction", introduction);
		
		cursor = db.query("User_body_data_table", 
						  new String[]{"height,weight,waist,experience,purpose"}, 
						  "name = ?", 
						  new String[]{getName}, null, null, null);
		cursor.moveToFirst();
		height = cursor.getInt(cursor.getColumnIndex("height"));
		weight = cursor.getInt(cursor.getColumnIndex("weight"));
		waist = cursor.getInt(cursor.getColumnIndex("waist"));
		experience = cursor.getString(cursor.getColumnIndex("experience"));
		purpose = cursor.getString(cursor.getColumnIndex("purpose"));
		
		editor.putInt("height", height);
		editor.putInt("weight", weight);
		editor.putInt("waist", waist);
		editor.putString("experience", experience);
		editor.putString("purpose", purpose);
		editor.putBoolean("isRecord", true);
		editor.commit();
		
		
		/* 这部分全部已经移植到Home——page的title上了，在那个上面进行读取写入sd
		//读取头像
		cursor = db.query("User_headImage_table", 
				new String[]{"user_head_img"}, 
				"name = ?",
				new String[]{getName},
				null,null,null);
		cursor.moveToFirst();
		if(cursor.getBlob(cursor.getColumnIndex("user_head_img")) != null){
		byte[] get_Headimg = cursor.getBlob(cursor.getColumnIndex("user_head_img"));
		Bitmap getbitmap = BitmapFactory.decodeByteArray(get_Headimg, 0, get_Headimg.length);
		//headImage.setImageDrawable(new CircleImageDrawable(getbitmap));
		
		if(location_image.exists()){
			location_image.delete();}
			try{
			FileOutputStream Outputimg = new FileOutputStream(location_image);
			getbitmap.compress(Bitmap.CompressFormat.JPEG, 100, Outputimg);
			Outputimg.flush();//清空缓存区域
			Outputimg.close();
			//Log.d("xxx", "已经保存");
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
			*/
		
		cursor.close();
		setdata();
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case 1:
			
			if(resultCode == RESULT_OK){
				getReturn_Data = data.getStringExtra("return_data");
				getReturn_id = data.getIntExtra("return_id", 0);
				switch(getReturn_id){
				case R.id.editor_personal_data_nickname:
					if(!edit_nickname.getText().equals(getReturn_Data)){
					edit_nickname.setText(getReturn_Data);
					nickname_change = true;}
					break;
				case R.id.editor_personal_data_sex:
					if(!edit_sex.getText().equals(getReturn_Data)){
					edit_sex.setText(getReturn_Data);
					sex_change = true;}
					break;
				case R.id.editor_personal_data_age:
					if(!edit_age.getText().equals(getReturn_Data)){
					edit_age.setText(getReturn_Data);
					age_change  = true;}
					break;
				case R.id.editor_personal_data_region:
					if(!edit_region.getText().equals(getReturn_Data)){
					edit_region.setText(getReturn_Data);
					region_change  = true;}
					break;
				case R.id.editor_personal_data_introduction:
					if(!edit_introduction.getText().equals(getReturn_Data)){
					edit_introduction.setText(getReturn_Data);
					introduction_change  = true;}
					break;
				case R.id.editor_personal_data_body_height:
					if(!edit_height.getText().equals(getReturn_Data)){
					edit_height.setText(getReturn_Data);
					height_change  = true;}
					break;
				case R.id.editor_personal_data_body_weight:
					if(!edit_weight.getText().equals(getReturn_Data)){
					edit_weight.setText(getReturn_Data);
					weight_change  = true;}
					break;
				case R.id.editor_personal_data_Waist_circumference:
					if(!edit_waist.getText().equals(getReturn_Data)){
					edit_waist.setText(getReturn_Data);
					waist_change  = true;}
					break;
				case R.id.editor_personal_data_training_experience:
					if(!edit_experience.getText().equals(getReturn_Data)){
					edit_experience.setText(getReturn_Data);
					experience_change  = true;}
					break;
				case R.id.editor_personal_data_training_purpose:
					if(!edit_purpose.getText().equals(getReturn_Data)){
					edit_purpose.setText(getReturn_Data);
					purpose_change  = true;}
					break;
				default:
					break;
				}
			}
			break;
			
		case TAKE_PHOTO:
			if(resultCode == RESULT_OK){
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);//aspectX aspectY 是宽高的比例
				intent.putExtra("outputX", 400);
				intent.putExtra("outputY", 400);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
			
		case CROP_PHOTO:
			if(resultCode == RESULT_OK){
				try{
					Bitmap bitmap = BitmapFactory
									.decodeStream(getContentResolver().openInputStream(imageUri));
					headImage.setImageDrawable(new CircleImageDrawable(bitmap));
					user_headimge_change = true;
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
			}
			break;
		case CHOOSE_PHOTO:
			if(resultCode == RESULT_OK){
				
				
				if(Build.VERSION.SDK_INT >= 19){
					//4.4及以上系统使用这个方法处理图片
					handleImageOnKitkat(data);
				}else{
					handleImageBeforeKitKat(data);
				}
			
			}
			break;
			default:
				break;
		}
	}

	@TargetApi(19)
	private void handleImageOnKitkat(Intent data){
		String imagePath = null;
		Uri uri = data.getData();
		if(DocumentsContract.isDocumentUri(this, uri)){
			//如果是document类型的uri，则通过document id处理
			String docId = DocumentsContract.getDocumentId(uri);
			if("com.android.providers.media.documents".equals(uri.getAuthority())){
				String id = docId.split(":")[1];//解析出数字格式的id
				String selection = MediaStore.Images.Media._ID + "=" +id;
				imagePath = getImagePath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
			}else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docId));
				imagePath = getImagePath(contentUri,null);
			}
		}else if("content".equalsIgnoreCase(uri.getScheme())){
			//如果不是document类型的uri，则使用普通的方式处理
			imagePath = getImagePath(uri,null);
		}
		displayImage(imagePath);//根据图片路径显示图片
	}
	
	private void handleImageBeforeKitKat(Intent data){
		Uri uri = data.getData();
		String imagePath = getImagePath(uri,null);
		displayImage(imagePath);
	}
	
	private String getImagePath(Uri uri,String selection){
		String path = null;
		//通过Uri和selection来获取真实的图片路径
		Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}
	
	
	private void displayImage(String imagePath){
		
		if(imagePath != null){
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			//headImage.setImageDrawable(new CircleImageDrawable(bitmap));
			File file = new File(imagePath);
			Uri get_ImageUri = Uri.fromFile(file);
			
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(get_ImageUri, "image/*");
			intent.putExtra("scale", true);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);//aspectX aspectY 是宽高的比例
			intent.putExtra("outputX", 400);
			intent.putExtra("outputY", 400);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, CROP_PHOTO);
			
		}else{
			Toast.makeText(this, "对不起，读取失败！", Toast.LENGTH_SHORT).show();
		}
		
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		db.close();
	}
	
	
	
	
}
