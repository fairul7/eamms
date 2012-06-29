package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 5:50:51 PM
 * To change this template use Options | File Templates.
 */
public class ContentPath extends Tree {

    private String rootId = ContentManager.CONTENT_TREE_ROOT_ID;

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public void init() {
        setSelectedId(ContentManager.CONTENT_TREE_ROOT_ID);
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentPath";
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, null);
        if (co != null) {
            setSelectedId(co.getId());
        }
        else {
            ContentHelper.setId(evt, getSelectedId());
        }
    }

    public Forward actionPerformed(Event evt) {
        Forward forward = super.actionPerformed(evt);
        ContentHelper.setId(evt, getSelectedId());
        forward = new Forward("selection");
        return forward;
    }


    public Collection getPathToContentObject() {
        List path = new ArrayList();

        try {
            // get current user
            User user = getWidgetManager().getUser();

            ContentObject co = null;

            // get path
            if (getSelectedId() != null) {
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                co = cm.viewPath(getSelectedId(), user);
            }

            while (co != null) {
                path.add(0, co);
                if (co.getId().equals(getRootId())) {
                    break;
                }
                co = co.getParent();
            }
        }
        catch(Exception e) {

        }

        return path;
    }

}
