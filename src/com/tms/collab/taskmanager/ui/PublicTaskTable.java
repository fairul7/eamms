package com.tms.collab.taskmanager.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.ui.TaskTable.TaskTableModel;
import com.tms.util.FormatUtil;

public class PublicTaskTable extends Table
{
    private String userId;
    private String categoryId = null;

    public void init()
    {
        super.init();
        setNumbering(true);
        setModel(new PublicTaskTableModel());
        setShowPageSize(true);
        setWidth("100%");
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(getModel()==null)
            setModel(new PublicTaskTableModel());
        if(userId==null)
            userId = getWidgetManager().getUser().getId();
        SelectBox cat = ((PublicTaskTableModel)getModel()).getCatlist();
        if(cat!=null){
            try
            {
                ((PublicTaskTableModel)getModel()).initCategories();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public class PublicTaskTableModel extends TableModel{
        private int totalRows;
        private SelectBox catlist;

        public PublicTaskTableModel()
        {
            TableColumn statusCol = new TableColumn("completed",Application.getInstance().getMessage("taskmanager.label.Status","Status"));
            statusCol.setFormat(new TableBooleanFormat("<FONT COLOR=\"#3130FF\">&#149;</FONT>","<FONT COLOR=\"#FF0000\">&#149;</FONT>"));
            addColumn(statusCol);
            TableColumn desCol = new TableColumn("title",Application.getInstance().getMessage("taskmanager.label.Task","Task"),true);
            desCol.setUrl("");
            addColumn(desCol);
            addColumn(new TableColumn("category",Application.getInstance().getMessage("taskmanager.label.Category","Category"),true));
            addColumn(new TableColumn("assigner",Application.getInstance().getMessage("taskmanager.label.AssignedBy","Assigned By"),true));
            addColumn(new TableColumn("assigneeName",Application.getInstance().getMessage("taskmanager.label.AssignedTo","Assigned To"),true));
            TableColumn deadlineCol = new TableColumn("dueDate",Application.getInstance().getMessage("taskmanager.label.Deadline","Deadline"),true);
            deadlineCol.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            deadlineCol.setUrl("");

            addColumn(deadlineCol);
            catlist = new SelectBox("catlist");
            try
            {
                initCategories();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            TableFilter catFilter = new TableFilter("catfilter");
            TableFilter completeFilter = new TableFilter("completeFilter");
            SelectBox completeBox = new SelectBox("completeBox");
            completeBox.addOption("-1",Application.getInstance().getMessage("taskmanager.label.AllTasks","All Tasks"));
            completeBox.addOption("0",Application.getInstance().getMessage("taskmanager.label.Incomplete","Incomplete"));
            completeBox.addOption("1",Application.getInstance().getMessage("taskmanager.label.Completed","Completed"));
            completeFilter.setWidget(completeBox);
            catFilter.setWidget(catlist);
            addFilter(new TableFilter("search"));
            addFilter(catFilter);
            addFilter(completeFilter);
        }

        public void initCategories() throws DaoException
        {
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            Collection col= tm.getUserCategories(getWidgetManager().getUser().getId(),true,"name",false,0,-1);
            if(catlist == null){
                catlist = new SelectBox("catlist");
            }
            else if(catlist.getOptionMap().size()>0)
                catlist.removeAllOptions();
            catlist.addOption("-1",Application.getInstance().getMessage("taskmanager.label.Allcategories","All categories"));
            for(Iterator i = col.iterator();i.hasNext();){
                TaskCategory cat = (TaskCategory)i.next();
                catlist.addOption(cat.getId(),cat.getName());
            }
        }

        public SelectBox getCatlist(){
            return catlist;
        }


        public Collection getTableRows()
        {
            if(userId==null)
                userId = getWidgetManager().getUser().getId();
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            List tempList = (List)getFilterValue("catfilter");
            if(tempList!=null&&tempList.size()>0)
                categoryId = (String)tempList.get(0);
            if(categoryId!=null&&categoryId.equals("-1"))
                categoryId = null;
            String filter = (String)getFilterValue("search");
            String sort = getSort();
            if (sort!=null&&sort.equals("assigneeName")){
                setSort("assigneeFirst");
            }
            else if (sort == null) {
                setSort("dueDate");
                setDesc(true);
            }
            List l = (List)getFilterValue("completeFilter");
            String completeFilter = null;
            if(l!=null&&l.size()>0)
                completeFilter = (String)l.get(0);
            if(completeFilter==null||completeFilter.trim().length()<=0)
                completeFilter = "-1";
            if(completeFilter.equals("-1")){
                try
                {
                    totalRows = tm.getAllPublicTasks(filter,userId,categoryId,0,-1,null,isDesc()).size();
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
                try
                {
                    return tm.getAllPublicTasks(filter,userId,categoryId,getStartIndex(),getRows(),getSort(),isDesc());
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }else{
                boolean completed;
                if(completeFilter.equals("0"))
                    completed = false;
                else
                    completed = true;
                try
                {
                    totalRows = tm.getPublicTasks(filter,userId,categoryId,completed,0,-1,null,isDesc()).size();
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
                try
                {
                    return tm.getPublicTasks(filter,userId,categoryId,completed,getStartIndex(),getRows(),getSort(),isDesc());
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }

            }
            return null;
        }

        public int getTotalRowCount()
        {
            return totalRows;
        }
    }

}




