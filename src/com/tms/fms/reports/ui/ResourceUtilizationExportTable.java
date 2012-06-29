package com.tms.fms.reports.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.setup.model.ProgramObject;

import kacang.Application;
import kacang.stdui.DatePopupField;
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

public class ResourceUtilizationExportTable extends Table{
	ResourceUtilizationTableExportModel resourceUtilizationTableExportModel ;
	
	public Date resourceStartDate;
	public Date resourceEndDate;
	public String resourceProgramId;
	public String resourceServiceTypeId;
	public String resourceStatus;
	public String programType, requestStatus;
	
	public void onRequest(Event event) {
		resourceUtilizationTableExportModel = new ResourceUtilizationTableExportModel();
        setModel(resourceUtilizationTableExportModel);
		
		init();
		resourceStartDate=(Date)event.getRequest().getSession().getAttribute("exportfromDate");
		resourceEndDate=(Date)event.getRequest().getSession().getAttribute("exporttoDate");
		resourceProgramId=(String)event.getRequest().getSession().getAttribute("exportprogram");
		resourceServiceTypeId=(String)event.getRequest().getSession().getAttribute("exportserviceType");
		resourceStatus=(String)event.getRequest().getSession().getAttribute("exportstatus");
		programType = (String) event.getRequest().getSession().getAttribute("programType");
		requestStatus = (String) event.getRequest().getSession().getAttribute("resource_requestStatus");
		
		event.getRequest().getSession().setAttribute("exportfromDate","");
		event.getRequest().getSession().setAttribute("exporttoDate","");
		event.getRequest().getSession().setAttribute("exportprogram","");
		event.getRequest().getSession().setAttribute("exportserviceType","");
		event.getRequest().getSession().setAttribute("exportstatus","");
		event.getRequest().getSession().setAttribute("programType", "");
		event.getRequest().getSession().setAttribute("resource_requestStatus", "");
	}
	
	public void init() {
		super.init();
		//setPageSize(20);
		resourceUtilizationTableExportModel = new ResourceUtilizationTableExportModel();
        setModel(resourceUtilizationTableExportModel);
	}
	
	
	 
	public class ResourceUtilizationTableExportModel extends TableModel{
		public ResourceUtilizationTableExportModel(){
			
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
		}

		
		
		public Collection getTableRows() {
			
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			return module.getResourceUtil(programType, resourceStartDate, resourceEndDate, resourceProgramId, resourceServiceTypeId, resourceStatus, 
					requestStatus, getSort(), isDesc(), 0, -1);
		}

		
		public int getTotalRowCount() {
			
			ReportsFmsModule module = (ReportsFmsModule)Application.getInstance().getModule(ReportsFmsModule.class);
			return module.countResourceUtil(programType, resourceStartDate, resourceEndDate, resourceProgramId, resourceServiceTypeId, resourceStatus, requestStatus);
		}
		
	}
	
	
	public String getDefaultTemplate(){
		return "fms/reports/exportTableReport";
	}

	public ResourceUtilizationTableExportModel getResourceUtilizationTableExportModel() {
		return resourceUtilizationTableExportModel;
	}

	public void setResourceUtilizationTableExportModel(
			ResourceUtilizationTableExportModel resourceUtilizationTableExportModel) {
		this.resourceUtilizationTableExportModel = resourceUtilizationTableExportModel;
	}

	public Date getResourceStartDate() {
		return resourceStartDate;
	}

	public void setResourceStartDate(Date resourceStartDate) {
		this.resourceStartDate = resourceStartDate;
	}

	public Date getResourceEndDate() {
		return resourceEndDate;
	}

	public void setResourceEndDate(Date resourceEndDate) {
		this.resourceEndDate = resourceEndDate;
	}

	public String getResourceProgramId() {
		return resourceProgramId;
	}

	public void setResourceProgramId(String resourceProgramId) {
		this.resourceProgramId = resourceProgramId;
	}

	public String getResourceServiceTypeId() {
		return resourceServiceTypeId;
	}

	public void setResourceServiceTypeId(String resourceServiceTypeId) {
		this.resourceServiceTypeId = resourceServiceTypeId;
	}

	public String getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}
	
	
}
