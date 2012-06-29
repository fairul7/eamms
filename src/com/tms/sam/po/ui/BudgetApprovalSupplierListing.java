package com.tms.sam.po.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.model.BudgetObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;

public class BudgetApprovalSupplierListing extends Table {
	protected String ppID = "";
	BudgetObject obj = new BudgetObject();
	boolean loaded = false;
	private int index;
	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public BudgetApprovalSupplierListing(String name){
		super.setName(name);
	}
	
	public String getName(){
		return super.getName();
	}
	
	public void init(){
		setWidth("100%");
		index = Integer.parseInt(getName().substring(getName().length()-1))+1;
		setMultipleSelect(false);
		setModel(new BudgetApprovalSupplierListingModel());
	
	}
	
	public class BudgetApprovalSupplierListingModel extends TableModel{
		public BudgetApprovalSupplierListingModel(){
			 Application app = Application.getInstance();
			
	         TableColumn idCol = new TableColumn("lastKnownCompany", app.getMessage("supplier.label.company","Company"));
	         idCol.setUrlParam("supplierID");
	         idCol.setUrl("viewQuotation.jsp?count="+getIndex()+"&ppID="+ppID);
	         addColumn(idCol);
	            
	         addColumn(new TableColumn("lastKnownSuppName", app.getMessage("supplier.label.supp","Supplier")));
	         addColumn(new TableColumn("currencyUsed", app.getMessage("po.label.currency", "Currency")));
	         addColumn(new TableColumn("totalQuotation", app.getMessage("supplier.label.minBudget", "Minimum Budget")));
	         addColumn(new TableColumn("recommended", app.getMessage("supplier.label.recommend","Recommendation")));
	         addColumn(new TableColumn("approved", app.getMessage("po.label.approved","Approved")));  
	        
	         addAction(new TableAction("approval", app.getMessage("userRequest.label.approve", "Approve"), null));   
	        
		}
		
		public Collection getTableRows() {
			
			obj.setSupplierID("");
           
			Application app = Application.getInstance();
			SupplierModule module = (SupplierModule) app.getModule(SupplierModule.class);
			if(loaded == false){
				Application.getThreadRequest().setAttribute("status","No");
			}else{
				Application.getThreadRequest().setAttribute("status","Yes");
			}
			return module.viewQuatation(ppID, index, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application app = Application.getInstance();
           
            SupplierModule module = (SupplierModule) app.getModule(SupplierModule.class);
        
            return module.countListing(ppID);
		}
		
		public String getTableRowKey() {
		    return "supplierID";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			SupplierObject so = new SupplierObject();
			Application application = Application.getInstance();
			SupplierModule module = (SupplierModule) application.getModule(SupplierModule.class);
			if ("approval".equals(action)) {
				
                for (int i=0; i<selectedKeys.length; i++) {
                	Application.getThreadRequest().setAttribute("status","Yes");
    				loaded = true;
                	obj.setSupplierID(selectedKeys[i]);
                	so.setSupplierID(selectedKeys[i]);
 	                so.setApproved("No");
 	                so.setPpID(ppID);
 	                so.setCounting(index);
 	                module.disapprove(so);
 	               
                }
                so.setApproved("Yes");
                so.setCounting(index);
                so.setPpID(ppID);
   			    module.approvedSupplier(so); 
            }else{
            	loaded = false;
            	Application.getThreadRequest().setAttribute("status","No");
            }
			
            return null;
		}
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
