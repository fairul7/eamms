/*
 * AlbumView
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

import com.tms.cms.medialib.model.AlbumModule;
import com.tms.cms.medialib.model.AlbumObject;


public class AlbumView extends Panel {
    private AlbumObject album;
    private String albumId = "";
    private String highestRole = "";
    private String createdBy = "";
    
    public String getDefaultTemplate() {
        return "medialib/viewAlbum";
    }
    
    public void init() {
    }
    
    public void onRequest(Event evt) {
        init();
        
        Application app = Application.getInstance();
        AlbumModule module = (AlbumModule) app.getModule(AlbumModule.class);
        album = module.selectAlbum(albumId);
        
        if(album != null) {
        	SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
        	try {
        		User user = securityService.getUser(album.getCreatedBy());
        		createdBy = user.getUsername();
        	}
        	catch(SecurityException error) {
        		Log.getLog(getClass()).error("Exception caught while retrieving User object", error);
        	}
        }
    }
    
    public AlbumObject getAlbum() {
        return album;
    }
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
	public String getCreatedBy() {
		return createdBy;
	}
}
