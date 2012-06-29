package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.CompanyModule;
import com.tms.collab.directory.model.DirectoryModule;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;

public class ContactSearchPortlet extends LightWeightWidget {

    private User user;
    private String personal = "true";
    private String intranet = "true";
    private String business = "true";

    public String getDefaultTemplate() {
        return "addressbook/contactSearchPortlet";
    }

    public void onRequest(Event event) {
        user = event.getWidgetManager().getUser();
    }

//-- Methods to retrieve required lists

    public Collection getPersonalFolderList() {
        // get personal folder list
        Application app = Application.getInstance();
        AddressBookModule am = (AddressBookModule)app.getModule(AddressBookModule.class);
        try {
            return am.getFolderList(null, null, user.getId(), "name", false, 0, -1);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving personal folder list: " + e.toString());
            return new ArrayList();
        }
    }

    public Collection getDirectoryFolderList() {
        // get biz directory folder list
        Application app = Application.getInstance();
        DirectoryModule dm = (DirectoryModule)app.getModule(DirectoryModule.class);
        try {
            return dm.getFolderList(null, null, null, "name", false, 0, -1);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving directory folder list: " + e.toString());
            return new ArrayList();
        }

    }

    public Collection getCompanyList() {
        // get company list
        Application app = Application.getInstance();
        CompanyModule cm = (CompanyModule)app.getModule(CompanyModule.class);
        try {
            return cm.getContactList(null, null, null, null, Boolean.TRUE, "company", false, 0, -1);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving company list: " + e.toString());
            return new ArrayList();
        }

    }

    public Collection getGroupList() {
        // get group list
        Application app = Application.getInstance();
        SecurityService service = (SecurityService) app.getService(SecurityService.class);
        try {
            DaoQuery q = new DaoQuery();
            q.addProperty(new OperatorEquals("active", "1", DaoOperator.OPERATOR_AND));
            return service.getGroups(q, 0, -1, "groupName", false);
        }
        catch (Exception e) {
            Log.getLog(UserTable.class).error("Error loading groups: " + e.toString(), e);
            return new ArrayList();
        }

    }

    public int getPendingContactCount() {
        // get biz directory pending count
        Application app = Application.getInstance();
        DirectoryModule dm = (DirectoryModule)app.getModule(DirectoryModule.class);
        try {
            return dm.getContactCount(null, null, null, null, Boolean.FALSE);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving directory pending contact count: " + e.toString());
            return 0;
        }

    }

    public int getPendingCompanyCount() {
        // get pending company count
        Application app = Application.getInstance();
        CompanyModule cm = (CompanyModule)app.getModule(CompanyModule.class);
        try {
            return cm.getContactCount(null, null, null, null, Boolean.FALSE);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving pending company count: " + e.toString());
            return 0;
        }
    }

//-- Getters and Setters

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getIntranet() {
        return intranet;
    }

    public void setIntranet(String intranet) {
        this.intranet = intranet;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

}
