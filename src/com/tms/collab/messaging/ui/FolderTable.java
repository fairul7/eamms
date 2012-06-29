package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.Application;

import java.util.ArrayList;
import java.util.Collection;

public class FolderTable extends Table {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_NEW_FOLDER = "newFolder";

    private boolean specialFolders;

    public boolean isSpecialFolders() {
        return specialFolders;
    }

    /**
     * Set to true to display only special folders, otherwise only custom folders are displayed.
     * @param specialFolders
     */
    public void setSpecialFolders(boolean specialFolders) {
        this.specialFolders = specialFolders;
    }

    public void init() {
        super.init();

        setWidth("100%");
        setModel(new FolderTableModel());
    }

    class FolderTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if (isSpecialFolders()) {
                return null; // do nothing
            }

            Log log = Log.getLog(getClass());
            log.debug("~~~ action = " + action);

            try {
                if (action.equalsIgnoreCase("delete")) {
                    Folder folder;
                    MessagingModule mm = Util.getMessagingModule();

                    for (int i = 0; i < selectedKeys.length; i++) {
                        folder = mm.getFolder(selectedKeys[i]);

                        if(!folder.isSpecialFolder()) {
                            mm.deleteFolder(selectedKeys[i]);
                        } else {
                            log.debug("Special folder " + selectedKeys[i] + " will not be deleted");
                        }
                    }

                } else if (action.equalsIgnoreCase("newFolder")) {
                    return new Forward(FORWARD_NEW_FOLDER);
                }

            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
                evt.getRequest().getSession().setAttribute("error", e);
                return new Forward(FORWARD_ERROR);
            }

            return new Forward(FORWARD_SUCCESS);
        }

        public FolderTableModel() {
            try {
                if (!isSpecialFolders()) {
                    addAction(new TableAction("newFolder", Application.getInstance().getMessage("messaging.label.newFolder","New Folder")));
                    addAction(new TableAction("delete", Application.getInstance().getMessage("messaging.label.delete","Delete"), Application.getInstance().getMessage("messaging.label.deleteselectedfolders","Delete selected folder(s)?")));
                }

                TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("messaging.label.folderName","Folder Name"));
                if (!isSpecialFolders()) {
                    tcName.setUrlParam("folderId");
                }
                addColumn(tcName);

                TableColumn tcDiskUsage = new TableColumn("diskUsage", Application.getInstance().getMessage("messaging.label.diskUsage","Disk Usage"));
                tcDiskUsage.setFormat(new UsageFormat());
                addColumn(tcDiskUsage);

/*
                TableColumn tcIsMandatory = new TableColumn("specialFolder", "Mandatory");
                String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
                tcIsMandatory.setFormat(booleanFormat);
                addColumn(tcIsMandatory);
*/

            } catch (Exception e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }

        public String getTableRowKey() {
            return "folderId";
        }

        public Collection getTableRows() {
            try {
                String special = (isSpecialFolders()) ? "1" : "0";
                MessagingModule m = Util.getMessagingModule();
                return m.getFolders(getWidgetManager().getUser().getId(), getStart(), getRows(), getSort(), isDesc(), "specialFolder", special);

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            try {
                String special = (isSpecialFolders()) ? "1" : "0";
                MessagingModule mm = Util.getMessagingModule();
                return mm.getFoldersCount(getWidgetManager().getUser().getId(), "specialFolder", special);

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
                return 0;
            }
        }
    }

    class UsageFormat implements TableFormat {
        public String format(Object o) {
            return format((Long)o);
        }

        public String format(Long o) {
            long l = o.longValue()/1024;
            return Long.toString(l) + "kb";
        }
    }
}
