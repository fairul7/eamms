package com.tms.collab.weblog.model;

import kacang.services.security.User;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 26, 2004
 * Time: 4:14:18 PM
 * To change this template use Options | File Templates.
 */
public class BlogUser implements Comparable
{
    private String userName;
    private Collection blogIds;
    private Collection blogNames;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Collection getBlogNames()
    {
        return blogNames;
    }

    public void setBlogNames(Collection blogNames)
    {
        this.blogNames = blogNames;
    }


    public Collection getBlogIds()
    {
        return blogIds;
    }

    public void setBlogIds(Collection blogIds)
    {
        this.blogIds = blogIds;
    }

    public int compareTo(Object o)
    {
        if(o instanceof BlogUser) {
            return userName.compareTo(((BlogUser)o).getUserName());
        }
        return -1;
    }
}
