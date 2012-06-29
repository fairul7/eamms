package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tms.fms.transport.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class VehicleTable extends Table{
	public static String FORWARD_LISTING_ADD="vehicle.listing.add";
	public static String FORWARD_LISTING_WRITEOFF="vehicle.listing.writeOff";
	public static String FORWARD_LISTING_INACTIVE="vehicle.listing.inactive";
	public static String FORWARD_LISTING_ERROR="listing.error";
	
	private String vehicle_num;
	
	public VehicleTable(){}
	
	public VehicleTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		setPageSize(10);
		setModel(new ViewVehicleModel());
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		setModel(new ViewVehicleModel());
	}
	
	class ViewVehicleModel extends  TableModel {
		private String vehicle_num;
		
		public ViewVehicleModel(){
			removeChildren();
			
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.tran.table.addNewVehicle", "Add New Vehicle")));
			addAction(new TableAction("writeOff", Application.getInstance().getMessage("fms.tran.table.writeOff", "Write Off")));
			addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.tran.setActive", "Set Active")));
		    addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.tran.setInactive", "Set Inactive")));
		    
		    TableColumn tcVehicleNum = new TableColumn("vehicle_num", Application.getInstance().getMessage("fms.tran.table.vehicleNum", "Vehicle No."));
		    tcVehicleNum.setUrlParam("vehicle_num");
		    addColumn(tcVehicleNum);
		    
		    TableColumn tcChannel = new TableColumn("channel_name", Application.getInstance().getMessage("fms.tran.table.channel", "Channel"));
		    addColumn(tcChannel);

		    TableColumn tcType = new TableColumn("type", Application.getInstance().getMessage("fms.tran.table.vehicleType", "Vehicle Type"));
		    Map mapType = new HashMap(); 
		    mapType.put("M", Application.getInstance().getMessage("fms.tran.table.managementVehicle", "Management")); 
		    mapType.put("P", Application.getInstance().getMessage("fms.tran.table.poolVehicle", "Pool"));
			tcType.setFormat(new TableStringFormat(mapType));
		    addColumn(tcType);
		    
		    TableColumn tcMakeType = new TableColumn("maketype_name", Application.getInstance().getMessage("fms.tran.table.makeType", "Make Type"));
		    addColumn(tcMakeType);
		    
		    TableColumn tcRegDate = new TableColumn("reg_date", Application.getInstance().getMessage("fms.tran.table.regDate", "Reg. Date"));
		    tcRegDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
		    addColumn(tcRegDate);
		    
		    TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.tran.table.status", "Status"));
		    Map mapStatus = new HashMap(); 
		    mapStatus.put("0", Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive")); 
		    mapStatus.put("1", Application.getInstance().getMessage("fms.tran.setup.active", "Active"));
		    mapStatus.put("2", Application.getInstance().getMessage("fms.tran.table.writeOff", "Write Off"));
			tcStatus.setFormat(new TableStringFormat(mapStatus));
		    addColumn(tcStatus);
		    
		    TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
			
			TableFilter tfChannel = new TableFilter("tfChannel");
			SelectBox sbChannel = new SelectBox("sbChannel");
		    sbChannel.setOptions("-1=" + Application.getInstance().getMessage("fms.tran.table.Channel", "Channel"));
		    try {
				TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				Collection lstChannel = mod.selectSetupObject(SetupObject.SETUP_CHANNEL, "", "1", "name", false, 0, -1);
			    if (lstChannel.size() > 0) {
			    	for (Iterator i=lstChannel.iterator(); i.hasNext();) {
			        	SetupObject o = (SetupObject)i.next();
			        	sbChannel.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			tfChannel.setWidget(sbChannel);
			addFilter(tfChannel);
			
			TableFilter tfType = new TableFilter("tfType");
			SelectBox sbType = new SelectBox("sbType");
		    sbType.setOptions("-1=" + Application.getInstance().getMessage("fms.tran.table.vehicleType", "Vehicle Type"));
		    sbType.setOptions("M=" + Application.getInstance().getMessage("fms.tran.table.managementVehicle", "Management"));
		    sbType.setOptions("P=" + Application.getInstance().getMessage("fms.tran.table.poolVehicle", "Pool"));
			tfType.setWidget(sbType);
			addFilter(tfType);
			
			TableFilter tfStatus = new TableFilter("tfStatus");
			SelectBox statusIsActive = new SelectBox("statusIsActive");
			statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.tran.setup.status", "Status"));
			statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.tran.setup.active", "Active"));
			statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
			statusIsActive.setOptions("2=" + Application.getInstance().getMessage("fms.tran.table.writeOff", "Write Off"));
			statusIsActive.setSelectedOption("1");
			tfStatus.setWidget(statusIsActive);
			addFilter(tfStatus);
		}
		
		public String getSearchText() {
			return (String)getFilterValue("tfSearchText");
		}
		
		public String getStatusIsActive() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("tfStatus");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
		
		public String getChannel() {
			String returnValue = "-1";
			List lstChannel = (List)getFilterValue("tfChannel");
			if (lstChannel.size() > 0) {returnValue = (String)lstChannel.get(0);}
			return returnValue;
		}
		
		public String getType() {
			String returnValue = "-1";
			List lstType = (List)getFilterValue("tfType");
			if (lstType.size() > 0) {returnValue = (String)lstType.get(0);}
			return returnValue;
		}
		
		public Collection getTableRows() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectVehicle(getSearchText(), getType(), getChannel(), "", "", "", "", getStatusIsActive(), getSort(), isDesc(), getStart(), getRows());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectVehicleCount(getSearchText(), getType(), getChannel(), "", "", "", "", getStatusIsActive());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
		
		public String getTableRowKey() {
		    return "vehicle_num";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			
			TransportModule mod = (TransportModule) Application.getInstance().getModule(TransportModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("setActive".equals(action)) {
				for (int i=0; i<selectedKeys.length; i++) {
					VehicleObject v = mod.getVehicle(selectedKeys[i]);
					if(!"0".equals(v.getStatus())){
						HttpServletRequest request = event.getRequest();
				    	request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.tran.msg.setActiveError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	for (int i=0; i<selectedKeys.length; i++) {
					VehicleObject v = mod.getVehicle(selectedKeys[i]);
					if("0".equals(v.getStatus())){
						v.setStatus("1");
						mod.updateVehicle(v);
					}
			    }
		    }else if ("setInactive".equals(action) && selectedKeys.length > 0) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					VehicleObject v = mod.getVehicle(selectedKeys[i]);
					if(!"1".equals(v.getStatus())){
						HttpServletRequest request = event.getRequest();
				    	request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.tran.msg.setInactiveError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	HttpServletRequest request = event.getRequest();
		    	request.setAttribute("selectedKeys", selectedKeys);
		    	event.setRequest(request);
		    	
		    	return new Forward(FORWARD_LISTING_INACTIVE);
		    }else if ("writeOff".equals(action) && selectedKeys.length > 0) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					VehicleObject v = mod.getVehicle(selectedKeys[i]);
					if("2".equals(v.getStatus())){
						HttpServletRequest request = event.getRequest();
				    	request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.tran.msg.setWriteOffError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	HttpServletRequest request = event.getRequest();
		    	request.setAttribute("selectedKeys", selectedKeys);
		    	event.setRequest(request);
		    	
		    	return new Forward(FORWARD_LISTING_WRITEOFF);
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
