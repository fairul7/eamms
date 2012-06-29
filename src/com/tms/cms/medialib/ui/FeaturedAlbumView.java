/*
 * FeaturedAlbumView
 * Date Created: Jun 16, 2005
 * Author: Tien Soon, Law
 * Description: UI class to represent any album featured in the home of Media Library
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.ArrayList;

import kacang.Application;
import kacang.stdui.Panel;
import kacang.ui.Event;

import com.tms.cms.medialib.model.AlbumModule;
import com.tms.cms.medialib.model.AlbumObject;
import com.tms.cms.medialib.model.MediaModule;
import com.tms.cms.medialib.model.MediaObject;

public class FeaturedAlbumView extends Panel {
    private boolean randomSelect = true;
    private String albumId = "";
    private AlbumObject album;
    private MediaObject media;
    private ArrayList libraryAlbumList;
    private int libraryAlbumListSize = 0;
    
    public String getDefaultTemplate() {
        return "medialib/featuredAlbumView";
    }
    
    public void init() {
        super.init();
        
        Application app = Application.getInstance();
        AlbumModule albumModule = (AlbumModule) app.getModule(AlbumModule.class);
        MediaModule mediaModule = (MediaModule) app.getModule(MediaModule.class);
        
        // Randomly select a featured album
        if(randomSelect) {
            album = albumModule.getFeaturedAlbum();
        }
        // Retrieve the selected album
        else {
            if(! "".equals(albumId))
                album = albumModule.selectAlbum(albumId);
            else {
                album = albumModule.getFeaturedAlbum();
            }
        }
        // Randomly select a media file
        if(album != null) {
            if("".equals(albumId)) {
                media = mediaModule.selectMedia(mediaModule.getRandomMediaId(album.getId(), false));
            }
            else {
                media = mediaModule.selectMedia(mediaModule.getRandomMediaId(album.getId(), true));
            }
        }
        
        libraryAlbumList = mediaModule.getLibraryAlbumList(false);
        libraryAlbumListSize = libraryAlbumList.size();
    }

    public void onRequest(Event ev) {
        init();
    }
    
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
    public AlbumObject getAlbum() {
        return album;
    }
    public ArrayList getLibraryAlbumList() {
        return libraryAlbumList;
    }
    public int getLibraryAlbumListSize() {
        return libraryAlbumListSize;
    }
    public MediaObject getMedia() {
        return media;
    }
    public boolean isRandomSelect() {
        return randomSelect;
    }
    public void setRandomSelect(boolean randomSelect) {
        this.randomSelect = randomSelect;
    }
}
