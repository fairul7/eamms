package com.tms.ekms.search;

import com.tms.ekms.search.model.SearchProfile;
import com.tms.ekms.search.model.SearchProfileException;
import com.tms.ekms.search.model.SearchProfileModule;
import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 23, 2005
 * Time: 4:49:42 PM
 * To change this template use File | Settings | File Templates.
 *
 * execute Search Profiles during off peak
 */
public class SearchProfileTask extends BaseJob {
    private Log log = Log.getLog(getClass());

    public void execute(JobTaskExecutionContext context) throws SchedulingException {        
        log.info("Executing SearchProfileTask...");
        long startTime = System.currentTimeMillis();

        SearchProfileModule spModule;
        Collection searchProfilesList;
        SearchProfile profile;
        Map userResults = new HashMap();
        spModule = (SearchProfileModule) Application.getInstance().getModule(SearchProfileModule.class);

        try {
            // results are sorted by userId
            searchProfilesList = spModule.getAllSearchProfiles();

            for(Iterator itr=searchProfilesList.iterator(); itr.hasNext();){
                profile = (SearchProfile) itr.next();

                //assign profile infomation
                String userId = profile.getUserId();
                String query = profile.getQuery();
                String searchOptions = profile.getOptions();
                String profileName = profile.getProfilename();
                Date lastRun = profile.getLastRun();
                Map selectedSearchType = new HashMap();

                // extract selected modules
                searchOptions = profile.getOptions();
                StringTokenizer st = new StringTokenizer(searchOptions, ",");
                String[] modules = new String[st.countTokens()];

                for(int i = 0; st.hasMoreTokens(); i++){
                    String token = st.nextToken();

                    // get search type... normal or full text search
                    StringTokenizer subSt = new StringTokenizer(token, " ");
                    if(subSt.countTokens() > 1){
                        modules[i] = subSt.nextToken();
                        String subToken = subSt.nextToken();

                        selectedSearchType.put(modules[i], subToken);
                    }else{
                        modules[i] = token;
                    }
                }

                 Map resultMap = spModule.getSearchResultsSummary(query, modules, userId, selectedSearchType, lastRun);

                // if new matches exist, add search results to map
                if(spModule.isMatchFound()){
                    profile.setLastRun(new Date());
                    spModule.saveProfile(profile);
                    if(userResults.containsKey(userId)){
                        Map results = (HashMap) userResults.get(userId);
                        results.put(profileName, resultMap);
                    }else{
                        Map results = new HashMap();
                        results.put(profileName, resultMap);
                        userResults.put(userId, results);
                    }
                }
            }

            // Notification...
            // for each users with new matches...
            for(Iterator itr = userResults.entrySet().iterator(); itr.hasNext();){
                Map.Entry entry = (Map.Entry) itr.next();
                String userId = (String) entry.getKey();
                Map results = (HashMap) entry.getValue();

                String content = "";
                // for each search profiles with new matches, generate email content
                for(Iterator itr2 = results.entrySet().iterator(); itr2.hasNext();){
                    Map.Entry entry2 = (Map.Entry) itr2.next();
                    String profileName = (String) entry2.getKey();
                    Map resultMap = (SequencedHashMap) entry2.getValue();
                    content += "<h2>"+profileName+"</h2>" + spModule.extractContent(resultMap);
                }

                spModule.notifyUser(userId, content);

            }
        } catch (SearchProfileException e) {
            log.error("cant get all search profiles list from search profile module", e);
        } catch (kacang.services.security.SecurityException e) {
            log.error("error notifying user", e);
        }
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - startTime;
        log.info("Duration for search profile task: " + duration + "ms");
    }
}
