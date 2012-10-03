package com.tms.fms.reports.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.reports.ui.TransportReqReportTotalTable.TransportReqReportTableModel;
import com.tms.fms.setup.model.ProgramObject;
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
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class TransportReqReportTotalExport extends Table{

	private Application app = Application.getInstance();
	private Collection rowsData;
	private int count;
	
	TransportReqReportTableModel transportReqReportTableModel ;
	
	public void onRequest(Event event) {
		init();
	}
	
	public void init() {
		transportReqReportTableModel = new TransportReqReportTableModel();
        setModel(transportReqReportTableModel);
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
			TableColumn pfeCode = new TableColumn("pfeCode", Application.getInstance().getMessage("fms.report.transport.message.pfeCode"));
			addColumn(pfeCode);
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
			
		}
		
		public Collection getTableRows() {
			return rowsData;
			
		}

		public int getTotalRowCount() {
			return getCount();
		}
		
	}
	
	@Override
	public String getDefaultTemplate(){
		return "fms/reports/exportTableReport";
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
