package com.tms.collab.project.ui;

import com.tms.collab.project.Project;
import com.tms.collab.project.ProjectMember;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import com.tms.portlet.Portlet;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

public class ProjectTable extends Table
{
    public static final String FORWARD_ADD = "project_add";
    public static final String FORWARD_DELETE = "project_delete";

    ProjectTableModel model;

    public void init()
    {
        User user = getWidgetManager().getUser();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try
        {
            if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_VIEW, WormsHandler.class.getName(), null)||service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null))
            {
                super.init();
                setWidth("100%");
                model = new ProjectTableModel();
                setModel(model);
            }
        }
        catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        model.refresh();
    }

    public class ProjectTableModel extends TableModel
    {
        public ProjectTableModel()
        {
            User user = getWidgetManager().getUser();
            /* Table Columns */
            TableColumn colName = new TableColumn("projectName", Application.getInstance().getResourceBundle().getString("project.label.name"));
            TableColumn colType = new TableColumn("projectCurrencyType", Application.getInstance().getResourceBundle().getString("project.label.projectCurrencyType"));
            TableColumn colValue = new TableColumn("projectValue", Application.getInstance().getResourceBundle().getString("project.label.projectValue"));
            TableColumn colCategory = new TableColumn("projectCategory", Application.getInstance().getResourceBundle().getString("project.label.category"));
            colValue.setFormat(new TableDecimalFormat("##,###,##0.00"));
            TableColumn colManaged = new TableColumn("ownerName", Application.getInstance().getResourceBundle().getString("project.label.projectOwner"));
            addColumn(colName);
            addColumn(colCategory);
            addColumn(colType);
            addColumn(colValue);
            addColumn(colManaged);
            /* Table Actions */
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try
            {
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADD, WormsHandler.class.getName(), null)||service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null))
                    addAction(new TableAction("add", Application.getInstance().getResourceBundle().getString("project.label.add")));
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_DELETE, WormsHandler.class.getName(), null)||service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null))
                    addAction(new TableAction("delete", Application.getInstance().getResourceBundle().getString("project.label.delete")));
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_EDIT, WormsHandler.class.getName(), null)||service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null))
                    colName.setUrlParam("projectId");
                
                /* Table Filters */
                TableFilter filterOwner = new TableFilter("owner");
                if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null)){           
                SelectBox selectOwner = new SelectBox("selectOwner");
                selectOwner.addOption(user.getId(), Application.getInstance().getResourceBundle().getString("project.label.myProjects"));
                selectOwner.addOption("-1", Application.getInstance().getResourceBundle().getString("project.label.allProjects"));
                selectOwner.setSelectedOption(user.getId());
                filterOwner.setWidget(selectOwner);}
                TableFilter filterCategory = new TableFilter("category");
                SelectBox selectCategory = new SelectBox("selectCategory");
                selectCategory.setSelectedOptions(new String[] {"-1"});
                filterCategory.setWidget(selectCategory);
    			TableFilter filterArchive = new TableFilter("archived");
    			SelectBox selectArchived = new SelectBox("selectArchived");
                selectArchived.addOption("0", Application.getInstance().getMessage("project.label.activeProjects","Active Projects"));
                selectArchived.addOption("1", Application.getInstance().getMessage("project.label.archivedProjects","Archived Projects"));
    			selectArchived.addOption("-1", Application.getInstance().getMessage("project.label.filterArchived","View All"));
    			selectArchived.setSelectedOption("0");
    			filterArchive.setWidget(selectArchived);
                addFilter(new TableFilter("search"));
                addFilter(filterCategory);
    			addFilter(filterArchive);
    			if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null)){
                addFilter(filterOwner);}
            }
            catch(SecurityException e)
            {
                Log.getLog(ProjectTableModel.class).error(e.getMessage(), e);
            }
            
        }

        public Collection getTableRows()
        {
            WormsHandler worm = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Collection list = new ArrayList();
            DaoQuery query = generateQuery();
            try
            {
				String sort = getSort();
				if("ownerName".equals(sort))
					sort = "ownerId";
                if(sort==null || "".equals(sort)){
                    sort = "modifiedDate";
                }
				list = worm.getProjects(query, getStart(), getRows(), sort, isDesc());
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return list;
        }

        public int getTotalRowCount()
        {
            WormsHandler worm = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            int count = 0;
            DaoQuery query = generateQuery();
            try
            {
                count = worm.getProjectsCount(query);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return count;
        }

        public String getTableRowKey()
        {
            return "projectId";
        }

        private DaoQuery generateQuery()
        {
        	SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        	
            DaoQuery query = new DaoQuery();
            if(!("".equals(getFilterValue("search"))))
            {
                OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                parenthesis.addOperator(new OperatorLike("projectName", getFilterValue("search"), null));
                parenthesis.addOperator(new OperatorLike("projectDescription", getFilterValue("search"), DaoOperator.OPERATOR_OR));
                query.addProperty(parenthesis);
            }
            String category = (String) ((SelectBox)getFilter("category").getWidget()).getSelectedOptions().keySet().iterator().next();
            if(!("-1".equals(category)))
                query.addProperty(new OperatorEquals("projectCategory", category, DaoOperator.OPERATOR_AND));
            
            try
            {
            User user = getWidgetManager().getUser();
            if(service.hasPermission(user.getId(), WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null)){
            	String owner = (String) ((SelectBox)getFilter("owner").getWidget()).getSelectedOptions().keySet().iterator().next();
            if(!("-1".equals(owner))){
            	OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                parenthesis.addOperator(new OperatorEquals("ownerId", owner, null));
                parenthesis.addOperator(new OperatorEquals("memberId", owner, DaoOperator.OPERATOR_OR));
                query.addProperty(parenthesis);
            }
            }else{
            	OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                parenthesis.addOperator(new OperatorEquals("ownerId", user.getId(), null));
                parenthesis.addOperator(new OperatorEquals("memberId", user.getId(), DaoOperator.OPERATOR_OR));
                query.addProperty(parenthesis);
            }
            }
            catch(SecurityException e)
            {
                Log.getLog(ProjectTableModel.class).error(e.getMessage(), e);
            }
			String archived = (String) ((SelectBox)getFilter("archived").getWidget()).getSelectedOptions().keySet().iterator().next();
			if(!("-1".equals(archived)))
				query.addProperty(new OperatorEquals("archived", archived, DaoOperator.OPERATOR_AND));
            return query;
        }

        public Forward processAction(Event event, String action, String[] selectedKeys)
        {
            Forward forward = null;
            if("add".equals(action))
                forward = new Forward(ProjectTable.FORWARD_ADD);
            else if("delete".equals(action))
            {
                WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                String userId = getWidgetManager().getUser().getId();
                try
                {
                	if(ss.hasPermission(userId, WormsHandler.PERMISSION_PROJECTS_ADMINISTER, WormsHandler.class.getName(), null)){
                    for(int i = 0; i < selectedKeys.length; i++)
                        handler.deleteProject(selectedKeys[i]);
                	}else if(ss.hasPermission(userId, WormsHandler.PERMISSION_PROJECTS_DELETE, WormsHandler.class.getName(), null)){
                		int check=0;
                		for(int i = 0; i < selectedKeys.length; i++){
                			Project project = handler.getProject(selectedKeys[i]);
                			if(!(project.getOwnerId().equals(userId))){
                				check++;	
                				break;
                			}    

                			int check2=0;
                	        for(Iterator j = project.getMembers().iterator(); j.hasNext();)
                	        {
                	            ProjectMember pm = (ProjectMember) j.next();
                	            if((pm.getMemberId().equals(userId))){
                	            	check2++;
                	            	break;
                	            }
                	        }
                	        if(check2==0)
                	        {	
                	        	check++;
                	        	break;
                	        }
                			
                		}
                		if(check==0){
                			for(int i = 0; i < selectedKeys.length; i++)
                                handler.deleteProject(selectedKeys[i]);	
                		}else{
                			return new Forward("noDelete");
                		}
                	}
                }
                catch(Exception e)
                {
                    Log.getLog(getClass()).error(e);
                }
                forward = new Forward(ProjectTable.FORWARD_DELETE);
            }
            return forward;
        }

        protected void refresh()
        {
            try
            {
                TableFilter filter = getFilter("category");
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Collection list = worms.getProjectCategories();
                SequencedHashMap map = new SequencedHashMap();
                map.put("-1", Application.getInstance().getResourceBundle().getString("project.label.allCategories"));
                for (Iterator i = list.iterator(); i.hasNext();)
                {
                    String category = (String) i.next();
                    map.put(category, category);
                }
                ((SelectBox)filter.getWidget()).setOptionMap(map);
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }
}
