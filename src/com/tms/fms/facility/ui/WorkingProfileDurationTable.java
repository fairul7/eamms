package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.facility.model.SetupDao;
import com.tms.fms.facility.model.SetupModule;

public class WorkingProfileDurationTable extends Table {


	public WorkingProfileDurationTable() {
	}

	public WorkingProfileDurationTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new WorkingProfileDurationTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new WorkingProfileDurationTableModel());
	}

	class WorkingProfileDurationTableModel extends TableModel {
		WorkingProfileDurationTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			addAction(new TableAction("add", app.getMessage("fms.facility.add")));
			addAction(new TableAction("delete", app.getMessage("fms.facility.delete"), app.getMessage("fms.facility.msg.confirmDelete")));

			// table columns
			TableColumn tcName = new TableColumn("name", app.getMessage("fms.facility.label.workingProfileName"));
			tcName.setUrlParam("workingProfileDurationId");
			addColumn(tcName);
			
			TableColumn tcStartDate = new TableColumn("startDate", app.getMessage("fms.facility.label.dateFrom"));
			tcStartDate.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(tcStartDate);
			
			TableColumn tcEndDate = new TableColumn("endDate", app.getMessage("fms.facility.label.dateTo"));
			tcEndDate.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(tcEndDate);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);

		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			SetupDao dao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
			try {
				return dao.selectWorkingProfileDuration(searchBy, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");

			SetupDao dao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
			try {
				return dao.selectWorkingProfileDurationCount(searchBy);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
				return "workingProfileDurationId";
		}

		public Forward processAction(Event event, String action,
				String[] selectedKeys) {
			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			if ("add".equals(action))
				return new Forward("ADD");
			else if ("delete".equals(action)) {
				try {
					for (int i = 0; i < selectedKeys.length; i++){
						module.deleteWorkingProfileDuration(selectedKeys[i]);
					}
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
					return new Forward("NOTDELETED");
				}
			}
			return super.processAction(event, action, selectedKeys);
			
		}
	}

}
