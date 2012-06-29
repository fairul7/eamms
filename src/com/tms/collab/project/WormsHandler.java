package com.tms.collab.project;

import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.TaskManagerDao;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.*;

public class WormsHandler extends DefaultModule
{
    public static final String PERMISSION_PROJECTS_VIEW = "com.tms.worms.project.Project.view";
    public static final String PERMISSION_PROJECTS_ADD = "com.tms.worms.project.Project.add";
    public static final String PERMISSION_PROJECTS_EDIT = "com.tms.worms.project.Project.edit";
    public static final String PERMISSION_PROJECTS_DELETE = "com.tms.worms.project.Project.delete";
    public static final String PERMISSION_PROJECTS_ADMINISTER = "com.tms.worms.project.Project.administer";

    public static final String PROPERTY_WORMS_USER = "com.tms.worms.wormsUser";

    /* Project Handling */
    public void addProject(Project project) throws WormsException
    {
        try
        {
			TaskManager handler = (TaskManager) Application.getInstance().getModule(TaskManager.class);
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorEquals("name", project.getProjectName(), DaoOperator.OPERATOR_AND));
			Collection list = handler.getCategories(query, 0, 1, null, false);
			if(list.size() == 0)
			{
				//Auto creating task category
				TaskCategory category = new TaskCategory(project.getProjectName(), project.getProjectDescription());
				category.setId(project.getProjectId());
				category.setUserId(project.getOwnerId());
				category.setGeneral(false);
				handler.addCategory(category);
			}
            WormsDao dao = (WormsDao) getDao();
            //added creation and modified date
            project.setCreationDate(new Date());
            project.setModifiedDate(new Date());
            dao.insertProject(project);
			dao.insertProjectFiles(project);
			dao.insertProjectMembers(project);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

	/**
	 * Creates a project with reference to a particular milestone
	 * @param project Project to create
	 * @param template Template to create with
	 */
	public void addProject(Project project, Template template, Date startDate, User user) throws WormsException
	{
		try
        {
			addProject(project);
			/* Creating project schedule */
			if(template != null)
			{
				WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
				TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
				CalendarModule handler = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
				int count = 1;
				/* Retrieving task category */
				DaoQuery query = new DaoQuery();
				query.addProperty(new OperatorEquals("id", project.getProjectId(), DaoOperator.OPERATOR_AND));
				Collection list = manager.getCategories(query, 0, 1, null, false);
				if(list.size() != 0)
				{
					TaskCategory category = (TaskCategory) list.iterator().next();
					/* Creating milestones */
					Collection milestones = worms.getMilestonesByProject(template.getTemplateId(), true);
					for (Iterator i = milestones.iterator(); i.hasNext();)
					{
						Milestone milestone = (Milestone) i.next();
						milestone.setProjectId(project.getProjectId());
						milestone.setMilestoneId(UuidGenerator.getInstance().getUuid());
						milestone.setMilestoneOrder(count);
						worms.addMilestone(milestone);
						/* Creating tasks */
						Calendar calStart = Calendar.getInstance();
						Calendar calEnd = Calendar.getInstance();
						for (Iterator it = milestone.getDescriptors().iterator(); it.hasNext();)
						{
							TaskDescriptor taskDescriptor = (TaskDescriptor) it.next();
							calStart.setTime(startDate);
							calStart.add(Calendar.DAY_OF_MONTH, taskDescriptor.getDescStart() - 1);
							calStart.set(Calendar.HOUR, 0);
							calStart.set(Calendar.MINUTE, 0);
							calStart.set(Calendar.SECOND, 0);
							calEnd.setTime(calStart.getTime());
							calEnd.add(Calendar.DAY_OF_MONTH, taskDescriptor.getDescDuration() - 1);
							calEnd.set(Calendar.HOUR, 23);
							calEnd.set(Calendar.MINUTE, 55);
							calEnd.set(Calendar.SECOND, 0);
							Task task = generateTask(taskDescriptor, user, calStart.getTime(), calEnd.getTime(), category);
							int totalMandaysEstimated = 0;
			                totalMandaysEstimated = WormsUtil.getWorkingDays(project.getProjectWorking(), calStart.getTime(), calEnd.getTime()) ;
			                task.setEstimation(totalMandaysEstimated);
							handler.addCalendarEvent(Task.class.getName(), task, task.getUserId(), true);
							manager.addTask(task);
							worms.addMilestoneTask(milestone.getMilestoneId(), task.getId());
						}
						count++;
					}
					/* Creating roles */
					Collection roles = worms.getRolesByProject(template.getTemplateId(), true);
					for (Iterator i = roles.iterator(); i.hasNext();)
					{
						Role role = (Role) i.next();
						role.setProjectId(project.getProjectId());
						role.setRoleId(UuidGenerator.getInstance().getUuid());
						worms.addRole(role);
					}
				}
			}
		}
        catch(Exception e)
        {
            throw new WormsException("Error while adding project " + project.getProjectName() +
				" with template " + template.getTemplateName(), e);
        }
	}

	protected Task generateTask(TaskDescriptor taskDescriptor, User user, Date startDate, Date endDate, TaskCategory category)
	{
		Task task = new Task();

        /* Forumulating attendee */
		Collection attendeeList = new TreeSet();
		Assignee att = new Assignee();
		att.setUserId(user.getId());
		att.setProperty("username", user.getUsername());
		att.setProperty("firstName", user.getProperty("firstName"));
		att.setProperty("lastName", user.getProperty("lastName"));
		att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
		att.setProgress(0);
		att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
		attendeeList.add(att);

		/* Assembling object */
		task.setId(Task.class.getName() + "_" + UuidGenerator.getInstance().getUuid());
		task.setEventId(task.getId());
		task.setDescription(taskDescriptor.getDescDescription());
        task.setDueDate(endDate);
        task.setStartDate(startDate);
        task.setUserId(user.getId());
        task.setAssigner(user.getName());
        task.setAssignerId(user.getId());
        task.setCategoryId(category.getId());
		task.setReassign(false);
		task.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
        task.setCreationDate(new Date());
        task.setEndDate(endDate);
        task.setTitle(taskDescriptor.getDescName());
        task.setAttendees(attendeeList);

		return task;
	}

    public void editProject(Project project) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            //added modifiedDate
            project.setModifiedDate(new Date());
            dao.updateProject(project);
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            TaskCategory tc = new TaskCategory();
            tc.setId(project.getProjectId());
            tc.setName(project.getProjectName());
            tc.setDescription(project.getProjectDescription());
            tc.setUserId(project.getOwnerId());
            tm.updateCategoryByProject(tc);
			dao.insertProjectFiles(project);
			dao.insertProjectMembers(project);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public void updateProject(Project project) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            //added modifiedDate
            project.setModifiedDate(new Date());
            dao.updateProjectByCategory(project);          
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void deleteProject(String projectId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.deleteProject(projectId);
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            tm.deleteCategory(projectId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Project getProject(String projectId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProject(projectId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Collection selectProjectUsers(String projectId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjectUsers(projectId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Collection selectRolePersonnel(String roleId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectRolePersonnel(roleId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getProjects() throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjects();
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    /**
     * Select Collection of Project object by month
     * @param calendarMonth The integer static field variable in java.util.Calendar indicating the month
     * @param year Four digit year value
     * @return Collection of Project object
     */
    public Collection selectProjectsWithTimeSheetByMonth(int calendarMonth, int year) {
    	WormsDao dao = (WormsDao) getDao();
    	try {
    		return dao.selectProjectsWithTimeSheetByMonth(calendarMonth, year);
    	}
    	catch(DaoException e)
        {
    		Log.getLog(getClass()).error(e, e);
            return null;
        }
    }

    public Collection getProjects(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjects(query, start, maxResults, sort, descending);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Collection getNonProjects(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectNonProjects(query, start, maxResults, sort, descending);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public int getProjectsCount(DaoQuery query) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjectsCount(query);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getProjectsByOwner(String userId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjectsByOwner(userId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Collection getNonProjectsByOwner(String userId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectNonProjectsByOwner(userId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getProjectInvolvedUsers(String projectId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjectInvolvedUsers(projectId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getProjectsInvolved(String userId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjectsInvolved(userId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getProjectCategories() throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectProjectCategories();
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    /**
     * Checks if a user has administratrion rights to a particular project
     * @param projectId the id of the project
     * @param userId the id of the user
     * @return a boolean value signifying if the user has administrative rights
     * @throws WormsException
     */
    public boolean hasProjectPermission(String projectId, String userId) throws WormsException
    {
        try
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            if(service.hasPermission(userId, PERMISSION_PROJECTS_ADMINISTER, getClass().getName(), projectId)){
            	return true;
            }
            else if(service.hasPermission(userId, PERMISSION_PROJECTS_EDIT, getClass().getName(), projectId))
            {
                Project project = getProject(projectId);
                if((project.getOwnerId().equals(userId)))
                    return true;
                if(project.getMembers()!=null){
					ProjectMember pm;

	                for(Iterator i= project.getMembers().iterator();i.hasNext();){
	                	pm = (ProjectMember)i.next();
	                	if((pm.getMemberId().equals(userId)))
	                		return true;
	                }
	                
	            }
            }
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * Checks if a user is involved in a particular project
     * @param projectId the id of the project
     * @param userId the id of the user
     * @return a boolean value signifying if the user is involved in the project
     * @throws WormsException
     */
    public boolean hasProjectInvolvement(String projectId, String userId) throws WormsException
    {
        boolean isInvolved = false;
        try
        {
            WormsDao dao = (WormsDao) getDao();
            isInvolved = dao.selectIsInvolved(projectId, userId);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return isInvolved;
    }

    public Date getProjectStart(String projectId) throws WormsException
    {
        Date startDate = null;
        try
        {
            WormsDao dao = (WormsDao) getDao();
            startDate = dao.selectProjectStart(projectId);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return startDate;
    } 
    
    public Date getProjectEnd(String projectId) throws WormsException
    {
        Date endDate = null;
        try
        {
            WormsDao dao = (WormsDao) getDao();
            endDate = dao.selectProjectEnd(projectId);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return endDate;
    }
    
    public Date selectProjectActualStart(String projectId, Date date) throws WormsException
    {
        Date startDate = null;
        try
        {
            WormsDao dao = (WormsDao) getDao();
            startDate = dao.selectProjectActualStart(projectId,date);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return startDate;
    } 
    
    public Date selectProjectActualEnd(String projectId, Date date) throws WormsException
    {
        Date endDate = null;
        try
        {
            WormsDao dao = (WormsDao) getDao();
            endDate = dao.selectProjectActualEnd(projectId,date);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return endDate;
    }
       

    /* Role Handling */
    public void addRole(Role role) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.insertRole(role);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void editRole(Role role) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.updateRole(role);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void deleteRole(String roleId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.deleteRole(roleId);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Role getRole(String roleId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectRole(roleId);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getRoles(boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectRoles(deepRetrieval);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getRoles(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectRoles(query, start, maxResults, sort, descending, deepRetrieval);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getRolesByProject(String projectId, boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectRolesByProject(projectId, deepRetrieval);
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    /* Milestones */
    public void addMilestone(Milestone milestone) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("projectId", milestone.getProjectId(), DaoOperator.OPERATOR_AND));
            Collection list = dao.selectMilestones(query, 0, 1, "milestoneOrder", true, false);
            int order = 0;
            if(list.size() > 0)
            {
                for(Iterator i = list.iterator(); i.hasNext();)
                {
                    Milestone max = (Milestone) i.next();
                    order = max.getMilestoneOrder();
                }
            }
            milestone.setMilestoneOrder(order + 1);
            dao.insertMilestone(milestone);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void editMilestone(Milestone milestone) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.updateMilestone(milestone);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void deleteMilestone(String milestoneId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            Milestone milestone = dao.selectMilestone(milestoneId);
            for(Iterator i = milestone.getTasks().iterator(); i.hasNext();)
            {
                try
                {
                    Task task = (Task) i.next();
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
            dao.deleteMilestone(milestoneId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Milestone getMilestone(String milestoneId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestone(milestoneId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getMilestones(boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestones(deepRetrieval);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getMilestones(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestones(query, start, maxResults, sort, descending, deepRetrieval);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getMilestonesByProject(String projectId, boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestonesByProject(projectId, deepRetrieval);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    public Collection getMilestonesByProjectChart(Project project, boolean deepRetrieval) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.getMilestonesByProjectChart(project, deepRetrieval);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    public Collection selectMilestonesByProjectReport(Project project, boolean deepRetrieval, Date date) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestonesByProjectReport(project, deepRetrieval, date);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Milestone getMilestoneByTask(String taskId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestoneByTask(taskId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

	public Milestone getMilestoneByMeeting(String meetingId) throws WormsException
	{
		try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestoneByMeeting(meetingId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
	}

    public int getMilestoneTotalProgress(String projectId, String exclude) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestoneTotalProgress(projectId, exclude);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void addMilestoneTask(String milestoneId, String taskId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.insertMilestoneTask(milestoneId, taskId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void deleteMilestoneTask(String milestoneId, String taskId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.deleteMilestoneTask(milestoneId, taskId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void deleteAllMilestoneTasks(String milestoneId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.deleteAllMilestoneTasks(milestoneId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

	public void addMilestoneMeeting(String milestoneId, String meetingId) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			dao.insertMilestoneMeeting(milestoneId, meetingId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
	}

	public void deleteMilestoneMeeting(String milestoneId, String meetingId) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			dao.deleteMilestoneMeeting(milestoneId, meetingId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
	}

	public void deleteAllMilestoneMeetings(String milestoneId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
			dao.deleteAllMilestoneMeetings(milestoneId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Date getMilestoneEnd(String milestoneId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectMilestoneEnd(milestoneId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    /* Templates */
    public void addTemplate(Template template) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.insertTemplate(template);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void updateTemplate(Template template) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.updateTemplate(template);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public void deleteTemplate(String templateId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.deleteTemplate(templateId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Template getTemplate(String templateId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectTemplate(templateId);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getTemplates() throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectTemplates();
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getTemplates(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectTemplates(query, start, maxResults, sort, descending);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public Collection getTemplateCategories() throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectTemplateCategories();
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

    public int getTemplateCount(DaoQuery query) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectTemplateCount(query);
        }
        catch (DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }

	/* Task Descriptors */
	public void addDescriptor(TaskDescriptor descriptor) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			dao.insertDescriptor(descriptor);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while adding new task descriptor " + descriptor.getDescName(), e);
		}
	}

	public void updateDescriptor(TaskDescriptor descriptor) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			TaskDescriptor original = getDescriptor(descriptor.getDescId());
			dao.deleteMilestoneTask(original.getMilestoneId(), original.getDescId());
			dao.updateDescriptor(descriptor);
			dao.insertMilestoneTask(descriptor.getMilestoneId(), descriptor.getDescId());
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while updating task descriptor " + descriptor.getDescId(), e);
		}
	}

	public void deleteDescriptor(String descId) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			TaskDescriptor descriptor = getDescriptor(descId);
			dao.deleteDescriptor(descId);
			dao.deleteMilestoneTask(descriptor.getMilestoneId(), descId);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while deleting task descriptor " + descId, e);
		}
	}

	public void deleteMilestoneDescriptors(String milestoneId) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			dao.deleteMilestoneDescriptors(milestoneId);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while deleting descriptors for milestone " + milestoneId, e);
		}
	}

	public TaskDescriptor getDescriptor(String descId) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			return dao.selectDescriptor(descId);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while retriving task descriptor " + descId, e);
		}
	}

	/**
	 * Returns all task descriptors
	 * @return A collection of TaskDescriptor
	 * @throws WormsException
	 */
	public Collection getDescriptors() throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			return dao.selectDescriptors();
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while retriving task descriptors", e);
		}
	}

	/**
	 * Returns all descriptors matching the formulated query
	 * @param query
	 * @param start
	 * @param maxResults
	 * @param sort
	 * @param descending
	 * @return A collection of TaskDescriptor
	 * @throws WormsException
	 */
    public Collection getDescriptors(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			return dao.selectDescriptors(query, start, maxResults, sort, descending);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while retriving task descriptors", e);
		}
	}

	/**
	 * Returns all descriptors within a particular milestone
	 * @param milestoneId
	 * @return A collection of TaskDescriptor
	 * @throws WormsException
	 */
	public Collection getDescriptorsByMilestone(String milestoneId) throws WormsException
	{
        try
		{
			WormsDao dao = (WormsDao) getDao();
			return dao.selectDescriptorsByMilestone(milestoneId);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while retriving task descriptors for milestone " + milestoneId, e);
		}
	}

	/**
	 * Returns all descriptors for a particular project
	 * @param projectId
	 * @return A collection of TaskDescriptor
	 * @throws WormsException
	 */
	public Collection getDescriptorsByProject(String projectId) throws WormsException
	{
		try
		{
			WormsDao dao = (WormsDao) getDao();
			return dao.selectDescriptorsByProject(projectId);
		}
		catch (DaoException e)
		{
			throw new WormsException("Error while retriving task descriptors for project " + projectId, e);
		}
	}

    /**
     * add on 04252005 - for TimeSheet module
     */
    public Project getProjectByTaskId(String taskId) throws WormsException {
        try {
            WormsDao dao = (WormsDao)getDao();
            return dao.selectProjectByTaskId(taskId);
        }
        catch(DaoException e) {
            throw new WormsException("Error while retriving project by task id ",e);
        }
    }

    /**
     * added for timesheet module
     */
    public Collection getProjectForTimeSheet(String userId, String archived) throws WormsException {
        try {
            WormsDao dao = (WormsDao)getDao();
            return dao.selectProjectsForTimeSheet(userId,archived);
        }
        catch(DaoException e) {
            throw new WormsException("Error while get project list for user in time sheet ",e);
        }
    }

    public Project getProjectByProjectName(String taskId) {
        WormsDao dao = (WormsDao)getDao();
        try {
            return dao.selectProjectByProjectName(taskId);//dao.selectProjectByTaskId(taskId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getProjectByTaskId "+e.toString());
        }
        return null;
    }

    public Collection getTaskAttachToProject(String projectId, String userid) {
        WormsDao dao = (WormsDao)getDao();
        Collection col = null;
        try {
            col = dao.selectTaskAttachToProject(projectId,userid);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getTaskList "+e.toString());
        }
        return col;
    }

    public Collection getTaskListAttachToProject(String projectId,String userId) {
        WormsDao dao = (WormsDao)getDao();
        Collection col = null;
        try {
            col = dao.selectTaskListAttachToProject(projectId,userId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getTaskList "+e.toString());
        }
        return col;
    }
    
    public Collection getTaskListAttachToProjects(String projectId,String userId) {
        WormsDao dao = (WormsDao)getDao();
        Collection col = null;
        try {
            col = dao.selectTaskListAttachToProjects(projectId,userId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getTaskList "+e.toString());
        }
        return col;
    }
    
    public Collection selectTaskListAttachToNonProject(String projectId,String userId) {
        WormsDao dao = (WormsDao)getDao();
        Collection col = null;
        try {
            col = dao.selectTaskListAttachToNonProject(projectId,userId);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Error in getTaskList "+e.toString());
        }
        return col;
    }

    public Collection getTaskList(String projectId,String userId) {
            WormsDao dao = (WormsDao)getDao();
            Collection col = null;
            try {
                col = dao.selectTaskList(projectId,userId);
            }
            catch(DaoException e) {
                Log.getLog(getClass()).error("Error in getTaskList "+e.toString());
            }
            return col;
        }
    
    public Collection selectDefectsType() throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.selectDefectsType();
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    /* Report Handling */
    public void addReport(Report report) throws WormsException
    {
        try
        {
        	WormsDao dao = (WormsDao) getDao();
        	dao.insertReport(report);
        	if(report.getProjects().getProjectId()!=null){
        		dao.insertReportProject(report.getProjects());
        	}if(report.getRoles().size()>0){
        		dao.insertReportRole(report.getRoles());
        	}if(report.getMilestones().size()>0){
        		dao.insertReportMilestone(report.getMilestones());
        	}if(report.getTasks().size()>0){
        		dao.insertReportTask(report.getTasks());
        	}if(report.getDefects().size()>0){
        		dao.insertReportDefect(report.getDefects());
        	}if(report.getCost().size()>0){
        		dao.insertReportCost(report.getCost());
        	}			
        }
        catch(Exception e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Report getReport(String reportId, boolean all) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.getReport(reportId, all);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Report getLatestReport(String projectId, boolean all) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.getLatestReport(projectId, all);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public Collection getProjectReport(String projectId, String filter, String sort,boolean desc,int startIndex,int rows) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            return dao.getProjectReport(projectId,filter, sort,desc,startIndex,rows);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    
    public void deleteReport(String reportId) throws WormsException
    {
        try
        {
            WormsDao dao = (WormsDao) getDao();
            dao.deleteReport(reportId);
        }
        catch(DaoException e)
        {
            throw new WormsException(e.getMessage(), e);
        }
    }
    public String getTaskProjectID(String taskId) throws DaoException{
    	WormsDao dao = (WormsDao) getDao();
        return dao.getTaskProjectID(taskId);
    }
    
    public boolean hasProjectReportInvolvement(String projectId, String userId) throws WormsException
    {
        boolean isInvolved = false;
        try
        {
            WormsDao dao = (WormsDao) getDao();
            isInvolved = dao.hasProjectReportInvolvement(projectId, userId);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return isInvolved;
    }
}
