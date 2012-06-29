package com.tms.cms.profile.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.Validator;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.Application;

import java.util.Collection;
import java.util.Iterator;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.model.ContentProfile;

public class AddContentProfileForm extends Form {

    public static final String FORWARD_ADD = "add";
    public static final String FORWARD_ERROR = "error";

    protected String profileId;

    protected TextField tfName;
    protected TextBox tfDescription;
    protected ValidatorNotEmpty vName;
    protected ValidatorDuplicateName vDuplicateName;
    protected Button bSave;
    protected Button bCancel;
    protected Panel p1;
    
    
    public String getDefaultTemplate() {
    	return "cms/admin/contentprofile";
    }
    

    public AddContentProfileForm() {
    }

    public AddContentProfileForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        setProfileId(null);
        initForm();
    }

    public void initForm() {
        setMethod("POST");
        setColumns(2);
        removeChildren();

        Application app = Application.getInstance();
        tfName = new TextField("tfName");
        vName = new ValidatorNotEmpty("vName");
        tfName.addChild(vName);
        vDuplicateName = new ValidatorDuplicateName("vDuplicateName");
        vDuplicateName.setText(app.getMessage("cms.label.profileNameInUse", "Profile Name Already In Use"));
        tfName.addChild(vDuplicateName);
        tfDescription = new TextBox("tfDescription");
        bSave = new Button("bSave", app.getMessage("general.label.save", "Save"));
        bCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        addChild(new Label("l1", app.getMessage("general.label.name", "Name")));
        addChild(tfName);
        addChild(new Label("l2", app.getMessage("general.label.description", "Description")));
        addChild(tfDescription);
        p1 = new Panel("p1");
        p1.addChild(bSave);
        p1.addChild(bCancel);
        addChild(new Label("l3", ""));
        addChild(p1);
    }

    public Forward onValidate(Event event) {
        try {
            ContentProfile profile = new ContentProfile();
            profile.setName((String)tfName.getValue());
            profile.setDescription((String)tfDescription.getValue());

            Application app = Application.getInstance();
            ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);
            String newId = cpm.addProfile(profile);
            setProfileId(newId);

            initForm();
            return new Forward(FORWARD_ADD);
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Error creating profile", e);
            throw new RuntimeException("Error creating profile: " + e.getMessage());
        }
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public class ValidatorDuplicateName extends Validator {

        public ValidatorDuplicateName() {
        }

        public ValidatorDuplicateName(String name) {
            super(name);
        }

        public boolean validate(FormField formField) {
            boolean valid = true;
            String profId = getProfileId() != null ? getProfileId() : "";
            String name = (String)formField.getValue();
            if (name == null) {
                name = "";
            }
            try {
                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);

                Collection list = cpm.getProfileList(name, null, false, 0, -1);
                for (Iterator i=list.iterator(); i.hasNext();) {
                    ContentProfile p = (ContentProfile)i.next();
                    if (!profId.equals(p.getId()) && name.equals(p.getName())) {
                        valid = false;
                        break;
                    }
                }
                return valid;
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Error retrieving profile list", e);
                return false;
            }
        }

    }
}
