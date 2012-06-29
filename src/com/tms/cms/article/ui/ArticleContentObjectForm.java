package com.tms.cms.article.ui;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentUtil;
import com.tms.cms.core.ui.ContentObjectForm;
import kacang.stdui.RichTextBox;
import kacang.stdui.TextBox;
import kacang.ui.Event;

/**
 * Form for adding/editing an article.
 */
public class ArticleContentObjectForm extends ContentObjectForm {

    private TextBox contentsBox;

    public ArticleContentObjectForm() {
    }

    public ArticleContentObjectForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/articleContentObjectForm";
    }

    public void init(Event evt) {
        super.init(evt);

        contentsBox = new RichTextBox("contentsBox");
        contentsBox.setCols("60");
        contentsBox.setRows("40");
        if (getContentObject() != null && getContentObject().getId() != null) {
            ((RichTextBox)contentsBox).setImageUrl(evt.getRequest().getContextPath() + "/cmsadmin/content/imageSelectorNew.jsp");
        }
/*
        else {
            ((RichTextBox)contentsBox).setImageUrl(evt.getRequest().getContextPath() + "/cmsadmin/content/imageSelectorFrameNew.jsp");
        }
*/
        ((RichTextBox)contentsBox).setLinkUrl(evt.getRequest().getContextPath() + "/cmsadmin/content/linkSelectorNew.jsp");
        addChild(contentsBox);

    }

    protected void populateFields(Event evt) {
        super.populateFields(evt);
        // populate values?
        ContentObject contentObject = getContentObject();
        if (contentObject != null) {
            contentsBox.setValue(ContentUtil.makeAbsolute(evt.getRequest(), contentObject.getContents()));
        }
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        super.populateContentObject(evt, contentObject);
        if (contentsBox.getValue() != null) {
            contentObject.setContents(ContentUtil.makeRelative(evt.getRequest(), contentsBox.getValue().toString()));
        }
    }

}
