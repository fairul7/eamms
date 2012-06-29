package com.tms.quotation.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.tms.quotation.model.QuotationDataObject;
import com.tms.quotation.model.QuotationModule;
//import com.tms.quotation.model.TemplateDataObject;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class GenerateQtnTable extends LightWeightWidget {
  private String id;
  
//  private QuotationDataObject qdo;
//  private TemplateDataObject quotationTemplate;
  private Collection quotationItems;
  private Collection tableColumns;
  private String tableId;
  private String canEdit;
  
  public GenerateQtnTable(){
    
  }
  
  public void init(){
    
  }
  
  public void onRequest(Event evt) {
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    QuotationDataObject qdo = (QuotationDataObject) MODULE.getQuotation(id);
    tableId = qdo.getTableId();
    tableColumns = MODULE.selectQtnTableColumn(tableId);    
    quotationItems = MODULE.selectQuotationItemQtnId(id);
    

/*    
    Collection itemIds = null;
    QuotationItemMapDataObject z = new QuotationItemMapDataObject();
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    if( null != quotationId ){
      quotationItems = new ArrayList();
      itemIds=MODULE.selectQuotationItemMap(quotationId);
      for(Iterator i = itemIds.iterator(); i.hasNext();){
        z = (QuotationItemMapDataObject) i.next();
        quotationItems.add(MODULE.getQuotationItem(z.getItemId()));
      }
    }
*/    
  }
  
  public String getDefaultTemplate() {
    return "quotation/generator/quotationItemTable";
  }

  public String getId() {
    return id;
  }

  public void setId(String quotationId) {
    this.id = quotationId;
  }

  public Collection getQuotationItems() {
    return quotationItems;
  }

  public Collection getTableColumns() {
    return tableColumns;
  }
  
  public String getTableId() {
    return tableId;
  }

  public String getCanEdit() {
    return canEdit;
  }
  
  public void setCanEdit( String string ) {
    canEdit = string;
  }
  
/*  public TemplateDataObject getQuotationTemplate() {
    return quotationTemplate;
  }
*/  
}
