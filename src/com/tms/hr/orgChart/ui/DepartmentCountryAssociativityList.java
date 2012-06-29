package com.tms.hr.orgChart.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;

public class DepartmentCountryAssociativityList extends Table {
	public void init(){
        super.init();
        DepartmentCountryAssociativityListModel model = new DepartmentCountryAssociativityListModel();
        setModel(model);
        setWidth("100%");
    }
	
	public class DepartmentCountryAssociativityListModel extends TableModel {
		public DepartmentCountryAssociativityListModel() {
			Application app = Application.getInstance();

            TableColumn deptCodeCol = new TableColumn("deptDesc", app.getMessage("orgChart.department.label.department", "Department"));
            addColumn(deptCodeCol);
			
			TableColumn countryCodeCol = new TableColumn("countryDesc", app.getMessage("orgChart.country.label.country", "Country"));
            addColumn(countryCodeCol);
            
            SelectBox deptSelect = new SelectBox("deptSelect");
            deptSelect.setMultiple(false);
            deptSelect.setOptionMap(getOptionList(OrgChartHandler.TYPE_DEPT));
            TableFilter deptFilter = new TableFilter("deptFilter");
            deptFilter.setWidget(deptSelect);
            addFilter(deptFilter);
            
            SelectBox countrySelect = new SelectBox("countrySelect");
            countrySelect.setMultiple(false);
            countrySelect.setOptionMap(getOptionList(OrgChartHandler.TYPE_COUNTRY));
            TableFilter countryFilter = new TableFilter("countryFilter");
            countryFilter.setWidget(countrySelect);
            addFilter(countryFilter);
            
            addAction(new TableAction("delete", app.getMessage("deal.label.delete", "Delete"), app.getMessage("orgChart.association.alert.delete", "Confirm delete selected mapping(s)?")));
		}
		
		private Map getOptionList(String type) {
	    	OrgChartHandler module = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
	    	Collection setupCol = module.findAllSetup(type, null, 0, -1, "code", false, false);
	    	Map setupList = new SequencedHashMap();
	    	
	    	if(OrgChartHandler.TYPE_COUNTRY.equals(type)) {
	    		setupList.put("", Application.getInstance().getMessage("orgChart.general.label.selectCountry", "---Select Country---"));
	    	}
	    	else if(OrgChartHandler.TYPE_DEPT.equals(type)) {
	    		setupList.put("", Application.getInstance().getMessage("orgChart.general.label.selectDept", "---Select Dept---"));
	    	}
	    	
	    	if(setupCol != null) 
	    	{
	    		for(Iterator i=setupCol.iterator(); i.hasNext();) {
	    			OrgSetup setup = (OrgSetup) i.next();
	    			setupList.put(setup.getCode(), setup.getShortDesc());
	    		}
	    	}
	    	
	    	return setupList;
	    }
		
		public Collection getTableRows() {
            Application application = Application.getInstance();
            OrgChartHandler module = (OrgChartHandler) application.getModule(OrgChartHandler.class);
            
            SelectBox countrySelect = (SelectBox) getFilter("countryFilter").getWidget();
            List countryCols = (List) countrySelect.getValue();
            String countryCode = null;
            if (countryCols.size() > 0) {
            	countryCode = (String) countryCols.get(0);
            }
            
            SelectBox deptSelect = (SelectBox) getFilter("deptFilter").getWidget();
            List deptCols = (List) deptSelect.getValue();
            String deptCode = null;
            if (deptCols.size() > 0) {
            	deptCode = (String) deptCols.get(0);
            }
            
            return module.selectDepartmentCountryAssociativity(null, deptCode, countryCode, getSort(), isDesc(), getStart(), getRows());
        }
		
		public int getTotalRowCount() {
			Application application = Application.getInstance();
            OrgChartHandler module = (OrgChartHandler) application.getModule(OrgChartHandler.class);
            
            SelectBox countrySelect = (SelectBox) getFilter("countryFilter").getWidget();
            List countryCols = (List) countrySelect.getValue();
            String countryCode = null;
            if (countryCols.size() > 0) {
            	countryCode = (String) countryCols.get(0);
            }
            
            SelectBox deptSelect = (SelectBox) getFilter("deptFilter").getWidget();
            List deptCols = (List) deptSelect.getValue();
            String deptCode = null;
            if (deptCols.size() > 0) {
            	deptCode = (String) deptCols.get(0);
            }
            
            return module.countDepartmentCountryAssociativity(null, deptCode, countryCode);
        }
		
		public String getTableRowKey() {
            return "associativityId";
        }
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application application = Application.getInstance();
            OrgChartHandler module = (OrgChartHandler) application.getModule(OrgChartHandler.class);
            
            if ("delete".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteDepartmentCountryAssociativity(selectedKeys[i]);
                }
            }
            return null;
        }
	}
}