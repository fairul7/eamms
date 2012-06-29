package com.tms.ekms.search.model;

import com.tms.util.MailUtil;
import com.tms.ekms.search.SearchProfileTask;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.ekms.setup.model.SetupException;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.model.Module;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;
import kacang.services.security.SecurityService;
import kacang.services.scheduling.*;
import kacang.util.Log;
import kacang.util.Mailer;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 23, 2005
 * Time: 10:15:29 AM
 * This module allows the user to save search profiles
 */
public class SearchProfileModule extends DefaultModule{
    public static final String PROPERTY_CONTEXT = "searchProfile.context";
    public static final String SEARCHPROFILE_NOTIFICATION = "searchprofile_daemon";
    public static final String EVENT_SEARCH_RESULT = "searchResult";
    public static final String LABEL_SEARCH_ONLY = "searchOnly";
    public static final String LABEL_FULLTEXT_ONLY = "fullTextOnly";
    public static final String LABEL_ALL_SEARCHES = "allSearches";
    public static final String LABEL_SEARCH_TYPE = "_searchType";

    private Log log = Log.getLog(getClass());
    private boolean isMatchFound = false;
    private int totalMatchFound = 0;
    private String context;
    private SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
    private SetupModule setupModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);


    public void init(){
        log.info("Initializing SearchProfile...");
        // initializing daemon

        Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        JobSchedule schedule = new JobSchedule(Application.getInstance().getMessage("searchprofile.label.jobname"), JobSchedule.DAILY);
        schedule.setGroup(SEARCHPROFILE_NOTIFICATION);
        schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setRepeatInterval(1);
		schedule.setStartTime(calendar.getTime());
        JobTask task = new SearchProfileTask();
        task.setName(Application.getInstance().getMessage("searchprofile.label.jobname"));
        task.setGroup(SEARCHPROFILE_NOTIFICATION);
        task.setDescription("Search Profiles runner");

        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        try {
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
            context = setupModule.get("siteUrl") + Application.getInstance().getProperty(PROPERTY_CONTEXT);

        } catch (SchedulingException e) {
            log.error("Error removing task:" + task.getName(), e);
        } catch (SetupException e) {
            e.printStackTrace();
        }
        log.info("started job");
    }

    public int getTotalMatchFound() {
        return totalMatchFound;
    }

    public void setTotalMatchFound(int totalMatchFound) {
        this.totalMatchFound = totalMatchFound;
    }

    public boolean isMatchFound() {
        return isMatchFound;
    }

    public void setMatchFound(boolean matchFound) {
        isMatchFound = matchFound;
    }

    public void deleteProfile(String profileId) throws SearchProfileException{
        SearchProfileDao dao = (SearchProfileDao) getDao();

        try {
            dao.delete(profileId);
        } catch (DaoException e) {
            throw new SearchProfileException("Error deleting search profile", e);
        }
    }

    /**
     * Add/Update search profile. When adding profile, a UUID is generated.
     * @param profile
     * @throws SearchProfileException
     */
    public void saveProfile(SearchProfile profile) throws SearchProfileException{
        SearchProfileDao dao = (SearchProfileDao) getDao();
        try {
            if(profile.getProfileId() == null || profile.getProfileId().equals("")){
                profile.setProfileId(UuidGenerator.getInstance().getUuid());
                dao.add(profile);
            }else{
                dao.update(profile);
            }
        } catch (DaoException e) {
            throw new SearchProfileException("Error adding new search profile", e);
        }
    }

    /**
     * Get a search profile from ProfileId
     * @param profileId
     * @return
     * @throws SearchProfileException
     */
    public SearchProfile getSearchProfile(String profileId) throws SearchProfileException{
        SearchProfileDao dao = (SearchProfileDao) getDao();
        SearchProfile profile;
        try {
            profile = dao.getSearchProfile(profileId);
        } catch (DaoException e) {
            throw new SearchProfileException("Error getting search profile", e);
        }
        return profile;
    }

    /**
     * Get a collection of Search Profiles which belongs to a user
     * @param userId
     * @param start
     * @param maxResults
     * @param sort
     * @param descending
     * @return
     * @throws SearchProfileException
     */
    public Collection getUserSearchProfiles(String userId, int start, int maxResults, String sort, boolean descending) throws SearchProfileException{
        SearchProfileDao dao = (SearchProfileDao) getDao();
        Collection list;
        try {
            list = dao.selectSearchProfiles(userId, start, maxResults, sort, descending);
        } catch (DaoException e) {
            throw new SearchProfileException("Error getting " + userId + " search profiles", e);
        }

        return list;
    }

    /**
     * Get a Collection of Search Profiles which belongs to a user with a
     * particular profile name pattern
     * @param userId
     * @param profileName
     * @param start
     * @param maxResults
     * @param sort
     * @param descending
     * @return
     * @throws SearchProfileException
     */
    public Collection getUserSearchProfiles(String userId, String profileName, int start, int maxResults, String sort, boolean descending) throws SearchProfileException{
        SearchProfileDao dao = (SearchProfileDao) getDao();
        Collection list;
        try {
            list = dao.selectSearchProfiles(userId, profileName, start, maxResults, sort, descending);
        } catch (DaoException e) {
            throw new SearchProfileException("Error getting " + userId + " search profiles", e);
        }

        return list;

    }

    /**
     * Count the numbers of result found
     * @param userId
     * @return
     * @throws SearchProfileException
     */
    public int getUserSearchProfilesCount(String userId) throws SearchProfileException
    {
        int count = 0;
        try {
            SearchProfileDao dao = (SearchProfileDao) getDao();
            count = dao.getUserSProfilesCount(userId);
        } catch (DaoException e) {
            throw new SearchProfileException("Error while retrieving profile count for user " + userId, e);
        }
        return count;
    }

    /**
     * Count the numbers of result found
     * @param userId
     * @param name
     * @return
     * @throws SearchProfileException
     */
    public int getUserSearchProfilesCount(String userId, String name) throws SearchProfileException
    {
        int count = 0;
        try {
            SearchProfileDao dao = (SearchProfileDao) getDao();
            count = dao.getUserSProfilesCount(userId, name);
        } catch (DaoException e) {
            throw new SearchProfileException("Error while retrieving profile count for user " + userId, e);
        }
        return count;
    }

    /**
     * Retrieves a summary of search results.
     * @return A Map containing Module Class Name (String) -> SearchResult object
     */
    public Map getSearchResultsSummary(String query, String[] modules, String userId, Map selectedSearchType) {
        isMatchFound = false;
        totalMatchFound = 0;

        Application app = Application.getInstance();
        SearchProfileModule spModule = (SearchProfileModule) app.getModule(SearchProfileModule.class);
        Map resultMap = new SequencedHashMap();

        if (query != null && query.trim().length() > 0) {
            // get required modules
            List requiredModules = null;
            if (modules != null) {
                requiredModules = Arrays.asList(modules);
            }

            // get module list
            Map moduleList = getSearchableModuleList();


            // iterate thru modules and invoke search
            for (Iterator i=moduleList.keySet().iterator(); i.hasNext();) {
                Module module = (Module)i.next();

                try {
                    if (requiredModules == null || requiredModules.contains(module.getClass().getName())) {
                        SearchResult results = spModule.getSearchResult((SearchableModule) module, query, userId, selectedSearchType);
                        if(results.getTotalSize() > 0){
                            isMatchFound = true;
                            totalMatchFound += results.getTotalSize();
                        }
                        resultMap.put(module.getClass().getName(), results);
                    }
                }
                catch (Exception e) {
                    String moduleClassName = (module != null) ? module.getClass().getName() : null;
                    log.error("Error in summary search for module " + moduleClassName + ": " + e.toString(), e);
                }
            }
        }

        return resultMap;
    }

    /**
     * Retrieves a summary of search results. Only latest matches
     * @return A Map containing Module Class Name (String) -> SearchResult object
     */
    public Map getSearchResultsSummary(String query, String[] modules, String userId, Map selectedSearchType, Date lastRun) {
        isMatchFound = false;
        totalMatchFound = 0;
        Application app = Application.getInstance();
        SearchProfileModule spModule = (SearchProfileModule) app.getModule(SearchProfileModule.class);
        Map resultMap = new SequencedHashMap();

        if (query != null && query.trim().length() > 0) {
            // get required modules
            List requiredModules = null;
            if (modules != null) {
                requiredModules = Arrays.asList(modules);
            }

            // get module list
            Map moduleList = getSearchableModuleList();


            // iterate thru modules and invoke search
            for (Iterator i=moduleList.keySet().iterator(); i.hasNext();) {
                Module module = (Module)i.next();

                try {
                    if (requiredModules == null || requiredModules.contains(module.getClass().getName())) {
                        SearchResult results = spModule.getSearchResult((SearchableModule) module, query, userId, selectedSearchType, lastRun);
                        if(results.getTotalSize() > 0){
                            isMatchFound = true;
                            totalMatchFound += results.getTotalSize();
                        }
                        resultMap.put(module.getClass().getName(), results);

                    }
                }
                catch (Exception e) {
                    String moduleClassName = (module != null) ? module.getClass().getName() : null;
                    log.error("Error in summary search for module " + moduleClassName + ": " + e.toString(), e);
                }
            }
        }

        return resultMap;
    }

    public Collection getAllSearchProfiles() throws SearchProfileException{
        SearchProfileDao dao = (SearchProfileDao) getDao();
        Collection list;
        try {
            list = dao.getAllSearchProfiles();
        } catch (DaoException e) {
            throw new SearchProfileException("Error getting all search profiles", e);
        }
        return list;
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

    private SearchResult getSearchResult(SearchableModule module, String query, String userId, Map selectedSearchType) throws QueryException
    {
        SearchResult results = new SearchResult();
        String type = getSearchType(module);
        if(LABEL_SEARCH_ONLY.equals(type))
        {
            results = executeNormalSearch(module, query, userId);
        }
        else if(LABEL_FULLTEXT_ONLY.equals(type))
        {
            results = executeFullTextSearch(module, query, userId);
        }
        else if(LABEL_ALL_SEARCHES.equals(type))
        {
            if(LABEL_SEARCH_ONLY.equals(selectedSearchType.get(module.getClass().getName())))
                results = executeNormalSearch(module, query, userId);
            else if(LABEL_FULLTEXT_ONLY.equals(selectedSearchType.get(module.getClass().getName())))
                results = executeFullTextSearch(module, query, userId);
            else
                results = executeNormalSearch(module, query, userId);
        }

        return results;
    }

    private SearchResult getSearchResult(SearchableModule module, String query, String userId, Map selectedSearchType, Date lastRun) throws QueryException
    {
        SearchResult results = new SearchResult();
        String type = getSearchType(module);
        if(LABEL_SEARCH_ONLY.equals(type))
        {
            results = executeNormalSearch(module, query, userId, lastRun);
        }
        else if(LABEL_FULLTEXT_ONLY.equals(type))
        {
            results = executeFullTextSearch(module, query, userId, lastRun);
        }
        else if(LABEL_ALL_SEARCHES.equals(type))
        {
            if(LABEL_SEARCH_ONLY.equals(selectedSearchType.get(module.getClass().getName())))
                results = executeNormalSearch(module, query, userId, lastRun);
            else if(LABEL_FULLTEXT_ONLY.equals(selectedSearchType.get(module.getClass().getName())))
                results = executeFullTextSearch(module, query, userId, lastRun);
            else
                results = executeNormalSearch(module, query, userId, lastRun);
        }

        return results;
    }

    private SearchResult executeFullTextSearch(SearchableModule module, String query, String userId) throws QueryException {
        return module.searchFullText(query, 0, -1, userId);
    }

    private SearchResult executeFullTextSearch(SearchableModule module, String query, String userId, Date lastRun) throws QueryException {
        return module.searchFullText(query, 0, -1, userId, lastRun, new Date());
    }

    private SearchResult executeNormalSearch(SearchableModule module, String query, String userId) throws QueryException {
        return module.search(query, 0, -1, userId);
    }

    private SearchResult executeNormalSearch(SearchableModule module, String query, String userId, Date lastRun) throws QueryException {
        return module.search(query, 0, -1, userId, lastRun, new Date());
    }

    /**
     * sends user an email containing the new matches
     * @param userId
     * @param content
     */
    public void notifyUser(String userId, String content) throws kacang.services.security.SecurityException {
        Application app = Application.getInstance();
        String css = "<style type=\"text/css\">\n" +
                "<!--\n" +
                ".contentTitleFont {background-color:#BBD5F2; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; color: #000000; font-weight:bold}\n" +
                ".contentBody {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}\n" +
                ".searchLink {font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8pt; font-weight:bold; color:#000000; text-decoration:none}\n" +
                ".contentChildSummary {font-size: 8pt;}\n" +
                "-->\n" +
                "</style>";
        String header = "<h3>"+app.getMessage("searchprofile.email.header")+"</h3><br>";
        String email = (String) securityService.getUser(userId).getProperty("email1");
        String subject = app.getMessage("searchprofile.email.subject");

        if(Mailer.isValidEmail(email)){
            MailUtil.sendEmail(null, true, null, email, subject, css+ header + content);
        }
    }

    /**
     * Transform result maps into html content
     * @param resultMap
     * @return html content ready for mailing
     */
    public String extractContent(Map resultMap){
        String content = "";
        for(Iterator itr = resultMap.keySet().iterator(); itr.hasNext();){
            String moduleName = (String) itr.next();
            SearchResult results = (SearchResult) resultMap.get(moduleName);
            if(results.getTotalSize() > 0){
                content += "<table cellpadding=\"3\" cellspacing=\"0\" width=\"100%\">\n" +
                        "     <tr><td class=\"contentTitleFont\"><span class=\"contentTitleFont\">"+Application.getInstance().getMessage(moduleName)+"</span></td></tr>\n" +
                        "        <tr><td class=\"contentBody\">&nbsp;</td></tr>\n" +
                        "             <tr>\n" +
                        "                  <td class=\"contentBody\" align=\"center\">\n" +
                        "                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"95%\">\n" +
                        "                    <tr>\n" +
                        "                       <td>\n" +
                        "                         <ul>\n";


                for(int i = 0;i < results.getSearchResultItems().size();i++){
                    SearchResultItem item = results.getSearchResultItem(i);
                    String searchKey = (String) item.getValueMap().get("key");
                    String searchModule = (String) item.getValueMap().get("moduleClass");
                    String searchObject = (String) item.getValueMap().get("objectClass");
                    String searchTitle = (String) item.getValueMap().get("title");

                    String url = context + "?et=searchResult&key="+searchKey+"&moduleClass="+searchModule+"&objectClass="+searchObject;

                    content +="              <li>\n" +
                            "           <div class=\"contentBody\">\n" +
                            "      <a href=\""+url+"\" class=\"searchLink\">"+searchTitle+"</a>\n";

                    if(item.getValueMap().get("date") != null){
                        content +="<br><font class=\"searchDate\">"+item.getValueMap().get("date")+"</font>\n";
                    }

                    content +="                      </div>\n" +
                            "        <div class=\"contentChildSummary\">"+item.getValueMap().get("description")+"</div>\n" +
                            "     <br>\n</li>\n";
                }
                content +="                     </td>\n" +
                        "                     </tr>\n" +
                        "                    </table>\n" +
                        "                 </td>\n" +
                        "              </tr>\n" +
                        "      </table><br>";
            }
        }
        return content;
    }
}
