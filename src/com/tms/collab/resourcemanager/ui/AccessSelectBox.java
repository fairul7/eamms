package com.tms.collab.resourcemanager.ui;

import kacang.stdui.SelectBox;
import kacang.stdui.FormField;
import kacang.stdui.Form;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.Group;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoQuery;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.Widget;

import java.util.*;
import org.apache.commons.collections.SequencedHashMap;
import com.tms.collab.calendar.model.CalendarModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2003
 * Time: 11:54:03 AM
 * To change this template use Options | File Templates.
 */
public class AccessSelectBox extends FormField
{
    public static final int GROUP = 0,USER = 1;
    private int accessType;
    private SelectBox left,right;
    private int leftRows = 5,rightRows=5;
    private Map users,groups,selectedUsers,selectedGroups;
 //   private String [] selectedUsersId,selectedGroupsId;
    private int usersCount=0,groupsCount=0,selectedUsersCount=0,selectedGroupsCount=0;
   // private String[] selectedUsersName,selectedGroupsName;
    private User[] userOptions;
    private Group[] groupOptions;
    private Collection selectedIds;
    private int selectedIdsCount = 0;
    private Form parentForm=null;

    public AccessSelectBox()
    {
        super();
        init();
    }

    public AccessSelectBox(String name)
    {
        super(name);
        init();
    }

