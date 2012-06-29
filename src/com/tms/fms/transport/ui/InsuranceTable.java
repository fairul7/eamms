package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;

import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleObject;

import kacang.Application;
import kacang.model.DaoException;
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

public class InsuranceTable extends Table{

	public static String FORWARD_LISTING_ADD="listing.add";
	public static String FORWARD_LISTING_DELETE="listing.delete";
	public static String FORWARD_LISTING_DELETE_FAIL="listing.delete.fail";
	
	private ViewInsuranceModel model;
	private String vehicle_num;
	
	public InsuranceTable(){}
	
	public InsuranceTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		setPageSize(10);
		model = new ViewInsuranceModel();
		model.setVehicle_num(vehicle_num);
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewInsuranceModel extends  TableModel {
		private String vehicle_num;
		
		public ViewInsuranceModel(){}
		
		public void resetTable(){
			removeChildren();
			
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.tran.add", "Add")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("fms.tran.delete", "Delete")));
			
			TableColumn tcRTRenew = new TableColumn("rt_renew", Application.getInstance().getMessage("fms.tran.table.roadTaxDate", "Road Tax Date"));
			tcRTRenew.setUrlParam("id");
			tcRTRenew.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(tcRTRenew);
			
			TableColumn tcRTAmount = new TableColumn("rt_amount", Application.getInstance().getMessage("fms.tran.table.roadTaxAmount", "Road Tax Amount"));
			addColumn(tcRTAmount);
			
			TableColumn tcISName = new TableColumn("is_name", Application.getInstance().getMessage("fms.tran.table.insuranceCompany", "Insurance Co."));
			addColumn(tcISName);
			
			TableColumn tcISRenew = new TableColumn("is_renew", Application.getInstance().getMessage("fms.tran.table.insuranceDate", "Insurance Date"));
			tcISRenew.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(tcISRenew);
			
			TableColumn tcISAmount = new TableColumn("is_amount", Application.getInstance().getMessage("fms.tran.table.insuranceAmount", "Insurance Amount"));
			addColumn(tcISAmount);
			
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
		    	return dao.selectInsurance(getSearchText(), vehicle_num, getSort(), isDesc(), getStart(), getRows());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectInsuranceCount(getSearchText(), vehicle_num);
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
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("delete".equals(action)) {
				boolean deleted=true;
		    	for (int i=0; i<selectedKeys.length; i++) {
		    		try{
		    			mod.deleteInsurance(selectedKeys[i]);
		    		}catch(Exception e){
		    			deleted=false;
		    		}
			    }
		    	
		    	if(deleted){
		    		return new Forward(FORWARD_LISTING_DELETE);
		    	}else{
		    		return new Forward(FORWARD_LISTING_DELETE_FAIL);
		    	}
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
