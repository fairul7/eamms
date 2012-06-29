package com.tms.elearning.core.ui;

import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;

import java.util.*;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.section.Section;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 8, 2005
 * Time: 10:48:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CourseMenuPanel extends Panel{

    private String id;
    private Map permissionMap;

    public String getDefaultTemplate() {
        return "elearning/courseMenuPanel";
    }

    public void onRequest(Event evt) {
      /*  ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());

            try {
                permissionMap = new HashMap();
                String userId = getWidgetManager().getUser().getId();
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

            }
            catch (ContentException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
        else {
            permissionMap = new HashMap();
        }*/
        String courseId = evt.getRequest().getParameter("cid");
        setId(courseId);
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

}
