package com.tms.cms.comment.ui;

import com.tms.cms.comment.Commentary;
import com.tms.cms.core.ui.ContentObjectPanel;

public class CommentContentObjectPanel extends ContentObjectPanel {
    public CommentContentObjectPanel() {
    }

    public CommentContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        if (Commentary.class.equals(getContentObject().getClass())) {
            return "cms/admin/commentaryContentObjectPanel";
        }
        else {
            return "cms/admin/commentContentObjectPanel";
        }
    }
}
