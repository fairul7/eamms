package com.tms.hr.orgChart.ui;

import com.tms.hr.orgChart.model.OrgChartHandler;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 16, 2006
 * Time: 11:27:09 AM
 */
public class CountryList extends Table {
    CountryListModel model;

    public CountryList(){
        super();
    }

    public CountryList(String name){
        super(name);
    }

    public void init(){
        super.init();
        model = new CountryListModel();
        setModel(model);
        setWidth("100%");
    }


    public class CountryListModel extends TableModel {

        public CountryListModel(){
            super();
            Application app = Application.getInstance();
            TableColumn countryCodeCol = new TableColumn("code", app.getMessage("orgChart.country.label.countryCode"));
            addColumn(countryCodeCol);
            TableColumn shortDescCol = new TableColumn("shortDesc", app.getMessage("orgChart.country.label.country"));
            addColumn(shortDescCol);
            TableColumn chkActiveCol = new TableColumn("active", app.getMessage("orgChart.general.label.active"));
            addColumn(chkActiveCol);
            addAction(new TableAction("activate", app.getMessage("orgChart.general.label.activate"), app.getMessage("orgChart.general.warn.activate")));
            addAction(new TableAction("deactivate", app.getMessage("orgChart.general.label.deactivate"), app.getMessage("orgChart.general.warn.deactivate")));
            addAction(new TableAction("delete", app.getMessage("general.label.delete"), app.getMessage("orgChart.country.warn.delete")));
            addFilter(new TableFilter("shortDesc"));
        }

        public Collection getTableRows() {
            Application app = Application.getInstance();
            String shortDesc = (String) getFilterValue("shortDesc");
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            return oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY, shortDesc, getStart(), getRows(), getSort(), isDesc());
        }

        public int getTotalRowCount() {
            Application app = Application.getInstance();
            String shortDesc = (String) getFilterValue("shortDesc");
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            return oc.countAllSetup(OrgChartHandler.TYPE_COUNTRY, shortDesc);
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application app = Application.getInstance();
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            if("delete".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.deleteSetup(OrgChartHandler.TYPE_COUNTRY, selectedKeys[i]);
                }
                return new Forward("deleted");
            }else if("activate".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.activateSetup(OrgChartHandler.TYPE_COUNTRY, selectedKeys[i], true);
                }
                return new Forward("activated");
            }else if("deactivate".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.activateSetup(OrgChartHandler.TYPE_COUNTRY, selectedKeys[i], false);
                }
                return new Forward("deactivated");
            }
            return new Forward("error");
        }

        public String getTableRowKey() {
            return "code";
        }
    }
}

