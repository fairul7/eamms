package com.tms.collab.directory.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import kacang.model.DaoException;

public class AddressBookDaoDB2 extends AddressBookDao{
	
	public void init() throws DaoException{
		try{
    		super.update("ALTER TABLE " + getTablePrefix() + "_contact ALTER COLUMN designation SET DATA TYPE VARCHAR(255)", null);
    	}
    	catch(DaoException e){}
	}
	
    public Collection selectFolderList(String query, String parentId, String userId, String sort, boolean desc, int start, int rows) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, parentId, name, description, userId FROM " + getTablePrefix() + "_folder WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            query = "%" + query.trim() + "%";
            sql.append(" AND (UPPER(name) LIKE ? OR description LIKE ?)");
            args.add(query.toUpperCase());
            args.add(query);
        }
        if (parentId != null) {
            sql.append(" AND parentId=?");
            args.add(parentId);
        }
        if (userId != null) {
            sql.append(" AND userId=?");
            args.add(userId);
        }

        // determine sorting
        if (sort != null && sort.trim().length() > 0) {
            sql.append(" ORDER BY " + sort);
            if (desc) {
                sql.append(" DESC");
            }
        }
        Collection results = super.select(sql.toString(), Folder.class, args.toArray(), start, rows);
        return results;
    }

    public int selectFolderCount(String query, String parentId, String userId) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total FROM " + getTablePrefix() + "_folder WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            query = "%" + query.trim() + "%";
            sql.append(" AND (UPPER(name) LIKE ? OR description LIKE ?)");
            args.add(query.toUpperCase());
            args.add(query);
        }
        if (parentId != null) {
            sql.append(" AND parentId=?");
            args.add(parentId);
        }
        if (userId != null) {
            sql.append(" AND userId=?");
            args.add(userId);
        }

        Collection results = super.select(sql.toString(), HashMap.class, args.toArray(), 0, 1);
        HashMap row = (HashMap)results.iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    public Collection selectContactList(String query, String folderId, String companyId, String ownerId, Boolean approved, Boolean contactGroup, String sort, boolean desc, int start, int rows) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, phone, extension, fax, mobile, ownerId, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails FROM " + getTablePrefix() + "_contact WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            StringTokenizer st = new StringTokenizer(query," ,;");
            while(st.hasMoreTokens()) {
                String word = "%" + st.nextToken() + "%";
                sql.append(" AND (UPPER(firstName) LIKE ? OR UPPER(middleName) LIKE ? OR UPPER(lastName) LIKE ? OR UPPER(email) LIKE ? OR phone LIKE ? OR mobile LIKE ? OR UPPER(company) LIKE ?)");
                args.add(word.toUpperCase());
                args.add(word.toUpperCase());
                args.add(word.toUpperCase());
                args.add(word.toUpperCase());
                args.add(word);
                args.add(word);
                args.add(word.toUpperCase());
            }
        }
        if (folderId != null) {
            sql.append(" AND folderId=?");
            args.add(folderId);
        }
        if (companyId != null) {
            sql.append(" AND companyId=?");
            args.add(companyId);
        }
        if (ownerId != null) {
            sql.append(" AND ownerId=?");
            args.add(ownerId);
        }
        if (approved != null) {
            sql.append(" AND approved=?");
            args.add(approved);
        }
        if (contactGroup != null) {
            sql.append(" AND contactGroup=?");
            args.add(contactGroup);
        }

        // determine sorting
        if (sort != null && sort.trim().length() > 0) {
            sql.append(" ORDER BY " + sort);
            if (desc) {
                sql.append(" DESC");
            }
        }
        Collection results = super.select(sql.toString(), Contact.class, args.toArray(), start, rows);
        return results;
    }

    public int selectContactCount(String query, String folderId, String companyId, String ownerId, Boolean approved, Boolean contactGroup) throws DaoException {
        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total FROM " + getTablePrefix() + "_contact WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            StringTokenizer st = new StringTokenizer(query," ,;");
            while(st.hasMoreTokens()) {
                String word = "%" + st.nextToken() + "%";
                sql.append(" AND (UPPER(firstName) LIKE ? OR UPPER(middleName) LIKE ? OR UPPER(lastName) LIKE ? OR UPPER(email) LIKE ? OR phone LIKE ? OR mobile LIKE ? OR UPPER(company) LIKE ?)");
                args.add(word.toUpperCase());
                args.add(word.toUpperCase());
                args.add(word.toUpperCase());
                args.add(word.toUpperCase());
                args.add(word);
                args.add(word);
                args.add(word.toUpperCase());
            }
        }
        if (folderId != null) {
            sql.append(" AND folderId=?");
            args.add(folderId);
        }
        if (companyId != null) {
            sql.append(" AND companyId=?");
            args.add(companyId);
        }
        if (ownerId != null) {
            sql.append(" AND ownerId=?");
            args.add(ownerId);
        }
        if (approved != null) {
            sql.append(" AND approved=?");
            args.add(approved);
        }
        if (contactGroup != null) {
            sql.append(" AND contactGroup=?");
            args.add(contactGroup);
        }
        Collection results = super.select(sql.toString(), HashMap.class, args.toArray(), 0, 1);
        HashMap row = (HashMap)results.iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
}
