package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentUtil;
import com.tms.cms.template.TemplateFrontServlet;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 9, 2003
 * Time: 2:47:35 PM
 * To change this template use Options | File Templates.
 */
public class FrontEndEditor extends LightWeightWidget {

    private String id;
    private Map permissionMap;
    private boolean editMode;

    public void onRequest(Event evt) {
        // determine edit mode
        boolean editMode = ContentUtil.isEditModeRequest(evt.getRequest()) && !ContentUtil.isPreviewRequest(evt.getRequest());
        if (!editMode) {
            editMode = isEditMode();
        }
        setEditMode(editMode);

        if (editMode) {

            // determine action
            String action = evt.getRequest().getParameter("action");
            String id = getId();
            if (id == null) {
                id = evt.getRequest().getParameter(TemplateFrontServlet.PARAMETER_KEY_CONTENT_ID);
            }
            if (id == null || id.trim().length() == 0) {
                return;
            }

            // do work
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
            if ("edit".equals(action)) {
                ContentHelper.setId(evt, id);
                editMode = false;
            }
            else if ("admin".equals(action)) {
                ContentHelper.setId(evt, id);
                editMode = false;
            }

            // retrieve permission map
            permissionMap = new HashMap();
            try {
                Collection permissions = cm.viewPermissions(id, user.getId());
                for (Iterator i=permissions.iterator(); i.hasNext();) {
                    String permissionId = (String)i.next();
                    if (permissionId != null) {
                        permissionMap.put(permissionId, Boolean.TRUE);
                    }
                }
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Unable to retrieve permissions for: " + id, e);
            }
        }
    }

    public String getDefaultTemplate() {
        return (editMode) ? "cms/admin/frontEndEditor" : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Map getPermissionMap() {
        return permissionMap;
    }

}
