package com.tms.cms.image.ui;

import com.tms.cms.core.ui.ContentObjectView;

public class ImageContentObjectView extends ContentObjectView {
    public ImageContentObjectView() {
    }

    public ImageContentObjectView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/imageContentObjectView";
    }
}