    public void init()
    {
        removeChildren();
        left = new SelectBox("Left");
        left.setMultiple(true);
        left.setRows(leftRows);
        right = new SelectBox("Right");
       // right.addChild(new ValidatorNotEmpty("Selected"));
        right.setMultiple(true);
        right.setRows(rightRows);
        addChild(left);
        addChild(right);

        users = new SequencedHashMap();
        groups = new SequencedHashMap();
        Collection usersCol = new ArrayList();
        Collection groupsCol = new ArrayList();

        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try{
            usersCol = ss.getUsersByPermission(CalendarModule.PERMISSION_CALENDARING,Boolean.TRUE,"username",false,0,-1);
            List tempUsers = new ArrayList();
            User user;
            for(Iterator i = usersCol.iterator();i.hasNext();)
            {
                user = (User)i.next();
                users.put(user.getId(),user.getName());
               // users.
                tempUsers.add(user);
            }
            List tempGroups = new ArrayList();
            groupsCol = ss.getGroups(new DaoQuery(),0,-1,"groupName",false);
            Group group;
            for(Iterator i = groupsCol.iterator();i.hasNext();)
            {
                group = (Group)i.next();
                groups.put(group.getId(),group.getName());
                tempGroups.add(group);
            }
            userOptions = new User[tempUsers.size()];
            for(int i=0; i<tempUsers.size();i++)
            {
                userOptions[i]=(User)tempUsers.toArray()[i];
            }
            groupOptions = new Group[tempGroups.size()];
            for(int i=0; i<tempGroups.size();i++)
            {
                groupOptions[i]=(Group)tempGroups.toArray()[i];
            }
            usersCount = userOptions.length;
            groupsCount = groupOptions.length;
        }catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }
    }

    public String getDefaultTemplate()
    {
        return "resourcemanager/AccessSelectBox";
    }

    public Forward onSubmit(Event evt)
    {
        //left.onSubmit(evt);
        right.onSubmit(evt);
        Forward forward = super.onSubmit(evt);
        Map selections = right.getSelectedOptions();
        selectedUsers = new SequencedHashMap();
        selectedGroups = new SequencedHashMap();
        for(Iterator i = selections.keySet().iterator();i.hasNext();){
            String key = (String)i.next();
            if(users.containsKey(key)){
                selectedUsers.put(key,users.get(key));
            } else if(groups.containsKey(key)){
                selectedGroups.put(key,groups.get(key));
            }
        }
        return forward;
    }


    public void setParent(Widget parent) {
        String onSubmit = "";
        // remove previous function call

        Form rootForm = getRootForm();
        if (rootForm != null) {
            onSubmit = (String) rootForm.getAttribute("onSubmit");
            if (onSubmit == null) {
                onSubmit = "";
            }
            onSubmit += "; selectAccessMembers();";
            parentForm.setAttribute("onSubmit", onSubmit);
//            else if (onSubmit.indexOf("selectMembers") >= 0)
//            {
//                int i = onSubmit.indexOf("selectMembers");
//                onSubmit = onSubmit.substring(0, i) + " " + onSubmit.substring(i + "selectMembers".length());
//                rootForm.setAttribute("onSubmit", onSubmit);
//            }
        } else  if(parent instanceof Form){
            parentForm = (Form)parent;
            onSubmit = (String) parentForm.getAttribute("onSubmit");
            if(onSubmit == null)
                onSubmit = "";
            onSubmit += "; selectAccessMembers();";
            parentForm.setAttribute("onSubmit", onSubmit);
        }
        // inherited behaviour
        super.setParent(parent);
    }


    public Map getUsers()
    {
        return users;
    }

    public void setUsers(Map users)
    {
        this.users = users;
    }

    public Map getGroups()
    {
        return groups;
    }

    public void setGroups(Map groups)
    {
        this.groups = groups;
    }


    public int getUsersCount()
    {
        return usersCount;
    }

    public void setUsersCount(int usersCount)
    {
        this.usersCount = usersCount;
    }

    public int getGroupsCount()
    {
        return groupsCount;
    }

    public void setGroupsCount(int groupsCount)
    {
        this.groupsCount = groupsCount;
    }

    public int getSelectedUsersCount()
    {
        return selectedUsersCount;
    }

    public void setSelectedUsersCount(int selectedUsersCount)
    {
        this.selectedUsersCount = selectedUsersCount;
    }

    public int getSelectedGroupsCount()
    {
        return selectedGroupsCount;
    }

    public void setSelectedGroupsCount(int selectedGroupsCount)
    {
        this.selectedGroupsCount = selectedGroupsCount;
    }

    public int getLeftRows()
    {
        return leftRows;
    }

    public void setLeftRows(int leftRows)
    {
        this.leftRows = leftRows;
    }

    public int getRightRows()
    {
        return rightRows;
    }

    public void setRightRows(int rightRows)
    {
        this.rightRows = rightRows;
    }

    public int getAccessType()
    {

        return accessType;
    }

    public void setAccessType(int accessType)
    {
        this.accessType = accessType;
    }

    public SelectBox getLeft()
    {
        return left;
    }

    public void setLeft(SelectBox left)
    {
        this.left = left;
    }

    public SelectBox getRight()
    {
        return right;
    }

    public void setRight(SelectBox right)
    {
        this.right = right;
    }

    public User[] getUserOptions()
    {
        return userOptions;
    }

    public void setUserOptions(User[] userOptions)
    {
        this.userOptions = userOptions;
    }

    public Group[] getGroupOptions()
    {
        return groupOptions;
    }

    public void setGroupOptions(Group[] groupOptions)
    {
        this.groupOptions = groupOptions;
    }

    public Map getSelectedUsers()
    {
        return selectedUsers;
    }

    public void setSelectedUsers(Map selectedUsers)
    {
        this.selectedUsers = selectedUsers;
    }

    public Map getSelectedGroups()
    {
        return selectedGroups;
    }

    public void setSelectedGroups(Map selectedGroups)
    {
        this.selectedGroups = selectedGroups;
    }

    public Collection getSelectedIds()
    {
        return selectedIds;
    }

    public void setSelectedIds(Collection selectedIds)
    {
        this.selectedIds = selectedIds;
        selectedIdsCount = selectedIds.size();
    }

    public int getSelectedIdsCount()
    {
        return selectedIdsCount;
    }

    public void setSelectedIdsCount(int selectedIdsCount)
    {
        this.selectedIdsCount = selectedIdsCount;
    }

}
/*if(selectedUsersId!=null&&selectedUsersId.length>0)
            {
                List tempUsersName = new ArrayList();
                for(int i=0;i<selectedUsersId.length;i++)
                {
                    if(users.containsKey(selectedUsersId[i]))
                    {
                        tempUsersName.add(users.get(selectedUsersId[i]));
                    }
                }
                selectedUsersName = new String[tempUsersName.size()];
                for(int i=0;i<tempUsersName.size();i++){
                    selectedUsersName[i] = (String)tempUsersName.toArray()[i];
                }
                selectedUsersCount = selectedUsersName.length;
            }

            if(selectedGroupsId!=null&&selectedGroupsId.length>0)
            {
                List tempGroupsName = new ArrayList();
                for(int i=0;i<selectedGroupsId.length;i++)
                {
                    if(groups.containsKey(selectedGroupsId[i]))
                    {
                        tempGroupsName.add(groups.get(selectedGroupsId[i]));
                        tempGroups.remove(groups.get(selectedGroupsId[i]));
                    }
                }
                selectedGroupsName = new String[tempGroupsName.size()];
                for(int i=0;i<tempGroupsName.size();i++){
                    selectedGroupsName[i] = (String) tempGroupsName.toArray()[i];
                }
                selectedGroupsCount = selectedGroupsName.length;
            }
*/