/*
 * LibraryModule
 * Date Created: Jun 17, 2005
 * Author: Tien Soon, Law
 * Description: Library module handler
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;


public class LibraryModule extends DefaultModule {
    public static final String PERMISSION_CREATE_LIBRARY = "com.tms.cms.medialib.CreateLibrary"; // Permission to create new library
    
    public boolean addLibrary(LibraryObject library) {
        LibraryDao dao = (LibraryDao) getDao();
        try {
            dao.insert(library);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error inserting library record: " + error, error);
            return false;
        }
    }
    
    public boolean updateLibrary(LibraryObject library) {
        LibraryDao dao = (LibraryDao) getDao();
        try {
            dao.update(library);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error updating library record: " + error, error);
            return false;
        }
    }
    
    public boolean deleteLibrary(String id) {
        LibraryDao dao = (LibraryDao) getDao();
        try {
            dao.delete(id);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error deleting library record: " + error, error);
            return false;
        }
    }
    
    public LibraryObject selectLibrary(String id) {
        LibraryDao dao = (LibraryDao) getDao();
        LibraryObject library = null;
        
        try {
            library = dao.select(id);
            return library;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error selecting library record: " + error, error);
            return null;
        }
    }
    
    public boolean toggleApprovalNeeded(String id, String value) {
        LibraryDao dao = (LibraryDao) getDao();
        try {
            dao.toggleApprovalNeeded(id, value);
            return true;
        }
        catch(Exception error) {
            Log.getLog(getClass()).error("Error toggling approval needed option: " + error, error);
            return false;
        }
    }

    public String getHighestRole(String libraryId) {
        LibraryDao dao = (LibraryDao) getDao();
        
        try {
            return dao.getHighestRole(libraryId);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error retrieving highest role for login user " + e.toString(), e);
            return "";
        }
    }
    
    public boolean isManager() {
        boolean isManager = false;
        LibraryDao dao = (LibraryDao) getDao();
        
        try {
            isManager = dao.isManager(); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is manager " + error.toString(), error);
        }
        
        return isManager;
    }
    
    public boolean isManager(String libraryId) {
        boolean isManager = false;
        LibraryDao dao = (LibraryDao) getDao();
        
        try {
            isManager = dao.isManager(libraryId); 
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error validating if user is manager " + error.toString(), error);
        }
        
        return isManager;
    }
    
    public boolean isAccessible(String libraryId) {
        boolean isAccessible = false;
        LibraryDao dao = (LibraryDao) getDao();
        
        try {
            isAccessible = dao.isAccessible(libraryId);
        }
        catch(DaoException error) {
            Log.getLog(getClass()).error("Error verifying accessibility to a library " + error.toString(), error);
        }
        
        return isAccessible;
    }
    
    public Collection findLibrary(String name, boolean editableOnly, String sort, boolean desc, int start, int rows) {
        LibraryDao dao = (LibraryDao) getDao();
        
        try {
            return dao.query(name, editableOnly, sort, desc, start, rows);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding libraries " + e.toString(), e);
            return null;
        }
    }
    
    public int countLibrary(String name, boolean editableOnly) {
        LibraryDao dao = (LibraryDao) getDao();
        
        try {
            return dao.count(name, editableOnly);
        } 
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error finding libraries " + e.toString(), e);
            return 0;
        }
    }
}
