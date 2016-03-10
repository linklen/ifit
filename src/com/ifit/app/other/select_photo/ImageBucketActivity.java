package com.ifit.app.other.select_photo;

import java.io.Serializable;
import java.util.List;

import com.ifit.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageBucketActivity extends Activity {
	// ArrayList<Entity> dataList;//����װ������Դ���б�
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// �Զ����������
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	TextView bucket_cancle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		// /**
		// * ������Ǽ����Ѿ���������߱��ؽ����������ݣ�����ֱ��������ģ����10��ʵ���ֱ࣬��װ���б���
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * ��ʼ��view��ͼ
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		bucket_cancle = (TextView)findViewById(R.id.bucket_cancle);
		
		bucket_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageGridAdapter.selectTotal = 0;
				ImageGridAdapter.map.clear();
				ImageGridActivity.record_Select.clear();;
				finish();
			}
		});
		
		
		adapter = new ImageBucketAdapter(ImageBucketActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * ����position���������Ի�ø�GridView����View��󶨵�ʵ���࣬Ȼ���������isSelected״̬��
				 * ���ж��Ƿ���ʾѡ��Ч���� ����ѡ��Ч���Ĺ��������������Ĵ����л���˵��
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * ֪ͨ���������󶨵����ݷ����˸ı䣬Ӧ��ˢ����ͼ
				 */
				//adapter.notifyDataSetChanged();
				ImageBucket getBucketname = dataList.get(position);
				String bucketname = getBucketname.bucketName;
				//Log.d("xxx", x);
				Intent intent = new Intent(ImageBucketActivity.this,
						ImageGridActivity.class);
				intent.putExtra(ImageBucketActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				//������Activity֮�䴫��һ��ʵ�����,��ô���Ǿͻ��õ�Serializable����Parcelable,�������������������л����ݵ�
				intent.putExtra("bucketname", bucketname);
				startActivity(intent);
				finish();
			}

		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ImageGridAdapter.selectTotal = 0;
		ImageGridAdapter.map.clear();
		ImageGridActivity.record_Select.clear();;
	}
}
