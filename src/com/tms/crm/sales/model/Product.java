/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Product extends DefaultDataObject {
	private String  category;
	private String  categoryName;
	private String  productID;
	private String  productName;
	private boolean isArchived;
	
	
	public String getProductID() {
		return productID;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public String getIsArchived() {
		if (isArchived) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public void setProductID(String string) {
		productID = string;
	}
	
	public void setProductName(String string) {
		productName = string;
		if (productName != null) {
			productName = productName.trim();
		}
	}
	
	public void setIsArchived(String string) {
		if (string.equals("1")) {
			isArchived = true;
		} else {
			isArchived = false;
		}
	}
	
	public String toString() {
		return(
			"<Sales Product" +
			" productID=\"" + getProductID() + "\"" +
			" productName=\"" + getProductName() + "\"" +
			" isArchived=\"" + getIsArchived() + "\"" +
			" />"
		);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}