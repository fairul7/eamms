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
public class SalutationDao extends DataSourceDao {
	public void insertRecord(Salutation salu) throws DaoException {
		super.update(
			"INSERT INTO salutation (salutationID, salutationText, isArchived) " +
			"VALUES (#salutationID#, #salutationText#, #isArchived#)"
		, salu);
	}
	
	public void updateRecord(Salutation salu) throws DaoException {
		super.update(
			"UPDATE salutation " +
			"SET salutationText = #salutationText#, " +
			"    isArchived     = #isArchived# " +
			"WHERE salutationID = #salutationID# "
		, salu);
	}
	
	public Salutation selectRecord(String salutationID) throws DaoException {
		Collection col = super.select(
			"SELECT salutationID, salutationText, isArchived " +
			"FROM salutation " +
			"WHERE salutationID = ? "
		, Salutation.class, new String[] {salutationID}, 0, 1);
		
		Salutation salu = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			salu = (Salutation) iterator.next(); 
		}
		
		return (salu);
	}
	
	public Collection listRecords(String sort, boolean desc, int start, int rows) throws DaoException {
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "salutationText") + ((desc) ? " DESC" : "");
		
		Collection col = super.select(
			"SELECT salutationID, salutationText, isArchived " +
			"FROM salutation " +
			"WHERE salutationID <> '0' " +
			orderBy
		, Salutation.class, null, start, rows);
		
		return (col);
	}
	
	public int count() throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM salutation WHERE salutationID <> '0' ", HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public boolean isUnique(Salutation salu) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM salutation WHERE salutationText = ? AND salutationID <> ? ", HashMap.class, new Object[] {salu.getSalutationText(), salu.getSalutationID()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}

    public void deleteSalutation(String id) throws DaoException {
        DefaultDataObject defaultDataObject = new DefaultDataObject();
        defaultDataObject.setId(id);
        super.update("DELETE FROM salutation WHERE salutationID= #id#",defaultDataObject);


    }
}