package com.tms.collab.isr.report.ui;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.isr.report.model.ReportModel;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class TimeOfResolveReportDetailTable extends Table{
	
	private Date fromDate;
	private Date toDate;
	private String condition;
	private String statusId;
	
	public TimeOfResolveReportDetailTable(){
		
	}
	
	public TimeOfResolveReportDetailTable(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setPageSize(50);
        setModel(new StatusReportDetailTableModel());
        setWidth("100%");
	}
	
	public class StatusReportDetailTableModel extends TableModel{
		
		public StatusReportDetailTableModel(){
			
			Application app = Application.getInstance();
			
			TableColumn reqId = new TableColumn("requestId", app.getMessage("isr.label.requestId"));
			reqId.setSortable(false);
			addColumn(reqId);
			
			TableColumn date = new TableColumn("dateCreated", app.getMessage("isr.label.date"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			date.setFormat(dateCreatedFormat);
			date.setSortable(false);
			addColumn(date);
			
			TableColumn status = new TableColumn("status", app.getMessage("isr.label.status"));
			addColumn(status);
			
			TableColumn reqDept = new TableColumn("createdBy", app.getMessage("isr.label.requesterName"));
			reqDept.setSortable(false);
			addColumn(reqDept);
			
			TableColumn priority = new TableColumn("requestPriority", app.getMessage("isr.label.priorityByRequester"));
			priority.setSortable(false);
			addColumn(priority);
						
			TableColumn subject = new TableColumn("requestSubject", app.getMessage("isr.label.subject"));
			subject.setSortable(false);
			addColumn(subject);
			
		}
		
		public int getTotalRowCount(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			OrgChartHandler orgChartModel = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
			
			DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(Application.getInstance().getCurrentUser().getId());
			
			return model.getTimeOfResolveReqListingCount(fromDate, toDate, condition, requesterDept.getAssociativityId(), statusId);
		}
		
		public Collection getTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			OrgChartHandler orgChartModel = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
			
			DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(Application.getInstance().getCurrentUser().getId());
			
			return model.getTimeOfResolveReqListing(fromDate, toDate, condition, requesterDept.getAssociativityId(), statusId); 
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			
			return new Forward("error");
		}
		
		public String getTableRowKey() {
            return "id";
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

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getDefaultTemplate(){
		return "isr/popupStatusReport";
	}
	
}
