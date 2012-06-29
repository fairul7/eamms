/*
 * Created on Jan 15, 2004
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
public class PartnerDao extends DataSourceDao {
	public Collection getPartnerTypes() throws DaoException {
		Collection col = super.select(
			"SELECT companyPartnerTypeID, companyPartnerTypeName " +
			"FROM companypartnertype " +
			"ORDER BY companyPartnerTypeID "
		, Hashtable.class, null, 0, -1);
		return col;
	}
	
	public String getPartnerTypeName(int companyPartnerTypeID) throws DaoException {
		Collection col = super.select(
			"SELECT companyPartnerTypeName " +
			"FROM companypartnertype " +
			"WHERE companyPartnerTypeID = " + companyPartnerTypeID
		, Hashtable.class, null, 0, 1);
		
		String typeName = "";
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			typeName = (String) ((Hashtable) iterator.next()).get("companyPartnerTypeName"); 
		}
		
		return typeName;
	}
}