package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

public class AssignmentHOUTable extends Table {
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField();
	protected SelectBox sbDepartment;
	
	public AssignmentHOUTable() {
	}

	public AssignmentHOUTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new AssignmentHOUTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new AssignmentHOUTableModel());
	}

	class AssignmentHOUTableModel extends TableModel {
		AssignmentHOUTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"));
			title.setUrl("assignmentListing.jsp");
			title.setUrlParam("groupId");
			addColumn(title);
			
			TableColumn code = new TableColumn("groupId", app.getMessage("fms.label.groupAssignmentCode"));
			addColumn(code);
			
			TableColumn firstName = new TableColumn("firstName", app.getMessage("fms.facility.form.requestor"));
			addColumn(firstName);
			
			TableColumn department = new TableColumn("department", app.getMessage("fms.facility.table.department"));
			addColumn(department);
			
			TableColumn requiredFrom = new TableColumn("requiredFrom", app.getMessage("fms.facility.label.requiredFrom"));
			requiredFrom.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(requiredFrom);
			
			TableColumn requiredTo = new TableColumn("requiredTo", app.getMessage("fms.facility.label.requiredTo"));
			requiredTo.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(requiredTo);
			
			TableColumn fromTime = new TableColumn("requiredTime", app.getMessage("fms.facility.label.requiredTime"));
			addColumn(fromTime);

			
			TableColumn items = new TableColumn("items", app.getMessage("fms.request.label.requestedItem"), false);
			addColumn(items);

			TableColumn status = new TableColumn("status", app.getMessage("fms.ratecard.table.label.status"), false);
			status.setFormat(new TableStringFormat(EngineeringModule.ASSIGNMENT_HOU_FACILITY_STATUS_MAP));
			addColumn(status);

			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter label1=new TableFilter("fromDateLabel");
			label1.setWidget(new Label("fromDate1",Application.getInstance().getMessage("fms.facility.label.fromDate")));
			addFilter(label1);
			
			TableFilter tfFromDate=new TableFilter("fromDateFilter");
			
			fromDateFilter.setOptional(true);
			fromDateFilter.setTemplate("extDatePopupField");
			fromDateFilter.setFormat("dd-MM-yyyy");
			fromDateFilter.setSize("10");
			Date from = new Date();
			Date to = new Date();
			int dayOfWeek = from.getDay();
			
			from.setDate((from.getDate()-dayOfWeek)+1);
			//to.setDate(from.getDate()+6);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(from);
			cal.add(Calendar.DATE, 6);
			to = cal.getTime();
			
			fromDateFilter.setDate(from);
			tfFromDate.setWidget(fromDateFilter);
			addFilter(tfFromDate);
			
			TableFilter label2=new TableFilter("toDateLabel");
			label2.setWidget(new Label("toDate1", Application.getInstance().getMessage("fms.facility.label.toDate")));
			addFilter(label2);
			
			TableFilter tfEndDate=new TableFilter("toDateFilter");
			
			toDateFilter.setOptional(true);
			toDateFilter.setTemplate("extDatePopupField");
			toDateFilter.setFormat("dd-MM-yyyy");
			toDateFilter.setSize("10");
			toDateFilter.setDate(to);
			tfEndDate.setWidget(toDateFilter);
			addFilter(tfEndDate);
			
			
			FMSDepartmentManager deptManager=(FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);
			TableFilter tfDepartment = new TableFilter("department");
			sbDepartment=new SelectBox("sbDepartment");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",app.getMessage("fms.facility.table.allDepartment"));
			try {
				typeMap.putAll(deptManager.getFMSDepartments());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			sbDepartment.setOptionMap(typeMap);
			tfDepartment.setWidget(sbDepartment);
			addFilter(tfDepartment);
			
			
		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			
			Date fromDate=(Date)fromDateFilter.getDate();
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			fromDate.setSeconds(0);
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				//toDate=DateUtil.dateAdd(toDateFilter.getDate(), Calendar.DATE, 1);
				toDate = (Date) toDateFilter.getDate();
				toDate.setHours(0);
				toDate.setMinutes(0);
				toDate.setSeconds(0);
			}
			
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			try {
				return module.getTodaysAssignmentsHOU(searchBy, dept, fromDate, toDate, getSort(), isDesc(), false, getStart(), getRows());
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			
			Date fromDate=(Date)fromDateFilter.getDate();
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			fromDate.setSeconds(0);
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				//toDate=DateUtil.dateAdd(toDateFilter.getDate(), Calendar.DATE, 1);
				toDate = (Date) toDateFilter.getDate();
				toDate.setHours(0);
				toDate.setMinutes(0);
				toDate.setSeconds(0);
			}
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				return module.countTodaysAssignmentsHOU(searchBy, dept, fromDate, toDate, getSort(), isDesc(), false);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
				return "groupId";
		}
	}

}
