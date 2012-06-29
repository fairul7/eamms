/*
 * Created on Nov 17, 2003
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
public class CompanyDao extends DataSourceDao {
	public void init() throws DaoException {
//		System.out.println("<!-- CompanyDAO init --> " + new Date());
	}
	
	public void insertRecord(Company com) throws DaoException {
		super.update(
			"INSERT INTO company (companyID, companyName, companyType, companyStreet1, companyStreet2, companyCity, companyState, companyPostcode, companyCountry, companyTel, companyFax, companyWebsite, companyPartnerTypeID,lastModified) " +
			"VALUES (#companyID#, #companyName#, #companyType#, #companyStreet1#, #companyStreet2#, #companyCity#, #companyState#, #companyPostcode#, #companyCountry#, #companyTel#, #companyFax#, #companyWebsite#, #companyPartnerTypeID#, #lastModified#)"
		, com);
	}
	
	public void updateRecord(Company com) throws DaoException {
		super.update(
			"UPDATE company " +
			"SET companyName          = #companyName#, " +
			"    companyType          = #companyType#, " +
			"    companyStreet1       = #companyStreet1#, " +
			"    companyStreet2       = #companyStreet2#, " +
			"    companyCity          = #companyCity#, " +
			"    companyState         = #companyState#, " +
			"    companyPostcode      = #companyPostcode#, " +
			"    companyCountry       = #companyCountry#, " +
			"    companyTel           = #companyTel#, " +
			"    companyFax           = #companyFax#, " +
			"    companyWebsite       = #companyWebsite#, " +
			"    companyPartnerTypeID = #companyPartnerTypeID#, " +
            "    lastModified = #lastModified# " +
			"WHERE companyID = #companyID#"
		, com);
	}
	
	public Company selectRecord(String companyID) throws DaoException {
		Collection col = super.select(
			"SELECT companyID, companyName, ct.type as companyType , companyStreet1, companyStreet2, companyCity, companyState, companyPostcode, companyCountry, companyTel, companyFax, companyWebsite, companyPartnerTypeID " +
			"FROM company c, sfa_companytype ct " +
			"WHERE companyID = ? AND c.companyType = ct.id "
		, Company.class, new String[] {companyID}, 0, 1);
		
		Company com = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			com = (Company) iterator.next(); 
		}
		
		return (com);
	}
	
	public Map getCompanyMap() throws DaoException {
		Collection col = super.select(
			"SELECT companyID, companyName " +
			"FROM company " +
			"ORDER BY companyName"
		, HashMap.class, null, 0, -1);
		
		Map map = MyUtil.collectionToMap(col, "companyID", "companyName");
		
		return (map);
	}
	
	private String getWhereClause(boolean partnerOnly, String keyword) {
		String whereClause = "";
		
		if (keyword != null && keyword.trim().length() != 0) {
			keyword = keyword.trim();
			whereClause = whereClause + "companyName LIKE '%" + keyword + "%' ";
		}
		
		if (partnerOnly) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "companyPartnerTypeID IS NOT NULL ";
		}
		
		if (whereClause.length() != 0) {
			whereClause = "WHERE " + whereClause;
		}
		
		return (whereClause);
	}
	
	public Collection listRecords(String keyword, String sort, boolean desc, int start, int rows) throws DaoException {
		return listRecords(false, keyword, sort, desc, start, rows);
	}
	
	public Collection listRecords(boolean partnerOnly, String keyword, String sort, boolean desc, int start, int rows) throws DaoException {
		String whereClause = getWhereClause(partnerOnly, keyword);
		String orderBy = "ORDER BY " + ((sort != null) ? sort : "companyName") + ((desc) ? " DESC" : "");

        String typeClause =    " company.companyType = ct.id ";
        if(whereClause.trim().length()>0){
            whereClause += " AND "+ typeClause;
        } else{
            whereClause = " WHERE " + typeClause;
        }
		Collection col = super.select(
			"SELECT companyID, companyName,ct.type as  companyType, companyStreet1, companyStreet2, companyCity, companyState, companyPostcode, companyCountry, companyTel, companyFax, companyWebsite, companyPartnerTypeID " +
			"FROM company, sfa_companytype ct " +
			whereClause +
			orderBy
		, Company.class, null, start, rows);
		
		return (col);
	}
	
	public int count(String keyword) throws DaoException {
		return count(false, keyword);
	}
	
	public int count(boolean partnerOnly, String keyword) throws DaoException {
		String whereClause = getWhereClause(partnerOnly, keyword);
		
		Collection list = super.select("SELECT COUNT(*) AS total FROM company " + whereClause, HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public boolean isUnique(Company com) throws DaoException {
		Collection list = super.select("SELECT COUNT(*) AS total FROM company WHERE companyName = ? AND companyID <> ? ", HashMap.class, new Object[] {com.getCompanyName(), com.getCompanyID()}, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		if (Integer.parseInt(map.get("total").toString()) == 0) {
			return true;
		} else {
			return false;
		}
	}

    public int countClosedSales(String companyId) throws DaoException {
        String sql = "SELECT COUNT(opportunityID) AS total FROM opportunity " +
        "WHERE (companyID = ? OR partnerCompanyID = ? )AND opportunityStatus = 100 ";
        Collection col = super.select(sql,HashMap.class,new Object[]{companyId,companyId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }

    public int countOpportunities(String companyId) throws DaoException {
        String sql = "SELECT COUNT(opportunityID) AS total FROM opportunity " +
                "WHERE (companyID = ? OR partnerCompanyID = ? )AND opportunityStatus <> 100 ";
        Collection col = super.select(sql,HashMap.class,new Object[]{companyId,companyId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }
    
    public int countCompanyByCompanyType(String companyTypeId) throws DaoException {
        String sql = "SELECT COUNT(companyID) AS total FROM company " +
                "WHERE companyType=? ";
        Collection col = super.select(sql,HashMap.class,new Object[]{companyTypeId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }

    public void deleteCompany(String companyID) throws DaoException {
        
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("companyId",companyID);
        super.update("DELETE FROM company WHERE companyID=#companyId#",obj);


    }



}
