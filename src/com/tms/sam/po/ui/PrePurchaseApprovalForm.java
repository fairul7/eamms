 package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.sam.po.model.PrePurchaseApprovalObject;
import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.permission.model.PermissionModel;

public class PrePurchaseApprovalForm extends Form{
	
	private Label txtDptCode;
	private Label txtRequester;
	private Label txtDate;
	private Label attachment;
	private Label txtPReason;
	private Label txtNeededBy;
	private Collection txtAttachment;
	private TextBox txtRemark;
	private Radio approve;
	private Radio reject;

	protected String ppID = "";
	private String requesterId;
	private String priority;
	private String[] itemCode;
	private String[] itemDesc;
	private double[] qty;
	private String[] unitOfMeasure;
	private Button submit;
	private Button cancel;
	private Panel buttonPanel;
	private ButtonGroup radioGroup;
	private Map attachmentMap;
	public static final String FORWARD_BACK = "back";	
	  
	public void init(){

		  setMethod("POST");
		  setColumns(2);
		  Application app = Application.getInstance();
		  txtRequester = new Label("txtrequester", "");
		 
		  txtDate = new Label("txtDate", "");
		  txtPReason = new Label("txtReason", "");
		  txtDptCode = new Label("txtDptCode", "");
		  txtNeededBy = new Label("txtNeededBy", "");
		  
		  attachment = new Label("attachment", app.getMessage("po.label.attachment", "Attachment"));
		  
		  txtRemark = new TextBox("txtRemark");
		  txtRemark.setRows("4");
		  txtRemark.setCols("80");
		  		  
		  approve = new Radio("approve");
		  approve.setText(app.getMessage("userRequest.label.approve", "Approve"));
		  approve.setChecked(true);
		  reject = new Radio("reject");
		  reject.setText(app.getMessage("userRequest.label.reject", "Reject"));
		  
		  radioGroup = new ButtonGroup("radioGroup");
		  radioGroup.setType(ButtonGroup.RADIO_TYPE);
		  radioGroup.addButton(approve);
		  radioGroup.addButton(reject);
		  
		  cancel = new Button("cancel", "Cancel"); 
		  submit = new Button("submit", "Submit"); 
		
		  buttonPanel = new Panel("buttonPanel");
		  buttonPanel.addChild(submit);
		  buttonPanel.addChild(cancel);
		
		  addChild(txtDptCode);
		  addChild(txtRequester);
		  
		  addChild(txtDate);
		  addChild(attachment);
	}
		
	public void onRequest(Event event){
		  init();
		  int size;
		  
		  PurchaseRequestObject oneRequest = null;
		  
		  Collection items = null;
		  attachmentMap = new SequencedHashMap();
		  txtAttachment = null;
		  if(! "".equals(ppID)) {
			  Application app = Application.getInstance();
			  PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
			  oneRequest = module.singleRequest(ppID);
			  txtAttachment = module.getAttachment(ppID);
			  requesterId = oneRequest.getRequesterUserID();
			  if(txtAttachment.size()!=0){
				  for(Iterator i = txtAttachment.iterator();i.hasNext();){
					  Map map = (Map)i.next() ;
					  String attach  = map.get("newFileName").toString();
					  String attachID = map.get("attachmentID").toString();
					  attachmentMap.put(attach,attachID);
				  }
			  }else{
				  attachment.setHidden(true);
			  }
			  
			  if(oneRequest.getNeededBy()!=null){
				  txtNeededBy.setText(oneRequest.getNeededBy().toString());
			  }else{
				  txtNeededBy.setText("---");
			  }
			  
			  txtDptCode.setText(oneRequest.getPurchaseCode());
			  txtRequester.setText(oneRequest.getRequester());
			  DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			  txtDate.setText(dmyDateFmt.format(oneRequest.getDateCreated()));
			  priority = oneRequest.getPriority();
			  txtPReason.setText(oneRequest.getReason());
			  
			  items = oneRequest.getPurchaseItemsObject();
			  size = oneRequest.getPurchaseItemsObject().size();
			  
			  itemCode = new String[size];
			  itemDesc = new String[size];
			  qty = new double[size];
			  unitOfMeasure = new String[size];
			 
			  
		      for(int count=0; count<size; count++){
		    	  itemCode[count]= new String("itemCode" + count);
		    	  itemDesc[count]= new String("itemDesc" + count);
		    	  unitOfMeasure[count]= new String("unitOfMeasure" + count);
		      }		
		      
		      
		      int index =0;
			  for(Iterator itr= items.iterator(); index<size && itr.hasNext(); ) {
				  PurchaseItemObject obj = (PurchaseItemObject)itr.next();
				  itemCode[index]=obj.getItemCode();
				  itemDesc[index]=obj.getItemDesc();
				  qty[index]= obj.getQty();
				  unitOfMeasure[index]=obj.getUnitOfMeasure();
				  index++;
			  }
			
			  addChild(txtPReason);
			  addChild(txtNeededBy);
			  addChild(txtRemark);
			  addChild(radioGroup);
			  addChild(buttonPanel);
		  }
	}
	  
