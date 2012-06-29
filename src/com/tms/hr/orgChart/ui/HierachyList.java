package com.tms.hr.orgChart.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.*;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 16, 2006
 * Time: 2:24:47 PM
 */
public class HierachyList extends Table {
	 private String type ="";
	 private String query ="";
     HierachyListModel model;

    public HierachyList(){
        super();
    }

    public HierachyList(String name){
        super(name);
    }

    public void onRequest(Event evt) {
    	super.onRequest(evt);
    	init();	
        TextField q = (TextField)getModel().getFilter("nameFilter").getWidget();
        q.setValue(query);
        
    	
    }
    public void init(){
        super.init();
        model = new HierachyListModel();
        setModel(model);
        setWidth("100%");
        
    }


    public class HierachyListModel extends TableModel{
        private Map countries = new LinkedHashMap();
        private Map titles = new LinkedHashMap();
        private Map stations = new LinkedHashMap();
        private Map depts = new LinkedHashMap();


        public HierachyListModel() {
            super();
            Application app = Application.getInstance();
            TableColumn countryCol = new TableColumn("countryCode", app.getMessage("orgChart.country.label.country"));
            addColumn(countryCol);
            TableColumn stationCol = new TableColumn("stationCode", app.getMessage("orgChart.station.label.station"));
            addColumn(stationCol);
            TableColumn deptCol = new TableColumn("deptCode", app.getMessage("orgChart.department.label.department"));
            addColumn(deptCol);
            TableColumn nameCol = new TableColumn("staffName", app.getMessage("orgChart.hierachy.label.name"));
            nameCol.setUrlParam("userId");
            nameCol.setSortable(false);
            addColumn(nameCol);
            TableColumn titleCol = new TableColumn("titleCode", app.getMessage("orgChart.title.label.title"));
            addColumn(titleCol);
            TableColumn noSubCol = new TableColumn("noSubordinates", app.getMessage("orgChart.hierachy.label.noSubordinates"));
            noSubCol.setSortable(false);
            addColumn(noSubCol);
            TableColumn activeCol = new TableColumn("active", app.getMessage("orgChart.general.label.active"));
            addColumn(activeCol);
            if(!"search".equals(type)){
            addAction(new TableAction("add", app.getMessage("general.label.add")));
            addAction(new TableAction("delete", app.getMessage("general.label.delete"), app.getMessage("orgChart.hierachy.alert.delete")));
            }
        	addFilter(new TableFilter("nameFilter"));

            


            // init OrgSetup Objects
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            Collection countriesCol = oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY);
            Collection titlesCol = oc.findAllSetup(OrgChartHandler.TYPE_TITLE);
            Collection stationsCol = oc.findAllSetup(OrgChartHandler.TYPE_STATION);
            Collection deptsCol = oc.findAllSetup(OrgChartHandler.TYPE_DEPT);

            countries.put("---", app.getMessage("general.hierachy.selectCountry"));
            for(Iterator itr = countriesCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                countries.put(obj.getCode(), obj.getShortDesc());
            }

            titles.put("---", app.getMessage("general.hierachy.selectTitle"));
            for(Iterator itr = titlesCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                titles.put(obj.getCode(), obj.getShortDesc());
            }


            for(Iterator itr = stationsCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                stations.put(obj.getCode(), obj.getShortDesc());
            }

            depts.put("---", app.getMessage("general.hierachy.selectDept"));
            for(Iterator itr = deptsCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                depts.put(obj.getCode(), obj.getShortDesc());
            }

            TableFilter countryFilter = new TableFilter("countryFilter");
            SelectBox countrySel = new SelectBox("countrySel", countries, null, false, 1);
            countryFilter.setWidget(countrySel);
            addFilter(countryFilter);

            TableFilter deptFilter = new TableFilter("deptFilter");
            SelectBox deptSel = new SelectBox("deptSel", depts, null, false, 1);
            deptFilter.setWidget(deptSel);
            addFilter(deptFilter);

            TableFilter titleFilter = new TableFilter("titleFilter");
            SelectBox titleSel = new SelectBox("titleSel", titles, null, false, 1);
            titleFilter.setWidget(titleSel);
            addFilter(titleFilter);
        }

        public Collection getTableRows() {
            Application app = Application.getInstance();
            List selectedCountry = (List) getFilterValue("countryFilter");
            List selectedDept = (List) getFilterValue("deptFilter");
            List selectedTitle = (List) getFilterValue("titleFilter");
            String strCountry = null;
            String strDept = null;
            String strTitle = null;
            String strName = (String) getFilterValue("nameFilter");

            if(selectedCountry != null && selectedCountry.size() > 0){
                strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
            }

            if(selectedDept != null && selectedDept.size() > 0){
                strDept = selectedDept.get(0).toString().startsWith("---")? null : selectedDept.get(0).toString();
            }

            if(selectedTitle !=null && selectedTitle.size() > 0){
                strTitle = selectedTitle.get(0).toString().startsWith("---")? null : selectedTitle.get(0).toString();
            }

            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);

            return oc.findAllHierachyForTableWithFilter(strName, getStart(), getRows(), getSort(), isDesc(), countries, depts, stations, titles,strCountry, false, strDept, strTitle);

        }

        public int getTotalRowCount() {
            Application app = Application.getInstance();
            List selectedCountry = (List) getFilterValue("countryFilter");
            List selectedDept = (List) getFilterValue("deptFilter");
            List selectedTitle = (List) getFilterValue("titleFilter");
            String strCountry = null;
            String strDept = null;
            String strTitle = null;
            String strName = (String) getFilterValue("nameFilter");

            if(selectedCountry != null && selectedCountry.size() > 0){
                strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
            }

            if(selectedDept != null && selectedDept.size() > 0){
                strDept = selectedDept.get(0).toString().startsWith("---")? null : selectedDept.get(0).toString();
            }

            if(selectedTitle !=null && selectedTitle.size() > 0){
                strTitle = selectedTitle.get(0).toString().startsWith("---")? null : selectedTitle.get(0).toString();
            }

            if(selectedTitle != null && selectedTitle.size() > 0){
                strTitle = selectedTitle.get(0).toString().startsWith("---")? null : selectedTitle.get(0).toString();
            }
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);

           return oc.countAllHierachy(strName, false, strDept, strCountry, strTitle);
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application app = Application.getInstance();
            if("add".equals(action)){
                return new Forward("add");

            }else if("delete".equals(action)){
                OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
                for(int i=0; i < selectedKeys.length;i++){
                    oc.deleteHierachy(selectedKeys[i]);
                }
                return new Forward("delete");
            }
            return new Forward("error");
        }

        public String getTableRowKey() {
            return "userId";
        }
    }


	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
}