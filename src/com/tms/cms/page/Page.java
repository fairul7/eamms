package com.tms.cms.page;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.profile.model.ProfiledContent;
import com.tms.cms.profile.model.ContentProfileModule;
import kacang.Application;
import kacang.util.Log;

/**
 * Represents a Page.
 */
public class Page extends ContentObject implements ProfiledContent {

    public Class getContentModuleClass() {
        return PageModule.class;
    }

    public String getIndexableContent() {
        StringBuffer buffer = new StringBuffer(getName());
        if (getDescription() != null) {
            buffer.append("\n");
            buffer.append(getDescription());
        }
        if (getSummary() != null) {
            buffer.append("\n");
            buffer.append(getSummary());
        }
        if (getContents() != null) {
            buffer.append("\n");
            buffer.append(getContents());
        }

        // handle profiled content
        Application app = Application.getInstance();
        if (ContentProfileModule.isProfileEnabled()) {
            try {
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                buffer.append("\n");
                buffer.append(profileMod.getIndexableProfileData(getId(), getVersion()));
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Unable to retrieve profile data for page " + getId());
            }
        }

        return buffer.toString();
    }

}
