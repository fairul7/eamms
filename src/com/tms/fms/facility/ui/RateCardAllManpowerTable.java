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

import com.tms.fms.facility.model.SetupModule;

public class RateCardAllManpowerTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	protected String idManpower;
	protected String id;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new RateCardAllManpowerTableModel());		
	}
	
	public void onRequest(Event event) {
		id = event.getRequest().getParameter("id");
		init();
	}
	
	public class RateCardAllManpowerTableModel extends TableModel {
		public RateCardAllManpowerTableModel(){            
			TableColumn tcManpower = new TableColumn("manpower", Application.getInstance().getMessage("fms.facility.table.name", "Name"), false);
			addColumn(tcManpower);
			
			TableColumn tcManpowerType = new TableColumn("competencyType", Application.getInstance().getMessage("project.label.type", "Type"), false);
			addColumn(tcManpowerType);
			
			TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
			
			addAction(new TableAction("select", Application.getInstance().getMessage("ratecard.listing.selectManpower", "Select Manpower")));
		}

		@Override
		public Collection getTableRows() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.getRateCardAllManpower(getSearchText());
		}

		@Override
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.countRateCardAllManpower(getSearchText());
		}
		
		public String getTableRowKey() {
		    return "idManpower";
		}
		
		public String getSearchText() {
			return (String)getFilterValue("tfSearchText");
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
				
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
//			
			if ("select".equals(action)) {
				for (int i=0; i< selectedKeys.length; i++) {
					
					mod.insertRateCardManpower(selectedKeys[i], getId(), 1);					
			    }
				return new Forward("select");
			}
//			
			return super.processAction(event, action, selectedKeys);
		}
		
	}

	public String getIdManpower() {
		return idManpower;
	}

	public void setIdManpower(String idManpower) {
		this.idManpower = idManpower;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
