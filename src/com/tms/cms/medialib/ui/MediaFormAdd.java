/*
 * MediaForm
 * Date Created: Jun 17, 2005
 * Author: Tien Soon, Law
 * Description: Form for uploading of media files
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.medialib.model.MediaModule;
import com.tms.cms.medialib.model.MediaObject;
import com.tms.cms.medialib.model.SelectListObject;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;


public class MediaFormAdd extends Form {
    private Log log = Log.getLog(getClass());
    
    private SelectBox libraryName;
    private SelectBox albumName;
    private String selectedLibraryId = "";
    private String selectedAlbumId = "";
    private FileUpload[] fileUpload;
    private Button submit;
    private Button cancel;
    private static final String FORWARD_SUCCESS = "success";
    private static final String FORWARD_FAILURE = "failure";
    private static final int MAX_UPLOAD = 10;
    
    
    public int getMaxUpload() { return MAX_UPLOAD; }
    
    public String getDefaultTemplate() {
    	return "medialib/newMedia";
    }
    
    
    public void init() {
        setMethod("POST");
        setColumns(2);
        
        Application app = Application.getInstance();
        MediaModule mediaModule = (MediaModule) app.getModule(MediaModule.class);
        ArrayList alist;
        
        // Library List
        addChild(new Label("l1", app.getMessage("medialib.label.library*", "Library *")));
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
        addChild(new Label("l2", app.getMessage("medialib.label.album*", "Album *")));
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
        
        // File Uploads
        fileUpload = new FileUpload[MAX_UPLOAD];
        String mediaNumLabel;
        for(int i=1; i<=MAX_UPLOAD; i++) {
            String asteriskSign = "";
            if(i == 1) { 
                mediaNumLabel = app.getMessage("medialib.label.media", "media") + " " + i + " *";
                addChild(new Label("l" + i + 2, mediaNumLabel));
            }
            else {
                mediaNumLabel = app.getMessage("medialib.label.media", "media") + " " + i;
                addChild(new Label("l" + i + 2, mediaNumLabel));
            }
            
            fileUpload[i - 1] = new FileUpload("fileUpload" + i);
            
            ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", app.getMessage("medialib.message.mustUpload", "Please upload at least 1 media file"));
            if(i == 1) {
                fileUpload[0].addChild(vne);
            }
            addChild(fileUpload[i - 1]);
        }
        
        addChild(new Label("lDummy", ""));
        
        // Submit and Cancel Buttons
        submit = new Button("submit");
        submit.setText(app.getMessage("medialib.label.submit", "Submit"));
        
        cancel = new Button(Form.CANCEL_FORM_ACTION, "cancel");
        cancel.setText(app.getMessage("medialib.label.cancel", "Cancel"));
        cancel.setOnClick("return back()");
        
        Panel buttonsPanel = new Panel("buttonsPanel");
        buttonsPanel.addChild(submit);
        buttonsPanel.addChild(cancel);
        addChild(buttonsPanel);
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public Forward onValidate(Event evt) {
        // Populate sufficient data in a Media Object, and copy the file stream as well as updating db record
        populateMediaObject(evt);
        
        removeChildren();
        init();
        
        return new Forward(selectedAlbumId);
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
    
    protected void populateMediaObject(Event evt) {
        Application app = Application.getInstance();
        MediaModule module = (MediaModule) app.getModule(MediaModule.class);
        MediaObject mediaObject;
        int totalMediaUploaded = 0;
        selectedLibraryId = (String) libraryName.getSelectedOptions().keySet().iterator().next();
        selectedAlbumId = (String) albumName.getSelectedOptions().keySet().iterator().next();
        boolean isAlbumManager = module.isAlbumManager(selectedAlbumId);
        boolean isAutoApproved = module.isAutoApproved(selectedLibraryId);
        String uploadedMediaHtml = "<ul>";
        
        for (int i=0; i<MAX_UPLOAD; i++) {
            mediaObject = new MediaObject();
            try {
            	// set file properties
                StorageFile sf = fileUpload[i].getStorageFile(evt.getRequest());
            	
                //log.info(sf.getContentType());
                if (sf != null) {
                	
                	boolean allowMedia = false;
                	String allowedMediaTypes = Application.getInstance().getProperty("extraAllowedMediaTypes");
                	if (allowedMediaTypes != null) {
                		String allowedMediaTypesArray[] = allowedMediaTypes.split(",");
                		for (String allowedMediaType: allowedMediaTypesArray) {
                			if (sf.getName() != null && sf.getName().trim().endsWith(allowedMediaType)) {
                				allowMedia = true;
                				break;
                			}
                		}
                	}
                	
                    if((sf.getContentType().startsWith("image") ||
                            sf.getContentType().startsWith("audio") ||
                            sf.getContentType().startsWith("video") ||
                            sf.getName().toLowerCase().endsWith("mov") || 
                            sf.getName().toLowerCase().endsWith("swf")) &&
                            ! sf.getContentType().endsWith("bmp") || allowMedia) {
	                    String id = UuidGenerator.getInstance().getUuid();
	                    
	                    String fileName = sf.getName();
	                    String mediaName = fileName.substring(0, fileName.lastIndexOf("."));
	                    String fileExt = fileName.substring(fileName.lastIndexOf("."));
	                    
	                    // Modify the filename, with inclusive of mediaId to prevent collision
	                    String newFileName = mediaName + " " + id + fileExt;
	                    sf.setName(newFileName);
	                    
	                    mediaObject.setId(id);
	                    mediaObject.setStorageFile(sf);
	                    mediaObject.setFileName(newFileName);
	                    mediaObject.setFileSize(sf.getSize());
	                    mediaObject.setMediaType(sf.getContentType());
	                    if(mediaObject.getFileName().toLowerCase().endsWith("mov"))
	                        mediaObject.setMediaType("video/mov");
	                    mediaObject.setName(mediaName);
	                    mediaObject.setLibraryId(selectedLibraryId);
	                    mediaObject.setAlbumId(selectedAlbumId);
	                    
	                    //log.info("Media " + i + ": " + mediaObject.getName() + " - " + mediaObject.getMediaType());
	                    
	                    mediaObject.setCreatedBy(getWidgetManager().getUser().getId());
	                    boolean isSuccess = module.addMedia(mediaObject);
	                    if(isSuccess) {
	                        totalMediaUploaded++;
	                        
	                        uploadedMediaHtml += "<li><a href=/ekms/medialib/editMedia.jsp?id=" + mediaObject.getId() + ">" + mediaObject.getMediaType() + " - " + mediaObject.getName() + "</a></li>";
	                    }
                    }
                }
            }
            catch(Exception error) {
                log.error("Exception caught while pupulating MediaObject: The uploaded file might be corrupted : " + error);
            }
        }
        
        uploadedMediaHtml += "</ul>";
        if(totalMediaUploaded > 0 && ! isAlbumManager && ! isAutoApproved) {
            Collection managerIds = module.getManagerUserIds(selectedLibraryId);
            if(managerIds != null && managerIds.size() > 0)
                sendNotification(managerIds, uploadedMediaHtml);
        }
    }
    
    private void sendNotification(Collection managerIds, String uploadedMediaHtml) {
        try {
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Application app = Application.getInstance();
            MessagingModule mm;
            User user;
            SmtpAccount smtpAccount;
            Message message;

            mm = Util.getMessagingModule();
            user = getWidgetManager().getUser();
            smtpAccount = mm.getSmtpAccountByUserId(user.getId());

            // construct the message to send
            message = new Message();
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            
            IntranetAccount intranetAccount;

            // intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(getWidgetManager().getUser().getId());
            // message.setFrom(intranetAccount.getFromAddress());

            List memoList;
            memoList = new ArrayList(managerIds.size());
            for (Iterator i = managerIds.iterator(); i.hasNext();) {
                Map map = (Map) i.next();
                intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId((String) map.get("userId"));
                if(intranetAccount != null) {
		            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
		            memoList.add(add);
                }
            }

            if (memoList.size() > 0)
                message.setToIntranetList(memoList);

            message.setSubject(app.getMessage("medialib.label.mediaLibrary", "Corporate Media Library") + ": " + 
                    app.getMessage("medialib.label.newMediaPendingApproval", "New Media(s) Pending for Approval"));

            String notificationBody = "<p>User <i>" + user.getName() + "</i> has uploaded media file(s) to " + "<a href=/ekms/medialib/index.jsp>Corporate Media Library</a>" + ", and pending for approval. </p>"
            						+ uploadedMediaHtml
            						+ "<p><i>* Please note that the media name as shown in the above list might have already been editted by the contributor</i></p>";
            message.setBody(notificationBody);

            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());

            mm.sendMessage(smtpAccount, message, user.getId(), false);
        }
        catch (Exception e) {
            log.error("Error sending notification: ", e);
        }
    }
}
