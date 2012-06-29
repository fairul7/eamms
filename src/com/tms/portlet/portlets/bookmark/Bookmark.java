package com.tms.portlet.portlets.bookmark;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.theme.ThemeManager;

import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 29, 2004
 * Time: 1:40:07 PM
 * To change this template use Options | File Templates.
 */
public class Bookmark extends LightWeightWidget
{
    public static final String BOOKMARK_DELIMITER = ":[bookmark-label]:";
    public static final String PREFERENCE_LINKS = "links";
    public static final String DEFAULT_TEMPLATE = "portal/portlets/bookmark";

    private Map links;

    public void onRequest(Event event)
    {
        Entity entity = (Entity) event.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);
        PortletPreference preferenceLinks = entity.getPreference(PREFERENCE_LINKS);
        String preferenceValue = "";
        if(preferenceLinks != null)
            preferenceValue = preferenceLinks.getPreferenceValue();
        links = Bookmark.getLinks(preferenceValue);
        super.onRequest(event);
    }

    public static Map getLinks(String value)
    {
        Map map = new SequencedHashMap();
        StringTokenizer tokenizer = new StringTokenizer(value, ";");
        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();
            int position = token.indexOf(BOOKMARK_DELIMITER);
            if(position != 0)
            {
                String link = token.substring(0, token.indexOf(BOOKMARK_DELIMITER));
                String label = token.substring(token.indexOf(BOOKMARK_DELIMITER) + BOOKMARK_DELIMITER.length(), token.length());
                map.put(link, label);
            }
        }
        return map;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public Map getLinks()
    {
        return links;
    }

    public void setLinks(Map links)
    {
        this.links = links;
    }
}
