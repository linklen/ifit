package com.ifit.app.adapter;

import java.util.List;

import com.ifit.app.R;
import com.ifit.app.other.CircleImageDrawable;
import com.ifit.app.other.rank_list_item;
import com.ifit.app.other.CircleImageDrawable;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class rank_list_adapter extends ArrayAdapter<rank_list_item> {

	private int resourceId;
	private ViewHolder viewholder;
	
	public rank_list_adapter(Context context, 
			int textViewResourceId, List<rank_list_item> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		rank_list_item item = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.order = (TextView)convertView.findViewById(R.id.rank);
			viewholder.userhead = (ImageView)convertView.findViewById(R.id.user_head);
			viewholder.user_nickname = (TextView)convertView.findViewById(R.id.user_nikename);
			viewholder.week_time = (TextView)convertView.findViewById(R.id.weektime);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		viewholder.order.setText(item.getrank());
		viewholder.userhead.setImageDrawable(new CircleImageDrawable(item.getuserhead()));
		viewholder.user_nickname.setText(item.getnickname());
		viewholder.week_time.setText(item.getweektime());
		
		return convertView;
	}

	class ViewHolder{
		TextView order;
		ImageView userhead;
		TextView user_nickname;
		TextView week_time;
	}
}
