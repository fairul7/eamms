package com.tms.collab.messaging.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.MessagingException;

import java.util.Map;

public class EditFolderForm extends NewFolderForm {
    private String folderId;

    public void onRequest(Event event) {
        MessagingModule mm;
        Folder folder;

        folderId = event.getRequest().getParameter("folderId");
        if (folderId == null) {
            Log.getLog(getClass()).error("FolderId not specified");
            return;
        }

        try {
            mm = Util.getMessagingModule();
            folder = mm.getFolder(folderId);
            getFolderName().setValue(folder.getName());

            // set parent ID
            FolderSelectBox parentFolder = getParentFolder();
            parentFolder.setFolderIdForParentSelection(folderId);
            parentFolder.setSelectedOption(folder.getParentId());

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            return;
        }
    }

    public Forward onValidate(Event event) {
        MessagingModule mm;
        Folder folder;

        if (folderId == null) {
            // folderId should be set by previous onRequest()
            String msg = "FolderId not specified";
            Log.getLog(getClass()).error(msg);
            event.getRequest().getSession().setAttribute("error", msg);
            return new Forward(FORWARD_ERROR);
        }

        try {
            mm = Util.getMessagingModule();
            folder = mm.getFolder(folderId);
            folder.setName(getFolderName().getValue().toString());

            // set parent ID
            Map selected = getParentFolder().getSelectedOptions();
            if (selected != null && selected.size() > 0);
            folder.setParentId(selected.keySet().iterator().next().toString());

            mm.updateFolder(folder);
            return new Forward(FORWARD_SUCCESS);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    // === [ getters/setters ] =================================================
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

}
