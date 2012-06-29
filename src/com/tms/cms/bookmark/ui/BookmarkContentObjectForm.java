package com.tms.cms.bookmark.ui;

import com.tms.cms.bookmark.Bookmark;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import kacang.stdui.TextField;
import kacang.ui.Event;

/**
 * Form for adding/editing a bookmark.
 */
public class BookmarkContentObjectForm extends ContentObjectForm {

    private TextField urlField;
    private TextField targetField;

    public BookmarkContentObjectForm() {
    }

    public BookmarkContentObjectForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/bookmarkContentObjectForm";
    }

    public void init(Event evt) {
        super.init(evt);

        urlField = new TextField("urlField");
        addChild(urlField);

        targetField = new TextField("targetField");
        addChild(targetField);
    }

    protected void populateFields(Event evt) {
        super.populateFields(evt);
        // populate values?
        Bookmark bookmark = (Bookmark)getContentObject();
        if (bookmark != null) {
            urlField.setValue(bookmark.getUrl());
            targetField.setValue(bookmark.getTarget());
        }
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        super.populateContentObject(evt, contentObject);
        Bookmark bookmark = (Bookmark)getContentObject();
        if (urlField.getValue() != null) {
            bookmark.setUrl(urlField.getValue().toString());
        }
        if (targetField.getValue() != null) {
            bookmark.setTarget(targetField.getValue().toString());
        }
    }

}
