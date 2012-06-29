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

public class MediaDaoDB2 extends MediaDao{
	
	public Collection queryOwnMedia(String name, boolean editableOnly, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException {
    	Collection col = null;
    	
    	if(isManager()) {
        	col = query(name, editableOnly, searchCol, sort, "", desc, start, rows);
        }
    	else {
	    	String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
	        String orderBy = (sort != null) ? sort : "media.name";
	        String columnName = "UPPER(media.name)";
	        
	        if(! "".equals(searchCol)) {
	            if("library".equalsIgnoreCase(searchCol)) {
	                columnName = "UPPER(library.name)";
	            }
	            else if("album".equals(searchCol)) {
	                columnName = "UPPER(album.name)";
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
	                "ORDER BY " + orderBy, MediaObject.class, args, 0, -1);
	        
	        MediaObject media = null;
	        
	        // Filter the collection again, for paging purposes
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
	
	public int countOwnMedia(String name, boolean editableOnly, String searchCol) throws DaoException {
        String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
        String columnName = "UPPER(media.name)";
        Collection col = null;
        int total = 0;
        
        if(! "".equals(searchCol)) {
            if("library".equalsIgnoreCase(searchCol)) {
                columnName = "UPPER(library.name)";
            }
            else if("album".equals(searchCol)) {
                columnName = "UPPER(album.name)";
            }
        }
        
        Object[] args = { condition, Application.getInstance().getCurrentUser().getId() };
        
        col = super.select("SELECT COUNT(media.id) AS total " +
                "FROM cml_media media, cml_album album, cml_library library " +
                "WHERE media.albumId = album.id AND " +
                "album.libraryId = library.id AND " +
                columnName + " LIKE ? " +
                "AND media.createdBy = ? ", HashMap.class, args, 0, -1);
        
        if(col != null) {
        	for(Iterator i=col.iterator(); i.hasNext(); ) {
        		HashMap map = (HashMap) i.next();
        		total = Integer.parseInt(map.get("total").toString());
        	}
        }
        
        return total;
    }
	
	public Collection query(String name, boolean editableOnly, String searchCol, String sort, String albumId, boolean desc, int start, int rows) throws DaoException {
        String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
        String orderBy = (sort != null) ? sort : "media.name";
        String columnName = "UPPER(media.name)";
        Collection col = null;
        
        if(! "".equals(searchCol)) {
            if("library".equalsIgnoreCase(searchCol)) {
                columnName = "UPPER(library.name)";
            }
            else if("album".equals(searchCol)) {
                columnName = "UPPER(album.name)";
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
	
	public int count(String name, boolean editableOnly, String searchCol, String albumId) throws DaoException {
        String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
        String columnName = "UPPER(media.name)";
        Collection col = null;
        
        if(! "".equals(searchCol)) {
            if("library".equalsIgnoreCase(searchCol)) {
                columnName = "UPPER(library.name)";
            }
            else if("album".equals(searchCol)) {
                columnName = "UPPER(album.name)";
            }
        }
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
            accessibleLibraryCondition = "album.id = '" + albumId + "'";
        }
        
        if(! "".equals(accessibleLibraryCondition)) {
            col = super.select("SELECT media.id, media.name, mediaType, media.isApproved, " +
                    "media.albumId, library.id libraryId, " +
                    "album.name albumName, library.name libraryName, " +
                    "media.dateCreated " +
                    "FROM cml_media media, cml_album album, cml_library library " +
                    "WHERE media.albumId = album.id AND " +
                    "album.libraryId = library.id AND " +
                    columnName + " LIKE ? AND " 
                    + accessibleLibraryCondition, MediaObject.class, args, 0, -1);
            
            MediaObject media = null;
            
            for(Iterator i=col.iterator(); i.hasNext();) {
                media = (MediaObject) i.next();
                if(! isAlbumContributor(media.getAlbumId()) && ! isAlbumManager(media.getAlbumId())) {
                    if(media.getIsApproved().equalsIgnoreCase("N")) {
                        i.remove();
                    }
                }
            }
        }
        
        int totalRecord = 0;
        if(col != null) {
            totalRecord = col.size();
        }
        
        return totalRecord;
    }
	
	public void insert(MediaObject media) throws DaoException {
        super.update("INSERT INTO cml_media (" +
            		"id, name, description, fileName, fileSize, mediaType, isApproved, " +
            		"imageWidth, imageHeight, thumbnailWidth, thumbnailHeight, albumId, " +
            		"dateCreated, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #fileName#, #fileSize#, #mediaType#, #isApproved#, " +
            		"#imageWidth#, #imageHeight#, #thumbnailWidth#, #thumbnailHeight#, #albumId#, " +
            		"CURRENT_TIMESTAMP, #createdBy#)", media);
    }
	
	public void insertMediaStat(MediaStatObject mediaStat) throws DaoException {
        super.update("INSERT INTO cml_media_stat (" +
        		"id, ip, mediaId, actionType, dateCreated, createdBy) VALUES " +
        		"(#id#, #ip#, #mediaId#, #actionType#, CURRENT_TIMESTAMP, #createdBy#)", mediaStat);
    }
	
    private ArrayList getUserPrincipalIds() throws DaoException {
        return getUserPrincipalIds(null);
    }
    
    private ArrayList getUserPrincipalIds(HttpServletRequest request) throws DaoException {
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
