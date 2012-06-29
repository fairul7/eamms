package com.tms.cms.core.model;

import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.core.ui.ContentObjectView;
import kacang.model.DefaultModule;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 19, 2003
 * Time: 5:05:06 PM
 * To change this template use Options | File Templates.
 */
public abstract class ContentModule extends DefaultModule {

    private String name;
    private String description;

    public abstract Class[] getContentObjectClasses();

    public String[] getAllowedClassNames(Class clazz) {
        return new String[0];
    }

    /**
     * Returns the appropriate CpntentObjectForm widget to add/edit this ContentObject
     * @return
     */
    public ContentObjectForm getContentObjectForm(Class clazz) {
        return new ContentObjectForm("form");
    }

    /**
     * Returns the appropriate ContentObjectPanel widget to display content meta information
     * @return
     */
    public ContentObjectPanel getContentObjectPanel(Class clazz) {
        return new ContentObjectPanel("panel");
    }

    /**
     * Returns the appropriate ContentObjectView widget to display the object's full content
     * @return
     */
    public ContentObjectView getContentObjectView(Class clazz) {
        return new ContentObjectView("view");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
