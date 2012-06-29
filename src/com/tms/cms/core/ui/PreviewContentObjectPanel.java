package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class PreviewContentObjectPanel extends Panel {

    private String id;
    private String version;
    private ContentObjectView contentObjectView;

    public PreviewContentObjectPanel() {
    }

    public PreviewContentObjectPanel(String name) {
        super(name);
    }

    public void init() {
    }

    public void onRequest(Event evt) {
        try {
            ContentObject contentObject = null;

            if (version == null) {
                contentObject = ContentHelper.getContentObject(evt, getId());
            }
            else {
                User user = getWidgetManager().getUser();

                // retrieve object from module
                try {
                    ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                    contentObject = cm.viewVersion(getId(), getVersion(), user);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }

            }

            if (contentObject != null) {
                setId(contentObject.getId());

                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());
                contentObjectView = module.getContentObjectView(contentObject.getClass());

                // initialize content form
                contentObjectView.setName("contentObjectView");
                contentObjectView.init();
                if (contentObject != null) {
                    contentObjectView.setContentObject(contentObject);
                }

                removeChild(contentObjectView);

                // add content form
                addChild(contentObjectView);
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ContentObjectView getContentObjectView() {
        return contentObjectView;
    }

    public void setContentObjectView(ContentObjectView contentObjectView) {
        this.contentObjectView = contentObjectView;
    }

}
