package com.tms.sam.po.permission.model;


import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class PermissionModel extends DefaultModule{
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
	
	public boolean addGroup(POGroup po){
		PermissionDao dao = (PermissionDao)getDao();
		boolean isSuccess = true;
		try{
			dao.insertGroup(po);
		}catch(DaoException e){
			isSuccess = false;
			Log.getLog(getClass()).error("Error in addGroup: ", e);
		}
		return isSuccess;
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
	
	public POGroup getGroup(String id){
		PermissionDao dao = (PermissionDao)getDao();
		POGroup po = new POGroup();
		try{
			po = dao.selectGroup(id);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in getGroup: ", e);
		}
		return po;
	}
	
	public boolean updateGroup(POGroup po){
		PermissionDao dao = (PermissionDao)getDao();
		boolean isSuccess = true;
		
		try{
			dao.updateGroup(po);
		}
		catch(DaoException e){
			isSuccess = false;
			Log.getLog(getClass()).error("Error in updateGroup: ", e);
		}
		
		return isSuccess;
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
	
	public boolean isHOD(String userId) {
		PermissionDao dao = (PermissionDao)getDao();
		boolean isHOD= false;
		
		try {
			isHOD = dao.isHOD(userId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in isHOD: ", e);
		}
		
		return isHOD;
	}
	
	public Collection groupOfUser(String permissionId) {
		PermissionDao dao = (PermissionDao)getDao();
		Collection groupOfUser = null;
		
		try {
			groupOfUser = dao.groupOfUser(permissionId);
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("Error in isHOD: ", e);
		}
		
		return groupOfUser;
	}
}
