package com.tms.collab.taskmanager.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendaringEventView;
import com.tms.collab.taskmanager.ui.TaskEventView;
import com.tms.collab.taskmanager.ui.TaskForm;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.stdui.Form;
import kacang.Application;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 26, 2003
 * Time: 10:28:19 AM 
 */
public class Task extends CalendarEvent implements CalendaringEventView
{
    private String id;
    private Date dueDate;
    private String categoryId;
    private String category;
    private String assignerId;
    private String assigner;
    private boolean reassign;
    private boolean completed = false;
    private String assigneeId;
    private String assigneeFirst;
    private String assigneeLast;
    private int progress;
    private Collection attachedFiles;
    private Collection reassignments;
    private Date completeDate;
    private TaskEventView taskView= null;
    private TaskForm taskForm = null;
    private String[][] userList = {};
    private double[] totalMandaysSpentEachUser = {};
    private double totalMandaysEstimated = 0;
    private double totalMandaysSpent = 0;
    private int totalAssignee = 0;
    private String[] userSpecificRemarks = {};
    private String remarks = "";

    // priority
    private String taskPriority;
    private String priorityDescription;
    // new
    private String estimationType;
    private double estimation;
    private double estimationMandays;
    private String projectId;
    private String isproject;
    private String comments;
    
    private Date actEndDate;
    private Date actStartDate;
    private String actualEndDate;
    private String actualStartDate;
    private String duration;
    private String actualDuration;
    private String variance;
    private String startVariance;
    private String endVariance;

    public Task()
    {
    }

