package com.tms.collab.taskmanager.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoQuery;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.Form;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.tms.collab.taskmanager.model.TaskManager;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 9, 2003
 * Time: 11:54:15 AM
 * To change this template use Options | File Templates.
 */
public class TaskView extends Form
{
    public final static int VIEW_CURRENT = 0;
    public final static int VIEW_ALL = 1;
    public final static int VIEW_COMPLETED = 2;
    public final static int VIEW_TODAY = 3;
    public final static int VIEW_CATEGORY = 4;
    public final static int VIEW_CATEGORIES = 5;
    public final static String PARAMETER_KEY_VIEW_CATEGORY = "category";
    public final static String PARAMETER_KEY_CATEGORYID = "categoryId";
    private int view = 0;
    private String popupViewUrl = null;
    private String categoryId = null;
    private Collection categories = null;
    private String userId;
    private TaskListing taskList = null;
    private SelectBox viewSB;
    private SelectBox userSB;
    private SelectBox pageSizeSB;
/*
    private Button addCatButton;
    private Button catButton;
*/
    private TaskCategoryTable catTable;
    public TaskView()
    {
    }

    public TaskView(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        userId = getWidgetManager().getUser().getId();
        taskList = new TaskListing("TaskListing");
        taskList.init();
        pageSizeSB = new SelectBox("pagesizesb");
        pageSizeSB.setOptions(Table.PAGE_SIZE_OPTIONS);
        pageSizeSB.setOnChange("javascript:submit();");
        List selectedList = new ArrayList();
        selectedList.add(new Integer(taskList.getPageSize()).toString());
        pageSizeSB.setValue(selectedList);
        viewSB = new SelectBox("viewsb");
        viewSB.addOption("0","View Current");
        viewSB.addOption("1","View All");
        viewSB.addOption("2","View Completed");
        viewSB.addOption("3","View Today");
        viewSB.setOnChange("javascript:change();");
        //viewSB.setAttribute("onClick","clearState()");
     /*   viewSB.setOnFocus("clearState();");*/
        userSB = new SelectBox("usersb");
        userSB.setOnChange("submit();");
        try
        {
            Collection col;
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            col = ss.getUsers(new DaoQuery(),0,-1,"username",false);
            userSB.setOptions(col,"id","username");
            userSB.setSelectedOptions(new String[]{userId});
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);
        }
/*        addCatButton = new Button("addCAT");
        addCatButton.setText("Add Category");
        catButton = new Button("categoryB");
        catButton.setText("View Category");*/
        userId = getWidgetManager().getUser().getId();
        taskList.setUserId(userId);
        if(popupViewUrl!=null&&popupViewUrl.trim().length()>0)
            taskList.setPopupViewUrl(popupViewUrl);
        catTable = new TaskCategoryTable("cateTable");
        catTable.init();
        addChild(catTable);
        addChild(taskList);
        addChild(viewSB);
        addChild(userSB);
 /*       addChild(addCatButton);
        addChild(catButton);*/
        addChild(pageSizeSB);
        try
        {
            setView(VIEW_CURRENT);
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
    }


    public Forward actionPerformed(Event evt)
    {
        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if(PARAMETER_KEY_VIEW_CATEGORY.equals(eventType)){
            categoryId = evt.getRequest().getParameter(PARAMETER_KEY_CATEGORYID);
            try
            {
                setView(VIEW_CATEGORY);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return null;
        }
        return super.actionPerformed(evt);
    }

    public Forward onValidate(Event evt)
    {
/*
        String buttonClicked = findButtonClicked(evt);
*/
            Iterator i =userSB.getSelectedOptions().keySet().iterator();
            if(i.hasNext()){
                String tempId = (String)i.next();
                if(!userId.equals(tempId)){
                    userId = tempId;
                    taskList.setUserId(userId);
                    taskList.setCurrentPage(1);
                    taskList.setView(getView());
                    try
                    {
                        setView(getView());
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                    return null;
                }
            }
            i = pageSizeSB.getSelectedOptions().keySet().iterator();
            if(i.hasNext()){
                int tempSize = Integer.parseInt((String)i.next());
                if(tempSize!=taskList.getPageSize()){
                    taskList.setPageSize(tempSize);
                    taskList.setCurrentPage(1);
                    try
                    {
                        taskList.refresh();
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                    return null;
                }
            }
            i = viewSB.getSelectedOptions().keySet().iterator();
            if(i.hasNext()){
                int tempView =Integer.parseInt((String)i.next());
                if(tempView!=getView()){
                    try
                    {
                        setView(tempView);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }


        return null;
    }

   /* public void onRequest(Event evt)
    {
        String tempUserId = getWidgetManager().getUser().getId();
        if(!tempUserId.equals(userId)){
            userId = tempUserId;
            taskList.setUserId(userId);
        }


    }
*/



    public String getDefaultTemplate()
    {
        return "taskmanager/taskview";
    }

    public int getView()
    {
        return view;
    }

    public void setView(int view) throws DaoException
    {
        this.view = view;
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        switch(view){
            case 0 :  //view current
                categories = tm.getCategories(userId,false);
                break;
            case 1 : //view all
            case 2 :
                taskList.setUserId(userId);
                taskList.setCategoryId(null);
                taskList.setSort("description");
                break;
           case 3: //view today
                taskList.setUserId(userId);
                taskList.setSort("description");
                break;
           case 4:  //view category
                categories = tm.getCategories(userId,false);
                taskList.setUserId(userId);
                taskList.setCategoryId(categoryId);
                break;
        }
        if(this.view != VIEW_CATEGORIES){
            taskList.setView(view);
            taskList.refresh();
            if(taskList.getTasks().size()<=0 && view == VIEW_CATEGORY){
                setView(0);
            }
        }
    }

    public Collection getCategories()
    {
        return categories;
    }

    public void setCategories(Collection categories)
    {
        this.categories = categories;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public TaskListing getTaskList()
    {
        return taskList;
    }

    public void setTaskList(TaskListing taskList)
    {
        this.taskList = taskList;
    }

    public SelectBox getViewSB()
    {
        return viewSB;
    }

    public void setViewSB(SelectBox viewSB)
    {
        this.viewSB = viewSB;
    }

    public SelectBox getUserSB()
    {
        return userSB;
    }

    public void setUserSB(SelectBox userSB)
    {
        this.userSB = userSB;
    }

    /*public Button getAddCatButton()
    {
        return addCatButton;
    }

    public void setAddCatButton(Button addCatButton)
    {
        this.addCatButton = addCatButton;
    }

    public Button getCatButton()
    {
        return catButton;
    }*/

  /*  public void setCatButton(Button catButton)
    {
        this.catButton = catButton;
    }*/

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public SelectBox getPageSizeSB()
    {
        return pageSizeSB;
    }

    public void setPageSizeSB(SelectBox pageSizeSB)
    {
        this.pageSizeSB = pageSizeSB;
    }

    public TaskCategoryTable getCatTable()
    {
        return catTable;
    }

    public void setCatTable(TaskCategoryTable catTable)
    {
        this.catTable = catTable;
    }

    public String getPopupViewUrl()
    {
        return popupViewUrl;
    }

    public void setPopupViewUrl(String popupViewUrl)
    {
        this.popupViewUrl = popupViewUrl;

    }

}
