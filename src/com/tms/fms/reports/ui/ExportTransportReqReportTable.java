package com.tms.fms.reports.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map; 

import com.tms.fms.reports.model.ReportsFmsModule;  
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
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
import kacang.util.Log;

public class ExportTransportReqReportTable extends Table{
	public DatePopupField reportDateFrom;
	public DatePopupField reportDateTo;
	public SelectBox sbProgram;
	public Collection tableRows=new ArrayList();
	
	private Date startDate;
	private Date endDate;
	private String programId;
	public String requestTypeId;
	public SelectBox sbRequestType;
	
	ExportTransportReqReportTableModel transportReqReportTableModel ;
	
	private Collection tableRowsData;
	private int totalRowCountData;
	
	public void onRequest(Event event) {
		transportReqReportTableModel = new ExportTransportReqReportTableModel();
        setModel(transportReqReportTableModel);
		
		init();
		
		startDate=(Date)event.getRequest().getSession().getAttribute("fromDateTransport");
		endDate=(Date)event.getRequest().getSession().getAttribute("toDateTransport");
		programId=(String)event.getRequest().getSession().getAttribute("programTransport");
		requestTypeId=(String)event.getRequest().getSession().getAttribute("transReqType");
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		transportReqReportTableModel = new ExportTransportReqReportTableModel();
        setModel(transportReqReportTableModel);
	}
	
	
	 
