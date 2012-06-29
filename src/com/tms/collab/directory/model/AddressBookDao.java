package com.tms.collab.directory.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.util.Transaction;

import java.util.*;

/**
 * Generic DAO for the address book module.
 */
public class AddressBookDao extends DataSourceDao {

    private Log log = Log.getLog(getClass());

    public void init() throws DaoException {        
        try {
            // update table to support contact groups
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD createdTime DATETIME", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD modifiedTime DATETIME", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD INDEX ( `createdTime` )", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD INDEX ( `modifiedTime` )", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD contactGroupIntranetIds TEXT", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD contactGroupEmails TEXT", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD comments TEXT", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD contactGroup CHAR(1) NOT NULL", null);
            super.update("ALTER TABLE " + getTablePrefix() + "_contact ADD contactGroupIds TEXT", null);
        }
        catch (DaoException e) {
            ;
        }
        super.update("CREATE TABLE " + getTablePrefix() + "_folder(id VARCHAR(35) NOT NULL, parentId VARCHAR(35), name VARCHAR(255) NOT NULL, description TEXT, userId VARCHAR(255) NOT NULL, PRIMARY KEY(id))", null);
        super.update("CREATE TABLE " + getTablePrefix() + "_contact(id VARCHAR(35) NOT NULL, folderId VARCHAR(35), companyId VARCHAR(255), ownerId VARCHAR(255) NOT NULL, userId VARCHAR(255), username VARCHAR(255), title VARCHAR(20), firstName VARCHAR(200), middleName VARCHAR(200), lastName VARCHAR(200), nickName VARCHAR(200), email VARCHAR(250), designation VARCHAR(255), company VARCHAR(255), address TEXT, city VARCHAR(100), state VARCHAR(100), postcode VARCHAR(20), country VARCHAR(200), extension VARCHAR(20), phone VARCHAR(100), fax VARCHAR(100), mobile VARCHAR(100), customProperty TEXT, approved CHAR(1), comments, contactGroup CHAR(1) NOT NULL, contactGroupIds TEXT, contactGroupIntranetIds TEXT, contactGroupEmails TEXT, createdTime DATETIME, modifiedTime DATETIME, PRIMARY KEY(id))", null);
        super.update("CREATE TABLE " + getTablePrefix() + "_folder_audit(id VARCHAR(35) NOT NULL, auditDateCreated DATETIME, auditDateModified DATETIME, auditDateDeleted DATETIME, auditUserCreated VARCHAR(255), auditUserModified VARCHAR(255), auditUserDeleted VARCHAR(255), created CHAR(1), modified CHAR(1), deleted CHAR(1), archived CHAR(1))", null);
        super.update("CREATE TABLE " + getTablePrefix() + "_contact_audit(id VARCHAR(35) NOT NULL, auditDateCreated DATETIME, auditDateModified DATETIME, auditDateDeleted DATETIME, auditUserCreated VARCHAR(255), auditUserModified VARCHAR(255), auditUserDeleted VARCHAR(255), created CHAR(1), modified CHAR(1), deleted CHAR(1), archived CHAR(1))", null);
    }

    public String getTablePrefix() {
        return "dir_per";
    }
