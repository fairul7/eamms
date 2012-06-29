package com.tms.fms.department.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;

import org.apache.commons.collections.SequencedHashMap;

public class FMSDepartmentManager extends DefaultModule
{
    private FMSDepartmentDao  dao;
    public static final String ACTIVE_DEPT = "1";
    public static final String INACTIVE_DEPT = "0";
    
    public static final String BACK_TO_DEPT_LIST = "Back to Dept List";
    public static final String EDIT_DEPT = "Edit Dept";
    
    public static final String TRANSPORT_DEPT = "TRANSPORT DEPARTMENT";
    public static final String SYSTEM_ADMIN = "kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c";
    public static final String SYSTEM_ADMIN_GROUP = "kacang.services.security.Group_e53be8e1-c0a8c860-1abf8700-fda285ea";
        
    public FMSDepartmentManager()
    {
        dao = (FMSDepartmentDao) getDao();
    }

    public void addDepartment(FMSDepartment fd)throws DaoException {
    	
    	try {
    	dao = (FMSDepartmentDao) getDao();
    	dao.addDepartment(fd);    	
    	}catch(Exception e){
    		
    	}
    }
    
    public void addDepartmentApprover(FMSDepartment fd)throws DaoException {
    	
    	try {
    	//dao = (FMSDepartmentDao) getDao();
    	dao.addDepartmentApprover(fd);    	
    	}catch(Exception e){
    		
    	}
    }
    
    
    public Collection getDepartment(String filter,String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
	    Collection col = dao.selectDepartment(filter, sort, desc, startIndex, maxRows);
	    
	    return col;
    }
    
    public Collection getAltApprover(String departmentId)throws DaoException{
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	Collection col = dao.getAltApprover(departmentId);
	    
	    return col;	
    }
    
    public void updateDeptStatus(String deptId,String status)throws DaoException {
    	    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	dao.updateDeptStatus(deptId,status);    	
    	    	
    }
    
    public Map getFMSDepartments() throws DaoException
    {
        dao = (FMSDepartmentDao) getDao();
        Collection col = dao.selectFMSDepartments();
        Map map = new SequencedHashMap();
        for(Iterator i = col.iterator();i.hasNext();){
            DefaultDataObject obj = (DefaultDataObject)i.next();
            map.put(obj.getId(),obj.getProperty("deptName"));
        }
        return map;
    }
    
    public Map getActiveFMSDepartments() throws DaoException
    {
        dao = (FMSDepartmentDao) getDao();
        Collection col = dao.selectActiveFMSDepartments();
        Map map = new SequencedHashMap();
        for(Iterator i = col.iterator();i.hasNext();){
            DefaultDataObject obj = (DefaultDataObject)i.next();
            map.put(obj.getId(),obj.getProperty("deptName"));
        }
        return map;
    }
       
    public Collection getDeptById(String deptId)throws DaoException{
    	
    	 dao = (FMSDepartmentDao) getDao();
    	 return dao.selectDeptById(deptId);
    	     	
    }
    
    public void addUnit(FMSUnit fu)throws SecurityException {
    	
    	try {
    	dao = (FMSDepartmentDao) getDao();
    	dao.addUnit(fu);    	
    	}catch(Exception e){
    		
    	}
    }
    
    public Collection getUnit(String filter,String statusFilter,String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
	    Collection col = dao.selectUnit(filter, statusFilter, sort, desc, startIndex, maxRows);
	    
	    return col;
    }
    
    public void updateUnitStatus(String unitId,String status)throws DaoException {
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	dao.updateUnitStatus(unitId,status);    	
    	    	
    }
    
    public FMSDepartment getselectFMSDepartment(String deptId)throws DaoException {
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	return dao.selectFMSDepartment(deptId);
    }
    
    public void updateDepartment(FMSDepartment fd)throws DaoException {
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	dao.updateDepartment(fd);
    	
    	dao.deleteDepartmentApprover(fd.getId());   	//delete old approvers 	
    	dao.addDepartmentApprover(fd);   				//add new one
    	
    }
    
    public FMSUnit getselectFMSUnit(String deptId)throws DaoException {
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	return dao.selectFMSUnit(deptId);
    }
    
    public void updateUnit(FMSUnit fu)throws DaoException {
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	dao.updateUnit(fu);
    	
    	dao.deleteUnitApprover(fu.getId());   	//delete old approvers 	
    	dao.addUnitApprover(fu);   				//add new one
    	
    }
    
    public Collection getUnitApprover(String unitId)throws DaoException{
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	Collection col = dao.getUnitApprover(unitId);
	    
	    return col;	
    }
    
    public boolean userIsHOD(String userId, String deptId)throws DaoException{
    	
    	Collection hods = new ArrayList();
    	Collection approvers = new ArrayList();
    	boolean hod = false;
    	    	
    	try {
    		FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
            hods = dao.getHODDepartment(userId, deptId);
            if(hods.size() > 0)
            	hod = true;
            
            if(!hod){		//check alternate approver
            	approvers = dao.getApproverDepartment(userId, deptId);
            	if(approvers.size() > 0)
            		hod = true;
            }
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
        return hod;
    }
    
    public boolean userHadPermission(String userId, String unitId)throws DaoException{
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	
    	boolean isHou = false;
    	try {
			if (dao.isHOUUnit(userId, unitId)) {
				isHou = true;
			}
			
			// check alternate approver
			if (!isHou && dao.isAlternateUnitApprover(userId)) {
				isHou = true;
			}
        }
        catch (Exception e) {
            throw new SecurityException(e.toString());
        }
        return isHou;
    }
    
    public String[] getEmailApprovers(String deptId)throws DaoException{
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	return dao.getEmailApprovers(deptId);
    	
    }
    
    
    public String[] getEmailRequestor(String requestId)throws DaoException{
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	return dao.getEmailRequestor(requestId);
    	
    }
    
    public Map getDeptUnitUser(String userId)throws DaoException{
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	return dao.getUserDeptUnit(userId);
    	
    }
    
    public Map getExistingUserDeptUnit(String userId)throws DaoException{
    	
    	FMSDepartmentDao dao = (FMSDepartmentDao) getDao();
    	return dao.getExistingUserDeptUnit(userId);
    	
    }
}
