package com.tms.hr.orgChart.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.orgChart.model.OrgChartHandler;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 16, 2006
 * Time: 12:13:19 PM
 */
public class StationList extends Table {
     StationListModel model;

    public StationList(){
        super();
    }

    public StationList(String name){
        super(name);
    }

    public void init(){
        super.init();
        model = new StationListModel();
        setModel(model);
        setWidth("100%");
    }


    public class StationListModel extends TableModel {

        public StationListModel(){
            super();
            Application app = Application.getInstance();
            TableColumn stationCodeCol = new TableColumn("code", app.getMessage("orgChart.station.label.stationCode"));
            addColumn(stationCodeCol);
            TableColumn shortDescCol = new TableColumn("shortDesc", app.getMessage("orgChart.station.label.station"));
            addColumn(shortDescCol);
            TableColumn chkActiveCol = new TableColumn("active", app.getMessage("orgChart.general.label.active"));
            addColumn(chkActiveCol);
            addAction(new TableAction("activate", app.getMessage("orgChart.general.label.activate"), app.getMessage("orgChart.general.warn.activate")));
            addAction(new TableAction("deactivate", app.getMessage("orgChart.general.label.deactivate"), app.getMessage("orgChart.general.warn.deactivate")));
            addAction(new TableAction("delete", app.getMessage("general.label.delete"), app.getMessage("orgChart.station.warn.delete")));
            addFilter(new TableFilter("shortDesc"));
        }

        public Collection getTableRows() {
            Application app = Application.getInstance();
            String shortDesc = (String) getFilterValue("shortDesc");
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            return oc.findAllSetup(OrgChartHandler.TYPE_STATION, shortDesc, getStart(), getRows(), getSort(), isDesc());
        }

        public int getTotalRowCount() {
            Application app = Application.getInstance();
            String shortDesc = (String) getFilterValue("shortDesc");
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            return oc.countAllSetup(OrgChartHandler.TYPE_STATION, shortDesc);
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application app = Application.getInstance();
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            if("delete".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.deleteSetup(OrgChartHandler.TYPE_STATION, selectedKeys[i]);
                }
                return new Forward("deleted");
            }else if("activate".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.activateSetup(OrgChartHandler.TYPE_STATION, selectedKeys[i], true);
                }
                return new Forward("activated");
            }else if("deactivate".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.activateSetup(OrgChartHandler.TYPE_STATION, selectedKeys[i], false);
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
