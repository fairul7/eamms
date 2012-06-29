package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 19, 2003
 * Time: 5:20:16 PM
 * To change this template use Options | File Templates.
 */
public class ContentPublisherDao extends DataSourceDao {

    public String getTableName() {
        return "cms_content_published";
    }

    public String getManagerTablePrefix() {
        return "cms_content";
    }

    /**
     * Called when publishing new content.
     * @param contentObject
     * @return
     * @throws kacang.model.DaoException
     */
    public int insert(ContentObject contentObject) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from table
            String[] params = new String[] { contentObject.getId() };
            int result = tx.update(
                    "DELETE FROM " + getTableName() + " WHERE id=?", params);

            // insert into published table
            result = tx.update(
                    "INSERT INTO " + getTableName() + " (id, className, version, name, description, summary, author, date, keywords, related, contents) " +
                    "VALUES (#id#, #className#, #version#, #name#, #description#, #summary#, #author#, #date#, #keywords#, #related#, #contents#)", contentObject);

            tx.commit();

            return result;
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called when withdrawing content.
     * @param key
     * @return
     * @throws kacang.model.DaoException
     */
    public int delete(String key) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from table
            String[] params = new String[] { key };
            int result = tx.update(
                    "DELETE FROM " + getTableName() + " WHERE id=?", params);

            tx.commit();

            return result;
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called when selecting a specific content object.
     * @param key
     * @return
     * @throws kacang.model.DataObjectNotFoundException
     * @throws kacang.model.DaoException
     */
    public ContentObject selectById(String key) throws DataObjectNotFoundException, DaoException {
        try {
            String[] params = new String[] { key };
            Collection results = super.select(
                    "SELECT p.id, className, version, name, description, summary, author, date, keywords, related, contents, " +
                    "new, modified, archived, aclId, parentId, ordering, template " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "WHERE p.id=?", DefaultContentObject.class, params, 0, 1);
            if (results.size() == 0) {
                throw new DataObjectNotFoundException(key);
            }
            return (ContentObject)results.iterator().next();
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }


    /**
     * Called to retrieve a Collection of content objects based on criteria.
     * @param keys
     * @param classes
     * @param parentId
     * @param archived
     * @param includeContents
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws kacang.model.DaoException
     */
    public Collection selectByCriteria(String[] keys, String[] classes, String name, String parentId,
       Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        return selectByCriteria(keys, classes, null, null, name, parentId, archived, includeContents, sort, desc, start,rows);
    }

    /**
     * Called to retrieve a Collection of content objects based on criteria.
     *
     * @param keys
     * @param classes
     * @param startDate specifies a start date for the date range
     * @param endDate specifies an end date for the date range
     * @param parentId
     * @param archived
     * @param includeContents
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws kacang.model.DaoException
     */
    public Collection selectByCriteria(String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "p.id, className, version, name, summary, author, date, keywords, related, contents, " +
                    "new, modified, archived, aclId, parentId " :
                    "p.id, className, version, name, author, date, keywords, related, " +
                    "new, modified, archived, aclId, parentId ";

            StringBuffer sql = new StringBuffer(
                    "SELECT " +
                    columns +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND p.id IN (");
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                sql.append(" AND className IN (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null) {
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
            }

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                sql.append(" AND date>=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND date<=?");
                paramList.add(endDate);
            }

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
                sql.append(sort);
                if (desc)
                    sql.append(" DESC");
            }
            else {
                sql.append(" ORDER BY ordering DESC, date DESC");
            }

/*
            Collection results = super.select(sql.toString(), DefaultContentObject.class, paramList.toArray(), start, rows);
            return results;
*/
            // custom JDBC for performance
            long startTime = System.currentTimeMillis();
            long queryTime = startTime;
            Collection results = new ArrayList();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            int currentRow = 0;

            try {
                startTime = System.currentTimeMillis();
                con = getDataSource().getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int c=1;
                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
                    pstmt.setObject(c, i.next());
                }
                if (rows > 0) {
                    pstmt.setMaxRows(start + rows);
                }
                rs = pstmt.executeQuery();
                queryTime = System.currentTimeMillis();
                while(rs.next()) {
                    if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
                        ContentObject co = new DefaultContentObject();
                        co.setId(rs.getString("id"));
                        co.setClassName(rs.getString("className"));
                        co.setVersion(rs.getString("version"));
                        co.setName(rs.getString("name"));
                        co.setAuthor(rs.getString("author"));
                        co.setDate(rs.getTimestamp("date"));
                        co.setRelated(rs.getString("related"));
                        if (includeContents) {
                            co.setSummary(rs.getString("summary"));
                            co.setContents(rs.getString("contents"));
                        }
                        co.setNew(rs.getInt("new") == 1);
                        co.setModified(rs.getInt("modified") == 1);
                        co.setArchived(rs.getInt("archived") == 1);
                        co.setAclId(rs.getString("aclId"));
                        co.setParentId(rs.getString("parentId"));
                        results.add(co);
                    }
                    currentRow++;
                }
                return results;
            }
            catch (SQLException e) {
                throw e;
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
                long endTime = System.currentTimeMillis();
                Log.getLog(getClass()).debug("selectByCriteria [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + sql);
            }

        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }


