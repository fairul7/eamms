package com.tms.collab.weblog.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 24, 2004
 * Time: 3:48:00 PM
 * To change this template use Options | File Templates.
 */
public class WeblogDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE weblog_blog ("+
        "id varchar(255) NOT NULL default '0',"+
                "userId varchar(255) NOT NULL default '0', "+
                "hits int(255) default '0', "+
                "title varchar(255) default '0', "+
                "description text,   "+
                "allowComments char(1) binary default '0', "+
                "creationDate datetime default NULL,  "+
                "lastModified datetime default NULL,  "+
                "name varchar(255) NOT NULL default '',  "+
                "userName varchar(255) default NULL,  "+
                "UNIQUE KEY id (id)   "+
        ") TYPE=MyISAM;",null);

        super.update("CREATE TABLE weblog_category ("+
        "name varchar(255) default '0', "+
                "description varchar(255) default '0', "+
                "id varchar(255) default '0',"+
                "blogId varchar(255) default NULL"+
        ") TYPE=MyISAM; ",null);

        super.update("CREATE TABLE weblog_comment ( "+
                "id varchar(255) default '0',"+
                "postId varchar(255) default '0', "+
               "comment text,  "+
                "date datetime default NULL,"+
                "userName varchar(255) default '0',"+
                "userId varchar(255) default '0',"+
                "email varchar(255) default '0',"+
                "url varchar(255) default '0' "+
        ") TYPE=MyISAM;",null);

        super.update("CREATE TABLE weblog_hit ("+
        "blogId varchar(255) default '0',"+
                "ip varchar(255) default '0',"+
                "sessionId varchar(255) default NULL,"+
                "date datetime default NULL "+
        ") TYPE=MyISAM;",null);

        super.update("CREATE TABLE weblog_link ("+
                "id varchar(255) default NULL,"+
                "name varchar(255) default '0',"+
                "url varchar(255) default '0',"+
                "blogId varchar(255) default '0',"+
                "folderId varchar(255) default '0'"+
        ") TYPE=MyISAM;",null);

        super.update("CREATE TABLE weblog_post (" +
                "id varchar(255) default '0'," +
                "blogId varchar(255) default '0'," +
                "userId varchar(255) default '0'," +
                "categoryId varchar(255) default NULL," +
                "title varchar(255) default NULL," +
                "contents text," +
                "lastModified datetime default NULL," +
                "commented char(1) binary default NULL," +
                "publishTime datetime default NULL," +
                "published char(1) binary default NULL)" +
                " TYPE=MyISAM;",null);

    }

    public void insertBlog(Blog blog) throws DaoException
    {
        String sql = "INSERT INTO weblog_blog(id,name,userId,hits,title,description,allowComments,creationDate,lastModified,userName) " +
                "VALUES(#id#,#name#,#userId#,#hits#,#title#,#description#,#allowComments#,#creationDate#,#lastModified#,#userName#)" ;
        super.update(sql,blog);
    }

    public void updateBlog(Blog blog) throws DaoException
    {
        String sql = "UPDATE weblog_blog SET name=#name#,title=#title#,description=#description#,allowComments=#allowComments#,lastModified=#lastModified#,userName=#userName# " +
                "WHERE id=#id#";
        super.update(sql,blog);
    }

    public void updateLink(Link link) throws DaoException
    {
        String sql = "UPDATE weblog_link SET name=#name#,url=#url# WHERE id=#id#";
        super.update(sql,link);
    }

    public void insertPost(Post post) throws DaoException
    {
        String sql = "INSERT INTO weblog_post(id,blogId,userId,categoryId,title,contents,publishTime,lastModified,commented,published) " +
                "VALUES(#id#,#blogId#,#userId#,#categoryId#,#title#,#contents#,#publishTime#,#lastModified#,#commented#,#published#)" ;
        super.update(sql,post);
    }

    public void updatePost(Post post) throws DaoException
    {
        String sql = "UPDATE weblog_post SET title=#title#,contents=#contents#,publishTime=#publishTime#,lastModified=#lastModified#,published=#published#,commented=#commented# " +
                "WHERE id=#id#";
        super.update(sql,post);
    }
    public void updatePostStatus(String postId,boolean publish) throws DaoException
    {
        DefaultDataObject dao = new DefaultDataObject();
        dao.setId(postId);
        dao.setProperty("published",new Boolean(publish));
        String sql = "UPDATE weblog_post SET published=#published# " +
                "WHERE id=#id#";
        super.update(sql,dao);
    }



    public void insertComment(Comment comment) throws DaoException
    {
        String sql = "INSERT INTO weblog_comment(id,postId,comment,date,userName,userId,email,url) " +
                "VALUES(#id#,#postId#,#comment#,#date#,#userName#,#userId#,#email#,#url#)" ;
        super.update(sql,comment);
    }

    public void insertCategory(Category category) throws DaoException
    {
        String sql = "INSERT INTO weblog_category(id,blogId,name,description) " +
                "VALUES(#id#,#blogId#,#name#,#description#)" ;
        super.update(sql,category);
    }

    public void insertLink(Link link) throws DaoException
    {
        String sql = "INSERT INTO weblog_link(id,blogId,name,url,folderId) " +
                "VALUES(#id#,#blogId#,#name#,#url#,#folderId#)" ;
        super.update(sql,link);
    }
    public void incrementHit(DefaultDataObject object) throws DaoException
    {
        String sql = "INSERT INTO weblog_hit(blogId,ip,sessionId,date) " +
                "VALUES(#blogId#,#ip#,#sessionId#,#date#)" ;
        super.update(sql,object);
    }

    public int selectHits(String blogId) throws DaoException
    {
        String sql = "SELECT COUNT(DISTINCT sessionId) as total FROM weblog_hit " +
                "WHERE blogId=?";
        Map map = (Map)super.select(sql,HashMap.class,new Object[]{blogId},0,-1).iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public int countPosts(String blogId) throws DaoException
    {
        String sql = "SELECT COUNT(DISTINCT id) as total FROM weblog_post " +
                "WHERE blogId=?";
        Map map = (Map)super.select(sql,HashMap.class,new Object[]{blogId},0,-1).iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }


    public Collection selectMostVisitedBlogIds(int max) throws DaoException
    {
        String sql = "select count(DISTINCT sessionId) as hits, blogId as blogId from weblog_hit "+
        "group by blogId order by hits desc ";
       Collection col = super.select(sql,DefaultDataObject.class,null,0,max);
       Collection blogs = new ArrayList();
       for (Iterator iterator = col.iterator(); iterator.hasNext();)
       {
           DefaultDataObject defaultDataObject = (DefaultDataObject) iterator.next();
           String blogId = (String)defaultDataObject.getProperty("blogId");
           Blog blog = selectBlog(blogId);
           blog.setHits(((Number)defaultDataObject.getProperty("hits")).intValue());
           blogs.add(blog);
       }
        return blogs;
        //return Integer.parseInt(map.get("total").toString());
    }

    public boolean isNameExisted(String name) throws DaoException
    {
        String sql = "SELECT COUNT(*) AS total FROM weblog_blog WHERE name=?";
        Map map = (Map) super.select(sql,HashMap.class,new Object[]{name},0,-1).iterator().next();
        return Integer.parseInt(map.get("total").toString())>0;
    }

    public Blog selectBlog(String blogId) throws DaoException
    {
        String sql = "SELECT id,userId,hits,title,description,allowComments,creationDate,lastModified,name,userName" +
                " FROM weblog_blog WHERE id=? ";
        Collection col = super.select(sql,Blog.class,new Object[]{blogId},0,-1);
        if(col.size()>0)
            return (Blog)col.iterator().next();
        return null;
    }

    public Collection selectUserBlogsByAlphabet(String alphabet,String filter, String sort, boolean desc, int startIndex, int rows) throws DaoException
    {
        filter = (filter==null)?"%": "%"+filter+"%";
        String sortStr = (sort==null?"": " ORDER BY "+sort + (desc?" DESC":""));
        String sql = "SELECT id,userId,hits,title,description,allowComments,creationDate,lastModified,name,userName" +
                " FROM weblog_blog WHERE userName LIKE ? AND (userName LIKE ? OR title LIKE ? OR description LIKE ? OR name LIKE ?) " + sortStr;
        return super.select(sql,Blog.class,new Object[]{alphabet+"%",filter, filter,filter,filter},startIndex,rows);
    }
    public int countUserBlogsByAlphabet(String alphabet,String filter) throws DaoException
    {
        filter = (filter==null)?"%": "%"+filter+"%";
        String sql = "SELECT COUNT(id) as total " +
                " FROM weblog_blog WHERE userName LIKE ? AND (userName LIKE ? OR title LIKE ? OR description LIKE ? OR name LIKE ?) ";
        Map map = (Map)super.select(sql,HashMap.class,new Object[]{alphabet+"%",filter, filter,filter,filter},0,-1).iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Collection selectLatestBlogs(int max) throws DaoException
    {
        String sql = "SELECT id,userId,title,description,allowComments,creationDate,lastModified,name,userName" +
                " FROM weblog_blog ORDER BY creationDate DESC ";
        return super.select(sql,Blog.class,null,0, max);
    }

    public Collection selectBlogByUserId(String userId) throws DaoException
    {
        String sql = "SELECT id,userId,hits,title,description,allowComments,creationDate,lastModified,name,userName" +
                " FROM weblog_blog WHERE userId=? ";
        return  super.select(sql,Blog.class,new Object[]{userId},0,-1);
/*
        if(col.size()>0)
            return (Blog)col.iterator().next();
        return null;
*/
    }

    public Blog selectBlogByName(String name) throws DaoException
    {
        String sql = "SELECT id,userId,hits,title,description,allowComments,creationDate,lastModified,name,userName" +
                " FROM weblog_blog WHERE name=? ";
        Collection col = super.select(sql,Blog.class,new Object[]{name},0,-1);
        if(col.size()>0)
            return (Blog)col.iterator().next();
        return null;
    }
    public Collection selectLatestPosts(Date date, int max) throws DaoException
    {
  //      String sql = "SELECT id,userId,title,description,allowComments,creationDate,lastModified,name" +
 //               " FROM weblog_blog ORDER BY creationDate DESC limit 0,"+max;
        String sql = "SELECT id,blogId,userId,categoryId,title,contents,publishTime,published,lastModified,commented" +
                " FROM weblog_post WHERE publishTime<=? AND published='1' ORDER BY publishTime DESC ";
        return super.select(sql,Post.class,new Object[]{date},0,max);
    }


    public Link selectLink(String linkId) throws DaoException
    {
        String sql = "SELECT id, name,url,blogId,folderId FROM weblog_link WHERE id=?";
        Collection col =  super.select(sql,Link.class,new Object[]{linkId},0,-1);
        if(col!=null&&col.size()>0)
            return (Link)col.iterator().next();
        return null;
    }
    public Collection selectLinks(String blogId) throws DaoException
    {
        String sql = "SELECT id, name,url,blogId,folderId FROM weblog_link WHERE blogId=?";
        return super.select(sql,Link.class,new Object[]{blogId},0,-1);
    }

    public Collection selectLinks(String blogId,String filter,String sort, boolean desc, int sIndex, int max) throws DaoException
    {
        String sortStr = sort==null? "": "ORDER BY "+sort + (desc?" DESC":"");
        String sql = "SELECT id, name,url,blogId,folderId FROM weblog_link WHERE " +
                "blogId=? AND (name LIKE ? OR url LIKE ?) " +sortStr;
        filter = (filter != null) ? "%" + filter + "%" : "%";

        return super.select(sql,Link.class,new Object[]{blogId,filter,filter},sIndex,max);
    }

    public int countLinks(String blogId, String filter) throws DaoException
    {
        String sql = "SELECT COUNT(id) as total FROM weblog_link WHERE blogId=? AND ( name LIKE ? OR url LIKE ? )";
        filter = (filter != null) ? "%" + filter + "%" : "%";
        Map map = (Map)super.select(sql,HashMap.class,new Object[]{blogId,filter,filter},0,-1).iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }


/*
    public Collection selectLinks(String blogId){

    }
*/



    public Post selectPost(String postId) throws DaoException
    {
        String sql = "SELECT id,blogId,userId,categoryId,title,contents,publishTime,published,lastModified,commented" +
                " FROM weblog_post WHERE id = ? ";
        Collection col = super.select(sql,Post.class,new Object[]{postId},0,-1);
        if(col!=null&&col.size()>0)
            return (Post)col.iterator().next();
        return null;
    }

    public Collection selectPosts(String blogId,String filter, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        String sortStr = sort==null?"" : " ORDER BY "+sort + (desc?" DESC":"");
        String sql = "SELECT id,blogId,userId,categoryId,title,contents,publishTime,published,lastModified,commented" +
                " FROM weblog_post WHERE blogId = ? AND (title LIKE ? OR "
                +"contents LIKE ?) "
                +sortStr;
        filter = (filter != null) ? "%" + filter + "%" : "%";
        return  super.select(sql,Post.class,new Object[]{blogId,filter,filter},sIndex,maxRow);
    }


    public Collection selectPosts(String blogId,boolean onlyPublished,int max) throws DaoException
    {
        String sql = "SELECT id,blogId,userId,categoryId,title,contents,publishTime,published,lastModified,commented" +
                " FROM weblog_post WHERE blogId = ? " +
                (onlyPublished?"AND published='1' " : "") +
                " ORDER BY publishTime DESC ";
        return  super.select(sql,Post.class,new Object[]{blogId},0,max);
    }

    public Collection selectPosts(String blogId,Date date, boolean onlyPublished,int max) throws DaoException
    {
        String sql = "SELECT id,blogId,userId,categoryId,title,contents,publishTime,published,lastModified,commented" +
                " FROM weblog_post WHERE blogId = ? AND publishTime<=? " +
                (onlyPublished?"AND published='1' " : "") +
                " ORDER BY publishTime DESC ";
        return  super.select(sql,Post.class,new Object[]{blogId,date},0,max);
    }

    public Collection selectPosts(String blogId,Date date, boolean onlyPublished) throws DaoException
    {
        String sql = "SELECT id,blogId,userId,categoryId,title,contents,publishTime,published,lastModified,commented" +
                " FROM weblog_post WHERE blogId = ? AND publishTime<=? " +
                (onlyPublished?"AND published='1' " : "") +
                " ORDER BY publishTime DESC ";
        return  super.select(sql,Post.class,new Object[]{blogId,date},0,-1);
    }


    public int countComments(String postId,String filter) throws DaoException
    {
        String sql = "SELECT COUNT(id) as total FROM weblog_comment WHERE postId=? AND comment LIKE ?";
        filter = (filter != null) ? "%" + filter + "%" : "%";
        Map map = (Map)super.select(sql,HashMap.class,new Object[]{postId,filter},0,-1).iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Collection selectComments(String postId) throws DaoException
    {
        String sql = "SELECT id,postId,comment,date,userName,userId,email,url " +
                "FROM weblog_comment WHERE postId=? ORDER BY date";
        return super.select(sql,Comment.class,new Object[]{postId},0,-1);

    }

    public Collection selectComments(String postId,String filter,String sort,boolean desc,int sIndex,int max) throws DaoException
    {
        String sortStr =(sort==null?"":" ORDER BY "+sort+ (desc?" DESC":""));

        String sql = "SELECT id,postId,comment,date,userName,userId,email,url " +
                "FROM weblog_comment WHERE postId=? and comment LIKE ?"+sortStr;
        filter = (filter != null) ? "%" + filter + "%" : "%";
        return super.select(sql,Comment.class,new Object[]{postId,filter},sIndex,max);

    }


    public Collection selectBlogUsersId() throws DaoException
    {
        String sql = "SELECT DISTINCT userId as userId FROM weblog_blog";
        return super.select(sql,HashMap.class,null,0,-1);
    }

    public Collection selectBlogIdName(String userId) throws DaoException
    {
        String sql = "SELECT id as id,name as name FROM weblog_blog WHERE userId = ?";
        return super.select(sql,DefaultDataObject.class,new Object[]{userId},0,-1);
    }

    public Collection selectMonthlyArchiveDates(String blogId,Date startDate, Date endDate) throws DaoException
    {
        String sql = "SELECT publishTime as publishTime from weblog_post WHERE blogId = ? AND publishTime>=? AND publishTime<=? AND published='1'";
        return super.select(sql,HashMap.class,new Object[]{blogId,startDate,endDate},0,-1);
    }

    public void deleteBlog(String blogId) throws DaoException
    {
        DefaultDataObject dao = new DefaultDataObject();
        dao.setId(blogId);
        Collection col = selectPosts(blogId,null,0,-1,null,false);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Post post = (Post) iterator.next();
            deletePost(post.getId());
        }
        String sql = "DELETE FROM weblog_hit WHERE blogId=#id#";
        super.update(sql,dao);
        sql = "DELETE FROM weblog_link WHERE blogId=#id#";
        super.update(sql,dao);
        sql = "DELETE FROM weblog_category WHERE blogId=#id#";
        super.update(sql,dao);
        sql = "DELETE FROM weblog_blog WHERE id=#id#";
        super.update(sql,dao);
    }

    public void deletePost(String postId) throws DaoException
    {
        String sql = "DELETE FROM weblog_post WHERE id=#id#";
        DefaultDataObject dao = new DefaultDataObject();
        dao.setId(postId);
        super.update(sql,dao);
        sql = "DELETE FROM weblog_comment WHERE postId=#id#";
        super.update(sql,dao);
    }

    public void deleteComment(String id) throws DaoException
    {
        String sql = "DELETE FROM weblog_comment WHERE id=#id#";
        DefaultDataObject dao = new DefaultDataObject();
        dao.setId(id);
        super.update(sql,dao);
    }

    public void deleteLink(String id) throws DaoException
    {
        String sql = "DELETE FROM weblog_link WHERE id=#id#";
        DefaultDataObject dao = new DefaultDataObject();
        dao.setId(id);
        super.update(sql,dao);
    }






}
