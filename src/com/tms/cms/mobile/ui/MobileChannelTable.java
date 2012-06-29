package com.tms.cms.mobile.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;

import com.tms.cms.mobile.model.MobileModule;
import com.tms.cms.core.model.ContentException;

public class MobileChannelTable extends Table {

    public void init() {
        super.init();
        setModel(new MobileChannelTableModel());
    }

    public class MobileChannelTableModel extends TableModel {

        public MobileChannelTableModel() {


            // add columns
            Application application = Application.getInstance();
            TableColumn titleColumn = new TableColumn("title", application.getMessage("general.label.title", "Title"));
            titleColumn.setUrlParam("title");
            addColumn(titleColumn);
/*
            TableColumn dateColumn = new TableColumn("dateModified", application.getMessage("siteadmin.label.dateModified", "Date Modified"));
            dateColumn.setFormat(new TableDateFormat("dd MMM yyyy, hh:mmaa"));
            addColumn(dateColumn);
*/

            // add filters
            TableFilter titleFilter = new TableFilter("title");
            addFilter(titleFilter);

            // add actions
            addAction(new TableAction("add", application.getMessage("siteadmin.label.newChannel", "New Channel")));
            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("channel.confirm.delete", "Delete Selected Channels?")));
        }

        public String getTableRowKey() {
            return "title";
        }

        public Collection getTableRows() {
            // get filter values
            String titleFilterValue = (String)getFilterValue("title");

            // retrieve from module
            Collection channelList = null;
            try {
                MobileModule mod = (MobileModule)Application.getInstance().getModule(MobileModule.class);
                channelList = mod.getChannelList(titleFilterValue, getStart(), getRows());
            } catch (ContentException e) {
                Log.getLog(getClass()).error("Error retrieving table rows for channels " + e.toString());
            }
            return channelList;
        }

        public int getTotalRowCount() {
            // get filter values
            String titleFilterValue = (String)getFilterValue("title");

            // retrieve from module
            int channelCount = 0;
            try {
                MobileModule mod = (MobileModule)Application.getInstance().getModule(MobileModule.class);
                channelCount = mod.getChannelCount(titleFilterValue);
            } catch (ContentException e) {
                Log.getLog(getClass()).error("Error retrieving table rows for channels " + e.toString());
            }
            return channelCount;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                MobileModule mod = (MobileModule)Application.getInstance().getModule(MobileModule.class);
                for (int i=0; i<selectedKeys.length; i++) {
                    try {
                        mod.deleteChannel(selectedKeys[i]);
                    }
                    catch (Exception e) {
                        ;
                    }
                }
                return new Forward("deleted");
            }
            else if ("add".equals(action)) {
                return new Forward("add");
            }
            else {
                return null;
            }
        }

    }
}
