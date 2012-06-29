package com.tms.collab.weblog.ui;

import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.ui.Event;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 11, 2004
 * Time: 11:31:32 AM
 * To change this template use Options | File Templates.
 */
public class UsersList extends Widget
{
    private UserTable userTable;
    private String alphabet="a";

    public UsersList()
    {
    }

    public UsersList(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        userTable = new UserTable("userTable");
        addChild(userTable);
        userTable.init();
        userTable.setAlphabet(alphabet);
    }

    public Forward actionPerformed(Event event)
    {
        if("select".equals(event.getType())){
            alphabet = event.getParameter("al");
            userTable.setAlphabet(alphabet);
        }
        return super.actionPerformed(event);
    }

    public String getDefaultTemplate()
    {
        return "weblog/userlist";
    }

    public UserTable getUserTable()
    {
        return userTable;
    }

    public void setUserTable(UserTable userTable)
    {
        this.userTable = userTable;
    }

    public String getAlphabet()
    {
        return alphabet;
    }

    public void setAlphabet(String alphabet)
    {
        this.alphabet = alphabet;
    }
}
