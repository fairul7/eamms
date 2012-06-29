package com.tms.hr.claim.model;

import kacang.model.*;

public class ClaimConfigTypeDaoSybase extends ClaimConfigTypeDaoMsSql {
	public void init() throws DaoException {
		try {
			super.init();
		} catch (DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE claim_form_item_category MODIFY [type] VARCHAR(55) NULL", null);
		} catch (DaoException e) {
		}
		
		try {
			super.update("CREATE TABLE claim_form_type (" +
					"id varchar(255) NOT NULL PRIMARY KEY," +
					"typeName varchar(255) NULL," +
					"accountcode varchar(100) NULL" +
					")", null);
		} catch (DaoException e) {
		}
		
		try {
			super.update("CREATE TABLE claim_form_typedept (" +
					"typeid varchar(255) default '0' NOT NULL," +
					"departmentid varchar(255) NULL" +
					")", null);
		} catch (DaoException e) {
		}
	}
}
