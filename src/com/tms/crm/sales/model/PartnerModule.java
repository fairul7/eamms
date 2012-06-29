/*
 * Created on Jan 15, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;
import kacang.util.Log;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PartnerModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public Collection getPartnerTypes() {
		PartnerDao dao = (PartnerDao) getDao();
		try {
			return (dao.getPartnerTypes());
		} catch (DaoException e) {
			log.error("Error getting Partner Types " + e.toString(), e);
			return (null);
		}
	}
	
	public String getPartnerTypeName(int companyPartnerTypeID) {
		PartnerDao dao = (PartnerDao) getDao();
		try {
			return (dao.getPartnerTypeName(companyPartnerTypeID));
		} catch (DaoException e) {
			log.error("Error getting Partner Type Name " + e.toString(), e);
			return (null);
		}
	}
}
