package com.tms.collab.forum.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.util.Log;

public class ForumDaoDB2 extends ForumDao
{
	
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
                sql += (threadId != null && !threadId.trim().equals("")?" AND " : " WHERE ") + 
                	(searchBy.equals("ownerId")?searchBy:"UPPER("+searchBy+") ");
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria.toUpperCase() + "%");
                }
            }
            if(sortBy!=null && !sortBy.trim().equals(""))
                sql += " ORDER BY " + (sortBy.equals("content")?"cast(content AS varchar(255))":sortBy);
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
                sql += (threadId != null && !threadId.trim().equals("")?" AND " : " WHERE ") + 
                	(searchBy.equals("ownerId")?searchBy:"UPPER("+searchBy+") ");
                
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria.toUpperCase() + "%");
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
                sql += (forumId != null && !forumId.trim().equals("")?" AND " : " WHERE ") + 
                	(searchBy.equals("ownerId")?searchBy:"UPPER("+searchBy+") ");
                
                if(searchBy.equals("ownerId"))
                {
                    sql+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sql+= " LIKE ?";
                    argsList.add("%" + searchCriteria.toUpperCase() + "%");
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
                      "COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate " +
                      "FROM frm_thread ft LEFT OUTER JOIN frm_message fm on ft.threadId=fm.threadId " +
                      "WHERE ft.forumId=?";
            }
            else
            {
                sql = "SELECT ft.threadId, ft.creationDate, ft.modificationDate, ft.ownerId, ft.isPublic, ft.active, ft.subject, " +
                      "COUNT(DISTINCT fm.messageId) AS numOfMessage, MAX(fm.creationDate) AS strLastPostDate " +
                      "FROM frm_thread ft LEFT OUTER JOIN frm_message fm on ft.threadId=fm.threadId ";
            }
            if(searchCriteria!=null && !searchCriteria.trim().equals(""))
            {
                sqlAppend += (forumId != null && !forumId.trim().equals("")?" AND " : " WHERE ft.") + 
                	(searchBy.equals("ownerId")? "ft."+searchBy:"UPPER(ft."+searchBy+") ");
                if(searchBy.equals("ownerId"))
                {
                    sqlAppend+= "=?";
                    argsList.add(searchCriteria);
                }
                else
                {
                    sqlAppend+= " LIKE ?";
                    argsList.add("%" + searchCriteria.toUpperCase() + "%");
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
            sql.append(" AND UPPER(VARCHAR(ff." + searchBy + ")) LIKE ?");
            paramList.add("%" + searchCriteria.toUpperCase() + "%");
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
	
	public Collection selectForumsByUserGroupAccess(String[] groupIds, DaoQuery query, String active, String sort, boolean desc, int start, int rows) throws DaoException
    {
        Collection paramList = new ArrayList();

        // formulate SQL
        StringBuffer sql = new StringBuffer("SELECT ff.forumId, ff.name, ff.description, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category " +
                "FROM frm_forum ff " +
                "WHERE ( ff.isPublic='1' ");
        if (groupIds != null && groupIds.length > 0)
        {
            sql.append(" OR ff.forumId IN ( " +
            		"SELECT forumId FROM frm_forum_user ffa WHERE ffa.groupId IN (");
            for (int i=0; i<groupIds.length; i++)
            {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")) ");
            paramList.addAll(Arrays.asList(groupIds));
        }
        sql.append(") ");
        
        if ("1".equals(active))
            sql.append(" AND ff.active='1' ");
        else if ("0".equals(active))
            sql.append(" AND ff.active='0' ");
        sql.append(" ");

        // append DaoQuery
        if (query == null) {
            query = new DaoQuery();
        }
        sql.append(query.getStatement());
        paramList.addAll(Arrays.asList(query.getArray()));

        // append sort
        if (sort != null && sort.trim().length() > 0)
        {
            sql.append(" ORDER BY ");
            sql.append(sort);
            if (desc)
                sql.append(" DESC");
        }
        else
        {
            sql.append(" ORDER BY ff.creationDate");
        }
        return super.select(sql.toString(), Forum.class, paramList.toArray(), start, rows);
    }
}
