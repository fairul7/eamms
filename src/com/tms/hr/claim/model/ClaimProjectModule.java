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

public class ClaimProjectModule extends DefaultModule
{
	public static String DEFAULTID = "default";
	public static String STATUS_INACTIVE = "ina";
	public static String STATUS_ACTIVE = "act";


	Log log = Log.getLog(getClass());

	public boolean addObject(ClaimProject obj)
	{
		ClaimProjectDao dao = (ClaimProjectDao) getDao();
		try
		{
			dao.insertRecord(obj);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error adding ClaimProject "+ ex.toString(),ex);
			return false;
		}
	}

	public boolean updateObject(ClaimProject obj)
	{
		ClaimProjectDao dao = (ClaimProjectDao) getDao();
		try
		{
			dao.updateRecord(obj);
			return true;
		}
		catch(DaoException ex)
		{
			log.error("Error updating ClaimProject " + ex.toString(),ex);
			return false;
		}

	}

	public ClaimProject selectObject(String id)
	{
		ClaimProjectDao dao = (ClaimProjectDao) getDao();
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

	public Collection selectObjects(String field, String value, int start, int number)
	{
		ClaimProjectDao dao = (ClaimProjectDao) getDao();

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
		ClaimProjectDao dao = (ClaimProjectDao) getDao();

		try
		{
			return dao.selectRecords(keyword,field,value, start, number,sort,desc);
		}
		catch(DaoException ex)	
		{
			log.error("Error selecting objects .. " + ex.toString(), ex);
			return null;
		}
	}
	public Collection findObjects(String name, String sort, 
				boolean desc, int start, int rows) 
	{
		ClaimProjectDao dao = (ClaimProjectDao)getDao();
		try 
		{
			return dao.query(name, sort, desc, start, rows);
		} 
		catch (DaoException e) 
		{
			log.error("Error finding contacts " + e.toString(), e);
			throw new ClaimProjectException(e.toString());
		}
	}



/*
   public int countObjects(String status) {
      ClaimProjectDao dao = (ClaimProjectDao) getDao();
      try {
         return dao.count(String status);
      } catch (DaoException e) {
         log.error("Error counting ContactType " + e.toString(), e);
         return(0);
      }
   }
*/
	public int countObjects(String keyword,String status) 
	{
		ClaimProjectDao dao = (ClaimProjectDao)getDao();
		try 
		{
			return dao.count(keyword,status);
		} 
		catch (DaoException e) 
		{
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimProjectException(e.toString());
		}
	}



	public boolean deleteObject(String id)
	{
		ClaimProjectDao dao = (ClaimProjectDao) getDao();
		try
		{
			if(id.equals(DEFAULTID)) return false;
			// we only set the status to inactive 
			//ClaimProject obj = selectObject(id);
			//obj.setStatus(STATUS_INACTIVE);
			//updateObject(obj);
			dao.deleteRecord(id);
			return true;
		}
		catch(Exception ex)
		{
			log.error("Error deleting object " + ex.toString(), ex);
			return false;
		}

	}


}



