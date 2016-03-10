package com.ifit.app.adapter;

import java.util.List;

import com.ifit.app.R;
import com.ifit.app.admin.learn_manage;
import com.ifit.app.other.learn_manage_listitem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class learn_manage_adapter extends ArrayAdapter<learn_manage_listitem> {

	private int resourceId;
	private ViewHolder viewholder;
	
	public learn_manage_adapter(Context context,
			int textViewResourceId, List<learn_manage_listitem> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}


	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		learn_manage_listitem item = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder = new ViewHolder();
			viewholder.learn_id = (TextView)convertView.findViewById(R.id.art_id);
			viewholder.learn_title = (TextView)convertView.findViewById(R.id.art_title);
			viewholder.layout = (LinearLayout)convertView.findViewById(R.id.learn_manager_item_layout);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)convertView.getTag();
		}
		
		viewholder.learn_id.setText(item.getid());
		viewholder.learn_title.setText(item.gettitle());
		viewholder.layout.setOnLongClickListener(new MyLongOnClickListen(position));
		
		return convertView;
	}



	class ViewHolder {
		TextView learn_id;
		TextView learn_title;
		LinearLayout layout;
		
	}

	class MyLongOnClickListen implements OnLongClickListener{
		
		private int position;
		
		public MyLongOnClickListen(int position){
			this.position = position;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.learn_manager_item_layout){
				
				show_dialog(position);
				
			}
			
			return false;
		}
		
	}
	
	private void show_dialog(final int position){
		
		new AlertDialog.Builder(getContext()).setMessage("是否删除这篇文章？")
		.setNegativeButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				learn_manage activity = (learn_manage)getContext();
				activity.delete_learn(getItem(position));
			}
		}).setPositiveButton("否", null).show();
		
	}
}
