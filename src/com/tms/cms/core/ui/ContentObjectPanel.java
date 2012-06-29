package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.profile.model.ContentProfile;
import com.tms.cms.profile.model.ContentProfileModule;
import kacang.stdui.Panel;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class ContentObjectPanel extends Panel {

    private ContentObject contentObject;

    public ContentObjectPanel() {
    }

    public ContentObjectPanel(String name) {
        super(name);
    }

    public ContentObject getContentObject() {
        return contentObject;
    }

    public void setContentObject(ContentObject contentObject) {
        this.contentObject = contentObject;
    }

    public String getDefaultTemplate() {
        if (contentObject != null)
            return "cms/admin/contentObjectPanel";
        else
            return "cms/tdk/displayContentNotFound";
    }

    public ContentProfile getContentProfile() {
        ContentProfile profile = null;
        try {
            Application app = Application.getInstance();
            ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
            if (contentObject != null && ContentProfileModule.isProfiledContentParent(contentObject.getClassName())) {
                profile = profileMod.getProfileByParent(contentObject.getId());
            }
            return profile;
        }
        catch(DataObjectNotFoundException e) {
            Log.getLog(getClass()).debug("Profile form not found for parent " + getContentObject());
            return null;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving profile for content", e);
            return null;
        }
    }

}
