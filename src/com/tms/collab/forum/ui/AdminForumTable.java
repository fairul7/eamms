package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumException;
import com.tms.collab.forum.model.ForumModule;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminForumTable extends Table
{

    public AdminForumTable()
    {
        super();
    }

    public AdminForumTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();

        setPageSize(10);
        setModel(new AdminForumTable.AdminForumTableModel());
        setWidth("100%");
    }

    class AdminForumTableModel extends TableModel
    {
        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ action = " + action);
            try
            {
                if(action.equalsIgnoreCase("delete"))
                {
                    ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                    for (int i = 0; i < selectedKeys.length; i++)
                    {
                        forumModule.deleteForum(selectedKeys[i], evt.getWidgetManager().getUser().getId(), true);
                    }
                }
            }
            catch (ForumException e)
            {
                log.error(e.getMessage(), e);
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public AdminForumTableModel()
        {
	        Application application = Application.getInstance();
	        SecurityService service = (SecurityService) application.getService(SecurityService.class);
	        TableColumn tcName = new TableColumn("name", application.getMessage("forum.label.forumName", "Forum Name"));
	        try {
				boolean manageForums = service.hasPermission(getWidgetManager().getUser().getId(), "com.tms.collab.forum.ManageForums", null, null);
				if(manageForums){
				addAction(new TableAction("add", application.getMessage("forum.label.newForum", "New Forum")));
	            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("forum.label.deleteSelectedItems", "Delete selected item(s)?")));
	            tcName.setUrlParam("forumId");
	            }
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
            

            
            
            addColumn(tcName);

            //addColumn(new TableColumn("description", application.getMessage("general.label.description", "Description")));

            TableColumn tcNumOfThread = new TableColumn("numOfThread", application.getMessage("forum.label.topics", "Topics"));
            tcNumOfThread.setUrlParam("id");
            addColumn(tcNumOfThread);

            addColumn(new TableColumn("numOfMessage", application.getMessage("forum.label.messages", "Messages")));
            TableColumn tcIsPublic = new TableColumn("isPublic", application.getMessage("forum.label.public", "Public"));
            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            tcIsPublic.setFormat(booleanFormat);
            addColumn(tcIsPublic);
            TableColumn tcActive = new TableColumn("active", application.getMessage("general.label.active", "Active"));
            tcActive.setFormat(booleanFormat);
            addColumn(tcActive);

            TableColumn tcCreationDate = new TableColumn("creationDate", application.getMessage("forum.label.created", "Created"));
            tcCreationDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcCreationDate);
            TableColumn tcModificationDate = new TableColumn("modificationDate", application.getMessage("forum.label.lastModified", "Last Modified"));
            tcModificationDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcModificationDate);
            TableColumn tcLastPostDate = new TableColumn("lastPostDate", application.getMessage("forum.label.lastPost", "Last Post"));
            tcLastPostDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcLastPostDate);
/*
            TableColumn tcEdit = new TableColumn("", application.getMessage("forum.label.edit", "Edit"));
            tcEdit.setLabel(application.getMessage("forum.label.edit", "Edit"));
            tcEdit.setUrlParam("forumId");
            addColumn(tcEdit);
*/
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria", application.getMessage("forum.label.forumSearch", "Forum Search"));
            addFilter(tfSearchCriteria);

            TableFilter tfSearchBy = new TableFilter("searchBy", application.getMessage("forum.label.searchBy", "Search By"));
            SelectBox selectSearchBy = new SelectBox("selectSearchBy");
            selectSearchBy.addOption("name", application.getMessage("forum.label.forumName", "Forum Name"));
            selectSearchBy.addOption("description", application.getMessage("general.label.description", "Description"));
            selectSearchBy.addOption("ownerId", application.getMessage("forum.label.forumOwner", "Forum Owner"));
            selectSearchBy.setMultiple(false);
            tfSearchBy.setWidget(selectSearchBy);
            addFilter(tfSearchBy);

            TableFilter tfIsActive = new TableFilter("isActive", application.getMessage("general.label.activeStatus", "Active Status"));
            SelectBox selectIsActive = new SelectBox("selectIsActive");
            selectIsActive.addOption(null, application.getMessage("general.label.allStatus", "All Status"));
            selectIsActive.addOption("1", application.getMessage("general.label.active", "Active"));
            selectIsActive.addOption("0", application.getMessage("general.label.inactive", "Inactive"));
            selectIsActive.setMultiple(false);
            tfIsActive.setWidget(selectIsActive);
            addFilter(tfIsActive);
        }

        public String getTableRowKey()
        {
            return "forumId";
        }

        public Collection getTableRows()
        {
            Log log = Log.getLog(this.getClass());
            try
            {
            	Application application = Application.getInstance();
            	SecurityService service = (SecurityService) application.getService(SecurityService.class);
				boolean manageForums = service.hasPermission(getWidgetManager().getUser().getId(), "com.tms.collab.forum.ManageForums", null, null);
				
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
                List search = (List) getFilterValue("searchBy");
                String searchBy = "";
                if(search.size()>0)
                    searchBy = (String)search.get(0);
                List activeStatus = (List) getFilterValue("isActive");
                String isActive = "";
                if(activeStatus.size()>0)
                    isActive = (String)activeStatus.get(0);
                if(isActive!=null && (isActive.trim().equalsIgnoreCase("null") || isActive.trim().equals("")))
                    isActive = "";

                String sort = getSort();
                if("lastPostDate".equals(sort))
                    sort = "strLastPostDate";

                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                if(manageForums){
                	return forumModule.getForumsByManager(searchBy, searchCriteria, isActive, sort, isDesc(), getStart(), getRows(), true);
                }else
                return forumModule.getForumsByModerator(getWidgetManager().getUser().getId(), searchBy, searchCriteria, isActive, sort, isDesc(), getStart(), getRows(), true);
            }
            catch (ForumException e)
            {
                // log error and return an empty collection
                log.error(e);
                return new ArrayList();
            } catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ArrayList();
			}
        }

        public int getTotalRowCount()
        {
            try
            {
            	Application application = Application.getInstance();
            	SecurityService service = (SecurityService) application.getService(SecurityService.class);
				boolean manageForums = service.hasPermission(getWidgetManager().getUser().getId(), "com.tms.collab.forum.ManageForums", null, null);
				
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
                List search = (List) getFilterValue("searchBy");
                String searchBy = "";
                if(search.size() > 0)
                    searchBy = (String)search.get(0);
                List activeStatus = (List) getFilterValue("isActive");
                String isActive = "";
                if(activeStatus.size()>0)
                    isActive = (String)activeStatus.get(0);
                if(isActive!=null && (isActive.trim().equalsIgnoreCase("null") || isActive.trim().equals("")))
                    isActive = "";

                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                if(manageForums){
                	return forumModule.getNumOfForumsByManager(searchBy, searchCriteria, isActive);	
                }else
                return forumModule.getNumOfForumsByModerator(getWidgetManager().getUser().getId(), searchBy, searchCriteria, isActive);
            }
            catch (ForumException e)
            {
                return 0;
            }catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
        }
    }

}
