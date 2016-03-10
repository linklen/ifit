package com.ifit.app.adapter;

import java.util.List;

import com.ifit.app.R;
import com.ifit.app.other.user_report_manage_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class user_report_manage_adapter extends ArrayAdapter<user_report_manage_item> {
	
	private int resourceId;
	private ViewHolder viewholder;

	public user_report_manage_adapter(Context context, 
			int textViewResourceId,  List<user_report_manage_item> objects) {
		super(context,  textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		user_report_manage_item item = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.content = (TextView)convertView.findViewById(R.id.content);
			viewholder.type = (TextView)convertView.findViewById(R.id.type);
			viewholder.id = (TextView)convertView.findViewById(R.id.reportid);
			convertView.setTag(viewholder);
			}else{
				viewholder = (ViewHolder)convertView.getTag();
			}
		
		viewholder.content.setText(item.getcontent());
		viewholder.type.setText(item.gettype());
		viewholder.id.setText(item.getid());
		
		return convertView;
	}



	class ViewHolder{
		TextView content;
		TextView type;
		TextView id;
	}
}
