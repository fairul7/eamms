package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Folder;
import com.tms.collab.directory.model.AddressBookException;

import java.util.Map;

public class FolderNewForm extends Form implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";

    protected TextField nameField;
    protected TextBox description;
    protected FolderSelectBox folderSelectBox;
    protected Button submit;
    protected Button cancel;
    protected Validator validNameField;

    
    public String getDefaultTemplate() {
    	return "addressbook/bdNewFolder";
    }
    
    
    public FolderNewForm() {
    }

    public FolderNewForm(String toEmail) {
        super(toEmail);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        initForm();
    }

    protected void initForm() {
        removeChildren();
        setColumns(2);
        setMethod("POST");

        Label lb1 = new Label("lb1", Application.getInstance().getMessage("addressbook.label.name_*","Name *"));
        Label lb2 = new Label("lb2", Application.getInstance().getMessage("addressbook.label.description","Description"));
        Label lb3 = new Label("lb3", Application.getInstance().getMessage("addressbook.label.parentFolder","Parent Folder"));
        Label lb4 = new Label("lb4", "");

        nameField = new TextField("nameField");
        description = new TextBox("description");
        folderSelectBox = new FolderSelectBox("folderSelectBox");
        folderSelectBox.setType(getType());
        validNameField = new ValidatorNotEmpty("validNameField");
        nameField.addChild(validNameField);

		if(isEditMode()) {
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.update","Update"));
		} else {
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.submit","Submit"));
		}

        cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("addressbook.label.cancel","Cancel"));
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);

        addChild(lb1);
        addChild(nameField);
        addChild(lb2);
        addChild(description);
        addChild(lb3);
        addChild(folderSelectBox);
        addChild(lb4);
        addChild(buttonPanel);
    }

    public Forward onValidate(Event event) {
        try {
            String userId = getWidgetManager().getUser().getId();
            Folder folder = new Folder();
            folder.setName((String)nameField.getValue());
            folder.setDescription((String)description.getValue());
            folder.setUserId(userId);

            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                String parentId = (String)selectedMap.keySet().iterator().next();
                folder.setParentId(parentId);
            }

            AddressBookModule dm = getModule();
            dm.addFolder(folder, userId);

            initForm();
            return new Forward(FORWARD_SUCCESS);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to add folder: " + e.toString(), e);
            return new Forward(FORWARD_ERROR);
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

	public boolean isEditMode() {
		return false;
	}

}
