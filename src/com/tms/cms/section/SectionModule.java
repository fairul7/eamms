package com.tms.cms.section;

import com.tms.cms.core.model.ContentModule;


/**
 * Section Module Handler.
 */
public class SectionModule extends ContentModule {
    public Class[] getContentObjectClasses() {
        return new Class[] { Section.class };
    }

    public String[] getAllowedClassNames(Class clazz) {
        return null;
    }
}
