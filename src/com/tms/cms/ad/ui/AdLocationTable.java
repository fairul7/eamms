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

public class AdLocationTable extends Table {

    public AdLocationTable() {
        super();
        setWidth("100%");
    }

    public AdLocationTable(String name) {
        this();
        setName(name);
    }

    public void init() {
        super.init();

        setModel(new AdLocationTableModel());
    }

    class AdLocationTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {

            if("delete".equals(action)) {
                try {
                    AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        adModule.deleteAdLocation(selectedKeys[i]);
                    }
                } catch (AdException e) {
                    Log log = Log.getLog(this.getClass());
                    log.error("Error deleting AdLocation", e);
                }

            } else if("new".equals(action)) {
                evt.setType("newAdLocation");
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public AdLocationTableModel() {
	        Application application = Application.getInstance();
            TableColumn tcName = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            tcName.setUrlParam("id");
            addColumn(tcName);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn statusColumn = new TableColumn("active", application.getMessage("general.label.active", "Active"));
            statusColumn.setFormat(booleanFormat);
            addColumn(statusColumn);

            TableColumn tcType = new TableColumn("adType", application.getMessage("general.label.type", "Type"));
            tcType.setFormat(new TypeTableFormat());
            addColumn(tcType);

            addAction(new TableAction("new", application.getMessage("ad.label.newAdLocation", "New Ad Location")));
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
                return adModule.getAdLocations(search, getSort(), isDesc(), getStart(), getRows());

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
                return adModule.getAdLocationCount(search);
            } catch (AdException e) {
                Log log = Log.getLog(this.getClass());
                log.error(e);
                return 0;
            }
        }
    }
}

class TypeTableFormat implements TableFormat {
    public String format(Object value) {
        int i;

        i = ((Integer)value).intValue();
        switch(i) {
            case 1:
                return "FULL_BANNER_468x60";

            case 2:
                return "HALF_BANNER_234x60";

            case 3:
                return "MICRO_BAR_88x31";

            case 4:
                return "BUTTON_1_120x90";

            case 5:
                return "BUTTON_2_120x60";

            case 6:
                return "WIDE_SKYSCRAPER_160x600";

            case 7:
                return "SKYSCRAPER_120x600";

            case 8:
                return "SQUARE_BUTTON_125x125";

            case 9:
                return "RECTANGLE_180x150";

            case 10:
                return "VERTICAL_BANNER_120x240";

            case 11:
                return "MEDIUM_RECTANGLE_300x250";

            case 12:
                return "SQUARE_POP_UP_250x250";

            case 13:
                return "VERTICAL_RECTANGLE_240x400";

            case 14:
                return "LARGE_RECTANGLE_336x280";

            case 15:
                return "CUSTOM_FREE_SIZE";

            default:
                return "UNKNOWN TYPE!";
        }
    }
}