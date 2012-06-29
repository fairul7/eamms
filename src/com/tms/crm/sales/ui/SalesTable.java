package com.tms.crm.sales.ui;

import kacang.stdui.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 4, 2004
 * Time: 6:02:07 PM
 * To change this template use Options | File Templates.
 */
public class SalesTable extends Table
{
    Object currentRow;
    public SalesTable()
    {
    }

    public SalesTable(String s)
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
        return "sfa/saletable";
    }

}
