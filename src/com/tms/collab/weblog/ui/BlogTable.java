package com.tms.collab.weblog.ui;

import kacang.stdui.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 4, 2004
 * Time: 6:02:07 PM
 * To change this template use Options | File Templates.
 */
public class BlogTable extends Table
{
    Object currentRow;
    public BlogTable()
    {
    }

    public BlogTable(String s)
    {
        super(s);
    }

    public Object getCurrentRow(){
        return currentRow;
    }

    public void setCurrentRow(Object currentRow)
    {
        this.currentRow = currentRow;
    }

    public String getDefaultTemplate()
    {
        return "weblog/blogtable";
    }

}
