package com.ifit.app.other;

import android.graphics.Bitmap;

public class newsItem {

	private Bitmap user_headimg;//头像
	private String username;//用户名
	private String text;//内容
	private String publishTime;//发布时间
	private int pic_Count;//图片数
	private String imageId_one;//地址
	private String imageId_two;
	private String imageId_three;
	private String imageId_four;
	public int good;//点赞人数
	public boolean islike;
	private int mark;//评论人数
	private String news_id;
	
	
	public newsItem(Bitmap user_headimg,String username,String publishTime,String text,
			int mark,int pic_Count,String news_id,int good,boolean islike){
		
		this.user_headimg = user_headimg;
		this.username = username;
		this.text = text;
		this.publishTime = publishTime;
		this.good = good;
		this.mark = mark;
		this.pic_Count = pic_Count;
		this.news_id = news_id;
		this.islike = islike;
	}
	
	public newsItem(Bitmap user_headimg,String username,String publishTime,String text,
			String imageId_one,int mark,int pic_Count,String news_id,int good,boolean islike){
		this.user_headimg = user_headimg;
		this.username = username;
		this.text = text;
		this.publishTime = publishTime;
		this.imageId_one = imageId_one;
		this.good = good;
		this.mark = mark;
		this.pic_Count = pic_Count;
		this.news_id = news_id;
		this.islike = islike;
	}
	
	public newsItem(Bitmap user_headimg,String username, String publishTime,String text, 
			String imageId_one,String imageId_two,int mark,int pic_Count,String news_id,int good
			,boolean islike) {
		this.user_headimg = user_headimg;
		this.username = username;
		this.text = text;
		this.publishTime = publishTime;
		this.imageId_one = imageId_one;
		this.imageId_two = imageId_two;
		this.good = good;
		this.mark = mark;
		this.pic_Count = pic_Count;
		this.news_id = news_id;
		this.islike = islike;
	}
	
	public newsItem(Bitmap user_headimg,String username, String publishTime,String text, 
			String imageId_one,String imageId_two,
			String imageId_three,int mark,int pic_Count,String news_id,int good,boolean islike) {
		this.user_headimg = user_headimg;
		this.username = username;
		this.text = text;
		this.publishTime = publishTime;
		this.imageId_one = imageId_one;
		this.imageId_two = imageId_two;
		this.imageId_three = imageId_three;
		this.good = good;
		this.mark = mark;
		this.pic_Count = pic_Count;
		this.news_id = news_id;
		this.islike = islike;
	}
	
	public newsItem(Bitmap user_headimg,String username,String publishTime,String text, 
			String imageId_one,String imageId_two,
			String imageId_three,String imageId_four,
			int mark,int pic_Count,String news_id,int good,boolean islike) {
		this.user_headimg = user_headimg;
		this.username = username;
		this.text = text;
		this.publishTime = publishTime;
		this.imageId_one = imageId_one;
		this.imageId_two = imageId_two;
		this.imageId_three = imageId_three;
		this.imageId_four = imageId_four;
		this.good = good;
		this.mark = mark;
		this.pic_Count = pic_Count;
		this.news_id = news_id;
		this.islike = islike;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getText(){
		return text;
	}
	
	public String getPublishTime(){
		return publishTime;
	}
	
	public String getImage_one_id(){
		return imageId_one;
	}
	public String getImage_two_id(){
		return imageId_two;
	}
	public String getImage_three_id(){
		return imageId_three;
	}
	public String getImage_four_id(){
		return imageId_four;
	}
	public int getlike_count(){
		return good;
	}
	public boolean getislike(){
		return islike;
	}
	public int getMark(){
		return mark;
	}
	public int getPicCount(){
		return pic_Count;
	}
	public Bitmap getBitmap(){
		return user_headimg;
	}
	public String getNews_id(){
		return news_id;
	}
}
