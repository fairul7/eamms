package com.tms.cms.profile.ui;

import com.tms.cms.profile.model.ContentProfileModule;
import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class ContentProfileForm extends Form {

    private String profileId;
    private String contentId;
    private String version;
    private String profileName;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ContentProfileForm() {
    }

    public ContentProfileForm(String name) {
        super(name);
    }

    public Forward onValidate(Event event) {
        try {
            Application app = Application.getInstance();
            if (ContentProfileModule.isProfileEnabled()) {
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                profileMod.storeForm(getProfileId(), getContentId(), getVersion(), this);
                return new Forward("success");
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error storing data for profile " + getProfileId(), e);
        }
        return super.onValidate(event);
    }

}
