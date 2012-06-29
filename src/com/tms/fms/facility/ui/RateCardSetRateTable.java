package com.tms.fms.facility.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.facility.model.SetupModule;

public class RateCardSetRateTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	protected String id;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new RateCardSetRateTableModel());		
	}
	
	public void onRequest(Event event) {
		//setId(event.getRequest().getParameter("id"));
		//Log.getLog(getClass()).info(getId());
		
		init();
	}
	
	public class RateCardSetRateTableModel extends TableModel {
		public RateCardSetRateTableModel(){            
			addColumn(new TableColumn("internalRate", Application.getInstance().getMessage("fms.ratecard.table.label.internalRate","Internal Rate"), false));		
			addColumn(new TableColumn("externalRate", Application.getInstance().getMessage("fms.ratecard.table.label.externalRate","External Rate"), false));
			
			TableColumn effectiveDate = new TableColumn("effectiveDate", Application.getInstance().getMessage("fms.ratecard.table.label.effectiveDate","Effective Date"), false);
			effectiveDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(effectiveDate);
			
			addColumn(new TableColumn("createdBy", Application.getInstance().getMessage("fms.ratecard.table.label.setBy","Set By"), false));
			
			TableColumn createdOn = new TableColumn("createdOn", Application.getInstance().getMessage("fms.ratecard.table.label.setDate","Set Date"), false);
			createdOn.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(createdOn);
		}

		@Override
		public Collection getTableRows() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.getRateCardHistory(getId());			
		}

		@Override
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.countRateCardHistory(getId());
		}
		
		public String getTableRowKey() {
		    return "idDetail";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			return super.processAction(event, action, selectedKeys);
		}
		
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
