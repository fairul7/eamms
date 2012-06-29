package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.util.Log;

import java.io.Serializable;

public class ContentHelper implements Serializable {

    public final static String REQUEST_KEY_CONTENT_ID = "com.tms.cms.core.ui.ContentObjectId";
    public final static String REQUEST_KEY_CONTENT = "com.tms.cms.core.ui.ContentObject";
    public final static String REQUEST_KEY_CONTENT_LIST = "com.tms.cms.core.ui.ContentObjectList";
    public final static String REQUEST_KEY_CONTENT_ACTION = "com.tms.cms.core.ui.ContentAction";

    public static String getId(Event evt) {
        // look in session
        String id = (String)evt.getRequest().getSession().getAttribute(REQUEST_KEY_CONTENT_ID);
        return id;
    }

    public static void setId(Event evt, String id) {
        // store in session
        evt.getRequest().getSession().setAttribute(REQUEST_KEY_CONTENT_ID, id);
    }

    public static ContentObject getContentObject(Event evt, String id) {
        // look in request
        ContentObject co = (ContentObject)evt.getRequest().getAttribute(REQUEST_KEY_CONTENT);
        if (co != null) {
            return co;
        }

        // determine id
        String coid = getId(evt);
        if (coid == null)
            coid = id;

        if (coid != null) {
            // get current user
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            User user = security.getCurrentUser(evt.getRequest());


            // retrieve object from module
            try {
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                co = cm.view(coid, user);

                // check permission, ignore if root
                if (ContentManager.CONTENT_TREE_ROOT_ID.equals(co.getId()) || cm.hasPermission(co, user.getId(), ContentManager.USE_CASE_PREVIEW))
                    evt.getRequest().setAttribute(REQUEST_KEY_CONTENT, co);
                else
                    co = null;
            }
            catch (DataObjectNotFoundException e) {
                Log.getLog(ContentHelper.class).error("Error loading content " + id, e);
                co = null;
            }
            catch (Exception e) {
                Log.getLog(ContentHelper.class).error("Error loading content " + id, e);
                co = null;
            }
        }
        return co;
    }

}
