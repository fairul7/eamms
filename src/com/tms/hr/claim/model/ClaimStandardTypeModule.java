/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.model;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

import java.util.Collection;

public class ClaimStandardTypeModule extends DefaultModule
{
	public static String DEFAULTID = "default";

	public static String STATUS_ACTIVE = "act";
	public static String STATUS_INACTIVE = "ina";

	Log log = Log.getLog(getClass());

	public boolean addObject(ClaimStandardType cst)
	{
		ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
		try
		{
			dao.insertRecord(cst);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error adding ClaimStandardType "+ ex.toString(),ex);
			return false;
		}
	}

	public boolean updateObject(ClaimStandardType cst)
	{
		ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
		try
		{
			dao.updateRecord(cst);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error updating ClaimStandardType " + ex.toString(),ex);
			return false;
		}

	}

	public ClaimStandardType getObject(String id)
	{
		ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
		try
		{
			return dao.selectRecord(id);
		}
		catch(Exception ex)
		{
			log.error("Error getting Product " + ex.toString(), ex);
			return null;
		}
	}

	public ClaimStandardType selectObject(String id)
	{
		return getObject(id);
	}


	public Collection selectObjects(String field, String value, int start, int number)
	{
		ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
		try
		{
			return dao.selectRecords(null,field,value, start, number,null,false);
		}
		catch(DaoException ex)	
		{
			log.error("Error selecting objects .. " + ex.toString(), ex);
			return null;
		}
	}
    
    public Collection selectObjects(String keyword,String field, String value, int start, int number,
                                    String sort, boolean desc)
	{
		ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
		try
		{
			return dao.selectRecords(keyword,field,value, start, number, sort, desc);
		}
		catch(DaoException ex)	
		{
			log.error("Error selecting objects .. " + ex.toString(), ex);
			return null;
		}
	}

   public int countObjects(String keyword) {
      ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
      try {
         return dao.count(keyword);
      } catch (DaoException e) {
         log.error("Error counting ContactType " + e.toString(), e);
         return(0);
      }
   }


	public boolean deleteObject(String id)
	{
		ClaimStandardTypeDao dao = (ClaimStandardTypeDao) getDao();
		try
		{
			if(id.equals(DEFAULTID)) return false;
//			dao.deleteRecord(id);
			ClaimStandardType obj = getObject(id);
			obj.setStatus(STATUS_INACTIVE);
			updateObject(obj);	
			return true;
		}
//		catch(DaoException ex)
		catch(Exception ex)
		{
			log.error("Error deleting object " + ex.toString(), ex);
			return false;
		}

	}


}



