package com.tms.crm.sales.model;

import kacang.model.*;

public class OpportunityDaoSybase extends OpportunityDaoMsSql {
	public void init() throws DaoException {
		try {
			super.init();
		} catch(Exception e) {
		}
		
		try {
			super.update("CREATE TABLE financial_year (id varchar(100) NOT NULL PRIMARY KEY, yearEnds varchar(50) default '0' NULL, currencySymbol varchar(20) NULL)", null);
			String sql = "INSERT INTO financial_year (id, yearEnds, currencySymbol) VALUES ('e6d98320-c0a8c894-d7581b00-922148d4', '11', 'RM')";
			super.update(sql, null);
		} catch(Exception e) {
		}
	}
}
