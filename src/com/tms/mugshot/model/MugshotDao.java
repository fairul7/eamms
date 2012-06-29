package com.tms.mugshot.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jul 6, 2005
 * Time: 2:43:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class MugshotDao extends DataSourceDao{
    public void init() throws DaoException{
        super.update("CREATE TABLE security_user_mugshot(userId VARCHAR(250) NOT NULL, filePath VARCHAR(250))", null);
    }

    public Mugshot get(String userId) throws DaoException, DataObjectNotFoundException {
        Collection col;
        Mugshot mugshot = null;
        col = super.select("SELECT userId, filePath FROM security_user_mugshot WHERE userId=?", Mugshot.class, new Object[]{userId}, 0, 1);

        if(col.size() > 0){
            mugshot = (Mugshot) col.iterator().next();
        }else throw new DataObjectNotFoundException("Mugshot not found for:"+userId);

        return mugshot;
    }

    public void save(Mugshot mug) throws DaoException {
        super.update("INSERT INTO security_user_mugshot(userId, filePath) VALUES(#userId#, #filePath#)", mug);
    }       

    public void remove(String userId) throws DaoException {
        super.update("DELETE FROM security_user_mugshot WHERE userId=?", new Object[]{userId});
    }
}
