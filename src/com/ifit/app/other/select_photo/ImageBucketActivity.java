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
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
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
	 * 初始化数据
	 */
	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
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
	 * 初始化view视图
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
				 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
				 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
				 */
				//adapter.notifyDataSetChanged();
				ImageBucket getBucketname = dataList.get(position);
				String bucketname = getBucketname.bucketName;
				//Log.d("xxx", x);
				Intent intent = new Intent(ImageBucketActivity.this,
						ImageGridActivity.class);
				intent.putExtra(ImageBucketActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				//在两个Activity之间传递一个实体对象,那么我们就会用到Serializable或者Parcelable,这两个方法是用来序列化数据的
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
