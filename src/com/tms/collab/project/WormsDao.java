package com.tms.collab.project;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.document.Document;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.emeeting.Meeting;
import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyHandler;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataSourceDao;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.JdbcUtil;
import kacang.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class WormsDao extends DataSourceDao
{
    public void init() throws DaoException
    {
    	try {
			super.update("ALTER TABLE worms_performance_report_project DROP PRIMARY KEY", null);
			super.update("ALTER TABLE worms_performance_report_project ADD PRIMARY KEY (reportId)", null);
			
		}catch(Exception e){}
		try {
			super.update("ALTER TABLE worms_performance_report_task MODIFY  estimatedMandays DOUBLE(11,2)", null);
			
		}catch(Exception e){}
    	//Update Old Project
    	try {
    		Collection col = super.select("SELECT DISTINCT worms_project.projectId AS worms_projectId, tm_category.id AS tm_categoryId  FROM worms_project, timesheet, tm_task, tm_category " +
    				" WHERE worms_project.projectId=timesheet.projectId AND timesheet.taskId=tm_task.id AND tm_task.categoryId=tm_category.id", HashMap.class, null, 0, -1);
    		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String projectId=map.get("worms_projectId").toString();
    			String catId=map.get("tm_categoryId").toString();
    			if(projectId==null){
    				projectId="";
    			}if(catId==null){
    				catId="";
    			}
    			if(!catId.equals(projectId)){
    				super.update("UPDATE tm_category set id=? WHERE id=?", new Object[]{projectId,catId});	
    				super.update("UPDATE tm_task set categoryId=? WHERE categoryId=?", new Object[]{projectId,catId});	
    			}
    		}
    	}
    	catch(Exception e) {
            //ignore
        }
    	//Update Old Project
    	try {
    		Collection col = super.select("SELECT DISTINCT worms_project.projectId AS worms_projectId, tm_category.id AS tm_categoryId  FROM worms_project, tm_category " +
    				" WHERE worms_project.projectName=tm_category.name", HashMap.class, null, 0, -1);
    		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String projectId=map.get("worms_projectId").toString();
    			String catId=map.get("tm_categoryId").toString();
    			if(projectId==null){
    				projectId="";
    			}if(catId==null){
    				catId="";
    			}
    			if(!catId.equals(projectId)){
    				super.update("UPDATE tm_category set id=? WHERE id=?", new Object[]{projectId,catId});	
    				super.update("UPDATE tm_task set categoryId=? WHERE categoryId=?", new Object[]{projectId,catId});	
    			}
    		}
    	}
    	catch(Exception e) {
            //ignore
        }
    	//Update Old Project
    	try {
    		String s="";
    		Collection col = super.select("SELECT DISTINCT worms_project.projectId AS worms_projectId, worms_project.projectName AS projectName  FROM worms_project, tm_category " +
    				" WHERE worms_project.projectId=tm_category.id", HashMap.class, null, 0, -1);
    		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String projectId=map.get("worms_projectId").toString();
    			 s += "AND worms_project.projectId <>'"+projectId+"' ";
    		}
    		Collection col2 = super.select("SELECT DISTINCT worms_project.projectId AS worms_projectId, worms_project.projectName AS projectName, " +
    				"worms_project.projectDescription AS projectDescription, worms_project.ownerId AS ownerId FROM worms_project " +
    				" WHERE 1=1 "+s, HashMap.class, null, 0, -1);

    		for (Iterator mapIt = col2.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String projectId=map.get("worms_projectId").toString();
    			String projectName=map.get("projectName").toString();
    			String description=map.get("projectDescription").toString();
    			String ownerId=map.get("ownerId").toString();

    			 String sql="INSERT INTO tm_category(id,name,description,userId,general)" +
                 "VALUES(?,?,?,?,'0')";
             super.update(sql,new String[]{projectId,projectName,description,ownerId});
    		}
    	}
    	catch(Exception e) {
            //ignore
        }
    	//Update Old Project
    	try {
    		String s="";
    		System.out.println("Projects Not Link With Task Category");
    		Collection col = super.select("SELECT DISTINCT worms_project.projectId AS worms_projectId, worms_project.projectName AS projectName  FROM worms_project, tm_category " +
    				" WHERE worms_project.projectId=tm_category.id", HashMap.class, null, 0, -1);
    		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String projectId=map.get("worms_projectId").toString();
    			 s += "AND worms_project.projectId <>'"+projectId+"' ";
    		}
    		Collection col2 = super.select("SELECT DISTINCT worms_project.projectId AS worms_projectId, worms_project.projectName AS projectName  FROM worms_project " +
    				" WHERE 1=1 "+s, HashMap.class, null, 0, -1);
    		int i=1;
    		for (Iterator mapIt = col2.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String projectId=map.get("worms_projectId").toString();
    			String projectName=map.get("projectName").toString();
    			System.out.println(i+" ID: "+projectId+" Name:"+projectName);
    			i++;
    		}
    	}
    	catch(Exception e) {
            //ignore
        }
    	
		/* Alter Tables */
    	try {
            super.update("ALTER TABLE worms_performance_report_defects ADD severity DOUBLE(11,2) DEFAULT 0.0",null);
        }catch(Exception e) {}
    	try {
            super.update("ALTER TABLE worms_performance_report_project ADD actualProjectStartDate VARCHAR(100) DEFAULT ''",null);
            super.update("ALTER TABLE worms_performance_report_project ADD actualProjectEndDate VARCHAR(100) DEFAULT ''",null);            
            super.update("ALTER TABLE worms_performance_report_project ADD startVariance VARCHAR(100) DEFAULT ''",null);
            super.update("ALTER TABLE worms_performance_report_project ADD endVariance VARCHAR(100) DEFAULT ''",null);
            super.update("ALTER TABLE worms_performance_report_project ADD estDuration VARCHAR(100) DEFAULT ''",null);
            super.update("ALTER TABLE worms_performance_report_project ADD actDuration VARCHAR(100) DEFAULT ''",null);
        }catch(Exception e) {}
    	try {
            super.update("ALTER TABLE worms_performance_report_milestone ADD estVariance VARCHAR(100) DEFAULT ''",null);
            super.update("ALTER TABLE worms_performance_report_milestone ADD actVariance VARCHAR(100) DEFAULT ''",null);
        }catch(Exception e) {}
    	try {
            super.update("ALTER TABLE worms_project ADD clientName VARCHAR(100) DEFAULT ''",null);
        }catch(Exception e) {}
        try {
            super.update("ALTER TABLE worms_project ADD projectCurrencyType VARCHAR(3)",null);
        }
        catch(Exception e) {}
		try
		{
			super.update("ALTER TABLE worms_project ADD projectWorkingDays VARCHAR(15)", null);
			super.update("UPDATE TABLE worms_project SET projectWorkingDays='2,3,4,5,6'", null);
		}
		catch(Exception e) {}
		try
		{
            super.update("ALTER TABLE worms_project ADD projectSummary TEXT", null);
		}
		catch(Exception e) {}
		try
		{
			super.update("ALTER TABLE worms_project ADD archived CHAR DEFAULT '0' NOT NULL", null);
		}
		catch(Exception e) {}
        try
        {
            Calendar cal = Calendar.getInstance();
            super.update("ALTER TABLE worms_project ADD creationDate datetime DEFAULT NULL", null);
            super.update("ALTER TABLE worms_project ADD modifiedDate datetime DEFAULT NULL", null);
            super.update("UPDATE worms_project SET creationDate=?, modifiedDate=?", new Object[] {cal.getTime(), cal.getTime()});
        }catch(Exception e){}
        try {
		/* Milestone Meetings */
		super.update("CREATE TABLE worms_milestone_meeting(milestoneId VARCHAR(35) NOT NULL, meetingId VARCHAR(255) NOT NULL, " +
            "PRIMARY KEY(milestoneId, meetingId))", null);
		/* Project Files */
		super.update("CREATE TABLE worms_project_file(projectId VARCHAR(35) NOT NULL, contentId VARCHAR(255) NOT NULL, " +
			"PRIMARY KEY(projectId, contentId))", null);
		/* Descriptor Tables */
		super.update("CREATE TABLE worms_descriptor(descId VARCHAR(35) NOT NULL, milestoneId VARCHAR(35) NOT NULL, " +
			"descName VARCHAR(250) NOT NULL, descDescription TEXT NOT NULL, descStart INT NOT NULL, descDuration INT NOT NULL, " +
			"PRIMARY KEY(descId))", null);
        /* Template Tables */
        super.update("CREATE TABLE worms_template(templateId VARCHAR(35) NOT NULL, templateName VARCHAR(250) NOT NULL, " +
            "templateDescription TEXT, templateCategory VARCHAR(250) NOT NULL, PRIMARY KEY(templateId))", null);
        super.update("CREATE TABLE worms_template_milestone(templateId VARCHAR(35) NOT NULL, milestoneId VARCHAR(35) NOT NULL, " +
            "PRIMARY KEY(templateId, milestoneId))", null);
        /* Milestone Tables */
        super.update("CREATE TABLE worms_milestone(milestoneId VARCHAR(35) NOT NULL, milestoneName VARCHAR(250) NOT NULL, " +
            "milestoneDescription TEXT, milestoneProgress INT NOT NULL, projectId VARCHAR(35) NOT NULL, milestoneOrder INT NOT NULL, " +
            "PRIMARY KEY(milestoneId))", null);
        super.update("CREATE TABLE worms_milestone_task(milestoneId VARCHAR(35) NOT NULL, taskId VARCHAR(255) NOT NULL, " +
            "PRIMARY KEY(milestoneId, taskId))", null);
        /* Role Tables */
        super.update("CREATE TABLE worms_role(roleId VARCHAR(35) NOT NULL, roleName VARCHAR(250) NOT NULL, " +
            "roleDescription TEXT, projectId VARCHAR(35) NOT NULL, PRIMARY KEY(roleId))", null);
        super.update("CREATE TABLE worms_role_competency(roleId VARCHAR(35) NOT NULL, competencyId VARCHAR(35) NOT NULL, " +
            "PRIMARY KEY(roleId, competencyId))", null);
        super.update("CREATE TABLE worms_role_user(roleId VARCHAR(35) NOT NULL, userId VARCHAR(250) NOT NULL, " +
            "PRIMARY KEY(roleId, userId))", null);
        /* Project Tables */
        super.update("CREATE TABLE worms_project(projectId VARCHAR(35) NOT NULL, projectName VARCHAR(250) NOT NULL, " +
            "projectCategory VARCHAR(250) NOT NULL, projectDescription TEXT, projectValue DOUBLE NOT NULL, " +
            "ownerId VARCHAR(250) NOT NULL, projectWorkingDays VARCHAR(15), projectSummary TEXT, archive CHAR NOT NULL, " +
			"creationDate datetime DEFAULT NULL, modifiedDate datetime DEFAULT NULL, PRIMARY KEY(projectId))", null);
        super.update("CREATE TABLE worms_project_task(projectId VARCHAR(35) NOT NULL, taskId VARCHAR(35) NOT NULL, " +
            "PRIMARY KEY(projectId, taskId))", null);
        }catch(Exception e){}
        try
		{  
        	/* Project Reports */
        super.update("CREATE TABLE worms_defects_type(defects_ID VARCHAR(100) NOT NULL, defects_Name VARCHAR(100) NOT NULL, " +
                "PRIMARY KEY(defects_ID, defects_Name))", null);
        
        super.update("CREATE TABLE worms_performance_report(reportId VARCHAR(100) NOT NULL, reportName VARCHAR(100) DEFAULT '', projectId VARCHAR(100) NOT NULL, " +
        		"reportDate datetime, dateCreated datetime, createdBy VARCHAR(100), PRIMARY KEY(reportId))", null);
        super.update("CREATE TABLE worms_performance_report_cost(reportId VARCHAR(100) NOT NULL, costId VARCHAR(100) NOT NULL, " +
        		"costName VARCHAR(100),estimated VARCHAR(100), actual VARCHAR(100), costOrder INT DEFAULT 0 NOT NULL, variance VARCHAR(100), " +
        		"PRIMARY KEY(costId))", null);
        super.update("CREATE TABLE worms_performance_report_defects(reportId VARCHAR(100) NOT NULL, defectTypeId VARCHAR(100) NOT NULL, " +
        		"defectTypeName VARCHAR(100),resolved INT DEFAULT 0, unresolved INT DEFAULT 0, defectOrder INT DEFAULT 0, total INT DEFAULT 0, " +
        		"PRIMARY KEY(defectTypeId))", null);
        super.update("CREATE TABLE worms_performance_report_milestone(reportId VARCHAR(100) NOT NULL, milestoneId VARCHAR(100) NOT NULL, " +
        		"milestoneName VARCHAR(100),estStartDate VARCHAR(100), estEndDate VARCHAR(100), actStartDate VARCHAR(100), actEndDate VARCHAR(100), " +
        		"milestoneOrder INT DEFAULT 0, variance VARCHAR(100), PRIMARY KEY(milestoneId))", null);
        super.update("CREATE TABLE worms_performance_report_project(reportId VARCHAR(100) NOT NULL, projectId VARCHAR(100) NOT NULL, " +
        		"projectName VARCHAR(100),projectValue VARCHAR(100), projectSummary VARCHAR(100) DEFAULT '', projectStartDate datetime, projectEndDate datetime, " +
        		"projectStatus FLOAT DEFAULT 0, currentHighlights TEXT DEFAULT '', clientName VARCHAR(100) DEFAULT'', PRIMARY KEY(reportId))", null);
        super.update("CREATE TABLE worms_performance_report_role(reportId VARCHAR(100) NOT NULL, roleId VARCHAR(100) NOT NULL, " +
        		"roleName VARCHAR(100),user VARCHAR(100), PRIMARY KEY(roleId))", null);
        super.update("CREATE TABLE worms_performance_report_task(reportId VARCHAR(100) NOT NULL, taskId VARCHAR(100) NOT NULL, " +
        		"taskName VARCHAR(100),estimatedMandays DOUBLE(11,1), actualMandays VARCHAR(100), variance VARCHAR(100), " +
        		"taskOrder INT DEFAULT 0, PRIMARY KEY(taskId))", null);
		}catch(Exception e){}
        try
		{
			super.update("INSERT INTO worms_defects_type (defects_ID,defects_Name) VALUES ('1','Blockers')", null);
			super.update("INSERT INTO worms_defects_type (defects_ID,defects_Name) VALUES ('2','Critical')", null);
			super.update("INSERT INTO worms_defects_type (defects_ID,defects_Name) VALUES ('3','Major')", null);
			super.update("INSERT INTO worms_defects_type (defects_ID,defects_Name) VALUES ('4','Normal')", null);
			super.update("INSERT INTO worms_defects_type (defects_ID,defects_Name) VALUES ('5','Minor')", null);
			super.update("INSERT INTO worms_defects_type (defects_ID,defects_Name) VALUES ('6','Enhancement')", null);
		}catch(Exception e){}
		
		try {
			/* Milestone Meetings */
			super.update("CREATE TABLE worms_project_members(projectId VARCHAR(35) NOT NULL, memberId VARCHAR(255) " +
					"NOT NULL, firstName VARCHAR(255) DEFAULT '', lastName VARCHAR(255) DEFAULT '')", null);
		}catch(Exception e){}
        
    }

    /* Project Dao Methods */
    public void insertProject(Project project) throws DaoException
    {
        super.update("INSERT INTO worms_project(projectId, projectName, projectDescription, projectCategory, projectValue, " +
            "ownerId, projectWorkingDays, projectSummary, archived, creationDate, modifiedDate, " +
            "projectCurrencyType,clientName) VALUES(#projectId#, #projectName#, #projectDescription#, " +
			"#projectCategory#, #projectValue#, #ownerId#, #projectWorkingDays#, #projectSummary#, " +
            "#archived#, #creationDate#, #modifiedDate#,#projectCurrencyType#,#clientName#)", project);
    }

    public void updateProject(Project project) throws DaoException
    {
        super.update("UPDATE worms_project SET projectName=#projectName#, projectDescription=#projectDescription#, " +
            "projectCategory=#projectCategory#, projectValue=#projectValue#, ownerId=#ownerId#, " +
			"projectWorkingDays=#projectWorkingDays#, projectSummary=#projectSummary#, archived=#archived#, " +
            "creationDate=#creationDate#, modifiedDate=#modifiedDate#, projectCurrencyType=#projectCurrencyType#, clientName=#clientName# " +
			"WHERE projectId=#projectId#", project);
    }
    
    public void updateProjectByCategory(Project project) throws DaoException
    {
        super.update("UPDATE worms_project SET projectName=#projectName#, projectDescription=#projectDescription#, " +
            "modifiedDate=#modifiedDate# WHERE projectId=#projectId#", project);
    }

    public void deleteProject(String projectId) throws DaoException
    {
        Collection milestones = selectMilestonesByProject(projectId, false);
        for(Iterator i = milestones.iterator(); i.hasNext();)
        {
            Milestone milestone = (Milestone) i.next();
            deleteMilestone(milestone.getMilestoneId());
        }
        Collection roles = selectRolesByProject(projectId, false);
        for(Iterator i = roles.iterator(); i.hasNext();)
        {
            Role role = (Role) i.next();
            deleteRole(role.getRoleId());
        }
        super.update("DELETE FROM worms_project WHERE projectId=?", new Object[] {projectId});
		deleteProjectFile(projectId);
    }

    public Project selectProject(String projectId) throws DaoException
    {
        Project project = null;
        Collection list = super.select("SELECT projectId, projectName, projectDescription, projectCategory, projectValue, clientName, " +
            "projectWorkingDays, projectSummary, ownerId, archived, creationDate, modifiedDate, projectCurrencyType FROM worms_project WHERE projectId=?", Project.class,
			new Object[] {projectId}, 0, -1);
        if(list.size() > 0) {
            project = (Project) list.iterator().next();
            project.setFiles(selectProjectFiles(projectId));	
            project.setMembers(selectProjectMembers(projectId));
		}
        return project;
    }

    public Collection selectProjects() throws DaoException
    {
        return super.select("SELECT projectId, projectName, projectDescription, projectCategory, projectValue, " +
            "projectWorkingDays, projectSummary, ownerId, archived, creationDate, modifiedDate, projectCurrencyType FROM worms_project ORDER BY projectName", Project.class, null, 0, -1);
    }

    public Collection selectProjects(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
    	String sql="SELECT DISTINCT worms_project.projectId, projectName, projectDescription, projectCategory, projectValue, " +
		"projectWorkingDays, projectSummary, ownerId, archived, creationDate, modifiedDate, projectCurrencyType " +
		"FROM worms_project LEFT JOIN worms_project_members ON worms_project.projectId=worms_project_members.projectId WHERE 1=1 " + query.getStatement() +
		JdbcUtil.getSort(sort, descending);
        return super.select(sql, Project.class, query.getArray(), start, maxResults);
    }
    
    public Collection selectNonProjects(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
    	Collection col=selectProjects();
    	Collection ids = new ArrayList();
    	for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
			Project project = (Project) mapIt.next();
			String projectId=project.getProjectId();
			ids.add(projectId);
		}    	
		
		DaoQuery query2 = new DaoQuery();
		if(ids.size()>0){
		query2.addProperty(new OperatorIn("id", ids.toArray(), DaoOperator.OPERATOR_NAN));
    	String sql="SELECT id, name, description, userId, general " +
		" FROM tm_category WHERE 1=1 " + query2.getStatement() +
		JdbcUtil.getSort(sort, descending);
        return super.select(sql, TaskCategory.class, query2.getArray(), start, maxResults);}
		else{
			String sql="SELECT id, name, description, userId, general " +
			" FROM tm_category WHERE 1=1 " +
			JdbcUtil.getSort(sort, descending);
	        return super.select(sql, TaskCategory.class, null, start, maxResults);	
		}
    }
    
    public Collection selectProjectsWithTimeSheetByMonth(int calendarMonth, int year) throws DaoException
    {
    	Collection projects = null;
    	
    	if(calendarMonth >=0 && calendarMonth <= 11) {
    		projects = super.select("SELECT DISTINCT worms_project.projectId, projectName, projectDescription, projectCategory, projectValue, " +
    	            "projectWorkingDays, projectSummary, ownerId, archived, creationDate, modifiedDate, projectCurrencyType " +
    	            "FROM worms_project, timesheet " +
    	            "WHERE worms_project.projectId = timesheet.projectId " +
    	            "AND adjustmentDateTime >= ? " +
    	            "AND adjustmentDateTime <= ?", 
    				Project.class, new Object[] {year + "-" + calendarMonth + "01", year + "-" + calendarMonth + "31"}, 0, -1);
    	}
    	else {
    		throw new DaoException("The month given, " + calendarMonth + ", is invalid. Please refer to the constant field value in java.util.Calendar.");
    	}
    	
    	return projects;
    }

    public int selectProjectsCount(DaoQuery query) throws DaoException
    {
        int count = 0;
        Collection list = super.select("SELECT DISTINCT COUNT(worms_project.projectId) AS intCount FROM " +
        		"worms_project LEFT JOIN worms_project_members ON worms_project.projectId=worms_project_members.projectId WHERE 1=1 " + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

    public Collection selectProjectsByOwner(String userId) throws DaoException
    {
        return super.select("SELECT DISTINCT worms_project.projectId, worms_project.projectName, worms_project.projectDescription, worms_project.projectCategory, worms_project.projectValue, " +
			"worms_project.projectWorkingDays, worms_project.projectSummary, worms_project.ownerId, worms_project.archived, worms_project.creationDate, worms_project.modifiedDate, worms_project.projectCurrencyType " +
			"FROM worms_project LEFT JOIN worms_project_members ON worms_project.projectId=worms_project_members.projectId LEFT JOIN tm_category ON worms_project.projectId=tm_category.id " +
			"LEFT JOIN tm_task ON tm_category.id=tm_task.categoryId WHERE (worms_project.ownerId=? OR tm_task.assignerId=? OR worms_project_members.memberId=?)", Project.class,
			new Object[] {userId,userId,userId}, 0, -1);
    }
    
    public Collection selectNonProjectsByOwner(String userId) throws DaoException
    {
    	Collection col=selectProjectsByOwner(userId);
    	Collection ids = new ArrayList();
    	for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
			Project project = (Project) mapIt.next();
			String projectId=project.getProjectId();
			ids.add(projectId);
		}
    	DaoQuery query2 = new DaoQuery();
    	if(ids.size()>0){
    		query2.addProperty(new OperatorEquals("tm_task.assignerId", userId, DaoOperator.OPERATOR_AND));	
    		query2.addProperty(new OperatorIn("tm_category.id", ids.toArray(), DaoOperator.OPERATOR_NAN));	
    	String sql="SELECT DISTINCT tm_category.id,tm_category.name,tm_category.description,tm_category.userId,tm_category.general  FROM tm_category LEFT JOIN tm_task ON tm_category.id=tm_task.categoryId WHERE 1=1 "+ query2.getStatement();
    	return super.select(sql, TaskCategory.class,query2.getArray(), 0, -1);
        }
    	else{
    		return super.select("SELECT DISTINCT tm_category.id,tm_category.name,tm_category.description,tm_category.userId,tm_category.general  FROM tm_category " +
    				"LEFT JOIN tm_task ON tm_category.id=tm_task.categoryId WHERE (tm_task.assignerId=?) ", TaskCategory.class,
    				new Object[] {userId}, 0, -1);
    	}
    }

    public Collection selectProjectInvolvedUsers(String projectId) throws DaoException
    {
        Collection involved = new ArrayList();
        Collection list = super.select("SELECT DISTINCT worms_role_user.userId FROM worms_role_user, " +
            "worms_role WHERE worms_role_user.roleId=worms_role.roleId AND worms_role.projectId=?",
            HashMap.class, new Object[] {projectId}, 0, -1);
        if(list.size() > 0)
        {
            Collection ids = new ArrayList();
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
                ids.add(map.get("userId"));
            }
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", ids.toArray(), DaoOperator.OPERATOR_AND));
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try
            {
                involved = service.getUsers(query, 0, -1, "firstName", false);
            }
            catch (SecurityException e)
            {
                throw new DaoException(e.getMessage(), e);
            }
        }
        return involved;
    }
    
    public Collection selectProjectUsers(String projectId) throws DaoException
    {
    	 Collection involved = new ArrayList();
    	 Collection collect = new ArrayList();
         Collection list = super.select("SELECT roleName, roleId FROM " +
             "worms_role WHERE worms_role.projectId=?",
             HashMap.class, new Object[] {projectId}, 0, -1);
    	if(list.size() > 0)
        {
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
                involved=selectRolePersonnel(map.get("roleId").toString());
                String user="";
                for(Iterator j = involved.iterator(); j.hasNext();)
                {
                	User roleuser = (User) j.next();
                	user+=roleuser.getProperty("firstName").toString()+" "+roleuser.getProperty("lastName").toString()+", ";
                }
                if(user.length()>0){
                	user=user.substring(0,user.length()-2);
                	map.put("user",user);
                	collect.add(map);
                }
                else{
                	user="-";
                	collect.remove(map);
                }
                
                
            }
            
        }     
        return collect;
    }

    public Collection selectProjectsInvolved(String userId) throws DaoException
    {
        Collection projects = new ArrayList();
        Collection list = super.select("SELECT DISTINCT worms_project.projectId FROM worms_project LEFT JOIN " +
        		"worms_role ON worms_project.projectId=worms_role.projectId " +
        		"LEFT JOIN worms_role_user ON worms_role.roleId=worms_role_user.roleId LEFT JOIN " +
        		"worms_project_members ON worms_project.projectId=worms_project_members.projectId WHERE " +
        		"(worms_role_user.userId=? OR worms_project.ownerId=? OR worms_project_members.memberId=?) " +
        		"AND worms_project.archived<>'1'", HashMap.class, new Object[] {userId,userId,userId}, 0, -1);
        if(list.size() > 0)
        {
            Collection ids = new ArrayList();
            for (Iterator i = list.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
                ids.add(map.get("projectId"));
            }
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("worms_project.projectId", ids.toArray(), DaoOperator.OPERATOR_AND));
            projects = selectProjects(query, 0, -1, "projectName", false);
        }
        return projects;
    }

    public boolean selectIsInvolved(String projectId, String userId) throws DaoException
    {
        boolean isInvolved = false;
        Collection list = super.select("SELECT worms_role.roleId FROM worms_role, worms_role_user WHERE worms_role_user.userId=? " +
            "AND worms_role.projectId=? AND worms_role_user.roleId=worms_role.roleId", HashMap.class, new Object[] {userId, projectId}, 0, 1);
        if(list.size() > 0)
            isInvolved = true;
        return isInvolved;
    }
    
    public boolean hasProjectReportInvolvement(String projectId, String userId) throws DaoException
    {
        boolean isInvolved = false;
        Collection list = super.select("SELECT worms_role.roleId FROM worms_project,worms_role, worms_role_user WHERE worms_project.projectId=worms_role.projectId AND (worms_role_user.userId=? OR worms_project.ownerId=?) " +
            "AND worms_role.projectId=? AND worms_role_user.roleId=worms_role.roleId", HashMap.class, new Object[] {userId,userId, projectId}, 0, 1);
        if(list.size() > 0)
            isInvolved = true;
        return isInvolved;
    }

    public Date selectProjectStart(String projectId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MIN(startDate) AS startDate FROM cal_event, worms_milestone_task, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.projectId=?", HashMap.class, new Object[] {projectId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("startDate") != null)
			{
				projectDate = Calendar.getInstance();
	            projectDate.setTime((Date) map.get("startDate"));
			}
        }
		//Getting Meeting Date
		String query = "SELECT MIN(startDate) AS startDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.projectId=?";
		Collection meetings = super.select(query, HashMap.class, new Object[] {projectId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("startDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("startDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() < meetingDate.getTimeInMillis())
					return projectDate.getTime();
				else
				    return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectProjectEnd(String projectId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MAX(dueDate) AS endDate FROM tm_task, worms_milestone_task, worms_milestone WHERE " +
            "tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.projectId=?", HashMap.class, new Object[] {projectId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("endDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("endDate"));
			}
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MAX(endDate) AS endDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.projectId=?", HashMap.class, new Object[] {projectId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("endDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("endDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() > meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectMilestoneEnd(String milestoneId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MAX(dueDate) AS endDate FROM tm_task, worms_milestone_task, worms_milestone WHERE " +
            "tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("endDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("endDate"));
			}
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MAX(endDate) AS endDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("endDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("endDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() > meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectMilestoneEstEnd(String milestoneId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MAX(dueDate) AS endDate FROM tm_task, worms_milestone_task, worms_milestone WHERE " +
            "tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("endDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("endDate"));
			}
        }	
        //Getting Meeting Date
		Collection meetings = super.select("SELECT MAX(endDate) AS actEndDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("actEndDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("actEndDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() > meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectMilestoneActualEnd(String milestoneId, Date date) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MAX(tm_assignee.completeDate) AS actEndDate FROM tm_assignee, tm_task, worms_milestone_task, worms_milestone WHERE " +
            "tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=? AND tm_assignee.completeDate <=? ", HashMap.class, new Object[] {milestoneId, date}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("actEndDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("actEndDate"));
			}
        }
        
        int count = 0;
        Collection milestones = super.select("SELECT COUNT(worms_milestone.milestoneId) AS intCount FROM tm_assignee, tm_task, worms_milestone_task, worms_milestone WHERE tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=? AND (tm_assignee.completeDate IS NULL OR tm_assignee.completeDate >?)",
            HashMap.class, new Object[] {milestoneId, date}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        if(count>0){        	
        	projectDate=null;
        	return null;
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MAX(endDate) AS actEndDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=? AND endDate <=?", HashMap.class, new Object[] {milestoneId, date}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("actEndDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("actEndDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() > meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    public Date selectTaskActualEnd(String taskId) throws DaoException
    {
		Calendar projectDate = null;
        Collection list = super.select("SELECT MAX(tm_assignee.completeDate) AS actEndDate FROM tm_assignee, tm_task WHERE " +
            "tm_task.id=tm_assignee.taskId AND tm_task.id=? ", HashMap.class, new Object[] {taskId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("actEndDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("actEndDate"));
			}
        }
        
        int count = 0;
        Collection milestones = super.select("SELECT COUNT(tm_task.id) AS intCount FROM tm_assignee, tm_task WHERE tm_task.id=tm_assignee.taskId AND  (tm_assignee.completeDate IS NULL) AND tm_task.id=? ",
            HashMap.class, new Object[] {taskId}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        if(count>0){        	
        	projectDate=null;
        	return null;
        }
		
		//Assining Date
		if(!(projectDate == null))
		{			
		return projectDate.getTime();
		}
        return null;
    }
    public Date selectMilestoneActualEnd(String milestoneId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MAX(tm_assignee.completeDate) AS actEndDate FROM tm_assignee, tm_task, worms_milestone_task, worms_milestone WHERE " +
            "tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=? ", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("actEndDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("actEndDate"));
			}
        }
        
        int count = 0;
        Collection milestones = super.select("SELECT COUNT(worms_milestone.milestoneId) AS intCount FROM tm_assignee, tm_task, worms_milestone_task, worms_milestone WHERE tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=? AND (tm_assignee.completeDate IS NULL)",
            HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        if(count>0){        	
        	projectDate=null;
        	return null;
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MAX(endDate) AS actEndDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("actEndDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("actEndDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() > meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectProjectActualEnd(String projectId, Date date) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MAX(tm_assignee.completeDate) AS actEndDate FROM tm_assignee, tm_task, worms_milestone_task, worms_milestone,worms_project WHERE " +
            "tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.projectId=worms_project.projectId AND worms_project.projectId=? AND tm_assignee.completeDate <=? ", HashMap.class, new Object[] {projectId, date}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("actEndDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("actEndDate"));
			}
        }
        
        int count = 0;
        Collection milestones = super.select("SELECT COUNT(worms_project.projectId) AS intCount FROM tm_assignee,worms_project, " +
        		"tm_task, worms_milestone_task, worms_milestone WHERE tm_task.id=worms_milestone_task.taskId AND " +
        		"worms_milestone_task.milestoneId=worms_milestone.milestoneId AND tm_task.id=tm_assignee.taskId AND " +
        		"worms_milestone.projectId=worms_project.projectId AND worms_project.projectId=? AND (tm_assignee.completeDate IS NULL OR tm_assignee.completeDate >?)",
            HashMap.class, new Object[] {projectId, date}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        if(count>0){
        	projectDate=null;
        	return null;
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MAX(endDate) AS actEndDate FROM cal_event, worms_milestone_meeting, worms_milestone, worms_project WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.projectId=worms_project.projectId AND worms_project.projectId=? AND endDate <=?", HashMap.class, new Object[] {projectId, date}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("actEndDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("actEndDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() > meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    
    public Date selectMilestoneEstStart(String milestoneId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MIN(startDate) AS startDate FROM cal_event,tm_task, worms_milestone_task, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_task.taskId AND tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("startDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("startDate"));
			}
        }
        //Getting Meeting Date
		Collection meetings = super.select("SELECT MIN(startDate) AS ActStartDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis()< meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}			
        return null;
    }

    public Date selectMilestoneActualStart(String milestoneId, Date date) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MIN(tm_assignee.startDate) AS ActStartDate FROM tm_assignee,cal_event,tm_task, worms_milestone_task, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_task.taskId AND tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=? AND tm_assignee.startDate<=?", HashMap.class, new Object[] {milestoneId, date}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MIN(startDate) AS ActStartDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=? AND startDate<=?", HashMap.class, new Object[] {milestoneId, date}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() < meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectMilestoneActualStart(String milestoneId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MIN(tm_assignee.startDate) AS ActStartDate FROM tm_assignee,cal_event,tm_task, worms_milestone_task, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_task.taskId AND tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MIN(startDate) AS ActStartDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() < meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectTaskActualStart(String taskId) throws DaoException
    {
		Calendar projectDate = null;
        Collection list = super.select("SELECT MIN(tm_assignee.startDate) AS ActStartDate FROM tm_assignee,cal_event,tm_task WHERE " +
            "cal_event.eventId=tm_task.id AND " +
            "tm_task.id=tm_assignee.taskId AND tm_task.id=?", HashMap.class, new Object[] {taskId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		
		//Assining Date
		if(!(projectDate == null))
		{
		return projectDate.getTime();
			
		}
        return null;
    }
    
    public Date selectMilestoneStart(String milestoneId) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MIN(tm_assignee.startDate) AS ActStartDate FROM tm_assignee,cal_event,tm_task, worms_milestone_task, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_task.taskId AND tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MIN(startDate) AS ActStartDate FROM cal_event, worms_milestone_meeting, worms_milestone WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() < meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Date selectProjectActualStart(String projectId, Date date) throws DaoException
    {
		Calendar projectDate = null;
		Calendar meetingDate = null;
        Collection list = super.select("SELECT MIN(tm_assignee.startDate) AS ActStartDate FROM tm_assignee,cal_event,tm_task, worms_milestone_task, worms_milestone,worms_project WHERE " +
            "cal_event.eventId=worms_milestone_task.taskId AND tm_task.id=worms_milestone_task.taskId AND worms_milestone_task.milestoneId=worms_milestone.milestoneId AND " +
            "tm_task.id=tm_assignee.taskId AND worms_milestone.projectId=worms_project.projectId AND worms_project.projectId=? AND tm_assignee.startDate<=?", HashMap.class, new Object[] {projectId, date}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				projectDate = Calendar.getInstance();
				projectDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Getting Meeting Date
		Collection meetings = super.select("SELECT MIN(startDate) AS ActStartDate FROM cal_event, worms_milestone_meeting, worms_milestone, worms_project WHERE " +
            "cal_event.eventId=worms_milestone_meeting.meetingId AND worms_milestone_meeting.milestoneId=worms_milestone.milestoneId AND " +
            "worms_milestone.projectId=worms_project.projectId AND worms_project.projectId=? AND startDate<=?", HashMap.class, new Object[] {projectId, date}, 0, 1);
        if(meetings.size() > 0)
        {
            HashMap map = (HashMap) meetings.iterator().next();
			if(map.get("ActStartDate") != null)
			{
				meetingDate = Calendar.getInstance();
				meetingDate.setTime((Date) map.get("ActStartDate"));
			}
        }
		//Assining Date
		if(!(projectDate == null && meetingDate == null))
		{
			if(projectDate == null && meetingDate != null)
				return meetingDate.getTime();
			else if(meetingDate == null && projectDate != null)
				return projectDate.getTime();
			else
			{
				if(projectDate.getTimeInMillis() < meetingDate.getTimeInMillis())
				    return projectDate.getTime();
				else
					return meetingDate.getTime();
			}
		}
        return null;
    }
    
    public Collection selectProjectCategories() throws DaoException
    {
        Collection categories = new ArrayList();
        Collection list = super.select("SELECT DISTINCT projectCategory FROM worms_project ORDER BY projectCategory",
            HashMap.class, null, 0, -1);
        for (Iterator i = list.iterator(); i.hasNext();)
        {
            HashMap map = (HashMap) i.next();
            categories.add(map.get("projectCategory"));
        }
        return categories;
    }

    /* Roles Dao Methods */
    public void insertRole(Role role) throws DaoException
    {
        super.update("INSERT INTO worms_role(roleId, roleName, roleDescription, projectId) " +
            "VALUES(#roleId#, #roleName#, #roleDescription#, #projectId#)", role);
        for(Iterator i = role.getCompetencies().iterator(); i.hasNext();)
        {
            Competency competency = (Competency) i.next();
            super.update("INSERT INTO worms_role_competency(roleId, competencyId) VALUES(?, ?)", new Object[] {role.getRoleId(), competency.getCompetencyId()});
        }
        for(Iterator i = role.getPersonnel().iterator(); i.hasNext();)
        {
            User user = (User) i.next();
            super.update("INSERT INTO worms_role_user(roleId, userId) VALUES(?, ?)", new Object[] {role.getRoleId(), user.getId()});
        }
    }

    public void updateRole(Role role) throws DaoException
    {
        super.update("UPDATE worms_role SET roleName=#roleName#, roleDescription=#roleDescription#, " +
            "projectId=#projectId# WHERE roleId=#roleId#", role);
        super.update("DELETE FROM worms_role_competency WHERE roleId=?", new Object[] {role.getRoleId()});
        super.update("DELETE FROM worms_role_user WHERE roleId=?", new Object[] {role.getRoleId()});
        for(Iterator i = role.getCompetencies().iterator(); i.hasNext();)
        {
            Competency competency = (Competency) i.next();
            super.update("INSERT INTO worms_role_competency(roleId, competencyId) VALUES(?, ?)", new Object[] {role.getRoleId(), competency.getCompetencyId()});
        }
        for(Iterator i = role.getPersonnel().iterator(); i.hasNext();)
        {
            User user = (User) i.next();
            super.update("INSERT INTO worms_role_user(roleId, userId) VALUES(?, ?)", new Object[] {role.getRoleId(), user.getId()});
        }
    }

    public void deleteRole(String roleId) throws DaoException
    {
        super.update("DELETE FROM worms_role WHERE roleId=?", new Object[] {roleId});
        super.update("DELETE FROM worms_role_competency WHERE roleId=?", new Object[] {roleId});
        super.update("DELETE FROM worms_role_user WHERE roleId=?", new Object[] {roleId});
    }

    public Role selectRole(String roleId) throws DaoException
    {
        Role role = null;
        Collection list = super.select("SELECT roleId, roleName, roleDescription, projectId FROM worms_role WHERE roleId=?", Role.class, new Object[] {roleId}, 0, 1);
        if(list.size() > 0)
        {
            role = (Role) list.iterator().next();
            role.setCompetencies(selectRoleCompetency(role.getRoleId()));
            role.setPersonnel(selectRolePersonnel(role.getRoleId()));
        }
        return role;
    }

    public Collection selectRoles(boolean deepRetrieval) throws DaoException
    {
        Collection list = super.select("SELECT roleId, roleName, roleDescription, projectId FROM worms_role ORDER BY roleName", Role.class, null, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                Role role = (Role) i.next();
                role.setCompetencies(selectRoleCompetency(role.getRoleId()));
                role.setPersonnel(selectRolePersonnel(role.getRoleId()));
            }
        }
        return list;
    }

    public Collection selectRoles(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws DaoException
    {
        Collection list = super.select("SELECT roleId, roleName, roleDescription, projectId FROM worms_role WHERE 1=1 " + query.getStatement() + JdbcUtil.getSort(sort, descending), Role.class, query.getArray(), start, maxResults);
        if(deepRetrieval)
        {
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                Role role = (Role) i.next();
                role.setCompetencies(selectRoleCompetency(role.getRoleId()));
                role.setPersonnel(selectRolePersonnel(role.getRoleId()));
            }
        }
        return list;
    }

    public Collection selectRolesByProject(String projectId, boolean deepRetrieval) throws DaoException
    {
        Collection list = super.select("SELECT roleId, roleName, roleDescription, projectId FROM worms_role WHERE projectId=? ORDER BY roleName", Role.class, new Object[] {projectId}, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                Role role = (Role) i.next();
                role.setCompetencies(selectRoleCompetency(role.getRoleId()));
                role.setPersonnel(selectRolePersonnel(role.getRoleId()));
            }
        }
        return list;
    }

    protected Collection selectRoleCompetency(String roleId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsCompetency = null;
        Collection competencies = new ArrayList();
        Collection competencyId = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT competencyId FROM worms_role_competency WHERE roleId = ?", new Object[] {roleId});
            rsCompetency = statement.executeQuery();
            while(rsCompetency.next())
                competencyId.add(rsCompetency.getString("competencyId"));
            if(competencyId.size() > 0)
            {
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("competencyId", competencyId.toArray(), DaoOperator.OPERATOR_AND));
                competencies = handler.getCompetencies(query, 0, -1, "competencyName", false);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
				if(rsCompetency != null)
					rsCompetency.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return competencies;
    }

    protected Collection selectRolePersonnel(String roleId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsUsers = null;
        Collection users = new ArrayList();
        Collection userId = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT userId FROM worms_role_user WHERE roleId = ?", new Object[] {roleId});
            rsUsers = statement.executeQuery();
            while(rsUsers.next())
                userId.add(rsUsers.getString("userId"));
            if(userId.size() > 0)
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", userId.toArray(), DaoOperator.OPERATOR_AND));
                users = service.getUsers(query, 0, -1, "firstName", false);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
				if(rsUsers != null)
					rsUsers.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return users;
    }

    /* Milestone Methods */
    public void insertMilestone(Milestone milestone) throws DaoException
    {
        super.update("INSERT INTO worms_milestone(milestoneId, milestoneName, milestoneDescription, milestoneProgress, " +
            "projectId, milestoneOrder) VALUES(#milestoneId#, #milestoneName#, #milestoneDescription#, #milestoneProgress#, " +
            "#projectId#, #milestoneOrder#)", milestone);
    }

    public void updateMilestone(Milestone milestone) throws DaoException
    {
        super.update("UPDATE worms_milestone SET milestoneName=#milestoneName#, milestoneDescription=#milestoneDescription#, " +
            "milestoneProgress=#milestoneProgress#, projectId=#projectId#, milestoneOrder=#milestoneOrder# WHERE " +
            "milestoneId=#milestoneId#", milestone);
    }

    public void deleteMilestone(String milestoneId) throws DaoException
    {
        Milestone milestone = selectMilestone(milestoneId);
        TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        for (Iterator i = milestone.getTasks().iterator(); i.hasNext();)
        {
            Task task = (Task) i.next();
            try
            {
                manager.deleteTask(task.getId());
            }
            catch (CalendarException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            catch (DaoException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
		deleteMilestoneDescriptors(milestoneId);
        super.update("DELETE FROM worms_milestone WHERE milestoneId=?", new Object[] {milestoneId});
        super.update("DELETE FROM worms_milestone_task WHERE milestoneId=?", new Object[] {milestoneId});
    }

    public Milestone selectMilestone(String milestoneId) throws DaoException
    {
        Milestone milestone = null;
        Collection list = super.select("SELECT milestoneId, milestoneName, milestoneDescription, milestoneProgress, projectId, " +
            "milestoneOrder FROM worms_milestone WHERE milestoneId=?", Milestone.class, new Object[] {milestoneId}, 0, 1);
        if(list.size() > 0)
        {
            milestone = (Milestone) list.iterator().next();
            milestone.setTasks(getTasks(milestone.getMilestoneId()));
			milestone.setDescriptors(getDescriptors(milestone.getMilestoneId()));
			milestone.setMeetings(getMeetings(milestone.getMilestoneId()));
        }
        return milestone;
    }

    public Collection selectMilestones(boolean deepRetrieval) throws DaoException
    {
        Collection milestones = super.select("SELECT milestoneId, milestoneName, milestoneDescription, milestoneProgress, " +
            "projectId, milestoneOrder FROM worms_milestone ORDER BY milestoneName", Milestone.class, new Object[] {}, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = milestones.iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                milestone.setTasks(getTasks(milestone.getMilestoneId()));
				milestone.setDescriptors(getDescriptors(milestone.getMilestoneId()));
				milestone.setMeetings(getMeetings(milestone.getMilestoneId()));
            }
        }
        return milestones;
    }

    public Collection selectMilestones(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws DaoException
    {
        Collection milestones = super.select("SELECT milestoneId, milestoneName, milestoneDescription, milestoneProgress, projectId, " +
            "milestoneOrder FROM worms_milestone WHERE 1=1 " + query.getStatement() + JdbcUtil.getSort(sort, descending),
            Milestone.class, query.getArray(), start, maxResults);
        if(deepRetrieval)
        {
            for(Iterator i = milestones.iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                milestone.setTasks(getTasks(milestone.getMilestoneId()));
				milestone.setDescriptors(getDescriptors(milestone.getMilestoneId()));
				milestone.setMeetings(getMeetings(milestone.getMilestoneId()));
            }
        }
        return milestones;
    }

    public int selectMilestonesCount(DaoQuery query) throws DaoException
    {
        int count = 0;
        Collection milestones = super.select("SELECT COUNT(milestoneId) as intCount FROM worms_milestone WHERE 1=1 " + query.getStatement(),
            HashMap.class, query.getArray(), 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

    public Collection selectMilestonesByProject(String projectId, boolean deepRetrieval) throws DaoException
    {
        Collection milestones = super.select("SELECT milestoneId, milestoneName, milestoneDescription, milestoneProgress, projectId, " +
            "milestoneOrder FROM worms_milestone WHERE projectId=? ORDER BY milestoneOrder", Milestone.class, new Object[] {projectId}, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = milestones.iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                milestone.setTasks(getTasks(milestone.getMilestoneId()));
				milestone.setDescriptors(getDescriptors(milestone.getMilestoneId()));
				milestone.setMeetings(getMeetings(milestone.getMilestoneId()));

            }
        }
        return milestones;
    }
    
    public Collection getMilestonesByProjectChart(Project project, boolean deepRetrieval) throws DaoException
    {
        Collection milestones = super.select("SELECT milestoneId, milestoneName, milestoneDescription, milestoneProgress, projectId, " +
            "milestoneOrder FROM worms_milestone WHERE projectId=? ORDER BY milestoneOrder", Milestone.class, new Object[] {project.getProjectId()}, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = milestones.iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                
				milestone.setDescriptors(getDescriptors(milestone.getMilestoneId()));
				milestone.setMeetings(getMeetings(milestone.getMilestoneId()));
				milestone.setEndDate(selectMilestoneEstEnd(milestone.getMilestoneId()));
				milestone.setStartDate(selectMilestoneEstStart(milestone.getMilestoneId()));			
				milestone.setActEndDate(selectMilestoneActualEnd(milestone.getMilestoneId()));
				milestone.setActStartDate(selectMilestoneActualStart(milestone.getMilestoneId()));
				milestone.setTasks(getChartTasks(project,milestone.getMilestoneId()));
				
				int dur=0;
				int actdur=0;
				int startvar=0;
				int endvar=0;
				dur=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getStartDate(), milestone.getEndDate());
				milestone.setDuration(Integer.toString(dur));

				if("Not Started".equals(milestone.getActualStartDate())&&"Not Started".equals(milestone.getActualEndDate())){
					actdur=0;
					milestone.setActualDuration("Not Started");	
					milestone.setVariance("Not Started");
					milestone.setStartVariance("Not Started");
					milestone.setEndVariance("Not Started");
				}else if(!"Not Started".equals(milestone.getActualStartDate())&&"Ongoing".equals(milestone.getActualEndDate())){
					if(milestone.getActStartDate().before(new Date())){
					actdur=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), new Date());
					}else{
						actdur=-WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), new Date());	
					}
					startvar=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getStartDate(), milestone.getActStartDate());
					endvar=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getEndDate(), new Date());				
					milestone.setStartVariance(Integer.toString(startvar-1));
					milestone.setEndVariance(Integer.toString(endvar-1));	
					milestone.setActualDuration(Integer.toString(actdur));	
					milestone.setVariance(Integer.toString(actdur-dur));
				}else if(!"Not Started".equals(milestone.getActualStartDate())&&!"Ongoing".equals(milestone.getActualEndDate())){
					if(milestone.getActStartDate().before(milestone.getActEndDate())){
					actdur=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), milestone.getActEndDate());
					}
					else{
						actdur=-WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), milestone.getActEndDate());	
					}
					startvar=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getStartDate(), milestone.getActStartDate());
					endvar=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getEndDate(), milestone.getActEndDate());				
					milestone.setStartVariance(Integer.toString(startvar-1));
					milestone.setEndVariance(Integer.toString(endvar-1));
					milestone.setActualDuration(Integer.toString(actdur));	
					milestone.setVariance(Integer.toString(actdur-dur));
				}

				
            }
        }
        return milestones;
    }
    
    public Collection selectMilestonesByProjectReport(Project project, boolean deepRetrieval, Date date) throws DaoException
    {
        Collection milestones = super.select("SELECT milestoneId, milestoneName, milestoneDescription, milestoneProgress, projectId, " +
            "milestoneOrder FROM worms_milestone WHERE projectId=? ORDER BY milestoneOrder", Milestone.class, new Object[] {project.getProjectId()}, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = milestones.iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                milestone.setTasks(getTasks(milestone.getMilestoneId()));
				milestone.setDescriptors(getDescriptors(milestone.getMilestoneId()));
				milestone.setMeetings(getMeetings(milestone.getMilestoneId()));
				milestone.setEndDate(selectMilestoneEstEnd(milestone.getMilestoneId()));
				milestone.setStartDate(selectMilestoneEstStart(milestone.getMilestoneId()));
				milestone.setActEndDate(selectMilestoneActualEnd(milestone.getMilestoneId(), date));
				milestone.setActStartDate(selectMilestoneActualStart(milestone.getMilestoneId(), date));
				int dur=0;
				int actdur=0;
				dur=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getStartDate(), milestone.getEndDate());
				milestone.setDuration(Integer.toString(dur));

				if("Not Started".equals(milestone.getActualStartDate())&&"Not Started".equals(milestone.getActualEndDate())){
					actdur=0;
					milestone.setActualDuration("Not Started");	
					milestone.setVariance("Not Started");
				}else if(!"Not Started".equals(milestone.getActualStartDate())&&"Ongoing".equals(milestone.getActualEndDate())){
					if(milestone.getActStartDate().before(new Date())){
					actdur=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), new Date());
					}else{
						actdur=-WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), new Date());	
					}
					milestone.setActualDuration("* "+Integer.toString(actdur));	
					milestone.setVariance(Integer.toString(actdur-dur));
				}else if(!"Not Started".equals(milestone.getActualStartDate())&&!"Ongoing".equals(milestone.getActualEndDate())){
					if(milestone.getActStartDate().before(milestone.getActEndDate())){
					actdur=WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), milestone.getActEndDate());
					}
					else{
						actdur=-WormsUtil.getWorkingDays(project.getProjectWorking(), milestone.getActStartDate(), milestone.getActEndDate());	
					}
					milestone.setActualDuration(Integer.toString(actdur));	
					milestone.setVariance(Integer.toString(actdur-dur));
				}
				
            }
        }
        return milestones;
    }

    public Milestone selectMilestoneByTask(String taskId) throws DaoException
    {
        Milestone milestone = null;
        Collection milestones = super.select("SELECT milestoneId FROM worms_milestone_task WHERE taskId=?", HashMap.class, new Object[] {taskId}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            milestone = selectMilestone(map.get("milestoneId").toString());
        }
        return milestone;
    }

	public Milestone selectMilestoneByMeeting(String meetingId) throws DaoException
	{
		Milestone milestone = null;
        Collection milestones = super.select("SELECT milestoneId FROM worms_milestone_meeting WHERE meetingId=?", HashMap.class, new Object[] {meetingId}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            milestone = selectMilestone(map.get("milestoneId").toString());
        }
        return milestone;
	}

    public int selectMilestonesCountByProject(String projectId) throws DaoException
    {
        int count = 0;
        Collection milestones = super.select("SELECT COUNT(milestoneId) AS intCount FROM worms_milestone WHERE projectId=?",
            HashMap.class, new Object[] {projectId}, 0, 1);
        if(milestones.size() > 0)
        {
            HashMap map = (HashMap) milestones.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

    public int selectMilestoneTotalProgress(String projectId, String exclude) throws DaoException
    {
        int total = 0;
        Collection list = super.select("SELECT SUM(milestoneProgress) AS intTotal FROM worms_milestone WHERE projectId=? AND NOT (milestoneId=?)", HashMap.class,
            new Object[] {projectId, exclude}, 0, 1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
            if(map.get("intTotal") != null)
                total = ((Number)map.get("intTotal")).intValue();
        }
        return total;
    }

    protected Collection getChartTasks(Project project, String milestoneId) throws DaoException
    {
		Collection tasks = getTasksVerifiedChart(project,milestoneId);
		//Additional check to cleanup tasks that might have been deleted outside of the module
		String deleteIds = "";
		if(tasks.size() > 0)
		{
			for (Iterator it = tasks.iterator(); it.hasNext();)
			{
				Task task = (Task) it.next();
				if(!("".equals(deleteIds)))
					deleteIds += ",";
				deleteIds += "'" + task.getEventId() + "'";
			}

			int delete = super.update("DELETE FROM worms_milestone_task WHERE milestoneId='" + milestoneId + "' AND (NOT taskId IN (" + deleteIds + "))", null);
			if(delete > 0)
				tasks = getTasksVerifiedChart(project,milestoneId);
		}
        return tasks;
    }
    
    protected Collection getTasks(String milestoneId) throws DaoException
    {
		Collection tasks = getTasksVerified(milestoneId);
		//Additional check to cleanup tasks that might have been deleted outside of the module
		String deleteIds = "";
		if(tasks.size() > 0)
		{
			for (Iterator it = tasks.iterator(); it.hasNext();)
			{
				Task task = (Task) it.next();
				if(!("".equals(deleteIds)))
					deleteIds += ",";
				deleteIds += "'" + task.getEventId() + "'";
			}

			int delete = super.update("DELETE FROM worms_milestone_task WHERE milestoneId='" + milestoneId + "' AND (NOT taskId IN (" + deleteIds + "))", null);
			if(delete > 0)
				tasks = getTasksVerified(milestoneId);
		}
        return tasks;
    }

	private Collection getTasksVerified(String milestoneId) throws DaoException
	{
		TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        Collection taskIds = super.select("SELECT taskId FROM worms_milestone_task WHERE milestoneId=? AND taskId LIKE '" + Task.class.getName() + "%'", HashMap.class, new Object[] {milestoneId}, 0, -1);
        Collection tasks = new ArrayList();
        if(taskIds.size() > 0)
        {
            Collection taskId = new ArrayList();
            for(Iterator i = taskIds.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
				taskId.add(map.get("taskId"));
			}
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorIn("tt.id", taskId.toArray(), DaoOperator.OPERATOR_AND));
			tasks = manager.getTasks(query, 0, -1, "startDate", false);
		}
        return tasks;
	}
	
	private Collection getTasksVerifiedChart(Project project, String milestoneId) throws DaoException
	{
		TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        Collection taskIds = super.select("SELECT taskId FROM worms_milestone_task WHERE milestoneId=? AND taskId LIKE '" + Task.class.getName() + "%'", HashMap.class, new Object[] {milestoneId}, 0, -1);
        Collection tasks = new ArrayList();
        if(taskIds.size() > 0)
        {
            Collection taskId = new ArrayList();
            for(Iterator i = taskIds.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
				taskId.add(map.get("taskId"));
			}
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorIn("tt.id", taskId.toArray(), DaoOperator.OPERATOR_AND));
			tasks = manager.getTasks(query, 0, -1, "startDate", false);
			for(Iterator z = tasks.iterator(); z.hasNext();)
            {
                Task task = (Task) z.next();
                task.setActEndDate(selectTaskActualEnd(task.getId()));
                task.setActStartDate(selectTaskActualStart(task.getId()));
                
                
                int dur=0;
				int actdur=0;
				int startvar=0;
				int endvar=0;
				dur=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getStartDate(), task.getDueDate());
				task.setDuration(Integer.toString(dur));

				if("Not Started".equals(task.getActualStartDate()) && "Not Started".equals(task.getActualEndDate())){
					actdur=0;
					task.setActualDuration("Not Started");	
					task.setVariance("Not Started");
					task.setStartVariance("Not Started");
					task.setEndVariance("Not Started");
				}else if(!"Not Started".equals(task.getActualStartDate()) && "Ongoing".equals(task.getActualEndDate())){
					if(task.getActStartDate().before(new Date())){
					actdur=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getActStartDate(), new Date());
					}else{
						actdur=-WormsUtil.getWorkingDays(project.getProjectWorking(), task.getActStartDate(), new Date());	
					}
					startvar=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getStartDate(), task.getActStartDate());
					endvar=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getDueDate(), new Date());				
					task.setStartVariance(Integer.toString(startvar-1));
					task.setEndVariance(Integer.toString(endvar-1));	
					task.setActualDuration(Integer.toString(actdur));	
					task.setVariance(Integer.toString(actdur-dur));
				}else if(!"Not Started".equals(task.getActualStartDate()) && !"Ongoing".equals(task.getActualEndDate())){
					if(task.getActStartDate().before(task.getActEndDate())){
					actdur=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getActStartDate(), task.getActEndDate());
					}
					else{
						actdur=-WormsUtil.getWorkingDays(project.getProjectWorking(), task.getActStartDate(), task.getActEndDate());	
					}
					startvar=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getStartDate(), task.getActStartDate());
					endvar=WormsUtil.getWorkingDays(project.getProjectWorking(), task.getDueDate(), task.getActEndDate());				
					task.setStartVariance(Integer.toString(startvar-1));
					task.setEndVariance(Integer.toString(endvar-1));
					task.setActualDuration(Integer.toString(actdur));	
					task.setVariance(Integer.toString(actdur-dur));
				}


			}
		}
        return tasks;
	}

	protected Collection getMeetings(String milestoneId) throws DaoException
	{
        Collection meetings = getMeetingsVerified(milestoneId);
		//Additional check to cleanup tasks that might have been deleted outside of the module
		String deleteIds = "";
		if(meetings.size() > 0)
		{
			for (Iterator it = meetings.iterator(); it.hasNext();)
			{
				Meeting meeting = (Meeting) it.next();
				if(!("".equals(deleteIds)))
					deleteIds += ",";
				deleteIds += "'" + meeting.getEventId() + "'";
			}

			int delete = super.update("DELETE FROM worms_milestone_meeting WHERE milestoneId='" + milestoneId + "' AND (NOT meetingId IN (" + deleteIds + "))", null);
			if(delete > 0)
				meetings = getTasksVerified(milestoneId);
		}
		return meetings;
	}

	private Collection getMeetingsVerified(String milestoneId) throws DaoException
	{
		MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
		Collection meetingIds = super.select("SELECT meetingId FROM worms_milestone_meeting WHERE milestoneId=?", HashMap.class, new Object[] {milestoneId}, 0, -1);
		Collection meetings = new ArrayList();
		if(meetingIds.size() > 0)
		{
			Collection ids = new ArrayList();
			for (Iterator i = meetingIds.iterator(); i.hasNext();)
			{
				HashMap map = (HashMap) i.next();
				ids.add(map.get("meetingId"));
			}
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorIn("eventId", ids.toArray(), DaoOperator.OPERATOR_AND));
			try
			{
				meetings = handler.getMeeting(query, 0, -1, "startDate", false, true);
			}
			catch (MeetingException e)
			{
				Log.getLog(getClass()).error("Error while retrieving meetings", e);
			}
		}
		return meetings;
	}

	protected Collection getDescriptors(String milestoneId) throws DaoException
	{
        WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
		Collection ids = super.select("SELECT taskId FROM worms_milestone_task WHERE milestoneId=? AND taskId NOT LIKE '" + Task.class.getName() + "%'", HashMap.class, new Object[] {milestoneId}, 0, -1);
		Collection descriptors = new ArrayList();
        if(ids.size() > 0)
        {
            Collection descriptorIds = new ArrayList();
            for(Iterator i = ids.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
				descriptorIds.add(map.get("taskId"));
			}
			try
			{
				DaoQuery query = new DaoQuery();
				query.addProperty(new OperatorIn("descId", descriptorIds.toArray(), DaoOperator.OPERATOR_AND));
				descriptors = handler.getDescriptors(query, 0, -1, "descStart", false);
			}
			catch (WormsException e)
			{
				Log.getLog(getClass()).error("Error while retrieving descriptors", e);
			}
		}
		return descriptors;
	}

    public void insertMilestoneTask(String milestoneId, String taskId) throws DaoException
    {
        super.update("INSERT INTO worms_milestone_task(milestoneId, taskId) VALUES(?, ?)", new Object[] {milestoneId, taskId});
    }

    public void deleteMilestoneTask(String milestoneId, String taskId) throws DaoException
    {
        super.update("DELETE FROM worms_milestone_task WHERE milestoneId=? AND taskId=?", new Object[] {milestoneId, taskId});
    }

    public void deleteAllMilestoneTasks(String milestoneId) throws DaoException
    {
        super.update("DELETE FROM worms_milestone_task WHERE milestoneId=?", new Object[] {milestoneId});
    }

	public void insertMilestoneMeeting(String milestoneId, String meetingId) throws DaoException
	{
		super.update("INSERT INTO worms_milestone_meeting(milestoneId, meetingId) VALUES(?, ?)", new Object[] {milestoneId, meetingId});
	}

	public void deleteMilestoneMeeting(String milestoneId, String meetingId) throws DaoException
	{
		super.update("DELETE FROM worms_milestone_meeting WHERE milestoneId=? AND meetingId=?", new Object[] {milestoneId, meetingId});
	}

	public void deleteAllMilestoneMeetings(String milestoneId) throws DaoException
	{
		super.update("DELETE FROM worms_milestone_meeting WHERE milestoneId=?", new Object[] {milestoneId});
	}

    /* Templates Management */
    public void insertTemplate(Template template) throws DaoException
    {
        super.update("INSERT INTO worms_template(templateId, templateName, templateDescription, templateCategory) " +
            "VALUES(#templateId#, #templateName#, #templateDescription#, #templateCategory#)", template);
    }

    public void updateTemplate(Template template) throws DaoException
    {
        super.update("UPDATE worms_template SET templateName=#templateName#, templateDescription=#templateDescription#, " +
            "templateCategory=#templateCategory# WHERE templateId=#templateId#", template);
    }

    public void deleteTemplate(String templateId) throws DaoException
    {
        Collection milestones = selectMilestonesByProject(templateId, false);
        for(Iterator i = milestones.iterator(); i.hasNext();)
        {
            Milestone milestone = (Milestone) i.next();
            deleteMilestone(milestone.getMilestoneId());
        }
        Collection roles = selectRolesByProject(templateId, false);
        for(Iterator i = roles.iterator(); i.hasNext();)
        {
            Role role = (Role) i.next();
            deleteRole(role.getRoleId());
        }
        super.update("DELETE FROM worms_template WHERE templateId=?", new Object[] {templateId});
    }

    public Template selectTemplate(String templateId) throws DaoException
    {
        Template template = null;
        Collection list = super.select("SELECT templateId, templateName, templateDescription, templateCategory FROM "
            + "worms_template WHERE templateId=?", Template.class, new Object[] {templateId}, 0, 1);
        if(list.size() > 0)
            template = (Template) list.iterator().next();
        return template;
    }

    public Collection selectTemplates() throws DaoException
    {
        return super.select("SELECT templateId, templateName, templateDescription, templateCategory FROM worms_template " +
            "ORDER BY templateName", Template.class, null, 0, -1);
    }

    public Collection selectTemplates(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT templateId, templateName, templateDescription, templateCategory FROM worms_template " +
            "WHERE 1=1 " + query.getStatement() + JdbcUtil.getSort(sort, descending), Template.class, query.getArray(), start, maxResults);
    }

    public Collection selectTemplateCategories() throws DaoException
    {
        Collection categories = new ArrayList();
        Collection list = super.select("SELECT DISTINCT templateCategory FROM worms_template ORDER BY templateCategory",
            HashMap.class, null, 0, -1);
        for (Iterator i = list.iterator(); i.hasNext();)
        {
            HashMap map = (HashMap) i.next();
            categories.add(map.get("templateCategory"));
        }
        return categories;
    }

    public int selectTemplateCount(DaoQuery query) throws DaoException
    {
        int count = 0;
        Collection list = super.select("SELECT COUNT(templateId) AS intCount FROM worms_template WHERE 1=1 " + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        if(list.size() > 0)
        {
            HashMap map = (HashMap) list.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

	/* Task Descriptor Management */
	public void insertDescriptor(TaskDescriptor descriptor) throws DaoException
	{
		super.update("INSERT INTO worms_descriptor(descId, milestoneId, descName, descDescription, descStart, descDuration) " +
			"VALUES(#descId#, #milestoneId#, #descName#, #descDescription#, #descStart#, #descDuration#)", descriptor);
	}

	public void updateDescriptor(TaskDescriptor descriptor) throws DaoException
	{
		super.update("UPDATE worms_descriptor SET descName=#descName#, descDescription=#descDescription#, " +
			"descStart=#descStart#, descDuration=#descDuration#, milestoneId=#milestoneId# WHERE " +
			"descId=#descId#", descriptor);
	}

	public void deleteDescriptor(String descId) throws DaoException
	{
		super.update("DELETE FROM worms_descriptor WHERE descId=?", new Object[] {descId});
	}

	public void deleteMilestoneDescriptors(String milestoneId) throws DaoException
	{
		super.update("DELETE FROM worms_descriptor WHERE milestoneId=?", new Object[] {milestoneId});
	}

	public TaskDescriptor selectDescriptor(String descId) throws DaoException
	{
		TaskDescriptor descriptor = null;
		Collection list = super.select("SELECT descId, milestoneId, descName, descDescription, descStart, " +
			"descDuration FROM worms_descriptor WHERE descId=?", TaskDescriptor.class, new Object[] {descId}, 0, 1);
        if(list.size() > 0)
			descriptor = (TaskDescriptor) list.iterator().next();
		return descriptor;
	}

	public Collection selectDescriptors() throws DaoException
	{
        return super.select("SELECT descId, milestoneId, descName, descDescription, descStart, descDuration " +
			"FROM worms_descriptor ORDER BY descName", TaskDescriptor.class, null, 0, -1);
	}

	public Collection selectDescriptors(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
	{
		return super.select("SELECT descId, milestoneId, descName, descDescription, descStart, descDuration " +
			"FROM worms_descriptor WHERE 1=1 " + query.getStatement() + JdbcUtil.getSort(sort, descending),
			TaskDescriptor.class, query.getArray(), start, maxResults);
	}

	public Collection selectDescriptorsByMilestone(String milestoneId) throws DaoException
	{
		return super.select("SELECT descId, milestoneId, descName, descDescription, descStart, descDuration " +
			"FROM worms_descriptor WHERE milestoneId=? ORDER BY descName", TaskDescriptor.class,
			new Object[] {milestoneId}, 0, -1);
	}

    public Collection selectDescriptorsByProject(String projectId) throws DaoException
	{
		return super.select("SELECT worms_descriptor.descId, worms_descriptor.milestoneId, worms_descriptor.descName, " +
			"worms_descriptor.descDescription, worms_descriptor.descStart, worms_descriptor.descDuration FROM " +
			"worms_descriptor, worms_milestone WHERE worms_descriptor.milestoneId=worms_milestone.milestoneId " +
			"AND worms_milestone.projectId=?", TaskDescriptor.class, new Object[] {projectId}, 0, -1);
	}

	/* Project Files */
	public void insertProjectFiles(Project project) throws DaoException
	{
        deleteProjectFile(project.getProjectId());
		if(project.getFiles().size() > 0)
		{
			for (Iterator i = project.getFiles().iterator(); i.hasNext();)
			{
				DefaultContentObject document = (DefaultContentObject) i.next();
				insertProjectFile(project.getProjectId(), document.getId());
			}
		}
	}

	public void insertProjectFile(String projectId, String contentId) throws DaoException
	{
        super.update("INSERT INTO worms_project_file(projectId, contentId) VALUES(?, ?)", new Object[] {projectId, contentId});
	}

	public void deleteProjectFile(String projectId, String contentId) throws DaoException
	{
		super.update("DELETE FROM worms_project_file WHERE projectId=? AND contentId=?", new Object[] {projectId, contentId});
	}

	public void deleteProjectFile(String projectId) throws DaoException
	{
		super.update("DELETE FROM worms_project_file WHERE projectId=?", new Object[] {projectId});
	}

	/**
	 * Retrieves all files related to a particular project
	 * @param projectId ID of the project
	 * @return A collection of document object
	 * @throws DaoException
	 */
	public Collection selectProjectFiles(String projectId) throws DaoException
	{
		Collection files = new ArrayList();
		Collection list = super.select("SELECT contentId FROM worms_project_file WHERE projectId=?", HashMap.class,
			new Object[] {projectId}, 0, -1);
		Collection ids = new ArrayList();
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			HashMap map = (HashMap) i.next();
			ids.add(map.get("contentId"));
		}
		if(ids.size() > 0)
		{
			try
			{
				ContentPublisher publisher = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
				files = publisher.viewList((String[])ids.toArray(new String[] {}), new String[] {Document.class.getName()},
					null, null, Boolean.FALSE, "name", false, 0, -1, null, null);
			}
			catch (ContentException e)
			{
				throw new DaoException("Error while retrieving files", e);
			}
		}
		return files;
	}

    /**
     * (add on for timesheet module)
     * return project by task id
     * @param taskId
     * @return project
     * @throws DaoException
     */
    public Project selectProjectByTaskId(String taskId) throws DaoException
    {
        Project project = null;
        String sql = "SELECT a.projectId, a.projectName, a.projectDescription, a.projectCategory, a.projectValue, " +
                "a.projectWorkingDays, a.projectSummary, a.ownerId, a.archived, a.creationDate, a.modifiedDate FROM " +
                "worms_project a LEFT JOIN  " +
                "worms_milestone c ON a.projectId=c.projectId LEFT JOIN "+
                "worms_milestone_task b ON c.milestoneId=b.milestoneId WHERE "+
                "b.taskId=?";
        Collection list = super.select(sql, Project.class,
			new Object[] {taskId}, 0, -1);
        if(list.size() > 0)
            project = (Project) list.iterator().next();
		project.setFiles(selectProjectFiles(taskId));
        return project;
    }

    /**
     * (add on for timesheet module)
     * return project by project name / task category name
     * @param taskId
     * @return project
     * @throws DaoException
     */
    public Project selectProjectByProjectName(String taskId) throws DaoException {
        String sql = "SELECT a.projectId, a.projectName, a.projectDescription, a.projectCategory, "+
                "a.projectValue, a.projectWorkingDays, a.projectSummary, a.ownerId, "+
                "a.archived, a.creationDate, a.modifiedDate FROM " +
                "worms_project a LEFT JOIN tm_category tc ON " +
                "tc.name=a.projectName LEFT JOIN tm_task tt ON " +
                "tt.categoryId=tc.id WHERE "+
                "tt.id=?";

        Collection col = super.select(sql,Project.class,new Object[]{taskId},0,-1);
        Project p = null;
        if (col!=null && col.size()>0) {
            p = (Project)col.iterator().next();
        }
        return p;
    }

    /**
     * (add on for timesheet module)
     * return task list that tied to a project and assigned to a user
     * @param projectId
     * @param userId
     * @return col Collection of Task
     * @throws DaoException
     */
    public Collection selectTaskList(String projectId, String userId) throws DaoException {
        String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, ce.archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification " +
                    "from tm_task tt LEFT JOIN cal_event ce ON " +
                "ce.eventId=tt.id LEFT JOIN cal_event_attendee ca ON " +
                "ca.eventId=tt.id LEFT JOIN tm_category tc ON " +
                "tt.categoryId=tc.id LEFT JOIN worms_milestone_task wmt ON " +
                "wmt.taskId=tt.id LEFT JOIN worms_milestone wm ON " +
                "wm.milestoneId=wmt.milestoneId LEFT JOIN worms_project wp ON " +
                "wm.projectId=wp.projectId WHERE " +
                    " wp.projectId=? AND ca.userId=?";
        Collection col = super.select(sql,Task.class,new String[]{projectId,userId},0,-1);
        return col;
    }

    /**
     * (add on for timesheet module)
     * return task list that attach to a project
     * @param projectId
     * @return col Collection of Task
     * @throws DaoException
     */
    public Collection selectTaskAttachToProject(String projectId, String userid) throws DaoException {
    	String sUser = (userid==null)?"":" and (tt.assignerId='"+userid+"' OR wp.ownerId='"+userid+"')";
        String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, ce.archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification " +
                    "from tm_task tt LEFT JOIN tm_category tc ON " +
                "tt.categoryId=tc.id LEFT JOIN cal_event ce ON " +
                "ce.eventId=tt.id LEFT JOIN cal_event_attendee ca ON " +
                "ca.eventId=tt.id LEFT JOIN worms_project wp ON " +
                "tc.name=wp.projectName WHERE " +
                    " wp.projectId=?"+sUser;
        Collection col = super.select(sql,Task.class,new String[]{projectId},0,-1);
        return col;
    }

    /**
     * (add on for timesheet module)
     * return task list attached to a project that assigned to a specific user
     * @param projectId of type String
     * @param userId of type String
     * @return col Collection of Task
     * @throws DaoException
     */
    public Collection selectTaskListAttachToProject(String projectId, String userId) throws DaoException {
        String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, ce.archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification " +
                    "from tm_task tt LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id LEFT JOIN cal_event ce ON " +
                    "ce.eventId=tt.id LEFT JOIN cal_event_attendee ca ON " +
                    "ca.eventId=tt.id LEFT JOIN worms_project wp ON " +
                    "tc.name=wp.projectName WHERE " +
                    " wp.projectId=? AND (ca.userId=?)";
        Collection col = super.select(sql,Task.class,new String[]{projectId,userId},0,-1);
        return col;
    }
    
    public Collection selectTaskListAttachToProjects(String projectId, String userId) throws DaoException {
    	String sUser = (userId==null)?"":" and (tt.assignerId='"+userId+"')";
        String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, ce.archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification " +
                    "from tm_task tt LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id LEFT JOIN cal_event ce ON " +
                    "ce.eventId=tt.id LEFT JOIN cal_event_attendee ca ON " +
                    "ca.eventId=tt.id LEFT JOIN worms_project wp ON " +
                    "tc.id=wp.projectId WHERE " +
                    " wp.projectId=?"+sUser;
        Collection col = super.select(sql,Task.class,new String[]{projectId},0,-1);
        return col;
    }
    public Collection selectTaskListAttachToNonProject(String projectId, String userId) throws DaoException {
    	String sUser = (userId==null)?"":" and (tt.assignerId='"+userId+"')";
        String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, ce.archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification " +
                    "from tm_task tt LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id LEFT JOIN cal_event ce ON " +
                    "ce.eventId=tt.id LEFT JOIN cal_event_attendee ca ON " +
                    "ca.eventId=tt.id WHERE " +
                    " tc.id=?"+sUser;
        Collection col = super.select(sql,Task.class,new String[]{projectId},0,-1);
        return col;
    }

    /**
     * (add on for time sheet module)
     * select project list that a user involved.
     * @param userId of type String
     * @return projects Collection of Project
     * @throws DaoException 
     */
    public Collection selectProjectsForTimeSheet(String userId, String archived) throws DaoException  {
        if (archived!=null && !archived.equals(""))
            archived = " AND worms_project.archived='"+archived+"'";
        else
            archived="";

        String sql = "SELECT DISTINCT worms_project.projectId FROM " +
                "worms_project LEFT JOIN " +
                "worms_role USING(projectId) LEFT JOIN " +
                "worms_role_user USING(roleId) JOIN " +
                "tm_category ON tm_category.name=worms_project.projectName JOIN " +
                "tm_task ON tm_task.categoryId=tm_category.id JOIN " +
                "cal_event_attendee ON cal_event_attendee.eventId=tm_task.id " +
                "WHERE (cal_event_attendee.userId=? " +
                "OR worms_role_user.userId=? " +
                "OR worms_project.ownerId=?) "+archived;//AND worms_project.archived='0' ";
        Object[] obj = new Object[] {userId,userId,userId};
        Collection col = super.select(sql,HashMap.class,obj,0,-1);
        Collection projects = new ArrayList();
        if(col.size() > 0)
        {
            Collection ids = new ArrayList();
            for (Iterator i = col.iterator(); i.hasNext();)
            {
                HashMap map = (HashMap) i.next();
                ids.add(map.get("projectId"));
            }
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("worms_project.projectId", ids.toArray(), DaoOperator.OPERATOR_AND));
            projects = selectProjects(query, 0, -1, "projectName", false);
        }

        return projects;

    }
    
    public Collection selectDefectsType() throws DaoException
    {
        return super.select("SELECT defects_ID, defects_Name FROM worms_defects_type", HashMap.class, null, 0, -1);
    }
    
    public void insertReport(Report report) throws DaoException
    {
        super.update("INSERT INTO worms_performance_report(reportId, reportName, reportDate, projectId, dateCreated, createdBy) VALUES(#reportId#,  #reportName#, #reportDate#, #projectId#, now(), #createdBy#)", report);
    }
    
    public void insertReportProject(ReportProject rp) throws DaoException
    {
        super.update("INSERT INTO worms_performance_report_project(reportId, projectId, projectName, projectValue, projectSummary, projectStartDate, projectEndDate, projectStatus,clientName,currentHighlights,actualProjectStartDate,actualProjectEndDate,startVariance,endVariance,estDuration,actDuration) " +
        		"VALUES(#reportId#, #projectId#, #projectName#, #projectValue#, #projectSummary#, #projectStartDate#, #projectEndDate#, #projectStatus#,#clientName#,#currentHighlights#,#actualProjectStartDate#,#actualProjectEndDate#,#startVariance#,#endVariance#,#estDuration#,#actDuration#)", rp);
    }
    
    public void insertReportRole(Collection roleList) throws DaoException
    {
    	for (Iterator i = roleList.iterator(); i.hasNext();)
        {                
				ReportRole role = (ReportRole) i.next();	
				super.update("INSERT INTO worms_performance_report_role(reportId, roleId, roleName, user) " +
        		"VALUES(#reportId#, #roleId#, #roleName#, #user#)", role);
        }
    }
    
    public void insertReportMilestone(Collection milestoneList) throws DaoException
    {
    	for (Iterator i = milestoneList.iterator(); i.hasNext();)
        {                
				ReportMilestone milestone = (ReportMilestone) i.next();	
				super.update("INSERT INTO worms_performance_report_milestone(reportId, milestoneId, milestoneName, estStartDate, estEndDate, actStartDate, actEndDate, milestoneOrder, variance, estVariance, actVariance) " +
        		"VALUES(#reportId#, #milestoneId#, #milestoneName#, #estStartDate#, #estEndDate#, #actStartDate#, #actEndDate#, #milestoneOrder#, #variance#, #estVariance#, #actVariance#)", milestone);
        }
    }
    
    public void insertReportTask(Collection taskList) throws DaoException
    {
    	for (Iterator i = taskList.iterator(); i.hasNext();)
        {                
				ReportTask task = (ReportTask) i.next();	
				super.update("INSERT INTO worms_performance_report_task(reportId, taskId, taskName, estimatedMandays, actualMandays, variance, taskOrder) " +
        		"VALUES(#reportId#, #taskId#, #taskName#, #estimatedMandays#, #actualMandays#, #variance#, #taskOrder#)", task);
        }
    }
    
    public void insertReportDefect(Collection defectList) throws DaoException
    {
    	for (Iterator i = defectList.iterator(); i.hasNext();)
        {                
				ReportDefects defects = (ReportDefects) i.next();	
				super.update("INSERT INTO worms_performance_report_defects(reportId, defectTypeId, defectTypeName, resolved, unresolved, defectOrder, total, severity) " +
        		"VALUES(#reportId#, #defectTypeId#, #defectTypeName#, #resolved#, #unresolved#, #defectOrder#, #total#, #severity#)", defects);
        }
    }
    
    public void insertReportCost(Collection costList) throws DaoException
    {
    	for (Iterator i = costList.iterator(); i.hasNext();)
        {                
				ReportCost cost = (ReportCost) i.next();	
				super.update("INSERT INTO worms_performance_report_cost(reportId, costId, costName, estimated, actual, costOrder, variance) " +
        		"VALUES(#reportId#, #costId#, #costName#, #estimated#, #actual#, #costOrder#, #variance#)", cost);
        }
    }
    
    public Report getReport(String reportId, boolean all) throws DaoException
    {
    	Report report = null;
        Collection list = super.select("SELECT reportId, reportName, reportDate, projectId, dateCreated, createdBy FROM worms_performance_report WHERE reportId=?", Report.class,
			new Object[] {reportId}, 0, -1);
        if(list.size() > 0){       	
        	report = (Report) list.iterator().next();
        	if(all){
        	report.setProjects(getReportProject(reportId));	
        	report.setRoles(getReportRoles(reportId));	
        	report.setMilestones(getReportMilestone(reportId));	
        	report.setTasks(getReportTask(reportId));	
        	report.setDefects(getReportDefect(reportId));	
        	report.setCost(getReportCost(reportId));}	
        }
        return report;
    }
    
    public Report getLatestReport(String projectId, boolean all) throws DaoException
    {
    	Report report = null;
        Collection list = super.select("SELECT reportId, reportName, reportDate, projectId,dateCreated, createdBy FROM worms_performance_report WHERE projectId=? ORDER BY dateCreated DESC", Report.class,
			new Object[] {projectId}, 0, 1);
        if(list.size() > 0){       	
        	report = (Report) list.iterator().next();
        	if(all){      	
        	report.setDefects(getReportDefect(report.getReportId()));	
        	report.setCost(getReportCost(report.getReportId()));}	
        }
        return report;
    }
       
    public ReportProject getReportProject(String reportId) throws DaoException {
    	ReportProject project= new ReportProject();
        String sql = "SELECT reportId, projectId, projectName, projectValue, projectSummary, estDuration, actDuration, actualProjectStartDate,actualProjectEndDate,startVariance,endVariance, projectStartDate, projectEndDate, projectStatus,clientName,currentHighlights FROM worms_performance_report_project WHERE reportId=? ";        		
        Collection col = super.select(sql,ReportProject.class,new String[]{reportId},0,1);
        if(col.size() > 0)
        	project = (ReportProject) col.iterator().next();
        return project;
    }

    public Collection getReportRoles(String reportId) throws DaoException {

        String sql = "SELECT reportId, roleId, roleName, user FROM worms_performance_report_role WHERE reportId=? " +
        		"ORDER BY roleName";
        Collection col = super.select(sql,ReportRole.class,new String[]{reportId},0,-1);
        return col;
    }
    
    public Collection getReportMilestone(String reportId) throws DaoException {
        String sql = "SELECT reportId, milestoneId, milestoneName, estStartDate, estEndDate, actStartDate, " +
        		"actEndDate, milestoneOrder, variance, estVariance, actVariance FROM worms_performance_report_milestone WHERE reportId=? " +
        		"ORDER BY milestoneOrder";
        Collection col = super.select(sql,ReportMilestone.class,new String[]{reportId},0,-1);
        return col;
    }
    
    public Collection getReportTask(String reportId) throws DaoException {

        String sql = "SELECT reportId, taskId, taskName, estimatedMandays, actualMandays, " +
        		"variance, taskOrder FROM worms_performance_report_task WHERE reportId=? ORDER BY taskOrder";
        Collection col = super.select(sql,ReportTask.class,new String[]{reportId},0,-1);
        return col;
    }
    
    public Collection getReportDefect(String reportId) throws DaoException {

        String sql = "SELECT reportId, defectTypeId, defectTypeName, resolved, unresolved, defectOrder, total, severity " +
        		"FROM worms_performance_report_defects  WHERE reportId=? ORDER BY defectOrder";
        Collection col = super.select(sql,ReportDefects.class,new String[]{reportId},0,-1);
        return col;
    }
    
    public Collection getReportCost(String reportId) throws DaoException {

        String sql = "SELECT reportId, costId, costName, estimated, actual, costOrder, variance " +
        		"FROM worms_performance_report_cost WHERE reportId=? ORDER BY costOrder";
        Collection col = super.select(sql,ReportCost.class,new String[]{reportId},0,-1);
        return col;
    }
    
    public Collection getProjectReport(String projectId,String filter, String sort,boolean desc,int startIndex,int rows) throws DaoException
    {
    	if(sort==null){
    		desc=true;
    	}if(filter==null){
    		filter="%%";
    	}else if("".equals(filter.trim())){
    		filter="%%";
    	}else
    		filter="%"+filter+"%";
    	String orderBy = (sort != null) ? sort : "dateCreated";
    	if("user".equals(sort)){
    		sort="security_user.firstName";
    	}
        if (desc)
            orderBy += " DESC";
        Collection list = super.select("SELECT reportId, projectId, reportDate, dateCreated, createdBy, security_user.firstName, " +
        		"security_user.lastName FROM worms_performance_report LEFT JOIN security_user ON worms_performance_report.createdBy=security_user.id " +
        		"WHERE projectId=? AND worms_performance_report.reportName LIKE ? ORDER BY "+ orderBy, Report.class,
			new Object[] {projectId, filter}, startIndex, rows);
        return list;
    }
    
    public void deleteReport(String reportId) throws DaoException
    {
        super.update("DELETE FROM worms_performance_report WHERE reportId=?", new Object[] {reportId});
        super.update("DELETE FROM worms_performance_report_cost WHERE reportId=?", new Object[] {reportId});
        super.update("DELETE FROM worms_performance_report_defects WHERE reportId=?", new Object[] {reportId});        
        super.update("DELETE FROM worms_performance_report_milestone WHERE reportId=?", new Object[] {reportId});
        super.update("DELETE FROM worms_performance_report_project WHERE reportId=?", new Object[] {reportId});
        super.update("DELETE FROM worms_performance_report_role WHERE reportId=?", new Object[] {reportId});      
        super.update("DELETE FROM worms_performance_report_task WHERE reportId=?", new Object[] {reportId});

    }
    
    public String getTaskProjectID(String taskId) throws DaoException {
        String sql = "SELECT worms_milestone.projectId FROM tm_task, worms_milestone_task, " +
        		"worms_milestone WHERE tm_task.id=worms_milestone_task.taskId AND " +
        		"worms_milestone_task.milestoneId=worms_milestone.milestoneId AND tm_task.id=?";
        Collection col=super.select(sql,HashMap.class,new String[]{taskId},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            String projectId = (String)map.get("projectId");
            return projectId;
        }
    return "";
    }
    
    /* Project Members */
	public void insertProjectMembers(Project project) throws DaoException
	{
		deleteProjectMembers(project.getProjectId());
		if(project.getMembers().size() > 0)
		{
			for (Iterator i = project.getMembers().iterator(); i.hasNext();)
			{
				ProjectMember pm = (ProjectMember) i.next();
				super.update("INSERT INTO worms_project_members(projectId, memberId, firstName, lastName) VALUES(?, ?, ?, ?)", new Object[] {pm.getProjectId(), pm.getMemberId(), pm.getFirstName(), pm.getLastName()});
			}
		}
	}
	
	public void deleteProjectMembers(String projectId) throws DaoException
	{
		super.update("DELETE FROM worms_project_members WHERE projectId=?", new Object[] {projectId});
	}
	
	public Collection selectProjectMembers(String projectId) throws DaoException
	{
		return super.select("SELECT projectId, memberId, firstName, lastName FROM worms_project_members WHERE projectId=?", ProjectMember.class,
			new Object[] {projectId}, 0, -1);
		
	}

}
