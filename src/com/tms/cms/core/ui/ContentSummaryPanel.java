package com.tms.cms.core.ui;

import kacang.stdui.Panel;
import kacang.Application;
import kacang.services.security.User;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.image.Image;

import java.util.List;
import java.util.ArrayList;

public class ContentSummaryPanel extends Panel {

    public String getDefaultTemplate() {
        return "cms/admin/contentSummaryPanel";
    }

    public int getCheckedOutCount() {
        try {
            User user = getWidgetManager().getUser();

            // query
            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            int total = cm.viewCount(null, null, null, null, Boolean.TRUE, null, null, null, null, Boolean.FALSE, user.getId(), ContentManager.USE_CASE_PREVIEW, user);
            return total;
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int getSubmittedCount() {
        try {
            // query
            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            int total = cm.viewCount(null, null, null, null, null, Boolean.TRUE, null, null, null, Boolean.FALSE, null, ContentManager.USE_CASE_APPROVE, getWidgetManager().getUser());
            return total;
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int getApprovedCount() {
        try {
            // query
            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            // ignore images
            Class[] classes = cm.getAllowedClasses(null);
            List classList = new ArrayList();
            for (int i=0; i<classes.length; i++) {
                Class cl = (Class)classes[i];
                if (!cl.equals(Image.class)) {
                    classList.add(cl.getName());
                }
            }
            String[] classNames = (String[])classList.toArray(new String[classList.size()]);
            int total = cm.viewCount(null, classNames, null, null, null, null, Boolean.TRUE, null, Boolean.FALSE, Boolean.FALSE, null, ContentManager.USE_CASE_PUBLISH, getWidgetManager().getUser());
            return total;
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
