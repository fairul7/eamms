package com.tms.cms.core.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.util.Log;

public class ContentPublisherDaoDB2 extends ContentPublisherDao{
	
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
                sql.append(" AND UPPER(name) LIKE ?");
                paramList.add("%" + name.toUpperCase() + "%");
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
                sql.append(" AND UPPER(name) LIKE ?");
                paramList.add("%" + name.toUpperCase() + "%");
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
				"SELECT " +
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
	
}
