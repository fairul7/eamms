package com.tms.collab.forum.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.util.JdbcUtil;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.services.security.Group;
import kacang.services.security.User;
import kacang.Application;
import org.apache.commons.collections.SequencedHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ForumDao extends DataSourceDao
{
    public void init() throws DaoException
    {
    	/* Alter Tables */
        try {
            super.update("ALTER TABLE frm_message ADD email VARCHAR(250)",null);
           
        }
        catch(Exception e) {}
        try {
        	super.update("ALTER TABLE frm_thread ADD email VARCHAR(250)",null);
           
        }
        catch(Exception e) {}
        
        super.update("CREATE TABLE frm_forum_subscription(forumId VARCHAR(250) NOT NULL, userId VARCHAR(250) NOT NULL, PRIMARY KEY(forumId, userId))", null);
        super.update("CREATE TABLE frm_forum_settings(userId VARCHAR(250) NOT NULL, forumType CHAR(1) NOT NULL, PRIMARY KEY(userId))", null);
        super.update("CREATE TABLE frm_forum (forumId VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, description TEXT, creationDate DATETIME NOT NULL, modificationDate DATETIME NOT NULL, ownerId VARCHAR(255) NOT NULL, isPublic CHAR(1) NOT NULL DEFAULT '1', active CHAR(1) NOT NULL DEFAULT '1', assigneeId VARCHAR(255), category VARCHAR(250), PRIMARY KEY (forumId), INDEX frm_forum_name_idx  (name(10)))", null);
        super.update("CREATE TABLE frm_thread (threadId VARCHAR(255) NOT NULL, forumId VARCHAR(255) NOT NULL, subject VARCHAR(255), content TEXT, creationDate DATETIME NOT NULL, modificationDate DATETIME NOT NULL, ownerId VARCHAR(255) NOT NULL, email VARCHAR(255), isPublic CHAR(1) NOT NULL DEFAULT '1', active CHAR(1) NOT NULL DEFAULT '1', PRIMARY KEY (threadId), INDEX frm_thread_forumId_idx (forumId), INDEX frm_thread_cDate_idx (creationDate), INDEX frm_thread_mDate_idx (modificationDate))", null);
        super.update("CREATE TABLE frm_message (messageId VARCHAR(255) NOT NULL, parentMessageId VARCHAR(255) NULL, threadId VARCHAR(255) NOT NULL, forumId VARCHAR(255) NOT NULL, ownerId VARCHAR(255) NOT NULL, email VARCHAR(255), subject VARCHAR(255), content TEXT, creationDate DATETIME NOT NULL, modificationDate DATETIME NOT NULL, PRIMARY KEY (messageId), INDEX frm_message_forumId_idx (forumId), INDEX frm_message_threadId_idx (threadId), INDEX frm_message_ownerId_idx (ownerId), INDEX frm_message_cDate_idx (creationDate), INDEX frm_message_mDate_idx (modificationDate))", null);        super.update("CREATE TABLE frm_forum_user (forumId VARCHAR(255) NOT NULL, groupId VARCHAR(255) NOT NULL)", null);
        super.update("CREATE TABLE frm_forum_moderator (forumId VARCHAR(255) NOT NULL, groupId VARCHAR(255) NOT NULL)", null);
    }

    /**
     * Select a specific Forum
     * @param forumId
     * @param userId
     * @return
     * @throws DaoException
     */
    public Forum selectForum(String forumId, String userId) throws DaoException
    {
        Collection forums = this.selectForums(forumId, userId, 0, 1, null, false, null, null, null);
        if(forums.size() == 0)
        {
            throw new DaoException("Forum with ID " + forumId + " does not exist");
        }
        else
        {
            setForumAccess(forums);
            return (Forum)forums.iterator().next();
        }
    }

    /**
     * Selects forums based on a DaoQuery
     * @param query
     * @param start
     * @param maxResults
     * @param sort
     * @param descending
     * @return
     * @throws DaoException
     */
    public Collection selectForums(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT forumId, name, description, creationDate, modificationDate, ownerId, isPublic, active, assigneeId, category from frm_forum WHERE forumId=forumId " + query.getStatement() + getSort(sort, descending), Forum.class, query.getArray(), start, maxResults);
    }

    /**
     * Selects forums based on search parameters
     * @param forumId
     * @param userId
     * @param start
     * @param numOfRows
     * @param sortBy
     * @param isDescending
     * @param searchBy
     * @param searchCriteria
     * @param isActive
     * @return
     * @throws DaoException
     */
    public Collection selectForums(String forumId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria, String isActive) throws DaoException
    {
        Collection forums = new ArrayList();
        try
        {
            String sql;
            Object[] args;
            List argsList = new ArrayList();

            if(forumId != null && !forumId.trim().equals(""))
            {
                argsList.add(forumId);
                sql = "SELECT forumId, name, description, creationDate, modificationDate, ownerId, isPublic, active, assigneeId, category from frm_forum where forumId=?";
            }
            else
            {
                sql = "SELECT ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category, " +
                      "COUNT(DISTINCT ft.threadId) AS numOfThread, COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate " +
                      "FROM frm_forum ff " +
                      "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                      "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId";
            }
            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                argsList.add("%" + searchCriteria + "%");
                if(forumId != null && !forumId.trim().equals(""))
                {
                    sql += " AND " + searchBy + " LIKE ?";
                }
                else
                {
                    if(searchBy.equalsIgnoreCase("numOfThread") || searchBy.equalsIgnoreCase("numOfMessage") || searchBy.equalsIgnoreCase("lastPostDate"))
                        sql += " WHERE " + (searchBy.equalsIgnoreCase("lastPostDate")? "strLastPostDate":searchBy) + " LIKE ?";
                    else
                        sql += " WHERE ff." + searchBy + " LIKE ?";
                }
            }
            if(isActive != null && (isActive.equals("0") || isActive.equals("1")))
            {
                argsList.add(isActive);
                if(forumId != null && !forumId.trim().equals(""))
                {
                    sql += " AND active=?";
                }
                else
                {
                    if(sql.toUpperCase().indexOf("WHERE")>-1)
                        sql += " AND ff.active=?";
                    else
                        sql += " WHERE ff.active=?";
                }
            }
            if(forumId == null || forumId.trim().equals(""))
                sql += " GROUP BY ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId";
            if(forumId != null && !forumId.trim().equals(""))
            {
                if(sortBy!=null && !sortBy.trim().equals(""))
                    sql += " ORDER BY " + sortBy;
                else
                    sql += " ORDER BY creationDate DESC";
            }
            else
            {
                if(sortBy!=null && !sortBy.trim().equals(""))
                {
                    if(sortBy.equalsIgnoreCase("numOfThread") || sortBy.equalsIgnoreCase("numOfMessage") || sortBy.equalsIgnoreCase("lastPostDate"))
                        sql += " ORDER BY " + (sortBy.equalsIgnoreCase("lastPostDate")? "strLastPostDate":sortBy);
                    else
                        sql += " ORDER BY ff." + sortBy;
                }
                else
                    sql += " ORDER BY ff.creationDate DESC";
            }
            if(isDescending && (sql.toUpperCase().lastIndexOf("DESC")!= (sql.length()-4)))
            {
                sql += " DESC";
            }
            args = argsList.toArray();
            forums = super.select(sql, Forum.class, args, start, numOfRows);


        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
        return forums;
    }

    /**
     * Selects forums that groups have access to as users.
     * @param groupIds
     * @param active
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws DaoException
     */
    public Collection selectForumsByUserGroupAccess(String[] groupIds, DaoQuery query, String active, String sort, boolean desc, int start, int rows) throws DaoException
    {
        Collection paramList = new ArrayList();

        // formulate SQL
        String sql = "SELECT DISTINCT ff.forumId, ff.name, ff.description, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_user ffa ON ff.forumId=ffa.forumId " +
                "WHERE (( ff.isPublic='1' ";
        if (groupIds != null && groupIds.length > 0)
        {
            sql+=" OR ffa.groupId IN (";
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql+=",";
                sql+="?";
            }
            sql+=")";
            paramList.addAll(Arrays.asList(groupIds));
        }
        sql+=") ";
        if ("1".equals(active))
            sql+=" AND ff.active='1' ";
        else if ("0".equals(active))
            sql+=" AND ff.active='0' ";
        sql+=") ";

        // append DaoQuery
        if (query == null) {
            query = new DaoQuery();
        }
        sql+=query.getStatement();
        paramList.addAll(Arrays.asList(query.getArray()));

        // append sort
        if (sort != null && sort.trim().length() > 0)
        {
            sql+=" ORDER BY ";
            sql+=sort;
            if (desc)
                sql+=" DESC";
        }
        else
        {
            sql+=" ORDER BY ff.creationDate";
        }
        return super.select(sql, Forum.class, paramList.toArray(), start, rows);
    }

