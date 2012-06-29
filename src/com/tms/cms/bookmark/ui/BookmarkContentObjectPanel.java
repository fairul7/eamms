package com.tms.cms.bookmark.ui;

import com.tms.cms.core.ui.ContentObjectPanel;

/**
 * Panel to display a bookmark.
 */
public class BookmarkContentObjectPanel extends ContentObjectPanel {
    public BookmarkContentObjectPanel() {
    }

    public BookmarkContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/bookmarkContentObjectPanel";
    }
}
