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

import org.apache.commons.collections.SequencedHashMap;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.permission.model.PermissionModel;
import com.tms.sam.po.setting.model.ItemObject;

public class ViewMyRequest extends Form{
	
	private Label remarks;
	private Label txtrequester;
	private Label txtDpt;
	private Label txtDate;
	private Label txtPReason;
	private Label txtNeededBy;
	private Label txtStatus;
	private Label txtRemarks;
	private Label attachment;
	protected String ppID = "";
	private String priority;
	private String[] itemCode;
	private String[] itemDesc;
	private double[] qty;
	private String[] unitOfMeasure;
	private String[] supplierItemCode;
	private String[] supplierItemDesc;
	private double[] supplierItemQty;
	private String[] supplierItemMeasure;
	private int[] supplierCount;
	private ViewOnlySupplierListing[] approvedSupplier;
	private ViewOnlySupplierListing[] rejectedSupplier;
	private Button cancel;
	private Button withdraw;
	private Button resubmit;
	private  PurchaseRequestObject oneRequest;
	private Collection txtAttachment;
	private Map attachmentMap;
	public static final String FORWARD_BACK = "back";
	public static final String FORWARD_PO = "PO Issuance";
	
	public void init(){
		
		 setMethod("POST");
	 	 setColumns(2);
	 	 Application app = Application.getInstance();
	 	 User currentUser;
	   	 currentUser =app.getCurrentUser();
	 	
	 	 txtrequester = new Label("txtrequester",  currentUser.getName());
	 	 txtDpt = new Label("txtDpt", "");
	 	 txtDate = new Label("txtDate", "");
	 	 txtPReason = new Label("txtReason", "");
	 	 txtNeededBy = new Label("txtNeededBy", "");	 	 
	 	 txtStatus = new Label("txtStatus", "");
	 	 
	 	 remarks = new Label("remarks", "Remarks");
	 	 txtRemarks = new Label("txtRemarks", "");
	 	 
	 	 attachment = new Label("attachment", app.getMessage("po.label.attachment", "Attachment"));
	 	  
	 	 cancel = new Button("cancel", app.getMessage("po.label.cancel", "Cancel")); 
	 	 withdraw = new Button("withdraw", app.getMessage("po.label.withdraw", "Withdraw"));
	 	 withdraw.setOnClick("return confirmWithdraw()");
	 	 resubmit = new Button("resubmit", app.getMessage("po.label.resubmit", "Resubmit"));
	 	 resubmit.setHidden(true);
	 	 
	 	 supplierItemCode = null;
		 supplierItemDesc = null;
		 supplierItemQty = null;
		 supplierItemMeasure = null;
		 supplierCount = null;
		 approvedSupplier = null;
		 rejectedSupplier = null;
		 
	 	 addChild(txtrequester);	 	 
	 	 addChild(txtDpt);
	 	 addChild(txtDate);
	 	 addChild(withdraw);
	 	 addChild(resubmit);
	 	 addChild(attachment);
	}
	 
