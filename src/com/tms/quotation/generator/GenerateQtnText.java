package com.tms.quotation.generator;

import com.tms.quotation.model.*;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class GenerateQtnText extends LightWeightWidget {
  private QtnTemplateTextDataObject textObject;
  private String id;
  private String canEdit;
  public GenerateQtnText() {}
  
  public String getDefaultTemplate() {
    return "quotation/generator/templateText";
  }
  
  public void onRequest(Event evt) {
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    textObject = MODULE.getQtnTemplateText(id);
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String templateId) {
    this.id = templateId;
  }
  
  public QtnTemplateTextDataObject getTextObject() {
    return textObject;
  }

  public String getCanEdit() {
    return canEdit;
  }

  public void setCanEdit(String canEdit) {
    this.canEdit = canEdit;
  }
}
