package com.tms.cms.document;

import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.core.ui.ContentObjectView;
import com.tms.cms.document.ui.DocumentContentObjectForm;
import com.tms.cms.document.ui.DocumentContentObjectPanel;
import com.tms.cms.document.ui.DocumentContentObjectView;
import kacang.services.indexing.*;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.Application;
import kacang.util.Log;

import java.util.*;
import java.text.SimpleDateFormat;


/**
 * Document Module Handler.
 */
public class DocumentModule extends ContentModule implements SearchableModule {
    public Class[] getContentObjectClasses() {
        return new Class[]{Document.class};
    }

    public ContentObjectForm getContentObjectForm(Class clazz) {
        return new DocumentContentObjectForm("contentObjectForm");
    }

    public ContentObjectView getContentObjectView(Class clazz) {
        return new DocumentContentObjectView("contentObjectView");
    }

    public ContentObjectPanel getContentObjectPanel(Class clazz) {
        return new DocumentContentObjectPanel("contentObjectPanel");
    }

//-- Implementation for SearchableModule

    protected String[] getSearchClasses() {
        return new String[]{
            "com.tms.cms.document.Document",
            "com.tms.cms.image.Image",
        };
    }

    public SearchResult search(String query, int start, int rows, String userId) throws QueryException {
        return search(query, start, rows, userId, null, null);
    }

    public SearchResult searchFullText(String query, int start, int rows, String userId) throws QueryException {
        return searchFullText(query, start, rows, userId, null, null);
    }

    public boolean isSearchSupported() {
        return true;
    }

    public boolean isFullTextSearchSupported() {
        return true;
    }

    public SearchResult search(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException {
        SearchResult sr = new SearchResult();
        try {
            Application application = Application.getInstance();
            ContentPublisher publisher = (ContentPublisher) application.getModule(ContentPublisher.class);
            Collection contentList = publisher.viewList(null, getSearchClasses(), startDate, endDate, query, null, Boolean.FALSE, "date", true, start, rows, ContentManager.USE_CASE_VIEW, userId);
            // retrieve with content
            contentList = publisher.viewListWithContents(contentList);
            for (Iterator i = contentList.iterator(); i.hasNext();) {
                ContentObject c = (ContentObject) i.next();
                SearchResultItem item = new SearchResultItem();
                String title = c.getName();
                String body = ContentPublisher.formatSearchSummary(c.getSummary());
                Map valueMap = new HashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, body);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DATE, c.getDate());
                item.setValueMap(valueMap);
                sr.add(item);
            }
            // retrieve count
            int count = publisher.viewCount(null, getSearchClasses(), startDate, endDate, query, null, Boolean.FALSE, ContentManager.USE_CASE_VIEW, userId);
            sr.setTotalSize(count);
            return sr;
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }

    /**
     * Search with date range. Note that both startDate AND endDate must be
     * specified for the date range search to take place.
     *
     * @param query
     * @param start
     * @param rows
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     * @throws QueryException
     */
    public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException {
        SearchResult sr = new SearchResult();
        try {
            Application application = Application.getInstance();
            ContentPublisher publisher = (ContentPublisher) application.getModule(ContentPublisher.class);
            SecurityService sec = (SecurityService) application.getService(SecurityService.class);
            User user = sec.getUser(userId);

            // form query
            String[] cs = getSearchClasses();
            if (cs != null) {
                StringBuffer qb = new StringBuffer();
                qb.append("(" + query + ") AND (");
                for (int i = 0; i < cs.length; i++) {
                    if (i > 0) {
                        qb.append(" OR ");
                    }
                    qb.append("className:" + cs[i]);
                }
                qb.append(")");

                // include startDate/endDate only if BOTH are specified
                if (startDate != null && endDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(IndexingThread.DATE_FORMAT);
                    qb.append(" AND (date:[");
                    String date = sdf.format(startDate);
                    qb.append(date);
                    qb.append(" TO ");
                    date = sdf.format(endDate);
                    qb.append(date);
                    qb.append("]) ");
                }

                query = qb.toString();
            }

            // perform search
            Collection contentList = publisher.search(query, null, start, rows, user);
            for (Iterator i = contentList.iterator(); i.hasNext();) {
                ContentObject c = (ContentObject) i.next();
                SearchResultItem item = new SearchResultItem();
                String title = c.getName();
                String body = ContentPublisher.formatSearchSummary(c.getSummary());
                Map valueMap = new HashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, body);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DATE, c.getDate());
                item.setValueMap(valueMap);
                sr.add(item);
            }
            int count = publisher.searchCount(query, user);
            sr.setTotalSize(count);
            return sr;
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }

    }

}
