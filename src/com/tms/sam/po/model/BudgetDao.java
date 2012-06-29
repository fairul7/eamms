package com.tms.sam.po.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;

public class BudgetDao extends DataSourceDao {
	public void init() throws DaoException {
	   
		try {
			 super.update("CREATE TABLE po_budget_approval (" +
				       	  "budgetID VARCHAR(100) NOT NULL, " +
				       	  "ppID VARCHAR(100) NOT NULL, " +
				          "supplierID VARCHAR(100) , " +
				          "counting INT, " +
				          "action VARCHAR(225), " +
					      "remarks TEXT, " +
					      "dateApproved DATETIME, " +
					      "approvedBy VARCHAR(100), " +
		        		  "PRIMARY KEY(budgetID))", null);		
			} catch (DaoException e) {}   
			
		try{
			super.update("CREATE INDEX ppIndex ON po_budget_approval(ppID)",null);
		   }catch(DaoException e){}
		   
		try{
			super.update("CREATE INDEX suppID_Index ON po_budget_approval(supplierID)",null);
		   }catch(DaoException e){}	  
		   
	}
	
	public void addBudget(BudgetObject bo) throws DaoException {
		super.update("INSERT INTO po_budget_approval ("+ 
				     "budgetID, ppID, supplierID, action, remarks, " +
				     "dateApproved, counting, approvedBy) VALUES " + 
				     "(#budgetID#, #ppID#, #supplierID#, #action#, #remarks#, now(), " +
				     "#count#,#approvedBy#)",bo);
		
	}
}
