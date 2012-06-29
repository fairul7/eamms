package com.tms.cms.medialib.model;

import kacang.model.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class MediaDaoSybase extends MediaDaoMsSql {
	// note: isManager is the same as the one in MediaDao except for "[role]"
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
	
	// note: isManager is the same as the one in MediaDao except for "[role]"
    public boolean isManager(String mediaId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album, cml_media media " +
            		"WHERE principalId = ? " +
            		"AND media.id = ? " +
            		"AND [role] = 'manager' " +
            		"AND media.albumId = album.id " +
            		"AND album.libraryId = permission.id", HashMap.class, new Object[] {id, mediaId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    } 
	
	// note: isAlbumManager is the same as the one in MediaDao except for "[role]"
    public boolean isAlbumManager(String albumId) throws DaoException {
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
	
	// note: isContributor is the same as the one in MediaDao except for "[role]"
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
	
	// note: isContributor is the same as the one in MediaDao except for "[role]"
    public boolean isContributor(String mediaId, HttpServletRequest request) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds(request);
        boolean isContributor = false;
        
        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
            String principalId = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album, cml_media media " +
            		"WHERE principalId = ? " +
            		"AND media.id = ? " +
            		"AND [role] = 'contributor' " +
            		"AND media.albumId = album.id " +
            		"AND album.libraryId = permission.id", HashMap.class, new Object[] {principalId, mediaId}, 0, -1);
            
            if(col.size() > 0) {
                isContributor = true;
            }
        }
        return isContributor;
    }
	
	// note: isAlbumContributor is the same as the one in MediaDao except for "[role]"
    public boolean isAlbumContributor(String albumId) throws DaoException {
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
	
	// note: isAccessible is the same as the one in MediaDao except for "[role]"
    public boolean isAccessible(String mediaId, HttpServletRequest request) throws DaoException {
        String condition = "";
        if(! isContributor(mediaId, request)) {
            condition = " AND media.isApproved = 'Y'";
        }
        
        boolean isAccessible = false;
        ArrayList principalIds = getUserPrincipalIds(request);
        
        for(int i=0; i<principalIds.size() && ! isAccessible; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id FROM " +
            		"cml_permission permission, cml_album album, cml_media media " +
            		"WHERE principalId = ? AND [role] = 'viewer' " +
            		"AND media.id = ? " +
            		"AND media.albumId = album.id " +
            		"AND album.libraryId = permission.id" + condition, HashMap.class, new Object[] {id, mediaId}, 0, -1);
            if(col.size() > 0) {
                isAccessible = true;
            }
        }
        
        return isAccessible;
    }
}
