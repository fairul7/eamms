/*
 * AlbumModule
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: Album module handler
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.util.ArrayList;
import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;


public class AlbumModule extends DefaultModule {
    
    public boolean addAlbum(AlbumObject album) {
        AlbumDao dao = (AlbumDao) getDao();
        try {
            dao.insert(album);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error inserting album record: " + error, error);
            return false;
        }
    }
    
    public boolean updateAlbum(AlbumObject album) {
        AlbumDao dao = (AlbumDao) getDao();
        try {
            dao.update(album);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error updating album record: " + error, error);
            return false;
        }
    }
    
    public boolean deleteAlbum(String id) {
        AlbumDao dao = (AlbumDao) getDao();
        try {
            dao.delete(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error deleting album record: " + error, error);
            return false;
        }
    }
    
    public AlbumObject selectAlbum(String id) {
        AlbumDao dao = (AlbumDao) getDao();
        AlbumObject album;
        
        try {
            album = dao.select(id);
            return album;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error selecting album record: " + error, error);
            return null;
        }
    }
    
    public boolean setFeaturedAlbum(String id) {
        AlbumDao dao = (AlbumDao) getDao();
        try {
            dao.setFeaturedAlbum(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error setting album to featured: " + error, error);
            return false;
        }
    }
    
    public boolean setNonfeaturedAlbum(String id) {
        AlbumDao dao = (AlbumDao) getDao();
        try {
            dao.setNonfeaturedAlbum(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error setting album to non-featured: " + error, error);
            return false;
        }
    }
    
    public ArrayList getLibrarySelectList() {
        AlbumDao dao = (AlbumDao) getDao();
        ArrayList alist = new ArrayList();
        
        try {
            alist = dao.getLibrarySelectList();
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving library list: " + error, error);
        }
        
        return alist;
    }
    
    public AlbumObject getFeaturedAlbum() {
        AlbumDao dao = (AlbumDao) getDao();
        AlbumObject album = null;
        
        try {
            album = dao.getFeaturedAlbum();
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error retrieving featured album: " + error, error);
        }
        
        return album;
    }
    
    public Collection findAlbum(String name, boolean editableOnly, String searchCol, String sort, String libraryId, boolean desc, int start, int rows) {
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            return dao.query(name, editableOnly, searchCol, sort, libraryId, desc, start, rows);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding albums " + e.toString(), e);
            return null;
        }
    }

    public int countAlbum(String name, boolean editableOnly, String searchCol, String libraryId) {
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            return dao.count(name, editableOnly, searchCol, libraryId);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding albums " + e.toString(), e);
            return 0;
        }
    }
    
    public boolean isManager() {
        boolean isManager = false;
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            isManager = dao.isManager(); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is manager " + error.toString(), error);
        }
        
        return isManager;
    }
    
    public boolean isManager(String albumId) {
        boolean isManager = false;
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            isManager = dao.isManager(albumId); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is manager " + error.toString(), error);
        }
        
        return isManager;
    }
    
    public boolean isContributor() {
        boolean isContributor = false;
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            isContributor = dao.isContributor(); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is contributor " + error.toString(), error);
        }
        
        return isContributor;
    }
    
    public boolean isContributor(String albumId) {
        boolean isContributor = false;
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            isContributor = dao.isContributor(albumId); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is contributor " + error.toString(), error);
        }
        
        return isContributor;
    }
    
    public boolean isAccessible(String albumId) {
        boolean isAccessible = false;
        AlbumDao dao = (AlbumDao) getDao();
        
        try {
            isAccessible = dao.isAccessible(albumId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error verifying accessibility to an album " + error.toString(), error);
        }
        
        return isAccessible;
    }
}
