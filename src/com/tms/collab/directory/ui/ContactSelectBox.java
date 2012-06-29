package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import kacang.Application;
import kacang.stdui.*;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContactSelectBox extends PopupSelectBox implements AddressBookWidget {

    public ContactSelectBox() {
        super();
    }

    public ContactSelectBox(String name) {
        super(name);
    }

    protected Table initPopupTable() {
        ContactTable ct = new ContactTable();
        ct.setWidgetManager(getWidgetManager());
        return ct;
    }

    protected Map generateOptionMap(String[] ids) {
        Map optionMap = new SequencedHashMap();
        try {
            AddressBookModule mod = getModule();
            Collection contactList = mod.getContactListById(ids, "firstName", false);
            for (Iterator i=contactList.iterator(); i.hasNext();) {
                Contact ct = (Contact)i.next();
                optionMap.put(ct.getId(), ct.getDisplayName());
            }
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving contacts", e);
        }
        return optionMap;
    }

    private String type;

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

    public class ContactTable extends PopupSelectBoxTable {
        public ContactTable() {
            super();
        }

        public ContactTable(String name) {
            super(name);
        }

        public void init() {
            setWidth("100%");
            setModel(new ContactTableModel(this));
        }

        public class ContactTableModel extends PopupSelectBoxTableModel {
            public ContactTableModel(Table table) {

                // add columns
                TableColumn firstName = new TableColumn("firstName", Application.getInstance().getMessage("addressbook.label.firstName","First Name"));
                firstName.setFormat(new ContactTableNameFormatter());
                addColumn(firstName);

                TableColumn lastName= new TableColumn("lastName", Application.getInstance().getMessage("addressbook.label.lastName","Last Name"));
                addColumn(lastName);

                TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
                addColumn(email);

                // add actions
                addAction(new TableAction(FORWARD_SELECT,  Application.getInstance().getMessage("general.label.select", "Select")));


                // add filters
                TableFilter query = new TableFilter("query");
                addFilter(query);

                TableFilter folderFilter = new TableFilter("folderSelectBox");
                FolderSelectBox folderSelectBox = new FolderSelectBox("folderSelectBox");
                folderSelectBox.setType(getType());
                folderFilter.setWidget(folderSelectBox);
                addFilter(folderFilter);
                folderSelectBox.setWidgetManager(table.getWidgetManager());;
                folderSelectBox.init();
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
                    String ownerId = getWidgetManager().getUser().getId();
                    Boolean approved = (getType() == null || AddressBookModule.MODULE_NAME.equals(getType())) ? null : Boolean.TRUE;
                    Boolean contactGroup = Boolean.FALSE;

                    // invoke module
                    AddressBookModule dm = getModule();
                    String sort = getSort();
                    if (sort == null) {
                        sort = "firstName";
                    }
                    Collection rows = dm.getContactList(query, folderId, companyId, ownerId, approved, contactGroup, sort, isDesc(), getStart(), getRows());
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
                    String ownerId = getWidgetManager().getUser().getId();
                    Boolean approved = (getType() == null || AddressBookModule.MODULE_NAME.equals(getType())) ? null : Boolean.TRUE;
                    Boolean contactGroup = Boolean.FALSE;

                    // invoke module
                    AddressBookModule dm = getModule();
                    int count = dm.getContactCount(query, folderId, companyId, ownerId, approved, contactGroup);
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

        }

    }
}
