package com.tms.collab.weblog.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;
import kacang.services.security.SecurityException;

import java.util.Collection;
import java.util.Date;

import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 24, 2004
 * Time: 3:04:51 PM
 * To change this template use Options | File Templates.
 */
public class Post extends DefaultDataObject
{

    private String title;
    private String contents;
    private Date publishTime;
    private Date lastModified;
    private String userId;
    private String blogId;
    private Collection comments;
    private String categoryId;
    private boolean commented;
    private boolean published;

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }

    public Collection getComments()
    {
        return comments;
    }

    public void setComments(Collection comments)
    {
        this.comments = comments;
    }

    public boolean isCommented()
    {
        return commented;
    }

    public void setCommented(boolean commented)
    {
        this.commented = commented;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setPublished(boolean published)
    {
        this.published = published;
    }

    public int getTotalComments(){
        if(commented&&comments!=null)
            return comments.size();
        return 0;
    }

    public String getUserName(){
        try
        {
            return UserUtil.getUser(userId).getUsername();
        } catch (SecurityException e)
        {
            return "{Deleted User}";
        }
    }
}
