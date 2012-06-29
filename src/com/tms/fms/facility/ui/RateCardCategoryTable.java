package com.tms.fms.facility.ui;

import java.util.Collection;

import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

/**
 * @author fahmi
 *
 */

public class RateCardCategoryTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	
	protected SelectBox sbServiceType;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new RateCardCategoryTableModel());
	}
	
	@SuppressWarnings("serial")
	public class RateCardCategoryTableModel extends TableModel {
		public RateCardCategoryTableModel(){
			TableColumn tcName = new TableColumn("categoryName", Application.getInstance().getMessage("fms.ratecard.table.label.name","Name"), true);
            tcName.setUrlParam("idCategory");
            addColumn(tcName);
                        
            TableColumn items = new TableColumn("itemsCSV", Application.getInstance().getMessage("fms.request.label.facilityItems"), false);
			addColumn(items);
			
			TableFilter filterSearch = new TableFilter("name");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			filterSearch.setWidget(searchText);
			addFilter(filterSearch);
			
			addAction(new TableAction("add", Application.getInstance().getMessage("ratecardCategory.listing.add", "Add Category")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("ratecardCategory.listing.delete", "Delete Selected Category")));	    
		}

		
		public String getServiceTypeFilter(){
			String serviceTypeFilter = "";
			serviceTypeFilter = WidgetUtil.getSbValue(sbServiceType);
			
			if (serviceTypeFilter != null && "-1".equals(serviceTypeFilter)) serviceTypeFilter = null;
			
			return serviceTypeFilter;
		}
		
		@Override
		public Collection getTableRows() {
			String name= (String)getFilterValue("name");
			
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			
			return module.getRateCardCategory(name, getSort(), isDesc(), getStart(), getRows());			
		}

		@Override
		public int getTotalRowCount() {
			String name= (String) getFilterValue("name");
			
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			
			return module.countRateCardCategory(name);
		}
		
		public String getTableRowKey() {
		    return "idCategory";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
				
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
		    } else if ("delete".equals(action)){
		    	
		    	for (int i=0; i<selectedKeys.length; i++){
		 			 mod.deleteRateCardCategory(selectedKeys[i]);
		 		}		    
		    	if (selectedKeys.length > 0) {
		    		return new Forward(FORWARD_LISTING_DELETE);
		    	}
			}
			return super.processAction(event, action, selectedKeys);
		}
	}
}
