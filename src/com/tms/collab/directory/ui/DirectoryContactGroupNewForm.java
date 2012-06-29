package com.tms.collab.directory.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.Validator;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.ui.UsersSelectBox;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.DirectoryModule;

import java.util.Map;

public class DirectoryContactGroupNewForm extends Form implements AddressBookWidget {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_PENDING = "pending";

    protected TextField firstName;
    protected TextBox comments;
    protected TextBox emails;
    protected FolderSelectBox folderSelectBox;
    protected DirectoryContactSelectBox contactSelectBox;
    protected UsersSelectBox usersSelectBox;
    protected CheckBox approved;
    protected Button submit;
    protected Button cancel;
    protected Validator validFirstName;

    
    public String getDefaultTemplate() {
    	return "addressbook/bdNewContactGroup";
    }
    
    public DirectoryContactGroupNewForm() {
    }

    public DirectoryContactGroupNewForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        initForm();
    }

    protected void initForm() {
        Application app = Application.getInstance();
        SecurityService security = (SecurityService)app.getService(SecurityService.class);

        removeChildren();
        setColumns(2);
        setMethod("POST");

        Label lb2 = new Label("lb2", Application.getInstance().getMessage("addressbook.label.contactGroupName","Distribution List Name") + " *");
        Label lb3 = new Label("lb3", Application.getInstance().getMessage("addressbook.label.comments","Comments"));
        Label lb4 = new Label("lb4", Application.getInstance().getMessage("addressbook.label.contacts","Contacts"));
        Label lb5 = new Label("lb5", Application.getInstance().getMessage("addressbook.label.contactGroupEmails","Other Emails"));
        Label lb6 = new Label("lb6", Application.getInstance().getMessage("addressbook.label.contactGroupIntranetUsers","Intranet Users"));
        Label lb15 = new Label("lb15", Application.getInstance().getMessage("addressbook.label.folder","Folder"));
        Label lbApproved = new Label("lbApproved", Application.getInstance().getMessage("addressbook.label.approved","Approved"));
        Label lb17 = new Label("lb17", "");

        firstName = new TextField("firstName");
        validFirstName = getNameValidator();
        firstName.addChild(validFirstName);
        comments = new TextBox("comments");
        emails = new TextBox("emails");
        folderSelectBox = new FolderSelectBox("folderSelectBox");
        folderSelectBox.setType(getType());
        contactSelectBox = new DirectoryContactSelectBox("contactSelectBox");
        usersSelectBox = new UsersSelectBox("usersSelectBox");

		if (isEditMode()) {
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.update","Update"));
		}
        else {
			submit = new Button("submit", Application.getInstance().getMessage("addressbook.label.submit","Submit"));
		}
		cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("addressbook.label.cancel","Cancel"));
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(submit);
        buttonPanel.addChild(cancel);

        approved = new CheckBox("approved");
        try {
            if (security.hasPermission(getWidgetManager().getUser().getId(), DirectoryModule.PERMISSION_MANAGE_CONTACTS, null, null)) {
                approved.setChecked(true);
            }
            else {
                approved.setChecked(false);
                approved.setHidden(true);
                lbApproved.setHidden(true);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error getting contact permission", e);
            approved.setChecked(false);
            approved.setHidden(true);
        }


        addChild(lb15);
        addChild(folderSelectBox);
        addChild(lb2);
        addChild(firstName);
        addChild(lb3);
        addChild(comments);
        addChild(lb4);
        addChild(contactSelectBox);
        addChild(lb6);
        addChild(usersSelectBox);
        addChild(lb5);
        addChild(emails);
        addChild(lbApproved);
        addChild(approved);
        addChild(lb17);
        addChild(buttonPanel);

        contactSelectBox.init();
        usersSelectBox.init();
    }

    protected Validator getNameValidator() {
        return new ValidatorNotEmpty("validFirstName") {
            public boolean validate(FormField formField) {
                boolean valid;
                Object value = formField.getValue();
                if (value == null || value.toString().trim().length() == 0) {
                    setText("");
                    valid = false;
                }
                else {
                    setText(Application.getInstance().getMessage("addressbook.label.contactGroupNameInUse", "Name already in use"));
                    try {
                        AddressBookModule mod = getModule();
                        Contact cg = mod.getContactGroupByName(value.toString(), null, null);
                        valid = (cg == null);
                    }
                    catch (DataObjectNotFoundException e) {
                        valid = true;
                    }
                    catch (AddressBookException e) {
                        Log.getLog(getClass()).error("Error retrieving contact group", e);
                        valid = false;
                    }
                }
                return valid;
            }
        };
    }

    public Forward onValidate(Event event) {
        try {
            String userId = getWidgetManager().getUser().getId();
            Contact contact = new Contact();

            contact.setContactGroup(true);
            contact.setFirstName((String)firstName.getValue());
            contact.setComments((String)comments.getValue());
            contact.setContactGroupEmails((String)emails.getValue());

            contact.setOwnerId(userId);

            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                String folderId = (String)selectedMap.keySet().iterator().next();
                contact.setFolderId(folderId);
            }

            String[] selectedContacts = contactSelectBox.getIds();
            contact.setContactIdArray(selectedContacts);

            String[] selectedUsers = usersSelectBox.getIds();
            contact.setIntranetIdArray(selectedUsers);

            contact.setApproved(approved.isChecked());

            AddressBookModule dm = getModule();
            dm.addContact(contact, userId);

            initForm();
            return new Forward(FORWARD_SUCCESS);
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Unable to add contact: " + e.toString(), e);
            return new Forward(FORWARD_ERROR);
        }
    }

    private String type;

    public String getType() {
        return DirectoryModule.MODULE_NAME;
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
