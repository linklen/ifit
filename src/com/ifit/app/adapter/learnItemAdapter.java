package com.ifit.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifit.app.R;
import com.ifit.app.other.learnItem;
import com.ifit.app.other.learnpic_loadImg;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

public class learnItemAdapter extends ArrayAdapter<learnItem> {

	private int resourceId;
	private ViewHolder viewholder;
	private learnpic_loadImg loadimg = new learnpic_loadImg();
	
	public learnItemAdapter(Context context, 
			int textViewResourceId, List<learnItem> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}

	private void pic_loading(String imgpath,final ViewHolder viewholder){
		
		Bitmap cacheimg =loadimg.loadBitmap(imgpath,
				new ImgCallback() {
					
					@Override
					public void imageLoaded(Bitmap bm) {
						// TODO Auto-generated method stub
						viewholder.Image.setImageBitmap(bm);
					}
				});
		if(cacheimg != null){
			viewholder.Image.setImageBitmap(cacheimg);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		learnItem item = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.Image = (ImageView)convertView.findViewById(R.id.learn_item_image);
			viewholder.Title = (TextView)convertView.findViewById(R.id.learn_item_text);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}
		
		viewholder.Title.setText(item.getTitle());
		pic_loading(item.getImagePath(),viewholder);
		
		return convertView;
	}

	class ViewHolder{
		ImageView Image;
		TextView Title;
	}
	
}
