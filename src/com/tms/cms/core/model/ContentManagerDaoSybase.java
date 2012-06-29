package com.tms.cms.core.model;

import kacang.model.*;
import kacang.util.*;
import java.util.*;

public class ContentManagerDaoSybase extends ContentManagerDaoMsSql {
	// important: similar to super class except it uses the field 'numericOrdering'
	public int insertNew(ContentObject contentObject) throws DaoException {
		addNumericOrdering(contentObject);
		
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
                    "VALUES (#id#, #new#, #modified#, #deleted#, #archived#, #published#, #approved#, #submitted#, #checkedOut#, #checkOutDate#, #checkOutUser#, #checkOutUserId#, #aclId#, #parentId#, #numericOrdering#, #template#, #startDate#, #endDate#, #publishDate#, #publishUser#, #publishUserId#, #publishVersion#)", contentObject);

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
	
	// important: similar to super class except it uses the field 'numericOrdering'
	public int insertVersion(ContentObject contentObject) throws DaoException {
		addNumericOrdering(contentObject);
		
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
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#numericOrdering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
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
	
	// important: similar to super class except it uses the field 'numericOrdering'
	public int updateVersion(ContentObject contentObject) throws DaoException {
		addNumericOrdering(contentObject);
		
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
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#numericOrdering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
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
	
	// important: similar to super class except it uses the field 'numericOrdering'
	public int updateStatus(ContentObject contentObject) throws DaoException {
		addNumericOrdering(contentObject);
		
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // update status table
            int result = tx.update(
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, aclId=#aclId#, parentId=#parentId#, ordering=#numericOrdering#, template=#template# " +
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
	
	// important: similar to super class except it uses the field 'numericOrdering'
	public int updateFullStatus(ContentObject contentObject) throws DaoException {
		addNumericOrdering(contentObject);
		
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            // update status table
            int result = tx.update(
                    "UPDATE " + getTableName() + "_status SET new=#new#, modified=#modified#, deleted=#deleted#, archived=#archived#, published=#published#, approved=#approved#, submitted=#submitted#, checkedOut=#checkedOut#, checkOutDate=#checkOutDate#, checkOutUser=#checkOutUser#, checkOutUserId=#checkOutUserId#, aclId=#aclId#, parentId=#parentId#, ordering=#numericOrdering#, template=#template#, startDate=#startDate#, endDate=#endDate#, publishDate=#publishDate#, publishUser=#publishUser#, publishUserId=#publishUserId#, publishVersion=#publishVersion# " +
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
	
	protected void addNumericOrdering(ContentObject contentObject) {
		String ordering = contentObject.getOrdering();
		if (ordering != null) {
			int numOrdering = Integer.parseInt(ordering);
			contentObject.setProperty("numericOrdering", new Integer(numOrdering));
		}
	}
	
	// important: since the database column for version is varchar(3), we need to order it correctly
    public Collection selectVersions(String key, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            String[] params = new String[] { key };
            String sql = "SELECT id, version, approved, approvalDate, approvalUser, approvalUserId, submissionDate, submissionUser, submissionUserId, comments " +
                    "FROM " + getTableName() + "_version " +
                    "WHERE id=? ";
            if (sort != null) {
				if (sort.equals("version")) {
					sort = "convert(int, version)";
				}
                sql += "ORDER BY " + sort;
                if (desc) {
                    sql += " DESC ";
                }
            }
            else {
                sql += "ORDER BY convert(int, version) DESC";
            }
            Collection results = super.select(sql, DefaultContentObject.class, params, start, rows);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }
	
	// note: selectCountByPermission is the same as the one in ContentManagerDao except for "[role]"
	// changed: to INNER JOIN
    public int selectCountByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(DISTINCT s.id) AS total " +
                    "FROM " + getTableName() + "_status s " +
                    "INNER JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "INNER JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "INNER JOIN " + getTableName() + "_role_permission rp2 ON rp1.[role] = rp2.[role] " +
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
	
	// note: selectPermissionsByUser is the same as the one in ContentManagerDao except for "[role]"
	// also: changed LEFT JOIN to INNER JOIN: to fix weird Sybase bug... returns more records then intended
    public Collection selectPermissionsByUser(String[] principalIds, String key) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT DISTINCT permissionId " +
                    "FROM " + getTableName() + "_status s " +
                    "INNER JOIN " + getTableName() + "_work w ON s.id=w.id " +
                    "INNER JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "INNER JOIN " + getTableName() + "_role_permission rp2 ON rp1.[role] = rp2.[role] " +
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
	
	// note: selectChildrenCount is the same as the one in ContentManagerDao except for "[role]"
	// changed: to INNER JOIN
    public Map selectChildrenCount(String[] principalIds, String permissionId, String[] keys, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted) throws DaoException {
        Collection paramList = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT s.id, COUNT(DISTINCT s2.id) AS childCount " +
                        "FROM " + getTableName() + "_status s " +
                        "INNER JOIN " + getTableName() + "_status s2 ON s.id=s2.parentId " +
                        "INNER JOIN " + getTableName() + "_work w ON s.id=w.id " +
                        "INNER JOIN " + getTableName() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                        "INNER JOIN " + getTableName() + "_role_permission rp2 ON rp1.[role] = rp2.[role] " +
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
	
	// note: updateRolePrincipals is the same as the one in ContentManagerDao except for "[role]"
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
                            "INSERT INTO " + getTableName() + "_role_principal ([role], objectId, principalId) " +
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
	
	// note: selectRolePrincipals is the same as the one in ContentManagerDao except for "[role]"
    public Collection selectRolePrincipals(String key, String[] roleArray, String[] principalIdArray) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT [role] AS \"role\", objectId, principalId " +
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
                    sql.append("[role]=?");
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
	
	// note: selectPermissionPrincipals is the same as the one in ContentManagerDao except for "[role]"
	// changed: to INNER JOIN
    public Collection selectPermissionPrincipals(String key, String permissionId) throws DaoException {
        try {
            String[] params = new String[] { key, permissionId };
            Collection results = super.select(
                    "SELECT DISTINCT principalId " +
                    "FROM " + getTableName() + "_role_permission p1 " +
                    "INNER JOIN " + getTableName() + "_role_principal p2 ON p1.[role]=p2.[role] " +
                    "INNER JOIN " + getTableName() + "_status s ON p2.objectId=s.aclId " +
                    "WHERE s.id=? AND p1.permissionId=?", HashMap.class, params, 0, -1);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }
}
