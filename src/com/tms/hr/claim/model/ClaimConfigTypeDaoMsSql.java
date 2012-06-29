package com.tms.hr.claim.model;

import java.util.Collection;

import kacang.model.DaoException;

public class ClaimConfigTypeDaoMsSql extends ClaimConfigTypeDao{
	
	public Collection retrieveAllTypeDepartment() throws DaoException {
        return super.select("SELECT typeid, departmentid from claim_form_typedept",
            ClaimTypeDepartObject.class, "", 0, -1);
    }
	
	public ClaimTypeObject retrieveType(String id) throws DaoException {
        Collection result = super.select("SELECT id, typeName, accountcode FROM claim_form_type WHERE id=?",
                ClaimTypeObject.class, new Object[] { id }, 0, 1);

        if (result != null) {
            return (ClaimTypeObject) result.iterator().next();
        }

        return null;
    }
	
	public ClaimTypeObject selectTypeName(String typeid)
	    throws DaoException {
	    
	    Collection result = super.select("SELECT id, typeName, accountcode from claim_form_type WHERE id=?",
	            ClaimTypeObject.class, new Object[] { typeid }, 0, 1);
	
	    if (result != null) {
	        return (ClaimTypeObject) result.iterator().next();
	    }
	
	    return null;
	}
	
	public Collection retrieveAllType(String keyword,String sort, boolean desc, int start,
	        int rows) throws DaoException {
	        String orderBy = (sort != null) ? sort : "typeName";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        if(keyword !=null && !("".equals(keyword))){
	        	keyword ="'%"+keyword+"%'";
	        	return super.select("SELECT id, typeName, accountcode FROM claim_form_type WHERE typeName LIKE "+keyword+" Order By " +
	        			orderBy, ClaimTypeObject.class, null, start, rows);
	        }
	        else
	        	return super.select("SELECT id, typeName, accountcode FROM claim_form_type Order By " +
	        			orderBy, ClaimTypeObject.class, null, start, rows);
	    }

}
