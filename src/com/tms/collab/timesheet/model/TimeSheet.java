package com.tms.collab.timesheet.model;

import kacang.model.DefaultDataObject;
import kacang.Application;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 10:59:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheet extends DefaultDataObject {
    private String id;
    private Date createdDateTime;
    private String projectId;
    private String taskId;
    private String createdBy;
    private Date tsDate;
    private double duration;
    private String description;
    private String adjustment;
    private Date adjustmentDateTime;
    private String taskName;
    private String categoryName;
    private String taskCategoryName;
    private String taskTitle;

    public String getAdjustmentById() {
        return adjustmentById;
    }

    public void setAdjustmentById(String adjustmentById) {
        this.adjustmentById = adjustmentById;
    }

    private String adjustmentById;
    private String adjustmentBy;
    private String adjustmentDescription;
    private double adjustedDuration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getTsDate() {
        return tsDate;
    }

    public void setTsDate(Date tsDate) {
        this.tsDate = tsDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    public Date getAdjustmentDateTime() {
        return adjustmentDateTime;
    }

    public void setAdjustmentDateTime(Date adjustmentDateTime) {
        this.adjustmentDateTime = adjustmentDateTime;
    }

    public String getAdjustmentBy() {
        return adjustmentBy;
    }

    public void setAdjustmentBy(String adjustmentBy) {
        this.adjustmentBy = adjustmentBy;
    }

    public String getAdjustmentDescription() {
        return adjustmentDescription;
    }

    public void setAdjustmentDescription(String adjustmentDescription) {
        this.adjustmentDescription = adjustmentDescription;
    }

    public double getAdjustedDuration() {
        return adjustedDuration;
    }

    public void setAdjustedDuration(double adjustedDuration) {
        this.adjustedDuration = adjustedDuration;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        if (taskName==null || taskName.equals(""))
            return Application.getInstance().getMessage("timesheet.message.unknowntask")+": "+taskTitle;
        else
            return taskName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        if (categoryName==null || categoryName.equals(""))
            return taskCategoryName;
        else
            return categoryName;
    }

    public void setTaskCategoryName(String taskCategoryName) {
        this.taskCategoryName = taskCategoryName;
    }

    public String getTaskCategoryName() {
        return taskCategoryName;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle=taskTitle;
    }

    public String getTaskTitle() {
        return taskTitle;
    }
}
