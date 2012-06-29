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
 * Date: Mar 15, 2006
 * Time: 5:36:11 PM
 */
public class DepartmentList extends Table {
    DepartmentListModel model;

    public DepartmentList(){
        super();
    }

    public DepartmentList(String name){
        super(name);
    }

    public void init(){
        super.init();
        model = new DepartmentListModel();
        setModel(model);
        setWidth("100%");
    }


    public class DepartmentListModel extends TableModel{

        public DepartmentListModel(){
            super();
            Application app = Application.getInstance();
            TableColumn deptCodeCol = new TableColumn("code", app.getMessage("orgChart.department.label.deptCode"));
            deptCodeCol.setUrlParam("code");
            addColumn(deptCodeCol);
            TableColumn shortDescCol = new TableColumn("shortDesc", app.getMessage("orgChart.department.label.department"));
            addColumn(shortDescCol);
            TableColumn deptDescCol = new TableColumn("deptDesc", app.getMessage("orgChart.department.label.departmentSection"));
            addColumn(deptDescCol);
            TableColumn chkActiveCol = new TableColumn("active", app.getMessage("orgChart.general.label.active"));
            addColumn(chkActiveCol);
            addAction(new TableAction("activate", app.getMessage("orgChart.general.label.activate"), app.getMessage("orgChart.general.warn.activate")));
            addAction(new TableAction("deactivate", app.getMessage("orgChart.general.label.deactivate"), app.getMessage("orgChart.general.warn.deactivate")));
            addAction(new TableAction("delete", app.getMessage("general.label.delete"), app.getMessage("orgChart.department.warn.delete")));
            addFilter(new TableFilter("shortDesc"));
        }

        public Collection getTableRows() {
            Application app = Application.getInstance();
            String shortDesc = (String) getFilterValue("shortDesc");
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            return oc.findDeptSetup(shortDesc, getStart(), getRows(), getSort(), isDesc());
        }

        public int getTotalRowCount() {
            Application app = Application.getInstance();
            String shortDesc = (String) getFilterValue("shortDesc");
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            return oc.countAllSetup(OrgChartHandler.TYPE_DEPT, shortDesc);
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application app = Application.getInstance();
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            if("delete".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.deleteSetup(OrgChartHandler.TYPE_DEPT, selectedKeys[i]);
                }
                return new Forward("deleted");
            }else if("activate".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.activateSetup(OrgChartHandler.TYPE_DEPT, selectedKeys[i], true);
                }
                return new Forward("activated");
            }else if("deactivate".equals(action)){
                for(int i=0; i < selectedKeys.length; i++){
                    oc.activateSetup(OrgChartHandler.TYPE_DEPT, selectedKeys[i], false);
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
