package com.tms.quotation.model;

import kacang.model.DefaultDataObject;

public class QtnContentDataObject extends DefaultDataObject {
  private String contentType;
  private String contentId;
  private String quotationId;
  private int sortNum;
  
  public QtnContentDataObject() {}
  
  public QtnContentDataObject(String quotationId) {
    this.quotationId = quotationId;
  }
  
  public String getContentId() {
    return contentId;
  }
  public String getContentType() {
    return contentType;
  }
  public String getQuotationId() {
    return quotationId;
  }
  public int getSortNum() {
    return sortNum;
  }
  public void setContentId(String contentId) {
    this.contentId = contentId;
  }
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  public void setQuotationId(String quotationId) {
    this.quotationId = quotationId;
  }
  public void setSortNum(int sortNum) {
    this.sortNum = sortNum;
  }
  
}
