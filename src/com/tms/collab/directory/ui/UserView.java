package com.tms.collab.directory.ui;

import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.stdui.CountrySelectBox;
import kacang.util.Log;

import java.util.Map;

import com.tms.mugshot.model.MugshotModule;
import com.tms.mugshot.model.Mugshot;

public class UserView extends Widget {

    private String id;
    private String mugshotpath;
    private User user;

    public String getDefaultTemplate() {
        return "addressbook/userView";
    }

    public void onRequest(Event event) {

        Application app = Application.getInstance();
        SecurityService sec = (SecurityService)app.getService(SecurityService.class);
        MugshotModule mm = (MugshotModule) app.getModule(MugshotModule.class);
        if (getId() != null) {
            try {
                user = sec.getUser(getId());
                //get mugshot
                Mugshot mug = null;
                mug = mm.get(getId());
                if(mug != null){
                    mugshotpath = "/storage"+mug.getFilePath();
                }else mugshotpath = "";

                // get country label
                String displayCountry = "";
                String country = (String)user.getProperty("country");
                if (country != null && !"-1".equals(country)) {
                    CountrySelectBox csb = new CountrySelectBox();
                    Map countryMap = csb.getOptionMap();
                    displayCountry = (String)countryMap.get(country);
                    if (displayCountry == null) {
                        displayCountry = country;
                    }
                }
                user.setProperty("displayCountry", displayCountry);

            }
            catch (SecurityException e) {
                Log.getLog(getClass()).error("Error retrieving user " + getId(), e);
            }
        }
    }

//-- Getters and Setters

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMugshotpath() {
        return mugshotpath;
    }

    public void setMugshotpath(String mugshotpath) {
        this.mugshotpath = mugshotpath;
    }

}
