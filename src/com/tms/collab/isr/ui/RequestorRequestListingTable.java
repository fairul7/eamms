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

public class RequestorRequestListingTable extends Table {
	public static final String WITHDRAWAL_FAILED = "withdrawal failed";
	private int intTotalRow = 0;
	private String status = "0";
	private int selectedStatusIndex = 0;
	
	public String getDefaultTemplate() {
        return "isr/table";
    }
	
	public void init() {
		super.init();
        setModel(new RequestorRequestListingTableModel());
        setWidth("100%");
	}
	
	public class RequestorRequestListingTableModel extends TableModel {
		public RequestorRequestListingTableModel() {
			Application application = Application.getInstance();
			
			TableColumn requestId = new TableColumn("requestIdRequestorUrl", application.getMessage("isr.label.requestId", "Request ID"));
			/*requestId.setUrlParam("requestId");
			requestId.setUrl("requestorViewRequest.jsp");*/
			addColumn(requestId);
			
			TableColumn requestToDeptName = new TableColumn("requestToDeptName", application.getMessage("isr.label.attentionTo", "Attention To"));
			addColumn(requestToDeptName);
			
			TableColumn subject = new TableColumn("requestSubject", application.getMessage("isr.label.subject", "Subject"));
			addColumn(subject);
			
			TableColumn statusName = new TableColumn("requestStatusName", application.getMessage("isr.label.status", "Status"));
			addColumn(statusName);
			
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
            
            SelectBox selectStatus = new SelectBox("selectStatus");
            selectStatus.setMultiple(false);
            selectStatus.setOptionMap(getStatusOptionsMap());
            selectStatus.setSelectedOption("active");
            TableFilter statusFilter = new TableFilter("statusFilter");
            statusFilter.setWidget(selectStatus);
            addFilter(statusFilter);
            
            SelectBox selectDept = new SelectBox("selectDept");
            selectDept.setMultiple(false);
            selectDept.setOptionMap(getAttendingDeptOptionsMap());
            TableFilter deptFilter = new TableFilter("deptFilter");
            deptFilter.setWidget(selectDept);
            addFilter(deptFilter);
            
            PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
            if(permissionModel.hasPermission(application.getCurrentUser().getId(), ISRGroup.PERM_WITHDRAW_REQUEST)) {
	            addAction(new TableAction("withdraw", application.getMessage("isr.label.withdraw", "Withdraw"),
	            		application.getMessage("isr.message.confirmWithdraw")));
            }
		}
		
		private Map getStatusOptionsMap() {
			Map optionsMap = new SequencedHashMap();
			
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			Collection statusCols = requestModel.selectAllStatus();
			optionsMap.put("", "---" + application.getMessage("isr.label.allStatus") + "---");
			optionsMap.put("active", application.getMessage("isr.label.active", "Active"));
			for(Iterator i=statusCols.iterator(); i.hasNext(); ) {
				StatusObject status = (StatusObject) i.next();
				optionsMap.put(status.getStatusId(), status.getStatusName());
			}
			
			return optionsMap;
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
		
		private int getSelectedStatusIndex(String selectedValue) {
			int selectedStatusIndex = -1;
			
			Map statusOptionsMap = getStatusOptionsMap();
			if(statusOptionsMap.containsKey(selectedValue)) {
				boolean found = false;
				for(Iterator itr=statusOptionsMap.keySet().iterator(); itr.hasNext() && !found; selectedStatusIndex++) {
					String optionKey = itr.next().toString();
					if(optionKey.equals(selectedValue)) {
						found = true;
					}
				}
			}
			
			return selectedStatusIndex;
		}
		
		public Collection getTableRows(){
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			String searchBy = (String) getFilterValue("searchFilter");
			
			String selectedStatus = "";
			if(status != null && !"".equals(status) && !"0".equals(status)) {
				selectedStatus = String.valueOf(status);
				selectedStatusIndex = getSelectedStatusIndex(selectedStatus);
			}
			else {
				selectedStatusIndex = 0;
				SelectBox selectStatus = (SelectBox) getFilter("statusFilter").getWidget();
	            List selectStatusCols = (List) selectStatus.getValue();
	            if (selectStatusCols.size() > 0) {
	            	selectedStatus = (String) selectStatusCols.get(0);
	            }
			}
            
            SelectBox selectDept = (SelectBox) getFilter("deptFilter").getWidget();
            List selectDeptCols = (List) selectDept.getValue();
            String selectedDept = "";
            if (selectDeptCols.size() > 0) {
            	selectedDept = (String) selectDeptCols.get(0);
            }
			
			return requestModel.selectRequest(searchBy, selectedStatus, selectedDept, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount(){
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			String searchBy = (String) getFilterValue("searchFilter");
			
			String selectedStatus = "";
			if(status != null && !"".equals(status) && !"0".equals(status)) {
				selectedStatus = String.valueOf(status);
			}
			else {
				SelectBox selectStatus = (SelectBox) getFilter("statusFilter").getWidget();
	            List selectStatusCols = (List) selectStatus.getValue();
	            if (selectStatusCols.size() > 0) {
	            	selectedStatus = (String) selectStatusCols.get(0);
	            }
			}
            
            SelectBox selectDept = (SelectBox) getFilter("deptFilter").getWidget();
            List selectDeptCols = (List) selectDept.getValue();
            String selectedDept = "";
            if (selectDeptCols.size() > 0) {
            	selectedDept = (String) selectDeptCols.get(0);
            }
			
			intTotalRow = requestModel.selectRequestCount(searchBy, selectedStatus, selectedDept);
			return intTotalRow;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			boolean isSuccess = false;
			
			if (action.equalsIgnoreCase("withdraw")) {
				Application application = Application.getInstance();
				RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
				
				isSuccess = requestModel.withdrawRequest(selectedKeys);
			}
			
			if(isSuccess)
				return new Forward();
			else 
				return new Forward(WITHDRAWAL_FAILED);
		}
		
		public String getTableRowKey() {
            return "requestId";
        }
	}

	public int getSelectedStatusIndex() {
		return selectedStatusIndex;
	}

	public void setSelectedStatusIndex(int selectedStatusIndex) {
		this.selectedStatusIndex = selectedStatusIndex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIntTotalRow() {
		return intTotalRow;
	}

	public void setIntTotalRow(int intTotalRow) {
		this.intTotalRow = intTotalRow;
	}
}
