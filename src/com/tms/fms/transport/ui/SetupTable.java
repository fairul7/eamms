package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tms.fms.facility.model.FacilityModule;
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
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class SetupTable extends Table{
	public static String FORWARD_LISTING_ADD="setup.listing.add";
	public static String FORWARD_LISTING_DELETE="setup.listing.delete";
	public static String FORWARD_LISTING_DELETE_FAIL="setup.listing.deleteFail";
	
	protected ViewSetupModel model;
	protected String setupType = SetupObject.SETUP_BODY_TYPE;
	
	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}
	
	public SetupTable(){}
	
	public SetupTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		model = new ViewSetupModel();
		model.setSetupType(setupType);
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewSetupModel extends TableModel {
		
		protected String setupType;
		protected String setupString;
		
		public String getSetupType() {
			return setupType;
		}

		public void setSetupType(String setupType) {
			this.setupType = setupType;
		}
		
		public ViewSetupModel(){
		}

		public void resetTable(){
			if(setupType.equals(SetupObject.SETUP_CATEGORY)){
				setupString = "category";
			}else if(setupType.equals(SetupObject.SETUP_BODY_TYPE)){
				setupString = "bodyType";
			}else if(setupType.equals(SetupObject.SETUP_CHANNEL)){
				setupString = "channel";
			}else if(setupType.equals(SetupObject.SETUP_FUEL_TYPE)){
				setupString = "fuelType";
			}else if(setupType.equals(SetupObject.SETUP_INACTIVE_REASON)){
				setupString = "inactiveReason";
			}else if(setupType.equals(SetupObject.SETUP_MAKE_TYPE)){
				setupString = "makeType";
			}else if(setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL)){
				setupString = "outsourcePanel";
			}else if(setupType.equals(SetupObject.SETUP_PETROL_CARD)){
				setupString = "petrolCard";
			}else if(setupType.equals(SetupObject.SETUP_WORKSHOP)){
				setupString = "workshop";
			}else if(setupType.equals(SetupObject.SETUP_LOCATION)){
				setupString = "location";
			}else if(setupType.equals(SetupObject.SETUP_F_INACTIVE_REASON)){
				setupString = "inactiveReason";
			}
			
			removeChildren();
			
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.tran.setup."+ setupString +"AddNew", "Add")));
		    addAction(new TableAction("delete", Application.getInstance().getMessage("fms.tran.delete", "Delete")));
		    addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.tran.setActive", "Set Active")));
		    addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.tran.setInactive", "Set Inactive")));
		    
		    TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.tran.setup."+ setupString +"Name", "Name"));
		    tcName.setUrlParam("setup_id");
		    addColumn(tcName);
		    
		    TableColumn tcDescription = new TableColumn("description", Application.getInstance().getMessage("fms.tran.setup."+ setupString +"Description", "Description"));
		    tcDescription.setSortable(false);
		    addColumn(tcDescription);
		    
		    if((setupType.equals(SetupObject.SETUP_CATEGORY)) || (setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL))){
		    	TableColumn tcType = new TableColumn("type", Application.getInstance().getMessage("fms.tran.setup.type", "Status"), false);
				Map mapType = new HashMap(); 
				mapType.put("E", Application.getInstance().getMessage("fms.tran.setup.engineering", "Engineering")); 
				mapType.put("T", Application.getInstance().getMessage("fms.tran.setup.transport", "Transport"));
				tcType.setFormat(new TableStringFormat(mapType));
				addColumn(tcType);
		    }
		    
		    TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.tran.setup.status", "Status"));
			Map mapIsActive = new HashMap(); 
			mapIsActive.put("1", Application.getInstance().getMessage("fms.tran.setup.active", "Active")); 
			mapIsActive.put("0", Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
			tcStatus.setFormat(new TableStringFormat(mapIsActive));
			addColumn(tcStatus);
			
			TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
			
			TableFilter tfStatus = new TableFilter("tfStatus");
			SelectBox statusIsActive = new SelectBox("statusIsActive");
			statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.tran.setup.status", "Status"));
			statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.tran.setup.active", "Active"));
			statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
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
		
		public Collection getTableRows() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectSetupObject(setupType, getSearchText(), getStatusIsActive(), getSort(), isDesc(), getStart(), getRows());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
			try {
		    	return dao.selectSetupObjectCount(setupType, getSearchText(), getStatusIsActive());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
		
		public String getTableRowKey() {
		    return "setup_id";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			TransportModule mod = (TransportModule) Application.getInstance().getModule(TransportModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("delete".equals(action)) {
				boolean allDeleted = true;
				for (int i=0; i<selectedKeys.length; i++) {
					boolean canDelete = true;
					
					if(setupType.equals(SetupObject.SETUP_CATEGORY)){
						canDelete = (mod.selectVehicleCount("", "", "", selectedKeys[i], "", "", "", "") == 0);
					}else if(setupType.equals(SetupObject.SETUP_BODY_TYPE)){
						canDelete = (mod.selectVehicleCount("", "", "", "", "", selectedKeys[i], "", "") == 0);
					}else if(setupType.equals(SetupObject.SETUP_CHANNEL)){
						canDelete = (mod.selectVehicleCount("", "", selectedKeys[i], "", "", "", "", "") == 0);
					}else if(setupType.equals(SetupObject.SETUP_FUEL_TYPE)){
						canDelete = (mod.selectVehicleCount("", "", "", "", "", "", selectedKeys[i], "") == 0);
					}else if(setupType.equals(SetupObject.SETUP_INACTIVE_REASON)){
						canDelete = (mod.selectInactiveCount("", "", Integer.parseInt(selectedKeys[i])) == 0);
					}else if(setupType.equals(SetupObject.SETUP_MAKE_TYPE)){
						canDelete = (mod.selectVehicleCount("", "", "", "", selectedKeys[i], "", "", "") == 0);
					}else if(setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL)){
						//not yet do
					}else if(setupType.equals(SetupObject.SETUP_PETROL_CARD)){
						//not yet do
					}else if(setupType.equals(SetupObject.SETUP_WORKSHOP)){
						canDelete = (mod.selectWorkshopUsedCount(selectedKeys[i]) == 0);
					}else if(setupType.equals(SetupObject.SETUP_LOCATION)){
						FacilityModule modF = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
						canDelete = (modF.selectItemCount("", "", selectedKeys[i], "-1") == 0);
					}else if(setupType.equals(SetupObject.SETUP_F_INACTIVE_REASON)){
						FacilityModule modF = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
						canDelete =(modF.selectFInactiveCount("", "", selectedKeys[i]) == 0);
						canDelete = (canDelete && !"0".equals(selectedKeys[i]));
					}
					
					if(canDelete){
						mod.deleteSetupObject(setupType, selectedKeys[i]);
					}else{
						allDeleted=false;
					}
			    }
				
				if(allDeleted){
					return new Forward(FORWARD_LISTING_DELETE);
				}else {
					return new Forward(FORWARD_LISTING_DELETE_FAIL);
				}
		    }else if ("setActive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					SetupObject o = mod.getSetupObject(setupType, selectedKeys[i]);
					o.setStatus("1");
					mod.updateSetupObject(setupType, o);
			    }
		    }else if ("setInactive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					SetupObject o = mod.getSetupObject(setupType, selectedKeys[i]);
					o.setStatus("0");
					mod.updateSetupObject(setupType, o);
			    }
		    }
			
			return super.processAction(event, action, selectedKeys);
		}
	}
}
