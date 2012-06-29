package com.tms.collab.isr.report.ui;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import com.tms.collab.isr.report.model.ReportModel;

public class StaffReportDetailTable extends Table{
	
	private Date fromDate;
	private Date toDate;
	private String linkId;
	
	private String staffName;
	
	public StaffReportDetailTable(){
		
	}
	
	public StaffReportDetailTable(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setPageSize(50);
        setModel(new StaffReportDetailTableModel());
        setWidth("100%");
	}
	
	public class StaffReportDetailTableModel extends TableModel{
		
		public StaffReportDetailTableModel(){
			
			Application app = Application.getInstance();
			
			TableColumn reqId = new TableColumn("requestId", app.getMessage("isr.label.requestId"));
			reqId.setSortable(false);
			addColumn(reqId);
			
			TableColumn date = new TableColumn("dateCreated", app.getMessage("isr.label.date"));
			TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
			date.setFormat(dateCreatedFormat);
			date.setSortable(false);
			addColumn(date);
			
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
			String assignee = new String();
			String status = new String();
			
			int firstIndex = linkId.lastIndexOf("*");
			
			if(firstIndex > 0){
				assignee = linkId.substring(0, firstIndex);
				status = linkId.substring(firstIndex+1, linkId.length());
			}else{
				status = "";
				assignee = linkId;
			}
			
			return model.getStaffReportDetailListingCount(fromDate, toDate, status, assignee);
		}
		
		public Collection getTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			String assignee = new String();
			String status = new String();
			
			int firstIndex = linkId.lastIndexOf("*");
			
			if(firstIndex > 0){
				assignee = linkId.substring(0, firstIndex);
				status = linkId.substring(firstIndex+1, linkId.length());
			}else{
				status = "";
				assignee = linkId;
			}
			
			return model.getStaffReportDetailListing(fromDate, toDate, status, assignee); 
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

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		 
		SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
		int firstIndex = linkId.lastIndexOf("*");
		String assignee = new String();
		
		if(firstIndex > 0){
			assignee = linkId.substring(0, firstIndex);
		}else{
			assignee = linkId;
		}
		
		try{
			User user = ss.getUser(assignee);
			staffName = user.getName();
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in getting user in StaffReportDetailTable: " + e.getMessage());
		}
		
		this.linkId = linkId;
	}
	
	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getDefaultTemplate(){
		return "isr/popupPriorityReport";
	}

}
