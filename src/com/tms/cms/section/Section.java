package com.tms.cms.section;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.profile.model.ProfiledContentParent;


/**
 * Represents a Section.
 */
public class Section extends ContentObject implements ProfiledContentParent {

    public Class getContentModuleClass() {
        return SectionModule.class;
    }

}
