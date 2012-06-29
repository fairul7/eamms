/*
 * LibraryDao
 * Date Created: Jun 17, 2005
 * Author: Tien Soon, Law
 * Description: Library DAO class
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataSourceDao;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;
import kacang.util.Transaction;

import org.apache.commons.collections.SequencedHashMap;


public class LibraryDao extends DataSourceDao {
    private Log log = Log.getLog(getClass());
    private final String STORAGE_ROOT = "medialib";
    
    public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cml_library (" +
            		"id VARCHAR(35) NOT NULL, " +
            		"name VARCHAR(255), " +
            		"description TEXT, " +
            		"approvalNeeded CHAR(1) DEFAULT 'Y', " +
            		"maxWidth INT(10) DEFAULT 800, " +
            		"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
            		"lastUpdatedDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
            		"createdBy VARCHAR(255), " +
            		"PRIMARY KEY(id))", null);
            super.update("CREATE TABLE cml_permission (" +
            		"id VARCHAR(255), " +
            		"role VARCHAR(255), " +
            		"principalId VARCHAR(255))", null);
        }
        catch(DaoException error) {
            throw new DaoException(error);
        }
    }

    public void insert(LibraryObject library) throws DaoException {
        Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("INSERT INTO cml_library (" +
            		"id, name, description, approvalNeeded, maxWidth, dateCreated, lastUpdatedDate, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #approvalNeeded#, #maxWidth#, now(), now(), #createdBy#)", library);
            updateLibraryAccess(tx, library.getId(), library.getManagerGroup(), library.getContributorGroup(), library.getViewerGroup());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void update(LibraryObject library) throws DaoException {
        Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("UPDATE cml_library SET " +
            		"name=#name#, description=#description#, approvalNeeded=#approvalNeeded#, maxWidth=#maxWidth#, lastUpdatedDate=now() " +
            		"WHERE id=#id#", library);
            updateLibraryAccess(tx, library.getId(), library.getManagerGroup(), library.getContributorGroup(), library.getViewerGroup());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void delete(String id) throws DaoException {
        if(isManager(id) || isAccessibleToAll()) {
	        Object[] args = new String[] {
	            id
	        };
	        String albumId = "";
	        Collection col = super.select("SELECT id " +
	        		"FROM cml_album " +
	        		"WHERE libraryId = ?", HashMap.class, args, 0, -1);
	        Map map = null;
	        
	        StorageFile albumDir;
	        StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
	        
	        for(Iterator i=col.iterator(); i.hasNext();) {
	            map = (Map) i.next();
	            albumId = (String) map.get("id");
	            albumDir = new StorageFile("/" + STORAGE_ROOT + "/" + albumId + "/");
	            try {
	                storage.delete(albumDir);
	                
	                super.update("DELETE FROM cml_album WHERE id=?", new Object[] { albumId });
	                super.update("DELETE FROM cml_media WHERE albumId=?", new Object[] { albumId });
	            }
	            catch(Exception error) {
		            throw new DaoException("Exception caught while deleting album: " + error.toString());
		        }
	        }
	        
	        super.update("DELETE FROM cml_library WHERE id=?", args);
	        super.update("DELETE FROM cml_permission WHERE id=?", args);
        }
    }
    
    public LibraryObject select(String id) throws DaoException {
        LibraryObject library = null;
        Collection col = super.select("SELECT id, name, description, approvalNeeded, maxWidth, dateCreated, lastUpdatedDate, createdBy " +
        		"FROM cml_library " +
        		"WHERE id = ?", 
                LibraryObject.class, new String[] {id}, 0, 1);
        
        if (col.size() > 0) {
            library = (LibraryObject) col.iterator().next();
            library = setLibraryAccess(library);
        }
        
        return library;
    }
    
    public void toggleApprovalNeeded(String id, String value) throws DaoException {
        if(isManager(id) || isAccessibleToAll()) {
	        Object[] args = new String[] {
	            value, id
	        };
	        
	        super.update("UPDATE cml_library SET approvalNeeded=? " +
	        		"WHERE id=?", args);
        }
    }
    
    public Collection query(String name, boolean editableOnly, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection col = null;
        String condition = (name != null) ? "%" + name + "%" : "%";
        String orderBy = (sort != null) ? sort : "name";
        if (desc)
            orderBy += " DESC";
        Object[] args = { condition };
        
        // If user is granted with application level access to create library
        if(isAccessibleToAll()) {
            col =  super.select("SELECT library.id, library.name, library.description, approvalNeeded " +
            		"FROM cml_library library " +
            		"WHERE name LIKE ? ORDER BY " + orderBy, LibraryObject.class, args, start, rows);
            
            LibraryObject library = null;
            for(Iterator i=col.iterator(); i.hasNext();) {
                library = (LibraryObject) i.next();
                
                library.setTotalAlbum(String.valueOf(countTotalAlbum(library.getId())));
            }
        }
        // Else the library listing has to be filtered by accessible libraries
        else {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = new ArrayList();
            if(editableOnly) {
            	accessibleLibrary = getEditableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            String accessibleLibraryCondition = "";
            
            if(accessibleLibrary.size() > 0) {
                accessibleLibraryCondition = "(";
                for(int i=0; i<accessibleLibrary.size(); i++) {
                    if(i==0) {
                        accessibleLibraryCondition += "id = '" + (String) accessibleLibrary.get(i) + "'"; 
                    }
                    else {
                        accessibleLibraryCondition += " OR id = '" + (String) accessibleLibrary.get(i) + "'";
                    }
                }
                accessibleLibraryCondition += ")";
                
                col =  super.select("SELECT library.id, library.name, library.description, approvalNeeded " +
                        "FROM cml_library library " +
                        "WHERE name LIKE ? " +
                        "AND " + accessibleLibraryCondition + " ORDER BY " + orderBy, LibraryObject.class, args, start, rows);
                
                // Explicitly set some data field
                LibraryObject library = null;
                int maxDescriptionLength = 255;
                boolean accessibleToAll = isAccessibleToAll();
                for(Iterator i=col.iterator(); i.hasNext();) {
                    library = (LibraryObject) i.next();
                    
                    library.setTotalAlbum(String.valueOf(countTotalAlbum(library.getId())));
                    if(library.getDescription() != null) {
                    	if (library.getDescription().length() > maxDescriptionLength) {
                    		library.setDescription(library.getDescription().substring(0, maxDescriptionLength - 1) + "...");
                    	}
                    }
                    else {
                        library.setDescription("");
                    }
                    if(accessibleToAll) {
                        library.setManageable(true);
                    }
                    else {
                        if(isManager(library.getId())) {
                            library.setManageable(true);
                        }
                    }
                }
            }
        }
        
        return col;
    }
    
    public int countTotalAlbum(String libraryId) throws DaoException {
    	Collection totalAlbumCol = null;
    	int totalAlbum = 0;
    	
    	totalAlbumCol = super.select("SELECT count(id) AS total FROM cml_album WHERE libraryId = ?", HashMap.class, new Object[] {libraryId}, 0, 1);
    	if(totalAlbumCol != null) {
            HashMap map = (HashMap)totalAlbumCol.iterator().next();
            totalAlbum = Integer.parseInt(map.get("total").toString());
        }
    	
    	return totalAlbum;
    }
    
    public int count(String name, boolean editableOnly) throws DaoException {
        String condition = (name != null) ? "%" + name + "%" : "%";
        Object[] args = { condition };
        
        Collection list = null;
        
        // If user is granted with application level access to create library
        if(isAccessibleToAll()) {
            list =  super.select("SELECT COUNT(*) AS total FROM cml_library WHERE name LIKE ?", HashMap.class, args, 0, 1);
        }
        // Else the library listing has to be filtered by accessible libraries
        else {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = new ArrayList();
            if(editableOnly) {
            	accessibleLibrary = getEditableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            String accessibleLibraryCondition = "";
            
            if(accessibleLibrary.size() > 0) {
                accessibleLibraryCondition = "(";
                for(int i=0; i<accessibleLibrary.size(); i++) {
                    if(i==0) {
                        accessibleLibraryCondition += "id = '" + (String) accessibleLibrary.get(i) + "'"; 
                    }
                    else {
                        accessibleLibraryCondition += " OR id = '" + (String) accessibleLibrary.get(i) + "'";
                    }
                }
                accessibleLibraryCondition += ")";
                
                list =  super.select("SELECT COUNT(*) AS total FROM cml_library WHERE name LIKE ? AND " + accessibleLibraryCondition, HashMap.class, args, 0, 1);
            }
        }
        
        int totalRecord = 0;
        if(list != null) {
            HashMap map = (HashMap)list.iterator().next();
            totalRecord = Integer.parseInt(map.get("total").toString());
        }
        
        return totalRecord;
    }
    
    // Check the highest role a user is holding in a specific library
    // null if no accessible role at all
    public String getHighestRole(String libraryId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        boolean isContributor = false;
        boolean isViewer = false;
        String role = null;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String principalId = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND role = 'manager' AND id = ?", HashMap.class, new Object[] {principalId, libraryId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        
        if(! isManager) {
	        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
	            String id = (String) principalIds.get(i);
	            
	            Collection col = super.select("SELECT id FROM cml_permission " +
	            		"WHERE principalId = ? AND role = 'contributor' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
	            if(col.size() > 0) {
	                isContributor = true;
	            }
	        }
        }
        
        if(! isManager && ! isContributor) {
            for(int i=0; i<principalIds.size() && ! isViewer; i++) {
	            String id = (String) principalIds.get(i);
	            
	            Collection col = super.select("SELECT id FROM cml_permission " +
	            		"WHERE principalId = ? AND role = 'viewer' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
	            if(col.size() > 0) {
	                isViewer = true;
	            }
	        }
        }
        
        if(isManager) {
            role = "manager";
        }
        else {
            if(isContributor) {
                role = "contributor";
            }
            else if(isViewer){
                role = "viewer";
            }
        }
        
        return role;
    }
    
    // Check if the user is holding role of Manager for any library
    public boolean isManager() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND role = 'manager'", HashMap.class, new Object[] {id}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    }
    
    //  Check if the user is holding role of Manager for a specific library
    public boolean isManager(String libraryId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND role = 'manager' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    }
    
    public boolean isAccessible(String libraryId) throws DaoException {
        boolean isAccessible = isAccessibleToAll();
        ArrayList principalIds = getUserPrincipalIds();
        
        for(int i=0; i<principalIds.size() && ! isAccessible; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND role = 'viewer' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
            if(col.size() > 0) {
                isAccessible = true;
            }
        }
        
        return isAccessible;
    }
    
    protected void updateLibraryAccess(Transaction tx, String libraryId, Map managerGroup, Map contributorGroup, Map viewerGroup) throws SQLException {
        // delete existing entries
        tx.update("DELETE FROM cml_permission WHERE id=?", new Object[] { libraryId });
        
        String sqlInsertPermission = "INSERT INTO cml_permission (id, role, principalId) VALUES (?, ?, ?)";

        // insert manager group
        if (managerGroup != null)
        {
            for (Iterator i=managerGroup.keySet().iterator(); i.hasNext();)
            {
                String principalId = (String)i.next();
                tx.update(sqlInsertPermission, new Object[] { libraryId, "manager", principalId });
                tx.update(sqlInsertPermission, new Object[] { libraryId, "contributor", principalId });
                tx.update(sqlInsertPermission, new Object[] { libraryId, "viewer", principalId });
            }
        }
        
        // insert contributor group
        if (contributorGroup != null)
        {
            for (Iterator i=contributorGroup.keySet().iterator(); i.hasNext();)
            {
                String principalId = (String)i.next();
                tx.update(sqlInsertPermission, new Object[] { libraryId, "contributor", principalId });
                tx.update(sqlInsertPermission, new Object[] { libraryId, "viewer", principalId });
            }
        }
        
        // insert viewer group
        if (viewerGroup != null)
        {
            for (Iterator i=viewerGroup.keySet().iterator(); i.hasNext();)
            {
                String principalId = (String)i.next();
                tx.update(sqlInsertPermission, new Object[] { libraryId, "viewer", principalId });
            }
        }
    }
    
    protected LibraryObject setLibraryAccess(LibraryObject library) throws DaoException {        
        String sqlManager = "SELECT principalId from cml_permission WHERE id = ? AND role = 'manager'";
        String sqlContributor = "SELECT principalId from cml_permission WHERE id = ? AND role = 'contributor'";
        String sqlViewer = "SELECT principalId from cml_permission WHERE id = ? AND role = 'viewer'";
        Map managerGroupMap = new SequencedHashMap();
        Map contributorGroupMap = new SequencedHashMap();
        Map viewerGroupMap = new SequencedHashMap();
        Collection groupIdList;
        Collection groupList;
        
        try
        {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);

            // get manager groups
            groupIdList = new ArrayList();
            Collection managerGroupList = super.select(sqlManager, HashMap.class, new Object[] {library.getId()}, 0, -1);
            for(Iterator i=managerGroupList.iterator(); i.hasNext();) {
                Map row = (Map) i.next();
                groupIdList.add(row.get("principalId"));
            }
            if(groupIdList.size() > 0) {
                groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                for (Iterator j=groupList.iterator(); j.hasNext();)
                {
                    Group group = (Group) j.next();
                    managerGroupMap.put(group.getId(), group.getName());
                }
            }
            library.setManagerGroup(managerGroupMap);
            
            // get contributor groups
            groupIdList = new ArrayList();
            Collection contributorGroupList = super.select(sqlContributor, HashMap.class, new Object[] {library.getId()}, 0, -1);
            for(Iterator i=contributorGroupList.iterator(); i.hasNext();) {
                Map row = (Map) i.next();
                groupIdList.add(row.get("principalId"));
            }
            if(groupIdList.size() > 0) {
                groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                for (Iterator j=groupList.iterator(); j.hasNext();)
                {
                    Group group = (Group) j.next();
                    contributorGroupMap.put(group.getId(), group.getName());
                }
            }
            library.setContributorGroup(contributorGroupMap);
            
            // get viewer groups
            groupIdList = new ArrayList();
            Collection viewerGroupList = super.select(sqlViewer, HashMap.class, new Object[] {library.getId()}, 0, -1);
            for(Iterator i=viewerGroupList.iterator(); i.hasNext();) {
                Map row = (Map) i.next();
                groupIdList.add(row.get("principalId"));
            }
            if(groupIdList.size() > 0) {
                groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                for (Iterator j=groupList.iterator(); j.hasNext();)
                {
                    Group group = (Group) j.next();
                    viewerGroupMap.put(group.getId(), group.getName());
                }
            }
            library.setViewerGroup(viewerGroupMap);
        }
        catch (DaoException e)
        {
            throw e;
        }
        catch (SecurityException e)
        {
            throw new DaoException("Error getting library groups " + e.toString());
        }
        
        return library;
    }
    
    protected ArrayList getUserPrincipalIds() throws DaoException {
        User user = Application.getInstance().getCurrentUser();
        // getWidgetManager().getUser()
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        String userId = user.getId();
        ArrayList principalIds = new ArrayList();
        
        Collection groups;
        try {
            groups = ss.getUserGroups(userId);
        }
        catch(SecurityException error) {
            throw new DaoException("Exception caught while retrieving user groups: " + error.toString());
        }
        for (Iterator i = groups.iterator(); i.hasNext();) {
            Group g = (Group) i.next();
            if (g.isActive()) {
                principalIds.add((String) g.getId());
            }
        }
        
        return principalIds;
    }
    
    protected ArrayList getAccessibleLibrary(ArrayList principalIds) throws DaoException {
        ArrayList accessibleLibrary = new ArrayList();
        String condition = "(";
        for(int i=0; i<principalIds.size(); i++) {
            if(i==0) {
                condition += "principalId = '" + (String) principalIds.get(i) + "'"; 
            }
            else {
                condition += " OR principalId = '" + (String) principalIds.get(i) + "'";
            }
        }
        condition += ")";
        Collection col = super.select("SELECT distinct id FROM cml_permission " +
        		"WHERE role = 'viewer' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
    
    protected ArrayList getEditableLibrary(ArrayList principalIds) throws DaoException {
        ArrayList accessibleLibrary = new ArrayList();
        String condition = "(";
        for(int i=0; i<principalIds.size(); i++) {
            if(i==0) {
                condition += "principalId = '" + (String) principalIds.get(i) + "'"; 
            }
            else {
                condition += " OR principalId = '" + (String) principalIds.get(i) + "'";
            }
        }
        condition += ")";
        Collection col = super.select("SELECT distinct id FROM cml_permission " +
        		"WHERE role = 'manager' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
    
    protected boolean isAccessibleToAll() {
        Application app = Application.getInstance();
        SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
        User user = Application.getInstance().getCurrentUser();
        boolean isAccessibleToAll = false;
    	try {
    	    isAccessibleToAll = securityService.hasPermission(user.getId(), LibraryModule.PERMISSION_CREATE_LIBRARY, null, null);
    	}
    	catch(SecurityException error) {
    	    log.error("Error retriving user id: " + error, error);
    	}
    
    	return isAccessibleToAll;
    }
}
