package com.tms.collab.directory.ui;

import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.directory.model.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class CompanySelectBox extends SelectBox implements AddressBookWidget {

    public CompanySelectBox() {
    }

    public CompanySelectBox(String name) {
        super(name);
    }

    public void onRequest(Event event) {
        try {
            AddressBookModule dm = getModule();
            String userId = null;

            addOption("", Application.getInstance().getMessage("addressbook.label.registeredCompany","--- Registered Company ---"));
            Collection contactList = dm.getContactList(null, null, null, userId, Boolean.TRUE, "company", false, 0, -1);
            for (Iterator i=contactList.iterator(); i.hasNext();) {
                Contact c = (Contact)i.next();
                String name = c.getCompany();
                if (name != null && name.trim().length() > 0) {
                    if (name.trim().length() > 50) {
                        name = name.substring(0, 50) + "..";
                    }
                    addOption(c.getId(), name);
                }
            }
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to retrieve company list: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve company list: " + e.toString());
        }
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

}
