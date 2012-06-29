package com.tms.collab.weblog.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.Application;

import java.util.*;
import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 24, 2004
 * Time: 2:00:31 PM
 * To change this template use Options | File Templates.
 */
public class WeblogModule extends DefaultModule
{
    WeblogDao dao;
    public static final String PERMISSION_CREATE_BLOG = "com.tms.collab.weblog.CreateBlog";
    public static final String PERMISSION_MANAGE_BLOG = "com.tms.collab.weblog.ManageBlog";
    public static final String PERMISSION_DELETE_BLOG = "com.tms.collab.weblog.DeleteBlog";

    public void addBlog(Blog blog) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.insertBlog(blog);
        for (Iterator iterator = blog.getCategories().iterator(); iterator.hasNext();)
        {
            Category category = (Category) iterator.next();
            addCategory(category);
        }
    }

    public void addPost(Post post) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.insertPost(post);
    }

    public void updatePost(Post post) throws DaoException
    {
        post.setLastModified(new Date());
        dao = (WeblogDao)getDao();
        dao.updatePost(post);
    }

    public void updateBlog(Blog blog) throws DaoException
    {
        blog.setLastModified(new Date());
        dao = (WeblogDao)getDao();
        dao.updateBlog(blog);
    }

    public void updateLink(Link link) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.updateLink(link);
    }

    public void addComment(Comment comment) throws DaoException
    {
        if(comment.getPostId()!=null||!comment.getPostId().equals("")){
            Post post = getPost(comment.getPostId());
            post.setCommented(true);
            updatePost(post);
            dao = (WeblogDao)getDao();
            dao.insertComment(comment);
        }
    }

    public void addCategory(Category category) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.insertCategory(category);
    }

    public void addLink(Link link) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.insertLink(link);
    }

    public boolean isNameExisted(String name) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.isNameExisted(name);
    }

    public Collection getBlogUsers() throws DaoException{
        dao = (WeblogDao)getDao();
        Collection users = new TreeSet();
        Collection ids = dao.selectBlogUsersId();
        for (Iterator iterator = ids.iterator(); iterator.hasNext();)
        {
            String userId  = (String) ((HashMap)iterator.next()).get("userId");
            try
            {
                Collection blogIds = null;
                Collection blogNames = null;
                String userName = UserUtil.getUser(userId).getUsername();
                Collection col = dao.selectBlogIdName(userId);
                if(col.size()>0){
                    blogIds = new TreeSet();
                    blogNames = new TreeSet();
                    for (Iterator i = col.iterator(); i.hasNext();)
                    {
                        DefaultDataObject defaultDataObject = (DefaultDataObject) i.next();
                        String id = defaultDataObject.getId();
                        String name = (String)defaultDataObject.getProperty("name");
                        blogIds.add(id);
                        blogNames.add(name);
                    }
                }
                BlogUser bUser = new BlogUser();
                bUser.setUserName(userName);
                bUser.setBlogIds(blogIds);
                bUser.setBlogNames(blogNames);
                users.add(bUser);
            } catch (SecurityException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return users;
    }

    public Collection getUserBlogsByAlphabet(String alphabet,String filter, String sort,boolean desc, int sIndex, int rows) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectUserBlogsByAlphabet(alphabet,filter, sort, desc, sIndex,rows);
    }

    public int countUserBlogsByAlphabet(String alphabet,String filter) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.countUserBlogsByAlphabet(alphabet,filter);
    }

    public Blog getBlog(String blogId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        Blog blog = dao.selectBlog(blogId);
        blog.setLinks(dao.selectLinks(blogId));
        blog.setHits(getHits(blogId));
        return blog;
    }

    public Link getLink(String linkId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectLink(linkId);
    }

    public Collection getLinks(String blogId,String filter,String sort,boolean desc,int startIndex, int max) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectLinks(blogId,filter,sort,desc,startIndex,max);
    }

    public int countLinks(String blogId, String filter) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.countLinks(blogId,filter);
    }

    public Collection getLatestBlogs(int max) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectLatestBlogs(max);

    }

    public Collection getLatestPosts(Date toDate,int max) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectLatestPosts(toDate,max);

    }

    public Blog getBlogByName(String name) throws DaoException
    {
        dao = (WeblogDao)getDao();
        Blog blog = dao.selectBlogByName(name);
        if (blog != null) {
            blog.setHits(getHits(blog.getId()));
        }
        return blog;
    }
    public Collection getBlogByUserId(String userId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectBlogByUserId(userId);
    }

    public Post getPost(String postId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectPost(postId);

    }

    public Collection getPosts(String blogId,String filter, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        dao = (WeblogDao)getDao();
        Collection col = dao.selectPosts(blogId,filter,sIndex,maxRow,sort,desc);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Post post = (Post) iterator.next();
            if(post.isCommented()){
                post.setComments(getComments(post.getId()));
            }
        }
        return col;
    }

    public int countPost(String blogId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.countPosts(blogId);

    }

    public Collection getPosts(String blogId,boolean onlyPublished,int max) throws DaoException
    {
        dao = (WeblogDao)getDao();
        Collection col = dao.selectPosts(blogId,onlyPublished,max);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Post post = (Post) iterator.next();
            if(post.isCommented()){
                post.setComments(getComments(post.getId()));
            }
        }
        return col;
    }



    public Collection getPosts(String blogId,Date date,boolean onlyPublished,int max) throws DaoException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        dao = (WeblogDao)getDao();
        Collection col = dao.selectPosts(blogId,cal.getTime(),onlyPublished,max);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Post post = (Post) iterator.next();
            if(post.isCommented()){
                post.setComments(getComments(post.getId()));
            }
        }
        return col;
    }



    public Collection getComments(String postId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectComments(postId);
    }

    public Collection getComments(String postId,String filter, String sort,boolean desc, int sIndex,int max) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectComments(postId,filter,sort,desc,sIndex,max);
    }

    public int countComments(String postId,String filter) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.countComments(postId,filter);
    }

    public Collection getMonthlyArchiveDate(String blogId,Date date) throws DaoException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date startDate = cal.getTime();
        cal.add(Calendar.MONTH,1);
        cal.add(Calendar.SECOND,-1);
        Date endDate = cal.getTime();
        //cal.
        dao = (WeblogDao)getDao();
        Collection col = dao.selectMonthlyArchiveDates(blogId,startDate,endDate);
        Collection dates = new ArrayList();
        Calendar temp_cal = Calendar.getInstance();
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            HashMap hashMap = (HashMap) iterator.next();
            Date pDate = (Date)hashMap.get("publishTime");
            temp_cal.setTime(pDate);
            Integer idate = new Integer(temp_cal.get(Calendar.DAY_OF_MONTH));
            if(!dates.contains(idate))
                dates.add(idate);
        }
        return dates;
    }

    public synchronized void incrementHit(String blogId, Event event) throws DaoException
    {
        dao = (WeblogDao)getDao();
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("blogId",blogId);
        obj.setProperty("ip",event.getRequest().getRemoteAddr());
        obj.setProperty("sessionId",event.getRequest().getSession(true).getId());
        obj.setProperty("date",new Date());
        dao.incrementHit(obj);
    }

    public int getHits(String blogId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectHits(blogId);
    }

    public Collection getMostVisistedBlog(int max) throws DaoException
    {
        dao = (WeblogDao)getDao();
        return dao.selectMostVisitedBlogIds(max);
    }

    public void setPostStatus(String postId,boolean publish) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.updatePostStatus(postId, publish);
    }

    public void deletePost(String postId) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.deletePost(postId);
    }

    public void deleteComment(String id) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.deleteComment(id);
    }

    public void deleteLink(String id) throws DaoException
    {
        dao = (WeblogDao)getDao();
        dao.deleteLink(id);
    }


    public void deleteBlog(Blog blog){
        dao = (WeblogDao)getDao();
    }

    public void deleteBlog(String userId, String blogId) throws SecurityException, DaoException
    {
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        if(ss.hasPermission(userId,PERMISSION_DELETE_BLOG,null,null)){
            dao = (WeblogDao)getDao();
            dao.deleteBlog(blogId);
        }
    }







}
