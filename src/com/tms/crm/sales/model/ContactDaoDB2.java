package com.tms.crm.sales.model;

import java.util.Collection;
import java.util.HashMap;

import com.tms.crm.sales.misc.MyUtil;

import kacang.model.DaoException;

public class ContactDaoDB2 extends ContactDao{
	
	public Collection listRecords(String keyword, String companyID, String opportunityID, String sort, boolean desc, int start, int rows) throws DaoException {
		String selectClause = "SELECT contact.contactID, companyID, contactLastName, contactFirstName, contactDesignation, salutationID, contactStreet1, contactStreet2, contactCity, contactState, contactPostcode, contactCountry, contactDirectNum, contactMobile, contactEmail, contactRemarks ";
		if (opportunityID != null && !opportunityID.equals("")) {
			selectClause = selectClause + ", contacttype.contactTypeName ";
		}
		String whereClause = getWhereClause(keyword, companyID, opportunityID);
		String fromClause  = getFromClause(opportunityID);
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

        if(keyword!=null&&keyword.trim().length()>0){
            keyword = "%"+keyword+"%";
        }

		Collection col = super.select(
			selectClause +
			fromClause +
			whereClause +
			orderBy
		, Contact.class, (keyword==null||keyword.trim().length()==0)?null:new Object[]{keyword.toUpperCase(),keyword.toUpperCase(),keyword.toUpperCase(),keyword,keyword,keyword.toUpperCase()}, start, rows);
		
		return (col);
	}
	
	public int count(String keyword, String companyID, String opportunityID) throws DaoException {
		String whereClause = getWhereClause(keyword, companyID, opportunityID);
		String fromClause  = getFromClause(opportunityID);
        if(keyword!=null&&keyword.trim().length()>0){
            keyword = "%"+keyword+"%";
        }
		Collection list = super.select("SELECT COUNT(*) AS total " + fromClause + whereClause, HashMap.class,  (keyword==null||keyword.trim().length()==0)?null:new Object[]{keyword.toUpperCase(),keyword.toUpperCase(),keyword.toUpperCase(),keyword,keyword,keyword.toUpperCase()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	private String getWhereClause(String keyword, String companyID, String opportunityID) {
		String whereClause = "";
		
		if (keyword != null && keyword.trim().length() != 0) {
			keyword = keyword.trim();
			whereClause = whereClause + "(UPPER(contactLastName)  LIKE ? OR UPPER(contactFirstName)  LIKE ? OR UPPER(contactDesignation)  LIKE ? OR contactDirectNum LIKE ? OR contactMobile LIKE ? OR UPPER(contactEmail) LIKE ? )";
		}
		
		if (companyID != null && !companyID.equals("")) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "companyID = '" + MyUtil.escapeSingleQuotes(companyID) + "' ";
		}
		
		// filter by contacts tied to the opportunity
		if (opportunityID != null && !opportunityID.equals("")) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunitycontact.opportunityID = '" + MyUtil.escapeSingleQuotes(opportunityID) + "' ";
			
			whereClause = whereClause + "AND contact.contactID = opportunitycontact.contactID AND opportunitycontact.contactTypeID = contacttype.contactTypeID ";
		}
		
		if (whereClause.length() != 0) {
			whereClause = "WHERE " + whereClause;
		}
		
		return (whereClause);
	}
	
	private String getFromClause(String opportunityID) {
		if (opportunityID != null && !opportunityID.equals("")) {
			return "FROM contact, opportunitycontact, contacttype ";
		} else {
			return "FROM contact ";
		}
	}

}