	public void onRequest(Event event){
		  init();
		  int size;
		  String reason = "";
		  Collection items = null;
		  txtAttachment = null;
		  attachmentMap = new SequencedHashMap();
		  if(! "".equals(ppID)) {
			  Application app = Application.getInstance();
			  PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
			  txtAttachment = module.getAttachment(ppID);			  
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
			  
			  oneRequest = module.singleRequest(ppID);
			  if(oneRequest.getStatus().equals("Approved by HOD")
					  ||oneRequest.getStatus().equals("Rejected by HOD")
					  ||oneRequest.getStatus().equals("Approved by BO")
					  ||oneRequest.getStatus().equals("Rejected by BO") 
					  ||oneRequest.getStatus().equals("Re-Quote")){
				  reason = module.getReason(ppID, oneRequest.getStatus());
			  }else{
				  remarks.setHidden(true);
				  txtRemarks.setHidden(true);
			  }
			  module.getStatus(ppID);
			 
			  txtDpt.setText(oneRequest.getDepartment());
			  DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			  txtDate.setText(dmyDateFmt.format(oneRequest.getDateCreated()));
			  priority = oneRequest.getPriority();
			  if(oneRequest.getNeededBy()!=null){
				  txtNeededBy.setText(dmyDateFmt.format(oneRequest.getNeededBy()));
			  }else{
				  txtNeededBy.setText("---");
			  }
			 
			  txtStatus.setText(oneRequest.getStatus());
			  txtPReason.setText(oneRequest.getReason());
			  
			  if(oneRequest.getStatus().equals("Withdrawn") || oneRequest.equals("Closed")){
				  withdraw.setHidden(true);
			  }
			  
			  if(oneRequest.getStatus().startsWith("Rejected")){
				  resubmit.setHidden(false);
			  }
			  
			  if(reason!=null && !reason.equals(""))
			  {
				  txtRemarks.setText(reason);
			  }
			  else{
				  remarks.setHidden(true);
				  txtRemarks.setHidden(true);
			  }
			  
			  items = oneRequest.getPurchaseItemsObject();
			  size = oneRequest.getPurchaseItemsObject().size();
			  
			
			  itemCode = new String[size];
			  itemDesc = new String[size];
			  qty = new double[size];
			  unitOfMeasure = new String[size];
			  Collection supplierItems = module.getSupplierItems(ppID);
			  if(supplierItems!=null){
				  if(supplierItems.size()!=0){
					  supplierItemCode = new String[size];
					  supplierItemDesc = new String[size];
					  supplierItemQty = new double[size];
					  supplierItemMeasure = new String[size];
					  supplierCount = new int[size];
				  }
			  }
			  
		      for(int count=0; count<size; count++){
		    		
		    	  itemCode[count]= new String("itemCode" + count);
		    	  itemDesc[count]= new String("itemDesc" + count);
		    	  unitOfMeasure[count]= new String("unitOfMeasure" + count);
		    	 
		    	  if(supplierItems!=null){
					  if(supplierItems.size()!=0){
		    		  supplierItemCode[count]= new String("supplierItemCode" + count);
			    	  supplierItemDesc[count]= new String("supplierItemDesc" + count);
			    	  supplierItemMeasure[count]= new String("supplierItemMeasure" + count);
					  }
		    	  }
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
			 
			  index=0;
			  if(supplierItems!=null){
				  if(supplierItems.size()!=0){
					  for(Iterator itr= supplierItems.iterator(); index<size && itr.hasNext(); ) {
						  ItemObject obj = (ItemObject)itr.next();
						  supplierItemCode[index]=obj.getItemCode();
						  supplierItemDesc[index]=obj.getItemDesc();
						  supplierItemQty[index]= obj.getMinQty();
						  supplierItemMeasure[index]=obj.getUnitOfMeasure();
						  supplierCount[index]=obj.getCounting();
						  index++;
					  }
				  }
			  }
			  
			  Collection supplier =null;
			  if(!oneRequest.getStatus().equals("Withdrawn")){
				  SupplierModule suppModule= (SupplierModule) app.getModule(SupplierModule.class);
				  supplier = suppModule.checkAddedSupplier(ppID);
				  if(suppModule.countTable(ppID)!=""){
					  int count = Integer.parseInt(suppModule.countTable(ppID));
					  approvedSupplier = new ViewOnlySupplierListing[count];
					  rejectedSupplier = new ViewOnlySupplierListing[count];
					  
					  for(int i=0; i<count; i++){
						  approvedSupplier[i]= new ViewOnlySupplierListing("approvedSupplier" + i);
						  approvedSupplier[i].setPpID(ppID);
						  approvedSupplier[i].init();
						 
				    	  addChild(approvedSupplier[i]);
				      }		
					  
					  for(int i=0; i<count; i++){
						  rejectedSupplier[i]= new ViewOnlySupplierListing("rejectedSupplier" + i);
						  rejectedSupplier[i].setPpID(ppID);
						  rejectedSupplier[i].init();
						 
				    	  addChild(rejectedSupplier[i]);
				      }		
				  }
			  }
			
			 
			 			 
			  addChild(txtPReason);
			  addChild(txtNeededBy);
			  addChild(txtStatus);
			  addChild(remarks);
			  addChild(txtRemarks);
			  addChild(cancel);
		  }
		  
	}
	  
	public String getTemplate() {
		return "po/myRequest";
    }

	// === [ getters/setters ] =================================================
    public Forward onValidate(Event evt){
    	String button = findButtonClicked(evt);
		button = (button == null)? "" : button;
		
		if(button.endsWith("cancel")){
			return new Forward(FORWARD_BACK);
	    }else if(button.endsWith("withdraw")){
	    	withdrawRequest();
	    	return new Forward(FORWARD_BACK);
	    }else if(button.endsWith("resubmit")){
	    	resubmitRequest();
	    	return new Forward(FORWARD_BACK);
	    }
		
		return null;
	    
    }
    
    public void withdrawRequest(){
    	PrePurchaseModule module = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	module.deleteRequest(ppID);
    	getUserID("withdraw");
    }
    
    public void resubmitRequest(){
    	PrePurchaseModule module = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	module.resubmitRequest(ppID);
    	getUserID("resubmit");
    }
    public void getUserID(String selectedStatus) {
    	
    	Collection userID = new ArrayList();
    	String status = oneRequest.getStatus();
    	Collection id = null;
    	PrePurchaseModule ppModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
    	userID = ppModule.getHOD(Application.getInstance().getCurrentUser().getId());
    	if(status.equals("Approved by HOD")){
    		id = model.groupOfUser("po.permission.manageQuotation");
    		for(Iterator i = id.iterator();i.hasNext();){
    			HashMap map = (HashMap)i.next();
    			userID.add(map);
    		}
    		
    	}else if(!status.equals("New") || !status.equals("Resubmit")){
    		id = model.groupOfUser("po.permission.manageQuotation");
    		for(Iterator i = id.iterator();i.hasNext();){
    			HashMap map = (HashMap)i.next();
    			userID.add(map);
    		}
    		
    		id = model.groupOfUser("po.permission.approveBudget");
    		for(Iterator i = id.iterator();i.hasNext();){
    			HashMap map = (HashMap)i.next();
    			userID.add(map);
    		}
  		  	
    	}
    	
    	sendNotification(userID, selectedStatus);
    } 
    
    public void sendNotification(Collection userID, String selectedStatus){
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
            HODList = new ArrayList(userID.size());
            for (Iterator i = userID.iterator(); i.hasNext();) {
         		HashMap map = (HashMap) i.next();
                intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId((String) map.get("userId"));
                if(intranetAccount != null) {
		            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
		            HODList.add(add);
                }
     		}
        
            if (HODList.size() > 0)
                message.setToIntranetList(HODList);

            
            String notificationBody = new String();
            if(selectedStatus.equals("withdraw")){
            	  message.setSubject(app.getMessage("po.label.withdrawRequest", "Request is being withdrawn"));
            	  notificationBody = user.getName()
                  	+ "</i> has withdrawn the request (" 
                  	+ "<a href='/ekms/purchaseordering/viewUserRequest.jsp?status=nonPO&id="
                	+ ppID
                	+ "'>" 
                	+ oneRequest.getPurchaseCode()
                	+ "</a> " 
                	+ ").</p>" ;
            }else if(selectedStatus.equals("resubmit")){
            	 message.setSubject(app.getMessage("po.label.prePurchase", "Pre-purchase Request") + ": " + 
                        app.getMessage("po.label.resubmitRequest", "Request is being resubmitted"));
            	  notificationBody = user.getName() 
                    	+ " has resubmitted request (" 
                    	+ "<a href='/ekms/purchaseordering/preApproval.jsp?id="
                    	+ ppID
                    	+ "'>" 
                    	+ oneRequest.getPurchaseCode() 
                    	+ "</a> ) " 
                    	+ "and pending for your approval. ";
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
    
	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public static String getFORWARD_BACK() {
		return FORWARD_BACK;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Label getRemarks() {
		return remarks;
	}

	public void setRemarks(Label remarks) {
		this.remarks = remarks;
	}

	public Label getTxtRemarks() {
		return txtRemarks;
	}

	public void setTxtRemarks(Label txtRemarks) {
		this.txtRemarks = txtRemarks;
	}

	public Label getTxtDate() {
		return txtDate;
	}

	public void setTxtDate(Label txtDate) {
		this.txtDate = txtDate;
	}

	public Label getTxtDpt() {
		return txtDpt;
	}

	public void setTxtDpt(Label txtDpt) {
		this.txtDpt = txtDpt;
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

	public Label getTxtrequester() {
		return txtrequester;
	}

	public void setTxtrequester(Label txtrequester) {
		this.txtrequester = txtrequester;
	}

	public Label getTxtStatus() {
		return txtStatus;
	}

	public void setTxtStatus(Label txtStatus) {
		this.txtStatus = txtStatus;
	}

	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public String[] getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String[] unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public double[] getQty() {
		return qty;
	}

	public void setQty(double[] qty) {
		this.qty = qty;
	}
	
	public ViewOnlySupplierListing[] getApprovedSupplier() {
		return approvedSupplier;
	}

	public void setApprovedSupplier(ViewOnlySupplierListing[] approvedSupplier) {
		this.approvedSupplier = approvedSupplier;
	}

	public ViewOnlySupplierListing[] getRejectedSupplier() {
		return rejectedSupplier;
	}

	public void setRejectedSupplier(ViewOnlySupplierListing[] rejectedSupplier) {
		this.rejectedSupplier = rejectedSupplier;
	}

	public String[] getSupplierItemCode() {
		return supplierItemCode;
	}

	public void setSupplierItemCode(String[] supplierItemCode) {
		this.supplierItemCode = supplierItemCode;
	}

	public String[] getSupplierItemDesc() {
		return supplierItemDesc;
	}

	public void setSupplierItemDesc(String[] supplierItemDesc) {
		this.supplierItemDesc = supplierItemDesc;
	}

	public String[] getSupplierItemMeasure() {
		return supplierItemMeasure;
	}

	public void setSupplierItemMeasure(String[] supplierItemMeasure) {
		this.supplierItemMeasure = supplierItemMeasure;
	}

	public double[] getSupplierItemQty() {
		return supplierItemQty;
	}

	public void setSupplierItemQty(double[] supplierItemQty) {
		this.supplierItemQty = supplierItemQty;
	}

	public int[] getSupplierCount() {
		return supplierCount;
	}

	public void setSupplierCount(int[] supplierCount) {
		this.supplierCount = supplierCount;
	}

	public Button getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(Button withdraw) {
		this.withdraw = withdraw;
	}

	public PurchaseRequestObject getOneRequest() {
		return oneRequest;
	}

	public void setOneRequest(PurchaseRequestObject oneRequest) {
		this.oneRequest = oneRequest;
	}

	public Button getResubmit() {
		return resubmit;
	}

	public void setResubmit(Button resubmit) {
		this.resubmit = resubmit;
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
}
