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

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
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
import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.permission.model.PermissionModel;
import com.tms.sam.po.setting.model.ItemObject;

public class SupplierListingForm extends Form {
	 
	  private Label txtDptCode;
	  private Label txtRequester;
	  private Label txtDpt;
	  private Label txtDate;
	  private Label attachment;
	  private Label txtNeededBy;
	  private Collection txtAttachment;
	
	  protected String ppID = "";
	  private String reason;
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
	  private Button send;
	  private Button cancel;
	  private Button add;
	  private Button generate;
	  private Panel buttonPanel;
	  private Map attachmentMap;
	  private SupplierListing[] supplierTable;
	  public static final String FORWARD_BACK = "back";	
	  public static final String FORWARD_SUPPLIER_ITEM= "supplier item";
	  public static final String FORWARD_GENERATE= "generate RFQ";
	  public static final String FORWARD_ERROR= "error";
	  public void init(){

		  setMethod("POST");
		  setColumns(2);
		  
		  Application app = Application.getInstance();
		  
		  txtDptCode = new Label("txtDptCode", ""); 
		  txtRequester = new Label("txtrequester", "");
		  txtDpt = new Label("txtDpt", "");
		  txtDate = new Label("txtDate", "");
		  txtNeededBy = new Label("txtNeededBy", "");
		  
		  attachment = new Label("attachment", app.getMessage("po.label.attachment", "Attachment"));
		  
		  send = new Button("send", app.getMessage("supplier.label.send", "Send to Budget Officer"));
		  send.setHidden(true);
		  
		  cancel = new Button("cancel", app.getMessage("po.label.cancel", "Cancel")); 
		  add = new Button("add", app.getMessage("supplier.label.addSupplierToItem", "Add Supplier to Item"));
		  generate = new Button("generate", app.getMessage("po.label.generateRFQ", "Generate Request for Quotation")); 
		  generate.setHidden(true);
		  
		  buttonPanel = new Panel("buttonPanel");
	      buttonPanel.addChild(add);
	      buttonPanel.addChild(generate);
	      buttonPanel.addChild(send);
	      buttonPanel.addChild(cancel);
	      supplierItemCode = null;
		  supplierItemDesc = null;
		  supplierItemQty = null;
		  supplierItemMeasure = null;
		  supplierCount = null;
		  supplierTable = null;
		  
		  addChild(txtDptCode);
		  addChild(txtRequester);
		  addChild(txtDpt);
		  addChild(txtDate);
		  addChild(attachment);
		  addChild(txtNeededBy);
		  addChild(buttonPanel);
		  		   		 	 
	  }
	  
	  public String getTemplate() {
		 return "po/supplierListingForm";
	  }
	  
	  public void onRequest(Event event){
		  init();
		  int size;
		  event.getRequest().getSession().setAttribute("purchaseID",ppID);
		  PurchaseRequestObject oneRequest = null;
		  Application app = Application.getInstance();
		  PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
		 
		  Collection items = null;
		 
		  attachmentMap = new SequencedHashMap();
		  txtAttachment = null;
		  if(! "".equals(ppID)) {
			  
			  oneRequest = module.singleRequest(ppID);
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
			  
			  priority = oneRequest.getPriority();
			  txtDptCode.setText(oneRequest.getPurchaseCode());
			  txtRequester.setText(oneRequest.getRequester());
			  txtDpt.setText(oneRequest.getDepartment());
			  
			  reason = oneRequest.getReason();
			  DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			  txtDate.setText(dmyDateFmt.format(oneRequest.getDateCreated()));
			  if(oneRequest.getNeededBy()== null){
				  txtNeededBy.setText("---");
			  }else{
				  txtNeededBy.setText(oneRequest.getNeededBy().toString()); 
			  }
			  
			  
			  items = oneRequest.getPurchaseItemsObject();
			  size = oneRequest.getPurchaseItemsObject().size();
			  
			  Collection supplierItems = module.getSupplierItems(ppID);
			  
 			  itemCode = new String[size];
			  itemDesc = new String[size];
			  qty = new double[size];
			  unitOfMeasure = new String[size];
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
		  }
		  Collection supplier =null;
		  SupplierModule suppModule= (SupplierModule) app.getModule(SupplierModule.class);
		  supplier = suppModule.checkAddedSupplier(ppID);
		  if(suppModule.countTable(ppID)!=""){
			  int count = Integer.parseInt(suppModule.countTable(ppID));
			  supplierTable = new SupplierListing[count];
			
			  for(int i=0; i<count; i++){
				  supplierTable[i]= new SupplierListing("supplierTable" + i);
				  supplierTable[i].setPpID(ppID);
				  supplierTable[i].init();
				 
		    	  addChild(supplierTable[i]);
		      }		
		  }
		
		  Collection addedItem = suppModule.checkAddedItem(ppID);
		  if(addedItem.size()>= oneRequest.getPurchaseItemsObject().size()){
			  add.setHidden(true);
			  generate.setHidden(false);
			  if(suppModule.checkInsertedData(ppID)){
				  send.setHidden(false);
			  }
		  }
		
	  }
	  
