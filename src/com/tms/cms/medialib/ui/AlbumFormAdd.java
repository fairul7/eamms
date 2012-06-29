/*
 * AlbumFormAdd
 * Date Created: Jun 16, 2005
 * Author: Tien Soon, Law
 * Description: Extends the abstract Album form to create new album
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.Date;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.cms.medialib.model.AlbumModule;
import com.tms.cms.medialib.model.AlbumObject;


public class AlbumFormAdd extends AlbumForm {
	
	public String getDefaultTemplate() {
		return "medialib/newAlbum";
	}
	
    public Forward onValidate(Event event) {
        boolean querySuccess = false;
        Application app = Application.getInstance();
        
        AlbumModule module = (AlbumModule) app.getModule(AlbumModule.class);
        AlbumObject album = new AlbumObject();
        
        album.setCreatedBy(getWidgetManager().getUser().getId());
        album.setName((String) albumName.getValue());
        album.setDescription((String) description.getValue());
        if(featured.isChecked()) {
            album.setFeatured(true);
        }
        else {
            album.setFeatured(false);
        }
        Date date = eventDate.getDate();
        date.setHours(Integer.parseInt((String) eventTimeHour.getValue()));
        date.setMinutes(Integer.parseInt((String) eventTimeMin.getValue()));
        album.setEventDate(date);
        String libraryId = (String) libraryName.getSelectedOptions().keySet().iterator().next();
        album.setLibraryId(libraryId);

        String albumId = UuidGenerator.getInstance().getUuid();
        album.setId(albumId);
        querySuccess = module.addAlbum(album);
        
        super.removeChildren();
        super.init();
        
        if(querySuccess) {
            return new Forward(albumId);
        }
        else {
            return new Forward(FORWARD_FAILURE);
        }
    }
}
