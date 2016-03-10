package com.ifit.app.other;

public class feedback_manage_item {

	
	private String content;
	private String contact;
	private int id;
	
	public feedback_manage_item(String content,String contact,int id){
		this.content = content;
		this.contact = contact;
		this.id = id;
	}
	
	public String getcontent(){
		return content;
	}
	
	public String getcontact(){
		return contact;
	}
	
	public int getid(){
		return id;
	}
}
