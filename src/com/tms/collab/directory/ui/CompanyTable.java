package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import kacang.services.security.SecurityService;

import java.util.Collection;
import java.util.List;

import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.CompanyModule;
import com.tms.collab.directory.model.DirectoryModule;

public class CompanyTable extends Table implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_DELETED = "deleted";
    public static final String FORWARD_NEW_CONTACT = "newContact";

    protected ContactTableModel tableModel;

    public CompanyTable() {
    }

    public CompanyTable(String name) {
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

    private String type;

    public String getType() {
        return CompanyModule.MODULE_NAME;
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
            TableColumn companyName = new TableColumn("company", Application.getInstance().getMessage("addressbook.label.company","Company"));
            companyName.setUrlParam("id");
            companyName.setFormat(new ContactTableNameFormatter());
            addColumn(companyName);

            TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
            addColumn(email);

            TableColumn phone= new TableColumn("phone", Application.getInstance().getMessage("addressbook.label.phone","Phone"));
            addColumn(phone);

            // add actions
            try {
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                String userId = getWidgetManager().getUser().getId();
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_EDIT_COMPANIES, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("newContact", Application.getInstance().getMessage("addressbook.label.newCompany","New Company")));
                }
                if (security.hasPermission(userId, DirectoryModule.PERMISSION_MANAGE_COMPANIES, DirectoryModule.class.getName(), null)) {
                    addAction(new TableAction("delete", Application.getInstance().getMessage("addressbook.label.delete","Delete"), Application.getInstance().getMessage("addressbook.label.deletecompaniesprompt","Delete selected companies?")));
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error adding table actions: " + e.toString(), e);
            }

            // add filters
            TableFilter query = new TableFilter("query");
            addFilter(query);

        }

        public Collection getTableRows() {
            try {
                // get filter values
                String query = (String)getFilterValue("query");
                String ownerId = null;
                Boolean approved = Boolean.TRUE;

                // invoke module
                AddressBookModule dm = getModule();
                Collection rows = dm.getContactList(query, null, null, ownerId, approved, getSort(), isDesc(), getStart(), getRows());
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
                String ownerId = null;
                Boolean approved = Boolean.TRUE;

                // invoke module
                AddressBookModule dm = getModule();
                int count = dm.getContactCount(query, null, null, ownerId, approved);
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
