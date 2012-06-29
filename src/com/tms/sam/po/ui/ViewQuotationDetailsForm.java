package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;
import com.tms.sam.po.setting.model.ItemObject;

public class ViewQuotationDetailsForm extends Form{

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
	private Button cancel;
	private Collection txtAttachment;
	private String supplierID = "";
	private int count = 0;
	private String ppID = "";
	private String[] itemCode;
	private String[] itemDesc;
	private String[] qty;
	private Hidden[] hide;
	private String[] unitOfMeasure;
	private String[] unitPrice;
	private int size = 0;
	public static final String FORWARD_BACK = "back";	
	
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
		 currency = new Label("currency", "");
		 quotationAttachment = new Label("quotationAttachment", app.getMessage("supplier.label.attachment", "Quotation Attachment"));
		 txtMinBudget = new Label("txtMinBudget", "");
		 
		 cancel = new Button("cancel", app.getMessage("po.label.cancel", "Cancel")); 

         addChild(txtSupplier);
         addChild(txtCompany);
         addChild(txtTelephone);
         addChild(txtDateSent);
         addChild(txtDateReceived);
         addChild(txtQuotation);
         addChild(quotationAttachment);
         addChild(txtMinBudget);
         addChild(currency);
         addChild(cancel);
	}
	
	public String getTemplate() {
		 return "po/viewQuotation";
	}
	
	public void onRequest(Event event){
		  init();
		
		  SupplierObject oneRequest = null;
		  HttpSession session;
	      session = event.getRequest().getSession();
	      Application app = Application.getInstance();
		  attachmentMap = new SequencedHashMap();
		  txtAttachment = null;
		  NumberFormat decimal = new DecimalFormat(".00");
		  SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
		  count = Integer.parseInt(event.getRequest().getParameter("count").toString());
		  if(! "".equals(supplierID)) {
			  Collection supplierItems = module.getSupplierItems(supplierID, ppID, count);		
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
					  unitPrice[index]=decimal.format(obj.getUnitPrice());
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
				  txtMinBudget.setText(decimal.format(oneRequest.getTotalQuotation()));
			  }else{
				  txtMinBudget.setText("---");
			  }
			  DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			 
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
		  }	  
		  
		  session.removeAttribute("attachmentMap");
	}
	
	public Forward onValidate(Event event){
		return new Forward(FORWARD_BACK);
	}

	// === [ getters/setters ] =================================================
	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Label getQuotationAttachment() {
		return quotationAttachment;
	}

	public void setQuotationAttachment(Label quotationAttachment) {
		this.quotationAttachment = quotationAttachment;
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

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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
	
}
