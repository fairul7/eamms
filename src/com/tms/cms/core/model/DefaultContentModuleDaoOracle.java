package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

public abstract class DefaultContentModuleDaoOracle extends DataSourceDao implements ContentModuleDao {

    protected final static Properties DEFAULT_PROPERTIES;

    static {
        DEFAULT_PROPERTIES = new Properties();
        DEFAULT_PROPERTIES.put("id", "id");
        DEFAULT_PROPERTIES.put("version", "version");
        DEFAULT_PROPERTIES.put("name", "name");
        DEFAULT_PROPERTIES.put("description", "description");
        DEFAULT_PROPERTIES.put("summary", "summary");
        DEFAULT_PROPERTIES.put("author", "author");
        DEFAULT_PROPERTIES.put("\"DATE\"", "date");
        DEFAULT_PROPERTIES.put("keywords", "keywords");
        DEFAULT_PROPERTIES.put("related", "related");
        DEFAULT_PROPERTIES.put("contents", "contents");
/*
        DEFAULT_PROPERTIES.put("submissionDate", "submissionDate");
        DEFAULT_PROPERTIES.put("submissionUser", "submissionUser");
        DEFAULT_PROPERTIES.put("submissionUserId", "submissionUserId");
        DEFAULT_PROPERTIES.put("approvalDate", "approvalDate");
        DEFAULT_PROPERTIES.put("approvalUser", "approvalUser");
        DEFAULT_PROPERTIES.put("approvalUserId", "approvalUserId");
        DEFAULT_PROPERTIES.put("comments", "comments");
*/
    }

    protected abstract String getTableName();

    protected abstract Class getContentObjectClass();

    protected Properties properties() {
        return DEFAULT_PROPERTIES;
    }

    public int insert(ContentObject contentObject) throws InvalidKeyException, DaoException {
        try {
            StringBuffer sql = new StringBuffer("INSERT INTO ");
            sql.append(getTableName());
            StringBuffer columns = new StringBuffer();
            StringBuffer values = new StringBuffer();
            for (Iterator i=properties().keySet().iterator(); i.hasNext();) {
                String column = (String)i.next();
                String property = properties().getProperty(column);
                columns.append(column);
                if (i.hasNext()) {
                    columns.append(",");
                }
                values.append("#");
                values.append(property);
                values.append("#");
                if (i.hasNext()) {
                    values.append(",");
                }
            }
            sql.append(" (");
            sql.append(columns);
            sql.append(") VALUES (");
            sql.append(values);
            sql.append(")");
            int result = super.update(sql.toString(), contentObject);
            return result;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public int processNew(ContentObject contentObject) throws DaoException {
        return 0;
    }

    public int update(ContentObject contentObject) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer("UPDATE ");
            sql.append(getTableName());
            sql.append(" SET ");
            for (Iterator i=properties().keySet().iterator(); i.hasNext();) {
                String column = (String)i.next();
                String property = properties().getProperty(column);
                sql.append(column);
                sql.append("=");
                sql.append("#");
                sql.append(property);
                sql.append("#");
                sql.append(",");
            }
            sql = new StringBuffer(sql.substring(0, sql.length()-1));
            sql.append(" WHERE id=#id# AND version=#version#");
            int result = super.update(sql.toString(), contentObject);
            return result;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public void delete(String key) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer("DELETE FROM ");
            sql.append(getTableName());
            sql.append(" WHERE id=?");
            String[] params = new String[] { key };
            super.update(sql.toString(), params);
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public void delete(String key, String version) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer("DELETE FROM ");
            sql.append(getTableName());
            sql.append(" WHERE id=? AND version=?");
            String[] params = new String[] { key, version };
            super.update(sql.toString(), params);
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public ContentObject selectByVersion(String key, String version) throws DataObjectNotFoundException, DaoException {
        try {
            StringBuffer sql = new StringBuffer("SELECT ");
            StringBuffer columns = new StringBuffer();
            for (Iterator i=properties().keySet().iterator(); i.hasNext();) {
                String column = (String)i.next();
                String property = properties().getProperty(column);
                columns.append(column);
				if(!(column.equals("\"DATE\"")))
				{
					columns.append(" AS ");
					columns.append(property);
				}
                if (i.hasNext()) {
                    columns.append(",");
                }
            }
            sql.append(columns);
            sql.append(" FROM ");
            sql.append(getTableName());
            sql.append(" WHERE id=? AND version=?");
            String[] params = new String[] {
                key,
                version
            };
            Collection results = super.select(sql.toString(), getContentObjectClass(), params, 0, -1);
            if (results.size() == 0) {
                throw new DataObjectNotFoundException("Object " + key + " version " + version + " not found");
            }
            return (ContentObject)results.iterator().next();
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }

    public Collection selectAllVersions(String key) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer("SELECT ");
            StringBuffer columns = new StringBuffer();
            for (Iterator i=properties().keySet().iterator(); i.hasNext();) {
                String column = (String)i.next();
                String property = properties().getProperty(column);
                columns.append(column);
				if(!(column.equals("\"DATE\"")))
				{
					columns.append(" AS ");
					columns.append(property);
				}
                if (i.hasNext()) {
                    columns.append(",");
                }
            }
            sql.append(columns);
            sql.append(" FROM ");
            sql.append(getTableName());
            sql.append(" WHERE id=?");
            String[] params = new String[] {
                key,
            };
            Collection results = super.select(sql.toString(), getContentObjectClass(), params, 0, -1);
            return results;
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }






}
