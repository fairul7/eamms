package com.tms.cms.image;

import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.core.ui.ContentObjectView;
import com.tms.cms.document.ui.DocumentContentObjectPanel;
import com.tms.cms.image.ui.ImageContentObjectForm;
import com.tms.cms.image.ui.ImageContentObjectView;

/**
 * Image Module Handler.
 */
public class ImageModule extends ContentModule {

    public static String[] IMAGE_CONTENT_TYPES = new String[] {
        "image/gif",
        "image/jpeg",
        "image/pjpeg",
        "image/png",
        "image/x-png",
        "image/bmp",
        "image/pict",
        "image/tiff"
    };

    public Class[] getContentObjectClasses() {
        return new Class[] { Image.class };
    }

    public ContentObjectForm getContentObjectForm(Class clazz) {
        return new ImageContentObjectForm("contentObjectForm");
    }

    public ContentObjectView getContentObjectView(Class clazz) {
        return new ImageContentObjectView("contentObjectView");
    }

    public ContentObjectPanel getContentObjectPanel(Class clazz) {
        return new DocumentContentObjectPanel("contentObjectPanel");
    }
}
