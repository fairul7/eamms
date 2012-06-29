package com.tms.cms.comment.ui;

import com.tms.cms.comment.Commentary;
import com.tms.cms.core.ui.ContentObjectView;

public class CommentContentObjectView extends ContentObjectView {
    public CommentContentObjectView() {
    }

    public CommentContentObjectView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        if (Commentary.class.equals(getContentObject().getClass())) {
            return "cms/admin/commentaryContentObjectView";
        }
        else {
            return "cms/admin/commentContentObjectView";
        }
    }
}
