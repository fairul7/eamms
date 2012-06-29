package com.tms.collab.isr.report.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.report.model.ReportModel;
import com.tms.collab.isr.report.model.ReportObject;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class StaffReportTable extends Table{
	
	private String departmentName;
	private Date fromDate;
	private Date toDate;
	
	public StaffReportTable(){
		
	}
	
	public StaffReportTable(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setPageSize(50);
        setModel(new StaffReportTableModel());
        setWidth("100%");
        
	}
	
	public class StaffReportTableModel extends TableModel{
		
		private DatePopupField dfFromDate;
		private DatePopupField dfToDate;
		
		public StaffReportTableModel(){
			
			Application app = Application.getInstance();
			
			TableColumn reqDept = new TableColumn("staffName", app.getMessage("isr.label.staffName"));
			reqDept.setSortable(false);
			addColumn(reqDept);
			
			Map statusMap = getStatusOptionsMap();
			
			TableColumn noOfReqNew = new TableColumn("noOfReqNew", (String)statusMap.get(StatusObject.STATUS_ID_NEW));
			noOfReqNew.setSortable(false);
			noOfReqNew.setUrlParam("reportLinkNew");
			addColumn(noOfReqNew);
			
			TableColumn noOfReqInProgress = new TableColumn("noOfReqInProgress", (String)statusMap.get(StatusObject.STATUS_ID_IN_PROGRESS));
			noOfReqInProgress.setSortable(false);
			noOfReqInProgress.setUrlParam("reportLinkInProgress");
			addColumn(noOfReqInProgress);
			
			TableColumn noOfReqCompleted = new TableColumn("noOfReqCompleted", (String)statusMap.get(StatusObject.STATUS_ID_COMPLETED));
			noOfReqCompleted.setSortable(false);
			noOfReqCompleted.setUrlParam("reportLinkCompleted");
			addColumn(noOfReqCompleted);
			
			TableColumn noOfReqClose = new TableColumn("noOfReqClose", (String)statusMap.get(StatusObject.STATUS_ID_CLOSE));
			noOfReqClose.setSortable(false);
			noOfReqClose.setUrlParam("reportLinkClose");
			addColumn(noOfReqClose);
			
			TableColumn noOfReqReopen = new TableColumn("noOfReqReopen", (String)statusMap.get(StatusObject.STATUS_ID_REOPEN));
			noOfReqReopen.setSortable(false);
			noOfReqReopen.setUrlParam("reportLinkReopen");
			addColumn(noOfReqReopen);
			
			TableColumn noOfReq = new TableColumn("noOfReq", app.getMessage("isr.label.total"));
			noOfReq.setSortable(false);
			noOfReq.setUrlParam("reportLink");
			addColumn(noOfReq);
			
			dfFromDate = new DatePopupField("fromDate");
			dfFromDate.setOptional(true);
			TableFilter fromDateFilter = new TableFilter("fromDateFilter");
			fromDateFilter.setWidget(dfFromDate);
			addFilter(fromDateFilter);
			
			dfToDate = new DatePopupField("toDate");
			dfToDate.setOptional(true);
			TableFilter toDateFilter = new TableFilter("toDateFilter");
			toDateFilter.setWidget(dfToDate);
			addFilter(toDateFilter);
			
			Calendar now = Calendar.getInstance();
			dfToDate.setDate(now.getTime());
			dfToDate.setSize("7");
			toDate = now.getTime();
			
			now.add(Calendar.MONTH, -1);
			dfFromDate.setDate(now.getTime());
			dfFromDate.setSize("7");
			fromDate = now.getTime();
			
			addAction(new TableAction("print", app.getMessage("isr.label.print")));
            //addAction(new TableAction("exportCsv", "Export to CSV"));
            
            RequestModel requestModel = (RequestModel) Application.getInstance().getModule(RequestModel.class);
            departmentName = requestModel.getDeptName(Application.getInstance().getCurrentUser().getId());
			
		}
		
		private Map getStatusOptionsMap() {
			Map optionsMap = new SequencedHashMap();
			
			Application application = Application.getInstance();
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			
			Collection statusCols = requestModel.selectAllStatus();
			optionsMap.put("", "---" + application.getMessage("isr.label.allStatus") + "---");
			for(Iterator i=statusCols.iterator(); i.hasNext(); ) {
				StatusObject status = (StatusObject) i.next();
				optionsMap.put(status.getStatusId(), status.getStatusName());
			}
			
			return optionsMap;
		}
		
		public int getTotalRowCount(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			OrgChartHandler orgChartModel = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
			DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(Application.getInstance().getCurrentUser().getId());
			
			String fromDate = (String) getFilterValue("fromDateFilter");
			String toDate = (String) getFilterValue("toDateFilter");
			
			DatePopupField tempFilterFromDate = (DatePopupField)getFilter("fromDateFilter").getWidget();
			DatePopupField tempFilterToDate = (DatePopupField)getFilter("toDateFilter").getWidget();
			
            Application.getThreadRequest().getSession().setAttribute("reportData", getAllTableRows());
            Application.getThreadRequest().getSession().setAttribute("reportFromDate", tempFilterFromDate.getDate());
            Application.getThreadRequest().getSession().setAttribute("reportToDate", tempFilterToDate.getDate());
            return model.getStaffReportListingCount(fromDate, toDate, requesterDept.getAssociativityId());
		}
		
		public Collection getTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			OrgChartHandler orgChartModel = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
			DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(Application.getInstance().getCurrentUser().getId());
			
			String fromDate = (String) getFilterValue("fromDateFilter");
			String toDate = (String) getFilterValue("toDateFilter");
			
            Collection result =  model.getStaffReportListing(fromDate, toDate, requesterDept.getAssociativityId(), getSort(), isDesc(), getStart(), getRows());
            List resultList = (List)result;
            Comparator com = new ReportSort(false);
            Collections.sort(resultList, com);
            return resultList;
		}
		
		public Collection getAllTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			OrgChartHandler orgChartModel = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
			DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(Application.getInstance().getCurrentUser().getId());
			
			String tempFromDate = (String) getFilterValue("fromDateFilter");
			String tempToDate = (String) getFilterValue("toDateFilter");
			
			DatePopupField tempFilterFromDate = (DatePopupField)getFilter("fromDateFilter").getWidget();
			DatePopupField tempFilterToDate = (DatePopupField)getFilter("toDateFilter").getWidget();
			
			fromDate = tempFilterFromDate.getDate();
			toDate = tempFilterToDate.getDate();
            
			Collection result =  model.getStaffReportListing(tempFromDate, tempToDate, requesterDept.getAssociativityId(), getSort(), isDesc(), 0, -1);
			List resultList = (List)result;
            Comparator com = new ReportSort(false);
            Collections.sort(resultList, com);
            return resultList;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			
			if(action.equalsIgnoreCase("print")){
				
				DatePopupField tempFilterFromDate = (DatePopupField)getFilter("fromDateFilter").getWidget();
				DatePopupField tempFilterToDate = (DatePopupField)getFilter("toDateFilter").getWidget();
				
				fromDate = tempFilterFromDate.getDate();
				toDate = tempFilterToDate.getDate();
				
				return new Forward("print");
			}
			/*else if(action.equalsIgnoreCase("exportCsv")){
				evt.getRequest().getSession().setAttribute("reportData", getAllTableRows());
				return new Forward("csv");
			}*/
			
			return new Forward("error");
		}
		
		public String getTableRowKey() {
            return "id";
        }
		
		public class ReportSort implements Comparator
		{
		    private boolean descending;

		    public ReportSort(boolean descending)
		    {
		        this.descending = descending;
		    }

		    public int compare(Object o1, Object o2)
		    {
		    	ReportObject obj = (ReportObject)o1;
		    	ReportObject obj2 = (ReportObject)o2;
		    	return obj.getStaffName().compareTo(obj2.getStaffName());
		    }
		}
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getDefaultTemplate(){
		return "isr/staffReport";
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
