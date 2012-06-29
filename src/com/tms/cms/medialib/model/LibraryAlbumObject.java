/*
 * LibraryAlbumObject
 * Date Created: Jun 21, 2005
 * Author: Tien Soon, Law
 * Description: An object class to model the library-album relationship.
 * 				The LibraryAlbumObject is used to store pair of library and album,
 * 				which is applicable in JavaScript to produce an auto-adjusted library-album selection list 
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import kacang.model.DefaultDataObject;


public class LibraryAlbumObject extends DefaultDataObject {
    String libraryId = "";
    String libraryName = "";
    String albumId = "";
    String albumName = "";
    
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public String getLibraryId() {
        return libraryId;
    }
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
    public String getLibraryName() {
        return libraryName;
    }
    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
