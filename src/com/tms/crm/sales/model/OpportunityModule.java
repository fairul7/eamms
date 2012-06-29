/*
 * Created on Dec 5, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;

import kacang.Application;
import kacang.model.*;
import kacang.util.Log;
import com.tms.crm.sales.misc.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addOpportunity(Opportunity opp) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			dao.insertRecord(opp);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Opportunity " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateOpportunity(Opportunity opp) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			Opportunity oldOpp = getOpportunity(opp.getOpportunityID());
			Date oldDate = DateUtil.getDateOnly(oldOpp.getModifiedDate());
			Date newDate = DateUtil.getDateOnly(opp.getModifiedDate());
			if (!oldDate.equals(newDate)) {
				Application application = Application.getInstance();
				OpportunityArchiveModule arcModule = (OpportunityArchiveModule) application.getModule(OpportunityArchiveModule.class);
				
				// Write to the archive
				OpportunityArchive oppArc = new OpportunityArchive(oldOpp);
				arcModule.addOpportunityArchive(oppArc);
			}
			dao.updateRecord(opp);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating Opportunity " + e.toString(), e);
			return (false);
		}
	}
	
	public void updateOpportunity(String opportunityID, double opportunityValue) {
		Opportunity opp = getOpportunity(opportunityID);
		opp.setOpportunityValue(opportunityValue);
		updateOpportunity(opp);
	}
	
	/**
	 * Note: cascade deletes the Opportunity
	 */
	public boolean deleteOpportunity(String opportunityID) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			dao.deleteRecord(opportunityID);
			return (true);
		} catch (DaoException e) {
			log.error("Error deleting Opportunity " + e.toString(), e);
			return (false);
		}
	}
	
	public Opportunity getOpportunity(String opportunityID) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return (dao.selectRecord(opportunityID));
		} catch (DaoException e) {
			log.error("Error getting Opportunity " + e.toString(), e);
			return (null);
		}
	}
	
	Collection getStageCollection() {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return (dao.getStageCollection());
		} catch (DaoException e) {
			log.error("Error getting Stage Collection " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getSourceCollection() {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return (dao.getSourceCollection());
		} catch (DaoException e) {
			log.error("Error getting Source Collection " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getSourceMap() {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return (dao.getSourceMap());
		} catch (DaoException e) {
			log.error("Error getting Source Map " + e.toString(), e);
			return (null);
		}
	}

	public Collection listOpportunities(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
		return listOpportunities(keyword, companyID, accountManagerID, stageID, statusFilter, showClosed, showLost, fromDate, toDate, null, null, sort, desc, start, rows);
	}

	public Collection listOpportunities(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost, Date fromDate, Date toDate, Date startFromDate, Date startToDate, String sort, boolean desc, int start, int rows) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			Collection col = dao.listRecords(keyword, companyID, accountManagerID, stageID, statusFilter,showClosed,showLost, fromDate, toDate, startFromDate, startToDate, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Opportunity " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listOpportunitiesByProducts(String productID, Date fromDate, Date toDate, Date startFromDate, Date startToDate) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			Collection col = dao.listOpportunitiesByProducts(productID, fromDate, toDate, startFromDate, startToDate);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Opportunity " + e.toString(), e);
			return (null);
		}
	}

    public Collection listLostOpportunities(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) {
        OpportunityDao dao = (OpportunityDao) getDao();
        try {
            Collection col = dao.listLostRecords(keyword, companyID, accountManagerID, stageID, statusFilter,showClosed,showLost, fromDate, toDate, sort, desc, start, rows);
            return (col);
        } catch (DaoException e) {
            log.error("Error listing Opportunity " + e.toString(), e);
            return (null);
        }
    }

	public int countOpportunities(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost, Date fromDate, Date toDate) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return dao.count(keyword, companyID, accountManagerID, stageID, statusFilter,showClosed,showLost, fromDate, toDate);
		} catch (DaoException e) {
			log.error("Error counting Opportunity " + e.toString(), e);
			return(0);
		}
	}

	public int countOppourtunityBySource(String sourceId){
		OpportunityDao dao = (OpportunityDao) getDao();
        try {
            return dao.countOppourtunityBySource(sourceId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;
    }

	public Collection listCompanies(String search, String sort, boolean desc, int start, int rows) throws Exception {
			OpportunityDao dao = (OpportunityDao) getDao();
			try {
				return dao.listCompanies(search, sort, desc, start, rows);
			} catch (Exception e) {
				throw new Exception(e.toString());
			}
		}

	public Opportunity getFinancilSetting() {
			OpportunityDao dao = (OpportunityDao) getDao();
			try {
				return (dao.getFinancilSetting());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return (null);
			}
		}

	public void insertSetting(Opportunity alert)	{
		OpportunityDao notify = (OpportunityDao) getDao();
		try {
			notify.insertSetting(alert);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	public void updateSetting(Opportunity setting) throws Exception {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			dao.updateSetting(setting);
			} catch (Exception e) {
			throw new Exception(e.toString());
		}
     }

	public String getMonthsText(int month)	{
	  DateFormatSymbols symbols = new DateFormatSymbols();
         String[] months =  symbols.getMonths();
         if(month<12)
             return months[month];
         else
             return "";
	}

	public Opportunity getMonthlyProjection(String month, String userId, String year) {
			OpportunityDao dao = (OpportunityDao) getDao();
			try {
				return (dao.getMontlyProection(month,userId,year));
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return (null);
			}
		}

	public Opportunity getMonthlySales(String month, String userId, String year) {
			OpportunityDao dao = (OpportunityDao) getDao();
			try {
				return (dao.getMonthlySales(month,userId,year));
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return (null);
			}
		}

	public String addDecimal(double amount){
		DecimalFormat df = new DecimalFormat("0.00");
		String temp = df.format(amount);
		return temp;
	}

	public Collection getUsers(String userType, String query, String sort, boolean desc) throws Exception {
			OpportunityDao dao = (OpportunityDao) getDao();
			try {
				return dao.listUsers(userType,query,sort, desc);
			} catch (Exception e) {
				throw new Exception(e.toString());
			}
		}

	public Opportunity getDeletedUsername(String userId) {
				OpportunityDao dao = (OpportunityDao) getDao();
				try {
					return (dao.getDeletedUsername(userId));
				} catch (DaoException e) {
					Log.getLog(getClass()).error(e.toString(), e);
					return (null);
				}
			}


	public Opportunity getAccountManager(String userID) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return (dao.getAccountManager(userID));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Account Manager " + e.toString(), e);
			return (null);
		}
	}

	/*public Opportunity getDeltedAccountManager(String userID) {
		OpportunityDao dao = (OpportunityDao) getDao();
		try {
			return (dao.getAccountManager(userID));
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getting Account Manager " + e.toString(), e);
			return (null);
		}
	}*/


}
