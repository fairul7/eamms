package com.tms.fms.engineering.ui;

import java.util.Collection;
import java.util.Iterator;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;

public class MyEngineeringAssignmentTable extends Table {
	public static String FORWARD_LISTING_ADD="listing.add";
	public static String FORWARD_LISTING_INACTIVE="listing.inactive";
	public static String FORWARD_LISTING_DELETE="listing.delete";
	
	protected SelectBox sbAssignmentStatus;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new MyEngineeringAssignmentTableModel());
	}
	
	public class MyEngineeringAssignmentTableModel extends TableModel {
		public MyEngineeringAssignmentTableModel() {
			TableColumn tcCode = new TableColumn("assignmentCode", Application.getInstance().getMessage("fms.facility.label.assignmentId","Name"), false);
            tcCode.setUrlParam("assignmentId");
			addColumn(tcCode);
            
            TableColumn tcTitle = new TableColumn("title", Application.getInstance().getMessage("fms.request.label.requestTitle","Request Title"), false);
            addColumn(tcTitle);
            
            TableColumn tcRequestType = new TableColumn("requestType", Application.getInstance().getMessage("fms.label.requestType","Request Type"), false);
            tcRequestType.setFormat(new TableStringFormat(EngineeringModule.SERVICES_MAP));
            addColumn(tcRequestType);
            
            TableColumn tcRequiredFrom = new TableColumn("requiredFrom", Application.getInstance().getMessage("fms.facility.label.requiredFrom","Required From"), false);
            tcRequiredFrom.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
            addColumn(tcRequiredFrom);
            
            TableColumn tcRequiredTo = new TableColumn("requiredTo", Application.getInstance().getMessage("fms.facility.label.requiredTo","Required To"), false);
            tcRequiredTo.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
            addColumn(tcRequiredTo);
            
            TableColumn tcFromTime = new TableColumn("requiredTime", Application.getInstance().getMessage("fms.facility.label.requiredTime","Required Time"), false);
            addColumn(tcFromTime);
            
            TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.label.status","Status"), false);
            tcStatus.setFormat(new TableStringFormat(EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP));
            addColumn(tcStatus);
                       
			TableFilter filterSearch = new TableFilter("search");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			filterSearch.setWidget(searchText);
			addFilter(filterSearch);
			
			TableFilter tfServiceType = new TableFilter("filterServiceType");
			sbAssignmentStatus = new SelectBox("filterServiceType");
			
			sbAssignmentStatus.addOption("-1", Application.getInstance().getMessage("fms.label.status","Status"));
			int i=0;
			for(Iterator itr=EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.keySet().iterator();itr.hasNext();i++){
				String key=(String)itr.next();
				sbAssignmentStatus.addOption(key, (String)EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(key));
			}	
			tfServiceType.setWidget(sbAssignmentStatus);
			addFilter(tfServiceType);
		}

		public String getStatusFilter(){
			String serviceTypeFilter = "";
			serviceTypeFilter = WidgetUtil.getSbValue(sbAssignmentStatus);
			
			if (serviceTypeFilter != null && "-1".equals(serviceTypeFilter)) serviceTypeFilter = null;
			
			return serviceTypeFilter;
		}
		
		@Override
		public Collection getTableRows() {
			String userId = getWidgetManager().getUser().getId();
			String search= (String)getFilterValue("search");
			
			Application app = Application.getInstance();
			EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
			
			return module.getAssignmentByUserId(userId, search, getStatusFilter(), getSort(), isDesc(), getStart(), getRows());
		}

		@Override
		public int getTotalRowCount() {
			String userId = getWidgetManager().getUser().getId();
			String search= (String)getFilterValue("search");
			
			Application app = Application.getInstance();
			EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
			
			int count = 0;
			
			count = module.countAssignmentByUserId(userId, search, getStatusFilter());
			
			return count;
		}
		
		public String getTableRowKey() {
		    return "assignmentId";
		}
	}
	
}
