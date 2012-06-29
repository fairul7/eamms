/*
 * AlbumFormEdit
 * Date Created: Jun 23, 2005
 * Author: Tien Soon, Law
 * Description: Extends the abstract Album form to edit album
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.medialib.model.AlbumModule;
import com.tms.cms.medialib.model.AlbumObject;


public class AlbumFormEdit extends AlbumForm {
    private String albumId = "";
    
    public void init() {
        super.init();
    }
    
    public Forward onValidate(Event event) {
        boolean querySuccess = false;
        Application app = Application.getInstance();
        
        AlbumModule module = (AlbumModule) app.getModule(AlbumModule.class);
        AlbumObject album = new AlbumObject();
        
        album.setId(albumId);
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

        querySuccess = module.updateAlbum(album);
        
        super.removeChildren();
        super.init();
        
        if(querySuccess) {
            return new Forward(FORWARD_SUCCESS);
        }
        else {
            return new Forward(FORWARD_FAILURE);
        }
    }
    
    public void onRequest(Event evt) {
        init();
        
        if(! "".equals(albumId)) {
	        AlbumModule module = (AlbumModule) Application.getInstance().getModule(AlbumModule.class);
	        AlbumObject album = module.selectAlbum(albumId);
	        libraryName.setSelectedOption(album.getLibraryId());
	        albumName.setValue(album.getName());
	        description.setValue(album.getDescription());
	        description.setRows("3");
	        if(album.isFeatured()) {
	            featured.setChecked(true);
	            nonfeatured.setChecked(false);
	        }
	        else {
	            nonfeatured.setChecked(true);
	            featured.setChecked(false);
	        }
	        eventDate.setDate(album.getEventDate());
	        NumberFormat zeroFillingNumberFormat = new DecimalFormat("00");
	        eventTimeHour.setValue(zeroFillingNumberFormat.format(album.getEventDate().getHours()));
	        eventTimeMin.setValue(zeroFillingNumberFormat.format(album.getEventDate().getMinutes()));
        }
    }
    
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
