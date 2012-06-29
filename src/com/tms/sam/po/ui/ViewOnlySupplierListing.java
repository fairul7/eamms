package com.tms.sam.po.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;

import com.tms.sam.po.model.SupplierModule;

public class ViewOnlySupplierListing extends Table{
	protected String ppID = "";
	protected String status;
	int index=0; 
	String tableName = "";
	public static final String FORWARD_ADD = "add";	
	public ViewOnlySupplierListing(String name) {
		super.setName(name);
	}
	
	public String getName(){
		return super.getName();
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void init(){
		setWidth("100%");
		index = Integer.parseInt(getName().substring(getName().length()-1))+1;
		tableName = getName().substring(0,getName().length()-1);
		status =Application.getInstance().getThreadRequest().getParameter("status");
		setModel(new ViewOnlySupplierListingModel());

	}
	
		public class ViewOnlySupplierListingModel extends TableModel{
			public ViewOnlySupplierListingModel(){
				 Application app = Application.getInstance();
		        	
		         TableColumn idCol = new TableColumn("lastKnownCompany", app.getMessage("supplier.label.company","Company"));
		         idCol.setUrlParam("supplierID");
		         if(tableName.equals("approvedSupplier")){
		        	 if(status.equals("po") || status.equals("PO")){
		        		 idCol.setUrl("purchaseOrder.jsp?count="+getIndex()+"&ppID="+ppID);
		        	 }else if(status.equals("nonPO")){
		        		 idCol.setUrl("viewCompleteOrder.jsp?count="+getIndex()+"&ppID="+ppID);
		        	 }
		        	 
		 		 }else if(tableName.equals("rejectedSupplier")){
		 			 idCol.setUrl("viewOnlySupplierInfo.jsp?count="+getIndex()+"&ppID="+ppID);
		 		 }
		        
		         addColumn(idCol);
		            
		         addColumn(new TableColumn("lastKnownSuppName", app.getMessage("supplier.label.supp","Supplier")));
		         addColumn(new TableColumn("lastKnownTelephone", app.getMessage("supplier.label.telephone", "Contact No")));
		         addColumn(new TableColumn("currencyUsed", app.getMessage("po.menu.currency","Currency")));
		         addColumn(new TableColumn("totalQuotation", app.getMessage("supplier.label.minBudget","Total Quotation")));
		            
			}
			
			public Collection getTableRows() {
				
				Application app = Application.getInstance();
				SupplierModule module = (SupplierModule) app.getModule(SupplierModule.class);
				return module.getApprovedSupplier(ppID, index, tableName, getSort(), isDesc(), getStart(), getRows());
			}
			
			public int getTotalRowCount() {
				Application app = Application.getInstance();
	          
	            SupplierModule module = (SupplierModule) app.getModule(SupplierModule.class);
	        
	            return module.countListing(ppID);
			}
			
			public String getTableRowKey() {
			    return "supplierID";
			}
			
			
		}
		
		public String getPpID() {
			return ppID;
		}
		public void setPpID(String ppID) {
			this.ppID = ppID;
		}
			
}
