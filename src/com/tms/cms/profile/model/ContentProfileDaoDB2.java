package com.tms.cms.profile.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kacang.model.DaoException;

public class ContentProfileDaoDB2 extends ContentProfileDao{
	
	public Collection selectProfileList(String name, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection argList = new ArrayList();
        String sql = "SELECT id, name, description, definition FROM cms_profile";
        if (name != null && name.trim().length() > 0) {
            sql += " WHERE UPPER(name) LIKE ?";
            argList.add("%" + name.toUpperCase() + "%");
        }
        if (sort != null) {
            sql += " ORDER BY " + (sort.equals("description")?"cast(description AS varchar(255))":sort);
            if (desc) {
                sql += " DESC";
            }
        }
        Object[] args = argList.toArray();
        Collection results = super.select(sql, ContentProfile.class, args, start, rows);
        return results;
    }
	
	public int selectProfileCount(String name) throws DaoException {
        Collection argList = new ArrayList();
        String sql = "SELECT COUNT(*) AS total FROM cms_profile";
        if (name != null && name.trim().length() > 0) {
            sql += " WHERE UPPER(name) LIKE ?";
            argList.add("%" + name.toUpperCase() + "%");
        }
        Object[] args = argList.toArray();
        Collection results = super.select(sql, HashMap.class, args, 0, 1);
        Map row = (Map)results.iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }
	
}
