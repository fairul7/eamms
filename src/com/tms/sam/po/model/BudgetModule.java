package com.tms.sam.po.model;

import kacang.model.DefaultModule;
import kacang.util.Log;

public class BudgetModule extends DefaultModule {
	 public void addBudget(BudgetObject bo){
		    BudgetDao dao = (BudgetDao) getDao();
	        try {
	             dao.addBudget(bo);
	        }
	        catch(Exception error) {
	            Log.getLog(getClass()).error("Error adding budget: " + error, error);
	            
	        }
	 }
}
