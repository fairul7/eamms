package com.tms.hr.claim.model;

import kacang.model.DaoException;

public class ClaimStandardTypeDaoDB2 extends ClaimStandardTypeDao
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
        "  	timeEdit TIMESTAMP default NULL, " +
        "	userOriginator varchar(255) NOT NULL default '',  " +
        "	userOwner varchar(255) NOT NULL default '', " +
        "	userApprover1 varchar(255) NOT NULL default '', " +
        "	userApprover2 varchar(255) NOT NULL default '', " +
        "	userApprover3 varchar(255) NOT NULL default '', " +
        "	userApprover4 varchar(255) NOT NULL default '', " +
        "	userAssessor varchar(255) NOT NULL default '', " +
        "	currency char(3) default 'MYR', " +
        "	amount decimal(12,4) default 0.0000, " +
        "	approvalLevelRequired INTEGER NOT NULL default 0, " +
        "	approvalLevelGranted INTEGER NOT NULL default 0, " +
        "	remarks varchar(255) default '', " +
        "	rejectReason varchar(255) default '', " +
        "	info varchar(255) default '', " +
        "	state char(3), " +
        "	status char(3), " +
        "   claimMonth TIMESTAMP, " +
        "   approvedBy CLOB(65K), " +
        "   rejectedBy CLOB(65K) "+
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
        "	timeFrom TIMESTAMP default NULL, " +
        "  	timeTo TIMESTAMP default NULL, " +
        "  	timeFinancial TIMESTAMP default NULL, " +
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
        "  	timeEdit TIMESTAMP default NULL, " +
        "	state char(3), " +
        "	status char(3) " +
        "	type varchar(10) default 'default'" +
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
        "VALUES('default','none','default','default','manager','2004-01-01 00:00:00', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('travel-mileage','mileage','Mileage','Travel (Mileage)','manager','2004-01-01 00:00:00', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('travel-parking','parking','Parking','Travel (Parking)','manager','2004-01-01 00:00:00', " +
        "	'act','act','default') ", null);
        } catch(Exception e) {
            // ignore
        }

        try{
        super.update("INSERT INTO claim_form_item_category " +
        "VALUES('travel-toll','toll','Toll','Travel (Toll)','manager','2004-01-01 00:00:00', " +
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
        "	description CLOB(65K), " +
        "	currency char(3) default 'MYR', " +
        "	amount decimal(12,4) default 0.0000, " +
        "	userEdit varchar(35) NOT NULL default '', " +
        "  	timeEdit TIMESTAMP default NULL, " +
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
        "		'MYR', '0.00', 'x','2004-01-01 00:00:00','act','act') ", null);
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
        "  	timeCreated TIMESTAMP NOT NULL default NULL, " +
        "  	timeScheduled TIMESTAMP NOT NULL default NULL, " +
        "  	timeExecuted TIMESTAMP NOT NULL default NULL, " +
        "	property1 varchar(255) NOT NULL default '', " +
        "	property2 varchar(255) NOT NULL default '', " +
        "	property3 varchar(255) NOT NULL default '', " +
        "	property4 varchar(255) NOT NULL default '', " +
        "	description CLOB(65K), " +
        "	state char(3), " +
        "	status char(3) " +
        ") ", null);
        } catch(Exception e) {
            // ignore
        }



    }
}
