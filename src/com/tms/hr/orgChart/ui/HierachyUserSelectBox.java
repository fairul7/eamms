package com.tms.hr.orgChart.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.orgChart.model.StaffHierachy;

public class HierachyUserSelectBox extends PopupSelectBox {
    public HierachyUserSelectBox() {
        super();
    }

    public HierachyUserSelectBox(String name) {
        super(name);
    }

    protected Table initPopupTable() {
        return new UsersPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
            Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", ids, DaoOperator.OPERATOR_AND));
            Collection userList = security.getUsers(query, 0, -1, "firstName", false);

            // build users map
            Map tmpMap = new SequencedHashMap();
            for (Iterator i=userList.iterator(); i.hasNext();) {
                User user = (User)i.next();
                tmpMap.put(user.getId(), user.getName());
            }

            // sort
            for (int j=0; j<ids.length; j++) {
                String name = (String)tmpMap.get(ids[j]);
                if (name == null) {
                    name = "---";
                }
                usersMap.put(ids[j], name);
            }
        }
        catch (kacang.services.security.SecurityException e) {
            Log.getLog(getClass()).error("Error retrieving users", e);
        }

        return usersMap;
    }

    public String getId() {
        Map optionMap = getOptionMap();
        if (optionMap != null) {
            Collection idSet = optionMap.keySet();
            idSet.remove("");
            String[] idArray = (String[])idSet.toArray(new String[0]);
            return idArray[0];
        }
        else {
            return new String();
        }
    }

    public class UsersPopupTable extends PopupSelectBoxTable {

        public UsersPopupTable() {
        }

        public UsersPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new UsersPopupTable.UserTableModel());
        }

        public void onRequest(Event evt) {
            
        }

        public class UserTableModel extends PopupSelectBoxTableModel {
        	private Map countries = new LinkedHashMap();
            private Map titles = new LinkedHashMap();
            private Map stations = new LinkedHashMap();
            private Map depts = new LinkedHashMap();
        	
            public UserTableModel() {
                super();

                Application app = Application.getInstance();
                TableColumn countryCol = new TableColumn("countryCode", app.getMessage("orgChart.country.label.country"));
                addColumn(countryCol);
                TableColumn stationCol = new TableColumn("stationCode", app.getMessage("orgChart.station.label.station"));
                addColumn(stationCol);
                TableColumn deptCol = new TableColumn("deptCode", app.getMessage("orgChart.department.label.department"));
                addColumn(deptCol);
                TableColumn nameCol = new TableColumn("staffName", app.getMessage("orgChart.hierachy.label.name"));
                nameCol.setSortable(false);
                addColumn(nameCol);
                TableColumn titleCol = new TableColumn("titleCode", app.getMessage("orgChart.title.label.title"));
                addColumn(titleCol);
                
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
                
                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  Application.getInstance().getMessage("general.label.select", "Select")));
                
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

                return oc.findAllHierachyForTableWithFilter(strName, getStart(), getRows(), getSort(), isDesc(), countries, depts, stations, titles,strCountry, true, strDept, strTitle);

            }

            public int getTotalRowCount() {
            	Application app = Application.getInstance();
                List selectedCountry = (List) getFilterValue("countryFilter");
                List selectedDept = (List) getFilterValue("deptFilter");
                List selectedTitle = (List) getFilterValue("titleFilter");
                List selectedName = (List) getFilterValue("nameFilter");
                String strCountry = null;
                String strDept = null;
                String strTitle = null;
                String strName = null;

                if(selectedName != null && selectedName.size() > 0){
                    strName = selectedName.get(0).toString().equals("")? null : selectedName.get(0).toString();
                }

                if(selectedCountry != null && selectedCountry.size() > 0){
                    strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
                }

                if(selectedCountry != null && selectedDept.size() > 0){
                    strDept = selectedDept.get(0).toString().startsWith("---")? null : selectedDept.get(0).toString();
                }

                if(selectedTitle != null && selectedTitle.size() > 0){
                    strTitle = selectedTitle.get(0).toString().startsWith("---")? null : selectedTitle.get(0).toString();
                }
                OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);

               return oc.countAllHierachy(strName, true, strDept, strCountry, strTitle);
            }
            
            public Forward processAction(Event event, String action, String[] selectedKeys) {
                try {
                    if (PopupSelectBox.FORWARD_SELECT.equals(action)) {
                        if (getPopupSelectBox() != null) {
                            getPopupSelectBox().setIds(selectedKeys);
                            
                            OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
                            StaffHierachy sh = oc.findStaffHierachy(selectedKeys[0]);
                            
                            //add the user into session and pick up by hierarchyform to populate related info to the fields
                            event.getRequest().getSession().setAttribute("hierachy_user_2", sh);
                            
                            return new Forward(PopupSelectBox.FORWARD_SELECT);
                        }
                        else {
                            return null;
                        }
                    }
                    else {
                        return null;
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                    return new Forward(PopupSelectBox.FORWARD_ERROR);
                }
            }

            public String getTableRowKey() {
                return "userId";
            }

        }
    }
}
