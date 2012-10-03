package com.tms.fms.reports.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.fortuna.ical4j.model.property.RequestStatus;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableModel;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


public class ResourceUtilizationTable extends Table{

	public DatePopupField assignmentDateFrom;
	public DatePopupField assignmentDateTo;
	public SelectBox sbProgram;
	public SelectBox sbServiceType;
	public SelectBox sbStatus;
	public ComboSelectBox comboSType;
	
	ResourceUtilizationTableModel resourceUtilizationTableModel ;
	
	public Date resourceStartDate;
	public Date resourceEndDate;
	public String resourceProgramId;
	public String resourceServiceTypeId;
	public String resourceStatus;
	public String programType, requestStatus;
	public int pageSize;
	
	
	public void onRequest(Event event) {
		resourceUtilizationTableModel = new ResourceUtilizationTableModel();
        setModel(resourceUtilizationTableModel);
        pageSize=Integer.parseInt(event.getRequest().getSession().getAttribute("pageSize")!=null && !event.getRequest().getSession().getAttribute("pageSize").toString().equals("") ? event.getRequest().getSession().getAttribute("pageSize").toString():"20");
        if(event.getRequest().getSession().getAttribute("fromDate") != null && !event.getRequest().getSession().getAttribute("fromDate").equals("")){
        	resourceStartDate = (Date)event.getRequest().getSession().getAttribute("fromDate");
    		resourceEndDate = (Date)event.getRequest().getSession().getAttribute("toDate");
    		resourceProgramId=(String)event.getRequest().getSession().getAttribute("program");
    		resourceServiceTypeId=(String)event.getRequest().getSession().getAttribute("serviceType");
    		resourceStatus=(String)event.getRequest().getSession().getAttribute("status");
    		programType = (String) event.getRequest().getSession().getAttribute("programType");
    		requestStatus = (String) event.getRequest().getSession().getAttribute("resource_requestStatus");
        }
		
		
		init();
		
		event.getRequest().getSession().setAttribute("fromDate","");
		event.getRequest().getSession().setAttribute("toDate","");
		event.getRequest().getSession().setAttribute("program","");
		event.getRequest().getSession().setAttribute("serviceType","");
		event.getRequest().getSession().setAttribute("status","");
		event.getRequest().getSession().setAttribute("programType", "");
		event.getRequest().getSession().setAttribute("resource_requestStatus", "");
		
	}
	
	public void init() {
		super.init();
		setCurrentPage(1);
		setPageSize(pageSize);
		resourceUtilizationTableModel = new ResourceUtilizationTableModel();
        setModel(resourceUtilizationTableModel);
	}
	
	
	
	
	public class ResourceUtilizationTableModel extends TableModel{
		public ResourceUtilizationTableModel(){
			
			TableColumn column1 = new TableColumn("requestId",Application.getInstance().getMessage("fms.report.resource.message.requestid"));
			addColumn(column1);
			TableColumn column2 = new TableColumn("blockBooking",Application.getInstance().getMessage("fms.report.resource.message.blockbooking"));
			addColumn(column2);
			TableColumn column4 = new TableColumn("title",Application.getInstance().getMessage("fms.report.resource.message.requesttitle"));
			addColumn(column4);
			TableColumn column5 = new TableColumn("program",Application.getInstance().getMessage("fms.report.resource.message.programtitle"));
			addColumn(column5);
			TableColumn column6 = new TableColumn("serviceType",Application.getInstance().getMessage("fms.report.resource.message.servicetype"));
			column6.setFormat(new TableFormat(){
				public String format(Object obj){
					String value = obj.toString();
					return (String)EngineeringModule.SERVICES_MAP.get(value);
				}
			});
			addColumn(column6);
			TableColumn column7 = new TableColumn("facilityEquip",Application.getInstance().getMessage("fms.report.resource.message.facility"));
			addColumn(column7);
			TableColumn column8 = new TableColumn("assignmentId",Application.getInstance().getMessage("fms.report.resource.message.assignmentid"));
			addColumn(column8);
			TableColumn column9 = new TableColumn("requiredFrom",Application.getInstance().getMessage("fms.report.resource.message.datestart"));
			column9.setFormat(new TableDateFormat("dd/MM/yyyy"));
			addColumn(column9);
			TableColumn column10 = new TableColumn("requiredTo",Application.getInstance().getMessage("fms.report.resource.message.dateend"));
			column10.setFormat(new TableDateFormat("dd/MM/yyyy"));
			addColumn(column10);
			TableColumn column11 = new TableColumn("fromTime",Application.getInstance().getMessage("fms.report.resource.message.timestart"));
			addColumn(column11);
			TableColumn column12 = new TableColumn("toTime",Application.getInstance().getMessage("fms.report.resource.message.timeend"));
			addColumn(column12);
			TableColumn column13 = new TableColumn("totalDuration",Application.getInstance().getMessage("fms.report.resource.message.duration"));
			addColumn(column13);
			TableColumn column14 = new TableColumn("requestStatus",Application.getInstance().getMessage("fms.report.message.label.requestStatus"));
			addColumn(column14);
			
//			TableColumn status = new TableColumn("status", "Status");
//			addColumn(status);
			
			
		}

