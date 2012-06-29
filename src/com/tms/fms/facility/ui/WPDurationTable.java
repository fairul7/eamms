package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
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

public class WPDurationTable extends Table {
	private Date startDate;
	private Date endDate;
	private Map manpowers;

	public WPDurationTable() {
	}

	public WPDurationTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new WPDurationTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new WPDurationTableModel());
	}

	class WPDurationTableModel extends TableModel {
		WPDurationTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			addAction(new TableAction("add", app.getMessage("fms.facility.add")));
			addAction(new TableAction("delete", app.getMessage("fms.facility.delete"), app.getMessage("fms.facility.msg.confirmDelete")));

			// table columns
			TableColumn tcManpower = new TableColumn("manpower", app.getMessage("fms.table.label.manpower"));
			tcManpower.setUrlParam("workingProfileDurationId");
			addColumn(tcManpower);
			
			TableColumn tcName = new TableColumn("name", app.getMessage("fms.facility.label.workingProfileName"));
			//tcName.setUrlParam("workingProfileDurationId");
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
			Map maps = manpowers;
			
			try {
				return dao.selectWorkingProfileDurationByManpower(searchBy, maps, startDate, endDate, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Map maps = manpowers;
			SetupDao dao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
			
			try {
				return dao.selectWorkingProfileDurationCountByManpower(searchBy, maps, startDate, endDate);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
			//return "workingProfileDurationId";
			return "wpdManpowerId";
		}

		public Forward processAction(Event event, String action,
				String[] selectedKeys) {
			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			if ("add".equals(action))
				return new Forward("ADD");
			else if ("delete".equals(action)) {
				try {
					for (int i = 0; i < selectedKeys.length; i++){
						String manpowerId = selectedKeys[i].split("~")[0];
						String workingProfileDurationId = selectedKeys[i].split("~")[1];
						
						if (manpowerId != null && workingProfileDurationId != null){
							//module.deleteWorkingProfileDuration(selectedKeys[i]);
							module.deleteWorkingProfileDurationManpower(workingProfileDurationId, manpowerId);
						}
					}
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
					return new Forward("NOTDELETED");
				}
			}
			return super.processAction(event, action, selectedKeys);
			
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Map getManpowers() {
		return manpowers;
	}

	public void setManpowers(Map manpowers) {
		this.manpowers = manpowers;
	}
	
}
