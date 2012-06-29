package com.tms.collab.directory.model;

import kacang.model.DaoException;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressBookDaoOracle extends AddressBookDao
{
	/**
     * Overridden to cater for ORACLE's inability to tell the difference between an empty string and a
	 * NULL value as is required by parentId
     * @param query
     * @param parentId
     * @param userId
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of Folder objects.
     */
    public Collection selectFolderList(String query, String parentId, String userId, String sort, boolean desc, int start, int rows) throws DaoException
	{
        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, parentId, name, description, userId FROM " + getTablePrefix() + "_folder WHERE 1=1");

        if (query != null && query.trim().length() > 0)
		{
            query = "%" + query.trim() + "%";
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            args.add(query);
            args.add(query);
        }
        if (parentId != null)
		{
			if("".equals(parentId))
				sql.append(" AND parentId IS NULL");
			else
			{
				sql.append(" AND parentId=?");
				args.add(parentId);
			}
        }
        if (userId != null)
		{
            sql.append(" AND userId=?");
            args.add(userId);
        }
        // determine sorting
        if (sort != null && sort.trim().length() > 0)
		{
            sql.append(" ORDER BY " + sort);
            if (desc)
                sql.append(" DESC");
        }
        Collection results = super.select(sql.toString(), Folder.class, args.toArray(), start, rows);
        return results;
    }

	/**
	 * Overridden to cater for ORACLE's inability to tell the difference between an empty string and a
	 * NULL value as is required by parentId
	 * @param query
	 * @param parentId
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
    public int selectFolderCount(String query, String parentId, String userId) throws DaoException {

        // formulate SQL and parameters
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total FROM " + getTablePrefix() + "_folder WHERE 1=1");
        if (query != null && query.trim().length() > 0)
		{
            query = "%" + query.trim() + "%";
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            args.add(query);
            args.add(query);
        }
        if (parentId != null)
		{
			if("".equals(parentId))
				sql.append(" AND parentId IS NULL");
			else
			{
				sql.append(" AND parentId=?");
				args.add(parentId);
			}
        }
        if (userId != null)
		{
            sql.append(" AND userId=?");
            args.add(userId);
        }
        Collection results = super.select(sql.toString(), HashMap.class, args.toArray(), 0, 1);
        HashMap row = (HashMap)results.iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
}
