package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Transaction;
import kacang.util.Log;

import java.util.*;
import java.util.Date;
import java.sql.*;

public class ContentManagerDaoOracle extends ContentManagerDao
{
	/**
     * Called when creating new content.
     * @param contentObject
     * @return
     * @throws kacang.model.DaoException
     */
    public int insertNew(ContentObject contentObject) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // insert into version table
            int result = tx.update(
                    "INSERT INTO " + getTableName() + "_version (id, version, approved, approvalDate, approvalUser, approvalUserId, submissionDate, submissionUser, submissionUserId, comments) " +
                    "VALUES (#id#, #version#, #approved#, #approvalDate#, #approvalUser#, #approvalUserId#, #submissionDate#, #submissionUser#, #submissionUserId#, #comments2#)", contentObject);

            // insert into work table
            result = tx.update(
                    "INSERT INTO " + getTableName() + "_work (id, className, version, name, description, summary, author, \"DATE\", related, contents) " +
                    "VALUES (#id#, #className#, #version#, #name#, #description#, #summary#, #author#, #date#, #related#, #contents#)", contentObject);

            // insert into status table
            result = tx.update(
                    "INSERT INTO " + getTableName() + "_status (id, new, modified, deleted, archived, published, approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion) " +
                    "VALUES (#id#, #new#, #modified#, #deleted#, #archived#, #published#, #approved#, #submitted#, #checkedOut#, #checkOutDate#, #checkOutUser#, #checkOutUserId#, #aclId#, #parentId#, #ordering#, #template#, #startDate#, #endDate#, #publishDate#, #publishUser#, #publishUserId#, #publishVersion#)", contentObject);

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
     * Called when creating a new version (when checking out to edit existing content).
     * @param contentObject
     * @return
     * @throws kacang.model.DaoException
     */
    public int insertVersion(ContentObject contentObject) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // insert into version table
            int result = tx.update(
                    "INSERT INTO " + getTableName() + "_version (id, version, approved, approvalDate, approvalUser, approvalUserId, submissionDate, submissionUser, submissionUserId, comments) " +
                    "VALUES (#id#, #version#, #approved#, #approvalDate#, #approvalUser#, #approvalUserId#, #submissionDate#, #submissionUser#, #submissionUserId#, #comments#)", contentObject);

            // update work table
            result = tx.update(
                    "UPDATE " + getTableName() + "_work SET version=#version#, name=#name#, description=#description#, summary=#summary#, author=#author#, \"DATE\"=#date#, related=#related#, contents=#contents# " +
                    "WHERE id=#id#", contentObject);

            // update status table
            result = tx.update(
                    "UPDATE " + getTableName() + "_status SET \"NEW\"=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
                    "WHERE id=#id#", contentObject);

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
     * Updates an existing version. Called when saving content (without submitting for approval).
     * @param contentObject
     * @return
     * @throws kacang.model.DaoException
     */
    public int updateVersion(ContentObject contentObject) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // update version table
            int result = tx.update(
                    "UPDATE " + getTableName() + "_version SET approved=#approved#, approvalDate=#approvalDate#, approvalUser=#approvalUser#, approvalUserId=#approvalUserId#, submissionDate=#submissionDate#, submissionUser=#submissionUser#, submissionUserId=#submissionUserId#, comments=#comments# " +
                    "WHERE id=#id# AND version=#version#", contentObject);

            // update work table
            result = tx.update(
                    "UPDATE " + getTableName() + "_work SET version=#version#, name=#name#, description=#description#, summary=#summary#, author=#author#, \"DATE\"=#date#, related=#related#, contents=#contents# " +
                    "WHERE id=#id#", contentObject);

