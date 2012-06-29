package com.tms.quotation.generator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import kacang.Application;
import kacang.ui.LightWeightWidget;
import kacang.ui.Event;

import com.tms.quotation.model.*;

public class GenerateQuotation extends LightWeightWidget {
  private String quotationId;
  private String canEdit;
  
  private QuotationDataObject      quotationObject;
  private Collection contentList;
//private TemplateDataObject       templateObject;
  
  public GenerateQuotation()
  {
    canEdit="1";
  }
  
  public String getDefaultTemplate() {
    return "quotation/generator/previewQuotation";
  }

  public void onRequest( Event evt ){
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    quotationId=evt.getRequest().getParameter("quotationId");
    if( quotationId != null ){
      quotationObject = MODULE.getQuotation(quotationId);
      contentList = MODULE.selectQtnContent(quotationId);
      if("Approved".equals(quotationObject.getStatus())) {
        canEdit="0";
      }
    }
  }

  public String getQuotationId() {
    return quotationId;
  }

  public void setQuotationId(String quotationId) {
    this.quotationId = quotationId;
  }

  public QuotationDataObject getQuotationObject() {
    return quotationObject;
  }
  
  public Collection getContentList() {
    return contentList;
  }
/*
  public TemplateDataObject getTemplateObject() {
    return templateObject;
  }
*/

  public String getCanEdit() {
    return canEdit;
  }

  public void setCanEdit(String canEdit) {
    this.canEdit = canEdit;
  }  
}
