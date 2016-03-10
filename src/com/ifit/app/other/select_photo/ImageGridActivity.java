package com.ifit.app.other.select_photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ifit.app.R;
import com.ifit.app.other.select_photo.ImageGridAdapter.TextCallback;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	// ArrayList<Entity> dataList;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;
	TextView back_to_bucket,cancle,bucket_name;
	
	
	
	
	public static Map<String,List>record_Select = new HashMap<String, List>();
	List<String> record_position = new ArrayList<String>();
	String getBucketName;
	String getItemPosition;
	ImageItem getItem;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "���ѡ��4��ͼƬ", 400).show();
				break;

			default:
				break;
			}
		}
	};

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		Intent getIntent = getIntent();
		dataList = (List<ImageItem>) getIntent.getSerializableExtra(
				EXTRA_IMAGE_LIST);
		getBucketName = getIntent.getStringExtra("bucketname");
		

		initView();
		bt = (Button) findViewById(R.id.bt);
		bucket_name = (TextView)findViewById(R.id.image_grid_name);
		bucket_name.setText(getBucketName);
		back_to_bucket = (TextView)findViewById(R.id.backtobucket);
		cancle = (TextView)findViewById(R.id.cancle);
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.selectTotal = 0;//��¼ѡ���˼���
				adapter.map.clear();//��¼ѡ���ͼƬ��·��
				record_Select.clear();//����ʱ��ȡѡ���position
				finish();
			}
		});
		back_to_bucket.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ImageGridActivity.this,
							ImageBucketActivity.class);
				
				startActivity(intent);
				record_Select.put(getBucketName, record_position);
				finish();
				
			}
		});
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				ArrayList<String> list = new ArrayList<String>();
				//List�������Collection,list�Ǽ̳�Collection�ģ�
				Collection<String> c = adapter.map.values();
				//Java���༯(Collection)���ʹ��ĳ����������ķ�����׼����Collection��һ���༯���
				Iterator<String> it = c.iterator();
				//Iterator java�����һ������������Ҫ����ȡ�������������ֵ
				for (; it.hasNext();) {
					list.add(it.next());
					}
				/*if (Bimp.act_bool) {
				Intent intent = new Intent(ImageGridActivity.this,
						PublishedActivity.class);
				startActivity(intent);
				Bimp.act_bool = false;
				}*/
				
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 4) {
						Bimp.drr.add(list.get(i));
						}
				}
				adapter.selectTotal = 0;
				adapter.map.clear();
				record_Select.clear();
				finish();
				
			}

		});
	}


	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {//�����ⲿ����
			public void onListen(int count) {
				bt.setText("���" + "(" + count + ")");
				
				
				
				
				
				getItemPosition = ""+adapter.getPosition;
				
				getItem =dataList.get(adapter.getPosition);
				//Log.d("xxx", ""+getItem.isSelected);
				if(getItem.isSelected){
					//Log.d("xxx", ""+getItem.isSelected);
					record_position.add(getItemPosition);
				}else{
					//Log.d("xxx", ""+getItem.isSelected);
					record_position.remove(getItemPosition);
				}
				
				
				
				//Log.d("xxx", ""+record_position.size());
				//Log.d("xxx", record_position.get(record_position.size()-1).toString());
				
				//Log.d("xxx", ""+adapter.getPosition);
			}
		});
		
		/*gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				//ImageItem i =dataList.get(position);
				//boolean x=i.isSelected;
				//String f= i.imageId;
				//String c = i.imagePath;
				//String a = i.thumbnailPath;
				//Log.d("xxx", f);
				//Log.d("xxx", c);
				//Log.d("xxx", a);
				Log.d("ssiii", "12312313");
				
				adapter.notifyDataSetChanged();
			
				
			}

		});*////�������ˣ��޷���ȡ

	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		isSelect();//�ٷ������ʱ�������һ���Ƿ���ѡ��
	}
	
	 public void isSelect(){
		 if(adapter.selectTotal>0){
			 adapter.textcallback.onListen(adapter.selectTotal);//���õײ���ť������
			 if(record_Select.containsKey(getBucketName)){
				 record_position = record_Select.get(getBucketName);
				 int size = record_position.size();
				 for(int i=0;i<size;i++){
					 int position = Integer.parseInt(record_position.get(i));//ȡ��λ�Ӽ�¼������ת����int��
					 getItem = dataList.get(position);
					 getItem.isSelected = true;
					 
				 }
				 
			 }
			 
		 }
	 }


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		adapter.selectTotal = 0;
		adapter.map.clear();
		record_Select.clear();
	}
	 
	 
	 
	
}
