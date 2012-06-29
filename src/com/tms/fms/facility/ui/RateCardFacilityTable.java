package com.tms.fms.facility.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;


/**
 * @author fahmi
 * 
 */
public class RateCardFacilityTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	protected String id;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("70%");
		setModel(new RateCardFacilityTableModel());		
	}
	
	public void onRequest(Event event) {
		id = event.getRequest().getParameter("id");
		init();
	}
	
	public class RateCardFacilityTableModel extends TableModel {
		public RateCardFacilityTableModel(){            
			addColumn(new TableColumn("equipment", Application.getInstance().getMessage("fms.facility.table.facilities", "Name"), false));		
			addColumn(new TableColumn("equipmentQty", Application.getInstance().getMessage("fms.facility.table.quantity", "Quantity"), false));
		}

		@Override
		public Collection getTableRows() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.getRateCardEquipment(getId(), "");			
		}

		@Override
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			return module.countRateCardEquipment(getId(), "");
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
