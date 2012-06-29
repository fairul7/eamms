package com.tms.cms.vsection.ui;

import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.vsection.VSection;
import com.tms.cms.vsection.VSectionModule;
import kacang.services.security.User;

import java.util.Collection;

public class VSectionContentObjectPanel extends ContentObjectPanel {
    public VSectionContentObjectPanel() {
    }

    public VSectionContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/vSectionContentObjectPanel";
    }

    public Collection getContentObjectList() {
        User user = getWidgetManager().getUser();
        VSection contentObject = (VSection)getContentObject();
        return VSectionModule.getContentObjectList(contentObject, user);
    }

}
