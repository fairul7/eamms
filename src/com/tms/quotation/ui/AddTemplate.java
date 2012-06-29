package com.tms.quotation.ui;

import com.tms.quotation.model.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

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


public class AddTemplate extends Form {

	public static final String TEMPLATE_ADD_SUCCESS="AddTemplate.success";
	public static final String TEMPLATE_ADD_FAIL="AddTemplate.fail";
	public static final String TEMPLATE_ADD_EXISTS="AddTemplate.exists";
	private Button cancel;
	private Button reset;
	private Button submit;
	private Label errorMsg;
	private Label lbTemplate;
	private String templateId;
	private String type;

    //private SelectBox [name];
	//private TextBox [name];
	//private TextField [name];
	
//    private RichTextBox TemplatetemplateHeader;
//    private RichTextBox TemplatetemplateFooter;
    
//    private Map unselected;
//    private Map selected;
    
    private ComboSelectBox TemplateTextSelectBox;
	private TextField TemplatetemplateName;
	private CheckBox Templateactive;

    private String canAdd = "0";
	private String canEdit = "0";
	private String canDelete = "0";
	private String whoModified = "whoModified";

	public AddTemplate() {}

	public AddTemplate(String s) {super(s);}

	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}
	}

	public void onRequest(Event event) {
		initForm();
		if ("Edit".equals(type)) {populateFields();}
        else initTextSelectBox();
        
	}

    private void initTextSelectBox() {
      QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
      Collection c = MODULE.selectQtnTemplateText();
      SequencedHashMap map = new SequencedHashMap();
      if ( c != null && c.size() > 0) {
        QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();          
        for( Iterator i = c.iterator(); i.hasNext(); ){
          z = (QtnTemplateTextDataObject)i.next();
          map.put(z.getTemplateId(), z.getTemplateDescription());
        }
        TemplateTextSelectBox.setLeftValues(map);
      }
      TemplateTextSelectBox.setRightValues(new SequencedHashMap());
    }
    
	public void initForm() {
	  removeChildren();      
	  Application app = Application.getInstance();
	  String msgNotEmpty  = app.getMessage("com.tms.quotation.required");
//	  String initialSelect = "-1=--- None ---";

	  setMethod("post");

//	  selected = new SequencedHashMap();
//	  unselected = new SequencedHashMap();

	  errorMsg = new Label("errorMsg");
	  addChild(errorMsg);

	  //[name] = new TextBox("[name]");
	  //[name].setCols("25");
	  //[name].setRows("4");
	  //[name].addChild(new ValidatorNotEmpty("[name]NotEmpty", msgNotEmpty));
	  //addChild("[name]");

	  TemplatetemplateName = new TextField("TemplatetemplateName");
	  TemplatetemplateName.setSize("50");
	  TemplatetemplateName.setMaxlength("255");
	  TemplatetemplateName.addChild(new ValidatorNotEmpty("TemplatetemplateNameNotEmpty", msgNotEmpty));
	  addChild(TemplatetemplateName);

	  TemplateTextSelectBox = new ComboSelectBox("TemplateTextSelectBox");
	  addChild(TemplateTextSelectBox);
//	  TemplateTextSelectBox.setTemplate("formwizard/formSortableComboSelectBox");
	  TemplateTextSelectBox.init();

	  /*
		TemplatetemplateHeader = new RichTextBox("TemplatetemplateHeader");
//		TemplatetemplateHeader.setSize("50");
//		TemplatetemplateHeader.setMaxlength("255");
		//TemplatetemplateHeader.addChild(new ValidatorNotEmpty("TemplatetemplateHeaderNotEmpty", msgNotEmpty));
		addChild(TemplatetemplateHeader);

		TemplatetemplateFooter = new RichTextBox("TemplatetemplateFooter");
//		TemplatetemplateFooter.setSize("50");
//		TemplatetemplateFooter.setMaxlength("255");
		//TemplatetemplateFooter.addChild(new ValidatorNotEmpty("TemplatetemplateFooterNotEmpty", msgNotEmpty));
		addChild(TemplatetemplateFooter);
	   */

	  Templateactive = new CheckBox("Templateactive");
	  Templateactive.setChecked(true);
	  //Templateactive.addChild(new ValidatorNotEmpty("TemplateactiveNotEmpty", msgNotEmpty));
	  addChild(Templateactive);

	  lbTemplate = new Label("lbTemplate");
	  addChild(lbTemplate);

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
	  TemplateDataObject z = new TemplateDataObject();
	  try {
	    z = MODULE.getTemplate(templateId);
	    if (z != null && z.getTemplateId() != null) {
	      TemplatetemplateName.setValue(z.getTemplateName());
	      Map selected = MODULE.csbGetSelectedText(templateId, true);
	      Map unselected = MODULE.csbGetSelectedText(templateId, false);
	      if ( null != selected ) TemplateTextSelectBox.setRightValues(selected);
	      if ( null != unselected) TemplateTextSelectBox.setLeftValues(unselected);
	      /*                
				TemplatetemplateHeader.setValue(z.getTemplateHeader());
				TemplatetemplateFooter.setValue(z.getTemplateFooter());
	       */                
	      if("1".equals(z.getActive()))Templateactive.setChecked(true);
	      else Templateactive.setChecked(false);
//	      lbTemplate.setText(z.getTemplateId());
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
	    return new Forward(Form.CANCEL_FORM_ACTION, "viewTemplate.jsp", true);
	  }
	  else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
	    init();
	    if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editTemplate.jsp?id="+templateId, true);}
	    else {return new Forward(Form.CANCEL_FORM_ACTION, "addTemplate.jsp", true);}
	  }
	  else {return result;}
	}

	//template directory = [root]\WEB-INF\templates\default
	public String getDefaultTemplate() {
		if ("Edit".equals(type)) {return "quotation/addTemplate";}
		else {return "quotation/addTemplate";}
	}

	public Forward onValidate(Event event) {

	  if ( ("1".equals(canEdit) && "Edit".equals(type)) || ("1".equals(canAdd) && "Add".equals(type)) ) {
	    TemplateDataObject z = new TemplateDataObject();
	    QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);

	    if ("Edit".equals(type)) {
	      try {z = MODULE.getTemplate(templateId);}
	      catch (Exception e) {Log.getLog(getClass()).error(e.toString());}
	      /* clear all items with this templateId */                
	      MODULE.deleteTemplateMap(templateId);                
	    }
	    else {
	      templateId=UuidGenerator.getInstance().getUuid();              
	      z.setTemplateId(templateId);
	    }
	    z.setTemplateName(TemplatetemplateName.getValue().toString());

	    /* add from right select box */
	    SequencedHashMap newRight = (SequencedHashMap)TemplateTextSelectBox.getRightValues();
	    /*
            for(Iterator i=newRight.iterator(); i.hasNext();) {
              System.out.println( i.next() );
            }
	     */
/*        
	    Map fixedItems = new SequencedHashMap();

	    fixedItems.put("-1", "DB - Quotation subject");
	    fixedItems.put("-2", "DB - Customer information");
	    fixedItems.put("-3", "DB - Quotation item table");
	    fixedItems.put("-4", "DB - Date");
	    
	    newRight.putAll(fixedItems);
*/	    
	    for (int c=0; c<newRight.size(); c++) {
	      TemplateMapDataObject tm = new TemplateMapDataObject();
	      tm.setTemplateId(templateId);              
	      tm.setTemplateTextId((String) newRight.get(c));
	      tm.setSortNum(c + 1);
	      MODULE.insertTemplateMap(tm);
	    }

	    /*            
			z.setTemplateHeader(TemplatetemplateHeader.getValue().toString());
			z.setTemplateFooter(TemplatetemplateFooter.getValue().toString());
	     */            
	    z.setWhoModified(whoModified);
	    z.setActive(Templateactive.isChecked() ? "1" : "0" );
	    if ("Add".equals(type)) {
	      //try {MODULE.insertTemplate(z.getTemplateHeader(), z.getTemplateFooter(), z.getRecdate(), z.getWhoModified(), z.getActive());}
	      try {MODULE.insertTemplate(z);}
	      catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(TEMPLATE_ADD_FAIL);} 
	    }
	    if ("Edit".equals(type)) {
	      try {MODULE.updateTemplate(z);}
	      catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(TEMPLATE_ADD_FAIL);} 
	    }
	  }
	  populateFields();
	  return new Forward(TEMPLATE_ADD_SUCCESS);
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