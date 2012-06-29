package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class AddContactMessagingForm extends Form {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";

    private String messageId;
    private FolderSelectBox folderSelectBox;
    private Collection contactFormList;
    
    private String _theIndex;

    public AddContactMessagingForm() {
        super();
    }

    public AddContactMessagingForm(String name) {
        super(name);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setTheIndex(String index) {
        _theIndex = index;
    }
    
    public String getTheIndex() { 
        return _theIndex;
    }
    
    public Collection getContactFormList() {
        return contactFormList;
    }

    public void onRequest(Event evt) {
        initForm();
    }

    public void initForm() {

        setMethod("POST");
        removeChildren();
        contactFormList = new ArrayList();

        String id = getMessageId();
        if (id != null && id.trim().length() > 0) {
            try {
                // get message
                Application app = Application.getInstance();
                MessagingModule mod = (MessagingModule)app.getModule(MessagingModule.class);
                Message message = mod.getMessageByMessageId(id);
                String userId = getWidgetManager().getUser().getId();
                Message explodedMessage = Util.processContactGroupAddresses(message, userId);

                // get message emails
                Collection emailList = new ArrayList();
                String from = explodedMessage.getFrom();
                if (!from.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN)) {
                    emailList.add(from);
                }
                Collection toList = explodedMessage.getToList();
                if (toList != null)
                    emailList.addAll(toList);
                Collection ccList = explodedMessage.getCcList();
                if (ccList != null)
                    emailList.addAll(toList);

                // determine new emails
                AddressBookModule abMod = (AddressBookModule)app.getModule(AddressBookModule.class);
                Collection filteredEmailList = abMod.filterContactEmails(emailList, userId);

                // add folder selection
                folderSelectBox = new FolderSelectBox("folderSelectBox");
                folderSelectBox.setType(AddressBookModule.MODULE_NAME);;
                addChild(folderSelectBox);

                // create and add sub forms
                int count = 0;
                for (Iterator i=filteredEmailList.iterator(); i.hasNext(); count++) {
                    String email = (String)i.next();
                    try {
                        InternetAddress ia = new InternetAddress(email);
                        AddContactForm contactForm = new AddContactForm("contactForm_" + count);
                        addChild(contactForm);
                        contactForm.init();

                        // set email address
                        contactForm.getEmail().setText(ia.getAddress());

                        // determine first and last name
                        String firstName = "";
                        String lastName = "";
                        String personal = ia.getPersonal();
                        int idx = (personal != null) ? personal.indexOf(",") : -1;
                        if (personal != null && idx > 0) {
                            firstName = personal.substring(idx+1).trim();
                            lastName = personal.substring(0,idx).trim();
                        }
                        else if (personal != null && personal.trim().length() > 0) {
                            idx = personal.lastIndexOf(" ");
                            if (idx > 0) {
                                lastName = personal.substring(idx+1).trim();
                                firstName = personal.substring(0,idx).trim();
                            }
                            else {
                                firstName = personal;
                            }
                        }
                        contactForm.getFirstName().setValue(firstName);
                        contactForm.getLastName().setValue(lastName);

                        // add sub form
                        contactFormList.add(contactForm);
                    }
                    catch (AddressException e) {
                        Log.getLog(getClass()).debug("Error parsing email address " + email);
                    }
                }

                // add button panel
                Panel buttonPanel = new Panel("buttonPanel");
                Button submit = new Button("submit", app.getMessage("general.label.add"));
                Button cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel"));
                buttonPanel.addChild(submit);
                buttonPanel.addChild(cancel);
                addChild(buttonPanel);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving message contacts", e);
            }
        }
    }

    public Forward onValidate(Event evt) {
        try {
            Application app = Application.getInstance();
            AddressBookModule mod = (AddressBookModule)app.getModule(AddressBookModule.class);
            String userId = getWidgetManager().getUser().getId();

            // get selected folder
            String folderId = null;
            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                folderId = (String)selectedMap.keySet().iterator().next();
            }

            // iterate thru subforms
            Collection formList = getContactFormList();
            for (Iterator i=formList.iterator(); i.hasNext();) {
                AddContactForm form = (AddContactForm)i.next();
                if (form.getAddBox().isChecked()) {
                    // add selected contact
                    try {
                        Contact contact = form.getContact();
                        contact.setOwnerId(userId);
                        contact.setFolderId(folderId);
                        contact.setApproved(true);

                        mod.addContact(contact, userId);
                    }
                    catch (Exception e) {
                        Log.getLog(getClass()).error("Error adding message contact", e);
                    }
                }
            }

            initForm();
            folderSelectBox.initField(); // HACK: reload folders
            return new Forward(FORWARD_SUCCESS);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error adding message contacts", e);
            return new Forward(FORWARD_ERROR);
        }
    }


    class AddContactForm extends Form {

        private TextField firstName;
        private TextField lastName;
        private Label email;
        private CheckBox addBox;

        public AddContactForm() {
            super();
        }

        public AddContactForm(String name) {
            super(name);
        }

        public TextField getFirstName() {
            return firstName;
        }

        public TextField getLastName() {
            return lastName;
        }

        public Label getEmail() {
            return email;
        }

        public CheckBox getAddBox() {
            return addBox;
        }

        public Contact getContact() {
            Contact c = new Contact();
            c.setEmail(email.getText());
            c.setFirstName((String)firstName.getValue());
            c.setLastName((String)lastName.getValue());
            return c;
        }

        public void init() {
            Application app = Application.getInstance();
            setMethod("POST");
            setColumns(3);

            firstName = new TextField("firstName");
            firstName.setSize("20");
            firstName.addChild(new ValidatorNotEmpty("v1") {
                public boolean validate(FormField field) {
                    if (addBox.isChecked())
                        return super.validate(field);
                    else
                        return true;
                }
            });
            lastName = new TextField("lastName");
            lastName.setSize("20");
            email = new Label("email");
            addBox = new CheckBox("add");
            addBox.setChecked(true);
            addBox.setRowspan(3);

            addChild(addBox);
            addChild(new Label("l1", app.getMessage("addressbook.label.email")));
            addChild(email);
            addChild(new Label("l2", app.getMessage("addressbook.label.firstName")));
            addChild(firstName);
            addChild(new Label("l3", app.getMessage("addressbook.label.lastName")));
            addChild(lastName);
        }

    }




}
