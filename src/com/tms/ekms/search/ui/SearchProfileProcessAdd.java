package com.tms.ekms.search.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.services.indexing.SearchableModule;
import kacang.services.security.SecurityService;
import kacang.Application;
import kacang.util.Log;

import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import java.io.IOException;

import com.tms.ekms.search.model.SearchProfileModule;
import com.tms.ekms.search.model.SearchProfile;
import com.tms.ekms.search.model.SearchProfileException;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 31, 2005
 * Time: 2:32:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchProfileProcessAdd extends LightWeightWidget{
    public static final String LABEL_SEARCH_TYPE = "_searchType";

    private String query;            
    private Map selectedSearchType;
    private String[] modules;
    private String userId;
    private String selectedModule;
    private String searchType;
    private String profileName;

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    private String forward;

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

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void onRequest(Event evt) {
        String action = evt.getRequest().getParameter("action");
        setProfileName(evt.getRequest().getParameter("profilename"));

        if(action != null && action.equals(Application.getInstance().getMessage("searchprofile.label.save"))){
            // get user info
            Application app = Application.getInstance();
            SecurityService sec = (SecurityService)app.getService(SecurityService.class);
            userId = sec.getCurrentUser(evt.getRequest()).getId();

            if(profileName != null && profileName.trim().length() > 0){

                if(getQuery() == null){
                    query = evt.getRequest().getParameter("query");
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

                // saving profile
                String selectedOptions = new String();
                SearchProfileModule spModule = (SearchProfileModule) app.getModule(SearchProfileModule.class);
                SearchProfile searchProfile = new SearchProfile();
                searchProfile.setProfilename(profileName);
                searchProfile.setQuery(query);
                searchProfile.setUserId(userId);

                if(modules != null)
                {
                    for(int i = 0; i < modules.length; i++)
                    {
                        selectedOptions += modules[i].toString();
                        String type = evt.getRequest().getParameter(modules[i] + LABEL_SEARCH_TYPE);
                        if(!(type == null || "".equals(type)))
                            selectedOptions += " " + type;

                        selectedOptions += ",";
                    }
                }
                searchProfile.setOptions(selectedOptions);
                searchProfile.setLastRun(new Date());
                searchProfile.setMatchFound("0");

                try {
                    evt.getResponse().sendRedirect(forward);
                    spModule.saveProfile(searchProfile);
                } catch (SearchProfileException e) {
                    Log.getLog(getClass()).error("Error adding new profile by user "+userId, e);
                } catch (IOException e) {
                    Log.getLog(getClass()).error("error redirecting user: " + forward, e);
                }
            }
        }
    }

    public String getDefaultTemplate() {
        return null;
    }
}

