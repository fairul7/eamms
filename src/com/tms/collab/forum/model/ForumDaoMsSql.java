package com.tms.collab.forum.model;

import kacang.model.DaoQuery;
import kacang.model.DaoException;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForumDaoMsSql extends ForumDao {
    
	public Collection selectThreads(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        String sql =
            "SELECT frm_thread.threadId, frm_thread.creationDate, frm_thread.modificationDate, frm_thread.ownerId, isPublic, " +
            "active, frm_thread.subject, COUNT(DISTINCT messageId) AS numOfMessage, MAX(frm_message.creationDate) AS strLastPostDate " +
            "FROM frm_thread LEFT OUTER JOIN frm_message ON frm_thread.threadId=frm_message.threadId WHERE 1=1 ";
        sql = sql + query.getStatement() + getSort(sort, descending);
        sql = sql + "GROUP BY frm_thread.threadId, frm_thread.forumId, frm_thread.creationDate, frm_thread.modificationDate, frm_thread.ownerId, isPublic, active, frm_thread.subject";
        return super.select(sql, Thread.class, query.getArray(), start, maxResults);
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
                sql += " GROUP BY ff.forumId, ff.name, ff.creationDate, ff.modificationDate, ff.ownerId, ff.isPublic, ff.active, ff.assigneeId, ff.category ";
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
            sqlAppend += " GROUP BY ft.threadId, ft.forumId, ft.creationDate, ft.modificationDate, ft.ownerId, ft.isPublic, ft.active, ft.subject, ft.email";
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
                sql += " ORDER BY " + (sortBy.equals("content")?"cast(content AS varchar)":sortBy);
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
	
	/**
     * Selects forums that groups have access to as users.
     * @param groupIds
     * @param active
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws kacang.model.DaoException
     */
    public Collection selectForumsByUserGroupAccess(String[] groupIds, DaoQuery query, String active, String sort, boolean desc, int start, int rows) throws DaoException
    {
        Collection paramList = new ArrayList();

        // formulate SQL
        StringBuffer sql = new StringBuffer("SELECT ff2.forumId, ff2.name, ff2.description, ff2.creationDate, ff2.modificationDate, ff2.ownerId, ff2.isPublic, ff2.active, ff2.assigneeId, ff2.category " +
                "FROM frm_forum ff2 WHERE ff2.forumId IN (" +
                "SELECT DISTINCT ff.forumId FROM frm_forum AS ff LEFT OUTER JOIN frm_forum_user AS ffa ON ff.forumId=ffa.forumId " +
                "WHERE (( ff.isPublic='1' ");
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
        sql.append(") ");

        // append DaoQuery
        if (query == null) {
            query = new DaoQuery();
        }
        sql.append(query.getStatement());
        paramList.addAll(Arrays.asList(query.getArray()));
        sql.append(") ");

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
            sql.append(" ORDER BY creationDate");
        }
        return super.select(sql.toString(), Forum.class, paramList.toArray(), start, rows);
    }
}
