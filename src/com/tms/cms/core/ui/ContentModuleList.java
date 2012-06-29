package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 30, 2003
 * Time: 11:44:01 PM
 * To change this template use Options | File Templates.
 */
public class ContentModuleList extends Widget {

    private Class[] contentModuleClasses;
    private String selectedClass;

    public ContentModuleList() {
    }

    public ContentModuleList(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentModuleList";
    }

    public void init() {
        super.init();
        ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
        contentModuleClasses = cm.getAllowedClasses(null);
    }

    public Class[] getContentModuleClasses() {
        return contentModuleClasses;
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public Forward actionPerformed(Event evt) {
        setSelectedClass(evt.getType());
        Forward forward = super.actionPerformed(evt);
        return forward;
    }
}
