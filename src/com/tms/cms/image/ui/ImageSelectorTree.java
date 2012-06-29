package com.tms.cms.image.ui;

import com.tms.cms.core.ui.ContentTree;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.image.Image;
import com.tms.cms.section.Section;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;

/**
 * A Tree of Sections and Images.
 */
public class ImageSelectorTree extends ContentTree {

    public ImageSelectorTree() {
    }

    public ImageSelectorTree(String name) {
        super(name);
    }

    public void init() {
        super.init();
        setIncludeContents(true);
    }

    public String[] getContentObjectClasses() {
        return new String[] { Section.class.getName(), Image.class.getName() };
    }

    public String getDefaultTemplate() {
        return "cms/admin/imageSelectorTree";
    }

    public Object getModel() {
        Application app = Application.getInstance();
        ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
        try {
            // get current user
            User user = getWidgetManager().getUser();

            // get content tree root
            ContentObject root = contentManager.viewTreeWithOrphans(getRootId(), getContentObjectClasses(), isIncludeContents(), ContentManager.USE_CASE_VIEW, user);

            return root;
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error(e.toString(), e);
            return null;
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
            return null;
        }
    }

}
