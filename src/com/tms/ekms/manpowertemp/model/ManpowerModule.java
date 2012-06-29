package com.tms.ekms.manpowertemp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.facility.model.SetupModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class ManpowerModule extends DefaultModule {
	
	public ManpowerModule(){
		
	}
	//LeaveTypeSetup
	 public void addLeaveType(LeaveTypeObject lto) {
    	ManpowerDao dao = (ManpowerDao) getDao();
    	try {
    	dao.addLeaveType(lto);  
    	}catch(Exception e){
    		Log.getLog(getClass()).error("Error addLeaveType(1)", e);
			throw new RuntimeException("DAO Error");
    	   }
       }
	 
	 public void updateLeaveType(LeaveTypeObject leaveObject){
		 try{
		 ManpowerDao dao =(ManpowerDao) getDao();
		 dao.updateLeaveType(leaveObject);
		 }catch(Exception e){
			 Log.getLog(getClass()).error("Error updateLeaveType(1)", e);
			 throw new RuntimeException("DAO Error");
		 }
	 }
	 
	 public void deleteLeaveType(String id){
		 try{
			 ManpowerDao dao=(ManpowerDao) getDao();
			 dao.deleteLeaveType(id);
		 }catch(Exception e){
			 Log.getLog(getClass()).error("Error deleteLeaveType(1)", e);
			 throw new RuntimeException("DAO Error");
		 }
	 }
	 
	 public LeaveTypeObject getLeaveType(String id){
		 LeaveTypeObject object=new LeaveTypeObject();
		 try{
			 ManpowerDao dao = (ManpowerDao) getDao();
			 Collection rows = dao.selectLeaveType(id);
			 if(rows.size()>0){
				 object =(LeaveTypeObject)rows.iterator().next();
			 }	 
		 }catch(Exception e){
			 Log.getLog(getClass()).error("Error getLeaveType(1)",e);
			 throw new RuntimeException("DAO Error");
		 }
		 return object;
	 }
	 
	 public Collection selectLeaveType(String search, String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
			try{
				ManpowerDao dao=(ManpowerDao) getDao();
				return dao.selectLeaveType(search, sort, desc, startIndex, maxRows);
			}catch (DaoException e){
				Log.getLog(getClass()).error("Error selectLeaveType(5)", e);
				throw new RuntimeException("DAO Error");
			}   
     }
		
	public int selectLeaveTypeCount(String search, String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
			try{
				ManpowerDao dao=(ManpowerDao) getDao();
				return dao.selectLeaveTypeCount(search, sort, desc, startIndex, maxRows);
			}catch (DaoException ex){
				Log.getLog(getClass()).error("Error selectLeaveTypeCount(5)", ex);
				throw new RuntimeException("DAO Error");
			}           	
	}
	
	//set Holiday/ Leave for staff
	public void addHolidayLeave(SetHolidayLeaveObject hlo) {
	    ManpowerDao dao = (ManpowerDao) getDao();
	    	try {
	    		dao.addHolidayLeave(hlo); 
	    	}catch(Exception e){
	    	Log.getLog(getClass()).error("Error addHolidayLeave(1)", e);
			throw new RuntimeException("DAO Error");
	    	}
	}
	
	public void updateHolidayLeave(SetHolidayLeaveObject hlObject){
		 try{
		 ManpowerDao dao =(ManpowerDao) getDao();
		 dao.updateHolidayLeave(hlObject);
		 }catch(Exception e){
			 Log.getLog(getClass()).error("Error updateLeaveType(1)", e);
			 throw new RuntimeException("DAO Error");
		 }
	 }
	
	public void deleteHolidayLeave(String id){
		try{
			 ManpowerDao dao=(ManpowerDao) getDao();
			 dao.deleteHolidayLeave(id);
		 }catch(Exception e){
			 Log.getLog(getClass()).error("Error deleteHolidayLeave(1)", e);
			 throw new RuntimeException("DAO Error");
		 }
		
	}
	 
	public SetHolidayLeaveObject getHolidayLeave(String id){
		SetHolidayLeaveObject obj=new SetHolidayLeaveObject();
		 try{
			 ManpowerDao dao = (ManpowerDao) getDao();
			 Collection rows = dao.selectHolidayLeave(id);
			 if(rows.size()>0){
				 obj =(SetHolidayLeaveObject)rows.iterator().next();
			 }	 
		 }catch(Exception e){
			 Log.getLog(getClass()).error("Error getLeaveType(1)",e);
			 throw new RuntimeException("DAO Error");
		 }
		 return obj;
	 }
	
	public Collection getLeaveType() throws DaoException{
	    	ManpowerDao dao = (ManpowerDao) getDao();
		    Collection col = dao.getLeaveType();
		    return col;
	 }
	
	public Collection selectHoliday(String year, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			return dao.selectHoliday(year, sort, desc, startIndex, maxRows);
		}catch (DaoException e){
			Log.getLog(getClass()).error("Error selectHoliday(5)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectHolidayCount(String year, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			return dao.selectHolidayCount(year, sort, desc, startIndex, maxRows);
		}catch (DaoException ex){
			Log.getLog(getClass()).error("Error selectHolidayCount(5)", ex);
			throw new RuntimeException("DaoError");
		}
	}
	
	public boolean isExistHoliday(String holiday) throws DaoException{
		ManpowerDao dao=(ManpowerDao) getDao();
		boolean isExist=false;
		isExist=dao.isExistHoliday(holiday);
		return isExist;
	}
	
	public Collection selectLeave(String year, Date fromDate, Date toDate, String unitApprover, String unitFilter, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		try {
			ManpowerDao dao=(ManpowerDao) getDao();
			SetupModule module=(SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			Collection leaveUsers = new ArrayList();
			ArrayList unitIds = new ArrayList();
			
	    	Collection lstUnit = module.getUnits(unitApprover);
		    if (lstUnit.size() > 0) {
		    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
		        	FMSUnit o = (FMSUnit) i.next();
		        	String unitId = o.getId();
		        	
		        	if (unitFilter != null) {
		        		if (unitId.equals(unitFilter)) {
		        			unitIds.add(unitId);
		        		}		        		
		        	} else {
		        		unitIds.add(unitId);
		        	}
		        }
		    }
		    
		    if (unitIds.size() > 0) {
		    	leaveUsers.addAll(dao.selectLeave(year, fromDate, toDate, unitIds, sort, desc, startIndex, maxRows));
		    }
		    
			return leaveUsers;
		}catch (DaoException e){
			Log.getLog(getClass()).error("Error selectLeave(5)", e);
			throw new RuntimeException("DaoError");
		}	
	}
	
	public int selectLeaveCount(String year, Date fromDate, Date toDate, String unitApprover, String sort, boolean desc, int startIndex, int maxRows) throws DaoException{
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			return dao.selectLeaveCount(year, fromDate, toDate, sort, desc, startIndex, maxRows);
		}catch (DaoException ex){
			Log.getLog(getClass()).error("Error selectLeaveCount(5)", ex);
			throw new RuntimeException("DaoError");
		}
	}
	
	//manpower leave
	public void addManpowerLeave(String id, String[] ids){
		try{
			deleteManpowerLeave(id);
			ManpowerDao dao = (ManpowerDao) getDao();
			for (int i=0; i<ids.length; i++) {
				ManpowerLeaveObject mlo = new ManpowerLeaveObject();
				mlo.setId(UuidGenerator.getInstance().getUuid());
				mlo.setLeaveId(id);
				mlo.setManpowerId(ids[i]);
				dao.addManpowerLeave(mlo);
			}
		}catch(Exception e){
			Log.getLog(getClass()).error("Error insertManpowerLeave(2)", e);
			throw new RuntimeException("Dao Error");
		}
	}
	
	public void updateManpowerLeave(ManpowerLeaveObject mlo){
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			dao.updateManpowerLeave(mlo);
		}catch(Exception e){
			Log.getLog(getClass()).error("Error updateManpowerLeave(1)", e);
			throw new RuntimeException("Dao Error");
		}
	}
	
	public void deleteManpowerLeave(String id){
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			dao.deleteManpowerLeave(id);
		}catch(Exception e){
			Log.getLog(getClass()).error("Error deleteManpowerLeave(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteManpowerLeaveById(String id){
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			Collection col = dao.selectManpowerLeave(id);
			String leaveId = "";
			
			for (Iterator i = col.iterator(); i.hasNext();){
				ManpowerLeaveObject mlo = (ManpowerLeaveObject) i.next();
				
				leaveId = mlo.getLeaveId();
			}
			
			
			dao.deleteManpowerLeaveById(id);
			
			int count = 0;
			count = dao.selectManpowerLeaveByLeaveId(leaveId).size();
			
			if (count <= 0) {
				dao.deleteManpowerLeaveSetupById(leaveId);
			}
		}catch(Exception e){
			Log.getLog(getClass()).error("Error deleteManpowerLeaveById(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public ManpowerLeaveObject getManpowerLeave(String id){
		ManpowerLeaveObject mlo=new ManpowerLeaveObject();
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			Collection rows=dao.selectManpowerLeave(id);
			if(rows.size()>0){
				mlo=(ManpowerLeaveObject)rows.iterator().next();
			}
		}catch(Exception e){
			Log.getLog(getClass()).error("Error getManpowerLeave(1)", e);
			throw new RuntimeException("Dao Error");
		}
		return mlo;
	}
	
	public Collection getManpowerLeaveByLeaveId(String leaveId){
		Collection rows = null;
		try{
			ManpowerDao dao=(ManpowerDao) getDao();
			rows=dao.selectManpowerLeaveByLeaveId(leaveId);
			
		}catch(Exception e){
			Log.getLog(getClass()).error("Error getManpowerLeave(1)", e);
			throw new RuntimeException("Dao Error");
		}
		return rows;
	}
	 
}
