package com.tms.sam.po.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;

import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;

public class PrintQuotationForm extends Panel {
	
	private Label txtSupplier;
	private Label txtSupplierCompany;	
	private Label company;
	private Label companyAdd;
	private Label companyTelephone;
	private Label companyFax;
	private Label companyEmail;
	private String txtQuotation = "";
	private String txtMinBudget = "";
	private Label purchaseItem;
	private Label[] item;
	private Label[] txtItem;
	private String chkCompany;
	private String chkAddr;
	private String chkPhone;
	private String chkEmail;
	private String chkFax;
	private int count = 0;
	public void init(){
	     setColumns(2);
		 txtSupplier = new Label("txtSupplier", ""); 		 	 
		 txtSupplierCompany = new Label("txtSupplierCompany", "");		 
		 company = new Label("company","" );
		 companyTelephone = new Label("companyTelephone",  "");	
		 companyFax = new Label("companyFax","");	
		 companyAdd = new Label("companyAdd", "");		
		 companyEmail = new Label("companyEmail", "");	
		 
		
		 purchaseItem = new Label("purchaseItem", "Item(s) Requested");
         addChild(txtSupplier);         
         addChild(txtSupplierCompany);         
         addChild(company);
         addChild(companyTelephone);  
         addChild(companyFax);  
         addChild(companyAdd);  
         addChild(companyEmail);  
              
	}
	
	public String getTemplate() {
			 return "po/printQuotation";
	}
	
	public void onRequest(Event event) {
		Application app = Application.getInstance();
		
		chkCompany = app.getProperty("sam.company.name");
		if(chkCompany != null && !"".equals(chkCompany)){
			company.setText(chkCompany);
		}else{
			company.setText("---");
		}
		
		chkAddr = app.getProperty("sam.company.address");
		if(chkAddr != null && !"".equals(chkAddr)){
			companyAdd.setText(chkAddr);
		}else{
			companyAdd.setText("---");
		}
		
		chkPhone = app.getProperty("sam.company.phone");
		if(chkPhone != null && !"".equals(chkPhone)){
			companyTelephone.setText(chkPhone);
		}else{
			companyTelephone.setText("---");
		}
		
		chkFax = app.getProperty("sam.company.fax");
		if(chkFax != null && !"".equals(chkFax)){
			companyFax.setText(chkFax);
		}else{
			companyFax.setText("---");
		}
		
		chkEmail =  app.getProperty("sam.company.email");
		if(chkEmail != null && !"".equals(chkEmail)){
			companyEmail.setText(chkEmail);
		}else{
			companyEmail.setText("---");
		}
		
		String supplierID = "";
		String ppID = "";
		int size;
		Collection items = null;
		supplierID = event.getRequest().getSession().getAttribute("supplierID").toString();
		SupplierObject oneRequest = null;
		PurchaseRequestObject purchaseRequest = null;
		SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
		oneRequest = module.singleRequest(supplierID, count,ppID);
		txtSupplier.setText(oneRequest.getLastKnownSuppName());
		txtSupplierCompany.setText(oneRequest.getLastKnownCompany());
	    ppID = oneRequest.getPpID();
		txtQuotation=oneRequest.getQuotationDetails();
		txtMinBudget= event.getRequest().getSession().getAttribute("minBudget").toString();
		
		
		  if(! "".equals(ppID)) {
			  PrePurchaseModule ppModule = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
			  purchaseRequest = ppModule.singleRequest(ppID);
			  
			  items = purchaseRequest.getPurchaseItemsObject();
			  size = purchaseRequest.getPurchaseItemsObject().size();
			  
			  item = new Label[size];
			  txtItem = new Label[size];
						  
		      for(int count=0; count<size; count++){
		    	 item[count]= new Label("item"+ count, app.getMessage("po.label.item", "Item"));		
		    	 txtItem[count]= new Label("txtItem" + count,"");
		    	 
		      }		
		      
			  int index =0;
			  for(Iterator itr= items.iterator(); index<size && itr.hasNext(); ) {
				  PurchaseItemObject obj = (PurchaseItemObject)itr.next();
				  txtItem[index].setText(obj.getItemCode());
				    addChild(item[index]);
				   addChild(txtItem[index]);
				 
				  index++;
			  }
			  addChild(purchaseItem);
		  }	  
	}

	// === [ getters/setters ] =================================================
	public Label getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(Label txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public Label getCompany() {
		return company;
	}

	public void setCompany(Label company) {
		this.company = company;
	}

	public Label getCompanyAdd() {
		return companyAdd;
	}

	public void setCompanyAdd(Label companyAdd) {
		this.companyAdd = companyAdd;
	}

	public Label getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(Label companyEmail) {
		this.companyEmail = companyEmail;
	}

	public Label getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(Label companyFax) {
		this.companyFax = companyFax;
	}

	public Label getCompanyTelephone() {
		return companyTelephone;
	}

	public void setCompanyTelephone(Label companyTelephone) {
		this.companyTelephone = companyTelephone;
	}

	public Label[] getItem() {
		return item;
	}

	public void setItem(Label[] item) {
		this.item = item;
	}

	public Label[] getTxtItem() {
		return txtItem;
	}

	public void setTxtItem(Label[] txtItem) {
		this.txtItem = txtItem;
	}

	public Label getTxtSupplierCompany() {
		return txtSupplierCompany;
	}

	public void setTxtSupplierCompany(Label txtSupplierCompany) {
		this.txtSupplierCompany = txtSupplierCompany;
	}

	public String getChkAddr() {
		return chkAddr;
	}

	public void setChkAddr(String chkAddr) {
		this.chkAddr = chkAddr;
	}

	public String getChkCompany() {
		return chkCompany;
	}

	public void setChkCompany(String chkCompany) {
		this.chkCompany = chkCompany;
	}

	public String getChkEmail() {
		return chkEmail;
	}

	public void setChkEmail(String chkEmail) {
		this.chkEmail = chkEmail;
	}

	public String getChkFax() {
		return chkFax;
	}

	public void setChkFax(String chkFax) {
		this.chkFax = chkFax;
	}

	public String getChkPhone() {
		return chkPhone;
	}

	public void setChkPhone(String chkPhone) {
		this.chkPhone = chkPhone;
	}

	public Label getPurchaseItem() {
		return purchaseItem;
	}

	public void setPurchaseItem(Label purchaseItem) {
		this.purchaseItem = purchaseItem;
	}

	public String getTxtMinBudget() {
		return txtMinBudget;
	}

	public void setTxtMinBudget(String txtMinBudget) {
		this.txtMinBudget = txtMinBudget;
	}

	public String getTxtQuotation() {
		return txtQuotation;
	}

	public void setTxtQuotation(String txtQuotation) {
		this.txtQuotation = txtQuotation;
	}
	
}