//-- Contact CRUD methods

    public void insertContact(Contact contact, String userId) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("INSERT INTO " + getTablePrefix() + "_contact (id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, address, city, state, postcode, country, phone, extension, fax, mobile, ownerId, customProperty, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails, createdTime, modifiedTime) VALUES (#id#, #folderId#, #companyId#, #userId#, #username#, #title#, #firstName#, #middleName#, #lastName#, #nickName#, #email#, #designation#, #company#, #address#, #city#, #state#, #postcode#, #country#, #phone#, #extension#, #fax#, #mobile#, #ownerId#, #customProperty#, #approved#, #comments#, #contactGroup#, #contactGroupIds#, #contactGroupIntranetIds#, #contactGroupEmails#, #createdTime#, #modifiedTime#)", contact);
            tx.update("INSERT INTO " + getTablePrefix() + "_contact_audit (id, auditDateCreated, auditUserCreated, created) VALUES (?, ?, ?, ?)", new Object[] { contact.getId(), new Date(), userId, Boolean.TRUE });

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error creating contact: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error creating contact: " + e.toString(), e);
        }
    }

    public void updateContact(Contact contact, String userId) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();            
            tx.update("UPDATE " + getTablePrefix() + "_contact SET folderId=#folderId#, companyId=#companyId#, userId=#userId#, username=#username#, title=#title#, firstName=#firstName#, middleName=#middleName#, lastName=#lastName#, nickName=#nickName#, email=#email#, designation=#designation#, company=#company#, address=#address#, city=#city#, state=#state#, postcode=#postcode#, country=#country#, phone=#phone#, extension=#extension#, fax=#fax#, mobile=#mobile#, ownerId=#ownerId#, customProperty=#customProperty#, approved=#approved#, comments=#comments#, contactGroup=#contactGroup#, contactGroupIds=#contactGroupIds#, contactGroupIntranetIds=#contactGroupIntranetIds#, contactGroupEmails=#contactGroupEmails#, modifiedTime=#modifiedTime# WHERE id=#id#", contact);
            tx.update("UPDATE " + getTablePrefix() + "_contact_audit SET auditDateModified=?, auditUserModified=?, created=?, modified=? WHERE id=?", new Object[] { new Date(), userId, Boolean.FALSE, Boolean.TRUE, contact.getId() });

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error updating contact: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error updating folder: " + e.toString(), e);
        }
    }

    public void deleteContact(String contactId, String userId) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.update("DELETE FROM " + getTablePrefix() + "_contact WHERE id=?", new String[] {contactId});
            tx.update("UPDATE " + getTablePrefix() + "_contact_audit SET auditDateDeleted=?, auditUserDeleted=?, created=?, modified=?, deleted=? WHERE id=?", new Object[] { new Date(), userId, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, contactId });

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error deleting contact: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error deleting folder: " + e.toString(), e);
        }
    }

    public Contact selectContact(String contactId) throws DataObjectNotFoundException, DaoException {
        Collection results = super.select(
                "SELECT c.id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, address, city, state, postcode, country, phone, extension, fax, mobile, ownerId, customProperty, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails " +
                        ", auditDateCreated, auditUserCreated, auditDateModified, auditUserModified, created, modified, createdTime, modifiedTime " +
                        " FROM " + getTablePrefix() + "_contact c " +
                        " LEFT OUTER JOIN " + getTablePrefix() + "_contact_audit a ON c.id=a.id " +
                        " WHERE c.id=?", Contact.class, new String[] {contactId}, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            return (Contact)results.iterator().next();
        }
    }

    /**
     *
     * @param query
     * @param folderId
     * @param companyId
     * @param ownerId
     * @param approved
     * @param contactGroup null for any contact, Boolean.TRUE for contact groups only, Boolean.FALSE otherwise
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of Contact objects
     * @throws DaoException
     */
    public Collection selectContactList(String query, String folderId, String companyId, String ownerId, Boolean approved, Boolean contactGroup, String sort, boolean desc, int start, int rows) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, phone, extension, fax, mobile, ownerId, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails, createdTime, modifiedTime FROM " + getTablePrefix() + "_contact WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            StringTokenizer st = new StringTokenizer(query," ,;");
            while(st.hasMoreTokens()) {
                String word = "%" + st.nextToken() + "%";
                sql.append(" AND (firstName LIKE ? OR middleName LIKE ? OR lastName LIKE ? OR email LIKE ? OR phone LIKE ? OR mobile LIKE ? OR company LIKE ? OR extension LIKE ? )");
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
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
                sql.append(" AND (firstName LIKE ? OR middleName LIKE ? OR lastName LIKE ? OR email LIKE ? OR phone LIKE ? OR mobile LIKE ? OR company LIKE ? OR extension LIKE ? )");
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
                args.add(word);
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

    /**
     *
     * @param query e.g. ABC for names beginning with A, B or C
     * @param folderId
     * @param companyId
     * @param ownerId
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws DaoException
     */
    public Collection selectContactListByAlphabet(String query, String field, String folderId, String companyId, String ownerId, Boolean approved, String sort, boolean desc, int start, int rows) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, phone, extension, fax, mobile, ownerId, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails, createdTime, modifiedTime FROM " + getTablePrefix() + "_contact WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            field = (field != null) ? field : "firstName";
            sql.append(" AND (1=1 ");
            for (int i=0; i<query.length(); i++) {
                sql.append(" OR " + field + " LIKE ?");
                sql.append(query.charAt(i));
            }
            sql.append(")");
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

    public int selectContactCountByAlphabet(String query, String field, String folderId, String companyId, String ownerId, Boolean approved) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total FROM " + getTablePrefix() + "_contact WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            field = (field != null) ? field : "firstName";
            sql.append(" AND (1=1 ");
            for (int i=0; i<query.length(); i++) {
                sql.append(" OR " + field + " LIKE ?");
                sql.append(query.charAt(i));
            }
            sql.append(")");
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
        Collection results = super.select(sql.toString(), HashMap.class, args.toArray(), 0, 1);
        HashMap row = (HashMap)results.iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    /**
     *
     * @param contactIdArray
     * @param sort
     * @param desc
     * @return A Collection of Contact objects.
     * @throws DaoException
     */
    public Collection selectContactListById(String[] contactIdArray, String sort, boolean desc) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, phone, extension, fax, mobile, ownerId, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails, createdTime, modifiedTime FROM " + getTablePrefix() + "_contact WHERE id IN (");
        if (contactIdArray == null || contactIdArray.length == 0) {
            return new ArrayList();
        }
        for (int i=0; i<contactIdArray.length; i++) {
            sql.append("?,");
            args.add(contactIdArray[i]);
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(")");

        // determine sorting
        if (sort != null && sort.trim().length() > 0) {
            sql.append(" ORDER BY " + sort);
            if (desc) {
                sql.append(" DESC");
            }
        }
        Collection results = super.select(sql.toString(), Contact.class, args.toArray(), 0, -1);
        return results;
    }

    /**
     * Returns emails that are registered in the database based on an array of emails passed in.
     * @param emailArray
     * @param userId The user ID of the user's contacts, use null for any user
     * @return A Collection of emails (String)
     * @throws DaoException
     */
    public Collection selectContactEmails(String[] emailArray, String userId) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT email FROM " + getTablePrefix() + "_contact WHERE email IN (");
        if (emailArray == null || emailArray.length == 0) {
            return new ArrayList();
        }
        for (int i=0; i<emailArray.length; i++) {
            sql.append("?,");
            args.add(emailArray[i]);
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(")");

        if (userId != null) {
            sql.append(" AND ownerId=?");
            args.add(userId);
        }
        Collection mapList = super.select(sql.toString(), HashMap.class, args.toArray(), 0, -1);

        // get registered emails
        Collection registeredEmails = new ArrayList();
        for (Iterator it=mapList.iterator(); it.hasNext();) {
            HashMap map = (HashMap)it.next();
            String email = (String)map.get("email");
            registeredEmails.add(email);
        }
        return registeredEmails;
    }

    /**
     * Returns a Collection of all current name titles used.
     * @return A Collection of String objects.
     * @throws DaoException
     */
    public Collection selectContactTitles() throws DaoException {
        Collection titleList = new ArrayList();
        String sql = "SELECT DISTINCT title FROM " + getTablePrefix() + "_contact ORDER BY title";
        Collection mapList =  super.select(sql, HashMap.class, null, 0, -1);
        for (Iterator i=mapList.iterator(); i.hasNext();) {
            HashMap map = (HashMap)i.next();
            String title = (String)map.get("title");
            if (title != null && title.trim().length() > 0) {
                titleList.add(title);
            }
        }
        return titleList;
    }
//-- Folder CRUD methods

    public void insertFolder(Folder folder, String userId) throws DaoException {
/*
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error creating folder: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
        }
*/
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.update("INSERT INTO " + getTablePrefix() + "_folder (id, parentId, name, description, userId) VALUES (#id#, #parentId#, #name#, #description#, #userId#)", folder);
            tx.update("INSERT INTO " + getTablePrefix() + "_folder_audit (id, auditDateCreated, auditUserCreated, created) VALUES (?, ?, ?, ?)", new Object[] { folder.getId(), new Date(), userId, Boolean.TRUE });

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error creating folder: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error creating folder: " + e.toString(), e);
        }
    }

    public void updateFolder(Folder folder, String userId) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.update("UPDATE " + getTablePrefix() + "_folder SET parentId=#parentId#, name=#name#, description=#description#, userId=#userId# WHERE id=#id#", folder);
            tx.update("UPDATE " + getTablePrefix() + "_folder_audit SET auditDateModified=?, auditUserModified=?, created=?, modified=? WHERE id=?", new Object[] { new Date(), userId, Boolean.FALSE, Boolean.TRUE, folder.getId() });

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error updating folder: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error updating folder: " + e.toString(), e);
        }
    }

    public void deleteFolder(String folderId, String userId) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();

            tx.update("DELETE FROM " + getTablePrefix() + "_folder WHERE id=?", new String[] {folderId});
            tx.update("UPDATE " + getTablePrefix() + "_folder_audit SET auditDateDeleted=?, auditUserDeleted=?, created=?, modified=?, deleted=? WHERE id=?", new Object[] { new Date(), userId, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, folderId });

            tx.commit();
        }
        catch (Exception e) {
            log.error("Error deleting folder: " + e.toString(), e);
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error deleting folder: " + e.toString(), e);
        }
    }

    public Folder selectFolder(String folderId) throws DaoException, DataObjectNotFoundException {
        Collection results = super.select("SELECT id, parentId, name, description, userId FROM " + getTablePrefix() + "_folder WHERE id=?", Folder.class, new String[] {folderId}, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            return (Folder)results.iterator().next();
        }
    }

    /**
     *
     * @param query
     * @param parentId
     * @param userId
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of Folder objects.
     */
    public Collection selectFolderList(String query, String parentId, String userId, String sort, boolean desc, int start, int rows) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, parentId, name, description, userId FROM " + getTablePrefix() + "_folder WHERE 1=1");
        if (query != null && query.trim().length() > 0) {
            query = "%" + query.trim() + "%";
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            args.add(query);
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
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            args.add(query);
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
/*
//-- Sync methods

    public Collection selectModifiedFolders(String userId) {
        return null;
    }

    public Collection selectModifiedContacts(String userId) {
        return null;
    }

    public void resetModifiedFolders(String userId) {
    }

    public void resetModifiedContacts(String userId) {
    }

    public void clearDeletedFolders(String userId) {
    }

    public void clearDeletedContacts(String userId) {
    }
*/

    public Collection getUpdatedContactList(String ownerId, Date sinceDate, Date untilDate) throws DaoException {

        String sql = "SELECT c.id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, address, city, state, postcode, country, phone, extension, fax, mobile, ownerId, customProperty, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails " +
                        ", auditDateCreated, auditUserCreated, auditDateModified, auditUserModified, created, modified, createdTime, modifiedTime " +
                        " FROM " + getTablePrefix() + "_contact c " +
                        " LEFT OUTER JOIN " + getTablePrefix() + "_contact_audit a ON c.id=a.id " +
                        " WHERE ownerId=?";

        ArrayList params = new ArrayList();
        params.add(ownerId);

        if(sinceDate != null){
            sql += "AND modifiedTime >= ? ";
            params.add(sinceDate);
        }

        if(untilDate != null){
            sql += "AND modifiedTime <= ? ";
            params.add(untilDate);
        }

        sql += " AND createdTime != modifiedTime";

        sql += " AND contactGroupIds IS NULL";

        sql += " AND modified=1";

        //Ensuring that objects matching the getNewContactList methods are not returned
        sql += " AND NOT c.id IN (SELECT id FROM " + getTablePrefix() + "_contact WHERE ownerId=? ";
        params.add(ownerId);
        if(sinceDate != null){
            sql += "AND createdTime >= ? ";
            params.add(sinceDate);
        }
        if(untilDate != null){
            sql += "AND createdTime <= ? ";
            params.add(untilDate);
        }
        sql += "AND contactGroupIds IS NULL)";

        return super.select(sql, Contact.class, params.toArray(), 0, -1);
    }

    public Collection getNewContactList(String ownerId, Date sinceDate, Date untilDate) throws DaoException {
        String sql = "SELECT id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, phone, extension, fax, mobile, ownerId, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails, createdTime, modifiedTime FROM " + getTablePrefix() + "_contact " +
                "WHERE ownerId=? ";
        ArrayList params = new ArrayList();
        params.add(ownerId);

        if(sinceDate != null){
            sql += "AND createdTime >= ? ";
            params.add(sinceDate);
        }

        if(untilDate != null){
            sql += "AND createdTime <= ? ";
            params.add(untilDate);
        }

        //do not include Distribution list
        sql += "AND contactGroupIds IS NULL";
        return super.select(sql.toString(), Contact.class, params.toArray(), 0, -1);
    }

    public Collection getContactsByCriteria(Contact contact) throws DaoException {
        //where clause
        String whereClause = "1=1";
        if(contact.getFirstName()!=null && !"".equals(contact.getFirstName())){
            whereClause += " AND firstName=#firstName#";
        }

        if(contact.getLastName()!=null && !"".equals(contact.getLastName())){
            whereClause += " AND lastName=#lastName#";
        }

        if(contact.getMobile()!=null && !"".equals(contact.getMobile())){
            whereClause += " AND mobile=#mobile#";
        }

        if(contact.getEmail()!=null && !"".equals(contact.getEmail())){
            whereClause += " AND email=#email#";
        }

        if(contact.getOwnerId()!=null && !"".equals(contact.getOwnerId())){
            whereClause += " AND ownerId=#ownerId#";
        }

        whereClause += " AND contactGroupIds IS NULL";

        String sql = "SELECT c.id, folderId, companyId, userId, username, title, firstName, middleName, lastName, nickName, email, designation, company, address, city, state, postcode, country, phone, extension, fax, mobile, ownerId, customProperty, approved, comments, contactGroup, contactGroupIds, contactGroupIntranetIds, contactGroupEmails " +
                        ", auditDateCreated, auditUserCreated, auditDateModified, auditUserModified, created, modified, createdTime, modifiedTime " +
                        " FROM " + getTablePrefix() + "_contact c " +
                        " LEFT OUTER JOIN " + getTablePrefix() + "_contact_audit a ON c.id=a.id " +
                        " WHERE " + whereClause;        
        return super.select(sql, Contact.class, contact, 0, -1);
    }

    public Collection getDeletedContactsKeys(String ownerId, Date sinceDate, Date untilDate) throws DaoException {


        String sql = "SELECT id AS contactId FROM " + getTablePrefix() + "_contact_audit WHERE auditUserCreated=? ";
        ArrayList params = new ArrayList();
        params.add(ownerId);
        if(sinceDate != null){
            sql += "AND auditDateDeleted >= ? ";
            params.add(sinceDate);
        }

        if(untilDate != null){
            sql += "AND auditDateDeleted <= ? ";
            params.add(untilDate);
        }

        sql += "AND deleted=?";
            params.add(Boolean.TRUE);

        Collection res = super.select(sql, HashMap.class, params.toArray(), 0, -1);
        ArrayList list = new ArrayList();
        for(Iterator itr=res.iterator(); itr.hasNext();){
            Map map = (Map) itr.next();
            list.add(map.get("contactId"));
        }

        return list;
    }
        
}
