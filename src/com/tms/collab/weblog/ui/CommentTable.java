package com.tms.collab.weblog.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.model.DaoException;
import java.util.Collection;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.Comment;
import com.tms.collab.weblog.model.BlogUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 3, 2004
 * Time: 1:23:25 PM
 * To change this template use Options | File Templates.
 */
public class CommentTable extends BlogTable
{
    private String postId;
    private boolean hasEditPermission = false;

    public CommentTable()
    {
    }

    public CommentTable(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setNumbering(true);
        setModel(new CommentTableModel());
        setShowPageSize(true);
        setWidth("100%");
        setMultipleSelect(true);
    }

    public String getPostId()
    {
        return postId;
    }

    public void setPostId(String postId)
    {
        this.postId = postId;
        hasEditPermission = BlogUtil.hasEditPostPermission(getWidgetManager().getUser().getId(),postId);
    }

    public class CommentTableModel extends TableModel{

        public CommentTableModel()
        {
            TableColumn commentColumn =new TableColumn("comment",Application.getInstance().getMessage("weblog.label.comment","Comment"),true);
            commentColumn.setFormat(new TableFormat(){
                public String format(Object o)
                {
                    if(o!=null)
                        return StringUtils.replace(o.toString(),"\n","<br>");
                    return null;
                }
            });
            addColumn(commentColumn);
            TableColumn nameColumn = new TableColumn("userName",Application.getInstance().getMessage("weblog.label.name","Name"),true);
            nameColumn.setFormat(new TableFormat(){
                public String format(Object o)
                {
                    Comment comment = (Comment)getCurrentRow();
                    if(comment.getEmail()!=null&&!comment.getEmail().equals(""))
                        return "<a href=\" mailto:"+comment.getEmail() +"\">"+o.toString()+"</a>";
                    else
                        return o.toString();
                }
            });
            addColumn(nameColumn);
            TableColumn websiteColumn = new TableColumn("url",Application.getInstance().getMessage("weblog.label.website","Website"),true);
            websiteColumn.setFormat(new TableFormat(){
                public String format(Object o)
                {
                    if(o!=null)
                        return "<a href=\""+o.toString() +"\">"+Application.getInstance().getMessage("weblog.label.homePage","Home Page")+"</a>";
                    else
                        return Application.getInstance().getMessage("weblog.label.none","None");
                }
            });
            addColumn(websiteColumn);
            addColumn(new TableColumn("date",Application.getInstance().getMessage("weblog.label.date","Date"),true));
            addAction(new TableAction("delete",Application.getInstance().getMessage("weblog.label.delete","Delete"),Application.getInstance().getMessage("weblog.label.deleteprompt","Are you sure you want to delete the selected comment(s)?")));
            addFilter(new TableFilter("search") );
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
                        wm.deleteComment(id);
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
            if(hasEditPermission){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                try
                {
                    return wm.getComments(postId,(String)getFilterValue("search"),getSort(),isDesc(),getStartIndex(),getRows());
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
                return wm.countComments(postId,(String)getFilterValue("search"));
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return 0;
        }
    }


}
