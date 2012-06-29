package com.tms.collab.weblog.ui;

import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.Blog;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.BlogUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 26, 2004
 * Time: 1:38:48 PM
 * To change this template use Options | File Templates.
 */
public class BlogView extends LightWeightWidget
{
    private String postViewUrl;
    private String blogId;
    private String name;
    private Blog blog;
    private String currentUserId;
    private Date date;
    public BlogView()
    {
    }



    public void onRequest(Event event)
    {
        //super.onRequest(event);

        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        String dateStr = event.getRequest().getParameter("date");
//        event.getRequest().getRemo
        if("delete".equals(event.getRequest().getParameter("et"))&&blogId!=null&&!blogId.equals("")){
            SecurityService  ss = (SecurityService )Application.getInstance().getService(SecurityService .class);
            String userId = ss.getCurrentUser(event.getRequest()).getId();
            try
            {
                if(ss.hasPermission(userId,WeblogModule.PERMISSION_DELETE_BLOG,null,null)){
                    wm.deleteBlog(userId,blogId);
                }
            } catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            } finally{
                    blog = null;
                    return;
            }

        }
        if(dateStr!=null&&!dateStr.equals("")){
			date = BlogUtil.parseDateUrl(dateStr);
        } else{
            date = new Date();
        }
        if(blogId!=null&&!blogId.equals("")){
            try
            {
                blog = wm.getBlog(blogId);
                if(blog!=null){
                    blog.setPosts(wm.getPosts(blogId,date,true,20));
                    wm.incrementHit(blogId,event);
                }
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        SecurityService  ss = (SecurityService )Application.getInstance().getService(SecurityService .class);
        currentUserId = ss.getCurrentUser(event.getRequest()).getId();
    }


    public String getDefaultTemplate()
    {
        return getTemplate();
    }

    public String getTemplate()
    {
        return "weblog/blogview";
    }

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

    public Blog getBlog()
    {
        return blog;
    }

    public void setBlog(Blog blog)
    {
        this.blog = blog;
    }

    public String getCurrentUserId()
    {
        return currentUserId;
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
