package com.tms.sam.po.report.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.sam.po.report.model.ReportModule;

public class StatusReportListing extends Table{
	
	private Date fromDate;
	private Date toDate;
	
	public StatusReportListing(){
		
	}
	
	public StatusReportListing(String name){
		super(name);
	}
	
	public void init(){
		super.init();
       
        setModel(new StatusReportListingModel());
        setWidth("100%");
	}
	
	public class StatusReportListingModel extends TableModel{
		private DatePopupField dfFromDate;
		private DatePopupField dfToDate;
		private SelectBox sbDept;
		private SelectBox sbStatus;
		
		public StatusReportListingModel() {
			Application app = Application.getInstance();
			
			TableColumn dept = new TableColumn("deptDesc", app.getMessage("po.label.department"));
			addColumn(dept);
			
			TableColumn status = new TableColumn("status", app.getMessage("myRequest.label.status"));
			addColumn(status);
			
			TableColumn totalRequest = new TableColumn("totalRequest", app.getMessage("report.label.totalRequest"));
			addColumn(totalRequest);
			
			dfFromDate = new DatePopupField("fromDate");
			dfFromDate.setOptional(true);
			dfFromDate.setSize("7");
			TableFilter fromDateFilter = new TableFilter("fromDateFilter");
			fromDateFilter.setWidget(dfFromDate);
			addFilter(fromDateFilter);
			
			dfToDate = new DatePopupField("toDate");
			dfToDate.setOptional(true);
			dfToDate.setSize("7");
			TableFilter toDateFilter = new TableFilter("toDateFilter");
			toDateFilter.setWidget(dfToDate);
			addFilter(toDateFilter);
			
			Calendar now = Calendar.getInstance();
			dfToDate.setDate(now.getTime());
			toDate = now.getTime();
			
			now.add(Calendar.MONTH, -1);
			dfFromDate.setDate(now.getTime());
			fromDate = now.getTime();
			
			sbDept = new SelectBox("sbDept");
			sbDept.setOptionMap(getAttendingDeptOptionsMap());
			TableFilter requestingDeptFilter = new TableFilter("requestingDeptFilter");
			requestingDeptFilter.setWidget(sbDept);
			addFilter(requestingDeptFilter);
			
			sbStatus = new SelectBox("sbStatus");
			sbStatus.setOptionMap(getStatusOptionsMap());
			TableFilter statusFilter = new TableFilter("statusFilter");
			statusFilter.setWidget(sbStatus);
			addFilter(statusFilter);
			
		}
		
		@Override
		public Collection getTableRows() {
			ReportModule module = (ReportModule) Application.getInstance().getModule(ReportModule.class);
			
			String tempFromDate = (String) getFilterValue("fromDateFilter");
			String tempToDate = (String) getFilterValue("toDateFilter");
			
			String status = new String();
			SelectBox tempStatus = (SelectBox) getFilter("statusFilter").getWidget();
			List statusList = (List) tempStatus.getValue();
            if (statusList.size() > 0) {
            	status = (String) statusList.get(0);
            } else {
            	status = "";
            }
            
			String reqDept = new String();
			SelectBox tempReqDept = (SelectBox) getFilter("requestingDeptFilter").getWidget();
			List reqDeptList = (List) tempReqDept.getValue();
            if (reqDeptList.size() > 0) {
            	reqDept = (String) reqDeptList.get(0);
            } else {
            	reqDept = "";
            }
			
            DatePopupField tempFilterFromDate = (DatePopupField)getFilter("fromDateFilter").getWidget();
            DatePopupField tempFilterToDate = (DatePopupField)getFilter("toDateFilter").getWidget();
			
			fromDate = tempFilterFromDate.getDate();
			toDate = tempFilterToDate.getDate();
            Collection data = module.getStatusListing(tempFromDate, tempToDate, status, reqDept, getSort(), isDesc(), getStart(), getRows());; 
            Application.getThreadRequest().getSession().setAttribute("reportData", data);
            Application.getThreadRequest().getSession().setAttribute("reportFromDate", fromDate);
            Application.getThreadRequest().getSession().setAttribute("reportToDate", toDate);
            Application.getThreadRequest().getSession().setAttribute("reportReqDept", reqDept);
            Application.getThreadRequest().getSession().setAttribute("reportStatus", status);
            
			return data;
		}
		
		@Override
		public int getTotalRowCount() {
			ReportModule module = (ReportModule) Application.getInstance().getModule(ReportModule.class);
			
			String tempFromDate = (String) getFilterValue("fromDateFilter");
			String tempToDate = (String) getFilterValue("toDateFilter");
			
			String status = new String();
			SelectBox tempStatus = (SelectBox) getFilter("statusFilter").getWidget();
			List statusList = (List) tempStatus.getValue();
            if (statusList.size() > 0) {
            	status = (String) statusList.get(0);
            } else {
            	status = "";
            }
            
			String reqDept = new String();
			SelectBox tempReqDept = (SelectBox) getFilter("requestingDeptFilter").getWidget();
			List reqDeptList = (List) tempReqDept.getValue();
            if (reqDeptList.size() > 0) {
            	reqDept = (String) reqDeptList.get(0);
            } else {
            	reqDept = "";
            }
			return module.countStatusListing(tempFromDate, tempToDate, status, reqDept); 
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
		
		public Map getStatusOptionsMap() {
		
			Map optionsMap = new SequencedHashMap();
			
			optionsMap.put("", "---All Status---");
			optionsMap.put("Draft","Draft");
			optionsMap.put("New","New");
			optionsMap.put("Withdrawn","Withdrawn");
			optionsMap.put("Resubmitted","Resubmitted");
			optionsMap.put("Approved by HOD","Approved by HOD");
			optionsMap.put("Rejected by HOD","Rejected by HOD");
			optionsMap.put("Quoted","Quoted");
			optionsMap.put("Approved by BO","Approved by BO");
			optionsMap.put("Rejected by BO","Rejected by BO");
			optionsMap.put("Re-Quote","Re-Quote");
			optionsMap.put("PO Fulfilled","PO FulFilled");
			optionsMap.put("Closed","Closed");
			return optionsMap;
	    }
	}
	
	@Override
	public String getDefaultTemplate() {
		return "po/statusReport";
	}
	
}
