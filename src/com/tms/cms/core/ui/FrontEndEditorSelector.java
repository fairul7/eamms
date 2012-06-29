package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentUtil;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 9, 2003
 * Time: 6:35:41 PM
 * To change this template use Options | File Templates.
 */
public class FrontEndEditorSelector extends LightWeightWidget {

    private boolean editMode;
    private boolean refreshCache;
    private boolean hasPermission;

    public boolean isEditMode() {
        return editMode;
    }

    public boolean isRefreshCache() {
        return refreshCache;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void onRequest(Event evt) {
        try {
            // check permission
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            User user = security.getCurrentUser(evt.getRequest());
            hasPermission =  !ContentUtil.isPreviewRequest(evt.getRequest()) && !SecurityService.ANONYMOUS_USER_ID.equals(user.getId()) && security.hasPermission(user.getId(), ContentManager.PERMISSION_FRONT_END_EDIT, ContentManager.class.getName(), null);

            String action = evt.getRequest().getParameter("editMode");
            if (action != null) {
                if (Boolean.valueOf(action).booleanValue()) {
                        if (hasPermission) {
                            ContentUtil.setEditModeRequest(evt.getRequest(), true);
                            editMode = true;
                            refreshCache = true;
                        }
                }
                else {
                    ContentUtil.setEditModeRequest(evt.getRequest(), false);
                    editMode = false;
                    refreshCache = true;
                }
            }
            else {
                editMode = ContentUtil.isEditModeRequest(evt.getRequest());
                refreshCache = editMode;
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to enter edit mode: " + e.toString(), e);
        }
    }

    public String getDefaultTemplate() {
        return "cms/admin/frontEndEditorSelector";
    }

}
