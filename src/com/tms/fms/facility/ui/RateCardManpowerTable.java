package com.tms.fms.facility.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.facility.model.SetupModule;


/**
 * @author fahmi
 * 
 */
public class RateCardManpowerTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	protected String id;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("70%");
		setModel(new RateCardManpowerTableModel());		
	}
	
	public void onRequest(Event event) {
		id = event.getRequest().getParameter("id");
		init();
	}
	
	public class RateCardManpowerTableModel extends TableModel {
		public RateCardManpowerTableModel(){            
			addColumn(new TableColumn("manpower", Application.getInstance().getMessage("fms.facility.table.manpower", "Manpower Type"), false));		
			addColumn(new TableColumn("manpowerQty", Application.getInstance().getMessage("fms.facility.table.quantity", "Quantity"), false));
		}

		@Override
		public Collection getTableRows() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.getRateCardManpower(getId(), "");			
		}

		@Override
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.countRateCardManpower(getId(), "");
		}
		
		public String getTableRowKey() {
		    return "idEquipment";
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
