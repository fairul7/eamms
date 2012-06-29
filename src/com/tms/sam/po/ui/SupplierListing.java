package com.tms.sam.po.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.model.SupplierModule;


public class SupplierListing extends Table{
	protected String ppID = "";
	public static String FORWARD_ADD = "add";	
	public static String FORWARD_DELETE = "delete";
	private int index;
	public void init(){
		setWidth("100%");
		index = Integer.parseInt(getName().substring(getName().length()-1))+1;
		setSortable(false);
		setModel(new SupplierListingModel());
	}
	
	public SupplierListing(String name){
		super.setName(name);
	}
	
	public String getName(){
		return super.getName();
	}
	@Override
	public void onRequest(Event evt) {
		init();
		super.onRequest(evt);
	}
	
		public class SupplierListingModel extends TableModel{
			public SupplierListingModel(){
				 Application app = Application.getInstance();
		         TableColumn idCol = new TableColumn("lastKnownCompany", app.getMessage("supplier.label.company","Company"));
		         idCol.setUrlParam("supplierID");
		         idCol.setUrl("viewSupplier.jsp?count="+getIndex()+"&ppID="+ppID);
		         addColumn(idCol);
		         
		         addColumn(new TableColumn("lastKnownSuppName", app.getMessage("supplier.label.supp","Supplier")));
		         addColumn(new TableColumn("responded", app.getMessage("supplier.label.responded", "Responded")));
		         addColumn(new TableColumn("recommended", app.getMessage("supplier.label.recommend","Recommendation")));
		           
		         TableColumn totalRating = new TableColumn("totalRating", app.getMessage("evaluation.label.rating","Rating"));
		         totalRating.setFormat(new TableFormat() {
		                public String format(Object value) {
		                   if( value.toString().equals("4"))
		                    {
		                	    return "Excellent";
		                    }else if ( value.toString().equals("3")){
		                    	return "Good";
		                    }else if ( value.toString().equals("2")){
		                    	return "Fair";
		                    }else if ( value.toString().equals("1")){
		                    	return "Poor";
		                    }else{
		                    	return "None";
		                    }
		                    
		                }
		            });
	             addColumn(totalRating);
		       
		         
		         addAction(new TableAction("addSupplier", "Add Supplier", null)); 
		         addAction(new TableAction("delete", "Delete", "Confirm to delete?"));
		           
			}
			
			public Collection getTableRows() {
				
				Application app = Application.getInstance();
				SupplierModule module = (SupplierModule) app.getModule(SupplierModule.class);
				
				return module.getSupplier(ppID, index, getSort(), isDesc(), getStart(), getRows());
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
	            Application application = Application.getInstance();
	            SupplierModule module = (SupplierModule) application.getModule(SupplierModule.class);
	           
	            if ("delete".equals(action)) {
	                for (int i=0; i<selectedKeys.length; i++) {
	                  module.deleteSupplier(ppID, selectedKeys[i], index);
	                }
	                return new Forward(FORWARD_DELETE);
	            }
	            
	            if ("addSupplier".equals(action)) {
	            	FORWARD_ADD= String.valueOf(index);
	                return new Forward(FORWARD_ADD);
	            }
	            return null;
			}
		}
		
		public String getPpID() {
			return ppID;
		}
		public void setPpID(String ppID) {
			this.ppID = ppID;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
		@Override
		public String getDefaultTemplate() {
			// TODO Auto-generated method stub
			return "po/newTable";
		}
			
}
