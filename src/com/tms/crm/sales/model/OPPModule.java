package com.tms.crm.sales.model;

import java.util.Collection;
import java.util.*;
import java.text.*;

import kacang.Application;
import kacang.model.*;
import kacang.util.*;

import com.tms.crm.sales.model.InvalidDataException;
import com.tms.crm.sales.model.OPPDao;
import com.tms.crm.sales.misc.*;

public class OPPModule extends DefaultModule {

	private Collection rows = null;
	private String distPerc = "100";
	
	/* *** */
	
	public String calculatePercentage(double value1, double value2) {
		DecimalFormat form = new DecimalFormat("0.0");
		double percentage = 0.0f;
		if (value2 != 0.0f) {
			percentage = value1 / value2 * 100;
		}
		return form.format(percentage);
	}
	
	/* *** */
	
	public Collection selectOpportunity() {
		try {
			OPPDao dao = (OPPDao)getDao();
			return dao.selectOpportunity();
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectOpportunity", e);
			e.printStackTrace();
			return null;
		}
	} 
	
	public Collection selectOpportunity(String date) {
		try {
			OPPDao dao = (OPPDao)getDao();
			return dao.selectOpportunity(date);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectOpportunity", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/* *** */
	
	public void insertProjection(Projection p) throws InvalidDataException {
		if (p == null) {
			throw new InvalidDataException();
		}
		
		try {
			OPPDao dao = (OPPDao)getDao();
			dao.insertProjection(p);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error insertProjection", e);
			e.printStackTrace();
		}
	}
	
	public Collection selectProjection() {
		try {
			OPPDao dao = (OPPDao)getDao();
			return dao.selectProjection();
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectProjection", e);
			e.printStackTrace();
			return null;
		}
	} 
	
	public Collection selectProjection(String userid) {
		try {
			OPPDao dao = (OPPDao)getDao();
			return dao.selectProjection(userid);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectProjection", e);
			e.printStackTrace();
			return null;
		}
	}
	
	public Projection selectProjection(String userid, String year) {
		try {
			OPPDao dao = (OPPDao)getDao();
			return dao.selectProjection(userid, year);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectProjection", e);
			e.printStackTrace();
			return null;
		}
	}  
	
	public void updateProjection(Projection p) throws InvalidDataException {
		if (p == null) {
			throw new InvalidDataException();
		}
		
		try {
			OPPDao dao = (OPPDao)getDao();
			dao.updateProjection(p);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error updateProjection", e);
			e.printStackTrace();
		}
	}
	
	/* *** */
	
	
	public int calculateCurrProj(Date fromDate, Date toDate, String userid) {
		int fromMonth = DateUtil.getMonth(fromDate);
		int fromYear  = DateUtil.getYear(fromDate);
		int toMonth   = DateUtil.getMonth(toDate);
		int toYear    = DateUtil.getYear(toDate);
		
		if (fromYear == toYear) {
			// within the same year 
			return calculateCurrProj(fromYear, fromMonth, toMonth, userid);
		} else if (toYear > fromYear) {
			// more than 1 year, do recursive
			int result = 
				calculateCurrProj(fromYear, fromMonth, Calendar.DECEMBER, userid) +
				calculateCurrProj(DateUtil.beginningOfYear(fromYear + 1), toDate, userid);
			return result;
		} else {
			return -1;
		}
	}
	
	private int calculateCurrProj(int year, int fromMonth, int toMonth, String userid) {
		int endTotal = 0;
		
		try {
			OPPDao dao = (OPPDao)getDao();
			
			Collection col;
			if ("".equals(userid.trim())) {
				col = dao.selectProjection(year);
			} else {
				col = dao.selectProjection(userid, year);
			}

			if (col != null) {
				Iterator i = col.iterator();
				while (i.hasNext()) {
					Projection q = (Projection) i.next();
					for (int j=fromMonth; j<=toMonth; j++) {
						endTotal = endTotal + q.getMonth(j).intValue();
					}
				}
			}
			return endTotal;
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error calculateCurrProj", e);
			e.printStackTrace();
			return -1;
		}
	}
	
	public int calculateCurrMonthProj(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfMonth(date);
		Date toDate   = DateUtil.endOfMonth(date);
		return calculateCurrProj(fromDate, toDate, userid);
	} 
	
	public int calculateCurrQuarterProj(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfQuarter(date);
		Date toDate   = DateUtil.endOfQuarter(date);
		return calculateCurrProj(fromDate, toDate, userid);
	} 
	
	public int calculateCurrAnnualProj(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfYear(date);
		Date toDate   = DateUtil.endOfYear(date);
		return calculateCurrProj(fromDate, toDate, userid);
	}
	
	public int calculateCurrProj_Group(Date fromDate, Date toDate, String groupID) {
		AccountManagerModule module = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
		Collection col = module.getGroupMembers(groupID);
		
		int currProj = 0;
        if(col==null||col.size()==0)
            return currProj;
		Iterator iterator = col.iterator();
		while (iterator.hasNext()) {
			AccountManager am = (AccountManager) iterator.next();
			currProj = currProj + calculateCurrProj(fromDate, toDate, am.getId());
		}
		
		return currProj;
	}

    public Collection listProjections(DaoQuery query,String sort, boolean desc, int sIndex, int rows){
        OPPDao dao = (OPPDao) getDao();
        try {
            return dao.listProjection(query,sort,desc,sIndex,rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

        return null;
    }

	public double calculateCurrOpp(Date fromDate, Date toDate, String userid) {
		double endTotal = 0;
		
		try {
			OPPDao dao = (OPPDao)getDao();
			
			Collection col;
			if ("".equals(userid.trim())) {
				col = dao.selectOpportunity(fromDate, toDate);
			} else {
				col = dao.selectOpportunity(fromDate, toDate, userid);
			}

			if (col != null) {
				for (Iterator i = col.iterator(); i.hasNext();) {
					Opportunity3 q = (Opportunity3) i.next();	
					double oppValue = q.getOpportunityValue();
					String distPerc = q.getDistributionPercentage();
					if (distPerc == null) {
						distPerc = "100";
					}
					double percentage = Float.parseFloat(distPerc) / 100.0f;
					Integer status = q.getOpportunityStatus();
					
					if (!status.equals(Opportunity.STATUS_CLOSE)) {
						endTotal = endTotal + (oppValue * percentage);
					}
				}
			}
			return endTotal;
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error calculateCurrOpp", e);
			e.printStackTrace();
			return -1.0f;
		}
	}
	
	public double calculateCurrMonthOpp(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfMonth(date);
		Date toDate   = DateUtil.endOfMonth(date);
		return calculateCurrOpp(fromDate, toDate, userid);
	}
	
	public double calculateCurrQuarterOpp(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfQuarter(date);
		Date toDate   = DateUtil.endOfQuarter(date);
		return calculateCurrOpp(fromDate, toDate, userid);
	}
	
	public double calculateCurrAnnualOpp(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfYear(date);
		Date toDate   = DateUtil.endOfYear(date);
		return calculateCurrOpp(fromDate, toDate, userid);
	}
	
	public double calculateCurrOpp_Group(Date fromDate, Date toDate, String groupID) {
		AccountManagerModule module = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
		Collection col = module.getGroupMembers(groupID);
		
		double currOpp = 0;
		Iterator iterator = col.iterator();
		while (iterator.hasNext()) {
			AccountManager am = (AccountManager) iterator.next();
			currOpp = currOpp + calculateCurrOpp(fromDate, toDate, am.getId());
		}
		
		return currOpp;
	}
	
	public double calculateCurrWeightedOpp(Date fromDate, Date toDate, String userid) {
		double endTotal = 0;
		
		try {
			OPPDao dao = (OPPDao)getDao();
			
			Collection col;
			if ("".equals(userid.trim())) {
				col = dao.selectOpportunity(fromDate, toDate);
			} else {
				col = dao.selectOpportunity(fromDate, toDate, userid);
			}

			if (col != null) {
				for (Iterator i = col.iterator(); i.hasNext();) {
					Opportunity3 q = (Opportunity3) i.next();	
					double oppValue = q.getOpportunityValue();
					String distPerc = q.getDistributionPercentage();
					if (distPerc == null) {
						distPerc = "100";
					}
					double percentage = Float.parseFloat(distPerc) / 100.0f;
					Integer status = q.getOpportunityStatus();
					Integer stage = q.getOpportunityStage();
					double stagePerc = Opportunity.getStagePercent(stage);
					
					if (!status.equals(Opportunity.STATUS_CLOSE)) {
						endTotal = endTotal + (oppValue * stagePerc * percentage);
					}
				}
			}
			return endTotal;
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error calculateCurrWeightedOpp", e);
			e.printStackTrace();
			return -1.0f;
		}
	}
	
	public double calculateCurrMonthWeightedOpp(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfMonth(date);
		Date toDate   = DateUtil.endOfMonth(date);
		return calculateCurrWeightedOpp(fromDate, toDate, userid);
	}
	
	public double calculateCurrQuarterWeightedOpp(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfQuarter(date);
		Date toDate   = DateUtil.endOfQuarter(date);
		return calculateCurrWeightedOpp(fromDate, toDate, userid);
	}
	
	public double calculateCurrAnnualWeightedOpp(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfYear(date);
		Date toDate   = DateUtil.endOfYear(date);
		return calculateCurrWeightedOpp(fromDate, toDate, userid);
	}
	
	public double calculateCurrWeightedOpp_Group(Date fromDate, Date toDate, String groupID) {
		AccountManagerModule module = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
		Collection col = module.getGroupMembers(groupID);
		
		double currWeightedOpp = 0;
		Iterator iterator = col.iterator();
		while (iterator.hasNext()) {
			AccountManager am = (AccountManager) iterator.next();
			currWeightedOpp = currWeightedOpp + calculateCurrWeightedOpp(fromDate, toDate, am.getId());
		}
		
		return currWeightedOpp;
	}
	
	public double calculateCurrSales(Date fromDate, Date toDate, String userid) {
		double endTotal = 0;
		
		try {
			OPPDao dao = (OPPDao)getDao();
			
			Collection col;
			if ("".equals(userid.trim())) {
				col = dao.selectSales(fromDate, toDate);
			} else {
				col = dao.selectSales(fromDate, toDate, userid);
			}

			if (col != null) {
				for (Iterator i = col.iterator(); i.hasNext();) {
					Opportunity3 q = (Opportunity3) i.next();	
					double oppValue = q.getOpportunityValue();
					String distPerc = q.getDistributionPercentage();
					if (distPerc == null) {
						distPerc = "100";
					}
					double percentage = Float.parseFloat(distPerc) / 100.0f;
					Integer status = q.getOpportunityStatus();
					
					if (status.equals(Opportunity.STATUS_CLOSE)) {
						endTotal = endTotal + (oppValue /** percentage*/);
					}
				}
			}
			return endTotal;
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error calculateCurrSales", e);
			e.printStackTrace();
			return -1.0f;
		}
	}
	
	public double calculateCurrMonthSales(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfMonth(date);
		Date toDate   = DateUtil.endOfMonth(date);
		return calculateCurrSales(fromDate, toDate, userid);
	}
	
	public double calculateCurrQuarterSales(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfQuarter(date);
		Date toDate   = DateUtil.endOfQuarter(date);
		return calculateCurrSales(fromDate, toDate, userid);
	}
	
	public double calculateCurrAnnualSales(Date date, String userid) {
		Date fromDate = DateUtil.beginningOfYear(date);
		Date toDate   = DateUtil.endOfYear(date);
		return calculateCurrSales(fromDate, toDate, userid);
	}
	
	public double calculateCurrSales_Group(Date fromDate, Date toDate, String groupID) {
		AccountManagerModule module = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
		Collection col = module.getGroupMembers(groupID);

        Collection opps = new ArrayList();
		double currSales = 0;
        if(col==null||col.size()==0)
            return currSales;
		Iterator iterator = col.iterator();
        ArrayList ids = new ArrayList();
		while (iterator.hasNext()) {
			AccountManager am = (AccountManager) iterator.next();
            Collection tempCol = module.getSales(am.getId(),fromDate,toDate);
            for (Iterator iterator1 = tempCol.iterator(); iterator1.hasNext();) {
                Opportunity opportunity = (Opportunity) iterator1.next();
                if(ids.contains(opportunity.getOpportunityID()))
                    iterator1.remove();
                else{
                    ids.add(opportunity.getOpportunityID());
                    opps.add(opportunity);
                }
            }
					//currSales = currSales + calculateCurrSales(fromDate, toDate, am.getId());
		}
        for (Iterator iterator1 = opps.iterator(); iterator1.hasNext();) {
            Opportunity opportunity = (Opportunity) iterator1.next();
            currSales += opportunity.getOpportunityValue();
        }

		return currSales;
	}
	
	public void deleteProjection(String projectionId){
		OPPDao dao = (OPPDao) getDao();
        try {
            dao.deleteProjection(projectionId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }


    }
}