package com.ifit.app.adapter;

import java.io.File;
import java.util.List;

import com.ifit.app.R;
import com.ifit.app.activity.Custom_plan;
import com.ifit.app.adapter.learnItemAdapter.ViewHolder;
import com.ifit.app.other.custom_plan_in_item;
import com.ifit.app.other.learnpic_loadImg;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class custom_plan_in_ItemAdapter extends ArrayAdapter<custom_plan_in_item> {
	
	private int resourceId;
	private ViewHolder viewholder;
	private learnpic_loadImg loadimg = new learnpic_loadImg();
	
	
	private void pic_loading(String imgpath, final ViewHolder viewholder) {

		Bitmap cacheimg = loadimg.loadBitmap(imgpath, new ImgCallback() {

			@Override
			public void imageLoaded(Bitmap bm) {
				// TODO Auto-generated method stub
				viewholder.Image.setImageBitmap(bm);
			}
		});
		if (cacheimg != null) {
			viewholder.Image.setImageBitmap(cacheimg);
		}
	}
	

	public custom_plan_in_ItemAdapter(Context context, 
			int textViewResourceId, List<custom_plan_in_item> objects) {
		super(context,textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		custom_plan_in_item item = getItem(position);
		
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.Image = (ImageView)convertView.findViewById(R.id.custom_plan_in_item_image);
			viewholder.name = (TextView)convertView.findViewById(R.id.custom_plan_in_item_name);
			viewholder.site = (TextView)convertView.findViewById(R.id.custom_plan_in_item_site);
			viewholder.place = (TextView)convertView.findViewById(R.id.custom_plan_in_item_place);
			viewholder.purpose = (TextView)convertView.findViewById(R.id.custom_plan_in_item_purpose);
			viewholder.btn = (ImageView)convertView.findViewById(R.id.custom_plan_in_item_btn_kuozhan);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}
		
		viewholder.name.setText(item.getPlanName());
		viewholder.site.setText(item.getPlanSite());
		viewholder.place.setText(item.getPlanPlace());
		viewholder.purpose.setText(item.getPlanPurpose());
		pic_loading(item.getPlanImagepath(),viewholder);
		viewholder.btn.setOnClickListener(new MyOnClickListen(item.getPlanImagepath(),item));
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView Image;
		ImageView btn;
		TextView name;
		TextView site;
		TextView place;
		TextView purpose;
	}
	
	class MyOnClickListen implements OnClickListener{

		String imgpath;
		custom_plan_in_item item;
		public MyOnClickListen(String imgpath,custom_plan_in_item item){
			this.imgpath = imgpath;
			this.item = item;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.custom_plan_in_item_btn_kuozhan:
				//这里添加扩展按钮
				popushow(v,imgpath,item);
				break;
			}
		}
		
	}
	
	public void popushow(View v,final String imgpath,final custom_plan_in_item item){
		View contentView = LayoutInflater.from(getContext()).
				inflate(R.layout.delete_popu_menu, null);
		RelativeLayout btn_delete = (RelativeLayout)contentView.findViewById(R.id.delete_popu_layout);
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		btn_delete.measure(w, h);
		int width = btn_delete.getMeasuredWidth();
		int height = btn_delete.getMeasuredHeight();
		final PopupWindow popu = new PopupWindow(contentView,
				width,height,true);
		btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getContext(), "show", 
						//Toast.LENGTH_SHORT).show();
				showdiolog(imgpath,item);
				popu.dismiss();
			}
		});
		
		
		
		popu.setTouchable(true);
		
		popu.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				
				return false;
				
				//这里如果返回true,touch事件将被拦截
				//拦截后popu的ontouchevent不被调用，这样点击外部区域无法dismis
			}
		});
		
		//如果不设置popu的背景，无论是点击外部区域还是back键都步法dissmiss弹窗
		popu.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.completely_transparent));

		popu.showAsDropDown(v,-60, -60,Gravity.RIGHT);
				
	}
	
	public void showdiolog(final String imgpath,final custom_plan_in_item item){
		new AlertDialog.Builder(getContext()).setMessage("确认删除此计划?")
		.setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				File path = new File (imgpath);
				File parent =path.getParentFile();
				for(File i : parent.listFiles()){
					if(i.isFile()){
						i.delete();
					}
				}
				parent.delete();
				
				Custom_plan activity = (Custom_plan)getContext();
				activity.itemlist.remove(item);
				notifyDataSetChanged();
				
				Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT);
				
			}
		}).setPositiveButton("否", null).show();
	}
}
