package com.ifit.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.db.MyDatabaseHelper;
import com.ifit.app.other.select_photo.Bimp;
import com.ifit.app.other.select_photo.FileUtils;
import com.ifit.app.other.select_photo.ImageBucketActivity;
import com.ifit.app.other.select_photo.PhotoActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class News_PublishedActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send,check_editor;
	public File sdCard,directory;
	
	private ImageView published_btn_back;
	private EditText published_editor;
	
	private SharedPreferences saveEditor,getUsername;
	private SharedPreferences.Editor save_editor;
	private String login_name;
	
	private MyDatabaseHelper usedb;
	private SQLiteDatabase db;
	private File dbfile;
	private ProgressDialog wait;
	private String timedb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_published_layout);
		Init();
		load_temp();
	}

	public void Init() {
		
		
		wait = new ProgressDialog(this);
		wait.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		wait.setMessage("wait...");
		wait.setIndeterminate(false);
		wait.setCancelable(false);
		
		
		saveEditor = getSharedPreferences("save_temp_editor", MODE_PRIVATE);
		getUsername = getSharedPreferences("islogin", MODE_PRIVATE);
		login_name = getUsername.getString("user", "");
		save_editor = saveEditor.edit();
		
		sdCard = Environment.getExternalStorageDirectory();
		directory = new File(sdCard.getAbsolutePath()+
				 "/DCIM/Camera/");
		if(!directory.exists()){
		directory.mkdirs();}
		
		
		
		usedb = new MyDatabaseHelper(this, "DataBase.db", null, 1);
		db = usedb.getWritableDatabase();
		
		published_btn_back = (ImageView)findViewById(R.id.published_layout_btn_back);
		published_btn_back.setOnClickListener(new MyOnClickListener());
		
		published_editor = (EditText)findViewById(R.id.published_layout_editor);
		check_editor = (TextView)findViewById(R.id.published_layout_editor_check);
		published_editor.addTextChangedListener(new MyTextWatcher());
		
		
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(News_PublishedActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(News_PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		activity_selectimg_send = (TextView)findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new MyOnClickListener());
		/*
		 * activity_selectimg_send.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { 
		 * List<String> list = new ArrayList<String>();
		 * for (int i = 0; i < Bimp.drr.size(); i++) {
		 * String Str = Bimp.drr.get(i).substring(
		 * Bimp.drr.get(i).lastIndexOf("/") + 1,
		 * Bimp.drr.get(i).lastIndexOf("."));
		 * list.add(FileUtils.SDPATH+Str+".JPEG"); } 
		 * // 高清的压缩图片全部就在 list 路径里面了
		 * // 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面 
		 * // 完成上传服务器后 .........
		 * FileUtils.deleteDir(); } });
		 */
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;  // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				/*holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));*/
				holder.image.setImageResource(R.drawable.bt_add_pic);
				if (position == 4) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
				
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image,image_delete;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.published_popupwindows, null);
			//view.startAnimation(AnimationUtils.loadAnimation(mContext,
			//		R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			//ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			//		R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(News_PublishedActivity.this,
							ImageBucketActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 1;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(directory,
					String.valueOf(System.currentTimeMillis())+ ".jpg");
		try{
			file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 4 && resultCode == -1) {
				Bimp.drr.add(path);
				//Log.d("xxx", ""+Bimp.max);
			}
			break;
		}
	}
	
	
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.published_layout_btn_back:
				back_dialog();
				break;
			case R.id.activity_selectimg_send:
				if((published_editor.getText().toString().equals("")) && Bimp.drr.isEmpty()){
					Toast.makeText(News_PublishedActivity.this, "发布内容不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					send_news_todb();}
				break;
			}
			
		}
		
	}

	// 监视字数改变
	class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			check_editor.setText((200-published_editor.length()) + "/200");
		}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
			
		}
	
	
	public void back_dialog(){
		
		if((published_editor.getText().toString().equals("")) && Bimp.drr.isEmpty()){
			finish();
		}else{
			new AlertDialog.Builder(News_PublishedActivity.this).setMessage("是否保存此次编辑？")
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					save_editor.clear();
					save_editor.commit();
					Bimp.bmp.clear();
					Bimp.drr.clear();
					Bimp.max = 0;
					FileUtils.deleteDir();
					finish();
					db.close();
					
				}
			}).setPositiveButton("是", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					String temp_text = published_editor.getText().toString();
					save_editor.putString("temp_text", temp_text);
					Bimp.bmp.clear();
					Bimp.drr.clear();
					Bimp.max = 0;
					save_editor.commit();
					finish();
					db.close();
				}
			}).show();
		}
	}

	
	
	public void load_temp(){
		String get_temp_text = saveEditor.getString("temp_text", "").toString();
		if(!get_temp_text.equals("")){
			published_editor.setText(get_temp_text);
			check_editor.setText((200-get_temp_text.length())+"/200");
		}
		File getPic = new File(FileUtils.SDPATH);
		if(getPic.exists()&&getPic.isDirectory()){
			if(getPic.list().length>0){
				for (File file : getPic.listFiles()) {
					String path = file.getAbsolutePath();
					Bimp.drr.add(path);
					Bitmap tempbit = BitmapFactory.decodeFile(path);
					Bimp.bmp.add(tempbit);
					Bimp.max ++;
				}
			}
		}
	}
	
	
	public void send_news_todb(){
		
		wait.show();
		
		/*
		 * activity_selectimg_send.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { 
		 * List<String> list = new ArrayList<String>();
		 * for (int i = 0; i < Bimp.drr.size(); i++) {
		 * String Str = Bimp.drr.get(i).substring(
		 * Bimp.drr.get(i).lastIndexOf("/") + 1,
		 * Bimp.drr.get(i).lastIndexOf("."));
		 * list.add(FileUtils.SDPATH+Str+".JPEG"); } 
		 * // 高清的压缩图片全部就在 list 路径里面了
		 * // 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面 
		 * // 完成上传服务器后 .........
		 * FileUtils.deleteDir(); } });
		 */
		//这里获取时间，如果是真实服务器，应该获取网络时间，这样不会随着用户的时间更改而改变
		Time now_time = new Time();//这种方法获取时间虽然要写很多代码，但是好控制
		now_time.setToNow();
		String year = ""+now_time.year;
		String month = ""+(now_time.month+1);
		String date = ""+now_time.monthDay;
		String hour = ""+now_time.hour;
		String minute = ""+now_time.minute;
		String second = ""+now_time.second;
		
		String timefile =year+month+date+hour+minute+second;
		
		
		if(month.length() == 1){
			month = "0"+month;
		}
		if(date.length() == 1){
			date = "0"+date;
		}
		if(hour.length() == 1){
			hour = "0"+hour;
		}
		if(minute.length() == 1){
			minute = "0"+minute;
		}
		
		timedb = year+month+date+hour+minute;
		
		
		//要是真实服务器，应该用URI和HTTP解析，并且保存图片和文字的事情应当交给服务器去做
		//这里只是把图片，文字等数据发送到服务器，并且发送一个保存指令即可
		//现在本机就是服务器，就不用网络服务了，直接用于保存
		dbfile = new File(sdCard.getAbsolutePath()+
				"/ifit/dbfile/User_news/"+login_name+"/"+timefile+"/");
		if(!dbfile.exists()){
			dbfile.mkdirs();
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				boolean success = false;
				int picnumber = 0;
				//将编写的内容保存
				String text = published_editor.getText().toString();
				//发送至服务器
				File saveFile = new File(dbfile,"news_text.txt");
				
				//图片
				Bitmap savebm;
				File savePic = null;
				FileOutputStream out;
				
				try {
					if(published_editor.getText().length()>0){
					FileOutputStream savetext = new FileOutputStream(saveFile);
					savetext.write(text.getBytes());
					savetext.close();}
					for(int i = 0;i<Bimp.max;i++){
						savebm = Bimp.bmp.get(i);
						savePic = new File(dbfile,(i+1)+".JPEG");
						picnumber++;
						out = new FileOutputStream(savePic);
						savebm.compress(Bitmap.CompressFormat.JPEG, 100, out);
						out.flush();
						out.close();}
					
					Message message = new Message();
					message.what = 1;
					success = true;
					save_feedback.sendMessage(message);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = new Message();
					message.what = 2;
					success = false;
					message.obj = dbfile;
					save_feedback.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = new Message();
					message.what = 2;
					message.obj = dbfile;
					success = false;
					save_feedback.sendMessage(message);
				}
				//将编写的图片保存到文件夹
				
				db.beginTransaction(); //开启事务，如果上面的数据传输不成功，则数据库不写入
				try{
					if(!success){
						throw new NullPointerException();
					}
					ContentValues values = new ContentValues();
					values.put("name", login_name);
					values.put("time", timedb);
					String dbkey = "imagepath_";
					for(int i=0;i<picnumber;picnumber--){
						values.put(dbkey+picnumber, dbfile.toString()+"/"+picnumber+".JPEG");
					}
					/*values.put("imagepath_1",dbfile.toString()+"1.JPEG");
					values.put("imagepath_2",dbfile.toString()+"2.JPEG");
					values.put("imagepath_3",dbfile.toString()+"3.JPEG");
					values.put("imagepath_4",dbfile.toString()+"4.JPEG");*/
					
					if(saveFile.exists()){
					values.put("words", dbfile.toString()+"/news_text.txt");}
				
					db.insert("User_news_table", null, values);
					db.setTransactionSuccessful();
				}catch(Exception e){
					e.printStackTrace();
					if(success){
						Message message = new Message();
						message.what = 2;
						message.obj = dbfile;
						save_feedback.sendMessage(message);
					}
				}finally{
					db.endTransaction();//事物结束
				}
				
			}
		}).start();
		
		//Log.d("xxx", time);
		
	}
	
	
	Handler save_feedback = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				wait.dismiss();
				Toast.makeText(News_PublishedActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
				save_editor.clear();
				save_editor.commit();
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				FileUtils.deleteDir();
				finish();
				db.close();
				break;
			case 2:
				wait.dismiss();
				Toast.makeText(News_PublishedActivity.this, "发布失败，请重试！", Toast.LENGTH_SHORT).show();
				String path = msg.obj.toString();
				File delfilepath = new File(path);
				if(delfilepath.exists()&&delfilepath.isDirectory()){
					if(delfilepath.list().length>0){
						for (File file : delfilepath.listFiles()) {
							if (file.isFile()){
								file.delete();}//发布失败删除该文件夹下面的所有文件
						}
					}
					delfilepath.delete();//删除文件夹本身
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		back_dialog();
	}
	
	
}
