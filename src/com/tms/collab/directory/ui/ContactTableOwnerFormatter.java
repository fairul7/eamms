package com.tms.collab.directory.ui;

import kacang.stdui.TableFormat;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;

import java.util.Map;
import java.util.HashMap;

public class ContactTableOwnerFormatter implements TableFormat {

    private Map ownerMap;

    public ContactTableOwnerFormatter() {
        ownerMap = new HashMap();
    }

    public String format(Object o) {
        if (o == null || o.toString().trim().length() == 0) {
            o = Application.getInstance().getMessage("addressbook.label.None","- None -");
        }
        else {
            String username = (String)ownerMap.get(o.toString());
            if (username == null) {
                try {
                    Application app = Application.getInstance();
                    SecurityService sec = (SecurityService)app.getService(SecurityService.class);
                    User user = sec.getUser(o.toString());
                    username = user.getUsername();
                }
                catch (SecurityException e) {
                    username = Application.getInstance().getMessage("addressbook.label.Invalid","- Invalid -");
                }
                ownerMap.put(o.toString(), username);
            }
            o = username;
        }
        return o.toString();
    }

}

