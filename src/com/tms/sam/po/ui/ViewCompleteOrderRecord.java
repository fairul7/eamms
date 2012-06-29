package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.stdui.Hidden;
import kacang.ui.Event;
import kacang.ui.Widget;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseOrderModule;
import com.tms.sam.po.model.PurchaseOrderObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;
import com.tms.sam.po.setting.model.ItemObject;

public class ViewCompleteOrderRecord extends Widget{
	private String purchaseCode="";
	private String dateRequested="";
	private String[] itemCode;
	private String[] itemDesc;
	private String[] qty;
	private Hidden[] hide;
	private String[] unitOfMeasure;
	private String[] unitPrice;
	private String txtSupplier;
	private String txtCompany;
	private String txtTelephone;
	private String txtDateSent;
	private String txtDateReceived;
	private String neededBy="";
	private String status="";
	private String txtQuotation;
	private String currency;
	private String quotationAttachment;
	private String txtMinBudget;
	private Map attachmentMap;
	
	private String deliveryOrderNo;
	private String invoiceNo;
	private String dateDelivered;
	private String invoiceDate;
	private String paymentTerm;
	
	private Collection txtAttachment;
	private String supplierID;
	private String stringCount;
	private int count;
	private String ppID;
	
	private PurchaseOrderObject po;
	private PurchaseOrderObject closePO;
	private int size = 0;
	

	private boolean paid;
	private String referenceNo;
	private String beneficiary;
	private String amount;
	private String datePaid;
	private String paymentType;

	
	public void onRequest(Event evt) {
		
		  SupplierObject oneRequest = null;
		  HttpSession session;
	      session = evt.getRequest().getSession();
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
				  
			  }
			  
			  evt.getRequest().getSession().setAttribute("supplierID", oneRequest.getSupplierID().toString());
			 
			  if(oneRequest.getTotalQuotation()!=0){
				  txtMinBudget= String.valueOf(oneRequest.getTotalQuotation());
			  }else{
				  txtMinBudget="---";
			  }
			 
			 
			  if(oneRequest.getDateReceived()!=null){
				  txtDateReceived = dmyDateFmt.format(oneRequest.getDateReceived());
			  }else{
				  txtDateReceived="---";
			  }
			  
			  currency=oneRequest.getCurrency();
			  
			  if(oneRequest.getLastKnownSuppName()!=null){
				  txtSupplier=oneRequest.getLastKnownSuppName();
			  }else{
				  txtSupplier="---";
			  }
			  
			  if(oneRequest.getLastKnownCompany()!=null){
				  txtCompany=oneRequest.getLastKnownCompany();
			  }else{
				  txtCompany="---";
			  }
			  
			  if(oneRequest.getLastKnownTelephone()!=null){
				  txtTelephone=oneRequest.getLastKnownTelephone();
			  }else{
				  txtTelephone="---";
			  }
			  
			 
			  if(oneRequest.getDateSent()!=null){
				  txtDateSent = dmyDateFmt.format(oneRequest.getDateSent());
			  }else{
				  txtDateSent="---";
			  }
			  
			  txtQuotation=oneRequest.getQuotationDetails();
			
			  po=null;
			  po = poModule.getPurchaseOrder(ppID, supplierID, count);
			  if(po!=null){
				
				  deliveryOrderNo =po.getDeliveryOrderNo();
				  invoiceNo =po.getInvoiceNo();
				  invoiceDate = dmyDateFmt.format(po.getInvoiceDate());
				  dateDelivered =dmyDateFmt.format(po.getDateDelivered());
				  paymentTerm =String.valueOf(po.getTerms());
				
			  }
			  
			  closePO = poModule.getClosedPurchaseOrder(ppID, supplierID, count);
			
			 if(closePO!=null){
					  referenceNo =closePO.getReferenceNo();
					  beneficiary =closePO.getBeneficiary();
					  paymentType = closePO.getTypeOfPayment();
					  datePaid =dmyDateFmt.format(closePO.getDatePaid());
					  amount =String.valueOf(closePO.getAmount());
					  paid=closePO.isPaid();
				  }
			 
		  }	  
		  
		  session.removeAttribute("attachmentMap");
	}

	@Override
	public String getDefaultTemplate() {
		return "po/viewCompleteOrder";
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDateDelivered() {
		return dateDelivered;
	}

	public void setDateDelivered(String dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	public String getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(String datePaid) {
		this.datePaid = datePaid;
	}

	public String getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(String dateRequested) {
		this.dateRequested = dateRequested;
	}

	public String getDeliveryOrderNo() {
		return deliveryOrderNo;
	}

	public void setDeliveryOrderNo(String deliveryOrderNo) {
		this.deliveryOrderNo = deliveryOrderNo;
	}

	public Hidden[] getHide() {
		return hide;
	}

	public void setHide(Hidden[] hide) {
		this.hide = hide;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getNeededBy() {
		return neededBy;
	}

	public void setNeededBy(String neededBy) {
		this.neededBy = neededBy;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public PurchaseOrderObject getPo() {
		return po;
	}

	public void setPo(PurchaseOrderObject po) {
		this.po = po;
	}

	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public String[] getQty() {
		return qty;
	}

	public void setQty(String[] qty) {
		this.qty = qty;
	}

	public String getQuotationAttachment() {
		return quotationAttachment;
	}

	public void setQuotationAttachment(String quotationAttachment) {
		this.quotationAttachment = quotationAttachment;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStringCount() {
		return stringCount;
	}

	public void setStringCount(String stringCount) {
		this.stringCount = stringCount;
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

	public String getTxtCompany() {
		return txtCompany;
	}

	public void setTxtCompany(String txtCompany) {
		this.txtCompany = txtCompany;
	}

	public String getTxtDateReceived() {
		return txtDateReceived;
	}

	public void setTxtDateReceived(String txtDateReceived) {
		this.txtDateReceived = txtDateReceived;
	}

	public String getTxtDateSent() {
		return txtDateSent;
	}

	public void setTxtDateSent(String txtDateSent) {
		this.txtDateSent = txtDateSent;
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

	public String getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(String txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public String getTxtTelephone() {
		return txtTelephone;
	}

	public void setTxtTelephone(String txtTelephone) {
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

	public PurchaseOrderObject getClosePO() {
		return closePO;
	}

	public void setClosePO(PurchaseOrderObject closePO) {
		this.closePO = closePO;
	}
	
}
