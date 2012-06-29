package com.tms.quotation.ui;

import com.tms.quotation.model.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.stdui.Button;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;


public class AddQtnTable extends Form {

    public static final String QTNTABLE_ADD_SUCCESS="AddQtnTable.success";
    public static final String QTNTABLE_ADD_FAIL="AddQtnTable.fail";
    public static final String QTNTABLE_ADD_EXISTS="AddQtnTable.exists";
    private Button cancel;
    private Button reset;
    private Button submit;
    private Label errorMsg;
    private Label lbQtnTable;
    private String tableId;
    private String type;
    //private SelectBox [name];
    //private TextBox [name];
    //private TextField [name];
    
    private TextField QtnTabletableDescription;
    private RichTextBox QtnTabletableCaption;
//    private TextField QtnTabletableStyle;
    private CheckBox QtnTableActive;
    
    private String canAdd = "0";
    private String canEdit = "0";
    private String canDelete = "0";
    private String whoModified = "whoModified";

    public AddQtnTable() {}

    public AddQtnTable(String s) {super(s);}

    public void init() {
        if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}
    }

    public void onRequest(Event event) {
        initForm();
        if ("Edit".equals(type)) {populateFields();}
    }

    public void initForm() {
      removeChildren();
      setMethod("post");
      Application app = Application.getInstance();
      String msgNotEmpty  = app.getMessage("com.tms.quotation.required");
//    String initialSelect = "-1=--- None ---";
      errorMsg = new Label("errorMsg");
      addChild(errorMsg);

      //[name] = new TextBox("[name]");
      //[name].setCols("25");
      //[name].setRows("4");
      //[name].addChild(new ValidatorNotEmpty("[name]NotEmpty", msgNotEmpty));
      //addChild("[name]");

      QtnTabletableDescription = new TextField("QtnTabletableDescription");
      QtnTabletableDescription.setSize("50");
      QtnTabletableDescription.setMaxlength("255");
      QtnTabletableDescription.addChild(new ValidatorNotEmpty("QtnTabletableDescriptionNotEmpty", msgNotEmpty));
      addChild(QtnTabletableDescription);

      QtnTabletableCaption = new RichTextBox("QtnTabletableCaption");
      QtnTabletableCaption.setSize("50");
      QtnTabletableCaption.setMaxlength("255");
      //QtnTabletableCaption.addChild(new ValidatorNotEmpty("QtnTabletableCaptionNotEmpty", msgNotEmpty));
      addChild(QtnTabletableCaption);

      QtnTableActive = new CheckBox("QtnTableActive");
      QtnTableActive.setText(app.getMessage("com.tms.quotation.active"));
      QtnTableActive.setChecked(true);
      addChild(QtnTableActive);
//    QtnTabletableStyle = new TextField("QtnTabletableStyle");
//    QtnTabletableStyle.setSize("50");
//    QtnTabletableStyle.setMaxlength("255");
//    //QtnTabletableStyle.addChild(new ValidatorNotEmpty("QtnTabletableStyleNotEmpty", msgNotEmpty));
//    addChild(QtnTabletableStyle);

      lbQtnTable = new Label("lbQtnTable");
      addChild(lbQtnTable);

      submit = new Button("submit", app.getMessage("com.tms.quotation.button.save"));
      reset = new Button("reset", app.getMessage("com.tms.quotation.button.reset"));
      cancel = new Button("cancel", app.getMessage("com.tms.quotation.button.cancel"));
      if (!"1".equals(canAdd) && "Add".equals(type)) {submit.setHidden(true);}
      if (!"1".equals(canEdit) && "Edit".equals(type)) {submit.setHidden(true);}
      addChild(submit);
      addChild(reset);
      addChild(cancel);
    }

    public void populateFields() {
        QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
        QtnTableDataObject z = new QtnTableDataObject();
        try {
            z = MODULE.getQtnTable(tableId);
            if (z != null && z.getTableId() != null) {
                QtnTabletableDescription.setValue(z.getTableDescription());
                QtnTabletableCaption.setValue(z.getTableCaption());
//              QtnTabletableStyle.setValue(z.getTableStyle());
                lbQtnTable.setText(z.getTableId());
                if("1".equals(z.getActive())) { QtnTableActive.setChecked(true); }
                else { QtnTableActive.setChecked(false); }
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }

    public Forward onSubmit(Event evt) {
        String buttonName = findButtonClicked(evt);
        kacang.ui.Forward result = super.onSubmit(evt);
        if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
            init();
            return new Forward(Form.CANCEL_FORM_ACTION, "viewQtnTable.jsp", true);
        }
        else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
            init();
            if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editQtnTable.jsp?id="+tableId, true);}
            else {return new Forward(Form.CANCEL_FORM_ACTION, "addQtnTable.jsp", true);}
        }
        else {return result;}
    }

    //template directory = [root]\WEB-INF\templates\default
    public String getDefaultTemplate() {
      return "quotation/addQtnTable";
//        if ("Edit".equals(type)) {return "addQtnTable";}
//        else {return "addQtnTable";}
    }

    public Forward onValidate(Event event) {
        if ( ("1".equals(canEdit) && "Edit".equals(type)) || ("1".equals(canAdd) && "Add".equals(type)) ) {
            QtnTableDataObject z = new QtnTableDataObject();
            QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);

            if ("Edit".equals(type)) {
                try {z = MODULE.getQtnTable(tableId);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString());}
            }
            else {
              tableId = UuidGenerator.getInstance().getUuid();
              z.setTableId(tableId);
            }
            z.setTableDescription(QtnTabletableDescription.getValue().toString());
            z.setTableCaption(QtnTabletableCaption.getValue().toString());
