/*
 * LibraryView
 * Date Created: Jul 1, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.cms.medialib.model.LibraryModule;
import com.tms.cms.medialib.model.LibraryObject;


public class LibraryView extends Panel {
    private LibraryObject library;
    private String libraryId = "";
    private String highestRole = "";
    private String createdBy = "";
    
    public String getDefaultTemplate() {
        return "medialib/viewLibrary";
    }
    
    public void init() {
    }
    
    public void onRequest(Event evt) {
        init();
        
        Application app = Application.getInstance();
        LibraryModule module = (LibraryModule) app.getModule(LibraryModule.class);
        library = module.selectLibrary(libraryId);
        highestRole = module.getHighestRole(libraryId);
        
        if(library != null) {
        	SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
        	try {
        		User user = securityService.getUser(library.getCreatedBy());
        		createdBy = user.getUsername();
        	}
        	catch(SecurityException error) {
        		Log.getLog(getClass()).error("Exception caught while retrieving User object", error);
        	}
        }
    }
    
    public String getCreatedBy() {
		return createdBy;
	}
	public String getHighestRole() {
        return highestRole;
    }
    public LibraryObject getLibrary() {
        return library;
    }
    public String getLibraryId() {
        return libraryId;
    }
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
}
