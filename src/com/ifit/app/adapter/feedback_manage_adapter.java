package com.ifit.app.adapter;

import java.util.List;

import com.ifit.app.R;
import com.ifit.app.other.feedback_manage_item;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class feedback_manage_adapter extends ArrayAdapter<feedback_manage_item> {
	
	private int resourceId;
	private ViewHolder viewholder;
	

	public feedback_manage_adapter(Context context,
			int textViewResourceId, List<feedback_manage_item> objects) {
		super(context,textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		feedback_manage_item item = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.content = (TextView)convertView.findViewById(R.id.content);
			viewholder.contact = (TextView)convertView.findViewById(R.id.contact);
			convertView.setTag(viewholder);
			}else{
				viewholder = (ViewHolder)convertView.getTag();
			}
		
		viewholder.content.setText(item.getcontent());
		viewholder.contact.setText(item.getcontact());
		
		return convertView;
	}



	class ViewHolder{
		TextView content;
		TextView contact;
	}
}
