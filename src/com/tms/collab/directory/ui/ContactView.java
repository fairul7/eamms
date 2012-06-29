package com.tms.collab.directory.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.directory.model.*;
import com.tms.collab.messaging.model.MessagingModule;

import java.util.Collection;
import java.util.ArrayList;

public class ContactView extends Widget implements AddressBookWidget {

    public static final String EVENT_VIEW = "view";
    public static final String EVENT_VIEW_INTRANET_USER = "viewIntranet";
    public static final String EVENT_SEND_MESSAGE = "sendMessage";

    private String id;
    private Contact contact;
    private Contact company;

    public ContactView() {
    }

    public ContactView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        if (contact != null && !contact.isContactGroup()) {
            return "addressbook/contactView";
        }
        else {
            return "addressbook/contactGroupView";
        }
    }

    public void onRequest(Event event) {

        loadContact();
    }

    public Forward actionPerformed(Event evt) {
        if (EVENT_VIEW.equals(evt.getType())) {
            String id = evt.getRequest().getParameter("id");
            setId(id);
        }
        else if (EVENT_VIEW_INTRANET_USER.equals(evt.getType())) {
            return new Forward(EVENT_VIEW_INTRANET_USER);
        }
        else if (EVENT_SEND_MESSAGE.equals(evt.getType())) {
            try {
                // send message
                String id = evt.getRequest().getParameter("id");
                String addresses = Util.getEmailAddressByIds(getModule(), new String[] {id});
                evt.getRequest().getSession().setAttribute(MessagingModule.TO_ATTRIBUTE, addresses);
                return new Forward(EVENT_SEND_MESSAGE);
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Error retrieving email for contact " + getId(), e);
            }
        }
        loadContact();
        return super.actionPerformed(evt);
    }

    protected void loadContact() {

        Application app = Application.getInstance();
        AddressBookModule dm = getModule();

        if (getId() != null) {
            try {
                // get contact
                company = null;
                contact = dm.getContact(getId());

                // get company
                if (contact.getCompanyId() != null) {
                    CompanyModule cm = (CompanyModule)app.getModule(CompanyModule.class);
                    company = cm.getContact(contact.getCompanyId());
                }
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Error retrieving contact " + getId(), e);
            }
            catch (DataObjectNotFoundException e) {
                ;
            }
        }

    }

//-- Implementation for AddressBookWidget

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


//-- Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public Contact getCompany() {
        return company;
    }

//-- Methods for contact group

    /**
     *
     * @return Collection of Contact objects
     */
    public Collection getContactGroupList() {
        Collection contactList = new ArrayList();
        try {
            Contact c = getContact();
            if (c != null) {
                String[] ids = c.getContactIdArray();
                AddressBookModule mod = getModule();
                contactList = mod.getContactListById(ids, "firstName", false);
            }
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving contact list", e);
        }
        return contactList;
    }

    public Collection getIntranetUsersList() {
        Collection contactList = new ArrayList();
        Contact c = getContact();
        if (c != null) {
            String[] ids = c.getIntranetIdArray();
            if (ids != null && ids.length > 0) {
                contactList = Util.getIntranetUsersList(ids);
            }
        }
        return contactList;
    }


}
