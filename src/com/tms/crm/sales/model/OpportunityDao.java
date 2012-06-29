/*
 * Created on Dec 5, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DataSourceFactory;
import kacang.util.JdbcUtil;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.crm.sales.misc.MyUtil;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityDao extends DataSourceDao {
    public void init() throws DaoException {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.

		try {
			super.update("CREATE TABLE financial_year (id varchar(100) NOT NULL default '0', yearEnds varchar(50) default '0' , currencySymbol varchar(20) default NULL, PRIMARY KEY(id) ) TYPE=MyISAM;",null);
			String sql = "INSERT INTO financial_year (id, yearEnds, currencySymbol) VALUES( 'e6d98320-c0a8c894-d7581b00-922148d4', '11', 'RM')";
			super.update(sql, null);
		} catch(Exception e) {
			//e.printStackTrace();
		}

        try{
            super.update("ALTER TABLE opportunityproduct CHANGE opValue opValue DOUBLE DEFAULT \"0\" NOT NULL",null);
            super.update("ALTER TABLE company ADD lastModified DATETIME",null);
            super.update("ALTER TABLE company CHANGE companyType companyType VARCHAR(255)  NOT NULL",null);
            super.update("ALTER TABLE opportunity CHANGE opportunityValue opportunityValue DOUBLE DEFAULT \"0\" NOT NULL",null);
            super.update("ALTER TABLE opportunityarchive CHANGE opportunityValue opportunityValue DOUBLE DEFAULT \"0\" NOT NULL",null);
            super.update("ALTER TABLE opportunityproductarchiveitem CHANGE opValue opValue DOUBLE DEFAULT \"0\" NOT NULL",null);
            super.update("ALTER TABLE salesgroup ADD description TEXT ",null);
            super.update("ALTER TABLE salesgroup ADD name VARCHAR(255) ",null);
        }catch(DaoException e){
            
        }


        super.update("CREATE TABLE sfa_lead ( id varchar(255) default NULL, creationDate datetime default '0000-00-00 00:00:00', companyId varchar(255) default '0', companyName varchar(255) default '0', modifiedDate datetime default '0000-00-00 00:00:00', modifiedBy varchar(255) default '0', remarks text, source varchar(255) default NULL, tel varchar(255) default NULL, userId varchar(255) default NULL, contactName varchar(255) default NULL) TYPE=MyISAM;",null);
        super.update("CREATE TABLE sfa_groupuser (  groupId varchar(255) default '0',  userId varchar(255) default '0') TYPE=MyISAM;",null);
        super.update("CREATE TABLE sfa_closedsale_contact (  contactID varchar(255) NOT NULL default '',  companyID varchar(255) NOT NULL default '',  contactLastName varchar(100) NOT NULL default '',  contactFirstName varchar(100) NOT NULL default '',  contactDesignation varchar(100) NOT NULL default '',  salutationID varchar(255) NOT NULL default '0',  contactStreet1 varchar(255) NOT NULL default '',  contactStreet2 varchar(255) NOT NULL default '',  contactCity varchar(50) NOT NULL default '',  contactState varchar(50) NOT NULL default '',  contactPostcode varchar(20) NOT NULL default '',  contactCountry varchar(50) NOT NULL default '',  contactDirectNum varchar(30) NOT NULL default '',  contactMobile varchar(30) NOT NULL default '',  contactEmail varchar(100) NOT NULL default '',  contactRemarks text NOT NULL,  PRIMARY KEY  (contactID)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE sfa_companytype (  id varchar(255) default '0',  type varchar(255) default '0',  archived char(1) binary default NULL) TYPE=MyISAM;",null);
        super.update("CREATE TABLE accountdistribution (  opportunityID varchar(255) NOT NULL default '',  distributionSequence int(11) NOT NULL default '0',  userID varchar(255) NOT NULL default '',  distributionPercentage int(11) NOT NULL default '0',  PRIMARY KEY  (opportunityID,distributionSequence)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE company (  companyID varchar(255) NOT NULL default '',  companyName varchar(255) NOT NULL default '',  companyType varchar(255) NOT NULL default '',  companyStreet1 varchar(255) NOT NULL default '',  companyStreet2 varchar(255) NOT NULL default '',  companyCity varchar(50) NOT NULL default '',  companyState varchar(50) NOT NULL default '',  companyPostcode varchar(20) NOT NULL default '',  companyCountry varchar(50) NOT NULL default '',  companyTel varchar(30) NOT NULL default '',  companyFax varchar(30) NOT NULL default '',  companyWebsite varchar(255) NOT NULL default '',  companyPartnerTypeID int(11) default NULL,  lastModified datetime default NULL,  PRIMARY KEY  (companyID),  UNIQUE KEY company_companyName (companyName)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE companypartnertype (  companyPartnerTypeID int(11) NOT NULL default '0',  companyPartnerTypeName varchar(255) NOT NULL default '',  PRIMARY KEY  (companyPartnerTypeID)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE contact (  contactID varchar(255) NOT NULL default '',  companyID varchar(255) NOT NULL default '',  contactLastName varchar(100) NOT NULL default '',  contactFirstName varchar(100) NOT NULL default '',  contactDesignation varchar(100) NOT NULL default '',  salutationID varchar(255) NOT NULL default '0',  contactStreet1 varchar(255) NOT NULL default '',  contactStreet2 varchar(255) NOT NULL default '',  contactCity varchar(50) NOT NULL default '',  contactState varchar(50) NOT NULL default '',  contactPostcode varchar(20) NOT NULL default '',  contactCountry varchar(50) NOT NULL default '',  contactDirectNum varchar(30) NOT NULL default '',  contactMobile varchar(30) NOT NULL default '',  contactEmail varchar(100) NOT NULL default '',  contactRemarks text NOT NULL,  PRIMARY KEY  (contactID)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE contacttype (  contactTypeID varchar(255) NOT NULL default '',  contactTypeName varchar(255) NOT NULL default '',  isArchived char(1) NOT NULL default '0',  PRIMARY KEY  (contactTypeID),  UNIQUE KEY contacttype_contactTypeName (contactTypeName)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE opportunity (  opportunityID varchar(255) NOT NULL default '',  companyID varchar(255) NOT NULL default '',  opportunityName varchar(100) NOT NULL default '',  opportunityStatus int(11) NOT NULL default '0',  opportunityStage int(11) NOT NULL default '0',  opportunityValue double NOT NULL default '0',  opportunityStart date NOT NULL default '0000-00-00',  opportunityEnd date NOT NULL default '0000-00-00',  opportunitySource varchar(255) NOT NULL default '0',  opportunityLastRemarks text NOT NULL,  hasPartner char(1) NOT NULL default '',  partnerCompanyID varchar(255) default NULL,  creationDateTime datetime NOT NULL default '0000-00-00 00:00:00',  modifiedDate date NOT NULL default '0000-00-00',  modifiedBy varchar(255) NOT NULL default '',  closeReferenceNo varchar(100) default NULL,  PRIMARY KEY  (opportunityID),  KEY opportunity_companyID (companyID),  KEY opportunity_opportunityEnd (opportunityEnd)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE opportunityarchive (  opportunityID varchar(255) NOT NULL default '',  opportunityName varchar(100) NOT NULL default '',  opportunityStatus int(11) NOT NULL default '0',  opportunityStage int(11) NOT NULL default '0',  opportunityValue double NOT NULL default '0',  opportunityStart date NOT NULL default '0000-00-00',  opportunityEnd date NOT NULL default '0000-00-00',  opportunityLastRemarks text NOT NULL,  hasPartner char(1) NOT NULL default '',  partnerCompanyID varchar(255) default NULL,  modifiedDate date NOT NULL default '0000-00-00',  modifiedBy varchar(255) NOT NULL default '',  PRIMARY KEY  (opportunityID,modifiedDate)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE opportunitycontact (  opportunityID varchar(255) NOT NULL default '',  opportunityContactType char(1) NOT NULL default '',  contactID varchar(255) NOT NULL default '',  contactTypeID varchar(255) NOT NULL default '0',  KEY opportunitycontact_opportunityID (opportunityID)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE opportunityproduct (  opportunityID varchar(255) NOT NULL default '',  productSeq int(11) NOT NULL default '0',  productID varchar(255) NOT NULL default '',  opDesc varchar(255) NOT NULL default '',  opValue double NOT NULL default '0',  modifiedDate date NOT NULL default '0000-00-00',  modifiedBy varchar(255) NOT NULL default '',  PRIMARY KEY  (opportunityID,productSeq)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE opportunityproductarchive (  opportunityID varchar(255) NOT NULL default '',  archiveSet int(11) NOT NULL default '0',  archivedOn date NOT NULL default '0000-00-00',  PRIMARY KEY  (opportunityID,archiveSet)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE opportunityproductarchiveitem (  opportunityID varchar(255) NOT NULL default '',  productSeq int(11) NOT NULL default '0',  productID varchar(255) NOT NULL default '',  opDesc varchar(255) NOT NULL default '',  opValue double NOT NULL default '0',  modifiedDate date NOT NULL default '0000-00-00',  modifiedBy varchar(255) NOT NULL default '',  archiveSet int(11) NOT NULL default '0',  PRIMARY KEY  (opportunityID,productSeq,archiveSet)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE product (  productID varchar(255) NOT NULL default '',  productName varchar(255) NOT NULL default '',  isArchived char(1) NOT NULL default '',  PRIMARY KEY  (productID),  UNIQUE KEY product_productName (productName)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE projection (  projectionID varchar(255) NOT NULL default '',  userID varchar(255) NOT NULL default '',  year int(11) NOT NULL default '0',  month1 int(11) NOT NULL default '0',  month2 int(11) NOT NULL default '0',  month3 int(11) NOT NULL default '0',  month4 int(11) NOT NULL default '0',  month5 int(11) NOT NULL default '0',  month6 int(11) NOT NULL default '0',  month7 int(11) NOT NULL default '0',  month8 int(11) NOT NULL default '0',  month9 int(11) NOT NULL default '0',  month10 int(11) NOT NULL default '0',  month11 int(11) NOT NULL default '0',  month12 int(11) NOT NULL default '0',  PRIMARY KEY  (projectionID)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE salesgroup (  id varchar(255) NOT NULL default '',  name varchar(255) default NULL,  description varchar(255) default NULL,  PRIMARY KEY  (id)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE salutation (  salutationID varchar(255) NOT NULL default '',  salutationText varchar(50) NOT NULL default '',  isArchived char(1) NOT NULL default '0',  PRIMARY KEY  (salutationID),  UNIQUE KEY salutation_salutationText (salutationText)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE sfa_dbversion (  dbversion float NOT NULL default '0') TYPE=MyISAM;",null);
        super.update("CREATE TABLE source (  sourceID varchar(255) NOT NULL default '',  sourceText varchar(50) NOT NULL default '',  isArchived char(1) NOT NULL default '0',  PRIMARY KEY  (sourceID),  UNIQUE KEY source_sourceText (sourceText)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE stage (  stageID int(11) NOT NULL default '0',  stagePercent tinyint(2) unsigned NOT NULL default '0',  stageText varchar(255) NOT NULL default '',  PRIMARY KEY  (stageID)) TYPE=MyISAM;",null);




    }


	public void insertRecord(Opportunity opp) throws DaoException {
		super.update(
			"INSERT INTO opportunity (opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunitySource, opportunityLastRemarks, hasPartner, partnerCompanyID, creationDateTime, modifiedDate, modifiedBy, closeReferenceNo) " +
			"VALUES (#opportunityID#, #companyID#, #opportunityName#, #opportunityStatus#, #opportunityStage#, #opportunityValue#, #opportunityStart#, #opportunityEnd#, #opportunitySource#, #opportunityLastRemarks#, #hasPartner#, #partnerCompanyID#, #creationDateTime#, #modifiedDate#, #modifiedBy#, #closeReferenceNo#)"
		, opp);
	}

	public void updateRecord(Opportunity opp) throws DaoException {
		super.update(
			"UPDATE opportunity " +
			"SET companyID              = #companyID#, " +
			"    opportunityName        = #opportunityName#, " +
			"    opportunityStatus      = #opportunityStatus#, " +
			"    opportunityStage       = #opportunityStage#, " +
			"    opportunityValue       = #opportunityValue#, " +
			"    opportunityStart       = #opportunityStart#, " +
			"    opportunityEnd         = #opportunityEnd#, " +
			"    opportunitySource      = #opportunitySource#, " +
			"    opportunityLastRemarks = #opportunityLastRemarks#, " +
			"    hasPartner             = #hasPartner#, " +
			"    partnerCompanyID       = #partnerCompanyID#, " +
			"    creationDateTime       = #creationDateTime#, " +
			"    modifiedDate           = #modifiedDate#, " +
			"    modifiedBy             = #modifiedBy#, " +
			"    closeReferenceNo       = #closeReferenceNo# " +
			"WHERE opportunityID = #opportunityID#"
		, opp);
	}
	
	public Opportunity selectRecord(String opportunityID) throws DaoException {
		Collection col = super.select(
			"SELECT opportunityID, companyID, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunitySource, opportunityLastRemarks, hasPartner, partnerCompanyID, creationDateTime, modifiedDate, modifiedBy, closeReferenceNo " +
			"FROM opportunity " +
			"WHERE opportunityID = ? "
		, Opportunity.class, new String[] {opportunityID}, 0, 1);
		
		Opportunity opp = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			opp = (Opportunity) iterator.next(); 
		}
		
		return (opp);
	}
	
	/**
	 * Note: cascade deletes the Opportunity
	 */
	public void deleteRecord(String opportunityID) throws DaoException {
		boolean status = true;
		Application application = Application.getInstance();
		
		// deletes the archive
		OpportunityArchiveModule oaModule = (OpportunityArchiveModule) application.getModule(OpportunityArchiveModule.class);
		status = oaModule.deleteOpportunityArchive(opportunityID);
		if (!status) {
			throw (new DaoException());
		}
		
		// deletes the Opportunity-Product
		OpportunityProductModule opModule = (OpportunityProductModule) application.getModule(OpportunityProductModule.class); 
		status = opModule.deleteOpportunityProduct(opportunityID);
		if (!status) {
			throw (new DaoException());
		}
		
		// deletes the Opportunity-Contact
		OpportunityContactModule ocModule = (OpportunityContactModule) application.getModule(OpportunityContactModule.class);
		status = ocModule.deleteOpportunityContacts(opportunityID);
		if (!status) {
			throw (new DaoException());
		}
		
		// deletes the Account Distribution
		AccountDistributionModule adModule = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
		adModule.deleteAccountDistribution(opportunityID);
		
		// deletes the opportunity
		deleteRecord_Opportunity(opportunityID);
	}
	
	protected void deleteRecord_Opportunity(String opportunityID) throws DaoException {
		super.update("DELETE FROM opportunity WHERE opportunityID = ?", new String[] {opportunityID});
	}
	
	Collection getStageCollection() throws DaoException {
		Collection col = super.select(
			"SELECT stageID, stagePercent, stageText " +
			"FROM stage " +
			"ORDER BY stageID "
		, HashMap.class, null, 0, -1);
		return (col);
	}
	
	public Collection getSourceCollection() throws DaoException {
		Collection col = super.select(
			"SELECT sourceID, sourceText " +
			"FROM source " +
			"WHERE isArchived = '0' " +
			"ORDER BY sourceText"
		, HashMap.class, null, 0, -1);
		return (col);
	}
	
	public Map getSourceMap() throws DaoException {
		Map map = MyUtil.collectionToMap(getSourceCollection(), "sourceID", "sourceText");
		return (map);
	}
	
	protected String getFromClause(String accountManagerID) {
		String fromClause = "FROM opportunity, company ";
		
		if (accountManagerID != null && !accountManagerID.equals("")) {
			fromClause = fromClause + ", accountdistribution "; 
		}
		
		return(fromClause);
	}
	
	/**
	 * 
	 * @param statusFilter	if -100, will exclude closed sales
	 * @param keyword
	 * @param companyID
	 * @param accountManagerID
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected String getWhereClause(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter, boolean showClosed,boolean showLost, Date fromDate, Date toDate) {
		return getWhereClause(keyword, companyID, accountManagerID, stageID, statusFilter, showClosed, showLost, fromDate, toDate, null, null);
	}

	protected String getWhereClause(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter, boolean showClosed,boolean showLost, Date fromDate, Date toDate, Date startFromDate, Date startToDate) {
		String whereClause = "opportunity.companyID = company.companyID ";
		
		if (keyword != null && keyword.trim().length() != 0) {
			keyword = keyword.trim();
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + 
					"(opportunityName LIKE '%" + keyword + "%' " +
					" OR  companyName LIKE '%" + keyword + "%') ";
		}
		
		if (companyID != null && !companyID.equals("")) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunity.companyID = '" + MyUtil.escapeSingleQuotes(companyID) + "' ";
		}
		
		if (accountManagerID != null && !accountManagerID.equals("")) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunity.opportunityID = accountdistribution.opportunityID ";
			whereClause = whereClause + "AND accountdistribution.userID = '" + MyUtil.escapeSingleQuotes(accountManagerID) + "' ";
		}
		
		if (stageID != null && !stageID.equals("")) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunityStage = " + Integer.parseInt(stageID) + " ";
		}
		
		if (statusFilter == -100 ) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
		} else if (statusFilter == -1000) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
            showClosed = true;
            showLost = true;
			// return all status
		} else {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "(opportunityStatus = " + statusFilter + " ";
		}

        if(showClosed){
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_CLOSE + " ";
        }
        if(showLost){
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_LOST+ " ";
        }
        whereClause = whereClause+ ") ";
		if (fromDate != null) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunityEnd >= '" + DateUtil.getDateTimeString(fromDate) + "' ";
		}
		
		if (toDate != null) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunityEnd <= '" + DateUtil.getDateTimeString(toDate) + "' ";
		}

		if (startFromDate != null) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunityStart >= '" + DateUtil.getDateTimeString(startFromDate) + "' ";
		}

		if (startToDate != null) {
			if (whereClause.length() != 0) whereClause = whereClause + "AND ";
			whereClause = whereClause + "opportunityStart <= '" + DateUtil.getDateTimeString(startToDate) + "' ";
		}

		if (whereClause.length() != 0) {
			whereClause = "WHERE " + whereClause;
		}
		
		return (whereClause);
	}


    protected String getLostWhereClause(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter, boolean showClosed,boolean showLost, Date fromDate, Date toDate) {
        String whereClause = "opportunity.companyID = company.companyID ";

        if (keyword != null && keyword.trim().length() != 0) {
            keyword = keyword.trim();
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause +
                    "(opportunityName LIKE '%" + keyword + "%' " +
                    " OR  companyName LIKE '%" + keyword + "%') ";
        }

        if (companyID != null && !companyID.equals("")) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "opportunity.companyID = '" + MyUtil.escapeSingleQuotes(companyID) + "' ";
        }

        if (accountManagerID != null && !accountManagerID.equals("")) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "opportunity.opportunityID = accountdistribution.opportunityID ";
            whereClause = whereClause + "AND accountdistribution.userID = '" + MyUtil.escapeSingleQuotes(accountManagerID) + "' ";
        }

        if (stageID != null && !stageID.equals("")) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "opportunityStage = " + Integer.parseInt(stageID) + " ";
        }

        if (statusFilter == -100 ) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
        } else if (statusFilter == -1000) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = 0 OR opportunityStatus = 1 or opportunityStatus = 2  ";
            showClosed = true;
            showLost = true;
            // return all status
        } else {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            whereClause = whereClause + "(opportunityStatus = " + statusFilter + " ";
        }

        if(showClosed){
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_CLOSE + " ";
        }
        if(showLost){
            whereClause =  whereClause + " OR opportunityStatus = "+ Opportunity.STATUS_LOST+ " ";
        }
        whereClause = whereClause+ ") ";
        if (fromDate != null) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            if (showClosed) {
                whereClause += "opportunityEnd";
            }
            else {
                whereClause += "modifiedDate";
            }
            whereClause = whereClause + " >= '" + DateUtil.getDateTimeString(fromDate) + "' ";
        }

        if (toDate != null) {
            if (whereClause.length() != 0) whereClause = whereClause + "AND ";
            if (showClosed) {
                whereClause += "opportunityEnd";
            }
            else {
                whereClause += "modifiedDate";
            }
            whereClause = whereClause + " <= '" + DateUtil.getDateTimeString(toDate) + "' ";
        }


        if (whereClause.length() != 0) {
            whereClause = "WHERE " + whereClause;
        }

        return (whereClause);
    }

	public Collection listRecords(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost, Date fromDate, Date toDate, Date startFromDate, Date startToDate, String sort, boolean desc, int start, int rows) throws DaoException {
		String fromClause  = getFromClause(accountManagerID);
		String whereClause = getWhereClause(keyword, companyID, accountManagerID, stageID, statusFilter,showClosed,showLost, fromDate, toDate, startFromDate, startToDate);
		String orderBy     = "ORDER BY " + ((sort != null) ? sort : "opportunityName") + ((desc) ? " DESC" : " ASC");

		String sql = " SELECT opportunity.opportunityID, opportunity.companyID, company.companyName, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, creationDateTime, modifiedDate, modifiedBy, closeReferenceNo " +
					fromClause + whereClause + orderBy;
		Collection col = super.select(sql, Opportunity.class, null, start, rows);
		return (col);
	}
	
	public Collection listOpportunitiesByProducts(String productID,  Date fromDate, Date toDate, Date startFromDate, Date startToDate) throws DaoException {

		String sql = "SELECT opportunityproduct.opValue, opportunity.opportunityID, opportunity.companyID, company.companyName, opportunity.opportunityName, opportunity.opportunityStatus, " +
			"opportunity.opportunityStage, opportunity.opportunityValue, opportunity.opportunityStart, opportunity.opportunityEnd, opportunity.opportunityLastRemarks, opportunity.hasPartner, opportunity.partnerCompanyID, " +
			"opportunity.creationDateTime, opportunity.modifiedDate, opportunity.modifiedBy, opportunity.closeReferenceNo " +
			"FROM opportunity, company, opportunityproduct WHERE opportunity.companyID = company.companyID " +
			"AND opportunity.opportunityID=opportunityproduct.opportunityID AND opportunityproduct.productID=? ";
		Collection col = null;

		if ((fromDate != null && toDate != null) && (startFromDate == null && startToDate == null) ) {
			sql = sql + " AND opportunity.opportunityEnd >=? AND opportunity.opportunityEnd<=? AND opportunity.opportunityStatus=1 ORDER BY companyName";
			col = super.select(sql, Opportunity.class, new String[]{productID, DateUtil.getDateTimeString(fromDate),DateUtil.getDateTimeString(toDate)}, 0, -1);
		}

		if ( (startFromDate != null && startToDate != null) && (fromDate == null && toDate == null) ) {
			sql = sql + " AND opportunity.opportunityStart >=? AND opportunity.opportunityStart<=? AND opportunity.opportunityStatus=1 ORDER BY companyName";
			col = super.select(sql, Opportunity.class, new String[]{productID, DateUtil.getDateTimeString(startFromDate),DateUtil.getDateTimeString(startToDate)}, 0, -1);
		}

		if ((startFromDate != null && startToDate != null) && (fromDate != null && toDate != null) ) {
			
			sql = sql + " AND (opportunity.opportunityEnd >=? AND opportunity.opportunityEnd<=?) AND (opportunity.opportunityStart >=? AND opportunity.opportunityStart<=?) AND opportunity.opportunityStatus=1 ORDER BY companyName";
			col = super.select(sql, Opportunity.class, new String[]{productID, DateUtil.getDateTimeString(fromDate),DateUtil.getDateTimeString(toDate), DateUtil.getDateTimeString(startFromDate), DateUtil.getDateTimeString(startToDate)}, 0, -1);
	
		}
		return (col);
	}

    public Collection listLostRecords(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost, Date fromDate, Date toDate, String sort, boolean desc, int start, int rows) throws DaoException {
        String fromClause  = getFromClause(accountManagerID);
        String whereClause = getLostWhereClause(keyword, companyID, accountManagerID, stageID, statusFilter,showClosed,showLost, fromDate, toDate);
        String orderBy     = "ORDER BY " + ((sort != null) ? sort : "opportunityName") + ((desc) ? " DESC" : "");

        Collection col = super.select(
                "SELECT opportunity.opportunityID, opportunity.companyID, company.companyName, opportunityName, opportunityStatus, opportunityStage, opportunityValue, opportunityStart, opportunityEnd, opportunityLastRemarks, hasPartner, partnerCompanyID, creationDateTime, modifiedDate, modifiedBy, closeReferenceNo " +
                fromClause +
                whereClause +
                orderBy
                , Opportunity.class, null, start, rows);

        return (col);
    }
	public int count(String keyword, String companyID, String accountManagerID, String stageID, int statusFilter,boolean showClosed,boolean showLost,Date fromDate, Date toDate) throws DaoException {
		String fromClause  = getFromClause(accountManagerID);
		String whereClause = getWhereClause(keyword, companyID, accountManagerID, stageID, statusFilter,showClosed,showLost, fromDate, toDate );
		
		Collection list = super.select("SELECT COUNT(*) AS total " + fromClause + whereClause, HashMap.class, null, 0, 1);
		HashMap map = (HashMap)list.iterator().next();
		return Integer.parseInt(map.get("total").toString());
	}
	
	public int countOppourtunityBySource(String sourceId) throws DaoException {
        String sql = "SELECT COUNT(opportunityID) AS total FROM opportunity " +
                "WHERE opportunitySource=? ";
        Collection col = super.select(sql,HashMap.class,new Object[]{sourceId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }

	public Collection listCompanies(String search, String sort, boolean desc, int start, int rows) throws DaoException {
		String sql = "SELECT companyName AS companyNameStr, companyID AS companyID FROM company";
		if (search != null && !search.equals("-1") && search != null && !search.equals(""))	{
			sql = sql + " WHERE companyName LIKE '%"+search+"%' " ;
		}

		if (sort != null && !sort.equals(""))	{
			sql = sql + " ORDER BY '" +sort + "'";
			if (desc) {
            	sql = sql + " DESC";
        	}
		}
		return super.select(sql, Opportunity.class, null, start, rows);
	}

	public int getCompaniesList(String search, String criteria) throws DaoException {
		String sql = "SELECT  COUNT(*)  as total FROM company";
		if (search != null && !search.equals("-1") && search != null && !search.equals(""))	{
			sql = sql + " WHERE companyName LIKE '%"+search+"%' " ;
		}
		HashMap row = (HashMap) super.select(sql, HashMap.class, null, 0, -1).iterator().next();
		Number number = (Number)row.get("total");
		return number.intValue();
	}

	public Opportunity getFinancilSetting() throws DaoException {
		Collection col = super.select("SELECT id AS fyId, yearEnds AS yearEnds, currencySymbol AS currencySymbol FROM financial_year", Opportunity.class, null, 0, 1);
		Opportunity cd = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			cd = (Opportunity) iterator.next();
		}
		return (cd);
	}

	public void insertSetting(Opportunity setting) throws Exception {
		String sql = "INSERT INTO financial_year (id, yearEnds) VALUES (#fyId#,#yearEnds#)";
		super.update(sql, setting);
	}

	public void updateSetting(Opportunity setting) throws Exception {
		super.update("UPDATE financial_year SET yearEnds=#yearEnds#, currencySymbol=#currencySymbol# WHERE id=#fyId#", setting);
	}

	public Opportunity getMontlyProection(String month, String userId, String year) throws DaoException {
			String sql = "SELECT "+month+" AS monthlyProjection FROM projection WHERE userID='"+userId+"' AND year="+year;
			Collection col = super.select(sql, Opportunity.class, null, 0, 1);
			Opportunity cd = null;
			if (col.size() == 1) {
				Iterator iterator = col.iterator();
				cd = (Opportunity) iterator.next();
			}
			return (cd);
		}

	public Opportunity getMonthlySales(String month, String userId, String year) throws DaoException {
		// TODO: find out why super.select doesnt work.
/*		String sql = " SELECT SUM((o.opportunityValue*ad.distributionPercentage)/100) AS opportunityValue FROM opportunity o " +
				   " INNER JOIN accountdistribution ad ON (o.opportunityID=ad.opportunityID) "+
				   " WHERE MONTH(opportunityEnd)="+month+" AND YEAR(opportunityEnd)="+year+" AND ad.userID='"+userId+"' AND opportunityStatus=100";

				Collection col = super.select(sql, Opportunity.class, null, 0, 1);
				Opportunity cd = null;
				if (col.size() == 1) {
					Iterator iterator = col.iterator();
					cd = (Opportunity) iterator.next();
				}
				return (cd);*/
		
		 	Connection con = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    Opportunity opp = null;
		    try
		    {
		        StringBuffer buffer = new StringBuffer("");
		        buffer.append("SELECT SUM((o.opportunityValue*ad.distributionPercentage)/100) AS opportunityValue FROM ");
		        buffer.append("opportunity o INNER JOIN accountdistribution ad ON (o.opportunityID=ad.opportunityID) WHERE ");
		        buffer.append("MONTH(opportunityEnd)=? AND YEAR(opportunityEnd)=? AND ");
		        buffer.append("ad.userID=? AND ");
		        buffer.append("opportunityStatus=100");
		        
		        DataSource dataSource = DataSourceFactory.getInstance().getDataSource("defaultdb");
		        con = dataSource.getConnection();
		        stmt = JdbcUtil.getInstance().createPreparedStatement(con, buffer.toString(), new Object[]{month,year,userId});
		        rs = stmt.executeQuery();

		        while(rs.next())
		        {
		            opp = new Opportunity();
		            opp.setOpportunityValue(rs.getDouble("opportunityValue"));
		        }
		    }
		    catch(Exception e)
		    {
		    }
		    finally
		    {
		        try
		        {
		            if(con != null)  con.close();
		        }
		        catch(Exception e1) {}
		        try
		        {
		            if(stmt != null)  stmt.close();
		        }
		        catch(Exception e2) {}
		        try
		        {
		            if(rs != null)  rs.close();
		        }
		        catch(Exception e3) {}
		    }

		    return opp;
	}

	public Collection listUsers(String userType, String query, String sort, boolean desc) throws DaoException {
		String sql = "";
		if ("1".equals(userType)) {
			sql = sql + "SELECT su.id AS userId, su.username AS username, su.firstName AS firstName, su.lastName AS lastName FROM security_user su WHERE su.active='1' ";
			if (query !=null && !"".equals(query))	{
				sql = sql + " AND (su.username LIKE '%"+query+"%' OR su.firstName LIKE '%"+query+"%' OR su.lastName LIKE '%"+query+"') " ;
			}

			if (sort != null && !sort.equals(""))	{
				sql = sql + " ORDER BY '" +sort + "'";
				if (desc) {
            		sql = sql + " DESC";
        		}
			}

		} else if ("2".equals(userType)) {
			sql = sql + "SELECT su.id AS userId, su.username AS username, su.firstName AS firstName, su.lastName AS lastName FROM security_user su WHERE su.active='0' ";
			if (query !=null && !"".equals(query))	{
				sql = sql + " AND (su.username LIKE '%"+query+"%' OR su.firstName LIKE '%"+query+"%' OR su.lastName LIKE '%"+query+"') " ;
			}

			if (sort != null && !sort.equals(""))	{
				sql = sql + " ORDER BY '" +sort + "'";
				if (desc) {
            		sql = sql + " DESC";
        		}
			}

		} else if ("3".equals(userType))	{
			sql = sql + " select DISTINCT ea.userId AS userId, ea.intranetUserName AS username FROM security_user su RIGHT OUTER JOIN emlaccount ea ON su.id=ea.userId " +
						" RIGHT OUTER JOIN opportunity op ON ea.userId=op.modifiedBy WHERE su.id IS null";
			if (query !=null && !"".equals(query))	{
				sql = sql + " AND (ea.intranetUserName LIKE '%"+query+"%' )";
			}

			if (sort != null && !sort.equals(""))	{
				sql = sql + " ORDER BY '" +sort + "'";
				if (desc) {
            		sql = sql + " DESC";
        		}
			}

		} else if("4".equals(userType)) {

			if (query == null || "".equals(query)){
			sql = sql + " select su.id AS userId, su.username AS username, su.firstName AS firstName, su.lastName AS lastName FROM security_user su "+
			" UNION "+
			" select DISTINCT ea.userId AS userId, ea.intranetUserName AS username, su.firstName AS firstName, su.lastName AS lastName FROM " +
			" security_user su RIGHT OUTER JOIN emlaccount ea ON su.id=ea.userId " +
			" RIGHT OUTER JOIN opportunity op ON ea.userId=op.modifiedBy "+
			" WHERE su.id IS null ";

				if (sort != null && !sort.equals(""))	{
					sql = sql + " ORDER BY '" +sort + "'";
					if (desc) {
            			sql = sql + " DESC";
        			}
				}
			}

			if (query !=null && !"".equals(query))	{
				 sql =  sql + " select su.id AS userId, su.username AS username, su.firstName AS firstName, "+
				 " su.lastName AS lastName FROM security_user su "+
				 " WHERE (su.username LIKE '%"+query+"%' OR su.firstName LIKE '%"+query+"%' OR su.lastName LIKE '%"+query+"%') "+
				 " UNION "+
				 " select DISTINCT ea.userId AS userId, ea.intranetUserName AS username, "+
				 " su.firstName AS firstName, su.lastName AS lastName " +
				 " FROM  security_user su RIGHT OUTER JOIN emlaccount ea ON su.id=ea.userId " +
				 " RIGHT OUTER JOIN opportunity op ON ea.userId=op.modifiedBy "+
				 " WHERE su.id IS null " +
				 " AND (ea.intranetUserName LIKE '%"+query+"%' OR su.username LIKE '%"+query+"%' OR su.firstName LIKE '%"+query+"%' OR su.lastName LIKE '%"+query+"') ";

				if (sort != null && !sort.equals(""))	{
					sql = sql + " ORDER BY '" +sort + "'";
					if (desc) {
            			sql = sql + " DESC";
        			}
				}

			}
		}
		return super.select(sql, Opportunity.class, null, 0, -1);
	}

	public int listUsersCount(String userType) throws DaoException {
		String sql = "SELECT COUNT(*) AS total FROM security_user su";
		if ("1".equals(userType)) {
			sql = sql + " WHERE su.active='1' ";
		} else if ("2".equals(userType)) {
			sql = sql + " WHERE su.active='0' ";
		}

		HashMap row = (HashMap) super.select(sql, HashMap.class, null, 0, -1).iterator().next();
		Number number = (Number)row.get("total");
		return number.intValue();
	}

	public Opportunity getDeletedUsername(String userId) throws DaoException {
			String sql = "SELECT intranetUserName AS username FROM emlaccount WHERE userId='"+userId+"'";
			Collection col = super.select(sql, Opportunity.class, null, 0, 1);
			Opportunity cd = null;
			if (col.size() == 1) {
				Iterator iterator = col.iterator();
				cd = (Opportunity) iterator.next();
			}
			return (cd);
		}

	public Opportunity getAccountManager(String id) throws DaoException {
		Collection col = super.select(
			"SELECT id, firstName, lastName " +
			"FROM security_user " +
			"WHERE id = ? "
		, Opportunity.class, new String[] {id}, 0, 1);

		Opportunity am = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			am = (Opportunity) iterator.next();
		}

		return (am);
	}

	/*public Opportunity getDeletedAccountManager(String id) throws DaoException {
		Collection col = super.select(
			"SELECT id, firstName, lastName " +
			"FROM security_user " +
			"WHERE id = ? "
		, Opportunity.class, new String[] {id}, 0, 1);

		Opportunity am = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			am = (Opportunity) iterator.next();
		}

		return (am);
	}*/



}
