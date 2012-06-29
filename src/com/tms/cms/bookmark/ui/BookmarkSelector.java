package com.tms.cms.bookmark.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.formwizard.model.FormModule;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.Collection;

/**
 * Light-weight Widget for selecting a link to an existing forum/form.
 */
public class BookmarkSelector extends LightWeightWidget {

    private Collection formList;
    private Collection forumList;

    public Collection getFormList() {
        return formList;
    }

    public Collection getForumList() {
        return forumList;
    }

    public void onRequest(Event evt) {
        // get current user
        Application application = Application.getInstance();
        User user = ((SecurityService)application.getService(SecurityService.class)).getCurrentUser(evt.getRequest());

        // retrieve forms
        try {
            FormModule fm = (FormModule)application.getModule(FormModule.class);
            formList = fm.getViewForms(new DaoQuery(), evt.getWidgetManager().getUser().getId(),"formDisplayName",false, 0, -1);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving form process listing: " + e.toString(), e);
        }

        // retrieve forums
        ForumModule forumMod = (ForumModule)application.getModule(ForumModule.class);
        try {
            forumList = forumMod.getAllForum(user.getId());
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving forum listing: " + e.toString(), e);
        }

    }

    public String getDefaultTemplate() {
        return "cms/admin/bookmarkSelector";
    }
}
