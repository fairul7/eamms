package com.tms.cms.medialib.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

public class MediaDaoMsSql extends MediaDao{
	
	public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cml_media (" +
            		"id VARCHAR(35) NOT NULL, " +
            		"name VARCHAR(255), " +
            		"description TEXT, " +
            		"fileName VARCHAR(255), " +
            		"fileSize INTEGER DEFAULT 0, " +
            		"mediaType VARCHAR(255), " +
            		"isApproved VARCHAR(1) DEFAULT 'N', " +
            		"imageWidth INTEGER DEFAULT 0, " +
            		"imageHeight INTEGER DEFAULT 0, " +
            		"thumbnailWidth INTEGER DEFAULT 0, " +
            		"thumbnailHeight INTEGER DEFAULT 0, " +
            		"albumId VARCHAR(35), " +
            		"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
            		"createdBy VARCHAR(255), " +
            		"PRIMARY KEY(id))", null);
            
            super.update("CREATE TABLE cml_media_stat (" +
            		"id VARCHAR(35) NOT NULL, " +
            		"ip VARCHAR(15), " +
            		"actionType VARCHAR(35), " +
            		"mediaId VARCHAR(35), " +
            		"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
            		"createdBy VARCHAR(255), " +
            		"PRIMARY KEY(id))", null);
        }
        catch(DaoException error) {
            throw new DaoException(error);
        }
    }
	
	public Collection queryOwnMedia(String name, boolean editableOnly, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException {
    	Collection col = null;
    	
    	if(isManager()) {
        	col = query(name, editableOnly, searchCol, sort, "", desc, start, rows);
        }
    	else {
	    	String condition = (name != null) ? "%" + name + "%" : "%";
	        String orderBy = (sort != null) ? (sort.equals("name")?"media.name":sort) : "media.name";
	        String columnName = "media.name";
	        
	        if(! "".equals(searchCol)) {
	            if("library".equalsIgnoreCase(searchCol)) {
	                columnName = "library.name";
	            }
	            else if("album".equals(searchCol)) {
	                columnName = "album.name";
	            }
	        }
	        
	        if (desc)
	            orderBy += " DESC";
	        Object[] args = { condition, Application.getInstance().getCurrentUser().getId() };
	        
	        col = super.select("SELECT media.id, media.name, mediaType, media.isApproved, fileSize, " +
	                "media.albumId, library.id libraryId, " +
	                "album.name albumName, library.name libraryName, " +
	                "media.dateCreated " +
	                "FROM cml_media media, cml_album album, cml_library library " +
	                "WHERE media.albumId = album.id AND " +
	                "album.libraryId = library.id AND " +
	                columnName + " LIKE ? " +
	                "AND media.createdBy = ? " +
	                "ORDER BY " + orderBy, MediaObject.class, args, start, rows);
	        
	        MediaObject media = null;
	        
	        // Update Manageability and Deletability attributes of each media
	        for(Iterator i=col.iterator(); i.hasNext();) {
	            media = (MediaObject) i.next();
	            media.setFileSizeStr(formatByte(media.getFileSize()));
	            if(isManager(media.getId())) {
	                media.setManageable(true);
	            }
	            if(isContributor(media.getId())) {
	                media.setDeletable(true);
	            }
	        }
    	}
    	
        return col;
    }
	
	public Collection query(String name, boolean editableOnly, String searchCol, String sort, String albumId, boolean desc, int start, int rows) throws DaoException {
        String condition = (name != null) ? "%" + name + "%" : "%";
        String orderBy = (sort != null) ? (sort.equals("name")?"media.name":sort) : "media.name";
        String columnName = "media.name";
        Collection col = null;
        
        if(! "".equals(searchCol)) {
            if("library".equalsIgnoreCase(searchCol)) {
                columnName = "library.name";
            }
            else if("album".equals(searchCol)) {
                columnName = "album.name";
            }
        }
        
        if (desc)
            orderBy += " DESC";
        Object[] args = { condition };

        String accessibleLibraryCondition = "";
        if("".equals(albumId)) {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = getAccessibleLibrary(principalIds);
            if(editableOnly) {
            	accessibleLibrary = getContributableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            
            if(accessibleLibrary.size() > 0) {
                accessibleLibraryCondition += "(";
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
            accessibleLibraryCondition = "album.id = '" + albumId + "'";
        }
        
        if(! "".equals(accessibleLibraryCondition)) {
            col = super.select("SELECT media.id, media.name, mediaType, media.isApproved, fileSize, " +
                    "media.albumId, library.id libraryId, " +
                    "album.name albumName, library.name libraryName, " +
                    "media.dateCreated " +
                    "FROM cml_media media, cml_album album, cml_library library " +
                    "WHERE media.albumId = album.id AND " +
                    "album.libraryId = library.id AND " +
                    columnName + " LIKE ? AND " 
                    + accessibleLibraryCondition + " ORDER BY " + orderBy, MediaObject.class, args, 0, -1);
            
            MediaObject media = null;
            int j=0;
            int totalAdded=0;
            
            // Filter the collection, by whichever items the user is not authorized to view
            for(Iterator i=col.iterator(); i.hasNext();) {
                media = (MediaObject) i.next();
                media.setMediaType(media.getMediaType().substring(0, media.getMediaType().indexOf("/")));
                if(! isAlbumContributor(media.getAlbumId()) && ! isAlbumManager(media.getAlbumId())) {
                    if(media.getIsApproved().equalsIgnoreCase("N")) {
                        i.remove();
                    }
                }
            }
            // Filter the collection again, for paging purposes
            for(Iterator i=col.iterator(); i.hasNext();j++) {
                media = (MediaObject) i.next();
                if(j >= start && totalAdded < rows) {
                    media.setFileSizeStr(formatByte(media.getFileSize()));
                    if(isManager(media.getId())) {
                        media.setManageable(true);
                    }
                    if(isContributor(media.getId())) {
                        media.setDeletable(true);
                    }
                    totalAdded++;
                }
                else {
                    i.remove();
                }
            }
            
            /*
             * This sql statement will return whichever media files accessible by the user, 
             * with at least the role of viewer. 
             * However, due to the following constraints, this statement is deprecated.
             *      - user with role of contributor and above can view all accessible media files
             *      - user with role of only viewer, can't view non-approved media files
             * 
             * select distinct media.id, media.name, media.mediaType, 
             * album.name albumName, 
             * library.name libraryName  
             * from cml_library library, cml_album album, cml_media media, 
             * cml_permission permission, 
             * security_group secGroup, 
             * security_user_group secUserGroup, 
             * security_user secUser 
             * where media.albumId = album.id 
             * and album.libraryId = library.id 
             * and library.id = permission.id 
             * and permission.role = 'viewer' 
             * and permission.principalId = secGroup.id 
             * and secUserGroup.groupId = secGroup.id 
             * and secUserGroup.userId = secUser.id 
             * and secUser.id = ?
             */
        }
        
        return col;
    }
	
	public void insert(MediaObject media) throws DaoException {
        super.update("INSERT INTO cml_media (" +
            		"id, name, description, fileName, fileSize, mediaType, isApproved, " +
            		"imageWidth, imageHeight, thumbnailWidth, thumbnailHeight, albumId, " +
            		"dateCreated, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #fileName#, #fileSize#, #mediaType#, #isApproved#, " +
            		"#imageWidth#, #imageHeight#, #thumbnailWidth#, #thumbnailHeight#, #albumId#, " +
            		"getDate(), #createdBy#)", media);
    }
	
	public void insertMediaStat(MediaStatObject mediaStat) throws DaoException {
        super.update("INSERT INTO cml_media_stat (" +
        		"id, ip, mediaId, actionType, dateCreated, createdBy) VALUES " +
        		"(#id#, #ip#, #mediaId#, #actionType#, getDate(), #createdBy#)", mediaStat);
    }
	

    public Collection getManagerUserIds(String libraryId) throws DaoException {
        Collection col = super.select("SELECT distinct userGroup.userId userId from cml_permission permission, security_user_group userGroup " +
        		"WHERE permission.id = ? " +
        		"AND permission.[role] = 'manager' " +
        		"AND permission.principalId = userGroup.groupId", HashMap.class, new Object[] {libraryId}, 0, -1);
        // note: role was changed to [role] to make it compatible with Sybase
        return col;
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
        		"WHERE [role] = 'viewer' AND " + condition, HashMap.class, null, 0, -1);
		// note: role was changed to [role] to make it compatible with Sybase
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
    
    protected ArrayList getContributableLibrary(ArrayList principalIds) throws DaoException {
        ArrayList contributableLibrary = new ArrayList();
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
        		"WHERE [role] = 'contributor' AND " + condition, HashMap.class, null, 0, -1);
		// note: role was changed to [role] to make it compatible with Sybase
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            contributableLibrary.add((String) map.get("id"));
        }
        
        return contributableLibrary;
    }
    
    protected ArrayList getUserPrincipalIds() throws DaoException {
        return getUserPrincipalIds(null);
    }
    
    protected ArrayList getUserPrincipalIds(HttpServletRequest request) throws DaoException {
        ArrayList principalIds = new ArrayList();
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        String userId;
        if(request != null) {
            User user = ss.getCurrentUser(request);
            userId = user.getId();
        }
        else {
            User user = Application.getInstance().getCurrentUser();
            userId = user.getId();
        }
        
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
    
	private String formatByte(long fileSize) {
        String result = "";
        
        if (fileSize >= 1048576) {
            double db = (((double)fileSize)/1048576);
            Math.pow(2, 20);
            result = new DecimalFormat("0.00").format(db) + "MB";
        }
        else if (fileSize >= 1024 && fileSize < 1048576) {
            double db = (((double)fileSize)/1024);
            Math.pow(2, 10);
            result = new DecimalFormat("0.00").format(db) + "KB";
        }
        else {
            result = fileSize + "B";
        }
        
        return result;
    }
	
}
