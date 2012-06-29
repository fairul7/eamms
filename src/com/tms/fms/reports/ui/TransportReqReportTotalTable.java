package com.tms.fms.reports.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.axis.collections.SequencedHashMap;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.PagingUtil; 

import kacang.Application;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction; 
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class TransportReqReportTotalTable extends Table{
	protected DatePopupField reportDateFrom;
	protected DatePopupField reportDateTo;
	
	private Date startDate;
	private Date endDate;
	private String programId;
	public SelectBox sbRequestType;
	private String reqType;
	
	public CheckBox programChbx;
	public CheckBox nonprogramChbx;
	private Application app = Application.getInstance();
	
	TransportReqReportTableModel transportReqReportTableModel ;
	private Collection rowsData;
	private int rowsCount;
	
	public void onRequest(Event event) {
//		transportReqReportTableModel = new TransportReqReportTableModel();
//        setModel(transportReqReportTableModel);
//		Form filterform = super.getFilterForm();
//		filterform.setTemplate("fms/reports/transportFilter");
		
		init();
	}
	
	public void init() {
		//super.init();
		setPageSize(20);
		transportReqReportTableModel = new TransportReqReportTableModel();
        setModel(transportReqReportTableModel);
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/reports/transportFilter");
	}
	
	
	 
	public class TransportReqReportTableModel extends TableModel{
		long count;
		public TransportReqReportTableModel(){
			
			TableColumn assignmentDate = new TableColumn("transAssignmentDateStrMod", app.getMessage("fms.report.message.label.assignmentdate"));
			addColumn(assignmentDate);
			TableColumn reqDept = new TableColumn("department",app.getMessage("fms.report.transport.message.department"));
			addColumn(reqDept);
			TableColumn requestorName = new TableColumn("fullname",app.getMessage("fms.report.transport.message.requestname"));
			addColumn(requestorName);
			TableColumn purpose = new TableColumn("purpose",app.getMessage("fms.report.transport.message.purpose"));
			addColumn(purpose);
			TableColumn destination = new TableColumn("destination",app.getMessage("fms.report.transport.message.destination"));
			addColumn(destination);
			TableColumn remarks = new TableColumn("remarks", app.getMessage("fms.report.transport.message.remark"));
			addColumn(remarks);
			TableColumn requestTitle = new TableColumn("requestTitle",app.getMessage("fms.report.transport.message.requesttitle"));
			addColumn(requestTitle);
			TableColumn reqDate = new TableColumn("requestDateStr0",app.getMessage("fms.report.transport.message.requestdate"));
			addColumn(reqDate);
			TableColumn requestType = new TableColumn("program",app.getMessage("fms.request.label.requestType"));
			addColumn(requestType);
			TableColumn progName = new TableColumn("programName", app.getMessage("fms.report.transport.message.programName"));
			addColumn(progName);
			TableColumn reqNo = new TableColumn("id", app.getMessage("fms.report.transport.message.reqNo"));
			addColumn(reqNo);
			TableColumn reqVehicles = new TableColumn("requestedVehicles", app.getMessage("fms.report.transport.message.requestedVehicles"));
			reqVehicles.setSortable(false);
			addColumn(reqVehicles);
			TableColumn reqDrivers = new TableColumn("requestedDrivers", app.getMessage("fms.report.transport.message.requestedDrivers"));
			reqDrivers.setSortable(false);
			addColumn(reqDrivers);
			TableColumn totalCostDaily = new TableColumn("totalCostDaily", app.getMessage("fms.report.transport.message.dailyTotalCost"));
			addColumn(totalCostDaily);
			
			reportDateFrom = new DatePopupField("reportDateFrom");
			reportDateFrom.setOptional(true);
			reportDateFrom.setDate(startDate);
			TableFilter reportDateFromFilter = new TableFilter("reportDateFrom");
			reportDateFromFilter.setWidget(reportDateFrom);
			
			addFilter(reportDateFromFilter);
						
			reportDateTo = new DatePopupField("reportDateTo");
			reportDateTo.setOptional(true);
			reportDateTo.setDate(endDate);
			TableFilter reportDateToFilter = new TableFilter("reportDateTo");
			reportDateToFilter.setWidget(reportDateTo);
			addFilter(reportDateToFilter);
			
			TableFilter tfRequestType = new TableFilter("tfRequestType");
			sbRequestType=new SelectBox("sbRequestType");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",app.getMessage("fms.request.label.requestType"));
			typeMap.putAll(EngineeringModule.REQUEST_TYPE_MAP);
			sbRequestType.setOptionMap(typeMap);
			tfRequestType.setWidget(sbRequestType);
			addFilter(tfRequestType);
			
			TableFilter tfProgram = new TableFilter("tfProgram");
			SelectBox sbProgram = new SelectBox("sbProgram");
			sbProgram.setOptions("-1=" + "--Please Select--");
		    try {
		    	ReportsFmsModule module = (ReportsFmsModule) app.getModule(ReportsFmsModule.class);
                 
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
			sbProgram.setSelectedOption(programId);
			addFilter(tfProgram);
			
			TableFilter tfChbx1 = new TableFilter("tfChbx1");
			programChbx = new CheckBox("programChbx");
			tfChbx1.setWidget(programChbx);
			addFilter(tfChbx1);
			
			TableFilter tfChbx3 = new TableFilter("tfChbx3");
			nonprogramChbx = new CheckBox("nonprogramChbx");
			tfChbx3.setWidget(nonprogramChbx);
			addFilter(tfChbx3);
			
			addAction(new TableAction("export",app.getMessage("fms.report.message.export")));
		}
		
		public String getProgram() {
			String returnValue = "-1";
			List lstDepartment = (List)getFilterValue("tfProgram");
			if (lstDepartment.size() > 0) {returnValue = (String)lstDepartment.get(0);}
			return returnValue;
		}
		
		public String getRequestType() {
			
			String returnValue = "";
			
			if(nonprogramChbx.isChecked()){
				returnValue="nonprogram";
			}
			
			if(programChbx.isChecked()){
				returnValue="program";
			}
			
			if(nonprogramChbx.isChecked()&&programChbx.isChecked()){
				returnValue="all";
			}
			
			return returnValue;
		}

		public Collection getTableRows() {
			Collection list =null;
			Date fromDate = reportDateFrom.getDate();
			Date toDate = reportDateTo.getDate();
			String reqType = getRequestType();
			ReportsFmsModule module = (ReportsFmsModule)app.getModule(ReportsFmsModule.class);
			
			if((fromDate==null) 
					&& (toDate==null)
					&& (getRequestType().equals(""))
					&& (getProgram()==null || getProgram().equals("-1"))){
				return null;
			} else {
				list = module.getTransportRequestWithTotalListing(reqType,fromDate, toDate, getProgram(), getSort(), isDesc(), 0, -1);
				rowsData = list;
				count = ReportsFmsModule.listCounter2;
            	list 	= PagingUtil.getPagedCollection(list, getStart(), getRows());
            	return list;
			}
			
		}

		public int getTotalRowCount() {
			rowsCount = ((Number) count).intValue();
			return rowsCount;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			if ("export".equals(action)) {
				
				
				startDate = reportDateFrom.getDate();
				endDate = reportDateTo.getDate();
				programId = getProgram();
				reqType = getRequestType();
				
				return new Forward("export");
			}
			return super.processAction(evt, action, selectedKeys);
		}
		
	}
	
	@Override
	public String getDefaultTemplate(){
		return "fms/reports/tableReportnobil";
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

	public TransportReqReportTableModel getTransportReqReportTableModel() {
		return transportReqReportTableModel;
	}

	public void setTransportReqReportTableModel(
			TransportReqReportTableModel transportReqReportTableModel) {
		this.transportReqReportTableModel = transportReqReportTableModel;
	}

	public Collection getRowsData() {
		return rowsData;
	}

	public void setRowsData(Collection rowsData) {
		this.rowsData = rowsData;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	
	

}
