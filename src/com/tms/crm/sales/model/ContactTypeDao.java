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
public class ContactTypeDao extends DataSourceDao {
	public void insertRecord(ContactType ct) throws DaoException {
		super.update(
			"INSERT INTO contacttype (contactTypeID, contactTypeName, isArchived) " +
			"VALUES (#contactTypeID#, #contactTypeName#, #isArchived#)"
		, ct);
	}
	
	public void updateRecord(ContactType ct) throws DaoException {
		super.update(
			"UPDATE contacttype " +
			"SET contactTypeName = #contactTypeName#, " +
			"    isArchived      = #isArchived# " +
			"WHERE contactTypeID = #contactTypeID# "
		, ct);
	}
	
	public ContactType selectRecord(String contactTypeID) throws DaoException {
		Collection col = super.select(
			"SELECT contactTypeID, contactTypeName, isArchived " +
			"FROM contacttype " +
			"WHERE contactTypeID = ? "
		, ContactType.class, new String[] {contactTypeID}, 0, 1);
		
		ContactType ct = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			ct = (ContactType) iterator.next(); 
		}
		
		return (ct);
	}
	
	public Collection listRecords(String sort, boolean desc, int start, int rows) throws DaoException {
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "contactTypeName") + ((desc) ? " DESC" : "");
		
		Collection col = super.select(
			"SELECT contactTypeID, contactTypeName, isArchived " +
			"FROM contacttype " +
			"WHERE contactTypeID <> '0' " +
			orderBy
		, ContactType.class, null, start, rows);
		
		return (col);
	}
	
	public int count() throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM contacttype WHERE contactTypeID <> '0' ", HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public boolean isUnique(ContactType ct) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM contacttype WHERE contactTypeName = ? AND contactTypeID <> ? ", HashMap.class, new Object[] {ct.getContactTypeName(), ct.getContactTypeID()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}

    public void deleteContactType(String id) throws DaoException {
        DefaultDataObject defaultDataObject = new DefaultDataObject();
        defaultDataObject.setId(id);
        super.update("DELETE FROM contacttype WHERE contactTypeID= #id#",defaultDataObject);

    }

}