package com.tms.cms.core.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.util.Log;

public class ContentManagerDaoDB2 extends ContentManagerDao{
	
	public int selectKeywordsCount(String search) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(DISTINCT keyword) AS total " +
                    "FROM " + getTableName() + "_keyword ");

            if (search != null) {
                sql.append("WHERE UPPER(keyword) LIKE ? ");
                paramList.add("%" + search.toUpperCase() + "%");
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                /*Integer totalStr = (Integer)resultMap.get("total");
                int total = totalStr.intValue();*/
				int total = ((Number)resultMap.get("total")).intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }
	
	public Collection selectKeywords(String search, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT keyword " +
                    "FROM " + getTableName() + "_keyword ");

            if (search != null) {
                sql.append("WHERE UPPER(keyword) LIKE ? ");
                paramList.add("%" + search.toUpperCase() + "%");
            }

            sql.append("ORDER BY keyword ");
            if (desc) {
                sql.append(" DESC");
            }

            Collection kwMapList =  super.select(sql.toString(), HashMap.class, paramList.toArray(), start, rows);
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
	
	public int selectCountByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(DISTINCT s.id) AS total " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "LEFT JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getTableName() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND s.id IN (");
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
                sql.append(" AND UPPER(name) LIKE ?");
                paramList.add("%" + name.toUpperCase() + "%");
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

            if (fromDate != null) {
                sql.append(" AND date>=?");
                paramList.add(fromDate);
            }

            if (toDate != null) {
                sql.append(" AND date<=?");
                paramList.add(toDate);
            }

            if (checkedOut != null) {
                sql.append(" AND checkedOut=?");
                paramList.add(checkedOut);
            }

            if (submitted != null) {
                sql.append(" AND submitted=?");
                paramList.add(submitted);
            }

            if (approved != null) {
                sql.append(" AND approved=?");
                paramList.add(approved);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if (published != null) {
                sql.append(" AND published=?");
                paramList.add(published);
            }

            if (deleted != null) {
                sql.append(" AND deleted=?");
                paramList.add(deleted);
            }

            if (checkOutUserId != null) {
                sql.append(" AND checkOutUserId=?");
                paramList.add(checkOutUserId);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                /*Integer totalStr = (Integer)resultMap.get("total");
                int total = totalStr.intValue();*/
				int total = ((Number)resultMap.get("total")).intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }
	
	public int selectCountByCriteria(String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(*) AS total " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND s.id IN (");
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

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (name != null) {
                sql.append(" AND UPPER(name) LIKE ?");
                paramList.add("%" + name.toUpperCase() + "%");
            }

            if (fromDate != null) {
                sql.append(" AND date>=?");
                paramList.add(fromDate);
            }

            if (toDate != null) {
                sql.append(" AND date<=?");
                paramList.add(toDate);
            }

            if (checkedOut != null) {
                sql.append(" AND checkedOut=?");
                paramList.add(checkedOut);
            }

            if (submitted != null) {
                sql.append(" AND submitted=?");
                paramList.add(submitted);
            }

            if (approved != null) {
                sql.append(" AND approved=?");
                paramList.add(approved);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if (published != null) {
                sql.append(" AND published=?");
                paramList.add(published);
            }

            if (deleted != null) {
                sql.append(" AND deleted=?");
                paramList.add(deleted);
            }

            if (checkOutUserId != null) {
                sql.append(" AND checkOutUserId=?");
                paramList.add(checkOutUserId);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                /*Integer totalStr = (Integer)resultMap.get("total");
                int total = totalStr.intValue();*/
				int total = ((Number)resultMap.get("total")).intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }
	
	public Collection selectByCriteria(String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, description, summary, author, date, related, contents " :
                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, author, date, related ";

            StringBuffer sql = new StringBuffer(
                    "SELECT " +
                    columns +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND s.id IN (");
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

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (name != null) {
                sql.append(" AND UPPER(name) LIKE ?");
                paramList.add("%" + name.toUpperCase() + "%");
            }

            if (fromDate != null) {
                sql.append(" AND (date >= ?)");
                paramList.add(fromDate);
            }

            if (toDate != null) {
                sql.append(" AND (date <= ?)");
                paramList.add(toDate);
            }

            if (checkedOut != null) {
                sql.append(" AND checkedOut=?");
                paramList.add(checkedOut);
            }

            if (submitted != null) {
                sql.append(" AND submitted=?");
                paramList.add(submitted);
            }

            if (approved != null) {
                sql.append(" AND approved=?");
                paramList.add(approved);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if (published != null) {
                sql.append(" AND published=?");
                paramList.add(published);
            }

            if (deleted != null) {
                sql.append(" AND deleted=?");
                paramList.add(deleted);
            }

            if (checkOutUserId != null) {
                sql.append(" AND checkOutUserId=?");
                paramList.add(checkOutUserId);
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
                con = getDataSource().getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int c=1;
                Object o;
                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
                    o = i.next();
                    if (o instanceof Date) {
                        pstmt.setTimestamp(c, new Timestamp(((Date)o).getTime()));
                    }
                    else {
                        pstmt.setObject(c, o);
                    }
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
                        co.setNew(rs.getInt("new") == 1);
                        co.setModified(rs.getInt("modified") == 1);
                        co.setDeleted(rs.getInt("deleted") == 1);
                        co.setArchived(rs.getInt("archived") == 1);
                        co.setPublished(rs.getInt("published") == 1);
                        co.setApproved(rs.getInt("approved") == 1);
                        co.setSubmitted(rs.getInt("submitted") == 1);
                        co.setCheckedOut(rs.getInt("checkedOut") == 1);
                        co.setAclId(rs.getString("aclId"));
                        co.setParentId(rs.getString("parentId"));
                        co.setOrdering(rs.getString("ordering"));
                        co.setTemplate(rs.getString("template"));
                        co.setPublishVersion(rs.getString("publishVersion"));
                        co.setClassName(rs.getString("className"));
                        co.setVersion(rs.getString("version"));
                        co.setName(rs.getString("name"));
                        co.setAuthor(rs.getString("author"));
                        co.setDate(rs.getTimestamp("date"));
                        co.setRelated(rs.getString("related"));
                        if (includeContents) {
                            co.setDescription(rs.getString("description"));
                            co.setSummary(rs.getString("summary"));
                            co.setContents(rs.getString("contents"));
                        }
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
	
	public Collection selectByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, description, summary, author, date, related, contents " :
                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, author, date, related ";
            StringBuffer sql = new StringBuffer(
                    "SELECT " +
                    columns +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "WHERE s.id IN (" +
                    "SELECT DISTINCT s.id " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "LEFT JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getTableName() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND s.id IN (");
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
                sql.append(" AND UPPER(name) LIKE ?");
                paramList.add("%" + name.toUpperCase() + "%");
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

            if (fromDate != null) {
                sql.append(" AND date>=?");
                paramList.add(fromDate);
            }

            if (toDate != null) {
                sql.append(" AND date<=?");
                paramList.add(toDate);
            }

            if (checkedOut != null) {
                sql.append(" AND checkedOut=?");
                paramList.add(checkedOut);
            }

            if (submitted != null) {
                sql.append(" AND submitted=?");
                paramList.add(submitted);
            }

            if (approved != null) {
                sql.append(" AND approved=?");
                paramList.add(approved);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if (published != null) {
                sql.append(" AND published=?");
                paramList.add(published);
            }

            if (deleted != null) {
                sql.append(" AND deleted=?");
                paramList.add(deleted);
            }

            if (checkOutUserId != null) {
                sql.append(" AND checkOutUserId=?");
                paramList.add(checkOutUserId);
            }

            sql.append(") ");
            
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
                con = getDataSource().getConnection();
                pstmt = con.prepareStatement(sql.toString());
                int c=1;
                Object o;
                for (Iterator i=paramList.iterator(); i.hasNext(); c++) {
                    o = i.next();
                    if (o instanceof Date) {
                        pstmt.setTimestamp(c, new Timestamp(((Date)o).getTime()));
                    }
                    else {
                        pstmt.setObject(c, o);
                    }
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
                        co.setNew(rs.getInt("new") == 1);
                        co.setModified(rs.getInt("modified") == 1);
                        co.setDeleted(rs.getInt("deleted") == 1);
                        co.setArchived(rs.getInt("archived") == 1);
                        co.setPublished(rs.getInt("published") == 1);
                        co.setApproved(rs.getInt("approved") == 1);
                        co.setSubmitted(rs.getInt("submitted") == 1);
                        co.setCheckedOut(rs.getInt("checkedOut") == 1);
                        co.setAclId(rs.getString("aclId"));
                        co.setParentId(rs.getString("parentId"));
                        co.setOrdering(rs.getString("ordering"));
                        co.setTemplate(rs.getString("template"));
                        co.setPublishVersion(rs.getString("publishVersion"));
                        co.setClassName(rs.getString("className"));
                        co.setVersion(rs.getString("version"));
                        co.setName(rs.getString("name"));
                        co.setAuthor(rs.getString("author"));
                        co.setDate(rs.getTimestamp("date"));
                        co.setRelated(rs.getString("related"));
                        if (includeContents) {
                            co.setDescription(rs.getString("description"));
                            co.setSummary(rs.getString("summary"));
                            co.setContents(rs.getString("contents"));
                        }
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
	
}
