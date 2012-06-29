package com.tms.cms.profile.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.*;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.model.ContentProfile;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;

public class ContentProfileAssignmentForm extends Form {

    protected String id;
    protected SelectBox sbProfile;
    protected Validator vProfile;

    public ContentProfileAssignmentForm() {
    }

    public ContentProfileAssignmentForm(String name) {
        super(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void init() {
        initForm();
    }

    public void initForm() {
        Application app = Application.getInstance();
        sbProfile = new SelectBox("sbProfile");
        Map optionMap = new SequencedHashMap();

        // get profiles
        try {
            if (ContentProfileModule.isProfileEnabled()) {
                optionMap.put("", app.getMessage("security.label.pleaseSelect", "---Please Select---"));
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                Collection list = profileMod.getProfileList(null, "name", false, 0, -1);
                for (Iterator i=list.iterator(); i.hasNext();) {
                    ContentProfile profile = (ContentProfile)i.next();
                    String name = profile.getName();
                    if (name != null && name.trim().length() > 50) {
                        name = name.substring(0, 50);
                    }
                    optionMap.put(profile.getId(), name);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving profiles", e);
        }

        sbProfile.setOptionMap(optionMap);
        addChild(sbProfile);

        if (Boolean.valueOf(app.getProperty(ContentProfileModule.APPLICATION_PROPERTY_CONTENT_PROFILE_COMPULSORY)).booleanValue()) {
            vProfile = new ValidatorMessage("vProfile");
            addChild(vProfile);
        }

    }

    public void onRequest(Event event) {
        populateForm();
    }

    public void populateForm() {
        if (getId() != null && ContentProfileModule.isProfileEnabled()) {
            Application app = Application.getInstance();
            try {
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                ContentProfile profile = profileMod.getProfileByParent(getId());
                sbProfile.setSelectedOptions(new String[] { profile.getId() });
            }
            catch(DataObjectNotFoundException e) {
                Log.getLog(getClass()).debug("Profile form not found for parent " + getId());
            }
            catch(Exception e) {
                Log.getLog(getClass()).error("Error retrieving profile for " + getId(), e);
            }

        }
    }

    public Forward onSubmit(Event event) {
        Application app = Application.getInstance();
        Forward fwd = super.onSubmit(event);
        if (Boolean.valueOf(app.getProperty(ContentProfileModule.APPLICATION_PROPERTY_CONTENT_PROFILE_COMPULSORY)).booleanValue()) {
            String profileId = getProfileId();
            if (profileId == null || profileId.trim().length() == 0) {
                sbProfile.setInvalid(true);
                vProfile.setInvalid(true);
                vProfile.setText(app.getMessage("general.label.required", "Required"));
                setInvalid(true);
            }
        }
        return fwd;
    }

    public String getProfileId() {
        String profileId = null;
        List selected = (List)sbProfile.getValue();
        if (selected != null) {
            profileId = selected.iterator().next().toString();
        }
        return profileId;
    }

/*
    public Forward onValidate(Event event) {
        Application app = Application.getInstance();
        try {
            if (getId() != null) {
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                ContentObject co = cm.view(getId(), user);
                if (cm.hasPermission(co, user.getId(), ContentManager.USE_CASE_APPROVE)) {
                    ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                    profileMod.assignByContent(getId(), getProfileId());
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("Error saving profile for " + getId(), e);
        }
        return super.onValidate(event);
    }
*/

}
