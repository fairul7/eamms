package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.SelectBox;
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

import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.facility.model.SetupDao;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.hr.competency.CompetencyHandler;

public class ManpowerTable extends Table {
	protected SelectBox unit;

	public ManpowerTable() {
	}

	public ManpowerTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new ManpowerTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new ManpowerTableModel());
	}

	class ManpowerTableModel extends TableModel {
		ManpowerTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			addAction(new TableAction("add", app.getMessage("fms.facility.add")));
			addAction(new TableAction("delete", app.getMessage("fms.facility.delete"), app.getMessage("fms.facility.msg.confirmDelete")));

			// table columns
			//TableColumn tcName = new TableColumn("firstName", app.getMessage("fms.facility.label.manpower"));
			TableColumn tcName = new TableColumn("manpowerName", app.getMessage("fms.facility.label.manpower"));
			//tcName.setUrlParam("workingProfileDurationId");
			addColumn(tcName);
			
			TableColumn competencyName = new TableColumn("competencyName", app.getMessage("fms.facility.label.competency"));
			competencyName.setEscapeXml(false);
			addColumn(competencyName);
			
			TableColumn competencyLevel = new TableColumn("competencyLevel", app.getMessage("fms.facility.label.level"));
			competencyLevel.setEscapeXml(false);
			addColumn(competencyLevel);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter filterUnit = new TableFilter("filterUnit");
			unit = new SelectBox("unit");
	        try {
		    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
		    	
				SetupModule module=(SetupModule)app.getModule(SetupModule.class);
				
		    	Collection lstUnit = module.getUnits(app.getCurrentUser().getId());//dao.selectUnit();
				unit.addOption("-1", "Unit Filter");
			    if (lstUnit.size() > 0) {
			    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
			        	FMSUnit o = (FMSUnit)i.next();
			        	unit.addOption(o.getId(),o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			filterUnit.setWidget(unit);
			addFilter(filterUnit);
		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			try {
				return module.selectManpowerSetup(searchBy, Application.getInstance().getCurrentUser().getId(), getUnitFilter(), getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");

			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			try {
				return module.selectManpowerSetupCount(searchBy, Application.getInstance().getCurrentUser().getId(), getUnitFilter());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
				return "userId";
		}
		
		public String getUnitFilter(){
			String unitFilter = "";
			unitFilter = WidgetUtil.getSbValue(unit);
			
			if (unitFilter != null && "-1".equals(unitFilter)) unitFilter = null;
			
			return unitFilter;
		}
		
		public Forward processAction(Event event, String action,
				String[] selectedKeys) {
			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			if ("add".equals(action))
				return new Forward("ADD");
			else if ("delete".equals(action)) {
				try {
					CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
					for (int i = 0; i < selectedKeys.length; i++){
			            handler.deleteUserCompetencies(selectedKeys[i]);
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
