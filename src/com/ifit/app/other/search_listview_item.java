package com.ifit.app.other;


public class search_listview_item {

	private String planname;
	private String plantime;
	private String planplace;
	private String plansite;
	private String planpath;
	
	public search_listview_item(String planname,String plantime,String planplace
			,String plansite,String planpath){
		this.planname = planname;
		this.plantime = plantime;
		this.planplace = planplace;
		this.plansite = plansite;
		this.planpath = planpath;
	}
	
	public String getPlanname(){
		return planname;
	}
	public String getPlantime(){
		return plantime;
	}
	public String getPlanplace(){
		return planplace;
	}
	public String getPlansite(){
		return plansite;
	}
	public String getPlanpath(){
		return planpath;
	}
}
