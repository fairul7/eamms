package com.tms.collab.weblog.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;

import java.util.Collection;
import java.util.Date;

import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 1, 2004
 * Time: 6:35:08 PM
 * To change this template use Options | File Templates.
 */
public class LatestPosts extends LightWeightWidget
{
    private Collection posts;
    private int count;
    private String postViewUrl;

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(count<=0){
            count = 20;
        }
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            posts = wm.getLatestPosts(new Date(),count);
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

    }

    public String getDefaultTemplate()
    {
        return "weblog/latestposts";
    }

    public String getTemplate()
    {
        return getDefaultTemplate();
    }

    public Collection getPosts()
    {
        return posts;
    }

    public void setPosts(Collection posts)
    {
        this.posts = posts;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getPostViewUrl()
    {
        return postViewUrl;
    }

    public void setPostViewUrl(String postViewUrl)
    {
        this.postViewUrl = postViewUrl;
    }



}
