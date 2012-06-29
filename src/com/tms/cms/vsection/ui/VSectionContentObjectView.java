package com.tms.cms.vsection.ui;

import com.tms.cms.core.ui.ContentObjectView;
import com.tms.cms.vsection.VSection;
import com.tms.cms.vsection.VSectionModule;
import kacang.services.security.User;

import java.util.Collection;

public class VSectionContentObjectView extends ContentObjectView {
    public VSectionContentObjectView() {
    }

    public VSectionContentObjectView(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/vSectionContentObjectView";
    }

    public Collection getContentObjectList() {
        User user = getWidgetManager().getUser();
        VSection contentObject = (VSection)getContentObject();
        return VSectionModule.getContentObjectList(contentObject, user);
    }

}
