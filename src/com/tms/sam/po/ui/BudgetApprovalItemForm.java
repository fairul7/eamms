package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.setting.model.ItemObject;

public class BudgetApprovalItemForm extends Form{

	private Label txtDptCode;
	private Label txtRequester;
	private Label txtDpt;
	private Label txtDate;
	private Label attachment;
	private Label txtReason;
	private Label txtNeededBy;
	private Collection txtAttachment;
	private String[] itemCode;
	private String[] itemDesc;
	private double[] qty;
	private String[] supplierItemCode;
	private String[] supplierItemDesc;
	private double[] supplierItemQty;
	private String[] supplierItemMeasure;
	private String[] unitOfMeasure;
	private int[] supplierCount;
	private BudgetApprovalSupplierListing[] budgetSupplierListing;
	protected String ppID = "";
    private Map attachmentMap;
	
	public void init(){

		  setMethod("POST");
		  setColumns(2);
		  Application app = Application.getInstance();
		  txtDptCode = new Label("txtDptCode", ""); 
		  txtRequester = new Label("txtrequester", "");
		  txtDpt = new Label("txtDpt", "");
		  txtDate = new Label("txtDate", "");
		  txtNeededBy = new Label("txtNeededBy", "");
		  txtReason = new Label("txtReason", "");
		  attachment = new Label("attachment", app.getMessage("po.label.attachment", "Attachment"));

		  supplierItemCode = null;
		  supplierItemDesc = null;
		  supplierItemQty = null;
		  supplierItemMeasure = null;
		  supplierCount = null;
		  budgetSupplierListing = null;
		  
		  addChild(txtDptCode);
		  addChild(txtRequester);
		  addChild(txtDpt);
		  addChild(txtDate);
		  addChild(attachment);  
		  addChild(txtNeededBy); 
		  addChild(txtReason);
	}
	  
	public String getTemplate() {
		  return "po/budgetApprovalItemForm";
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
			
			  txtDptCode.setText(oneRequest.getPurchaseCode());
			  txtRequester.setText(oneRequest.getRequester());
			  txtDpt.setText(oneRequest.getDepartment());
			  
			  DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
			
			  if(oneRequest.getDateCreated() == null){
				  txtDate.setText("---");
			  }else{
				  txtDate.setText(dmyDateFmt.format(oneRequest.getDateCreated()));
			  }
			  
			  if(oneRequest.getNeededBy() == null){
				  txtNeededBy.setText("---");
			  }else{
				  txtNeededBy.setText(dmyDateFmt.format(oneRequest.getNeededBy()));
			  }
			  
			  txtReason.setText(oneRequest.getReason());
			
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
			  
			  Collection supplier =null;
			  SupplierModule suppModule= (SupplierModule) app.getModule(SupplierModule.class);
			  supplier = suppModule.checkAddedSupplier(ppID);
			  if(suppModule.countTable(ppID)!=""){
				  int count = Integer.parseInt(suppModule.countTable(ppID));
				  budgetSupplierListing = new BudgetApprovalSupplierListing[count];
				
				  for(int i=0; i<count; i++){
					  budgetSupplierListing[i]= new BudgetApprovalSupplierListing("budgetSupplierListing" + i);
					  budgetSupplierListing[i].setPpID(ppID);
					  budgetSupplierListing[i].init();
					 
			    	  addChild(budgetSupplierListing[i]);
			      }		
			  }
		  }	  
	}
	  
	// === [ getters/setters ] =================================================
	public Forward onValidate(Event event){
		  return null;
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
	
	public String getPpID() {
		return ppID;
	}
	public void setPpID(String ppID) {
		this.ppID = ppID;
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
	public Label getTxtReason() {
		return txtReason;
	}
	public void setTxtReason(Label txtReason) {
		this.txtReason = txtReason;
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

	public int[] getSupplierCount() {
		return supplierCount;
	}

	public void setSupplierCount(int[] supplierCount) {
		this.supplierCount = supplierCount;
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

	public BudgetApprovalSupplierListing[] getBudgetSupplierListing() {
		return budgetSupplierListing;
	}

	public void setBudgetSupplierListing(
			BudgetApprovalSupplierListing[] budgetSupplierListing) {
		this.budgetSupplierListing = budgetSupplierListing;
	}

}