	public class ExportTransportReqReportTableModel extends TableModel{
		public ExportTransportReqReportTableModel(){
			
			TableColumn column2Assignment = new TableColumn("transAssignmentDateStrMod","Assignment Date");
			addColumn(column2Assignment);
			TableColumn column3 = new TableColumn("department",Application.getInstance().getMessage("fms.report.transport.message.department"));
			addColumn(column3);
			TableColumn column11 = new TableColumn("driversAssigned",Application.getInstance().getMessage("fms.report.transport.message.driverassigned"));
			addColumn(column11);
			TableColumn column12 = new TableColumn("vehicleAssigned",Application.getInstance().getMessage("fms.report.transport.message.vehicleassigned"));
			addColumn(column12);
			TableColumn column2 = new TableColumn("fullname",Application.getInstance().getMessage("fms.report.transport.message.requestname"));
			addColumn(column2);
			TableColumn column5 = new TableColumn("purpose",Application.getInstance().getMessage("fms.report.transport.message.purpose"));
			addColumn(column5);
			TableColumn column6 = new TableColumn("destination",Application.getInstance().getMessage("fms.report.transport.message.destination"));
			addColumn(column6);
			TableColumn column13 = new TableColumn("singleMeterStart",Application.getInstance().getMessage("fms.report.transport.message.meterstart"));
			addColumn(column13);
			TableColumn column14 = new TableColumn("singleMeterEnd",Application.getInstance().getMessage("fms.report.transport.message.meterend"));
			addColumn(column14);
			TableColumn column15 = new TableColumn("singleTotalMeter",Application.getInstance().getMessage("fms.report.transport.message.totalmileage"));
			addColumn(column15);
			TableColumn column16 = new TableColumn("singleCheckInDate",Application.getInstance().getMessage("fms.report.transport.message.timein"));
			addColumn(column16);
			TableColumn column17 = new TableColumn("singleCheckOutDate",Application.getInstance().getMessage("fms.report.transport.message.timeout"));
			addColumn(column17);
			TableColumn column18 = new TableColumn("singleTimeRec",Application.getInstance().getMessage("fms.report.transport.message.timerec"));
			addColumn(column18);
			TableColumn remarks = new TableColumn("remarks", Application.getInstance().getMessage("fms.report.transport.message.remark"));
			addColumn(remarks);
			TableColumn column1 = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.report.transport.message.requesttitle"));
			addColumn(column1);
			TableColumn column4 = new TableColumn("requestDateStr",Application.getInstance().getMessage("fms.report.transport.message.requestdate"));
			addColumn(column4);
			TableColumn columntype = new TableColumn("program",Application.getInstance().getMessage("fms.request.label.requestType"));
			addColumn(columntype);
			TableColumn column10 = new TableColumn("transStatus",Application.getInstance().getMessage("fms.report.transport.message.requeststatus"));
			column10.setFormat(new TableFormat(){
					
					public String format(Object obj){
						ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
						SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
						String statusNo = obj.toString()!=null&&!obj.toString().equals("")?obj.toString().split("/")[0]:"";	
						String requestId = obj.toString()!=null&&!obj.toString().equals("")?obj.toString().split("/")[1]:"";	
						String status = "";					
						TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
						//checks if status is assigned 
						if(module.getAssignedStatus(requestId)){
							return Application.getInstance().getMessage("fms.report.transport.message.TA","TA");
						}
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.PROCESS_STATUS)){
							return Application.getInstance().getMessage("fms.report.transport.message.hodprocess","HOD Processed");
						}
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.CANCELLED_STATUS)){
							return Application.getInstance().getMessage("fms.report.status.batal","Batal");
						}
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.REJECTED_STATUS)){
							return Application.getInstance().getMessage("fms.report.status.batal","Batal");
						}
						
						status = TM.selectStatus(statusNo);					
						
			    		return status;		
						
					}
				});
			addColumn(column10);
			TableColumn column7 = new TableColumn("outsourceFlag",Application.getInstance().getMessage("fms.report.transport.message.outsourceflag"));
			addColumn(column7);
			TableColumn column8 = new TableColumn("singleDriverStatus",Application.getInstance().getMessage("fms.report.transport.message.driverstatus"));
			column8.setFormat(new TableFormat(){
				
				public String format(Object obj){
					String statusNo = obj.toString();
					String status="";
					TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
					status = TM.selectStatus(statusNo);
					if(statusNo.equals("0")){
						return Application.getInstance().getMessage("fms.report.status.notAssigned","Not Assigned");
					}
					if(statusNo.equals("1")){
						return Application.getInstance().getMessage("fms.report.status.assigned","Assigned");
					}
					if(statusNo.equals("2")){
						return Application.getInstance().getMessage("fms.report.status.pandusendiri","Pandu Sendiri");
					}
					if(statusNo.equals(SetupModule.UNFULFILLED_STATUS)){
						return Application.getInstance().getMessage("fms.report.status.nonews","No News");
					}
					
		    		return status;		
					
				}
			});
			addColumn(column8);
			TableColumn column9 = new TableColumn("singleVehicleStatus",Application.getInstance().getMessage("fms.report.transport.message.vehiclestatus"));
			column9.setFormat(new TableFormat(){
				
				public String format(Object obj){
					String statusNo = obj.toString();
					String status="";
					TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
					status = TM.selectStatus(statusNo);	
					if(statusNo.equals("0")){
						return Application.getInstance().getMessage("fms.report.status.notAssigned","Not Assigned");
					}
					
					if(statusNo.equals("1")){
						return Application.getInstance().getMessage("fms.report.status.assigned","Assigned");
					}
					
					if(statusNo.equals(SetupModule.UNFULFILLED_STATUS)){
						return Application.getInstance().getMessage("fms.report.status.nonews","No News");
					}
		    		return status;		
					
				}
			});
			
			addColumn(column9);
			TableColumn reqNo = new TableColumn("reqAsssignId", Application.getInstance().getMessage("fms.report.transport.message.reqNoOrAssgnNo"));
			addColumn(reqNo);
			TableColumn progName = new TableColumn("programName", Application.getInstance().getMessage("fms.report.transport.message.programName"));
			addColumn(progName);
			TableColumn pfeCode = new TableColumn("pfeCode", Application.getInstance().getMessage("fms.report.transport.message.pfeCode"));
			addColumn(pfeCode);
		}
		
		public Collection getTableRows() {
			return tableRowsData;
		}

		public int getTotalRowCount() {
			return totalRowCountData;
		}
			
	}
	
	@Override
	public String getDefaultTemplate(){
		return "fms/reports/exportTableReport";
	}

	public DatePopupField getReportDateFrom() {
		return reportDateFrom;
	}

	public void setReportDateFrom(DatePopupField reportDateFrom) {
		this.reportDateFrom = reportDateFrom;
	}

	public DatePopupField getReportDateTo() {
		return reportDateTo;
	}

	public void setReportDateTo(DatePopupField reportDateTo) {
		this.reportDateTo = reportDateTo;
	}

	public SelectBox getSbProgram() {
		return sbProgram;
	}

	public void setSbProgram(SelectBox sbProgram) {
		this.sbProgram = sbProgram;
	}

	public ExportTransportReqReportTableModel getTransportReqReportTableModel() {
		return transportReqReportTableModel;
	}

	public void setTransportReqReportTableModel(
			ExportTransportReqReportTableModel transportReqReportTableModel) {
		this.transportReqReportTableModel = transportReqReportTableModel;
	}

	public Collection getTableRows() {
		return tableRows;
	}

	public void setTableRows(Collection tableRows) {
		this.tableRows = tableRows;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public SelectBox getSbRequestType() {
		return sbRequestType;
	}

	public void setSbRequestType(SelectBox sbRequestType) {
		this.sbRequestType = sbRequestType;
	}

	public String getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(String requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Collection getTableRowsData() {
		return tableRowsData;
	}

	public void setTableRowsData(Collection tableRowsData) {
		this.tableRowsData = tableRowsData;
	}

	public int getTotalRowCountData() {
		return totalRowCountData;
	}

	public void setTotalRowCountData(int totalRowCountData) {
		this.totalRowCountData = totalRowCountData;
	}

}
