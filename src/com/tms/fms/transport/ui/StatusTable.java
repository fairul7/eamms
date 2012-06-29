package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;

import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleObject;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Form;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class StatusTable extends Table{
	public static String FORWARD_LISTING_DELETE="listing.delete";
	public static String FORWARD_LISTING_DELETE_FAIL="listing.delete.fail";
	
	private ViewStatusModel model;
	private String vehicle_num;
	private String cancelUrl = "";
	
	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public StatusTable(){}
	
	public StatusTable(String s){
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(10);
		model = new ViewStatusModel();
		model.setVehicle_num(vehicle_num);
		model.setCancelUrl(cancelUrl);
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewStatusModel extends  TableModel {
		private String vehicle_num;
		private String cancelUrl="";
		
		public String getCancelUrl() {
			return cancelUrl;
		}

		public void setCancelUrl(String cancelUrl) {
			this.cancelUrl = cancelUrl;
		}

		public ViewStatusModel(){}
		
		public void resetTable(){
			removeChildren();
			
			addAction(new TableAction("delete", Application.getInstance().getMessage("fms.tran.delete", "Delete")));
			addAction(new TableAction("cancel", Application.getInstance().getMessage("fms.tran.cancel", "Cancel")));
			
			TableColumn tcDateFrom = new TableColumn("date_from", Application.getInstance().getMessage("fms.tran.table.dateFrom", "Start Date"));
			tcDateFrom.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(tcDateFrom);
			
			TableColumn tcDateTo = new TableColumn("date_to", Application.getInstance().getMessage("fms.tran.table.dateTo", "End Date"));
			tcDateTo.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(tcDateTo);
			
			TableColumn tcReason = new TableColumn("reason", Application.getInstance().getMessage("fms.tran.table.reason", "Reason"));
			addColumn(tcReason);
			
			TableColumn tcCreatedby = new TableColumn("createdby_name", Application.getInstance().getMessage("fms.tran.table.createdBy", "Created By"));
			addColumn(tcCreatedby);
			
			TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
		}
		
		public String getSearchText() {
			return (String)getFilterValue("tfSearchText");
		}
		
		public Collection getTableRows() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectInactive(getSearchText(), vehicle_num, -1, getSort(), isDesc(), getStart(), getRows());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectInactiveCount(getSearchText(), vehicle_num, -1);
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
		
		public String getTableRowKey() {
		    return "id";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			
			TransportModule mod = (TransportModule) Application.getInstance().getModule(TransportModule.class);
			
			if ("delete".equals(action)) {
				boolean deleted=true;
		    	for (int i=0; i<selectedKeys.length; i++) {
		    		try{
		    			mod.deleteInactive(selectedKeys[i]);
		    		}catch(Exception e){
		    			deleted=false;
		    		}
			    }
		    	
		    	if(deleted){
		    		return new Forward(FORWARD_LISTING_DELETE);
		    	}else{
		    		return new Forward(FORWARD_LISTING_DELETE_FAIL);
		    	}
		    }if ("cancel".equals(action)) {
		    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
		    }
			
			return super.processAction(event, action, selectedKeys);
		}

		public String getVehicle_num() {
			return vehicle_num;
		}

		public void setVehicle_num(String vehicle_num) {
			this.vehicle_num = vehicle_num;
		}
	}

	public String getVehicle_num() {
		return vehicle_num;
	}

	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
}
