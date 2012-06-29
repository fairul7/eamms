package com.tms.quotation.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.tms.quotation.model.QtnColumnDataObject;
import com.tms.quotation.model.QtnTableDataObject;
import com.tms.quotation.model.QuotationDataObject;
import com.tms.quotation.model.QuotationItemDataObject;

import com.tms.quotation.model.QuotationModule;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;

public class QtnItemForm extends Form {
  public static final String QTNITEM_ADD_SUCCESS = "QtnItemAdd.success";
  public static final String QTNITEM_EDIT_SUCCESS = "QtnItemEdit.success";
  public static final String QTNITEM_ADD_FAIL = "QtnItemAdd.fail";
  public static final String QTNITEM_ADD_EXIT = "QtnItemAdd.finished";
  
  private String type;
  
//  private QtnTableDataObject itemTable;
  private String quotationId;
  private String tableId;
  private String itemId;
  
  private List fieldList;
  private Button reset;
  private Button cancel;
  private Button submit;
  private Button exit;
  
//  private Button preview;
  
  public void init() {
    if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}
  }
  
  /* Get a table format from the database, 
   * for each column in the table create a Label and a TextField */
//  public void initForm (String tableId) {
  public void initForm() {
    removeChildren();
    Application app = Application.getInstance();
    QuotationModule module = (QuotationModule)app.getModule(QuotationModule.class);
    
    String msgNotEmpty = app.getMessage("com.tms.quotation.required");
    
    if( null != quotationId && !"".equals(quotationId)) {
      QuotationDataObject q = module.getQuotation(quotationId);
      tableId = q.getTableId();
    }
    else if ( null != itemId && !"".equals(itemId)) {
      QuotationItemDataObject qi = module.getQuotationItem(itemId);
      quotationId = qi.getQuotationId();
      QuotationDataObject q = module.getQuotation(quotationId);
      tableId = q.getTableId();
    }
    
    setMethod("POST");
    fieldList = new ArrayList();

//    itemTable = (QtnTableDataObject)module.selectQtnTable(tableId).iterator().next();
    Collection cols = module.selectQtnTableColumn(tableId);
    int count=1;
    for( Iterator i=cols.iterator(); i.hasNext(); count++ ) {
      QtnColumnDataObject c = (QtnColumnDataObject)i.next();
      StringBuffer labelText = new StringBuffer( c.getHeader() );
      ItemField item = new ItemField("Column"+count);
      item.setColumnId(c.getColumnId());
     
      if( "1".equals(c.getCompulsory()) ) {
        item.getTextField().addChild(new ValidatorNotEmpty(c.getHeader()+"notEmpty",msgNotEmpty));
        labelText.append("&nbsp;*");
      }
      item.getLabel().setText(labelText.toString());
      addChild(item.getLabel());      
      addChild(item.getTextField());
      fieldList.add(item);
    }
    submit = new Button("submit", app.getMessage("com.tms.quotation.button.continue"));
    reset = new Button("reset", app.getMessage("com.tms.quotation.button.reset"));
    cancel = new Button("cancel", app.getMessage("com.tms.quotation.button.cancel"));
    exit = new Button("exit", app.getMessage("com.tms.quotation.button.exit"));
//    preview = new Button("preview", app.getMessage("com.tms.quotation.button.preview"));
    addChild(cancel);
    addChild(reset);
    addChild(submit);
    addChild(exit);
//    addChild(preview);
  }

  
  public void onRequest(Event evt) {
    initForm();
    if( "Edit".equals(type)) {populateFields();}
  }
  
  
  private void populateFields() {
    QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
    QuotationItemDataObject qi = MODULE.getQuotationItem(itemId);
    /* Strip the <td> tags                              *
     * DANGER! this method doesn't handle nested tables */    
    String rowData = qi.getItemTableRow();
    String [] tmpArr = rowData.split("</td>");
    
    for( int i=0; i<fieldList.size(); i++ ) {
      ItemField itm = (ItemField)fieldList.get(i);
      itm.getTextField().setValue(tmpArr[i].replaceFirst("<td>", ""));
    }
  }

  
  public Forward onSubmit(Event evt) {
    String fwdUrl="";
    String buttonName = findButtonClicked(evt);
    kacang.ui.Forward result = super.onSubmit(evt);
    if (buttonName != null) {
      if (cancel.getAbsoluteName().equals(buttonName)) {
        init();
        
        if ("Edit".equals(type)) {
          fwdUrl = "previewQuotation.jsp?quotationId="+quotationId;
        }
        else {
          fwdUrl = "viewQuotation.jsp";
        }

//        fwdUrl = "viewQuotation.jsp";
        result = new Forward(Form.CANCEL_FORM_ACTION, fwdUrl, true);
      }
      else if (reset.getAbsoluteName().equals(buttonName)) {
        init();
        if ("Edit".equals(type)) {
          fwdUrl = "editQtnItem.jsp?quotationId="+quotationId+"&itemId="+itemId;
          result = new Forward(Form.CANCEL_FORM_ACTION, fwdUrl, true);
        }
        else {
          fwdUrl = "qtnItemForm.jsp?quotationId="+quotationId+"&tableId="+tableId;
          result = new Forward(Form.CANCEL_FORM_ACTION, fwdUrl, true);
        }
      }
//    else if( preview.getAbsoluteName().equals(buttonName) ) {
//    fwdUrl = "previewQuotation.jsp?quotationId="+quotationId;
//    result = new Forward(Form.CANCEL_FORM_ACTION, fwdUrl, false);
//    }
    }
    return result;
  }
  
  public Forward onValidate(Event evt) {
    QuotationModule MODULE = (QuotationModule) Application.getInstance().getModule(QuotationModule.class);
    QuotationItemDataObject z = new QuotationItemDataObject();

    Forward fwd = null;
    String buttonName = findButtonClicked(evt);
    boolean exitFlag = buttonName.endsWith("exit");
    
//    Forward result = super.onValidate(evt);
    StringBuffer sb = new StringBuffer();
    if( "Edit".equals(type)){
      try {
        z = MODULE.getQuotationItem(itemId);
      }catch( Exception e ) {Log.getLog(getClass()).error(e.toString());}
      quotationId=z.getQuotationId();
    }
    else {
      z.setQuotationId(quotationId);
    }
    for( Iterator i=fieldList.iterator(); i.hasNext(); ) {
      ItemField item = (ItemField)i.next();
      sb.append("<td>" );
      sb.append(item.getTextField().getValue().toString());
      sb.append("</td>");
    }
    z.setItemTableRow(sb.toString());
   
    if( "Add".equals(type)) {
      z.setItemId(UuidGenerator.getInstance().getUuid());      
      try { MODULE.insertQuotationItem(z); }
      catch (Exception e) {
        Log.getLog(getClass()).error(e.toString());
        return new Forward(QTNITEM_ADD_FAIL);
      }
      if (exitFlag)
        fwd = new Forward(QTNITEM_ADD_EXIT);
      else
        fwd = new Forward(QTNITEM_ADD_SUCCESS);      
    }
    
    if( "Edit".equals(type)) {
      try { MODULE.updateQuotationItem(z); }
      catch (Exception e) { 
        Log.getLog(getClass()).error(e.toString());
        return new Forward(QTNITEM_ADD_FAIL);
      }
      if (exitFlag)
        fwd = new Forward(QTNITEM_ADD_EXIT);
      else
        fwd = new Forward(QTNITEM_ADD_SUCCESS);      
    }
    return fwd;
  }
  
  public String getDefaultTemplate() {
    return "quotation/qtnItemForm";
  }
  
  public List getFieldList() {
    return fieldList;
  }
  
  public String getQuotationId() {
    return quotationId;
  }
  
  public String getTableId() {
    return tableId;
  }
  
  public String getItemId() {
    return itemId;
  }
  
  public String getType() {
    return type;
  }
  
  public void setQuotationId (String quotationId) {
    this.quotationId = quotationId;
  }
  
  public void setTableId (String tableId) {
    this.tableId = tableId;
  }

  public void setItemId (String itemId) {
    this.itemId = itemId;
  }

  public void setType( String type ) {
    this.type = type;
  }
}
