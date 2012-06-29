/*
 * MediaDao
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: Media DAO class
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;

import com.tms.cms.medialib.util.ImageUtil;


public class MediaDao extends DataSourceDao {
    private final String STORAGE_ROOT = "medialib";
    
    public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cml_media (" +
            		"id VARCHAR(35) NOT NULL, " +
            		"name VARCHAR(255), " +
            		"description TEXT, " +
            		"fileName VARCHAR(255), " +
            		"fileSize INT(10) DEFAULT 0, " +
            		"mediaType VARCHAR(255), " +
            		"isApproved VARCHAR(1) DEFAULT 'N', " +
            		"imageWidth INT(10) DEFAULT 0, " +
            		"imageHeight INT(10) DEFAULT 0, " +
            		"thumbnailWidth INT(10) DEFAULT 0, " +
            		"thumbnailHeight INT(10) DEFAULT 0, " +
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
    
    public void insert(MediaObject media) throws DaoException {
        super.update("INSERT INTO cml_media (" +
            		"id, name, description, fileName, fileSize, mediaType, isApproved, " +
            		"imageWidth, imageHeight, thumbnailWidth, thumbnailHeight, albumId, " +
            		"dateCreated, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #fileName#, #fileSize#, #mediaType#, #isApproved#, " +
            		"#imageWidth#, #imageHeight#, #thumbnailWidth#, #thumbnailHeight#, #albumId#, " +
            		"now(), #createdBy#)", media);
    }
    
    public void update(MediaObject media, String previousAlbumId) throws DaoException {
        super.update("UPDATE cml_media SET " +
        		"name=#name#, description=#description#, isApproved=#isApproved#, albumId=#albumId# " +
        		"WHERE id=#id#", media);
        
        if(previousAlbumId != null) {
	        StorageFile sourceFile = new StorageFile("/" + STORAGE_ROOT + "/" + previousAlbumId + "/" + media.getFileName());
	        StorageDirectory newDir = new StorageDirectory("/" + STORAGE_ROOT + "/" + media.getAlbumId() + "/");
	        StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
	        try {
	            storage.move(sourceFile, newDir);
	            
	            if(media.getMediaType().startsWith("image")) {
	                StorageFile thumbnailFile = new StorageFile("/" + STORAGE_ROOT + "/" + previousAlbumId + "/" + "thumbnails" + "/" + media.getFileName());
	    	        StorageDirectory newThumbnailDir = new StorageDirectory("/" + STORAGE_ROOT + "/" + media.getAlbumId() + "/" + "thumbnails" + "/");
	    	        
	    	        storage.move(thumbnailFile, newThumbnailDir);
	            }
	        }
	        catch(Exception error) {
	            throw new DaoException("Exception caught while moving file: " + error.toString());
	        }
        }
    }

    public void delete(String id) throws DaoException {
        if(isContributor(id)) {
	        Object[] args = new String[] {
	            id
	        };
	        String albumId = "";
	        String mediaType = "";
	        String fileName = "";
	        
	        Collection col = super.select("SELECT fileName, mediaType, albumId " +
	        		"FROM cml_media " +
	        		"WHERE id = ?", HashMap.class, args, 0, 1);
	        
	        if (col.size() > 0) {
	            Map map = (Map) col.iterator().next();
	            fileName = (String)map.get("fileName");
	            mediaType = (String)map.get("mediaType");
	            albumId = (String)map.get("albumId");
	            
	            StorageFile file = new StorageFile("/" + STORAGE_ROOT + "/" + albumId + "/" + fileName);
	            StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
	            try {
	                storage.delete(file);
	                if(mediaType.startsWith("image")) {
	                    StorageFile thumbnail = new StorageFile("/" + STORAGE_ROOT + "/" + albumId + "/" + "thumbnails" + "/" + fileName);
	                    storage.delete(thumbnail);
	                }
	                
	                super.update("DELETE FROM cml_media WHERE id=?", args);
	            }
	            catch(Exception error) {
		            throw new DaoException("Exception caught while deleting file: " + error.toString());
		        }
	        }
        }
    }
    
    public MediaObject select(String id) throws DaoException {
        Collection col = super.select("SELECT media.id, media.name, media.description, media.fileName, media.fileSize, mediaType, media.isApproved, " +
        		"media.imageWidth, media.imageHeight, media.thumbnailWidth, media.thumbnailHeight, media.albumId, library.id libraryId, " +
        		"album.name albumName, library.name libraryName, " +
        		"media.dateCreated, media.createdBy " +
        		"FROM cml_media media, cml_album album, cml_library library " +
        		"WHERE media.id = ? AND " +
        		"media.albumId = album.id AND " +
        		"album.libraryId = library.id", 
                MediaObject.class, new Object[] {id}, 0, 1);
        
        MediaObject media = null;
        if (col.size() > 0) {
            media = (MediaObject) col.iterator().next();
            media.setFileSizeStr(formatByte(media.getFileSize()));
        }
        
        return media;
    }
    
    public void insertMediaStat(MediaStatObject mediaStat) throws DaoException {
        super.update("INSERT INTO cml_media_stat (" +
        		"id, ip, mediaId, actionType, dateCreated, createdBy) VALUES " +
        		"(#id#, #ip#, #mediaId#, #actionType#, now(), #createdBy#)", mediaStat);
    }
    
    public void approveMedia(String id) throws DaoException {
        if(isManager(id)) {
	        Object[] args = new String[] {
	            id
	        };
	        
	        super.update("UPDATE cml_media set isApproved = 'Y' " +
	        		"WHERE id = ?", args);
        }
    }
    
    public void disapproveMedia(String id) throws DaoException {
        if(isManager(id)) {
	        Object[] args = new String[] {
	            id
	        };
	        
	        super.update("UPDATE cml_media set isApproved = 'N' " +
	        		"WHERE id = ?", args);
        }
    }
    
    public MediaObject saveMediaFile(MediaObject mediaObject) throws Exception {
        boolean isSuccess = true;
        
        try {
            Application application = Application.getInstance();
            StorageService storage = (StorageService) application.getService(StorageService.class);
            // StorageFile file = new StorageFile("/" + STORAGE_ROOT + "/" + mediaObject.getLibraryId() + "/" + mediaObject.getAlbumId(), mediaObject.getStorageFile());
            StorageFile file = new StorageFile("/" + STORAGE_ROOT + "/" + mediaObject.getAlbumId(), mediaObject.getStorageFile());
            mediaObject.setStorageFile(file);
            mediaObject.setFilePath(file.getAbsolutePath());
            storage.store(file);
            
            if(mediaObject.getMediaType().startsWith("image")) {
                ImageUtil image = new ImageUtil(file);
                int maxWidth = getMaxWidth(mediaObject.getLibraryId());
                if(image.getSourceWidth() > maxWidth) {
                    image.scaleImage(maxWidth, null, 85);
                    // Adjust the referencing source image, as it was re-scaled
                    image.setSourceWidth(image.getOutWidth());
                    image.setSourceHeight(image.getOutHeight());
                    // Adjust the file size to the re-scaled image's size
        	        StorageFile rescaledImage = new StorageFile("/" + STORAGE_ROOT + "/" + mediaObject.getAlbumId() + "/" + mediaObject.getFileName());
        	        rescaledImage = storage.get(rescaledImage);
        	        mediaObject.setFileSize(rescaledImage.getSize());
                }
                mediaObject.setImageWidth(image.getSourceWidth());
                mediaObject.setImageHeight(image.getSourceHeight());
                
                image.generateThumbnail();
                mediaObject.setThumbnailWidth(image.getOutWidth());
                mediaObject.setThumbnailHeight(image.getOutHeight());
            }
            
            if(isAutoApproved(mediaObject.getLibraryId())) {
                mediaObject.setIsApproved("Y");
            }
        }
        catch(Exception e) {
            isSuccess = false;
            throw new DaoException("Unable to save uploaded file for " + mediaObject.getId() + ": " + e.toString());
        }
        
        mediaObject.setUploadSuccess(isSuccess);
        return mediaObject;
    }
    
    public ArrayList getLibrarySelectList() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList contributableLibrary = getContributableLibrary(principalIds);
        ArrayList alist = new ArrayList();
        
        if(contributableLibrary.size() > 0) {
            String contributableLibraryCondition = "(";
            for(int i=0; i<contributableLibrary.size(); i++) {
                if(i==0) {
                    contributableLibraryCondition += "id = '" + (String) contributableLibrary.get(i) + "'"; 
                }
                else {
                    contributableLibraryCondition += " OR id = '" + (String) contributableLibrary.get(i) + "'";
                }
            }
            contributableLibraryCondition += ")";
            
            Collection col = super.select("SELECT id, name FROM cml_library " +
                    "WHERE " + contributableLibraryCondition + " ORDER BY name", 
                    SelectListObject.class, null, 0, -1);
            
            for(Iterator i=col.iterator(); i.hasNext(); ) {
                alist.add((SelectListObject) i.next());
            }
        }
        
        return alist;
    }
    
    public ArrayList getAlbumSelectList() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList contributableLibrary = getContributableLibrary(principalIds);
        ArrayList alist = new ArrayList();
        
        if(contributableLibrary.size() > 0) {
            String contributableLibraryCondition = "(";
            for(int i=0; i<contributableLibrary.size(); i++) {
                if(i==0) {
                    contributableLibraryCondition += "libraryId = '" + (String) contributableLibrary.get(i) + "'"; 
                }
                else {
                    contributableLibraryCondition += " OR libraryId = '" + (String) contributableLibrary.get(i) + "'";
                }
            }
            contributableLibraryCondition += ")";
            
            Collection col = super.select("SELECT id, name " +
                    "FROM cml_album " +
                    "WHERE " + contributableLibraryCondition +
                    " ORDER BY name", 
                    SelectListObject.class, null, 0, -1);
            
            for(Iterator i=col.iterator(); i.hasNext(); ) {
                alist.add((SelectListObject) i.next());
            }
        }
        
        return alist;
    }
    
    public ArrayList getLibraryAlbumList(boolean emptyAlbum) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList accessibleLibrary = getAccessibleLibrary(principalIds);
        ArrayList alist = new ArrayList();
        String accessibleLibraryCondition = "";
        
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
            
            String sql = "";
            if(emptyAlbum) {
                // Populate empty album as well
                sql = "SELECT library.id libraryId, library.name libraryName, album.id albumId, album.name albumName " + 
                "FROM cml_library library, cml_album album " +
                "WHERE album.libraryId = library.id AND " + accessibleLibraryCondition +
                " ORDER BY library.name, album.name";
            }
            else {
                sql = "SELECT distinct library.id libraryId, library.name libraryName, album.id albumId, album.name albumName " + 
                "FROM cml_library library, cml_album album, cml_media media " +
                "WHERE media.albumId = album.id " +
                "AND album.libraryId = library.id AND " + accessibleLibraryCondition +
                " ORDER BY library.name, album.name";
            }
            
            Collection col = super.select(sql,
                    LibraryAlbumObject.class, null, 0, -1);
            
            LibraryAlbumObject object;
            for(Iterator i=col.iterator(); i.hasNext(); ) {
                object = (LibraryAlbumObject) i.next();
                if(isAlbumManager(object.getAlbumId()) || isAlbumContributor(object.getAlbumId())) {
                    alist.add(object);
                }
                else {
                    // If the user is just a viewer in this album, check if there was any approved media
                    Collection approvedMediaList = super.select("SELECT id FROM cml_media " +
                            "WHERE albumId = ? AND isApproved = 'Y'", HashMap.class, new Object[] {object.getAlbumId()}, 0, -1);
                    if(approvedMediaList.size() > 0) {
                        alist.add(object);
                    }
                }
            }
        }
        
        return alist;
    }

    public ArrayList getMediaList(String albumId) throws DaoException {
    	return getMediaList(albumId, false);
    }

    public ArrayList getMediaList(String albumId, boolean viewOnly) throws DaoException {
        ArrayList mediaList = new ArrayList();
        
        // If user is not a contributor, media list will only be populated from approved items
        boolean isAlbumContributor = !viewOnly && isAlbumContributor(albumId);
        String condition = "";
        if(! isAlbumContributor) {
            condition = " AND isApproved = 'Y' ";
        }
        
        Collection col = super.select("SELECT id, name, description, fileName, fileSize, mediaType, isApproved, " +
        		"imageWidth, imageHeight, thumbnailWidth, thumbnailHeight, albumId, " +
        		"dateCreated, createdBy " +
        		"FROM cml_media " +
        		"WHERE albumId = ? " + condition +
        		"ORDER BY dateCreated desc", 
                MediaObject.class, new Object[] {albumId}, 0, -1);
        
        for(Iterator i=col.iterator(); i.hasNext();) {
            mediaList.add((MediaObject) i.next());
        }
        
        return mediaList;
    }
    
    public MediaNavObject getMediaNav(String mediaId) throws DaoException {
    	MediaNavObject mediaNav = new MediaNavObject();
    	
    	Collection col = super.select("SELECT albumId " +
    			"FROM cml_media " +
    			"WHERE id = ?", HashMap.class, new Object[] {mediaId}, 0, 1);
    	String albumId = "";
    	
    	if(col != null) {
    		for(Iterator i=col.iterator(); i.hasNext(); ) {
    			HashMap map = (HashMap) i.next();
    			albumId = map.get("albumId").toString();
    		}
    	}
    	
    	if(!"".equals(albumId)) {
    		// If user is not a contributor, media list will only be populated from approved items
            boolean isAlbumContributor = isAlbumContributor(albumId);
            String condition = "";
            if(! isAlbumContributor) {
                condition = " AND isApproved = 'Y' ";
            }
            
    		col = super.select("SELECT id " +
            		"FROM cml_media " +
            		"WHERE albumId = ? " + condition +
            		"ORDER BY dateCreated desc", 
                    MediaObject.class, new Object[] {albumId}, 0, -1);
    		
    		if(col != null) {
    			int count = 0;
    			boolean prevFound = false;
    			boolean nextFound = false;
    			
    			for(Iterator i=col.iterator(); i.hasNext() && ! nextFound; count++) {
        			MediaObject media = (MediaObject) i.next();
        			if(! media.getId().equals(mediaId)) {
        				if(! prevFound) {
        					mediaNav.setPrevId(media.getId());
        				}
        				else {
        					if(! nextFound) {
        						mediaNav.setNextId(media.getId());
        						nextFound = true;
        					}
        				}
        			}
        			else {
        				prevFound = true;
        			}
        		}
    		}
    	}
    	
    	return mediaNav;
    }
    
    public long getFileSize(String mediaId) throws DaoException {
        long fileSize = 0;
        
        Collection col = super.select("SELECT fileSize " +
        		"FROM cml_media " +
        		"WHERE id = ?", HashMap.class, new Object[] {mediaId}, 0, 1);
        
        if (col.size() > 0) {
            Map map = (Map) col.iterator().next();
            try {
                fileSize = ((Number)map.get("fileSize")).intValue();
            }
            catch(NumberFormatException error) {
                fileSize = 0;
                throw new NumberFormatException("Exception caught while retrieving fileSize: " + error);
            }
        }
        
        return fileSize;
    }
    
    public String getImageDimension(String mediaId) throws DaoException {
        String imageDimension = "";
        int width = 0;
        int height = 0;
        
        Collection col = super.select("SELECT imageWidth, imageHeight " +
        		"FROM cml_media " +
        		"WHERE id = ?", HashMap.class, new Object[] {mediaId}, 0, 1);
        
        if (col.size() > 0) {
            Map map = (Map) col.iterator().next();
            try {
                width = ((Number)map.get("imageWidth")).intValue();
                height = ((Number)map.get("imageHeight")).intValue();
            }
            catch(NumberFormatException error) {
                width = 0;
                height = 0;
                throw new NumberFormatException("Exception caught while retrieving fileSize: " + error);
            }
        }
        
        if(width != 0 && height !=0) {
            imageDimension = String.valueOf(width) + " x " + String.valueOf(height) + " px"; 
        }
        
        return imageDimension;
    }
    
    public Collection query(String name, boolean editableOnly, String searchCol, String sort, String albumId, boolean desc, int start, int rows) throws DaoException {
        String condition = (name != null) ? "%" + name + "%" : "%";
        String orderBy = (sort != null) ? sort : "media.name";
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
                if(!editableOnly || (! isAlbumContributor(media.getAlbumId()) && ! isAlbumManager(media.getAlbumId()))) {
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
        String condition = (name != null) ? "%" + name + "%" : "%";
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
    
    public Collection queryOwnMedia(String name, boolean editableOnly, String searchCol, String sort, boolean desc, int start, int rows) throws DaoException {
    	Collection col = null;
    	
    	if(isManager()) {
        	col = query(name, editableOnly, searchCol, sort, "", desc, start, rows);
        }
    	else {
	    	String condition = (name != null) ? "%" + name + "%" : "%";
	        String orderBy = (sort != null) ? sort : "media.name";
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
    
    public int countOwnMedia(String name, boolean editableOnly, String searchCol) throws DaoException {
        String condition = (name != null) ? "%" + name + "%" : "%";
        String columnName = "media.name";
        Collection col = null;
        int total = 0;
        
        if(! "".equals(searchCol)) {
            if("library".equalsIgnoreCase(searchCol)) {
                columnName = "library.name";
            }
            else if("album".equals(searchCol)) {
                columnName = "album.name";
            }
        }
        
        Object[] args = { condition, Application.getInstance().getCurrentUser().getId() };
        
        col = super.select("SELECT COUNT(media.id) AS total " +
                "FROM cml_media media, cml_album album, cml_library library " +
                "WHERE media.albumId = album.id AND " +
                "album.libraryId = library.id AND " +
                columnName + " LIKE ? " +
                "AND media.createdBy = ? ", HashMap.class, args, 0, 1);
        
        if(col != null) {
        	for(Iterator i=col.iterator(); i.hasNext(); ) {
        		HashMap map = (HashMap) i.next();
        		total = Integer.parseInt(map.get("total").toString());
        	}
        }
        
        return total;
    }
    
    public Collection globalQueryResult(String whereClause, Object[] args, String sort, boolean desc, int start, int rows) throws DaoException {
        String orderBy = (sort != null) ? sort : "media.name";
        
        // Form a condition to limit listing of media to those associating with accessible libraries
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList accessibleLibrary = getAccessibleLibrary(principalIds);
        String accessibleLibraryCondition = "";
        Collection col = null;
        
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
            
            col = super.select("SELECT media.id id, media.name name, media.description description, " +
                    "mediaType, media.isApproved isApproved, media.dateCreated dateCreated, " +
                    "media.albumId albumId, library.id libraryId, " +
                    "album.name albumName, library.name libraryName " +
                    "FROM cml_media media, cml_album album, cml_library library " +
                    "WHERE media.albumId = album.id AND " +
                    "album.libraryId = library.id " + whereClause + 
                    " ORDER BY " + orderBy, MediaObject.class, args, 0, -1);
            
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
                    totalAdded++;
                }
                else {
                    i.remove();
                }
            }
        }
        
        return col;
    }
    
    public int globalQueryResultCount(String whereClause, Object[] args) throws DaoException {
        // Form a condition to limit listing of media to those associating with accessible libraries
        ArrayList principalIds = getUserPrincipalIds();
        ArrayList accessibleLibrary = getAccessibleLibrary(principalIds);
        String accessibleLibraryCondition = "";
        Collection col = null;
        
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
            
            col = super.select("SELECT media.id id, media.name name, media.description description, " +
                    "mediaType, media.isApproved isApproved, media.dateCreated dateCreated, " +
                    "media.albumId albumId, library.id libraryId, " +
                    "album.name albumName, library.name libraryName " +
                    "FROM cml_media media, cml_album album, cml_library library " +
                    "WHERE media.albumId = album.id AND " +
                    "album.libraryId = library.id" + whereClause, MediaObject.class, args, 0, -1);
            
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
    
    public String getRandomMediaId(String albumId, boolean userSelectedAlbum) throws DaoException {
        String selectedMediaId = "";
        ArrayList mediaId = new ArrayList();
        String sql = "";
        
        if(userSelectedAlbum) {
            if(isAlbumManager(albumId) || isAlbumContributor(albumId)) {
                sql = "SELECT id FROM cml_media WHERE albumId = ?";
            }
            else {
                sql = "SELECT id FROM cml_media WHERE albumId = ? AND isApproved = 'Y'";
            }
        }
        else {
            sql = "SELECT id FROM cml_media WHERE albumId = ? AND isApproved = 'Y'";
        }
        
        Collection col = super.select(sql, HashMap.class, new Object[] {albumId}, 0, -1);
        if(col.size() > 0) {
            Map map;
            for(Iterator i=col.iterator(); i.hasNext();) {
                map = (Map) i.next();
                mediaId.add((String) map.get("id"));                
            }
            Random rand = new Random();
            selectedMediaId = (String) mediaId.get(rand.nextInt(mediaId.size()));
        }
        
        return selectedMediaId;
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
    
    public boolean isManager(String mediaId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album, cml_media media " +
            		"WHERE principalId = ? " +
            		"AND media.id = ? " +
            		"AND role = 'manager' " +
            		"AND media.albumId = album.id " +
            		"AND album.libraryId = permission.id", HashMap.class, new Object[] {id, mediaId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    } 
    
    public boolean isAlbumManager(String albumId) throws DaoException {
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
    
    // Check if the user is holding contributor role on any media
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
    
    // Check if the user is holding contributor role on a specific media
    public boolean isContributor(String mediaId) throws DaoException {
        return isContributor(mediaId, null);
    }
    
    // Check if the user is holding contributor role on a specific media
    public boolean isContributor(String mediaId, HttpServletRequest request) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds(request);
        boolean isContributor = false;
        
        for(int i=0; i<principalIds.size() && ! isContributor; i++) {
            String principalId = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT permission.id " +
            		"FROM cml_permission permission, cml_album album, cml_media media " +
            		"WHERE principalId = ? " +
            		"AND media.id = ? " +
            		"AND role = 'contributor' " +
            		"AND media.albumId = album.id " +
            		"AND album.libraryId = permission.id", HashMap.class, new Object[] {principalId, mediaId}, 0, -1);
            
            if(col.size() > 0) {
                isContributor = true;
            }
        }
        return isContributor;
    }
    
    // Check if the user is holding contributor role on a specific album
    public boolean isAlbumContributor(String albumId) throws DaoException {
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
    
    public boolean isAccessible(String mediaId) throws DaoException {
        return isAccessible(mediaId, null);
    }
    
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
            		"WHERE principalId = ? AND role = 'viewer' " +
            		"AND media.id = ? " +
            		"AND media.albumId = album.id " +
            		"AND album.libraryId = permission.id" + condition, HashMap.class, new Object[] {id, mediaId}, 0, -1);
            if(col.size() > 0) {
                isAccessible = true;
            }
        }
        
        return isAccessible;
    }
    
    private ArrayList getUserPrincipalIds() throws DaoException {
        return getUserPrincipalIds(null);
    }
    
    private ArrayList getUserPrincipalIds(HttpServletRequest request) throws DaoException {
        ArrayList principalIds = new ArrayList();
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        String userId = null;
        if(request != null) {
            User user = ss.getCurrentUser(request);
            userId = user.getId();
        }
        else {
            User user = Application.getInstance().getCurrentUser();
            if(user != null)
                userId = user.getId();
        }
        if(!(userId == null || "".equals(userId))) {
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
        }


        
        return principalIds;
    }
    
    private int getMaxWidth(String libraryId) throws DaoException {
        Collection col = super.select("SELECT maxWidth " +
        		"FROM cml_library " +
        		"WHERE id = ?", HashMap.class, new Object[] {libraryId}, 0, 1);
        
        int maxWidth = 500;
        
        if (col.size() > 0) {
            Map map = (Map) col.iterator().next();
            try {
                maxWidth = ((Number)map.get("maxWidth")).intValue();
            }
            catch(NumberFormatException error) {
                maxWidth = 500;
                throw new NumberFormatException("Exception caught while retrieving maxWidth: " + error);
            }
        }
        
        return maxWidth;
    }
    
    public boolean isAutoApproved(String libraryId) throws DaoException {
        boolean isAutoApproved = false;
        
        Collection col = super.select("SELECT approvalNeeded " +
        		"FROM cml_library " +
        		"WHERE id = ?", HashMap.class, new Object[] {libraryId}, 0, 1);
        
        if(col.size() > 0) {
            Map map = (Map) col.iterator().next();
            String isApproved = (String) map.get("approvalNeeded");
            
            if("N".equals(isApproved)) {
                isAutoApproved = true;
            }
        }
        
        return isAutoApproved;
    }
    
    public Collection getManagerUserIds(String libraryId) throws DaoException {
        Collection col = super.select("SELECT distinct userGroup.userId userId from cml_permission permission, security_user_group userGroup " +
        		"WHERE permission.id = ? " +
        		"AND permission.role = 'manager' " +
        		"AND permission.principalId = userGroup.groupId", HashMap.class, new Object[] {libraryId}, 0, -1);
        
        return col;
    }
    
    protected ArrayList getAccessibleLibrary(ArrayList principalIds) throws DaoException {
        ArrayList accessibleLibrary = new ArrayList();
        String condition = "";
        if(principalIds.size() > 0)
        {
            condition = " AND (";
            for(int i=0; i<principalIds.size(); i++) {
                if(i==0) {
                    condition += "principalId = '" + (String) principalIds.get(i) + "'";
                }
                else {
                    condition += " OR principalId = '" + (String) principalIds.get(i) + "'";
                }
            }
            condition += ")";
        }

        Collection col = super.select("SELECT distinct id FROM cml_permission " +
        		"WHERE role = 'viewer' " + condition, HashMap.class, null, 0, -1);
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
        		"WHERE role = 'contributor' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            contributableLibrary.add((String) map.get("id"));
        }
        
        return contributableLibrary;
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
