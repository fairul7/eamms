package com.tms.cms.mobile.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Transaction;
import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;

public class MobileDao extends DataSourceDao {

    private Log log = Log.getLog(getClass());

    public void init() throws DaoException {
        super.init();
 //       super.update("CREATE TABLE cms_mobile_channel (title VARCHAR(255) PRIMARY KEY, dateCreated DATETIME, dateModified DATETIME, maxSize INT, depth INT, allowImages CHAR(1), offsiteLinks CHAR(1), refresh VARCHAR(255), refreshHourlyDuration INT, refreshDailyTime DATETIME, contentId VARCHAR(255))", null);
    }

    public void save(MobileChannel channel) throws DaoException {
        Transaction tx = null;
        try {
            tx = super.getTransaction();
            tx.begin();

            int result = tx.update("UPDATE cms_mobile_channel " +
                    " SET dateCreated=#dateCreated#, dateModified=#dateModified#, maxSize=#maxSize#, depth=#depth#, allowImages=#allowImages#, offsiteLinks=#offsiteLinks#, refresh=#refresh#, refreshHourlyDuration=#refreshHourlyDuration#, refreshDailyTime=#refreshDailyTime#, contentId=#contentId# " +
                    " WHERE title=#title#", channel);
            if (result == 0) {
                tx.update("INSERT INTO cms_mobile_channel (title, dateCreated, dateModified, maxSize, depth, allowImages, offsiteLinks, refresh, refreshHourlyDuration, refreshDailyTime, contentId) " +
                        "VALUES (#title#, #dateCreated#, #dateModified#, #maxSize#, #depth#, #allowImages#, #offsiteLinks#, #refresh#, #refreshHourlyDuration#, #refreshDailyTime#, #contentId#)", channel);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Unable to save channel " + e.toString(), e);
            throw new DaoException("Unable to save channel " + e.toString(), e);
        }
    }

    public MobileChannel load(String title) throws DaoException, DataObjectNotFoundException {
        try {
            Collection results = super.select("SELECT title, dateCreated, dateModified, maxSize, depth, allowImages, offsiteLinks, refresh, refreshHourlyDuration, refreshDailyTime, contentId " +
                    "FROM cms_mobile_channel WHERE title=?", MobileChannel.class, new Object[] {title}, 0, 1);
            if (results.size() < 1)
                throw new DataObjectNotFoundException();
            return (MobileChannel)results.iterator().next();
        } catch (Exception e) {
            log.error("Unable to load channel " + title, e);
            throw new DaoException("Unable to load channel " + title);
        }
    }

    public Collection list(String title, int start, int rows) throws DaoException {
        try {
            title = (title != null) ? "%" + title + "%" : "%";
            Collection results = super.select("SELECT title, dateCreated, dateModified, maxSize, depth, allowImages, offsiteLinks, refresh, refreshHourlyDuration, refreshDailyTime, contentId " +
                    "FROM cms_mobile_channel WHERE title LIKE ? ORDER BY title", MobileChannel.class, new Object[] {title}, start, rows);
            return results;
        } catch (Exception e) {
            log.error("Unable to load channels " + e.toString(), e);
            throw new DaoException("Unable to load channels");
        }
    }

    public int count(String title) throws DaoException {
        try {
            title = (title != null) ? "%" + title + "%" : "%";
            Collection results = super.select("SELECT COUNT(*) AS channelCount FROM cms_mobile_channel WHERE title LIKE ?", HashMap.class, new Object[] {title}, 0, -1);
            HashMap row = (HashMap)results.iterator().next();
            return Integer.parseInt(row.get("channelCount").toString());
        } catch (Exception e) {
            log.error("Unable to load channels " + e.toString(), e);
            throw new DaoException("Unable to load channels");
        }
    }

    public void delete(String title) throws DaoException {
        try {
            super.update("DELETE FROM cms_mobile_channel WHERE title=?", new Object[] {title});
        } catch (Exception e) {
            log.error("Unable to delete channel " + title, e);
            throw new DaoException("Unable to delete channel " + title, e);
        }
    }

}
