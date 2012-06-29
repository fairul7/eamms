package com.tms.sam.po.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.collab.directory.model.Contact;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;

public class AddSupplierToItemForm extends Form{
	private ItemPopupSelectBox item;
	private SingleBizSelectBox txtSupplier;
	private Label txtCompany;
	private Label txtTelephone;
	private TextBox txtQuotation;
	private DatePopupField txtDeliveryDate;
	protected Button btnSubmit;
	protected Button btnCancel;
	protected Panel btnPanel;
	private String ppID = "";
	private Contact contact;
    public static final String FORWARD_CANCEL = "backToMain";	
    
    public void init(){
    	setMethod("POST");
		setColumns(2);
		Application app = Application.getInstance();
			 
		item = new ItemPopupSelectBox("item"); 
		txtSupplier = new SingleBizSelectBox("txtSupplier"); 
		 
		txtCompany = new Label("txtCompany", "");
			 
		txtTelephone = new Label("txtTelephone", "");
		
		txtDeliveryDate = new DatePopupField("txtDeliveryDate");
			 
		txtQuotation = new TextBox("txtQuotation");
		txtQuotation.setRows("4");
		txtQuotation.setCols("80");
		txtQuotation.addChild(new ValidatorNotEmpty("txtQuotation"));	
		
		btnPanel = new Panel("btnPanel");
	    btnPanel.setColumns(2);
	        
	    btnSubmit = new Button("submit", app.getMessage("po.label.submit"));
	    btnPanel.addChild(btnSubmit);
	        
	    btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("po.label.cancel"));
	    btnPanel.addChild(btnCancel);
       
        addChild(txtSupplier); 
        addChild(item);        
        addChild(txtCompany);        
        addChild(txtTelephone);
        addChild(txtQuotation);
        addChild(btnPanel);
        addChild(txtDeliveryDate);
        item.init();
        txtSupplier.init();
      
	}
    
    public void onRequest(Event event) {
    	ppID = event.getRequest().getSession().getAttribute("purchaseID").toString();
    	String temp = event.getRequest().getParameter("flag");
		String []supplier = txtSupplier.getIds();
		String []items = item.getIds();
		contact = (Contact)event.getRequest().getSession().getAttribute("biz_dir");
		if(temp != null && temp.equals("new1")){
			init();
			if(supplier.length!=0 && items.length!=0){
				if(contact!=null){
		        	item.setIds(items);
		        	txtSupplier.setIds(supplier);
		        	txtCompany.setText(contact.getCompany());
		        	txtTelephone.setText(contact.getPhone());
		        }
			}
		}else{
	        if(contact!=null){
	        	item.setIds(items);
	        	txtSupplier.setIds(supplier);
	        	txtCompany.setText(contact.getCompany());
	        	txtTelephone.setText(contact.getPhone());
	        }  
		}
		
    }
    
	public String getTemplate() {
		return "po/addSupplierToItem";
	}
	
	public Forward onValidate(Event event){
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
            // process upload
            addSuppliertoItem(event);    	
            return new Forward(FORWARD_CANCEL);
            
        }else if(button.endsWith("cancel")){
        	event.getRequest().getSession().removeAttribute("bir_dir");
        	return new Forward(FORWARD_CANCEL);
        }

		return new Forward(FORWARD_CANCEL);
	}
	
	public Forward onSubmit(Event evt) {
		if(item.getIds().length==0){
			item.setInvalid(true);
			this.setInvalid(true);
		}
		if(contact==null){
			txtSupplier.setInvalid(true);
			this.setInvalid(true);
		}
		
		return super.onSubmit(evt);
	}
	 
	public void addSuppliertoItem(Event evt){
		
		Collection itemId = new ArrayList();
		String [] itemID = item.getIds();
		
		for (int j=0; j<itemID.length; j++) {
			 itemId.add(itemID[j]);
		}
		SupplierObject obj = new SupplierObject();
		String []id = txtSupplier.getIds();
		
		for(int j=0;j<id.length;j++){
			obj.setId(id[j]);
		}
		
		String supplierID = UuidGenerator.getInstance().getUuid();
		obj.setItemID(itemId);
		obj.setSupplierID(supplierID);
		obj.setLastKnownSuppName(contact.getFirstName());
		obj.setLastKnownCompany(txtCompany.getText());
		obj.setLastKnownTelephone(txtTelephone.getText());
		obj.setPpID(ppID);
		obj.setQuotationDetails(txtQuotation.getValue().toString());
		obj.setQuotationID(UuidGenerator.getInstance().getUuid());
		obj.setDateDelivery(txtDeliveryDate.getDate());
		obj.setApproved("No");
		obj.setLastUpdateBy(Application.getInstance().getCurrentUser().getId());
		obj.setRecommended("No");
		obj.setResponded("No");
		SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
		module.addSupplier(obj);
		
		HttpSession session;
	    session = evt.getRequest().getSession();
	    session.removeAttribute("biz_dir");
	}

	
	public ItemPopupSelectBox getItem() {
		return item;
	}

	public void setItem(ItemPopupSelectBox item) {
		this.item = item;
	}
	
	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public Label getTxtCompany() {
		return txtCompany;
	}

	public void setTxtCompany(Label txtCompany) {
		this.txtCompany = txtCompany;
	}

	public TextBox getTxtQuotation() {
		return txtQuotation;
	}

	public void setTxtQuotation(TextBox txtQuotation) {
		this.txtQuotation = txtQuotation;
	}

	public Label getTxtTelephone() {
		return txtTelephone;
	}

	public void setTxtTelephone(Label txtTelephone) {
		this.txtTelephone = txtTelephone;
	}

	public SingleBizSelectBox getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(SingleBizSelectBox txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	
	public DatePopupField getTxtDeliveryDate() {
		return txtDeliveryDate;
	}

	public void setTxtDeliveryDate(DatePopupField txtDeliveryDate) {
		this.txtDeliveryDate = txtDeliveryDate;
	}
}
