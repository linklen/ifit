package com.ifit.app.other;

public class user_report_manage_item {

	private String content;
	private String type;
	private String id;
	
	public user_report_manage_item(String content,String type,String id){
		this.content = content;
		this.type = type;
		this.id = id;
	}
	
	public String getcontent(){
		return content;
	}
	
	public String gettype(){
		return type;
	}
	
	public String getid(){
		return id;
	}
}
