package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Collection;

import com.tms.crm.sales.model.SalesGroupModule;
import com.tms.crm.sales.model.SalesGroupException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 29, 2004
 * Time: 4:12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesGroupTable extends Table{
    public SalesGroupTable() {
    }

    public SalesGroupTable(String s) {
        super(s);
    }

    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setNumbering(true);
        setModel(new SalesGroupTableModel());
    }


    public class SalesGroupTableModel extends TableModel{
        public SalesGroupTableModel() {
            TableColumn nameColumn = new TableColumn("name",Application.getInstance().getMessage("sfa.label.name","Name"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);
            addColumn(new TableColumn("description",Application.getInstance().getMessage("sfa.label.description","Description")));
            addFilter(new TableFilter("search"));
            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.label.delete","Delete"),Application.getInstance().getMessage("sfa.label.suretodeletegroup","Are you sure you want to delete the selected groups?")));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            SalesGroupModule sgm = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
            return sgm.listGroups((String)getFilterValue("search"),getSort(),isDesc(),getStartIndex(),getRows());
            //return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int getTotalRowCount() {
            SalesGroupModule sgm = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
            return sgm.countGroups((String)getFilterValue("search"));
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                 SalesGroupModule sgm = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
                 for (int i = 0; i < selectedKeys.length; i++) {
                     String groupId = selectedKeys[i];
                     try {
                         sgm.deleteGroup(groupId);
                     } catch (SalesGroupException e) {
                         Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                     }
                 }
            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
