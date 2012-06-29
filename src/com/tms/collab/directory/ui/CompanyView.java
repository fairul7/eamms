package com.tms.collab.directory.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.directory.model.*;

public class CompanyView extends Widget {

    private String id;
    private Contact company;

    public CompanyView() {
    }

    public CompanyView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "addressbook/companyView";
    }

    public void onRequest(Event event) {

        if (getId() != null) {
            try {
                // get company
                Application app = Application.getInstance();
                CompanyModule cm = (CompanyModule)app.getModule(CompanyModule.class);
                company = cm.getContact(getId());
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Error retrieving contact " + getId(), e);
            }
            catch (DataObjectNotFoundException e) {
                ;
            }
        }

    }

//-- Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contact getCompany() {
        return company;
    }



}
