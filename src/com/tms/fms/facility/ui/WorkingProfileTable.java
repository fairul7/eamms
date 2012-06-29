package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.facility.model.SetupDao;
import com.tms.fms.facility.model.SetupModule;

public class WorkingProfileTable extends Table {

	public WorkingProfileTable() {
	}

	public WorkingProfileTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new WorkingProfileTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new WorkingProfileTableModel());
	}

	class WorkingProfileTableModel extends TableModel {
		WorkingProfileTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			addAction(new TableAction("add", app.getMessage("fms.facility.add")));
			addAction(new TableAction("delete", app.getMessage("fms.facility.delete"), app.getMessage("fms.facility.msg.confirmDelete")));

			// table columns
			TableColumn tcName = new TableColumn("name", app.getMessage("fms.facility.label.workingProfileName"));
			tcName.setUrlParam("workingProfileId");
			addColumn(tcName);
			
			TableColumn tcDesc = new TableColumn("description", app.getMessage("fms.label.description"));
			addColumn(tcDesc);
			
			TableColumn tcWorkingHours = new TableColumn("workingHours", app.getMessage("fms.facility.label.workingHours"));
			addColumn(tcWorkingHours);
			
			TableColumn tcDefaultProfile = new TableColumn("defaultProfile", app.getMessage("fms.facility.label.defaultProfile"));
			tcDefaultProfile.setFormat(new TableBooleanFormat("<img src='/common/table/booleantrue.gif'>",""));
			addColumn(tcDefaultProfile);

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
				return dao.selectWorkingProfile(searchBy, getSort(), isDesc(), getStart(), getRows());
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
				return dao.selectWorkingProfileCount(searchBy);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
				return "workingProfileId";
		}

		public Forward processAction(Event event, String action,
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
			
		}
	}

}
