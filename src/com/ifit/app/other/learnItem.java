package com.ifit.app.other;

public class learnItem {
	private String Title;
	private String ImagePath;
	private String url;
	
	public learnItem(String Title ,String ImagePath,String Url){
		this.Title = Title;
		this.ImagePath = ImagePath;
		this.url = Url;
	}
	
	public String getTitle(){
		return Title;
	}
	public String getImagePath(){
		return ImagePath;
	}
	public String geturl(){
		return url;
	}
}
