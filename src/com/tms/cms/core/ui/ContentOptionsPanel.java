package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.section.Section;
import kacang.Application;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 2, 2003
 * Time: 4:23:16 PM
 * To change this template use Options | File Templates.
 */
public class ContentOptionsPanel extends Panel {

    private String id;
    private Map permissionMap;
    private boolean checkIn;

    public String getDefaultTemplate() {
        return "cms/admin/contentOptionsPanel";
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());

            try {
                permissionMap = new HashMap();
                String userId = getWidgetManager().getUser().getId();
                checkIn = userId.equals(co.getCheckOutUserId());  
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                Collection permissions = cm.viewPermissions(co.getId(), userId);
                for (Iterator i=permissions.iterator(); i.hasNext();) {
                    String permissionId = (String)i.next();
                    if (permissionId != null) {
                        permissionMap.put(permissionId, Boolean.TRUE);
                    }
                }

                // check move permission
                Class coClass;
                try {
                    coClass = Class.forName(co.getClassName());
                }
                catch (Exception e) {
                    coClass = co.getClass();
                }
                Collection allowedClassList = Arrays.asList(cm.getAllowedParentClasses(coClass));
                if (!allowedClassList.contains(Section.class)) {
                    permissionMap.remove(ContentManager.USE_CASE_MOVE);
                }

    /*
                // add normal permissions
                for (int i=0; i<PERMISSIONS.length; i++) {
                    if (cm.hasPermission(co, userId, PERMISSIONS[i])) {
                        permissionMap.put(PERMISSIONS[i], Boolean.TRUE);
                    }
                }

                // add edit permission
                if (userId.equals(co.getCheckOutUserId()) || (!co.isCheckedOut() && !co.isSubmitted() && cm.hasPermission(co, userId, ContentManager.USE_CASE_CHECKOUT))) {
                    permissionMap.put(ContentManager.USE_CASE_CHECKOUT, Boolean.TRUE);
                }

                // add undo checkout permission
                if (co.isCheckedOut() && (userId.equals(co.getCheckOutUserId()) || cm.hasPermission(co, userId, ContentManager.USE_CASE_UNDO_CHECKOUT))) {
                    permissionMap.put(ContentManager.USE_CASE_UNDO_CHECKOUT, Boolean.TRUE);
                }

                // add approve permission
                if (co.isSubmitted() && cm.hasPermission(co, userId, ContentManager.USE_CASE_APPROVE)) {
                    permissionMap.put(ContentManager.USE_CASE_APPROVE, Boolean.TRUE);
                }

                // add publish permission
                if ((co.isPublished() || co.isApproved()) && cm.hasPermission(co, userId, ContentManager.USE_CASE_PUBLISH)) {
                    permissionMap.put(ContentManager.USE_CASE_PUBLISH, Boolean.TRUE);
                }
    */
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
        else {
            permissionMap = new HashMap();
        }
    }

    public void init() {
    }

    public Forward actionPerformed(Event evt) {
        Forward forward = super.actionPerformed(evt);
        String eventType = evt.getType();
        Forward tmp = new Forward(eventType);
        return (tmp != null) ? tmp : forward;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map permissionMap) {
        this.permissionMap = permissionMap;
    }

}
