package com.tms.sam.po.ui;


import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
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
import com.tms.sam.po.model.AttachmentObject;
import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.setting.model.ConfigModule;
import com.tms.sam.po.setting.model.ItemObject;
import com.tms.sam.po.setting.model.SettingModule;

public class PrePurchaseRequestForm extends Form {
	
	private TextField[] qty;
	private Label[] itemCode;
	private Label[] itemDesc;
	private Label[] unitOfMeasure;
	private TextBox reason;
	private DatePopupField neededBy;
	private Button submit;
	private Button draft;
	private Button cancel;
	private CheckBox[] deleteBox;
	private CheckBox mainDelete;
	private Button delete;
	private Button addItem;
	private SelectBox priority;
	private AttachmentInfo attachment;
	private String info;
	private int size;
	private Panel buttonPanel;
	private Collection itemInfo;
	private String ppID="";
	private String draftPPID;
	private String strReason;
	private String strPriority;
	private Date strNeededBy;
	private PurchaseRequestObject infoData;

	public static final String FORWARD_SUBMIT = "submit";
	public static final String FORWARD_ADD_ITEM = "addItem";
	public static final String FORWARD_DELETE = "delete";
	public static final String FORWARD_ERROR = "error";
	public static final String FORWARD_ITEM = "item";
	
	Collection quantityComparison;
	String capturingInfo = null;
	
