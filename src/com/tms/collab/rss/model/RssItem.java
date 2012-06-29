package com.tms.collab.rss.model;

import kacang.model.*;

public class RssItem extends DefaultDataObject{
	
	private String categoryId;
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
