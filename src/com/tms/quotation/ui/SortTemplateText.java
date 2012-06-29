package com.tms.quotation.ui;

import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.quotation.model.QuotationModule;
import com.tms.quotation.model.TemplateMapDataObject;

import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;

public class SortTemplateText extends Form {

  private String templateId;
  
  private SortableSelectBox sbSortTemplateBox;
  private Button submit;
  private Button cancel;
  private Button reset;
  
  public SortTemplateText() {}
  
  public String getDefaultTemplate() {
    return "quotation/sortTemplateText";
  }

  public String getTemplateId() {
    return templateId;
  }
  
  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }
  
  public void onRequest(Event evt) {
    initForm();
  }
  
  public void initForm() {
    removeChildren();
    Application app = Application.getInstance();
    QuotationModule MODULE = (QuotationModule)app.getModule(QuotationModule.class);

    sbSortTemplateBox = new SortableSelectBox("sbSortTemplateBox");
    sbSortTemplateBox.setRows(10);
    sbSortTemplateBox.setOptionMap(MODULE.csbGetSelectedText(templateId, true));
/*    
    sbSortTemplateBox.addOption("-1", "DB - Quotation subject");
    sbSortTemplateBox.addOption("-2", "DB - Customer information");
    sbSortTemplateBox.addOption("-3", "DB - Quotation item table");
    sbSortTemplateBox.addOption("-4", "DB - Date");
*/    
    addChild(sbSortTemplateBox);
    
    submit = new Button("submit", app.getMessage("com.tms.quotation.button.save"));
    reset = new Button("reset", app.getMessage("com.tms.quotation.button.reset"));
    cancel = new Button("cancel", app.getMessage("com.tms.quotation.button.cancel"));
    addChild(submit);
    addChild(cancel);
    addChild(reset);
  }
  
  public Forward onSubmit(Event evt) {
    String buttonName = findButtonClicked(evt);
    kacang.ui.Forward result = super.onSubmit(evt);
    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
        init();
        return new Forward(Form.CANCEL_FORM_ACTION, "viewTemplate.jsp", true);
    }
    else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
        init();
        return new Forward(Form.CANCEL_FORM_ACTION, "sortTemplateText.jsp?templateId="+templateId, true);
    }
    else {return result;}
  }
  
  public Forward onValidate(Event evt) {
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    SequencedHashMap map = (SequencedHashMap)sbSortTemplateBox.getOptionMap();
    for(int c=0; c<map.size(); c++) {
      TemplateMapDataObject tm = MODULE.getTemplateMap(templateId, (String)map.get(c));
      tm.setSortNum(c+1);
      MODULE.updateTemplateMap(tm);
    }
    return new Forward("sort");
  }
}
