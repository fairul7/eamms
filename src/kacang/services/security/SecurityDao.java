package kacang.services.security;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.model.operator.OperatorEquals;
import kacang.util.JdbcUtil;
import kacang.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.tms.fms.department.model.FMSDepartment;

/**
 * Implementation of a Data Access Object that manages
 * the persistence of Users, Groups and Principals
 * for the Security Service. This implementation uses
 * generic SQL. Extend this class to customize database-specific
 * behaviour.
 */
public class SecurityDao extends DataSourceDao {
    /** Interface Implementation */

    /**
     * Initializes the data source, e.g. creates tables, etc.
     * @throws DaoException if a fatal error occurs.
     */
    public void init() throws DaoException {
        try {
            super.update("ALTER TABLE `security_user` ADD `locale` VARCHAR(50)", null);
        }
        catch(DaoException e) {
        }
		try {
            super.update("ALTER TABLE `security_principal_permission` CHANGE `moduleId` `moduleId` VARCHAR(250)  NOT NULL", null);
		}
        catch(DaoException e) {
		}
        super.update("CREATE TABLE security_user(" +
                "id VARCHAR(250) NOT NULL, username VARCHAR(255), password VARCHAR(255), weakpass VARCHAR(255),  " +
                "firstName VARCHAR(255), lastName VARCHAR(255), nickName VARCHAR(255), title " +
                "VARCHAR(255), designation VARCHAR(255), email1 VARCHAR(255), email2 " +
                "VARCHAR(255), email3 VARCHAR(255), company VARCHAR(255), homepage " +
                "VARCHAR(255), address VARCHAR(255), postcode VARCHAR(255), city " +
                "VARCHAR(255), state VARCHAR(255), country VARCHAR(255), telOffice " +
                "VARCHAR(255), telHome VARCHAR(255), telMobile VARCHAR(255), fax " +
                "VARCHAR(255), notes VARCHAR(255), " +
                "property1 VARCHAR(255), property2 VARCHAR(255), property3 VARCHAR(255), " +
                "property4 VARCHAR(255), property5 VARCHAR(255), active CHAR(1) NOT NULL, locale VARCHAR(50), " +
                "PRIMARY KEY(id))", null);
        super.update("CREATE TABLE security_group(id VARCHAR(250) NOT NULL, groupName VARCHAR(255), description VARCHAR(255), active CHAR(1) NOT NULL, PRIMARY KEY(id))", null);
        super.update("CREATE TABLE security_principal_permission(principalId VARCHAR(250) NOT NULL, moduleId VARCHAR(250) NOT NULL, permissionId VARCHAR(250) NOT NULL, objectId VARCHAR(35))", null);
        super.update("CREATE TABLE security_user_group(userId VARCHAR(250) NOT NULL, groupId VARCHAR(250) NOT NULL, PRIMARY KEY(userId, groupId))", null);
    }

    /** Methods for class User */

    /**
     * Loads a specific user.
     * @param userId the unique identifier for the user.
     * @return a User object.
     * @throws DaoException if an error occurs.
     */
    public User selectUser(String userId) throws DaoException, DataObjectNotFoundException {
        Object[] args = new Object[]{userId};
        Collection list = super.select("SELECT id, username, password, weakpass, firstName, lastName, nickName, title, designation, email1, email2, email3, company, homepage, address, postcode, city, state, country, telOffice, telHome, telMobile, fax, notes, property1, property2, property3, property4, property5, active, locale FROM security_user WHERE id = ?", User.class, args, 0, -1);
        if (list.size() <= 0) throw new DataObjectNotFoundException("User " + userId + " unavailable");
        return (User) list.iterator().next();
    }