            // update status table
            result = tx.update(
                    "UPDATE " + getTableName() + "_status SET \"NEW\"=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
                    "WHERE id=#id#", contentObject);

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
     * Updates status flags. Called when submitting for approval, approving, rejecting, undo-ing checkout.
     * @param contentObject
     * @return
     * @throws kacang.model.DaoException
     */
    public int updateStatus(ContentObject contentObject) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // update status table
            int result = tx.update(
                    "UPDATE " + getTableName() + "_status SET \"NEW\"=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template# " +
                    "WHERE id=#id#", contentObject);

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
     * Updates status flags, checkout and publishing details. Called when publishing, withdrawing and deleting.
     * @param contentObject
     * @return
     * @throws kacang.model.DaoException
     */
    public int updateFullStatus(ContentObject contentObject) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // update status table
            int result = tx.update(
                    "UPDATE " + getTableName() + "_status SET \"NEW\"=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
                    "WHERE id=#id#", contentObject);

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
                    "SELECT s.id, \"NEW\", modified, deleted, archived, published, s.approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion,  " +
                    "className, w.version, name, description, summary, author, \"DATE\", related, contents, " +
                    "approvalDate, approvalUser, approvalUserId, submissionDate, submissionUser, submissionUserId, comments " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "LEFT JOIN " + getTableName() + "_version v ON w.id=v.id AND w.version=v.version " +
                    "WHERE s.id=?", DefaultContentObject.class, params, 0, 1);
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
     * Called to retrieve a specific version of a content object.
     * @param key
     * @param version
     * @return
     * @throws kacang.model.DataObjectNotFoundException
     * @throws kacang.model.DaoException
     */
    public ContentObject selectByVersion(String key, String version) throws DataObjectNotFoundException, DaoException {
        try {
            String[] params = new String[] { key, version };
            Collection results = super.select(
                    "SELECT s.id, \"NEW\", modified, deleted, archived, published, s.approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion, " +
                    "className, v.version, name, description, summary, author, \"DATE\", related, contents, " +
                    "approvalDate, approvalUser, approvalUserId, submissionDate, submissionUser, submissionUserId, comments " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "LEFT JOIN " + getTableName() + "_version v ON w.id=v.id " +
                    "WHERE s.id=? AND v.version=?", DefaultContentObject.class, params, 0, 1);
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
     * @param name
     * @param parentId
     * @param fromDate
     * @param toDate
     * @param checkedOut
     * @param submitted
     * @param approved
     * @param archived
     * @param published
     * @param deleted
     * @param checkOutUserId
     * @param includeContents
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of DefaultContentObject objects
     * @throws kacang.model.DaoException
     */
    public Collection selectByCriteria(String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "s.id, \"NEW\", modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, description, summary, author, \"DATE\", related, contents " :
                    "s.id, \"NEW\", modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, author, \"DATE\", related ";

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
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
            }

            if (fromDate != null) {
                sql.append(" AND (\"DATE\" >= ?)");
                paramList.add(fromDate);
            }

            if (toDate != null) {
                sql.append(" AND (\"DATE\" <= ?)");
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
				if(sort.equals("date"))
					sql.append("\"DATE\"");
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

	/**
     * Called to retrieve a Collection of content objects based on criteria.
     * @param principalIds
     * @param permissionId
     * @param keys
     * @param classes
     * @param name
     * @param parentId
     * @param fromDate
     * @param toDate
     * @param checkedOut
     * @param submitted
     * @param approved
     * @param archived
     * @param published
     * @param deleted
     * @param checkOutUserId
     * @param includeContents
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of DefaultContentObject objects
     * @throws kacang.model.DaoException
     */
    public Collection selectByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, boolean includeContents, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            String columns = (includeContents) ?
                    "s.id, \"NEW\", modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, description, summary, author, \"DATE\", related, contents " :
                    "s.id, \"NEW\", modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, author, \"DATE\", related ";
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

            if (fromDate != null) {
                sql.append(" AND \"DATE\">=?");
                paramList.add(fromDate);
            }

            if (toDate != null) {
                sql.append(" AND \"DATE\"<=?");
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

			sql.append(")");

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
				if(sort.equals("date"))
					sql.append("\"" + sort.toUpperCase() + "\"");
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

	/**
     * Retrieves a Collection of content related by keywords
     * @param key
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of DefaultContentObject objects
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
                    "SELECT DISTINCT s.id, \"NEW\", modified, deleted, archived, published, approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion, " +
                    "className, w.version, name, description, author, \"DATE\", related " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "INNER JOIN " + getTableName() + "_keyword k ON s.id=k.id " +
                    "WHERE s.id <> ? " +
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

            Collection results = super.select(sql.toString(), DefaultContentObject.class, paramList.toArray(), start, rows);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

	/**
     * Called when updating keywords for a content object.
     * @param contentObject
     * @throws kacang.model.DaoException
     */
    public void updateRelated(ContentObject contentObject, String[] keywordArray) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from keywords table
            Object[] args = { contentObject.getId() };
            tx.update("DELETE FROM " + getTableName() + "_keyword WHERE id=?", args);

            // insert into keywords table
            for (int i=0; i<keywordArray.length; i++) {
                String kw = keywordArray[i];
                args = new Object[] { kw };
                Collection col = tx.select("SELECT keyword FROM " + getTableName() + "_keyword WHERE keyword=?", HashMap.class, args, 0, 1);
                if (col.size() == 0) {
                    tx.update(
                        "INSERT INTO " + getTableName() + "_keyword (id, keyword) " +
                        "VALUES (' ', ?)", args);
                }
                args = new Object[] { contentObject.getId(), kw };
                tx.update(
                    "INSERT INTO " + getTableName() + "_keyword (id, keyword) " +
                    "VALUES (?, ?)", args);
            }

            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }
}
