package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Date;
import java.util.Map;

public class DirectoryContactGroupEditForm extends DirectoryContactGroupNewForm {

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event evt) {
        populateForm();
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
                        valid = (cg == null || cg.getId().equals(getId()));
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

    public void populateForm() {
        if (getId() != null) {
            AddressBookModule dm = getModule();

            try {
                // load contact
                Contact contact = dm.getContact(getId());
                firstName.setValue(contact.getFirstName());
                comments.setValue(contact.getComments());
                emails.setValue(contact.getContactGroupEmails());
                contactSelectBox.setIds(contact.getContactIdArray());
                usersSelectBox.setIds(contact.getIntranetIdArray());
                folderSelectBox.setSelectedOptions(new String[] {contact.getFolderId()});
                approved.setChecked(contact.isApproved());


            } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error("Contact group " + getId() + " not found");
                init();
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Contact group " + getId() + " error: " + e.toString(), e);
                init();
                throw new RuntimeException("Contact group " + getId() + " error: " + e.toString());
            }
        }
    }

    public Forward onValidate(Event evt) {
        try {
            String userId = getWidgetManager().getUser().getId();
            AddressBookModule dm = getModule();

            // populate values
            Contact contact = dm.getContact(getId());

            contact.setContactGroup(true);
            contact.setFirstName((String)firstName.getValue());
            contact.setComments((String)comments.getValue());
            contact.setContactGroupEmails((String)emails.getValue());
            contact.setContactIdArray(contactSelectBox.getIds());
            contact.setIntranetIdArray(usersSelectBox.getIds());

            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                String folderId = (String)selectedMap.keySet().iterator().next();
                contact.setFolderId(folderId);
            }

            contact.setApproved(approved.isChecked());

            // store contact
            contact = dm.updateContact(contact, userId);
			User user = getWidgetManager().getUser();
            Log.getLog(getClass()).write(new Date(), id, user.getId(), "kacang.services.log.directory.UpdateContact", "Contact " + contact.getName() + " has been updated by user " + user.getName(), evt.getRequest().getRemoteAddr(), evt.getRequest().getSession().getId());

            initForm();
            if (contact.isApproved()) {
                return new Forward(FORWARD_SUCCESS);
            }
            else {
                return new Forward(FORWARD_PENDING);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error updating contact " + getId() + ": " + e.toString(), e);
            init();
            return new Forward(FORWARD_ERROR);
        }
    }

	public boolean isEditMode() {
		return true;
	}

}
