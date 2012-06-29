package com.tms.quotation.ui;

import com.tms.quotation.model.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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


public class AddCustomer extends Form {

  public static final String CUSTOMER_ADD_SUCCESS="AddCustomer.success";
  public static final String CUSTOMER_ADD_FAIL="AddCustomer.fail";
  public static final String CUSTOMER_ADD_EXISTS="AddCustomer.exists";

  private Button cancel;
  private Button reset;
  private Button submit;
  private Label errorMsg;
  private Label lbCustomer;
  private String customerId;
  private String type;
  //private SelectBox [name];
  //private TextBox [name];
  //private TextField [name];
  private TextField CustomercontactFirstName;
  private TextField CustomercontactLastName;
  private TextField CustomercompanyName;
  private TextBox Customeraddress1;
//private TextField Customeraddress2;
//private TextField Customeraddress3;
//private TextField Customerpostcode;
//private TextField Customerstate;
//private TextField Customercountry;
  private SelectBox Customergender;
  private TextField Customersalutation;
  private CheckBox Customeractive;

  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";
  private String whoModified = "whoModified";
  private String cancelUrl = "viewCustomer.jsp";
  private String resetUrl = "addCustomer.jsp";
  
  public AddCustomer() {}

  public AddCustomer(String s) {super(s);}

  public void init() {
    if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {type = "Add";}
  }

  public void onRequest(Event event) {
    initForm();
    if ("Edit".equals(type)) {populateFields();}
  }

