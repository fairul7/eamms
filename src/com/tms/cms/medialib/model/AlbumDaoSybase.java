package com.tms.cms.medialib.model;

import kacang.model.*;
import java.util.*;

public class AlbumDaoSybase extends AlbumDaoMsSql {
	// note: isContributor is the same as the one in AlbumDao except for "[role]"
    public boolean isContributor() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isContributor = false;
        
        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND [role] = 'contributor'", HashMap.class, new Object[] {id}, 0, -1);
            if(col.size() > 0) {
                isContributor = true;
            }
        }
        return isContributor;
    }
	
	// note: isContributor is the same as the one in AlbumDao except for "[role]"
    public boolean isContributor(String albumId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isContributor = false;
        
        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album " +
            		"WHERE principalId = ? " +
            		"AND album.id = ? " +
            		"AND [role] = 'contributor' " +
            		"AND permission.id = album.libraryId", HashMap.class, new Object[] {id, albumId}, 0, -1);
            if(col.size() > 0) {
                isContributor = true;
            }
        }
        
        return isContributor;
    } 
	
	// note: isManager is the same as the one in AlbumDao except for "[role]"
    public boolean isManager() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND [role] = 'manager'", HashMap.class, new Object[] {id}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    }
	
	// note: isManager is the same as the one in AlbumDao except for "[role]"
    public boolean isManager(String albumId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album " +
            		"WHERE principalId = ? " +
            		"AND album.id = ? " +
            		"AND [role] = 'manager' " +
            		"AND permission.id = album.libraryId", HashMap.class, new Object[] {id, albumId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    }
	
	// note: isAccessible is the same as the one in AlbumDao except for "[role]"
    public boolean isAccessible(String albumId) throws DaoException {
        boolean isAccessible = false;
        ArrayList principalIds = getUserPrincipalIds();
        
        for(int i=0; i<principalIds.size() && ! isAccessible; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id FROM " +
            		"cml_permission permission, cml_album album " +
            		"WHERE principalId = ? AND [role] = 'viewer' " +
            		"AND album.id = ? " +
            		"AND album.libraryId = permission.id", HashMap.class, new Object[] {id, albumId}, 0, -1);
            if(col.size() > 0) {
                isAccessible = true;
            }
        }
        
        return isAccessible;
    }
	
	// note: getAccessibleLibrary is the same as the one in AlbumDao except for "[role]"
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
        		"WHERE [role] = 'viewer' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
	
	// note: getEditableLibrary is the same as the one in AlbumDao except for "[role]"
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
        		"WHERE [role] = 'manager' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
	
	// note: getManagableLibrary is the same as the one in AlbumDao except for "[role]"
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
        		"WHERE [role] = 'manager' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            managableLibrary.add((String) map.get("id"));
        }
        
        return managableLibrary;
    }
}
