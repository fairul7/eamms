package com.tms.fms.reports.ui;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
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
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.util.WidgetUtil;

public class EngineeringReqReportTable extends Table{
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
	
	public CheckBox internalChbx;
	public CheckBox externalChbx;
	public CheckBox nonprogramChbx;
	
	EngineeringReqReportTableModel engineeringReqReportTableModel ;
	
	public void onRequest(Event event) {
		engineeringReqReportTableModel = new EngineeringReqReportTableModel();
        setModel(engineeringReqReportTableModel);
		Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/reports/engRequestFilter");
		
		init();
		
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		engineeringReqReportTableModel = new EngineeringReqReportTableModel();
        setModel(engineeringReqReportTableModel);
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/reports/engRequestFilter");
	}
	 
	public class EngineeringReqReportTableModel extends TableModel{
		public EngineeringReqReportTableModel(){
			
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
			column4.setFormat(new TableFormat(){
				public String format(Object value){
					if(value.toString().equals("")){
						return Application.getInstance().getMessage("fms.report.notavailable", "Not Available");
					}else{
						return value.toString();
					}
					
					
				}
			});
			addColumn(column4);
			
			TableColumn column9 = new TableColumn("department",Application.getInstance().getMessage("fms.report.request.message.departmentrequest"));
			column9.setFormat(new TableFormat(){
				public String format(Object value){
					if(value.toString().equals("")){
						return Application.getInstance().getMessage("fms.report.notavailable", "Not Available");
					}else{
						return value.toString();
					}
					
					
				}
			});
			addColumn(column9);
			
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
					Double totalFacility=module.getTotalFacility(requestId, requestType);
					Double totalManpower=module.getTotalManpower(requestId, requestType);
					Double totaldoub = 0.0;
					if(module.getCancelCharge(requestId)!=null && !module.getCancelCharge(requestId).equals("")){
						totaldoub = Double.parseDouble(module.getCancelCharge(requestId));
					}
					Double totalCost= (totalFacility+totalManpower)+totaldoub;
					String curr=NumberFormat.getCurrencyInstance().format(totalCost);
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
			
			bookingDateFrom = new DatePopupField("bookingDateFrom");
			bookingDateFrom.setOptional(true);
			bookingDateFrom.setDate(requestStartDate);
			TableFilter bookingDateFromFilter = new TableFilter("bookingDateFrom");
			bookingDateFromFilter.setWidget(bookingDateFrom);
			addFilter(bookingDateFromFilter);
						
			bookingDateTo = new DatePopupField("bookingDateTo");
			bookingDateTo.setOptional(true);
			bookingDateTo.setDate(requestEndDate);
			TableFilter bookingDateToFilter = new TableFilter("bookingDateTo");
			bookingDateToFilter.setWidget(bookingDateTo);
			addFilter(bookingDateToFilter);
			
			TableFilter tfProgram = new TableFilter("tfProgram");
			SelectBox sbProgram = new SelectBox("sbProgram");
			sbProgram.setOptions("-1=" + "--Please Select--");
		    try {
		    	ReportsFmsModule module = (ReportsFmsModule) Application.getInstance().getModule(ReportsFmsModule.class);
                 
                Collection listPrograms = module.getProgram();
			    if (listPrograms.size() > 0) {
			    	for (Iterator i=listPrograms.iterator(); i.hasNext();) {
			    		ProgramObject progObj = (ProgramObject)i.next();
			        	sbProgram.setOptions(progObj.getProgramId()+"="+ progObj.getProgramName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			tfProgram.setWidget(sbProgram);
			sbProgram.setSelectedOption(requestProgramId);
			addFilter(tfProgram);
			
			
			TableFilter tfRequestType = new TableFilter("tfRequestType");
			sbRequestType=new SelectBox("sbRequestType");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",Application.getInstance().getMessage("fms.request.label.requestType"));
			typeMap.putAll(EngineeringModule.REQUEST_TYPE_MAP);
			sbRequestType.setOptionMap(typeMap);
			tfRequestType.setWidget(sbRequestType);
			sbRequestType.setSelectedOption(requestTypeId);
			addFilter(tfRequestType);
			
			TableFilter tfStatus = new TableFilter("tfStatus");
			SequencedHashMap statusMap=new SequencedHashMap();
			statusMap.put("-1",Application.getInstance().getMessage("fms.facility.table.status"));
			statusMap.putAll(EngineeringModule.FC_HEAD_STATUS_MAP);
			sbStatus=new SelectBox("sbStatus");
			sbStatus.setOptionMap(statusMap);
			tfStatus.setWidget(sbStatus);
			sbStatus.setSelectedOption(requestStatus);
			addFilter(tfStatus);
			
			TableFilter tfChbx1 = new TableFilter("tfChbx1");
			internalChbx = new CheckBox("internalChbx");
			tfChbx1.setWidget(internalChbx);
			addFilter(tfChbx1);
			
			TableFilter tfChbx2 = new TableFilter("tfChbx2");
			externalChbx = new CheckBox("externalChbx");
			tfChbx2.setWidget(externalChbx);
			addFilter(tfChbx2);
			
			TableFilter tfChbx3 = new TableFilter("tfChbx3");
			nonprogramChbx = new CheckBox("nonprogramChbx");
			tfChbx3.setWidget(nonprogramChbx);
			addFilter(tfChbx3);
			
			addAction(new TableAction("export",Application.getInstance().getMessage("fms.report.message.export")));
		}

		public String getProgram() {
			String returnValue = "-1";
			List lstProgram = (List)getFilterValue("tfProgram");
			if (lstProgram.size() > 0) {returnValue = (String)lstProgram.get(0);}
			return returnValue;
		}
		
		public String getStatus() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("tfStatus");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
		
		public String getRequestType() {
			
			String returnValue = "";
			
			if(internalChbx.isChecked()){
				returnValue+=EngineeringModule.REQUEST_TYPE_INTERNAL+"/";
			}
			if(externalChbx.isChecked()){
				returnValue+=EngineeringModule.REQUEST_TYPE_EXTERNAL+"/";
			}
			if(nonprogramChbx.isChecked()){
				returnValue+=EngineeringModule.REQUEST_TYPE_NONPROGRAM+"/";
			}
			
			String[] splitvalue = returnValue.split("/");
			int i=0;
			for (String val : splitvalue) {
				if(i==0){
					returnValue="";
					returnValue+=val;
				}else{
					returnValue+=","+val;
				}
				i++;
			}
			return returnValue;
		}
		

		public Collection getTableRows() {
			Date fromDate = bookingDateFrom.getDate();
			Date toDate = bookingDateTo.getDate();
			
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			if((fromDate==null) 
					&& (toDate==null)
					&& (getProgram()==null || getProgram().equals("-1"))
					&& (getRequestType().equals(""))
					&& (getStatus()==null || getStatus().equals("-1"))){
				return null;
			}else{
				return module.getRequestRepListing(fromDate, toDate, getProgram(), getRequestType(), getStatus(), getSort(), isDesc(), getStart(), getRows());
			}
		}

		@Override
		public int getTotalRowCount() {
			Date fromDate = bookingDateFrom.getDate();
			Date toDate = bookingDateTo.getDate();
			
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			if((fromDate==null) 
					&& (toDate==null)
					&& (getProgram()==null || getProgram().equals("-1"))
					&& (getRequestType().equals(""))
					&& (getStatus()==null || getStatus().equals("-1"))){
				return 0;
			}else{
				return module.countRequestRepListing(fromDate, toDate, getProgram(), getRequestType(), getStatus());
			}
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			if ("export".equals(action)) {
				requestStartDate = bookingDateFrom.getDate();
				requestEndDate = bookingDateTo.getDate();
				requestProgramId = getProgram();
				requestTypeId = getRequestType();
				requestStatus = getRequestStatus();
				
				evt.getRequest().getSession().setAttribute("fromDateRequest", bookingDateFrom.getDate());
				evt.getRequest().getSession().setAttribute("toDateRequest", bookingDateTo.getDate());
				evt.getRequest().getSession().setAttribute("programRequest", getProgram());
				evt.getRequest().getSession().setAttribute("requestTypeRequest", getRequestType());
				evt.getRequest().getSession().setAttribute("statusRequest", getStatus());
				
				return new Forward("export");
			}
			return super.processAction(evt, action, selectedKeys);
		}
		
	}
	
	public String getDefaultTemplate(){
		return "fms/reports/tableReport";
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

	public EngineeringReqReportTableModel getEngineeringReqReportTableModel() {
		return engineeringReqReportTableModel;
	}

	public void setEngineeringReqReportTableModel(
			EngineeringReqReportTableModel engineeringReqReportTableModel) {
		this.engineeringReqReportTableModel = engineeringReqReportTableModel;
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
