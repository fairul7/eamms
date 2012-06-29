package com.tms.cms.syndication.rss;

import org.xml.sax.SAXException;
import com.tms.cms.syndication.rss.digester.Channel;
import com.tms.cms.syndication.rss.digester.Item;
import com.tms.cms.syndication.rss.digester.RSSDigester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RssManager {
    private static RssManager rssManager;

    public static RssManager getInstance() {
        if (rssManager == null) {
            synchronized (RssManager.class) {
                if (rssManager == null) {
                    rssManager = new RssManager();
                }
            }
        }

        return rssManager;
    }

    private RssManager() {
    }

    /**
     * Updates a feed by connecting to the feed's URL.
     * 
     * @param feed
     * @throws IOException
     * @throws SAXException
     */
    public void retrieveRssFeed(RssFeed feed) throws IOException, SAXException {
        RSSDigester digester;
        URL u;
        InputStream in;
        Channel channel;
        RssItem rssItem;
        Item[] items;


        // get the RSS channel via URL
        digester = new RSSDigester();
        u = new URL(feed.getUrl());
        in = u.openStream();
        channel = (Channel) digester.parse(in);
        in.close();

        if(channel==null) {
            // TODO: throw exception here??
            // TODO: probaby cannot parse
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Channel is null");
            return;
        }


        // update rssFeed data
        feed.setTitle(channel.getTitle());
        feed.setDescription(channel.getDescription());
        feed.setFormat(Double.toString(channel.getVersion()));
        feed.setLanguage(channel.getLanguage());
        feed.setCopyright(channel.getCopyright());

        // update rssFeed's image information
        if (channel.getImage() != null) {
            feed.setImageHeight(channel.getImage().getHeight());
            feed.setImageWidth(channel.getImage().getWidth());
            feed.setImageLink(channel.getImage().getLink());
            feed.setImageTitle(channel.getImage().getTitle());
            feed.setImageUrl(channel.getImage().getURL());
        }

        // add rssFeed's items
        items = channel.getItems();
        
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            rssItem = new RssItem();
            rssItem.setRssFeed(feed);
            rssItem.setTitle(item.getTitle());
            rssItem.setDescription(item.getDescription());
            rssItem.setLink(item.getLink());
            rssItem.setPubDate(item.getPubDate());

            feed.getRssItemList().add(rssItem);
        }

        // update rssFeed's last update timestamp
        feed.setLastUpdateDate(Calendar.getInstance().getTime());


        // TODO: write to disk
    }

}