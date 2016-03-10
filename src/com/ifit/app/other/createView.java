package com.ifit.app.other;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifit.app.R;
import com.ifit.app.activity.edit_plan;
import com.ifit.app.other.learnpic_loadImg.ImgCallback;

public class createView {

	private learnpic_loadImg loadimg = new learnpic_loadImg();
	
	private View getView=null;
	private ViewHolder viewholder;
	
	private View temp_view;
	
	public static Map<View,add_action_listItem> Action_out_cache
	= new HashMap<View,add_action_listItem>();
	
	public void init_getView(Context context){
	
	LayoutInflater inflater=LayoutInflater.from(context);
	
	View view = inflater.inflate(R.layout.edit_plan_addview_layout,null);
	
	getView = view;
	}
	
	public  View createView(add_action_listItem item,Context context){
		
		
		
		
			viewholder = new ViewHolder();
			init_getView(context);
			viewholder.action_img = (ImageView)getView.findViewById(R.id.edit_plan_addview_layout_action_pic);
			viewholder.action_name = (TextView)getView.findViewById(R.id.edit_plan_addview_layout_action_name);
			viewholder.group_Number = (TextView)getView.findViewById(R.id.edit_plan_addview_layout_action_group_Number);
			viewholder.Number = (TextView)getView.findViewById(R.id.edit_plan_addview_layout_action_Number);
			viewholder.Number_edit = (ImageView)getView.findViewById(R.id.edit_plan_addview_layout_action_Number_edit);
			viewholder.action_delete = (ImageView)getView.findViewById(R.id.edit_plan_addview_layout_action_delete);
		
			viewholder.rest_time = (TextView)getView.findViewById(R.id.edit_plan_addview_layout_rest_time);
		
			viewholder.action_name.setText(item.getAction_Name());
			pic_loading(item.getImgPath(),viewholder);
			
		
			
			
			viewholder.action_delete.setOnClickListener(new MyClickListen(getView));
			viewholder.rest_time.setOnClickListener(new MyClickListen(context));
			viewholder.Number_edit.setOnClickListener(new MyClickListen(context));
			
			Action_out_cache.put(getView, item);
			
			return getView;
		
	}
	
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
	
	class ViewHolder{
		ImageView action_img;
		TextView action_name;
		TextView group_Number;
		TextView Number;
		ImageView Number_edit;
		ImageView action_delete;
		
		TextView rest_time;
	}
	
	class MyClickListen implements OnClickListener{
		
		View view;
		Context context;
		public MyClickListen(View view){
			this.view = view;
		}
		
		public MyClickListen(Context context){
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.edit_plan_addview_layout_action_delete:
				edit_plan.linearlayout.removeView(view);
				Action_out_cache.remove(view);
				break;
			case R.id.edit_plan_addview_layout_rest_time:
				setIntent(context,v,"WheelView",viewholder.rest_time.getText().toString());
				break;
			case R.id.edit_plan_addview_layout_action_Number_edit:
				String str = viewholder.group_Number.getText().toString()
				+","+viewholder.Number.getText().toString();
				
				
				setIntent(context,v,"WheelView_small",str);
				break;
			}
		}
		
	}
	
	
	public void setIntent(Context context,View v,String type,String Values){
		
		Intent open_dialog = new Intent(context,Edit_Plan_ChangeData_dialog.class);
		open_dialog.putExtra("Id", v.getId());
		open_dialog.putExtra("type", type);
		open_dialog.putExtra("getValues", Values);
		temp_view = v ;
		((Activity) context).startActivityForResult(open_dialog,3);
		((Activity) context).overridePendingTransition(R.anim.slide_downtoin,0);
	}
	
	public View get_tempView(){
		return temp_view;
	}
	

}