/*
    public Collection selectForumsByUserGroupAccess(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        String sql =
            "SELECT DISTINCT frm_forum.forumId, name, description, creationDate, modificationDate, ownerId, isPublic, active, assigneeId, category " +
            "FROM frm_forum LEFT OUTER JOIN frm_forum_user ON frm_forum.forumId=frm_forum_user.forumId " +
            "WHERE ( ff.isPublic='1' ) ";
        return super.select(sql + query.getStatement() + getSort(sort, descending), Forum.class, query.getArray(), start, maxResults);
    }

    public int selectForumCountByUserGroupAccess(DaoQuery query) throws DaoException
    {
        String sql =
            "SELECT COUNT(DISTINCT frm_forum.forumId) AS count " +
            "FROM frm_forum LEFT OUTER JOIN frm_forum_user ON frm_forum.forumId=frm_forum_user.forumId " +
            "WHERE 1=1";
        Collection list = super.select(sql + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        return getCount(list);
    }
*/

    /**
     * Selects a count of forums that groups have access to as users.
     * @param groupIds
     * @param active
     * @return
     * @throws DaoException
     */
    public int selectForumCountByUserGroupAccess(String[] groupIds, DaoQuery query, String active) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT ff.forumId) AS total " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_user ffa ON ff.forumId=ffa.forumId " +
                "WHERE ( ff.isPublic='1' ");
        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" OR ffa.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }
        sql.append(") ");
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");

        // append DaoQuery
        if (query == null) {
            query = new DaoQuery();
        }
        sql.append(query.getStatement());
        paramList.addAll(Arrays.asList(query.getArray()));

        Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    /**
     * Selects forums that groups have access to as moderators.
     * @param groupIds
     * @param searchBy
     * @param searchCriteria
     * @param active
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws DaoException
     */
    public Collection selectForumsByModeratorGroupAccess(String[] groupIds, String searchBy, String searchCriteria, String active, String sort, boolean desc, int start, int rows) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category, " +
                "COUNT(DISTINCT ft.threadId) AS numOfThread, COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_moderator ffm ON ff.forumId=ffm.forumId " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE (ff.isPublic='1' ");

        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" OR ffm.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }
        sql.append(") ");
        if (searchBy != null && searchBy.trim().length() > 0 && searchCriteria != null)
        {
            sql.append(" AND ff." + searchBy + " LIKE ?");
            paramList.add("%" + searchCriteria + "%");
        }
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        sql.append(" GROUP BY ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category ");
        if (sort != null && sort.trim().length() > 0)
        {
            sql.append(" ORDER BY ");
            if (sort.equals("numOfThread") || sort.equals("numOfMessage") || sort.equalsIgnoreCase("lastPostDate"))
                sql.append(sort);
            else
                sql.append("ff." + sort);
            if (desc)
                sql.append(" DESC");
        }
        else {
            sql.append(" ORDER BY ff.creationDate");
        }
        return super.select(sql.toString(), Forum.class, paramList.toArray(), start, rows);
    }
    
