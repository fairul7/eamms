package com.tms.cms.portlet;

import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.theme.ThemeManager;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentReporter;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;
import com.tms.cms.article.Article;
import com.tms.cms.document.Document;
import com.tms.cms.image.Image;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Dec 15, 2003
 * Time: 9:58:28 AM
 * To change this template use Options | File Templates.
 */
public class ContentPortlet extends Widget
{
    public static final String PREFERENCE_SECTIONS = "sections";
    public static final String PREFERENCE_LISTING = "listing";
    public static final String PREFERENCE_ARTICLE = "article";
    public static final String PREFERENCE_DOCUMENT = "document";

    public static final String DEFAULT_LISTING = "5";
    public static final String DEFAULT_TEMPLATE = "cms/portlet/contentPortlet";
    public static final String DEFAULT_DELIMETER = ",";

    public static final String EVENT_CLICK = "content_click";
    public static final String FORWARD_CLICK = "content_click";
    public static final String KEY_ID = "id";

    private Map articles;
    private Map articleCount;

    public ContentPortlet()
    {
    }

    public ContentPortlet(String name)
    {
        super(name);
    }

    public void init()
    {
        articles = new SequencedHashMap();
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = null;
        if(EVENT_CLICK.equals(evt.getType()))
            forward =  new Forward(FORWARD_CLICK);
        return forward;
    }

    public void onRequest(Event evt)
    {
        articles = new SequencedHashMap();
        articleCount = new HashMap();

        Entity entity = (Entity) evt.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);

        PortletPreference preferenceSection = new PortletPreference();
        PortletPreference preferenceListing = new PortletPreference();

        ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        ContentReporter reporter = (ContentReporter) Application.getInstance().getModule(ContentReporter.class);
        User user = getWidgetManager().getUser();

        if(entity.getPreferences().containsKey(PREFERENCE_SECTIONS))
            preferenceSection = (PortletPreference) entity.getPreferences().get(PREFERENCE_SECTIONS);
        else
            preferenceSection.setPreferenceValue("");
        if(entity.getPreferences().containsKey(PREFERENCE_LISTING))
            preferenceListing = (PortletPreference) entity.getPreferences().get(PREFERENCE_LISTING);
        else
            preferenceListing.setPreferenceValue(DEFAULT_LISTING);

        try
        {
            // retrieve sections
            StringTokenizer tokenizer = new StringTokenizer(preferenceSection.getPreferenceValue(), ",");
            Collection idList = new ArrayList();
            while(tokenizer.hasMoreTokens())
            {
                String sectionId = tokenizer.nextToken();
                idList.add(sectionId);
            }
            String[] idArray = (String[])idList.toArray(new String[0]);

            if (idArray != null && idArray.length > 0)
            {
                Collection sections = publisher.viewList(idArray, new String[] {Section.class.getName(), VSection.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, ContentManager.USE_CASE_VIEW, user.getId());

                // determine preferences
                Collection types = new ArrayList();
                String preferenceArticles = getPreferenceArticles(entity);
                String preferenceDocuments = getPreferenceDocuments(entity);
                if("1".equals(preferenceArticles) || "1".equals(preferenceDocuments))
                {
                    if("1".equals(preferenceArticles))
                        types.add(Article.class.getName());
                    if("1".equals(preferenceDocuments)) {
                        types.add(Document.class.getName());
                        types.add(Image.class.getName());
                    }
                }
                String[] typeArray = (String[])types.toArray(new String[0]);

                for (Iterator it=sections.iterator(); it.hasNext();)
                {
                    ContentObject section = (ContentObject)it.next();
                    String sectionId = section.getId();
                    Collection list = publisher.viewList(null, typeArray, null, sectionId, Boolean.FALSE, "date", true, 0, Integer.parseInt(preferenceListing.getPreferenceValue()), ContentManager.USE_CASE_VIEW, user.getId());
                    articles.put(section, list);
                    for(Iterator i = list.iterator(); i.hasNext();)
                    {
                        ContentObject article = (ContentObject) i.next();
                        articleCount.put(article.getId(), new Integer(reporter.viewAuditCount(article.getId(), null, null)));
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log.getLog(ContentPortlet.class).error(e);
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    protected String getPreferenceArticles(Entity entity) {
        if(entity.getPreferences().containsKey(PREFERENCE_ARTICLE))
            return ((PortletPreference) entity.getPreferences().get(PREFERENCE_ARTICLE)).getPreferenceValue();
        else
            return "0";
    }

    protected String getPreferenceDocuments(Entity entity) {
        if(entity.getPreferences().containsKey(PREFERENCE_DOCUMENT))
            return ((PortletPreference) entity.getPreferences().get(PREFERENCE_DOCUMENT)).getPreferenceValue();
        else
            return "0";
    }

    public Map getArticles()
    {
        return articles;
    }

    public void setArticles(Map articles)
    {
        this.articles = articles;
    }

    public Map getArticleCount()
    {
        return articleCount;
    }

    public void setArticleCount(Map articleCount)
    {
        this.articleCount = articleCount;
    }
}
