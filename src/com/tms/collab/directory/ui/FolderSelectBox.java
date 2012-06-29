package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Folder;
import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class FolderSelectBox extends SelectBox implements AddressBookWidget {

    public FolderSelectBox() {
    }

    public FolderSelectBox(String name) {
        super(name);
    }

    public void init() {
        initField();
    }

    public void onRequest(Event evt) {
        initField();
    }

    public void initField() {
        try {
            AddressBookModule dm = getModule();
            String userId = getUserId();

            // generate folder hierarchy
            Map folderMap = new SequencedHashMap();
            folderMap.put("", Application.getInstance().getMessage("addressbook.label.folder","--- Folder ---"));
            Folder root = dm.getFolderTree(userId);
            populateFolderMap(folderMap, root, 0);
            this.setOptionMap(folderMap);

        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to retrieve folder list: " + e.toString(), e);
            throw new RuntimeException("Unable to retrieve folder list: " + e.toString());
        }
    }

    private void populateFolderMap(Map folderMap, Folder folder, int level) {
        Collection children = folder.getSubfolders();
        if (children != null) {
            for (Iterator i = children.iterator(); i.hasNext();) {
                Folder f = (Folder)i.next();
                StringBuffer buffer = new StringBuffer();
                for (int j=0; j<level; j++) {
                    buffer.append("...");
                }
                if (level > 0) {
                    buffer.append("| ");
                }
                buffer.append(f.getName());
                String title = buffer.toString();
                if (title.length() > 30) {
                    title = title.substring(0, 30) + "..";
                }

                folderMap.put(f.getId(), title);
                populateFolderMap(folderMap, f, level+1);
            }
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
