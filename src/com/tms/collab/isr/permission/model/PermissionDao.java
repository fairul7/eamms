package com.tms.collab.isr.permission.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.Transaction;

public class PermissionDao extends DataSourceDao {
	public void init() {
		try{
			super.update("CREATE TABLE isr_group (" +
					"id varchar(255) NOT NULL, " +
					"groupName varchar(255) NOT NULL, " +
					"description varchar(255) DEFAULT '', " +
					"role varchar(255) NOT NULL, " +
					"active char(1) NOT NULL, " +
					"PRIMARY KEY(id))", null);
		} 
		catch(DaoException e){
			 // do nothing
		}
		
		try{
			super.update("CREATE TABLE isr_group_user (" +
					"groupId varchar(255) NOT NULL, " +
					"userId varchar(255) NOT NULL)", null);
		} 
		catch(DaoException e){
			// do nothing
		}
		
		try{
			super.update("CREATE TABLE isr_group_permission (" +
					"groupId varchar(255) NOT NULL, " +
					"permissionId varchar(255) NOT NULL)", null);
		} 
		catch(DaoException e){
			// do nothing
		}
	}
	
	public boolean hasPermission(String userId, String permission) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT groupUser.groupId " +
				"FROM isr_group_user groupUser, isr_group_permission groupPermission " +
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
	
