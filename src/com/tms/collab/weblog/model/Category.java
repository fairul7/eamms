package com.tms.collab.weblog.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 24, 2004
 * Time: 5:17:55 PM
 * To change this template use Options | File Templates.
 */
public class Category extends DefaultDataObject
{
    private String name;
    private String description;
    private String blogId;

    public Category()
    {
    }

    public Category(String name, String description, String blogId)
    {
        this.name = name;
        this.description = description;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }
}
