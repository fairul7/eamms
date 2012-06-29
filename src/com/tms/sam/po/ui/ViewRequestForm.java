package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseItemObject;
import com.tms.sam.po.model.PurchaseRequestObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.setting.model.ItemObject;

public class ViewRequestForm extends Form{
	
	private Label remarks;
	private Label txtrequester;
	private Label txtDpt;
	private Label txtDate;
	private Label txtPReason;
	private Label txtNeededBy;
	private Label txtStatus;
	private Label txtRemarks;
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
	 	 
	 	 cancel = new Button("cancel", app.getMessage("po.label.cancel", "Cancel")); 
	 	
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
	 	 
	}
	 
	public void onRequest(Event event){
		  init();
		  int size;
		  PurchaseRequestObject oneRequest = null;
		  String reason = "";
		  Collection items = null;
		  if(! "".equals(ppID)) {
			  Application app = Application.getInstance();
			  PrePurchaseModule module = (PrePurchaseModule)app.getModule(PrePurchaseModule.class);
			 
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
			  
			  if(reason!=null && !reason.equals("") )
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
			 
			 			 
			  addChild(txtPReason);
			  addChild(txtNeededBy);
			  addChild(txtStatus);
			  addChild(remarks);
			  addChild(txtRemarks);
			  addChild(cancel);
		  }
		  
	}
	  
	public String getTemplate() {
		return "po/request";
    }

	// === [ getters/setters ] =================================================
    public Forward onValidate(Event evt){
    	
	    return new Forward(FORWARD_BACK);
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
	
}
