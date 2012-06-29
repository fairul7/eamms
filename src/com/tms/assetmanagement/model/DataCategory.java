package com.tms.assetmanagement.model;

import kacang.model.DefaultDataObject;

public class DataCategory extends DefaultDataObject {
	
	private String categoryId;
	private String categoryName;
	private float depreciation;	
	
	//-------getters/setters--------
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public float getDepreciation() {
		return depreciation;
	}
	public void setDepreciation(float depreciation) {
		this.depreciation = depreciation;
	}
}
