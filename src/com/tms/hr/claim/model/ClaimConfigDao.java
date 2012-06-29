/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ClaimConfigDao extends DataSourceDao
{
	public static final String PERMISSION_CONFIG_ADMIN=
									"com.tms.hr.claim.model.Admin";


	public static final String TABLENAME = "claim_config";
	public void insertRecord(ClaimConfig obj)
			throws DaoException
	{
		super.update(
		"INSERT INTO "+TABLENAME+" (id, namespace, category, property1, property2, property3, property4, property5 )" +
				" VALUES  (#id#, #namespace#, #category#, #property1#, #property2#, #property3#, #property4#, #property5# )" , obj);
	}

   public void updateRecord(ClaimConfig obj)
         throws DaoException
   {
      super.update(
            "UPDATE "+TABLENAME+" SET " +
				" namespace = #namespace# , " +
				" category = #category# , "+
				" property1 = #property1# , "+
				" property2 = #property2# , "+
				" property3 = #property3# , "+
				" property4 = #property4# , "+
				" property5 = #property5#  "+
				" WHERE id = #id# ", obj);
   }

	public Collection selectRecords(String field1 ,String value1, int start, int number)
			throws DaoException
	{
		/// super.select
		//// starting row and max number of records.. 0,1
		//// -1 if grep everything

		Collection col = super.select(
				" SELECT "+
           "  id , " +
            " namespace, "+
            " category, "+
            " property1, "+
            " property2, "+
            " property3, "+
            " property4, "+
            " property5 "+
				" FROM "+TABLENAME+" WHERE " + field1+" = ? ", 
			ClaimConfig.class, new Object[] { value1 },start,number);
/*		ClaimConfig obj = null;
		if(col.size()>0)
		{
			Iterator iterator = col.iterator();
			oppArc = (OpportunityArchive) iterator.next();
		}
		return (oppArc);
*/
		return col;
	}


	public ClaimConfig selectRecord(String id)
			throws DaoException
	{
		Collection col = super.select(
				" SELECT " +
				" id , " +
            " namespace, "+
            " category, "+
            " property1, "+
            " property2, "+
            " property3, "+
            " property4, "+
            " property5 "+
				" FROM " + TABLENAME + " WHERE id = ? ",
				ClaimConfig.class, new  Object[] { id },0,1);

		ClaimConfig obj = null;
      if(col.size()>0)
      {
         Iterator iterator = col.iterator();
         obj = (ClaimConfig) iterator.next();
      }
      return (obj);
	}



	public void deleteRecord(String claimStandardTypeID)
		throws DaoException
	{
		Log.getLog(this.getClass()).debug("Deleting......." + claimStandardTypeID);
		super.update("DELETE FROM "+TABLENAME+" WHERE id = ? ", new String[]{claimStandardTypeID});
	}

   public int count(String status) throws DaoException 
	{
      Collection list = super.select("SELECT COUNT(*) AS total FROM claim_config WHERE status = '"+status+"' ", HashMap.class, null, 0, 1);
      HashMap map = (HashMap)list.iterator().next();
      return Integer.parseInt(map.get("total").toString());
   }



	public Collection query(String[] conditions, String sort, 
									boolean desc, int start, int rows) 
		throws DaoException 
	{

		String sqlStmt = " SELECT id, namespace, category, property1, "
							+ " property2, property3, property4, property5  " 
							+ " FROM claim_config ";

		if(conditions.length>0){ sqlStmt += " WHERE "; }
		for(int cnt = 0; cnt<conditions.length; cnt++)
		{
			if(cnt>0){ sqlStmt += " AND ";}
			sqlStmt +=" "+ conditions[cnt]+" ";
		}
		if(sort!=null){ sqlStmt += " ORDER BY " + sort;}
		return super.select( sqlStmt, ClaimConfig.class, new String[]{}, start, rows);
    }




}


