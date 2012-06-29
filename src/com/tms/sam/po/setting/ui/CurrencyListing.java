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

public class CurrencyListing extends Table{
	    public void init() {
	        setWidth("100%");
	        setModel(new CurrencyListingTableModel());
	    }
	    
	    public void onRequest(Event evt) {
	        init();
	    }
	    
	    public class CurrencyListingTableModel extends TableModel {
	        public CurrencyListingTableModel() {
	        	Application app = Application.getInstance();
	        	
	        	TableColumn country = new TableColumn("country", app.getMessage("po.label.country", "Country"));
	        	country.setUrlParam("currencyID");
	            addColumn(country);
	            
	            TableColumn currency = new TableColumn("currency", app.getMessage("po.menu.currency", "Currency"));
	            addColumn(currency);
	            
	            addFilter(new TableFilter("name"));
	            

	            
	            addAction(new TableAction("add", app.getMessage("po.label.add", "Add"), ""));
	            addAction(new TableAction("delete", app.getMessage("po.label.delete", "Delete"), app.getMessage("po.message.deleteConfirm", "Confirm to delete?")));
	        }
	        
	        public Collection getTableRows() {
	        	String name = (String) getFilterValue("name");
	            
	 			Application app = Application.getInstance();
	 			SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	 	    	
	 			return module.getListing(name, getSort(), isDesc(), getStart(), getRows());
	        }
	        
	        public int getTotalRowCount() {
				Application app = Application.getInstance();
	            String name = (String) getFilterValue("name");
	           	           
	            SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	            return module.countListing(name);
	        }
	        
	        public Forward processAction(Event evt, String action, String[] selectedKeys) {
	            Application app = Application.getInstance();
	            SettingModule module = (SettingModule) app.getModule(SettingModule.class);
	          
	            if ("delete".equals(action)) {
	                for (int i=0; i<selectedKeys.length; i++) {
	                  module.deleteCurrency(selectedKeys[i]);
	                }
	            }
	            
	            if ("add".equals(action)) {
	               return new Forward("Add");
	            }
	            return null;
	 		}
	        
	        public String getTableRowKey() {
	            return "currencyID";
	        }
	    }
}
