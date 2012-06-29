package com.tms.cms.document.ui;

import com.tms.cms.core.ui.ContentObjectPanel;

public class DocumentContentObjectPanel extends ContentObjectPanel {
    public DocumentContentObjectPanel() {
    }

    public DocumentContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/documentContentObjectPanel";
    }
}
