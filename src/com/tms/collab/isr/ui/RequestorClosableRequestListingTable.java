package com.tms.collab.isr.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class RequestorClosableRequestListingTable extends Table {
private int intTotalRow = 0;
	
	public String getDefaultTemplate() {
        return "isr/table";
    }
	
	public void init() {
		super.init();
        setModel(new RequestorClosableRequestListingTableModel());
        setWidth("100%");
	}
	
	public class RequestorClosableRequestListingTableModel extends TableModel {
		public RequestorClosableRequestListingTableModel() {
			Application application = Application.getInstance();
			
			TableColumn requestId = new TableColumn("requestIdRequestorUrl", application.getMessage("isr.label.requestId", "Request ID"));
			/*requestId.setUrlParam("requestId");
			requestId.setUrl("requestorViewRequest.jsp");*/
			addColumn(requestId);
			
			TableColumn requestToDeptName = new TableColumn("requestToDeptName", application.getMessage("isr.label.attentionTo", "Attention To"));
			addColumn(requestToDeptName);
			
			TableColumn subject = new TableColumn("requestSubject", application.getMessage("isr.label.subject", "Subject"));
			addColumn(subject);
			
			TableColumn dateCreated = new TableColumn("dateCreated", application.getMessage("isr.label.dateTimeReported", "Date/Time Reported"));
			TableFormat dateCreatedFormat = new TableDateFormat(application.getProperty("globalDatetimeLong"));
			dateCreated.setFormat(dateCreatedFormat);
			addColumn(dateCreated);
			
			TextField tfSearch = new TextField("tfSearch");
            TableFilter searchFilter = new TableFilter("searchFilter");
            searchFilter.setWidget(tfSearch);
            addFilter(searchFilter);
            
            Label lblDummyBr1 = new Label("lblDummyBr1", "<br/>");
            TableFilter dummyBrFilter1 = new TableFilter("lblDummyBr1");
            dummyBrFilter1.setWidget(lblDummyBr1);
            addFilter(dummyBrFilter1);
            
            SelectBox selectDept = new SelectBox("selectDept");
            selectDept.setMultiple(false);
            selectDept.setOptionMap(getAttendingDeptOptionsMap());
            TableFilter deptFilter = new TableFilter("deptFilter");
            deptFilter.setWidget(selectDept);
            addFilter(deptFilter);
            
            PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
            if(permissionModel.hasPermission(application.getCurrentUser().getId(), ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT)) {
	            addAction(new TableAction("close", application.getMessage("isr.label.close", "Close"),
	            		application.getMessage("isr.message.confirmClose")));
            }
		}
		
		private Map getAttendingDeptOptionsMap() {
	    	Map optionsMap = new SequencedHashMap();
	    	
	    	Application application = Application.getInstance();
	    	OrgChartHandler orgChartModel = (OrgChartHandler) application.getModule(OrgChartHandler.class);
	    	
	    	Collection deptCols = orgChartModel.selectDepartmentCountryAssociativity(null, null, null, "countryDesc, deptDesc", false, 0, -1);
	    	optionsMap.put("", "---" + application.getMessage("isr.label.allDepartment") + "---");
	    	for(Iterator i=deptCols.iterator(); i.hasNext();) {
	    		DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) i.next();
	    		optionsMap.put(obj.getAssociativityId(), 
    					obj.getCountryDesc() + " - " + obj.getDeptDesc());
	    	}
	    	
	    	return optionsMap;
	    }
		
		public Collection getTableRows(){
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			String searchBy = (String) getFilterValue("searchFilter");
            
            SelectBox selectDept = (SelectBox) getFilter("deptFilter").getWidget();
            List selectDeptCols = (List) selectDept.getValue();
            String selectedDept = "";
            if (selectDeptCols.size() > 0) {
            	selectedDept = (String) selectDeptCols.get(0);
            }
			
			return requestModel.selectRequest(searchBy, StatusObject.STATUS_ID_COMPLETED, selectedDept, true, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount(){
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			String searchBy = (String) getFilterValue("searchFilter");
            
            SelectBox selectDept = (SelectBox) getFilter("deptFilter").getWidget();
            List selectDeptCols = (List) selectDept.getValue();
            String selectedDept = "";
            if (selectDeptCols.size() > 0) {
            	selectedDept = (String) selectDeptCols.get(0);
            }
			
			intTotalRow = requestModel.selectRequestCount(searchBy, StatusObject.STATUS_ID_COMPLETED, selectedDept);
			return intTotalRow;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			if (action.equalsIgnoreCase("close")) {
				Application application = Application.getInstance();
				RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
				
				for(int i=0; i<selectedKeys.length; i++)
					requestModel.acceptRejectRequestResolution(selectedKeys[i], true);
			}
			
			return new Forward();
		}
		
		public String getTableRowKey() {
            return "requestId";
        }
	}
	
	public int getIntTotalRow() {
		return intTotalRow;
	}

	public void setIntTotalRow(int intTotalRow) {
		this.intTotalRow = intTotalRow;
	}
}