    public void init(){
    	 setMethod("POST");
    	 setColumns(2);
    	 
    	 Application app = Application.getInstance();
    	 ConfigModule configModel = (ConfigModule) app.getModule(ConfigModule.class);
    	
         priority = new SelectBox("priority");
 		 priority.setMultiple(false);
 		 priority.setOptionMap(configModel.getPriorityOptionsMap());
 		 addChild(priority);
 	
 		 reason = new TextBox("reason");
 		
         reason.setRows("4");
         reason.setCols("80");
        
         addChild(reason);
         info = "";
         size = 0;
         neededBy = new DatePopupField("neededBy");
         neededBy.setOptional(true);
         addChild(neededBy);
         
         qty=null;
         itemCode=null;
         itemDesc = null;
         unitOfMeasure = null;
         deleteBox = null;
         
         addChild(new Label("lblAttachment", app.getMessage("po.label.attachment", "Attachment")));
         attachment = new AttachmentInfo("attachment");
         addChild(attachment);
         
         mainDelete = new CheckBox("mainDelete");
         mainDelete.setOnClick("toggleCheck()");
         
         buttonPanel = new Panel("buttonPanel");
                  
         delete = new Button("delete", app.getMessage("po.label.delete", "Delete"));
         addItem = new Button("addItem","Add Item");
         addItem.setOnClick("add()");
         buttonPanel.addChild(addItem);
         buttonPanel.addChild(delete);
         
         infoData= null;
         submit = new Button("submit", app.getMessage("po.label.submit", "Submit"));
        
         draft = new Button("draft", app.getMessage("po.label.draft", "Save as Draft"));
         cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("po.label.cancel", "Cancel"));
         addChild(submit);
         addChild(draft);
         addChild(cancel);
         addChild(buttonPanel);
         addChild(mainDelete); 
        
    }
    
     
    public void onRequest(Event event) {
    	
    	setInvalid(false);
    	HttpSession session;
    	PrePurchaseModule module = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	session = event.getRequest().getSession();
    	if(event.getParameter("flag")!=null){
    		draftPPID="";
    		removeChildren();
    		init();
    	
    		session.removeAttribute("attachmentMap");
    		session.removeAttribute("selectedItemCode");
    	}else if(event.getRequest().getSession().getAttribute("selectedItemCode")!=null){
    		Collection items =  (Collection)event.getRequest().getSession().getAttribute("selectedItemCode");
    		itemList(items);
    		getField();
    		if(draftPPID!=null && !"".equals(draftPPID)){
    				infoData = module.singleRequest(draftPPID);
    			
    			if(infoData!=null){
        			
            		reason.setValue(infoData.getReason());
            		priority.setSelectedOption(infoData.getPriority());
            		neededBy.setDate(infoData.getNeededBy());
            		if(infoData.getAttachmentObject()!=null){
            			try {
            				setAttachmentValues(event, infoData.getAttachmentObject());
            			} catch (FileNotFoundException e) {
            				e.printStackTrace();
            			} catch (StorageException e) {
            				e.printStackTrace();
            			}
            		}
            		
        		}
    		}
    	
    	}else if (event.getRequest().getParameter("status")!=null){
    		String status = event.getRequest().getParameter("status").toString();
    		draftPPID = event.getRequest().getParameter("ppID").toString();
    		
    		Collection items = module.getItems(draftPPID);
    		itemList(items);
    		event.getRequest().getSession().setAttribute("selectedItemCode", items);
    		infoData = module.singleRequest(draftPPID);
    		reason.setValue(infoData.getReason());
    		
    		priority.setSelectedOption(infoData.getPriority());
    		neededBy.setDate(infoData.getNeededBy());
    		if(infoData.getAttachmentObject()!=null){
    			try {
    				setAttachmentValues(event, infoData.getAttachmentObject());
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			} catch (StorageException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	 
    }
    
    protected void setAttachmentValues(Event event, Collection attachment) throws StorageException, FileNotFoundException {
    	User currentUser;
 	    Application app = Application.getInstance();
	   	currentUser =app.getCurrentUser();
        // clear attachment map
        HttpSession session = event.getRequest().getSession();
        session.removeAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE);
        Map attachmentMap = AttachmentForm.getAttachmentMapFromSession(event);

        // populate attachment map
        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
        for (Iterator i = attachment.iterator();i.hasNext();) {
        	AttachmentObject obj = (AttachmentObject)i.next();
            StorageFile dest = new StorageFile("/purchaseOrdering"+ "/" + currentUser.getId() + "/temp/"+ obj.getNewFileName());
           
            ss.store(dest);
            attachmentMap.put(dest.getName(), "-data in storage service-");
            
        }
    }
    
    public String getTemplate() {
		return "po/prePurchase";
	}
    
    public Forward onValidate(Event evt) {
    	String button = findButtonClicked(evt);
		button = (button == null)? "" : button;
		HttpSession session;
		session = evt.getRequest().getSession();
        if (button.endsWith("submit")) {
            // process upload
        	 session.removeAttribute("selectedItemCode");
            return saveRequest(evt,"New", 2);
        }else if(button.endsWith("addItem")){
        	setField();
        	return new Forward(FORWARD_ADD_ITEM);
        }else if(button.endsWith("delete")){
        	setField();
        	deleteItems();
        	
        }else if(button.endsWith("draft")){
        	 return saveRequest(evt,"Draft",1);
        }
        
		return null;
		
	    
    }
    
    public void getField(){
    	if(strReason!=null){
			reason.setValue(strReason);
		}
		if(strPriority!=null){
			priority.setSelectedOption(strPriority);
		}
		if(strNeededBy!=null){
			neededBy.setDate(strNeededBy);
		}
    	
    }
    public void setField(){
    	strReason = reason.getValue().toString();
    	strPriority = priority.getSelectedOptions().keySet().iterator().next().toString();
    	strNeededBy = neededBy.getDate();
    }
    public Forward onSubmit(Event event) {
        Forward forward = super.onSubmit(event);
        
	    Date checkDate = neededBy.getDate();
        Calendar calendar = Calendar.getInstance();
        if(checkDate != null){
        	 calendar.setTime(checkDate);
             boolean isDate=false;        
             
             if (calendar.before(Calendar.getInstance())) {
                 isDate=true;
             }
             
             if(isDate) {
             	neededBy.setInvalid(true);
                this.setInvalid(true);
             }
        }
        
        String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("submit") || button.endsWith("draft")) {
        	 if(reason.getValue().toString().equals("")){
        		 reason.setInvalid(true);
        		 this.setInvalid(true);
        	 }
        	 if(size == 0){
       	    	info = "Please add in at least one item.";
       	    	//this.setInvalid(true);
       	    	//return new Forward(FORWARD_ITEM);
          	 }
        	 
        	
        	
        	 if(priority.getSelectedOptions().keySet().iterator().next().toString().equals("")){
     	    	priority.setInvalid(true);
     	     }
        	 
        	
        	 int index = 0;
        	 SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
        	 ItemObject obj = new ItemObject();
        	 if(quantityComparison!=null)
	         	 for(Iterator i= quantityComparison.iterator(); index<size && i.hasNext();){
	         		String id = (String)i.next();
	         		itemInfo = module.getSelectedItem(id);
	         		for(Iterator itr= itemInfo.iterator(); itr.hasNext();){
	         			obj = (ItemObject)itr.next();
	         			if (qty[index].getValue() != null || qty[index].getValue() != ""){
		         			if (Double.parseDouble(qty[index].getValue().toString()) < obj.getMinQty()){
		         				capturingInfo = Application.getInstance().getMessage("po.message.invalidQuantity");
		         			}
	         			}
	         		index++;
	         		}
	         	 }
         	 if (capturingInfo != null){
         		 info = capturingInfo;
         		capturingInfo = null;
         		this.setInvalid(true);
         	 }
        }
       
        return forward;
    }
    
    public Forward saveRequest(Event evt, String status, int rank){
    	Application app = Application.getInstance();
		User currentUser;  
 		currentUser =app.getCurrentUser();
		
		PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
	    PurchaseRequestObject prObj = new PurchaseRequestObject();
	    
	    PurchaseItemObject piObj = new PurchaseItemObject();
	    Collection items = new ArrayList();
	    
	    AttachmentObject aObj = new AttachmentObject();
	    Collection attachment = new ArrayList();
	    
	    
	    // purchase request object
	    ppID = UuidGenerator.getInstance().getUuid();
	    if(draftPPID!=null && !"".equals(draftPPID)){
	    	ppID = draftPPID;
	    	prObj.setPpID(draftPPID);
	    }else{
	    	prObj.setPpID(ppID);
	    }
	    
	    prObj.setRequesterUserID(app.getCurrentUser().getId());
	   
	    if((String)reason.getValue()!="")
	    {
	       prObj.setReason((String)reason.getValue());
	    }
	    
	    Date date = neededBy.getDate();
	    prObj.setNeededBy(date);
	    prObj.setPurchaseCode(generatePurchaseCode());
	    
	    
	    prObj.setStatus(status);
	    prObj.setRank(rank);
	    prObj.setLastUpdatedBy(app.getCurrentUser().getName());
	    
	    if(priority.getSelectedOptions().keySet().iterator().next().toString()!=null){
	    	prObj.setPriority(priority.getSelectedOptions().keySet().iterator().next().toString());
	    }
	    //item object
	    if(size == 0){
	    	return new Forward(FORWARD_ERROR);
	    }else{
	    	 for(int i=0; i<size; i++){
	   	      if((String)itemCode[i].getText()!="")
	   	      {
	   	    		piObj = new PurchaseItemObject();
	   	    		String itemID = UuidGenerator.getInstance().getUuid();
	   	    	    piObj.setItemID(itemID);
	   	    	    if(draftPPID!=null && !"".equals(draftPPID)){
		   	 	    	piObj.setPpID(draftPPID);
		   		    }else{
		   		    	piObj.setPpID(ppID);
		   		    }
	   	    	    piObj.setUserID(app.getCurrentUser().getId());
	   	    	    piObj.setItemCode((String)itemCode[i].getText());
	   	
	   	    	    if((String)qty[i].getValue()!="")
	   		    	{
	   		    		piObj.setQty(Double.parseDouble(qty[i].getValue().toString()));
	   	
	   		    	}else{
	   		    		piObj.setQty(1);
	   		    	}
	   	    	    
	   	    	    items.add(piObj);
	   	    	   
	   	    	}	    		    	
	   	    }
	    }
	   
	    	
	   	    
	    //attachment object
	    Map attachmentMap;
        HttpSession session;
        session = evt.getRequest().getSession();
        attachmentMap = (Map) session.getAttribute("attachmentMap");
        if(attachmentMap==null) {
            attachmentMap = new SequencedHashMap();
            //session.setAttribute("attachmentMap", attachmentMap);
        } 
       	   
        if(!attachmentMap.isEmpty() ){
        	String path ="/purchaseOrdering"+ "/" + currentUser.getId() + "/temp/";
        	for(Iterator i=attachmentMap.keySet().iterator(); i.hasNext(); ){
        		String attachmentID = UuidGenerator.getInstance().getUuid();
        		aObj = new AttachmentObject();
        		aObj.setAttachmentID(attachmentID);
        		 if(draftPPID!=null && !"".equals(draftPPID)){
	   	 	    	aObj.setPpID(draftPPID);
	   		    }else{
	   		    	aObj.setPpID(ppID);
	   		    }
        		
        		aObj.setUserID(currentUser.getId());
        		aObj.setPath(path);
        		aObj.setNewFileName(i.next().toString());
        		attachment.add(aObj);
        		//System.out.println(aObj.getPath());
        	}
        }
        
        //addAttachment(attachment);
        prObj.setAttachmentObject(attachment);
        prObj.setPurchaseItemsObject(items);
        //attachmentMap = null;
        
        session.removeAttribute("attachmentMap");
       
        if(module.addPurchseRequest(prObj)){
        	if(!status.equals("Draft")){
        		getUserID(prObj);
        	}
        	
        	return new Forward(FORWARD_SUBMIT);
        }
       
	    return null;
    }
    
    public void deleteItems(){
    	Collection item = new ArrayList();
    	if(mainDelete.isChecked()){
    		removeChildren();
    		init();
    		getField();
    	}else{
    		for(int i=0; i<size;i++){
        		if(!deleteBox[i].isChecked()){
        				item.add(itemCode[i].getText());
        		}
        	}
    		itemList(item);
    	}
    	
    }
    
    public void itemList(Collection item){
    	SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
    	init();
    	quantityComparison = item;
    	size = item.size();
    	itemCode = new Label[size];
    	itemDesc= new Label[size];
    	qty = new TextField[size];
    	unitOfMeasure = new Label[size];
    	deleteBox = new CheckBox[size];
    	 for(int count=0; count<size; count++){
    		 qty[count]= new TextField("qty"+ count);	
    		 qty[count].setSize("10");
    		 qty[count].setAlign("right");
    		 deleteBox[count]= new CheckBox("deleteBox"+ count);
    		 itemCode[count]= new Label("itemCode"+ count);	
    		 itemDesc[count]= new Label("itemDesc"+ count);	
    		 unitOfMeasure[count]= new Label("unitOfMeasure"+ count);	
	      }		
    	 
    	int index = 0;
    	ItemObject obj = new ItemObject();
    	for(Iterator i= item.iterator(); index<size && i.hasNext();){
    		String id = (String)i.next();
    		itemInfo = module.getSelectedItem(id);
    		for(Iterator itr= itemInfo.iterator(); itr.hasNext();){
    			obj = (ItemObject)itr.next();
    			itemCode[index].setText(obj.getItemCode());
    			itemDesc[index].setText(obj.getItemDesc());
    			unitOfMeasure[index].setText(obj.getUnitOfMeasure());
    			qty[index].setValue(obj.getMinQty());
    			addChild(itemCode[index]);
    			addChild(itemDesc[index]);
    			addChild(unitOfMeasure[index]);
				addChild(qty[index]);
				addChild(deleteBox[index]);
				index++;
    		}
    	}
    	getField();
    }
    public String generatePurchaseCode(){
    	
    	Collection usedCode = new ArrayList();
    	Calendar year = Calendar.getInstance();
    	int yr =  year.get(Calendar.YEAR);
    	String y = String.valueOf(yr).substring(2,4);
    	String pCode = "PR" + y;
    	
    	String purchaseCode  = new String();
    	String oldPCode = new String();
    	//System.out.println(dptCode);
    	PrePurchaseModule ppModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	usedCode = ppModule.getPurchaseCode();
    	
    	if(usedCode.size()== 0){
   	 	 purchaseCode = pCode + "A" + "0000";
    	}else{
    		for (Iterator i = usedCode.iterator(); i.hasNext();) {
          		HashMap map = (HashMap) i.next();
          		oldPCode =(String) map.get("purchaseCode");
      		} 
    		purchaseCode = generateNewCode(pCode,oldPCode);
    	}
    
    	return purchaseCode;
    }
    
    public String generateNewCode(String pCode, String oldPCode){
    	int noLength;
    	noLength = oldPCode.length()- 4;
    	char a = oldPCode.charAt(4);
    	int code;
    	if(Integer.parseInt(oldPCode.substring(noLength,oldPCode.length()))==9999){
    		code = 0000;
    		
    		a++;
    		pCode = pCode + a;
    	}else{
    		
    		pCode = pCode + a;
    		code = Integer.parseInt(oldPCode.substring(noLength,oldPCode.length()))+ 1;
    	}
    	
    	String runningNumber =  String.valueOf(code);
    	NumberFormat zeroFillingFormat = new DecimalFormat("0000");
    	runningNumber = zeroFillingFormat.format(new Long(code));
    	
    	return pCode + runningNumber;
    }
    
    public void getUserID(PurchaseRequestObject obj) {
    	
    	Collection userID = new ArrayList();
    	PrePurchaseModule ppModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	userID = ppModule.getHOD(Application.getInstance().getCurrentUser().getId());
    	sendNotification(userID, obj);
    } 
    
    public void sendNotification(Collection userID, PurchaseRequestObject obj){
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

            message.setSubject(app.getMessage("po.label.newPendingApproval", "New Request(s) Pending for Approval"));

            String notificationBody = user.getName() 
            	+ " has submitted a new request ("             	
            	+ "<a href='/ekms/purchaseordering/preApproval.jsp?id="
            	+ ppID
            	+ "'>" 
            	+ obj.getPurchaseCode() 
            	+ "</a> ) " 
            	+ "and pending for your approval. ";
            message.setBody(notificationBody);

            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());

            mm.sendMessage(smtpAccount, message, user.getId(), false);
        }
        catch (Exception e) {
            log.error("Error sending notification: ", e);
        }
    }
    
   
    
    //Getter and Setter--------------------------
	public AttachmentInfo getAttachment() {
		return attachment;
	}

	public void setAttachment(AttachmentInfo attachment) {
		this.attachment = attachment;
	}

	
	public DateField getNeededBy() {
		return neededBy;
	}

	public void setNeededBy(DatePopupField neededBy) {
		this.neededBy = neededBy;
	}
	public TextBox getReason() {
		return reason;
	}

	public void setReason(TextBox reason) {
		this.reason = reason;
	}
	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	
	public SelectBox getPriority() {
		return priority;
	}


	public void setPriority(SelectBox priority) {
		this.priority = priority;
	}


	public Button getDelete() {
		return delete;
	}


	public void setDelete(Button delete) {
		this.delete = delete;
	}


	public CheckBox[] getDeleteBox() {
		return deleteBox;
	}


	public void setDeleteBox(CheckBox[] deleteBox) {
		this.deleteBox = deleteBox;
	}

	public TextField[] getQty() {
		return qty;
	}


	public void setQty(TextField[] qty) {
		this.qty = qty;
	}


	public Label[] getItemCode() {
		return itemCode;
	}


	public void setItemCode(Label[] itemCode) {
		this.itemCode = itemCode;
	}


	public Label[] getItemDesc() {
		return itemDesc;
	}


	public void setItemDesc(Label[] itemDesc) {
		this.itemDesc = itemDesc;
	}


	public Label[] getUnitOfMeasure() {
		return unitOfMeasure;
	}


	public void setUnitOfMeasure(Label[] unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}


	public CheckBox getMainDelete() {
		return mainDelete;
	}


	public void setMainDelete(CheckBox mainDelete) {
		this.mainDelete = mainDelete;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public Collection getItemInfo() {
		return itemInfo;
	}


	public void setItemInfo(Collection itemInfo) {
		this.itemInfo = itemInfo;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public String getPpID() {
		return ppID;
	}


	public void setPpID(String ppID) {
		this.ppID = ppID;
	}


	public Button getDraft() {
		return draft;
	}


	public void setDraft(Button draft) {
		this.draft = draft;
	}


	public String getDraftPPID() {
		return draftPPID;
	}


	public void setDraftPPID(String draftPPID) {
		this.draftPPID = draftPPID;
	}


	public Button getCancel() {
		return cancel;
	}


	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}


	public String getStrReason() {
		return strReason;
	}


	public void setStrReason(String strReason) {
		this.strReason = strReason;
	}


	public Button getAddItem() {
		return addItem;
	}


	public void setAddItem(Button addItem) {
		this.addItem = addItem;
	}


	
}
