package com.tms.collab.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.util.JdbcUtil;


public class WormsDaoMsSql extends WormsDao{
	
	public void init(){
		try{
			super.init();
		}
		catch(Exception e){}{
			
		}
		
		try {
            super.update("ALTER TABLE worms_performance_report_defects ADD severity NUMERIC(11,2) DEFAULT 0.0",null);
        }catch(Exception e) {}
		
		try{
			super.update("CREATE TABLE worms_performance_report_role(reportId VARCHAR(100) NOT NULL, roleId VARCHAR(100) NOT NULL, " +
	        		"roleName VARCHAR(100),\"user\" VARCHAR(100), PRIMARY KEY(roleId))", null);
	        super.update("CREATE TABLE worms_performance_report_task(reportId VARCHAR(100) NOT NULL, taskId VARCHAR(100) NOT NULL, " +
	        		"taskName VARCHAR(100),estimatedMandays NUMERIC(11,1), actualMandays VARCHAR(100), variance VARCHAR(100), " +
	        		"taskOrder INT DEFAULT 0, PRIMARY KEY(taskId))", null);
		}
		catch(Exception e){}{
			
		}
	}
	
	public void insertReportRole(Collection roleList) throws DaoException
    {
    	for (Iterator i = roleList.iterator(); i.hasNext();)
        {                
				ReportRole role = (ReportRole) i.next();	
				super.update("INSERT INTO worms_performance_report_role(reportId, roleId, roleName, \"USER\") " +
        		"VALUES(#reportId#, #roleId#, #roleName#, #user#)", role);
        }
    }
	
	public void insertReport(Report report) throws DaoException
    {
        super.update("INSERT INTO worms_performance_report(reportId, reportName, reportDate, projectId, dateCreated, createdBy) VALUES(#reportId#,  #reportName#, #reportDate#, #projectId#, getDate(), #createdBy#)", report);
    }
	
	public Collection selectProjectsForTimeSheet(String userId, String archived) throws DaoException  {
        if (archived!=null && !archived.equals(""))
            archived = " AND worms_project.archived='"+archived+"'";
        else
            archived="";

        String sql = "SELECT DISTINCT worms_project.projectId FROM " +
                "worms_project LEFT JOIN " +
                "worms_role ON worms_project.projectId = worms_role.projectId LEFT JOIN " +
                "worms_role_user ON worms_role_user.roleId = worms_role.roleId JOIN " +
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
	
	public Collection selectProjects(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
    	String sql="SELECT DISTINCT worms_project.projectId, projectName, projectCategory, projectValue, " +
			"projectWorkingDays, ownerId, archived, creationDate, modifiedDate, projectCurrencyType " +
			"FROM worms_project LEFT JOIN worms_project_members ON worms_project.projectId=worms_project_members.projectId WHERE 1=1 " + query.getStatement() +
		JdbcUtil.getSort(sort, descending);
        return super.select(sql, Project.class, query.getArray(), start, maxResults);
    }
    
    public Collection selectProjectsByOwner(String userId) throws DaoException
    {
        return super.select("SELECT DISTINCT worms_project.projectId, worms_project.projectName, worms_project.projectCategory, worms_project.projectValue, " +
			"worms_project.projectWorkingDays, worms_project.ownerId, worms_project.archived, worms_project.creationDate, worms_project.modifiedDate, worms_project.projectCurrencyType " +
			"FROM worms_project LEFT JOIN worms_project_members ON worms_project.projectId=worms_project_members.projectId LEFT JOIN tm_category ON worms_project.projectId=tm_category.id " +
			"LEFT JOIN tm_task ON tm_category.id=tm_task.categoryId WHERE (worms_project.ownerId=? OR tm_task.assignerId=? OR worms_project_members.memberId=?)", Project.class,
			new Object[] {userId,userId,userId}, 0, -1);
    }
}
