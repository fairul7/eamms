package com.tms.ekms.search.ui;

import kacang.Application;
import kacang.model.Module;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchableModule;
import kacang.services.indexing.QueryException;
import kacang.services.security.SecurityService;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

public class SearchWidget extends LightWeightWidget {

    public static final String EVENT_SEARCH_RESULT = "searchResult";
    public static final String LABEL_SEARCH_ONLY = "searchOnly";
    public static final String LABEL_FULLTEXT_ONLY = "fullTextOnly";
    public static final String LABEL_ALL_SEARCHES = "allSearches";
    public static final String LABEL_SEARCH_TYPE = "_searchType";

    private String query;
    private String page;
    private String pageSize;
    private int pageCount = 1;
    private String summaryPageSize;
    private Map selectedSearchType;
    private String[] modules;
    private String userId;
    private String selectedModule;
    private String searchType;

    public String getDefaultTemplate() {
        if (selectedModule != null && selectedModule.trim().length() > 0) {
            return "search/searchResults";
        }
        else {
            return "search/searchResultsSummary";
        }
    }

    public void onRequest(Event evt) {

        // get query
        if (getQuery() == null) {
           // query = evt.getRequest().getParameter("query");

            if(evt.getRequest().getParameter("query") == null)
            query = "";
            else
            query = evt.getRequest().getParameter("query").trim();

        }

        // get paging info
        if (query != null && query.trim().length() > 0) {
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
            pageSize = new Integer(pageSizeInt).toString();
            int summaryPageSizeInt = 5;
            if (summaryPageSize == null) {
                summaryPageSize = evt.getRequest().getParameter("summaryPageSize");
            }
            try {
                summaryPageSizeInt = Integer.parseInt(summaryPageSize);
                if (summaryPageSizeInt < 0)
                    summaryPageSizeInt = -1;
            }
            catch (Exception e) {
                summaryPageSizeInt = 5;
            }
            summaryPageSize = new Integer(summaryPageSizeInt).toString();
        }

        selectedSearchType = new HashMap();

        // get module
        if (getSelectedModule() == null)
            selectedModule = evt.getRequest().getParameter(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS);

        //get search type
        if(getSearchType() == null)
            searchType = evt.getRequest().getParameter(LABEL_SEARCH_TYPE);

        if(!(selectedModule == null || "".equals(selectedModule) || searchType == null || "".equals(searchType)))
            selectedSearchType.put(selectedModule, searchType);

        // get modules
        if (getModules() == null)
        {
            modules = evt.getRequest().getParameterValues("modules");
            //populating selected search type
            if(modules != null)
            {
                for(int i = 0; i < modules.length; i++)
                {
                    String type = evt.getRequest().getParameter(modules[i] + LABEL_SEARCH_TYPE);
                    if(!(type == null || "".equals(type)))
                        selectedSearchType.put(modules[i], type);
                }
            }
        }

        // get user info
        Application app = Application.getInstance();
        SecurityService sec = (SecurityService)app.getService(SecurityService.class);
        userId = sec.getCurrentUser(evt.getRequest()).getId();
    }

    public SearchResult getSearchResults() {

        if (query != null && query.trim().length() > 0) {
            // get paging info
            int pageInt = 1;
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

            // perform search
            SearchResult results = null;
            Application app = Application.getInstance();
            Module module = null;
            try {
                // get module
                Class moduleClass = Class.forName(selectedModule);
                module = app.getModule(moduleClass);
                // invoke search
                if (module instanceof SearchableModule)
                {
                    results = getSearchResult((SearchableModule) module, startIndex, pageSizeInt);
                }
            }
            catch (Exception e) {
                String moduleClassName = (module != null) ? module.getClass().getName() : null;
                Log.getLog(getClass()).error("Error searching module " + moduleClassName + ": " + e.toString(), e);
            }

            // page count
            if (results != null)
            this.pageCount = (int)Math.ceil(((double)results.getTotalSize()) / pageSizeInt);

            return results;
        }
        else {
            return new SearchResult();
        }
    }

    public Map getSearchableModuleList()
    {
        Map moduleMap = Application.getInstance().getModuleMap();
        Map moduleList = new SequencedHashMap();
        for (Iterator i = moduleMap.keySet().iterator(); i.hasNext();)
        {
            Module module = (Module) moduleMap.get(i.next());
            if (module instanceof SearchableModule)
            {
                String label = getSearchType((SearchableModule) module);
                if(!("".equals(label)))
                    moduleList.put(module, label);
            }
        }
        return moduleList;
    }