		public Collection getTableRows() {
			
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			if((resourceStartDate==null) 
					&& (resourceEndDate==null)
					&& (resourceProgramId==null || resourceProgramId.equals("-1"))
					&& (resourceServiceTypeId==null || resourceServiceTypeId.equals("") || resourceServiceTypeId.equals("-1"))
					&& (resourceStatus==null || resourceStatus.equals("-1"))
					&& (requestStatus == null || requestStatus.equals("-1"))){
				return null;
			}else{
				return module.getResourceUtil(programType, resourceStartDate, resourceEndDate, resourceProgramId, resourceServiceTypeId, resourceStatus, 
						getRequestStatus(), getSort(), isDesc(), getStart(), getRows());
			}
		}

		
		public int getTotalRowCount() {
			
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			if((resourceStartDate==null) 
					&& (resourceEndDate==null)
					&& (resourceProgramId==null || resourceProgramId.equals("-1"))
					&& (resourceServiceTypeId==null || resourceServiceTypeId.equals("") || resourceServiceTypeId.equals("-1"))
					&& (resourceStatus==null || resourceStatus.equals("-1"))
					&& (requestStatus == null || requestStatus.equals("-1"))){
				return 0;
			}else{
				return module.countResourceUtil(programType, resourceStartDate, resourceEndDate, resourceProgramId, resourceServiceTypeId, resourceStatus, requestStatus);
			}
		}
		
		
		/*public Forward processAction(Event evt, String action, String[] selectedKeys) {
			if ("export".equals(action)) {
				
				evt.getRequest().getSession().setAttribute("exportfromDate", resourceStartDate);
				evt.getRequest().getSession().setAttribute("exporttoDate", resourceEndDate);
				evt.getRequest().getSession().setAttribute("exportprogram", resourceProgramId);
				evt.getRequest().getSession().setAttribute("exportserviceType", resourceServiceTypeId);
				evt.getRequest().getSession().setAttribute("exportstatus", resourceStatus);
				
				return new Forward("export");
			}
			return super.processAction(evt, action, selectedKeys);
		}*/
	}
	
	
	public String getDefaultTemplate(){
		return "fms/reports/tableReport";
	}

	public DatePopupField getAssignmentDateFrom() {
		return assignmentDateFrom;
	}

	public void setAssignmentDateFrom(DatePopupField assignmentDateFrom) {
		this.assignmentDateFrom = assignmentDateFrom;
	}

	public DatePopupField getAssignmentDateTo() {
		return assignmentDateTo;
	}

	public void setAssignmentDateTo(DatePopupField assignmentDateTo) {
		this.assignmentDateTo = assignmentDateTo;
	}

	public SelectBox getSbProgram() {
		return sbProgram;
	}

	public void setSbProgram(SelectBox sbProgram) {
		this.sbProgram = sbProgram;
	}

	public SelectBox getSbServiceType() {
		return sbServiceType;
	}

	public void setSbServiceType(SelectBox sbServiceType) {
		this.sbServiceType = sbServiceType;
	}

	public SelectBox getSbStatus() {
		return sbStatus;
	}

	public void setSbStatus(SelectBox sbStatus) {
		this.sbStatus = sbStatus;
	}

	public ResourceUtilizationTableModel getResourceUtilizationTableModel() {
		return resourceUtilizationTableModel;
	}

	public void setResourceUtilizationTableModel(
			ResourceUtilizationTableModel resourceUtilizationTableModel) {
		this.resourceUtilizationTableModel = resourceUtilizationTableModel;
	}

	public ComboSelectBox getComboSType() {
		return comboSType;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestType) {
		this.requestStatus = requestType;
	}

	public void setComboSType(ComboSelectBox comboSType) {
		this.comboSType = comboSType;
	}

	
	
	
}
