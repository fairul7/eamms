package com.tms.collab.weblog.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;

import java.util.Collection;

import com.tms.collab.weblog.model.WeblogModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 1, 2004
 * Time: 6:35:00 PM
 * To change this template use Options | File Templates.
 */
public class LatestBlogs extends LightWeightWidget
{
    private int count;
    private Collection blogs;
    private String blogViewUrl;

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(count<=0){
            count = 20;
        }
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            blogs = wm.getLatestBlogs(count);
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
    }

    public String getDefaultTemplate()
    {
        return "weblog/latestblogs";
    }

    public String getTemplate()
    {
        return getDefaultTemplate();
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public Collection getBlogs()
    {
        return blogs;
    }

    public void setBlogs(Collection blogs)
    {
        this.blogs = blogs;
    }

    public String getBlogViewUrl()
    {
        return blogViewUrl;
    }

    public void setBlogViewUrl(String blogViewUrl)
    {
        this.blogViewUrl = blogViewUrl;
    }


}
