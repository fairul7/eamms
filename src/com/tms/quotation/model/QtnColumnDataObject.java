package com.tms.quotation.model;

import kacang.model.DefaultDataObject;

public class QtnColumnDataObject extends DefaultDataObject {

  private String    columnId;
  private String    tableId;
  private int       position;
  private String    header;
  private String    columnClassName;
  private String    columnStyle;
  private String   compulsory;
  
  /* Getters */
  public String getColumnId() {
    return columnId;
  }
  public String getHeader() {
    return header;
  }
  public int getPosition() {
    return position;
  }
  public String getTableId() {
    return tableId;
  }
  public String getWidth() {
    return columnStyle;
  }
  public String getColumnClassName() {
    return columnClassName;
  }
  public String getColumnStyle() {
    return columnStyle;
  }
  public String getCompulsory() {
    return compulsory;
  }
  
  /* Setters */
  public void setColumnId(String columnId) {
    this.columnId = columnId;
  }
  public void setHeader(String header) {
    this.header = header;
  }
  public void setPosition(int position) {
    this.position = position;
  }
  public void setTableId(String tableId) {
    this.tableId = tableId;
  }
  public void setWidth(String style) {
    this.columnStyle = style;
  }
  public void setColumnClassName(String columnClassName) {
    this.columnClassName = columnClassName;
  }
  public void setColumnStyle(String columnStyle) {
    this.columnStyle = columnStyle;
  }
  public void setCompulsory(String compulsory) {
    this.compulsory = compulsory;
  }
 
}
