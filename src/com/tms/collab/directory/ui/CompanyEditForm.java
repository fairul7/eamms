package com.tms.collab.directory.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;

import java.util.Date;

public class CompanyEditForm extends CompanyNewForm {

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

            try {
                // load contact
                Contact contact = dm.getContact(getId());
                company.setValue(contact.getCompany());
                address.setValue(contact.getAddress());
                city.setValue(contact.getCity());
                state.setValue(contact.getState());
                postcode.setValue(contact.getPostcode());
                country.setValue(contact.getCountry());
                email.setValue(contact.getEmail());
                phone.setValue(contact.getPhone());
                fax.setValue(contact.getFax());
                comments.setValue(contact.getComments());
                approved.setChecked(contact.isApproved());


            } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error("Company " + getId() + " not found");
                init();
            }
            catch (AddressBookException e) {
                Log.getLog(getClass()).error("Company " + getId() + " error: " + e.toString(), e);
                init();
                throw new RuntimeException("Company " + getId() + " error: " + e.toString());
            }
        }
    }

    public Forward onValidate(Event evt) {
        try {
            String userId = getWidgetManager().getUser().getId();
            AddressBookModule dm = getModule();

            // populate values
            Contact contact = dm.getContact(getId());

            contact.setFirstName((String)company.getValue());
            contact.setCompany((String)company.getValue());
            contact.setAddress((String)address.getValue());
            contact.setCity((String)city.getValue());
            contact.setState((String)state.getValue());
            contact.setPostcode((String)postcode.getValue());
            contact.setCountry((String)country.getValue());
            contact.setEmail((String)email.getValue());
            contact.setPhone((String)phone.getValue());
            contact.setFax((String)fax.getValue());
            contact.setComments((String)comments.getValue());

            contact.setApproved(approved.isChecked());

            // store contact
            contact = dm.updateContact(contact, userId);
			User user = getWidgetManager().getUser();
			Log.getLog(getClass()).write(new Date(), id, user.getId(), "kacang.services.log.directory.UpdateCompany", "Company " + contact.getCompany() + " update by user "+ user.getName(), evt.getRequest().getRemoteAddr(), evt.getRequest().getSession().getId());

            initForm();
            if (contact.isApproved()) {
                return new Forward(FORWARD_SUCCESS);
            }
            else {
                return new Forward(FORWARD_PENDING);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error updating company " + getId() + ": " + e.toString(), e);
            init();
            return new Forward(FORWARD_ERROR);
        }
    }

	public boolean isEditMode() {
		return true;
	}

}
