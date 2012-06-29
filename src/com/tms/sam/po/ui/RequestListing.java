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


public class RequestListing  extends Table{
	public void init(){
		setWidth("100%");
		setModel(new RequestListingModel());
	}
	
	public class RequestListingModel extends TableModel{
		public RequestListingModel(){
			Application app = Application.getInstance();
        	
            TableColumn idCol = new TableColumn("purchaseCode", app.getMessage("myRequest.label.id","ID"));
            idCol.setUrlParam("ppID");
            addColumn(idCol);
            
            addColumn(new TableColumn("requester", app.getMessage("po.label.reqester","Requester")));
            
            TableColumn items = new TableColumn("delimitedItems", app.getMessage("myRequest.label.itemRequested", "Item Requested"));
            items.setSortable(false);
            addColumn(items);
            
            TableColumn neededBy = new TableColumn("neededBy", app.getMessage("po.label.needed","Needed By"));
            TableFormat neededByFormat = new TableDateFormat(app.getProperty("globalDateLong"));
            neededBy.setFormat(neededByFormat);
            addColumn(neededBy);
            
            
            addColumn(new TableColumn("status", app.getMessage("myRequest.label.status","Status")));
            
            TableColumn dateCreated = new TableColumn("dateCreated", "Date/Time Requested");
            TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
            dateCreated.setFormat(dateCreatedFormat);
            addColumn(dateCreated);
                                         
            addFilter(new TableFilter("name"));
            
            SelectBox searchCol = new SelectBox("searchCol");
            searchCol.setMultiple(false);
            Map searchColMap = new SequencedHashMap();
            searchColMap.put("purchaseCode", app.getMessage("myRequest.label.id","ID"));
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
			PrePurchaseModule module = (PrePurchaseModule) app.getModule(PrePurchaseModule.class);
	    	
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
		    return "ppID";
		}
	}
}
