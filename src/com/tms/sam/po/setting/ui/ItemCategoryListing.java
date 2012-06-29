package com.tms.sam.po.setting.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.setting.model.SettingModule;

public class ItemCategoryListing extends Table{
	 	public void init() {
	        setWidth("100%");
	        setModel(new ItemCategoryListingTableModel());
	    }
	    
	    public void onRequest(Event evt) {
	        init();
	    }
	    
	    public class ItemCategoryListingTableModel extends TableModel {
	        public ItemCategoryListingTableModel() {
	        	Application app = Application.getInstance();
	        	
	        	TableColumn item = new TableColumn("category", app.getMessage("po.menu.itemCategory", "Item Category"));
	        	item.setUrlParam("categoryID");
	            addColumn(item);
	            
	            addFilter(new TableFilter("name"));
	            
	            SelectBox searchCol = new SelectBox("searchCol");
	            searchCol.setMultiple(false);
	            Map searchColMap = new SequencedHashMap();
	            searchColMap.put("category", app.getMessage("po.menu.itemCategory","Item Category"));
	            searchCol.setOptionMap(searchColMap);
	            
	            TableFilter searchColFilter = new TableFilter("searchColFilter");
	            searchColFilter.setWidget(searchCol);
	            addFilter(searchColFilter);
	            
	            addAction(new TableAction("add", app.getMessage("po.label.add", "Add"), ""));
	            addAction(new TableAction("delete", app.getMessage("po.label.delete", "Delete"), app.getMessage("po.message.deleteConfirm", "Confirm to delete?")));
	        }
	        
	        public Collection getTableRows() {
	        	 String name = (String) getFilterValue("name");
	             SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
	             List cols = (List) searchCol.getValue();
	             String searchColValue = "category";
	             if (cols.size() > 0) {
	                 searchColValue = (String) cols.get(0);
	             }
	             
	 			Application app = Application.getInstance();
	 			SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	 	    	
	 			return module.getCategory(name, searchColValue, getSort(), isDesc(), getStart(), getRows());
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
	           
	            SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	            return module.countCategory(name, searchColValue);
	        }
	        
	        public Forward processAction(Event evt, String action, String[] selectedKeys) {
	        	 Application app = Application.getInstance();
		         SettingModule module = (SettingModule) app.getModule(SettingModule.class);
		          
		         if ("delete".equals(action)) {
		            for (int i=0; i<selectedKeys.length; i++) {
		               module.deleteCategory(selectedKeys[i]);
		            }
		         }
	            
	            if ("add".equals(action)) {
	               return new Forward("Add");
	            }
	            return null;
	 		}
	        
	        public String getTableRowKey() {
	            return "categoryID";
	        }
	    }
}
