package com.tms.cms.medialib.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

public class AlbumDaoDB2 extends AlbumDao{
	
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
            String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
            String orderBy = (sort != null) ? sort : "album.name";
            String columnName = "UPPER(album.name)";
            
            if(! "".equals(searchCol)) {
                if("library".equalsIgnoreCase(searchCol)) {
                    columnName = "UPPER(library.name)";
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
	
	public void update(AlbumObject album) throws DaoException {
        super.update("UPDATE cml_album SET " +
        		"name=#name#, description=#description#, eventDate=#eventDate#, featured=#featured#, libraryId=#libraryId#, lastUpdatedDate=CURRENT_TIMESTAMP " +
        		"WHERE id=#id#", album);
    }
	
	public void insert(AlbumObject album) throws DaoException {
        /*
        String id = UuidGenerator.getInstance().getUuid();
        album.setId(id);
        */
        super.update("INSERT INTO cml_album (" +
            		"id, name, description, eventDate, featured, libraryId, dateCreated, lastUpdatedDate, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #eventDate#, #featured#, #libraryId#, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, #createdBy#)", album);
    }
}