//  Retrieve forums for Moderator
    public Collection selectForumsByModerator(String[] groupIds, String searchBy, String searchCriteria, String active, String sort, boolean desc, int start, int rows) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category, " +
                "COUNT(DISTINCT ft.threadId) AS numOfThread, COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_moderator ffm ON ff.forumId=ffm.forumId " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE 1=1 ");

        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" AND ffm.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }

        if (searchBy != null && searchBy.trim().length() > 0 && searchCriteria != null)
        {
            sql.append(" AND ff." + searchBy + " LIKE ?");
            paramList.add("%" + searchCriteria + "%");
        }
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        sql.append(" GROUP BY ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category ");
        if (sort != null && sort.trim().length() > 0)
        {
            sql.append(" ORDER BY ");
            if (sort.equals("numOfThread") || sort.equals("numOfMessage") || sort.equalsIgnoreCase("strLastPostDate"))
                sql.append(sort);
            else
                sql.append("ff." + sort);
            if (desc)
                sql.append(" DESC");
        }
        else {
            sql.append(" ORDER BY ff.creationDate");
        }
        return super.select(sql.toString(), Forum.class, paramList.toArray(), start, rows);
    }
    
    //Retrieve forums for Forum Manager
    public Collection getForumsByManager(String searchBy, String searchCriteria, String active, String sort, boolean desc, int start, int rows) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category, " +
                "COUNT(DISTINCT ft.threadId) AS numOfThread, COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate " +
                "FROM frm_forum ff " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE 1=1");

        if (searchBy != null && searchBy.trim().length() > 0 && searchCriteria != null)
        {
            sql.append(" AND ff." + searchBy + " LIKE ?");
            paramList.add("%" + searchCriteria + "%");
        }
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        sql.append(" GROUP BY ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category ");
        if (sort != null && sort.trim().length() > 0)
        {
            sql.append(" ORDER BY ");
            if (sort.equals("numOfThread") || sort.equals("numOfMessage") || sort.equalsIgnoreCase("strLastPostDate"))
                sql.append(sort);
            else
                sql.append("ff." + sort);
            if (desc)
                sql.append(" DESC");
        }
        else {
            sql.append(" ORDER BY ff.creationDate");
        }
        return super.select(sql.toString(), Forum.class, paramList.toArray(), start, rows);
    }
    
    public boolean isForumModeratorCheck(String[] groupIds, String forumid) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT ff.forumId) AS total " +
                "FROM frm_forum ff, frm_forum_moderator ffm " +               
                "WHERE ff.forumId=ffm.forumId AND ff.forumId='"+forumid+"' AND ");

        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" ffm.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }      
        Collection list= super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
        if (list.size() > 0) {
			HashMap map = (HashMap) list.iterator().next();
			if(Integer.parseInt(map.get("total").toString())>0)
			return true;
			else
				return false;
		} else
			return false;
    }

    /**
     * Selects a count of forums that groups have access to as moderators.
     * @param groupIds
     * @param searchBy
     * @param searchCriteria
     * @param active
     * @return
     * @throws DaoException
     */
    public int selectForumCountByModeratorGroupAccess(String[] groupIds, String searchBy, String searchCriteria, String active) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT ff.forumId) AS total " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_moderator ffm ON ff.forumId=ffm.forumId " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE (ff.isPublic='1' ");
        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" OR ffm.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }
        sql.append(") ");
        if (searchBy != null && searchBy.trim().length() > 0 && searchCriteria != null)
        {
            sql.append(" AND ff." + searchBy + " LIKE ?");
            paramList.add("%" + searchCriteria + "%");
        }
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
    
    //  Retrieve Count forums for moderator
    public int selectForumCountByModerator(String[] groupIds, String searchBy, String searchCriteria, String active) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT ff.forumId) AS total " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_moderator ffm ON ff.forumId=ffm.forumId " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE 1=1 ");
        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" AND ffm.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }
        if (searchBy != null && searchBy.trim().length() > 0 && searchCriteria != null)
        {
            sql.append(" AND ff." + searchBy + " LIKE ?");
            paramList.add("%" + searchCriteria + "%");
        }
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
    
    //  Retrieve count forums for Forum Manager
    public int getNumOfForumsByManager(String searchBy, String searchCriteria, String active) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT ff.forumId) AS total " +
                "FROM frm_forum ff " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE 1=1 ");        
        if (searchBy != null && searchBy.trim().length() > 0 && searchCriteria != null)
        {
            sql.append(" AND ff." + searchBy + " LIKE ?");
            paramList.add("%" + searchCriteria + "%");
        }
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    //To Check whether is moderator
    public int selectForumCountByModeratorGroupAccess(String[] groupIds, String active) throws DaoException
    {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT ff.forumId) AS total " +
                "FROM frm_forum ff LEFT OUTER JOIN frm_forum_moderator ffm ON ff.forumId=ffm.forumId " +
                "LEFT OUTER JOIN frm_thread ft ON ff.forumId = ft.forumId " +
                "LEFT OUTER JOIN frm_message fm ON ft.forumId = fm.forumId " +
                "WHERE (1=1 ");
        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" AND ffm.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(groupIds));
        }
        sql.append(") ");        
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        Map row = (Map)super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
    
    public Thread selectThread(String threadId, String userId) throws DaoException
    {
        Collection threads = new ArrayList();
        String sql;
        Object[] args = new Object[] {threadId};
        if(threadId != null && !threadId.trim().equals(""))
            sql = "SELECT threadId, forumId, creationDate, modificationDate, ownerId, isPublic, active, subject, content, email from frm_thread where threadId=?";
        else
            throw new DaoException("Invalid Thread ID!");
        threads = super.select(sql, Thread.class, args, 0, -1);
        if(threads.size() != 1)
            throw new DaoException("Either thread with ID = " + threadId + " does not exist or duplicated");
        setNumOfMessage(threads);
        setLastPost(threads);
        return (Thread)threads.iterator().next();
    }

    public Collection selectThreads(String forumId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws DaoException
    {
        Collection threads = new ArrayList();
        String sql;
        String sqlAppend = "";
        Object[] args;
        List argsList = new ArrayList();

        try
        {
            if(forumId != null && !forumId.trim().equals(""))
            {
                argsList.add(forumId);
                sql = "SELECT ft.threadId, ft.creationDate, ft.modificationDate, ft.ownerId, ft.isPublic, ft.active, ft.subject, " +
                      "COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate, ft.email " +
                      "FROM frm_thread ft LEFT OUTER JOIN frm_message fm on ft.threadId=fm.threadId " +
                      "WHERE ft.forumId=?";
            }
            else
            {
                sql = "SELECT ft.threadId, ft.creationDate, ft.modificationDate, ft.ownerId, ft.isPublic, ft.active, ft.subject, " +
                      "COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate, ft.email " +
                      "FROM frm_thread ft LEFT OUTER JOIN frm_message fm on ft.threadId=fm.threadId ";
            }
            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                sqlAppend += (forumId != null && !forumId.trim().equals("")?" AND ft." : " WHERE ft.") + searchBy;
                if(searchBy.equals("ownerId"))
                {
                    sqlAppend+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sqlAppend+= " LIKE ?";
                    argsList.add("%" + searchCriteria + "%");
                }
            }
            sqlAppend += " GROUP BY ft.threadId, ft.forumId, ft.creationDate, ft.modificationDate, ft.ownerId, ft.isPublic, ft.active, ft.subject";
            if(sortBy!=null && !sortBy.trim().equals(""))
            {
                if(sortBy.equalsIgnoreCase("numOfMessage") || sortBy.equalsIgnoreCase("lastPostDate"))
                    sqlAppend += " ORDER BY " + (sortBy.equalsIgnoreCase("lastPostDate")? "strLastPostDate":sortBy);
                else
                    sqlAppend += " ORDER BY ft." + sortBy;
            }
            else
                sqlAppend += " ORDER BY ft.creationDate DESC";
            if(isDescending && sqlAppend.toUpperCase().indexOf("DESC")<0)
                sqlAppend += " DESC";
            sql += sqlAppend;
            args = argsList.toArray();
            threads = super.select(sql, Thread.class, args, start, numOfRows);
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
        }
        return threads;
    }

    public Collection selectThreads(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        String sql =
            "SELECT frm_thread.threadId, frm_thread.creationDate, frm_thread.modificationDate, frm_thread.ownerId, isPublic, " +
            "active, frm_thread.subject, COUNT(DISTINCT messageId) AS numOfMessage, MAX(frm_message.creationDate) AS strLastPostDate " +
            "FROM frm_thread LEFT OUTER JOIN frm_message ON frm_thread.threadId=frm_message.threadId WHERE 1=1 ";
        sql = sql + query.getStatement() + getSort(sort, descending);
        sql = sql + "GROUP BY frm_thread.threadId, frm_thread.forumId, creationDate, modificationDate, ownerId, isPublic, active, subject";
        return super.select(sql, Thread.class, query.getArray(), start, maxResults);
    }

    public int selectThreadCount(DaoQuery query) throws DaoException
    {
        String sql =
            "SELECT COUNT(DISTINCT frm_thread.threadId) AS intCount  " +
            "FROM frm_thread LEFT OUTER JOIN frm_message ON frm_thread.threadId=frm_message.threadId WHERE 1=1 ";
        Collection list = super.select(sql + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        return getCount(list);
    }

    public Collection selectFullThreads(String forumId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws DaoException
    {
        Collection threads = new ArrayList();
        String sql;
        Object[] args;
        List argsList = new ArrayList();

        if(forumId != null && !forumId.trim().equals(""))
        {
            argsList.add(forumId);
            sql = "SELECT threadId, forumId, creationDate, modificationDate, ownerId, isPublic, active, subject, content from frm_thread where forumId=?";
        }
        else
        {
            sql = "SELECT threadId, forumId, creationDate, modificationDate, ownerId, isPublic, active, subject, content from frm_thread";
        }

        if(searchCriteria!=null && !searchCriteria.trim().equals(""))
        {
            sql += (forumId != null && !forumId.trim().equals("")?" AND " : " WHERE ") + searchBy;
            if(searchBy.equals("ownerId"))
            {
                sql+= "=?";
                argsList.add(searchCriteria);
            }
            else
            {
                sql+= " LIKE ?";
                argsList.add("%" + searchCriteria + "%");
            }
        }
        if(sortBy!=null && !sortBy.trim().equals(""))
            sql += " ORDER BY " + sortBy;
        else
            sql += " ORDER BY creationDate DESC";
        if(isDescending && sql.toUpperCase().indexOf("DESC")<0)
            sql += " DESC";
        args = argsList.toArray();
        threads = super.select(sql, Thread.class, args, start, numOfRows);
        setNumOfMessage(threads);
        setLastPost(threads);
        return threads;
    }

    public Message selectMessage(String messageId, String userId) throws DaoException
    {
        Collection messages = new ArrayList();
        String sql;
        Object[] args = new Object[] {messageId};
        if(messageId != null && !messageId.trim().equals(""))
            sql = "SELECT messageId, parentMessageId, threadId, forumId, ownerId, subject, content, creationDate, modificationDate, email from frm_message where messageId=?";
        else
            throw new DaoException("Invalid Message ID!");
        messages = super.select(sql, Message.class, args, 0, -1);
        if(messages.size() > 1)
            throw new DaoException("Message with ID = " + messageId + " does not exist or duplicated");
        return (Message)messages.iterator().next();
    }

    public Collection selectMessages(String threadId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws DaoException
    {
        Collection messages = new ArrayList();
        try
        {
            String sql;
            List argsList = new ArrayList();

            if(threadId != null && !threadId.trim().equals(""))
            {
                argsList.add(threadId);
                sql = "SELECT messageId, parentMessageId, threadId, forumId, ownerId, subject, content, creationDate, modificationDate, email from frm_message where threadId=?";
            }
            else
                sql = "SELECT messageId, parentMessageId, threadId, forumId, ownerId, subject, content, creationDate, modificationDate, email from frm_message";
            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                sql += (threadId != null && !threadId.trim().equals("")?" AND " : " WHERE ") + searchBy;
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria + "%");
                }
            }
            if(sortBy!=null && !sortBy.trim().equals(""))
                sql += " ORDER BY " + sortBy;
            else
                sql += " ORDER BY creationDate";

            if(isDescending)
                sql += " DESC";
            Object[] args = argsList.toArray();
            messages = super.select(sql, Message.class, args, start, numOfRows);
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
        }
        return messages;
    }

    public Collection selectMessages(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT messageId, parentMessageId, threadId, forumId, ownerId, subject, content, creationDate, modificationDate FROM frm_message WHERE 1=1 " + query.getStatement() + getSort(sort, descending), Message.class, query.getArray(), start, maxResults);
    }

    public int selectMessageCount(DaoQuery query) throws DaoException
    {
        Collection list = super.select("SELECT COUNT(DISTINCT messageId) AS intCount FROM frm_message WHERE 1=1 " + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        return getCount(list);
    }

    public Collection selectMessagesByParent(String parentMessageId, String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws DaoException
    {
        Collection messages = new ArrayList();
        String sql;
        List argsList = new ArrayList();

        if(parentMessageId != null && !parentMessageId.trim().equals(""))
        {
            argsList.add(parentMessageId);
            sql = "SELECT messageId, parentMessageId, threadId, forumId, ownerId, subject, content, creationDate, modificationDate from frm_message WHERE parentMessageId=?";
        }
        else
            throw new DaoException("Invalid ParentMessageId!");
        if(searchCriteria!=null && !searchCriteria.trim().equals(""))
        {
            sql += " AND " + searchBy;
            if(searchBy.equals("ownerId"))
            {
                sql+= "=?";
                argsList.add(searchCriteria);
            }
            else
            {
                sql+= " LIKE ?";
                argsList.add("%" + searchCriteria + "%");
            }
        }
        if(sortBy!=null && !sortBy.trim().equals(""))
            sql += " ORDER BY " + sortBy;
        else
            sql += " ORDER BY creationDate";
        if(isDescending)
            sql += " DESC";
        Object[] args = argsList.toArray();
        messages = super.select(sql, Message.class, args, start, numOfRows);
        return messages;
    }

    public int selectForumCount(String searchBy, String searchCriteria, String isActive) throws DaoException
    {
        try
        {
            String sql = "SELECT COUNT(forumId) AS total FROM frm_forum";
            List argsList = new ArrayList();

            if(searchCriteria != null && !searchCriteria.trim().equals(""))
            {
                sql += " WHERE " + searchBy;
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria + "%");
                }
            }

            if(isActive != null && !isActive.trim().equals(""))
            {
                argsList.add(isActive);
                if(sql.toUpperCase().indexOf("WHERE")>-1)
                    sql += " AND active=?";
                else
                    sql += " WHERE active=?";
            }

            HashMap count = (HashMap)super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public int selectThreadCount(String forumId, String searchBy, String searchCriteria) throws DaoException
    {
        try
        {
            String sql = "SELECT COUNT(threadId) AS total FROM frm_thread";
            List argsList = new ArrayList();

            if(forumId!=null && !forumId.trim().equals(""))
            {
                argsList.add(forumId);
                sql+= " WHERE forumId=?";
            }

            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                sql += (forumId != null && !forumId.trim().equals("")?" AND " : " WHERE ") + searchBy;
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria + "%");
                }
            }

            HashMap count = (HashMap)super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public int selectMessageCountPerForum(String forumId, String searchBy, String searchCriteria) throws DaoException
    {
        try
        {
            String sql = "SELECT COUNT(messageId) AS total FROM frm_message";
            List argsList = new ArrayList();

            if(forumId!=null && !forumId.trim().equals(""))
            {
                argsList.add(forumId);
                sql+= " WHERE forumId=?";
            }

            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                sql += (forumId != null && !forumId.trim().equals("")?" AND " : " WHERE ") + searchBy;
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria + "%");
                }
            }

            HashMap count = (HashMap)super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public int selectMessageCountPerThread(String threadId, String searchBy, String searchCriteria) throws DaoException
    {
        try
        {
            String sql = "SELECT COUNT(messageId) AS total FROM frm_message";
            List argsList = new ArrayList();

            if(threadId!=null && !threadId.trim().equals(""))
            {
                argsList.add(threadId);
                sql+= " WHERE threadId=?";
            }

            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                sql += (threadId != null && !threadId.trim().equals("")?" AND " : " WHERE ") + searchBy;
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria + "%");
                }
            }

            HashMap count = (HashMap)super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public String selectForumId(String id, String type)
    {
        String forumName = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "";
        try
        {
            if(type.equals(Thread.class.getName()))
                sql  = "SELECT forumId from frm_thread WHERE threadId=?";
            else if(type.equals(Message.class.getName()))
                sql  = "SELECT forumId from frm_message WHERE messageId=?";

            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if(rs.next())
                forumName = rs.getString("forumId");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return forumName;
    }

    public String selectThreadId(String messageId)
    {
        String forumName = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT threadId from frm_message WHERE messageId=?";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, messageId);
            rs = pstmt.executeQuery();

            if(rs.next())
                forumName = rs.getString("threadId");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return forumName;
    }

    public String selectForumName(String forumId)
    {
        String forumName = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT name from frm_forum WHERE forumId=?";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, forumId);
            rs = pstmt.executeQuery();

            if(rs.next())
                forumName = rs.getString("name");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return forumName;
    }

    public String selectForumDesc(String forumId)
    {
        String forumDesc = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT description from frm_forum WHERE forumId=?";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, forumId);
            rs = pstmt.executeQuery();

            if(rs.next())
                forumDesc = rs.getString("description");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return forumDesc;
    }

    public String selectThreadSubject(String threadId)
    {
        String threadSubject = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT subject from frm_thread where threadId=?";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, threadId);
            rs = pstmt.executeQuery();

            if(rs.next())
                threadSubject = rs.getString("subject");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return threadSubject;
    }

    public String selectThreadContent(String threadId)
    {
        String threadContent = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT content from frm_thread where threadId=?";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, threadId);
            rs = pstmt.executeQuery();

            if(rs.next())
                threadContent = rs.getString("content");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return threadContent;
    }

    public String selectMessageSubject(String messageId)
    {
        String threadSubject = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT subject from frm_message where messageId=?";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, messageId);
            rs = pstmt.executeQuery();

            if(rs.next())
                threadSubject = rs.getString("subject");
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);

        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return threadSubject;
    }

    public void insertForum(Forum forum, String[] userSelectBoxArray, String[] userModeratorSelectBoxArray) throws DaoException
    {
        Transaction tx = null;
        try
        {
            if(!forumNameExist(forum.getName()))
            {
                tx = getTransaction();
                tx.begin();
                String sql = "INSERT INTO frm_forum (forumId, name, description, creationDate, modificationDate, ownerId, isPublic, active, category) " +
                             "VALUES (#forumId#, #name#, #description#, #creationDate#, #modificationDate#, #ownerId#, #isPublic#, #active#, #category#)";
                tx.update(sql, forum);
                updateForumAccess(tx, forum.getForumId(), forum.getUserGroup(), forum.getModeratorGroup(), userSelectBoxArray, userModeratorSelectBoxArray);
                tx.commit();
            }
            else
            {
                throw new DaoException(ForumModule.FORUM_NAME_EXIST);
            }
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void insertThread(Thread thread) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO frm_thread (threadId, forumId, creationDate, modificationDate, ownerId, isPublic, active, subject, content, email) " +
                         "VALUES (#threadId#, #forumId#, #creationDate#, #modificationDate#, #ownerId#, #isPublic#, #active#, #subject#, #content#, #email#)";
            tx.update(sql, thread);
            tx.commit();

        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    public void insertMessage(Message message) throws DaoException
    {
        Transaction tx = null;
        try
        {
            tx = getTransaction();
            tx.begin();
            String sql = "INSERT INTO frm_message (messageId, parentMessageId, threadId, forumId, ownerId, subject, content, creationDate, modificationDate, email) " +
                         "VALUES (#messageId#, #parentMessageId#, #threadId#, #forumId#, #ownerId#, #subject#, #content#, #creationDate#, #modificationDate#, #email#)";

            tx.update(sql, message);
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    public void updateForum(Forum forum, String userId, String[] userSelectBoxArray, String[] userModeratorSelectBoxArray) throws DaoException
    {
        Transaction tx = null;
        try
        {
            String sql =
                    "UPDATE frm_forum SET name=#name#, description=#description#, modificationDate=#modificationDate#, isPublic=#isPublic#, active=#active#, category=#category#" +
                    " WHERE forumId = #forumId#";
            Log.getLog(ForumDao.class).debug("~~~ SQL = " + sql);
            tx = getTransaction();
            tx.begin();
            tx.update(sql, forum);
            updateForumAccess(tx, forum.getForumId(), forum.getUserGroup(), forum.getModeratorGroup(), userSelectBoxArray, userModeratorSelectBoxArray);
            updateForumSubsription(forum.getForumId(), forum.getUserGroup(), forum.getModeratorGroup());
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
    
    public void deleteForum(Message message) throws DaoException
    {
        Transaction tx = null;
        try
        {
            String sql =
                    "UPDATE frm_message SET parentMessageId=#parentMessageId# " +
                    " WHERE parentMessageId = #messageId#";
            String sql2 =
                "DELETE FROM frm_message " +
                " WHERE messageId = #messageId#";
            Log.getLog(ForumDao.class).debug("~~~ SQL = " + sql);
            tx = getTransaction();
            tx.begin();
            tx.update(sql, message);
            tx.update(sql2, message);            
            tx.commit();
        }
        catch(Exception e)
        {
            if (tx != null)
            {
                tx.rollback();
            }
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public void updateThread(Thread thread, String userId) throws DaoException
    {
        try
        {
            String sql = "UPDATE frm_thread SET modificationDate=#modificationDate#, isPublic=#isPublic#, active=#active#, subject=#subject#, content=#content# WHERE threadId = #threadId#";
            super.update(sql, thread);
        }
        catch(DaoException e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public void updateMessage(Message message, String userId) throws DaoException
    {
        try
        {
            String sql = "UPDATE frm_message SET modificationDate=#modificationDate#, subject=#subject#, content=#content# " +
                    "WHERE messageId = #messageId#";
            super.update(sql, message);
        }
        catch(DaoException e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public void deleteForum(String forumId, String userId, boolean force) throws DaoException
    {
        try
        {
            Forum forum = new Forum();
            forum.setForumId(forumId);
            String sql;
            if(force)
            {
                sql = "DELETE FROM frm_forum WHERE forumId=#forumId#";
                super.update(sql, forum);
                sql = "DELETE FROM frm_thread WHERE forumId=#forumId#";
                super.update(sql, forum);
                sql = "DELETE FROM frm_message WHERE forumId=#forumId#";
                super.update(sql, forum);
                sql = "DELETE FROM frm_forum_user WHERE forumId=#forumId#";
                super.update(sql, forum);
                sql = "DELETE FROM frm_forum_moderator WHERE forumId=#forumId#";
                super.update(sql, forum);
            }
            else
            {
                if(selectThreads(forumId, userId, 0, -1, null, false, null, null).size() == 0)
                {
                    sql = "DELETE FROM frm_forum WHERE forumId=#forumId#";
                    super.update(sql, forum);
                    sql = "DELETE FROM frm_forum_user WHERE forumId=#forumId#";
                    super.update(sql, forum);
                    sql = "DELETE FROM frm_forum_moderator WHERE forumId=#forumId#";
                    super.update(sql, forum);
                }
                else
                    throw new DaoException("Unable to delete forum with available thread(s)!");
            }
        }
        catch(DaoException e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public void deleteThread(String threadId, String userId, boolean force) throws DaoException
    {
        try
        {
            Thread thread = new Thread();
            thread.setThreadId(threadId);
            String sql;
            if(force)
            {
                sql = "DELETE FROM frm_thread WHERE threadId=#threadId#";
                super.update(sql, thread);
                sql = "DELETE FROM frm_message WHERE threadId=#threadId#";
                super.update(sql, thread);
            }
            else
            {
                if(selectMessages(threadId, userId, 0, -1, null, false, null, null).size() == 0)
                {
                    sql = "DELETE FROM frm_thread WHERE threadId=#threadId#";
                    super.update(sql, thread);
                }
                else
                    throw new DaoException("Unable to delete Thread with available message(s)!");
            }
        }
        catch(DaoException e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public void deleteMessage(String messageId, String userId, boolean force) throws DaoException
    {
        try
        {
            Message message = new Message();
            String sql;
            if(force)
            {
                sql = "SELECT parentMessageId FROM frm_message WHERE messageId=?";
                Collection messages = super.select(sql, Message.class, new Object[]{messageId}, 0, -1);
                message = (Message) messages.iterator().next();
                message.setMessageId(messageId);
                sql = "DELETE FROM frm_message WHERE messageId=#messageId#";
                super.update(sql, message);
                sql = "UPDATE frm_message SET parentMessageId=#parentMessageId# " +
                      "WHERE parentMessageId = #messageId#";
                super.update(sql, message);
            }
            else
            {
                if(selectMessagesByParent(messageId, userId, 0, -1, null, false, null, null).size()==0)
                {
                    sql = "DELETE FROM frm_message WHERE messageId=#messageId# AND ownerId=#ownerId#";
                    super.update(sql, message);
                }
                else
                    throw new DaoException("Unable to delete Message with child message(s)!");
            }
        }
        catch(DaoException e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    protected void setNumOfThread(Collection objs) throws DaoException
    {
        Iterator itObjs = objs.iterator();
        Object obj;
        while(itObjs.hasNext())
        {
            obj = itObjs.next();
            if(obj instanceof Forum)
            {
                ((Forum)obj).setNumOfThread(this.selectThreadCount(((Forum)obj).getForumId(), null, null));
            }
            else
                throw new DaoException("Invalid Object Type!");
        }
    }

    protected void setNumOfMessage(Collection objs) throws DaoException
    {
        Iterator itObjs = objs.iterator();
        Object obj;
        while(itObjs.hasNext())
        {
            obj = itObjs.next();
            if(obj instanceof Forum)
                //((Forum)obj).setNumOfMessage(this.selectMessageCountPerForum(((Forum)obj).getForumId(), null, null)-this.selectThreadCount(((Forum)obj).getForumId(), null, null));
                ((Forum)obj).setNumOfMessage(this.selectMessageCountPerForum(((Forum)obj).getForumId(), null, null));
            else if (obj instanceof Thread)
                ((Thread)obj).setNumOfMessage(this.selectMessageCountPerThread(((Thread)obj).getThreadId(), null, null)-1);
            else
                throw new DaoException("Invalid Object Type!");
        }
    }

    protected void setForumAccess(Collection forums) throws DaoException
    {
        String sql = "SELECT groupId from frm_forum_user WHERE forumId=?";
        String sql2 = "SELECT groupId from frm_forum_moderator WHERE forumId=?";
        Forum forum;
        Map userGroupMap = new SequencedHashMap();
        Map moderatorGroupMap = new SequencedHashMap();
        Collection groupIdList;
        Collection groupList;
        try
        {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            
            Collection allGroupList = security.getGroups(new DaoQuery(), 0, -1, "groupName", false);
            // iterate thru each forum
            for (Iterator i=forums.iterator(); i.hasNext();)
            {
                forum = (Forum)i.next();
                // get user groups for the forum
                groupIdList = new ArrayList();
                Collection userGroupList = super.select(sql, HashMap.class, new Object[] {forum.getForumId()}, 0, -1);
                for (Iterator j=userGroupList.iterator(); j.hasNext();)
                {
                    Map row = (Map)j.next();
                    groupIdList.add(row.get("groupId"));
                    forum.getUsers().add(row.get("groupId"));

                }
                if (groupIdList.size() > 0)
                {
                    groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                    for (Iterator k=groupList.iterator(); k.hasNext();)
                    {
                        Group group = (Group)k.next();
                        userGroupMap.put(group.getId(), group.getName());
                        forum.getUsers().remove(group.getId());
                    }
                }
                forum.setUserGroup(userGroupMap);

                // get moderator groups for the forum
                groupIdList = new ArrayList();
                Collection moderatorGroupList = super.select(sql2, HashMap.class, new Object[] {forum.getForumId()}, 0, -1);
                for (Iterator j=moderatorGroupList.iterator(); j.hasNext();)
                {
                    Map row = (Map)j.next();
                    groupIdList.add(row.get("groupId"));
                    forum.getModerators().add(row.get("groupId"));

                }
                if (groupIdList.size() > 0)
                {
                    groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                    for (Iterator k=groupList.iterator(); k.hasNext();)
                    {
                        Group group = (Group)k.next();
                        moderatorGroupMap.put(group.getId(), group.getName());
                        forum.getModerators().remove(group.getId());
                    }
                }
                forum.setModeratorGroup(moderatorGroupMap);
            }
        }
        catch (DaoException e)
        {
            throw e;
        }
        catch (SecurityException e)
        {
            throw new DaoException("Error getting forum groups " + e.toString());
        }
    }

    protected void updateForumAccess(Transaction tx, String forumId, Map mapUserGroup, Map mapModeratorGroup, String[] userSelectBoxArray, String[] userModeratorSelectBoxArray) throws SQLException {
        String sql = "DELETE from frm_forum_user WHERE forumId=?";
        String sql2 = "DELETE from frm_forum_moderator WHERE forumId=?";
        String sql3 = "INSERT INTO frm_forum_user (forumId, groupId) VALUES (?, ?)";
        String sql4 = "INSERT INTO frm_forum_moderator (forumId, groupId) VALUES (?, ?)";

        // delete existing entries
        tx.update(sql, new Object[] {forumId});
        tx.update(sql2, new Object[] {forumId});

        // insert new ones
        if (mapUserGroup != null)
        {
            for (Iterator i=mapUserGroup.keySet().iterator(); i.hasNext();)
            {
                String groupId = (String)i.next();
                tx.update(sql3, new Object[] { forumId, groupId });
            }
        }
        for (int i=0; i<userSelectBoxArray.length; i++) {
            String userId = userSelectBoxArray[i];
            tx.update(sql3, new Object[] { forumId, userId });
        }
        if (mapModeratorGroup != null)
        {
            for (Iterator i=mapModeratorGroup.keySet().iterator(); i.hasNext();)
            {
                String groupId = (String)i.next();
                tx.update(sql4, new Object[] { forumId, groupId });
            }
        }
        for (int i=0; i<userModeratorSelectBoxArray.length; i++) {
            String userModeratorId = userModeratorSelectBoxArray[i];
            tx.update(sql4, new Object[] { forumId, userModeratorId });
        }
    }

	protected void updateForumSubsription(String forumId, Map mapUserGroup, Map mapModeratorGroup)
	{
		Map userIds = new HashMap();
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		mapUserGroup.putAll(mapModeratorGroup);
		for (Iterator i = mapUserGroup.keySet().iterator(); i.hasNext();)
		{
			String groupId = (String) i.next();
			try
			{
				Collection users = service.getGroupUsers(groupId);
                for (Iterator iu = users.iterator(); iu.hasNext();)
				{
					User user = (User) iu.next();
					userIds.put(user.getId(), null);
				}
			}
			catch (SecurityException e)
			{
				Log.getLog(ForumDao.class).error("Error while retrieving users of group " + groupId, e);
			}
		}
		if(userIds.size() > 0)
		{
			//Structuring sql query
			StringBuffer query = new StringBuffer("(");
			Collection ids = new ArrayList();
			ids.add(forumId);
			for (Iterator id = userIds.keySet().iterator(); id.hasNext();)
			{
				String userId = (String) id.next();
				if("(".equals(query.toString()))
					query.append("?");
				else
					query.append(", ?");
				ids.add(userId);
			}
			query.append(")");
			try
			{
				super.update("DELETE FROM frm_forum_subscription WHERE forumId=? AND userId NOT IN " + query.toString(), ids.toArray(new Object[] {}));
			}
			catch (DaoException e)
			{
				Log.getLog(ForumDao.class).error("Error occured while removing user subscriptions", e);
			}
		}
		else
		{
			try
			{
				super.update("DELETE FROM frm_forum_subscription WHERE forumId=?", new Object[] {forumId});
			}
			catch (DaoException e)
			{
				Log.getLog(ForumDao.class).error("Error occured while removing all user subscriptions", e);
			}
		}

		//Executing query

	}

    protected void setLastPost(Collection objs)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        Iterator itObjs = objs.iterator();
        Object obj;
        try
        {
            conn = getDataSource().getConnection();
            while(itObjs.hasNext())
            {
                obj = itObjs.next();
                if(obj instanceof Forum)
                {
                    sql="SELECT creationDate from frm_message where forumId=? UNION SELECT creationDate from frm_thread where forumId=? order by creationDate DESC";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, ((Forum)obj).getForumId());
                    pstmt.setString(2, ((Forum)obj).getForumId());
                    rs = pstmt.executeQuery();
                    if(rs.next())
                        ((Forum)obj).setLastPostDate((Date)rs.getObject("creationDate"));
                }
                else if (obj instanceof Thread)
                {
                    sql="SELECT creationDate from frm_message where threadId=? order by creationDate DESC";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, ((Thread)obj).getThreadId());
                    rs = pstmt.executeQuery();
                    if(rs.next())
                        ((Thread)obj).setLastPostDate((Date)rs.getObject("creationDate"));
                }
                else
                    throw new DaoException("Invalid Object Type!");

                if (pstmt != null)
                {
                    try
                    {
                        pstmt.close();
                    }
                    catch (SQLException e)
                    {
                        Log.getLog(ForumDao.class).error(e);
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(ForumDao.class).error(e.getMessage(), e);
        }
        finally
        {

            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
    }

    protected List getForumNames()
    {
        List forumNames = new ArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String sql = "SELECT name FROM frm_forum";
        try
        {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
                forumNames.add(rs.getString("name"));
        }
        catch (SQLException e)
        {
            Log.getLog(ForumDao.class).debug(e.getMessage(), e);
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }

            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    Log.getLog(ForumDao.class).error(e);
                }
            }
        }
        return forumNames;
    }

    protected boolean forumNameExist(String forumName)
    {
        return getForumNames().contains(forumName.trim());
    }

    public Collection selectLatestMessages(String forumId, String userId, int rows) throws DaoException
    {
        String sql =
                " SELECT frm_message.messageId AS id, " +
                " frm_message.subject AS subject, frm_message.modificationDate AS modificationDate, frm_message.threadId AS threadId," +
                " 'M' AS objtype, frm_message.ownerId " +
                " FROM frm_message, frm_thread, frm_forum" +
                " WHERE " +
                " frm_forum.forumId = ? AND " +
                " frm_thread.forumId = frm_forum.forumId AND" +
                " frm_message.threadId = frm_thread.threadId" +
                "" +
                " UNION" +
                "" +
                " SELECT frm_thread.threadId AS id, " +
                " frm_thread.subject AS s, frm_thread.modificationDate AS modificationDate, frm_thread.threadId as threadId," +
                " 'T' AS objtype, frm_thread.ownerId " +
                " FROM frm_thread, frm_forum" +
                " WHERE " +
                " frm_forum.forumId = ? AND " +
                " frm_thread.forumId = frm_forum.forumId" +
                "" +
                " ORDER BY modificationDate DESC";

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsMessages = null;
        Collection messages = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setString(1, forumId);
            statement.setString(2, forumId);
            rsMessages = statement.executeQuery();

            int count = 0;

            while(rsMessages.next() && count < rows)
            {
                HashMap map = new HashMap();
                map.put("id", rsMessages.getString("id"));
                map.put("threadId", rsMessages.getString("threadId"));
                map.put("subject", rsMessages.getString("subject"));
                map.put("modificationDate", rsMessages.getTimestamp("modificationDate"));
                map.put("type", rsMessages.getString("objtype"));
                map.put("ownerId", rsMessages.getString("ownerId"));
                messages.add(map);
                count++;
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return messages;
    }

    public Collection selectCategories() throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsCategories = null;
        Collection categories = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT DISTINCT category FROM frm_forum ORDER BY category", null);
            rsCategories = statement.executeQuery();
            while(rsCategories.next())
                categories.add(rsCategories.getString("category"));
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsCategories != null)
                    rsCategories.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return categories;
    }

    /** Forum subscriptions and profile handling */
    public Collection selectSubscriptions(String userId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsSubscriptions = null;
        Collection forums = new ArrayList();
        Collection forumId = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT forumId FROM frm_forum_subscription WHERE userId = ?", new Object[] {userId});
            rsSubscriptions = statement.executeQuery();
            while(rsSubscriptions.next())
                forumId.add(rsSubscriptions.getString("forumId"));
            if(forumId.size() > 0)
            {
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("forumId", forumId.toArray(), DaoOperator.OPERATOR_AND));
                forums = selectForums(query, 0, -1, "name", false);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsSubscriptions != null)
                    rsSubscriptions.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return forums;
    }

    public Collection selectSubscribers(String forumId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsSubscribers = null;
        Collection users = new ArrayList();
        Collection userId = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT frm_forum_subscription.userId FROM frm_forum_subscription, security_user WHERE frm_forum_subscription.userId=security_user.id AND security_user.active='1' AND frm_forum_subscription.forumId = ?", new Object[] {forumId});
            rsSubscribers = statement.executeQuery();
            while(rsSubscribers.next())
                userId.add(rsSubscribers.getString("userId"));
            if(userId.size() > 0)
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", userId.toArray(), DaoOperator.OPERATOR_AND));
                users = service.getUsers(query, 0, -1, "username", false);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsSubscribers != null)
                    rsSubscribers.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return users;
    }

    public void insertSubscription(String userId, String forumId) throws DaoException
    {
        super.update("INSERT INTO frm_forum_subscription(userId, forumId) VALUES(?, ?)", new Object[] {userId, forumId});
    }

    public void deleteSubscriptionByUser(String userId) throws DaoException
    {
        super.update("DELETE FROM frm_forum_subscription WHERE userId=?", new Object[] {userId});
    }

    public void deleteSubscriptionByForum(String forumId) throws DaoException
    {
        super.update("DELETE FROM frm_forum_subscription WHERE forumId=?", new Object[] {forumId});
    }

    public Map selectUserSettings(String userId) throws DaoException
    {
        Map settings = new HashMap();
        Collection list = super.select("SELECT userId, forumType FROM frm_forum_settings WHERE userId = ?", HashMap.class, new Object[] {userId}, 0, 1);
        if(list.size() > 0)
            settings = (Map) list.iterator().next();
        return settings;
    }

    public void insertSettings(String userId, Map settings) throws DaoException
    {
        super.update("INSERT INTO frm_forum_settings(userId, forumType) VALUES(?, ?)", new Object[] {userId, settings.get(ForumModule.SETTINGS_LABEL_TYPE)});
    }

    public void updateSettings(String userId, Map settings) throws DaoException
    {
        super.update("UPDATE frm_forum_settings SET forumType=? WHERE userId=?", new Object[] {settings.get(ForumModule.SETTINGS_LABEL_TYPE), userId});
    }

    private String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY " + sort;
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }

    private int getCount(Collection list)
    {
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
            if(map.containsKey("intCount"))
                return ((Number) map.get("intCount")).intValue();
        }
        return 0;
    }
}
