package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Transaction;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 21, 2003
 * Time: 4:48:19 PM
 * To change this template use Options | File Templates.
 */
public class ContentAuditorDao extends DataSourceDao {

    public void init() throws DaoException {
    }

    public void insert(ContentEvent event) throws DaoException {
        Transaction tx = null;

        try {
            StringBuffer sql = new StringBuffer(
                    "INSERT INTO " + getTableName() + " " +
                    "(id, event, userId, username, date, host, param, message)" +
                    "VALUES " +
                    "(#id#, #event#, #userId#, #username#, #date#, #host#, #param#, #message#)");

            tx = getTransaction();
            tx.begin();
            tx.update(sql.toString(), event);
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    public void delete(String key, Date startDate, Date endDate) throws DaoException {
        Transaction tx = null;

        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "DELETE FROM " + getTableName() + " " +
                    "WHERE 1=1");

            if (key != null) {
                sql.append(" AND id=?");
                paramList.add(key);
            }

            if (startDate != null) {
                sql.append(" AND startDate >= ?");
                paramList.add(startDate);
            }

            if (endDate != null) {
                sql.append(" AND endDate <= ?");
                paramList.add(endDate);
            }

            tx = getTransaction();
            tx.begin();
            tx.update(sql.toString(), paramList.toArray());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
    }

    public Collection select(String key, Date startDate, Date endDate, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT id, event, userId, username, date, host, param, message " +
                    "FROM " + getTableName() + " " +
                    "WHERE 1=1");

            if (key != null) {
                sql.append(" AND id=?");
                paramList.add(key);
            }

            if (startDate != null) {
                sql.append(" AND startDate >= ?");
                paramList.add(startDate);
            }

            if (endDate != null) {
                sql.append(" AND endDate <= ?");
                paramList.add(endDate);
            }

            if (sort != null && sort.trim().length() > 0) {
                sql.append(" ORDER BY ");
                sql.append(sort);
                if (desc)
                    sql.append(" DESC");
            }
            else {
                sql.append(" ORDER BY date DESC");
            }

            Collection results = super.select(sql.toString(), ContentEvent.class, paramList.toArray(), start, rows);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public int count(String key, Date startDate, Date endDate) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(*) AS total " +
                    "FROM " + getTableName() + " " +
                    "WHERE 1=1");

            if (key != null) {
                sql.append(" AND id=?");
                paramList.add(key);
            }

            if (startDate != null) {
                sql.append(" AND startDate >= ?");
                paramList.add(startDate);
            }

            if (endDate != null) {
                sql.append(" AND endDate <= ?");
                paramList.add(endDate);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
//                Integer totalStr = (Integer)resultMap.get("total");
//                int total = totalStr.intValue();
                int total = ((Number) resultMap.get("total")).intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    protected String getTableName() {
        return "cms_content_audit";
    }


}
