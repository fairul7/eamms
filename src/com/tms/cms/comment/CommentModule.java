package com.tms.cms.comment;

import com.tms.cms.comment.ui.CommentContentObjectForm;
import com.tms.cms.comment.ui.CommentContentObjectPanel;
import com.tms.cms.comment.ui.CommentContentObjectView;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.core.ui.ContentObjectView;


/**
 * Module Handler for the Comment Module. Handles both Comment and Commentary objects.
 */
public class CommentModule extends ContentModule {
    public Class[] getContentObjectClasses() {
        return new Class[] { Commentary.class, Comment.class };
    }

    public ContentObjectForm getContentObjectForm(Class clazz) {
        return new CommentContentObjectForm("contentObjectForm");
    }

    public ContentObjectView getContentObjectView(Class clazz) {
        return new CommentContentObjectView("contentObjectView");
    }

    public ContentObjectPanel getContentObjectPanel(Class clazz) {
        return new CommentContentObjectPanel("contentObjectPanel");
    }

}
