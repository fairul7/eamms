package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 26, 2003
 * Time: 2:18:03 PM
 * To change this template use Options | File Templates.
 */
public class ContentPublisherDaoMsSql extends ContentPublisherDao {
    public Collection selectByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
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
                    "WHERE s.id IN (" +
                    "SELECT DISTINCT s.id " +
                    "FROM " + getTableName() + " p " +
                    "INNER JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "INNER JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "INNER JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.[role] = rp2.[role] " +
                    "WHERE 1=1");
			// note: role was changed to [role] to make it compatible with Sybase
			// changed: to INNER JOIN

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

}
