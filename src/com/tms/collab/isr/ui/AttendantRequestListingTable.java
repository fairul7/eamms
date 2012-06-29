package com.tms.collab.isr.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.WidgetManager;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class AttendantRequestListingTable extends Table {
	private int intTotalRow = 0;
	private String status = "0";
	private int selectedStatusIndex = 0;
	
	public String getDefaultTemplate() {
        return "isr/table";
    }
	
	public void init() {
		super.init();
        setModel(new AttendantRequestListingTableModel());
        setWidth("100%");
	}
	
	public class AttendantRequestListingTableModel extends TableModel {
		public AttendantRequestListingTableModel() {
			Application application = Application.getInstance();
			String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
			
			TableColumn requestId = new TableColumn("requestIdRequestorUrl", application.getMessage("isr.label.requestId", "Request ID"));
			addColumn(requestId);
			
			TableColumn requesterName = new TableColumn("createdBy", application.getMessage("isr.label.requester", "Requester"));
			addColumn(requesterName);
			
			TableColumn subject = new TableColumn("requestSubject", application.getMessage("isr.label.subject", "Subject"));
			addColumn(subject);
			
			TableColumn statusName = new TableColumn("requestStatusName", application.getMessage("isr.label.status", "Status"));
			addColumn(statusName);
			
			TableColumn priority = new TableColumn("requestPriorityByAdmin", application.getMessage("isr.label.priority", "Priority"));
			TableFormat abbreviationFormat = new TableSingleAbbreviationFormat();
			priority.setFormat(abbreviationFormat);
			addColumn(priority);
			
			TableColumn requestType = new TableColumn("requestType", application.getMessage("isr.label.requestType", "Request Type"));
			addColumn(requestType);
			
			TableColumn dateCreated = new TableColumn("dateCreated", application.getMessage("isr.label.dateTimeReported", "Date/Time Reported"));
			TableFormat dateCreatedFormat = new TableDateFormat(application.getProperty("globalDatetimeLong"));
			dateCreated.setFormat(dateCreatedFormat);
			addColumn(dateCreated);
			
			TableColumn assignees = new TableColumn("assigneeList", application.getMessage("isr.label.assignees", "Assignee(s)"));
			assignees.setSortable(false);
			addColumn(assignees);
			
			TableColumn assigneeResponded = new TableColumn("staffResponded", application.getMessage("isr.label.assigneeResponded", "Assignee Responded"));
			TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
			assigneeResponded.setFormat(booleanFormat);
			addColumn(assigneeResponded);
			
			TableColumn lastUpdatedDate = new TableColumn("lastUpdatedDate", application.getMessage("isr.label.lastUpdatedDate", "Last Updated Date"));
			TableFormat lastUpdatedDateFormat = new TableDateFormat(application.getProperty("globalDatetimeLong"));
			lastUpdatedDate.setFormat(lastUpdatedDateFormat);
			addColumn(lastUpdatedDate);
			
			TableColumn lastUpdatedBy = new TableColumn("lastUpdatedBy", application.getMessage("isr.label.lastUpdatedBy", "Last Updated By"));
			lastUpdatedBy.setSortable(false);
			addColumn(lastUpdatedBy);
			
			TextField tfSearch = new TextField("tfSearch");
            TableFilter searchFilter = new TableFilter("searchFilter");
            searchFilter.setWidget(tfSearch);
            addFilter(searchFilter);
            
            SelectBox selectAttribute = new SelectBox("selectAttribute");
            selectAttribute.setMultiple(false);
            selectAttribute.setOptionMap(getSearchAttributesMap());
            TableFilter attributeFilter = new TableFilter("attributeFilter");
            attributeFilter.setWidget(selectAttribute);
            addFilter(attributeFilter);
            
            Label lblAssigneeName = new Label("lblAssigneeName", application.getMessage("isr.label.assigneeName", "Assignee Name"));
            TableFilter dummyLblFilter2 = new TableFilter("dummyLblFilter2");
            dummyLblFilter2.setWidget(lblAssigneeName);
            addFilter(dummyLblFilter2);
            
            TextField tfAssigneeName = new TextField("tfAssigneeName");
            tfAssigneeName.setSize("20");
            TableFilter assigneeNameFilter = new TableFilter("assigneeNameFilter");
            assigneeNameFilter.setWidget(tfAssigneeName);
            addFilter(assigneeNameFilter);
            
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
            
            SelectBox selectPriority = new SelectBox("selectPriority");
            selectPriority.setMultiple(false);
            selectPriority.setOptionMap(getPriorityOptionsMap());
            TableFilter priorityFilter = new TableFilter("priorityFilter");
            priorityFilter.setWidget(selectPriority);
            addFilter(priorityFilter);
            
            SelectBox selectDept = new SelectBox("selectDept");
            selectDept.setMultiple(false);
            selectDept.setOptionMap(getAttendingDeptOptionsMap());
            TableFilter deptFilter = new TableFilter("deptFilter");
            deptFilter.setWidget(selectDept);
            addFilter(deptFilter);
            
            SelectBox selectRequestType = new SelectBox("selectRequestType");
            selectRequestType.setMultiple(false);
            selectRequestType.setOptionMap(getRequestTypeOptionsMap());
            TableFilter requestTypeFilter = new TableFilter("requestTypeFilter");
            requestTypeFilter.setWidget(selectRequestType);
            addFilter(requestTypeFilter);
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
		
		private Map getPriorityOptionsMap() {
			Map optionsMap = new SequencedHashMap();
			
			Application application = Application.getInstance();
			ConfigModel configModel = (ConfigModel)application.getModule(ConfigModel.class);
			
			Collection priorityCols = configModel.getConfigDetailsByType(ConfigDetailObject.PRIORITY, null);
			optionsMap.put("", "---" + application.getMessage("isr.label.allPriorities") + "---");
			for(Iterator i=priorityCols.iterator(); i.hasNext(); ) {
				ConfigDetailObject config = (ConfigDetailObject) i.next();
				optionsMap.put(config.getConfigDetailName(), config.getConfigDetailName());
			}
			
			return optionsMap;
		}
		
		private Map getSearchAttributesMap() {
			Map optionsMap = new SequencedHashMap();
			Application application = Application.getInstance();
			
			optionsMap.put("", "---" + application.getMessage("isr.label.all") + "---");
			optionsMap.put("requestId", application.getMessage("isr.label.requestId"));
			optionsMap.put("requestSubject", application.getMessage("isr.label.subject"));
			optionsMap.put("createdBy", application.getMessage("isr.label.requesterName"));
			
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
		
		public Map getRequestTypeOptionsMap() {
			Application application = Application.getInstance();
			ConfigModel model = (ConfigModel)application.getModule(ConfigModel.class);
			ConfigDetailObject config = new ConfigDetailObject();
			Map optionsMap = new SequencedHashMap();
			Collection cols = model.getConfigDetailsByType(ConfigDetailObject.REQUEST_TYPE, null);
			
			optionsMap.put("", "---" + application.getMessage("isr.label.allRequestType") + "---");
			for(Iterator i=cols.iterator(); i.hasNext();) {
				config = (ConfigDetailObject) i.next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					optionsMap.put(config.getConfigDetailName(), config.getConfigDetailName());
				}
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
			String assigneeName = (String) getFilterValue("assigneeNameFilter");
			
			SelectBox selectAttribute = (SelectBox) getFilter("attributeFilter").getWidget();
            List selectAttributeCols = (List) selectAttribute.getValue();
            String selectedSearchAttribute = "";
            if (selectAttributeCols.size() > 0) {
            	selectedSearchAttribute = (String) selectAttributeCols.get(0);
            }
			
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
            
			SelectBox selectPriority = (SelectBox) getFilter("priorityFilter").getWidget();
            List selectPriorityCols = (List) selectPriority.getValue();
            String selectedPriority = "";
            if (selectPriorityCols.size() > 0) {
            	selectedPriority = (String) selectPriorityCols.get(0);
            }
			
            SelectBox selectDept = (SelectBox) getFilter("deptFilter").getWidget();
            List selectDeptCols = (List) selectDept.getValue();
            String selectedDept = "";
            if (selectDeptCols.size() > 0) {
            	selectedDept = (String) selectDeptCols.get(0);
            }
            
            SelectBox selectRequestType = (SelectBox) getFilter("requestTypeFilter").getWidget();
            List selectRequestTypeCols = (List) selectRequestType.getValue();
            String selectedRequestType = "";
            if (selectRequestTypeCols.size() > 0) {
            	selectedRequestType = (String) selectRequestTypeCols.get(0);
            }
			
			return requestModel.selectAttendingRequest(searchBy, selectedSearchAttribute, selectedStatus, selectedPriority, selectedDept, selectedRequestType, assigneeName, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount(){
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			String searchBy = (String) getFilterValue("searchFilter");
			String assigneeName = (String) getFilterValue("assigneeNameFilter");
			
			SelectBox selectAttribute = (SelectBox) getFilter("attributeFilter").getWidget();
            List selectAttributeCols = (List) selectAttribute.getValue();
            String selectedSearchAttribute = "";
            if (selectAttributeCols.size() > 0) {
            	selectedSearchAttribute = (String) selectAttributeCols.get(0);
            }
			
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
            
			SelectBox selectPriority = (SelectBox) getFilter("priorityFilter").getWidget();
            List selectPriorityCols = (List) selectPriority.getValue();
            String selectedPriority = "";
            if (selectPriorityCols.size() > 0) {
            	selectedPriority = (String) selectPriorityCols.get(0);
            }
			
            SelectBox selectDept = (SelectBox) getFilter("deptFilter").getWidget();
            List selectDeptCols = (List) selectDept.getValue();
            String selectedDept = "";
            if (selectDeptCols.size() > 0) {
            	selectedDept = (String) selectDeptCols.get(0);
            }
            
            SelectBox selectRequestType = (SelectBox) getFilter("requestTypeFilter").getWidget();
            List selectRequestTypeCols = (List) selectRequestType.getValue();
            String selectedRequestType = "";
            if (selectRequestTypeCols.size() > 0) {
            	selectedRequestType = (String) selectRequestTypeCols.get(0);
            }
			
			intTotalRow = requestModel.selectAttendingRequestCount(searchBy, selectedSearchAttribute, selectedStatus, selectedPriority, selectedDept, selectedRequestType, assigneeName);
			return intTotalRow;
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
