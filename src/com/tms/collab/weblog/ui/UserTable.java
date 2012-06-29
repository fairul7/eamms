package com.tms.collab.weblog.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;

import java.util.Collection;

import com.tms.collab.weblog.model.WeblogModule;
import com.tms.util.FormatUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 11, 2004
 * Time: 10:53:07 AM
 * To change this template use Options | File Templates.
 */
public class UserTable extends BlogTable
{
    private String alphabet;

    public UserTable()
    {

    }

    public UserTable(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setModel(new UserTableModel());
        setNumbering(true);
        setShowPageSize(true);
        setWidth("100%");
    }

    public String getAlphabet()
    {
        return alphabet;
    }

    public void setAlphabet(String alphabet)
    {
        this.alphabet = alphabet;
    }

    public class UserTableModel extends TableModel{
        public UserTableModel()
        {
            TableColumn nameColumn = new TableColumn("userName",Application.getInstance().getMessage("weblog.label.name","Name"),true);
            addColumn(nameColumn);
            TableColumn titleColumn = new TableColumn("title",Application.getInstance().getMessage("weblog.label.title","Title"),true);
            titleColumn.setUrl("");
            titleColumn.setUrlParam("id");
            addColumn(titleColumn);
            TableColumn dateColumn = new TableColumn("creationDate",Application.getInstance().getMessage("weblog.label.date","Date"),true);
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(dateColumn);
            addFilter(new TableFilter("search"));

        }

        public Collection getTableRows()
        {
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                return wm.getUserBlogsByAlphabet(alphabet,(String)getFilterValue("search"),getSort(),isDesc(),getStartIndex(),getRows());
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return null;
        }

        public int getTotalRowCount()
        {
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                return wm.countUserBlogsByAlphabet(alphabet,(String)getFilterValue("search"));
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return 0;
        }
    }

}