  public void initForm() {
    setMethod("post");
    Application app = Application.getInstance();
    String msgNotEmpty  = app.getMessage("com.tms.quotation.required");
    String initialSelect = "-1=--- None ---";

    removeChildren();      
    errorMsg = new Label("errorMsg");
    addChild(errorMsg);

    //[name] = new TextBox("[name]");
    //[name].setCols("25");
    //[name].setRows("4");
    //[name].addChild(new ValidatorNotEmpty("[name]NotEmpty", msgNotEmpty));
    //addChild("[name]");

    CustomercontactFirstName = new TextField("CustomercontactFirstName");
    CustomercontactFirstName.setSize("50");
    CustomercontactFirstName.setMaxlength("255");
    CustomercontactFirstName.addChild(new ValidatorNotEmpty("CustomercontactFirstNameNotEmpty", msgNotEmpty));
    addChild(CustomercontactFirstName);

    CustomercontactLastName = new TextField("CustomercontactLastName");
    CustomercontactLastName.setSize("50");
    CustomercontactLastName.setMaxlength("255");
    CustomercontactLastName.addChild(new ValidatorNotEmpty("CustomercontactLastNameNotEmpty", msgNotEmpty));
    addChild(CustomercontactLastName);

    CustomercompanyName = new TextField("CustomercompanyName");
    CustomercompanyName.setSize("50");
    CustomercompanyName.setMaxlength("255");
    CustomercompanyName.addChild(new ValidatorNotEmpty("CustomercompanyNameNotEmpty", msgNotEmpty));
    addChild(CustomercompanyName);

    Customeraddress1 = new TextBox("Customeraddress1");
    Customeraddress1.setSize("50");
    Customeraddress1.setRows("6");
    Customeraddress1.setMaxlength("255");
    Customeraddress1.addChild(new ValidatorNotEmpty("Customeraddress1NotEmpty", msgNotEmpty));
    addChild(Customeraddress1);
    /*
	  Customeraddress2 = new TextField("Customeraddress2");
	  Customeraddress2.setSize("50");
	  Customeraddress2.setMaxlength("255");
	  //Customeraddress2.addChild(new ValidatorNotEmpty("Customeraddress2NotEmpty", msgNotEmpty));
	  addChild(Customeraddress2);

	  Customeraddress3 = new TextField("Customeraddress3");
	  Customeraddress3.setSize("50");
	  Customeraddress3.setMaxlength("255");
	  //Customeraddress3.addChild(new ValidatorNotEmpty("Customeraddress3NotEmpty", msgNotEmpty));
	  addChild(Customeraddress3);

	  Customerpostcode = new TextField("Customerpostcode");
	  Customerpostcode.setSize("50");
	  Customerpostcode.setMaxlength("255");
	  //Customerpostcode.addChild(new ValidatorNotEmpty("CustomerpostcodeNotEmpty", msgNotEmpty));
	  addChild(Customerpostcode);

	  Customerstate = new TextField("Customerstate");
	  Customerstate.setSize("50");
	  Customerstate.setMaxlength("255");
	  //Customerstate.addChild(new ValidatorNotEmpty("CustomerstateNotEmpty", msgNotEmpty));
	  addChild(Customerstate);

	  Customercountry = new TextField("Customercountry");
	  Customercountry.setSize("50");
	  Customercountry.setMaxlength("255");
	  //Customercountry.addChild(new ValidatorNotEmpty("CustomercountryNotEmpty", msgNotEmpty));
	  addChild(Customercountry);
     */
    Customergender = new SelectBox("Customergender");
    Map sbMap = new HashMap();
    sbMap.put("Male", "Male");
    sbMap.put("Female", "Female");
    Customergender.setOptionMap(sbMap);
    Customergender.setMultiple(false);
//  Customergender.setSize("50");
//  Customergender.setMaxlength("255");
//  Customergender.addChild(new ValidatorNotEmpty("CustomergenderNotEmpty", msgNotEmpty));
    addChild(Customergender);

    Customersalutation = new TextField("Customersalutation");
    Customersalutation.setSize("8");
    Customersalutation.setMaxlength("255");
    Customersalutation.addChild(new ValidatorNotEmpty("CustomersalutationNotEmpty", msgNotEmpty));
    addChild(Customersalutation);

    Customeractive = new CheckBox("Customeractive");
    Customeractive.setText(app.getMessage("com.tms.quotation.active"));
    Customeractive.setChecked(true);
    addChild(Customeractive);
    lbCustomer = new Label("lbCustomer");
    addChild(lbCustomer);

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
    CustomerDataObject z = new CustomerDataObject();
    try {
      z = MODULE.getCustomer(customerId);
      if (z != null && z.getCustomerId() != null) {
	CustomercontactFirstName.setValue(z.getContactFirstName());
	CustomercontactLastName.setValue(z.getContactLastName());
	CustomercompanyName.setValue(z.getCompanyName());
	Customeraddress1.setValue(z.getAddress1());
//	Customeraddress2.setValue(z.getAddress2());
//	Customeraddress3.setValue(z.getAddress3());
//	Customerpostcode.setValue(z.getPostcode());
//	Customerstate.setValue(z.getState());
//	Customercountry.setValue(z.getCountry());
	Customergender.setSelectedOption(z.getGender());
	Customersalutation.setValue(z.getSalutation());
	lbCustomer.setText(z.getCustomerId());
	if( "1".equals(z.getActive())) { Customeractive.setChecked(true); }
	else{Customeractive.setChecked(false);}
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
      return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
    }
    else if (buttonName != null && reset.getAbsoluteName().equals(buttonName)) {
      init();
      if ("Edit".equals(type)) {return new Forward(Form.CANCEL_FORM_ACTION, "editCustomer.jsp?id="+customerId, true);}
      else {return new Forward(Form.CANCEL_FORM_ACTION, resetUrl, true);}
    }
    else {return result;}
  }

  //template directory = [root]\WEB-INF\templates\default
  public String getDefaultTemplate() {
    if ("Edit".equals(type)) {return "quotation/addCustomer";}
    else {return "quotation/addCustomer";}
  }

  public Forward onValidate(Event event) {
    if ( ("1".equals(canEdit) && "Edit".equals(type)) || ("1".equals(canAdd) && "Add".equals(type)) ) {
      CustomerDataObject z = new CustomerDataObject();
      QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);

      if ("Edit".equals(type)) {
	try {z = MODULE.getCustomer(customerId);}
	catch (Exception e) {Log.getLog(getClass()).error(e.toString());}
      }
      else {
	customerId = UuidGenerator.getInstance().getUuid();
	z.setCustomerId(customerId);
      }
      z.setContactFirstName(CustomercontactFirstName.getValue().toString());
      z.setContactLastName(CustomercontactLastName.getValue().toString());
      z.setCompanyName(CustomercompanyName.getValue().toString());
      z.setAddress1(Customeraddress1.getValue().toString());
//    z.setAddress2(Customeraddress2.getValue().toString());
//    z.setAddress3(Customeraddress3.getValue().toString());
//    z.setPostcode(Customerpostcode.getValue().toString());
//    z.setState(Customerstate.getValue().toString());
//    z.setCountry(Customercountry.getValue().toString());
      Collection sel = (Collection)Customergender.getValue();
      String strSel = sel.iterator().next().toString();
      z.setGender(strSel);
      z.setSalutation(Customersalutation.getValue().toString());
      if(Customeractive.isChecked())z.setActive("1");
      else z.setActive("0");

      if ("Add".equals(type)) {
	//try {MODULE.insertCustomer(z.getContactFirstName(), z.getContactLastName(), z.getCompanyName(), z.getAddress1(), z.getAddress2(), z.getAddress3(), z.getPostcode(), z.getState(), z.getCountry(), z.getGender(), z.getSalutation());}
	try {MODULE.insertCustomer(z);}
	catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(CUSTOMER_ADD_FAIL);} 
      }
      if ("Edit".equals(type)) {
	try {MODULE.updateCustomer(z);}
	catch (Exception e) {Log.getLog(getClass()).error(e.toString()); return new Forward(CUSTOMER_ADD_FAIL);} 
      }
    }
    populateFields();
    return new Forward(CUSTOMER_ADD_SUCCESS);
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String string) {
    customerId = string;
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
  public String getCancelUrl() { return cancelUrl; }
  public void setCancelUrl(String url) { cancelUrl=url; }
  public String getResetUrl() { return resetUrl; }
  public void setResetUrl(String url) {resetUrl=url;}
}