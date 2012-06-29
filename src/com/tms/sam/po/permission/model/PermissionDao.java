package com.tms.sam.po.permission.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.Transaction;

public class PermissionDao extends DataSourceDao{
	public void init() {
		try{
			super.update("CREATE TABLE po_group (" +
					"id varchar(255) NOT NULL, " +
					"groupName varchar(255) NOT NULL, " +
					"description varchar(255) DEFAULT '', " +
					"active char(1) NOT NULL, " +
					"PRIMARY KEY(id))", null);
		} 
		catch(DaoException e){
			 // do nothing
		}
		
		try{
			super.update("CREATE TABLE po_group_user (" +
					"groupId varchar(255) NOT NULL, " +
					"userId varchar(255) NOT NULL)", null);
		} 
		catch(DaoException e){
			// do nothing
		}
		
		try{
			super.update("CREATE TABLE po_group_permission (" +
					"groupId varchar(255) NOT NULL, " +
					"permissionId varchar(255) NOT NULL)", null);
		} 
		catch(DaoException e){
			// do nothing
		}
	}
	
	public void insertGroup(POGroup po) throws DaoException{
		Transaction tx = null;
		
		try {
            
			//insert new group record into group table
			StringBuffer newGroupSql = new StringBuffer(
                    "INSERT INTO po_group " +
                    "(id, groupName, active) " +
                    "VALUES " +
                    "(#id#, #groupName#, #active#)");
            
            tx = getTransaction();
            tx.begin();
            tx.update(newGroupSql.toString(), po);
            
            //insert permission into table
            StringBuffer groupPermissionSql = new StringBuffer(
            		"INSERT INTO po_group_permission " +
            		"(groupId, permissionId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            ArrayList permission = po.getPermission();
            if(permission != null && permission.size() > 0){
            	
            	for(int i=0; i<permission.size(); i++){
            		String perm = (String)permission.get(i);
            		tx.update(groupPermissionSql.toString(), new Object[] { po.getId(), perm });
            	}
            }
            
            //insert user into table
            StringBuffer groupUserSql = new StringBuffer(
            		"INSERT INTO po_group_user " +
            		"(groupId, userId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            String[] userIds = po.getUserIds();
            
            if(userIds != null && userIds.length > 0){
            	
            	for(int i=0; i<userIds.length; i++){
            		tx.update(groupUserSql.toString(), new Object[] {po.getId(), userIds[i]});
            	}
            }
            
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
	}
	
	public Collection getGroupDeptStaff(String groupId) throws DaoException {
		return super.select("SELECT securityUser.id, securityUser.username, securityUser.firstName, securityUser.lastName, department.shortDesc AS deptCode " +
				"FROM po_group_user, security_user securityUser, org_chart_hierachy hierachy, org_chart_department department WHERE " +
				"securityUser.id = po_group_user.userId " +
				"AND po_group_user.userId = hierachy.userId " +
				"AND hierachy.deptCode=department.code " +
				"AND po_group_user.groupId = ? " +
				"ORDER BY department.shortDesc, securityUser.firstName ", User.class, new Object[] {groupId}, 0, -1);
	}
	
	public int selectGroupListingCount(String searchBy) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total FROM po_group WHERE 1=1 ");
		if(searchBy != null && !searchBy.trim().equals("")){
			sql.append("AND ( groupName LIKE '%" + searchBy + "%' ");
			sql.append(" OR description LIKE '%" + searchBy + "%' ) ");
		}
		
		Map row = (Map) super.select(sql.toString(), HashMap.class, null, 0, -1).iterator().next();
        return Integer.parseInt(((Number) row.get("total")).toString());
	}
	
	public Collection selectGroupListing(String searchBy, String sort, boolean desc, int start, int rows) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT id, groupName, description, active FROM po_group WHERE 1=1 ");
		
		if(searchBy != null && !searchBy.trim().equals("")){
			sql.append("AND ( groupName LIKE '%" + searchBy + "%' ");
			sql.append(" OR description LIKE '%" + searchBy + "%' ) ");
		}
		
		if(sort != null && !sort.trim().equals("")){
			sql.append(" ORDER BY " + sort);
		}else{
			sql.append(" ORDER BY groupName");
		}
		
		if(desc){
			sql.append(" DESC");
		}
		
		Collection col = super.select(sql.toString(), POGroup.class, null, start, rows);
		
		return col;
	}
	
	public int deleteGroups(String[] selectedKeys) throws DaoException{
		int results = 0;
		Transaction tx = null;
		
		try {
			tx = getTransaction();
            tx.begin();
            
            StringBuffer groupSql = new StringBuffer("DELETE FROM po_group WHERE id IN (");
            StringBuffer groupPermissionSql = new StringBuffer("DELETE FROM po_group_permission WHERE groupId IN (");
            StringBuffer groupUserSql = new StringBuffer("DELETE FROM po_group_user WHERE groupId IN (");
            
            Collection paramList = new ArrayList();
    		for(int i=0; i<selectedKeys.length; i++){
    			if(i>0){
    				groupSql.append(",");
    				groupPermissionSql.append(",");
    				groupUserSql.append(",");
    			}
    			groupSql.append("?");
				groupPermissionSql.append("?");
				groupUserSql.append("?");
    			paramList.add(selectedKeys[i]);
    		}
    		groupSql.append(")");
			groupPermissionSql.append(")");
			groupUserSql.append(")");
            
			results = tx.update(groupSql.toString(), paramList.toArray());
			results = tx.update(groupPermissionSql.toString(), paramList.toArray());
			results = tx.update(groupUserSql.toString(), paramList.toArray());
			
		}
		catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
        return results;
	}
	
	public boolean isGroupNameExist(String groupName) throws DaoException {
		boolean isGroupNameExist = false;
		
		Collection col = super.select("SELECT id FROM po_group " +
				"WHERE LOWER(groupName) = ?", HashMap.class, new Object[] {groupName}, 0, -1);
		
		if(col != null) {
			Iterator i = col.iterator();
			if(i.hasNext()){
				isGroupNameExist = true;
			}
		}
		
		return isGroupNameExist;
	}
	
	public POGroup selectGroup(String id) throws DaoException{
		POGroup po = new POGroup();
		
		//get group
		StringBuffer groupSql = new StringBuffer("SELECT id, groupName, active FROM po_group WHERE id = ? ");
		Collection group = super.select(groupSql.toString(), POGroup.class, new Object[]{id}, 0, -1);
		
		Iterator i = group.iterator();
		if(i.hasNext()){
			po = (POGroup)i.next();
		}
		
		//get permission of the group
		ArrayList permission = new ArrayList();
		StringBuffer groupPermissionSql = new StringBuffer("SELECT permissionId FROM po_group_permission WHERE groupId = ? ");
		Collection groupPermissions = super.select(groupPermissionSql.toString(), HashMap.class, new Object[]{id}, 0, -1);
		
		Iterator j = groupPermissions.iterator();
		while(j.hasNext()){
			Map row = (Map)j.next();
			permission.add(row.get("permissionId").toString());
		}
		
		po.setPermission(permission);
		
		//get user of the group
		StringBuffer groupUserSql = new StringBuffer("SELECT userId FROM po_group_user WHERE groupId = ? ");
		Collection groupUsers = super.select(groupUserSql.toString(), HashMap.class, new Object[]{id}, 0, -1);
		String[] userIds = new String[groupUsers.size()];
		
		int counter = 0;
		Iterator k = groupUsers.iterator();
		while(k.hasNext()){
			Map row = (Map)k.next();
			userIds[counter] = row.get("userId").toString();
			counter++;
		}
		
		po.setUserIds(userIds);
		
		return po;
	}
	
	public void updateGroup(POGroup po) throws DaoException{
		Transaction tx = null;
		
		try {
			tx = getTransaction();
            tx.begin();
            
            //update group data
    		StringBuffer groupSql = new StringBuffer("UPDATE po_group SET groupName = ?, active = ? WHERE id = ? ");
    		tx.update(groupSql.toString(), new Object[]{po.getGroupName(), po.isActive()?Boolean.TRUE:Boolean.FALSE, po.getId()});
    		
    		//update group permission
    		StringBuffer deleteGroupPermissionSql = new StringBuffer("DELETE FROM po_group_permission WHERE groupId = ?");
    		tx.update(deleteGroupPermissionSql.toString(), new Object[]{po.getId()});
    		
    		StringBuffer groupPermissionSql = new StringBuffer(
            		"INSERT INTO po_group_permission " +
            		"(groupId, permissionId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            ArrayList permission = po.getPermission();
            if(permission != null && permission.size() > 0){
            	for(int i=0; i<permission.size(); i++){
            		String perm = (String)permission.get(i);
            		tx.update(groupPermissionSql.toString(), new Object[] { po.getId(), perm });
            	}
            }
    		
    		//update group user
    		StringBuffer deleteGroupUserSql = new StringBuffer("DELETE FROM po_group_user WHERE groupId = ?");
    		tx.update(deleteGroupUserSql.toString(), new Object[]{po.getId()});
            
    		StringBuffer groupUserSql = new StringBuffer(
            		"INSERT INTO po_group_user " +
            		"(groupId, userId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            String[] userIds = po.getUserIds();
            
            if(userIds != null && userIds.length > 0){
            	
            	for(int i=0; i<userIds.length; i++){
            		tx.update(groupUserSql.toString(), new Object[] {po.getId(), userIds[i]});
            	}
            }
            
            tx.commit();
    		
		}
		catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
	}
	
	public boolean hasPermission(String userId, String permission) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT groupUser.groupId " +
				"FROM po_group_user groupUser, po_group_permission groupPermission " +
				"WHERE groupUser.groupId = groupPermission.groupId " +
				"AND groupUser.userId = ? " +
				"AND groupPermission.permissionId = ?");
		
		Collection groups = super.select(sql.toString(), HashMap.class, new Object[]{userId, permission}, 0, -1);
		if(groups != null) {
			Iterator i = groups.iterator();
			if(i.hasNext()){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isUserInActiveGroup(String userId) throws DaoException {
		boolean isUserInActiveGroup = false;
		
		Collection col = super.select("SELECT userId " +
				"FROM po_group_user groupUser, po_group poGroup " +
				"WHERE groupUser.groupId = poGroup.id " +
				"AND poGroup.active = '1' " +
				"AND userId = ? ", HashMap.class, new Object[] {userId}, 0, 1);
		
		if(col != null) {
			if(col.size() > 0) {
				isUserInActiveGroup = true;
			}
		}
		
		return isUserInActiveGroup;
	}
	
	public boolean isHOD(String userId) throws DaoException {
		boolean isHOD = false;
		
		Collection col = super.select("SELECT userId " +
				"FROM org_chart_hierachy " +
				"WHERE hod = '1' " +
				"AND active = '1' " +
				"AND userId = ? ", HashMap.class, new Object[] {userId}, 0, 1);
		
		if(col != null) {
			if(col.size() > 0) {
				isHOD = true;
			}
		}
		
		return isHOD;
	}
	
	
	public Collection groupOfUser(String permissionId) throws DaoException {
		Collection user ;
		
		user = super.select("SELECT userId " +
				"FROM po_group_permission gr, po_group_user user " +
				"WHERE gr.groupId=user.groupId " +
				"AND permissionId = ? ", HashMap.class, new Object[] {permissionId}, 0, -1);
		
		
		return user;
	}
}
