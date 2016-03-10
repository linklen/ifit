package com.ifit.app.other;

import android.graphics.Bitmap;

public class rank_list_item {

	private String rank;
	private String nickname;
	private String weektime;
	private Bitmap userhead;
	
	public rank_list_item(String rank,String nickname,String weektime,Bitmap userhead){
		this.rank = rank;
		this.nickname = nickname;
		this.weektime = weektime;
		this.userhead = userhead;
	}
	public String getrank(){
		return rank;
	}
	public String getnickname(){
		return nickname;
	}
	public String getweektime(){
		return weektime;
	}
	public Bitmap getuserhead(){
		return userhead;
	}
}
