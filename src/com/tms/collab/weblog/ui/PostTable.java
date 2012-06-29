package com.tms.collab.weblog.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.WidgetManager;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.model.DaoException;

import java.util.Collection;

import com.tms.collab.weblog.model.Blog;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.BlogUtil;
import com.tms.util.FormatUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 2, 2004
 * Time: 2:33:34 PM
 * To change this template use Options | File Templates.
 */
public class PostTable extends Table
{
    private String blogId;
    private Blog blog;
    public PostTable()
    {
    }

    public PostTable(String s)
    {
        super(s);

    }

    public void init()
    {
        super.init();
        setNumbering(true);
        setModel(new PostTableModel());
        setShowPageSize(true);
        setWidth("100%");
        setMultipleSelect(true);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(blogId!=null&&!blogId.equals("")){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                blog = wm.getBlog(blogId);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
    }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }



    public class PostTableModel extends TableModel
    {
        public PostTableModel()
        {
            TableColumn titleColumn = new TableColumn("title",Application.getInstance().getMessage("weblog.label.title","Title"),true);
            titleColumn.setUrl("");
            titleColumn.setUrlParam("id");
            addColumn(titleColumn);
            TableColumn dateColumn = new TableColumn("publishTime",Application.getInstance().getMessage("weblog.label.publishTime","Publish Time"),true);
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(dateColumn);
            TableColumn publishedColumn = new TableColumn("published",Application.getInstance().getMessage("weblog.label.published","Published"),true);
            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            publishedColumn.setFormat(new TableBooleanFormat("<img src=\""+contextPath +"/common/table/booleantrue.gif\">",""));
            addColumn(publishedColumn);
            addAction(new TableAction("publish",Application.getInstance().getMessage("weblog.label.publish","Publish")));
            addAction(new TableAction("unpublish",Application.getInstance().getMessage("weblog.label.unpublish","Unpublish")));
            addAction(new TableAction("delete",Application.getInstance().getMessage("weblog.label.delete","Delete"),Application.getInstance().getMessage("weblog.label.deletepostprompt","Are you sure you want to delete the selected post(s)?")));
            setMultipleSelect(true);
            addFilter(new TableFilter("search"));
        }

        public Forward processAction(Event event, String s, String[] strings)
        {
            if("publish".equals(s)){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                for (int i = 0; i < strings.length; i++)
                {
                    String id = strings[i];
                    try
                    {
                        wm.setPostStatus(id, true);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }else if("unpublish".equals(s)){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                for (int i = 0; i < strings.length; i++)
                {
                    String id = strings[i];
                    try
                    {
                        wm.setPostStatus(id, false);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }else if("delete".equals(s)){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                if(getWidgetManager().getUser().getId().equals(blog.getUserId())){
                    for (int i = 0; i < strings.length; i++)
                    {
                        String id = strings[i];
                        try
                        {
                            wm.deletePost(id);
                        } catch (DaoException e)
                        {
                            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                        }
                        //    wm.deleteBlog();
                    }
                }
            }
            return super.processAction(event, s, strings);
        }

        public Collection getTableRows()
        {
            if(blogId==null||blogId.equals("")||blog==null||!BlogUtil.hasEditBlogPermission(getWidgetManager().getUser().getId(),blogId))
                return null;
            else {
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                try
                {
                    return wm.getPosts(blogId,(String)getFilterValue("search"),getStartIndex(),getRows(),getSort()==null?"publishTime":getSort(),getSort()==null?true:isDesc());
                } catch (DaoException e){
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    return null;
                }
            }
        }

        public int getTotalRowCount()
        {
            if(blogId==null||blogId.equals("")||blog==null||!blog.getUserId().equals(getWidgetManager().getUser().getId()))
                return 0;
            else{
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                try
                {
                    return wm.countPost(blogId);
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
            return 0;
        }

        public String getTableRowKey()
        {
            return "id";
        }
    }

}