	public String getTemplate() {
			 return "po/preApproval";
	}
	 
	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public Forward onSubmit(Event event) {
		  Forward forward = super.onSubmit(event);
		  if((String)radioGroup.getValue()=="")
			  radioGroup.setInvalid(true);
		  
		  return forward;
	}
	  
	public Forward onValidate(Event event){
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	
        if (button.endsWith("submit")) {
            // process upload
        	
        	decisionMaking();
        	
            return new Forward(FORWARD_BACK);
        }else if(button.endsWith("cancel")){
        	
        	return new Forward(FORWARD_BACK);
        }
		
		return new Forward(FORWARD_BACK);
	}

	public void decisionMaking(){
		String selected = (String)radioGroup.getValue();
		
		Application app = Application.getInstance();
		String ppaID = UuidGenerator.getInstance().getUuid();
		
		PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
		PrePurchaseApprovalObject obj= new PrePurchaseApprovalObject();
		PurchaseRequestObject po = new PurchaseRequestObject();
		String purchaseCode = "";
		obj.setPpaID(ppaID);
		obj.setPpID(ppID);
		purchaseCode = module.getPCode(ppID);
		Collection userID = new ArrayList();
		if(selected.endsWith("approve")){
			
			PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
			
			userID = model.groupOfUser("po.permission.manageQuotation");
		
			sendNotification(userID, purchaseCode,"Approved");
			obj.setApproved(true);
			po.setStatus("Approved by HOD");
			po.setRank(5);
			
		}else if(selected.endsWith("reject")){
			obj.setApproved(false);
			po.setStatus("Rejected by HOD");
			po.setRank(6);
			HashMap map = new HashMap();
			map.put("userId", requesterId);
			userID.add(map);
			sendNotification(userID,purchaseCode,"Rejected");
		}
		po.setPpID(ppID);
		if(txtRemark.getValue().toString()!=""){
			obj.setRemarks(txtRemark.getValue().toString());
		}
	
		obj.setCheckedBy(app.getCurrentUser().getName());
		
		module.saveDecision(obj,po);
		
	}
	
	public void sendNotification(Collection id, String purchaseCode, String status){
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
        
            if (HODList.size() > 0)
                message.setToIntranetList(HODList);

            String reason = null ;
            if(txtRemark.getValue().toString()!=null){
            	reason = txtRemark.getValue().toString();
            }else
            	reason= app.getMessage("po.label.none");
            String notificationBody= new String();
            if(status.equals("Approved")){
            	message.setSubject(app.getMessage("po.label.newPendingQuotation", "New Request(s) Pending for Quotation"));
                notificationBody = "Request ("
                	+ "<a href='/ekms/purchaseordering/supplierListing.jsp?id="
	            	+ ppID
	            	+ "'>" 
	            	+ purchaseCode 
	            	+"</a>) is approved and pending for quotaion. ";
            }else if(status.equals("Rejected")){
            	message.setSubject(app.getMessage("po.label.rejectRequest", "Request is being rejected"));
            	notificationBody = user.getName()+ "has rejected your request (" 
	            	+ "<a href='/ekms/purchaseordering/viewMyRequest.jsp?id="
	            	+ ppID
	            	+ "'>" 
	            	+ purchaseCode 
	            	+"</a>" 
                	+ ") <br>" 
                	+ "Reason: " 
                	+ reason;
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

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Label getTxtDate() {
		return txtDate;
	}

	public void setTxtDate(Label txtDate) {
		this.txtDate = txtDate;
	}

	public Label getTxtDptCode() {
		return txtDptCode;
	}

	public void setTxtDptCode(Label txtDptCode) {
		this.txtDptCode = txtDptCode;
	}


	public Label getTxtNeededBy() {
		return txtNeededBy;
	}

	public void setTxtNeededBy(Label txtNeededBy) {
		this.txtNeededBy = txtNeededBy;
	}

	public Label getTxtPReason() {
		return txtPReason;
	}

	public void setTxtPReason(Label txtPReason) {
		this.txtPReason = txtPReason;
	}

	public TextBox getTxtRemark() {
		return txtRemark;
	}

	public void setTxtRemark(TextBox txtRemark) {
		this.txtRemark = txtRemark;
	}

	public Label getTxtRequester() {
		return txtRequester;
	}

	public void setTxtRequester(Label txtRequester) {
		this.txtRequester = txtRequester;
	}

	

	public Label getAttachment() {
		return attachment;
	}

	public void setAttachment(Label attachment) {
		this.attachment = attachment;
	}

	public Collection getTxtAttachment() {
		return txtAttachment;
	}

	public void setTxtAttachment(Collection txtAttachment) {
		this.txtAttachment = txtAttachment;
	}

	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public String[] getItemCode() {
		return itemCode;
	}

	public void setItemCode(String[] itemCode) {
		this.itemCode = itemCode;
	}

	public String[] getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String[] itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public double[] getQty() {
		return qty;
	}

	public void setQty(double[] qty) {
		this.qty = qty;
	}

	public String[] getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String[] unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	 	 
	  
}
