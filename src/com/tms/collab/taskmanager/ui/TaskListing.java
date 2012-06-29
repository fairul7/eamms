package com.tms.collab.taskmanager.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Table;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Button;

import java.util.Collection;
import java.util.Date;

import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskMailer;
import com.tms.collab.calendar.model.CalendarException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 8, 2003
 * Time: 3:50:20 PM
 * To change this template use Options | File Templates.
 */
public class TaskListing extends Form
{

    public final static String PARAMETER_EVENT_COMPLETE = "complete";
    public final static String PARAMETER_EVENT_COMPLETE_TASKID = "taskId";
    public final static String PARAMETER_EVENT_USERID = "userId";
    private Collection tasks = null;
    private String categoryId=null;
    private String category=null;
    private String userId=null;
    private int pageSize=3;
    private int currentPage=1;
    private int rows=3,totalRows;
    private boolean desc = false;
    private String sort = null;
    private int view = 0;
    private String popupViewUrl = null;
    public CheckBox sel;
    public Button deleteButton;
    public TaskListing()
    {
    }



    public TaskListing(String name)
    {
        super(name);
        currentPage = 1;
        pageSize = 5;
        rows = 5;
    }

    public void init()
    {
        super.init();
        sel = new CheckBox("selcheckbox");
        deleteButton = new Button("deletebutton",Application.getInstance().getMessage("general.label.delete","Delete"));
        addChild(deleteButton);
        addChild(sel);
    }

    public String getDefaultTemplate()
    {
        return "taskmanager/tasklisting";
    }



    public Collection getTasks()
    {
        return tasks;
    }

    public void setTasks(Collection tasks)
    {
        this.tasks = tasks;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;

    }

    public Forward onValidate(Event evt)
    {
        String[] ids = evt.getRequest().getParameterValues(sel.getAbsoluteName());
        if(deleteButton.getAbsoluteName().equals(findButtonClicked(evt))&&ids!=null){
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            for(int i=0;i<ids.length;i++){
                try
                {
                    tm.deleteTask(ids[i]);
                } catch (CalendarException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
            try
            {
                refresh();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            int total = (getCurrentPage()-1) * pageSize;
            while(totalRows<=total){
                setCurrentPage(getCurrentPage()-1);
                total =  (getCurrentPage()-1) * pageSize;
            }
            setRows(getPageSize());
            try
            {
                refresh();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }

        }
        return super.onValidate(evt);
    }



    public void refresh() throws DaoException
    {
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        if(view == TaskView.VIEW_CURRENT||view == TaskView.VIEW_CATEGORY){
            tasks = tm.getTasks(null,userId,categoryId,false,getStartIndex(),getRows(),getSort(),isDesc());
            totalRows = tm.getTasks(null,userId,categoryId,false,0,-1,null,false).size();
        }else if(view == TaskView.VIEW_ALL){
            tasks = tm.getAllTasks(null,userId,categoryId,getStartIndex(),getRows(),getSort(),isDesc());
            totalRows = tm.getAllTasks(null,userId,categoryId,0,-1,null,false).size();
        }else if(view == TaskView.VIEW_COMPLETED){
            tasks = tm.getTasks(null,userId,categoryId,true,getStartIndex(),getRows(),getSort(),isDesc());
            totalRows = tm.getTasks(null,userId,categoryId,true,0,-1,null,false).size();
        }else if(view == TaskView.VIEW_TODAY){
            tasks = tm.getTasksByDate(new Date(),userId,getStartIndex(),getRows(),getSort(),isDesc());
            totalRows = tm.getTasksByDate(new Date(),userId,0,-1,getSort(),isDesc()).size();
        }
       // else if()

        /*
                    */
        if(categoryId!=null&&tasks.size()>0)
            category =((Task)tasks.iterator().next()).getCategory();
    }

    public Forward actionPerformed(Event evt)
    {
        String type = evt.getType();
        if(Table.PARAMETER_KEY_PAGE.equals(type)){
            int page = Integer.parseInt(evt.getRequest().getParameter("page"));
            currentPage = page;
            try
            {
                refresh();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }else if(Table.PARAMETER_KEY_SORT.equals(type)){
            String sort = evt.getRequest().getParameter(Table.PARAMETER_KEY_SORT);
            String descStr = evt.getRequest().getParameter(Table.PARAMETER_KEY_DESC);
            boolean descending = Boolean.valueOf(descStr).booleanValue();
            // set desired sorting
            if (getSort() == null || !getSort().equals(sort))
                setDesc(false);
            else
                setDesc(descending);
            setSort(sort);
            // reset to first page
            setCurrentPage(1);
            setSort(sort);
            try
            {
                refresh();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }else if(PARAMETER_EVENT_COMPLETE.equals(type)){
            String taskId = evt.getRequest().getParameter(PARAMETER_EVENT_COMPLETE_TASKID);
            if(taskId!=null&&taskId.trim().length()>0){
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                try
                {
                    tm.completeTask(taskId,getWidgetManager().getUser().getId());
                    TaskMailer.sentTaskCompletedNotification(tm.getTask(taskId),evt,getWidgetManager().getUser().getId());
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                } catch (DataObjectNotFoundException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
                try
                {
                    refresh();
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
        }
        return super.actionPerformed(evt);
    }


    public Form getRootForm()
    {
        return super.getRootForm();
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
        rows = pageSize;
    }

    public int getPageCount(){
        try {
            int pageCount = (int) Math.ceil(((double) totalRows) / getPageSize());
            return (pageCount > 0) ? pageCount : 1;
        } catch (Exception e) {
            return 1;
        }
    }

    public int getStartIndex() {
        return (getCurrentPage() - 1) * getPageSize();
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public int getTotalRows()
    {
        return totalRows;
    }

    public void setTotalRows(int totalRows)
    {
        this.totalRows = totalRows;
    }

    public boolean isDesc()
    {
        return desc;
    }

    public void setDesc(boolean desc)
    {
        this.desc = desc;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public int getView()
    {
        return view;
    }

    public void setView(int view)
    {
        this.view = view;
    }

    public String getPopupViewUrl()
    {
        return popupViewUrl;
    }

    public void setPopupViewUrl(String popupViewUrl)
    {
        this.popupViewUrl = popupViewUrl;
    }

    public CheckBox getSel()
    {
        return sel;
    }

    public void setSel(CheckBox sel)
    {
        this.sel = sel;
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton)
    {
        this.deleteButton = deleteButton;
    }
}
