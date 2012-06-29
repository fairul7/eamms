/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SourceDao extends DataSourceDao {
	public void insertRecord(Source src) throws DaoException {
		super.update(
			"INSERT INTO source (sourceID, sourceText, isArchived) " +
			"VALUES (#sourceID#, #sourceText#, #isArchived#)"
		, src);
	}
	
	public void updateRecord(Source src) throws DaoException {
		super.update(
			"UPDATE source " +
			"SET sourceText = #sourceText#, " +
			"    isArchived     = #isArchived# " +
			"WHERE sourceID = #sourceID# "
		, src);
	}
	
	public Source selectRecord(String sourceID) throws DaoException {
		Collection col = super.select(
			"SELECT sourceID, sourceText, isArchived " +
			"FROM source " +
			"WHERE sourceID = ? "
		, Source.class, new String[] {sourceID}, 0, 1);
		
		Source src = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			src = (Source) iterator.next(); 
		}
		
		return (src);
	}
	
	public Collection listRecords(String sort, boolean desc, int start, int rows) throws DaoException {
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "sourceText") + ((desc) ? " DESC" : "");
		
		Collection col = super.select(
			"SELECT sourceID, sourceText, isArchived " +
			"FROM source " +
			"WHERE sourceID <> '0' " +
			orderBy
		, Source.class, null, start, rows);
		
		return (col);
	}
	
	public int count() throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM source WHERE sourceID <> '0' ", HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public boolean isUnique(Source src) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM source WHERE sourceText = ? AND sourceID <> ? ", HashMap.class, new Object[] {src.getSourceText(), src.getSourceID()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}

    public void deleteSource(String id) throws DaoException {
        DefaultDataObject defaultDataObject = new DefaultDataObject();
        defaultDataObject.setId(id);
        super.update("DELETE FROM source WHERE sourceID= #id# ",defaultDataObject);
    }
}