package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DisplayContentPath extends LightWeightWidget {

    private String rootId;
    private String id;
    private String userId;

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event evt) {
        // get id
        id = getId();
        if (id == null)
            id = evt.getRequest().getParameter("id");

        // get user ID
        User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
        userId = user.getId();
    }

    public String getDefaultTemplate() {
        if (id == null)
            return "";
        else
            return "cms/tdk/displayContentPath";
    }

    public Collection getPathToContentObject() {
        List path = new ArrayList();

        try {

            // get path
            ContentObject co = null;
            if (this.id != null) {
                Application application = Application.getInstance();
                ContentPublisher cm = (ContentPublisher)application.getModule(ContentPublisher.class);
                co = cm.viewPath(this.id, this.userId);
            }

            boolean isInPath = false;
            ContentObject tmp = co;
            while (tmp != null) {
                path.add(0, tmp);
                tmp = tmp.getParent();
                if (tmp != null && tmp.getId().equals(rootId)) {
                    isInPath = true;
                    break;
                }
            }
            if (!isInPath) {
                path.clear();
                path.add(co);
            }
        }
        catch(Exception e) {
        }

        return path;
    }

}