    /**
     * Retrieves a summary of search results.
     * @return A Map containing Module Class Name (String) -> SearchResult object
     */
    public Map getSearchResultsSummary() {

        Map resultMap = new SequencedHashMap();

        if (query != null && query.trim().length() > 0) {
            // get required modules
            List requiredModules = null;
            if (modules != null) {
                requiredModules = Arrays.asList(modules);
            }

            // get module list
            Map moduleList = getSearchableModuleList();

            // calc page size
            int summaryPageSizeInt = 5;
            try {
                summaryPageSizeInt = Integer.parseInt(summaryPageSize);
                if (summaryPageSizeInt < 0)
                    summaryPageSizeInt = -1;
            }
            catch (Exception e) {
                summaryPageSizeInt = 5;
            }

            // iterate thru modules and invoke search
            for (Iterator i=moduleList.keySet().iterator(); i.hasNext();) {
                Module module = (Module)i.next();
                try {
                    if (requiredModules == null || requiredModules.contains(module.getClass().getName())) {
                        SearchResult results = getSearchResult((SearchableModule) module, 0, summaryPageSizeInt);
                        resultMap.put(module.getClass().getName(), results);
                    }
                }
                catch (Exception e) {
                    String moduleClassName = (module != null) ? module.getClass().getName() : null;
                    Log.getLog(getClass()).error("Error in summary search for module " + moduleClassName + ": " + e.toString(), e);
                }
            }
        }

        return resultMap;
    }

    private SearchResult executeNormalSearch(SearchableModule module, int startIndex, int pageSizeInt) throws QueryException
    {
        return module.search(query, startIndex, pageSizeInt, userId);
    }

    private SearchResult executeFullTextSearch(SearchableModule module, int startIndex, int pageSizeInt) throws QueryException
    {
        return module.searchFullText(query, startIndex, pageSizeInt, userId);
    }

    private String getSearchType(SearchableModule module)
    {
        boolean search = module.isSearchSupported();
        boolean fullText = module.isFullTextSearchSupported();
        String label = "";
        if(search && !fullText)
            label = LABEL_SEARCH_ONLY;
        else if(!search && fullText)
            label = LABEL_FULLTEXT_ONLY;
        else if(search && fullText)
            label = LABEL_ALL_SEARCHES;
        return label;
    }

    private SearchResult getSearchResult(SearchableModule module, int startIndex, int pageSizeInt) throws QueryException
    {
        SearchResult results = new SearchResult();
        String type = getSearchType(module);
        if(LABEL_SEARCH_ONLY.equals(type))
        {
            results = executeNormalSearch(module, startIndex, pageSizeInt);
        }
        else if(LABEL_FULLTEXT_ONLY.equals(type))
        {
            results = executeFullTextSearch(module, startIndex, pageSizeInt);
        }
        else if(LABEL_ALL_SEARCHES.equals(type))
        {
            if(LABEL_SEARCH_ONLY.equals(selectedSearchType.get(module.getClass().getName())))
                results = executeNormalSearch(module, startIndex, pageSizeInt);
            else if(LABEL_FULLTEXT_ONLY.equals(selectedSearchType.get(module.getClass().getName())))
                results = executeFullTextSearch(module, startIndex, pageSizeInt);
            else
                results = executeNormalSearch(module, startIndex, pageSizeInt);
        }
        return results;
    }

    //Getters and Setters
    public Map getSelectedSearchType()
    {
        return selectedSearchType;
    }

    public void setSelectedSearchType(Map selectedSearchType)
    {
        this.selectedSearchType = selectedSearchType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getSummaryPageSize() {
        return summaryPageSize;
    }

    public void setSummaryPageSize(String summaryPageSize) {
        this.summaryPageSize = summaryPageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(String selectedModule) {
        this.selectedModule = selectedModule;
    }

    public String[] getModules() {
        return modules;
    }

    public void setModules(String[] modules) {
        this.modules = modules;
    }

    public String getSearchType()
    {
        return searchType;
    }

    public void setSearchType(String searchType)
    {
        this.searchType = searchType;
    }
}
