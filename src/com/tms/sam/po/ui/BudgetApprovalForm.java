package com.tms.sam.po.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.sam.po.model.BudgetModule;
import com.tms.sam.po.model.BudgetObject;
import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;
import com.tms.sam.po.permission.model.PermissionModel;


public class BudgetApprovalForm extends Form {
	  private ButtonGroup radioGroup;
	  private TextBox txtRemark;
	  private Radio approve;
	  private Radio reject;
	  private Radio reQuote;
	  private Button submit;
	  private Button cancel;
	  private Panel buttonPanel;
	  protected String ppID = "";
	  protected String status = "No";
	  private Hidden approval;
	  public static final String FORWARD_BACK = "back";	
	  public static final String FORWARD_SUBMIT = "submit";
	
	  public void init(){

		  setMethod("POST");
		  setColumns(2);
		  Application app = Application.getInstance();
		  
		  txtRemark = new TextBox("txtRemark");
		  txtRemark.setRows("4");
		  txtRemark.setCols("80");
		  
		  approve = new Radio("approve");
		  approve.setText(app.getMessage("userRequest.label.approve", "Approve"));
		  approve.setChecked(true);
		  
		  reject = new Radio("reject");
		  reject.setText(app.getMessage("userRequest.label.reject", "Reject"));
		  
		  reQuote = new Radio("reQuote");
		  reQuote.setText(app.getMessage("userRequest.label.reQuote", "Re-Quote"));
		  
		  radioGroup = new ButtonGroup("radioGroup");
		  radioGroup.setType(ButtonGroup.RADIO_TYPE);
		  radioGroup.addButton(approve);
		  radioGroup.addButton(reject);
		  radioGroup.addButton(reQuote);
		  
		  submit = new Button("submit", app.getMessage("po.label.submit", "Submit"));       
	      cancel = new Button("cancel", app.getMessage("po.label.cancel", "Cancel")); 
		 
		  buttonPanel = new Panel("buttonPanel");
		  buttonPanel.addChild(submit);
		  buttonPanel.addChild(cancel);
		
		  addChild(txtRemark);
		  addChild(radioGroup);
		  addChild(buttonPanel);
			  		   		 	 
	  }
	  
	  public String getTemplate() {
		  return "po/budgetApprovalForm";
	  }
	 	  
	  public void onRequest(Event event){
		 Collection approved = null;
		 Application application = Application.getInstance();
		 SupplierModule module = (SupplierModule) application.getModule(SupplierModule.class);
		
		  if( !module.getApprovedSupplier(ppID)){
			  approve.setHidden(true);
		  }else{
			  approve.setHidden(false);
		  }
	  }
	  
	  public Forward onValidate(Event event){

		 String button = findButtonClicked(event);
			button = (button == null)? "" : button;
				
			if(button.endsWith("cancel")){
		       	return new Forward(FORWARD_BACK);
			        	
		    }else if(button.endsWith("submit")){
		    	
		    	requestResult(event);
				return new Forward(FORWARD_BACK);
					
	    	}
			 
		  return new Forward(FORWARD_BACK);
	  }
	  
	  public void requestResult(Event event){
		  
		  Collection supplier =null;
		  Application application = Application.getInstance();
		  SupplierModule module = (SupplierModule) application.getModule(SupplierModule.class);
		  BudgetModule bModule = (BudgetModule)application.getModule(BudgetModule.class);
		  PrePurchaseModule ppModule = (PrePurchaseModule)application.getModule(PrePurchaseModule.class);
		  supplier = module.getSupplierID(ppID, "Yes");
		  
		  String selected = (String)radioGroup.getValue();
		  String budgetID = UuidGenerator.getInstance().getUuid();
		
		  BudgetObject obj= null;
		  PurchaseRequestObject po = new PurchaseRequestObject();
		  SupplierObject so = new SupplierObject();
		 		 
			for(Iterator i=supplier.iterator();i.hasNext();){
				SupplierObject sObject = (SupplierObject)i.next();
				obj = new BudgetObject();
				obj.setBudgetID(UuidGenerator.getInstance().getUuid());
				obj.setPpID(ppID);
				obj.setApprovedBy(application.getCurrentUser().getName());
				obj.setSupplierID(sObject.getSupplierID());
				obj.setCount(sObject.getCounting());
				if(selected.endsWith("approve")){
					getPO("approve");
					obj.setAction("Approved");
					po.setStatus("Approved by BO");
					po.setRank(8);
					
				}else if(selected.endsWith("reject")){
					getPO("reject");
					so.setApproved("No");
					so.setPpID(ppID);
					module.disapprove(so);
					obj.setAction("Rejected");
					po.setStatus("Rejected by BO");
					po.setRank(9);
					
				}else if(selected.endsWith("reQuote")){
					getPO("reQuote");
					so.setApproved("No");
					so.setPpID(ppID);
					module.disapprove(so);
					obj.setAction("Re-Quote");
					po.setStatus("Re-Quote");
					po.setRank(10);
				}
				
				po.setPpID(ppID);
				if(txtRemark.getValue().toString()!=""){
					obj.setRemarks(txtRemark.getValue().toString());
				}
				
				bModule.addBudget(obj);
			}
			 
			ppModule.changeDecision(po);
			
	  }
	  
