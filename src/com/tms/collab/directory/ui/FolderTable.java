package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;

import kacang.Application;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;

import java.util.Collection;
import java.util.List;


public class FolderTable extends Table implements AddressBookWidget {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_MOVED = "moved";
    public static final String FORWARD_DELETED = "deleted";
    public static final String FORWARD_SEND_MESSAGE = "sendMessage";
    public static final String FORWARD_NEW_FOLDER = "newFolder";
    private String type;

    public FolderTable() {
    }

    public FolderTable(String name) {
        super(name);
    }

    public void init() {
        setWidth("100%");
        setModel(new FolderTableModel());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected AddressBookModule getModule() {
        return Util.getModule(this);
    }

    protected String getUserId() {
        return Util.getUserId(this);
    }

    public class FolderTableModel extends TableModel {
        public FolderTableModel() {
            // add columns
            TableColumn name = new TableColumn("name",
                    Application.getInstance().getMessage("addressbook.label.name",
                        "Name"));
            name.setUrlParam("id");
            addColumn(name);

            TableColumn description = new TableColumn("description",
                    Application.getInstance().getMessage("addressbook.label.description",
                        "Description"));
            addColumn(description);

            // add actions
            addAction(new TableAction("newFolder",
                    Application.getInstance().getMessage("addressbook.label.newFolder",
                        "New Folder")));

            TableAction move = new TableAction("move",
                    Application.getInstance().getMessage("addressbook.label.move",
                        "Move"));
            move.setMessage(Application.getInstance().getMessage("addressbook.label.movefolderprompt",
                    "Are you sure you want to move this folder(s) ?"));
            addAction(move);
            addAction(new TableAction("delete",
                    Application.getInstance().getMessage("addressbook.label.delete",
                        "Delete"),
                    Application.getInstance().getMessage("addressbook.label.deleteFolderWarning",
                        "Delete selected folder(s)? (WARNING: All subfolders and contacts will be deleted)")));
            addAction(new TableAction("sendMessage",
                    Application.getInstance().getMessage("addressbook.label.sendMessage",
                        "Send Message")));

            // add filters
            TableFilter query = new TableFilter("query");
            addFilter(query);

            TableFilter folderFilter = new TableFilter("folderSelectBox");
            FolderSelectBox folderSelectBox = new FolderSelectBox(
                    "folderSelectBox");
            folderSelectBox.setType(getType());
            folderFilter.setWidget(folderSelectBox);
            addFilter(folderFilter);
        }

        public Collection getTableRows() {
            try {
                // get filter values
                String query = (String) getFilterValue("query");
                String folderId = null;
                List selected = (List) getFilterValue("folderSelectBox");

                if ((selected != null) && (selected.size() > 0)) {
                    folderId = (String) selected.iterator().next();

                    if (folderId.trim().length() == 0) {
                        folderId = null;
                    }
                }

                String ownerId = getUserId();

                // invoke module
                AddressBookModule dm = getModule();
                Collection rows = dm.getFolderList(query, folderId, ownerId,
                        getSort(), isDesc(), getStart(), getRows());

                return rows;
            } catch (AddressBookException e) {
                Log.getLog(getClass()).error("Unable to retrieve folders: " +
                    e.toString(), e);
                throw new RuntimeException("Unable to retrieve folders: " +
                    e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // get filter values
                String query = (String) getFilterValue("query");
                String folderId = null;
                List selected = (List) getFilterValue("folderSelectBox");

                if ((selected != null) && (selected.size() > 0)) {
                    folderId = (String) selected.iterator().next();

                    if (folderId.trim().length() == 0) {
                        folderId = null;
                    }
                }

                String ownerId = getUserId();

                // invoke module
                AddressBookModule dm = getModule();
                int count = dm.getFolderCount(query, folderId, ownerId);

                return count;
            } catch (AddressBookException e) {
                Log.getLog(getClass()).error("Unable to retrieve folder count: " +
                    e.toString(), e);
                throw new RuntimeException("Unable to retrieve folder count: " +
                    e.toString());
            }
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event event, String action,
            String[] selectedKeys) {
            try {
                AddressBookModule dm = getModule();
                String userId = getWidgetManager().getUser().getId();

                if ("newFolder".equals(action)) {
                    return new Forward(FORWARD_NEW_FOLDER);
                } else if ("move".equals(action)) {
                    // get parent ID
                    String parentId = null;
                    List selected = (List) getFilterValue("folderSelectBox");

                    if ((selected != null) && (selected.size() > 0)) {
                        parentId = (String) selected.iterator().next();

                        if (parentId.trim().length() == 0) {
                            parentId = AddressBookModule.ROOT_FOLDER_ID;
                        }
                    }

                    // update parent IDs
                    for (int i = 0; i < selectedKeys.length; i++) {
                        try {
                            dm.moveFolder(selectedKeys[i], parentId, userId);
                        } catch (AddressBookException e) {
                            Log.getLog(getClass()).error("Error updating folder parent for " +
                                selectedKeys[i] + ": " + e.toString());
                        }
                    }

                    return new Forward(FORWARD_MOVED);
                } else if ("sendMessage".equals(action)) {
                    // send message
                    String addresses = Util.getEmailAddressByFolders(dm,
                            selectedKeys);
                    Util.setMessagingRecipient(event.getRequest(), addresses,
                        Util.RECIPIENT_TO);

                    return new Forward(FORWARD_SEND_MESSAGE);
                } else if ("delete".equals(action)) {
                    // delete
                    for (int i = 0; i < selectedKeys.length; i++) {
                        try {
                            /*
                                                        // move contacts
                                                        Collection folderList = dm.getFolderList(null, selectedKeys[i], userId, null, false, 0, -1);
                                                        for (Iterator iter=folderList.iterator(); iter.hasNext();) {
                                                            Folder folder = (Folder)iter.next();
                                                            folder.setParentId(null);
                                                            try {
                                                                dm.updateFolder(folder, userId);
                                                            }
                                                            catch (AddressBookException e) {
                                                                Log.getLog(getClass()).error("Error updating folder parent for " + selectedKeys[i] + ": " + e.toString());
                                                            }
                                                        }
                            */

                            // delete folder
                            dm.deleteFolder(selectedKeys[i], userId);
                        } catch (AddressBookException e) {
                            Log.getLog(getClass()).error("Error deleting folder " +
                                selectedKeys[i] + ": " + e.toString());
                        }
                    }

                    return new Forward(FORWARD_DELETED);
                } else {
                    return null;
                }
            } catch (Exception e) {
                Log.getLog(getClass()).error("Error processing action " +
                    action + ": " + e.toString(), e);

                return new Forward(FORWARD_ERROR);
            }
        }
    }
}
