package com.tms.collab.taskmanager.model;

import kacang.model.*;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingService;
import kacang.services.scheduling.SchedulingException;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.project.Project;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.taskmanager.jobs.NotificationJob;
import com.tms.collab.timesheet.model.TimeSheetModule;

import java.util.*;
import java.sql.SQLException;

public class TaskManager extends DefaultModule
{
    public final static String PERMISSION_MANAGETASK = "com.tms.collab.taskmanager.ManageTask";
    public final static String DEFAULT_CATEGORY_ID = "com.tms.collab.taskmanager.category.general";
	public static final String SCHEDULER_NOTIFICATION = "task_manager_notification";
    public final static String TASKMANAGER_FILES_FOLDER ="TM";
    private TaskManagerDao dao;

	public void init()
	{
		super.init();
		//Initializing Notification Daemon
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, calendar.getMaximum(Calendar.HOUR));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        JobSchedule schedule = new JobSchedule(Application.getInstance().getMessage("com.tms.collab.taskmanager.model.TaskManager"), JobSchedule.DAILY);
        schedule.setGroup(SCHEDULER_NOTIFICATION);
        schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setRepeatInterval(1);
		schedule.setStartTime(calendar.getTime());

        JobTask task = new NotificationJob();
        task.setName(Application.getInstance().getMessage("com.tms.collab.taskmanager.model.TaskManager"));
        task.setGroup(SCHEDULER_NOTIFICATION);
        task.setDescription("Task Notification Daemon");

        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        try
        {
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
        }
        catch (SchedulingException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
	}

    public void addTask(Task task) throws CalendarException, DaoException
    {

        dao = (TaskManagerDao) getDao();
        dao.addTask(task);
        addAssignees(task);
    }

