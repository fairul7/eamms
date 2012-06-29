package com.tms.sam.po.ui;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.validator.Validator;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.collab.directory.model.Contact;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;

public class AddSupplierForm extends Form{
	private SingleBizSelectBox txtSupplier;
	private Label txtCompany;
	private Label txtTelephone;
	private DatePopupField txtDeliveryDate;
	private TextBox txtQuotation;
	private Button submit;
	private Button cancel;
    private Panel buttonPanel;
    private String ppID = "";
    private int index = 0;
    private Contact contact;
    protected ValidatorUniqueCodeName validateUniqueCodeName;
    public static final String FORWARD_BACK = "back";	
	
	public void init(){
		 setMethod("POST");
		 setColumns(2);
		 Application app = Application.getInstance();
			 
		 txtSupplier = new SingleBizSelectBox("txtSupplier"); 
		 validateUniqueCodeName = new ValidatorUniqueCodeName("validateUniqueCodeName", Application.getInstance().getMessage("po.validator.countryNameNotUnique"));
		 txtSupplier.addChild(validateUniqueCodeName);
		 
		 txtCompany = new Label("txtCompany", "");
			 
		 txtTelephone = new Label("txtTelephone", "");
		 txtDeliveryDate = new DatePopupField("txtDeliveryDate");
		
		 txtQuotation = new TextBox("txtQuotation");
		 txtQuotation.setRows("4");
		 txtQuotation.setCols("80");
				 
         submit = new Button("submit", app.getMessage("training.button.submit", "Submit"));       
         cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("training.button.submit", "Cancel")); 
         
         buttonPanel = new Panel("buttonPanel");
         buttonPanel.addChild(submit);
         buttonPanel.addChild(cancel);
        
         addChild(txtSupplier);        
         addChild(txtCompany);        
         addChild(txtTelephone);
         addChild(txtDeliveryDate);
         addChild(txtQuotation);
         addChild(buttonPanel);
         txtSupplier.init();
       
	}
	
	public void onRequest(Event event) {
		String []supplier = txtSupplier.getIds();
		init();
		ppID = event.getRequest().getSession().getAttribute("purchaseID").toString();
		String temp = event.getRequest().getParameter("flag");
		contact = (Contact)event.getRequest().getSession().getAttribute("biz_dir");
		
		if(temp != null && temp.equals("new")){
			index = Integer.parseInt(event.getRequest().getParameter("index"));
			init();
			if(supplier.length!=0){
				 if(contact!=null){
		        		txtSupplier.setIds(supplier);
		        		txtCompany.setText(contact.getCompany());
		        		txtTelephone.setText(contact.getPhone());
		        }
			}
		}else{
		  	

	        if(contact!=null){
	        		txtSupplier.setIds(supplier);
	        		txtCompany.setText(contact.getCompany());
	        		txtTelephone.setText(contact.getPhone());
	        }
	        contact = null;
		}
		
        HttpSession session;
        session = event.getRequest().getSession();
    	session.removeAttribute("attachmentMap");
      	
    }
	
	public String getTemplate() {
		return "po/addSupplier";
	}
	
	public Forward onSubmit(Event event) {
		 Forward forward = super.onSubmit(event);
		
		 String button = findButtonClicked(event);
		 button = (button == null)? "" : button;
		 
		 if (button.endsWith("submit")) {
	          if(txtSupplier.getValue().toString().equals("[]")) {
		        	txtSupplier.setInvalid(true);
		            this.setInvalid(true);
		        }
		        
		        if("".equals(txtQuotation.getValue().toString())){
		        	txtQuotation.setInvalid(true);
		            this.setInvalid(true);
		        }
		 }
		
		 return forward;
	}
	      
	public Forward onValidate(Event event){
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit")) {
            // process upload
            addSupplier(ppID, event);    	
            event.getRequest().getSession().removeAttribute("purchaseID");
            return new Forward(FORWARD_BACK);
            
        }
		return new Forward(FORWARD_BACK);
	}

	public void addSupplier(String ppID, Event evt){
	  
	    contact = (Contact)evt.getRequest().getSession().getAttribute("biz_dir");
		SupplierObject obj = new SupplierObject();
		SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
		String supplierID = UuidGenerator.getInstance().getUuid();
		obj.setSupplierID(supplierID);
		obj.setLastKnownSuppName(contact.getFirstName());
		obj.setId(contact.getId());
		obj.setLastKnownCompany(txtCompany.getText());
		obj.setLastKnownTelephone(txtTelephone.getText());
		obj.setPpID(ppID);
		obj.setQuotationDetails(txtQuotation.getValue().toString());
		obj.setQuotationID(UuidGenerator.getInstance().getUuid());
		obj.setCounting(index);
		obj.setDateDelivery(txtDeliveryDate.getDate());
		Collection itemID = module.getItems(ppID, index);
		obj.setItemID(itemID);
	    obj.setApproved("No");
	    obj.setLastUpdateBy(Application.getInstance().getCurrentUser().getId());
	    obj.setRecommended("No");
	    obj.setResponded("No");
		
		module.addSupplier(obj);
		HttpSession session;
	    session = evt.getRequest().getSession();
	    session.removeAttribute("biz_dir");
		
	}
	
	// === [ getters/setters ] =================================================
	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
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
	
	public SingleBizSelectBox getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(SingleBizSelectBox txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public Label getTxtTelephone() {
		return txtTelephone;
	}

	public void setTxtTelephone(Label txtTelephone) {
		this.txtTelephone = txtTelephone;
	}

	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public DatePopupField getTxtDeliveryDate() {
		return txtDeliveryDate;
	}

	public void setTxtDeliveryDate(DatePopupField txtDeliveryDate) {
		this.txtDeliveryDate = txtDeliveryDate;
	}

	protected class ValidatorUniqueCodeName extends Validator {
		private String oriValue = null;
		
		public ValidatorUniqueCodeName(String name, String text) {
	        super(name);
	        setText(text);
	    }

		public String getOriValue() {
			return oriValue;
		}

		public void setOriValue(String oriValue) {
			this.oriValue = oriValue;
		}
		
		public boolean validate(FormField formField) {
			boolean validationPass = true;
			
			Object value = formField.getValue();
	        if(value != null)
	        {
	            String strValue = null;
	            if(value instanceof Collection)
	            {
	                if(((Collection) value).size() > 0)
	                	strValue = (String) ((Collection) value).iterator().next();
	            }
	            else if(value instanceof Map)
	            {
	                if(((Map) value).keySet().size() > 0)
	                	strValue = (String) ((Map) value).keySet().iterator().next();
	            }
	            else if(value instanceof String)
	            	strValue = (String) value;
	            
	            if(strValue != null && 
	            		!"".equals(strValue)) {
	            	if(! strValue.toLowerCase().equals(oriValue)) {
	            		SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
	            		if(module.isSupplierNameExist(ppID, txtCompany.getText(), txtTelephone.getText())) {
	            			validationPass = false;
	            		}
	            	}
	            }
	        }
	        
	        return validationPass;
	    }
	}
}
