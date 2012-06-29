package com.tms.collab.isr.permission.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class PermissionModel extends DefaultModule {
	public boolean hasPermission(String userId, String permission){
		PermissionDao dao = (PermissionDao)getDao();
		boolean result = false;
		try{
			result = dao.hasPermission(userId, permission);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in hasPermission: ", e);
		}
		return result;
	}
	
	public int deleteGroups(String[] selectedGroups){
		PermissionDao dao = (PermissionDao)getDao();
		int results = 0;
		try{
			results =  dao.deleteGroups(selectedGroups);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in deleteGroups: ", e);
		}
		return results;
	}
	
	public boolean updateGroup(ISRGroup gp){
		PermissionDao dao = (PermissionDao)getDao();
		boolean isSuccess = true;
		
		try{
			dao.updateGroup(gp);
		}
		catch(DaoException e){
			isSuccess = false;
			Log.getLog(getClass()).error("Error in updateGroup: ", e);
		}
		
		return isSuccess;
	}
	
	public ISRGroup getGroup(String id){
		PermissionDao dao = (PermissionDao)getDao();
		ISRGroup gp = new ISRGroup();
		try{
			gp = dao.selectGroup(id);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getGroup: ", e);
		}
		return gp;
	}
	
	public boolean addGroup(ISRGroup gp){
		PermissionDao dao = (PermissionDao)getDao();
		boolean isSuccess = true;
		try{
			dao.insertGroup(gp);
		}catch(DaoException e){
			isSuccess = false;
			Log.getLog(getClass()).error("Error in addGroup: ", e);
		}
		return isSuccess;
	}
	
	public Collection getGroupListing(String searchBy, String sort, boolean desc, int start, int rows){
		PermissionDao dao = (PermissionDao)getDao();
		Collection results = null;
		try{
			results = dao.selectGroupListing(searchBy, sort, desc, start, rows);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getGroupListing: ", e);
		}
		
		return results; 
	}
	
	public int getGroupListingCount(String searchBy) {
		PermissionDao dao = (PermissionDao)getDao();
		int count = 0;
		try{
			count = dao.selectGroupListingCount(searchBy);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getGroupListingCount: ", e);
		}
		
		return count; 
	}
	
	public boolean isGroupNameExist(String groupName) {
		PermissionDao dao = (PermissionDao)getDao();
		boolean isGroupNameExist = false;
		
		try {
			isGroupNameExist = dao.isGroupNameExist(groupName);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in isGroupNameExist: ", e);
		}
		
		return isGroupNameExist;
	}
	
	public boolean hasISRRole(String userId, String role) {
		PermissionDao dao = (PermissionDao)getDao();
		boolean hasISRRole = false;
		
		try {
			hasISRRole = dao.hasISRRole(userId, role);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in isAdmin: ", e);
		}
		
		return hasISRRole;
	}
	
	public boolean isUserInActiveGroup(String userId) {
		PermissionDao dao = (PermissionDao)getDao();
		boolean isUserInActiveGroup = false;
		
		try {
			isUserInActiveGroup = dao.isUserInActiveGroup(userId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in isUserInActiveGroup: ", e);
		}
		
		return isUserInActiveGroup;
	}
	
	public Collection getDeptUsers(String deptCode, String countryCode) {
		PermissionDao dao = (PermissionDao)getDao();
		Collection results = null;
		try{
			results = dao.getDeptUsers(deptCode, countryCode);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getDeptUsers: ", e);
		}
		
		return results; 
	}
	
	public Collection getDeptUsersWithRole(String deptCode, String countryCode, String role) {
		PermissionDao dao = (PermissionDao)getDao();
		Collection results = null;
		try{
			results = dao.getDeptUsersWithRole(deptCode, countryCode, role);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getDeptUsersWithRole: ", e);
		}
		
		return results; 
	}
	
	public Collection getAllISRUsers() {
		PermissionDao dao = (PermissionDao)getDao();
		Collection results = null;
		try{
			results = dao.getAllISRUsers();
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getAllISRUsers: ", e);
		}
		
		return results; 
	}
	
	
	public Collection getGroupDeptStaff(String groupId) {
		PermissionDao dao = (PermissionDao)getDao();
		Collection results = null;
		try{
			results = dao.getGroupDeptStaff(groupId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getGroupDeptStaff: ", e);
		}
		
		return results; 
	}
}
