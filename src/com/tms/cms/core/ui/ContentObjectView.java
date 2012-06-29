package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentObject;
import kacang.stdui.Panel;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class ContentObjectView extends Panel {

    private ContentObject contentObject;

    public ContentObjectView() {
    }

    public ContentObjectView(String name) {
        super(name);
    }

    public ContentObject getContentObject() {
        return contentObject;
    }

    public void setContentObject(ContentObject contentObject) {
        this.contentObject = contentObject;
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentObjectView";
    }

}
