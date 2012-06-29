package com.tms.cms.core.ui;

import com.tms.cms.bookmark.Bookmark;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentUtil;
import com.tms.cms.section.Section;
import com.tms.cms.spot.Spot;
import com.tms.cms.vsection.VSection;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Arrays;
import java.util.Collection;

public class ContentTree extends Tree {

    private String rootId = ContentManager.CONTENT_TREE_ROOT_ID;
    private boolean includeContents = false;
    private String[] contentObjectClasses = new String[] {
        Section.class.getName(),
        VSection.class.getName(),
        Bookmark.class.getName(),
        Spot.class.getName(),
    };

    public ContentTree() {
    }

    public ContentTree(String name) {
        super(name);
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public boolean isIncludeContents() {
        return includeContents;
    }

    public void setIncludeContents(boolean includeContents) {
        this.includeContents = includeContents;
    }

    public String[] getContentObjectClasses() {
        return contentObjectClasses;
    }

    public void setContentObjectClasses(String[] contentObjectClasses) {
        this.contentObjectClasses = contentObjectClasses;
    }

    public void init() {
        setSelectedId(ContentManager.CONTENT_TREE_ROOT_ID);
/*
        ContentObject root = (ContentObject)getModel();
        if (root != null) {
            // check root permission
            User user = getWidgetManager().getUser();
            Application app = Application.getInstance();
            ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
            if (cm.hasPermission(root, user.getId(), ContentManager.USE_CASE_PREVIEW)) {
                // select root
                setSelectedId(ContentManager.CONTENT_TREE_ROOT_ID);
            }
            else {
                // select first child
                Collection children = root.getChildren();
                if (children != null && children.size() > 0) {
                    ContentObject child = (ContentObject)children.iterator().next();
                    ContentHelper.setId(evt, getSelectedId());
                    selectContentId(getSelectedId());

                }

            }
        }
*/
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, null);
        if (co != null) {
            selectContentId(co.getId());
        }
    }

    protected void selectContentId(String id) {
        try {
            // check to see if in tree
            String className = ContentUtil.getClassNameFromKey(id);
            Collection classList = Arrays.asList(contentObjectClasses);
            if (classList.contains(className)) {
                setSelectedId(id);
            }
            else {
                // select parent instead
                Application app = Application.getInstance();
                ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
                ContentObject tmp = contentManager.viewPath(id, getWidgetManager().getUser());
                while(tmp.getParent() != null) {
                    tmp = tmp.getParent();
                    if (classList.contains(tmp.getClassName())) {
                        setSelectedId(tmp.getId());
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }

    public Forward actionPerformed(Event evt) {
        Forward forward = super.actionPerformed(evt);
        if (forward == null) {
            ContentHelper.setId(evt, getSelectedId());
            selectContentId(getSelectedId());
            forward = new Forward("selection");
        }
        return forward;
    }


    public Object getModel() {
        Application app = Application.getInstance();
        ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
        try {
            // get current user
            User user = getWidgetManager().getUser();

            // get content tree root
            ContentObject root = contentManager.viewTreeWithOrphans(getRootId(), getContentObjectClasses(), isIncludeContents(), ContentManager.USE_CASE_PREVIEW, user);

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

    public String getDefaultTemplate() {
        return "cms/admin/contentTree";
    }
}
