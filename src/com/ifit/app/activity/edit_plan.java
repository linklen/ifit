package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.Edit_Plan_ChangeData_dialog;
import com.ifit.app.other.add_action_listItem;
import com.ifit.app.other.createView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class edit_plan extends Activity {

	private ImageView back_btn,add_cardimg;
	private TextView save_btn,name_text,Training_site_text,Training_place_text,Training_purpose_text,
	                 Training_time_text,Training_describe_text;
	private RelativeLayout name_layout,Training_site_layout,Training_place_layout,
						   Training_purpose_layout,Training_time_layout,Training_describe_layout,
						   add_action_layout;
	
	private ScrollView scrollview;
	
	public  static LinearLayout linearlayout=null;;
	
	public static final int TAKE_PHOTO = 0;
	public static final int CHOOSE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	public static final int OPEN_EDIT_DIALOG = 3;
	
	public File sdCard,directory,temp_image;
	
	private AlertDialog setCardimage_dialog;
	private String[] dialog_item;
	public Uri temp_imageUri;
	
	private boolean imgcard_change=false;
	
	private SharedPreferences getUsername;
	private String User_name;
	String outAction="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_plan);
		
		getUsername = getSharedPreferences("islogin", MODE_PRIVATE);
		User_name = getUsername.getString("user", "");
		
		initControls();
		initpath();
		initDialog();
		setlisten();
	}

	
	public void initpath(){
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/temp/");
		if(!directory.exists()){
		directory.mkdirs();}
		temp_image = new File(directory,"card_image.jpg");
	}
	
	
	public void initDialog(){
		dialog_item = new String[]{"选择本地图片","拍照"};
		setCardimage_dialog = new AlertDialog.Builder(this)
								 .setTitle("选择封面")
								 .setItems(dialog_item, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										
										
										temp_imageUri = Uri.fromFile(temp_image);
										
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
											open_camera.putExtra(MediaStore.EXTRA_OUTPUT, temp_imageUri);
											startActivityForResult(open_camera, TAKE_PHOTO);
											break;
										}
									}
								})
								 .setPositiveButton("取消", null)
								 .create();
	}
	
	
	public void initControls(){
		
		back_btn = (ImageView)findViewById(R.id.edit_plan_in_title_back);
		//order_btn_layout = (RelativeLayout)findViewById(R.id.edit_plan_order_btn_layout);
		save_btn = (TextView)findViewById(R.id.edit_plan_in_title_save);
		add_cardimg = (ImageView)findViewById(R.id.edit_plan_add_cardimg);
		
		name_layout = (RelativeLayout)findViewById(R.id.edit_plan_name_layout);
		name_text = (TextView)findViewById(R.id.edit_plan_name_text);
		
		Training_site_layout = (RelativeLayout)findViewById(R.id.edit_plan_Training_site_layout);
		Training_site_text = (TextView)findViewById(R.id.edit_plan_Training_site_text);
		
		Training_place_layout = (RelativeLayout)findViewById(R.id.edit_plan_Training_place_layout);
		Training_place_text = (TextView)findViewById(R.id.edit_plan_Training_place_text);
		
		Training_purpose_layout = (RelativeLayout)findViewById(R.id.edit_plan_Training_purpose_layout);
		Training_purpose_text = (TextView)findViewById(R.id.edit_plan_Training_purpose_text);
		
		Training_time_layout = (RelativeLayout)findViewById(R.id.edit_plan_Training_time_layout);
		Training_time_text = (TextView)findViewById(R.id.edit_plan_Training_time_text);
		
		Training_describe_layout = (RelativeLayout)findViewById(R.id.edit_plan_Training_describe_layout);
		Training_describe_text = (TextView)findViewById(R.id.edit_plan_Training_describe_text);
		
		add_action_layout = (RelativeLayout)findViewById(R.id.edit_plan_add_action_layout);
		
		scrollview = (ScrollView)findViewById(R.id.edit_plan_scrollview);
		
		linearlayout = (LinearLayout)findViewById(R.id.edit_plan_add_view);
		
	}
	
	public void setlisten(){
		back_btn.setOnClickListener(new MyOnClickListen());
		save_btn.setOnClickListener(new MyOnClickListen());
		add_cardimg.setOnClickListener(new MyOnClickListen());
		name_layout.setOnClickListener(new MyOnClickListen());
		Training_site_layout.setOnClickListener(new MyOnClickListen());
		Training_place_layout.setOnClickListener(new MyOnClickListen());
		Training_purpose_layout.setOnClickListener(new MyOnClickListen());
		Training_time_layout.setOnClickListener(new MyOnClickListen());
		Training_describe_layout.setOnClickListener(new MyOnClickListen());
		add_action_layout.setOnClickListener(new MyOnClickListen());
		//order_btn_layout.setOnClickListener(new MyOnClickListen());
	}
	
	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.edit_plan_in_title_back:
				new AlertDialog.Builder(edit_plan.this).setMessage("确认退出？")
				.setPositiveButton("否", null).setNegativeButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).show();
				break;
			case R.id.edit_plan_in_title_save:
				if(CheckisNull()){
					save_page();
					Toast.makeText(edit_plan.this, "添加成功", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					new AlertDialog.Builder(edit_plan.this).setMessage("请将内容填写完整。")
					.setPositiveButton("知道了", null).show();
				}
				break;
			case R.id.edit_plan_add_cardimg:
				setCardimage_dialog.show();
				break;
			case R.id.edit_plan_name_layout:
				set_Intent(v.getId(),name_text.getText().toString(),"EditText");
				break;
			case R.id.edit_plan_Training_site_layout:
				set_Intent(v.getId(),Training_site_text.getText().toString(),"Button");
				break;
			case R.id.edit_plan_Training_place_layout:
				set_Intent(v.getId(),Training_place_text.getText().toString(),"WheelView");
				break;
			case R.id.edit_plan_Training_purpose_layout:
				set_Intent(v.getId(),Training_purpose_text.getText().toString(),"WheelView");
				break;
			case R.id.edit_plan_Training_time_layout:
				set_Intent(v.getId(),Training_time_text.getText().toString(),"WheelView");
				break;
			case R.id.edit_plan_Training_describe_layout:
				set_Intent(v.getId(),Training_describe_text.getText().toString(),"EditText");
				break;
			case R.id.edit_plan_add_action_layout:
				Intent intent = new Intent(edit_plan.this,add_plan_action.class);
				startActivity(intent);
				
				break;
			//case R.id.edit_plan_order_btn_layout:
				//break;
			}
		}
		
	}

	public boolean CheckisNull(){
		if(imgcard_change){
			if(name_text.getText().length()>0){
				if(Training_site_text.getText().length()>0){
					if(Training_place_text.getText().length()>0){
						if(Training_purpose_text.getText().length()>0){
							if(Training_time_text.getText().length()>0){
								if(Training_describe_text.getText().length()>0){
									if(linearlayout.getChildCount()>0){
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public void save_page(){
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/");
		if(!directory.exists()){
		directory.mkdirs();}
		
		
		add_action_listItem item;
		String action_name;
		String action_img_path;
		
		for(int i=0;i<linearlayout.getChildCount();i++){
			
			item = createView.Action_out_cache.get(linearlayout.getChildAt(i));
			action_name = item.getAction_Name();
			action_img_path = item.getImgPath();
			
			TextView text_group = (TextView) (linearlayout.getChildAt(i))
					.findViewById(R.id.edit_plan_addview_layout_action_group_Number);

			String group = text_group.getText().toString();

			TextView text_number = (TextView) (linearlayout.getChildAt(i))
					.findViewById(R.id.edit_plan_addview_layout_action_Number);
			
			String number = text_number.getText().toString();
			
			TextView text_time = (TextView) (linearlayout.getChildAt(i))
					.findViewById(R.id.edit_plan_addview_layout_rest_time);
			
			String time = text_time.getText().toString();
					
			outAction =outAction+i+"，"+action_name+"，"+group+"，"+number+"，"+time+"，"+action_img_path+"。";
			
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		
				SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
				Date curDate = new Date(System.currentTimeMillis());
				String str = formatter.format(curDate);
				
				File path = new File(directory+"/dbfile/User_plan/"+User_name+"/"+str+"/");
				if(!path.exists()){
					path.mkdirs();}
				File output_imgpath = new File(path,"imgcard.jpg");
				
				Bitmap bitmap;
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(temp_imageUri));
					FileOutputStream out = new FileOutputStream(output_imgpath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String outStr = name_text.getText()+"---"+Training_site_text.getText()+"---"+
					    Training_place_text.getText()+"---"+Training_purpose_text.getText()+"---"+
					    Training_time_text.getText()+"---"+Training_describe_text.getText();
				
				
				File saveFile = new File(path,"basic_information.txt");
				
				try {
					FileOutputStream savefile = new FileOutputStream(saveFile);
					savefile.write(outStr.getBytes());
					savefile.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
				
				
				File saveAction = new File(path,"action_information.txt");
				
				try {
					
					FileOutputStream saveaction = new FileOutputStream(saveAction);
					saveaction.write(outAction.getBytes());
					saveaction.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			}
		}).start();
	}
	
	public void set_Intent(int id,String values,String type){
		Intent open_dialog = new Intent(edit_plan.this,Edit_Plan_ChangeData_dialog.class);
		open_dialog.putExtra("Id", id);
		open_dialog.putExtra("type", type);
		open_dialog.putExtra("getValues", values);
		startActivityForResult(open_dialog,OPEN_EDIT_DIALOG);
		overridePendingTransition(R.anim.slide_downtoin,0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		
		case TAKE_PHOTO:
			if(resultCode == RESULT_OK){
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(temp_imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra("aspectX", 100);
				intent.putExtra("aspectY", 175);//aspectX aspectY 是宽高的比例
				intent.putExtra("outputX", 300);
				intent.putExtra("outputY", 525);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, temp_imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
			
		case CROP_PHOTO:
			if(resultCode == RESULT_OK){
				try{
					Bitmap bitmap = BitmapFactory
									.decodeStream(getContentResolver().openInputStream(temp_imageUri));
					add_cardimg.setImageBitmap(bitmap);
					
					imgcard_change = true;
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
			}
			break;
		case CHOOSE_PHOTO:
			if(resultCode == RESULT_OK){
				
				Resolution_images(data);
			}
			break;
			
		case OPEN_EDIT_DIALOG:
			if(resultCode == RESULT_OK){
				String getReturn_Data = data.getStringExtra("return_data");
				int getReturn_id = data.getIntExtra("return_id", 0);
				switch(getReturn_id){
				case R.id.edit_plan_name_layout:
					if(!name_text.getText().equals(getReturn_Data)){
					name_text.setText(getReturn_Data);}
					break;
				case R.id.edit_plan_Training_site_layout:
					if(!Training_site_text.getText().equals(getReturn_Data)){
					Training_site_text.setText(getReturn_Data);}
					break;
				case R.id.edit_plan_Training_place_layout:
					if(!Training_place_text.getText().equals(getReturn_Data)){
					Training_place_text.setText(getReturn_Data);}
					break;
				case R.id.edit_plan_Training_purpose_layout:
					if(!Training_purpose_text.getText().equals(getReturn_Data)){
					Training_purpose_text.setText(getReturn_Data);}
					break;
				case R.id.edit_plan_Training_time_layout:
					if(!Training_time_text.getText().equals(getReturn_Data)){
					Training_time_text.setText(getReturn_Data);}
					break;
				case R.id.edit_plan_Training_describe_layout:
					if(!Training_describe_text.getText().equals(getReturn_Data)){
					Training_describe_text.setText(getReturn_Data);}
					break;
				case R.id.edit_plan_addview_layout_rest_time:
					View v = cr.get_tempView();
					if(!((TextView)v).getText().equals(getReturn_Data)){
						((TextView)v).setText(getReturn_Data);
					}
				case R.id.edit_plan_addview_layout_action_Number_edit:
					
					View v1 = cr.get_tempView();
					
					String[] value = getReturn_Data.split(",");
					
					TextView text_group = (TextView)((View)((v1.getParent()).getParent())).
					findViewById(R.id.edit_plan_addview_layout_action_group_Number);
					
					text_group.setText(value[0]);
					
					
					TextView text_number = (TextView)((View)((v1.getParent()).getParent())).
							findViewById(R.id.edit_plan_addview_layout_action_Number);
					
					text_number.setText(value[1]);
					
					
					break;
				}
			}
			break;
			default:
				break;
		}
		
	}
	

		
		public void Resolution_images(Intent data){
		if(Build.VERSION.SDK_INT >= 19){
			//4.4及以上系统使用这个方法处理图片
			handleImageOnKitkat(data);
		}else{
			handleImageBeforeKitKat(data);
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
				intent.putExtra("aspectX", 100);
				intent.putExtra("aspectY", 175);//aspectX aspectY 是宽高的比例
				intent.putExtra("outputX", 300);
				intent.putExtra("outputY", 525);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, temp_imageUri);
				startActivityForResult(intent, CROP_PHOTO);
				
			}else{
				Toast.makeText(this, "对不起，读取失败！", Toast.LENGTH_SHORT).show();
			}
			
		}


		
		
		private createView cr= new createView();
		
		@Override
		protected void onRestart() {
			// TODO Auto-generated method stub
			super.onRestart();
			
			//ii = new LinearLayout(this);
			//TextView tn = new TextView(this);
			//tn.setText("11111111111111111111111111111111");
			//linearlayout.addView(tn);
			if(!add_plan_action.itemCache.isEmpty()){
				
				
				//scrollview.addView(tn);
				int count = add_plan_action.MapKey.size();
				for(int i=0;i<count;i++){
				
				String Key = add_plan_action.MapKey.get(0);
				add_action_listItem item = add_plan_action.itemCache.get(Key);
				View view = new View(this);
				view = cr.createView(item,edit_plan.this);
				linearlayout.addView(view);
				
				add_plan_action.MapKey.remove(0);//ArrayList 移除元素数组下标会前移
				add_plan_action.itemCache.remove(Key);
				
				}
			}
			
		}
		
		
		
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(edit_plan.this).setMessage("确认退出？")
			.setPositiveButton("否", null).setNegativeButton("是", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			}).show();
		}


		@Override
		public void finish() {
			// TODO Auto-generated method stub
			super.finish();
			
			
			if(temp_image.exists()){
				temp_image.delete();
			}
			
			if( !createView.Action_out_cache.isEmpty()){
				createView.Action_out_cache.clear();
			}
		}
	
	
		/*排序按钮的布局记入在这里，是在动作信息后面，现在删了
		 * <RelativeLayout 
            android:id="@+id/edit_plan_order_btn_layout"
            android:layout_width="60dp"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_centerVertical="true"
            android:background="@drawable/order_btn"
            >
            
            <ImageView 
                android:layout_width="15dp"
                android:layout_height="15dp"
                android:layout_alignParentLeft="true"
                android:layout_centerVertical="true"
                android:layout_marginLeft="5dp"
                android:src="@drawable/order_pic"/>
            
            <TextView 
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="排序"
                android:layout_margin="5dp"
                android:textSize="15dip"
                android:layout_alignParentRight="true"
           	 	android:layout_centerVertical="true"
                android:textColor="@color/textgray"/>
            
            </RelativeLayout>
		 */
		
}
