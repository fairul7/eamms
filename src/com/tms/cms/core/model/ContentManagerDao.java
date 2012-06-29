package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.Application;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ContentManagerDao extends DataSourceDao {

    public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cms_content_subscription (userId VARCHAR(255), contentId VARCHAR(255))", null);
        }
        catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_subscription");
        }

        try {
            Application app = Application.getInstance();
            if (Boolean.valueOf(app.getProperty(ContentManager.APPLICATION_PROPERTY_ADMIN_READER_DISABLED)).booleanValue()) {
                super.update("DELETE FROM cms_content_role_permission WHERE role=? AND permissionId=?", new Object[] { "reader", ContentManager.USE_CASE_PREVIEW });
            }
            else {
                super.update("DELETE FROM cms_content_role_permission WHERE role=? AND permissionId=?", new Object[] { "reader", ContentManager.USE_CASE_PREVIEW });
                super.update("INSERT INTO cms_content_role_permission (role, permissionId) VALUES (?,?)", new Object[] { "reader", ContentManager.USE_CASE_PREVIEW });
            }
        }
        catch (DaoException e) {
            Log.getLog(getClass()).debug("Error initializing table cms_content_role_permission");
        }
    }

    public String getTableName() {
        return "cms_content";
    }

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
                    "INSERT INTO " + getTableName() + "_work (id, className, version, name, description, summary, author, date, related, contents) " +
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
                    "UPDATE " + getTableName() + "_work SET version=#version#, name=#name#, description=#description#, summary=#summary#, author=#author#, date=#date#, related=#related#, contents=#contents# " +
                    "WHERE id=#id#", contentObject);

            // update status table
            result = tx.update(
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
                    "WHERE id=#id#", contentObject);

