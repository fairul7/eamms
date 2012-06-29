package com.tms.hr.claim.model;

import kacang.model.*;

public class ClaimFormIndexDaoSybase extends ClaimFormIndexDaoMsSql {
	public void init() throws DaoException {
		try {
			super.init();
		} catch (DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE " + TABLENAME + " ADD approvedBy varchar(255) NULL", null);
		} catch (DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE " + TABLENAME + " ADD rejectedBy varchar(255) NULL", null);
		} catch (DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE " + TABLENAME + " ADD userApprover1Date datetime NULL", null);
		} catch (DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE " + TABLENAME + " ADD userApprover2Date datetime NULL", null);
		} catch (DaoException e) {
		}
	}
}
