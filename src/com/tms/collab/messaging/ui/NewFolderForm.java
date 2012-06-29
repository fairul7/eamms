package com.tms.collab.messaging.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;

import java.util.Map;

public class NewFolderForm extends Form {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";

    private TextField folderName;
    private Button submit;
    private Button cancel;
    private FolderSelectBox parentFolder;

    
    public String getDefaultTemplate() {
    	return "messaging/newFolder";
    }
    
    
    public void init() {
        setColumns(2);
        setMethod("POST");

        addChild(new Label("1", Application.getInstance().getMessage("messaging.label.folderName","Folder Name")+" *"));
        folderName = new TextField("name");
        folderName.addChild(new ValidatorNotEmpty("1"));
        addChild(folderName);

        addChild(new Label("2", Application.getInstance().getMessage("messaging.label.parentFolder","Parent Folder")));
        parentFolder = new FolderSelectBox("parentFolder");
        parentFolder.setParentSelection(true);
        addChild(parentFolder);

        addChild(new Label("3", "&nbsp;"));
        submit = new Button("submit", Application.getInstance().getMessage("messaging.label.submit","Submit"));
        cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("general.label.cancel","Cancel"));
        Panel p = new Panel("buttons");
        p.addChild(submit);
        p.addChild(cancel);
        addChild(p);
    }

    public Forward onValidate(Event event) {
        User user = getWidgetManager().getUser();
        Folder folder;

        folder = new Folder();
        folder.setFolderId(UuidGenerator.getInstance().getUuid());
        folder.setSpecialFolder(false);
        folder.setUserId(user.getId());
        folder.setName(getFolderName().getValue().toString());

        // set parent ID
        Map selected = getParentFolder().getSelectedOptions();
        if (selected != null && selected.size() > 0);
        folder.setParentId(selected.keySet().iterator().next().toString());

        try {
            MessagingModule mm = Util.getMessagingModule();
            mm.addFolder(folder);
            init();
            return new Forward(FORWARD_SUCCESS);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    // === [ getters/setters ] =================================================
    public TextField getFolderName() {
        return folderName;
    }

    public void setFolderName(TextField folderName) {
        this.folderName = folderName;
    }

    public Button getSubmit() {
        return submit;
    }

    public void setSubmit(Button submit) {
        this.submit = submit;
    }

    public FolderSelectBox getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderSelectBox parentFolder) {
        this.parentFolder = parentFolder;
    }
}
