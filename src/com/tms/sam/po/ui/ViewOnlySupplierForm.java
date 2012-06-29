package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;

public class ViewOnlySupplierForm extends Form {
	private Label txtSupplier;
	private Label txtCompany;
	private Label txtTelephone;
	private Label attached;
	private Label txtDateSent;
	private Label txtQuotation;
	private String strQuotation = "";
	private String strMinBudget = "";
	private Label txtDateReceived;
	private Collection txtAttachment;
	private Map attachmentMap;
    private Panel buttonPanel;
	private Button cancel;
    private Button evaluate;
	private String supplierID = "";
	private int count = 0;
	private String ppID = "";
	public static final String FORWARD_BACK = "back";	
    public static final String FORWARD_EVALUATE = "evaluate";
    
    public void init(){
		 setMethod("POST");
		 setColumns(2);
		 Application app = Application.getInstance();
		 
		 txtSupplier = new Label("txtSupplier", "");
		 txtCompany = new Label("txtCompany", "");
		 txtTelephone = new Label("txtTelephone", "");
		 txtDateSent = new Label("txtDateSent", "");
		 txtDateReceived = new Label("txtDateReceived", "");
		
		 
		 txtQuotation = new Label("txtQuotation", "");
		 attached = new Label("attached", "Attached");
        
      
        cancel = new Button(Form.CANCEL_FORM_ACTION, "cancel");
        cancel.setText(app.getMessage("po.label.cancel", "Cancel"));
        cancel.setOnClick("return back()");
        

        evaluate = new Button("evaluate", app.getMessage("po.label.viewEvaluate", " View Evaluate"));   
        
        buttonPanel = new Panel("buttonPanel");
    
        buttonPanel.addChild(evaluate);
        buttonPanel.addChild(cancel);
        
        
        addChild(txtSupplier);
        addChild(txtCompany);
        addChild(txtTelephone);         
        addChild(txtDateSent);         
        addChild(txtDateReceived);         
        addChild(txtQuotation);         
        addChild(buttonPanel);
        addChild(attached);
          
	}
    
    public String getTemplate() {
		 return "po/viewOnlySupplier";
	}
	
	public void onRequest(Event event){
		  init();
		  Application app = Application.getInstance();
		  SupplierObject oneRequest = null;
		  HttpSession session;
	      session = event.getRequest().getSession();
	      DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
		  
		  attachmentMap = new SequencedHashMap();
		  txtAttachment = null;
		  if(! "".equals(supplierID)) {
					 
			  SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
			  oneRequest = module.singleRequest(supplierID, count, ppID);
			  txtAttachment = module.getAttachment(supplierID,count,ppID);
			  if(txtAttachment.size()!=0){
				  for(Iterator i = txtAttachment.iterator();i.hasNext();){
					  Map map = (Map)i.next() ;
					  String attach  = map.get("newFileName").toString();
					  String attachID = map.get("attachmentID").toString();
					  attachmentMap.put(attach,attachID);
				  }
			  }else{
				  attached.setHidden(true);
			  }
			  
			  event.getRequest().getSession().setAttribute("supplierID", oneRequest.getSupplierID().toString());
			  if(oneRequest.getTotalQuotation()!=0){
				  strMinBudget = String.valueOf(oneRequest.getTotalQuotation());
			  }else{
				  strMinBudget = "---";
			  }
			  
			  if(oneRequest.getDateReceived()!=null){
				     txtDateReceived.setText(dmyDateFmt.format(oneRequest.getDateReceived()));
			  }
			  
			  txtSupplier.setText(oneRequest.getLastKnownSuppName());
			  txtCompany.setText(oneRequest.getLastKnownCompany());
			  txtTelephone.setText(oneRequest.getLastKnownTelephone());
			  
			  if(oneRequest.getDateSent() == null){
				  txtDateSent.setText("---");
			  }else{
				  txtDateSent.setText(dmyDateFmt.format(oneRequest.getDateSent()));
			  }

			  
			  txtQuotation.setText(oneRequest.getQuotationDetails());
			  strQuotation = oneRequest.getQuotationDetails();
		  }	  
		  
		  session.removeAttribute("attachmentMap");
	}
	
	// === [ getters/setters ] =================================================
	public Forward onValidate(Event event){
	
		return new Forward(FORWARD_EVALUATE);
	}

	public Label getAttached() {
		return attached;
	}

	public void setAttached(Label attached) {
		this.attached = attached;
	}

	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Button evaluate) {
		this.evaluate = evaluate;
	}

	public String getStrMinBudget() {
		return strMinBudget;
	}

	public void setStrMinBudget(String strMinBudget) {
		this.strMinBudget = strMinBudget;
	}

	public String getStrQuotation() {
		return strQuotation;
	}

	public void setStrQuotation(String strQuotation) {
		this.strQuotation = strQuotation;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public Collection getTxtAttachment() {
		return txtAttachment;
	}

	public void setTxtAttachment(Collection txtAttachment) {
		this.txtAttachment = txtAttachment;
	}

	public Label getTxtCompany() {
		return txtCompany;
	}

	public void setTxtCompany(Label txtCompany) {
		this.txtCompany = txtCompany;
	}

	public Label getTxtDateReceived() {
		return txtDateReceived;
	}

	public void setTxtDateReceived(Label txtDateReceived) {
		this.txtDateReceived = txtDateReceived;
	}

	public Label getTxtDateSent() {
		return txtDateSent;
	}

	public void setTxtDateSent(Label txtDateSent) {
		this.txtDateSent = txtDateSent;
	}

	public Label getTxtQuotation() {
		return txtQuotation;
	}

	public void setTxtQuotation(Label txtQuotation) {
		this.txtQuotation = txtQuotation;
	}

	public Label getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(Label txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public Label getTxtTelephone() {
		return txtTelephone;
	}

	public void setTxtTelephone(Label txtTelephone) {
		this.txtTelephone = txtTelephone;
	}
}
