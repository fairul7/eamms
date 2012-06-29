package com.tms.cms.image.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.image.Image;
import com.tms.cms.image.ImageModule;
import kacang.Application;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.stdui.FileUpload;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Arrays;

/**
 * Listener to handle image uploads when editing content.
 */
public class ImageSelectorFormListener extends FormEventAdapter {

    public Forward onValidate(Event evt) {
        try {
            String id = ContentHelper.getId(evt);

            ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
            StorageFile sf = ((FileUpload) evt.getWidget().getChild("FileUpload")).getStorageFile(evt.getRequest());
            if (sf != null) {
                // check for image content type
                if (Arrays.asList(ImageModule.IMAGE_CONTENT_TYPES).contains(sf.getContentType())) {
                    Image image = new Image();
                    image.setName(sf.getName());
                    image.setParentId(id);
                    image.setStorageFile(sf);
                    image.setFileName(sf.getName());
                    image.setFilePath(sf.getAbsolutePath());
                    image.setFileSize(sf.getSize());
                    image.setContentType(sf.getContentType());
                    User user = evt.getWidgetManager().getUser();
                    ContentObject result = cm.createNew(image, user);
                    result = cm.submitForApproval(result.getId(), user);
                    cm.approve(result, user);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return null;
    }

}