	  public Forward onValidate(Event event){
		  String button = findButtonClicked(event);
			button = (button == null)? "" : button;
			
			if(button.endsWith("cancel")){
		       	return new Forward(FORWARD_BACK);
		        	
		    }else if(button.endsWith("send")){
		    	getBOID();
				return new Forward(FORWARD_BACK);
				
		    }else if(button.endsWith("add")){
				return new Forward(FORWARD_SUPPLIER_ITEM);
				
		    }else if(button.endsWith("generate")){
				Collection supplier =null;
				SupplierModule suppModule= (SupplierModule) Application.getInstance().getModule(SupplierModule.class);
				supplier = suppModule.checkAddedSupplier(ppID);
				if(supplier.size()!=0){
					HttpSession session = event.getRequest().getSession();
					if(session != null)
						session.setAttribute("ppID", ppID);
					return new Forward(FORWARD_GENERATE);
				}else{
					return new Forward(FORWARD_ERROR);
				}
		    	
				
		    }
			
		  return null;
	  }
	  
	  public void getBOID(){
		  PurchaseRequestObject po = new PurchaseRequestObject();
		  Application app = Application.getInstance();
		  po.setStatus("Quoted");
		  po.setRank(7);
		  PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
		  String purchaseCode = "";
		  purchaseCode = module.getPCode(ppID);
		  PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
		  Collection userID = new ArrayList();
		  userID = model.groupOfUser("po.permission.approveBudget");
		
			
		  po.setPpID(ppID);
		  module.changeDecision(po);
		  sendNotification(userID, purchaseCode);
	  }
	  
	  public void  sendNotification(Collection id, String purchaseCode){
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

	            message.setSubject(app.getMessage("po.label.prePurchase", "Pre-purchase Request") + ": " + 
	                    app.getMessage("po.label.newPendingBudget", "New Request(s) Pending for budget approval"));

	            String notificationBody = "<p>There is a new purchase request <i>" 
	            	+ purchaseCode 
	            	+ "</i> and is pending for budget approval. </p>" 
	            	+ "<p><b>View the " 
	            	+ "<a href='/ekms/purchaseordering/budgetApproval.jsp?id="
	            	+ ppID
	            	+ "'>request</a></b></p>" ;
	          
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
	  public String getPpID() {
		return ppID;
	  }

	  public void setPpID(String ppID) {
		this.ppID = ppID;
	  }

	  public Button getSend() {
		return send;
	  }
	  
	  public void setSend(Button send) {
		this.send = send;
      }
	  
	  public Label getAttachment() {
		return attachment;
	  }

	  public void setAttachment(Label attachment) {
		this.attachment = attachment;
	  }

	  public Map getAttachmentMap() {
		return attachmentMap;
	  }

	  public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	  }
	  public Collection getTxtAttachment() {
		return txtAttachment;
	  }

	  public void setTxtAttachment(Collection txtAttachment) {
		this.txtAttachment = txtAttachment;
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

	  public Label getTxtRequester() {
		return txtRequester;
	  }

	  public void setTxtRequester(Label txtRequester) {
		this.txtRequester = txtRequester;
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

	public Button getAdd() {
		return add;
	}

	public void setAdd(Button add) {
		this.add = add;
	}

	public Button getGenerate() {
		return generate;
	}

	public void setGenerate(Button generate) {
		this.generate = generate;
	}

	public SupplierListing[] getSupplierTable() {
		return supplierTable;
	}

	public void setSupplierTable(SupplierListing[] supplierTable) {
		this.supplierTable = supplierTable;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
