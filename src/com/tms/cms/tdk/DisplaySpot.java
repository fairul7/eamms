package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.spot.Spot;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class DisplaySpot extends LightWeightWidget {

    private ContentObject contentObject;
    private String id;

    public ContentObject getContentObject() {
        return contentObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event evt) {
        // get id
//        String key = evt.getRequest().getParameter("id");
        String key = getId();
        if (key == null)
            key = evt.getRequest().getParameter("id");

        // retrieve content object
        if (key != null && key.trim().length() > 0) {
            try {
                User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
                Application application = Application.getInstance();
                ContentPublisher cm = (ContentPublisher)application.getModule(ContentPublisher.class);
                this.contentObject = cm.view(key, user);
            }
            catch(DataObjectNotFoundException e) {
                this.contentObject = new Spot();
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
    }

    public String getDefaultTemplate() {
        if (contentObject == null)
            return "cms/tdk/displayContentNotFound";
        else
            return "cms/tdk/displaySpot";
    }

}
