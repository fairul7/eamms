package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tms.fms.facility.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class ItemTable extends Table{
	public static String FORWARD_LISTING_ADD="listing.add";
	public static String FORWARD_LISTING_DELETE="listing.delete";
	public static String FORWARD_LISTING_DELETE_FAIL="listing.delete.fail";
	public static String FORWARD_LISTING_WRITEOFF="listing.writeoff";
	public static String FORWARD_LISTING_MISSING="listing.missing";
	public static String FORWARD_LISTING_INACTIVE="listing.inactive";
	public static String FORWARD_LISTING_ERROR="listing.error";
	public static String FORWARD_LISTING_UNDO_SUCC="listing.undo.success";
	
	public ViewItemModel model;
	private String facility_id;
	private String searchModeText="";
	private boolean searchMode=false;
	private boolean undoCheckOut=false;
	
	public String getSearchModeText() {
		return searchModeText;
	}

	public void setSearchModeText(String searchModeText) {
		this.searchModeText = searchModeText;
	}

	public boolean isSearchMode() {
		return searchMode;
	}

	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

	public boolean isUndoCheckOut() {
		return undoCheckOut;
	}

	public void setUndoCheckOut(boolean undoCheckOut) {
		this.undoCheckOut = undoCheckOut;
	}

	public String getFacility_id() {
		return facility_id;
	}

	public void setFacility_id(String facility_id) {
		this.facility_id = facility_id;
	}

	public ItemTable(){}
	
	public ItemTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		setCurrentPage(1);
		setPageSize(10);
		model = new ViewItemModel();
		model.setFacility_id(facility_id);
		model.setSearchMode(searchMode);
		model.setUndoCheckOut(undoCheckOut);
		model.setSearchModeText(searchModeText);
		model.resetTable();
		if(searchMode){
			setMultipleSelect(false);
		}
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewItemModel extends TableModel{
		
		private String facility_id;
		private String searchModeText;
		private boolean searchMode;
		private boolean undoCheckOut;
		
		public String getSearchModeText() {
			return searchModeText;
		}

		public void setSearchModeText(String searchModeText) {
			this.searchModeText = searchModeText;
		}

		public boolean isSearchMode() {
			return searchMode;
		}

		public void setSearchMode(boolean searchMode) {
			this.searchMode = searchMode;
		}

		public boolean isUndoCheckOut() {
			return undoCheckOut;
		}

		public void setUndoCheckOut(boolean undoCheckOut) {
			this.undoCheckOut = undoCheckOut;
		}

		public String getFacility_id() {
			return facility_id;
		}

		public void setFacility_id(String facility_id) {
			this.facility_id = facility_id;
		}

		public ViewItemModel(){}
		
		public void resetTable(){
			removeChildren();
			
			if(!searchMode){
				addAction(new TableAction("add", Application.getInstance().getMessage("fms.facility.table.addNewItem", "Add New Item")));
			}
			addAction(new TableAction("setWriteOff", Application.getInstance().getMessage("fms.facility.table.writeOff", "Write Off")));
			addAction(new TableAction("setMissing", Application.getInstance().getMessage("fms.facility.table.missingStolen", "Missing/Stolen")));
			addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.facility.table.setActive", "Set Active")));
			addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.facility.table.setInactive", "Set Inactive")));
			if(undoCheckOut){
				addAction(new TableAction("setUndoCheckOut", Application.getInstance().getMessage("fms.facility.table.undoCheckOut", "Undo Check Out")));
			}
			
			TableColumn tcCode = new TableColumn("barcode", Application.getInstance().getMessage("fms.facility.table.itemCode", "Item Code"));
			tcCode.setUrlParam("barcode");
			addColumn(tcCode);
			
			if(searchMode){
				TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.facility.table.itemName", "Item Name"));
				addColumn(tcName);
			}
			
			TableColumn tcLocation = new TableColumn("location_name", Application.getInstance().getMessage("fms.facility.table.location", "Location"));
			tcLocation.setFormat(new TableFormat(){

				public String format(Object arg0) {
					if(arg0.toString() == null || "".equals(arg0.toString())){
						return "-";
					}else{
						return arg0.toString();
					}
				}
		    	
		    });
			addColumn(tcLocation);
			
			TableColumn tcDate = new TableColumn("purchased_date", Application.getInstance().getMessage("fms.facility.table.datePurchased", "Date Purchased"));
			tcDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
			addColumn(tcDate);
			
			TableColumn tcCost = new TableColumn("purchased_cost", Application.getInstance().getMessage("fms.facility.table.purchasedCost", "Purchased Cost"));
			addColumn(tcCost);
			
			TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.facility.table.status", "Status"));
			Map mapIsActive = new HashMap(); 
			mapIsActive.put(FacilityModule.ITEM_STATUS_CHECKED_IN, Application.getInstance().getMessage("fms.facility.table.active", "Active")); 
			mapIsActive.put(FacilityModule.ITEM_STATUS_INACTIVE, Application.getInstance().getMessage("fms.facility.table.inactive", "Inactive"));
			mapIsActive.put(FacilityModule.ITEM_STATUS_WRITE_OFF, Application.getInstance().getMessage("fms.facility.table.writeOff", "Write Off"));
			mapIsActive.put(FacilityModule.ITEM_STATUS_CHECKED_OUT, Application.getInstance().getMessage("fms.facility.table.checkOut", "Check Out"));
			mapIsActive.put(FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT, Application.getInstance().getMessage("fms.facility.prepareCheckout", "Prepare Check Out"));
			mapIsActive.put(FacilityModule.ITEM_STATUS_MISSING, Application.getInstance().getMessage("fms.facility.table.missingStolen", "Missing/Stolen"));
			tcStatus.setFormat(new TableStringFormat(mapIsActive));
			addColumn(tcStatus);
				
			if(!searchMode){
				TableFilter tfSearchText = new TableFilter("tfSearchText");
				TextField searchText = new TextField("searchText");
				searchText.setSize("20");
				tfSearchText.setWidget(searchText);
				addFilter(tfSearchText);
				
				TableFilter tfStatus = new TableFilter("tfStatus");
				SelectBox statusIsActive = new SelectBox("statusIsActive");
				statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.status", "Status"));
				statusIsActive.setOptions(FacilityModule.ITEM_STATUS_CHECKED_IN + "=" + Application.getInstance().getMessage("fms.facility.table.active", "Active"));
				statusIsActive.setOptions(FacilityModule.ITEM_STATUS_INACTIVE + "=" + Application.getInstance().getMessage("fms.facility.table.inactive", "Inactive"));
				statusIsActive.setOptions(FacilityModule.ITEM_STATUS_WRITE_OFF + "=" + Application.getInstance().getMessage("fms.facility.table.writeOff", "Write Off"));
				statusIsActive.setOptions(FacilityModule.ITEM_STATUS_CHECKED_OUT + "=" + Application.getInstance().getMessage("fms.facility.table.checkOut", "Check Out"));
				statusIsActive.setOptions(FacilityModule.ITEM_STATUS_MISSING + "=" + Application.getInstance().getMessage("fms.facility.table.missingStolen", "Missing/Stolen"));
				statusIsActive.setOptions(FacilityModule.ITEM_STATUS_PREPARE_CHECKOUT + "=" + Application.getInstance().getMessage("fms.facility.prepareCheckout", "Prepare Check Out"));
				statusIsActive.setSelectedOption(FacilityModule.ITEM_STATUS_CHECKED_IN);
				tfStatus.setWidget(statusIsActive);
				addFilter(tfStatus);
			}
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

		public Collection getTableRows() {
			FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				Collection col;
				if(searchMode){
					col = dao.selectItem(searchModeText, "-1", "-1", "-1", getSort(), isDesc(), getStart(), getRows());
				}else{
					col = dao.selectItem(getSearchText(), getFacility_id(), "-1", getStatusIsActive(), getSort(), isDesc(), getStart(), getRows());
				}
				return col;
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				int result;
				if(searchMode){
					result = dao.selectItemCount(searchModeText, "-1", "-1", "-1");
				}else{
					result = dao.selectItemCount(getSearchText(), getFacility_id(), "-1", getStatusIsActive());
				}
				return result;
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
		
		public String getTableRowKey() {
		    return "barcode";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("setActive".equals(action)) {
				for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_WRITE_OFF.equals(o.getStatus())||FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(o.getStatus())||FacilityModule.ITEM_STATUS_MISSING.equals(o.getStatus())||FacilityModule.ITEM_STATUS_CHECKED_IN.equals(o.getStatus())){
						HttpServletRequest request = event.getRequest();
				    	request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.facility.msg.setActiveError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_INACTIVE.equals(o.getStatus())){
						o.setStatus(FacilityModule.ITEM_STATUS_CHECKED_IN);
						mod.updateItem(o);
					}
			    }
		    }else if ("setInactive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_WRITE_OFF.equals(o.getStatus())||FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(o.getStatus())||FacilityModule.ITEM_STATUS_MISSING.equals(o.getStatus())||FacilityModule.ITEM_STATUS_INACTIVE.equals(o.getStatus())){
						HttpServletRequest request = event.getRequest();
						request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.facility.msg.setInactiveError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	HttpServletRequest request = event.getRequest();
		    	request.setAttribute("selectedKeys", selectedKeys);
		    	event.setRequest(request);
		    	
		    	return new Forward(FORWARD_LISTING_INACTIVE);
		    }else if ("setWriteOff".equals(action) && selectedKeys.length > 0) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_WRITE_OFF.equals(o.getStatus())||FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(o.getStatus())||FacilityModule.ITEM_STATUS_MISSING.equals(o.getStatus())){
						HttpServletRequest request = event.getRequest();
						request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.facility.msg.setWriteOffError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	HttpServletRequest request = event.getRequest();
		    	request.setAttribute("selectedKeys", selectedKeys);
		    	event.setRequest(request);
		    	
		    	return new Forward(FORWARD_LISTING_WRITEOFF);
		    }else if ("setMissing".equals(action) && selectedKeys.length > 0) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_WRITE_OFF.equals(o.getStatus())||FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(o.getStatus())||FacilityModule.ITEM_STATUS_MISSING.equals(o.getStatus())){
						HttpServletRequest request = event.getRequest();
						request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.facility.msg.setMissingError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	HttpServletRequest request = event.getRequest();
		    	request.setAttribute("selectedKeys", selectedKeys);
		    	event.setRequest(request);
		    	
		    	return new Forward(FORWARD_LISTING_MISSING);
		    }else if ("setUndoCheckOut".equals(action) && selectedKeys.length > 0 && undoCheckOut) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_CHECKED_IN.equals(o.getStatus())||FacilityModule.ITEM_STATUS_INACTIVE.equals(o.getStatus())||FacilityModule.ITEM_STATUS_MISSING.equals(o.getStatus())||FacilityModule.ITEM_STATUS_WRITE_OFF.equals(o.getStatus())){
						HttpServletRequest request = event.getRequest();
						request.setAttribute("errorMsg", Application.getInstance().getMessage("fms.facility.msg.setUndoCheckOutError"));
				    	event.setRequest(request);
						return new Forward(FORWARD_LISTING_ERROR);
					}
			    }
		    	
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getItem(selectedKeys[i]);
					if(FacilityModule.ITEM_STATUS_CHECKED_OUT.equals(o.getStatus())){
						mod.undoCheckOut(selectedKeys[i]);
					}
			    }
		    	return new Forward(FORWARD_LISTING_UNDO_SUCC);
		    }
			
			return super.processAction(event, action, selectedKeys);
		}
	}
}
