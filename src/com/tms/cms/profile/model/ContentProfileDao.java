package com.tms.cms.profile.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Transaction;

import java.util.*;

public class ContentProfileDao extends DataSourceDao {

    public void init() throws DaoException {
        try {
            super.update("ALTER TABLE cms_profile_data ADD label VARCHAR(255)", null);
        }
        catch (DaoException e) {
            ;
        }
        super.update("CREATE TABLE cms_profile (id varchar(250) NOT NULL, name varchar(255) default NULL, description text, definition text, PRIMARY KEY (id))", null);
        super.update("CREATE TABLE cms_profile_map (profileId varchar(250) NOT NULL, parentId varchar(255) NOT NULL)", null);
        super.update("CREATE TABLE cms_profile_data (id varchar(250) NOT NULL, type varchar(255), name varchar(255) NOT NULL, value text, contentId varchar(255), version int, ordering int)", null);
    }

    public ContentProfile selectById(String profileId) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT id, name, description, definition " +
                "FROM cms_profile d " +
                "WHERE id=?";
        String[] args = new String[] { profileId };
        Collection results = super.select(sql, ContentProfile.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            return (ContentProfile)results.iterator().next();
        }
    }

    public ContentProfile selectByParent(String parentId) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT id, name, description, definition " +
                "FROM cms_profile d " +
                "LEFT JOIN cms_profile_map p ON d.id=p.profileId " +
                "WHERE p.parentId=?";
        String[] args = new String[] { parentId };
        Collection results = super.select(sql, ContentProfile.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            return (ContentProfile)results.iterator().next();
        }
    }

/*
    public ContentProfile selectByContent(String contentId, String version) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT id, name, description, definition " +
                "FROM cms_profile d " +
                "LEFT JOIN cms_profile_map m ON d.id=m.profileId " +
                "WHERE m.contentId=? AND m.version=?";
        String[] args = new String[] { contentId, version };
        Collection results = super.select(sql, ContentProfile.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            return (ContentProfile)results.iterator().next();
        }
    }
*/

    public Collection selectData(String contentId, String version) throws DaoException {
        String sql = "SELECT type, name, value, label " +
                "FROM cms_profile_data d " +
                "WHERE d.contentId=? AND d.version=? " +
                "ORDER BY d.ordering";
        String[] args = new String[] { contentId, version };
        Collection results = super.select(sql, HashMap.class, args, 0, -1);
        return results;
    }

    public void updateData(String contentId, String version, ContentProfile profile) throws DaoException {

        if (contentId == null || version == null || profile == null) {
            throw new DaoException("ContentProfile is null and data cannot be updated");
        }

        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.update("DELETE FROM cms_profile_data WHERE contentId=? AND version=?", new Object[] { contentId, version });

            Collection fields = profile.getFields();
            if (fields != null) {
                int count=10;
                for (Iterator i=fields.iterator(); i.hasNext(); count+=10) {
                    ContentProfileField field = (ContentProfileField)i.next();
                    tx.update("INSERT INTO cms_profile_data (type, name, value, contentId, version, ordering, label) VALUES (?,?,?,?,?,?,?)", new Object[] { field.getType(), field.getName(), field.getValue(), contentId, version, new Integer(count), field.getLabel() });
                }
            }

            tx.commit();
        }
        catch (Exception e) {
            try {
                tx.rollback();
            }
            catch(Exception re) {
                ;
            }
            throw new DaoException("Unable to update profile data: " + e.getMessage(), e);
        }

    }

    public void deleteData(String contentId, String version) throws DaoException {

        if (contentId == null) {
            throw new DaoException("contentId is null and data cannot be deleted");
        }

        if (version != null) {
            super.update("DELETE FROM cms_profile_data WHERE contentId=? AND version=?", new Object[] { contentId, version });
        }
        else {
            super.update("DELETE FROM cms_profile_data WHERE contentId=?", new Object[] { contentId });
        }

    }


    public void deleteProfileMap(String profileId, String contentId) throws DaoException {
        if (profileId == null && contentId == null) {
            return;
        }

        Collection argList = new ArrayList();
        String sql = "DELETE FROM cms_profile_map WHERE 1=1";
        if (profileId != null) {
            sql += " AND profileId=?";
            argList.add(profileId);
        }
        if (contentId != null) {
            sql += " AND parentId=?";
            argList.add(contentId);
        }
        Object[] args = argList.toArray();
        super.update(sql, args);
    }

    public void insertProfileMap(String profileId, String parentId) throws DaoException {
        super.update("INSERT INTO cms_profile_map (profileId, parentId) VALUES (?,?)", new String[] { profileId, parentId });
    }

    public void insertProfile(ContentProfile profile) throws DaoException {
        super.update("INSERT INTO cms_profile (id, name, description, definition) VALUES (#id#,#name#,#description#,#definition#)", profile);
    }

    public void updateProfile(ContentProfile profile) throws DaoException {
        super.update("UPDATE cms_profile SET name=#name#, description=#description#, definition=#definition# WHERE id=#id#", profile);
    }

    public void deleteProfile(String profileId) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.update("DELETE FROM cms_profile WHERE id=?", new String[] { profileId });
            tx.update("DELETE FROM cms_profile_map WHERE profileId=?", new String[] { profileId });

            tx.commit();
        }
        catch (Exception e) {
            try {
                tx.rollback();
            }
            catch(Exception re) {
                ;
            }
            throw new DaoException("Unable to delete profile: " + profileId, e);
        }
    }

    public Collection selectProfileList(String name, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection argList = new ArrayList();
        String sql = "SELECT id, name, description, definition FROM cms_profile";
        if (name != null && name.trim().length() > 0) {
            sql += " WHERE name LIKE ?";
            argList.add("%" + name + "%");
        }
        if (sort != null) {
            sql += " ORDER BY " + sort;
            if (desc) {
                sql += " DESC";
            }
        }
        Object[] args = argList.toArray();
        Collection results = super.select(sql, ContentProfile.class, args, start, rows);
        return results;
    }

    public int selectProfileCount(String name) throws DaoException {
        Collection argList = new ArrayList();
        String sql = "SELECT COUNT(*) AS total FROM cms_profile";
        if (name != null && name.trim().length() > 0) {
            sql += " WHERE name LIKE ?";
            argList.add("%" + name + "%");
        }
        Object[] args = argList.toArray();
        Collection results = super.select(sql, HashMap.class, args, 0, 1);
        Map row = (Map)results.iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }


}
