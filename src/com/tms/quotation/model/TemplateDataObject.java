package com.tms.quotation.model;

import java.util.Date;
import kacang.Application;
import kacang.model.DefaultDataObject;


public class TemplateDataObject extends DefaultDataObject {

  private String templateId;
  private String templateHeader;
  private String templateFooter;
  private String templateName;
  private Date recdate;
  private String whoModified;
  private String active;
  
public String getActive() {
	return active;
}
public void setActive(String active) {
	this.active = active;
}
public Date getRecdate() {
	return recdate;
}
public void setRecdate(Date recdate) {
	this.recdate = recdate;
}
public String getTemplateName() {
	return templateName;
}
public String getTemplateFooter() {
	return templateFooter;
}
public void setTemplateName(String templateName) {
	this.templateName = templateName;
}
public void setTemplateFooter(String templateFooter) {
	this.templateFooter = templateFooter;
}
public String getTemplateHeader() {
	return templateHeader;
}
public void setTemplateHeader(String templateHeader) {
	this.templateHeader = templateHeader;
}
public String getTemplateId() {
	return templateId;
}
public void setTemplateId(String templateId) {
	this.templateId = templateId;
}
public String getWhoModified() {
	return whoModified;
}
public void setWhoModified(String whoModified) {
	this.whoModified = whoModified;
}

}
