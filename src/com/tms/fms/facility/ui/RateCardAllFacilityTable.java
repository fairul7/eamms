package com.tms.fms.facility.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;

import com.tms.fms.facility.model.SetupModule;

public class RateCardAllFacilityTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	protected String idEquipment;
	protected String id;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new RateCardAllFacilityTableModel());		
	}
	
	public void onRequest(Event event) {
		id = event.getRequest().getParameter("id");
		init();
	}
	
	public class RateCardAllFacilityTableModel extends TableModel {
		public RateCardAllFacilityTableModel(){            
			TableColumn tcEquipment = new TableColumn("categoryName", Application.getInstance().getMessage("fms.facility.table.name", "Name"));
			addColumn(tcEquipment);
			
			addColumn(new TableColumn("equipmentQty", Application.getInstance().getMessage("fms.facility.table.quantity", "Quantity")));
			
			TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
			
			addAction(new TableAction("select", Application.getInstance().getMessage("ratecard.listing.selectEquipment", "Select Equipment")));
		}

		@Override
		public Collection getTableRows() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.getRateCardAllCategory(getSearchText(), getSort(), isDesc(), getStart(), getRows());
		}

		@Override
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
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
				
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
//			
			if ("select".equals(action)) {
				for (int i=0; i< selectedKeys.length; i++) {
					//Log.getLog(getClass()).info(selectedKeys[i]);
					mod.insertRateCardEquipment(selectedKeys[i], getId(), 1);					
			    }
				return new Forward("select");
			}
//			
			return super.processAction(event, action, selectedKeys);
		}
		
	}

	public String getIdEquipment() {
		return idEquipment;
	}

	public void setIdEquipment(String idEquipment) {
		this.idEquipment = idEquipment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
