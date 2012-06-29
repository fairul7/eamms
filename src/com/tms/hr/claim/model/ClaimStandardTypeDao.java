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

public class ClaimStandardTypeDao extends DataSourceDao
{

    public void init() throws DaoException {
        super.init();

        try {
            super.update("ALTER TABLE claim_form_index ADD approvedBy TEXT DEFAULT ''", null);
            super.update("ALTER TABLE claim_form_index ADD rejectedBy TEXT DEFAULT ''", null);
            super.update("ALTER TABLE claim_form_index ADD claimMonth DATETIME", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("CREATE TABLE claim_form_index " +
        "( " +
        "	id varchar(35) NOT NULL default '', " +
        "  	timeEdit datetime default '0000-00-00 00:00:00', " +
        "	userOriginator varchar(255) NOT NULL default '',  " +
        "	userOwner varchar(255) NOT NULL default '', " +
        "	userApprover1 varchar(255) NOT NULL default '', " +
        "	userApprover2 varchar(255) NOT NULL default '', " +
        "	userApprover3 varchar(255) NOT NULL default '', " +
        "	userApprover4 varchar(255) NOT NULL default '', " +
        "	userAssessor varchar(255) NOT NULL default '', " +
        "	currency char(3) default 'MYR', " +
        "	amount decimal(12,4) default '0.00', " +
        "	approvalLevelRequired int NOT NULL default '0', " +
        "	approvalLevelGranted int NOT NULL default '0', " +
        "	remarks varchar(255) default '', " +
        "	rejectReason varchar(255) default '', " +
        "	info varchar(255) default '', " +
        "	state char(3), " +
        "	status char(3), " +
        "   claimMonth DATETIME, " +
        "   approvedBy TEXT, " +
        "   rejectedBy TEXT "+
        ") ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("CREATE TABLE claim_form_item " +
        "( " +
        "	id varchar(35) NOT NULL default '',  " +
        "	formId varchar(35) NOT NULL default '', " +
        "	categoryId varchar(35) NOT NULL default '',  " +
        "	standardTypeId varchar(35) NOT NULL default '',  " +
        "	projectId varchar(35) NOT NULL default '',  " +
        "	timeFrom datetime default '0000-00-00 00:00:00', " +
        "  	timeTo datetime default '0000-00-00 00:00:00', " +
        "  	timeFinancial datetime default '0000-00-00 00:00:00', " +
        "	currency char(3) default 'MYR', " +
        "	amount decimal(12,4) default '0.00', " +
        "	qty decimal(12,4) default '0.00', " +
        "	unitPrice decimal(12,4) default '0.00', " +
        "	uom varchar(255) NOT NULL default '', " +
        "	description varchar(255), " + 
        "	remarks varchar(255), " +
        "	rejectReason varchar(255), " +
        "	state char(3), " +
        "	status char(3) " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("CREATE TABLE claim_form_item_category " +
        "( " +
        "	id varchar(35) NOT NULL default '',  " +
        "	code varchar(255) NOT NULL default '', " +
        "	name varchar(255) NOT NULL default '', " +
        "	description varchar(255), " +
        "	userEdit varchar(35) NOT NULL default '', " +
        "  	timeEdit datetime default '0000-00-00 00:00:00', " +
        "	state char(3), " +
        "	status char(3) " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
            super.update("ALTER TABLE claim_form_item_category ADD PRIMARY KEY (id)",null);
        }
        catch(Exception e){
            //ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('default','none','default','default','manager','2004-01-01', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('travel-mileage','mileage','Mileage','Travel (Mileage)','manager','2004-01-01', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('travel-parking','parking','Parking','Travel (Parking)','manager','2004-01-01', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('travel-toll','toll','Toll','Travel (Toll)','manager','2004-01-01', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("CREATE TABLE claim_standard_type " +
        "( " +
        "	id varchar(35) NOT NULL default '', " +
        "	category varchar(35) NOT NULL default '',  " +
        "	code varchar(255) NOT NULL default '', " +
        "	name varchar(255) NOT NULL default '', " +
        "	description text, " +
        "	currency char(3) default 'MYR', " +
        "	amount decimal(12,4) default '0.00', " +
        "	userEdit varchar(35) NOT NULL default '', " +
        "  	timeEdit datetime default '0000-00-00 00:00:00', " +
        "	state char(3), " +
        "	status char(3) " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
            super.update("ALTER TABLE claim_standard_type ADD PRIMARY KEY (id) ",null);
        }
        catch(Exception e){

        }

        try{
        super.update("INSERT INTO claim_standard_type " +
        "VALUES('default','default','default','Default Type','The Standard Type', " +
        "		'MYR', '0.00', 'x','2004-01-01','act','act') ", null);
        } catch(Exception e) {
            // ignore
        }


        try{
        super.update("CREATE TABLE claim_project " +
        "( " +
        "	id varchar(35) NOT NULL default '', " +
        "	fkPcc varchar(35) NOT NULL default '',  " +
        "   name varchar(255) NOT NULL default '', " +
        "	description varchar(255) NOT NULL default '', " +
        "	status varchar(3) NOT NULL default '' " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }


        try{
            super.update("ALTER TABLE claim_project ADD PRIMARY KEY (id)",null);
        }
        catch(Exception e){

        }

      /*  try{
        super.update("INSERT INTO claim_project " +
        "VALUES('default','','default','default project','act') ", null);
        } catch(Exception e) {

        }*/


        try{
        super.update("CREATE TABLE claim_config " +
        "( " +
        "	id varchar(35) NOT NULL default '', " +
        "	namespace varchar(100) NOT NULL default '', " +
        "	category varchar(100) NOT NULL default '', " +
        "	property1 varchar(255) NOT NULL default '', " +
        "	property2 varchar(255) NOT NULL default '', " +
        "	property3 varchar(255) NOT NULL default '', " +
        "	property4 varchar(255) NOT NULL default '', " +
        "	property5 varchar(255) NOT NULL default '' " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("CREATE TABLE claim_event( " +
        "	id varchar(35) NOT NULL default '', " +
        "	claimFormId varchar(35) NOT NULL default '', " +
        "	namespace varchar(100) NOT NULL default '', " +
        "	category varchar(100) NOT NULL default '', " +
        "	action varchar(100) NOT NULL default '', " +
        "	userid varchar(255) NOT NULL default '',  " +
        "  	timeCreated datetime NOT NULL default '0000-00-00 00:00:00', " +
        "  	timeScheduled datetime NOT NULL default '0000-00-00 00:00:00', " +
        "  	timeExecuted datetime NOT NULL default '0000-00-00 00:00:00', " +
        "	property1 varchar(255) NOT NULL default '', " +
        "	property2 varchar(255) NOT NULL default '', " +
        "	property3 varchar(255) NOT NULL default '', " +
        "	property4 varchar(255) NOT NULL default '', " +
        "	description text, " +
        "	state char(3), " +
        "	status char(3) " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }



    }

	public static final String TABLENAME = "claim_standard_type";
	public void insertRecord(ClaimStandardType cst)
			throws DaoException
	{
		super.update(
				"INSERT INTO "+TABLENAME+" (id, category, code, name, description, currency, amount, userEdit, timeEdit, state, status )" +
				" VALUES  (#id#, #category#, #code#, #name#, #description#, #currency#, #amount#, #userEdit#, #timeEdit#, #state#, #status# )" , cst);
	}

   public void updateRecord(ClaimStandardType cst)
         throws DaoException
   {
      super.update(
            "UPDATE "+TABLENAME+" SET " +
				" category = #category# , " +
				" code = #code# , "+
				" name = #name# , "+
				" description = #description# , "+
				" currency = #currency# , " +
				" amount = #amount# , " +
				" userEdit = #userEdit# , "+
				" timeEdit = #timeEdit# , "+
				" state = #state# , "+
				" status = #status#  "+
				" WHERE id = #id# ", cst);
   }

	public Collection selectRecords(String keyword, String field1 ,String value1, int start, int number,
                                    String sort, boolean desc)
			throws DaoException
	{
		/// super.select
		//// starting row and max number of records.. 0,1
		//// -1 if grep everything

        Collection col = null;
        String s = (sort==null?"":" ORDER BY "+sort+(desc?" DESC":""));

        if(keyword !=null && !("".equals(keyword))){

           keyword = "%"+keyword+"%";

            col = super.select(
				" SELECT "+
           " id , " +
           " category , " +
            " code  , "+
            " name  , "+
            " description , "+
            " currency , " +
            " amount  , " +
            " userEdit , "+
            " timeEdit , "+
            " state ,  "+
            " status "+
				" FROM "+TABLENAME+" WHERE " + field1+" = ? "+"AND name LIKE '"+keyword+"' " +s,
			ClaimStandardType.class, new Object[] { value1 },start,number);


        }


        else
        col = super.select(
				" SELECT "+
           " id , " +
           " category , " +
            " code  , "+
            " name  , "+
            " description , "+
            " currency , " +
            " amount  , " +
            " userEdit , "+
            " timeEdit , "+
            " state ,  "+
            " status "+
				" FROM "+TABLENAME+" WHERE " + field1+" = ? "+s,
			ClaimStandardType.class, new Object[] { value1 },start,number);
/*		ClaimStandardType cst = null;
		if(col.size()>0)
		{
			Iterator iterator = col.iterator();
			oppArc = (OpportunityArchive) iterator.next();
		}
		return (oppArc);
*/
		return col;
	}


	public ClaimStandardType selectRecord(String id)
			throws DaoException
	{
		Collection col = super.select(
				" SELECT " +
           " id , " +
           " category , " +
            " code  , "+
            " name  , "+
            " description , "+
            " currency , " +
            " amount  , " +
            " userEdit , "+
            " timeEdit , "+
            " state ,  "+
            " status "+ 
				" FROM " + TABLENAME + " WHERE id = ? ",
				ClaimStandardType.class, new  Object[] { id },0,1);

		ClaimStandardType cst = null;
      if(col.size()>0)
      {
         Iterator iterator = col.iterator();
         cst = (ClaimStandardType) iterator.next();
      }
      return (cst);
	}



	public void deleteRecord(String claimStandardTypeID)
		throws DaoException
	{
		Log.getLog(this.getClass()).debug("Deleting......." + claimStandardTypeID);
		super.update("DELETE FROM "+TABLENAME+" WHERE id = ? ", new String[]{claimStandardTypeID});
	}

   public int count(String keyword) throws DaoException
	{

        Collection list =null;
        if(keyword !=null && !("".equals(keyword))){

        keyword = "'%"+keyword+"%'";

        list = super.select("SELECT COUNT(*) AS total FROM claim_standard_type WHERE id <> '' AND name LIKE "+keyword, HashMap.class, null, 0, 1);

        }
        else
        list = super.select("SELECT COUNT(*) AS total FROM claim_standard_type WHERE id <> '' ", HashMap.class, null, 0, 1);

        HashMap map = (HashMap)list.iterator().next();
      return Integer.parseInt(map.get("total").toString());
   }


}


