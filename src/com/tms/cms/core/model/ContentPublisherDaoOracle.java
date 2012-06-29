package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Transaction;
import kacang.util.Log;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContentPublisherDaoOracle extends ContentPublisherDao
{
	public Collection selectSideBarSearchByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {

		try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
                    "\"NEW\", modified, archived, aclId, parentId, sourceDate, localSource, foreignSource " :
                    "p.id, className, version, name, summary, author, \"DATE\", keywords, related, " +
                    "\"NEW\", modified, archived, aclId, parentId ";

            String query = "SELECT " +
                    columns +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "WHERE s.id IN (" +
                    "SELECT DISTINCT s.id " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1";

            if (keys != null && keys.length > 0) {
                query += " AND p.id IN (";
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        query += ",";
                    query += "?";
                }
                query += ")";
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                query += " AND className IN (";
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        query += ",";
                    query += "?";
                }
                query += ")";
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null && !"".equals(name)) {
                query += " AND ( REGEXP_LIKE(name, ?, 'i') OR REGEXP_LIKE(summary, ?, 'i') OR REGEXP_LIKE(contents, ?, 'i') ) ";
                paramList.add(name);
                paramList.add(name);
                paramList.add(name);
            }

            if (principalIds != null && principalIds.length > 0) {
                query += " AND principalId IN (";
                for (int i=0; i<principalIds.length; i++) {
                    if (i > 0)
                        query += ", ";
                    query += "?";
                }
                query += ")";
                paramList.addAll(Arrays.asList(principalIds));
            }

            if (permissionId != null) {
                query += " AND permissionId=?";
                paramList.add(permissionId);
            }

            if (parentId != null) {
                query += " AND parentId=?";
                paramList.add(parentId);
            }

            if (archived != null) {
                query += " AND archived=?";
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                query += " AND \"DATE\">=?";
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                query += " AND \"DATE\"<=?";
                paramList.add(endDate);
            }

			query += ") ";

            if (sort != null && sort.trim().length() > 0) {
                query += " ORDER BY ";
                if(sort.equals("date"))
					query += "\"DATE\"";
				else if(sort.equals("new"))
					query += "\"NEW\"";
				else
                	query += sort;
                if (desc)
                    query += " DESC";
            }
            else {
                query += " ORDER BY ordering DESC, \"DATE\" DESC";
            }

            // custom JDBC for performance
            long startTime = System.currentTimeMillis();
            long queryTime = startTime;
            Collection results = new ArrayList();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            //int currentRow = 0;

            try {
                startTime = System.currentTimeMillis();

                // modify for oracle
                Object[] objList = new Object[paramList.size()];
                int k=0;
                for (Iterator iParam=paramList.iterator();iParam.hasNext();k++) {
                    objList[k]=iParam.next();
                }
                Collection col = super.select(query,HashMap.class,objList,start,rows);
                if (col!=null && col.size()>0) {
                    for (Iterator i=col.iterator();i.hasNext();) {
                        HashMap map = (HashMap)i.next();
                        ContentObject co = new DefaultContentObject();
                        co.setId((String)map.get("id"));
                        co.setClassName((String)map.get("className"));
                        co.setVersion(((Number)map.get("version")).toString());
                        co.setName((String)map.get("name"));
                        co.setSummary((String)map.get("summary"));
                        co.setAuthor((String)map.get("author"));
                        co.setDate((Date)map.get("date"));
                        co.setRelated((String)map.get("related"));
                        if (includeContents) {
                            co.setContents((String)map.get("contents"));
                        }
                        String num = (String)map.get("new");
                        co.setNew((num!=null && num.equals("1")));
                        num = (String)map.get("modified");
                        co.setModified((num!=null && num.equals("1")));
                        num = (String)map.get("archived");
                        co.setArchived((num!=null && num.equals("1")));
                        co.setAclId((String)map.get("aclId"));
                        co.setParentId((String)map.get("parentId"));
                        results.add(co);
                    }
                }

                return results;
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
                Log.getLog(getClass()).debug("selectByCriteria [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + query);
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

	public int selectSideBarSearchResultsCountByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived) throws DaoException {
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

            if (name != null && !"".equals(name)) {
                sql.append(" AND ( REGEXP_LIKE(name, ?, 'i') OR REGEXP_LIKE(summary, ?, 'i') OR REGEXP_LIKE(contents, ?, 'i') ) ");
                paramList.add(name);
                paramList.add(name);
                paramList.add(name);
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
                sql.append(" AND \"DATE\">=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND \"DATE\"<=?");
                paramList.add(endDate);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                int total1 = ((Number)resultMap.get("total")).intValue();
                return total1;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
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
                    "INSERT INTO " + getTableName() + " (id, className, version, name, description, summary, author, \"DATE\", keywords, related, contents) " +
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
                    "SELECT p.id, className, version, name, description, summary, author, \"DATE\", keywords, related, contents, " +
                    "\"NEW\", modified, archived, aclId, parentId, ordering, template " +
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
    public Collection selectByCriteria(String[] keys, String[] classes, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
                    "\"NEW\", modified, archived, aclId, parentId " :
                    "p.id, className, version, name, author, \"DATE\", keywords, related, " +
                    "\"NEW\", modified, archived, aclId, parentId ";

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

            if (name != null && !"".equals(name)) {
                sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
                paramList.add(name);
            }

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
                sql.append(sort);
                if (desc)
                    sql.append(" DESC");
            }
            else {
                sql.append(" ORDER BY ordering DESC, \"DATE\" DESC");
            }

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
                    "p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
                    "\"NEW\", modified, archived, aclId, parentId " :
                    "p.id, className, version, name, author, \"DATE\", keywords, related, " +
                    "\"NEW\", modified, archived, aclId, parentId ";

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

            if (name != null && !"".equals(name)) {
                sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
                paramList.add(name);
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
                sql.append(" AND \"DATE\">=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND \"DATE\"<=?");
                paramList.add(endDate);
            }

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
                if(sort.equals("date"))
					sql.append("\"DATE\"");
				else if(sql.equals("new"))
					sql.append("\"NEW\"");
				else
                	sql.append(sort);
                if (desc)
                    sql.append(" DESC");
            }
            else {
                sql.append(" ORDER BY ordering DESC, \"DATE\" DESC");

            }

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

            if (name != null && !"".equals(name)) {
                sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
                paramList.add(name);
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
                sql.append(" AND \"DATE\">=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND \"DATE\"<=?");
                paramList.add(endDate);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                Integer totalStr = new Integer(((Number)resultMap.get("total")).intValue());
                //int total = totalStr.intValue();
                return totalStr.intValue();
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
        try {
        	Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
                    "\"NEW\", modified, archived, aclId, parentId " :
                    "p.id, className, version, name, author, \"DATE\", keywords, related, " +
                    "\"NEW\", modified, archived, aclId, parentId ";

            StringBuffer sql = new StringBuffer(
                    "SELECT " +
                    columns +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "WHERE s.id IN (" +
                    "SELECT DISTINCT s.id " +
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

            if (name != null && !"".equals(name)) {
                sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
                paramList.add(name);
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

			sql.append(") ");

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
				if(sort.equals("date"))
					sql.append("\"DATE\"");
				else if(sql.equals("new"))
					sql.append("\"NEW\"");
				else
                	sql.append(sort);
                if (desc)
                    sql.append(" DESC");
            }
            else {
            	sql.append(" ORDER BY ordering DESC, \"DATE\" DESC");
            }

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
    public Collection selectByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
                    "\"NEW\", modified, archived, aclId, parentId " :
                    "p.id, className, version, name, author, \"DATE\", keywords, related, " +
                    "\"NEW\", modified, archived, aclId, parentId ";

            String query = "SELECT " +
                    columns +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "WHERE s.id IN (" +
                    "SELECT DISTINCT s.id " +
                    "FROM " + getTableName() + " p " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1";

            if (keys != null && keys.length > 0) {
                query += " AND p.id IN (";
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        query += ",";
                    query += "?";
                }
                query += ")";
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                query += " AND className IN (";
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        query += ",";
                    query += "?";
                }
                query += ")";
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null && !"".equals(name)) {
                query += " AND REGEXP_LIKE(name, ?, 'i') ";
                paramList.add(name);
            }

            if (principalIds != null && principalIds.length > 0) {
                query += " AND principalId IN (";
                for (int i=0; i<principalIds.length; i++) {
                    if (i > 0)
                        query += ", ";
                    query += "?";
                }
                query += ")";
                paramList.addAll(Arrays.asList(principalIds));
            }

            if (permissionId != null) {
                query += " AND permissionId=?";
                paramList.add(permissionId);
            }

            if (parentId != null) {
                query += " AND parentId=?";
                paramList.add(parentId);
            }

            if (archived != null) {
                query += " AND archived=?";
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                query += " AND \"DATE\">=?";
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                query += " AND \"DATE\"<=?";
                paramList.add(endDate);
            }

			query += ") ";

            if (sort != null && sort.trim().length() > 0) {
                query += " ORDER BY ";
                if(sort.equals("date"))
					query += "\"DATE\"";
				else if(sort.equals("new"))
					query += "\"NEW\"";
				else
                	query += sort;
                if (desc)
                    query += " DESC";
            }
            else {
                query += " ORDER BY ordering DESC, \"DATE\" DESC";
            }

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
                pstmt = con.prepareStatement(query);
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
                Log.getLog(getClass()).debug("selectByPermission [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + query);
            }

            /*try{

                results = super.select(query, DefaultContentObject.class, paramList.toArray(), start, rows);
                return results;
            }
            finally {
                Log.getLog(getClass()).debug("selectByPermission");
            }*/
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
                    "SELECT DISTINCT p.id, className, version, name, description, author, \"DATE\", keywords, related, " +
                    "\"NEW\", modified, archived, aclId, parentId, ordering, template " +
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
                sql.append("ORDER BY \"DATE\" DESC");
            }

            /*Collection results = super.select(sql.toString(), DefaultContentObject.class, paramList.toArray(), start, rows);*/

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
                        //co.setSummary(rs.getString("summary"));
                        co.setAuthor(rs.getString("author"));
                        co.setDate(rs.getTimestamp("date"));
                        co.setRelated(rs.getString("related"));
                        //co.setContents(rs.getString("contents"));
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

            if (name != null && !"".equals(name)) {
                sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
                paramList.add(name);
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
                sql.append(" AND \"DATE\">=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND \"DATE\"<=?");
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

	/* Mod for custom Khazanah behaviour */
	public Collection selectByPermissionMultipleParent(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String[] parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException
	{
		try
		{
			Collection paramList = new ArrayList();
			String columns = (includeContents) ?
					"p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
					"\"NEW\", modified, archived, aclId, parentId " :
					"p.id, className, version, name, summary, author, \"DATE\", keywords, related, " +
					"\"NEW\", modified, archived, aclId, parentId ";

			String query = "SELECT " +
					columns +
					"FROM " + getTableName() + " p " +
					"LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
					"WHERE s.id IN (" +
					"SELECT DISTINCT s.id " +
					"FROM " + getTableName() + " p " +
					"LEFT JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
					"LEFT JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
					"LEFT JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.role = rp2.role " +
					"WHERE 1=1";

			if (keys != null && keys.length > 0)
			{
				query += " AND p.id IN (";
				for (int i=0; i<keys.length; i++)
				{
					if (i > 0)
						query += ",";
					query += "?";
				}
				query += ")";
				paramList.addAll(Arrays.asList(keys));
			}
			if (classes != null && classes.length > 0)
			{
				query += " AND className IN (";
				for (int i=0; i<classes.length; i++)
				{
					if (i > 0)
						query += ",";
					query += "?";
				}
				query += ")";
				paramList.addAll(Arrays.asList(classes));
			}
			if (name != null && !"".equals(name))
			{
				query += " AND REGEXP_LIKE(name, ?, 'i') ";
				paramList.add(name);
			}
			if (principalIds != null && principalIds.length > 0)
			{
				query += " AND principalId IN (";
				for (int i=0; i<principalIds.length; i++)
				{
					if (i > 0)
						query += ", ";
					query += "?";
				}
				query += ")";
				paramList.addAll(Arrays.asList(principalIds));
			}
			if (permissionId != null)
			{
				query += " AND permissionId=?";
				paramList.add(permissionId);
			}
			if (parentId != null)
			{
				if(parentId.length > 0)
				{
					String parentQuery = "";
					for(int i=0; i<parentId.length; i++)
					{
						if(!("".equals(parentQuery)))
							parentQuery += ",";
						parentQuery += "?";
						paramList.add(parentId[i]);
					}
					query += " AND parentId IN (" + parentQuery + ")";
				}
			}
			if (archived != null) {
				query += " AND archived=?";
				paramList.add(archived);
			}
			if(startDate!=null) {
				// only startDate specified
				query += " AND \"DATE\">=?";
				paramList.add(startDate);

			}
			if(endDate!=null)
			{
				// only endDate specified
				query += " AND \"DATE\"<=?";
				paramList.add(endDate);
			}
			query += ") ";
			if (sort != null && sort.trim().length() > 0)
			{
				query += " ORDER BY ";
				if(sort.equals("date"))
					query += "\"DATE\"";
				else if(sort.equals("new"))
					query += "\"NEW\"";
				else
					query += sort;
				if (desc)
					query += " DESC";
			}
			else
			{
				query += " ORDER BY ordering DESC";
			}

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
                pstmt = con.prepareStatement(query);
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
                        co.setSummary(rs.getString("summary"));
                        co.setAuthor(rs.getString("author"));
                        co.setDate(rs.getTimestamp("date"));
                        co.setRelated(rs.getString("related"));
						if(includeContents)
                        	co.setContents(rs.getString("contents"));
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
                Log.getLog(getClass()).debug("selectByPermission [" + (queryTime - startTime) + " ms, " + (endTime - queryTime) + " ms] " + query);
            }

			/*Collection results = new ArrayList();
			try{
				results = super.select(query, DefaultContentObject.class, paramList.toArray(), start, rows);
				return results;
			}
			finally {
				Log.getLog(getClass()).debug("selectByPermission");
			}*/
		}
		catch(Exception e) {
			throw new DaoException(e.toString());
		}
	}

	public int selectCountByPermissionMultipleParent(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String[] parentId, Boolean archived) throws DaoException {
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

            if (name != null && !"".equals(name)) {
                sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
                paramList.add(name);
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

            if (parentId != null)
			{
				if(parentId.length > 0)
				{
                    String parentQuery = "";
					for(int i=0; i<parentId.length; i++)
					{
						if(!("".equals(parentQuery)))
							parentQuery += ",";
						parentQuery += "?";
						paramList.add(parentId[i]);
					}
					sql.append(" AND parentId IN (" + parentQuery + ")");
				}
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                sql.append(" AND \"DATE\">=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND \"DATE\"<=?");
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

	public Collection selectByCriteriaMultipleParent(String[] keys, String[] classes, Date startDate, Date endDate, String name, String[] parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
		try {
			Collection paramList = new ArrayList();
			String columns = (includeContents) ?
					"p.id, className, version, name, summary, author, \"DATE\", keywords, related, contents, " +
					"\"NEW\", modified, archived, aclId, parentId " :
					"p.id, className, version, name, summary, author, \"DATE\", keywords, related, " +
					"\"NEW\", modified, archived, aclId, parentId ";

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

			if (name != null && !"".equals(name)) {
				sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
				paramList.add(name);
			}

			if (parentId != null)
			{
				if(parentId.length > 0)
				{
                    String parentQuery = "";
					for(int i=0; i<parentId.length; i++)
					{
						if(!("".equals(parentQuery)))
							parentQuery += ",";
						parentQuery += "?";
						paramList.add(parentId[i]);
					}
					sql.append(" AND parentId IN (" + parentQuery + ")");
				}
            }

			if (archived != null) {
				sql.append(" AND archived=?");
				paramList.add(archived);
			}

			if(startDate!=null) {
				// only startDate specified
				sql.append(" AND \"DATE\">=?");
				paramList.add(startDate);

			}

			if(endDate!=null) {
				// only endDate specified
				sql.append(" AND \"DATE\"<=?");
				paramList.add(endDate);
			}

			if (sort != null && sort.trim().length() > 0) {
				sql.append(" ORDER BY ");
				if(sort.equals("date"))
					sql.append("\"DATE\"");
				else if(sql.equals("new"))
					sql.append("\"NEW\"");
				else
					sql.append(sort);
				if (desc)
					sql.append(" DESC");
			}
			else {
				sql.append(" ORDER BY ordering DESC");
			}

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
						co.setSummary(rs.getString("summary"));
						co.setAuthor(rs.getString("author"));
						co.setDate(rs.getTimestamp("date"));
						co.setRelated(rs.getString("related"));
						if (includeContents) {
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

	public int selectCountByCriteriaMultipleParent(String[] keys, String[] classes, Date startDate, Date endDate, String name, String[] parentId, Boolean archived) throws DaoException {
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

			if (name != null && !"".equals(name)) {
				sql.append(" AND REGEXP_LIKE(name, ?, 'i') ");
				paramList.add(name);
			}

			if (parentId != null)
			{
				if(parentId.length > 0)
				{
                    String parentQuery = "";
					for(int i=0; i<parentId.length; i++)
					{
						if(!("".equals(parentQuery)))
							parentQuery += ",";
						parentQuery += "?";
						paramList.add(parentId[i]);
					}
					sql.append(" AND parentId IN (" + parentQuery + ")");
				}
            }

			if (archived != null) {
				sql.append(" AND archived=?");
				paramList.add(archived);
			}

			if(startDate!=null) {
				// only startDate specified
				sql.append(" AND \"DATE\">=?");
				paramList.add(startDate);

			}

			if(endDate!=null) {
				// only endDate specified
				sql.append(" AND \"DATE\"<=?");
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

}
