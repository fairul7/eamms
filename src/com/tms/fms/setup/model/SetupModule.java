package com.tms.fms.setup.model;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;

import com.tms.fms.abw.model.AbwModule;

public class SetupModule extends DefaultModule{
	
	public static final String DRAFT_STATUS 		= "D";
	public static final String CANCELLED_STATUS 	= "C";
	public static final String PROCESS_STATUS 		= "S";
	public static final String FULFILLED_STATUS 	= "F";
	public static final String UNFULFILLED_STATUS 	= "U";
	public static final String OUTSOURCED_STATUS 	= "O";	
	public static final String REJECTED_STATUS 		= "R";
	public static final String APPROVED_STATUS 		= "A";	
	public static final String NORMAL_STATUS 		= "N";
	public static final String LATE_STATUS 			= "L";
	public static final String ADHOC_STATUS 		= "H";
	public static final String PENDING_STATUS 		= "P";
	public static final String COMPLETED_STATUS 	= "M";	
	public static final String CHECKOUT_STATUS 		= "T";
	public static final String NEW_STATUS 			= "W";
	public static final String CLOSED_STATUS 		= "E";
	public static final String ASSIGNED_STATUS 		= "G";
	public static final String UPDATED_STATUS 		= "Z";
	
	public static final String ABW_TRANSFER_COST_SCHEDULE_ID = "atcs";
	public static final String ABW_ENG_TRANSFER_COST_SCHEDULE_ID = "aetcs";
	
       public void addProgram(ProgramObject p) {
    	SetupDao dao = (SetupDao) getDao();
    	try {
    	dao.addProgram(p);  
    	}catch(Exception e){
    		Log.getLog(getClass()).error("Error addProgram(1)", e);
			throw new RuntimeException("DAO Error");
    	   }
       }
		
        public void updateProgram(ProgramObject p){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.updateProgram(p);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateProgram(1)", e);
			throw new RuntimeException("DAO Error");
		}
	   }
	
	   public void deleteProgram(String programId){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.deleteProgram(programId);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteProgram(1)", e);
			throw new RuntimeException("DAO Error");
		}
	   }
        
	   public ProgramObject getProgram(String programId ){
		   ProgramObject p = new ProgramObject();
		   try{
			   SetupDao dao = (SetupDao) getDao();
			   Collection rows = dao.selectProgram(programId);
				if (rows.size() > 0) {
					p = (ProgramObject)rows.iterator().next();
				}
	      }catch(DaoException e){
		   Log.getLog(getClass()).error("Error getProgram(1)", e);
	       }
	      return p;
	   }
	   
	 public Collection selectProgram(String search, String status,String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
			try{
				SetupDao dao=(SetupDao) getDao();
				return dao.selectProgram(search, status, sort, desc, startIndex, maxRows);
			}catch (DaoException e){
				Log.getLog(getClass()).error("Error selectProgram(6)", e);
				throw new RuntimeException("DAO Error");
			}   
       	        	        	
        }
		
		public int selectProgramCount(String search, String status) throws DaoException{
			try{
				SetupDao dao=(SetupDao) getDao();
				return dao.selectProgramCount(search, status);
			}catch (DaoException e){
				Log.getLog(getClass()).error("Error selectProgram(6)", e);
				throw new RuntimeException("DAO Error");
			}   
       	        	        	
        }
   
	    public Collection getDepartment() throws DaoException{
	    	SetupDao dao = (SetupDao) getDao();
		    Collection col = dao.getDept();
		    return col;
	    }

	    public boolean isExistPfeCode(String pfeCode) throws DaoException{
	    	SetupDao dao = (SetupDao) getDao();
	    	boolean isExist=false;
	    	isExist=dao.isExistPfeCode(pfeCode);
		    return isExist;
	    }
	    
	    public String selectProgName(String id)throws DaoException{
	    	SetupDao dao = (SetupDao) getDao();		    
		    return dao.getProgramName(id);
	    }
	    
	    public void insertStatus(FMSStatus status) {
	    	SetupDao dao = (SetupDao) getDao();
	    	try {
	    	dao.addStatus(status);  
	    	}catch(Exception e){
	    		Log.getLog(getClass()).error("Error insertStatus", e);
				throw new RuntimeException("DAO Error");
	    	   }
	       }
	    
	    public String selectAssignmentIdByBookingIdUserId(String requestId, String userId)throws DaoException{
	    	SetupDao dao = (SetupDao) getDao();		    
		    return dao.getAssignmentIdByBookingIdUserId(requestId, userId);
	    }
	    
	    public String selectAssignmentIdByBookingIdUserId(String requestId, String userId, String assignmentId) throws DaoException {
	    	SetupDao dao = (SetupDao) getDao();		    
		    return dao.getAssignmentIdByBookingIdUserId(requestId, userId, assignmentId);
	    }
	    
	    public Collection getEngineeringUserByRequestId(String requestId) throws DaoException{
	    	SetupDao dao = (SetupDao) getDao();
		    Collection col = dao.getEngineeringUserByRequestId(requestId);
		    return col;
	    }
	    
	    public Collection getDutyRosterAssignUser(String requestId, Date requiredFrom) throws DaoException {
	    	SetupDao dao = (SetupDao) getDao();
	    	return dao.getDutyRosterAssignUser(requestId, requiredFrom);
	    }
	    
	/**
	 * Return an object for ABW transport scheduler time. Return null if no scheduler is defined yet
	 * @param abwSchedulerId
	 * @return
	 * @throws DaoException
	 */
	public DefaultDataObject getAbwSchedulerTime(String abwSchedulerId){
		try {
			SetupDao dao = (SetupDao) getDao();
			return dao.getAbwSchedulerTime(abwSchedulerId);
		} catch (DaoException e) {
			// TODO: handle exception
			Log.getLog(getClass()).error(e, e);
			return null;
		}
	}
	
	/**
	 * Update ABW scheduler time from Global Setup page
	 * @param object
	 */
	public void updateAbwSchedulerTime(DefaultDataObject object){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.updateAbwSchedulerTime(object);
			
			AbwModule abwMod = (AbwModule) Application.getInstance().getModule(AbwModule.class);
			int hour = Integer.parseInt(((String) object.getProperty("scheduleTime1")).substring(0, 2));
			int min = Integer.parseInt(((String) object.getProperty("scheduleTime1")).substring(3));
			abwMod.initializeAbwTransferCostScheduler(hour, min);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error(e, e);
		}
	}
}
