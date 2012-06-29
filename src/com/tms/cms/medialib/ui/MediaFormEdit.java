/*
 * MediaFormEdit
 * Date Created: Jun 24, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.ArrayList;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.medialib.model.MediaModule;
import com.tms.cms.medialib.model.MediaObject;
import com.tms.cms.medialib.model.MediaStatObject;
import com.tms.cms.medialib.model.SelectListObject;


public class MediaFormEdit extends Form {
    protected MediaObject mediaObject;
    protected String mediaId = "";
    protected String accessedMediaId = "";
    protected SelectBox libraryName;
    protected SelectBox albumName;
    protected String selectedLibraryId = "";
    protected String selectedAlbumId = "";
    protected String size = "";
    protected TextField mediaName;
    protected TextBox description;
    protected Radio approved;
    protected Radio notApproved;
    protected Panel publishingStatusPanel; 
    protected ValidatorNotEmpty vne;
    protected Button submit;
    protected Button cancel;
    protected String isManager = "false";
    protected static final String FORWARD_SUCCESS = "success";
    protected static final String FORWARD_FAILURE = "failure";
    protected String ip = "";
    
    public String getDefaultTemplate() {
        return "medialib/editMedia";
    }
    
    public void init() {
        setMethod("POST");
        
        Application app = Application.getInstance();
        MediaModule mediaModule = (MediaModule) app.getModule(MediaModule.class);
        ArrayList alist;
        
        vne = new ValidatorNotEmpty("vne", app.getMessage("medialib.message.notEmpty", "Please fill in this field"));
        
        // Library List
        libraryName = new SelectBox("libraryName");
        libraryName.setMultiple(false);
        Map mapLibNames = new SequencedHashMap();
        mapLibNames.put("", app.getMessage("medialib.label.pleaseSelect", "---Please Select---"));
        alist = mediaModule.getLibrarySelectList();
        if(alist.size() > 0) {
	        for(int i=0; i<alist.size(); i++) {
	            SelectListObject item = (SelectListObject) alist.get(i);
	            mapLibNames.put(item.getId(), item.getName());
	        }
        }
        libraryName.setOptionMap(mapLibNames);
        libraryName.setOnChange("setLibraryAlbumChange()");
        addChild(libraryName);
        
        // Album List
        albumName = new SelectBox("albumName");
        albumName.setMultiple(false);
        Map mapAlbumNames = new SequencedHashMap();
        mapAlbumNames.put("", app.getMessage("medialib.label.pleaseSelect", "---Please Select---"));
        alist = mediaModule.getAlbumSelectList();
        if(alist.size() > 0) {
	        for(int i=0; i<alist.size(); i++) {
	            SelectListObject item = (SelectListObject) alist.get(i);
	            mapAlbumNames.put(item.getId(), item.getName());
	        }
        }
        albumName.setOptionMap(mapAlbumNames);
        addChild(albumName);
        
        // Media Name
        mediaName = new TextField("mediaName");
        mediaName.setMaxlength("255");
        mediaName.addChild(vne);
        addChild(mediaName);
        
        // Descriptions
        description = new TextBox("description");
        addChild(description);
        
        // Publishing Status
        approved = new Radio("approved");
        approved.setText(app.getMessage("medialib.label.publishingStatus.approved", "Approved and published"));
        approved.setGroupName("publishingStatus");
        
        notApproved = new Radio("notApproved");
        notApproved.setText(app.getMessage("medialib.label.publishingStatus.notApproved", "Pending approval"));
        notApproved.setGroupName("publishingStatus");
        
        publishingStatusPanel = new Panel("publishingStatusPanel");
        publishingStatusPanel.setColumns(1);
        publishingStatusPanel.addChild(approved);
        publishingStatusPanel.addChild(notApproved);
        addChild(publishingStatusPanel);
        
        // Submit and Cancel Buttons
        submit = new Button("submit");
        submit.setText(app.getMessage("medialib.label.submit", "Submit"));
        addChild(submit);
        
        cancel = new Button(Form.CANCEL_FORM_ACTION, "cancel");
        cancel.setText(app.getMessage("medialib.label.cancel", "Cancel"));
        cancel.setOnClick("return back()");
        addChild(cancel);
    }
    
    public void onRequest(Event evt) {
        init();
        
        if(! "".equals(mediaId)) {
            Application app = Application.getInstance();
            MediaModule mediaModule = (MediaModule) app.getModule(MediaModule.class);
            mediaObject = mediaModule.selectMedia(mediaId);
            
            libraryName.setSelectedOption(mediaObject.getLibraryId());
            selectedLibraryId = mediaObject.getLibraryId();
            albumName.setSelectedOption(mediaObject.getAlbumId());
            selectedAlbumId = mediaObject.getAlbumId();
            mediaName.setValue(mediaObject.getName());
            description.setValue(mediaObject.getDescription());
            size = mediaModule.getSize(mediaObject);
            if("Y".equals(mediaObject.getIsApproved())) {
                approved.setChecked(true);
            }
            else {
                notApproved.setChecked(true);
            }
            if(mediaModule.isAlbumManager(mediaObject.getAlbumId())) {
                isManager = "true";
            }
            else {
                isManager = "false";
            }
            
            // Update media statistics
            if(! mediaId.equals(accessedMediaId) && ! "".equals(ip)) {
                MediaStatObject mediaStat = new MediaStatObject();
                mediaStat.setId(UuidGenerator.getInstance().getUuid());
                mediaStat.setActionType("edit");
                mediaStat.setCreatedBy(getWidgetManager().getUser().getId());
                mediaStat.setIp(ip);
                mediaStat.setMediaId(mediaId);
                mediaModule.insertMediaStat(mediaStat);
                accessedMediaId = mediaId;
            }
        }
    }
    
    public Forward onValidate(Event evt) {
        boolean updateSuccess = true;
        boolean albumChanged = false;
        String previousAlbumId = selectedAlbumId;
        
        selectedLibraryId = (String) libraryName.getSelectedOptions().keySet().iterator().next();
        selectedAlbumId = (String) albumName.getSelectedOptions().keySet().iterator().next();
        if(! previousAlbumId.equals(selectedAlbumId)) {
            albumChanged = true;
        }
        mediaObject.setLibraryId(selectedLibraryId);
        mediaObject.setAlbumId(selectedAlbumId);
        mediaObject.setName((String) mediaName.getValue());
        mediaObject.setDescription((String) description.getValue());
        if(approved.isChecked()) {
            mediaObject.setIsApproved("Y");
        }
        else {
            mediaObject.setIsApproved("N");
        }
        
        Application app = Application.getInstance();
        MediaModule mediaModule = (MediaModule) app.getModule(MediaModule.class);
        if(albumChanged) {
            updateSuccess = mediaModule.updateMedia(mediaObject, previousAlbumId);
        }
        else {
            updateSuccess = mediaModule.updateMedia(mediaObject, null);
        }
        
        removeChildren();
        init();
        
        if(updateSuccess)
            return new Forward(FORWARD_SUCCESS);
        else
            return new Forward(FORWARD_FAILURE);
    }
    
    public Forward onSubmit(Event event) {
        Forward forward = super.onSubmit(event);
        String librarySelectedOption = (String) libraryName.getSelectedOptions().keySet().iterator().next();
        String albumSelectedOption = (String) albumName.getSelectedOptions().keySet().iterator().next();
        
        if("".equals(librarySelectedOption)) {
            libraryName.setInvalid(true);
            this.setInvalid(true);
        }
        
        if("".equals(albumSelectedOption)) {
            albumName.setInvalid(true);
            this.setInvalid(true);
        }
        
        return forward;
    }
    
    
    public SelectBox getAlbumName() {
        return albumName;
    }
    public SelectBox getLibraryName() {
        return libraryName;
    }
    public String getMediaId() {
        return mediaId;
    }
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
	public Radio getApproved() {
		return approved;
	}
	public Button getCancel() {
		return cancel;
	}
	public TextBox getDescription() {
		return description;
	}
	public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIsManager() {
        return isManager;
    }
    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }
    public MediaObject getMediaObject() {
        return mediaObject;
    }
	public TextField getMediaName() {
		return mediaName;
	}
	public Radio getNotApproved() {
		return notApproved;
	}
    public Panel getPublishingStatusPanel() {
        return publishingStatusPanel;
    }
	public String getSelectedAlbumId() {
		return selectedAlbumId;
	}
	public String getSelectedLibraryId() {
		return selectedLibraryId;
	}
    public String getSize() {
        return size;
    }
	public Button getSubmit() {
		return submit;
	}
}
