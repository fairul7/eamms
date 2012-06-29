package com.tms.quotation.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.crm.helpdesk.Incident;
import com.tms.quotation.model.QtnContentDataObject;
import com.tms.quotation.model.QtnTemplateTextDataObject;
import com.tms.quotation.model.QuotationDataObject;
import com.tms.quotation.model.QuotationModule;


public class AddQuotation extends Form {

	public static final String QUOTATION_ADD_SUCCESS="AddQuotation.success";
	public static final String QUOTATION_ADD_FAIL="AddQuotation.fail";
	public static final String QUOTATION_ADD_EXISTS="AddQuotation.exists";
	private Button cancel;
	private Button reset;
	private Button submit;
    private Button addItems;
    
	private Label errorMsg;
	private Label lbQuotation;
	private String quotationId;
    private String customerId;
	private String type;
    private String whoModified;
    
    private String companyId;
    
    private Label lbQuotationcustomer;
    private Label lblQuotationCompany;
    private SelectBox QuotationQtnTableId;
    private SelectBox QuotationtemplateId;
	private TextField Quotationsubject;

	private String canAdd = "0";
	private String canEdit = "0";
	private String canDelete = "0";

	public AddQuotation() {}

	public AddQuotation(String s) {super(s);}

	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}
	}

	public void onRequest(Event event) {
		initForm();
		if ("Edit".equals(type)) {populateFields();}
	}

    class ValidatorSelectBox extends Validator {
      ValidatorSelectBox() { super(); }
      ValidatorSelectBox(String name) { super(name); }
      ValidatorSelectBox(String name, String text) { super(name); setText(text); }
      
      public boolean validate(FormField formField) {
        SelectBox sb = (SelectBox)formField;
        Collection c = (Collection)sb.getValue();
        String sel = (String)c.iterator().next();
        if( !"-1".equals(sel)) return true;
        return false;
      }
    }
    
	public void initForm() {
      removeChildren();      
	  String [] arrTemp;
	  setMethod("post");
	  Application app = Application.getInstance();
	  QuotationModule MODULE = (QuotationModule) app.getModule(QuotationModule.class);
	  String msgNotEmpty  = app.getMessage("com.tms.quotation.required");

	  String initialTemplate = "-1=-- Please Select a Template --";
      String initialTable  = "-1=-- Please Select a Table Type --";

	  errorMsg = new Label("errorMsg");
	  addChild(errorMsg);

	  //[name] = new TextBox("[name]");
	  //[name].setCols("25");
	  //[name].setRows("4");
	  //[name].addChild(new ValidatorNotEmpty("[name]NotEmpty", msgNotEmpty));
	  //addChild("[name]");
	  
	  Incident incident = new Incident();
	  incident.populateContactId(customerId);
	  incident.populateCompanyId(companyId);
	  
	  lbQuotationcustomer = new Label("lbQuotationcustomer");
	  lbQuotationcustomer.setText(incident.getContactFirstName() + " " + incident.getContactLastName());        
//	  lbQuotationcustomer.setText("Customer name");
	  addChild(lbQuotationcustomer);
	  
	  lblQuotationCompany = new Label("lblQuotationCompany");
	  lblQuotationCompany.setText(incident.getCompanyName());
	  addChild(lblQuotationCompany);
	  
	  QuotationQtnTableId = new SelectBox("QuotationQtnTableId");
      QuotationQtnTableId.setOptions(initialTable);
      Map optionMap = QuotationQtnTableId.getOptionMap(); 
      optionMap.putAll(MODULE.selectBoxQtnTable());
      QuotationQtnTableId.setOptionMap(optionMap);
      QuotationQtnTableId.addChild( new ValidatorSelectBox("TableIdNotEmpty", msgNotEmpty));
	  addChild(QuotationQtnTableId);

	  QuotationtemplateId = new SelectBox("QuotationtemplateId");
      QuotationtemplateId.setOptions(initialTemplate);
      optionMap = QuotationtemplateId.getOptionMap();
	  optionMap.putAll(MODULE.selectBoxTemplateId());
      QuotationtemplateId.addChild(new ValidatorSelectBox("TemplateIdNotEmpty", msgNotEmpty));
	  addChild(QuotationtemplateId);

	  Quotationsubject = new TextField("Quotationsubject");
	  Quotationsubject.setSize("50");
	  Quotationsubject.setMaxlength("255");
	  Quotationsubject.addChild(new ValidatorNotEmpty("QuotationsubjectNotEmpty", msgNotEmpty));
	  addChild(Quotationsubject);

	  lbQuotation = new Label("lbQuotation");
	  addChild(lbQuotation);

      submit = new Button("submit", app.getMessage("com.tms.quotation.button.save"));
      reset = new Button("reset", app.getMessage("com.tms.quotation.button.reset"));
      cancel = new Button("cancel", app.getMessage("com.tms.quotation.button.cancel"));
      addItems = new Button("addItems", app.getMessage("com.tms.quotation.button.addItems")); 
	  if (!"1".equals(canAdd) && "Add".equals(type)) {submit.setHidden(true);}
	  if (!"1".equals(canEdit) && "Edit".equals(type)) {submit.setHidden(true);}
      if( !"Edit".equals(type)) {addItems.setHidden(true);}
	  addChild(submit);
	  addChild(reset);
	  addChild(cancel);
      addChild(addItems);
	}

	public void populateFields() {
		QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
		QuotationDataObject z = new QuotationDataObject();
		try {
			z = MODULE.getQuotation(quotationId);
			if (z != null && z.getQuotationId() != null) {
                
				QuotationtemplateId.setSelectedOption(z.getTemplateId());
                QuotationQtnTableId.setSelectedOption(z.getTableId());
				Quotationsubject.setValue(z.getSubject());
				lbQuotation.setText(z.getQuotationId());
				
				Incident incident = new Incident();
				incident.populateContactId(z.getCustomerId());
				incident.populateCompanyId(z.getCompanyId());
				lbQuotationcustomer.setText(incident.getContactFirstName() + " " + incident.getContactLastName());
				lblQuotationCompany.setText(incident.getCompanyName());
				
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
			return new Forward(Form.CANCEL_FORM_ACTION, "viewQuotation.jsp", true);
		}
		else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
			init();
			if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editQuotation.jsp?id="+quotationId, true);}
			else {return new Forward(Form.CANCEL_FORM_ACTION, "addQuotation.jsp", true);}
		}
        else if (buttonName != null && addItems.getAbsoluteName().equals(buttonName)) {
          QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
          QuotationDataObject z = MODULE.getQuotation(quotationId); 
          String fwdUrl = "qtnItemForm.jsp?quotationId="+z.getQuotationId();//+"&tableId="+z.getTableId();
          return new Forward(Form.CANCEL_FORM_ACTION, fwdUrl, true );
        }
		else {return result;}
	}

	//template directory = [root]\WEB-INF\templates\default
	public String getDefaultTemplate() {
		return "quotation/addQuotation";
	}

	public Forward onValidate(Event event) {
      Collection cSelected = null;
      String strSelected = "";
      SecurityService srv = (SecurityService) Application.getInstance().getService(SecurityService.class);
      User user = srv.getCurrentUser(event.getRequest());
      
		if ( ("1".equals(canEdit) && "Edit".equals(type)) || ("1".equals(canAdd) && "Add".equals(type)) ) {
			QuotationDataObject z = new QuotationDataObject();
			QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);

			if ("Edit".equals(type)) {
				try { z = MODULE.getQuotation(quotationId); }
				catch (Exception e) {Log.getLog(getClass()).error(e.toString());}
                customerId=z.getCustomerId();
			}
            else {
              quotationId = UuidGenerator.getInstance().getUuid();
              z.setQuotationId(quotationId);
              z.setOpenDate(new Date());
              z.setRecdate(z.getOpenDate());
              z.setCustomerId(customerId);
              
              z.setCompanyId(companyId);
            }
            z.setWhoModified(user.getId());
            z.setSubject(Quotationsubject.getValue().toString());
            z.setStatus("New");
           
            cSelected = (Collection) QuotationQtnTableId.getValue();
            strSelected = cSelected.iterator().next().toString();
            if( !"-1".equals(strSelected)) {
              z.setTableId(strSelected);
            }
           
            cSelected = (Collection)QuotationtemplateId.getValue();
            strSelected = cSelected.iterator().next().toString();
            if( !"-1".equals(strSelected)) {
              z.setTemplateId(strSelected);
            }

            if( "Edit".equals(type) ) {
              MODULE.deleteQtnContent( quotationId );
            }
            
            ArrayList content = new ArrayList (MODULE.selectTemplateIdText(strSelected));
//          int c = 1;
//          for( Iterator i=content.iterator(); i.hasNext(); c++) {
            for( int c=0; c<content.size(); c++ ) {
              QtnTemplateTextDataObject tmpl = (QtnTemplateTextDataObject)content.get(c);
              QtnContentDataObject co = new QtnContentDataObject(quotationId);
              String contentId, contentType;
              if(QtnTemplateTextDataObject.DB_SUBJECT_ID.equals(tmpl.getTemplateId())) {
                contentType="com.tms.quotation.generator.Subject";
                contentId=QtnTemplateTextDataObject.DB_SUBJECT_ID;
              }
              else if( QtnTemplateTextDataObject.DB_CUSTOMER_ID.equals(tmpl.getTemplateId())) {
                contentType="com.tms.quotation.generator.GenerateQtnCustomer";
                contentId=customerId;
              }
              else if( QtnTemplateTextDataObject.DB_TABLE_ID.equals(tmpl.getTemplateId())) {
                contentType="com.tms.quotation.generator.GenerateQtnTable";
                contentId=quotationId;
              }
              else if( QtnTemplateTextDataObject.DB_DATE_ID.equals(tmpl.getTemplateId())) {
                contentType="com.tms.quotation.generator.Date";
                contentId=QtnTemplateTextDataObject.DB_DATE_ID;
              }
              else {
                contentType="com.tms.quotation.generator.GenerateQtnText";
                contentId=tmpl.getTemplateId();
              }
              co.setContentType(contentType);
              co.setContentId(contentId);
              co.setSortNum(c+1);
              MODULE.insertQtnContent(co);
            }
            
			if ("Add".equals(type)) {
				//try {MODULE.insertQuotation(z.getCustomerId(), z.getTemplateId(), z.getSubject(), z.getContent(), z.getStatus(), z.getOpenDate(), z.getCloseDate(), z.getRecdate());}
				try {MODULE.insertQuotation(z);}
				catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QUOTATION_ADD_FAIL);} 
			}
			if ("Edit".equals(type)) {
				try {MODULE.updateQuotation(z);}
				catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(QUOTATION_ADD_FAIL);} 
			}
		}
		populateFields();
		return new Forward(QUOTATION_ADD_SUCCESS);
	}

	public String getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(String string) {
		quotationId = string;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
  
	public String getTableId() {
		Collection cSelected;
		String strSelected;
		cSelected = (Collection) QuotationQtnTableId.getValue();
		strSelected = cSelected.iterator().next().toString();
		if( !"-1".equals(strSelected)) {
			return strSelected;
		}
		else return null;
	}
  
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getWhoModified() {
		return whoModified;
	}
  
	public void setWhoModified( String whoModified ) {
		this.whoModified = whoModified;
	}
}