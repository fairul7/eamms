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

public class ClaimConfigModule extends DefaultModule
{
	Log log = Log.getLog(getClass());

	public boolean addObject(ClaimConfig obj)
	{
		ClaimConfigDao dao = (ClaimConfigDao) getDao();
		try
		{
			dao.insertRecord(obj);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error adding ClaimConfig "+ ex.toString(),ex);
			return false;
		}
	}

	public boolean updateObject(ClaimConfig obj)
	{
		ClaimConfigDao dao = (ClaimConfigDao) getDao();
		try
		{
			dao.updateRecord(obj);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error updating ClaimConfig " + ex.toString(),ex);
			return false;
		}

	}

	public ClaimConfig selectObject(String id)
	{
		ClaimConfigDao dao = (ClaimConfigDao) getDao();
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

	public Collection selectObjects(String field, String value, 
												int start, int number)
	{
		ClaimConfigDao dao = (ClaimConfigDao) getDao();
		try
		{
			return dao.selectRecords(field,value, start, number);
		}
		catch(DaoException ex)	
		{
			log.error("Error selecting objects .. " + ex.toString(), ex);
			return null;
		}
	}


	public Collection findObjects(String[] conditions, String sort, 
				boolean desc, int start, int rows) 
	{
		ClaimConfigDao dao = (ClaimConfigDao)getDao();
		try 
		{
			return dao.query(conditions, sort, desc, start, rows);
		} 
		catch (DaoException e) 
		{
			e.printStackTrace();
			log.error("Error finding contacts " + e.toString(), e);
			throw new ClaimConfigException(e.toString());
		}
	}



/*
   public int countObjects(String status) {
      ClaimConfigDao dao = (ClaimConfigDao) getDao();
      try {
         return dao.count(String status);
      } catch (DaoException e) {
         log.error("Error counting ContactType " + e.toString(), e);
         return(0);
      }
   }
*/
	public int countObjects(String status) 
	{
		ClaimConfigDao dao = (ClaimConfigDao)getDao();
		try 
		{
			return dao.count(status);
		} 
		catch (DaoException e) 
		{
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimConfigException(e.toString());
		}
	}



	public boolean deleteObject(String id)
	{
		ClaimConfigDao dao = (ClaimConfigDao) getDao();
		try
		{
			dao.deleteRecord(id);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error deleting object " + ex.toString(), ex);
			return false;
		}

	}


}



