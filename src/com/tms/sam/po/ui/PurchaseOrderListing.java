package com.tms.sam.po.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.PrePurchaseModule;
import com.tms.sam.po.model.PurchaseOrderModule;

public class PurchaseOrderListing extends Table{
	public void init(){
		setWidth("100%");
		setMultipleSelect(false);
		setModel(new PurchaseOrderListingModel());
	
	}

	public class PurchaseOrderListingModel extends TableModel{
		public PurchaseOrderListingModel(){
			Application app = Application.getInstance();
			
	         TableColumn idCol = new TableColumn("poCode", app.getMessage("myRequest.label.id","ID"));
	         idCol.setUrlParam("ppID");
	         idCol.setUrl("closedPurchaseOrder.jsp");
	         addColumn(idCol);
	            
	         addColumn(new TableColumn("requester", app.getMessage("po.label.requester","Requester")));
	         addColumn(new TableColumn("delimitedItems", app.getMessage("myRequest.label.itemRequested ", "Item Requested")));
	         addColumn(new TableColumn("status", app.getMessage("myRequest.label.status","Status")));
	        
	         TableColumn dateCreated = new TableColumn("dateCreated", app.getMessage(" po.label.dateTimeIssued","Date/Time Issued/Closed"));
	         TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
	         dateCreated.setFormat(dateCreatedFormat);
	         addColumn(dateCreated);
	         
	         addFilter(new TableFilter("name"));
	           
	         SelectBox searchCol = new SelectBox("searchCol");
	         searchCol.setMultiple(false);
	         Map searchColMap = new SequencedHashMap();
	         searchColMap.put("id", app.getMessage("myRequest.label.id","ID"));
	         searchCol.setOptionMap(searchColMap);
	            
	         TableFilter searchColFilter = new TableFilter("searchColFilter");
	         searchColFilter.setWidget(searchCol);
	         addFilter(searchColFilter);
	        
		}
		
		public Collection getTableRows() {
			String name = (String) getFilterValue("name");
            SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
                searchColValue = (String) cols.get(0);
            }
            
			Application app = Application.getInstance();
			PurchaseOrderModule module = (PurchaseOrderModule)app.getModule(PurchaseOrderModule.class);
	    	
			return module.getRequestListing(name, searchColValue, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application app = Application.getInstance();
            String name = (String) getFilterValue("name");
            SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
                searchColValue = (String) cols.get(0);
            }
           
            PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
            
        
            return module.countRequestListing(name, searchColValue);
		}
		
		public String getTableRowKey() {
		    return "poID";
		}
	}
}
