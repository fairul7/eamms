package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.SetupModule;

public class AssignmentCheckOutTable extends Table {
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField(); 
	public AssignmentCheckOutTable() {
	}

	public AssignmentCheckOutTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new AssignmentCheckOutTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new AssignmentCheckOutTableModel());
	}

	class AssignmentCheckOutTableModel extends TableModel {
		AssignmentCheckOutTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn requiredFrom = new TableColumn("convertedCheckedOutDate", app.getMessage("fms.facility.table.checkOutDate"));
			//TableColumn requiredFrom = new TableColumn("checkedOutDate", app.getMessage("fms.facility.table.checkOutDate"));
			//requiredFrom.setFormat(new TableDateFormat(SetupModule.DATE_TIME_FORMAT));
			requiredFrom.setUrlParam("requestId");
			addColumn(requiredFrom);
			
			TableColumn requestTitle = new TableColumn("title", app.getMessage("fms.label.requestTitle"));
			requestTitle.setUrlParam("requestId");
			addColumn(requestTitle);
			
			TableColumn requestId = new TableColumn("requestId", app.getMessage("fms.request.label.requestId"));
			addColumn(requestId);
			
			//TableColumn tcGroupAssignmentCode = new TableColumn("serviceType", app.getMessage("fms.label.groupAssignmentCode"));
			//addColumn(tcGroupAssignmentCode);
			
			TableColumn tcRequestor = new TableColumn("createdByFullName", app.getMessage("fms.facility.form.requestor"));
			addColumn(tcRequestor);

			TableColumn requiredTo = new TableColumn("assignmentLocation", app.getMessage("fms.facility.table.assignmentLocation"));
			addColumn(requiredTo);
			
			TableColumn noOfCheckedOut = new TableColumn("totalCheckedOut", app.getMessage("fms.facility.form.totalCheckedOut"));
			addColumn(noOfCheckedOut);

			TableColumn noOfCheckedIn = new TableColumn("totalCheckedIn", app.getMessage("fms.facility.form.totalCheckedIn"));
			addColumn(noOfCheckedIn);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter label1=new TableFilter("fromDateLabel");
			label1.setWidget(new Label("fromDate1",app.getMessage("fms.facility.label.fromDate")));
			addFilter(label1);
			
			TableFilter tfFromDate=new TableFilter("fromDateFilter");
			
			fromDateFilter.setOptional(true);
			tfFromDate.setWidget(fromDateFilter);
			addFilter(tfFromDate);
			
			TableFilter label2=new TableFilter("toDateLabel");
			label2.setWidget(new Label("toDate1",app.getMessage("fms.facility.label.toDate")));
			addFilter(label2);
			
			TableFilter tfEndDate=new TableFilter("toDateFilter");
			
			toDateFilter.setOptional(true);
			tfEndDate.setWidget(toDateFilter);
			addFilter(tfEndDate);

		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Date fromDate=(Date)fromDateFilter.getDate();
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				toDate=DateUtil.dateAdd(toDateFilter.getDate(), Calendar.DATE, 1);
			}
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				return dao.selectAssignmentCheckOutList(searchBy, fromDate, toDate, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Date fromDate=(Date)fromDateFilter.getDate();
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				toDate=DateUtil.dateAdd(toDateFilter.getDate(), Calendar.DATE, 1);
			}
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				return dao.selectAssignmentCheckOutListCount(searchBy,fromDate,toDate);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
			return "requestId";
		}
	}

}
