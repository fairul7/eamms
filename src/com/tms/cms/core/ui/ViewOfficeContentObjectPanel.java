package com.tms.cms.core.ui;

import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;

public class ViewOfficeContentObjectPanel extends ViewContentObjectPanel {

    public ViewOfficeContentObjectPanel() {
    }

    public void onRequest(Event evt) {
        try {
            User user = getWidgetManager().getUser();
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            ContentObject contentObject = cm.view(getId(), user);

            ContentObjectPanel contentObjectPanel = getContentObjectPanel();
            if (contentObject != null) {
                setId(contentObject.getId());
                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());
                contentObjectPanel = module.getContentObjectPanel(contentObject.getClass());
            }
            else {
                contentObjectPanel = new ContentObjectPanel("panel");
            }
            setContentObjectPanel(contentObjectPanel);

            // initialize content form
            contentObjectPanel.setName("contentObjectPanel");
            contentObjectPanel.init();
            contentObjectPanel.setContentObject(contentObject);

            removeChild(contentObjectPanel);

            // add content form
            addChild(contentObjectPanel);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

    }
}
