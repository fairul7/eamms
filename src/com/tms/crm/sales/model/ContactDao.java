/*
 * Created on Dec 18, 2003
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
public class ContactDao extends DataSourceDao {

    public void init() throws DaoException {
        super.update("ALTER TABLE contact ADD dateCreated DATETIME", null);
        super.update("ALTER TABLE contact ADD dateModified DATETIME", null);
    }

	public void insertRecord(Contact con) throws DaoException {
        con.setProperty("dateCreated", new Date());
		super.update(
			"INSERT INTO contact (contactID, companyID, contactLastName, contactFirstName, contactDesignation, salutationID, contactStreet1, contactStreet2, contactCity, contactState, contactPostcode, contactCountry, contactDirectNum, contactMobile, contactEmail, contactRemarks, dateCreated) " +
			"VALUES (#contactID#, #companyID#, #contactLastName#, #contactFirstName#, #contactDesignation#, #salutationID#, #contactStreet1#, #contactStreet2#, #contactCity#, #contactState#, #contactPostcode#, #contactCountry#, #contactDirectNum#, #contactMobile#, #contactEmail#, #contactRemarks#, #dateCreated#)"
		, con);
	}
	
	public void updateRecord(Contact con) throws DaoException {
        con.setProperty("dateModified", new Date());
		super.update(
			"UPDATE contact " +
			"SET contactLastName    = #contactLastName#, " +
			"    contactFirstName   = #contactFirstName#, " +
			"    contactDesignation = #contactDesignation#, " +
			"    salutationID       = #salutationID#, " +
			"    contactStreet1     = #contactStreet1#, " +
			"    contactStreet2     = #contactStreet2#, " +
			"    contactCity        = #contactCity#, " +
			"    contactState       = #contactState#, " +
			"    contactPostcode    = #contactPostcode#, " +
			"    contactCountry     = #contactCountry#, " +
			"    contactDirectNum   = #contactDirectNum#, " +
			"    contactMobile      = #contactMobile#, " +
			"    contactEmail       = #contactEmail#, " +
			"    contactRemarks     = #contactRemarks#, " +
            "    companyID          = #companyID#,  "+
            "    dateModified       = #dateModified#  "+
			"WHERE contactID = #contactID#"
		, con);
	}
	
	public Contact selectRecord(String contactID) throws DaoException {
		Collection col = super.select(
			"SELECT contactID, companyID, contactLastName, contactFirstName, contactDesignation, salutationID, contactStreet1, contactStreet2, contactCity, contactState, contactPostcode, contactCountry, contactDirectNum, contactMobile, contactEmail, contactRemarks " +
			"FROM contact " +
			"WHERE contactID = ? "
		, Contact.class, new String[] {contactID}, 0, 1);
		
		Contact con = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			con = (Contact) iterator.next(); 
		}
		
		return (con);
	}
	
	public Collection getSalutationCollection() throws DaoException {
		Collection col = super.select(
			"SELECT salutationID, salutationText " +
			"FROM salutation " +
			"WHERE isArchived = '0' " +
			"ORDER BY salutationText"
		, HashMap.class, null, 0, -1);
		return (col);
	}
	
	public Map getSalutationMap() throws DaoException {
		Map map = MyUtil.collectionToMap(getSalutationCollection(), "salutationID", "salutationText");
		return (map);
	}
	
	private String getWhereClause(String keyword, String companyID, String opportunityID) {
		String whereClause = "";
		
		if (keyword != null && keyword.trim().length() != 0) {
			keyword = keyword.trim();
			whereClause = whereClause + "(contactLastName  LIKE ? OR contactFirstName  LIKE ? OR contactDesignation  LIKE ? OR contactDirectNum LIKE ? OR contactMobile LIKE ? OR contactEmail LIKE ? )";
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


    public Collection listClosedSaleRecords(String keyword, String companyID, String opportunityID, String sort, boolean desc, int start, int rows) throws DaoException {
        String selectClause = "SELECT contact.contactID, companyID, contactLastName, contactFirstName, contactDesignation, salutationID, contactStreet1, contactStreet2, contactCity, contactState, contactPostcode, contactCountry, contactDirectNum, contactMobile, contactEmail, contactRemarks ";
        if (opportunityID != null && !opportunityID.equals("")) {
            selectClause = selectClause + ", contacttype.contactTypeName ";
        }
        String whereClause = getWhereClause(keyword, companyID, opportunityID);
        String fromClause  = " FROM  sfa_closedsale_contact contact, opportunitycontact, contacttype ";
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
                , Contact.class,  keyword==null?null:new Object[]{keyword,keyword,keyword,keyword,keyword,keyword}, start, rows);

        return (col);
    }


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
		} else if (sort.equals("emailLink")) {
			mySort = " contactEmail "+ascDescOrder+" ";
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
		, Contact.class, (keyword==null||keyword.trim().length()==0)?null:new Object[]{keyword,keyword,keyword,keyword,keyword,keyword}, start, rows);
		
		return (col);
	}
	
	public int count(String keyword, String companyID, String opportunityID) throws DaoException {
		String whereClause = getWhereClause(keyword, companyID, opportunityID);
		String fromClause  = getFromClause(opportunityID);
        if(keyword!=null&&keyword.trim().length()>0){
            keyword = "%"+keyword+"%";
        }
		Collection list = super.select("SELECT COUNT(*) AS total " + fromClause + whereClause, HashMap.class,  (keyword==null||keyword.trim().length()==0)?null:new Object[]{keyword,keyword,keyword,keyword,keyword,keyword}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}

    public int countClosedSale(String keyword, String companyID, String opportunityID) throws DaoException {
        String whereClause = getWhereClause(keyword, companyID, opportunityID);
        String fromClause  = getFromClause(opportunityID);
        if(keyword!=null&&keyword.trim().length()>0){
            keyword = "%"+keyword+"%";
        }
        Collection list = super.select("SELECT COUNT(*) AS total  FROM  sfa_closedsale_contact contact, opportunitycontact, contacttype " +  whereClause, HashMap.class,  keyword==null?null:new Object[]{keyword,keyword,keyword,keyword,keyword,keyword}, 0, 1);
        HashMap map = (HashMap)list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public void deleteCompanyContacts(String companyId) throws DaoException {
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("companyId",companyId);
        super.update("DELETE FROM contact WHERE companyID = #companyId#",obj);

    }
    
    public int countContactBySalutation(String salutationId) throws DaoException {
        String sql = "SELECT COUNT(contactID) AS total FROM contact " +
                "WHERE salutationID=? ";
        Collection col = super.select(sql,HashMap.class,new Object[]{salutationId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }


}
