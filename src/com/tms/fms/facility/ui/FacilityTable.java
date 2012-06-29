package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tms.fms.facility.model.*;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
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
import kacang.util.Log;

public class FacilityTable extends Table{
	
	public static String FORWARD_LISTING_ADD="listing.add";
	public static String FORWARD_LISTING_DELETE="listing.delete";
	public static String FORWARD_LISTING_DELETE_FAIL="listing.delete.fail";
	
	private ViewFacilityModel model;
	
	public FacilityTable(){}
	
	public FacilityTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		setPageSize(10);
		model = new ViewFacilityModel();
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewFacilityModel extends TableModel{
		public ViewFacilityModel(){}
		
		public void resetTable(){
			
			SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
			boolean hasPermission = false;
			try {
				hasPermission = ss.hasPermission(getWidgetManager().getUser().getId(),"com.tms.fms.facility.permission.activeInactiveFacility", null, null);
			} catch (SecurityException er) {
				Log.getLog(getClass()).error(er);
			}
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.facility.table.addNewFacility", "Add New Facility")));			
			if(hasPermission){
				addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.facility.table.setActive", "Set Active")));
				addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.facility.table.setInactive", "Set Inactive")));
			}
			
			TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.facility.table.name", "Name"));
		    tcName.setUrlParam("id");
			addColumn(tcName);
		    
		    TableColumn tcMakeType = new TableColumn("maketype", Application.getInstance().getMessage("fms.facility.table.makeType", "Make Type"));
		    addColumn(tcMakeType);
		    
		    TableColumn tcCategory = new TableColumn("category_name", Application.getInstance().getMessage("fms.facility.table.category", "Category"));
		    addColumn(tcCategory);
		    
		    TableColumn tcQuantity = new TableColumn("quantity", Application.getInstance().getMessage("fms.facility.table.totalQuantity", "Total Quantity"));
		    addColumn(tcQuantity);
		    
		    TableColumn tcQuantityAvailable = new TableColumn("quantityAvailable", Application.getInstance().getMessage("fms.facility.table.quantityAvailable", "Quantity Available"));
		    addColumn(tcQuantityAvailable);
		    
		    TableColumn tcCOQuantity = new TableColumn("quantityCheckedOut", Application.getInstance().getMessage("fms.facility.table.quantityCheckOut", "Quantity Checked Out"));
		    addColumn(tcCOQuantity);
		    
		    TableColumn tcIsPool = new TableColumn("is_pool", Application.getInstance().getMessage("fms.facility.form.poolableItem", "Usage"));
		    Map mapIsPoolable = new HashMap();
		    mapIsPoolable.put("P", Application.getInstance().getMessage("fms.facility.form.poolable"));
		    mapIsPoolable.put("N", Application.getInstance().getMessage("fms.facility.form.nonPoolable"));
		    mapIsPoolable.put("S", Application.getInstance().getMessage("fms.facility.form.studioMode"));
		    tcIsPool.setFormat(new TableStringFormat(mapIsPoolable));
		    addColumn(tcIsPool);
		    
		    TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.facility.table.status", "Status"));
			Map mapIsActive = new HashMap(); 			
			mapIsActive.put("1", Application.getInstance().getMessage("fms.facility.table.active", "Active")); 
			mapIsActive.put("0", Application.getInstance().getMessage("fms.facility.table.inactive", "Inactive"));
			tcStatus.setFormat(new TableStringFormat(mapIsActive));
			addColumn(tcStatus);
		    
		    TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
			
			TableFilter tfCategory = new TableFilter("tfCategory");
			SelectBox sbCategory = new SelectBox("sbCategory");
			sbCategory.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.category", "Category"));
		    try {
				FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
				Collection lstCategory = mod.selectCategory("","","","",false,"","",false,0,-1);
			    if (lstCategory.size() > 0) {
			    	for (Iterator i=lstCategory.iterator(); i.hasNext();) {
			        	CategoryObject o = (CategoryObject)i.next();
			        	sbCategory.setOptions(o.getId()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			tfCategory.setWidget(sbCategory);
			addFilter(tfCategory);
			
			TableFilter tfStatus = new TableFilter("tfStatus");
			SelectBox statusIsActive = new SelectBox("statusIsActive");
			statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.status", "Status"));
			statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.facility.table.active", "Active"));
			statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.facility.table.inactive", "Inactive"));
			statusIsActive.setSelectedOption("1");
			tfStatus.setWidget(statusIsActive);
			addFilter(tfStatus);
		}
		
		public Collection getTableRows() {
			FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
		    	return dao.selectFacility(getSearchText(), getCategory(), "", getStatusIsActive(), getSort(), isDesc(), getStart(), getRows());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}

		public int getTotalRowCount() {
			FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
		    	return dao.selectFacilityCount(getSearchText(), getCategory(), "", getStatusIsActive());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}		
		
		public String getStatusIsActive() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("tfStatus");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
		
		public String getCategory() {
			String returnValue = "-1";
			List lstCategory = (List)getFilterValue("tfCategory");
			if (lstCategory.size() > 0) {returnValue = (String)lstCategory.get(0);}
			return returnValue;
		}
		
		public String getTableRowKey() {
		    return "id";
		}
		
		public String getSearchText() {
			return (String)getFilterValue("tfSearchText");
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("setActive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getFacility(selectedKeys[i]);
					o.setStatus("1");
					mod.updateFacility(o);
			    }
		    }else if ("setInactive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					FacilityObject o = mod.getFacility(selectedKeys[i]);
					o.setStatus("0");
					mod.updateFacility(o);
			    }
		    }
			
			return super.processAction(event, action, selectedKeys);
		}
	}
}
