package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.HashMap;
import java.util.Map;

public class AclContentTree extends ContentTree {

    public static final String EVENT_UPDATE_ACL = "update";
    public static final String FORWARD_ACL_UPDATED = "aclUpdated";

    private String principalId;
    private String group;

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public AclContentTree() {
    }

    public AclContentTree(String name) {
        super(name);
    }

    public Forward actionPerformed(Event evt) {

        // perform selection
        String selectedId = evt.getRequest().getParameter("id");
        setSelectedId(selectedId);

        // invoke listeners
        Forward fwd = fireActionEvent(evt);
        return fwd;
    }

    public Object getModel() {
        Application app = Application.getInstance();
        ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
        try {
            // get user and permission
            String permissionId = null; // ContentManager.USE_CASE_PREVIEW;
            User user = null;
            if (!Boolean.valueOf(getGroup()).booleanValue()) {
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                user = security.getUser(getPrincipalId());
            }
            else {
                user = new User();
                user.setId(getPrincipalId());
            }

            // get content tree root
            ContentObject root = (!isIncludeContents()) ?
                    contentManager.viewTree(getRootId(), getContentObjectClasses(), permissionId, user) :
                    contentManager.viewTreeWithContents(getRootId(), getContentObjectClasses(), permissionId, user);

            return root;
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Content not found: " + getRootId(), e);
            return null;
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).error("Principal not available: " + getPrincipalId(), e);
            return null;
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Content Exception for content: " + getRootId(), e);
            return null;
        }
    }

    public Map getAclMap() {
        Application app = Application.getInstance();
        ContentManager cm = (ContentManager)app.getModule(ContentManager.class);

        try {
            Map aclMap = cm.viewAclByPrincipal(getContentObjectClasses(), cm.getRoleArray(), getPrincipalId());
            return aclMap;
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Content Exception for content: " + getRootId(), e);
            return new HashMap();
        }
    }

    public Principal getPrincipal() {
        if (principalId != null) {
            try {
                // get user
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                if (!Boolean.valueOf(getGroup()).booleanValue()) {
                    User user = security.getUser(getPrincipalId());
                    return user;
                }
                else {
                    Group group = security.getGroup(getPrincipalId());
                    return group;
                }
            }
            catch (SecurityException e) {
                Log.getLog(getClass()).error("Principal not available: " + getPrincipalId(), e);
                return null;
            }

        }
        else {
            return null;
        }
    }

    public String getDefaultTemplate() {
        return "cms/admin/aclContentTree";
    }
}
