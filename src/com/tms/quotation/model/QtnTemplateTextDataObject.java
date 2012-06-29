package com.tms.quotation.model;

import kacang.model.DefaultDataObject;

public class QtnTemplateTextDataObject extends DefaultDataObject {
  
  public static String DB_SUBJECT_ID = "-255";
  public static String DB_CUSTOMER_ID = "-254";
  public static String DB_TABLE_ID = "-253";
  public static String DB_DATE_ID = "-252";
  
    
  private String templateId;
  private String templateDescription;
  private String templateBody;
  private String active;
  
  public String getTemplateId() { return templateId; }
  public String getTemplateDescription() { return templateDescription; }
  public String getTemplateBody() { return templateBody; }
  public String getActive() {return active; }
  
  public void setTemplateId( String templateId ) { this.templateId = templateId; }
  public void setTemplateDescription (String templateDescription ) {this.templateDescription = templateDescription; }
  public void setTemplateBody ( String templateBody ) { this.templateBody = templateBody; }
  public void setActive( String active ) { this.active = active ; }
}
