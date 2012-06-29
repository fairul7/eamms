package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.ArrayList;
import java.util.Collection;

public class DisplayRelatedContent extends LightWeightWidget {

    private Collection related;
    private String id;

    public Collection getRelated() {
        return related;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event evt) {
        // get id
        String key = getId();
        if (key == null)
            key = evt.getRequest().getParameter("id");

        if (key == null || key.trim().length() == 0) {
            this.related = new ArrayList();
        }

        // retrieve related content
        try {
            User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
            Application application = Application.getInstance();
            ContentPublisher cm = (ContentPublisher)application.getModule(ContentPublisher.class);
            this.related = cm.viewRelated(key, null, null, false, 0, -1, user);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayRelatedContent";
    }
}
