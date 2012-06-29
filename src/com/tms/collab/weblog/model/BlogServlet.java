package com.tms.collab.weblog.model;

import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 11, 2004
 * Time: 2:09:55 PM
 * To change this template use Options | File Templates.
 */
public class BlogServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        doPost(httpServletRequest,httpServletResponse);
    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        String requestedPath =  httpServletRequest.getPathInfo();
        if(requestedPath==null||requestedPath.trim().length()<=0){
            httpServletResponse.sendRedirect("/cms/weblog.jsp");
            return;
        }
        int index = requestedPath.indexOf("/",1);
        String blogName;
        if(index!=-1)
            blogName = requestedPath.substring(1,index);
        else
            blogName = requestedPath.substring(1,requestedPath.length());
        if(blogName==null||blogName.trim().length()<=0){
            httpServletResponse.sendRedirect("/cms/weblog.jsp");
            return;
        }
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            Blog blog = wm.getBlogByName(blogName);
            if(blog!=null)
                httpServletResponse.sendRedirect("/cms/blogview.jsp?blogId="+blog.getId());
            else
                httpServletResponse.sendRedirect("/cms/blognotfound.jsp");
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            httpServletResponse.sendRedirect("/cms/blognotfound.jsp");
        }
//        super.doPost(httpServletRequest, httpServletResponse);
    }

}
