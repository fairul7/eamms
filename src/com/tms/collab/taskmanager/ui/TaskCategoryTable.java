package com.tms.collab.taskmanager.ui;

import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Collection;
import java.util.List;

import com.tms.collab.taskmanager.model.TaskManager;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 15, 2003
 * Time: 5:23:48 PM
 * To change this template use Options | File Templates.
 */
public class TaskCategoryTable extends Table
{
    public TaskCategoryTable()
    {
    }

    public TaskCategoryTable(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setModel(new TaskCategoryTableModel());
        setMultipleSelect(true);
        setNumbering(true);
        setWidth("100%");
    }

    public class TaskCategoryTableModel extends TableModel{
        private int totalRows;
        private String userId;
        public TaskCategoryTableModel()
        {
            setSort("name");
            setDesc(false);
            setShowPageSize(true);
            TableColumn nameC = new TableColumn("name",Application.getInstance().getMessage("taskmanager.label.Name","Name"),true);
            nameC.setUrl("");
            nameC.setUrlParam("id");
            addColumn(nameC);
            addColumn(new TableColumn("description",Application.getInstance().getMessage("taskmanager.label.Description","Description"),true));
            addAction(new TableAction("delete",Application.getInstance().getMessage("taskmanager.label.Delete","Delete")));
            TableFilter categoryFilter = new TableFilter("categoryFilter");
            SelectBox completeBox = new SelectBox("completeBox");
            completeBox.addOption("-1",Application.getInstance().getMessage("taskmanager.label.viewAllCategories","View All Categories"));
            completeBox.addOption("0",Application.getInstance().getMessage("timesheet.label.selectprojectTask","Project Task Category"));
            completeBox.addOption("1",Application.getInstance().getMessage("timesheet.menu.nonprojectTask","Non Project Tasks"));
            completeBox.setSelectedOption("1");
            categoryFilter.setWidget(completeBox);
            addFilter(new TableFilter("name"));
            addFilter(categoryFilter);
        }

        public Collection getTableRows()
        {
            String catName = (String) getFilterValue("name");
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            userId = getWidgetManager().getUser().getId();
            List l = (List)getFilterValue("categoryFilter");
            String completeFilter = null;
            if(l!=null&&l.size()>0)
                completeFilter = (String)l.get(0);
            if(completeFilter==null||completeFilter.trim().length()<=0)
                completeFilter = "1";
            
            if(catName != null && !"".equals(catName)){
            	if(completeFilter.equals("-1")){
            		totalRows = tm.countUserCategories(userId, catName, true, getStart(), getRows());
            		return tm.getUserCategories(userId, catName, true, getSort(), isDesc(), getStart(), getRows());
            	}else if(completeFilter.equals("0")){
            		totalRows = tm.countUserProjectCategories(userId, catName, true, getStart(), getRows());
                    return tm.getUserProjectCategories(userId, catName, true, getSort(), isDesc(), getStart(), getRows());	
            	}else if(completeFilter.equals("1")){
            		totalRows = tm.countUserNonProjectCategories(userId, catName, true, getStart(), getRows());
                    return tm.getUserNonProjectCategories(userId, catName, true, getSort(), isDesc(), getStart(), getRows());	
            	}
            }else{
                try
                {
                	if(completeFilter.equals("-1")){
                    //boolean hasPermission = ss.hasPermission(userId,TaskManager.PERMISSION_MANAGETASK,null,null);
                		totalRows = tm.countCategories(getWidgetManager().getUser().getId(),true);
                		return tm.getUserCategories(userId,true,getSort(),isDesc(),getStartIndex(),getRows());
                    //totalRows =  tm.getUserCategories(userId,true,getSort(),isDesc(),0,-1).size();
                	}else if(completeFilter.equals("0")){
                		totalRows = tm.countProjectCategories(getWidgetManager().getUser().getId(),true);
                        return tm.getUserProjectCategories(userId,true,getSort(),isDesc(),getStartIndex(),getRows());
                	}else if(completeFilter.equals("1")){
                		totalRows = tm.countNonProjectCategories(getWidgetManager().getUser().getId(),true);
                        return tm.getUserNonProjectCategories(userId,true,getSort(),isDesc(),getStartIndex(),getRows());
                	}
                } catch (Exception e){
                	Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            return null;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            if("delete".equals(action)){
            	userId = getWidgetManager().getUser().getId();
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                int check=0;
                try
                {
                for(int i=0;i<selectedKeys.length;i++){
                	if(tm.getUserCategory(selectedKeys[i],userId)==null||!ss.hasPermission(userId,TaskManager.PERMISSION_MANAGETASK,null,null)){
                		check++;
                		break;
                	}
                    if(tm.countTasksByCategory(selectedKeys[i])==0){
                    	if("1".equals(selectedKeys[i])){
                    		check++;
                    		break;
                    	}
                    }else{
                    		check++;
                    		break;
                    }
                    } 
                if(check==0){
                for(int i=0;i<selectedKeys.length;i++){
                	
                if(!"1".equals(selectedKeys[i]))
                    tm.deleteCategory(selectedKeys[i]);
                }
                }else{
                	return new Forward("noDelete");
                }
                }
                
                catch (Exception e)
                {
                	Log.getLog(getClass()).error(e.toString(), e);  //To change body of catch statement use Options | File Templates.
                }
                }
            return super.processAction(evt,action,selectedKeys);
        }

        public int getTotalRowCount()
        {
            return totalRows;
        }

        public String getTableRowKey()
        {
            return "id";
        }
    }
}
