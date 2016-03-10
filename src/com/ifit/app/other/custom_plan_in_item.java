package com.ifit.app.other;

public class custom_plan_in_item {
	
	private String planname;
	private String plansite;
	private String planplace;
	private String planpurpose;
	private String planimgpath;
	
	public custom_plan_in_item(String name,String site,String place,String purpose,String imgpath){
		planname = name;
		plansite = site;
		planplace = place;
		planpurpose = purpose;
		planimgpath = imgpath;
	}

	
	public String getPlanName(){
		return planname;
		
	}
	
	public String getPlanSite() {
		return plansite;

	}

	public String getPlanPlace() {

		return planplace;
	}

	public String getPlanPurpose() {

		return planpurpose;
	}

	public String getPlanImagepath() {

		return planimgpath;
	}
}
