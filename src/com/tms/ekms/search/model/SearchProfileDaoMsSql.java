package com.tms.ekms.search.model;

import kacang.model.DaoException;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 23, 2005
 * Time: 10:12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchProfileDaoMsSql extends SearchProfileDao{
    /**
     * Create the neccessary table
     * @throws DaoException
     */
    public void init() throws DaoException {
        super.update("CREATE TABLE search_profile(profileId CHAR(40) NOT NULL, userId VARCHAR(250) NOT NULL, " +
                "profilename VARCHAR(250) NOT NULL, query VARCHAR(255) NOT NULL, options TEXT, lastRun DATETIME, " +
                "matchFound SMALLINT, PRIMARY KEY(profileId))", null);
    }
    
}
