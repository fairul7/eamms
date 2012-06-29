package com.tms.collab.emeeting;

import org.apache.commons.collections.SequencedHashMap;

import java.util.Map;
import java.util.Collection;
import java.io.Serializable;

import com.tms.collab.taskmanager.model.Task;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Sep 22, 2003
 * Time: 11:26:49 AM
 * To change this template use Options | File Templates.
 */
public class AgendaItem implements Serializable
{
    public static final String NO_PARENT = "-1";

    private String eventId;
    private String itemId;
    private String parentId;
    private String title;
    private String notes;
    private String action;
    private int itemOrder;
    private Map children;
    private String taskId;
    private Task task;
    private Collection tasks;

    public AgendaItem()
    {
        children = new SequencedHashMap();
    }

    public void addChild(AgendaItem item)
    {
        children.put(item.getItemId(), item);
    }

    public AgendaItem getChild(String itemId)
    {
        return (AgendaItem) children.get(itemId);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getItemId()
    {
        return itemId;
    }

    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }

    public Map getChildren()
    {
        return children;
    }

    public void setChildren(Map children)
    {
        this.children = children;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public int getItemOrder()
    {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder)
    {
        this.itemOrder = itemOrder;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public Collection getTasks() {
        return tasks;
    }

    public void setTasks(Collection tasks) {
        this.tasks = tasks;
    }
}
