package com.tms.collab.taskmanager.model;

import kacang.model.*;
import kacang.util.JdbcUtil;
import kacang.Application;

import java.util.*;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsUtil;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;

public class TaskManagerDao extends DataSourceDao
{

    public void init()throws DaoException{
    	
    	try {
    		super.update("ALTER TABLE tm_task MODIFY estimation Double(11,2)",null);
    	}catch(Exception e) {
            //ignore
        }
    	try {
    		super.update("ALTER TABLE tm_task ADD estimationType VARCHAR(255) DEFAULT 'Mandays'",null);
    		super.update("ALTER TABLE tm_task ADD estimation Double(11,1)",null);
    	}catch(Exception e) {
            //ignore
        }
    	//Update Old Project task
    	try {
    		Collection col = super.select("SELECT tm_task.id AS tm_taskid, worms_project.projectId, worms_project.projectName, worms_project.projectDescription, worms_project.projectCategory, worms_project.projectValue, " +
            "worms_project.projectWorkingDays, worms_project.projectSummary, worms_project.ownerId, worms_project.archived, worms_project.creationDate, worms_project.modifiedDate, worms_project.projectCurrencyType " +
            "FROM tm_task INNER JOIN worms_milestone_task ON tm_task.id=worms_milestone_task.taskId INNER JOIN worms_milestone ON worms_milestone_task.milestoneId=worms_milestone.milestoneId " +
            "INNER JOIN worms_project ON worms_milestone.projectId=worms_project.projectId INNER JOIN cal_event ce ON tm_task.id=ce.eventId WHERE (tm_task.estimation IS NULL OR tm_task.estimation=0.0)", Project.class, null, 0, -1);
    		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    			Project project = (Project) mapIt.next();
    			String taskid=project.getProperty("tm_taskid").toString();
    			CalendarModule calModule = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
    			CalendarEvent event = calModule.getCalendarEvent(taskid);
    			int totalMandaysEstimated = 0;
    			if(project.getProjectWorking().size()==0){
    				totalMandaysEstimated = WormsUtil.getWorkingDays(TimeSheetUtil.getWorkingDays(), event.getStartDate(), event.getEndDate()) ;
    			}else{
                totalMandaysEstimated = WormsUtil.getWorkingDays(project.getProjectWorking(), event.getStartDate(), event.getEndDate()) ;
    			}super.update("UPDATE tm_task set estimation=? WHERE id=?", new Object[]{Integer.toString(totalMandaysEstimated),taskid});
    			}
    		}
    	catch(Exception e) {
            //ignore
        }
    	//Update Old Project task
    	try {
    		Collection col = super.select("SELECT tm_task.id AS tm_taskid " +
            "FROM tm_task INNER JOIN cal_event ce ON tm_task.id=ce.eventId WHERE (tm_task.estimation IS NULL OR tm_task.estimation=0.0)", HashMap.class, null, 0, -1);
    		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    			HashMap map = (HashMap) mapIt.next();
    			String taskid=map.get("tm_taskid").toString();
    			CalendarModule calModule = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
    			CalendarEvent event = calModule.getCalendarEvent(taskid);
    			int totalMandaysEstimated = 0;
                totalMandaysEstimated = WormsUtil.getWorkingDays(TimeSheetUtil.getWorkingDays(), event.getStartDate(), event.getEndDate()) ;
    			super.update("UPDATE tm_task set estimation=? WHERE id=?", new Object[]{Integer.toString(totalMandaysEstimated),taskid});
    			}
    		}
    	catch(Exception e) {
            //ignore
        }
        try {

            // add completedSetBy & completedDateSetBy
            super.update("ALTER TABLE tm_assignee ADD completedSetBy VARCHAR(255)",null);
            super.update("ALTER TABLE tm_assignee ADD completedDateSetOn DATETIME",null);

            // add comments
            super.update("ALTER TABLE tm_task ADD comments TEXT",null);

            // add updateBy & updateDate for tm_assignee
            super.update("ALTER TABLE tm_assignee ADD updateBy VARCHAR(255)",null);
            super.update("ALTER TABLE tm_assignee ADD updateDate DATETIME",null);

            // add priority field
            super.update("ALTER TABLE tm_task ADD taskPriority CHAR(1)",null);
            
            super.update("ALTER TABLE tm_assignee MODIFY taskStatus tinyint(3) default '0'",null);
        }
        catch(Exception e) {
            //ignore
        }

        

        Collection col = super.select("SELECT COUNT(id) as total FROM tm_category WHERE id=?",HashMap.class,new Object[]{TaskManager.DEFAULT_CATEGORY_ID},0,-1);
        boolean existed = false;
        if(col!=null&&col.size()>0){
            HashMap map = (HashMap) col.iterator().next();
            int total = 0;
            total = Integer.parseInt(map.get("total").toString());
            if(total>0)
                existed = true;
        }
        if(!existed)
        {
            try{
            super.update("INSERT INTO tm_category(id,name,description,userId,general) " +
                    "VALUES('"+TaskManager.DEFAULT_CATEGORY_ID+"','General','General Category','administrator',1)",null);
            }catch(DaoException e){
            }
        }
        
        try {
	        super.update("CREATE TABLE tm_assignee ("+
	        "userId varchar(255) default '0', "+
	                "taskId varchar(255) default '0',"+
	                "startDate datetime default '0000-00-00 00:00:00',"+
	                "completeDate datetime default '0000-00-00 00:00:00',"+
	                "progress int(3) default '0',"+
	                "taskStatus tinyint(3) unsigned default '0', " +
	                "updateBy VARCHAR(255), " +
	                "updateDate datetime," +
	                "completedSetBy varchar(255)," +
	                "completedDateSetOn datetime"+
	        ") TYPE=MyISAM;",null);
	    }
	    catch(Exception e) {
	       System.out.println("create table tm_assignee" + e.toString());
	    }

	    try {
	        super.update("CREATE TABLE tm_category (" +
	                "id varchar(255) NOT NULL default '0'," +
	                "name varchar(255) default '0'," +
	                "description varchar(255) default '0'," +
	                "userId varchar(255) default '0'," +
	                "general char(1) binary default '0'" +
	                ") TYPE=MyISAM;",null);
	    }
	    catch(Exception e) {
	       System.out.println("create table tm_category" + e.toString());
	    }
        
        try {
		    super.update("INSERT INTO tm_category(id,name,description,userId,general) " +
		            "VALUES('"+TaskManager.DEFAULT_CATEGORY_ID+"','General','General Category','administrator',1)",null);
	    }
	    catch(Exception e) {
	       System.out.println("insert table tm_category" + e.toString());
	    }
	    
	    try {
		    super.update("CREATE TABLE tm_reassign (" +
	                "reassignedBy varchar(255) default NULL," +
	                "reassignedTo varchar(255) default NULL," +
	                "taskId varchar(255) default NULL," +
	                "reassignDate datetime default NULL" +
	                ") TYPE=MyISAM;",null);
	    }
	    catch(Exception e) {
	       System.out.println("create table tm_reassign" + e.toString());
	    }
	    
	    try {
	        super.update("CREATE TABLE tm_task (id varchar(255) NOT NULL default '0'," +
	                "dueDate datetime default '0000-00-00 00:00:00'," +
	                "categoryId varchar(255) default NULL," +
	                "category varchar(255) default NULL," +
	                "assignerId varchar(255) NOT NULL default ''," +
	                "assigner varchar(255) default NULL," +
	                "reassign char(1) binary default NULL," +
	                "completed char(1) binary default NULL," +
	                "completeDate datetime default NULL," +
	                "reassigned char(1) binary default NULL,progress int(11) default '0'" +
	                ") TYPE=MyISAM;",null);
	    }
	    catch(Exception e) {
	       System.out.println("create table tm_task" + e.toString());
	    }
        
        try {
            super.update("ALTER TABLE tm_assignee MODIFY taskStatus tinyint(3) default '0'",null);
        }
        catch(Exception e) {
           System.out.println("alter table tm_assignee" + e.toString());
        }
        
    }

    public void updateTask(Task task) throws DaoException
    {
        String sql = "UPDATE tm_task set dueDate=#dueDate#," +
                "category=#category#,categoryId=#categoryId#," +/*assigner=#assigner#,assignerId=#assignerId#,*/
                "reassign=#reassign#,completed=#completed#,completeDate=#completeDate#"  +
                ",taskPriority=#taskPriority#,estimation=#estimation#,estimationType=#estimationType#"+
                " WHERE id=#id#";
            super.update(sql,task);
    }

    public void addTask(Task task) throws DaoException
    {
        String sql = "INSERT INTO tm_task(id,dueDate,category,categoryId,assigner," +
                "assignerId,reassign,completed,taskPriority,estimationType," +
                "estimation) VALUES(#id#,#dueDate#,#category#," +
                "#categoryId#,#assigner#,#assignerId#,#reassign#,#completed#," +
                "#taskPriority#,#estimationType#,#estimation#)";
            super.update(sql,task);
    }

    public void addCategory(TaskCategory category) throws DaoException
    {
            String sql="INSERT INTO tm_category(id,name,description,userId,general)" +
                "VALUES(#id#,#name#,#description#,#userId#,#general#)";
            super.update(sql,category);
    }

    public void insertReassignment(String taskId,String assignerId,String assigneeId,Date date) throws DaoException
    {
            String sql="INSERT INTO tm_reassign(taskId,reassignedBy,reassignedTo,reassignDate) " +
                    "VALUES(#taskId#,#reassignedBy#,#reassignedTo#,#reassignDate#)";
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("taskId",taskId);
        obj.setProperty("reassignedBy",assignerId);
        obj.setProperty("reassignedTo",assigneeId);
        obj.setProperty("reassignDate",date);
/*
        '"+taskId+"','"+assignerId+"','"+assigneeId+"','"+date+"'
*/
        super.update(sql,obj);
        sql = "UPDATE tm_task set reassign ='1'"+
                " WHERE id='"+taskId +"'";
        super.update(sql,null);
    }

    public void insertAssignee(Assignee assignee) throws DaoException
    {
        String sql="INSERT INTO tm_assignee(userId,taskId,startDate,completeDate,progress,taskStatus)" +
                "VALUES(#userId#,#eventId#,#startDate#,#completeDate#,#progress#,#taskStatus#) " ;
        super.update(sql,assignee);
    }

    public void updateCategory(TaskCategory category) throws DaoException
    {
            String sql="UPDATE tm_category set name=#name#,description=#description#," +
                    "userId=#userId#,general=#general# WHERE id=#id#";
            super.update(sql,category);
    }

    public void updateCategoryByProject(TaskCategory category) throws DaoException
    {
            String sql="UPDATE tm_category set name=#name#,description=#description#," +
                    "userId=#userId# WHERE id=#id#";
            super.update(sql,category);
    }

    public void updateTaskReassignment(String taskId, boolean reassigned) throws DaoException
    {
            String sql = "UPDATE tm_task set reassign=? WHERE id=?";
            super.update(sql, new Object[] { (reassigned ? "1":"0") , taskId });
    }

    public void updateTaskCategory(String taskId, String categoryId) throws DaoException
    {
            String sql = "UPDATE tm_task set categoryId=? WHERE id=?";
            super.update(sql, new Object[] { categoryId, taskId });
    }

    public Task selectTask(String taskId, String userId) throws DaoException
    {
            String sql = "SELECT DISTINCT ce.title,worms_project.projectId,ce.startDate,tt.id,dueDate,categoryId,name as category," +
                    "ce.description,assignerId,assigner,reassign,completed,ca.firstName as assigneeFirst," +
                    "ca.lastName as assigneeLast,completeDate, ca.userId as assigneeId, ce.modified, ce.new, " +
                    "ce.archived, deleted, ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate" +
                    ",tt.taskPriority, ce.reminderDate " +
                    "FROM tm_task tt LEFT JOIN cal_event ce ON " +
                    "tt.id=ce.eventId LEFT JOIN cal_event_attendee ca ON " +
                    "ca.eventId=tt.id LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId " +
                    "WHERE tt.id='" + taskId +"' AND ca.userId='" +userId+"' ";
                    //"AND tc.id=tt.categoryId AND ca.eventId = tt.id AND ce.eventId = tt.id "; //+AND
                   // " ca.userId = tt.assigneeId";
            Collection col= super.select(sql,Task.class,null,0,-1);
            if(col!=null&&col.size()>0){
                Task task = (Task)col.iterator().next();
                sql = "SELECT eventId, resourceId, name FROM cal_event_resource cr, rm_resource rr" +
                        " WHERE eventId=? AND cr.resourceId=rr.id";
                task.setResources(super.select(sql,Resource.class,new Object[]{taskId},0,-1));
                return task;
            }
        return null;

    }

    public int updateAssignee(Assignee assignee) throws DaoException
    {
        String sql = "UPDATE tm_assignee set startDate=#startDate#,progress=#progress#,completeDate=#completeDate#," +
                "taskStatus=#taskStatus# WHERE userId=#userId# AND taskId=#eventId#";
        return super.update(sql,assignee);
    }

    public int updateAssigneeStartTask(Assignee assignee) throws DaoException {
        
    	try {
	    	String sql = "UPDATE tm_assignee set startDate=#startDate#,progress=#progress#,completeDate=#completeDate#,"+
	                "taskStatus=#taskStatus#,updateBy=#updateBy#,updateDate=#updateDate# " +
	                "WHERE " +
	                "userId=#userId# AND taskId=#eventId#";
	        int i=super.update(sql,assignee);
	        return i;
    	} catch (Exception e){
    		System.out.println("updateAssigneeStartTask="+e);
    	}
        return 0;
    }

    // update assignee with update userid & date
    public int updateAssigneeTaskCompletion(Assignee assignee)throws DaoException {
        String sql = "UPDATE tm_assignee set startDate=#startDate#,progress=#progress#,completeDate=#completeDate#,"+
                "taskStatus=#taskStatus#,updateBy=#updateBy#,updateDate=#updateDate#," +
                "completedSetBy=#completedSetBy#, completedDateSetOn=#completedDateSetOn# " +
                " WHERE " +
                "userId=#userId# AND taskId=#eventId#";
        int i =  super.update(sql,assignee);
        return i;
    }

    public Assignee selectAssignee(String taskId,String userId) throws DaoException
    {
        String sql = "SELECT cea.eventId, ceis.instanceId, cea.userId, cea.attendeeId, ta.startDate,ta.completeDate," +
                "CASE WHEN ta.progress IS NULL THEN 0 ELSE ta.progress END AS progress," +
                "CASE WHEN ta.taskStatus IS NULL THEN 0 ELSE ta.taskStatus END AS taskStatus , " +
                "cea.username, cea.firstName, cea.lastName, cea.compulsory, ceis.status, ceis.comments " +
                "FROM cal_event_attendee cea " +
                "LEFT OUTER JOIN cal_event_attendee_status ceis ON cea.attendeeId = ceis.attendeeId LEFT JOIN tm_assignee ta ON ta.taskId = cea.eventId AND ta.userId = cea.userId " +
                "WHERE cea.eventId=? AND cea.userId=? ";
        Collection col = super.select(sql, Assignee.class, new Object[]{taskId,userId}, 0, -1);
        if(col!=null&&col.size()>0)
            return (Assignee)col.iterator().next();
        return null;
    }

    public Collection selectAssignees(String taskId) throws DaoException
    {
        String sql = "SELECT cea.eventId, ceis.instanceId, cea.userId, cea.attendeeId, ta.startDate,ta.completeDate," +
                "CASE WHEN ta.progress IS NULL THEN 0 ELSE ta.progress  END AS progress," +
                "CASE WHEN ta.taskStatus IS NULL THEN 0 ELSE ta.taskStatus  END AS taskStatus ,  " +
                "cea.username, cea.firstName, cea.lastName, cea.compulsory, ceis.status, ceis.comments " +
                "FROM cal_event_attendee cea " +
                "LEFT OUTER JOIN cal_event_attendee_status ceis ON cea.attendeeId = ceis.attendeeId LEFT JOIN tm_assignee ta ON ta.taskId = cea.eventId AND ta.userId = cea.userId " +
                "WHERE cea.eventId=? " +
                "ORDER BY cea.firstName ";
        return super.select(sql, Assignee.class, new Object[]{taskId}, 0, -1);
    }

	public Collection selectTaskAssignees(String taskId) throws DaoException {
		String sql = "SELECT userId, taskId AS eventId FROM tm_assignee WHERE taskId = ?";
		return super.select(sql, Assignee.class, new Object[] {taskId}, 0, -1);
	}

    public Collection selectTasks(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        String sql = "SELECT DISTINCT ce.title,tt.id,worms_project.projectId,worms_project.archived AS projectArchived,dueDate,categoryId,name as category,ce.reminderDate,ce.eventId, ce.description," +
            "ce.startDate,assignerId,assigner,reassign,completed,completeDate, ce.modified, ce.new, ce.archived, deleted,ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate" +
                ",taskPriority " +
            "from tm_task tt INNER JOIN cal_event ce ON (ce.eventId = tt.id) LEFT JOIN tm_category tc ON tc.id=tt.categoryId " +
            "LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId WHERE 1=1 " +
            query.getStatement() + JdbcUtil.getSort(sort, descending);
        Collection tasks = super.select(sql, Task.class, query.getArray(), start, maxResults);
        ResourceManager manager = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
        for (Iterator i = tasks.iterator(); i.hasNext();)
        {
            Task task = (Task) i.next();
            task.setAttendees(selectAssignees(task.getId()));
            task.setResources(manager.getBookedResources(task.getId(), CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection selectTasksReport(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        String sql = "SELECT ce.title,tt.id,dueDate,categoryId,name as category,ce.reminderDate,ce.eventId, ce.description," +
            "ce.startDate,assignerId,assigner,reassign,completed,completeDate, modified, new, archived, deleted,ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate" +
            ",taskPriority from tm_task tt INNER JOIN cal_event ce ON (ce.eventId = tt.id) LEFT JOIN tm_category tc " +
            "ON tc.id=tt.categoryId LEFT JOIN timesheet ON tt.id=timesheet.taskId WHERE 1=1 " +
            query.getStatement() + JdbcUtil.getSort(sort, descending);
        Collection tasks = super.select(sql, Task.class, query.getArray(), start, maxResults);
        ResourceManager manager = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
        for (Iterator i = tasks.iterator(); i.hasNext();)
        {
            Task task = (Task) i.next();
            task.setAttendees(selectAssignees(task.getId()));
            task.setResources(manager.getBookedResources(task.getId(), CalendarModule.DEFAULT_INSTANCE_ID));
            Collection assigneeList = task.getAttendees();
			String[][] userList = new String[assigneeList.size()][2];
			int totalMandaysEstimated = 0;
			double totalMandaysSpent = 0;
			double totalHoursSpent = 0;

			// Calculate total estimated manday for this task
			CalendarModule calModule = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
/*			CalendarEvent event = calModule.getCalendarEvent(task.getId());
            Calendar startdate = Calendar.getInstance();
            Calendar enddate = Calendar.getInstance();*/
/*
            if (project!=null) {
            	totalMandaysEstimated = WormsUtil.getWorkingDays(project.getProjectWorking(), startdate.getTime(), enddate.getTime()) ;
            } else {
            	totalMandaysEstimated = WormsUtil.getWorkingDays(TimeSheetUtil.getWorkingDays(), startdate.getTime(), enddate.getTime()) ;
            }*/

            int j=0;
            // Calculate total hour spent by each user on this task
			for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
				Assignee assignee = (Assignee)itr.next();

/*
				double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, task[iCounter].getId());
				totalHourSpentThisUser += mod.getTotalHour(assignee.getUserId(), null, task[iCounter].getId());*/


/*				totalHoursSpent += totalHourSpentThisUser;*/

			}
        }
        return tasks;
    }

    public Task selectTask(String taskId) throws DaoException, DataObjectNotFoundException {
            String sql = "SELECT DISTINCT ce.title,worms_project.projectId,worms_project.archived AS projectArchived,tt.id,dueDate,tt.categoryId,tc.name as category,ce.creationDate," +
                    "ce.reminderDate,ce.eventId,ce.description,ce.startDate,assignerId,assigner,reassign,completed,completeDate,ca.firstName as assigneeFirst," +
                    "ca.lastName as assigneeLast, ce.userId, modified, new, ce.archived, ce.deleted, ce.lastModified, ce.lastModifiedBy,ce.classification" +
                    ",tt.taskPriority,tt.estimationType,tt.estimation " +
                    "from tm_task tt LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId LEFT JOIN cal_event ce ON (ce.eventId = tt.id) LEFT JOIN cal_event_attendee ca ON (ca.eventId = tt.id) LEFT JOIN tm_category tc ON tc.id=tt.categoryId " +
                    "WHERE tt.id='" + taskId +"'";// AND" +
                   // " ca.userId = tt.assigneeId";
            Collection col= super.select(sql,Task.class,null,0,-1);
            if(col!=null&&col.size()>0){
                Task task = (Task)col.iterator().next();
                sql = "SELECT eventId, resourceId as id, name FROM cal_event_resource cr, rm_resource rr" +
                        " WHERE eventId=? AND cr.resourceId=rr.id";
                task.setResources(super.select(sql,Resource.class,new Object[]{taskId},0,-1));
                return task;
            }else {
                throw new DataObjectNotFoundException("task not found");
            }        
    }

    public Collection selectTasks(Date date, String userId,int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
            Date b4, after;
            Calendar b4Cal = Calendar.getInstance();
            Calendar afterCal = Calendar.getInstance();
            b4Cal.setTime(date);
            afterCal.setTime(date);
            b4Cal.set(Calendar.HOUR_OF_DAY,23);
            b4Cal.set(Calendar.MINUTE,59);
            b4Cal.set(Calendar.SECOND,59);
            b4 = b4Cal.getTime();
            afterCal.set(Calendar.HOUR_OF_DAY,0);
            afterCal.set(Calendar.MINUTE,0);
            afterCal.set(Calendar.SECOND,0);
            after = afterCal.getTime();
            String sql = "SELECT DISTINCT ce.title,ce.startDate,worms_project.projectId,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, ce.modified, ce.new, ce.archived, ce.deleted, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate" +
                    ",tt.taskPriority " +
                    "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON(ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +
                    " LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId WHERE " +
                    "(( tt.assignerId = ? AND ca.userId <> ? ) OR" +
                    "(ca.userId = ?) OR (tr.reassignedBy = ?)) AND (dueDate >=? AND dueDate<= ?) "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    " AND 1=1 "+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            Object [] args = {userId, userId,userId,userId,after,b4};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public Collection selectCalendarTasks(Date from,Date to,String[] userIds,boolean onlyIncomplete/*, boolean onlyReminder*/,int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
            String userClause = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " (ca.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )OR (tr.reassignedBy IN( "+ quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )AND tr.taskId=tt.id) OR tt.assignerId IN(" +quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause+= "))";
            }

            String range = "((dueDate>=? AND dueDate<= ?)";
            if(onlyIncomplete){
                range += "OR (dueDate<?))";
            } else{
                range += ")" ;
            }

        String sql = "SELECT DISTINCT  ce.title,ce.startDate,tt.id,title,ce.eventId," +
                "ce.userId,endDate,dueDate,ce.reminderDate,categoryId,name as category," +
                "ce.description,assigner,assignerId,reassign,completed, ce.modified, new, " +
                "ce.archived, deleted,taskPriority,worms_project.projectId, worms_project.archived AS projectArchived, " +
               /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate " +
                "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON (ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +
                "LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id  WHERE " +
                "("+range +"OR completed='0')" /*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                "AND "+ userClause + (onlyIncomplete?" AND completed='0'":"")+
                (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            Object [] args = null ;
            if(onlyIncomplete)
                args =new Object[] {from,to,to};
            else
                args =new Object[]{from,to};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public Collection selectCalendarTasks(String search, Date from,Date to,String[] userIds,boolean onlyIncomplete/*, boolean onlyReminder*/,int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
        String userClause = "";
        if (userIds != null && userIds.length > 0) {
            userClause = " (ca.userId IN (" + quote(userIds[0]);
            for (int i=1; i<userIds.length; i++) {
                userClause += ", " + quote(userIds[i]);
            }
            userClause += " )OR (tr.reassignedBy IN( "+ quote(userIds[0]);
            for (int i=1; i<userIds.length; i++) {
                userClause += ", " + quote(userIds[i]);
            }
            userClause += " )AND tr.taskId=tt.id) OR tt.assignerId IN(" +quote(userIds[0]);
            for (int i=1; i<userIds.length; i++) {
                userClause += ", " + quote(userIds[i]);
            }
            userClause+= "))";
        }

        String range = "((dueDate>=? AND dueDate<= ?)";
        if(onlyIncomplete){
            range += "OR (dueDate<?))";
        } else{
            range += ")" ;
        }

        String sql = "SELECT DISTINCT  ce.title,ce.startDate,tt.id,worms_project.projectId,title,ce.eventId,ce.userId," +
                "endDate,dueDate,ce.reminderDate,categoryId,name as category,ce.description," +
                "assigner,assignerId,reassign,tt.completed, ce.modified, ce.new, ce.archived, deleted," +
                /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate " +
                ",taskPriority " +
                "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON (ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +
                " LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId  WHERE " +
                "("+range +"OR completed='0')" /*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                "AND "+ userClause + (onlyIncomplete?" AND completed='0'":"")+ " AND (title LIKE ? OR ce.description LIKE ?) "+
                (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
        Object [] args = null ;
        String strSearch = "%"+(search==null?"":search)+"%";
        if(onlyIncomplete)
            args =new Object[] {from,to,to,strSearch,strSearch};
        else
            args =new Object[]{from,to,strSearch,strSearch};
        return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public int countCalendarTasks(Date from,String[] userIds,boolean onlyIncomplete, int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
        String userClause = "";
        if (userIds != null && userIds.length > 0) {
            userClause = " (ca.userId IN (" + quote(userIds[0]);
            for (int i=1; i<userIds.length; i++) {
                userClause += ", " + quote(userIds[i]);
            }
            userClause += " ) OR (tr.reassignedBy IN( "+ quote(userIds[0]);
            for (int i=1; i<userIds.length; i++) {
                userClause += ", " + quote(userIds[i]);
            }
            userClause += " )) OR tt.assignerId IN(" +quote(userIds[0]);
            for (int i=1; i<userIds.length; i++) {
                userClause += ", " + quote(userIds[i]);
            }
            userClause+= "))";
        }
        String range = "((dueDate>=? )OR (completed='0'))";
        String sql = "SELECT COUNT(DISTINCT ce.eventId) as total " +
                "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON (ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +/**/
                "LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id WHERE " +
                range + " AND 1=1 "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                "AND "+ userClause + (onlyIncomplete?" AND completed='0'":"")+
                (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
        Object [] args = null ;
        args =new Object[] {from};
        return Integer.parseInt(((HashMap)super.select(sql,HashMap.class,args,sIndex,maxRow).iterator().next()).get("total").toString());
    }

    public Collection selectCalendarTasks(Date from,String[] userIds,boolean onlyIncomplete, int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
            String userClause = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " (ca.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " ) OR (tr.reassignedBy IN( "+ quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )) OR tt.assignerId IN(" +quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause+= "))";
            }

            String range = "((dueDate>=? )OR (completed='0'))";

            String sql = "SELECT DISTINCT ce.title,ce.startDate,worms_project.projectId,worms_project.archived AS projectArchived,tt.id,title,ce.eventId,endDate," +
                    "dueDate,categoryId,name as category,ce.description,assigner,assignerId," +
                    "reassign,completed, ce.modified, new, ce.archived, deleted,taskPriority," +
                    /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate " +
                    "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON (ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +/**/
                    "LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId WHERE " +
                    range + " AND 1=1 "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    "AND "+ userClause + (onlyIncomplete?" AND completed='0'":"")+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            Object [] args = null ;
         //   if(onlyIncomplete)
                args =new Object[] {from};
         /*   else
                args =new Object[]{from};*/
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public Collection selectOverdueTasks(Date from,String[] userIds, int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
            String userClause = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " (ca.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )) ";
            }

            String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,title,ce.eventId,endDate," +
                    "dueDate,categoryId,name as category,ce.description,assigner,reassign," +
                    "completed, modified, new, archived, deleted,taskPriority," +
                    /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate " +
                    "from tm_task tt inner join tm_category tc on tt.categoryId=tc.id "+
                    " inner join cal_event ce on ce.eventId=tt.id "+
                    " inner join cal_event_attendee ca on ca.eventId=ce.eventId WHERE " +
                    "dueDate<? AND ca.eventId = tt.id AND ce.eventId = tt.id "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    "AND tc.id=tt.categoryId AND "+ userClause + " AND completed='0'"+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            Object [] args = null ;
                args =new Object[] {from};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public Collection selectTasks(String filter,String userId,String categoryId,boolean completed, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
            String nameFilter = " AND (ce.description LIKE ? OR ce.title LIKE ?) ";
            String sql = "SELECT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category," +
                    "ce.description,assigner,reassign,completed, modified, new, archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority  " +
                    "from tm_task tt LEFT JOIN cal_event ce ON " +
                    "tt.id=ce.eventId LEFT JOIN cal_event_attendee ca ON " +
                    "tt.id=ca.eventId LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id WHERE " +
                    "(( tt.assignerId = ? AND ca.userId <> ? ) OR" +/* AND tt.id = ca.eventId AND ce.eventId = tt.id*/
                    "(ca.userId = ?)) "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    (categoryId==null?"":" AND tt.categoryId='"+categoryId+"'") + " "+ nameFilter +
                    (completed? " AND tt.completed='1'" : " AND tt.completed='0'") + (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            filter = (filter != null) ? "%" + filter + "%" : "%";
        Object [] args = {userId, userId,userId,filter,filter};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public Collection selectPublicTasks(String filter,String userId,String categoryId,boolean completed, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
    	Collection col=selectTasks(filter,userId,categoryId,completed,sIndex,maxRow,sort,desc);
    	String s="";

        if (col!=null && col.size()>0){
            for (Iterator i=col.iterator();i.hasNext();) {
            	Task task = (Task)i.next();
                String taskId = (String)task.getId();
                if (taskId!=null && !taskId.equals("")) {
                    s += "AND tt.id<>'"+taskId+"' ";
                }
            }
        }
            String nameFilter = " AND (ce.description LIKE ? OR ce.title LIKE ?) ";
            String sql = "SELECT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category," +
                    "ce.description,assigner,reassign,completed, modified, new, archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority  " +
                    "from tm_task tt LEFT JOIN cal_event ce ON " +
                    "tt.id=ce.eventId LEFT JOIN cal_event_attendee ca ON " +
                    "tt.id=ca.eventId LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id WHERE " +
                    "tt.assignerId <> ? AND ca.userId <> ?  AND tc.general='1' "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    (categoryId==null?"":" AND tt.categoryId='"+categoryId+"'") + " "+ nameFilter +
                    (completed? " AND tt.completed='1'" : " AND tt.completed='0'") +s+ (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            filter = (filter != null) ? "%" + filter + "%" : "%";
        Object [] args = {userId, userId,filter,filter};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }

    public Collection selectTasks(String filter,String userId,String categoryId, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
            String nameFilter = " AND (ce.description LIKE ? OR ce.title LIKE ?)";

            String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,tt.completed, ce.modified, new, ce.archived, deleted," +
                    "ca.firstName as assigneeFirst,worms_project.projectId,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority " +
                    "FROM tm_task tt INNER JOIN "+
                    "tm_category tc ON tt.categoryId=tc.id INNER JOIN "+
                    "cal_event ce ON ce.eventId=tt.id INNER JOIN "+
                    "cal_event_attendee ca ON ca.eventId=ce.eventId LEFT JOIN worms_project ON tt.categoryId=worms_project.projectId WHERE " +
                    "(( tt.assignerId = ? AND ca.userId <> ? ) OR" +/* AND tt.id = ca.eventId AND ce.eventId = tt.id*/
                    "(ca.userId = ?))AND ca.eventId = tt.id AND ce.eventId = tt.id "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    (categoryId==null?"":" AND tt.categoryId='"+categoryId+"'") + " AND tc.id=tt.categoryId "+ nameFilter+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            filter = (filter != null) ? "%" + filter + "%" : "%";
            Object [] args = {userId, userId,userId,filter,filter};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }
    public Collection getAllPublicTasks(String filter,String userId,String categoryId, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {

    		Collection col=selectTasks(filter, userId,categoryId,sIndex,maxRow,sort,desc);
    		String s="";

            if (col!=null && col.size()>0){
                for (Iterator i=col.iterator();i.hasNext();) {
                	Task task = (Task)i.next();
                    String taskId = (String)task.getId();
                    if (taskId!=null && !taskId.equals("")) {
                        s += "AND tt.id<>'"+taskId+"' ";
                    }
                }
            }
            String nameFilter = " AND (ce.description LIKE ? OR ce.title LIKE ?) ";

            String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority " +
                    "FROM tm_task tt INNER JOIN "+
                    "tm_category tc ON tt.categoryId=tc.id INNER JOIN "+
                    "cal_event ce ON ce.eventId=tt.id INNER JOIN "+
                    "cal_event_attendee ca ON ca.eventId=ce.eventId WHERE " +
                    "tt.assignerId <> ? AND ca.userId <> ?  AND tc.general='1' " +
                    "AND ca.eventId = tt.id AND ce.eventId = tt.id "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    (categoryId==null?"":" AND tt.categoryId='"+categoryId+"'") +s+ " AND tc.id=tt.categoryId "+ nameFilter+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            filter = (filter != null) ? "%" + filter + "%" : "%";
            Object [] args = {userId, userId,filter,filter};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }



    public Collection selectReassignments(String taskId) throws DaoException
    {
            String sql = "SELECT DISTINCT reassignedBy as assignerId,reassignedTo as assigneeId, reassignDate as reassignmentDate " +
                    "FROM tm_reassign WHERE taskId=? ORDER BY reassignDate";
            return super.select(sql,Reassignment.class,new String[]{taskId},0,-1);
    }

    public Collection selectCategories(String userId) throws DaoException
    {
        String sql = "SELECT DISTINCT tc.id,name,tc.description,tc.userId,general FROM "+
                "tm_category tc inner join " +
                "tm_task tt on tc.id=tt.categoryId inner join "+
                "cal_event_attendee ca on ca.eventId=tt.id " +
                "WHERE ((ca.userId = ? AND ca.eventId = tt.id AND tt.categoryId=tc.id) " +
                "OR (tt.assignerId=? AND tt.categoryId=tc.id))";
        Object []args ={userId,userId};
            return super.select(sql,TaskCategory.class,args,0,-1);
    }

    public Collection selectTMCategories(String userId) throws DaoException
    {
            String sql = "select DISTINCT tm_category.id,tm_category.name FROM tm_category LEFT JOIN worms_project ON tm_category.name=worms_project.projectName " +
            		"LEFT JOIN tm_task ON tm_category.id=tm_task.categoryId WHERE (worms_project.ownerId=? OR tm_task.assignerId=?)";
            return super.select(sql,TaskCategory.class,new String[]{userId, userId},0,-1);
    }

    public Collection selectCategories() throws DaoException
    {
            String sql = "select id,name,description,userId,general FROM tm_category";
            return super.select(sql,TaskCategory.class,null,0,-1);
    }

    public Collection selectAllCategories() throws DaoException
    {
            String sql = "select worms_project.archived,worms_project.projectId,tm_category.id,tm_category.name,tm_category.description,tm_category.userId," +
            		" tm_category.general FROM tm_category LEFT JOIN worms_project ON tm_category.id=worms_project.projectId ORDER BY tm_category.name";
            return super.select(sql,TaskCategory.class,null,0,-1);
    }

    public Collection selectCategory(String categoryId) throws DaoException
    {
            String sql = "select id,name,description,userId,general FROM tm_category" +
                    " WHERE id='"+categoryId+"'";
            return super.select(sql,TaskCategory.class,null,0,-1);
    }

    public Collection selectUserCategory(String categoryId, String userId) throws DaoException
    {
            String sql = "select id,name,description,userId,general FROM tm_category" +
                    " WHERE id='"+categoryId+"' AND userId='"+userId+"'";
            return super.select(sql,TaskCategory.class,null,0,-1);
    }

    public Collection selectCategoryByTask(String taskId) throws DaoException
    {
            String sql = "select tm_category.id,tm_category.name,tm_category.description,tm_category.userId,tm_category.general FROM tm_category" +
                    " INNER JOIN tm_task ON tm_category.id=tm_task.categoryId WHERE tm_task.id='"+taskId+"'";
            return super.select(sql,TaskCategory.class,null,0,-1);
    }

    public Collection selectCategories(String userId, boolean completed) throws DaoException
    {
        String completeClause = " AND tt.completed=";
        if(completed){
            completeClause += "'1'";
        }else
            completeClause += "'0'";
        String sql = "SELECT DISTINCT tc.id,name,tc.description,tc.userId,general FROM tm_category tc," +
                "tm_task tt ,cal_event_attendee ca "+
                "WHERE ((ca.userId = ? AND  tt.categoryId=tc.id AND ca.eventId = tt.id ) " +/* &&*/
                "OR (tt.assignerId=? AND tt.categoryId=tc.id))" + completeClause;
        Object []args ={userId,userId};
            return super.select(sql,TaskCategory.class,args,0,-1);
    }

	public Collection selectCategories(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
	{
		return super.select("SELECT id, name, description, userId, general FROM tm_category WHERE 1=1 " +
			query.getStatement() + JdbcUtil.getSort(sort, descending), TaskCategory.class, query.getArray(),
			start, maxResults);
	}

    public int countCategory(String userId, boolean includeGeneral) throws DaoException
    {
        String sql = "SELECT COUNT(id) as total FROM tm_category WHERE userId=? "+
                (includeGeneral?" OR general='1'":"");
        Map rows =  (Map) super.select(sql,HashMap.class,new Object[]{userId},0,-1).iterator().next();
        return Integer.parseInt(rows.get("total").toString());
    }

    public int countProjectCategory(String userId, boolean includeGeneral) throws DaoException
    {
        String sql = "SELECT COUNT(id) as total FROM tm_category INNER JOIN " +
        		"worms_project ON tm_category.id=worms_project.projectId WHERE userId=? "+
                (includeGeneral?" OR general='1'":"");
        Map rows =  (Map) super.select(sql,HashMap.class,new Object[]{userId},0,-1).iterator().next();
        return Integer.parseInt(rows.get("total").toString());
    }
    public int countNonProjectCategory(String userId, boolean includeGeneral) throws DaoException
    {
    	Collection col=selectAllUserProjectCategories();
    	String id="";
    	for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
			TaskCategory project = (TaskCategory) mapIt.next();
			String projectId=project.getId();
			id+="AND id!='"+projectId+"' ";
		}
        String sql = "SELECT COUNT(id) as total FROM tm_category WHERE (userId=? "+
                (includeGeneral?" OR general='1'":"")+")"+id;
        Map rows =  (Map) super.select(sql,HashMap.class,new Object[]{userId},0,-1).iterator().next();
        return Integer.parseInt(rows.get("total").toString());
    }
    public Collection selectUserCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
        String sql = "SELECT id,name,description,userId,general FROM tm_category " +
                "WHERE userId='"+userId+"'" +
                (includeGeneral?" OR general='1' ":"") +
                (sort==null?"": " ORDER BY "+sort+(desc?" DESC":"" ) + ", general");
        return super.select(sql,TaskCategory.class,null,startIndex,rows);
    }
    public Collection selectUserProjectCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
        String sql = "SELECT id,name,description,userId,general FROM tm_category INNER JOIN " +
        		"worms_project ON tm_category.id=worms_project.projectId " +
                "WHERE (userId='"+userId+"'" +
                (includeGeneral?" OR general='1' ":"") +")"+
                (sort==null?"": " ORDER BY "+sort+(desc?" DESC":"" ) + ", general");
        return super.select(sql,TaskCategory.class,null,startIndex,rows);
    }

    public Collection selectAllUserProjectCategories() throws DaoException
    {
        String sql = "SELECT id,name,description,userId,general FROM tm_category INNER JOIN " +
        		"worms_project ON tm_category.id=worms_project.projectId ";
        return super.select(sql,TaskCategory.class,null,0,-1);
    }

    public Collection selectUserNonProjectCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
    	Collection col=selectAllUserProjectCategories();
    	String id="";
    	for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
			TaskCategory project = (TaskCategory) mapIt.next();
			String projectId=project.getId();
			id+="AND id!='"+projectId+"' ";
		}
        String sql = "SELECT id,name,description,userId,general FROM tm_category WHERE (userId='"+userId+"'" +
                (includeGeneral?" OR general='1' ":"") +")"+id+
                (sort==null?"": " ORDER BY "+sort+(desc?" DESC":"" ) + ", general");
        return super.select(sql,TaskCategory.class,null,startIndex,rows);
    }

    public Collection selectUserTaskCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
        String sql = "SELECT worms_project.archived,worms_project.projectId,tm_category.id,tm_category.name,tm_category.description,tm_category.userId,tm_category.general FROM tm_category LEFT JOIN worms_project ON tm_category.id=worms_project.projectId" +
                " WHERE tm_category.userId='"+userId+"'" +
                (includeGeneral?" OR tm_category.general='1' ":"") +
                (sort==null?"": " ORDER BY "+sort+(desc?" ASC":"" ) + ", general");
        return super.select(sql,TaskCategory.class,null,startIndex,rows);
    }

    /**
     * Get user categories which matches "name"
     * @param userId
     * @param name
     * @param includeGeneral
     * @param sort
     * @param desc
     * @param startIndex
     * @param rows
     * @return
     * @throws DaoException
     */
    public Collection selectUserCategories(String userId,String name, boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {

        String sql = "SELECT id,name,description,userId,general FROM tm_category " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") + " AND name LIKE ?" + JdbcUtil.getSort(sort, desc);

        return super.select(sql,TaskCategory.class,new Object[]{userId, '%' + name + '%'},startIndex,rows);
    }

    public int countUserCategories(String userId, String name, boolean includeGeneral, int startIndex, int rows) throws DaoException {
        String sql = "SELECT COUNT(id) catNum FROM tm_category " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") + " AND name LIKE ?";

        Collection list = super.select(sql,HashMap.class,new Object[]{userId, '%' + name + '%'},startIndex,rows);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            int i = ((Number)map.get("catNum")).intValue();
            return i;
        }
        return 0;
    }

    public Collection selectUserProjectCategories(String userId,String name, boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {

        String sql = "SELECT id,name,description,userId,general FROM tm_category INNER JOIN worms_project ON tm_category.id=worms_project.projectId " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") + " AND name LIKE ?" + JdbcUtil.getSort(sort, desc);

        return super.select(sql,TaskCategory.class,new Object[]{userId, '%' + name + '%'},startIndex,rows);
    }

    public Collection selectUserNonProjectCategories(String userId,String name, boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
    	Collection col=selectAllUserProjectCategories();
    	String id="";
    	for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    		TaskCategory project = (TaskCategory) mapIt.next();
			String projectId=project.getId();
			id+="AND id!='"+projectId+"' ";
		}
        String sql = "SELECT id,name,description,userId,general FROM tm_category  " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") +id+ " AND name LIKE ?" + JdbcUtil.getSort(sort, desc);

        return super.select(sql,TaskCategory.class,new Object[]{userId, '%' + name + '%'},startIndex,rows);
    }

    public int countUserProjectCategories(String userId, String name, boolean includeGeneral, int startIndex, int rows) throws DaoException {
        String sql = "SELECT COUNT(id) catNum FROM tm_category INNER JOIN worms_project ON tm_category.id=worms_project.projectId " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") + " AND name LIKE ?";

        Collection list = super.select(sql,HashMap.class,new Object[]{userId, '%' + name + '%'},startIndex,rows);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            int i = ((Number)map.get("catNum")).intValue();
            return i;
        }
        return 0;
    }

    public int countUserNonProjectCategories(String userId, String name, boolean includeGeneral, int startIndex, int rows) throws DaoException {
    	Collection col=selectAllUserProjectCategories();
    	String id="";
    	for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
    		TaskCategory project = (TaskCategory) mapIt.next();
			String projectId=project.getId();
			id+="AND id!='"+projectId+"' ";
		}
        String sql = "SELECT COUNT(id) catNum FROM tm_category " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") +id+ " AND name LIKE ?";

        Collection list = super.select(sql,HashMap.class,new Object[]{userId, '%' + name + '%'},startIndex,rows);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            int i = ((Number)map.get("catNum")).intValue();
            return i;
        }
        return 0;
    }

    public void deleteReassignments(String taskId) throws DaoException
    {
            String sql = "DELETE FROM tm_reassign WHERE taskId=?";
            super.update(sql,new Object[]{taskId});
    }

    public Collection selectTasks(String categoryId,int sIndex, int maxRow,String sort,boolean desc) throws DaoException
    {
            String sql = "SELECT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority " +
                    "from tm_task tt LEFT JOIN cal_event ce ON " +
                    "tt.id=ce.eventId LEFT JOIN cal_event_attendee ca ON " +
                    "tt.id=ca.eventId LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id WHERE " +
                    "tt.categoryId='"+categoryId+"' " +
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            return super.select(sql,Task.class,null,sIndex,maxRow);
    }

    public void deleteAssignee(Assignee assignee) throws DaoException
    {
        String sql = "DELETE FROM tm_assignee WHERE userId=#userId# AND taskId=#eventId#";
        super.update(sql,assignee);
    }

    public void deleteCategory(String categoryId) throws DaoException
    {
            String sql = "DELETE FROM tm_category WHERE id='"+categoryId+"'";
            super.update(sql,null);
    }

    public void deleteTask(String taskId) throws DaoException
    {
            String sql = "DELETE FROM tm_task WHERE id='"+taskId+"'";
            super.update(sql,null);
            sql = "DELETE FROM tm_reassign WHERE taskId='"+taskId+"'";
            super.update(sql,null);
    }

    protected String quote(String str) {
        if (str == null)
            return null;

        StringBuffer buffer = new StringBuffer();
        buffer.append('\'');
        for(int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            if(c == '\\' || c == '\'' || c == '"')
                buffer.append('\\');
            buffer.append(c);
        }

        buffer.append('\'');
        return buffer.toString();
    }

    /**
     * select task category that's not attached to project
     *
     */
    public Collection selectNonProjectCategories(String userId, String[] projectId) throws DaoException
    {
        String s="";

        if (projectId!=null && projectId.length>0) {
            for (int i=0; i<projectId.length;i++) {
                    s+= "AND tc.id!='"+projectId[i]+"' ";
            }
        }
        String sql = "SELECT DISTINCT tc.id, tc.name, tc.description, tc.userId, tc.general " +
                "from tm_task tt INNER JOIN tm_category tc on tt.categoryId=tc.id "+
                "INNER JOIN cal_event ce on ce.eventId=tt.id "+
                "INNER JOIN cal_event_attendee ca on ca.eventId=ce.eventId WHERE " +
                " ca.userId='"+userId+"' "+s;
            return super.select(sql,TaskCategory.class,null,0,-1);
    }

    /**
     * update task comments
     * - comments can be added after a task is created.
     */
    public void addComments(String taskId, String comments) throws DaoException {
        String sql = "UPDATE tm_task SET comments=? WHERE id=?";
        super.update(sql,new Object[]{comments,taskId});
    }

    public Collection getComments(String taskId) throws DaoException {
        String sql = "SELECT comments FROM tm_task WHERE id=?";
        return super.select(sql,HashMap.class,new String[]{taskId},0,-1);
    }

    public int countTasksByCategory(String categoryid) throws DaoException {

		Object[] args = {categoryid};

		String sql = "SELECT COUNT(*) AS total FROM tm_task WHERE tm_task.categoryId = ?";
		Collection list = super.select(sql, HashMap.class, args, 0, 1);
		if (list.size() > 0) {
			HashMap map = (HashMap) list.iterator().next();
			int num= Integer.parseInt(map.get("total").toString());
			if(num==0){
				String sql2 = "SELECT COUNT(*) AS total FROM worms_project WHERE projectId = ?";
				Collection list2 = super.select(sql2, HashMap.class, args, 0, 1);
				if (list2.size() > 0) {
					HashMap map2 = (HashMap) list.iterator().next();
					return Integer.parseInt(map2.get("total").toString());
				} else
					return 0;
			}else
			return num;
		} else{
			String sql2 = "SELECT COUNT(*) AS total FROM worms_project WHERE projectId = ?";
			Collection list2 = super.select(sql2, HashMap.class, args, 0, 1);
			if (list2.size() > 0) {
				HashMap map2 = (HashMap) list.iterator().next();
				return Integer.parseInt(map2.get("total").toString());
			} else
				return 0;
		}

	}

    public void updateTaskModifiedTime(String taskId) throws DaoException {
        super.update("UPDATE cal_event set new=?, modified=?, lastModified=? WHERE eventId=? ", new Object[]{"0", "1", new Date(), taskId});
    }
}
