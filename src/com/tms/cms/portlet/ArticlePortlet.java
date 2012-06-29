package com.tms.cms.portlet;

import com.tms.portlet.Entity;

public class ArticlePortlet extends ContentPortlet {
    public static final String DEFAULT_TEMPLATE = "cms/portlet/articlePortlet";

    public ArticlePortlet() {
    }

    public ArticlePortlet(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    protected String getPreferenceArticles(Entity entity) {
        return "1";
    }

    protected String getPreferenceDocuments(Entity entity) {
        return "0";
    }
}
