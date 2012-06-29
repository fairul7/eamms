package com.tms.fms.reports.ui;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.setup.model.ProgramObject;

public class ExportEngineeringReqReportTable extends Table{
	public DatePopupField bookingDateFrom;
	public DatePopupField bookingDateTo;
	public SelectBox sbProgram;
	public SelectBox sbRequestType;
	public SelectBox sbStatus;
	
	public Date requestStartDate;
	public Date requestEndDate;
	public String requestProgramId;
	public String requestTypeId;
	public String requestStatus;
	
	ExportEngineeringReqReportTableModel exportEngineeringReqReportTableModel ;
	
	public void onRequest(Event event) {
		exportEngineeringReqReportTableModel = new ExportEngineeringReqReportTableModel();
        setModel(exportEngineeringReqReportTableModel);
		Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/reports/engReqFilterFormTemplate");
		
		init();
		
		requestStartDate=(Date)event.getRequest().getSession().getAttribute("fromDateRequest");
		requestEndDate=(Date)event.getRequest().getSession().getAttribute("toDateRequest");
		requestProgramId=(String)event.getRequest().getSession().getAttribute("programRequest");
		requestTypeId=(String)event.getRequest().getSession().getAttribute("requestTypeRequest");
		requestStatus=(String)event.getRequest().getSession().getAttribute("statusRequest");
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		exportEngineeringReqReportTableModel = new ExportEngineeringReqReportTableModel();
        setModel(exportEngineeringReqReportTableModel);
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/reports/engReqFilterFormTemplate");
	}
	 
	public class ExportEngineeringReqReportTableModel extends TableModel{
		public ExportEngineeringReqReportTableModel(){
			
			TableColumn column1 = new TableColumn("requestId",Application.getInstance().getMessage("fms.report.request.message.requestid"));
			addColumn(column1);
			TableColumn column2 = new TableColumn("requestType",Application.getInstance().getMessage("fms.report.request.message.requesttype"));
			column2.setFormat(new TableFormat(){
				public String format(Object obj){
					String value=obj.toString();
					if(value.equals(EngineeringModule.REQUEST_TYPE_INTERNAL)){
						return Application.getInstance().getMessage("fms.facility.requestType.I");
					}else if(value.equals(EngineeringModule.REQUEST_TYPE_EXTERNAL)){
						return Application.getInstance().getMessage("fms.facility.requestType.E");
					}else if(value.equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM)){
						return Application.getInstance().getMessage("fms.facility.requestType.N");
					}else{
						return "";
					}
				}
			});
			addColumn(column2);
			TableColumn column3 = new TableColumn("programType",Application.getInstance().getMessage("fms.report.request.message.programtype"));
			column3.setFormat(new TableFormat(){
				public String format(Object obj){
					String value=obj.toString();
					if(value.equals(EngineeringModule.PROGRAM_TYPE_LIVE)){
						return Application.getInstance().getMessage("fms.facility.programType.L");
					}else if(value.equals(EngineeringModule.PROGRAM_TYPE_RECORDING)){
						return Application.getInstance().getMessage("fms.facility.programType.R");
					}else{
						return "";
					}
				}
			});
			addColumn(column3);
			TableColumn column4 = new TableColumn("program",Application.getInstance().getMessage("fms.report.request.message.programtitle"));
			addColumn(column4);
			TableColumn column5 = new TableColumn("bookFrom",Application.getInstance().getMessage("fms.report.request.message.bookingfrom"));
			column5.setFormat(new TableDateFormat("dd/MM/yyyy"));
			addColumn(column5);
			TableColumn column6 = new TableColumn("bookTo",Application.getInstance().getMessage("fms.report.request.message.bookingto"));
			column6.setFormat(new TableDateFormat("dd/MM/yyyy"));
			addColumn(column6);
			
