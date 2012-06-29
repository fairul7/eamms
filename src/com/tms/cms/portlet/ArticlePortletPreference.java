package com.tms.cms.portlet;

import kacang.stdui.*;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.PortletException;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;
import com.tms.cms.portlet.ArticlePortlet;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ArticlePortletPreference extends ContentPortletPreference
{
    public static final String DEFAULT_TEMPLATE = "cms/portlet/articlePortletPreference";

    public ArticlePortletPreference() {
    }

    public ArticlePortletPreference(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }
}
