package com.tms.quotation.ui;

import java.util.Collection;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.quotation.model.QtnColumnDataObject;
import com.tms.quotation.model.QuotationModule;

import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;

public class SortQtnColumn extends Form {
  public static String SORT_COLUMN_SUCCESS = "sortQtnColumn.success";
  
  private String tableId;
  
  private SortableSelectBox columnSort;
  private Button cancel;
  private Button reset;
  private Button submit;
  
  public String getTableId() { return tableId; }
  public void setTableId(String tableId ) {this.tableId = tableId;}
  
  public void onRequest(Event evt) {
    initForm();
  }
  
  public void initForm() {
    removeChildren();
    Application app = Application.getInstance();
    QuotationModule MODULE = (QuotationModule)app.getModule(QuotationModule.class);
    
    setMethod("POST");
    columnSort = new SortableSelectBox("columnSort");
    Collection col = MODULE.selectQtnTableColumn(tableId);
    columnSort.setOptions(col, "columnId", "header");
    addChild(columnSort);
    
    submit = new Button("submit", app.getMessage("com.tms.quotation.button.save"));
    reset = new Button("reset", app.getMessage("com.tms.quotation.button.reset"));
    cancel = new Button("cancel", app.getMessage("com.tms.quotation.button.cancel"));
    
    addChild(cancel);
    addChild(reset);
    addChild(submit);
    
  }
  
  public Forward onSubmit( Event evt ) {
    Forward result = super.onSubmit(evt);
    String buttonName = findButtonClicked(evt);
    if( null != buttonName ) { 
      if (reset.getAbsoluteName().equals(buttonName)) {
        result = new Forward(Form.CANCEL_FORM_ACTION,"sortQtnColumn.jsp?tableId="+tableId,true);        
      }
      else if( cancel.getAbsoluteName().equals(buttonName)) {
        result = new Forward(Form.CANCEL_FORM_ACTION,"viewQtnColumn.jsp?tableId="+tableId,true);
      }
    }
    return result;
  }
  
  public Forward onValidate(Event evt) {
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    SequencedHashMap sorted = (SequencedHashMap) columnSort.getOptionMap();
    for(int c=0; c<sorted.size(); c++) {
      QtnColumnDataObject z = MODULE.getQtnColumn((String)sorted.get(c));
      z.setPosition(c+1);
      MODULE.updateQtnColumn(z);
    }
    
    return new Forward(SORT_COLUMN_SUCCESS);
  }
  
  public String getDefaultTemplate(){
    return "quotation/sortQtnColumn";
  }
}
