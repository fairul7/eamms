package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumException;
import com.tms.collab.forum.model.ForumModule;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 9, 2003
 * Time: 9:29:59 PM
 * To change this template use Options | File Templates.
 */
public class AdminThreadTable extends Table
{
    private String forumId;

    public AdminThreadTable()
    {
        super();
    }

    public AdminThreadTable(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        super.init();

        setPageSize(10);
        setModel(new AdminThreadTable.AdminThreadTableModel());
        setWidth("100%");
    }

    public void setForumId(String forumId)
    {
        this.forumId = forumId;
    }

    public String getForumId()
    {
        return forumId;
    }

    class AdminThreadTableModel extends TableModel
    {
        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ event Type = " + evt.getType());
            try
            {
                if(action.equalsIgnoreCase("delete"))
                {
                    ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                    for (int i = 0; i < selectedKeys.length; i++)
                    {
                        forumModule.deleteThread(selectedKeys[i], evt.getWidgetManager().getUser().getId(), true);
                    }
                }
            }
            catch (ForumException e)
            {
                log.error(e.getMessage(), e);
            }

            return super.processAction(evt, action, selectedKeys);
        }

        public AdminThreadTableModel()
        {
            addAction(new TableAction("delete", Application.getInstance().getMessage("forum.label.delete","Delete"), Application.getInstance().getMessage("forum.label.deleteSelectedItems","Delete selected item(s)?")));

            TableColumn tcName = new TableColumn("subject", Application.getInstance().getMessage("forum.label.topicSubject","Topic subject"));
            //tcName.setUrl("editForum.jsp");
            tcName.setUrlParam("threadId");
            //tcName.setUrlParam(getWidgetManager().getUser().getId());
            addColumn(tcName);

            //addColumn(new TableColumn("content", "Content"));

            TableColumn tcReplies = new TableColumn("numOfMessage", Application.getInstance().getMessage("forum.label.posting","Posting"));
            tcReplies.setUrlParam("id");
            addColumn(tcReplies);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");

/*
            TableColumn tcIsPublic = new TableColumn("isPublic", Application.getInstance().getMessage("forum.label.public","Public"));
            tcIsPublic.setFormat(booleanFormat);
            addColumn(tcIsPublic);
*/
            TableColumn tcActive = new TableColumn("active", Application.getInstance().getMessage("forum.label.active","Active"));
            tcActive.setFormat(booleanFormat);
            addColumn(tcActive);

            addColumn(new TableColumn("ownerId", Application.getInstance().getMessage("forum.label.author","Author")));
            addColumn(new TableColumn("email", Application.getInstance().getMessage("forum.label.email","Email")));
            TableColumn tcCreationDate = new TableColumn("creationDate", Application.getInstance().getMessage("forum.label.createdOn","Created On"));
            tcCreationDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcCreationDate);
            TableColumn tcModificationDate = new TableColumn("modificationDate", Application.getInstance().getMessage("forum.label.lastModified","Last Modified"));
            tcModificationDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcModificationDate);
            TableColumn tcLastPostDate = new TableColumn("lastPostDate", Application.getInstance().getMessage("forum.label.lastPost","Last Post"));
            tcLastPostDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(tcLastPostDate);
/*
            TableColumn tcEdit = new TableColumn("", "Edit");
            tcEdit.setLabel("Edit");
            tcEdit.setUrlParam("threadId");
            addColumn(tcEdit);
*/
            TableFilter tfSearchCriteria = new TableFilter("searchCriteria", Application.getInstance().getMessage("forum.label.topicSearch","Topic Search"));
            addFilter(tfSearchCriteria);

            TableFilter tfSearchBy = new TableFilter("searchBy", Application.getInstance().getMessage("forum.label.searchBy","Search By"));
            SelectBox selectSearchBy = new SelectBox();
            selectSearchBy.addOption("subject", Application.getInstance().getMessage("forum.label.subject","Subject"));
            selectSearchBy.addOption("content", Application.getInstance().getMessage("forum.label.content","Content"));
            selectSearchBy.addOption("ownerId", Application.getInstance().getMessage("forum.label.author","Author"));
            selectSearchBy.addOption("email", Application.getInstance().getMessage("forum.label.email","Email"));
            selectSearchBy.setMultiple(false);
            tfSearchBy.setWidget(selectSearchBy);
            addFilter(tfSearchBy);

        }

        public String getTableRowKey()
        {
            return "threadId";
        }

        public Collection getTableRows()
        {
            try
            {
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
                List search = (List) getFilterValue("searchBy");
                String searchBy = "";
                if(search.size() != 0)
                    searchBy = (String)search.get(0);
                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                return forumModule.getThreads(getForumId(), getWidgetManager().getUser().getId(), getStart(), getRows(), getSort(), isDesc(), searchBy, searchCriteria);

            }
            catch (ForumException e)
            {
                // log error and return an empty collection
                Log log = Log.getLog(this.getClass());
                log.error(e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount()
        {
            try
            {
                String searchCriteria = (String) getFilterValue("searchCriteria");
                if(searchCriteria == null)
                    searchCriteria = "";
                List search = (List) getFilterValue("searchBy");
                String searchBy = "";
                if(search.size() != 0)
                    searchBy = (String)search.get(0);
                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                return forumModule.getNumOfThread(getForumId(), searchBy, searchCriteria);
            }
            catch (ForumException e)
            {
                return 0;
            }
        }
    }

}
