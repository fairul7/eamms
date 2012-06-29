package com.tms.collab.weblog.model;

import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 3, 2004
 * Time: 3:53:17 PM
 * To change this template use Options | File Templates.
 */
public class BlogUtil
{
	public static String DATE_URL_FORMAT = "yyyyMMdd";
	
    public static boolean hasEditPostPermission(String userId,String postId){
        if(postId==null||postId.equals(""))
            return false;
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        Post post;
        try
        {
            post = wm.getPost(postId);
            if(userId.equals(post.getUserId())){
                return true;
            }
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            if(ss.hasPermission(userId,WeblogModule.PERMISSION_MANAGE_BLOG,null,null)){
                return true;
            }
            Blog blog = wm.getBlog(post.getBlogId());
            if(blog.getUserIds()!=null&&blog.getUserIds().contains(userId))
                return true;
            Collection col = ss.getUserGroups(userId);
            for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                Group group = (Group) iterator.next();
                if(blog.getGroupIds()!=null&&blog.getGroupIds().contains(group.getId()))
                    return true;
            }
        } catch (DaoException e)
        {
            Log.getLog(BlogUtil.class).error(e);  //To change body of catch statement use Options | File Templates.
        } catch (SecurityException e)
        {
            Log.getLog(BlogUtil.class).error(e);  //To change body of catch statement use Options | File Templates.
        }

        return false;

    }

    public static boolean hasEditLinkPermission(String userId,String blogId){
        if(blogId==null||blogId.equals(""))
            return false;
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            if(ss.hasPermission(userId,WeblogModule.PERMISSION_MANAGE_BLOG,null,null)){
                return true;
            }
            Blog blog = wm.getBlog(blogId);
            if(blog.getUserId().equals(userId))
                return true;
            if(blog.getUserIds()!=null&&blog.getUserIds().contains(userId))
                return true;
            Collection col = ss.getUserGroups(userId);
            for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                Group group = (Group) iterator.next();
                if(blog.getGroupIds()!=null&&blog.getGroupIds().contains(group.getId()))
                    return true;
            }
        } catch (DaoException e)
        {
            Log.getLog(BlogUtil.class).error(e);  //To change body of catch statement use Options | File Templates.
        } catch (SecurityException e)
        {
            Log.getLog(BlogUtil.class).error(e);  //To change body of catch statement use Options | File Templates.
        }

        return false;

    }
	
    public static boolean hasEditBlogPermission(String userId,String blogId){
        if(blogId==null||blogId.equals(""))
            return false;
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            if(ss.hasPermission(userId,WeblogModule.PERMISSION_MANAGE_BLOG,null,null)){
                return true;
            }
            Blog blog = wm.getBlog(blogId);
            if(blog.getUserId().equals(userId))
                return true;
            if(blog.getUserIds()!=null&&blog.getUserIds().contains(userId))
                return true;
            Collection col = ss.getUserGroups(userId);
            for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                Group group = (Group) iterator.next();
                if(blog.getGroupIds()!=null&&blog.getGroupIds().contains(group.getId()))
                    return true;
            }
        } catch (DaoException e)
        {
            Log.getLog(BlogUtil.class).error(e);  //To change body of catch statement use Options | File Templates.
        } catch (SecurityException e)
        {
            Log.getLog(BlogUtil.class).error(e);  //To change body of catch statement use Options | File Templates.
        }

        return false;

    }
	
	public static String formatDateUrl(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_URL_FORMAT);
		return sdf.format(date);
	}
	
	public static Date parseDateUrl(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_URL_FORMAT);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
