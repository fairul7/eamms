package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;

import java.util.Collection;
import java.util.List;

public class ContactMessagingTable extends Table implements AddressBookWidget {

    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_TO = "to";
    public static final String FORWARD_CC = "cc";
    public static final String FORWARD_BCC = "bcc";

    protected ContactTableModel tableModel;

    public ContactMessagingTable() {
    }

    public ContactMessagingTable(String name) {
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
    }

    public void setSelectedFolder(String folderId) {
        SelectBox sb = (SelectBox) getModel().getFilter("folderSelectBox").getWidget();
        sb.setSelectedOptions(new String[]{folderId});
    }

    private String type;

    public String getType() {
        return AddressBookModule.MODULE_NAME;
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
            firstName.setFormat(new ContactTableNameFormatter(getWidgetManager()));
            addColumn(firstName);

            TableColumn lastName= new TableColumn("lastName", Application.getInstance().getMessage("addressbook.label.lastName","Last Name"));
            addColumn(lastName);

            TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
            addColumn(email);

            // add actions
            addAction(new TableAction("to", Application.getInstance().getMessage("addressbook.label.to","To")));
            addAction(new TableAction("cc", Application.getInstance().getMessage("addressbook.label.cC","CC")));
            addAction(new TableAction("bcc", Application.getInstance().getMessage("addressbook.label.bCC","BCC")));

            // add filters
            TableFilter query = new TableFilter("query");
            addFilter(query);

            TableFilter folderFilter = new TableFilter("folderSelectBox");
            FolderSelectBox folderSelectBox = new FolderSelectBox("folderSelectBox");
            folderSelectBox.setType(getType());
            folderFilter.setWidget(folderSelectBox);
            addFilter(folderFilter);

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
                String ownerId = getUserId();
                Boolean approved = null;

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
                String ownerId = getUserId();
                Boolean approved = null;

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

                if ("to".equals(action)) {
                    String addresses = Util.getEmailAddressByIds(dm, selectedKeys);
                    Util.setMessagingRecipient(event.getRequest(), addresses, Util.RECIPIENT_TO);
                    return new Forward(FORWARD_TO);
                }
                else if ("cc".equals(action)) {
                    String addresses = Util.getEmailAddressByIds(dm, selectedKeys);
                    Util.setMessagingRecipient(event.getRequest(), addresses, Util.RECIPIENT_CC);
                    return new Forward(FORWARD_CC);
                }
                else if ("bcc".equals(action)) {
                    String addresses = Util.getEmailAddressByIds(dm, selectedKeys);
                    Util.setMessagingRecipient(event.getRequest(), addresses, Util.RECIPIENT_BCC);
                    return new Forward(FORWARD_BCC);
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
