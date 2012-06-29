package com.tms.fms.facility.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.widgets.ExtendedTableColumn;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
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

/**
 * @author fahmi
 *
 */

public class RateCardTable extends Table {
	public static String FORWARD_LISTING_ADD="ratecard.listing.add";
	public static String FORWARD_LISTING_INACTIVE="ratecard.listing.inactive";
	public static String FORWARD_LISTING_DELETE="ratecard.listing.delete";
	public static String FORWARD_LISTING_INVALID="ratecard.listing.invalid";
	public static String FORWARD_LISTING_CANNOT_INACTIVATE="ratecard.listing.cannotInactivate";
	
	protected SelectBox sbServiceType;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new RateCardTableModel());
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/extendedtable";
	}
	
	@SuppressWarnings("serial")
	public class RateCardTableModel extends TableModel {
		public RateCardTableModel(){
			TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.ratecard.table.label.name","Name"), true);
            tcName.setUrlParam("id");
            addColumn(tcName);
            
			TableColumn tcServiceType = new TableColumn("serviceTypeId", Application.getInstance().getMessage("fms.request.label.servicesRequired"), true);
            tcServiceType.setFormat(new TableStringFormat(EngineeringModule.SERVICES_MAP));
            addColumn(tcServiceType);
            
            TableColumn abwCode = new TableColumn("abwCode", Application.getInstance().getMessage("fms.facility.rateCard.label.abwCode","ABW Code"));
           	addColumn(abwCode);
            
            ExtendedTableColumn etcInternalRate = new ExtendedTableColumn("internalRate", Application.getInstance().getMessage("fms.ratecard.table.label.internalRate","Internal Rate"), false);
            etcInternalRate.setAlign("right", "center");
			addColumn(etcInternalRate);		
			
			ExtendedTableColumn etcExternalRate = new ExtendedTableColumn("externalRate", Application.getInstance().getMessage("fms.ratecard.table.label.externalRate","External Rate"), false);
			etcExternalRate.setAlign("right", "center");
			addColumn(etcExternalRate);
			
			TableColumn tcStatus=new TableColumn("status",Application.getInstance().getMessage("fms.ratecard.table.label.status", "Status"));
				Map mapIsActive = new HashMap(); 
				mapIsActive.put("1", Application.getInstance().getMessage("fms.tran.setup.active", "Active")); 
				mapIsActive.put("0", Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
			tcStatus.setFormat(new TableStringFormat(mapIsActive));
			addColumn(tcStatus);
			
			TableFilter filterSearch = new TableFilter("name");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			filterSearch.setWidget(searchText);
			addFilter(filterSearch);
			
			TableFilter tfServiceType = new TableFilter("filterServiceType");
			sbServiceType = new SelectBox("filterServiceType");
			//sbServiceType.setOptionMap(EngineeringModule.SERVICES_MAP);
			sbServiceType.addOption("-1", Application.getInstance().getMessage("fms.label.serviceType", "Service Type"));
			int i=0;
			for(Iterator itr=EngineeringModule.SERVICES_MAP.keySet().iterator();itr.hasNext();i++){
				String key=(String)itr.next();
				sbServiceType.addOption(key, (String)EngineeringModule.SERVICES_MAP.get(key));
			}	
			tfServiceType.setWidget(sbServiceType);
			addFilter(tfServiceType);
			
			TableFilter filterStatus = new TableFilter("filterStatus");
			SelectBox statusIsActive = new SelectBox("statusIsActive");
			statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.setup.status", "Status"));
			statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.setup.active", "Active"));
			statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.setup.inactive", "Inactive"));
			filterStatus.setWidget(statusIsActive);
			addFilter(filterStatus);
			
			addAction(new TableAction("add", Application.getInstance().getMessage("ratecard.listing.add", "Add Rate Card")));
			SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
			
			boolean hasDeletePermission = false;
			try {				
				hasDeletePermission = ss.hasPermission(getWidgetManager().getUser().getId(),"com.tms.fms.facility.permission.deleteRateCard", null, null);
			} catch (SecurityException er) {
				Log.getLog(getClass()).error(er);
			}
			
			addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.facility.table.setActive", "Activate")));
		    addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.facility.table.setInactive", "Deactivate")));
			
		    if(hasDeletePermission){
				addAction(new TableAction("delete", Application.getInstance().getMessage("ratecard.listing.delete", "Delete Selected Rate Card"),Application.getInstance().getMessage("ratecard.listing.deleteConfirmation", "Delete Selected Rate Card")));
			}
			
		}

		public String getStatusIsActive() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("filterStatus");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
		
		public String getServiceTypeFilter(){
			String serviceTypeFilter = "";
			serviceTypeFilter = WidgetUtil.getSbValue(sbServiceType);
			
			if (serviceTypeFilter != null && "-1".equals(serviceTypeFilter)) serviceTypeFilter = null;
			
			return serviceTypeFilter;
		}
		
		@Override
		public Collection getTableRows() {
			String name= (String)getFilterValue("name");
			
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			
			return module.getRateCard(name, getStatusIsActive(), getServiceTypeFilter(), getSort(), isDesc(), getStart(), getRows());			
		}

		@Override
		public int getTotalRowCount() {
			String name= (String) getFilterValue("name");
			
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);
			
			return module.countRateCard(name, getStatusIsActive(), getServiceTypeFilter());
		}
		
		public String getTableRowKey() {
		    return "id";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
				
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			
			} else if ("setActive".equals(action)) {
		    	
				for (int i=0; i< selectedKeys.length; i++) {
					RateCard rc = mod.getRateCard(selectedKeys[i]);
					if("0".equals(rc.getStatus())){
						// check for valid rate card
						if (mod.isValidRateCard(selectedKeys[i])) {
							rc.setStatus("1");
							mod.updateRateCard(rc);
							mod.pushToAbwServer(rc);
						} else {
							return new Forward(FORWARD_LISTING_INVALID);
						}
					}
			    }
		    } else if ("setInactive".equals(action)) {
	    	
		    	for(int i=0; i<selectedKeys.length; i++){
	    	    	 RateCard rc = mod.getRateCard(selectedKeys[i]);
	    	    	 if("1".equals(rc.getStatus())){
	    	    		 rc.setStatus("0");
	    	    		 if (mod.canInactiveRateCard(selectedKeys[i], rc.getServiceTypeId())) {
	    	    			 mod.updateRateCard(rc);
	    	    			 
	    	    			 {
	    	    				//#13014 send email for inactive updates
	    	    				DefaultDataObject obj = new DefaultDataObject();
								obj.setId(rc.getId());
								obj.setProperty("abwCode", rc.getAbwCode());
								obj.setProperty("rate_card_name", rc.getName());
									
								mod.insertRateCardEmailNotification(obj);
	    	    			 }
	    	    			 
	    	    		 } else {
	    	    			 return new Forward(FORWARD_LISTING_CANNOT_INACTIVATE);
	    	    		 }
	    	    	 } 
	    	     }
		    } else if ("delete".equals(action)){
		    	
		    	for (int i=0; i<selectedKeys.length; i++){
		 			 mod.deleteRateCard(selectedKeys[i]);
		 		}		    
		    	if (selectedKeys.length > 0) {
		    		return new Forward(FORWARD_LISTING_DELETE);
		    	}
			}
			return super.processAction(event, action, selectedKeys);
		}
	}
}