	  public void getPO(String status){
		  Application app = Application.getInstance();
		  PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
		 
		  String purchaseCode = "";
		  purchaseCode = module.getPCode(ppID);
		  PrePurchaseModule ppModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
		  PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
		  PurchaseRequestObject oneRequest = ppModule.singleRequest(ppID);	
		  String requester = oneRequest.getRequesterUserID();
		  Collection userID = new ArrayList();
		  userID = model.groupOfUser("po.permission.manageQuotation");
		  if(status.equals("reject")){
			  Collection id = ppModule.getHOD(requester);
			  for(Iterator i = id.iterator();i.hasNext();){
	    			HashMap map = (HashMap)i.next();
	    			userID.add(map);
	    		}
			  HashMap map= new HashMap();
			  map.put("userId", requester);
			  userID.add(map);
		  }
		  sendNotificationToPO(userID, purchaseCode, status);
	  }
	  
	  public void sendNotificationToPO(Collection id, String purchaseCode, String status){
	       Log log = Log.getLog(getClass());
	    	
	    	try {
	             Application app = Application.getInstance();
	             MessagingModule mm;
	             User user;
	             SmtpAccount smtpAccount;
	             Message message;

	             mm = Util.getMessagingModule();
	             user = app.getCurrentUser();
	             smtpAccount = mm.getSmtpAccountByUserId(user.getId());

	             // construct the message to send
	             message = new Message();
	             message.setMessageId(UuidGenerator.getInstance().getUuid());
	             
	             IntranetAccount intranetAccount;

	            // intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(getWidgetManager().getUser().getId());
	            // message.setFrom(intranetAccount.getFromAddress());
	            List HODList;
	            HODList = new ArrayList(id.size());
	            for (Iterator i = id.iterator(); i.hasNext();) {
	            	HashMap map = (HashMap) i.next();
	                intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId((String) map.get("userId"));
	                if(intranetAccount != null) {
			            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
			            HODList.add(add);
	                }
	     		}
	            String notificationBody = new String();
	            if (HODList.size() > 0)
	                message.setToIntranetList(HODList);
	            if(status.equals("approve")){
	            	message.setSubject(app.getMessage("po.label.prePurchase", "Pre-purchase Request") + ": " + 
	 	                    app.getMessage("po.label.newPendingPOIssuance", " New Request(s) Pending for PO Fulfillment"));
	 	           
	 	           notificationBody = "<p>There is a purchase request <i>" 
	 	            	+ purchaseCode 
	 	            	+ "</i> and is pending for purchase order fulfillment </p>" 
	 	            	+ "<p><b>View the request at: </b></p>" 
	 	            	+ "<p><a href='/ekms/purchaseordering/viewRequestPO.jsp?id="
	 	            	+ ppID
	 	            	+ "&status=po'>Here</a> </p>" ;
	 	            
	            }else if(status.equals("reQuote")){
	            	message.setSubject(app.getMessage("po.label.prePurchase", "Pre-purchase Request") + ": " + 
	 	                    app.getMessage("po.label.newPendingReQuotation", "New Request(s) Pending for Re-Quote"));
	 	           
	 	           notificationBody = "<p>There is a new purchase request <i>" 
	 	            	+ purchaseCode 
	 	            	+ "</i> and is pending for re-quote. </p>" 
	 	            	+ "<p><b>View the request at: </b></p>" 
	 	            	+ "<p><a href='/ekms/purchaseordering/supplierListing.jsp?id="
	 	            	+ ppID
	 	            	+ "&status=po'>Here</a> </p>" ;
	 	          
	            }else if(status.equals("reject")){
	            	message.setSubject(app.getMessage("po.label.prePurchase", "Pre-purchase Request") + ": " + 
	                           app.getMessage("po.label.rejectRequest", "Request is being rejected"));
	            	notificationBody = "<p>Request <i>" 
	                	+ purchaseCode 
	                	+ "</i> is rejected by " 
	                	+ user.getName()
	                	+ "</p>" ;
	            }
	           
	            message.setBody(notificationBody);
	            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
	            message.setDate(new Date());

	            mm.sendMessage(smtpAccount, message, user.getId(), false);
	        }
	        catch (Exception e) {
	            log.error("Error sending notification: ", e);
	        }
	  }
	  
	  
	  // === [ getters/setters ] =================================================  
	  public Radio getApprove() {
		  return approve;
	  }
	  public void setApprove(Radio approve) {
		  this.approve = approve;
	  }
	 
	  public Button getCancel() {
		return cancel;
	  }

	  public void setCancel(Button cancel) {
		this.cancel = cancel;
	  }

	  public Panel getButtonPanel() {
		  return buttonPanel;
	  }
	  
	  public void setButtonPanel(Panel buttonPanel) {
		  this.buttonPanel = buttonPanel;
	  }
	  public String getPpID() {
		  return ppID;
	  }
	  public void setPpID(String ppID) {
		  this.ppID = ppID;
	  }
	  public ButtonGroup getRadioGroup() {
		  return radioGroup;
	  }
	  public void setRadioGroup(ButtonGroup radioGroup) {
		  this.radioGroup = radioGroup;
	  }
	  public Radio getReject() {
		  return reject;
	  }
	  public void setReject(Radio reject) {
		  this.reject = reject;
	  }
	  public Radio getReQuote() {
		  return reQuote;
	  }
	  public void setReQuote(Radio reQuote) {
		  this.reQuote = reQuote;
	  }
	  public Button getSubmit() {
		  return submit;
	  }
	  public void setSubmit(Button submit) {
		  this.submit = submit;
	  }
	  public TextBox getTxtRemark() {
		  return txtRemark;
	  }
	  public void setTxtRemark(TextBox txtRemark) {
		  this.txtRemark = txtRemark;
	  }

	  public Hidden getApproval() {
		  return approval;
	  }

	  public void setApproval(Hidden approval) {
		  this.approval = approval;
	  }

	  public String getStatus() {
		  return status;
	  }

	  public void setStatus(String status) {
		  this.status = status;
	  }

}
