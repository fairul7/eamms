package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
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
import com.tms.sam.po.model.PurchaseOrderModule;
import com.tms.sam.po.model.PurchaseOrderObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;
import com.tms.sam.po.permission.model.PermissionModel;
import com.tms.sam.po.setting.model.ItemObject;

public class PurchaseOrder extends Form{
	
	private String dateRequested="";
	private String purchaseCode="";
	private String neededBy="";
	private String status="";
	private String delivery;
	private String invoiceN;
	private String invoiceD;
	private String delivered;
	private String paymentTerm;
	private TextField deliveryOrderNo;
	private TextField invoiceNo;
	private DatePopupField dateDelivered;
	private DatePopupField invoiceDate;
	private TextField terms;
	private Panel btnPanel;
	private Label txtSupplier;
	private Label txtCompany;
	private Label txtTelephone;
	private Label txtDateSent;
	private Label txtDateReceived;
	private Label txtQuotation;
	private Label currency;
	private Label quotationAttachment;
	private Label txtMinBudget;
	private Map attachmentMap;

	private Button poIssuance;
	private Button cancel;
	private Collection txtAttachment;
	private String supplierID;
	private String stringCount;
	private int count;
	private String ppID;
	private String[] itemCode;
	private String[] itemDesc;
	private String[] qty;
	private Hidden[] hide;
	private String[] unitOfMeasure;
	private String[] unitPrice;
	private PurchaseOrderObject po;
	private int size = 0;
	protected static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_BACK = "back";	;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
	    setColumns(2);
	    
	    Application app = Application.getInstance();
	    
	    deliveryOrderNo = new TextField("deliveryOrderNo");
	    deliveryOrderNo.setSize("10");
	    deliveryOrderNo.setMaxlength("10");
	    deliveryOrderNo.addChild(new ValidatorNotEmpty("deliveryNo"));
	    
	    invoiceNo = new TextField("invoiceNo");
	    invoiceNo.setSize("10");
	    invoiceNo.setMaxlength("10");
	    invoiceNo.addChild(new ValidatorNotEmpty("iNo"));
	    
	    dateDelivered = new DatePopupField("dateDelivered");
	    
	    terms = new TextField("terms");
	    terms.setSize("10");
	    terms.setMaxlength("10");
	    terms.addChild(new ValidatorIsNumeric("vTerms"));
	    
	    invoiceDate = new DatePopupField("invoiceDate"); 
	    
	    btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
        
        poIssuance = new Button("poIssuance", app.getMessage("po.label.poIssuance", "PO Fulfillment"));
        btnPanel.addChild(poIssuance);
        
        cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("po.label.cancel"));
        btnPanel.addChild(cancel);
        
        txtSupplier = new Label("txtSupplier", ""); 
		txtCompany = new Label("txtCompany", "");
		txtTelephone = new Label("txtTelephone", "");
		txtDateSent = new Label("txtDateSent", ""); 
		txtDateReceived = new Label("txtDateReceived", "");
		txtQuotation = new Label("txtQuotation", "");
		currency = new Label("currency", "");
		quotationAttachment = new Label("quotationAttachment", app.getMessage("supplier.label.attachment", "Quotation Attachment"));
		txtMinBudget = new Label("txtMinBudget", "");
		 
		 

        addChild(txtSupplier);
        addChild(txtCompany);
        addChild(txtTelephone);
        addChild(txtDateSent);
        addChild(txtDateReceived);
        addChild(txtQuotation);
        addChild(quotationAttachment);
        addChild(txtMinBudget);
        addChild(currency);
        addChild(deliveryOrderNo);
        addChild(invoiceDate);
        addChild(terms);
        addChild(invoiceNo);
        addChild(dateDelivered);
        addChild(btnPanel);
	}


	public void onRequest(Event event){
			  initForm();
			  
			  SupplierObject oneRequest = null;
			  HttpSession session;
		      session = event.getRequest().getSession();
		      Application app = Application.getInstance();
		      DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDateLong"));
			  attachmentMap = new SequencedHashMap();
			  txtAttachment = null;
			  SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
			  count = Integer.parseInt(stringCount);
			  if(! "".equals(supplierID)) {
				  Collection supplierItems = module.getSupplierItems(supplierID, ppID, count);
				  PrePurchaseModule ppModule = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
				  PurchaseOrderModule poModule = (PurchaseOrderModule)Application.getInstance().getModule(PurchaseOrderModule.class);
				  PurchaseRequestObject request = ppModule.singleRequest(ppID);
				  
				  if(request.getNeededBy()!=null){
					  neededBy= dmyDateFmt.format(request.getNeededBy());
				  }else{
					  neededBy="---";
				  }
				  
				  if(request.getPurchaseCode()!=null){
					  purchaseCode= request.getPurchaseCode();
				  }else{
					  purchaseCode="---";
				  }
				  
				  if(request.getDateCreated()!=null){
					  dateRequested= dmyDateFmt.format(request.getDateCreated());
				  }else{
					  dateRequested="---";
				  }
				  
				  if(request.getStatus()!=null){
					  status = request.getStatus();
				  }
				  
				  if(supplierItems.size()!=0){
					  size = supplierItems.size();
					  itemCode = new String[size];
					  itemDesc = new String[size];
					  qty = new String[size];
					  hide = new Hidden[size];
					  unitOfMeasure = new String[size];
					  unitPrice =new String[size];
					  
					  
					  for(int count=0; count<size; count++){
				    	  itemCode[count]= new String("itemCode" + count);
				    	  itemDesc[count]= new String("itemDesc" + count);
				    	  unitOfMeasure[count]= new String("unitOfMeasure" + count);
				    	  qty[count]= new String("qty" + count);
				    	  hide[count] = new Hidden("hide" + count);
				    	  unitPrice[count]= new String("unitPrice" + count);
				      }		
					  
					  int index =0;
					  for(Iterator itr= supplierItems.iterator(); index<size && itr.hasNext(); ) {
						  ItemObject obj = (ItemObject)itr.next();
						  itemCode[index]=obj.getItemCode();
						  itemDesc[index]=obj.getItemDesc();
						  qty[index]=Double.toString(obj.getMinQty());
						  unitPrice[index]=Double.toString(obj.getUnitPrice());
						  unitOfMeasure[index]=obj.getUnitOfMeasure();
						  hide[index].setValue(obj.getMinQty());
						  addChild(hide[index]);
						  index++;
					  }
				  }
				  
				  
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
					  quotationAttachment.setHidden(true);
				  }
				  
				  event.getRequest().getSession().setAttribute("supplierID", oneRequest.getSupplierID().toString());
				 
				  if(oneRequest.getTotalQuotation()!=0){
					  txtMinBudget.setText(String.valueOf(oneRequest.getTotalQuotation()));
				  }else{
					  txtMinBudget.setText("---");
				  }
				 
				 
				  if(oneRequest.getDateReceived()!=null){
					     txtDateReceived.setText(dmyDateFmt.format(oneRequest.getDateReceived()));
				  }else{
					     txtDateReceived.setText("---");
				  }
				  
				  currency.setText(oneRequest.getCurrency());
				  txtSupplier.setText(oneRequest.getLastKnownSuppName());
				  txtCompany.setText(oneRequest.getLastKnownCompany());
				  txtTelephone.setText(oneRequest.getLastKnownTelephone());
				  
				  if(oneRequest.getDateSent()!=null){
					  txtDateSent.setText(dmyDateFmt.format(oneRequest.getDateSent()));
				  }else{
					  txtDateSent.setText("---");
				  }
				  
				  txtQuotation.setText(oneRequest.getQuotationDetails());
				  delivery ="";
				  invoiceN ="";
				  invoiceD = "";
				  delivered ="";
				  paymentTerm ="";
				  po=null;
				  po = poModule.getPurchaseOrder(ppID, supplierID, count);
				  if(po!=null){
					  deliveryOrderNo.setHidden(true);
					  invoiceNo.setHidden(true);
					  invoiceDate.setHidden(true);
					  terms.setHidden(true);
					  dateDelivered.setHidden(true);
					  
					  delivery =po.getDeliveryOrderNo();
					  invoiceN =po.getInvoiceNo();
					  invoiceD = dmyDateFmt.format(po.getInvoiceDate());
					  delivered =dmyDateFmt.format(po.getDateDelivered());
					  paymentTerm =String.valueOf(po.getTerms());
					  
				  }
				 
			  }	  
			  
			  session.removeAttribute("attachmentMap");
	}

    
    public Forward onValidate(Event event) {
    	String button = findButtonClicked(event);
		button = (button == null)? "" : button;
	    
        if (button.endsWith("poIssuance")) {
            // process upload
        	issuePO();
        	return new Forward(FORWARD_BACK);
        }
	    return new Forward(FORWARD_BACK);
    }
    
    public void issuePO(){
    	PurchaseOrderObject obj = new PurchaseOrderObject();
    	obj.setPpID(ppID);
    	obj.setSupplierID(supplierID);
    	obj.setCount(count);
    	obj.setPoID(UuidGenerator.getInstance().getUuid());
    	obj.setPoCode(generatePurchaseOrderCode());
    	obj.setCreatedBy(Application.getInstance().getCurrentUser().getId());
    	obj.setDeliveryOrderNo(deliveryOrderNo.getValue().toString());
    	obj.setInvoiceNo(invoiceNo.getValue().toString());
    	obj.setInvoiceDate(invoiceDate.getDate());
    	obj.setTerms(Integer.parseInt(terms.getValue().toString()));
    	obj.setDateDelivered(dateDelivered.getDate());
    	obj.setDateCreated(new Date());
    	obj.setLastUpdatedBy(Application.getInstance().getCurrentUser().getId());
    	obj.setLastUpdatedDate(new Date());
    	PurchaseRequestObject prObj = new PurchaseRequestObject();
    	prObj.setPpID(ppID);
    	prObj.setStatus("PO Fulfilled");
    	prObj.setRank(11);
    	PurchaseOrderModule module = (PurchaseOrderModule)Application.getInstance().getModule(PurchaseOrderModule.class);
    	if (module.insertPurchaseOrder(obj, prObj)){
    		PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
    		Collection userID = new ArrayList();
    		userID = model.groupOfUser("po.permission.manageQuotation");
    	
    		sendNotification(userID, purchaseCode);
    	}
    	
    }
    

    public void sendNotification(Collection id, String purchaseCode){
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
                     app.getMessage("po.label.newPendingClosing", "New Request(s) Pending for Closing"));
            
             String notificationBody = "<p>A purchase request <i>" 
            	 + purchaseCode 
            	 + "</i> is pending for closing. </p>" 
            	 + "<p><b>View the " 
            	 + "<a href='/ekms/purchaseordering/viewRequestPO.jsp?id="
	             + ppID
	             + "&status=po'>request</a></b></p>" ;
              message.setBody(notificationBody);

             message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
             message.setDate(new Date());

             mm.sendMessage(smtpAccount, message, user.getId(), false);
         }
         catch (Exception e) {
             log.error("Error sending notification: ", e);
         }
 	}
    
    public String generatePurchaseOrderCode(){
    	
    	Collection usedCode = new ArrayList();
    	String pCode = "PO" ;
    	
    	String purchaseCode  = new String();
    	String oldPCode = new String();
    	//System.out.println(dptCode);
    	PrePurchaseModule ppModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
    	usedCode = ppModule.getPurchaseOrderCode();
    	
    	if(usedCode.size()== 0){
   	 	 purchaseCode = pCode + "00000";
    	}else{
    		for (Iterator i = usedCode.iterator(); i.hasNext();) {
          		HashMap map = (HashMap) i.next();
          		oldPCode =(String) map.get("poCode");
      		} 
    		purchaseCode = generateNewCode(pCode,oldPCode);
    	}
    
    	return purchaseCode;
    }
    
    public String generateNewCode(String pCode, String oldPCode){
    	int noLength;
    	noLength = oldPCode.length()- 5;
    	//System.out.println(oldPCode.substring(noLength,oldPCode.length()));
    	int code;
    	code = Integer.parseInt(oldPCode.substring(noLength,oldPCode.length()))+ 1;
      
    	//System.out.println(code);
    	String runningNumber =  String.valueOf(code);
    	NumberFormat zeroFillingFormat = new DecimalFormat("00000");
    	runningNumber = zeroFillingFormat.format(new Long(code));
    	
    	return pCode + runningNumber;
    }
    
    
    public String getTemplate() {
		return "po/purchaseOrder";
	}

    
	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public DatePopupField getDateDelivered() {
		return dateDelivered;
	}

	public void setDateDelivered(DatePopupField dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	public String getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(String dateRequested) {
		this.dateRequested = dateRequested;
	}

	public TextField getDeliveryOrderNo() {
		return deliveryOrderNo;
	}

	public void setDeliveryOrderNo(TextField deliveryOrderNo) {
		this.deliveryOrderNo = deliveryOrderNo;
	}

	public TextField getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(TextField invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getNeededBy() {
		return neededBy;
	}

	public void setNeededBy(String neededBy) {
		this.neededBy = neededBy;
	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public DatePopupField getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(DatePopupField invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public TextField getTerms() {
		return terms;
	}

	public void setTerms(TextField terms) {
		this.terms = terms;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Label getCurrency() {
		return currency;
	}

	public void setCurrency(Label currency) {
		this.currency = currency;
	}

	public Hidden[] getHide() {
		return hide;
	}

	public void setHide(Hidden[] hide) {
		this.hide = hide;
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

	public String[] getQty() {
		return qty;
	}

	public void setQty(String[] qty) {
		this.qty = qty;
	}

	public Label getQuotationAttachment() {
		return quotationAttachment;
	}

	public void setQuotationAttachment(Label quotationAttachment) {
		this.quotationAttachment = quotationAttachment;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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

	public Label getTxtMinBudget() {
		return txtMinBudget;
	}

	public void setTxtMinBudget(Label txtMinBudget) {
		this.txtMinBudget = txtMinBudget;
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

	public String[] getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String[] unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String[] getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String[] unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public Button getPoIssuance() {
		return poIssuance;
	}

	public void setPoIssuance(Button poIssuance) {
		this.poIssuance = poIssuance;
	}

	
	public String getDelivered() {
		return delivered;
	}

	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getInvoiceD() {
		return invoiceD;
	}

	public void setInvoiceD(String invoiceD) {
		this.invoiceD = invoiceD;
	}

	public String getInvoiceN() {
		return invoiceN;
	}

	public void setInvoiceN(String invoiceN) {
		this.invoiceN = invoiceN;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public PurchaseOrderObject getPo() {
		return po;
	}

	public void setPo(PurchaseOrderObject po) {
		this.po = po;
	}

	public String getStringCount() {
		return stringCount;
	}

	public void setStringCount(String stringCount) {
		this.stringCount = stringCount;
	}

	
}
