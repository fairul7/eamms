package com.tms.cms.profile.model;

import java.util.ArrayList;
import java.util.Collection;

import kacang.model.DaoException;

public class ContentProfileDaoMsSql extends ContentProfileDao{
	
	public void init() throws DaoException {
        try{
        	super.init();
        }catch(DaoException e){
        	;
        }
        try{
        	super.update("ALTER TABLE cms_profile_data alter column id varchar(255) NULL", null);
        }
        catch(DaoException e){
        	;
        }
    }
	
	public Collection selectProfileList(String name, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection argList = new ArrayList();
        String sql = "SELECT id, name, description, definition FROM cms_profile";
        if (name != null && name.trim().length() > 0) {
            sql += " WHERE name LIKE ?";
            argList.add("%" + name + "%");
        }
        if (sort != null) {
            sql += " ORDER BY " + (sort.equals("description")?"cast(description as varchar)":sort);
            if (desc) {
                sql += " DESC";
            }
        }
        Object[] args = argList.toArray();
        Collection results = super.select(sql, ContentProfile.class, args, start, rows);
        return results;
    }
	
}
