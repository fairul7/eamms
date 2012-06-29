package com.tms.ekms.setup.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

/**
 * DAO to manage the persistence for the Setup Module.
 * This default implementation uses generic SQL.
 */
public class SetupDao extends DataSourceDao {

    private Log log = Log.getLog(getClass());

    /**
     * Create the necessary database tables.
     * @throws DaoException
     */
    public void init() throws DaoException {
        super.update("CREATE TABLE setup (property VARCHAR(255) PRIMARY KEY, value VARCHAR(255), ordering INT)", null);
    }

    /**
     * Saves a Map of property->value pairs. If any property already exists, its value is updated.
     * Otherwise a new property is created.
     * @param propertyMap
     * @throws DaoException
     */
    public void save(Map propertyMap) throws DaoException {
        Transaction tx = null;
        try {
            tx = super.getTransaction();
            tx.begin();

            for (Iterator i=propertyMap.keySet().iterator(); i.hasNext();) {
                String property = (String)i.next();
                String value = (String)propertyMap.get(property);
                Object[] args = new Object[] { value, property };
                int result = tx.update("UPDATE setup SET value=? WHERE property=?", args);
                if (result == 0) {
                    tx.update("INSERT INTO setup (value, property) VALUES (?, ?)", args);
                }
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Unable to save setup propertyMap", e);
            throw new DaoException("Unable to save setup propertyMap");
        }
    }

    /**
     * Saves the value of a specific property. If the property already exists, the value is updated.
     * Otherwise a new property is created.
     * @param property
     * @param value
     * @throws DaoException
     */
    public void save(String property, String value) throws DaoException {
        Transaction tx = null;
        try {
            tx = super.getTransaction();
            tx.begin();

            Object[] args = new Object[] { value, property };
            int result = tx.update("UPDATE setup SET value=? WHERE property=?", args);
            if (result == 0) {
                tx.update("INSERT INTO setup (value, property) VALUES (?, ?)", args);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Unable to save setup property " + property, e);
            throw new DaoException("Unable to save setup property " + property);
        }
    }

    /**
     * Removes a specific property.
     * @param property
     * @throws DaoException
     */
    public void remove(String property) throws DaoException {
        try {
            Object[] args = new Object[] { property };
            super.update("DELETE FROM setup WHERE property=?", args);
        } catch (Exception e) {
            log.error("Unable to remove setup property " + property, e);
            throw new DaoException("Unable to remove setup property " + property);
        }
    }

    /**
     * Removes all properties. Use with caution.
     * @throws DaoException
     */
    public void clear() throws DaoException {
        try {
            super.update("DELETE FROM setup WHERE property=?", null);
        } catch (Exception e) {
            log.error("Unable to clear setup properties", e);
            throw new DaoException("Unable to clear setup properties");
        }
    }

    /**
     * Retrieves all system properties.
     * @return a Map of all property->value pairs.
     * @throws DaoException
     */
    public Map load() throws DaoException {
        try {
            Map resultMap = new SequencedHashMap();
            Collection results = super.select("SELECT property, value FROM setup ORDER BY ordering", HashMap.class, null, 0, -1);
            for (Iterator i=results.iterator(); i.hasNext();) {
                Map row = (Map)i.next();
                resultMap.put(row.get("property"), row.get("value"));
            }
            return resultMap;
        } catch (Exception e) {
            log.error("Unable to load setup properties", e);
            throw new DaoException("Unable to load setup properties");
        }
    }

    /**
     * Retrieves a specific property value.
     * @param property
     * @return null if the property is not available.
     * @throws DaoException
     */
    public String load(String property) throws DaoException {
        try {
            Object[] args = new Object[] { property };
            Collection results = super.select("SELECT property, value FROM setup WHERE property=?", HashMap.class, args, 0, -1);
            Iterator i=results.iterator();
            if (i.hasNext()) {
                return (String) ((Map)i.next()).get("value");
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Unable to load setup property " + property);
            throw new DaoException("Unable to load setup property " + property);
        }
    }

}
