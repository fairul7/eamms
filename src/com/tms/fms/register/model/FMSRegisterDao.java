package com.tms.fms.register.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.Group;
import kacang.services.security.SecurityDao;
import kacang.services.security.User;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;

public class FMSRegisterDao extends SecurityDao {
    /** Interface Implementation */

    /**
     * Initializes the data source, e.g. creates tables, etc.
     * @throws DaoException if a fatal error occurs.
     */
    public void init() throws DaoException {
       
        ////
        try {
        	super.update("ALTER TABLE security_user ADD department VARCHAR(255)", null);            
    	}
        catch(DaoException e) {
        }
        
        try {
        	super.update("ALTER TABLE security_user ADD unit VARCHAR(255)", null);            
    	}
        catch(DaoException e) {
        }
        
        // create index for security_user(username, unit)
        try {
        	super.update("CREATE INDEX username_unit ON security_user(username, unit)", null);
    	}
        catch(DaoException e) {
        }

      
        try {
			super.update(
					"CREATE TABLE fms_register_user (" +
						"id varchar(255) default '' NOT NULL, " +
						"username varchar(255), " +
						"firstName varchar(255), " +
						"lastName varchar(255), " +
						"department varchar(255), " +
						"designation varchar(255), " +
						"email1 varchar(255), " +
						"password varchar(255), " +
						"weakpass varchar(255), " +
						"active CHAR(1) default '' NOT NULL, " +
						"unit VARCHAR(255), " +
						"reason VARCHAR(255), " +
						"statusDate datetime" +
					")", null);
        } catch(DaoException e) {
        }
        
		try {
			super.update("ALTER TABLE fms_register_user ADD unit VARCHAR(255)", null);            
		} catch(DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE fms_register_user ADD reason VARCHAR(255)", null);            
		} catch(DaoException e) {
		}
		
		try {
			super.update("ALTER TABLE fms_register_user ADD statusDate datetime", null);            
		} catch(DaoException e) {
		}
		
		// create primary key for fms_register_user
		try {
			super.update("ALTER TABLE fms_register_user ADD CONSTRAINT PK_fms_register_user PRIMARY KEY CLUSTERED (id)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_register_user(username)
		try {
			super.update("CREATE INDEX username ON fms_register_user(username)", null);
		} catch (Exception e) {
		}
		
		try{
    		super.update("ALTER TABLE fms_register_user ADD " +
    				"telMobile nvarchar(255) NULL, " +
    				"telOffice nvarchar(255) NULL, " +
    				"staffID varchar(255) NULL", null);
    	} catch (DaoException e) {}
        
        
        try {         
        	addDefaultGroup();
        }catch(Exception e){
        }
    }
    
    public void storeUser(User user) throws DaoException {
        // check existing user
        Collection existing = super.select("SELECT id FROM security_user WHERE id=?", HashMap.class, new Object[] { user.getId() }, 0, 1);
        if (existing.size() > 0) {
            super.update(
                    "UPDATE security_user SET username = #username#, password = #password#, weakpass = #weakpass#, " +
                    "firstName = #firstName#, lastName = #lastName#, nickName = #nickName#, title = #title#," +
                    "department = #department#, designation = #designation#, unit = #unit#, email1 = #email1#, email2 = #email2#, email3 = #email3#, company = #company#, homepage = #homepage#," +                    
                    "address = #address#, postcode = #postcode#, city = #city#, state = #state#, country = #country#, telOffice = #telOffice#," +
                    "telHome = #telHome#, telMobile = #telMobile#, fax = #fax#, notes = #notes#, active = #active#," +
                    "property1 = #property1#, property2 = #property2#, property3 = #property3#, property4 = #property4#, property5 = #property5#, locale=#locale# WHERE id=#id#", user);
        } else {
            // user not found, insert user
            super.update(
                    "INSERT INTO security_user (id, username, password, weakpass, " +
                    "firstName, lastName, nickName, title, designation, department, unit, " +                    
                    "email1, email2, email3, company, homepage, address, postcode, city, state, country, " +
                    "telOffice, telHome, telMobile, fax, notes, active," +
                    "property1, property2, property3, property4, property5, locale) VALUES " +
                    "(#id#, #username#, #password#, #weakpass#, " +
                    "#firstName#, #lastName#, #nickName#, #title#, #designation#, #department#, #unit#, " +                    
                    "#email1#, #email2#, #email3#, #company#, #homepage#, #address#, #postcode#, #city#, #state#, #country#, " +
                    "#telOffice#, #telHome#, #telMobile#, #fax#, #notes#, #active#," +
                    "#property1#, #property2#, #property3#, #property4#, #property5#, #locale#)", user);
        }
    }
        
    
      ////
	public Collection selectFMSRegisterUser(String filter,String deptFilter, String sort, boolean desc, int startIndex,int maxRows,String active) throws DaoException{
		String sql = 
			"SELECT r.id, username, firstName, lastName, department, d.name AS departmentName, " +
				"designation, email1, password, weakpass, statusDate, active " +
			"FROM fms_register_user r " +
			"LEFT OUTER JOIN fms_department d ON (r.department = d.id) " +
			"WHERE 1 = 1 ";
	
		if (filter != null && !filter.equals("")) {
			sql += "AND username LIKE '%" + filter + "%' ";
		}
	
		if (deptFilter != null) {
			sql += "AND department='" + deptFilter + "' ";
		}
	
		if (!(null == active || "".equals(active))) {
			sql += "AND (active='"+active+"') ";
		}
		
		String strSort= "";
		if (sort != null) {
			strSort += "ORDER BY " + sort + (desc ? " DESC" : "");
		} else { 
			strSort += "ORDER BY username ASC";
		}
		sql += strSort;
		
		return super.select(sql,User.class,null,startIndex,maxRows);
	}
        
        public void storeFMSUser(User user) throws DaoException {
            // check existing user
            Collection existing = super.select("SELECT id FROM fms_register_user WHERE username=?", HashMap.class, new Object[] { user.getUsername() }, 0, 1);
            if (existing.size() > 0) {
                super.update(
                        "UPDATE fms_register_user SET id = #id#, firstName = #firstName#, lastName = #lastName#, password = #password#, weakpass = #weakpass#, " +                    
                        "department = #department#, unit = #unit#, designation = #designation#, email1 = #email1#, statusDate = #statusDate#, active=#active#, " +
                        "staffID=#staffID#,telMobile=#telMobile#,telOffice=#telOffice# " +
                        "WHERE username=#username#", user);
            } else {
                // user not found, insert user
                super.update(
                        "INSERT INTO fms_register_user (id, username, firstName, lastName, password, weakpass, " +
                        "designation, department, unit, email1, statusDate, active, " +
                        "staffID,telMobile,telOffice) VALUES " +
                        "(#id#, #username#, #firstName#, #lastName#, #password#, #weakpass#, " +
                        "#designation#, #department#, #unit#, #email1#, #statusDate#, #active#, " +
                        "#staffID#,#telMobile#,#telOffice#)", user);
            }
        }
        
        public User selectFMSUser(String userId) throws DaoException, DataObjectNotFoundException {
            Object[] args = new Object[]{userId};
            Collection list = super.select("SELECT id, username, firstName, lastName, department, unit, designation, email1, password, weakpass, " +
            		"staffID, telMobile, telOffice " +
            		"FROM fms_register_user WHERE id = ?", User.class, args, 0, -1);
            if (list.size() <= 0) throw new DataObjectNotFoundException("User " + userId + " unavailable");
            return (User) list.iterator().next();
        }
        
        public void deleteFMSUser(String userId){
            try{
                String sql = "DELETE FROM fms_register_user WHERE id=? ";
                super.update(sql,new String[]{userId});
            }catch(Exception e){
            	Log.getLog(getClass()).error(e.toString(), e);
            }
        }
        
        public void updateFMSUser(String active, String reason, Date statusDate, String userId)throws DaoException {
        	
        	super.update("UPDATE fms_register_user SET active = ?, reason = ?, statusDate = ? WHERE id = ?", new Object[] {active, reason, statusDate, userId});
        	
        }
        
        public void addDefaultGroup() throws DaoException {    	 
            String fmsGroupId = Application.getInstance().getProperty("FMSUserGroup");
            Collection groupExist = super.select("SELECT id FROM security_group WHERE id=?", HashMap.class, new Object[] { fmsGroupId }, 0, 1);
            if (groupExist.size() == 0) {
            	Group group = new Group();	            	
            	group.setId(fmsGroupId);
            	group.setGroupName(FMSRegisterManager.GROUP_FMS_USER);
            	group.setProperty("active","1");        	         	
            	storeGroup(group);	                
            }
            
            
            Map eammsGroups = new SequencedHashMap();
            eammsGroups.put(Application.getInstance().getProperty("SuperAdministrator"), FMSRegisterManager.GROUP_EAMMS_SUPER_ADMINISTRATOR);
            eammsGroups.put(Application.getInstance().getProperty("Administrator"), FMSRegisterManager.GROUP_EAMMS_ADMINISTRATOR);
            eammsGroups.put(Application.getInstance().getProperty("UnitHead"), FMSRegisterManager.GROUP_EAMMS_UNIT_HEAD);
            eammsGroups.put(Application.getInstance().getProperty("SeniorEngineer"), FMSRegisterManager.GROUP_EAMMS_SENIOR_ENGINEER);
            eammsGroups.put(Application.getInstance().getProperty("Engineer"), FMSRegisterManager.GROUP_EAMMS_ENGINEER);
            eammsGroups.put(Application.getInstance().getProperty("NormalUser"), FMSRegisterManager.GROUP_EAMMS_NORMAL_USER);
            eammsGroups.put(Application.getInstance().getProperty("UnitHeadEngineering"), FMSRegisterManager.GROUP_EAMMS_UNIT_HEAD_ENGINEERING);
            eammsGroups.put(Application.getInstance().getProperty("NetworkEngineer"), FMSRegisterManager.GROUP_EAMMS_NETWORK_ENGINEER);
            eammsGroups.put(Application.getInstance().getProperty("UnitHeadNetwork"), FMSRegisterManager.GROUP_EAMMS_UNIT_HEAD_NETWORK);
            eammsGroups.put(Application.getInstance().getProperty("MCR"), FMSRegisterManager.GROUP_EAMMS_MCR);
            eammsGroups.put(Application.getInstance().getProperty("NetworkSuperUser"), FMSRegisterManager.GROUP_EAMMS_NETWORK_SUPER_USER);
            eammsGroups.put(Application.getInstance().getProperty("NetworkUser"), FMSRegisterManager.GROUP_EAMMS_NETWORK_USER);
            eammsGroups.put(Application.getInstance().getProperty("Intern"), FMSRegisterManager.GROUP_EAMMS_INTERN);
            
            for (Iterator i = eammsGroups.keySet().iterator(); i.hasNext();) {    
            	String id = (String) i.next();
            	String groupName = (String) eammsGroups.get(id);
	        	Collection checkEammsGroup = super.select("SELECT id FROM security_group WHERE id=?", HashMap.class, new Object[] { id }, 0, 1);
	        	if(checkEammsGroup == null || checkEammsGroup.size() == 0){
	        			Log.getLog(getClass()).info("ADD: "+ id + "||"+groupName);
	        			Group group = new Group();	            	
	                	group.setId(id);
	                	group.setGroupName(groupName);
	                	group.setProperty("active","1");        	         	
	                	storeGroup(group);		    
            	 }
            }
           
        }
        
        public User selectUser(String userId) throws DaoException, DataObjectNotFoundException {
            Object[] args = new Object[]{userId};
            Collection list = super.select("SELECT id, username, password, weakpass, firstName, lastName, nickName, title, designation, email1, email2, email3, company, homepage, address, postcode, city, state, country, telOffice, telHome, telMobile, fax, notes, property1, property2, property3, property4, property5, active, department, unit, locale FROM security_user WHERE id = ?", User.class, args, 0, -1);
            if (list.size() <= 0) throw new DataObjectNotFoundException("User " + userId + " unavailable");
            return (User) list.iterator().next();
        }
        ////
        
    	
    	public String getUserDepartment(String userId)throws DaoException{
    		
    		String sql = "SELECT department FROM security_user WHERE id = ?";
    		Object[] arg = new Object[] {userId};
    		String dept = null;
    		
    		Collection tmp = super.select(sql.toString(), HashMap.class, arg, 0, -1);
    		for (Iterator i=tmp.iterator(); i.hasNext();) {
                HashMap m = (HashMap)i.next();
                dept = (String) m.get("department");
            }
    		
    		return dept;    			
    			
    	}
    	
    	public Collection selectUsersByDepartment(String deptId, String userId) throws DaoException {
            String query = "SELECT u.id, u.username, u.password, u.weakpass, u.firstName, u.lastName, "+
			"u.nickName, u.title, u.designation, u.email1, u.email2, u.email3, u.company, "+
			"u.homepage, u.address, u.postcode, u.city, u.state, u.country, u.telOffice, "+
			"u.telHome, u.telMobile, u.fax, u.notes, u.property1, u.property2, u.property3, "+ 
			"u.property4, u.property5, u.active, u.locale "+			
			"FROM security_user u, fms_department d "+
			"WHERE u.department=d.id AND d.id = ? AND u.id= ?" ;
            		
            return super.select(query, User.class, new String[]{deptId, userId}, 0, -1);
        }
    	
    	public Collection selectExistUser(String username, String pending, String accepted)throws DaoException {
    		
    		return super.select("SELECT id FROM fms_register_user WHERE username=? AND (active=? OR active=?)", HashMap.class, new Object[] { username,pending,accepted }, 0, 1);
    	}
        
    	    	
    	public Collection selectUserWorkingProfile(String userId, String startTime, String endTime) throws DaoException
        {		
    		////
    		 String filterDate = "";
    		 Object arg[] = new Object[]{userId,startTime,endTime};
    	        if(startTime != null && endTime != null){
    	        	filterDate += " AND " +	        	
                   
    	        	"(startTime <= ? AND endTime >= ?) ";
    	        	
    	        }      
    		 ////

    		String sql = "Select DISTINCT m.userId, d.workingProfileDurationId, p.workingProfileId, d.startDate, d.endDate, p.startTime, p.endTime " +
    		             "FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON p.workingProfileId=d.workingProfileId " +    		             
    		             "INNER JOIN fms_working_profile_duration_manpower m ON d.workingProfileDurationId=m.workingProfileDurationId " +
    		             "WHERE m.userId = ? " + filterDate;		             
    		             
            return super.select(sql,ManpowerAssignmentObject.class,arg,0,-1);
            
        }
    	
    	public Collection selectUsersByDepartment(String deptId)throws DaoException{
    		
    		 String sql = "select * from security_user where department = ?";
 	        
    		 return super.select(sql,ManpowerAssignmentObject.class,new String[]{deptId},0,-1);
    		 
    	}
    	
    	
    	public Collection selectDriversByAssignment(String deptId, Date start, Date end,String startTime, String endTime) throws DaoException
        {			
    		//for users who has working profile inside request time/date
    		/*String filterDate = "";
    		 Object arg[] = new Object[]{deptId}; //new Object[]{deptId, start, end, startTime, endTime};
    	        if(start != null && end != null){
    	        	filterDate += " AND " +	        
                  
    	        	"(Dur.startDate <= ? AND Dur.endDate >= ?) ";
    	        	
    	        	arg = new Object[]{deptId, start, end};	        	
    	        }      
    		      
    	        if(startTime != null && endTime != null){
    	        	filterDate += " AND " +            
    	        	
    	        	"(Pro.startTime <= ? AND Pro.endTime >= ?)";
    	        	
    	        	arg = new Object[]{deptId, start, end, startTime, endTime};
    	        }   
    	        
    	        String sql = "SELECT DISTINCT SU.id, SU.* " +    	       
    	        "FROM fms_working_profile_duration_manpower Man "+      
    	        "INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId "+
    	        "INNER JOIN fms_working_profile Pro ON pro.workingProfileId = Dur.workingProfileId "+
    	        "INNER JOIN security_user SU ON SU.id =  Man.userId "+
    	        "WHERE 1=1 AND " +
    	        "SU.department = ?" + filterDate;
    	        
            return super.select(sql,ManpowerAssignmentObject.class,arg,0,-1);       */ 
            
    		
    		
            

    		Object arg[] = null;
            
            String sql = "SELECT DISTINCT SU.id, SU.* " +    	       
	        "FROM fms_working_profile_duration_manpower Man "+      
	        "INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId "+
	        "INNER JOIN fms_working_profile Pro ON pro.workingProfileId = Dur.workingProfileId "+
	        "INNER JOIN security_user SU ON SU.id =  Man.userId "+
	        "WHERE 1=1";
				
		if (deptId != null && !"".equals(deptId)) {
			sql += " AND ( SU.department = '" + deptId + "' ) ";
		}
		
		if (null != start && !"".equals(start) || null != end && !"".equals(end)) {
			arg = new Object[]{start, end};
			sql += " AND ( Dur.startDate <= ? AND Dur.endDate >= ? ) ";
		}
		
		if (null != startTime && !"".equals(startTime) || null != endTime && !"".equals(endTime)) {
			
			sql += " AND ( Pro.startTime <= '" + startTime + "' AND Pro.endTime >= '" + endTime + "' ) ";
		}
		
		return super.select(sql,ManpowerAssignmentObject.class,arg,0,-1);  
		
        }
    	
    	public Collection selectManPowerWithoutWorkingProfile(Date start, Date end, String deptId) throws DaoException
        {			
    		//for users who don't have any working profile on that day, so assigned to DEFAULT working profile
    		 	Object arg[] = new Object[]{start, end, deptId};    	            		      
    	        String sql = "SELECT DISTINCT SU.id, SU.* FROM security_user SU WHERE SU.id not in " +  
    	        
    	        "(SELECT DISTINCT Man.userId  "+      
    	        "FROM fms_working_profile_duration_manpower Man  "+
    	        "INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId  "+
    	        "INNER JOIN fms_working_profile Pro ON pro.workingProfileId = Dur.workingProfileId  "+
    	        "WHERE 1=1 AND " +
    	        "startDate <= ? AND endDate >= ? ) " +
    	        
    	        "AND SU.department = ?";
    	        
            return super.select(sql,ManpowerAssignmentObject.class,arg,0,-1);        
        }
    	
    	public Collection checkTimeIsInWorkingProfileDefault(String startTime, String endTime) throws DaoException
        {			
    		//check whether the time is in range of working profile's default
    			Object arg[] = new Object[]{startTime, endTime};    	            		      
    	        String sql = "SELECT DISTINCT startTime, endTime FROM fms_working_profile " +    	           	        
    	        "WHERE defaultProfile = '1' AND  " +
    	        "startTime <= ?  AND endTime >= ?";    	        
    	        
            return super.select(sql,ManpowerAssignmentObject.class,arg,0,-1);        
        }
    	
    	
    	public ManpowerAssignmentObject selectWorkingProfileUser(String userId) throws DaoException
        {		
    		ManpowerAssignmentObject mObj = null;
    		Collection cOL = new ArrayList();

    		String sql = "Select DISTINCT m.userId, d.workingProfileDurationId, p.workingProfileId, d.startDate, d.endDate, p.startTime, p.endTime " +
    		             "FROM fms_working_profile_duration d INNER JOIN fms_working_profile p ON p.workingProfileId=d.workingProfileId " +    		             
    		             "INNER JOIN fms_working_profile_duration_manpower m ON d.workingProfileDurationId=m.workingProfileDurationId " +
    		             "WHERE m.userId = ? ";		             
    		             
    		cOL = super.select(sql,ManpowerAssignmentObject.class,new Object[]{userId},0,-1);
    		
    		if(cOL.size() > 0)
    			mObj = (ManpowerAssignmentObject) cOL.iterator().next();
    		
    		
    		return mObj;
                        
        }
    	
    	public String selectExistSecurityUser(String username)throws DaoException {
    		    		
    	    	
    	    String id = null;	
    		String sql = "SELECT id FROM security_user " +
    		    			"WHERE username = ? ";
    		    			

    		    	Collection col = super.select(sql, HashMap.class, new Object[] {username}, 0, 1);
    		    	if(col != null) {
    		    		if(col.size() > 0) {
    		    			HashMap map = (HashMap) col.iterator().next();
    		    			id = map.get("id").toString();
    		    		}
    		    	}
    	    	
    	    	return id;
    	        		
    	}
    	
    	public Collection selectUsersByUnit(String unit, String groupId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException {
    		
    		String strSort = "";
	        Collection args = new ArrayList();
	        
	        args.addAll(Arrays.asList(properties.getArray()));
    	        if (sort != null) {
    	        	if ("firstName".equals(sort)) {
    	        		sort = "u.firstName";
    	        	}
    	            strSort += " ORDER BY " + sort;
    	            if (descending)
    	                strSort += " DESC";
    	        }
    	                           
            String query = "SELECT DISTINCT u.id as tmpId,u.* FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId	"; 
                             
            
            if (unit != null && !"".equals(unit)){
    			if (!unit.equals("-1")){
    				query += " AND (u.unit = '" + unit + "') 	";
    				//args.add(unit);
    			}
    		}
            
            if (groupId != null && !"".equals(groupId)){
    			if (!groupId.equals("-1")){
    				query += " AND (g.id = '" + groupId + "') ";
    			}
    		}
            
//            if (keyword != null && !"".equals(keyword)){
//    	    	query += "AND (su.username LIKE ? OR su.firstName LIKE ? OR su.lastName LIKE ?) ";
//    	    	args.add("%" + keyword + "%");
//    	    	args.add("%" + keyword + "%");
//    	    	args.add("%" + keyword + "%");
//    	    }
            return super.select(query + properties.getStatement() + strSort, User.class, args.toArray(), start, maxResults);
        }
    	
    	public Collection selectUsersUnitByUnitApprover(String userId, String groupId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException {
    			    	
    		String strSort = "";
	        Collection args = new ArrayList();
	        args.add(userId);
	        args.add(userId);
	        args.addAll(Arrays.asList(properties.getArray()));
	        if (sort != null) {
	        	if ("firstName".equals(sort)) {
	        		sort = "u.firstName";
	        	}
	        	strSort += " ORDER BY " + sort;
	            if (descending)
	                strSort += " DESC";
	        }
    	        
    		/*String sql = "SELECT DISTINCT u.id as tmpId,u.* FROM security_user u, security_group g, security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId	"+ 
    		"AND (u.unit in(SELECT a.unitId FROM security_user su INNER JOIN fms_unit_alternate_approver a ON su.id=a.userId where su.id=?))	";
    		*/
    	       
	        String sql = "SELECT DISTINCT u.id as tmpId,u.* FROM security_user u, security_group g, 	" +
	        		"security_user_group ug WHERE u.id=ug.userId AND g.id=ug.groupId AND		" +
	        		"u.unit IN (	" +
	        		"SELECT a.unitId FROM security_user su INNER JOIN fms_unit_alternate_approver a ON su.id=a.userId 	" +
	        		"WHERE su.id = ?	" +
	        		"OR u.unit IN(	" +
	        		"SELECT u.id FROM security_user su INNER JOIN fms_unit u ON " +
	        		"su.id=u.HOU where su.id = ?))	";
    	        
//    	    if (keyword != null && !"".equals(keyword)){
//    	    	sql += "AND (su.username LIKE ? OR su.firstName LIKE ? OR su.lastName LIKE ?) ";
//    	    	args.add("%" + keyword + "%");
//    	    	args.add("%" + keyword + "%");
//    	    	args.add("%" + keyword + "%");
//    	    }
    		
    		if (groupId != null && !"".equals(groupId)){
    			if (!groupId.equals("-1")){
    				sql += " AND (g.id = '" + groupId + "') ";
    			}
    		}
    		
    		return super.select(sql + properties.getStatement() + strSort, User.class, args.toArray(), start, maxResults);    			
    		       		
    	}
    	
    
}