    /**
     * Loads users with a specific username.
     */
    public Collection selectUsersByUsername(String username) throws DaoException, DataObjectNotFoundException {
        Object[] args = new Object[]{username};
        Collection list = super.select("SELECT id, username, password, weakpass, firstName, lastName, nickName, title, designation, email1, email2, email3, company, homepage, address, postcode, city, state, country, telOffice, telHome, telMobile, fax, notes, property1, property2, property3, property4, property5, active, locale FROM security_user WHERE username = ?", User.class, args, 0, -1);
        if (list.size() <= 0) throw new DataObjectNotFoundException("User " + username + " unavailable");
        return list;
    }

    /**
     * Find all active users.
     *
     * @param start starting position
     * @param maxResults max row return
     * @param sort column to sort according to
     * @param descending true if descending sort order
     * @return collection of active users
     * @throws DaoException if failed to retrieve records from database
     */
    public Collection selectActiveUsers(int start, int maxResults, String sort, boolean descending)
            throws DaoException {
        DaoQuery prop = new DaoQuery();
        prop.addProperty(new OperatorEquals("active", "1", null));
        return selectUsers(prop, start, maxResults, sort, descending);
    }

    /**
     * Find users based on the given DAO query.
     *
     * @param properties search criteria
     * @param start starting position
     * @param maxResults max row return
     * @param sort column to sort according to
     * @param descending true if descending sort order
     * @return collection of active users
     * @throws DaoException if failed to retrieve records from database
     */
    public Collection selectUsers(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException {
        Collection list = new ArrayList();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsUsers = null;
        int currentRow = 0;
        try {
            con = super.getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, 
            		"SELECT id, username, password, weakpass, firstName, lastName, nickName, title, designation, email1, email2, email3, company, " +
            		"homepage, address, postcode, city, state, country, telOffice, telHome, telMobile, fax, notes, property1, property2, property3, " +
            		"property4, property5, active, locale, department, unit FROM security_user u WHERE id = id" + properties.getStatement() + getSort("firstName", descending), properties.getArray());
            try {
                if (maxResults > 0)
                    statement.setMaxRows(start + maxResults);
                rsUsers = statement.executeQuery();
                while (rsUsers.next()) {
                    if (currentRow >= start && (maxResults < 0 || currentRow < (start + maxResults))) {
                        User user = new User();
                        user.setId(rsUsers.getString("id"));
                        user.setUsername(rsUsers.getString("username"));
                        user.setPassword(rsUsers.getString("password"));
                        user.setProperty("weakpass", rsUsers.getString("weakpass"));
                        user.setProperty("firstName", rsUsers.getString("firstName"));
                        user.setProperty("lastName", rsUsers.getString("lastName"));
                        user.setProperty("email1", rsUsers.getString("email1"));
                        user.setProperty("address", rsUsers.getString("address"));
                        user.setProperty("postcode", rsUsers.getString("postcode"));
                        user.setProperty("city", rsUsers.getString("city"));
                        user.setProperty("state", rsUsers.getString("state"));
                        user.setProperty("country", rsUsers.getString("country"));
                        user.setProperty("telOffice", rsUsers.getString("telOffice"));
                        user.setProperty("telHome", rsUsers.getString("telHome"));
                        user.setProperty("telMobile", rsUsers.getString("telMobile"));
                        user.setProperty("fax", rsUsers.getString("fax"));
                        user.setProperty("active", rsUsers.getString("active"));
                        user.setProperty("notes", rsUsers.getString("notes"));
                        user.setProperty("property1", rsUsers.getString("property1"));
                        user.setProperty("property2", rsUsers.getString("property2"));
                        user.setProperty("property3", rsUsers.getString("property3"));
                        user.setProperty("property4", rsUsers.getString("property4"));
                        user.setProperty("property5", rsUsers.getString("property5"));
                        user.setProperty("locale", rsUsers.getString("locale"));
                        user.setProperty("department", rsUsers.getString("department"));
                        user.setProperty("unit", rsUsers.getString("unit"));

                        list.add(user);
                    }
                    currentRow++;
                }
            } catch (SQLException e) {
                Log.getLog(SecurityDao.class).error(e);
            } finally {
                if (rsUsers != null)
                    rsUsers.close();
                if (statement != null)
                    statement.close();
                if (con != null)
                    con.close();
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        }
        return list;
    }

    /**
     * Stores a user to the data source.
     * @param user the User object to store.
     * @throws DaoException if an error occurs.
     */
    public void storeUser(User user) throws DaoException {
        // check existing user
        Collection existing = super.select("SELECT id FROM security_user WHERE id=?", HashMap.class, new Object[] { user.getId() }, 0, 1);
        if (existing.size() > 0) {
            super.update(
                    "UPDATE security_user SET username = #username#, password = #password#, weakpass = #weakpass#, " +
                    "firstName = #firstName#, lastName = #lastName#, nickName = #nickName#, title = #title#," +
                    "designation = #designation#, email1 = #email1#, email2 = #email2#, email3 = #email3#, company = #company#, homepage = #homepage#," +
                    "address = #address#, postcode = #postcode#, city = #city#, state = #state#, country = #country#, telOffice = #telOffice#," +
                    "telHome = #telHome#, telMobile = #telMobile#, fax = #fax#, notes = #notes#, active = #active#," +
                    "property1 = #property1#, property2 = #property2#, property3 = #property3#, property4 = #property4#, property5 = #property5#, locale=#locale# WHERE id=#id#", user);
        } else {
            // user not found, insert user
            super.update(
                    "INSERT INTO security_user (id, username, password, weakpass, " +
                    "firstName, lastName, nickName, title, designation, " +
                    "email1, email2, email3, company, homepage, address, postcode, city, state, country, " +
                    "telOffice, telHome, telMobile, fax, notes, active," +
                    "property1, property2, property3, property4, property5, locale) VALUES " +
                    "(#id#, #username#, #password#, #weakpass#, " +
                    "#firstName#, #lastName#, #nickName#, #title#, #designation#, " +
                    "#email1#, #email2#, #email3#, #company#, #homepage#, #address#, #postcode#, #city#, #state#, #country#, " +
                    "#telOffice#, #telHome#, #telMobile#, #fax#, #notes#, #active#," +
                    "#property1#, #property2#, #property3#, #property4#, #property5#, #locale#)", user);
        }
    }

    /**
     * Deletes a user.
     * @param userId the unique identifier for the user.
     * @throws DaoException if an error occurs.
     */
    public void deleteUser(String userId) throws DaoException {
        User user = new User();
        user.setId(userId);
        super.update("DELETE FROM security_user WHERE id = #id#", user);
    }

    /** Methods for class Group */

    /**
     * Find all active groups.
     *
     * @param start starting position
     * @param maxResults max row return
     * @param sort column to sort according to
     * @param descending true if descending sort order
     * @return collection of active users
     * @throws DaoException if failed to retrieve records from database
     */
    public Collection selectActiveGroups(int start, int maxResults,
                                         String sort, boolean descending) throws DaoException {
        DaoQuery prop = new DaoQuery();
        prop.addProperty(new OperatorEquals("active", "1", null));
        return selectGroups(prop, start, maxResults, sort, descending);
    }

    /**
     * Find groups based on the given DAO query.
     *
     * @param properties search criteria
     * @param start starting position
     * @param maxResults max row return
     * @param sort column to sort according to
     * @param descending true if descending sort order
     * @return collection of active users
     * @throws DaoException if failed to retrieve records from database
     */
    public Collection selectGroups(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException {
        Collection list = new ArrayList();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsGroups = null;
        int currentRow = 0;
        try {
            con = super.getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT id, groupName, description, active FROM security_group WHERE id = id" + properties.getStatement() + getSort("groupName", descending), properties.getArray());
            try {
                if (maxResults > 0)
                    statement.setMaxRows(start + maxResults);
                rsGroups = statement.executeQuery();
                while (rsGroups.next()) {
                    if (currentRow >= start && (maxResults < 0 || currentRow < (start + maxResults))) {
                        Group group = new Group();
                        group.setId(rsGroups.getString("id"));
                        group.setGroupName(rsGroups.getString("groupName"));
                        group.setProperty("groupName", rsGroups.getString("description"));
                        group.setProperty("active", rsGroups.getString("active"));
                        list.add(group);
                    }
                    currentRow++;
                }
            } catch (SQLException e) {
                Log.getLog(SecurityDao.class).error(e);
            } finally {
                if (rsGroups != null)
                    rsGroups.close();
                if (statement != null)
                    statement.close();
                if (con != null)
                    con.close();
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        }
        return list;
        //return super.select("SELECT id, groupName, description, active FROM security_group WHERE id = id" + properties.getStatement() + getSort(sort, descending), Group.class, properties.getArray(), start, maxResults);
    }

    /**
     * Loads a collection of groups using the LIKE operator.
     *
     * @param properties an array containing the value of parameters for the query.
     * @param start the starting index of results to return.
     * @param maxResults the maximum number of results to return, use -ve value to return all results.
     * @return a Collection of Group objects.
     * @throws DaoException if an error occurs.
     */
    public Collection selectLikeGroups(Map properties, int start, int maxResults) throws DaoException {
        Map newProperties = new HashMap();
        for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
            String property = (String) i.next();
            newProperties.put(property, "%" + (String) properties.get(property) + "%");
        }
        return constructGroupSelect(properties, start, maxResults, " LIKE ?");
    }

    /**
     * Loads a collection of groups using the specified operator.
     *
     * @param properties an array containing the value of parameters for the query.
     * @param start the starting index of results to return.
     * @param maxResults the maximum number of results to return, use -ve value to return all results.
     * @param operator the operation to execute the query with
     * @return a Collection of Group objects.
     * @throws DaoException if an error occurs.
     */
    private Collection constructGroupSelect(Map properties, int start, int maxResults, String operator) throws DaoException {
        String query = "";
        Collection args = new ArrayList();
        if (!(properties == null)) {
            Iterator propertySet = properties.keySet().iterator();
            while (propertySet.hasNext()) {
                String property = (String) propertySet.next();
                query = query + " AND " + property + operator;
                args.add(properties.get(property));
            }
        }
        return super.select("SELECT id, groupName, description, active FROM security_group WHERE id = id" + query, new Group().getClass(), args.toArray(), start, maxResults);
    }

    /**
     * Loads a specific group.
     * @param groupId the unique identifier for the group.
     * @return a Group object.
     * @throws DaoException if an error occurs.
     */
    public Group selectGroup(String groupId) throws DaoException, DataObjectNotFoundException {
        Object[] args = new Object[]{groupId};
        Collection list = super.select("SELECT id, groupName, description, active FROM security_group WHERE id = ?", Group.class, args, 0, -1);
        if (list.size() <= 0) throw new DataObjectNotFoundException("Group " + groupId + " unavailable");
        return (Group) list.iterator().next();
    }

    /**
     * Stores a group to the data source.
     * @param group the Group object to store.
     * @throws DaoException if an error occurs.
     */
    public void storeGroup(Group group) throws DaoException {
        int result = super.update("UPDATE security_group SET groupName=#groupName#, description=#description#, active=#active# WHERE id=#id#", group);
        if (result == 0) {
            // group not found, insert group
            super.update("INSERT INTO security_group (id, groupName, description, active) " +
                    "VALUES (#id#, #groupName#, #description#, #active#)", group);
        }
    }

    /**
     * Deletes a group.
     * @param groupId the unique identifier for the group.
     * @throws DaoException if an error occurs.
     */
    public void deleteGroup(String groupId) throws DaoException {
        Group group = new Group();
        group.setId(groupId);
        super.update("DELETE FROM security_group WHERE id = #id#", group);
    }


//-- relationships

    /**
     * Loads a collection of principalPermissions.
     *
     * @param properties an array containing the value of parameters for the query.
     * @param start the starting index of results to return.
     * @param maxResults the maximum number of results to return, use -ve value to return all results.
     * @return a Collection of principalPermission objects.
     * @throws DaoException if an error occurs.
     */
    public Collection selectPrincipalPermission(Map properties, int start, int maxResults) throws DaoException {
        String query = "";
        Collection args = new ArrayList();
        if (!(properties == null)) {
            Iterator propertySet = properties.keySet().iterator();
            while (propertySet.hasNext()) {
                String property = (String) propertySet.next();
                query = query + " AND " + property + " = ?";
                args.add(properties.get(property));
            }
        }
        return super.select("SELECT principalId, moduleId, permissionId, objectId FROM security_principal_permission WHERE principalId = principalId" + query, PrincipalPermission.class, args.toArray(), start, maxResults);
    }

    /**
     * Stores an array of principal-permission mappings.
     * @param mappings an array of PrincipalPermission objects to store.
     * @throws DaoException if an error occurs.
     */
    public void storePrincipalPermissions(PrincipalPermission[] mappings) throws DaoException {
        for (int i = 0; i < mappings.length; i++) {
            super.update("INSERT INTO security_principal_permission (principalId, moduleId, permissionId, objectId) VALUES (#principalId#, #moduleId#, #permissionId#, #objectId#)", mappings[i]);
        }
    }

    /**
     * Deletes specified principal-permission mappings.
     * @param mappings the principal-permission mappings to delete.
     * @throws DaoException if an error occurs.
     */
    public void deletePrincipalPermissions(PrincipalPermission[] mappings) throws DaoException {
        for (int i = 0; i < mappings.length; i++) {
            super.update("DELETE FROM security_principal_permission WHERE principalId=#principalId# AND moduleId=#moduleId# AND permissionId=#permissionId# AND objectId=#objectId#", mappings[i]);
        }
    }

    /**
     * Deletes principal-permission mappings.
     * <li>Use null for principalId delete all
     * principal-permission relationships for the specified permissionId.
     * <li>Use null for permissionId to delete all
     * principal-permission relationships for the specified principalId.
     * @param principalId the unique identifier for the principal.
     * @param permissionId the unique identifier for the permission.
     * @throws DaoException if an error occurs.
     */
    public void deletePrincipalPermissions(String principalId, String moduleId, String permissionId, String objectId) throws DaoException {
        PrincipalPermission rp = new PrincipalPermission(principalId, moduleId, permissionId, objectId);
        /** Constructing query */
        String query = "";
        if (principalId != null) query = query + " AND principalId = #principalId#";
        if (moduleId != null) query = query + " AND moduleId = #moduleId#";
        if (permissionId != null) query = query + " AND permissionId = #permissionId#";
        if (objectId != null) query = query + " AND objectId = #objectId#";
        super.update("DELETE FROM security_principal_permission WHERE principalId = principalId" + query, rp);
        /**
         if (principalId != null && permissionId != null)
         {
         super.update("DELETE FROM security_principal_permission WHERE principalId = #principalId# AND permissionId = #permissionId#", rp);
         }
         else if (principalId == null && permissionId != null)
         {
         super.update("DELETE FROM security_principal_permission WHERE permissionId = #permissionId#", rp);
         }
         else if (principalId != null && permissionId == null)
         {
         super.update("DELETE FROM security_principal_permission WHERE principalId = #principalId#", rp);
         }
         */
    }

    /**
     * Returns the Groups a user belongs to.
     * @return a Collection of Group objects.
     */
    public Collection selectGroupsByUser(String userId) throws DaoException {
        String query = "SELECT g.id, g.groupName, g.active FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId AND u.id=?";
        return super.select(query, Group.class, new String[]{userId}, 0, -1);
    }

    public Collection selectGroupsByUser(String userId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException {
        String strSort = "";
        Collection args = new ArrayList();
        args.add(userId);
        args.addAll(Arrays.asList(properties.getArray()));
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (descending)
                strSort += " DESC";
        }
        String query = "SELECT g.id, g.groupName, g.active FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId AND u.id=?";
        return super.select(query + properties.getStatement() + strSort, Group.class, args.toArray(), start, maxResults);
    }

    /**
     * Returns the Users in a group.
     * @return a Collection of User objects.
     */
    public Collection selectUsersByGroup(String groupId) throws DaoException {
        String query = "SELECT u.id, u.username, u.password, u.weakpass, u.firstName, u.lastName, u.nickName, u.title, u.designation, u.email1, u.email2, u.email3, u.company, u.homepage, u.address, u.postcode, u.city, u.state, u.country, u.telOffice, u.telHome, u.telMobile, u.fax, u.notes, u.property1, u.property2, u.property3, u.property4, u.property5, u.active, u.locale FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId AND g.id=?";
        return super.select(query, User.class, new String[]{groupId}, 0, -1);
    }

    /**
     * Returns the Users in a group.
     * @return a Collection of User objects.
     */
    public Collection selectUsersByGroup(String groupId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException {
        String strSort = "";
        Collection args = new ArrayList();
        args.add(groupId);
        args.addAll(Arrays.asList(properties.getArray()));
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (descending)
                strSort += " DESC";
        }
        String query = "SELECT u.id, u.username, u.password, u.weakpass, u.firstName, u.lastName, u.nickName, u.title, u.designation, u.email1, u.email2, u.email3, u.company, u.homepage, u.address, u.postcode, u.city, u.state, u.country, u.telOffice, u.telHome, u.telMobile, u.fax, u.notes, u.property1, u.property2, u.property3, u.property4, u.property5, u.active, u.locale FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId AND g.id=?";
        return super.select(query + properties.getStatement() + strSort, User.class, args.toArray(), start, maxResults);
    }

    /**
     * Selects and returns a Collection of User objects that are associated with a specified permissionId,
     * either directly or through groups.
     * @param permissionId
     * @param userId
     * @param start
     * @param maxResults
     * @param sort
     * @param desc
     * @return a Collection of User objects
     * @throws DaoException
     */
    public Collection selectUsersByPermission(String permissionId, String userId, Boolean active, int start, int maxResults, String sort, boolean desc) throws DaoException {
        String strSort = "";
        Collection args = new ArrayList();
        String query =
                "SELECT u.id, u.username, u.firstName, u.lastName, u.email1, u.active, u.locale " +
                "FROM security_user u " +
                "INNER JOIN security_principal_permission p ON u.id=p.principalId " +
                "WHERE p.permissionId=? ";
        args.add(permissionId);
        if (active != null) {
            query += " AND u.active=? ";
            args.add(active);
        }
        if (userId != null) {
            query += " AND u.id=? ";
            args.add(userId);
        }
        query +=
                "UNION " +
                "SELECT u.id, u.username, u.firstName, u.lastName, u.email1, u.active, u.locale " +
                "FROM security_user u " +
                "INNER JOIN security_user_group ug ON u.id=ug.userId " +
                "INNER JOIN security_group g ON ug.groupId = g.id " +
                "INNER JOIN security_principal_permission p ON g.id=p.principalId " +
                "WHERE p.permissionId=? ";
        args.add(permissionId);
        if (active != null) {
            query += " AND g.active=? ";
            args.add(active);
        }
        if (userId != null) {
            query += " AND u.id=? ";
            args.add(userId);
        }
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (desc)
                strSort += " DESC";
        }
        query += strSort;
        return super.select(query, User.class, args.toArray(), start, maxResults);
    }

    /**
     * Stores an array of user-group mappings.
     * @param mappings an array of UserGroup objects to store.
     * @throws DaoException if an error occurs.
     */
    public void storeUserGroups(UserGroup[] mappings) throws DaoException {
        for (int i = 0; i < mappings.length; i++) {
            super.update("INSERT INTO security_user_group (userId, groupId) VALUES (#userId#, #groupId#)", mappings[i]);
        }
    }

    /**
     * Deletes specified user-group mappings.
     * @param mappings the user-group mappings to delete.
     * @throws DaoException if an error occurs.
     */
    public void deleteUserGroups(UserGroup[] mappings) throws DaoException {
        for (int i = 0; i < mappings.length; i++) {
            super.update("DELETE FROM security_user_group WHERE userId=#userId# AND groupId=#groupId#", mappings[i]);
        }
    }

    /**
     * Deletes user-group mappings.
     * <li>Use null for userId delete all
     * user-group relationships for the specified groupId.
     * <li>Use null for groupId to delete all
     * user-group relationships for the specified userId.
     * @param userId the unique identifier for the user.
     * @param groupId the unique identifier for the group.
     * @throws DaoException if an error occurs.
     */
    public void deleteUserGroups(String userId, String groupId) throws DaoException {
        UserGroup ug = new UserGroup(userId, groupId);
        if (userId != null && groupId != null) {
            super.update("DELETE FROM security_user_group WHERE userId = #userId# AND groupId = #groupId#", ug);
        } else if (userId == null && groupId != null) {
            super.update("DELETE FROM security_user_group WHERE groupId = #groupId#", ug);
        } else if (userId != null && groupId == null) {
            super.update("DELETE FROM security_user_group WHERE userId = #userId#", ug);
        }
    }

    public int selectGroupCount(DaoQuery properties) throws DaoException {
        Collection list = super.select("SELECT COUNT(id) AS value FROM security_group WHERE id = id" + properties.getStatement(), HashMap.class, properties.getArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    public int selectUserCount(DaoQuery properties) throws DaoException {
        Collection list = super.select("SELECT COUNT(id) AS value FROM security_user u WHERE id = id" + properties.getStatement(), HashMap.class, properties.getArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    public int selectUsersByGroupCount(String groupId, DaoQuery properties) throws DaoException {
        Collection args = new ArrayList();
        args.add(groupId);
        args.addAll(Arrays.asList(properties.getArray()));
        String query = "SELECT COUNT(u.id) AS value FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId AND g.id=?";
        Collection list = super.select(query + properties.getStatement(), HashMap.class, args.toArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    private String getSort(String sort, boolean descending) {
        String strSort = "";
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (descending)
                strSort += " DESC";
        }
        return strSort;
    }

    /**
         * Selects and returns a Collection of Group objects that are associated with a specified permissionId
         * @param permissionId
         * @param groupId
         * @param start
         * @param maxResults
         * @param sort
         * @param desc
         * @return a Collection of User objects
         * @throws DaoException
         */
    public Collection selectGroupsByPermission(String permissionId, String groupId, Boolean active, int start, int maxResults, String sort, boolean desc) throws DaoException {
            String strSort = "";
            Collection args = new ArrayList();
            String query =
                    "SELECT g.id, g.groupName, g.description, g.active " +
                    "FROM security_group g " +
                    "INNER JOIN security_principal_permission p ON g.id=p.principalId " +
                    "WHERE p.permissionId=? ";
            args.add(permissionId);
            if (active != null) {
                query += " AND g.active=? ";
                args.add(active);
            }
            if (groupId != null) {
                query += " AND g.id=? ";
                args.add(groupId);
            }
            if (sort != null) {
                strSort += " ORDER BY " + sort;
                if (desc)
                    strSort += " DESC";
            }
            query += strSort;
            return super.select(query, Group.class, args.toArray(), start, maxResults);
        }

    public void updateUserState(String userId, String active) throws DaoException
    {
        super.update("UPDATE security_user SET active = ? WHERE id = ?", new Object[] {active, userId});
    }
    
    public User selectUsersByEmail(String email)throws DaoException{
		Collection result = super.select("SELECT id,username,firstName,lastName,email1 " +
				"FROM security_user " +
				"WHERE email1 = ?", User.class, new Object[] {email}, 0, 1);
		
		if (result.size() <= 0)
			try {
				throw new DataObjectNotFoundException("User " + email + " unavailable");
			} catch (DataObjectNotFoundException e) {
				e.printStackTrace();
			}
        return (User) result.iterator().next();
	}
}
