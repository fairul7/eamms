package com.tms.cms.profile.ui;

import kacang.stdui.Form;
import kacang.Application;
import kacang.ui.Event;
import kacang.util.Log;
import com.tms.cms.profile.model.ContentProfileModule;

public class ContentProfilePreviewForm extends Form {

    private String profileId;

    public ContentProfilePreviewForm() {
    }

    public ContentProfilePreviewForm(String name) {
        super(name);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void onRequest(Event event) {
        initForm();
    }

    public void initForm() {
        removeChildren();

        if (getProfileId() != null) {
            try {
                ContentProfileModule cpm = (ContentProfileModule)Application.getInstance().getModule(ContentProfileModule.class);
                Form form = cpm.loadForm(getProfileId(), null, null);
                addChild(form);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error loading profile form " + getProfileId(), e);
            }
        }
    }

}
