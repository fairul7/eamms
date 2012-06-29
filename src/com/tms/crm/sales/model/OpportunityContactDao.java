/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;
import com.tms.crm.sales.misc.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityContactDao extends DataSourceDao {
	public void insertRecord(OpportunityContact opCon) throws DaoException {
		super.update(
			"INSERT INTO opportunitycontact (opportunityID, opportunityContactType, contactID, contactTypeID) " +
			"VALUES (#opportunityID#, #opportunityContactType#, #contactID#, #contactTypeID#)"
		, opCon);
	}

    public void insertClosedOpportunityContact(Contact opCon) throws DaoException {
        super.update(
                "INSERT INTO sfa_closedsale_contact(contactID, companyID, contactLastName, contactFirstName, contactDesignation, salutationID, contactStreet1, contactStreet2, contactCity, contactState, contactPostcode, contactCountry, contactDirectNum, contactMobile, contactEmail, contactRemarks) " +
                "VALUES (#contactID#, #companyID#, #contactLastName#, #contactFirstName#, #contactDesignation#, #salutationID#, #contactStreet1#, #contactStreet2#, #contactCity#, #contactState#, #contactPostcode#, #contactCountry#, #contactDirectNum#, #contactMobile#, #contactEmail#, #contactRemarks#)"
                , opCon);
    }


	public void updateRecord(OpportunityContact opCon) throws DaoException {
		super.update(
			"UPDATE opportunitycontact " +
			"SET contactTypeID = #contactTypeID# " +
			"WHERE opportunityID          = #opportunityID# " +
			"  AND contactID              = #contactID# " +
			"  AND opportunityContactType = #opportunityContactType# "
		, opCon);
	}
	
	public void deleteRecord(String opportunityID) throws DaoException {
		super.update("DELETE FROM opportunitycontact WHERE opportunityID = ?", new String[] {opportunityID});
	}
	
	public void deleteRecord(OpportunityContact opCon) throws DaoException {
		super.update("DELETE FROM opportunitycontact WHERE opportunityID = #opportunityID# AND contactID = #contactID# AND opportunityContactType = #opportunityContactType#", opCon);
	}
	
	public void deleteRecords(String opportunityID, String opportunityContactType) throws DaoException {
		super.update("DELETE FROM opportunitycontact WHERE opportunityID = ? AND opportunityContactType = ?", new String[] {opportunityID, opportunityContactType});
	}
	
	public OpportunityContact selectRecord(String opportunityID, String contactID, String opportunityContactType) throws DaoException {
		Collection col = super.select(
			"SELECT opportunityID, opportunityContactType, opportunitycontact.contactID, contactTypeID, contactLastName, contactFirstName " +
			"FROM opportunitycontact, contact " +
			"WHERE opportunityID = ? " +
			"  AND opportunitycontact.contactID = contact.contactID " + 
			"  AND opportunitycontact.contactID = ? " +
			"  AND opportunityContactType = ? "
		, OpportunityContact.class, new String[] {opportunityID, contactID, opportunityContactType}, 0, 1);
		
		OpportunityContact opCon = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			opCon = (OpportunityContact) iterator.next(); 
		}
		
		return (opCon);
	}
	
	public Collection getContactTypeCollection() throws DaoException {
		Collection col = super.select(
			"SELECT contactTypeID, contactTypeName " +
			"FROM contacttype " +
			"WHERE isArchived = '0' " +
			"ORDER BY contactTypeName"
		, HashMap.class, null, 0, -1);
		return (col);
	}
	
	public Map getContactTypeMap() throws DaoException {
		Map map = MyUtil.collectionToMap(getContactTypeCollection(), "contactTypeID", "contactTypeName");
		return (map);
	}
	
	public Collection listRecords(String opportunityID, String opportunityContactType, String sort, boolean desc, int start, int rows) throws DaoException {
		String ascDescOrder = ((desc) ? " DESC" : "");
		String mySort;
		if (sort == null || sort.equals("contactLastName")) {
			mySort = "contactLastName" + ascDescOrder + ", contactFirstName" + ascDescOrder;
		} else if (sort.equals("contactFirstName")) {
			mySort = "contactFirstName" + ascDescOrder + ", contactLastName" + ascDescOrder;
		} else {
			mySort = sort + ascDescOrder;
		}
		String orderBy = "ORDER BY " + mySort;
		
		Collection col = super.select(
			"SELECT opportunityID, opportunityContactType, opportunitycontact.contactID, contactTypeID, contactLastName, contactFirstName " +
			"FROM opportunitycontact, contact " +
			"WHERE opportunityID = ? " +
			"  AND opportunitycontact.contactID = contact.contactID " +
			"  AND opportunityContactType = ? " +
			orderBy
		, OpportunityContact.class, new String[] {opportunityID, opportunityContactType}, start, rows);
		
		return (col);
	}
	
	public int count(String opportunityID, String opportunityContactType) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM opportunitycontact WHERE opportunityID = ? AND opportunityContactType = ? ", HashMap.class, new String[] {opportunityID, opportunityContactType}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}

    public int countContactOpportunities(String contactId) throws DaoException {
        Collection col = super.select("SELECT COUNT(o.opportunityID) AS total FROM opportunitycontact oc,opportunity o " +
                "WHERE oc.contactID = ? AND o.opportunityID = oc.opportunityID AND o.opportunityStatus <> 100 AND o.opportunityStatus <> 3",HashMap.class,new Object[]{contactId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }

    public int countContactByContactType(String contactTypeId) throws DaoException {
        Collection col = super.select("SELECT COUNT(oc.opportunityID) AS total FROM opportunitycontact oc " +
                "WHERE oc.contactTypeID = ?",HashMap.class,new Object[]{contactTypeId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }
    
    public int countContactBySalutation(String salutationId) throws DaoException {
        Collection col = super.select("SELECT COUNT(contactID) AS total FROM sfa_closedsale_contact " +
                "WHERE salutationID = ?",HashMap.class,new Object[]{salutationId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }
    
    public Collection selectOpportunites(String contactId) throws DaoException {
        String sql =
                "SELECT o.opportunityID,o.opportunityName,opportunityValue,opportunityStart,opportunityEnd " +
                " FROM opportunity o, opportunitycontact oc WHERE oc.contactID =? AND oc.opportunityID = o.opportunityID" +
                " AND o.opportunityStatus <> 100 AND o.opportunityStatus <> 3 " +
                " ORDER BY opportunityName";
        return super.select(sql,Opportunity.class,new Object[]{contactId},0,-1);


    }


}
