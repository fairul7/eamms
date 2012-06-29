package com.tms.cms.personalization;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.services.security.SecurityDao;
import kacang.util.JdbcUtil;
import kacang.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jun 16, 2003
 * Time: 3:39:12 PM
 * To change this template use Options | File Templates.
 */
public class PersonalizationDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE personalization_settings(userId VARCHAR(100) NOT NULL, numberArticles INT NOT NULL, numberTopics INT NOT NULL, PRIMARY KEY(userId))", null);
        super.update("CREATE TABLE personalization_details(userId VARCHAR(100) NOT NULL, objectId VARCHAR(100) NOT NULL, isSection VARCHAR(1) NOT NULL, PRIMARY KEY(userId, objectId))", null);
    }

    public Collection selectSections(DaoQuery properties, String userId) throws DaoException
    {
        Collection list = new ArrayList();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsSections = null;
        try
        {
            con = super.getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT objectId FROM personalization_details WHERE isSection='1'" + properties.getStatement(), properties.getArray());
            try
            {
                rsSections = statement.executeQuery();
                while(rsSections.next())
                    list.add(rsSections.getString("objectId"));
            }
            catch (SQLException e)
            {
                throw new DaoException(e.toString());
            }
            finally
            {
                if(rsSections != null)
                    rsSections.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
        }
        catch (SQLException e)
        {
            throw new DaoException(e.toString());
        }
        return list;
    }

    public Collection selectForums(DaoQuery properties, String userId) throws DaoException
    {
        Collection list = new ArrayList();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsForums = null;
        try
        {
            con = super.getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT objectId FROM personalization_details WHERE isSection='0'" + properties.getStatement(), properties.getArray());
            try
            {
                rsForums = statement.executeQuery();
                while(rsForums.next())
                {
                    list.add(rsForums.getString("objectId"));
                }
            }
            catch (Exception e)
            {
                Log.getLog(SecurityDao.class).error(e);
            }
            finally
            {
                if(rsForums != null)
                    rsForums.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
        }
        catch (SQLException e)
        {
            throw new DaoException(e.toString());
        }
        return list;
    }

    public PersonalizationSetting selectSettings(String userId) throws DaoException, DataObjectNotFoundException
    {
        Collection list = super.select("SELECT userId, numberArticles, numberTopics FROM personalization_settings WHERE userId=?", PersonalizationSetting.class, new Object[] {userId}, 0, 1);
        if (list.size() <= 0) throw new DataObjectNotFoundException("PersonalizationSetting Setting For " + userId + " unavailable");
        return (PersonalizationSetting) list.iterator().next();
    }

    public void insertSettings(PersonalizationSetting settings) throws DaoException
    {
        super.update("INSERT INTO personalization_settings(userId, numberArticles, numberTopics) VALUES(#userId#, #numberArticles#, #numberTopics#)", settings);
    }

    public void insertDetails(String userId, String objectId, String isSection) throws DaoException
    {
        super.update("INSERT INTO personalization_details(userId, objectId, isSection) VALUES(?,?,?)", new Object[] {userId, objectId, isSection});
    }

    public void updateSettings(PersonalizationSetting settings) throws DaoException
    {
        super.update("UPDATE personalization_settings SET numberArticles=#numberArticles#, numberTopics=#numberTopics# WHERE userId=#userId#", settings);
    }

    public void deleteSettings(String userId) throws DaoException
    {
        super.update("DELETE FROM personalization_settings WHERE userId=?", new Object[] {userId});
    }

    public void deleteDetails(String userId, String isSection) throws DaoException
    {
        super.update("DELETE FROM personalization_details WHERE userId=? and isSection=?", new Object[] {userId, isSection});
    }
}
