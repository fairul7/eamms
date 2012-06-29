package com.tms.hr.claim.model;

import kacang.model.DaoException;

public class ClaimStandardTypeDaoOracle extends ClaimStandardTypeDao
{
	public void init() throws DaoException
	{
        super.update("CREATE TABLE claim_form_index " +
			"( " +
			"	id VARCHAR(35) default '' NOT NULL, " +
			"  	timeEdit DATE, " +
			"	userOriginator VARCHAR(255) default '' NOT NULL,  " +
			"	userOwner VARCHAR(255) default '' NOT NULL, " +
			"	userApprover1 VARCHAR(255) default '' NOT NULL, " +
			"	userApprover2 VARCHAR(255) default '' NOT NULL, " +
			"	userApprover3 VARCHAR(255) default '' NOT NULL, " +
			"	userApprover4 VARCHAR(255) default '' NOT NULL, " +
			"	userAssessor VARCHAR(255) default '' NOT NULL, " +
			"	currency CHAR(3) default 'MYR', " +
			"	amount DECIMAL(12,4) default '0.00', " +
			"	approvalLevelRequired INT default '0' NOT NULL, " +
			"	approvalLevelGranted INT default '0' NOT NULL, " +
			"	remarks VARCHAR(255) default '', " +
			"	rejectReason VARCHAR(255) default '', " +
			"	info VARCHAR(255) default '', " +
			"	state CHAR(3), " +
			"	status CHAR(3) " +
			") ", null);
        super.update("CREATE TABLE claim_form_item " +
			"( " +
			"	id VARCHAR(35) default '' NOT NULL,  " +
			"	formId VARCHAR(35) default '' NOT NULL, " +
			"	categoryId VARCHAR(35) default '' NOT NULL,  " +
			"	standardTypeId VARCHAR(35) default '' NOT NULL,  " +
			"	projectId VARCHAR(35) default '' NOT NULL,  " +
			"	timeFrom DATE, " +
			"  	timeTo DATE, " +
			"  	timeFinancial DATE, " +
			"	currency CHAR(3) default 'MYR', " +
			"	amount DECIMAL(12,4) default '0.00', " +
			"	qty DECIMAL(12,4) default '0.00', " +
			"	unitPrice DECIMAL(12,4) default '0.00', " +
			"	uom VARCHAR(255) default '' NOT NULL, " +
			"	description VARCHAR(255), " +
			"	remarks VARCHAR(255), " +
			"	rejectReason VARCHAR(255), " +
			"	state CHAR(3), " +
			"	status CHAR(3) " +
			") ", null);
        super.update("CREATE TABLE claim_form_item_category " +
			"( " +
			"	id VARCHAR(35) default '' NOT NULL,  " +
			"	code VARCHAR(255) default '' NOT NULL, " +
			"	name VARCHAR(255) default '' NOT NULL, " +
			"	description VARCHAR(255), " +
			"	userEdit VARCHAR(35)default '' NOT NULL, " +
			"  	timeEdit DATE, " +
			"	state CHAR(3), " +
			"	status CHAR(3) " +
			") ", null);
        super.update("INSERT INTO claim_form_item_category " +
			"VALUES('default','none','default','default','manager',TO_DATE('2004-01-01', 'yyyy-mm-dd'), " +
			"	'act','act') ", null);
        super.update("INSERT INTO claim_form_item_category " +
			"VALUES('travel-mileage','mileage','Mileage','Travel (Mileage)','manager',TO_DATE('2004-01-01', 'yyyy-mm-dd'), " +
			"	'act','act') ", null);
        super.update("INSERT INTO claim_form_item_category " +
			"VALUES('travel-parking','parking','Parking','Travel (Parking)','manager',TO_DATE('2004-01-01', 'yyyy-mm-dd'), " +
			"	'act','act') ", null);
        super.update("INSERT INTO claim_form_item_category " +
			"VALUES('travel-toll','toll','Toll','Travel (Toll)','manager',TO_DATE('2004-01-01', 'yyyy-mm-dd'), " +
			"	'act','act') ", null);
        super.update("CREATE TABLE claim_standard_type " +
			"( " +
			"	id VARCHAR(35) default '' NOT NULL, " +
			"	category VARCHAR(35)default '' NOT NULL,  " +
			"	code VARCHAR(255) default '' NOT NULL, " +
			"	name VARCHAR(255) default '' NOT NULL, " +
			"	description CLOB, " +
			"	currency CHAR(3) default 'MYR', " +
			"	amount DECIMAL(12,4) default '0.00', " +
			"	userEdit VARCHAR(35) default '' NOT NULL, " +
			"  	timeEdit DATE, " +
			"	state CHAR(3), " +
			"	status CHAR(3) " +
			") ", null);
        super.update("INSERT INTO claim_standard_type " +
			"VALUES('default','default','default','Default Type','The Standard Type', " +
			"		'MYR', '0.00', 'x',TO_DATE('2004-01-01', 'yyyy-mm-dd'),'act','act') ", null);
        super.update("CREATE TABLE claim_project " +
			"( " +
			"	id VARCHAR(35) default '' NOT NULL, " +
			"	fkPcc VARCHAR(35) default '' NOT NULL,  " +
			"   name VARCHAR(255) default '' NOT NULL, " +
			"	description VARCHAR(255) default '' NOT NULL, " +
			"	status VARCHAR(3) default '' NOT NULL" +
			") ", null);
        super.update("INSERT INTO claim_project " +
			"VALUES('default',' ','default','default project','act') ", null);
        super.update("CREATE TABLE claim_config " +
			"( " +
			"	id VARCHAR(35) default '' NOT NULL, " +
			"	namespace VARCHAR(100) default '' NOT NULL, " +
			"	category VARCHAR(100) default '' NOT NULL, " +
			"	property1 VARCHAR(255) default '' NOT NULL, " +
			"	property2 VARCHAR(255) default '' NOT NULL, " +
			"	property3 VARCHAR(255) default '' NOT NULL, " +
			"	property4 VARCHAR(255) default '' NOT NULL, " +
			"	property5 VARCHAR(255) default '' NOT NULL" +
			") ", null);
        super.update("CREATE TABLE claim_event( " +
			"	id VARCHAR(35) default '' NOT NULL, " +
			"	claimFormId VARCHAR(35) default '' NOT NULL, " +
			"	namespace VARCHAR(100) default '' NOT NULL, " +
			"	category VARCHAR(100) default '' NOT NULL, " +
			"	action VARCHAR(100) default '' NOT NULL, " +
			"	userid VARCHAR(255) default '' NOT NULL,  " +
			"  	timeCreated DATE NOT NULL, " +
			"  	timeScheduled DATE NOT NULL, " +
			"  	timeExecuted DATE NOT NULL, " +
			"	property1 VARCHAR(255) default '' NOT NULL, " +
			"	property2 VARCHAR(255) default '' NOT NULL, " +
			"	property3 VARCHAR(255) default '' NOT NULL, " +
			"	property4 VARCHAR(255) default '' NOT NULL, " +
			"	description CLOB, " +
			"	state CHAR(3), " +
			"	status CHAR(3) " +
			") ", null);
    }
}
