package com.tms.collab.weblog.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;

import java.util.Collection;
import java.util.Date;

import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 24, 2004
 * Time: 2:00:09 PM
 * To change this template use Options | File Templates.
 */
public class Blog extends DefaultDataObject
{
    private String name;
    private String userId;
    private String userName;
    private Collection links;
    private Collection posts;
    private Collection categories;
    private int hits;
    private String title;
    private String description;
    private boolean allowComments;
    private Date creationDate;
    private Date lastModified;
    private Collection userIds;
    private Collection groupIds;

    public Collection getLinks()
    {
        return links;
    }

    public void setLinks(Collection links)
    {
        this.links = links;
    }

    public Collection getPosts()
    {
        return posts;
    }

    public void setPosts(Collection posts)
    {
        this.posts = posts;
    }

    public Collection getCategories()
    {
        return categories;
    }

    public void setCategories(Collection categories)
    {
        this.categories = categories;
    }

    public int getHits()
    {
        return hits;
    }

    public void setHits(int hits)
    {
        this.hits = hits;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isAllowComments()
    {
        return allowComments;
    }

    public void setAllowComments(boolean allowComments)
    {
        this.allowComments = allowComments;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName() throws SecurityException
    {
        if(userName==null||userName==""){
            if(userId==null)
                return null;
            return UserUtil.getUser(userId).getUsername();
        }
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Collection getUserIds()
    {
        return userIds;
    }

    public void setUserIds(Collection userIds)
    {
        this.userIds = userIds;
    }

    public Collection getGroupIds()
    {
        return groupIds;
    }

    public void setGroupIds(Collection groupIds)
    {
        this.groupIds = groupIds;
    }


}
