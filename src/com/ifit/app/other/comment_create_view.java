package com.ifit.app.other;

import com.ifit.app.R;
import com.ifit.app.activity.news_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class comment_create_view {

	private View getView;
	private ViewHolder viewholder;
	
	
	
	public void init_getView(Context context){
		
		LayoutInflater inflater=LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.comments_item_layout,null);
		getView = view;
		}
	
	public View createView(String commenttitle,String commenttext,boolean mycomment,
			String commenttime,String commentname,Context context){
		
		viewholder = new ViewHolder();
		init_getView(context);
		viewholder.comment_title = (TextView)getView.findViewById(R.id.comments_title);
		viewholder.comment_content = (TextView)getView.findViewById(R.id.comments_content);
		if(mycomment){
			viewholder.comment_delete = (TextView)getView.findViewById(R.id.delete_comment);
			viewholder.comment_delete.setVisibility(View.VISIBLE);
			viewholder.comment_delete.setOnClickListener(new MyOnClickListen(getView,commenttime,commentname,commenttitle));
		}
		
		viewholder.comment_title.setText(commenttitle);
		viewholder.comment_title.setOnClickListener(new MyOnClickListen(getView,commenttime,commentname,commenttitle));
		
		viewholder.comment_content.setText(commenttext);
		
		return getView;
	}
	
	
	class ViewHolder {
		TextView comment_title;
		TextView comment_content;
		TextView comment_delete;
	}
	
	class MyOnClickListen implements OnClickListener{

		View view;
		String comment_time;
		String comment_name;
		String comment_title;
		
		
		public MyOnClickListen(View view,String comment_time,String comment_name,String comment_title){
			this.view = view;
			this.comment_time = comment_time;
			this.comment_name = comment_name;
			this.comment_title = comment_title;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.delete_comment:
				news_details activity = (news_details)view.getContext();
				activity.delete_comment_view(view,comment_time,comment_name,comment_title);
				break;
			case R.id.comments_title:
				
				news_details activity_other = (news_details)view.getContext();
				String[] x = comment_title.split("»Ø¸´");
				
				if(x.length == 2){
					String name = x[0];
					activity_other.edit_comment.setHint("»Ø¸´"+":"+name);
					
				}else{
					String name = x[0].substring(0,x[0].length()-1);
					activity_other.edit_comment.setHint("»Ø¸´"+":"+name);
				}
				
				break;
			}
		}
		
	}
}
