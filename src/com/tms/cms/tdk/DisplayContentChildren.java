package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class DisplayContentChildren extends LightWeightWidget {

    private Collection children;
    private String hideSummary;
    private String id;
    private String page;
    private String pageSize;
    private int pageCount = 1;
    private String types;

    public Collection getChildren() {
        return children;
    }

    public String getHideSummary() {
        return hideSummary;
    }

    public void setHideSummary(String hideSummary) {
        this.hideSummary = hideSummary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTypes()
    {
        return types;
    }

    public void setTypes(String types)
    {
        this.types = types;
        if("".equals(types))
            types = null;
    }

    public void onRequest(Event evt) {
        // get id
        String key = getId();
        if (key == null)
            key = evt.getRequest().getParameter("id");

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

        // retrieve content children
        try {
            User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
            Application application = Application.getInstance();
            ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
            String permission = (Boolean.valueOf(getHideSummary()).booleanValue()) ? ContentManager.USE_CASE_VIEW : null;
            String[] include = null;
            if(!(types == null || "".equals(types)))
            {
                Collection list = new ArrayList();
                StringTokenizer tokenizer = new StringTokenizer(types, ",");
                while(tokenizer.hasMoreTokens())
                    list.add(tokenizer.nextToken());
                include = (String[]) list.toArray(new String[] {});
            }
            Collection tmpChildren = cp.viewList(null, include, null, key, Boolean.FALSE, null, false, startIndex, pageSizeInt, permission, user.getId());

            // retrieve with content
            this.children = cp.viewListWithContents(tmpChildren);
            tmpChildren = null;
            
            // get total
            int total = cp.viewCount(null, include, null, key, Boolean.FALSE, null, user.getId());
            this.pageCount = (int)Math.ceil(((double)total) / pageSizeInt);
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayContentChildren";
    }
}
