package com.tms.cms.bookmark;

import com.tms.cms.bookmark.ui.BookmarkContentObjectForm;
import com.tms.cms.bookmark.ui.BookmarkContentObjectPanel;
import com.tms.cms.bookmark.ui.BookmarkContentObjectView;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.core.ui.ContentObjectView;


/**
 * Bookmark Module Handler.
 */
public class BookmarkModule extends ContentModule {
    public Class[] getContentObjectClasses() {
        return new Class[] { Bookmark.class };
    }

    public ContentObjectForm getContentObjectForm(Class clazz) {
        return new BookmarkContentObjectForm("contentObjectForm");
    }

    public ContentObjectView getContentObjectView(Class clazz) {
        return new BookmarkContentObjectView("contentObjectView");
    }

    public ContentObjectPanel getContentObjectPanel(Class clazz) {
        return new BookmarkContentObjectPanel("contentObjectPanel");
    }

}
