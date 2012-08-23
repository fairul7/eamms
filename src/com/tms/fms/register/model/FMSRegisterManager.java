package com.tms.fms.register.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.services.security.User;
import kacang.util.Encryption;
import kacang.util.Log;

import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;

public class FMSRegisterManager extends DefaultModule {
	
	protected Log log;
	public static final String GROUP_FMS_USER = "Default FMS User Group";
	
	public static final String GROUP_EAMMS_SUPER_ADMINISTRATOR = Application.getInstance().getMessage("eamms.group.superAdministrator");
	public static final String GROUP_EAMMS_ADMINISTRATOR = Application.getInstance().getMessage("eamms.group.administrator");
	public static final String GROUP_EAMMS_UNIT_HEAD = Application.getInstance().getMessage("eamms.group.unitHead");
	public static final String GROUP_EAMMS_SENIOR_ENGINEER = Application.getInstance().getMessage("eamms.group.seniorEngineer");
	public static final String GROUP_EAMMS_ENGINEER = Application.getInstance().getMessage("eamms.group.engineer");
	public static final String GROUP_EAMMS_NORMAL_USER = Application.getInstance().getMessage("eamms.group.normalUser");
	public static final String GROUP_EAMMS_UNIT_HEAD_ENGINEERING = Application.getInstance().getMessage("eamms.group.unitHeadEngineering_manager");
	public static final String GROUP_EAMMS_NETWORK_ENGINEER = Application.getInstance().getMessage("eamms.group.networkEngineer");
	public static final String GROUP_EAMMS_UNIT_HEAD_NETWORK = Application.getInstance().getMessage("eamms.group.unitHeadNetwork_manager");
	public static final String GROUP_EAMMS_MCR = Application.getInstance().getMessage("eamms.group.mcr");
	public static final String GROUP_EAMMS_NETWORK_SUPER_USER = Application.getInstance().getMessage("eamms.group.networkSuperUser");
	public static final String GROUP_EAMMS_NETWORK_USER = Application.getInstance().getMessage("eamms.group.networkUser");
	public static final String GROUP_EAMMS_INTERN = Application.getInstance().getMessage("eamms.group.intern");
	
	public static final String PENDING_FMS_USER = "0";
	public static final String ACCEPTED_FMS_USER = "1";
	public static final String REJECTED_FMS_USER = "2";
	
	
	 public FMSRegisterManager() {
	        log = Log.getLog(getClass());
	        
	    }
	 
	 ////
	    public Collection getFMSWaitingUser(String filter,String deptFilter, String sort, boolean desc, int startIndex,int maxRows, String active) throws DaoException
	    {
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	        Collection col = dao.selectFMSRegisterUser(filter, deptFilter, sort, desc, startIndex, maxRows, active);
	        
	        return col;
	    }
	        
	    public void addFMSUser(User user,boolean edit) throws SecurityException {
	        // check validity of user
	        if (user == null || user.getId() == null) throw new SecurityException();
	        
	        FMSRegisterDao dao = (FMSRegisterDao) getDao();
	        if(!edit){
		        try {        
		             user.setId(User.class.getName() + "_" + user.getId());
		             user.setProperty("weakpass", Encryption.encrypt((String) user.getProperty("weakpass"), user.getId()));	             
		             dao.storeFMSUser(user);            
		        }
		        catch (Exception e) {
		            throw new SecurityException(e.toString());
		        }
	        }else{
	        	try {    
	        	dao.storeFMSUser(user);
	        	}catch (Exception e) {
		            throw new SecurityException(e.toString());
		        }
	        }
	    }
	    
