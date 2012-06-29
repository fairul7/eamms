package com.tms.cms.syndication.rss;

import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * A feed contains a set of RssItem.
 */
public class RssFeed {
    private String name;
    private String category;

    private String url;
    private boolean active;

    private boolean overrideUpdateInterval;
    private long updateInterval;

    private boolean overrideDaysToStoreOldHeadlines;
    private int daysToStoreOldHeadlines;

    private Date lastUpdateDate;
    private Date nextUpdateDate;

    private String format;

    // required by rss 2.0
    private String title;
    private String link;
    private String description;

    // optional by rss 2.0
    private String language;
    private String copyright;
    private String managingEditor;
    private String webMaster;
    private String pubDate;
    private String lastBuildDate;
    private String generator;
    private String docs;

    private String imageTitle;
    private String imageLink;
    private int imageWidth;
    private int imageHeight;
    private String imageUrl;

    private List rssItemList;

    public RssFeed() {
        rssItemList = new LinkedList();
    }

    public String getFormattedString() {
        StringBuffer sb = new StringBuffer();

        sb.append("RssFeed\n");
        sb.append("=======\n");
        sb.append("Title          : " + title + "\n");
        sb.append("Link           : " + link + "\n");
        sb.append("Description    : " + description + "\n");
        sb.append("Language       : " + language + "\n");
        sb.append("Copyright      : " + copyright + "\n");
        sb.append("ManagingEditor : " + managingEditor + "\n");
        sb.append("WebMaster      : " + webMaster + "\n");
        sb.append("PubDate        : " + pubDate + "\n");
        sb.append("LastBuildDate  : " + lastBuildDate + "\n");
        sb.append("Generator      : " + generator + "\n");
        sb.append("Docs           : " + docs + "\n");
        sb.append("ImageTitle     : " + imageTitle + "\n");
        sb.append("ImageLink      : " + imageLink + "\n");
        sb.append("ImageWidth     : " + imageWidth + "\n");
        sb.append("ImageHeight    : " + imageHeight + "\n");
        sb.append("ImageUrl       : " + imageUrl + "\n");

        sb.append("rssItemList\n");
        sb.append("----------\n");
        for (Iterator iterator = rssItemList.iterator(); iterator.hasNext();) {
            RssItem rssItem = (RssItem) iterator.next();
            sb.append(rssItem.getFormattedString());
            sb.append("-----\n");
        }

        return sb.toString();
    }


    // === [ getters/setters ] =================================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOverrideUpdateInterval() {
        return overrideUpdateInterval;
    }

    public void setOverrideUpdateInterval(boolean overrideUpdateInterval) {
        this.overrideUpdateInterval = overrideUpdateInterval;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    public boolean isOverrideDaysToStoreOldHeadlines() {
        return overrideDaysToStoreOldHeadlines;
    }

    public void setOverrideDaysToStoreOldHeadlines(boolean overrideDaysToStoreOldHeadlines) {
        this.overrideDaysToStoreOldHeadlines = overrideDaysToStoreOldHeadlines;
    }

    public int getDaysToStoreOldHeadlines() {
        return daysToStoreOldHeadlines;
    }

    public void setDaysToStoreOldHeadlines(int daysToStoreOldHeadlines) {
        this.daysToStoreOldHeadlines = daysToStoreOldHeadlines;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
    }

    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List getRssItemList() {
        return rssItemList;
    }

    public void setRssItemList(List rssItemList) {
        this.rssItemList = rssItemList;
    }

}
