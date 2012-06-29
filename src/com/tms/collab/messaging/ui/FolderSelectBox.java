package com.tms.collab.messaging.ui;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.SelectBox;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;

public class FolderSelectBox extends SelectBox {

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
            Application app = Application.getInstance();
            String userId = getWidgetManager().getUser().getId();
            Map folderMap = new SequencedHashMap();

            if (isParentSelection()) {
                folderMap.put("", Application.getInstance().getMessage("messaging.label.folder","--- Folder ---"));
            }

            MessagingModule mm = (MessagingModule)app.getModule(MessagingModule.class);
            Folder root = mm.getFolderTree(userId);
            String folderIdForParentSel = getFolderIdForParentSelection();
            populateFolderMap(folderMap, root, 0, folderIdForParentSel);
            this.setOptionMap(folderMap);
//            String folderName = evt.getRequest().getParameter("folder");
//            if (folderName != null) {
//                    Folder folder = mm.getSpecialFolder(userId, folderName);
//                    this.setSelectedOptions(new String[]{folder.getId()});
//            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to retrieve folder list: " + e.toString(), e);
        }
    }

    public String getSelectedFolderName() {
        Map selected = getSelectedOptions();
        Map options = getOptionMap();
        String key = null;
        String folderName = null;

        if (selected != null && selected.size() > 0) {
            key = selected.keySet().iterator().next().toString();
        }
        else {
            if (options != null && options.size() > 0) {
                key = options.keySet().iterator().next().toString();
            }
        }
        if (key != null) {
            try {
                Application app = Application.getInstance();
                MessagingModule mm = (MessagingModule)app.getModule(MessagingModule.class);
                Folder f = mm.getFolder(key);
                folderName = f.getName();
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Unable to retrieve folder: " + key, e);
            }
        }
//        return (folderName != null) ? folderName : "";
        return getDisplayFolderName(folderName);
    }

    private void populateFolderMap(Map folderMap, Folder folder, int level, String folderIdForParentSel) {
        Collection children = folder.getSubfolders();
        if (children != null) {
            for (Iterator i = children.iterator(); i.hasNext();) {
                Folder f = (Folder)i.next();

                if (!isParentSelection() || (!f.isSpecialFolder() && !f.getId().equals(folderIdForParentSel))) {
                    StringBuffer buffer = new StringBuffer();
                    for (int j=0; j<level; j++) {
                        buffer.append("...");
                    }
                    if (level > 0) {
                        buffer.append("| ");
                    }
                    if(f.isSpecialFolder()){
                        buffer.append(f.getDisplayName());
                    }else{
                        buffer.append(f.getName());             // changed on 13/03/2006
                    }
                    String title = buffer.toString();
                    if (title.length() > 30) {
                        title = title.substring(0, 30) + "..";
                    }

                    folderMap.put(f.getId(), title);
                    populateFolderMap(folderMap, f, level+1, folderIdForParentSel);
                }
            }
        }
    }

    private boolean parentSelection;
    private String folderIdForParentSelection;

    public boolean isParentSelection() {
        return parentSelection;
    }

    public void setParentSelection(boolean parentSelection) {
        this.parentSelection = parentSelection;
    }

    public String getFolderIdForParentSelection() {
        return folderIdForParentSelection;
    }

    /**
     * Set this value to show only the valid parent folders for the specified folderId
     * @param folderIdForParentSelection
     */
    public void setFolderIdForParentSelection(String folderIdForParentSelection) {
        this.folderIdForParentSelection = folderIdForParentSelection;
    }

    public String getDisplayFolderName(String folderName){
        String name="";
        Application app = Application.getInstance();
        if(folderName.equalsIgnoreCase(Folder.FOLDER_INBOX)){
            name = app.getMessage("messaging.label.inbox");
        }else if(folderName.equalsIgnoreCase(Folder.FOLDER_DRAFT)){
            name = app.getMessage("messaging.label.draft");
        }else if(folderName.equalsIgnoreCase(Folder.FOLDER_SENT)){
            name = app.getMessage("messaging.label.sent");
        }else if(folderName.equalsIgnoreCase(Folder.FOLDER_OUTBOX)){
            name = app.getMessage("messaging.label.outbox");
        }else if(folderName.equalsIgnoreCase(Folder.FOLDER_TRASH)){
            name = app.getMessage("messaging.label.trash");
        }else if (folderName.equalsIgnoreCase(Folder.FOLDER_QM)){
            name = app.getMessage("messaging.label.quickMessages");
        }else{
            name=folderName;
        }
        return name;
    }
}
