package com.tms.collab.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;


public class WormsDaoDB2 extends WormsDao{
	
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
	
	public Collection selectProjectsForTimeSheet(String userId, String archived) throws DaoException  {
        if (archived!=null && !archived.equals(""))
            archived = " AND worms_project.archived='"+archived+"'";
        else
            archived="";

        String sql = "SELECT DISTINCT worms_project.projectId FROM " +
                "worms_project LEFT JOIN " +
                "worms_role ON worms_project.projectId = worms_role.projectId LEFT JOIN " +
                "worms_role_user ON worms_role.roleId = worms_role_user.roleId JOIN " +
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
            query.addProperty(new OperatorIn("projectId", ids.toArray(), DaoOperator.OPERATOR_AND));
            projects = selectProjects(query, 0, -1, "projectName", false);
        }

        return projects;

    }
    
}
