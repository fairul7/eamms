package com.tms.cms.tdk;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import java.util.Collection;
import java.util.Iterator;

import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;

public class DisplayContentSubsections extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "cms/tdk/displaySubsections";

    private Collection subsections;
    private String id;
    private ContentObject currentObject;
    private boolean noHeader;
    private String hideSummary;
    private String orphans;
    private boolean showCount;

    public void onRequest(Event event)
    {
        if(!(id == null || "".equals(id)))
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
            try
            {
                User user = service.getCurrentUser(event.getRequest());
                String[] classes = new String[] {Section.class.getName(), VSection.class.getName()};
                String permission = (Boolean.valueOf(getHideSummary()).booleanValue()) ? ContentManager.USE_CASE_VIEW : null;
                currentObject = publisher.view(id, user);

                if (!Boolean.valueOf(getOrphans()).booleanValue()) {
                    subsections = publisher.viewList(null, classes, null, id, Boolean.FALSE, "name", false, 0, -1, permission, user.getId());
                }
                else {
                    ContentObject tmpRoot = publisher.viewTreeWithOrphans(id, classes, permission, user.getId());
                    subsections = tmpRoot.getChildren();
                }

                // get child count
                if (showCount) {
                    for (Iterator i=subsections.iterator(); i.hasNext();) {
                        ContentObject co = (ContentObject)i.next();
                        int childCount = publisher.viewCount(null, null, null, co.getId(), Boolean.FALSE, permission, user.getId());
                        co.setProperty("childCount", new Integer(childCount).toString());
                    }
                }

            }
            catch (DataObjectNotFoundException e)
            {
                Log.getLog(DisplayContentSubsections.class).error(e.getMessage(), e);
            }
            catch (ContentException e)
            {
                Log.getLog(DisplayContentSubsections.class).error(e.getMessage(), e);
            }
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public boolean isNoHeader()
    {
        return noHeader;
    }

    public void setNoHeader(boolean noHeader)
    {
        this.noHeader = noHeader;
    }

    public String getOrphans()
    {
        return orphans;
    }

    public void setOrphans(String orphans)
    {
        this.orphans = orphans;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Collection getSubsections()
    {
        return subsections;
    }

    public void setSubsections(Collection subsections)
    {
        this.subsections = subsections;
    }

    public ContentObject getCurrentObject()
    {
        return currentObject;
    }

    public void setCurrentObject(ContentObject currentObject)
    {
        this.currentObject = currentObject;
    }

    public boolean isShowCount()
    {
        return showCount;
    }

    public void setShowCount(boolean showCount)
    {
        this.showCount = showCount;
    }

    public String getHideSummary()
    {
        return hideSummary;
    }

    public void setHideSummary(String hideSummary)
    {
        this.hideSummary = hideSummary;
    }

}
