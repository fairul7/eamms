package com.tms.cms.article;

import com.tms.cms.article.filter.ArticleFilter;
import com.tms.cms.article.filter.ArticleFilterFactory;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.profile.model.ProfiledContent;
import com.tms.cms.profile.model.ContentProfileModule;
import org.apache.commons.collections.SequencedHashMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import kacang.Application;
import kacang.util.Log;


/**
 * Represents an Article.
 */
public class Article extends ContentObject implements ProfiledContent {

    public static final String COMMENT_PAGE_BREAK = "<!-- PageBreak -->";

    public Class getContentModuleClass() {
        return ArticleModule.class;
    }

    public String getIndexableContent() {
        StringBuffer buffer = new StringBuffer(getName());
        if (getDescription() != null) {
            buffer.append("\n");
            buffer.append(getDescription());
        }
        if (getSummary() != null) {
            buffer.append("\n");
            buffer.append(getSummary());
        }
        if (getContents() != null) {
            buffer.append("\n");
            buffer.append(getContents());
        }

        // handle profiled content
        Application app = Application.getInstance();
        if (ContentProfileModule.isProfileEnabled()) {
            try {
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                buffer.append("\n");
                buffer.append(profileMod.getIndexableProfileData(getId(), getVersion()));
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Unable to retrieve profile data for article " + getId());
            }
        }

        return buffer.toString();
    }

    public Map getPageMap() {
        Map pageMap = new SequencedHashMap();

        String contents = getContents();
        if (contents == null || contents.indexOf(COMMENT_PAGE_BREAK) <= 0) {
        }
        else {
            int page = 1;
            int base = 0;
            int c = contents.indexOf(COMMENT_PAGE_BREAK);
            String pageContents;
            while(c > 0) {
                pageContents = contents.substring(base, c);
                pageMap.put(new Integer(page).toString(), pageContents);
                page++;
                base = c + COMMENT_PAGE_BREAK.length();
                c = contents.indexOf(COMMENT_PAGE_BREAK, base);
            }
            pageContents = contents.substring(base, contents.length());
            pageMap.put(new Integer(page).toString(), pageContents);
        }
        return pageMap;
    }

    /**
     *
     * @param request
     * @param contents
     * @return
     */
    public String getFilteredContent(HttpServletRequest request, String contents) {
        if (request == null || contents == null) {
            return null;
        }

        // get filters
        ArticleFilter[] filters = ArticleFilterFactory.getArticleFilters();

        // perform filtering
        for (int i=0; i<filters.length; i++) {
            contents = filters[i].filter(request, contents);
        }
        return contents;
    }

}
