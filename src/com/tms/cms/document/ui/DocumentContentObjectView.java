package com.tms.cms.document.ui;

import com.tms.cms.core.ui.ContentObjectView;

public class DocumentContentObjectView extends ContentObjectView {
    public DocumentContentObjectView() {
    }

    public DocumentContentObjectView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/documentContentObjectView";
    }
}