/*
            // update keywords table
            Object[] args = { contentObject.getId() };
            tx.update("DELETE FROM " + getTableName() + "_keyword WHERE id=?", args);
            String keywords = contentObject.getKeywords();
            Collection keywordList = ContentUtil.keywordStringToCollection(keywords);
            for (Iterator i=keywordList.iterator(); i.hasNext();) {
                String kw = (String)i.next();
                args = new Object[] { contentObject.getId(), kw };
                tx.update(
                    "INSERT INTO " + getTableName() + "_keyword (id, keyword) " +
                    "VALUES (?, ?)", args);
            }
*/

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
                    "UPDATE " + getTableName() + "_work SET version=#version#, name=#name#, description=#description#, summary=#summary#, author=#author#, date=#date#, related=#related#, contents=#contents# " +
                    "WHERE id=#id#", contentObject);

            // update status table
            result = tx.update(
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
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
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template# " +
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
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#ordering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
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
     * Updates specified properties for a set of content objects.
     * @param keys Array of content IDs
     * @param propertyMap A Map containing propertyName=propertyValue pairs
     * @return the number of rows updated.
     * @throws DaoException
     */
    public int updateStatus(String[] keys, Map propertyMap) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            if (keys == null || keys.length == 0 || propertyMap == null || propertyMap.size() == 0) {
                return 0;
            }

            // formulate statement
            Collection argsList = new ArrayList();
            String columns = "";
            for (Iterator i=propertyMap.keySet().iterator(); i.hasNext();) {
                String property = (String)i.next();
                Object value = propertyMap.get(property);
                columns += "," + property + "=?";
                argsList.add(value);
            }
            columns = columns.substring(1);
            String subset = "";
            for (int i=0; i<keys.length; i++) {
                subset += ",?";
                argsList.add(keys[i]);
            }
            subset = subset.substring(1);
            String sql =
                    "UPDATE " + getTableName() + "_status " +
                    "SET " + columns +
                    " WHERE id IN (" + subset + ")";

            // update status table
            int result = tx.update(sql, argsList.toArray());

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
     * Called when destroying content.
     * @param key
     * @return
     * @throws kacang.model.DaoException
     */
    public int delete(String key) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from version table
            String[] params = new String[] { key };
            int result = tx.update(
                    "DELETE FROM " + getTableName() + "_version WHERE id=?", params);

            // delete from status table
            result = tx.update(
                    "DELETE FROM " + getTableName() + "_status WHERE id=?", params);

            // delete from work table
            result = tx.update(
                    "DELETE FROM " + getTableName() + "_work WHERE id=?", params);

            // delete from keywords table
            result = tx.update(
                    "DELETE FROM " + getTableName() + "_keyword WHERE id=?", params);

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
     * Called when removing version e.g. rollback.
     * @param key
     * @return
     * @throws kacang.model.DaoException
     */
    public int deleteVersion(String key, String version) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from version table
            String[] params = new String[] { key, version };
            int result = tx.update(
                    "DELETE FROM " + getTableName() + "_version WHERE id=? AND version=?", params);

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
                    "SELECT s.id, new, modified, deleted, archived, published, s.approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion,  " +
                    "className, w.version, name, description, summary, author, date, related, contents, " +
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
     * Called when retrieving all versions for a content object.
     * @param key
     * @return A Collection of DefaultContentObject objects
     * @throws kacang.model.DaoException
     */
    public Collection selectVersions(String key, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            String[] params = new String[] { key };
            String sql = "SELECT id, version, approved, approvalDate, approvalUser, approvalUserId, submissionDate, submissionUser, submissionUserId, comments " +
                    "FROM " + getTableName() + "_version " +
                    "WHERE id=? ";
            if (sort != null) {
                sql += "ORDER BY " + sort;
                if (desc) {
                    sql += " DESC ";
                }
            }
            else {
                sql += "ORDER BY version DESC";
            }
            Collection results = super.select(sql, DefaultContentObject.class, params, start, rows);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called to retrieve the number of versions for a specified content.
     * @param key
     * @return
     * @throws DaoException
     */
    public int selectVersionsCount(String key) throws DaoException {
        try {
            String[] params = new String[] { key };
            String sql = "SELECT COUNT(*) AS total " +
                    "FROM " + getTableName() + "_version " +
                    "WHERE id=? ";

            Collection results = super.select(sql.toString(), HashMap.class, params, 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                /*Integer totalStr = (Integer)resultMap.get("total");
                int total = totalStr.intValue();*/
				int total = ((Number) resultMap.get("total")).intValue();
                return total;
            }
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
                    "SELECT s.id, new, modified, deleted, archived, published, s.approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion, " +
                    "className, v.version, name, description, summary, author, date, related, contents, " +
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
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
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

    /**
     * Called to retrieve a count of content objects based on criteria.
     * @param keys
     * @param classes
     * @param parentId
     * @param fromDate
     * @param toDate
     * @param checkedOut
     * @param submitted
     * @param approved
     * @param archived
     * @return
     * @throws kacang.model.DaoException
     */
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
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
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
                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, description, summary, author, date, related, contents " :
                    "s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, aclId, parentId, ordering, template, publishVersion, " +
                    "className, w.version, name, author, date, related ";
            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT " +
                    columns +
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

    /**
     * Called to retrieve a count of content objects based on criteria.
     * @param principalIds
     * @param permissionId
     * @param keys
     * @param classes
     * @param parentId
     * @param fromDate
     * @param toDate
     * @param checkedOut
     * @param submitted
     * @param approved
     * @param archived
     * @param deleted
     * @return
     * @throws kacang.model.DaoException
     */
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

    /**
     *
     * @param principalIds
     * @param key
     * @return
     * @throws DaoException
     */
    public Collection selectPermissionsByUser(String[] principalIds, String key) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT permissionId " +
                    "FROM " + getTableName() + "_status s " +
                    "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "LEFT JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "LEFT JOIN " + getTableName() + "_role_permission rp2 ON rp1.role = rp2.role " +
                    "WHERE 1=1");

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

            if (key != null) {
                sql.append(" AND s.id=?");
                paramList.add(key);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     *
     * @param principalIds
     * @param permissionId
     * @param keys
     * @return A Map of contentID(String)=number of children(String)
     * @throws DaoException
     */
    public Map selectChildrenCount(String[] principalIds, String permissionId, String[] keys, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted) throws DaoException {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT s.id, COUNT(DISTINCT s2.id) AS childCount " +
                        "FROM " + getTableName() + "_status s " +
                        "LEFT JOIN " + getTableName() + "_status s2 ON s.id=s2.parentId " +
                        "LEFT JOIN " + getTableName() + "_work w ON s.id=w.id " +
                        "LEFT JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                        "LEFT JOIN " + getTableName() + "_role_permission rp2 ON rp1.role = rp2.role " +
                        "WHERE 1=1 ");

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

        if (checkedOut != null) {
            sql.append(" AND s2.checkedOut=?");
            paramList.add(checkedOut);
        }

        if (submitted != null) {
            sql.append(" AND s2.submitted=?");
            paramList.add(submitted);
        }

        if (approved != null) {
            sql.append(" AND s2.approved=?");
            paramList.add(approved);
        }

        if (archived != null) {
            sql.append(" AND s2.archived=?");
            paramList.add(archived);
        }

        if (published != null) {
            sql.append(" AND s2.published=?");
            paramList.add(published);
        }

        if (deleted != null) {
            sql.append(" AND s2.deleted=?");
            paramList.add(deleted);
        }

        sql.append(" GROUP BY s.id");

        Map resultMap = new HashMap();
        Collection resultList = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1);
        for (Iterator i=resultList.iterator(); i.hasNext();) {
            Map row = (Map)i.next();
            resultMap.put(row.get("id"), row.get("childCount"));
        }
        return resultMap;
    }

//--- ACL Methods

    /**
     * Updates the ACL entries for a specified content.
     * @param key
     * @param aclArray
     * @throws DaoException
     */
    public void updateRolePrincipals(String key, ContentAcl[] aclArray) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from table
            String[] params = new String[] { key };
            tx.update(
                    "DELETE FROM " + getTableName() + "_role_principal WHERE objectId=?", params);

            // insert into table
            for (int i=0; i<aclArray.length; i++) {
                if (key.equals(aclArray[i].getObjectId())) {
                    tx.update(
                            "INSERT INTO " + getTableName() + "_role_principal (role, objectId, principalId) " +
                            "VALUES (#role#, #objectId#, #principalId#)", aclArray[i]);
                }
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

    /**
     * Removes all ACL entries for specified content objects
     * @param keyArray
     * @throws DaoException
     */
    public void deleteRolePrincipals(String[] keyArray) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            if (keyArray == null || keyArray.length == 0) {
                return;
            }

            // delete from table
            String subset = "";
            for (int i=0; i<keyArray.length; i++) {
                subset += ",?";
            }
            subset = subset.substring(1);
            tx.update("DELETE FROM " + getTableName() + "_role_principal WHERE objectId IN (" + subset + ")", keyArray);

            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called to retrieve ACL entries.
     * @param key
     * @param roleArray
     * @param principalIdArray
     * @return A Collection of ContentAcl objects.
     * @throws DaoException
     */
    public Collection selectRolePrincipals(String key, String[] roleArray, String[] principalIdArray) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT role, objectId, principalId " +
                    "FROM " + getTableName() + "_role_principal " +
                    "WHERE 1=1");

            if (key != null) {
                sql.append(" AND objectId=?");
                paramList.add(key);
            }

            if (roleArray != null && roleArray.length > 0) {
                sql.append(" AND (");
                for (int i=0; i<roleArray.length; i++) {
                    if (i > 0)
                        sql.append(" OR ");
                    sql.append("role=?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(roleArray));
            }

            if (principalIdArray != null && principalIdArray.length > 0) {
                sql.append(" AND (");
                for (int i=0; i<principalIdArray.length; i++) {
                    if (i > 0)
                        sql.append(" OR ");
                    sql.append("principalId=?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(principalIdArray));
            }

            Collection results = super.select(sql.toString(), ContentAcl.class, paramList.toArray(), 0, -1);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called to retrieve the permissions for a specified role.
     * @param role
     * @return A Collection of HashMap objects, each of which contains an entry "permissionId"=value
     * @throws DaoException
     */
    public Collection selectRolePermissions(String role) throws DaoException {
        try {
            String[] params = new String[] { role };
            Collection results = super.select(
                    "SELECT permissionId" +
                    "FROM " + getTableName() + "_role_permission " +
                    "WHERE role=?", HashMap.class, params, 0, -1);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Called to retrieve the principals for a specified content and permission.
     * @param key
     * @param permissionId
     * @return A Collection of HashMap objects, each of which contains an entry "principalId"=value
     * @throws DaoException
     */
    public Collection selectPermissionPrincipals(String key, String permissionId) throws DaoException {
        try {
            String[] params = new String[] { key, permissionId };
            Collection results = super.select(
                    "SELECT DISTINCT principalId " +
                    "FROM " + getTableName() + "_role_permission p1 " +
                    "LEFT JOIN " + getTableName() + "_role_principal p2 ON p1.role=p2.role " +
                    "LEFT JOIN " + getTableName() + "_status s ON p2.objectId=s.aclId " +
                    "WHERE s.id=? AND p1.permissionId=?", HashMap.class, params, 0, -1);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

//--- Keyword Methods

    /**
     *
     * @param key
     * @return a Collection of keyword Strings.
     * @throws kacang.model.DaoException
     */
    public Collection selectKeywordsById(String key) throws DaoException {
        try {
            String[] args = new String[] { key };
            Collection kwMapList = super.select(
                    "SELECT keyword " +
                    "FROM " + getTableName() + "_keyword " +
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
     *
     * @return a Collection of Strings representing the keywords.
     * @throws kacang.model.DaoException
     */
    public Collection selectKeywords(String search, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT keyword " +
                    "FROM " + getTableName() + "_keyword ");

            if (search != null) {
                sql.append("WHERE keyword LIKE ? ");
                paramList.add("%" + search + "%");
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

    public int selectKeywordsCount(String search) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(DISTINCT keyword) AS total " +
                    "FROM " + getTableName() + "_keyword ");

            if (search != null) {
                sql.append("WHERE keyword LIKE ? ");
                paramList.add("%" + search + "%");
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

    public void insertKeywords(String[] keywordArray) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // insert into keywords table
            for (int i=0; i<keywordArray.length; i++) {
                String kw = keywordArray[i];
                if (kw.length() > 50) {
                    kw = kw.substring(0, 50);
                }
                Object[] args = new Object[] { kw };
                Collection col = super.select("SELECT keyword FROM " + getTableName() + "_keyword WHERE keyword=?", HashMap.class, args, 0, 1);
                if (col.size() == 0) {
                    tx.update(
                        "INSERT INTO " + getTableName() + "_keyword (keyword) " +
                        "VALUES (?)", args);
                }
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

    public void deleteKeywords(String[] keywordArray) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "DELETE FROM " + getTableName() + "_keyword WHERE 1=1");
            if (keywordArray != null && keywordArray.length > 0) {
                sql.append(" AND keyword IN (");
                for (int i=0; i<keywordArray.length; i++) {
                    if (i > 0)
                        sql.append(", ");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(keywordArray));
                super.update(sql.toString(), paramList.toArray());
            }
        }
        catch (DaoException e) {
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
                        "INSERT INTO " + getTableName() + "_keyword (keyword) " +
                        "VALUES (?)", args);
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
                    "SELECT DISTINCT s.id, new, modified, deleted, archived, published, approved, submitted, checkedOut, checkOutDate, checkOutUser, checkOutUserId, aclId, parentId, ordering, template, startDate, endDate, publishDate, publishUser, publishUserId, publishVersion, " +
                    "className, w.version, name, description, author, date, related " +
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
                    "SELECT COUNT(DISTINCT s.id) AS total " +
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

//--- Template Methods

    /**
     * Retrieves the template for a specified content.
     * @param key
     * @return
     * @throws kacang.model.DaoException
     */
    public String selectTemplateById(String key) throws DaoException {
        try {
            String[] args = new String[] { key };
            Collection kwMapList = super.select(
                    "SELECT template " +
                    "FROM " + getTableName() + "_template " +
                    "WHERE id=?", HashMap.class, args, 0, -1);
            if (kwMapList.size() > 0) {
                Map kwMap = (Map)kwMapList.iterator().next();
                return (String)kwMap.get("template");
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Retrieves a Collection content IDs using a specified template.
     * @param template
     * @return A Collection of HashMap objects, each of which contains an entry "id"=value
     * @throws DaoException
     */
    public Collection selectByTemplate(String template) throws DaoException {
        try {
            Collection keyList = new ArrayList();
            String[] args = new String[] { template };
            Collection kwMapList = super.select(
                    "SELECT id " +
                    "FROM " + getTableName() + "_template " +
                    "WHERE template=?", HashMap.class, args, 0, -1);
            for (Iterator i=kwMapList.iterator(); i.hasNext();) {
                Map kwMap = (Map)i.next();
                keyList.add(kwMap.get("id"));
            }
            return keyList;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    /**
     * Updates a template for an array of content IDs
     * @param template
     * @param keyArray
     * @throws DaoException
     */
    public void updateTemplate(String template, String[] keyArray) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // delete from template table
            if (template != null) {
                Object[] args = { template };
                tx.update("DELETE FROM " + getTableName() + "_template WHERE template=?", args);
            }

            // insert into template table
            for (int i=0; i<keyArray.length; i++) {
                String key = keyArray[i];
                Object[] args1 = new Object[] { key };
                tx.update(
                    "DELETE FROM " + getTableName() + "_template WHERE id=?", args1);
                if (template != null) {
                    Object[] args2 = new Object[] { template, key };
                    tx.update(
                        "INSERT INTO " + getTableName() + "_template (template, id) " +
                        "VALUES (?, ?)", args2);
                }
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


//--- Notification Subscription Methods

    /**
     * Inserts subscription entries for a user and specified content IDs
     * @param userId
     * @param keys
     * @throws DaoException
     */
    public void insertSubscriptionByUser(String userId, String[] keys) throws DaoException {
        if (keys == null) {
            return;
        }
        for (int i=0; i<keys.length; i++) {
            String[] params = new String[] { userId, keys[i] };
            super.update("INSERT INTO cms_content_subscription (userId, contentId) VALUES (?, ?)", params);
        }
    }


    /**
     * Deletes subscription entries for a user and specified content IDs
     * @param userId
     * @param keys Use null or empty array to delete all subscription entries for the user
     * @return
     * @throws DaoException
     */
    public int deleteSubscriptionByUser(String userId, String[] keys) throws DaoException {
        if (keys == null || keys.length == 0) {
            return super.update("DELETE FROM cms_content_subscription WHERE userId=?", new String[] { userId });
        }
        else {
            Collection paramList = new ArrayList();
            paramList.add(userId);
            StringBuffer sql = new StringBuffer("DELETE FROM cms_content_subscription WHERE userId = ? AND contentId IN (");
            for (int i=0; i<keys.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(keys));
            return super.update(sql.toString(), paramList.toArray());
        }
    }

    /**
     * Inserts subscription entries for a content and specified user IDs
     * @param contentId
     * @param userIds
     * @throws DaoException
     */
    public void insertSubscriptionByContent(String contentId, String[] userIds) throws DaoException {
        if (userIds == null) {
            return;
        }
        for (int i=0; i<userIds.length; i++) {
            String[] params = new String[] { contentId, userIds[i] };
            super.update("INSERT INTO cms_content_subscription (contentId, userId) VALUES (?, ?)", params);
        }
    }


    /**
     * Deletes subscription entries for a content and specified user IDs
     * @param contentId
     * @param userIds Use null or empty array to delete all subscription entries for the content
     * @return
     * @throws DaoException
     */
    public int deleteSubscriptionByContent(String contentId, String[] userIds) throws DaoException {
        if (userIds == null || userIds.length == 0) {
            return super.update("DELETE FROM cms_content_subscription WHERE contentId=?", new String[] { contentId });
        }
        else {
            Collection paramList = new ArrayList();
            paramList.add(contentId);
            StringBuffer sql = new StringBuffer("DELETE FROM cms_content_subscription WHERE contentId = ? AND userId IN (");
            for (int i=0; i<userIds.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            paramList.addAll(Arrays.asList(userIds));
            return super.update(sql.toString(), paramList.toArray());
        }
    }

    /**
     *
     * @param userId
     * @param contentId
     * @param start
     * @param rows
     * @return A Collection of ContentSubscription objects.
     * @throws DaoException
     */
    public Collection selectSubscriptions(String userId, String contentId, int start, int rows) throws DaoException {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT userId, contentId FROM cms_content_subscription WHERE 1=1");
        if (userId != null) {
            sql.append(" AND userId=?");
            paramList.add(userId);
        }
        if (contentId != null) {
            sql.append(" AND contentId=?");
            paramList.add(contentId);
        }
        return super.select(sql.toString(), ContentSubscription.class, paramList.toArray(), start, rows);
    }

    /**
     *
     * @param userId
     * @param contentId
     * @return
     * @throws DaoException
     */
    public int selectSubscriptionsCount(String userId, String contentId) throws DaoException {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total FROM cms_content_subscription WHERE 1=1");
        if (userId != null) {
            sql.append(" AND userId=?");
            paramList.add(userId);
        }
        if (contentId != null) {
            sql.append(" AND contentId=?");
            paramList.add(contentId);
        }
        Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
        Map map = (Map)results.iterator().next();
        int count = Integer.parseInt(map.get("total").toString());
        return count;
    }

}
