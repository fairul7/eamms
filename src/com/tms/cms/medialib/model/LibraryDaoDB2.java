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
import kacang.util.Log;
import kacang.util.Transaction;

public class LibraryDaoDB2 extends LibraryDao{
	
	public void update(LibraryObject library) throws DaoException {
        Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("UPDATE cml_library SET " +
            		"name=#name#, description=#description#, approvalNeeded=#approvalNeeded#, maxWidth=#maxWidth#, lastUpdatedDate=CURRENT_TIMESTAMP " +
            		"WHERE id=#id#", library);
            updateLibraryAccess(tx, library.getId(), library.getManagerGroup(), library.getContributorGroup(), library.getViewerGroup());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }
	
	public Collection query(String name, boolean editableOnly, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection col = null;
        String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
        String orderBy = (sort != null) ? sort : "name";
        if (desc)
            orderBy += " DESC";
        Object[] args = { condition };
        
        // If user is granted with application level access to create library
        if(isAccessibleToAll()) {
            col =  super.select("SELECT library.id, library.name, library.description, approvalNeeded " +
            		"FROM cml_library library " +
            		"WHERE UPPER(name) LIKE ? ORDER BY " + orderBy, LibraryObject.class, args, start, rows);
            
            LibraryObject library = null;
            for(Iterator i=col.iterator(); i.hasNext();) {
                library = (LibraryObject) i.next();
                
                library.setTotalAlbum(String.valueOf(countTotalAlbum(library.getId())));
            }
        }
        // Else the library listing has to be filtered by accessible libraries
        else {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = new ArrayList();
            if(editableOnly) {
            	accessibleLibrary = getEditableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            String accessibleLibraryCondition = "";
            
            if(accessibleLibrary.size() > 0) {
                accessibleLibraryCondition = "(";
                for(int i=0; i<accessibleLibrary.size(); i++) {
                    if(i==0) {
                        accessibleLibraryCondition += "id = '" + (String) accessibleLibrary.get(i) + "'"; 
                    }
                    else {
                        accessibleLibraryCondition += " OR id = '" + (String) accessibleLibrary.get(i) + "'";
                    }
                }
                accessibleLibraryCondition += ")";
                
                col =  super.select("SELECT library.id, library.name, library.description, approvalNeeded " +
                        "FROM cml_library library " +
                        "WHERE UPPER(name) LIKE ? " +
                        "AND " + accessibleLibraryCondition + " ORDER BY " + orderBy, LibraryObject.class, args, start, rows);
                
                // Explicitly set some data field
                LibraryObject library = null;
                int maxDescriptionLength = 255;
                boolean accessibleToAll = isAccessibleToAll();
                for(Iterator i=col.iterator(); i.hasNext();) {
                    library = (LibraryObject) i.next();
                    
                    library.setTotalAlbum(String.valueOf(countTotalAlbum(library.getId())));
                    if(library.getDescription() != null) {
                    	if (library.getDescription().length() > maxDescriptionLength) {
                    		library.setDescription(library.getDescription().substring(0, maxDescriptionLength - 1) + "...");
                    	}
                    }
                    else {
                        library.setDescription("");
                    }
                    if(accessibleToAll) {
                        library.setManageable(true);
                    }
                    else {
                        if(isManager(library.getId())) {
                            library.setManageable(true);
                        }
                    }
                }
            }
        }
        
        return col;
    }
	
	public int count(String name, boolean editableOnly) throws DaoException {
        String condition = (name != null) ? "%" + name.toUpperCase() + "%" : "%";
        Object[] args = { condition };
        
        Collection list = null;
        
        // If user is granted with application level access to create library
        if(isAccessibleToAll()) {
            list =  super.select("SELECT COUNT(*) AS total FROM cml_library WHERE UPPER(name) LIKE ?", HashMap.class, args, 0, 1);
        }
        // Else the library listing has to be filtered by accessible libraries
        else {
            ArrayList principalIds = getUserPrincipalIds();
            ArrayList accessibleLibrary = new ArrayList();
            if(editableOnly) {
            	accessibleLibrary = getEditableLibrary(principalIds);
            }
            else {
            	accessibleLibrary = getAccessibleLibrary(principalIds);
            }
            String accessibleLibraryCondition = "";
            
            if(accessibleLibrary.size() > 0) {
                accessibleLibraryCondition = "(";
                for(int i=0; i<accessibleLibrary.size(); i++) {
                    if(i==0) {
                        accessibleLibraryCondition += "id = '" + (String) accessibleLibrary.get(i) + "'"; 
                    }
                    else {
                        accessibleLibraryCondition += " OR id = '" + (String) accessibleLibrary.get(i) + "'";
                    }
                }
                accessibleLibraryCondition += ")";
                
                list =  super.select("SELECT COUNT(*) AS total FROM cml_library WHERE UPPER(name) LIKE ? AND " + accessibleLibraryCondition, HashMap.class, args, 0, 1);
            }
        }
        
        int totalRecord = 0;
        if(list != null) {
            HashMap map = (HashMap)list.iterator().next();
            totalRecord = Integer.parseInt(map.get("total").toString());
        }
        
        return totalRecord;
    }
	
	public void insert(LibraryObject library) throws DaoException {
        Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("INSERT INTO cml_library (" +
            		"id, name, description, approvalNeeded, maxWidth, dateCreated, lastUpdatedDate, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #approvalNeeded#, #maxWidth#, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, #createdBy#)", library);
            updateLibraryAccess(tx, library.getId(), library.getManagerGroup(), library.getContributorGroup(), library.getViewerGroup());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

}
