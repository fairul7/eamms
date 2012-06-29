package com.tms.ekms.search.ui;

import com.tms.ekms.search.model.SearchProfile;
import com.tms.ekms.search.model.SearchProfileException;
import com.tms.ekms.search.model.SearchProfileModule;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 29, 2005
 * Time: 4:48:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchProfileDisplay extends LightWeightWidget{
    public static String FORWARD_CANCEL = Application.getInstance().getMessage("searchprofile.label.cancel");
    public static String FORWARD_RUN = Application.getInstance().getMessage("searchprofile.label.run");
    public static final String LABEL_SEARCH_ONLY = "searchOnly";
    public static final String LABEL_FULLTEXT_ONLY = "fullTextOnly";
    public static final String LABEL_ALL_SEARCHES = "allSearches";
    public static final String LABEL_SEARCH_TYPE = "_searchType";

    private String profileId;
    private String query;
    private Map selectedSearchType = new HashMap();
    private String[] modules;
    private String userId;
    private String selectedModule;
    private String profileName;
    private Date lastRun;
    private String matchFound;
    private boolean allMatches = true;

    public boolean getAllMatches() {
        return allMatches;
    }

    public void setAllMatches(boolean allMatches) {
        this.allMatches = allMatches;
    }

    private String summaryPageSize;

    private Log log = Log.getLog(getClass());
    private SearchProfile profile;

    public void onRequest(Event evt) {
        Application app = Application.getInstance();
        SearchProfileModule module = (SearchProfileModule) app.getModule(SearchProfileModule.class);

        if(profileId != null && profileId.trim().length() > 0){
            String paramAllMatches = evt.getRequest().getParameter("allMatches");
            if(paramAllMatches != null){
                allMatches = true;
            }else{
                allMatches = false;
            }
            try {
                profile = module.getSearchProfile(profileId);

                // set profiles value
                setProfileName(profile.getProfilename());
                setQuery(profile.getQuery());
                setLastRun(profile.getLastRun());
                setMatchFound(profile.getMatchFound());
                setUserId(profile.getUserId());

                // get summaryPageSize
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

                // extract selected modules
                String selectedOptions = profile.getOptions();
                StringTokenizer st = new StringTokenizer(selectedOptions, ",");
                modules = new String[st.countTokens()];

                for(int i = 0; st.hasMoreTokens(); i++){
                    String token = st.nextToken();
                    //log.info('"'+token+'"');

                    StringTokenizer subSt = new StringTokenizer(token, " ");
                    if(subSt.countTokens() > 1){
                        modules[i] = subSt.nextToken();
                        String subToken = subSt.nextToken();

                        //  log.info(modules[i] + ":" + subToken);
                        getSelectedSearchType().put(modules[i], subToken);
                    }else{
                        modules[i] = token;
                    }
                }

            } catch (SearchProfileException e) {
                log.error("Error getting profile " + profileId, e);
            }
        }

        String action = evt.getRequest().getParameter("action");

        if(action != null){
            if(action.equals(FORWARD_CANCEL)){
                evt.getRequest().setAttribute("forward_action", FORWARD_CANCEL);
            }else if(action.equals(FORWARD_RUN)){
                evt.getRequest().setAttribute("forward_action", FORWARD_RUN);
            }
        }
        super.onRequest(evt);
    }

    public Map getSearchProfile(){

        return null;
    }

    public String getDefaultTemplate() {
        return "search/searchProfileTemplate";
    }

    public Map getSearchableModuleList()
    {
        Application app = Application.getInstance();
        SearchProfileModule module = (SearchProfileModule) app.getModule(SearchProfileModule.class);

        return module.getSearchableModuleList();
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map getSelectedSearchType() {
        return selectedSearchType;
    }

    public void setSelectedSearchType(Map selectedSearchType) {
        this.selectedSearchType = selectedSearchType;
    }

    public String[] getModules() {
        return modules;
    }

    public void setModules(String[] modules) {
        this.modules = modules;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(String selectedModule) {
        this.selectedModule = selectedModule;
    }


    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getMatchFound() {
        return matchFound;
    }

    public void setMatchFound(String matchFound) {
        this.matchFound = matchFound;
    }

    public String getSummaryPageSize() {
        return summaryPageSize;
    }

    public void setSummaryPageSize(String summaryPageSize) {
        this.summaryPageSize = summaryPageSize;
    }

    /**
     * Retrieves a summary of search results.
     * @return A Map containing Module Class Name (String) -> SearchResult object
     */
    public Map getSearchResultsSummary() {
        Application app = Application.getInstance();
        SearchProfileModule spModule = (SearchProfileModule) app.getModule(SearchProfileModule.class);
        Map resultMap;

        if(allMatches == false ){
            resultMap = spModule.getSearchResultsSummary(query, modules, userId, selectedSearchType, lastRun);
            log.info("search only latest:" + lastRun);
        }else{
            resultMap = spModule.getSearchResultsSummary(query, modules, userId, selectedSearchType);
            log.info("search all");
        }

        if(spModule.isMatchFound()){
            profile.setLastRun(new Date());
            profile.setMatchFound(String.valueOf(spModule.getTotalMatchFound()));
            try {
                spModule.saveProfile(profile);
            } catch (SearchProfileException e) {
                log.info("Cant save profile: " + profile.getProfilename(), e);
            }
        }

        return resultMap;
    }
}