    public void addAssignees(Task task) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            dao.insertAssignee(assignee);
        }
    }

    public void updateTask(Task task) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection newAssignees = task.getAttendees();
        Collection oldAssignees = dao.selectTaskAssignees(task.getId());
        Collection newAssigneeIds = new TreeSet();
        Collection oldAssigneeIds = new TreeSet();
        int delete=0;
        int add=0;
        for (Iterator iterator = newAssignees.iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            newAssigneeIds.add(assignee.getUserId());
        }
        for (Iterator iterator = oldAssignees.iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            oldAssigneeIds.add(assignee.getUserId());
        }
        for (Iterator iterator = oldAssignees.iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            if(!newAssigneeIds.contains(assignee.getUserId()))
            {
                dao.deleteAssignee(assignee);
                delete++;
            }
        }
        for (Iterator iterator = newAssignees.iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            if(!oldAssigneeIds.contains(assignee.getUserId())){
                dao.insertAssignee(assignee);
                add++;
            }
        }
        if(add==0&&delete>0){
        boolean completed = true;
        boolean first = true;
        Date date= new Date();
        for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            if(assignee.getTaskStatus() != Assignee.TASK_STATUS_COMPLETED ){
                completed = false;
            	break;
            }
            else {
                if (first) {
                	date = assignee.getCompleteDate();
                    first = false;
                }
                else {
                    if (assignee.getCompleteDate().after(date)) {
                            date = assignee.getCompleteDate();
                    }
                }

            }
        }
        if(completed){
            task.setCompleted(true);
            task.setCompleteDate(date);
            dao.updateTask(task);
        }
       }

        dao.updateTask(task);
    }

    public void clearTaskReassignments(String taskId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        dao.deleteReassignments(taskId);

    }

    public float getAverageProgress(String []taskIds) throws DaoException, DataObjectNotFoundException
    {
        float totalProgress = 0;
        int totalTasks = 0;
        for (int i = 0; i < taskIds.length; i++)
        {
            String taskId = taskIds[i];
            Task task = getTask(taskId);
            if(task!=null){
                totalProgress += task.getOverallProgress();
                totalTasks++;
            }
        }
        if(totalTasks == 0)
            return -1;
        return totalProgress/totalTasks;
    }

    public void startTask(String taskId, String userId) throws DataObjectNotFoundException, DaoException
    {
        dao = (TaskManagerDao) getDao();
        Assignee assignee = getAssignee(taskId,userId);
        if(assignee!=null){
            assignee.setTaskStatus(Assignee.TASK_STATUS_IN_PROGRESS);
            assignee.setStartDate(new Date());
            assignee.setProgress(0);
            int updateCount = dao.updateAssignee(assignee);
            if (updateCount == 0) {
                dao.insertAssignee(assignee);
            }
            updateTaskModifiedTime(taskId);
        }
    }

    public Assignee getAssignee(String taskId,String userId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectAssignee(taskId,userId);
    }

    public void updateAssignee(Assignee assignee) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        dao.updateAssignee(assignee);
        updateTaskModifiedTime(assignee.getEventId());
    }

    public void updateAssigneeStartTask(Assignee assignee) throws DaoException {
        dao = (TaskManagerDao)getDao();        
        dao.updateAssigneeStartTask(assignee);
        updateTaskModifiedTime(assignee.getEventId());
    }

    public void updateAssigneeTaskCompletion(Assignee assignee) throws DaoException
    {
        dao = (TaskManagerDao)getDao();
        dao.updateAssigneeTaskCompletion(assignee);
        updateTaskModifiedTime(assignee.getEventId());
    }

    public Collection getUserCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex,int rows) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectUserCategories(userId,includeGeneral,sort,desc,startIndex,rows);
    }

    public Collection getUserProjectCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex,int rows) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectUserProjectCategories(userId,includeGeneral,sort,desc,startIndex,rows);
    }

    public Collection getUserNonProjectCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex,int rows) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectUserNonProjectCategories(userId,includeGeneral,sort,desc,startIndex,rows);
    }

    public Collection getUserTaskCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex,int rows) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectUserTaskCategories(userId,includeGeneral,sort,desc,startIndex,rows);
    }

    public Collection getUserCategories(String userId, String name, boolean includeGeneral,String sort,boolean desc,int startIndex,int rows){
        dao = (TaskManagerDao) getDao();
        try {
            return dao.selectUserCategories(userId, name, includeGeneral,sort,desc,startIndex,rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).error("error getting categories");
        }
        return null;
    }

    public Collection getUserProjectCategories(String userId, String name, boolean includeGeneral,String sort,boolean desc,int startIndex,int rows){
        dao = (TaskManagerDao) getDao();
        try {
            return dao.selectUserProjectCategories(userId, name, includeGeneral,sort,desc,startIndex,rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).error("error getting categories");
        }
        return null;
    }

    public Collection getUserNonProjectCategories(String userId, String name, boolean includeGeneral,String sort,boolean desc,int startIndex,int rows){
        dao = (TaskManagerDao) getDao();
        try {
            return dao.selectUserNonProjectCategories(userId, name, includeGeneral,sort,desc,startIndex,rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).error("error getting categories");
        }
        return null;
    }

    public Collection getCategories(String userId,boolean completed) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectCategories(userId,completed);

    }

    public Collection getCategories() throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectCategories();
    }

    public Collection getAllCategories() throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectAllCategories();
    }

    public Collection getCategories(String userId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectCategories(userId);

    }

	public Collection getCategories(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws TaskException
	{
		try
		{
			TaskManagerDao dao = (TaskManagerDao) getDao();
			return dao.selectCategories(query, start, maxResults, sort, descending);
		}
		catch (DaoException e)
		{
			throw new TaskException("Error while retrieving task categories", e);
		}
	}

	public Collection getTMCategories(String userid) throws TaskException
	{
		try
		{
			TaskManagerDao dao = (TaskManagerDao) getDao();
			return dao.selectTMCategories(userid);
		}
		catch (DaoException e)
		{
			throw new TaskException("Error while retrieving task categories", e);
		}
	}

    public void addCategory(TaskCategory category) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        dao.addCategory(category);
    }

    public void updateCategory(TaskCategory category) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        dao.updateCategory(category);
        Project project = new Project();
        project.setProjectId(category.getId());
        project.setProjectName(category.getName());
        project.setProjectDescription(category.getDescription());
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        try {
			worms.updateProject(project);
		} catch (WormsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void updateCategoryByProject(TaskCategory category) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        dao.updateCategoryByProject(category);
    }

    public TaskCategory getCategory(String categoryId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection col =dao.selectCategory(categoryId);
        if(col.size()>0)
            return (TaskCategory)col.iterator().next();
        return null;
    }

    public TaskCategory getCategoryByTask(String taskId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection col =dao.selectCategoryByTask(taskId);
        if(col.size()>0)
            return (TaskCategory)col.iterator().next();
        return null;
    }

    public Collection getTasks(String filter,String userId,String categoryId, boolean completed, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);

        Collection tasks =  dao.selectTasks(filter,userId,categoryId,completed,sIndex,maxRow,sort,desc);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getPublicTasks(String filter,String userId,String categoryId, boolean completed, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);

        Collection tasks =  dao.selectPublicTasks(filter,userId,categoryId,completed,sIndex,maxRow,sort,desc);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getTasksByDate(Date date, String userId,int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection tasks = dao.selectTasks(date,userId,sIndex,maxRow,sort,desc);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getAllTasks(String filter,String userId,String categoryId, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection tasks = dao.selectTasks(filter, userId,categoryId,sIndex,maxRow,sort,desc);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getAllPublicTasks(String filter,String userId,String categoryId, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection tasks = dao.getAllPublicTasks(filter, userId,categoryId,sIndex,maxRow,sort,desc);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getCalendarTasks(String search, Date from,Date to, String[] userIds,boolean onlyIncomplete,int sIndex,int rows,String sort,boolean desc) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection tasks = dao.selectCalendarTasks(search,from,to,userIds,onlyIncomplete,sIndex,rows,sort,desc);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getCalendarTasks(Date from,Date to, String[] userIds,boolean onlyIncomplete) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection tasks = dao.selectCalendarTasks(from,to,userIds,onlyIncomplete,0,-1,"dueDate",false);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
        }
        return tasks;
    }

    public Collection getCalendarTasks(Date from, String[] userIds,boolean onlyIncomplete) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection tasks = dao.selectCalendarTasks(from,userIds,onlyIncomplete,0,-1,"dueDate",false);
        ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
        Collection newTasks = new ArrayList(tasks.size());
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();)
        {
            Task task = (Task) iterator.next();
            task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
            // add in for get attendee list in tasklist portlet
            task.setAttendees(getAssignees(task.getId()));
            task.setReassignments(dao.selectReassignments(task.getId()));
            newTasks.add(task);

        }
        //return tasks;
        return newTasks;
    }

    public int countCalendarTasks(Date from, String[] userIds,boolean onlyIncomplete) throws DaoException {
        dao = (TaskManagerDao) getDao();
        return dao.countCalendarTasks(from,userIds,onlyIncomplete,0,-1,"dueDate",false);

    }

    public int countCategories(String userId,boolean includeGeneral) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.countCategory(userId,includeGeneral);
    }

    public int countProjectCategories(String userId,boolean includeGeneral) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.countProjectCategory(userId,includeGeneral);
    }

    public int countNonProjectCategories(String userId,boolean includeGeneral) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.countNonProjectCategory(userId,includeGeneral);
    }

    public int countUserCategories(String userId, String name, boolean includeGeneral, int start, int rows)
    {
        dao = (TaskManagerDao) getDao();
        try {
            return dao.countUserCategories(userId,name, includeGeneral, start, rows);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countUserProjectCategories(String userId, String name, boolean includeGeneral, int start, int rows)
    {
        dao = (TaskManagerDao) getDao();
        try {
            return dao.countUserProjectCategories(userId,name, includeGeneral, start, rows);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countUserNonProjectCategories(String userId, String name, boolean includeGeneral, int start, int rows)
    {
        dao = (TaskManagerDao) getDao();
        try {
            return dao.countUserNonProjectCategories(userId,name, includeGeneral, start, rows);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Collection getAssignees(String taskId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        return dao.selectAssignees(taskId);
    }

    public Task getTask(String taskId) throws DaoException, DataObjectNotFoundException
   {
     //  CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            //CalendarEvent e = cm.getCalendarEvent(taskId);
       dao = (TaskManagerDao) getDao();
       Task task = dao.selectTask(taskId);
       ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
       if(task!=null) {
                //Task task = (Task)col.iterator().next();
                //task.setLastModifiedBy(UserUtil.getUser(task.getLastModifiedBy()).getName());
           task.setAttendees(getAssignees(taskId));
           task.setReassignments(dao.selectReassignments(taskId));
           task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));
           return task;
       }
        return null;
    }

    public Task getTask(String taskId, String userId) throws DaoException, DataObjectNotFoundException, SecurityException
    {
      //      CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
         //   CalendarEvent e = cm.getCalendarEvent(taskId);
            dao = (TaskManagerDao) getDao();
            Task task = dao.selectTask(taskId,userId);
            if(task!=null) {
               // Task task = (Task)col.iterator().next();
                task.setAttendees(getAssignees(taskId));
                task.setReassignments(getReassignments(taskId));
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                task.setResources(rm.getBookedResources(task.getId(),CalendarModule.DEFAULT_INSTANCE_ID));   return task;
            }else throw new DataObjectNotFoundException();        
    }

    public Collection getTasks(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        TaskManagerDao dao = (TaskManagerDao) getDao();
        return dao.selectTasks(query, start, maxResults, sort, descending);
    }

    public Collection selectTasksReport(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        TaskManagerDao dao = (TaskManagerDao) getDao();
        return dao.selectTasksReport(query, start, maxResults, sort, descending);
    }

    public Collection getReassignments(String taskId) throws SecurityException, DaoException
    {
            dao = (TaskManagerDao) getDao();
            Collection col = dao.selectReassignments(taskId);
            Collection reass = new ArrayList();
            for (Iterator i= col.iterator();i.hasNext();){
                DefaultDataObject obj = (DefaultDataObject)i.next();
                User user= UserUtil.getUser((String)obj.getProperty("assignerId"));
                obj.setProperty("name",user.getName());
                reass.add(obj);
            }
            return reass;
    }

    public void completeTask(String taskId,String userId) throws DaoException, DataObjectNotFoundException
    {
        dao = (TaskManagerDao) getDao();
        Task task = getTask(taskId);
        if(task!=null){
        for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            if(assignee.getUserId().equals(userId)&&assignee.getTaskStatus()==Assignee.TASK_STATUS_IN_PROGRESS){
                assignee.setProgress(100);
                assignee.setTaskStatus(Assignee.TASK_STATUS_COMPLETED);
                assignee.setCompleteDate(new Date());
                updateAssignee(assignee);
            }
        }
            boolean completed = true;
            for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
            {
                Assignee assignee = (Assignee) iterator.next();
                if(assignee.getTaskStatus() != Assignee.TASK_STATUS_COMPLETED )
                    completed = false;
            }
            if(completed){
                task.setCompleted(true);
                task.setCompleteDate(new Date());
                dao.updateTask(task);
            }
        }
    }

    public void completeTask(String taskId,Assignee orgAssignee) throws DaoException, DataObjectNotFoundException
    {
        dao = (TaskManagerDao) getDao();
        Task task = getTask(taskId);
        if(task!=null){
        for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            if(assignee.getUserId().equals(orgAssignee.getUserId())) {//&&assignee.getTaskStatus()==Assignee.TASK_STATUS_IN_PROGRESS){
                assignee.setProgress(100);
                assignee.setTaskStatus(Assignee.TASK_STATUS_COMPLETED);
                assignee.setCompleteDate(orgAssignee.getCompleteDate());
                assignee.setCompletedSetBy(orgAssignee.getUpdateBy());
                assignee.setCompletedDateSetOn(orgAssignee.getUpdateDate());
                updateAssigneeTaskCompletion(assignee);
            }
        }

            boolean completed = true;
            boolean first = true;
            Date date = orgAssignee.getCompleteDate();
            for (Iterator iterator = task.getAttendees().iterator(); iterator.hasNext();)
            {
                Assignee assignee = (Assignee) iterator.next();
                if(assignee.getTaskStatus() != Assignee.TASK_STATUS_COMPLETED ){
                    completed = false;
                    break;
                }
                else {
                    if (first) {
                        if (!assignee.getUserId().equals(orgAssignee.getUserId()))
                            date = assignee.getCompleteDate();
                        first = false;
                    }
                    else {
                        if (!assignee.getUserId().equals(orgAssignee.getUserId()) && assignee.getCompleteDate().after(date)) {
                            if (!assignee.getUserId().equals(orgAssignee.getUserId()))
                                date = assignee.getCompleteDate();
                        }
                    }

                }
            }
            if(completed){
                task.setCompleted(true);
                task.setCompleteDate(date);
                dao.updateTask(task);
            }
        }
    }

    public void setReassign(String taskId, boolean reassign) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        dao.updateTaskReassignment(taskId,reassign);
    }

    public void deleteCategory(String categoryId) throws DaoException
    {
        if (!DEFAULT_CATEGORY_ID.equals(categoryId))
        {
            dao = (TaskManagerDao) getDao();
            Collection tasks = dao.selectTasks(categoryId,0,-1,null,false);
            Task task;
            for(Iterator i=tasks.iterator();i.hasNext();)
            {
                task = (Task)i.next();
                dao.updateTaskCategory(task.getId(), DEFAULT_CATEGORY_ID);
            }
            dao.deleteCategory(categoryId);
            String ts = Application.getInstance().getProperty("com.tms.collab.timesheet");
            if (ts!=null && ts.equals("true")) {
                TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
                mod.updateTaskCategory(categoryId,DEFAULT_CATEGORY_ID);
            }
        }
    }

    public void deleteTask(String taskId) throws CalendarException, DaoException
    {
        dao = (TaskManagerDao) getDao();
        CalendarModule cm =(CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        try {
            cm.deleteCalendarEvent(taskId, "FUNAMBOL");
        } catch (DataObjectNotFoundException e) {
            throw new DaoException(e);
        }
        //cm.destroyCalendarEvent(taskId);
        dao.deleteTask(taskId);
    }

    public void destroyTask(String taskId) throws CalendarException, DaoException
    {
        dao = (TaskManagerDao) getDao();
        CalendarModule cm =(CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        cm.destroyCalendarEvent(taskId);
        dao.deleteTask(taskId);
    }

    public void reassignTask(String taskId,String assignerId,Map assignees) throws DaoException, DataObjectNotFoundException, SecurityException, SQLException
    {
        dao = (TaskManagerDao) getDao();
        CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
        Attendee att = cm.getAttendee(taskId,assignerId);
        Date date = new Date();
        boolean reassigned = false;

        // get current task attendees
        Task task = getTask(taskId);
        Collection attendees = task.getAttendees();
        Collection existingAtendees = new ArrayList();
        for (Iterator it = attendees.iterator(); it.hasNext();)
        {
            Attendee attendee = (Attendee)it.next();
            existingAtendees.add(attendee.getUserId());
        }

        for (Iterator iterator = assignees.keySet().iterator(); iterator.hasNext();)
        {
            String id = (String) iterator.next();
            if (!existingAtendees.contains(id)) {
                User tmpUser = UserUtil.getUser(id);
                Attendee tmpatt = new Attendee();
                tmpatt.setUserId(tmpUser.getId());
                tmpatt.setEventId(taskId);
                tmpatt.setAttendeeId(taskId+"_"+CalendarModule.DEFAULT_INSTANCE_ID+"_"+tmpUser.getId());
                tmpatt.setProperty("username", tmpUser.getUsername());
                tmpatt.setProperty("firstName", tmpUser.getProperty("firstName"));
                tmpatt.setProperty("lastName", tmpUser.getProperty("lastName"));
                tmpatt.setCompulsory(true);
                tmpatt.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                tmpatt.setInstanceId(CalendarModule.DEFAULT_INSTANCE_ID);
                cm.addEventAttendee(tmpatt);
                dao.insertReassignment(taskId,assignerId,id,date);
                reassigned = true;
            }
        }
        dao.updateTaskReassignment(taskId,true);
        if(reassigned)
            cm.deleteEventAttendee(att);
    }

    public Collection getNonProjectCategories(String userId, String[] projectId) throws DaoException {
        dao = (TaskManagerDao)getDao();
        return dao.selectNonProjectCategories(userId,projectId);
    }

    public void addComments(String taskId, String comments) throws DaoException {
            dao = (TaskManagerDao)getDao();
            dao.addComments(taskId,comments);
    }

    public String getComments(String taskId) throws DaoException{
            dao = (TaskManagerDao)getDao();
            Collection col = dao.getComments(taskId);
            if (col!=null && col.size()>0) {
                HashMap map = (HashMap)col.iterator().next();
                String sComments = (String)map.get("comments");
                return sComments;
            }
        return "";
    }

    public int countTasksByCategory(String categoryid) throws DaoException {
        dao = (TaskManagerDao) getDao();
        return dao.countTasksByCategory(categoryid);

    }
    public TaskCategory getUserCategory(String categoryId,String userId) throws DaoException
    {
        dao = (TaskManagerDao) getDao();
        Collection col =dao.selectUserCategory(categoryId,userId);
        if(col.size()>0)
            return (TaskCategory)col.iterator().next();
        return null;
    }

    // Blake
    // added for PIMSyncService
    public void updateTaskModifiedTime(String taskId){
        dao = (TaskManagerDao) getDao();
        try {
            dao.updateTaskModifiedTime(taskId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error("error updating modifiedTime of TaskId: " + taskId);
        }
    }
}
