package com.tms.quotation.generator;

import com.tms.crm.sales.model.Company;
import com.tms.crm.sales.model.CompanyModule;
import com.tms.crm.sales.model.Contact;
import com.tms.crm.sales.model.ContactModule;
import com.tms.quotation.model.CustomerDataObject;
import com.tms.quotation.model.QuotationModule;

import kacang.Application;
import kacang.stdui.CountrySelectBox;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class GenerateQtnCustomer extends LightWeightWidget {
//  private String quotationId;
  private String id;
  private CustomerDataObject customerInfo;
  private String formattedAddress;
  private String canEdit;
  
  public void onRequest(Event evt){
    QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
    
    if (null != id) {
//      customerInfo = MODULE.getCustomer(id);
//      String str = customerInfo.getAddress1();
//      str = str.replaceAll("\\r", "");
//      formattedAddress = str.replaceAll("\\n", "<br/>");
    	
    	Application application = Application.getInstance();
		ContactModule contactModule = (ContactModule) application.getModule(ContactModule.class);
		CompanyModule companyModule = (CompanyModule) application.getModule(CompanyModule.class);
		
		Contact con = contactModule.getContact(id);
		Company com = companyModule.getCompany(con.getCompanyID());
		
    	customerInfo = new CustomerDataObject();
    	customerInfo.setCompanyName(com.getCompanyName());
    	
    	if(con.getSalutationID() != null && !"".equals(con.getSalutationID())){
    		customerInfo.setSalutation(contactModule.getSalutationMap().get(con.getSalutationID()).toString());
    	}
    	
    	customerInfo.setContactFirstName(con.getContactFirstName());
    	customerInfo.setContactLastName(con.getContactLastName());
    	
    	if(com.getCompanyStreet1() != null && !"".equals(com.getCompanyStreet1())){
    		formattedAddress = com.getCompanyStreet1();
    	}
    	if(com.getCompanyStreet2() != null && !"".equals(com.getCompanyStreet2())){
    		formattedAddress = formattedAddress + "<br/>"+com.getCompanyStreet2();
    	}
    	
    }    
  }

  public String getFormattedAddress() {return formattedAddress;}
  public String getDefaultTemplate() {
    return "quotation/generator/previewCustomerInfo";
  }

  public String getId() {
    return id;
  }
/*  
  public String getQuotationId() {
    return quotationId;
  }
*/
  public CustomerDataObject getCustomerInfo() {
    return customerInfo;
  }

  public void setId( String customerId ) {
    this.id = customerId;
  }

  public String getCanEdit() {
    return canEdit;
  }

  public void setCanEdit(String canEdit) {
    this.canEdit = canEdit;
  }

/*  
  public void setQuotationId(String quotationId) {
    this.quotationId = quotationId;
  }
*/  
}
