package com.ifit.app.other;

public class add_action_listItem {

	private String imgpath;
	private String action_name;
	
	public add_action_listItem(String imgpath,String action_name){
		this.imgpath = imgpath;
		this.action_name = action_name;
	}
	
	public String getImgPath(){
		return imgpath;
	}
	public String getAction_Name(){
		return action_name;
	}
}
