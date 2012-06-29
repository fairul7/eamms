package com.tms.cms.tdk;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

import com.tms.cms.core.model.*;
import com.tms.cms.section.Section;
import com.tms.cms.article.Article;
import com.tms.cms.document.Document;
import org.apache.commons.collections.SequencedHashMap;

public class DisplayContentSubscriptionList extends LightWeightWidget {

    Map contentMap;

    public String getDefaultTemplate() {
        return "cms/tdk/displayContentSubscriptionList";
    }

    public void onRequest(Event event) {

        Application app = Application.getInstance();
        ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
        ContentPublisher cp = (ContentPublisher)app.getModule(ContentPublisher.class);
        User user = event.getWidgetManager().getUser();

        try {
            // handle action
            String action = event.getRequest().getParameter("action");
            if ("unsubscribe".equals(action)) {
                String[] keys = event.getRequest().getParameterValues("id");
                if (keys != null && keys.length > 0) {
                    cm.unsubscribeByUser(user.getId(), keys);
                }
            }
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Error unsubscribing content", e);
        }

        try {
            // retrieve list of subscribtions
            contentMap = new SequencedHashMap();
            Collection subscriptionList = cm.getSubscriptionsByUser(user.getId(), 0, -1);

            // retrieve list of content
            Collection contentList = new ArrayList();
            String[] classes = new String[] { Section.class.getName(), Article.class.getName(), Document.class.getName() };
            if (subscriptionList.size() > 0) {
                Collection idList = new ArrayList();
                for (Iterator i=subscriptionList.iterator(); i.hasNext();) {
                    ContentSubscription cs = (ContentSubscription)i.next();
                    idList.add(cs.getContentId());
                }
                String[] idArray = (String[])idList.toArray(new String[0]);
                contentList = cp.viewList(idArray, classes, null, null, null, "name", false, 0, -1, ContentManager.USE_CASE_VIEW, user.getId());
            }

            // group by type
            for (int c=0; c<classes.length; c++) {
                contentMap.put(classes[c], new ArrayList());
            }
            for (Iterator i=contentList.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                String className = co.getClassName();
                Collection list = (Collection)contentMap.get(className);
                if (list == null) {
                    list = new ArrayList();
                    contentMap.put(className, list);
                }
                list.add(co);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving content subscriptions", e);
        }

    }

    public Map getContentMap() {
        return contentMap;
    }

}
