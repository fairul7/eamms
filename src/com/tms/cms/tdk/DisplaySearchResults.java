package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.ArrayList;
import java.util.Collection;

public class DisplaySearchResults extends LightWeightWidget {

    private String query;
    private String sort;
    private Collection results = new ArrayList();
    private String page;
    private String pageSize;
    private int pageCount = 1;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Collection getResults() {
        return results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void onRequest(Event evt) {

        if (getQuery() == null) {
            query = evt.getRequest().getParameter("query");
        }

        if (query != null && query.trim().length() > 0) {
            // get paging info
            int pageInt = 1;
            if (page == null) {
                page = evt.getRequest().getParameter("page");
            }
            try {
                pageInt = Integer.parseInt(page);
                if (pageInt < 0)
                    pageInt = 1;
            }
            catch (Exception e) {
                pageInt = 1;
            }
            page = new Integer(pageInt).toString();
            int pageSizeInt = 20;
            if (pageSize == null) {
                pageSize = evt.getRequest().getParameter("pageSize");
            }
            try {
                pageSizeInt = Integer.parseInt(pageSize);
                if (pageSizeInt < 0)
                    pageSizeInt = -1;
            }
            catch (Exception e) {
                pageSizeInt = 20;
            }

            // calc start index
            int startIndex = (pageInt-1) * pageSizeInt;

            // perform content search
            try {
                User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
                Application application = Application.getInstance();
                ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                this.results = cp.search(getQuery(), getSort(), startIndex, pageSizeInt, user);

                // get total
                int total = cp.searchCount(query, user);
                this.pageCount = (int)Math.ceil(((double)total) / pageSizeInt);
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
    }

    public String getDefaultTemplate() {
        return "cms/tdk/displaySearchResults";
    }
}
