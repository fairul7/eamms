package com.tms.collab.directory.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.directory.model.DirectoryModule;
import com.tms.collab.directory.model.CompanyModule;
import com.tms.collab.directory.model.AddressBookException;

public class DirectoryPendingCount extends LightWeightWidget {

    private int pendingContactCount;
    private int pendingCompanyCount;

    public int getPendingContactCount() {
        return pendingContactCount;
    }

    public int getPendingCompanyCount() {
        return pendingCompanyCount;
    }

    public String getDefaultTemplate() {
        return "";
    }

    public void onRequest(Event event) {

        Application app = Application.getInstance();
        DirectoryModule dm = (DirectoryModule)app.getModule(DirectoryModule.class);
        CompanyModule cm = (CompanyModule)app.getModule(CompanyModule.class);

        try {
            pendingContactCount = dm.getContactCount(null, null, null, null, Boolean.FALSE);
            pendingCompanyCount = cm.getContactCount(null, null, null, null, Boolean.FALSE);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error retrieving count for pending contacts", e);
        }

    }

}
