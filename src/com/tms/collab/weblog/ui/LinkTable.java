package com.tms.collab.weblog.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.model.DaoException;

import java.util.Collection;

import com.tms.collab.weblog.model.BlogUtil;
import com.tms.collab.weblog.model.WeblogModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 10, 2004
 * Time: 3:36:09 PM
 * To change this template use Options | File Templates.
 */
public class LinkTable extends Table
{
    private String blogId;
    private boolean hasPermission;

    public LinkTable()
    {
    }

    public LinkTable(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setNumbering(true);
        setModel(new LinkTableModel());
        setShowPageSize(true);
        setWidth("100%");
        setMultipleSelect(true);
  }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
        hasPermission = BlogUtil.hasEditLinkPermission(getWidgetManager().getUser().getId(),this.blogId);
    }

    public boolean isHasPermission()
    {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission)
    {
        this.hasPermission = hasPermission;
    }

    public class LinkTableModel extends TableModel {
        public LinkTableModel()
        {
            TableColumn nameColumn =new TableColumn("name",Application.getInstance().getMessage("weblog.label.name","Name"),true);
            nameColumn.setUrl("");
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);
            addColumn(new TableColumn("url",Application.getInstance().getMessage("weblog.label.url","Url"),true));
            addAction(new TableAction("delete",Application.getInstance().getMessage("weblog.label.delete","Delete"),Application.getInstance().getMessage("weblog.label.deletelinkprompt","Are you sure you want to delete the selected link(s)?")));
            addFilter(new TableFilter("search"));
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public Forward processAction(Event event, String s, String[] strings)
        {
            if("delete".equals(s)){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                for (int i = 0; i < strings.length; i++)
                {
                    String id = strings[i];
                    try
                    {
                        wm.deleteLink(id);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }
            return super.processAction(event, s, strings);
        }

        public Collection getTableRows()
        {
            if(hasPermission){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                try
                {
                    return wm.getLinks(blogId,(String)getFilterValue("search"),getSort(),isDesc(),getStartIndex(),getRows());
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
            return null;
        }

        public int getTotalRowCount()
        {
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                return wm.countLinks(blogId,(String)getFilterValue("search"));
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return 0;
        }
    }
}
