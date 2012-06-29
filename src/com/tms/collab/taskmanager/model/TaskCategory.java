package com.tms.collab.taskmanager.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 8, 2003
 * Time: 5:00:19 PM
 * To change this template use Options | File Templates.
 */
public class TaskCategory extends DefaultDataObject implements Comparable
{
    private String name;
    private String description;
    private String userId;
    private boolean general;

    public TaskCategory()
    {
    }

    public TaskCategory(String name)
    {
        this.name = name;
    }

    public TaskCategory(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public boolean isGeneral()
    {
        return general;
    }

    public void setGeneral(boolean general)
    {
        this.general = general;
    }

    public int compareTo(Object o) {
        TaskCategory category = (TaskCategory) o;
      //  boolean general = category.isGeneral() ;
        if(isGeneral()){
            if(category.isGeneral())
                return name.compareTo(category.getName());
            else
                return -1;
        } else{
            if(category.isGeneral())
                return +1;
            else
                return name.compareTo(category.getName());
        }
//        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
