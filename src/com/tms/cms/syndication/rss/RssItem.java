package com.tms.cms.syndication.rss;

public class RssItem {
    private String title;
    private String link;
    private String description;
    private String author;
    private String comments;
    private String guid;
    private String pubDate;

    private boolean read;

    private RssFeed rssFeed;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RssItem)) return false;

        final RssItem rssItem = (RssItem) o;

        if (guid != null ? !guid.equals(rssItem.guid) : rssItem.guid != null) return false;
        if (rssFeed != null ? !rssFeed.equals(rssItem.rssFeed) : rssItem.rssFeed != null) return false;
        if (title != null ? !title.equals(rssItem.title) : rssItem.title != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (title != null ? title.hashCode() : 0);
        result = 29 * result + (guid != null ? guid.hashCode() : 0);
        result = 29 * result + (rssFeed != null ? rssFeed.hashCode() : 0);
        return result;
    }

    public String getFormattedString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Title       : " + title + "\n");
        sb.append("Link        : " + link + "\n");
        sb.append("Description : " + description + "\n");
        sb.append("Author      : " + author + "\n");
        sb.append("Comments    : " + comments + "\n");
        sb.append("Guid        : " + guid + "\n");
        sb.append("PubDate     : " + pubDate + "\n");

        return sb.toString();
    }

    // === [ getters/setters ] =================================================
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public RssFeed getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(RssFeed rssFeed) {
        this.rssFeed = rssFeed;
    }
}
