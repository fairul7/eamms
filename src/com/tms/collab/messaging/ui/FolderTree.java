package com.tms.collab.messaging.ui;

import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Folder;

public class FolderTree extends Tree {

    public FolderTree() {
    }

    public FolderTree(String name) {
        super(name);
    }

    public String getDisplayId() {
        return "id";
    }

    public String getDisplayProperty() {
        return "name";
    }
    public String getDisplayProperty1() {
        return "displayName";
    }

    public String getChildrenProperty() {
        return "subfolders";
    }

    public void onRequest(Event event) {
        try {
            User user = getWidgetManager().getUser();
            MessagingModule mm = (MessagingModule)Application.getInstance().getModule(MessagingModule.class);
            Folder root = mm.getFolderTree(user.getId());
            setModel(root);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to retrieve folder tree: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve folder tree: " + e.toString());
        }
    }

}
