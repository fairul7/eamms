package com.tms.collab.taskmanager.ui;

import kacang.stdui.Form;
import kacang.stdui.FileUpload;
import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.Application;
import com.tms.collab.taskmanager.model.TaskManager;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 22, 2003
 * Time: 6:05:24 PM
 * To change this template use Options | File Templates.
 */
public class FileUploadForm extends Form {
    private FileUpload fileUpload;
    private Button uploadButton;
    private String folderId;
    private String folderIdPrefix;
    private String backUrl;
    private FileListing filesListing;
    private String generatedId;


    public FileUploadForm() {
    }

    public FileUploadForm(String name) {
        super(name);
    }

    public void init() {
        super.init();
        fileUpload = new FileUpload("fileupload");
        uploadButton = new Button("uploadbutton", Application.getInstance().getMessage("taskmanager.label.Upload","Upload"));
        filesListing = new FileListing("filelisting");
        filesListing.setDeleteable(true);
        if (folderId != null && folderId.trim().length() > 0) {
            filesListing.setFolderId(folderId);
            filesListing.refresh();
        }
        addChild(uploadButton);
        addChild(fileUpload);
        addChild(filesListing);
    }

    public String getDefaultTemplate() {
        return "taskmanager/fileuploadform";
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        if (folderId != null && folderId.trim().length() > 0)
            filesListing.setFolderId(folderId);
    }

    public Forward onValidate(Event evt) {
        String buttonClicked = findButtonClicked(evt);
        if (uploadButton.getAbsoluteName().equals(buttonClicked)) {
            if (folderId == null || folderId.trim().length() < 1) {
                generatedId = UuidGenerator.getInstance().getUuid();
                folderId = (folderIdPrefix == null ? "" : folderIdPrefix + "_") + generatedId;
            }
            try {
                StorageFile file = fileUpload.getStorageFile(evt.getRequest());
                if (file != null) {
                    StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
                    file.setParentDirectoryPath(folderId);
                    ss.store(file);
                    filesListing.setFolderId(folderId);
                    filesListing.refresh();
                    return null;
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e);
            }
        }
        return super.onValidate(evt);
    }

    /* public Forward onSubmit(Event evt)
     {

         if(getParent() instanceof Form)
            ((Form)getParent()).onSubmit(evt);
 //        return
         return super.onSubmit(evt); //null;
     }*/

    public Forward actionPerformed(Event evt) {
        return super.actionPerformed(evt);
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Button getUploadButton() {
        return uploadButton;
    }

    public void setUploadButton(Button uploadButton) {
        this.uploadButton = uploadButton;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
        filesListing.setFolderId(folderId);
        filesListing.refresh();
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public FileListing getFilesListing() {
        return filesListing;
    }

    public void setFilesListing(FileListing filesListing) {
        this.filesListing = filesListing;
    }

    public String getFolderIdPrefix() {
        return folderIdPrefix;
    }

    public void setFolderIdPrefix(String folderIdPrefix) {
        this.folderIdPrefix = folderIdPrefix;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }
}
