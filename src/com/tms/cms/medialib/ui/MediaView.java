/*
 * MediaView
 * Date Created: Jun 27, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import kacang.Application;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.util.UuidGenerator;

import com.tms.cms.medialib.model.MediaModule;
import com.tms.cms.medialib.model.MediaNavObject;
import com.tms.cms.medialib.model.MediaObject;
import com.tms.cms.medialib.model.MediaStatObject;


public class MediaView extends Panel {
    private MediaObject mediaObject;
    private String mediaId = "";
    private String accessedMediaId = "";
    private String ip = "";
    private MediaNavObject mediaNav;
    
    public String getDefaultTemplate() {
        return "medialib/viewMedia";
    }
    
    public void init() {
    }
    
    public void onRequest(Event evt) {
        init();
        
        if(! "".equals(mediaId)) {
            Application app = Application.getInstance();
            MediaModule mediaModule = (MediaModule) app.getModule(MediaModule.class);
            mediaObject = mediaModule.selectMedia(mediaId);
            mediaNav = mediaModule.getMediaNav(mediaObject.getId());
            
            // Update media statistics
            if(! mediaId.equals(accessedMediaId) && ! "".equals(ip)) {
                MediaStatObject mediaStat = new MediaStatObject();
                mediaStat.setId(UuidGenerator.getInstance().getUuid());
                mediaStat.setActionType("viewOnly");
                mediaStat.setCreatedBy(getWidgetManager().getUser().getId());
                mediaStat.setIp(ip);
                mediaStat.setMediaId(mediaId);
                mediaModule.insertMediaStat(mediaStat);
                accessedMediaId = mediaId;
            }
        }
    }
    
    public String getMediaId() {
        return mediaId;
    }
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
    public MediaObject getMediaObject() {
        return mediaObject;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

	public MediaNavObject getMediaNav() {
		return mediaNav;
	}
}
