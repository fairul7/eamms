package com.tms.ekms.search.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.JdbcUtil;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 23, 2005
 * Time: 10:12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchProfileDao extends DataSourceDao{
    /**
     * Create the neccessary table
     * @throws DaoException
     */
    public void init() throws DaoException {
        super.update("CREATE TABLE search_profile(profileId CHAR(40) NOT NULL, userId VARCHAR(250) NOT NULL, " +
                "profilename VARCHAR(250) NOT NULL, query VARCHAR(255) NOT NULL, options MEDIUMTEXT, lastRun DATETIME, " +
                "matchFound SMALLINT, PRIMARY KEY(profileId))", null);
    }

    public void add(SearchProfile profile) throws DaoException{
        super.update("INSERT INTO search_profile(profileId, userId, profilename, query, options, lastRun, matchFound) " +
                "VALUES(#profileId#, #userId#, #profilename#, #query#, #options#, #lastRun#, #matchFound#)", profile);
    }

    public void update(SearchProfile profile) throws DaoException{
        super.update("UPDATE search_profile SET lastRun=#lastRun#, matchFound=#matchFound# WHERE profileId=#profileId#", profile);
    }

     public int getUserSProfilesCount(String userId) throws DaoException{
         int count = 0;
         Collection list = super.select("SELECT COUNT(profileId) as intCount FROM search_profile WHERE userId=?", HashMap.class, new Object[]{userId}, 0, -1);
         if(list.size() > 0)
         {
             HashMap map = (HashMap) list.iterator().next();
             count = ((Number)map.get("intCount")).intValue();
         }
        return count;
    }

    public int getUserSProfilesCount(String userId, String profilename) throws DaoException{
        int count = 0;
        Collection list = super.select("SELECT COUNT(profileId) as intCount FROM search_profile WHERE userId=? AND profilename LIKE '%"+profilename+"%'", HashMap.class, new Object[]{userId}, 0, -1);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

    public SearchProfile getSearchProfile(String profileId) throws DaoException{
        Collection list = super.select("SELECT profileId, userId, profilename, query, options, lastRun, matchFound FROM search_profile WHERE profileId=?", SearchProfile.class, new Object[]{profileId}, 0, -1);

        if(list.size() > 0){
            return (SearchProfile) list.iterator().next();
        }else return null;
    }

    public void delete(String profileId) throws DaoException{
        if(getSearchProfile(profileId) != null){
            super.update("DELETE FROM search_profile WHERE profileId=?", new Object[]{profileId});
        }
    }

    public Collection getUsersWithSearchProfile() throws DaoException{
        Collection list = super.select("SELECT DISTINCT userId FROM search_profile", SearchProfile.class, null, 0, -1);

        return list;
    }

    public Collection getAllSearchProfiles() throws DaoException{
        Collection list = super.select("SELECT profileId, userId, profilename, query, options, lastRun, matchFound FROM search_profile ORDER BY userId", SearchProfile.class, null, 0, -1);

        return list;
    }

    public Collection selectSearchProfiles(String userId, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT profileId, userId, profilename, query, options, lastRun, matchFound FROM search_profile WHERE userId=? " +
                JdbcUtil.getSort(sort, descending), SearchProfile.class, new Object[]{userId}, start, maxResults);

    }

    public Collection selectSearchProfiles(String userId, String profilename, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT profileId, userId, profilename, query, options, lastRun, matchFound FROM search_profile WHERE userId=? AND profilename LIKE ?" +
                JdbcUtil.getSort(sort, descending), SearchProfile.class, new Object[]{userId, '%' + profilename + '%'}, start, maxResults);

    }
}
