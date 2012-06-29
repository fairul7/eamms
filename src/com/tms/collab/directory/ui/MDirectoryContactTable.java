package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.Application;

import java.util.Collection;
import java.util.List;

import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.DirectoryModule;

public class MDirectoryContactTable extends Table implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_MOVED = "moved";
    public static final String FORWARD_DELETED = "deleted";
    public static final String FORWARD_SEND_MESSAGE = "sendMessage";
    public static final String FORWARD_NEW_CONTACT = "newContact";

    protected ContactTableModel tableModel;

    public MDirectoryContactTable() {
    }

    public MDirectoryContactTable(String name) {
        super(name);
    }

    public void init() {
        setWidth("100%");
        tableModel = new ContactTableModel();
        setModel(tableModel);
    }

    public void setQuery(String query) {
        TextField q = (TextField)getModel().getFilter("query").getWidget();
        q.setValue(query);
        if (query != null) {
            // reset paging
            setCurrentPage(1);
            TableModel mod = getModel();
            if (mod != null) {
                mod.setStart(0);
            }
        }
    }

    public void setSelectedFolder(String folderId) {
        SelectBox sb = (SelectBox) getModel().getFilter("folderSelectBox").getWidget();
        sb.setSelectedOptions(new String[]{folderId});
    }

    public void setSelectedCompany(String companyId) {
        SelectBox sb = (SelectBox) getModel().getFilter("companySelectBox").getWidget();
        sb.setSelectedOptions(new String[]{companyId});
    }

    private String type;

    public String getType() {
        return DirectoryModule.MODULE_NAME;
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

    public class ContactTableModel extends TableModel {

        public ContactTableModel() {

            // add columns
            TableColumn firstName = new TableColumn("displayFirstName", Application.getInstance().getMessage("addressbook.label.firstName","First Name"));
            firstName.setUrlParam("id");
            firstName.setFormat(new ContactTableNameFormatter(getWidgetManager()));
            addColumn(firstName);

            TableColumn lastName= new TableColumn("lastName", Application.getInstance().getMessage("addressbook.label.lastName","Last Name"));
            addColumn(lastName);

/*
            TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
            addColumn(email);
*/

            TableColumn phone= new TableColumn("phone", Application.getInstance().getMessage("addressbook.label.phone","Phone"));
            addColumn(phone);

/*
			TableColumn extension = new TableColumn("extension", Application.getInstance().getMessage("addressbook.label.extension","Extension"));
            addColumn(extension);
*/

            TableColumn mobile = new TableColumn("mobile", Application.getInstance().getMessage("addressbook.label.mobile","Mobile"));
            addColumn(mobile);

            // add actions
            try {
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                String userId = getWidgetManager().getUser().getId();
/*
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_CONTACTS, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("newContact", Application.getInstance().getMessage("addressbook.label.newContact","New Contact")));
                }
*/
/*
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("move", Application.getInstance().getMessage("addressbook.label.move","Move")));
                }
*/
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_CONTACTS, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("delete", Application.getInstance().getMessage("addressbook.label.delete","Delete"), Application.getInstance().getMessage("addressbook.label.deletecontactsprompt","Delete selected contact(s)?")));
                }
                addAction(new TableAction("sendMessage", Application.getInstance().getMessage("addressbook.label.sendMessage","Send Message")));
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error adding table actions: " + e.toString(), e);
            }

            // add filters

            TableFilter br = new TableFilter("space");
            br.setWidget(new Label("spacelb","<br>"));

            TableFilter folderFilter = new TableFilter("folderSelectBox");
            FolderSelectBox folderSelectBox = new FolderSelectBox("folderSelectBox");
            folderSelectBox.setType(getType());
            folderFilter.setWidget(folderSelectBox);
            addFilter(folderFilter);

            addFilter(br);

            TableFilter companyFilter = new TableFilter("companySelectBox");
            CompanySelectBox companySelectBox = new CompanySelectBox("companySelectBox");
            companySelectBox.setType(getType());
            companyFilter.setWidget(companySelectBox);
            addFilter(companyFilter);
            addFilter(br);

            TableFilter query = new TableFilter("query");
            addFilter(query);

        }

        public Collection getTableRows() {
            try {
                // get filter values
                String query = (String)getFilterValue("query");
                String folderId = null;
                List selected = (List)getFilterValue("folderSelectBox");
                if (selected != null && selected.size() > 0) {
                    folderId = (String)selected.iterator().next();
                    if (folderId.trim().length() == 0) {
                        folderId = null;
                    }
                }
                String companyId = null;
                selected = (List)getFilterValue("companySelectBox");
                if (selected != null && selected.size() > 0) {
                    companyId = (String)selected.iterator().next();
                    if (companyId.trim().length() == 0) {
                        companyId = null;
                    }
                }
                String ownerId = null;
                Boolean approved = Boolean.TRUE;

                // invoke module
                AddressBookModule dm = getModule();
                Collection rows = dm.getContactList(query, folderId, companyId, ownerId, approved, getSort(), isDesc(), getStart(), getRows());
                return rows;
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Unable to retrieve contacts: " + e.toString(), e);
                throw new RuntimeException("Unable to retrieve contacts: " + e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // get filter values
                String query = (String)getFilterValue("query");
                String folderId = null;
                List selected = (List)getFilterValue("folderSelectBox");
                if (selected != null && selected.size() > 0) {
                    folderId = (String)selected.iterator().next();
                    if (folderId.trim().length() == 0) {
                        folderId = null;
                    }
                }
                String companyId = null;
                selected = (List)getFilterValue("companySelectBox");
                if (selected != null && selected.size() > 0) {
                    companyId = (String)selected.iterator().next();
                    if (companyId.trim().length() == 0) {
                        companyId = null;
                    }
                }
                String ownerId = null;
                Boolean approved = Boolean.TRUE;

                // invoke module
                AddressBookModule dm = getModule();
                int count = dm.getContactCount(query, folderId, companyId, ownerId, approved);
                return count;
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Unable to retrieve contact count: " + e.toString(), e);
                throw new RuntimeException("Unable to retrieve contact count: " + e.toString());
            }
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event event, String action, String[] selectedKeys) {
            try {
                AddressBookModule dm = getModule();
                String userId = getWidgetManager().getUser().getId();

                if ("newContact".equals(action)) {
                    return new Forward(FORWARD_NEW_CONTACT);
                }
                else if ("move".equals(action)) {
                    // get folder ID
                    String folderId = null;
                    List selected = (List)getFilterValue("folderSelectBox");
                    if (selected != null && selected.size() > 0) {
                        folderId = (String)selected.iterator().next();
                        if (folderId.trim().length() == 0) {
                            folderId = null;
                        }
                    }

                    // update folder IDs
                    for (int i=0; i<selectedKeys.length; i++) {
                        try {
                            dm.moveContact(selectedKeys[i], folderId, userId);
                        }
                        catch (AddressBookException e) {
                            Log.getLog(getClass()).error("Error updating folder for contact " + selectedKeys[i] + ": " + e.toString());
                        }
                    }
                    return new Forward(FORWARD_MOVED);
                }
                else if ("sendMessage".equals(action)) {
                    // send message
                    String addresses = Util.getEmailAddressByIds(dm, selectedKeys);
                    Util.setMessagingRecipient(event.getRequest(), addresses, Util.RECIPIENT_TO);
                    return new Forward(FORWARD_SEND_MESSAGE);
                }
                else if ("delete".equals(action)) {
                    // delete
                    for (int i=0; i<selectedKeys.length; i++) {
                        try {
                            // delete contact
                            dm.deleteContact(selectedKeys[i], userId);
                        }
                        catch (AddressBookException e) {
                            Log.getLog(getClass()).error("Error deleting contact " + selectedKeys[i] + ": " + e.toString());
                        }
                    }
                    return new Forward(FORWARD_DELETED);
                }
                else {
                    return null;
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                return new Forward(FORWARD_ERROR);
            }
        }

    }

}
