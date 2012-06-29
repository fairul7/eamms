package com.tms.cms.syndication.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.Application;

import java.util.*;
import java.io.IOException;

import com.tms.cms.syndication.model.SyndicationDaoException;
import com.tms.cms.syndication.model.SyndicationModule;
import com.tms.cms.syndication.model.SynObject;
import com.tms.cms.syndication.model.Rss;
import org.xml.sax.SAXException;
import com.tms.cms.syndication.rss.RssFeed;
import com.tms.cms.syndication.rss.RssManager;
import com.tms.cms.syndication.rss.RssItem;


public class SyndicationPortlet extends Form  {
    private List itemsList;
    private Map timeMap;
    private String next;
    private String previous;
    private Button btnUpdate;


    public SyndicationPortlet() {
    }

    public void init() {
        itemsList = new ArrayList();
        timeMap = new HashMap();
        btnUpdate = new  Button("btnUpdate", "Refresh Now");
        addChild(btnUpdate);
    }

    public void onRequest(Event event) {
        updateFeed(event.getWidgetManager().getUser().getId());

    }

    public Forward onValidate(Event evt) {
        if(btnUpdate.getAbsoluteName().equals(findButtonClicked(evt))) {
            timeMap.clear();
            itemsList.clear();
            updateFeed(evt.getWidgetManager().getUser().getId());
            return new Forward("refresh");
        }

        return null;
    }

    public String getDefaultTemplate() {
        return "syndication/portlet";
    }

    public Collection getFeeds(String userId) {
        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);
        try {
            return module.getSynObjectByUserId(userId);
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        return null;
    }

    public void updateFeed(String userId) {
        SynObject object = null;
        RssManager rm = RssManager.getInstance();
        RssFeed feed = new RssFeed();
        List rssList = null;
        RssItem rssItem = null;
        Rss rss = null;
        Map map = new HashMap();
      //  Iterator iterator = null;

        Collection feeds = null;

        if (next == null)
            feeds = getFeeds(userId);

        if (feeds != null) {
            for (Iterator iterator = feeds.iterator(); iterator.hasNext();) {
                object = (SynObject) iterator.next();
                map.put(object.getId(),null);
                if (!isUpdateRequired(object.getId(),object.getRefreshRate()))
                    continue;

                feed = new RssFeed();
                feed.setUrl(object.getLink());
                try {
                    rm.retrieveRssFeed(feed);
                    rssList = feed.getRssItemList();

                    for (Iterator iterator1 = rssList.iterator(); iterator1.hasNext();) {
                        rssItem =  (RssItem) iterator1.next();
                        rss = new Rss();
                        rss.setTitle(rssItem.getTitle());
                        rss.setSummary(rssItem.getDescription());
                        rss.setLink(rssItem.getLink());
                        rss.setId(object.getId());
                        rss.setChannelName(object.getTitle());
                        rss.setPubDate(rssItem.getPubDate());
                        rss.setImageLink(feed.getImageUrl());
                        itemsList.add(rss);
                    }
                    rssList.clear();
                }
                catch (IOException e) {
                    Log.getLog(getClass()).info(e.getMessage());
                }
                catch (SAXException e) {
                    Log.getLog(getClass()).info(e.getMessage());
                }


            }


        }
        checkForDeletedObject(map);
    }

    public void checkForDeletedObject(Map map) {
        Set set = timeMap.keySet();
        String id = null;
        Map idMap = new HashMap();

        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            id =  (String) iterator.next();
            if (!map.containsKey(id)) {
                clearItemList(id);
                idMap.put(id,null);
            }

        }

        set = idMap.keySet();

        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            id =  (String) iterator.next();
            timeMap.remove(id);
        }
    }

    public boolean isUpdateRequired(String id, String refreshRate) {
        Long value = null;
        if (timeMap.containsKey(id)) {
            value = (Long) timeMap.get(id);
            boolean isUpdatedRequired =  computeTime(value,refreshRate);
            if (isUpdatedRequired) {
                timeMap.put(id,new Long(System.currentTimeMillis()));
                clearItemList(id);
            }
            return isUpdatedRequired;
        }
        else {
            timeMap.put(id,new Long(System.currentTimeMillis()));
            return true;
        }
    }

    public boolean computeTime(Long value, String refreshRate) {
        long currentTime = System.currentTimeMillis();
        long computeTime = Long.parseLong(refreshRate) * 60 * 1000 + value.longValue();


        if (currentTime > computeTime) {
            return true;
        }

        return false;

    }

    public void clearItemList(String id) {
        Rss rss = null;
        Map map = new HashMap();
        Set set = null;


        for (Iterator iterator = itemsList.iterator(); iterator.hasNext();) {
             rss = (Rss) iterator.next();
             if (rss.getId().equals(id))
                 map.put(rss,null);
        }

        set = map.keySet();

        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            rss = (Rss) iterator.next();
            itemsList.remove(rss);

        }


    }

    public List getItemsList() {
        return itemsList;
    }

    public void setItemsList(List itemsList) {
        this.itemsList = itemsList;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public Button getBtnUpdate() {
        return btnUpdate;
    }

    public void setBtnUpdate(Button btnUpdate) {
        this.btnUpdate = btnUpdate;
    }
}

