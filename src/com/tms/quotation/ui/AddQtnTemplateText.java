package com.tms.quotation.ui;

import com.tms.quotation.model.*;
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


public class AddQtnTemplateText extends Form {

    public static final String QTNTEMPLATETEXT_ADD_SUCCESS="AddQtnTemplateText.success";
    public static final String QTNTEMPLATETEXT_ADD_FAIL="AddQtnTemplateText.fail";
    public static final String QTNTEMPLATETEXT_ADD_EXISTS="AddQtnTemplateText.exists";
    private Button cancel;
    private Button reset;
    private Button submit;
    private Label errorMsg;
    private Label lbQtnTemplateText;
    private String templateId;
    private String type;
    //private SelectBox [name];
    //private TextBox [name];
    //private TextField [name];
    private TextBox QtnTemplateDescription;
    private RichTextBox QtnTemplateBody;
    private CheckBox QtnTemplateactive;
    
    private String canAdd = "0";
    private String canEdit = "0";
    private String canDelete = "0";
    private String whoModified = "whoModified";

    public AddQtnTemplateText() {}

    public AddQtnTemplateText(String s) {super(s);}

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

      QtnTemplateDescription = new TextBox("QtnTemplateDescription");
      QtnTemplateDescription.setCols("80");
      QtnTemplateDescription.setRows("4");
      QtnTemplateDescription.setMaxlength("255");
      QtnTemplateDescription.addChild(new ValidatorNotEmpty("QtnTemplateDescriptionNotEmpty", msgNotEmpty));
      addChild(QtnTemplateDescription);

      QtnTemplateBody = new RichTextBox("QtnTemplateBody");
//    QtnTemplateBody.setSize("50");
//    QtnTemplateBody.setMaxlength("255");
      //QtnTemplateTexttemplateBody.addChild(new ValidatorNotEmpty("QtnTemplateTexttemplateBodyNotEmpty", msgNotEmpty));
      addChild(QtnTemplateBody);

      QtnTemplateactive = new CheckBox("QtnTemplateactive");
      QtnTemplateactive.setText(app.getMessage("com.tms.quotation.active"));
      QtnTemplateactive.setChecked(true);
      addChild(QtnTemplateactive);
      
      lbQtnTemplateText = new Label("lbQtnTemplateText");
      addChild(lbQtnTemplateText);

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
        QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();
        try {
            z = MODULE.getQtnTemplateText(templateId);
            if (z != null && z.getTemplateId() != null) {
                QtnTemplateDescription.setValue(z.getTemplateDescription());
                QtnTemplateBody.setValue(z.getTemplateBody());
                lbQtnTemplateText.setText(z.getTemplateId());
                if( "1".equals(z.getActive())) { QtnTemplateactive.setChecked(true); }
                else { QtnTemplateactive.setChecked(false);}
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
            return new Forward(Form.CANCEL_FORM_ACTION, "viewQtnTemplateText.jsp", true);
        }
        else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
            init();
            if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editQtnTemplateText.jsp?id="+templateId, true);}
            else {return new Forward(Form.CANCEL_FORM_ACTION, "addQtnTemplateText.jsp", true);}
        }
        else {return result;}
    }

    //template directory = [root]\WEB-INF\templates\default
    public String getDefaultTemplate() {
      return "quotation/addQtnTemplateText";
//        if ("Edit".equals(type)) {return "addQtnTemplateText";}
//        else {return "addQtnTemplateText";}
    }

    public Forward onValidate(Event event) {
        if ( ("1".equals(canEdit) && "Edit".equals(type)) || ("1".equals(canAdd) && "Add".equals(type)) ) {
            QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();
            QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);

            if ("Edit".equals(type)) {
                try {z = MODULE.getQtnTemplateText(templateId);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString());}
            }
            z.setTemplateDescription(QtnTemplateDescription.getValue().toString());
            z.setTemplateBody(QtnTemplateBody.getValue().toString());
            if( QtnTemplateactive.isChecked() ) { z.setActive("1"); }
            else { z.setActive("0"); }
            
            if ("Add".equals(type)) {
                //try {MODULE.insertQtnTemplateText(z.getTemplateDescription(), z.getTemplateBody());}
                try {MODULE.insertQtnTemplateText(z);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QTNTEMPLATETEXT_ADD_FAIL);} 
            }
            if ("Edit".equals(type)) {
                try {MODULE.updateQtnTemplateText(z);}
                catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QTNTEMPLATETEXT_ADD_FAIL);} 
            }
        }
        populateFields();
        return new Forward(QTNTEMPLATETEXT_ADD_SUCCESS);
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String string) {
        templateId = string;
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

/*package com.tms.quotation.ui;

import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.quotation.model.*;

public class AddQtnTemplateText extends Form {
  public static final String TEMPLATE_TEXT_ADD = "templateText.add";
  public static final String TEMPLATE_TEXT_FAIL = "templateText.fail";
  
  private TextBox tmplDescription;
  private RichTextBox tmplBody;
  
  private Button submit, cancel, reset;
  
  private String tmplTextId;
  
  public String getTmplTextId() { return tmplTextId; }
  public void setTemplTextId(String text ) { this.tmplTextId = text; }
  
  public void initForm() {
    setMethod("POST");
    removeChildren();
    tmplDescription = new TextBox("tmplDescription");
    tmplDescription.setCols("80");
    tmplDescription.setRows("4");
    tmplDescription.setMaxlength("255");
    
    tmplBody = new RichTextBox("tmplBody");
    
    addChild(tmplDescription);
    addChild(tmplBody);
    
    submit = new Button("submit","Submit");
    cancel = new Button("cancel","Cancel");
    reset = new Button("reset","Reset");
    addChild(submit);
    addChild(cancel);
    addChild(reset);
  }

  public void populateFields() {
    QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
    QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();
    try {
        z = MODULE.getQtnTemplateText(tmplTextId);
        if (z != null && z.getTemplateId() != null) {
          tmplDescription.setValue(z.getTemplateDescription());
          tmplBody.setValue(z.getTemplateBody());
        }
    } catch (Exception e) {
        Log.getLog(getClass()).error(e.toString());
    }
  }
  
  public void onRequest(Event evt) {
    initForm();
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
        return new Forward(Form.CANCEL_FORM_ACTION, "addTemplate.jsp", true);
        if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editTemplate.jsp?id="+templateId, true);}
        else {return new Forward(Form.CANCEL_FORM_ACTION, "addTemplate.jsp", true);}
    }
    else {return result;}
  }
 
  public Forward onValidate(Event evt) {
    QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);    
    QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();
    z.setTemplateDescription(tmplDescription.getValue().toString());
    z.setTemplateBody(tmplBody.getValue().toString());
    
    try {
      MODULE.insertQtnTemplateText(z);
    } catch (Exception e) {
      Log.getLog(getClass()).error(e.toString());
      return new Forward(TEMPLATE_TEXT_FAIL);
    }
    return new Forward(TEMPLATE_TEXT_ADD);
  }
  
  public String getDefaultTemplate() {
    return "quotation/addQtnTemplateText";
  }
}
*/