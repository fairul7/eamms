package com.tms.hr.employee.model;

import kacang.model.DaoException;
import kacang.util.Log;

public class EmployeeDaoMsSql extends EmployeeDao{
	
	Log log = Log.getLog(getClass());

    public void init() throws DaoException {
    	
    	try{
    		super.update("ALTER TABLE employee_main ALTER COLUMN em_status nvarchar(255) NULL ", null);
    	} catch (DaoException e) {
            ;
        }
    	
    }

}
