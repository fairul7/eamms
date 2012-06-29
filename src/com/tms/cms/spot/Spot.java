package com.tms.cms.spot;

import com.tms.cms.core.model.ContentObject;

/**
 * Represents a Spot.
 */
public class Spot extends ContentObject {

    public Class getContentModuleClass() {
        return SpotModule.class;
    }

    public boolean isIndexable() {
        return false;
    }

}
