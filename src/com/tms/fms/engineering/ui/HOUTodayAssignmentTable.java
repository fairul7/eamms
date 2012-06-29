package com.tms.fms.engineering.ui;

import java.util.Collection;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.util.WidgetUtil;

public class HOUTodayAssignmentTable extends Table {
	
	protected SelectBox sbDepartment;
	protected int count;
	
	public DatePopupField requiredFieldTo;
	
	public HOUTodayAssignmentTable() {
	}

	public HOUTodayAssignmentTable(String s) {
		super(s);
	}
	
	public void onRequest(Event event) {
		setModel(new HOUTodayAssignmentTableModel());
		setCurrentPage(1);
	}
	public void init() {
		super.init();
		setPageSize(20);
		setModel(new HOUTodayAssignmentTableModel());
		setWidth("100%");
	}
	
	class HOUTodayAssignmentTableModel extends TableModel {
		HOUTodayAssignmentTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn requestId = new TableColumn("requestId", app.getMessage("fms.request.label.requestId"));
			requestId.setUrlParam("requestId");
			addColumn(requestId);
			
			TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"));
			addColumn(title);
			
			TableColumn servicesCSV = new TableColumn("servicesCSV", app.getMessage("fms.request.label.servicesRequired"), false);
			addColumn(servicesCSV);
			
			TableColumn requiredFrom = new TableColumn("requiredDateFrom", app.getMessage("fms.facility.label.requiredDate"));
			addColumn(requiredFrom);
			
			TableColumn requestorName = new TableColumn("createdBy", app.getMessage("fms.facility.assignment.requestorName"));
			addColumn(requestorName);
			
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
			
			
		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			String userId = Application.getInstance().getCurrentUser().getId();
			return module.getHOUTodaysAssignment(searchBy, dept, userId, getSort(), isDesc(), getStart(), getRows());
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			String userId = Application.getInstance().getCurrentUser().getId();
			UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			return module.countHOUTodaysAssignment(searchBy, dept, userId);
		}
	}

}