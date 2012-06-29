package com.tms.crm.sales.model;

import kacang.model.DaoException;

public class OpportunityDaoMsSql extends OpportunityDao{
	
	public void init() throws DaoException {
        try{
        	super.init();    //To change body of overridden methods use File | Settings | File Templates.
        }catch(Exception e) {
			//e.printStackTrace();
		}
		
		try {
			super.update("CREATE TABLE financial_year (id varchar(100) NOT NULL default '0', yearEnds varchar(50) default '0' , currencySymbol varchar(20) default NULL, PRIMARY KEY(id) )",null);
			String sql = "INSERT INTO financial_year (id, yearEnds, currencySymbol) VALUES( 'e6d98320-c0a8c894-d7581b00-922148d4', '11', 'RM')";
			super.update(sql, null);
		} catch(Exception e) {
			//e.printStackTrace();
		}
	}
	
}
