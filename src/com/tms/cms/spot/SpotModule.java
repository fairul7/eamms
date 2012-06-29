package com.tms.cms.spot;

import com.tms.cms.core.model.ContentModule;

/**
 * Spot Module Handler.
 */
public class SpotModule extends ContentModule {
    public Class[] getContentObjectClasses() {
        return new Class[] { Spot.class };
    }
}
