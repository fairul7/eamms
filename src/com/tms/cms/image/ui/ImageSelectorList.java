package com.tms.cms.image.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.image.Image;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Listing of related Images (children) for a selected content.
 */
public class ImageSelectorList extends Widget {

    private String id;

    public ImageSelectorList() {
    }

    public ImageSelectorList(String name) {
        super(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "cms/admin/imageSelectorList";
    }

    public void onRequest(Event evt) {
        String newId = ContentHelper.getId(evt);
        if (newId != null && !newId.equals(id)) {
            id = newId;
        }
    }

    public Collection getImageList() {
        ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
        try {
            return cm.viewListWithContents(null, new String[] { Image.class.getName() }, null, getId(), Boolean.FALSE, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, ContentManager.USE_CASE_VIEW, getWidgetManager().getUser());
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
            return new ArrayList();
        }
    }

}
