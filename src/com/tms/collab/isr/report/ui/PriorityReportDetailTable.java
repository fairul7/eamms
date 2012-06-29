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

public class PriorityReportDetailTable extends Table{
	
	private Date fromDate;
	private Date toDate;
	private String linkId;
	
	public PriorityReportDetailTable(){
		
	}
	
	public PriorityReportDetailTable(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setPageSize(50);
        setModel(new PriorityReportDetailTableModel());
        setWidth("100%");
	}
	
	public class PriorityReportDetailTableModel extends TableModel{
		
		public PriorityReportDetailTableModel(){
			
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
			
			TableColumn assignee = new TableColumn("assignee", app.getMessage("isr.label.assignee"));
			assignee.setSortable(false);
			addColumn(assignee);
			
		}
		
		public int getTotalRowCount(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			String reqDept = new String();
			String recDept = new String();
			String priority = new String();
			
			int firstIndex = linkId.indexOf("_");
			int secondIndex = linkId.lastIndexOf("_");
			
			reqDept = linkId.substring(0, firstIndex);
			recDept = linkId.substring(firstIndex+1, secondIndex);
			priority = linkId.substring(secondIndex+1, linkId.length());
			
			return model.getPriorityReportDetailListingCount(fromDate, toDate, priority, reqDept, recDept);
		}
		
		public Collection getTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			String reqDept = new String();
			String recDept = new String();
			String priority = new String();
			
			int firstIndex = linkId.indexOf("_");
			int secondIndex = linkId.lastIndexOf("_");
			
			reqDept = linkId.substring(0, firstIndex);
			recDept = linkId.substring(firstIndex+1, secondIndex);
			priority = linkId.substring(secondIndex+1, linkId.length());
			
			return model.getPriorityReportDetailListing(fromDate, toDate, priority, reqDept, recDept); 
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
		this.linkId = linkId;
	}

	public String getDefaultTemplate(){
		return "isr/popupPriorityReport";
	}

}
