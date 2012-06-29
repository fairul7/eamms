package com.tms.cms.profile.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.stdui.Button;
import kacang.util.Log;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.model.ContentProfile;

public class EditContentProfileForm extends AddContentProfileForm {

    public static final String FORWARD_UPDATE = "update";
    public static final String FORWARD_EDIT_FIELDS = "fields";

    protected Button bEdit;

    public EditContentProfileForm() {
    }

    public EditContentProfileForm(String name) {
        super(name);
    }

    public void onRequest(Event event) {
        populateForm();
    }

    public void initForm() {
        super.initForm();
        bEdit = new Button("bEdit", "Edit Fields");
        p1.addChild(bEdit);
    }

    protected void populateForm() {
        if (getProfileId() != null) {
            try {
                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                ContentProfile profile = cpm.getProfile(getProfileId());
                tfName.setValue(profile.getName());
                tfDescription.setValue(profile.getDescription());
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving profile " + getProfileId(), e);
                throw new RuntimeException("Error retrieving profile " + getProfileId());
            }

        }
    }

    public Forward onValidate(Event event) {
        try {
            String button = findButtonClicked(event);
            if (bEdit.getAbsoluteName().equals(button)) {
                initForm();
                return new Forward(FORWARD_EDIT_FIELDS);
            }
            else {
                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                ContentProfile profile = cpm.getProfile(getProfileId());
                profile.setName((String)tfName.getValue());
                profile.setDescription((String)tfDescription.getValue());

                cpm.updateProfile(profile);

                initForm();
                return new Forward(FORWARD_UPDATE);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error updating profile " + getProfileId(), e);
            throw new RuntimeException("Error updating profile: " + e.getMessage());
        }
    }

}