//          z.setTableStyle(QtnTabletableStyle.getValue().toString());
            if(QtnTableActive.isChecked()) { z.setActive("1"); }
            else{ z.setActive("0"); }
              
            if ("Add".equals(type)) {
                //try {MODULE.insertQtnTable(z.getTableDescription(), z.getTableCaption(), z.getTableStyle());}
                try {MODULE.insertQtnTable(z);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QTNTABLE_ADD_FAIL);} 
            }
            if ("Edit".equals(type)) {
                try {MODULE.updateQtnTable(z);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QTNTABLE_ADD_FAIL);} 
            }
        }
        populateFields();
        return new Forward(QTNTABLE_ADD_SUCCESS);
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String string) {
        tableId = string;
    }

    public String getType() {
        return type;
    }

    public void setType(String string) {
        type = string;
    }

    public String getCanAdd() {return canAdd;}
    public void setCanAdd(String string) {canAdd = string;}
    public String getCanEdit() {return canEdit;}
    public void setCanEdit(String string) {canEdit = string;}
    public String getCanDelete() {return canDelete;}
    public void setCanDelete(String string) {canDelete = string;}
    public String getWhoModified() {return whoModified;}
    public void setWhoModified(String string) {whoModified = string;}
}

/*
package com.tms.quotation.ui;

import javax.servlet.http.HttpServletRequest;

import com.tms.quotation.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class AddQtnTable extends Form {
  public static final String QTNTABLE_ADD_SUCCESS="AddQtnTable.success";
  public static final String QTNTABLE_ADD_FAIL="AddQtnTable.fail";
  public static final String QTNTABLE_ADD_EXISTS="AddQtnTable.exists";

  public static final String QTNTABLE_NEXT = "setupTable.next";
  
  private String tableId;
  private String type;
  
  private TextField         setupTableDesc;
  private RichTextBox       setupTableCaption;

  private Button cancel;
  private Button reset;
  private Button submit;
  
  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";
  private String whoModified = "whoModified";
  
  public AddQtnTable() {}
  
  public AddQtnTable(String name) {
    super(name);
  }

  public void init() {
    if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}    
  }
  
  private void initForm(){
    setMethod("POST");
    removeChildren();

    setupTableDesc      = new TextField("setupTableDesc");
    setupTableDesc.setSize("60");
    setupTableDesc.setMaxlength("255");
    addChild(setupTableDesc);
    
    setupTableCaption   = new RichTextBox("setupTableCaption");
    setupTableCaption.setRows("5");
    addChild(setupTableCaption);    
    
    cancel  = new Button("cancel", "Cancel");
    reset   = new Button("reset","Reset");
    submit  = new Button("submit","Next");

    addChild(cancel);
    addChild(reset);
    addChild(submit);
  }

  public void onRequest(Event evt) {
    initForm();
    if( "Edit".equals(type)) {
      populateFields();
    }
  }
  
  public void populateFields () {
    QuotationModule module = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
    try {
      QtnTableDataObject z = (QtnTableDataObject) module.selectQtnTable(tableId).iterator().next();
      if( null != z ) {
        setupTableDesc.setValue(z.getTableDescription());
        setupTableCaption.setValue(z.getTableCaption());
      }
    } catch (Exception e) {
      Log.getLog(getClass()).error(e.toString());
    }
  }
  
  public Forward onSubmit(Event evt) {
    Forward result = super.onSubmit(evt);
    String buttonName = findButtonClicked(evt);

    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
        init();
        result = new Forward(Form.CANCEL_FORM_ACTION, "viewQuotation.jsp", true);
    }    
    else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
      init();
      if("Edit".equals(type)) {
        result = new Forward(Form.CANCEL_FORM_ACTION, "editQtnTable.jsp?tableId="+tableId, true);
      }
      else {
        result = new Forward(Form.CANCEL_FORM_ACTION, "addQtnTable.jsp", true);
      }
    }
    return result;
  }
 
  public Forward onValidate(Event evt) {
    Forward result = super.onValidate(evt);
    
    QtnTableDataObject z = new QtnTableDataObject();
    
    z.setTableDescription(setupTableDesc.getValue().toString());    
    z.setTableCaption(setupTableCaption.getValue().toString());

    HttpServletRequest req = evt.getRequest();
    req.setAttribute(z.getClass().getName(), z);
    result = new Forward(QTNTABLE_NEXT);
    return result;
  }
  
  public String getDefaultTemplate() {
    return "quotation/addQtnTable";
  }
  
  public String getTableId() {
    return tableId;
  }
  
  public void setTableId(String tableId) {
    this.tableId = tableId;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }
  
  public String getCanAdd() {return canAdd;}
  public void setCanAdd(String string) {canAdd = string;}
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}
  public String getWhoModified() {return whoModified;}
  public void setWhoModified(String string) {whoModified = string;}  
}
*/