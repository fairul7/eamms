package com.tms.crm.sales.model;

import kacang.model.DefaultDataObject;

public class CategoryObject extends DefaultDataObject{
	private String categoryID;
	private String categoryName;
	/*private String isArchived;*/   
	public String getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/*public String getIsArchived() {
		return isArchived;
	}
	public void setIsArchived(String isArchived) {
		this.isArchived = isArchived;
	} */
	
} 
