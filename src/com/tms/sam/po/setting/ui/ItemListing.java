package com.tms.sam.po.setting.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.setting.model.CategoryObject;
import com.tms.sam.po.setting.model.SettingModule;

public class ItemListing extends Table{
 	public void init() {
        setWidth("100%");
        setModel(new ItemListingTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class ItemListingTableModel extends TableModel {
        public ItemListingTableModel() {
        	Application app = Application.getInstance();
        	
        	TableColumn itemCode = new TableColumn("itemCode", app.getMessage("po.label.itemCode", "Item Code"));
        	itemCode.setUrlParam("itemCode");
            addColumn(itemCode);
            
        	TableColumn itemDesc = new TableColumn("itemDesc", app.getMessage("po.label.itemDesc", "Item Description"));
            addColumn(itemDesc);
            
        	TableColumn category = new TableColumn("category", app.getMessage("po.menu.itemCategory", "Item Category"));
            addColumn(category);
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableColumn approved = new TableColumn("approved", app.getMessage("myRequest.label.status", "Status"));
            approved.setFormat(new TableBooleanFormat("<img src=\"" +
                    contextPath + "/common/table/booleantrue.gif\">", ""));
           
            addColumn(approved);
            
            addFilter(new TableFilter("name"));
            
       /*     SelectBox searchCol = new SelectBox("searchCol");
            searchCol.setMultiple(false);
            Map searchColMap = new SequencedHashMap();
            searchColMap.put("itemCode", app.getMessage("po.label.itemCode","Item Code"));
            searchColMap.put("itemDesc", app.getMessage("po.label.itemDesc","Item Description"));
            searchCol.setOptionMap(searchColMap);
            
            TableFilter searchColFilter = new TableFilter("searchColFilter");
            searchColFilter.setWidget(searchCol);
            addFilter(searchColFilter);*/
            
			SelectBox sbCategory = new SelectBox("sbCategory");
			sbCategory.setOptionMap(getCategoryOptionsMap());
			TableFilter categoryFilter = new TableFilter("categoryFilter");
			categoryFilter.setWidget(sbCategory);
			addFilter(categoryFilter);
			
            addAction(new TableAction("add", app.getMessage("po.label.add", "Add"), ""));
            addAction(new TableAction("delete", app.getMessage("po.label.delete", "Delete"), app.getMessage("po.message.deleteConfirm", "Confirm to delete?")));
        }
        
        public Collection getTableRows() {
        	String name = (String) getFilterValue("name");
        	
      //      SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
      //      List cols = (List) searchCol.getValue();
       //     String searchColValue = "";
       //     if (cols.size() > 0) {
       //         searchColValue = (String) cols.get(0);
        //    }
          
            SelectBox categoryCol = (SelectBox) getFilter("categoryFilter").getWidget();
            List categoryCols = (List) categoryCol.getValue();
            String categoryColValue = "";
            if (categoryCols.size() > 0) {
            	categoryColValue = (String) categoryCols.get(0);
            }
            
			Application app = Application.getInstance();
			SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	    	
			return module.getItem(name, /*searchColValue, */categoryColValue, getSort(), isDesc(), getStart(), getRows());
        }
        
        public int getTotalRowCount() {
        	return 0;
        }
        
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
        	 Application app = Application.getInstance();
	         SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	          
	         if ("delete".equals(action)) {
	            for (int i=0; i<selectedKeys.length; i++) {
	               module.deleteItem(selectedKeys[i]);
	            }
	         }
            
            if ("add".equals(action)) {
               return new Forward("Add");
            }
            return null;
 		}
        
        public String getTableRowKey() {
            return "itemCode";
        }
        
		private Map getCategoryOptionsMap() {
	       
	    	Map optionsMap = new SequencedHashMap();
	    	
	    	SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
	    	
	    	Collection cat = module.getCategory();
	    	optionsMap.put("", Application.getInstance().getMessage("po.label.pleaseSelect","--Please Select--"));
	    	 for( Iterator i=cat.iterator(); i.hasNext(); ){
		        	CategoryObject o = (CategoryObject) i.next();
		        	optionsMap.put(o.getCategoryID(), o.getCategory());
		        }
		        
	    	
	    	return optionsMap;
	    }
    }
}
