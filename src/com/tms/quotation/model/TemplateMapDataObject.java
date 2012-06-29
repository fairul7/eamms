package com.tms.quotation.model;

import kacang.model.DefaultDataObject;

public class TemplateMapDataObject extends DefaultDataObject {
  private String templateId;
  private String templateTextId;
  private int sortNum;
  
  public int getSortNum() {
    return sortNum;
  }
  public String getTemplateId() {
    return templateId;
  }
  public String getTemplateTextId() {
    return templateTextId;
  }
  public void setSortNum(int sortNum) {
    this.sortNum = sortNum;
  }
  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }
  public void setTemplateTextId(String templateTextId) {
    this.templateTextId = templateTextId;
  }
  
  
}
