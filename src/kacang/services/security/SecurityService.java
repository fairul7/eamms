package kacang.services.security;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DaoException;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.DefaultService;
import kacang.services.presence.PresenceException;
import kacang.services.presence.PresenceService;
import kacang.services.session.SessionService;
import kacang.ui.WidgetManager;
import kacang.util.Encryption;
import kacang.util.Log;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentDao;


/**
 * The default implementation of the Security Service
 * in the framework.
 */
public class SecurityService extends DefaultService {

    public static final String SESSION_KEY_USER = "currentUser";
    public static final String SESSION_KEY_USER_ID = "id";
    public static final String ANONYMOUS_USER_ID = "anonymous";
    public static final String PROPERTY_KEY_HOST = "host";
    public static final String PROPERTY_EMAIL_COMPULSORY = "kacang.services.security.User.EmailCompulsory";

    public static final String EVENT_USER_ADDED = "EventUserAdded";
    public static final String EVENT_USER_UPDATED = "EventUserUpdated";
    public static final String EVENT_USER_REMOVED = "EventUserRemoved";
    public static final String EVENT_GROUP_ADDED = "EventGroupAdded";
    public static final String EVENT_GROUP_UPDATED = "EventGroupUpdated";
    public static final String EVENT_GROUP_REMOVED = "EventGroupRemoved";

    public static final String PERMISSION_GROUP_ADD = "kacang.services.security.Group.add";
    public static final String PERMISSION_GROUP_EDIT = "kacang.services.security.Group.update";
    public static final String PERMISSION_GROUP_DELETE = "kacang.services.security.Group.delete";
    public static final String PERMISSION_GROUP_VIEW = "kacang.services.security.Group.view";
    public static final String PERMISSION_USER_ADD = "kacang.services.security.User.add";
    public static final String PERMISSION_USER_EDIT = "kacang.services.security.User.update";
    public static final String PERMISSION_USER_DELETE = "kacang.services.security.User.delete";
    public static final String PERMISSION_USER_VIEW = "kacang.services.security.User.view";

    protected Log log;
    protected Cache permissionCache = new Cache(true, false, true);
    private Collection eventListenerList;

    public static final Integer DEFAULT_CACHE_DURATION_PERMISSION = new Integer(300);
    public static final String APPLICATION_KEY_PERMISSION_CACHE_DURATION = "services.security.permission-cache-duration";
    protected Integer permissionCacheDuration = DEFAULT_CACHE_DURATION_PERMISSION;

    /*
     * Default constructor.
     */
    public SecurityService() {
        log = Log.getLog(getClass());
        eventListenerList = new ArrayList();
    }

    /**
     * Performs initialization for the handler.
     */
    public void init() {
    }

    public void addEventListener(SecurityEventListener listener) {
        if (!eventListenerList.contains(listener)) {
            eventListenerList.add(listener);
        }
    }

    public void removeEventListener(SecurityEventListener listener) {
        if (eventListenerList.contains(listener)) {
            eventListenerList.contains(listener);
        }
    }

    public Collection getEventListenerList() {
        return eventListenerList;
    }

    public void fireSecurityEvent(SecurityEvent evt) {
        for (Iterator i = eventListenerList.iterator(); i.hasNext();) {
            SecurityEventListener listener = (SecurityEventListener) i.next();
            listener.handleSecurityEvent(evt);
        }
    }

//---- manage users

