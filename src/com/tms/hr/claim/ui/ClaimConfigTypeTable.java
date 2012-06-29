package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfigTypeModule;

import kacang.Application;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 8, 2005
 * Time: 2:01:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimConfigTypeTable extends Table {
    String id;

    public void init() {
        setModel(new ClaimConfigTypeTableModule());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class ClaimConfigTypeTableModule extends TableModel {
        ClaimConfigTypeTableModule() {
            Application app = Application.getInstance();

            TableColumn naCol = new TableColumn("typeName",
                    app.getMessage("claims.type.name", "type"));
            naCol.setUrlParam("id");
            addColumn(naCol);

            TableColumn naCol2 = new TableColumn("accountcode",
                    "Code");

            addColumn(naCol2);


            addAction(new TableAction("delete",
                    app.getMessage("claims.label.delete", "delete"),app.getMessage("claims.label.note.confirm.delete.type")));


            addFilter(new TableFilter("keyword"));
        }

        public Collection getTableRows() {
            Application app = Application.getInstance();
            String keyword = (String)getFilterValue("keyword");
            ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);

            return module.retrieveAllType(keyword,getSort(), isDesc(), getStart(),
                getRows());
        }

        public int getTotalRowCount() {
            Application app = Application.getInstance();
            String keyword = (String) getFilterValue("keyword");

            ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);

            return module.countRetrieveAllType(keyword);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            Application app = Application.getInstance();
            ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);

            if (selectedKeys.length <= 0) {
                return new Forward("notSelected");
            }

            if ("delete".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteType(selectedKeys[i]);
                }

                return new Forward("delete");
            }

            return null;
        }
    }
}
