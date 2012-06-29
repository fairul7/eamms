package com.tms.cms.page;

import com.tms.cms.core.model.ContentModule;

/**
 * Page Module Handler.
 */
public class PageModule extends ContentModule {
    public Class[] getContentObjectClasses() {
        return new Class[] { Page.class };
    }
}