    /**
     * Creates a new user.
     * @param user a User object representing the new user.
     * @throws SecurityException if an error occurs.
     */
    public void addUser(User user, boolean prefix) throws SecurityException {
        // check validity of user
        if (user == null || user.getId() == null) throw new SecurityException();

        // persist user
        try {
            if (prefix) {
                user.setId(User.class.getName() + "_" + user.getId());
                user.setProperty("weakpass", Encryption.encrypt((String) user.getProperty("weakpass"), user.getId()));
            }
            SecurityDao dao = (SecurityDao) getDao();
            dao.storeUser(user);
            log.debug("addUser: user " + user.getId() + " added");
            fireSecurityEvent(new SecurityEvent(EVENT_USER_ADDED, user));
            log.debug("addUser: event listeners notified");
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Updates an existing user.
     * @param user a User object containing updated user information.
     * @throws SecurityException if an error occurs.
     */
    public void updateUser(User user) throws SecurityException {
        // check validity of user
        if (user == null || user.getId() == null) throw new SecurityException();

        // persist user
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.storeUser(user);
            log.debug("user " + user.getId() + " updated");
            fireSecurityEvent(new SecurityEvent(EVENT_USER_UPDATED, user));
            log.debug("event listeners notified");
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Removes an existing user.
     * @param userId the unique identifier for the user.
     * @throws SecurityException if an error occurs.
     */
    public void removeUser(String userId) throws SecurityException {
        // check validity of user
        if (userId == null) throw new SecurityException();

        // disallow deletion of anonymous user
        if (ANONYMOUS_USER_ID.equals(userId)) {
            return;
        }

        // delete user
        try {
            SecurityDao dao = (SecurityDao) getDao();
            User user = dao.selectUser(userId);
            dao.deleteUser(userId);
            dao.deleteUserGroups(userId, null);
            dao.deletePrincipalPermissions(userId, null, null, null);
            log.debug("user " + userId + " removed");
            fireSecurityEvent(new SecurityEvent(EVENT_USER_REMOVED, user));
            log.debug("event listeners notified");
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Returns an existing user.
     * @param userId the unique identifier for the user.
     * @return the desired User.
     * @throws SecurityException if an error occurs.
     */
    public User getUser(String userId) throws SecurityException {
        // check validity of user
        if (userId == null) throw new SecurityException();

        // select user
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUser(userId);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Returns existing users based on a username.
     * @param username the username for the user.
     * @return a Collection of matching User objects.
     * @throws SecurityException if an error occurs.
     */
    public Collection getUsersByUsername(String username) throws SecurityException {
        // select users
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUsersByUsername(username);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public Collection getUsers(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SecurityException {
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUsers(properties, start, maxResults, sort, descending);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

//---- manage groups

    /**
     * Creates a new group.
     * @param group a Group object representing the new group.
     * @throws SecurityException if an error occurs.
     */
    public void addGroup(Group group, boolean prefix) throws SecurityException {
        // check validity of group
        if (group == null || group.getId() == null) throw new SecurityException();
        // persist group
        try {
            if (prefix)
                group.setId(Group.class.getName() + "_" + group.getId());
            SecurityDao dao = (SecurityDao) getDao();
            dao.storeGroup(group);
            log.debug("group " + group.getId() + " added");
            fireSecurityEvent(new SecurityEvent(EVENT_GROUP_ADDED, group));
            log.debug("event listeners notified");
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Updates an existing group.
     * @param group a Group object containing updated group information.
     * @throws SecurityException if an error occurs.
     */
    public void updateGroup(Group group) throws SecurityException {
        // check validity of group
        if (group == null || group.getId() == null) throw new SecurityException();

        // persist group
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.storeGroup(group);
            log.debug("group " + group.getId() + " updated");
            fireSecurityEvent(new SecurityEvent(EVENT_GROUP_UPDATED, group));
            log.debug("event listeners notified");
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Removes an existing group.
     * @param groupId the unique identifier for the group.
     * @throws SecurityException if an error occurs.
     */
    public void removeGroup(String groupId) throws SecurityException {
        // check validity of group
        if (groupId == null) throw new SecurityException();

        // persist group
        try {
            SecurityDao dao = (SecurityDao) getDao();
            Group group = getGroup(groupId);
            dao.deleteGroup(groupId);
            dao.deleteUserGroups(null, groupId);
            dao.deletePrincipalPermissions(groupId, null, null, null);
            log.debug("group " + groupId + " removed");
            fireSecurityEvent(new SecurityEvent(EVENT_GROUP_REMOVED, group));
            log.debug("event listeners notified");
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Returns an existing group.
     * @param groupId the unique identifier for the group.
     * @return the desired Group.
     * @throws SecurityException if an error occurs.
     */
    public Group getGroup(String groupId) throws SecurityException {
        // check validity of group
        if (groupId == null) throw new SecurityException();

        // select group
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectGroup(groupId);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public Collection getGroups(DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SecurityException {
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectGroups(properties, start, maxResults, sort, descending);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

//--- relationships

    /**
     * Checks to see whether a user belongs to a particular group.
     * @param userId the unique identifier for the user.
     * @param groupId the unique identifier for the group.
     * @throws SecurityException if an error occurs.
     */
    public boolean isInGroup(String userId, String groupId) throws SecurityException {
        // validate params
        if (userId == null || groupId == null) throw new SecurityException();
        // select user group relationships
        Collection userGroups = new HashSet(getUserGroups(userId));
        // check for relationship
        Group g = new Group();
        g.setId(groupId);
        return userGroups.contains(g);
    }

    /**
     * Checks to see whether a user is associated with a particular permission.
     * @param principalId the unique identifier for the user.
     * @param permissionId the unique identifier for the permission.
     * @throws SecurityException if an error occurs.
     */
    public boolean hasPermission(String principalId, String permissionId, String moduleId, String objectId) throws SecurityException {
        // validate params
        if (principalId == null)
            throw new SecurityException("Invalid call to hasPermission, principalId must not be null");
        try {

            if (permissionCacheDuration == null) {
                String durationStr = Application.getInstance().getProperty(APPLICATION_KEY_PERMISSION_CACHE_DURATION);
                if (durationStr == null || durationStr.trim().length() == 0) {
                    try {
                        permissionCacheDuration = new Integer(durationStr);
                    }
                    catch (Exception e) {
                        permissionCacheDuration = DEFAULT_CACHE_DURATION_PERMISSION;
                    }
                }
                log.info("Security service permission cache duration set at " + permissionCacheDuration + "s");
            }

            // read from cache
            boolean hasPermission;
            String cacheKey = principalId + "|" + permissionId + "|" + moduleId + "|" + objectId;
            try {
                Boolean permissionValue = (Boolean)permissionCache.getFromCache(cacheKey, permissionCacheDuration.intValue());
                if (permissionValue != null) {
                    hasPermission = permissionValue.booleanValue();
//                    log.debug("Using cached permission " + cacheKey + ": " + hasPermission);
                    return hasPermission;
                }
            }
            catch (NeedsRefreshException e) {
            }

            // retrieve permission from DAO
            SecurityDao dao = (SecurityDao) getDao();
            Collection users = dao.selectUsersByPermission(permissionId, principalId, Boolean.TRUE, 0, -1, null, false);
            hasPermission = (users.size() > 0);

            // store in cache and return
//            log.debug("Read permission from DAO " + cacheKey + ": " + hasPermission);
            permissionCache.putInCache(cacheKey, new Boolean(hasPermission));
            return hasPermission;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public void flushPermissionCache() {
        permissionCache.flushAll(new Date());
    }

    /**
     * Retrieves a Collection of Users that are associated with a specified permission.
     * @param permissionId The permission
     * @param active Use Boolean.TRUE for active users and groups only, null for any
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return a Collection of User objects
     * @throws SecurityException
     */
    public Collection getUsersByPermission(String permissionId, Boolean active, String sort, boolean desc, int start, int rows) throws SecurityException {
        // validate params
        if (permissionId == null)
            throw new SecurityException("Invalid call to getUsersByPermission");
        try {
            SecurityDao dao = (SecurityDao) getDao();
            Collection users = dao.selectUsersByPermission(permissionId, null, active, start, rows, sort, desc);
            return users;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    private Map getQueryMap(String principalId, String permissionId, String moduleId, String objectId) {
        Map permission = new HashMap();
        if (principalId != null) permission.put("principalId", principalId);
        if (permissionId != null) permission.put("permissionId", permissionId);
        if (moduleId != null) permission.put("moduleId", moduleId);
        if (objectId != null) permission.put("objectId", objectId);
        return permission;
    }

    /**
     * Returns a collection of groups a user belongs to.
     *
     * @param userId the unique identifier for the user.
     * @return a Collection of Group objects.
     * @throws SecurityException if an error occurs.
     */
    public Collection getUserGroups(String userId) throws SecurityException {
        // validate params
        if (userId == null) throw new SecurityException();
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectGroupsByUser(userId);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public Collection getUserGroups(String userId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SecurityException {
        // validate params
        if (userId == null) throw new SecurityException();
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectGroupsByUser(userId, properties, start, maxResults, sort, descending);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Returns a collection of users a group contains.
     *
     * @param groupId the unique identifier for the group.
     * @return a Collection of User objects.
     * @throws SecurityException if an error occurs.
     */
    public Collection getGroupUsers(String groupId) throws SecurityException {
        // validate params
        if (groupId == null) throw new SecurityException();
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUsersByGroup(groupId);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public Collection getGroupUsers(String groupId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws SecurityException {
        // validate params
        if (groupId == null) throw new SecurityException();
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUsersByGroup(groupId, properties, start, maxResults, sort, descending);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Returns a collection of permissions associated with a Principal.
     *
     * @param properties hashmap containing the appropriate properties to scope the search.
     * @return a Collection of Permission objects.
     * @throws SecurityException if an error occurs.
     */
    public Collection getPrincipalPermissions(Map properties, int start, int maxResults) throws SecurityException {
        // validate params
        if (properties == null) throw new SecurityException();
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectPrincipalPermission(properties, start, maxResults);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Assigns groups to a user.
     * @param userId the unique identifier for the user.
     * @param groupIds an array of groupIds to assign to the user.
     * @throws SecurityException if an error occurs.
     */
    public void assignGroups(String userId, String[] groupIds) throws SecurityException {
        // validate params
        if (userId == null || groupIds == null) {
            throw new SecurityException();
        }

        // create relationships
        Collection userGroups = new ArrayList();
        for (int i = 0; i < groupIds.length; i++) {
            UserGroup ug = new UserGroup(userId, groupIds[i]);
            userGroups.add(ug);
        }

        // store relationships
        if (userGroups.size() > 0) {
            try {
                SecurityDao dao = (SecurityDao) getDao();
                UserGroup[] array = (UserGroup[]) userGroups.toArray(new UserGroup[0]);
                dao.storeUserGroups(array);
            }
            catch (Exception e) {
                throw new SecurityException(e.toString());
            }
        }
    }

    /**
     * Unassigns specified groups from a user.
     * @param userId the unique identifier for the user.
     * @param groupIds an array of groupIds to unassign from the user.
     * @throws SecurityException if an error occurs.
     */
    public void unassignGroups(String userId, String[] groupIds) throws SecurityException {
        // validate params
        if (userId == null || groupIds == null) throw new SecurityException();

        // create array of mappings
        Collection col = new ArrayList();
        for (int i = 0; i < groupIds.length; i++) {
            UserGroup ug = new UserGroup(userId, groupIds[i]);
            col.add(ug);
        }
        UserGroup[] ugArray = (UserGroup[]) col.toArray(new UserGroup[0]);

        // remove relationships
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.deleteUserGroups(ugArray);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Unassigns all groups from a user.
     * @param userId the unique identifier for the user.
     * @throws SecurityException if an error occurs.
     */
    public void unassignGroups(String userId) throws SecurityException {
        // validate params
        if (userId == null) throw new SecurityException();

        // remove relationships
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.deleteUserGroups(userId, null);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Assigns users to a group.
     * @param groupId the unique identifier for the group.
     * @param userIds an array of userIds to assign to the group.
     * @throws SecurityException if an error occurs.
     */
    public void assignUsers(String groupId, String[] userIds) throws SecurityException {
        // validate params
        if (groupId == null || userIds == null) throw new SecurityException();

        // create relationships
        Collection userGroups = new ArrayList();
        for (int i = 0; i < userIds.length; i++) {
            UserGroup ug = new UserGroup(userIds[i], groupId);
            userGroups.add(ug);
        }

        // store relationships
        if (userGroups.size() > 0) {
            try {
                SecurityDao dao = (SecurityDao) getDao();
                UserGroup[] array = (UserGroup[]) userGroups.toArray(new UserGroup[0]);
                dao.storeUserGroups(array);
            }
            catch (Exception e) {
                throw new SecurityException(e.toString());
            }
        }
    }

    /**
     * Unassigns specified users from a group.
     * @param groupId the unique identifier for the group.
     * @param userIds an array of userIds to unassign from the group.
     * @throws SecurityException if an error occurs.
     */
    public void unassignUsers(String groupId, String[] userIds) throws SecurityException {
        // validate params
        if (groupId == null || userIds == null) throw new SecurityException();

        // create array of mappings
        Collection col = new ArrayList();
        for (int i = 0; i < userIds.length; i++) {
            UserGroup ug = new UserGroup(userIds[i], groupId);
            col.add(ug);
        }
        UserGroup[] ugArray = (UserGroup[]) col.toArray(new UserGroup[0]);

        // remove relationships
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.deleteUserGroups(ugArray);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Unassigns all users from a group.
     * @param groupId the unique identifier for the group.
     * @throws SecurityException if an error occurs.
     */
    public void unassignUsers(String groupId) throws SecurityException {
        // validate params
        if (groupId == null) throw new SecurityException();

        // remove relationships
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.deleteUserGroups(null, groupId);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Assigns permissions to a principal.
     * @param principalId the unique identifier for the principal.
     * @param permissionIds an array of permissionIds to assign to the principal.
     * @throws SecurityException if an error occurs.
     */
    public void assignPermissions(String principalId, String moduleId, String[] permissionIds, String objectId) throws SecurityException {
        // validate params
        if (principalId == null || permissionIds == null) throw new SecurityException();

        // create relationships
        Collection principalPermissions = new ArrayList();
        for (int i = 0; i < permissionIds.length; i++) {
            PrincipalPermission ug = new PrincipalPermission(principalId, moduleId, permissionIds[i], objectId);
            principalPermissions.add(ug);
        }

        // store relationships
        if (principalPermissions.size() > 0) {
            try {
                SecurityDao dao = (SecurityDao) getDao();
                PrincipalPermission[] array = (PrincipalPermission[]) principalPermissions.toArray(new PrincipalPermission[0]);
                dao.storePrincipalPermissions(array);
            }
            catch (Exception e) {
                throw new SecurityException(e.toString());
            }
        }

        // flush permissions
        flushPermissionCache();
    }

    /**
     * Unassigns specified permissions from a principal.
     * @param principalId the unique identifier for the principal.
     * @param permissionIds an array of permissionIds to unassign from the principal.
     * @throws SecurityException if an error occurs.
     */
    public void unassignPermissions(String principalId, String moduleId, String[] permissionIds, String objectId) throws SecurityException {
        // validate params
        if (principalId == null || permissionIds == null) throw new SecurityException();

        // create array of mappings
        Collection col = new ArrayList();
        for (int i = 0; i < permissionIds.length; i++) {
            PrincipalPermission rp = new PrincipalPermission(principalId, moduleId, permissionIds[i], objectId);
            col.add(rp);
        }
        PrincipalPermission[] rpArray = (PrincipalPermission[]) col.toArray(new PrincipalPermission[0]);

        // remove relationships
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.deletePrincipalPermissions(rpArray);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Unassigns all permissions from a principal.
     * @param principalId the unique identifier for the principal.
     * @throws SecurityException if an error occurs.
     */
    public void unassignPermissions(String principalId) throws SecurityException {
        // validate params
        if (principalId == null) throw new SecurityException();

        // remove relationships
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.deletePrincipalPermissions(principalId, null, null, null);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Returns a Collection of Strings (principalIds) representing the userId and GroupsIds the
     * user belongs to.
     * @param userId the unique identifier for the user.
     * @throws SecurityException if an error occurs.
     */
    public Collection getUserPrincipalIds(String userId) throws SecurityException {
        // validate params
        if (userId == null) throw new SecurityException();

        try {
            // select principals for the user
            Collection principalList = new ArrayList();
            SecurityDao dao = (SecurityDao) getDao();
            principalList.add(userId);
            Collection groups = dao.selectGroupsByUser(userId);
            for (Iterator i = groups.iterator(); i.hasNext();) {
                Group g = (Group) i.next();
                principalList.add(g.getId());
            }
            return principalList;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Logs in a user. Throws a SecurityException if the authentication parameters are incorrect.
     * @param request the servlet request.
     * @param username the username of the user.
     * @param password the password of the user.
     * @throws SecurityException if an error occurs.
     */
    public void login(HttpServletRequest request, String username, String password) throws SecurityException {
        try {
            // encrypt password
            password = Encryption.encrypt(password);
            loginWithEncryptedPassword(request, username, password);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Logs out a user
     */
    protected void logout(HttpSession session) {
        try {
            /*
            session.setAttribute(SESSION_KEY_USER, null);
            session.setAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER, null);
            */
            Application application = Application.getInstance();
            SessionService sessionService = (SessionService) application.getService(SessionService.class);
            sessionService.removeSession(session);
        }
        catch (Exception e) {
            Log.getLog(SecurityService.class).error(e.getMessage(), e);
        }
        try {
            session.invalidate();
        }
        catch (Throwable t) {
        }
    }

    public void logout(User user, HttpSession session) {
        //Addition for integration with the PresenceService
        logout(user, session, true);
    }

    public void logout(User user, HttpSession session, boolean invalidate)
    {
        if (user != null) {
            PresenceService service = (PresenceService) Application.getInstance().getService(PresenceService.class);
            try {
                if (service != null) {
                    service.logout(user, session);
                }
            }
            catch (PresenceException e) {
                Log.getLog(SecurityService.class).error(e.getMessage(), e);
            }
        }
        if(invalidate)
            logout(session);
    }

    public void loginWithEncryptedPassword(HttpServletRequest request, String username, String password) throws SecurityException {
        try {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute(SESSION_KEY_USER);
            if (currentUser != null) {
                if (currentUser.getUsername().equals(username)) {
                    Log.getLog(SecurityService.class).debug("User Already Logged In. Unable To Continue");
                    return;
                }
            }

            // retrieve user
            SecurityDao dao = (SecurityDao) getDao();
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("username", username, DaoOperator.OPERATOR_AND));
            properties.addProperty(new OperatorEquals("password", password, DaoOperator.OPERATOR_AND));
            properties.addProperty(new OperatorEquals("active", DefaultPrincipal.STATE_ACTIVE, DaoOperator.OPERATOR_AND));
            Collection list = dao.selectUsers(properties, 0, 1, null, false);
            if (list.size() == 0) {
                throw new SecurityException("Invalid login for user " + username);
            }
            User user = (User) list.iterator().next();

            // invalidate if not anonymous user
            if (currentUser != null && !ANONYMOUS_USER_ID.equals(currentUser.getId())) {
                session.invalidate();
                session = request.getSession();
            }

            // save in session
            session.setAttribute(SESSION_KEY_USER, user);

            // update widget manager's user
            WidgetManager wm = (WidgetManager) session.getAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER);
            if (wm != null) {
                wm.setUser(user);
                session.setAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER, wm);
            }
            // inform PresenceService
            PresenceService service = (PresenceService) Application.getInstance().getService(PresenceService.class);
            if (service != null) {
                service.login(user, request);
            }
        }
        catch (SecurityException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Logs in a user. Throws a SecurityException if the authentication parameters are incorrect.
     * @deprecated Use loginWithEncryptedPassword(HttpServletRequest request, String username, String password) instead
     * @param session the HttpSession in question.
     * @param username the username of the user.
     * @param password the password of the user.
     * @throws SecurityException if an error occurs.
     */
    public void login(HttpSession session, String username, String password) throws SecurityException {
        try {
            // encrypt password
            password = Encryption.encrypt(password);
            loginWithEncryptedPassword(session, username, password);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * @deprecated Use loginWithEncryptedPassword(HttpServletRequest request, String username, String password) instead
     * @param session
     * @param username
     * @param password
     * @throws SecurityException
     */
    public void loginWithEncryptedPassword(HttpSession session, String username, String password) throws SecurityException {
        try {
            User currentUser = (User) session.getAttribute(SESSION_KEY_USER);
            if (currentUser != null) {
                if (currentUser.getUsername().equals(username)) {
                    Log.getLog(SecurityService.class).debug("User Already Logged In. Unable To Continue");
                    return;
                }
            }

            // retrieve user
            SecurityDao dao = (SecurityDao) getDao();
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("username", username, DaoOperator.OPERATOR_AND));
            properties.addProperty(new OperatorEquals("password", password, DaoOperator.OPERATOR_AND));
            properties.addProperty(new OperatorEquals("active", DefaultPrincipal.STATE_ACTIVE, DaoOperator.OPERATOR_AND));
            Collection list = dao.selectUsers(properties, 0, 1, null, false);
            if (list.size() == 0) {
                throw new SecurityException("Invalid login for user " + username);
            }
            User user = (User) list.iterator().next();

            // invalidate if not anonymous user
            if (currentUser != null && !ANONYMOUS_USER_ID.equals(currentUser.getId())) {
                session.invalidate();
            }

            // save in session
            session.setAttribute(SESSION_KEY_USER, user);

            // update widget manager's user
            WidgetManager wm = (WidgetManager) session.getAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER);
            if (wm != null) {
                wm.setUser(user);
                session.setAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER, wm);
            }
            // inform PresenceService
            PresenceService service = (PresenceService) Application.getInstance().getService(PresenceService.class);
            if (service != null) {
                service.login(user, session);
            }
        }
        catch (SecurityException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public void loginWithoutPassword(HttpSession session, String username) throws SecurityException {
        //TODO: (Michael) Make sure this works with PresenceService. Ensure logged in user cannot log in again
        try {
            // retrieve user
            Collection list = getUsersByUsername(username);
            if (list.size() == 0) {
                throw new SecurityException("Invalid login for user " + username);
            }
            User user = (User) list.iterator().next();

            // check user's active flag
            if (!user.isActive()) {
                throw new SecurityException("Attempted login for inactive user " + username);
            }

            // save in session
            session.setAttribute(SESSION_KEY_USER, user);

            // update widget manager's user
            WidgetManager wm = (WidgetManager) session.getAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER);
            if (wm != null) {
                wm.setUser(user);
                session.setAttribute(WidgetManager.SESSION_KEY_WIDGET_MANAGER, wm);
            }
        }
        catch (SecurityException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Retrieves and returns the current user in session
     * @param session the HttpSession in question.
     * @return the user object. null if the user is not currently logged in
     */
    private User getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY_USER);
        if (user == null) {
            // get Anonymous user
            try {
                user = getUser(ANONYMOUS_USER_ID);
                session.setAttribute(SESSION_KEY_USER, user);
            }
            catch (SecurityException e) {
                log.error("Anonymous User not available", e);
            }
        }
        return user;
    }

    /**
     * Retrieves the current user for the HTTP request.
     * @param request
     * @return
     */
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = getCurrentUser(session);
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        //Checking client cookie
        if (user == null || ANONYMOUS_USER_ID.equals(user.getId())) {
            try {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    String userId = null;
                    String password = null;
                    for (int i = 0; i < cookies.length; i++) {
                        Cookie cookie = cookies[i];
                        if (User.COOKIE_USER_KEY.equals(cookie.getName()))
                            userId = cookie.getValue();
                        else if (User.COOKIE_PASSWORD_KEY.equals(cookie.getName()))
                            password = cookie.getValue();
                    }
                    if (userId != null) {
                        user = service.getUser(userId);
                        if (user.isActive()) {
                            service.loginWithEncryptedPassword(request, user.getUsername(), password);
                        }
                    }
                }
            }
            catch (Exception e) {
                Log.getLog(SecurityService.class).error("Error retrieving login cookie", e);
            }
        }
        //END: Checking client cookie
        if (user != null) {
            user.setProperty(PROPERTY_KEY_HOST, request.getRemoteHost());
        }
        return user;
    }

    public int getGroupCount(DaoQuery properties) throws SecurityException {
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectGroupCount(properties);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public int getGroupUsersCount(String groupId, DaoQuery properties) throws SecurityException {
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUsersByGroupCount(groupId, properties);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public int getUserCount(DaoQuery properties) throws SecurityException {
        try {
            SecurityDao dao = (SecurityDao) getDao();
            return dao.selectUserCount(properties);
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    /**
     * Retrieves a Collection of Groups that are associated with a specified permission.
     * @param permissionId The permission
     * @param active Use Boolean.TRUE for active users and groups only, null for any
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return a Collection of User objects
     * @throws SecurityException
     */
    public Collection getGroupsByPermission(String permissionId, Boolean active, String sort, boolean desc, int start, int rows) throws SecurityException {
        // validate params
        if (permissionId == null)
            throw new SecurityException("Invalid call to getUsersByPermission");
        try {
            SecurityDao dao = (SecurityDao) getDao();
            Collection groups = dao.selectGroupsByPermission(permissionId, null, active, start, rows, sort, desc);
            return groups;
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
    }

    public void setUserStatus(String userId, String active) throws SecurityException {
        try {
            SecurityDao dao = (SecurityDao) getDao();
            dao.updateUserState(userId, active);
        }
        catch (DaoException e) {
            throw new SecurityException(e.getMessage());
        }
    }
    
    public User getUsersEmail(String email)throws DaoException{
    	
    	SecurityDao dao = (SecurityDao) getDao();
    	return dao.selectUsersByEmail(email);
    	
    }
}
