package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class DisplayContentTree extends LightWeightWidget {

    private String id;
    private ContentObject root;
    private String hideSummary;

    public String getId() {
        return (id != null) ? id : ContentManager.CONTENT_TREE_ROOT_ID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContentObject getRoot() {
        return root;
    }

    public String getHideSummary() {
        return hideSummary;
    }

    public void setHideSummary(String hideSummary) {
        this.hideSummary = hideSummary;
    }

    public void onRequest(Event evt) {

        String rootId = getId();

        // retrieve content tree
        try {
            User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());

            Application application = Application.getInstance();
            ContentPublisher cm = (ContentPublisher)application.getModule(ContentPublisher.class);
            String permission = (Boolean.valueOf(getHideSummary()).booleanValue()) ? ContentManager.USE_CASE_VIEW : null;
            this.root = cm.viewTree(rootId, null, permission, user.getId());
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayContentTree";
    }
}
