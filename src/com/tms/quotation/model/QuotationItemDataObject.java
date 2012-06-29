package com.tms.quotation.model;

import java.util.Date;
import kacang.Application;
import kacang.model.DefaultDataObject;


public class QuotationItemDataObject extends DefaultDataObject {

  private String itemId;
  private String quotationId;
  private String itemTableRow;
  
/*  
  private String itemName;
  private String itemDescription;
  private String itemUnit;
  private String itemQuantity;
  private String itemCost;
  private int version;
*/  
  public QuotationItemDataObject() {}

  public String getItemId() {
    return itemId;
  }
  
  public String getQuotationId() {
    return quotationId;
  }
  
  public String getItemTableRow() {
    return itemTableRow;
  }
  
/*
  public String getItemName() {
    return itemName;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  public String getItemUnit() {
    return itemUnit;
  }

  public String getItemQuantity() {
    return itemQuantity;
  }

  public String getItemCost() {
    return itemCost;
  }
*/
  /* setter methods */

  public void setItemId(String string) {
    itemId = string;
  }
  
  public void setQuotationId( String quotationId ) {
    this.quotationId = quotationId;
  }
  
  public void setItemTableRow( String itemTableRow ) {
    this.itemTableRow = itemTableRow;
  }
  
/*
  public void setItemName(String string) {
    itemName = string;
  }

  public void setItemDescription(String string) {
    itemDescription = string;
  }

  public void setItemUnit(String string) {
    itemUnit = string;
  }

  public void setItemQuantity(String string) {
    itemQuantity = string;
  }

  public void setItemCost(String string) {
    itemCost = string;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
*/
}