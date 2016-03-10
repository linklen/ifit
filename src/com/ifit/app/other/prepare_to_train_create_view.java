package com.ifit.app.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifit.app.R;
import com.ifit.app.other.createView.MyClickListen;
import com.ifit.app.other.createView.ViewHolder;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

public class prepare_to_train_create_view {

	private View getView;
	private ViewHolder viewholder;
	private learnpic_loadImg loadimg = new learnpic_loadImg();
	
	
	private void pic_loading(String imgpath,final ViewHolder viewholder){
		
		Bitmap cacheimg =loadimg.loadBitmap(imgpath,
				new ImgCallback() {
					
					@Override
					public void imageLoaded(Bitmap bm) {
						// TODO Auto-generated method stub
						viewholder.action_img.setImageBitmap(bm);
					}
				});
		if(cacheimg != null){
			viewholder.action_img.setImageBitmap(cacheimg);
		}
	
	}		
		
	public void init_getView(Context context){
		
		LayoutInflater inflater=LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.prepare_to_train_listitem,null);
		getView = view;
		
		}
	
	
	
	public  View createView(String imgpath,String Action_name,String group,String order,Context context){
		
		
		viewholder = new ViewHolder();
		init_getView(context);
		viewholder.action_img = (ImageView)getView.findViewById(R.id.prepare_to_train_list_action_img);
		viewholder.action_name = (TextView)getView.findViewById(R.id.prepare_to_train_list_action_name);
		viewholder.group = (TextView)getView.findViewById(R.id.prepare_to_train_list_action_group);
		viewholder.order = (TextView)getView.findViewById(R.id.prepare_to_train_list_action_order);
	
	
		viewholder.action_name.setText(Action_name);
		viewholder.group.setText(group);
		viewholder.order.setText(order);
		pic_loading(imgpath,viewholder);
		
		
		return getView;
	
}
	
	
	
	
	class ViewHolder{
		ImageView action_img;
		TextView action_name;
		TextView group;
		TextView order;
	}
}
