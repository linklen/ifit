package com.ifit.app.other;

public class learn_manage_listitem {

	private String learn_id;
	private String learn_title;
	private String learn_imgpath;
	
	public learn_manage_listitem(String learn_id,String learn_title,String learn_imgpath){
		this.learn_id = learn_id;
		this.learn_title = learn_title;
		this.learn_imgpath = learn_imgpath;
	}
	
	public String getid(){
		return learn_id;
	}
	
	public String gettitle(){
		return learn_title;
	}
	
	public String get_imgpath(){
		return learn_imgpath;
	}
	
}