    public int selectCountByCriteria(String[] keys, String[] classes, String name, String parentId, Boolean archived) throws DaoException {
        return selectCountByCriteria(keys, classes, null, null, name, parentId, archived);
    }

    public int selectCountByCriteria(String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(*) AS total " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND p.id IN (");
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                sql.append(" AND className IN (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null) {
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
            }

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                sql.append(" AND date>=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND date<=?");
                paramList.add(endDate);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                Integer totalStr = new Integer(((Number)resultMap.get("total")).intValue());
                int total = totalStr.intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called to retrieve a Collection of content objects based on criteria.
     * @param keys
     * @param classes
     * @param principalIds
     * @param permissionId
     * @param parentId
     * @param archived
     * @param includeContents
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws kacang.model.DaoException
     */
    public Collection selectByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        return selectByPermission(principalIds, permissionId, keys, classes, null, null, name, parentId, archived, includeContents,  sort, desc,  start, rows);
    }

    /**
     * Called to retrieve a Collection of content objects based on criteria.
     * @param startDate
     * @param endDate
     * @param keys
     * @param classes
     * @param principalIds
     * @param permissionId
     * @param parentId
     * @param archived
     * @param includeContents
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws kacang.model.DaoException
     */
    public Collection selectByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes,
                                         Date startDate, Date endDate, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "p.id, className, version, name, summary, author, date, keywords, related, contents, " +
                    "new, modified, archived, aclId, parentId " :
                    "p.id, className, version, name, author, date, keywords, related, " +
                    "new, modified, archived, aclId, parentId ";

            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT " +
                    columns +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND p.id IN (");
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                sql.append(" AND className IN (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null) {
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
            }

            if (principalIds != null && principalIds.length > 0) {
                sql.append(" AND principalId IN (");
                for (int i=0; i<principalIds.length; i++) {
                    if (i > 0)
                        sql.append(", ");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(principalIds));
            }

            if (permissionId != null) {
                sql.append(" AND permissionId=?");
                paramList.add(permissionId);
            }

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                sql.append(" AND date>=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND date<=?");
                paramList.add(endDate);
            }

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
                sql.append(sort);
                if (desc)
                    sql.append(" DESC");
            }
            else {
                sql.append(" ORDER BY ordering DESC, date DESC");
            }

