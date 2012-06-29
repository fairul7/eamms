package com.tms.crm.sales.model;

import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;

public class CompanyDaoDB2 extends CompanyDao{
	
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
	
	public int count(boolean partnerOnly, String keyword) throws DaoException {
		String whereClause = getWhereClause(partnerOnly, keyword);
		
		Collection list = super.select("SELECT COUNT(*) AS total FROM company " + whereClause, HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	private String getWhereClause(boolean partnerOnly, String keyword) {
		String whereClause = "";
		
		if (keyword != null && keyword.trim().length() != 0) {
			keyword = keyword.trim();
			whereClause = whereClause + "UPPER(companyName) LIKE '%" + keyword.toUpperCase() + "%' ";
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

}
