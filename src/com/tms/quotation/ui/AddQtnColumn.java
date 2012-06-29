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


public class AddQtnColumn extends Form {

    public static final String QTNCOLUMN_ADD_SUCCESS="AddQtnColumn.success";
    public static final String QTNCOLUMN_ADD_FAIL="AddQtnColumn.fail";
    public static final String QTNCOLUMN_ADD_EXISTS="AddQtnColumn.exists";

    private static final String [] TEXTALIGN_OPT = {"left=Left", "center=Center", "right=Right" };
    private static final String [] VERTALIGN_OPT = {"top=Top", "center=Center", "bottom=Bottom"};

    private TextField QtnColumnheader;
    private TextField colWidth;
    private SelectBox colTextAlign;
    private SelectBox colVertAlign;
    private CheckBox isCompulsory;
    
    private Button cancel;
    private Button reset;
    private Button submit;
    
    private Label errorMsg;
    private Label lbQtnColumn;
    private String columnId;
    private String tableId;
    
    private String type;
    //private SelectBox [name];
    //private TextBox [name];
    //private TextField [name];

    

    private String canAdd = "0";
    private String canEdit = "0";
    private String canDelete = "0";
    private String whoModified = "whoModified";

    public AddQtnColumn() {}

    public AddQtnColumn(String s) {super(s);}

    public void init() {
        if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}
    }

    public void onRequest(Event event) {
        initForm();
        if ("Edit".equals(type)) {populateFields();}
        else {tableId=event.getRequest().getParameter("tableId"); } 
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
      QtnColumnheader = new TextField("QtnColumnheader");
      QtnColumnheader.setSize("50");
      QtnColumnheader.setMaxlength("255");
      QtnColumnheader.addChild(new ValidatorNotEmpty("QtnColumnheaderNotEmpty", msgNotEmpty));
      addChild(QtnColumnheader);

      colWidth = new TextField("colWidth");
      colWidth.setValue("auto");
      colWidth.setSize("10");
      colWidth.setMaxlength("4");
      addChild(colWidth);

      colTextAlign = new SelectBox("colTextAlign");
      for(int i=0; i<TEXTALIGN_OPT.length; i++) {
        colTextAlign.setOptions(TEXTALIGN_OPT[i]);
      }

      colTextAlign.setSelectedOption("center");
      addChild(colTextAlign);
      colVertAlign = new SelectBox("colVertAlign");

      for(int i=0; i<VERTALIGN_OPT.length; i++) {
        colVertAlign.setOptions(VERTALIGN_OPT[i]);
      }

      colVertAlign.setSelectedOption("center");
      addChild(colVertAlign);

      isCompulsory = new CheckBox("isCompulsory");
      isCompulsory.setText(app.getMessage("com.tms.quotation.column.compulsory"));
      isCompulsory.setValign("center");
      addChild(isCompulsory);
      
      lbQtnColumn = new Label("lbQtnColumn");
      addChild(lbQtnColumn);

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
        QtnColumnDataObject z = new QtnColumnDataObject();
        try {
            z = MODULE.getQtnColumn(columnId);
            if (z != null && z.getColumnId() != null) {
                QtnColumnheader.setValue(z.getHeader());
                lbQtnColumn.setText(z.getColumnId());
                tableId = z.getTableId();
                if( "1".equals(z.getCompulsory())) isCompulsory.setChecked(true);
                else isCompulsory.setChecked(false);
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
            return new Forward(Form.CANCEL_FORM_ACTION, "viewQtnColumn.jsp?tableId="+tableId, true);
        }
        else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
            init();
            if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editQtnColumn.jsp?columnId="+columnId, true);}
            else {return new Forward(Form.CANCEL_FORM_ACTION, "addQtnColumn.jsp?tableId="+tableId, true);}
        }
        else {return result;}
    }

    //template directory = [root]\WEB-INF\templates\default
    public String getDefaultTemplate() {
      return "quotation/addQtnColumn";
      
//    if ("Edit".equals(type)) {return "addQtnColumn";}
//    else {return "addQtnColumn";}
    }

    public Forward onValidate(Event event) {
      Collection cSelected;
      String strSelected;
      
        if ( ("1".equals(canEdit) && "Edit".equals(type)) || ("1".equals(canAdd) && "Add".equals(type)) ) {
            QtnColumnDataObject z = new QtnColumnDataObject();
            QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);

            if ("Edit".equals(type)) {
                try {z = MODULE.getQtnColumn(columnId);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString());}
            }
            else {
              z.setColumnId(UuidGenerator.getInstance().getUuid());
              z.setTableId(tableId);
            }
            z.setHeader(QtnColumnheader.getValue().toString());
            
      // build the column style from the various text-fields and selectboxes 
            StringBuffer sb = new StringBuffer();
            sb.append("width:"+colWidth.getValue().toString()+";");
            
            cSelected = (Collection) colTextAlign.getValue();
            strSelected = cSelected.iterator().next().toString();
            sb.append("text-align:"+strSelected+";");
            
            cSelected = (Collection) colVertAlign.getValue();
            strSelected = cSelected.iterator().next().toString();
            sb.append("vertical-align:"+strSelected+";");

            z.setColumnStyle(sb.toString());

      // set the compulsory flag
            if( isCompulsory.isChecked()) {
              z.setCompulsory("1");
            }
            else {
              z.setCompulsory("0");
            }
            
            if ("Add".equals(type)) {
                //try {MODULE.insertQtnColumn(z.getTableId(), z.getPosition(), z.getHeader(), z.getColumnClassName(), z.getColumnStyle());}
                try {MODULE.insertQtnColumn(z);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QTNCOLUMN_ADD_FAIL);} 
            }
            if ("Edit".equals(type)) {
                try {MODULE.updateQtnColumn(z);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QTNCOLUMN_ADD_FAIL);} 
            }
        }
        populateFields();
        return new Forward(QTNCOLUMN_ADD_SUCCESS);
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String string) {
        columnId = string;
    }

    public void setTableId(String tableId ) {
      this.tableId = tableId;
    }
    
    public String getTableId() {
      return tableId;
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
