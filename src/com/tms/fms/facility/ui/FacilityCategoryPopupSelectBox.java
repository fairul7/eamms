package com.tms.fms.facility.ui;

import java.util.Collection;
import java.util.Map;

import kacang.Application;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

public class FacilityCategoryPopupSelectBox extends PopupSelectBox{

	public FacilityCategoryPopupSelectBox(){}
	
	public FacilityCategoryPopupSelectBox(String s){
		super(s);
	}
	
	protected Map generateOptionMap(String[] ids) {
		Map itemMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemMap;
        }
        
        try{
        	Application app = Application.getInstance();
        	SetupModule module = (SetupModule)app.getModule(SetupModule.class);
        	//FacilityModule mod = (FacilityModule)app.getModule(FacilityModule.class);
        	
        	for (int i=0; i<ids.length; i++) {
	    		//FacilityObject o = (FacilityObject)mod.getFacility(ids[i]);
        		RateCard rc = (RateCard) module.getRateCardCategory(ids[i]);
	    		itemMap.put(ids[i], rc.getCategoryName());
		    }
        }catch(Exception e){
        	 Log.getLog(getClass()).error("Error retrieving item", e);
        }
		return itemMap;
	}

	protected Table initPopupTable() {
		return new FacilityCategoryPopupSelectBoxTable();
	}

	class FacilityCategoryPopupSelectBoxTable extends PopupSelectBoxTable{
		
		public FacilityCategoryPopupSelectBoxTable(){}
		
		public FacilityCategoryPopupSelectBoxTable(String s){
			super(s);
		}
		 
		public void init() {
	        super.init();
	        setWidth("100%");
	        setModel(new FacilityCategoryPopupSelectBoxTableModel());
        }
		
		public void onRequest(Event evt) {
			init();
        }
		
		class FacilityCategoryPopupSelectBoxTableModel extends PopupSelectBoxTableModel{
			
			public FacilityCategoryPopupSelectBoxTableModel(){
				super();
				Application application = Application.getInstance();
				addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
				
				TableColumn tcName = new TableColumn("categoryName", application.getMessage("fms.facility.table.name", "Category Name"));			    
				addColumn(tcName);
			    
			    TableColumn tcEquipmentQty = new TableColumn("equipmentQty", application.getMessage("fms.facility.table.quantity", "Quantity"));
			    addColumn(tcEquipmentQty);			 
			    
			    TableFilter tfSearchText = new TableFilter("tfSearchText");
				TextField searchText = new TextField("searchText");
				searchText.setSize("20");
				tfSearchText.setWidget(searchText);
				addFilter(tfSearchText);
				
			}
			
			public Collection getTableRows() {
				Application application = Application.getInstance();
				SetupModule module = (SetupModule) application.getModule(SetupModule.class);

				return module.getRateCardAllCategory(getSearchText(), getSort(), isDesc(), getStart(), getRows());
			}

			public int getTotalRowCount() {
				Application application = Application.getInstance();
				SetupModule module = (SetupModule) application.getModule(SetupModule.class);
				
				return module.countRateCardAllCategory(getSearchText());
			}		
			
			
			public String getTableRowKey() {
			    return "idCategory";
			}
			
			public String getSearchText() {
				return (String)getFilterValue("tfSearchText");
			}
		}
	}
}
