package com.tms.cms.core.model;

import kacang.util.Log;
import kacang.Application;

public class ContentReporter extends ContentAuditor {

    private Log log = Log.getLog(getClass());

    public boolean isDisabled() {
        return Boolean.valueOf(Application.getInstance().getProperty(ContentManager.APPLICATION_PROPERTY_STATISTICS_DISABLED)).booleanValue();
    }

}
