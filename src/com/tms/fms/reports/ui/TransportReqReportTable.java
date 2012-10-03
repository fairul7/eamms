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

public class TransportReqReportTable extends Table{
	protected DatePopupField reportDateFrom;
	protected DatePopupField reportDateTo;
	
	private Date startDate;
	private Date endDate;
	private String programId;
	public SelectBox sbRequestType;
	
	/*public CheckBox internalChbx;
	public CheckBox externalChbx;
	public CheckBox nonprogramChbx;*/
	
	public CheckBox programChbx;
	public CheckBox nonprogramChbx;
	
	TransportReqReportTableModel transportReqReportTableModel ;
	
	private Collection tableRowsData;
	private int totalRowCountData;
	
	public void onRequest(Event event) {
		transportReqReportTableModel = new TransportReqReportTableModel();
        setModel(transportReqReportTableModel);
		Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/reports/transportFilter");
		
		init();
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		transportReqReportTableModel = new TransportReqReportTableModel();
        setModel(transportReqReportTableModel);
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/reports/transportFilter");
	}
	
	
	 
	public class TransportReqReportTableModel extends TableModel{
		long count;
		public TransportReqReportTableModel(){
			
			TableColumn assignmentDate = new TableColumn("transAssignmentDateStrMod","Assignment Date");
			addColumn(assignmentDate);
			TableColumn reqDept = new TableColumn("department",Application.getInstance().getMessage("fms.report.transport.message.department"));
			addColumn(reqDept);
			TableColumn driverAssigned = new TableColumn("driversAssigned",Application.getInstance().getMessage("fms.report.transport.message.driverassigned"));
			addColumn(driverAssigned);
			TableColumn vehicleAssigned = new TableColumn("vehicleAssigned",Application.getInstance().getMessage("fms.report.transport.message.vehicleassigned"));
			addColumn(vehicleAssigned);
			TableColumn requestorName = new TableColumn("fullname",Application.getInstance().getMessage("fms.report.transport.message.requestname"));
			addColumn(requestorName);
			TableColumn purpose = new TableColumn("purpose",Application.getInstance().getMessage("fms.report.transport.message.purpose"));
			addColumn(purpose);
			TableColumn destination = new TableColumn("destination",Application.getInstance().getMessage("fms.report.transport.message.destination"));
			addColumn(destination);
			TableColumn meterStart = new TableColumn("singleMeterStart",Application.getInstance().getMessage("fms.report.transport.message.meterstart"));
			addColumn(meterStart);
			TableColumn meterEnd = new TableColumn("singleMeterEnd",Application.getInstance().getMessage("fms.report.transport.message.meterend"));
			addColumn(meterEnd);
			TableColumn totalMeter = new TableColumn("singleTotalMeter",Application.getInstance().getMessage("fms.report.transport.message.totalmileage"));
			addColumn(totalMeter);
			TableColumn checkInDate = new TableColumn("singleCheckInDate",Application.getInstance().getMessage("fms.report.transport.message.timein"));
			addColumn(checkInDate);
			TableColumn chekOutDate = new TableColumn("singleCheckOutDate",Application.getInstance().getMessage("fms.report.transport.message.timeout"));
			addColumn(chekOutDate);
			TableColumn timeRec = new TableColumn("singleTimeRec",Application.getInstance().getMessage("fms.report.transport.message.timerec"));
			addColumn(timeRec);
			TableColumn remarks = new TableColumn("remarks", Application.getInstance().getMessage("fms.report.transport.message.remark"));
			addColumn(remarks);
			TableColumn requestTitle = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.report.transport.message.requesttitle"));
			addColumn(requestTitle);
			TableColumn reqDate = new TableColumn("requestDateStr0",Application.getInstance().getMessage("fms.report.transport.message.requestdate"));
			addColumn(reqDate);
			TableColumn requestType = new TableColumn("program",Application.getInstance().getMessage("fms.request.label.requestType"));
			addColumn(requestType);
			TableColumn requestStatus = new TableColumn("transStatus",Application.getInstance().getMessage("fms.report.transport.message.requeststatus"));
			requestStatus.setFormat(new TableFormat(){
					public String format(Object obj){
						ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
						//SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
						String statusNo = obj.toString()!=null&&!obj.toString().equals("")?obj.toString().split("/")[0]:"";	
						String requestId = obj.toString()!=null&&!obj.toString().equals("")?obj.toString().split("/")[1]:"";	
						String status = "";					
						TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.PROCESS_STATUS)){
							return Application.getInstance().getMessage("fms.report.transport.message.hodprocess","HOD Processed");
						}
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.CANCELLED_STATUS)){
							return Application.getInstance().getMessage("fms.report.status.batal","Batal");
						}
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.REJECTED_STATUS)){
							return Application.getInstance().getMessage("fms.report.status.batal","Batal");
						}
						
						if(statusNo!=null && !statusNo.equals("") && statusNo.equals(SetupModule.CLOSED_STATUS)){
							return Application.getInstance().getMessage("fms.tran.status.closed","Closed");
						}
						
						//checks if status is assigned 
						if(module.getAssignedStatus(requestId)){
							return Application.getInstance().getMessage("fms.report.transport.message.TA","TA");
						}
						
						status = TM.selectStatus(statusNo);					
						
			    		return status;		
						
					}
				});
			addColumn(requestStatus);
			TableColumn outSourceFlag = new TableColumn("outsourceFlag",Application.getInstance().getMessage("fms.report.transport.message.outsourceflag"));
			addColumn(outSourceFlag);
			TableColumn driverStatus = new TableColumn("singleDriverStatus",Application.getInstance().getMessage("fms.report.transport.message.driverstatus"));
			driverStatus.setFormat(new TableFormat(){
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
						return Application.getInstance().getMessage("fms.report.status.no.news","No.News");
					}
					
		    		return status;		
					
				}
			});
			addColumn(driverStatus);
			TableColumn vehicleStatus = new TableColumn("singleVehicleStatus",Application.getInstance().getMessage("fms.report.transport.message.vehiclestatus"));
			vehicleStatus.setFormat(new TableFormat(){
				
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
						return Application.getInstance().getMessage("fms.report.status.no.news","No.News");
					}
		    		return status;		
					
				}
			});
			addColumn(vehicleStatus);
			TableColumn reqNo = new TableColumn("reqAsssignId", Application.getInstance().getMessage("fms.report.transport.message.reqNoOrAssgnNo"));
			addColumn(reqNo);
			TableColumn progName = new TableColumn("programName", Application.getInstance().getMessage("fms.report.transport.message.programName"));
			addColumn(progName);
			TableColumn pfeCode = new TableColumn("pfeCode", Application.getInstance().getMessage("fms.report.transport.message.pfeCode"));
			addColumn(pfeCode);
			
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
			typeMap.put("-1",Application.getInstance().getMessage("fms.request.label.requestType"));
			typeMap.putAll(EngineeringModule.REQUEST_TYPE_MAP);
			sbRequestType.setOptionMap(typeMap);
			tfRequestType.setWidget(sbRequestType);
			addFilter(tfRequestType);
			
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
			sbProgram.setSelectedOption(programId);
			addFilter(tfProgram);
			
			
			
			/*TableFilter tfChbx1 = new TableFilter("tfChbx1");
			internalChbx = new CheckBox("internalChbx");
			tfChbx1.setWidget(internalChbx);
			addFilter(tfChbx1);
			
			TableFilter tfChbx2 = new TableFilter("tfChbx2");
			externalChbx = new CheckBox("externalChbx");
			tfChbx2.setWidget(externalChbx);
			addFilter(tfChbx2);*/
			
			TableFilter tfChbx1 = new TableFilter("tfChbx1");
			programChbx = new CheckBox("programChbx");
			tfChbx1.setWidget(programChbx);
			addFilter(tfChbx1);
			
			TableFilter tfChbx3 = new TableFilter("tfChbx3");
			nonprogramChbx = new CheckBox("nonprogramChbx");
			tfChbx3.setWidget(nonprogramChbx);
			addFilter(tfChbx3);
			
			addAction(new TableAction("export",Application.getInstance().getMessage("fms.report.message.export")));
		}
		
		public String getProgram() {
			String returnValue = "-1";
			List lstDepartment = (List)getFilterValue("tfProgram");
			if (lstDepartment.size() > 0) {returnValue = (String)lstDepartment.get(0);}
			return returnValue;
		}
		
		/*public String getRequestType() {
			
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
		}*/
		
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
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			
			if((fromDate==null) 
					&& (toDate==null)
					&& (getRequestType().equals(""))
					&& (getProgram()==null || getProgram().equals("-1"))){
				return null;
			} else {
				list = module.getTransportRequestListing(reqType,fromDate, toDate, getProgram(), getSort(), isDesc(), 0, -1);
				tableRowsData = list;
				count = ReportsFmsModule.listCounter;
            	list 	= PagingUtil.getPagedCollection(list, getStart(), getRows());
            	return list;
			}
			
		}

		public int getTotalRowCount() {

			totalRowCountData = ((Number) count).intValue(); 
			return totalRowCountData;
		}
		
		
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			if ("export".equals(action)) {
				
				
				startDate = reportDateFrom.getDate();
				endDate = reportDateTo.getDate();
				programId = getProgram();
				
				evt.getRequest().getSession().setAttribute("fromDateTransport", reportDateFrom.getDate());
				evt.getRequest().getSession().setAttribute("toDateTransport", reportDateTo.getDate());
				evt.getRequest().getSession().setAttribute("programTransport", getProgram());
				evt.getRequest().getSession().setAttribute("transReqType", getRequestType());
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
