/*
 * Created on Nov 17, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;
import kacang.util.Log;
import kacang.Application;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CompanyModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addCompany(Company com) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			dao.insertRecord(com);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Company " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateCompany(Company com) {
		CompanyDao dao = (CompanyDao) getDao();
        com.setLastModified(new Date());
		try {
			dao.updateRecord(com);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Company " + e.toString(), e);
			return (false);
		}
	}
	
	public Company getCompany(String companyID) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			return (dao.selectRecord(companyID));
		} catch (DaoException e) {
			log.error("Error getting Company " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getCompanyMap() {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			return (dao.getCompanyMap());
		} catch (DaoException e) {
			log.error("Error getting Company Map " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listCompanies(String keyword, String sort, boolean desc, int start, int rows) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			Collection col = dao.listRecords(keyword, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Company " + e.toString(), e);
			return (null);
		}
	}
	
	public int countCompanies(String keyword) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			return dao.count(keyword);
		} catch (DaoException e) {
			log.error("Error counting Company " + e.toString(), e);
			return(0);
		}
	}
	
	public Collection listPartners(String keyword, String sort, boolean desc, int start, int rows) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			Collection col = dao.listRecords(true, keyword, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Partners " + e.toString(), e);
			return (null);
		}
	}
	
	public int countPartners(String keyword) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			return dao.count(true, keyword);
		} catch (DaoException e) {
			log.error("Error counting Partners " + e.toString(), e);
			return(0);
		}
	}
	
	public boolean isUnique(Company com) {
		CompanyDao dao = (CompanyDao) getDao();
		try {
			return dao.isUnique(com);
		} catch (DaoException e) {
			log.error("Error isUnique Company " + e.toString(), e);
			return(false);
		}
	}

    public int countClosedSales(String companyId){
        CompanyDao dao = (CompanyDao) getDao();
        try {
            return dao.countClosedSales(companyId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;

    }

    public int countOpportunites(String companyId){
        CompanyDao dao = (CompanyDao) getDao();
        try {
            return dao.countOpportunities(companyId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;

    }

    public int countCompanyByCompanyType(String companyTypeId){
        CompanyDao dao = (CompanyDao) getDao();
        try {
            return dao.countCompanyByCompanyType(companyTypeId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;

    }
    
    public void deleteCompany(String companyId){
        ContactModule cm = (ContactModule)Application.getInstance().getModule(ContactModule.class);
        cm.deleteCompanyContacts(companyId);

        CompanyDao dao = (CompanyDao) getDao();
        try {
            dao.deleteCompany(companyId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }


    }

}
