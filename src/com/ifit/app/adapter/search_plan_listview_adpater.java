package com.ifit.app.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ifit.app.R;
import com.ifit.app.adapter.learnItemAdapter.ViewHolder;
import com.ifit.app.other.learnItem;
import com.ifit.app.other.search_listview_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class search_plan_listview_adpater extends ArrayAdapter<search_listview_item> {

	private int resourceId;
	private ViewHolder viewholder;
	
	public Map<Integer,Boolean> ischeck = new HashMap<Integer,Boolean>();
	
	public search_plan_listview_adpater(Context context,
			int textViewResourceId, List<search_listview_item> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		search_listview_item item = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.planname = (TextView)convertView.findViewById(R.id.search_plan_listitem_planname);
			viewholder.plantime = (TextView)convertView.findViewById(R.id.search_plan_listitem_plantime);
			viewholder.planplace = (TextView)convertView.findViewById(R.id.search_plan_listitem_planplace);
			viewholder.plansite = (TextView)convertView.findViewById(R.id.search_plan_listitem_plansite);
			viewholder.select = (RadioButton)convertView.findViewById(R.id.search_plan_listitem_plan_radio);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}
		if(ischeck.containsKey(position)){
			viewholder.select.setChecked(ischeck.get(position));
		}else{
			ischeck.put(position, false);
		}
		viewholder.planname.setText(item.getPlanname());
		viewholder.plantime.setText(item.getPlantime());
		viewholder.planplace.setText(item.getPlanplace());
		viewholder.plansite.setText(item.getPlansite());
		viewholder.select.setOnClickListener(new MyOnClickListen(position));
		return convertView;
	}

	class ViewHolder{
		TextView planname;
		TextView plantime;
		TextView planplace;
		TextView plansite;
		RadioButton select;
	}
	
	class MyOnClickListen implements OnClickListener{

		int position;
		public MyOnClickListen(int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((RadioButton)v).setChecked(true);
			for(int key : ischeck.keySet()){
				ischeck.put(key, false);
			}
			ischeck.put(position, true);
			notifyDataSetChanged();
		}
		
	}
}
