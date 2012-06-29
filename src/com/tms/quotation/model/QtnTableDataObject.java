package com.tms.quotation.model;

import java.util.Collection;

import kacang.model.DefaultDataObject;

public class QtnTableDataObject extends DefaultDataObject {

  private String tableId;
  private String tableDescription;
  private String tableCaption;
  private String tableStyle;
  private String active;
  
/*  
  private int numOfColumns;
  private Collection columnList;
*/  
  /* Getters */
  public String getTableCaption() {
    return tableCaption;
  }
  public String getTableId() {
    return tableId;
  }
  public String getTableStyle() {
    return tableStyle;
  }
/*  
  public int getNumOfColumns() {
    return numOfColumns;
  }
  public Collection getColumnList() {
    return columnList;
  }
*/  
  public String getTableDescription() {
    return tableDescription;
  }
  public String getActive() {
    return active;
  }
  
  /* Setters */
  public void setTableCaption(String tableCaption) {
    this.tableCaption = tableCaption;
  }
  public void setTableId(String tableId) {
    this.tableId = tableId;
  }
  public void setTableStyle(String tableStyle) {
    this.tableStyle = tableStyle;
  }
/*  
  public void setNumOfColumns(int num) {
    this.numOfColumns = num;
  }
  public void setColumnList(Collection lst) {
    this.columnList=lst;
  }
*/  
  public void setTableDescription(String tableDescription) {
    this.tableDescription = tableDescription;
  }
  public void setActive(String active) {
    this.active = active;
  }
}
