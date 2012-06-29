/*
 * AlbumDao
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: Album DAO class
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;


public class AlbumDao extends DataSourceDao {
    private final String STORAGE_ROOT = "medialib";
    
    public void init() throws DaoException {
        super.update("CREATE TABLE cml_album (" +
        		"id VARCHAR(35) NOT NULL, " +
        		"name VARCHAR(255), " +
        		"description TEXT, " +
        		"eventDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
        		"featured CHAR(1) DEFAULT '0', " +
        		"libraryId VARCHAR(35), " +
        		"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
        		"lastUpdatedDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
        		"createdBy VARCHAR(255), " +
        		"PRIMARY KEY(id))", null);
    }
    
    public void insert(AlbumObject album) throws DaoException {
        /*
        String id = UuidGenerator.getInstance().getUuid();
        album.setId(id);
        */
        super.update("INSERT INTO cml_album (" +
            		"id, name, description, eventDate, featured, libraryId, dateCreated, lastUpdatedDate, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #eventDate#, #featured#, #libraryId#, now(), now(), #createdBy#)", album);
    }
    
    public void update(AlbumObject album) throws DaoException {
        super.update("UPDATE cml_album SET " +
        		"name=#name#, description=#description#, eventDate=#eventDate#, featured=#featured#, libraryId=#libraryId#, lastUpdatedDate=now() " +
        		"WHERE id=#id#", album);
    }

    public void delete(String id) throws DaoException {
        if(isManager(id)) {
	        Object[] args = new String[] {
	            id
	        };
	        
	        StorageFile albumDir = new StorageFile("/" + STORAGE_ROOT + "/" + id + "/");
	        StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
	        
	        try {
	            storage.delete(albumDir);
	            
	            super.update("DELETE FROM cml_album WHERE id=?", args);
	            super.update("DELETE FROM cml_media WHERE albumId=?", args);
	        }
	        catch(Exception error) {
	            throw new DaoException("Exception caught while deleting album: " + error.toString());
	        }
        }
    }
    
    public AlbumObject select(String id) throws DaoException {
        Collection col = super.select("SELECT album.id, album.name, album.description, eventDate, featured, " +
        		"album.libraryId, library.name libraryName, album.dateCreated, album.lastUpdatedDate, album.createdBy " +
        		"FROM cml_album album, cml_library library " +
        		"WHERE album.id = ? " +
        		"AND album.libraryId = library.id", 
                AlbumObject.class, new String[] {id}, 0, 1);
        
        AlbumObject album = null;
        if (col.size() > 0) {
            album = (AlbumObject) col.iterator().next();
        }
        
        return album;
    }
    
    public void setFeaturedAlbum(String id) throws DaoException {
        if(isManager(id)) {
	        Object[] args = new String[] {
	            id
	        };
	        
	        super.update("UPDATE cml_album SET featured='1' " +
	        		"WHERE id=?", args);
        }
    }
    
    public AlbumObject getFeaturedAlbum() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList accessibleLibrary = getAccessibleLibrary(principalIds);
        AlbumObject album = null;
        String condition = "";
        
        if(accessibleLibrary.size() > 0) {
            condition += "(";
            for(int i=0; i<accessibleLibrary.size(); i++) {
                if(i==0) {
                    condition += "libraryId = '" + (String) accessibleLibrary.get(i) + "'"; 
                }
                else {
                    condition += " OR libraryId = '" + (String) accessibleLibrary.get(i) + "'";
                }
            }
            condition += ")";
            
            Collection col = super.select("SELECT distinct album.id " +
                    "FROM cml_album album, cml_media media " +
                    "WHERE album.featured = '1' " +
                    "AND media.albumId = album.id " +
                    "AND media.isApproved = 'Y' " +
                    "AND " + condition, HashMap.class, null, 0, -1);
            if(col != null && col.size() > 0) {
                Map map;
                ArrayList id = new ArrayList();
                for(Iterator i=col.iterator(); i.hasNext();) {
                    map = (Map) i.next();
                    id.add((String) map.get("id"));
                }
                Random rand = new Random();
                String selectedAlbumId = (String) id.get(rand.nextInt(id.size()));
                
                Collection selectedAlbum = super.select("SELECT album.id, album.name, album.description, album.eventDate, " +
                        "library.id libraryId, library.name libraryName " +
                        "FROM cml_album album, cml_library library " +
                        "WHERE album.id = ? " +
                        "AND album.libraryId = library.id", AlbumObject.class, new Object[] {selectedAlbumId}, 0, 1);
                if(selectedAlbum.size() > 0) {
                    album = (AlbumObject) selectedAlbum.iterator().next();
                }
            }
        }
        
        return album;
    }
    
    public void setNonfeaturedAlbum(String id) throws DaoException {
        if(isManager(id)) {
	        Object[] args = new String[] {
	            id
	        };
	        
	        super.update("UPDATE cml_album SET featured='0' " +
	        		"WHERE id=?", args);
        }
    }
    
    public ArrayList getLibrarySelectList() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList mamagableLibrary = getManagableLibrary(principalIds);
        ArrayList alist = new ArrayList();
        
        if(mamagableLibrary.size() > 0) {
            String managableLibraryCondition = "(";
            for(int i=0; i<mamagableLibrary.size(); i++) {
                if(i==0) {
                    managableLibraryCondition += "id = '" + (String) mamagableLibrary.get(i) + "'"; 
                }
                else {
                    managableLibraryCondition += " OR id = '" + (String) mamagableLibrary.get(i) + "'";
                }
            }
            managableLibraryCondition += ")";
            
            Collection col = super.select("SELECT id, name FROM cml_library " +
                    "WHERE " + managableLibraryCondition + " ORDER BY name", 
                    SelectListObject.class, null, 0, -1);
            
            for(Iterator i=col.iterator(); i.hasNext(); ) {
                alist.add((SelectListObject) i.next());
            }
        }
        
        return alist;
    }
    
    public Collection query(String name, boolean editableOnly, String searchCol, String sort, String libraryId, boolean desc, int start, int rows) throws DaoException {
        String accessibleLibraryCondition = "";
        Collection col = null;
        
        if("".equals(libraryId)) {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = new ArrayList();
            if(editableOnly) {
            	accessibleLibrary = getEditableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            
            if(accessibleLibrary.size() > 0) {
    	        accessibleLibraryCondition = "(";
    	        for(int i=0; i<accessibleLibrary.size(); i++) {
    	            if(i==0) {
    	                accessibleLibraryCondition += "library.id = '" + (String) accessibleLibrary.get(i) + "'"; 
    	            }
    	            else {
    	                accessibleLibraryCondition += " OR library.id = '" + (String) accessibleLibrary.get(i) + "'";
    	            }
    	        }
    	        accessibleLibraryCondition += ")";
            }
        }
        else {
            accessibleLibraryCondition = "library.id = '" + libraryId + "'"; 
        }
        
        // If the user is accessible to at least 1 library
        if(! "".equals(accessibleLibraryCondition)) {
            String condition = (name != null) ? "%" + name + "%" : "%";
            String orderBy = (sort != null) ? sort : "album.name";
            String columnName = "album.name";
            
            if(! "".equals(searchCol)) {
                if("library".equalsIgnoreCase(searchCol)) {
                    columnName = "library.name";
                }
            }
            
            if (desc)
                orderBy += " DESC";
            Object[] args = { condition };
            
            col =  super.select("SELECT album.id id, album.name name, album.description description, eventDate, featured, " +
            		"libraryId, library.name libraryName " +
            		"FROM cml_album album, cml_library library " +
            		"WHERE album.libraryId = library.id AND " +
            		columnName + " LIKE ? AND " + accessibleLibraryCondition + " ORDER BY " + orderBy, AlbumObject.class, args, start, rows);
            AlbumObject album = null;
            int maxDescriptionLength = 255;
            
            for(Iterator i=col.iterator(); i.hasNext();) {
                album = (AlbumObject) i.next();
                if(album.getDescription() != null) {
                	if (album.getDescription().length() > maxDescriptionLength) {
                		album.setDescription(album.getDescription().substring(0, maxDescriptionLength - 1) + "...");
                	}
                }
                else {
                    album.setDescription("");
                }
                
                if(isManager(album.getId())) {
                    album.setManageable(true);
                }
            }
        }
        
        return col;
    }
    
    public int count(String name, boolean editableOnly, String searchCol, String libraryId) throws DaoException {
        String accessibleLibraryCondition = "";
        Collection list = null;
        
        if("".equals(libraryId)) {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = new ArrayList();
            if(editableOnly) {
            	accessibleLibrary = getEditableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            
            if(accessibleLibrary.size() > 0) {
                accessibleLibraryCondition = "(";
                for(int i=0; i<accessibleLibrary.size(); i++) {
                    if(i==0) {
                        accessibleLibraryCondition += "library.id = '" + (String) accessibleLibrary.get(i) + "'"; 
                    }
                    else {
                        accessibleLibraryCondition += " OR library.id = '" + (String) accessibleLibrary.get(i) + "'";
                    }
                }
                accessibleLibraryCondition += ")";
            }
        }
        else {
            accessibleLibraryCondition = "library.id = '" + libraryId + "'"; 
        }
        
        // If the user is accessible to at least 1 library
        if(! "".equals(accessibleLibraryCondition)) {
            String condition = (name != null) ? "%" + name + "%" : "%";
            String columnName = "album.name";
            
            if(! "".equals(searchCol)) {
                if("library".equalsIgnoreCase(searchCol)) {
                    columnName = "library.name";
                }
            }
            Object[] args = { condition };
    
            list = super.select("SELECT COUNT(*) AS total " +
                    "FROM cml_album album, cml_library library " +
                    "WHERE album.libraryId = library.id AND " +
                    columnName + " LIKE ? AND " + accessibleLibraryCondition, HashMap.class, args, 0, 1);
        }
        
        int totalRecord = 0;
        if(list != null) {
            HashMap map = (HashMap)list.iterator().next();
            totalRecord = Integer.parseInt(map.get("total").toString());
        }
        
        return totalRecord;
    }
    
    public boolean isContributor() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isContributor = false;
        
        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND role = 'contributor'", HashMap.class, new Object[] {id}, 0, -1);
            if(col.size() > 0) {
                isContributor = true;
            }
        }
        return isContributor;
    }
    
    public boolean isContributor(String albumId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isContributor = false;
        
        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album " +
            		"WHERE principalId = ? " +
            		"AND album.id = ? " +
            		"AND role = 'contributor' " +
            		"AND permission.id = album.libraryId", HashMap.class, new Object[] {id, albumId}, 0, -1);
            if(col.size() > 0) {
                isContributor = true;
            }
        }
        
        return isContributor;
    } 
    
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
    
    public boolean isManager(String albumId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album " +
            		"WHERE principalId = ? " +
            		"AND album.id = ? " +
            		"AND role = 'manager' " +
            		"AND permission.id = album.libraryId", HashMap.class, new Object[] {id, albumId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    } 
    
    public boolean isAccessible(String albumId) throws DaoException {
        boolean isAccessible = false;
        ArrayList principalIds = getUserPrincipalIds();
        
        for(int i=0; i<principalIds.size() && ! isAccessible; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id FROM " +
            		"cml_permission permission, cml_album album " +
            		"WHERE principalId = ? AND role = 'viewer' " +
            		"AND album.id = ? " +
            		"AND album.libraryId = permission.id", HashMap.class, new Object[] {id, albumId}, 0, -1);
            if(col.size() > 0) {
                isAccessible = true;
            }
        }
        
        return isAccessible;
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
    
    protected ArrayList getManagableLibrary(ArrayList principalIds) throws DaoException {
        ArrayList managableLibrary = new ArrayList();
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
            managableLibrary.add((String) map.get("id"));
        }
        
        return managableLibrary;
    }
}