package com.tms.cms.portlet;

import com.tms.portlet.Entity;

public class DocumentPortlet extends ContentPortlet {
    public static final String DEFAULT_TEMPLATE = "cms/portlet/documentPortlet";

    public DocumentPortlet() {
    }

    public DocumentPortlet(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    protected String getPreferenceArticles(Entity entity) {
        return "0";
    }

    protected String getPreferenceDocuments(Entity entity) {
        return "1";
    }
}
