/*
 * MediaModule
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: Media module handler
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;


public class MediaModule extends DefaultModule implements SearchableModule {
	
    public boolean addMedia(MediaObject media) {
        MediaDao dao = (MediaDao) getDao();
        boolean isSuccess;
        try {
            media = dao.saveMediaFile(media);
            isSuccess = media.isUploadSuccess();
            if(isSuccess) {
                dao.insert(media);
            }
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error inserting media record: " + error, error);
            isSuccess =  false;
        }
        
        return isSuccess;
    }
    
    public boolean updateMedia(MediaObject media, String previousAlbumId) {
        MediaDao dao = (MediaDao) getDao();
        try {
            dao.update(media, previousAlbumId);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error updating media record: " + error, error);
            return false;
        }
    }
    
    public boolean deleteMedia(String id) {
        MediaDao dao = (MediaDao) getDao();
        try {
            dao.delete(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error deleting media record: " + error, error);
            return false;
        }
    }
    
    public MediaObject selectMedia(String id) {
        MediaDao dao = (MediaDao) getDao();
        MediaObject mediaObject = null;
        
        try {
            mediaObject = dao.select(id);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error selecting media record: " + error, error);
        }
        
        return mediaObject;
    }
    
    public boolean insertMediaStat(MediaStatObject mediaStat) {
        MediaDao dao = (MediaDao) getDao();
        boolean isSuccess;
        try {
            dao.insertMediaStat(mediaStat);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error inserting media stat: " + error, error);
            return false;
        }
    }
    
    public boolean approveMedia(String id) {
        MediaDao dao = (MediaDao) getDao();
        try {
            dao.approveMedia(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error approving media: " + error, error);
            return false;
        }
    }
    
    public boolean disapproveMedia(String id) {
        MediaDao dao = (MediaDao) getDao();
        try {
            dao.disapproveMedia(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error disapproving media: " + error, error);
            return false;
        }
    }
    
    public ArrayList getLibrarySelectList() {
        MediaDao dao = (MediaDao) getDao();
        ArrayList alist = new ArrayList();
        
        try {
            alist = dao.getLibrarySelectList();
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving library list: " + error, error);
        }
        
        return alist;
    }
    
    public ArrayList getAlbumSelectList() {
        MediaDao dao = (MediaDao) getDao();
        ArrayList alist = new ArrayList();
        
        try {
            alist = dao.getAlbumSelectList();
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving album list: " + error, error);
        }
        
        return alist;
    }
    
    public ArrayList getLibraryAlbumList(boolean emptyAlbum) {
        MediaDao dao = (MediaDao) getDao();
        ArrayList alist = new ArrayList();
        
        try {
            alist = dao.getLibraryAlbumList(emptyAlbum);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving library-album list: " + error, error);
        }
        
        return alist;
    }
    
    public ArrayList getMediaList(String albumId) {
        MediaDao dao = (MediaDao) getDao();
        ArrayList mediaList = new ArrayList();
        
        try {
            mediaList = dao.getMediaList(albumId);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving media list: " + error, error);
        }
        
        return mediaList;
    }
    
    public ArrayList browseMediaList(String albumId) {
        MediaDao dao = (MediaDao) getDao();
        ArrayList mediaList = new ArrayList();
        
        try {
            mediaList = dao.getMediaList(albumId, true);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving media list: " + error, error);
        }
        
        return mediaList;
    }
    
    public MediaNavObject getMediaNav(String mediaId) {
        MediaDao dao = (MediaDao) getDao();
        MediaNavObject mediaNav = new MediaNavObject();
        
        try {
            mediaNav = dao.getMediaNav(mediaId);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving media navigation info: " + error, error);
        }
        
        return mediaNav;
    }
    
    public String getRandomMediaId(String albumId, boolean userSelectedAlbum) {
        MediaDao dao = (MediaDao) getDao();
        String selectedMediaId = "";
        
        try {
            selectedMediaId = dao.getRandomMediaId(albumId, userSelectedAlbum);
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving random mediaId: " + error, error);
        }
        
        return selectedMediaId;
    }
    
    public String getSize(MediaObject mediaObject) {
        MediaDao dao = (MediaDao) getDao();
        String size = "";
        long fileSize = 0;
        String imageDimension = "";
        
        try {
            fileSize = dao.getFileSize(mediaObject.getId());
            if(mediaObject.getMediaType().startsWith("image")) {
                imageDimension = dao.getImageDimension(mediaObject.getId());
            }
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving media file: " + error, error);
        }
        
        if(fileSize != 0) {
            size = formatByte(fileSize);
        }
        if(! "".equals(imageDimension)) {
            size += ", " + imageDimension;
        }
        
        return size;
    }
    
    public boolean isManager() {
        boolean isManager = false;
        MediaDao dao = (MediaDao) getDao();
        
        try {
            isManager = dao.isManager(); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is manager " + error.toString(), error);
        }
        
        return isManager;
    }
    
    public boolean isAlbumManager(String albumId) {
        boolean isManager = false;
        MediaDao dao = (MediaDao) getDao();
        
        try {
            isManager = dao.isAlbumManager(albumId); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is manager " + error.toString(), error);
        }
        
        return isManager;
    }
    
    public boolean isContributor() {
        boolean isContributor = false;
        MediaDao dao = (MediaDao) getDao();
        
        try {
            isContributor = dao.isContributor(); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is contributor " + error.toString(), error);
        }
        
        return isContributor;
    }
    
    public boolean isContributor(String mediaId) {
        boolean isContributor = false;
        MediaDao dao = (MediaDao) getDao();
        
        try {
            isContributor = dao.isContributor(mediaId); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is contributor " + error.toString(), error);
        }
        
        return isContributor;
    }
    
    public boolean isAlbumContributor(String albumId) {
        boolean isContributor = false;
        MediaDao dao = (MediaDao) getDao();
        
        try {
            isContributor = dao.isAlbumContributor(albumId); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is contributor " + error.toString(), error);
        }
        
        return isContributor;
    }
    
    public boolean isAccessible(String mediaId) {
        return isAccessible(mediaId, null);
    }
    
    public boolean isAccessible(String mediaId, HttpServletRequest request) {
        boolean isAccessible = false;
        MediaDao dao = (MediaDao) getDao();
        
        try {
            isAccessible = dao.isAccessible(mediaId, request);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error verifying accessibility to an media " + error.toString(), error);
        }
        
        return isAccessible;
    }
    
    public Collection findMedia(String name, boolean editableOnly, String searchCol, String sort, String albumId, boolean desc, int start, int rows) {
        MediaDao dao = (MediaDao) getDao();
        
        try {
            return dao.query(name, editableOnly, searchCol, sort, albumId, desc, start, rows);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding medias " + e.toString(), e);
            return null;
        }
    }

    public int countMedia(String name, boolean editableOnly, String searchCol, String albumId) {
        MediaDao dao = (MediaDao) getDao();
        
        try {
            return dao.count(name, editableOnly, searchCol, albumId);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding medias " + e.toString(), e);
            return 0;
        }
    }
    
    public Collection queryOwnMedia(String name, boolean editableOnly, String searchCol, String sort, boolean desc, int start, int rows) {
    	MediaDao dao = (MediaDao) getDao();
        
        try {
            return dao.queryOwnMedia(name, editableOnly, searchCol, sort, desc, start, rows);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding medias " + e.toString(), e);
            return null;
        }
    }
    
    public int countOwnMedia(String name, boolean editableOnly, String searchCol) {
        MediaDao dao = (MediaDao) getDao();
        
        try {
            return dao.countOwnMedia(name, editableOnly, searchCol);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding medias " + e.toString(), e);
            return 0;
        }
    }
    
    public boolean isAutoApproved(String libraryId) {
        MediaDao dao = (MediaDao) getDao();
        
        try {
            return dao.isAutoApproved(libraryId);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error checking media approval option " + e.toString(), e);
            return false;
        }
    }
    
    public Collection getManagerUserIds(String libraryId) {
        MediaDao dao = (MediaDao) getDao();
        
        try {
            return dao.getManagerUserIds(libraryId);
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error retrieving Manager IDs " + e.toString(), e);
            return null;
        }
    }
    
    private String formatByte(long fileSize) {
        String result = "";
        
        if (fileSize >= 1024) {
            double db = (((double)fileSize)/1024);
            Math.pow(2, 10);
            result = new DecimalFormat("0.00").format(db) + "KB";
        }
        else {
            result = fileSize + "bytes";
        }
        
        return result;
    }
    
    public boolean isSearchSupported()
    {
        return true;
    }
    
    public boolean isFullTextSearchSupported()
    {
        return false;
    }
    
    public SearchResult search(String query, int start, int rows, String userId)  throws QueryException
	{
		return search(query, start, rows, userId, null, null);
	}
    
    public SearchResult search(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
        MediaDao dao = (MediaDao) getDao();
        Map valueMap;
        SearchResultItem item;
        SearchResult results = new SearchResult();
        String whereClause = " ";
        String likeClauseParam = "'%" + query + "%'";
        Object[] args = null;
        int resultCount = 0;
        
        if(query.trim().length() > 0) {
			try
			{
			    whereClause += " AND (media.name LIKE " + likeClauseParam +
			    " OR media.description LIKE " + likeClauseParam +
			    " OR album.name LIKE " + likeClauseParam +
			    " OR library.name LIKE " + likeClauseParam;
			    if(startDate != null) {
			        whereClause += " OR media.dateCreated >= '" + startDate + "'";
			    }
			    if(endDate != null) {
			        whereClause += " OR media.dateCreated <= '" + endDate + "'";
			    }
			    whereClause += ") ";
			    
			    Collection col = dao.globalQueryResult(whereClause, args, "media.name", true, start, rows);
			    MediaObject media;
                
                if(col != null) {
    			    for(Iterator i = col.iterator(); i.hasNext();) {
    			        media = (MediaObject) i.next();
    			        String title = media.getLibraryName() + " > " + media.getAlbumName() + " > " + media.getName();
    			        String description = "-No Description-";
    			        if(media.getDescription().trim().length() > 0) {
    			            if(media.getDescription().trim().length() > 200) {
    			                description = media.getDescription().trim().substring(0, 200) + "...";
    			            }
    			            else {
    			                description = media.getDescription().trim();
    			            }
    			        }
    			        valueMap = new SequencedHashMap();
    	                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
    	                valueMap.put(SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS, media.getClass().getName());
    	                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, media.getId());
    	                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
    	                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, description);
    	                valueMap.put(SearchableModule.SEARCH_PROPERTY_DATE, media.getDateCreated());
    	                
    	                item = new SearchResultItem();
    	                item.setValueMap(valueMap);
    	                if (media.getName() != null && media.getName().indexOf(query) != -1) {
    	                    // search string is in media name, so score 100
    	                    item.setScore(new Float(100));
    	                } else {
    	                    // search string is in else where, so score 50
    	                    item.setScore(new Float(50));
    	                }
    	                results.add(item);
    			    }
    			    
    			    resultCount = dao.globalQueryResultCount(whereClause, args);
                }
			}
			catch(Exception error)
			{
				Log.getLog(getClass()).error(error.getMessage(), error);
			}
        }
		results.setTotalSize(resultCount);
		
		return results;
	}
    
    public SearchResult searchFullText(String s, int i, int i1, String s1) throws QueryException
	{
		return new SearchResult();
	}
    
    public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		return new SearchResult();
	}
}