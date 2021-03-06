package com.tms.ekms.search.model;

import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 23, 2005
 * Time: 10:15:43 AM
 */
public class SearchProfile implements Serializable{
    private String profileId; // unique profileId generated by UUID Generator
    private String userId; // search profile owner's userid
    private String profilename; // search profile profilename
    private String query; // query for lucene search
    private String options; // search options selected
    private Date lastRun; // date of which this pattern last run
    private String matchFound; // number of matches last found

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
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

}
