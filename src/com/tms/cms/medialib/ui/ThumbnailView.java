/*
 * ThumbnailView
 * Date Created: Jun 23, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.ArrayList;

import kacang.Application;
import kacang.stdui.Panel;
import kacang.ui.Event;

import com.tms.cms.medialib.model.AlbumModule;
import com.tms.cms.medialib.model.MediaModule;
import com.tms.cms.medialib.model.MediaObject;


public class ThumbnailView extends Panel {
    private String isEditable = "false";
    private ArrayList mediaList = new ArrayList();
    private String page = "";
    private String row = "";
    private String albumId = "";
    private int totalRecord = 0;
    private int totalPage = 0;
    private int beginIndex = 0;
    private int endIndex = 0;
    private final int maxRowItem = 4;
    private int maxPageRow = 3;
    
    public String getDefaultTemplate() {
        return "medialib/thumbnailView";
    }
    
    public void init() {
        super.init();
        setEditable();
        
        Application app = Application.getInstance();
        MediaModule module = (MediaModule) app.getModule(MediaModule.class);
        
        resetDefault();
        mediaList = module.browseMediaList(albumId);
        pagingHangler();
    }
    
    public void onRequest(Event ev) {
        init();
    }
    
    private void pagingHangler() {
        // Validate if the 'page' URL param is a valid int
        int intPage = 1;
        try {
            intPage = Integer.parseInt(page);
        }
        catch(NumberFormatException error) {
            page = "1";
            intPage = 1;
        }
        
        // totalPage must be at least 1, or adjusted according to totalRecord 
        totalRecord = mediaList.size();
        if(totalRecord == 0) {
            totalPage = 1;
        }
        else {
            for(int i=1; i<=totalRecord; i+=(maxRowItem * maxPageRow)) {
                totalPage++;
            }
        }
        
        // Make sure that the 'page' URL param is not exceeding totalRecord
        // Assume 12 medias are shown in a page, and totalRecord is 11. 
        // If an user manipulated the 'page' param to value of 2,
        // certainly there's no record beginning from index of 13 can be shown.
        // if(intPage * (maxRowItem * maxPageRow) > totalRecord) {
        if(intPage > totalPage) {
            page = "1";
            intPage = 1;
        }
        
        // Filter the total record to be shown in a page according to paging parameter
        ArrayList tempList = new ArrayList();
        MediaObject tempObject;
        beginIndex = ((maxRowItem * maxPageRow) * (intPage - 1)) + 1;
        endIndex = (maxRowItem * maxPageRow) * (intPage);
        if(endIndex > totalRecord) {
            endIndex = totalRecord;
        }
        if(totalRecord > (maxRowItem * maxPageRow)) {
            for(int i=beginIndex; i<=endIndex; i++) {
                tempObject = (MediaObject) mediaList.get(i - 1);
                tempList.add(tempObject);
            }
            
            mediaList = tempList;
        }
    }
    
    private void resetDefault() {
        totalPage = 0;
    }
    
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
    public int getBeginIndex() {
        return beginIndex;
    }
    public String getIsEditable() {
        return isEditable;
    }
    public void setEditable() {
        Application app = Application.getInstance();
		AlbumModule module = (AlbumModule) app.getModule(AlbumModule.class);
		
        if(module.isManager(albumId) || module.isContributor(albumId)) {
            isEditable = "true";
        }
        else {
            isEditable = "false";
        }
    }
    public int getEndIndex() {
        return endIndex;
    }
    public int getMaxPageRow() {
        return maxPageRow;
    }
    public void setMaxPageRow(int maxPageRow) {
        this.maxPageRow = maxPageRow;
    }
    public int getMaxRowItem() {
        return maxRowItem;
    }
    public ArrayList getMediaList() {
        return mediaList;
    }
    public void setMediaList(ArrayList mediaList) {
        this.mediaList = mediaList;
    }
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String getRow() {
        return row;
    }
    public void setRow(String row) {
        this.row = row;
        try {
            maxPageRow = Integer.parseInt(row);
        }
        catch(NumberFormatException error) {
            maxPageRow = 3;
            row = "3";
        }
    }
    public int getTotalPage() {
        return totalPage;
    }
    public int getTotalRecord() {
        return totalRecord;
    }
}
