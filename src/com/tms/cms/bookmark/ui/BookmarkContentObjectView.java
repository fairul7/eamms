package com.tms.cms.bookmark.ui;

import com.tms.cms.core.ui.ContentObjectView;

/**
 * Displays a bookmark.
 */
public class BookmarkContentObjectView extends ContentObjectView {
    public BookmarkContentObjectView() {
    }

    public BookmarkContentObjectView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/bookmarkContentObjectView";
    }
}