			TableColumn column8 = new TableColumn("createdBy",Application.getInstance().getMessage("fms.report.request.message.requestorname"));
			column8.setFormat(new TableFormat(){
				public String format(Object obj){
					String username=obj.toString();
					EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
					return dao.getFullName(username);
				}
			});
			addColumn(column8);
			TableColumn column9 = new TableColumn("department",Application.getInstance().getMessage("fms.report.request.message.departmentrequest"));
			addColumn(column9);
			TableColumn column10 = new TableColumn("title",Application.getInstance().getMessage("fms.report.request.message.requesttitle"));
			addColumn(column10);
			TableColumn column11 = new TableColumn("request",Application.getInstance().getMessage("fms.report.request.message.totalfacility"));
			column11.setFormat(new TableFormat(){
				public String format(Object obj){
					ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
						
					String total="";
					String requestId="";
					String requestType="";
					if(obj!=null && !obj.toString().equals("")){
						if(obj.toString().split("/")!=null && obj.toString().split("/").length>0){
							requestId=obj.toString().split("/")[0];
							requestType=obj.toString().split("/")[1];
						}
					}
					
					Double totaldoub=module.getTotalFacility(requestId, requestType);
					String curr=NumberFormat.getCurrencyInstance().format(totaldoub);
					total=curr.substring(curr.length()-(curr.length()-1), curr.length());	
					return total;
					
				}
			});
			addColumn(column11);
			TableColumn column12 = new TableColumn("request",Application.getInstance().getMessage("fms.report.request.message.totalmanpower"));
			column12.setFormat(new TableFormat(){
				public String format(Object obj){
					ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
					
					String total="";
					String requestId="";
					String requestType="";
					if(obj!=null && !obj.toString().equals("")){
						if(obj.toString().split("/")!=null && obj.toString().split("/").length>0){
							requestId=obj.toString().split("/")[0];
							requestType=obj.toString().split("/")[1];
						}
					}
					
					Double totaldoub=module.getTotalManpower(requestId, requestType);
					String curr=NumberFormat.getCurrencyInstance().format(totaldoub);
					total=curr.substring(curr.length()-(curr.length()-1), curr.length());	
					
					//total=module.getTotalManpower(requestId, requestType);
					if(total!=null && !total.equals("")){
						return total;
					}else{
						return "0.00";
					}
					
					
				}
			});
			addColumn(column12);
			TableColumn column13 = new TableColumn("request",Application.getInstance().getMessage("fms.report.request.message.totalcost"));
			column13.setFormat(new TableFormat(){
				public String format(Object obj){
					ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
					
					String total="";
					String requestId="";
					String requestType="";
					if(obj!=null && !obj.toString().equals("")){
						if(obj.toString().split("/")!=null && obj.toString().split("/").length>0){
							requestId=obj.toString().split("/")[0];
							requestType=obj.toString().split("/")[1];
						}
					}
					
					//total=module.getTotalCost(requestId, requestType);
					Double totaldoub=module.getTotalCost(requestId, requestType);
					String curr=NumberFormat.getCurrencyInstance().format(totaldoub);
					total=curr.substring(curr.length()-(curr.length()-1), curr.length());	
					
					return total;
				}
			});
			addColumn(column13);
			
			TableColumn column14 = new TableColumn("status",Application.getInstance().getMessage("fms.report.message.label.status"));
			column14.setFormat(new TableFormat(){
				public String format(Object obj){
					String value=obj.toString();
					if(value.equals(EngineeringModule.ASSIGNMENT_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.assignment");
					}else if(value.equals(EngineeringModule.FC_ASSIGNED_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.fcAssigned");
					}else if(value.equals(EngineeringModule.REJECTED_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.rejected");
					}else if(value.equals(EngineeringModule.CANCELLED_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.cancelled");
					}else if(value.equals(EngineeringModule.FULFILLED_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.fulfilled");
					}else if(value.equals(EngineeringModule.CLOSED_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.closed");
					}else if(value.equals(EngineeringModule.LATE_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.fulfilledDelay");
					}else if(value.equals(EngineeringModule.APPLIED_CANCELLATION)){
						return Application.getInstance().getMessage("fms.engineering.request.status.appliedForCancellation");
					}else if(value.equals(EngineeringModule.OUTSOURCED_STATUS)){
						return Application.getInstance().getMessage("fms.engineering.request.status.outsource");
					}else{
						return "";
					}
				}
			});
			addColumn(column14);
			
		}

		public Collection getTableRows() {
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			
			return module.getRequestRepListing(requestStartDate, requestEndDate, requestProgramId, requestTypeId, requestStatus, "", true, 0, -1);
		}

		
		public int getTotalRowCount() {
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			return module.countRequestRepListing(requestStartDate, requestEndDate, requestProgramId, requestTypeId, requestStatus);
		}
		
	}
	
	public String getDefaultTemplate(){
		return "fms/reports/exportTableReport";
	}

	public DatePopupField getBookingDateFrom() {
		return bookingDateFrom;
	}

	public void setBookingDateFrom(DatePopupField bookingDateFrom) {
		this.bookingDateFrom = bookingDateFrom;
	}

	public DatePopupField getBookingDateTo() {
		return bookingDateTo;
	}

	public void setBookingDateTo(DatePopupField bookingDateTo) {
		this.bookingDateTo = bookingDateTo;
	}

	public SelectBox getSbProgram() {
		return sbProgram;
	}

	public void setSbProgram(SelectBox sbProgram) {
		this.sbProgram = sbProgram;
	}

	public SelectBox getSbRequestType() {
		return sbRequestType;
	}

	public void setSbRequestType(SelectBox sbRequestType) {
		this.sbRequestType = sbRequestType;
	}

	public SelectBox getSbStatus() {
		return sbStatus;
	}

	public void setSbStatus(SelectBox sbStatus) {
		this.sbStatus = sbStatus;
	}
	
	public String getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(String requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	

	public ExportEngineeringReqReportTableModel getExportEngineeringReqReportTableModel() {
		return exportEngineeringReqReportTableModel;
	}

	public void setExportEngineeringReqReportTableModel(
			ExportEngineeringReqReportTableModel exportEngineeringReqReportTableModel) {
		this.exportEngineeringReqReportTableModel = exportEngineeringReqReportTableModel;
	}

	public Date getRequestStartDate() {
		return requestStartDate;
	}

	public void setRequestStartDate(Date requestStartDate) {
		this.requestStartDate = requestStartDate;
	}

	public Date getRequestEndDate() {
		return requestEndDate;
	}

	public void setRequestEndDate(Date requestEndDate) {
		this.requestEndDate = requestEndDate;
	}

	public String getRequestProgramId() {
		return requestProgramId;
	}

	public void setRequestProgramId(String requestProgramId) {
		this.requestProgramId = requestProgramId;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	
}
