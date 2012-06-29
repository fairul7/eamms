package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
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
import com.tms.fms.util.WidgetUtil;

public class TodaysAssignmentHOUTable extends Table {
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField();
	protected SelectBox sbDepartment;
	
	public TodaysAssignmentHOUTable() {
	}

	public TodaysAssignmentHOUTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new TodaysAssignmentHOUTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new TodaysAssignmentHOUTableModel());
	}

	class TodaysAssignmentHOUTableModel extends TableModel {
		TodaysAssignmentHOUTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"));
			title.setUrl("todaysAssignment.jsp");
			title.setUrlParam("groupId");
			addColumn(title);
			
			TableColumn code = new TableColumn("groupId", app.getMessage("fms.label.groupAssignmentCode"));
			addColumn(code);
			
			TableColumn firstName = new TableColumn("firstName", app.getMessage("fms.facility.form.requestor"));
			addColumn(firstName);
			
			TableColumn department = new TableColumn("department", app.getMessage("fms.facility.table.department"));
			addColumn(department);

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
			
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			try {
				return module.getTodaysAssignmentsHOU(searchBy,dept, null, null, getSort(), isDesc(), true, getStart(), getRows());
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			try {
				//return dao.selectTodaysAssignmentCount(searchBy,dept);
				//return dao.selectAssignmentHOUCount(searchBy, dept, null, null);
				return module.countTodaysAssignmentsHOU(searchBy, dept, null, null, getSort(), isDesc(), true);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
				return "groupId";
		}

		/*public Forward processAction(Event event, String action,
				String[] selectedKeys) {
			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			if ("add".equals(action))
				return new Forward("ADD");
			else if ("delete".equals(action)) {
				try {
					for (int i = 0; i < selectedKeys.length; i++){
						module.deleteWorkingProfile(selectedKeys[i]);
					}
					
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
					return new Forward("NOTDELETED");
				}
			}
			return super.processAction(event, action, selectedKeys);
			
		}*/
	}

}
