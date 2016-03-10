package com.ifit.app.adapter;

import java.util.List;

import com.ifit.app.R;
import com.ifit.app.activity.add_plan_action;
import com.ifit.app.other.add_action_listItem;
import com.ifit.app.other.learnpic_loadImg;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class add_plan_action_adapter extends ArrayAdapter<add_action_listItem> {
	
	private int resourceId;
	private ViewHolder viewholder;
	private learnpic_loadImg loadimg = new learnpic_loadImg();

	public add_plan_action_adapter(Context context,
			int textViewResourceId, List<add_action_listItem> objects) {
		super(context,textViewResourceId, objects);
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
		add_action_listItem item = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.Image = (ImageView)convertView.findViewById(R.id.add_plan_action_listitem_img);
			viewholder.name = (TextView)convertView.findViewById(R.id.add_plan_action_listitem_name);
			viewholder.check = (ImageView)convertView.findViewById(R.id.add_plan_action_listitem_check);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}
			viewholder.check.setFocusable(false);
			viewholder.name.setText(item.getAction_Name());
			pic_loading(item.getImgPath(),viewholder);
			
			if(add_plan_action.itemCache.containsKey(item.getAction_Name())){
				viewholder.check.setSelected(true);
				viewholder.check.setImageResource(R.drawable.icon_data_select);
			}else{
				viewholder.check.setSelected(false);
				viewholder.check.setImageResource(R.drawable.check_none);
			}
			
			viewholder.check.setOnClickListener(new MyOnClickListen(item));
		return convertView;
	}
	
	
	class MyOnClickListen implements OnClickListener{

		add_action_listItem item;
		public MyOnClickListen(add_action_listItem item){
			this.item = item;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.add_plan_action_listitem_check:
				
				if(((ImageView)v).isSelected()){
					((ImageView)v).setImageResource(R.drawable.check_none);
					((ImageView)v).setSelected(false);
					add_plan_action.itemCache.remove(item.getAction_Name());
					add_plan_action.MapKey.remove(item.getAction_Name());
				}else{
					((ImageView)v).setImageResource(R.drawable.icon_data_select);
					((ImageView)v).setSelected(true);
					add_plan_action.itemCache.put(item.getAction_Name(), item);
					add_plan_action.MapKey.add(item.getAction_Name());
				}
				
				
				break;
			
			
			}
		}
		
	}
	
	class ViewHolder{
		ImageView Image;
		TextView name;
		ImageView check;
	}
}
