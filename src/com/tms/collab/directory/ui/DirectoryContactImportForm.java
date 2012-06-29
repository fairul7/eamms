package com.tms.collab.directory.ui;

import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.AddressBookException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

import java.util.Map;

public class DirectoryContactImportForm extends DirectoryContactEditForm {

    public void onRequest(Event evt) {
        populateForm();
    }

    public void populateForm() {
        if (getId() != null) {
            AddressBookModule dm = getSourceModule();

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
//                companySelectBox.setSelectedOptions(new String[] {contact.getCompanyId()});
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
//                folderSelectBox.setSelectedOptions(new String[] {contact.getFolderId()});
                approved.setChecked(contact.isApproved());


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

            // populate values
            Contact contact = new Contact();

            Map titleMap = title.getSelectedOptions();
            if (titleMap != null && titleMap.size() > 0) {
                String title = (String)titleMap.keySet().iterator().next();
                contact.setTitle(title);
            }
            contact.setFirstName((String)firstName.getValue());
            contact.setMiddleName((String)middleName.getValue());
            contact.setLastName((String)lastName.getValue());
            contact.setNickName((String)nickName.getValue());

            contact.setDesignation((String)designation.getValue());
            contact.setCompany((String)company.getValue());

            Map companyMap = companySelectBox.getSelectedOptions();
            if (companyMap != null && companyMap.size() > 0) {
                String companyId = (String)companyMap.keySet().iterator().next();
                contact.setCompanyId(companyId);
            }

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

            contact.setApproved(approved.isChecked());

            // store contact
            AddressBookModule dm = getModule();
            contact = dm.addContact(contact, userId);

            initForm();
            if (contact.isApproved()) {
                return new Forward(FORWARD_SUCCESS);
            }
            else {
                return new Forward(FORWARD_PENDING);
            }
        }
        catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error adding contact " + getId() + ": " + e.toString(), e);
            init();
            return new Forward(FORWARD_ERROR);
        }
    }

    AddressBookModule getSourceModule() {
        Application app = Application.getInstance();
        AddressBookModule dm = (AddressBookModule)app.getModule(AddressBookModule.class);
        return dm;
    }

    AddressBookModule getTargetModule() {
        return getModule();
    }

}
