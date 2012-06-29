package com.tms.ekms.setup.model;

import kacang.model.DefaultModule;
import kacang.util.Log;

import java.util.Map;

/**
 * This module handles the loading and saving of system settings (properties)
 */
public class SetupModule extends DefaultModule {

    private Log log = Log.getLog(getClass());

    public void init() {
    }

    /**
     * Saves a Map of property->value pairs. If any property already exists, its value is updated.
     * Otherwise a new property is created.
     * @param propertyMap
     * @throws SetupException
     */
    public void saveAll(Map propertyMap) throws SetupException {
        try {
            SetupDao dao = (SetupDao)getDao();
            dao.save(propertyMap);
        } catch (Exception e) {
            throw new SetupException("Unable to save setup propertyMap");
        }
    }

    /**
     * Saves the value of a specific property. If the property already exists, the value is updated.
     * Otherwise a new property is created.
     * @param property
     * @param value
     * @throws SetupException
     */
    public void save(String property, String value) throws SetupException {
        try {
            SetupDao dao = (SetupDao)getDao();
            dao.save(property, value);
        } catch (Exception e) {
            throw new SetupException("Unable to save setup property " + property);
        }
    }

    /**
     * Removes a specific property.
     * @param property
     * @throws SetupException
     */
    public void remove(String property) throws SetupException {
        try {
            SetupDao dao = (SetupDao)getDao();
            dao.remove(property);
        } catch (Exception e) {
            throw new SetupException("Unable to remove setup property " + property);
        }
    }

    /**
     * Removes all properties. Use with caution.
     * @throws SetupException
     */
    public void clear() throws SetupException {
        try {
            SetupDao dao = (SetupDao)getDao();
            dao.clear();
        } catch (Exception e) {
            throw new SetupException("Unable to clear setup properties");
        }
    }

    /**
     * Retrieves all system properties.
     * @return a Map of all property->value pairs.
     * @throws SetupException
     */
    public Map getAll() throws SetupException {
        try {
            SetupDao dao = (SetupDao)getDao();
            return dao.load();
        } catch (Exception e) {
            throw new SetupException("Unable to get setup propertyMap");
        }
    }

    /**
     * Retrieves a specific property value.
     * @param property
     * @return null if the property is not available.
     * @throws SetupException
     */
    public String get(String property) throws SetupException {
        try {
            SetupDao dao = (SetupDao)getDao();
            return dao.load(property);
        } catch (Exception e) {
            throw new SetupException("Unable to get setup property " + property);
        }
    }

}
