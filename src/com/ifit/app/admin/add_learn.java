package com.ifit.app.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.CircleImageDrawable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
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
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class add_learn extends Activity {

	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private EditText title_edit,url_edit;
	private Button complete_btn;
	private ImageView add_Image;
	public static final int CHOOSE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	
	public File sdCard,directory,dbfile,output_image,temp_image;
	public Uri temp_imageUri;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_learn);
		
		title_edit = (EditText)findViewById(R.id.add_learn_Title_edit);
		url_edit = (EditText)findViewById(R.id.add_learn_url_edit);
		complete_btn = (Button)findViewById(R.id.add_learn_edit_complete);
		add_Image = (ImageView)findViewById(R.id.add_learn_selectImage);
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/ifit/dbfile/learn/");
		if(!directory.exists()){
			directory.mkdirs();}
		temp_image = new File(directory,"temp_image.jpg");
		temp_imageUri = Uri.fromFile(temp_image);
		
		add_Image.setOnClickListener(new MyOnClickListen());
		complete_btn.setOnClickListener(new MyOnClickListen());
	}

	class MyOnClickListen implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.add_learn_selectImage:
				Intent open_Photo_album = new Intent(
						"android.intent.action.GET_CONTENT");
				open_Photo_album.setType("image/*");
				startActivityForResult(open_Photo_album, CHOOSE_PHOTO);//�����
				break;
			case R.id.add_learn_edit_complete:
				String getTitle = title_edit.getText().toString();
				String getUrl = url_edit.getText().toString(); 
				boolean getImage = temp_image.exists();
				if(getTitle.equals("")||getUrl.equals("")||!getImage){
					new AlertDialog.Builder(add_learn.this).setTitle("ע��").setMessage("���⣬ͼƬ����ַ������ȱһ���ɣ�")
					.setPositiveButton("OK", null).show();
				}else{
					createfile();
					output_db();
					Toast.makeText(add_learn.this, "�����ɹ���", Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			}
		}
		
	}
	
	public void createfile(){
		Time now_time = new Time();//���ַ�����ȡʱ����ȻҪд�ܶ���룬���Ǻÿ���
		now_time.setToNow();
		String year = ""+now_time.year;
		String month = ""+(now_time.month+1);
		String date = ""+now_time.monthDay;
		String hour = ""+now_time.hour;
		String minute = ""+now_time.minute;
		String second = ""+now_time.second;
		
		String timefile =year+month+date+hour+minute+second;
		
		dbfile = new File(directory+"/"+timefile+"/");
		if(!dbfile.exists()){
			dbfile.mkdirs();
		}
		
		output_image = new File(dbfile,"Image.jpg");
		//Ҫ����ʵ��������Ӧ����URI��HTTP���������ұ���ͼƬ�����ֵ�����Ӧ������������ȥ��
		//����ֻ�ǰ�ͼƬ�����ֵ����ݷ��͵������������ҷ���һ������ָ���
		//���ڱ������Ƿ��������Ͳ�����������ˣ�ֱ�����ڱ���
		if(output_image.exists()){
			output_image.delete();
		}
		try{
			Bitmap bitmap = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(temp_imageUri));
			FileOutputStream out = new FileOutputStream(output_image);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			Log.d("xxx", "ͼƬ�Ѿ�����");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void output_db(){
		String getTitle = title_edit.getText().toString();
		String getUrl = url_edit.getText().toString(); 
		String getImagePath = output_image.toString();
		
		ContentValues values = new ContentValues();
		values.put("learn_title", getTitle);
		values.put("learn_url", getUrl);
		values.put("learn_imagepath",getImagePath);
		
		db.insert("learn_table", null, values);
		//Log.d("xxx", "�Ѿ��������ݿ�");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case CHOOSE_PHOTO:
				if(resultCode == RESULT_OK){
				
				
				if(Build.VERSION.SDK_INT >= 19){
					//4.4������ϵͳʹ�������������ͼƬ
					handleImageOnKitkat(data);
				}else{
					handleImageBeforeKitKat(data);
				}
			
			}
			break;
		case CROP_PHOTO:
			if(resultCode == RESULT_OK){
				try{
					Bitmap bitmap = BitmapFactory
									.decodeStream(getContentResolver().openInputStream(temp_imageUri));
					add_Image.setImageBitmap(bitmap);
				}catch(FileNotFoundException e){
					e.printStackTrace();
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
			//�����document���͵�uri����ͨ��document id����
			String docId = DocumentsContract.getDocumentId(uri);
			if("com.android.providers.media.documents".equals(uri.getAuthority())){
				String id = docId.split(":")[1];//���������ָ�ʽ��id
				String selection = MediaStore.Images.Media._ID + "=" +id;
				imagePath = getImagePath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
			}else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docId));
				imagePath = getImagePath(contentUri,null);
			}
		}else if("content".equalsIgnoreCase(uri.getScheme())){
			//�������document���͵�uri����ʹ����ͨ�ķ�ʽ����
			imagePath = getImagePath(uri,null);
		}
		displayImage(imagePath);//����ͼƬ·����ʾͼƬ
	}
	
	private void handleImageBeforeKitKat(Intent data){
		Uri uri = data.getData();
		String imagePath = getImagePath(uri,null);
		displayImage(imagePath);
	}
	
	private String getImagePath(Uri uri,String selection){
		String path = null;
		//ͨ��Uri��selection����ȡ��ʵ��ͼƬ·��
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
			intent.putExtra("aspectX", 3);
			intent.putExtra("aspectY", 2);//aspectX aspectY �ǿ�ߵı���
			intent.putExtra("outputX", 600);
			intent.putExtra("outputY", 400);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, temp_imageUri);
			startActivityForResult(intent, CROP_PHOTO);
			
		}else{
			Toast.makeText(this, "�Բ��𣬶�ȡʧ�ܣ�", Toast.LENGTH_SHORT).show();
		}
		
	}

@Override
public void finish() {
	// TODO Auto-generated method stub
	super.finish();
	
	if(temp_image.exists()){
		temp_image.delete();
	}
}


}
