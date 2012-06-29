package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.util.PagingUtil;
import com.tms.fms.util.WidgetUtil;

public class AllAssignmentFCTable extends Table {
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField();
	protected SelectBox sbDepartment;
	protected SelectBox sbStatus;
	public AllAssignmentFCTable() {
	}

	public AllAssignmentFCTable(String s) {
		super(s);
	}

	public void onRequest(Event event) {
		setModel(new AllAssignmentFCTableModel());
		Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/engineering/allAssignmentFCTemplate");
		init();
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		setModel(new AllAssignmentFCTableModel());
		setWidth("100%");
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/engineering/allAssignmentFCTemplate");
	}


	class AllAssignmentFCTableModel extends TableModel {
		int count;
		AllAssignmentFCTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"), false);
			addColumn(title);
			
			TableColumn code = new TableColumn("code", app.getMessage("fms.facility.label.assignmentId"));
			addColumn(code);

			TableColumn requestId = new TableColumn("requestId", app.getMessage("fms.label.requestId"), false);
			addColumn(requestId);
			
			
			TableColumn firstName = new TableColumn("firstName", app.getMessage("fms.facility.form.requestor"), false);
			addColumn(firstName);
			
			TableColumn department = new TableColumn("department", app.getMessage("fms.facility.table.department"), false);
			addColumn(department);
			
			TableColumn items = new TableColumn("items", app.getMessage("fms.request.label.requestedItem"), false);
			addColumn(items);
			
/*			TableColumn fromTime = new TableColumn("requiredTime", app.getMessage("fms.facility.label.requiredTime"), false);
			addColumn(fromTime);*/
			
			TableColumn fromDateTime = new TableColumn("requiredDateTime", app.getMessage("fms.facility.label.requiredDate")+" "+ app.getMessage("weblog.label.time"), false);
			addColumn(fromDateTime);

			TableColumn status = new TableColumn("status", app.getMessage("fms.ratecard.table.label.status"), false);
			//status.setFormat(new TableStringFormat(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_MAP));
			addColumn(status);

			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
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
			
			TableFilter tfStatus = new TableFilter("status");
			SequencedHashMap statusMap=new SequencedHashMap();
			statusMap.put("-1",app.getMessage("fms.facility.table.status"));
			statusMap.putAll(EngineeringModule.ASSIGNMENT_ALL_STATUS_MAP);
			sbStatus=new SelectBox("sbStatus");
			sbStatus.setOptionMap(statusMap);
			tfStatus.setWidget(sbStatus);
			addFilter(tfStatus);
			
			fromDateFilter = new DatePopupField("requiredFrom");
			fromDateFilter.setOptional(true);
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
			TableFilter requiredFrom = new TableFilter("requiredFrom");
			requiredFrom.setWidget(fromDateFilter);
			addFilter(requiredFrom);
			
			toDateFilter = new DatePopupField("requiredTo");
			toDateFilter.setOptional(true);
			toDateFilter.setFormat("dd-MM-yyyy");
			toDateFilter.setSize("10");
			toDateFilter.setDate(to);
			TableFilter requiredTo= new TableFilter("requiredTo");
			requiredTo.setWidget(toDateFilter);
			addFilter(requiredTo);
		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			String status = WidgetUtil.getSbValue(sbStatus);
			Date requiredFrom = fromDateFilter.getDate();
			Date requiredTo = toDateFilter.getDate();
			
			FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule) Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
			
			try {
				Collection col = mod.getAllRequest(searchBy, dept, status, requiredFrom, requiredTo,  getSort(), isDesc(), 0, -1);
				count = col.size();
				return PagingUtil.getPagedCollection(col, getStart(), getRows());
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			/*String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			//FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) Application.getInstance().getModule(FacilitiesCoordinatorModule.class).getDao();
			FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule) Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
			try {
				//return dao.countAllRequestFC(searchBy,dept);
				return mod.getAllRequest(searchBy,dept, getSort(), isDesc(), 0, -1).size();
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}*/
			return count;
		}

		public String getTableRowKey() {
				return "assignmentId";
		}

		
	}

}