    public Task(String categoryId, String category, String assignerId, String assigner, boolean reassign, Collection attachedFiles)
    {
        this.categoryId = categoryId;
        this.category = category;
        this.assignerId = assignerId;
        this.assigner = assigner;
        this.reassign = reassign;
        this.attachedFiles = attachedFiles;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getAssignerId()
    {
        return assignerId;
    }

    public void setAssignerId(String assignerId)
    {
        this.assignerId = assignerId;
    }

    public String getAssigner()
    {
        return assigner;
    }

    public void setAssigner(String assigner)
    {
        this.assigner = assigner;
    }

    public boolean isReassign()
    {
        return reassign;
    }

    public void setReassign(boolean reassign)
    {
        this.reassign = reassign;
    }

    public Collection getAttachedFiles()
    {
        return attachedFiles;
    }

    public void setAttachedFiles(Collection attachedFiles)
    {
        this.attachedFiles = attachedFiles;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public Date getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAssigneeFirst()
    {
        return assigneeFirst;
    }

    public void setAssigneeFirst(String assigneeFirst)
    {
        this.assigneeFirst = assigneeFirst;
    }

    public String getAssigneeLast()
    {
        return assigneeLast;
    }

    public void setAssigneeLast(String assigneeLast)
    {
        this.assigneeLast = assigneeLast;
    }

    public String getAssigneeId()
    {
        return assigneeId;
    }

    public String getAssigneeName(){
        return assigneeFirst +" "+ assigneeLast;
    }

    public void setAssigneeId(String assigneeId)
    {
        this.assigneeId = assigneeId;
    }

    public Date getCompleteDate()
    {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate)
    {
        this.completeDate = completeDate;
    }

    public Collection getReassignments()
    {
        return reassignments;
    }

    public void setReassignments(Collection reassignments)
    {
        this.reassignments = reassignments;
    }


    public Widget getEventView(){
        if(taskView==null)
            taskView = new TaskEventView("taskview");
        return taskView;
    }

    public Form getEventEdit(){
        if(taskForm == null)
            taskForm = new TaskForm("editform");
        return taskForm;
    }

    public Forward deleteEvent(String eventId,String userId){
        return null;
    }

    public boolean isOverdue(){
        if(!completed)
            return (new Date().after(dueDate));
        return false;
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }

    public float getOverallProgress(){
        int total = 0;
        for (Iterator iterator = attendees.iterator(); iterator.hasNext();)
        {
            Assignee assignee = (Assignee) iterator.next();
            total += assignee.getProgress();
        }
        return (float)total/attendees.size();
    }

    public void setTaskPriority(String priority) {
        this.taskPriority=priority;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public String getPriorityDescription() {
        String sDesc = Application.getInstance().getProperty("com.tms.collab.taskmanager.taskpriority");
        String[] array = sDesc.split(",");
        for (int i=0;i<array.length;i++) {
            String[] sLabel = array[i].split("-");
            if (taskPriority.equals(sLabel[0])) {
                priorityDescription = sLabel[1];
                break;
            }
        }
        return priorityDescription;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String[] getUserSpecificRemarks() {
		return userSpecificRemarks;
	}

	public void setUserSpecificRemarks(String[] userSpecificRemarks) {
		this.userSpecificRemarks = userSpecificRemarks;
	}

	public String[][] getUserList() {
		return userList;
	}

	public void setUserList(String[][] userList) {
		this.userList = userList;
	}

	public int getTotalAssignee() {
		return totalAssignee;
	}

	public void setTotalAssignee(int totalAssignee) {
		this.totalAssignee = totalAssignee;
	}

	public double getTotalMandaysEstimated() {
		return totalMandaysEstimated;
	}

	public void setTotalMandaysEstimated(double totalMandaysEstimated) {
		this.totalMandaysEstimated = totalMandaysEstimated;
	}

	public double getTotalMandaysSpent() {
		return totalMandaysSpent;
	}

	public void setTotalMandaysSpent(double totalMandaysSpent) {
		this.totalMandaysSpent = totalMandaysSpent;
	}

	public double[] getTotalMandaysSpentEachUser() {
		return totalMandaysSpentEachUser;
	}

	public void setTotalMandaysSpentEachUser(double[] totalMandaysSpentEachUser) {
		this.totalMandaysSpentEachUser = totalMandaysSpentEachUser;
	}
	public double getEstimation() {
		return estimation;
	}

	public void setEstimation(double estimation) {
		this.estimation = estimation;
	}

	public double getEstimationMandays() {

		if("Manhours".equals(estimationType))
			return estimation/8;
		else
			return estimation;
	}

	public void setEstimationMandays(double estimationMandays) {
		this.estimationMandays = estimationMandays;
	}

	public String getEstimationType() {
		return estimationType;
	}

	public void setEstimationType(String estimationType) {
		this.estimationType = estimationType;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
		if(projectId==null)
        	isproject="No";
        else
        	isproject="Yes";
	}

	public String getIsproject() {
		return isproject;
	}

	public void setIsproject(String isproject) {
		this.isproject = isproject;
	}

	public Date getActEndDate() {
		return actEndDate;
	}

	public void setActEndDate(Date actEndDate) {
		this.actEndDate = actEndDate;
	}

	public Date getActStartDate() {
		return actStartDate;
	}

	public void setActStartDate(Date actStartDate) {
		this.actStartDate = actStartDate;
	}

	public String getActualDuration() {
		return actualDuration;
	}

	public void setActualDuration(String actualDuration) {
		this.actualDuration = actualDuration;
	}

	public String getActualEndDate() {
		SimpleDateFormat end = new SimpleDateFormat("ddMMMyyyy");
		if("Not Started".equals(getActualStartDate())){
			return"Not Started";
		}else{
		if(actEndDate!=null)
		return end.format(actEndDate);
		else
			return"Ongoing";
		}		
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getActualStartDate() {
		SimpleDateFormat start = new SimpleDateFormat("ddMMMyyyy");
		if(actStartDate!=null)
		return start.format(actStartDate);
		else
			return"Not Started";
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getEndVariance() {
		return endVariance;
	}

	public void setEndVariance(String endVariance) {
		this.endVariance = endVariance;
	}

	public String getStartVariance() {
		return startVariance;
	}

	public void setStartVariance(String startVariance) {
		this.startVariance = startVariance;
	}

	public String getVariance() {
		return variance;
	}

	public void setVariance(String variance) {
		this.variance = variance;
	}
	
		
}
