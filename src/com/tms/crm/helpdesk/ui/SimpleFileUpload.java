package com.tms.crm.helpdesk.ui;

import kacang.Application;
import kacang.runtime.upload.MultipartRequest;
import kacang.runtime.upload.FormFile;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.FileUpload;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

public class SimpleFileUpload extends FileUpload {

    private String path;
    private boolean delete;
    private boolean readOnly;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getDeleteParameter() {
        return getAbsoluteName() + "_delete";
    }

    public SimpleFileUpload() {
        super();
    }

    public SimpleFileUpload(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "helpdesk/fileUpload";
    }

    public Forward onSubmit(Event evt) {
        String deleteParam = evt.getRequest().getParameter(getDeleteParameter());
        if (Boolean.valueOf(deleteParam).booleanValue()) {
            setDelete(true);
        }
        else {
            setDelete(false);
        }
        HttpServletRequest request = evt.getRequest();
        if (request instanceof MultipartRequest) {
            MultipartRequest multipartReq = (MultipartRequest) request;
            FormFile formFile = multipartReq.getFormFile(getAbsoluteName());
            if (formFile != null && formFile.getFileName().trim().length() > 0) {
                setValue(formFile.getFileName());
            }
            else {
                setValue(null);
            }
        }
        if (getValue() == null && !isDelete()) {
            Collection fileList = getFileList();
            if (fileList.size() > 0) {
                StorageFile sf = (StorageFile)fileList.iterator().next();
                setValue(sf.getName());
            }
        }
        performValidation(evt);
        return null;
    }

    public Collection getFileList() {
        Collection fileList = new ArrayList();
        String path = getPath();
        if (path != null && path.trim().length() > 0) {
            StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
            try {
                StorageDirectory sd = (StorageDirectory) ss.get(new StorageFile(path));
                fileList = sd.getFileListing();
            }
            catch (Exception e) {
                Log.getLog(getClass()).debug("Error retrieving file list for path: " + path, e);
            }
        }
        return fileList;
    }
}
