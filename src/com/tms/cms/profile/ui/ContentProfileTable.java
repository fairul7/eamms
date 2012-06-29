package com.tms.cms.profile.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Collection;
import java.util.ArrayList;

import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.core.model.ContentException;

public class ContentProfileTable extends Table {

    public ContentProfileTable() {
    }

    public ContentProfileTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new ContentProfileTableModel());
    }

    public class ContentProfileTableModel extends TableModel {
        public static final String ACTION_ADD = "add";
        public static final String ACTION_DELETE = "delete";
        public static final String FORWARD_ADD = "add";
        public static final String FORWARD_DELETE = "delete";
        public static final String FORWARD_ERROR = "error";

        public ContentProfileTableModel() {
            Application app = Application.getInstance();
            TableColumn nc = new TableColumn("name", app.getMessage("general.label.name", "Name"));
            nc.setUrlParam("id");
            addColumn(nc);
            addColumn(new TableColumn("description", app.getMessage("general.label.description", "Description")));

            addFilter(new TableFilter("name"));

            addAction(new TableAction(ACTION_ADD, app.getMessage("cms.label.profileAdd", "New Profile")));
            addAction(new TableAction(ACTION_DELETE, app.getMessage("general.label.delete", "Delete")));
        }

        public Collection getTableRows() {
            try {
                String name = (String)getFilterValue("name");

                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);

                return cpm.getProfileList(name, getSort(), isDesc(), getStart(), getRows());
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Error retrieving profile list", e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            try {
                String name = (String)getFilterValue("name");

                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);

                return cpm.getProfileCount(name);
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Error retrieving profile count", e);
                return 0;
            }
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event event, String action, String[] keys) {

            try {
                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);

                if (ACTION_ADD.equals(action)) {
                    return new Forward(FORWARD_ADD);
                }
                else if (ACTION_DELETE.equals(action)) {
                    for (int i=0; i<keys.length; i++) {
                        cpm.deleteProfile(keys[i]);
                    }
                    return new Forward(FORWARD_DELETE);
                }
                else {
                    return super.processAction(event, action, keys);
                }
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Error processing profile action " + action, e);
                return new Forward(FORWARD_ERROR);
            }

        }

    }
}