	    public void addUser(User user, boolean prefix) throws SecurityException {
	        // check validity of user
	        if (user == null || user.getId() == null) throw new SecurityException();

	        // persist user
	        try {
	            if (prefix) {
	                user.setId(User.class.getName() + "_" + user.getId());
	                user.setProperty("weakpass", Encryption.encrypt((String) user.getProperty("weakpass"), user.getId()));
	            }
	            FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            dao.storeUser(user);
	            log.debug("addUser: user " + user.getId() + " added");
	            //fireSecurityEvent(new SecurityEvent(EVENT_USER_ADDED, user));
	            log.debug("addUser: event listeners notified");
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public void updateUser(User user) throws SecurityException {
	        // check validity of user
	        if (user == null || user.getId() == null) throw new SecurityException();

	        // persist user
	        try {
	        	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            dao.storeUser(user);
	            log.debug("user " + user.getId() + " updated");
	            
	            log.debug("event listeners notified");
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public User getFMSUser(String userId) throws SecurityException {
	        // check validity of user
	        if (userId == null) throw new SecurityException();

	        // select user
	        try {
	        	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            return dao.selectFMSUser(userId);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public void deleteFMSUser(String userId) throws SecurityException {
	    	try {
	    		FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            dao.deleteFMSUser(userId);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public void updateFMSUser(String active, String reason, Date statusDate, String userId) throws SecurityException {
	    	try {
	    		FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            dao.updateFMSUser(active, reason, statusDate, userId);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public User getUser(String userId) throws SecurityException {
	        // check validity of user
	        if (userId == null) throw new SecurityException();

	        // select user
	        try {
	        	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            return dao.selectUser(userId);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public String getUserDepartment(String userId)throws SecurityException {
	    	
	    	try {
	    		FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            return dao.getUserDepartment(userId);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public Collection getDepartmentUsers(String deptId,String userId) throws SecurityException {

	        if (deptId == null) throw new SecurityException();
	        try {
	        	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            return dao.selectUsersByDepartment(deptId, userId);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public boolean isTransDepartment(String deptId,String userId) throws SecurityException {

	    	boolean fromTrans = false;
	        if (deptId == null) throw new SecurityException();
	        try {
	        	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	            if(dao.selectUsersByDepartment(deptId, userId).size() > 0)
	            	fromTrans = true;
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	        return fromTrans;
	    }
	    
	    public Collection selectExistUser(String username, String pending, String accepted)throws SecurityException {
	    	
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	    	 try {
	    		 return dao.selectExistUser(username, pending, accepted);
	    	 }catch (Exception e) {
	 	            throw new SecurityException(e.toString());
	    	 }	    	
	    }
	    
	    public Collection selectUserWorkingProfile(String userId,String startTime,String endTime) throws DaoException{ 
	    	
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
		 	
		 	return dao.selectUserWorkingProfile(userId,startTime,endTime);
		 	
	    }
	    
	    public Collection selectUsersByDepartment(String deptId)throws DaoException
	    {
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();		 	
		 	return dao.selectUsersByDepartment(deptId);
	    	
	    }
	    public Collection selectManpowerWorkingProfile(String deptId, Date start, Date end,String startTime, String endTime) throws DaoException{ 
	    		    	
	    	Collection userInWorkingProfile = new ArrayList();
	    	Collection allUser = new ArrayList();
	    	
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	    	userInWorkingProfile = dao.selectDriversByAssignment(deptId, null, null, null, null);
	    	
	    	for(Iterator it = userInWorkingProfile.iterator(); it.hasNext(); ){
	    		ManpowerAssignmentObject MObj = (ManpowerAssignmentObject) it.next();
	    		allUser.add(MObj);
	    	}
	    	
		 	return allUser;
		 	
	    }

	    public ManpowerAssignmentObject getWorkingProfileUser(String userId)throws DaoException{ 
	    
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	    	
	    	return dao.selectWorkingProfileUser(userId);
	    }
	    
	    
	    public String selectExistSecurityUser(String username)throws SecurityException {
	    	
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	    	 try {
	    		 return dao.selectExistSecurityUser(username);
	    	 }catch (Exception e) {
	 	            throw new SecurityException(e.toString());
	    	 }	    	
	    }
	    
	    public Collection getUnitUsers(String unitId, String groupId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException{ 
	        
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	    	Application application = Application.getInstance();
	    	String userId = application.getInstance().getCurrentUser().getId(); 
	        if (unitId == null || "-1".equals(unitId)){
	        	return dao.selectUsersUnitByUnitApprover(userId, groupId, properties, start, maxResults, sort, descending);	//if null current get user's unit
	        }
	        try {
	        	
	            return dao.selectUsersByUnit(unitId, groupId, properties, start, maxResults, sort, descending);
	        }
	        catch (Exception e) {
	            throw new SecurityException(e.toString());
	        }
	    }
	    
	    public Collection getAllUsers(String unitId, String groupId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException{
	    	
	    	FMSRegisterDao dao = (FMSRegisterDao) getDao();
	    	return dao.selectUsersByUnit(unitId, groupId, properties, start, maxResults, sort, descending);
	    }
	    
	    ////
}