	public int deleteGroups(String[] selectedKeys) throws DaoException{
		int results = 0;
		Transaction tx = null;
		
		try {
			tx = getTransaction();
            tx.begin();
            
            StringBuffer groupSql = new StringBuffer("DELETE FROM isr_group WHERE id IN (");
            StringBuffer groupPermissionSql = new StringBuffer("DELETE FROM isr_group_permission WHERE groupId IN (");
            StringBuffer groupUserSql = new StringBuffer("DELETE FROM isr_group_user WHERE groupId IN (");
            
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
	
	public void updateGroup(ISRGroup gp) throws DaoException{
		Transaction tx = null;
		
		try {
			tx = getTransaction();
            tx.begin();
            
            //update group data
    		StringBuffer groupSql = new StringBuffer("UPDATE isr_group SET groupName = ?, role = ?, active = ? WHERE id = ? ");
    		tx.update(groupSql.toString(), new Object[]{gp.getGroupName(), gp.getRole(), gp.getActive()?Boolean.TRUE:Boolean.FALSE, gp.getId()});
    		
    		//update group permission
    		StringBuffer deleteGroupPermissionSql = new StringBuffer("DELETE FROM isr_group_permission WHERE groupId = ?");
    		tx.update(deleteGroupPermissionSql.toString(), new Object[]{gp.getId()});
    		
    		StringBuffer groupPermissionSql = new StringBuffer(
            		"INSERT INTO isr_group_permission " +
            		"(groupId, permissionId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            ArrayList permission = gp.getPermission();
            if(permission != null && permission.size() > 0){
            	for(int i=0; i<permission.size(); i++){
            		String perm = (String)permission.get(i);
            		tx.update(groupPermissionSql.toString(), new Object[] { gp.getId(), perm });
            	}
            }
    		
    		//update group user
    		StringBuffer deleteGroupUserSql = new StringBuffer("DELETE FROM isr_group_user WHERE groupId = ?");
    		tx.update(deleteGroupUserSql.toString(), new Object[]{gp.getId()});
            
    		StringBuffer groupUserSql = new StringBuffer(
            		"INSERT INTO isr_group_user " +
            		"(groupId, userId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            String[] userIds = gp.getUserIds();
            
            if(userIds != null && userIds.length > 0){
            	
            	for(int i=0; i<userIds.length; i++){
            		tx.update(groupUserSql.toString(), new Object[] {gp.getId(), userIds[i]});
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
	
	public ISRGroup selectGroup(String id) throws DaoException{
		ISRGroup gp = new ISRGroup();
		
		//get group
		StringBuffer groupSql = new StringBuffer("SELECT id, groupName, role, active FROM isr_group WHERE id = ? ");
		Collection group = super.select(groupSql.toString(), ISRGroup.class, new Object[]{id}, 0, -1);
		
		Iterator i = group.iterator();
		if(i.hasNext()){
			gp = (ISRGroup)i.next();
		}
		
		//get permission of the group
		ArrayList permission = new ArrayList();
		StringBuffer groupPermissionSql = new StringBuffer("SELECT permissionId FROM isr_group_permission WHERE groupId = ? ");
		Collection groupPermissions = super.select(groupPermissionSql.toString(), HashMap.class, new Object[]{id}, 0, -1);
		
		Iterator j = groupPermissions.iterator();
		while(j.hasNext()){
			Map row = (Map)j.next();
			permission.add(row.get("permissionId").toString());
		}
		
		gp.setPermission(permission);
		
		//get user of the group
		StringBuffer groupUserSql = new StringBuffer("SELECT userId FROM isr_group_user WHERE groupId = ? ");
		Collection groupUsers = super.select(groupUserSql.toString(), HashMap.class, new Object[]{id}, 0, -1);
		String[] userIds = new String[groupUsers.size()];
		
		int counter = 0;
		Iterator k = groupUsers.iterator();
		while(k.hasNext()){
			Map row = (Map)k.next();
			userIds[counter] = row.get("userId").toString();
			counter++;
		}
		
		gp.setUserIds(userIds);
		
		return gp;
	}
	
	public void insertGroup(ISRGroup gp) throws DaoException{
		Transaction tx = null;
		
		try {
            
			//insert new group record into group table
			StringBuffer newGroupSql = new StringBuffer(
                    "INSERT INTO isr_group " +
                    "(id, groupName, role, active) " +
                    "VALUES " +
                    "(#id#, #groupName#, #role#, #active#)");
            
            tx = getTransaction();
            tx.begin();
            tx.update(newGroupSql.toString(), gp);
            
            //insert permission into table
            StringBuffer groupPermissionSql = new StringBuffer(
            		"INSERT INTO isr_group_permission " +
            		"(groupId, permissionId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            ArrayList permission = gp.getPermission();
            if(permission != null && permission.size() > 0){
            	
            	for(int i=0; i<permission.size(); i++){
            		String perm = (String)permission.get(i);
            		tx.update(groupPermissionSql.toString(), new Object[] { gp.getId(), perm });
            	}
            }
            
            //insert user into table
            StringBuffer groupUserSql = new StringBuffer(
            		"INSERT INTO isr_group_user " +
            		"(groupId, userId) " +
            		"VALUES " +
            		"(?, ?) "
            		);
            
            String[] userIds = gp.getUserIds();
            
            if(userIds != null && userIds.length > 0){
            	
            	for(int i=0; i<userIds.length; i++){
            		tx.update(groupUserSql.toString(), new Object[] {gp.getId(), userIds[i]});
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
	
	public Collection selectGroupListing(String searchBy, String sort, boolean desc, int start, int rows) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT id, groupName, role, description, active FROM isr_group WHERE 1=1 ");
		
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
		
		Collection col = super.select(sql.toString(), ISRGroup.class, null, start, rows);
		if(col != null) {
			Application app = Application.getInstance();
			
			for(Iterator i=col.iterator(); i.hasNext();) {
				ISRGroup group = (ISRGroup) i.next();
				group.setRole(app.getMessage(group.getRole()));
			}
		}
		
		return col;
	}
	
	public int selectGroupListingCount(String searchBy) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total FROM isr_group WHERE 1=1 ");
		if(searchBy != null && !searchBy.trim().equals("")){
			sql.append("AND ( groupName LIKE '%" + searchBy + "%' ");
			sql.append(" OR description LIKE '%" + searchBy + "%' ) ");
		}
		
		Map row = (Map) super.select(sql.toString(), HashMap.class, null, 0, -1).iterator().next();
        return Integer.parseInt(((Number) row.get("total")).toString());
	}
	
	public boolean isGroupNameExist(String groupName) throws DaoException {
		boolean isGroupNameExist = false;
		
		Collection col = super.select("SELECT id FROM isr_group " +
				"WHERE LOWER(groupName) = ?", HashMap.class, new Object[] {groupName}, 0, -1);
		
		if(col != null) {
			Iterator i = col.iterator();
			if(i.hasNext()){
				isGroupNameExist = true;
			}
		}
		
		return isGroupNameExist;
	}
	
	public boolean hasISRRole(String userId, String role) throws DaoException {
		boolean hasISRRole = false;
		
		Collection col = super.select("SELECT groupUser.groupId " +
				"FROM isr_group isrgroup, isr_group_user groupUser " +
				"WHERE isrgroup.id = groupUser.groupId " +
				"AND groupUser.userId = ? " +
				"AND isrgroup.role = ?", HashMap.class, new Object[] {userId, role}, 0, -1);
		if(col != null) {
			if(col.size() > 0) {
				hasISRRole = true;
			}
		}
		
		return hasISRRole;
	}
	
	public boolean isUserInActiveGroup(String userId) throws DaoException {
		boolean isUserInActiveGroup = false;
		
		Collection col = super.select("SELECT userId " +
				"FROM isr_group_user groupUser, isr_group isrGroup " +
				"WHERE groupUser.groupId = isrGroup.id " +
				"AND isrGroup.active = '1' " +
				"AND userId = ? ", HashMap.class, new Object[] {userId}, 0, 1);
		
		if(col != null) {
			if(col.size() > 0) {
				isUserInActiveGroup = true;
			}
		}
		
		return isUserInActiveGroup;
	}
	
	public Collection getDeptUsers(String deptCode, String countryCode) throws DaoException {
		return super.select("SELECT securityUser.id, securityUser.username, securityUser.firstName, securityUser.lastName " +
				"FROM security_user securityUser, isr_group_user userGroup, isr_group isrGroup, org_chart_hierachy hierachy  " +
				"WHERE securityUser.id = userGroup.userId " +
				"AND userGroup.groupId = isrGroup.id " +
				"AND securityUser.id = hierachy.userId " +
				"AND hierachy.deptCode = ? " +
				"AND hierachy.countryCode = ? " +
				"ORDER BY securityUser.firstName, securityUser.lastName", User.class, new Object[] {deptCode, countryCode}, 0, -1);
	}
	
	public Collection getDeptUsersWithRole(String deptCode, String countryCode, String role) throws DaoException {
		return super.select("SELECT securityUser.id, securityUser.username, securityUser.firstName, securityUser.lastName " +
				"FROM security_user securityUser, isr_group_user userGroup, isr_group isrGroup, org_chart_hierachy hierachy  " +
				"WHERE securityUser.id = userGroup.userId " +
				"AND userGroup.groupId = isrGroup.id " +
				"AND isrGroup.role = ? " +
				"AND securityUser.id = hierachy.userId " +
				"AND hierachy.deptCode = ? " +
				"AND hierachy.countryCode = ? " +
				"ORDER BY securityUser.firstName, securityUser.lastName", User.class, new Object[] {role, deptCode, countryCode}, 0, -1);
	}
	
	public Collection getAllISRUsers() throws DaoException {
		return super.select("SELECT distinct groupUser.userId id, username, firstName, lastName, email1 " +
				"FROM isr_group_user groupUser, security_user user, org_chart_hierachy hierachy " +
				"WHERE groupUser.userId = user.id" +
				"AND groupUser.userId = hierachy.userId AND hierachy.active = '1'", User.class, null, 0, -1);
	}
	
	public Collection getGroupDeptStaff(String groupId) throws DaoException {
		return super.select("SELECT securityUser.id, securityUser.username, securityUser.firstName, securityUser.lastName, department.shortDesc AS deptCode " +
				"FROM isr_group_user, security_user securityUser, org_chart_hierachy hierachy, org_chart_department department WHERE " +
				"securityUser.id = isr_group_user.userId " +
				"AND isr_group_user.userId = hierachy.userId " +
				"AND hierachy.deptCode=department.code " +
				"AND isr_group_user.groupId = ? " +
				"ORDER BY department.shortDesc, securityUser.firstName ", User.class, new Object[] {groupId}, 0, -1);
	}
}
