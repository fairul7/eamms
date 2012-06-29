package com.tms.cms.ad.ui;

import com.tms.cms.ad.model.AdException;
import com.tms.cms.ad.model.AdModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Collection;
import java.util.HashSet;

public class AdTable extends Table {

    public AdTable() {
        super();
        setWidth("100%");
    }

    public AdTable(String name) {
        this();
        setName(name);
    }

    public void init() {
        super.init();
        setModel(new AdTableModel());
    }

    class AdTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)) {
                try {
                    AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        adModule.deleteAd(selectedKeys[i]);
                    }
                } catch (AdException e) {
                    Log log = Log.getLog(this.getClass());
                    log.error("Error deleting Ad", e);
                }

            } else if("new".equals(action)) {
                evt.setType("newAd");
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public AdTableModel() {
	        Application application = Application.getInstance();
            TableColumn tcName = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            tcName.setUrlParam("id");
            addColumn(tcName);

            addColumn(new TableColumn("url", application.getMessage("general.label.url", "Url")));

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn statusColumn = new TableColumn("active", application.getMessage("general.label.active", "Active"));
            statusColumn.setFormat(booleanFormat);
            addColumn(statusColumn);

            addAction(new TableAction("new", application.getMessage("ad.label.newAd", "New Ad")));
            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("ad.message.delete", "Delete selected items?")));

            TableFilter tf = new TableFilter("name", null);
            addFilter(tf);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                String search = (String) getFilterValue("name");
                search = search == null ? "" : search;
                AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
                return adModule.getAds(search, getSort(), isDesc(), getStart(), getRows());

            } catch (AdException e) {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error(e);
                return new HashSet();
            }
        }

        public int getTotalRowCount() {
            try {
                String search = (String) getFilterValue("name");
                search = search == null ? "" : search;
                AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
                return adModule.getAdCount(search);
            } catch (AdException e) {
                Log log = Log.getLog(this.getClass());
                log.error(e);
                return 0;
            }
        }
    }
}