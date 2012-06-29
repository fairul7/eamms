package com.tms.collab.directory.ui;

import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Folder;
import com.tms.collab.directory.model.AddressBookException;

public class FolderTree extends Tree implements AddressBookWidget {

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

    public String getChildrenProperty() {
        return "subfolders";
    }

    public void onRequest(Event event) {
        try {
            AddressBookModule dm = getModule();
            String userId = getUserId();

            Folder root = dm.getFolderTree(userId);
            setModel(root);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to retrieve folder tree: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve folder tree: " + e.toString());
        }
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

}
