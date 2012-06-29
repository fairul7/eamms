package com.tms.hr.claim.model;

import kacang.model.DaoException;
import kacang.util.Log;

public class ClaimFormIndexDaoMsSql extends ClaimFormIndexDao{
	
	public void init() throws DaoException{
        try {
            super.update("ALTER TABLE "+TABLENAME+" ADD userApprover1Date datetime ", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cms_content_module");
        }
        
        try {
            super.update("ALTER TABLE "+TABLENAME+" ADD userApprover2Date datetime ", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cms_content_module");
        }
        
    }
	
	public void updateObjectApprover1(ClaimFormIndex obj) throws DaoException{

        super.update("UPDATE " + TABLENAME + " SET " +
          " timeEdit = #timeEdit# , " +
          " userOriginator = #userOriginator# , " +
          " userOwner = #userOwner# , " +
          " userApprover1 = #userApprover1# , " +
          " userApprover2 = #userApprover2# , " +
          " userApprover3 = #userApprover3# , " +
          " userApprover4 = #userApprover4# , " +
          " userAssessor = #userAssessor# , " + " currency = #currency# , " +
          " amount = #amount# , " +
          " approvalLevelRequired = #approvalLevelRequired# , " +
          " approvalLevelGranted = #approvalLevelGranted# , " +
          " remarks = #remarks# , " + " rejectReason = #rejectReason# , " +
          " info= #info# , " + " state = #state# , " +
          " status = #status#, " + " claimMonth = #claimMonth#, " +
          "approvedBy = #approvedBy#, " + "rejectedBy = #rejectedBy# , userApprover1Date =getDate() " +
          " WHERE id = #id# ", obj);

  }
	
	public void updateObjectApprover2(ClaimFormIndex obj) throws DaoException{

        super.update("UPDATE " + TABLENAME + " SET " +
          " timeEdit = #timeEdit# , " +
          " userOriginator = #userOriginator# , " +
          " userOwner = #userOwner# , " +
          " userApprover1 = #userApprover1# , " +
          " userApprover2 = #userApprover2# , " +
          " userApprover3 = #userApprover3# , " +
          " userApprover4 = #userApprover4# , " +
          " userAssessor = #userAssessor# , " + " currency = #currency# , " +
          " amount = #amount# , " +
          " approvalLevelRequired = #approvalLevelRequired# , " +
          " approvalLevelGranted = #approvalLevelGranted# , " +
          " remarks = #remarks# , " + " rejectReason = #rejectReason# , " +
          " info= #info# , " + " state = #state# , " +
          " status = #status#, " + " claimMonth = #claimMonth#, " +
          "approvedBy = #approvedBy#, " + "rejectedBy = #rejectedBy# , userApprover2Date =getDate() " +
          " WHERE id = #id# ", obj);

	}
	
}
