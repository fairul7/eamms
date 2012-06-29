/*
 * Created on Dec 16, 2003
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
public class AccountManagerDao extends DataSourceDao {
	public AccountManager selectRecord(String id) throws DaoException {
		Collection col = super.select(
			"SELECT id, firstName, lastName, email1 " +
			"FROM security_user " +
			"WHERE id = ? " +
			"AND active = '1' "
		, AccountManager.class, new String[] {id}, 0, 1);
		
		AccountManager am = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			am = (AccountManager) iterator.next(); 
		}
		
		return (am);
	}
	
	public Collection listRecords() throws DaoException {
		Collection col = super.select(
			"SELECT id, firstName, lastName, email1 " +
			"FROM security_user " +
			"WHERE active = '1' " +
			"ORDER BY firstName, lastName "
		, AccountManager.class, null, 0, -1);
		
		return (col);
	}
	
	public String selectNames(String idList) throws DaoException {
		String names = "";
		
		if (idList != null && !idList.equals("")) {
			Collection col = super.select(
				"SELECT firstName, lastName " +
				"FROM security_user " +
				"WHERE id IN (" + idList + ") " +
				"AND active = '1' " +
				"ORDER BY firstName, lastName "
			, DefaultDataObject.class, null, 0, -1);
			
			Iterator iterator = col.iterator();
			while (iterator.hasNext()) {
				DefaultDataObject userDo = (DefaultDataObject) iterator.next();
				if (names.length() != 0) {
					names = names + ", ";
				}
				names = names + userDo.getProperty("firstName") + " " + userDo.getProperty("lastName");  
			}
		}
		
		return (names);
	}
	
	public SalesGroup getGroup(String groupID) throws DaoException {
		Collection col = super.select(
			"SELECT id, name,description " +
			"FROM salesgroup " +
			"WHERE id = ? "
		, SalesGroup.class, new String[] {groupID}, 0, 1);
		
		SalesGroup grp = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			grp = (SalesGroup) iterator.next(); 
		}
		
		return (grp);
	}
	
	public Collection getGroups() throws DaoException {
		Collection col = super.select(
			"SELECT id, groupName " +
			"FROM security_group " +
			"WHERE active = '1' " +
			"ORDER BY groupName " 
		, SalesGroup.class, null, 0, -1);
		
		return (col);
	}
	
	public Collection getGroups(String userID) throws DaoException {
		Collection col = super.select(
			"SELECT id, groupName " +
			"FROM security_group, security_user_group " +
			"WHERE security_group.id = security_user_group.groupId " +
			"AND userId = ? " +
			"AND active = '1' " +
			"ORDER BY groupName " 
		, SalesGroup.class, new String[] {userID}, 0, -1);
		
		return (col);
	}
	
	public Collection getGroupMembers(String groupID) throws DaoException {
		Collection col = super.select(
			"SELECT id, firstName, lastName, email1 " +
			"FROM security_user, sfa_groupuser gu " +
			"WHERE security_user.id = gu.userId " +
			"AND gu.groupId = ? "
		, AccountManager.class, new String[] {groupID}, 0, -1);
		
		return (col);
	}

    public Collection selectSales(String userId, Date from, Date to) throws DaoException {
        String sql =
                "SELECT o.opportunityID,o.opportunityName,opportunityValue,opportunityStart,opportunityEnd " +
                "FROM opportunity o, accountdistribution ad " +
                "WHERE ad.userID =? AND ad.opportunityID = o.opportunityID" +
                " AND o.opportunityStatus = 100 " +
                "AND opportunityEnd >= ? " +
                "AND opportunityEnd <= ? ";
        return super.select(sql,Opportunity.class,new Object[]{userId,from,to},0,-1);


    }


}
