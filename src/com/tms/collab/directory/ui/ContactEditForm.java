package com.tms.collab.directory.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

public class ContactEditForm extends ContactNewForm {

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

    public void populateForm() {
        if (getId() != null) {
            AddressBookModule dm = getModule();

            // get existing contact titles
            try {
                Collection titleList = dm.getContactTitleList();
                title.setOptionMap(new SequencedHashMap());
                title.addOption("", "");
                for (Iterator i=titleList.iterator(); i.hasNext();) {
                    String t = (String)i.next();
                    title.addOption(t, t);
                }
            }
            catch (AddressBookException e) {
                // ignore
            }

            try {
                // load contact
                Contact contact = dm.getContact(getId());
                title.setSelectedOptions(new String[] {contact.getTitle()});
                firstName.setValue(contact.getFirstName());
                middleName.setValue(contact.getMiddleName());
                lastName.setValue(contact.getLastName());
                nickName.setValue(contact.getNickName());
                designation.setValue(contact.getDesignation());
                company.setValue(contact.getCompany());
                address.setValue(contact.getAddress());
                city.setValue(contact.getCity());
                state.setValue(contact.getState());
                postcode.setValue(contact.getPostcode());
                country.setValue(contact.getCountry());
                email.setValue(contact.getEmail());
                phone.setValue(contact.getPhone());
                extension.setValue(contact.getExtension());
                fax.setValue(contact.getFax());
                mobile.setValue(contact.getMobile());
                comments.setValue(contact.getComments());
                folderSelectBox.setSelectedOptions(new String[] {contact.getFolderId()});

            } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error("Contact " + getId() + " not found");
                init();
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Contact " + getId() + " error: " + e.toString(), e);
                init();
                throw new RuntimeException("Contact " + getId() + " error: " + e.toString());
            }
        }
    }

    public Forward onValidate(Event evt) {
        try {
            String userId = getWidgetManager().getUser().getId();
            AddressBookModule dm = getModule();

            // populate values
            Contact contact = dm.getContact(getId());

            String t = (String)newTitle.getValue();
            if (t == null || t.trim().length() == 0) {
                Map titleMap = title.getSelectedOptions();
                if (titleMap != null && titleMap.size() > 0) {
                    t = (String)titleMap.keySet().iterator().next();
                }
            }
            contact.setTitle(t);
            contact.setFirstName((String)firstName.getValue());
            contact.setMiddleName((String)middleName.getValue());
            contact.setLastName((String)lastName.getValue());
            contact.setNickName((String)nickName.getValue());

            contact.setDesignation((String)designation.getValue());
            contact.setCompany((String)company.getValue());

            contact.setAddress((String)address.getValue());
            contact.setCity((String)city.getValue());
            contact.setState((String)state.getValue());
            contact.setPostcode((String)postcode.getValue());
            contact.setCountry((String)country.getValue());
            contact.setEmail((String)email.getValue());
            contact.setPhone((String)phone.getValue());
            contact.setExtension((String)extension.getValue());
            contact.setFax((String)fax.getValue());
            contact.setMobile((String)mobile.getValue());
            contact.setComments((String)comments.getValue());

            Map selectedMap = folderSelectBox.getSelectedOptions();
            if (selectedMap != null && selectedMap.size() > 0) {
                String folderId = (String)selectedMap.keySet().iterator().next();
                contact.setFolderId(folderId);
            }

            contact.setApproved(true);

            // store contact
            dm.updateContact(contact, userId);

            initForm();
            return new Forward(FORWARD_SUCCESS);
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
