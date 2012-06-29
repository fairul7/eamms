package com.tms.collab.weblog.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 24, 2004
 * Time: 5:16:20 PM
 * To change this template use Options | File Templates.
 */
public class Link extends DefaultDataObject
{
    private String blogId;
    private String name;
    private String url;
    private String folderId;


    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getFolderId()
    {
        return folderId;
    }

    public void setFolderId(String folderId)
    {
        this.folderId = folderId;
    }

}
