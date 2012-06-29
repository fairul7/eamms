package com.tms.assetmanagement.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class DataItems extends DefaultDataObject {
	
	private String itemId;	
	private String categoryId;
	private String itemName;
	private Date datePurchased;
	private float itemQty;
	private double itemUnitPrice; 
	private double itemCost;
	private String itemDescription;
	
	//	-------getters/setters--------
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Date getDatePurchased() {
		return datePurchased;
	}
	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getItemQty() {
		return itemQty;
	}
	public void setItemQty(float itemQty) {
		this.itemQty = itemQty;
	}
	public double getItemCost() {
		return itemCost;
	}
	public void setItemCost(double itemCost) {
		this.itemCost = itemCost;
	}
	public double getItemUnitPrice() {
		return itemUnitPrice;
	}
	public void setItemUnitPrice(double itemUnitPrice) {
		this.itemUnitPrice = itemUnitPrice;
	}



}