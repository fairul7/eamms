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

public class ClaimProjectDao extends DataSourceDao
{
	public static final String TABLENAME = "claim_project";
	public void insertRecord(ClaimProject obj)
			throws DaoException
	{
		super.update(
				"INSERT INTO "+TABLENAME+" (id, fkPcc, name, description, status )" +
				" VALUES  (#id#, #fkPcc#, #name#, #description#, #status# )" , obj);
	}

   public void updateRecord(ClaimProject obj)
         throws DaoException
   {
      super.update(
            "UPDATE "+TABLENAME+" SET " +
				" fkPcc = #fkPcc# , " +
				" name = #name# , "+
				" description = #description# , "+
				" status = #status#  "+
				" WHERE id = #id# ", obj);
   }

	public Collection selectRecords(String keyword,String field1 ,String value1, int start, int number,
                                    String sort, boolean desc)
			throws DaoException
	{







        String sqlStmt = " SELECT "+
           "  id , " +
           "  fkPcc, " +
            " name  , "+
            " description , "+
            " status "+
            " FROM "+TABLENAME;


        if(field1!=null && value1!=null)
		{ sqlStmt += " WHERE " + field1+" = '"+value1+"' ";}

        if(keyword !=null && !("".equals(keyword))){

          keyword = "'%"+keyword+"%'";
          sqlStmt += " WHERE name LIKE "+keyword;
        }


        //sqlStmt += " ORDER BY status, name " ;
        sqlStmt += (sort==null?"":" ORDER BY "+sort)+(desc?" DESC":"");

		Collection col = super.select(sqlStmt,
			ClaimProject.class, new Object[] { },start,number);
/*		ClaimProject obj = null;
		if(col.size()>0)
		{
			Iterator iterator = col.iterator();
			oppArc = (OpportunityArchive) iterator.next();
		}
		return (oppArc);
*/
		return col;
	}


	public ClaimProject selectRecord(String id)
			throws DaoException
	{
		Collection col = super.select(
				" SELECT " +
           " id , " +
           " fkPcc, " +
            " name  , "+
            " description , "+
            " status "+ 
				" FROM " + TABLENAME + " WHERE id = ? ",
				ClaimProject.class, new  Object[] { id },0,1);

		ClaimProject obj = null;
      if(col.size()>0)
      {
         Iterator iterator = col.iterator();
         obj = (ClaimProject) iterator.next();
      }
      return (obj);
	}



	public void deleteRecord(String claimStandardTypeID)
		throws DaoException
	{
		Log.getLog(this.getClass()).debug("Deleting......." + claimStandardTypeID);
		super.update("DELETE FROM "+TABLENAME+" WHERE id = ? ", new String[]{claimStandardTypeID});
	}

   public int count(String keyword,String status) throws DaoException
	{
		String sqlStmt = "SELECT COUNT(*) AS total FROM claim_project ";
		if(status!=null)
		{ sqlStmt += " WHERE status = '"+status+"' ";}

        if(keyword !=null && !("".equals(keyword))){

            keyword = " '%"+keyword+"%' ";
            sqlStmt +=" AND name LIKE "+keyword;
        }
      Collection list = super.select(sqlStmt, HashMap.class, null, 0, 1);
      HashMap map = (HashMap)list.iterator().next();
      return Integer.parseInt(map.get("total").toString());
   }

	public Collection query(String name, String sort, 
									boolean desc, int start, int rows) 
		throws DaoException 
	{
		String condition = (name != null) ? "%" + name + "%" : "%";
		String orderBy = (sort != null) ? sort : "name";
		if (desc) orderBy += " DESC";
		Object[] args = { condition };

		return super.select("SELECT id, fkPcc, name, description, status FROM claim_project WHERE name LIKE ? ORDER BY " 
				+ orderBy, ClaimProject.class, args, start, rows);
    }




}


