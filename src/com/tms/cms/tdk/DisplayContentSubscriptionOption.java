package com.tms.cms.tdk;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentSubscription;

public class DisplayContentSubscriptionOption extends LightWeightWidget {

    private String id;
    private ContentSubscription subscription;
    private boolean hidden;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContentSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(ContentSubscription subscription) {
        this.subscription = subscription;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayContentSubscriptionOption";
    }

    public void onRequest(Event event) {
        Application app = Application.getInstance();
        SecurityService security = (SecurityService)app.getService(SecurityService.class);
        ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
        User user = event.getWidgetManager().getUser();
        String contentId = getId();

        try {
            // check that feature is enabled and user is logged in
            if (!Boolean.valueOf(app.getProperty(ContentManager.APPLICATION_PROPERTY_CONTENT_SUBSCRIPTION)).booleanValue()
                    || SecurityService.ANONYMOUS_USER_ID.equals(user.getId())
                    || !security.hasPermission(user.getId(), ContentManager.PERMISSION_SUBSCRIBE_CONTENT, ContentManager.class.getName(), null)) {
                this.subscription = null;
                this.hidden = true;
                return;
            }

            // get current subscription
            this.subscription = cm.getSubscription(user.getId(), contentId);

            // handle event
            String action = event.getRequest().getParameter("action");
            if ("subscribe".equals(action)) {
                if (this.subscription == null) {
                    cm.subscribeByUser(user.getId(), new String[] {contentId});
                    this.subscription = cm.getSubscription(user.getId(), contentId);
                }
            }
            else if ("unsubscribe".equals(action)) {
                if (this.subscription != null) {
                    cm.unsubscribeByUser(user.getId(), new String[] {contentId});
                    this.subscription = cm.getSubscription(user.getId(), contentId);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to get subscription for content " + contentId);
            this.subscription = null;
        }

    }

}