/*
            Collection results = super.select(sql.toString(), DefaultContentObject.class, paramList.toArray(), start, rows);
            return results;
*/
            // custom JDBC for performance
            long startTime = System.currentTimeMillis();
            long queryTime = startTime;
            Collection results = new ArrayList();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            int currentRow = 0;

            try {
                startTime = System.currentTimeMillis();
                con = getDataSource().getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int c=1;
                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
                    pstmt.setObject(c, i.next());
                }
                if (rows > 0) {
                    pstmt.setMaxRows(start + rows);
                }
                rs = pstmt.executeQuery();
                queryTime = System.currentTimeMillis();
                while(rs.next()) {
                    if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
                        ContentObject co = new DefaultContentObject();
                        co.setId(rs.getString("id"));
                        co.setClassName(rs.getString("className"));
                        co.setVersion(rs.getString("version"));
                        co.setName(rs.getString("name"));
                        co.setAuthor(rs.getString("author"));
                        co.setDate(rs.getTimestamp("date"));
                        co.setRelated(rs.getString("related"));
                        if (includeContents) {
                            co.setSummary(rs.getString("summary"));
                            co.setContents(rs.getString("contents"));
                        }
                        co.setNew(rs.getInt("new") == 1);
                        co.setModified(rs.getInt("modified") == 1);
                        co.setArchived(rs.getInt("archived") == 1);
                        co.setAclId(rs.getString("aclId"));
                        co.setParentId(rs.getString("parentId"));
                        results.add(co);
                    }
                    currentRow++;
                }
                return results;
            }
            catch (SQLException e) {
                throw e;
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
                long endTime = System.currentTimeMillis();
                Log.getLog(getClass()).debug("selectByPermission [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + sql);
            }

        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }


    public int selectCountByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, String name, String parentId, Boolean archived) throws DaoException {
        return selectCountByPermission(principalIds, permissionId, keys, classes, null, null, name, parentId, archived);
    }

    public int selectCountByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(DISTINCT s.id) AS total " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND p.id IN (");
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                sql.append(" AND className IN (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null) {
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
            }

            if (principalIds != null && principalIds.length > 0) {
                sql.append(" AND principalId IN (");
                for (int i=0; i<principalIds.length; i++) {
                    if (i > 0)
                        sql.append(", ");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(principalIds));
            }

            if (permissionId != null) {
                sql.append(" AND permissionId=?");
                paramList.add(permissionId);
            }

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                sql.append(" AND date>=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND date<=?");
                paramList.add(endDate);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                int total = ((Number)resultMap.get("total")).intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     *
     * @param key
     * @return a Collection of keyword Strings.
     * @throws kacang.model.DaoException
     */
    public Collection selectKeywordsById(String key) throws DaoException {
        try {
            if (key == null || key.trim().length() == 0) {
                return new ArrayList();
            }
            String[] args = new String[] { key };
            Collection kwMapList = super.select(
                    "SELECT keyword " +
                    "FROM " + getManagerTablePrefix() + "_keyword " +
                    "WHERE id=?", HashMap.class, args, 0, -1);
            Collection kwList = new ArrayList();
            for (Iterator i=kwMapList.iterator(); i.hasNext();) {
                Map kwMap = (Map)i.next();
                String kw = (String)kwMap.get("keyword");
                if (kw != null)
                    kwList.add(kw);
            }
            return kwList;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Retrieves a Collection of content related by keywords
     * @param key
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws kacang.model.DaoException
     */
    public Collection selectRelated(String key, String[] classes, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            paramList.add(key);

            // retrieve keywords and form IN clause
            Collection keywordList = selectKeywordsById(key);
            StringBuffer inClause = new StringBuffer();
            Collection inClauseList = new ArrayList();
            for (Iterator i=keywordList.iterator(); i.hasNext();) {
                String kw = (String)i.next();
                if (kw != null) {
                    inClause.append("?");
                    inClauseList.add(kw);
                    if (i.hasNext())
                        inClause.append(",");
                }
            }
            if (inClauseList.size() == 0)
                return new ArrayList();
            paramList.addAll(inClauseList);

            // form query
            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT p.id, className, version, name, description, author, date, keywords, related, " +
                    "new, modified, archived, aclId, parentId, ordering, template " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "INNER JOIN " + getManagerTablePrefix() + "_keyword k ON s.id=k.id " +
                    "WHERE p.id <> ? " +
                    "AND archived='0' " +
                    "AND k.keyword IN (" + inClause.toString() + ") ");

            if (classes != null && classes.length > 0) {
                sql.append(" AND (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(" OR ");
                    sql.append("className=?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            if (sort != null) {
                sql.append("ORDER BY " + sort);
                if (desc) {
                    sql.append(" DESC ");
                }
            }
            else {
                sql.append("ORDER BY date DESC");
            }

            Collection results = super.select(sql.toString(), DefaultContentObject.class, paramList.toArray(), start, rows);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public int selectRelatedCount(String key, String[] classes) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            paramList.add(key);

            // retrieve keywords and form IN clause
            Collection keywordList = selectKeywordsById(key);
            StringBuffer inClause = new StringBuffer();
            Collection inClauseList = new ArrayList();
            for (Iterator i=keywordList.iterator(); i.hasNext();) {
                String kw = (String)i.next();
                if (kw != null) {
                    inClause.append("?");
                    inClauseList.add(kw);
                    if (i.hasNext())
                        inClause.append(",");
                }
            }
            if (inClauseList.size() == 0)
                return 0;
            paramList.addAll(inClauseList);

            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT COUNT(*) AS total " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "INNER JOIN " + getManagerTablePrefix() + "_keyword k ON s.id=k.id " +
                    "WHERE p.id <> ? " +
                    "AND archived='0' " +
                    "AND k.keyword IN (" + inClause.toString() + ") ");

            if (classes != null && classes.length > 0) {
                sql.append(" AND (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(" OR ");
                    sql.append("className=?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                Integer totalStr = (Integer)resultMap.get("total");
                int total = totalStr.intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }


}
