package com.tms.collab.emeeting;

import kacang.model.*;
import kacang.util.UuidGenerator;
import kacang.util.JdbcUtil;
import kacang.Application;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Sep 22, 2003
 * Time: 5:24:39 PM
 * To change this template use Options | File Templates.
 */
public class MeetingDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE meeting_task (itemId varchar(255) default '0', taskId varchar(255) default '0')",null);
        super.update("CREATE TABLE meeting_details(eventId VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL, startDate DATE NOT NULL, endDate DATE NOT NULL, category VARCHAR(255) NOT NULL, secretary VARCHAR(250), chairman VARCHAR(250), PRIMARY KEY(eventId))", null);
        super.update("CREATE TABLE meeting_agenda(itemId VARCHAR(255) NOT NULL, eventId VARCHAR(255) NOT NULL, parentId VARCHAR(255) NOT NULL, taskId VARCHAR(255), itemOrder INTEGER NOT NULL, title VARCHAR(250) NOT NULL, notes TEXT, action TEXT, PRIMARY KEY(itemId))", null);
    }

    public Meeting selectMeeting(String eventId) throws DaoException, DataObjectNotFoundException
    {
        Collection list = super.select("SELECT eventId, title, startDate, endDate, category, secretary FROM meeting_details WHERE eventId = ?", Meeting.class, new Object[] {eventId}, 0, 1);
        Meeting meeting = null;
        if(list.size() > 0)
        {
            meeting = (Meeting) list.iterator().next();
            meeting.setMeetingAgenda(selectAgendaItems(eventId));
        }
        else
            throw new DataObjectNotFoundException("Meeting with ID: " + eventId + " not found");
        return meeting;
    }

    public Collection selectMeeting(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        return super.select("SELECT eventId, title, startDate, endDate, category, secretary FROM meeting_details WHERE eventId = eventId" + query.getStatement() + getSort(sort, descending), Meeting.class, query.getArray(), start, maxResults);
    }

    public Collection selectRelatedMeetings(String category) throws DaoException, DataObjectNotFoundException
    {
        Collection list = super.select("SELECT eventId, title, startDate, endDate, category, secretary FROM meeting_details WHERE category = ? ORDER BY startDate DESC", Meeting.class, new Object[] {category}, 0, -1);
        for(Iterator i = list.iterator(); i.hasNext();)
        {
            Meeting meeting = (Meeting) i.next();
            meeting.setMeetingAgenda(selectAgendaItems(meeting.getEventId()));
        }
        return list;
    }

    public Collection selectCategories() throws DaoException
    {
        Collection list = new ArrayList();
        Collection results = super.select("SELECT DISTINCT category FROM meeting_details", HashMap.class, null, 0, -1);
        for (Iterator i=results.iterator(); i.hasNext();)
        {
            HashMap map = (HashMap)i.next();
            list.add(map.get("category"));
        }
        return list;
    }

    public Collection selectAgendaItems(String eventId) throws DaoException, DataObjectNotFoundException
    {
        Collection list = selectAgendaItems(eventId, AgendaItem.NO_PARENT);
        for(Iterator i = list.iterator(); i.hasNext();)
        {
            AgendaItem item = (AgendaItem) i.next();
            item.setChildren(getChildren(item));
/*
            List taskIds =
*/

/*
            item.setTask(getTask(item));
*/
            item.setTasks(selectTasks(item.getItemId()));
        }
        return list;
    }
/*
    public List selectTaskIdList(String itemId){
    /    super.

    }
*/

    public Collection selectTasks(String itemId) throws DaoException, DataObjectNotFoundException {
        Collection col = super.select("SELECT taskId as taskId FROM meeting_task WHERE itemId=?",HashMap.class,new Object[]{itemId},0,-1);
        Collection tasks = new TreeSet();
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            HashMap hashMap = (HashMap) iterator.next();
            String taskId = (String) hashMap.get("taskId");
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            Task task = tm.getTask(taskId);
            if(task==null){
                unassignTask(itemId,taskId);
            }else{
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void unassignTask(String itemId, String taskId) throws DaoException {
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("itemId",itemId);
        obj.setProperty("taskId",taskId);
        super.update("DELETE FROM meeting_task WHERE itemId= #itemId# AND taskId=#taskId#",obj);
    }

    public Collection selectAgendaItems(String eventId, String parentId) throws DaoException
    {
        return super.select("SELECT itemId, eventId, parentId, taskId, title, notes, action, itemOrder FROM meeting_agenda WHERE eventId = ? AND parentId = ? ORDER BY itemOrder", AgendaItem.class, new Object[] {eventId, parentId}, 0, -1);
    }

    public Collection selectAgendaItemChildren(String parentId) throws DaoException
    {
        return super.select("SELECT itemId, eventId, parentId, taskId, title, notes, action, itemOrder FROM meeting_agenda WHERE parentId = ? ORDER BY itemOrder", AgendaItem.class, new Object[] { parentId}, 0, -1);
    }

    public AgendaItem selectAgendaItem(String itemId) throws DaoException, DataObjectNotFoundException
    {
        Collection items = super.select("SELECT itemId, eventId, parentId, taskId, title, notes, action, itemOrder FROM meeting_agenda WHERE itemId = ?", AgendaItem.class, new Object[] {itemId}, 0, 1);
        AgendaItem item = (AgendaItem) items.iterator().next();
        item.setChildren(getChildren(item));
        item.setTasks(selectTasks(itemId));
/*
        item.setTask(getTask(item));
*/
        return item;
    }

    private Map getChildren(AgendaItem item) throws DaoException, DataObjectNotFoundException
    {
        SequencedHashMap children = new SequencedHashMap();
        Collection list = selectAgendaItems(item.getEventId(), item.getItemId());
        for(Iterator i = list.iterator(); i.hasNext();)
        {
            AgendaItem child = (AgendaItem) i.next();
            child.setTasks(selectTasks(child.getItemId()));
/*
            child.setTask(getTask(child));
*/
            children.put(child.getItemId(), child);
        }
        return children;
    }

    private Task getTask(AgendaItem item) throws DaoException, DataObjectNotFoundException
    {
        Task task = null;
        if(!(item.getTaskId() == null || "".equals(item.getTaskId())))
        {
            TaskManager manager= (TaskManager) Application.getInstance().getModule(TaskManager.class);
            task = manager.getTask(item.getTaskId());
            //Task removed externally. Handle exception

/*
            if(task == null)
                assignTask(item.getItemId(), "");
*/
        }
        return task;
    }

    public void insertMeeting(Meeting meeting) throws DaoException
    {
        super.update("INSERT INTO meeting_details(eventId, title, startDate, endDate, category, secretary) VALUES(#eventId#, #title#, #startDate#, #endDate#, #category#, #secretary#)", meeting);
        insertMeetingAgenda(meeting);
    }

    public void insertMeetingAgenda(Meeting meeting) throws DaoException
    {
        int count = 1;
        for(Iterator i = meeting.getMeetingAgenda().iterator(); i.hasNext();)
        {
            AgendaItem item = (AgendaItem) i.next();
            insertAgendaItems(item, AgendaItem.NO_PARENT, new Integer(count));
            if(!item.getChildren().isEmpty())
            {
                int childCount = 1;
                for(Iterator it = item.getChildren().keySet().iterator(); it.hasNext();)
                {
                    String key = (String) i.next();
                    AgendaItem child = (AgendaItem) item.getChildren().get(key);
                    insertAgendaItems(child, item.getItemId(), new Integer(childCount));
                    childCount++;
                }
            }
            count ++;
        }
    }

    public void insertAgendaItems(AgendaItem item, String parentId, Integer order) throws DaoException
    {
        if(item.getItemId() == null || "".equals(item.getItemId()))
            item.setItemId(UuidGenerator.getInstance().getUuid());
        Object[] params = new Object[] {item.getItemId(), item.getEventId(), parentId, order, item.getTitle(), item.getNotes(), item.getAction()};
        super.update("INSERT INTO meeting_agenda(itemId, eventId, parentId, itemOrder, title, notes, action) VALUES(?, ?, ?, ?, ?, ?, ?)", params);
    }

    public void updateMeeting(Meeting meeting) throws DaoException
    {
        super.update("UPDATE meeting_details SET title = #title#, startDate = #startDate#, endDate = #endDate#, category = #category#, secretary = #secretary# WHERE eventId = #eventId#", meeting);
    }

    public void updateMeetingAgenda(Meeting meeting) throws DaoException
    {
        int count = 1;
        for(Iterator i = meeting.getMeetingAgenda().iterator(); i.hasNext();)
        {
            AgendaItem item = (AgendaItem) i.next();
            updateAgendaItems(item, AgendaItem.NO_PARENT, new Integer(count));
            if(!item.getChildren().isEmpty())
            {
                int childCount = 1;
                for(Iterator it = item.getChildren().keySet().iterator(); it.hasNext();)
                {
                    String key = (String) i.next();
                    AgendaItem child = (AgendaItem) item.getChildren().get(key);
                    updateAgendaItems(child, item.getItemId(), new Integer(childCount));
                    childCount++;
                }
            }
            count ++;
        }
    }

    public void updateAgendaItems(AgendaItem item, String parentId, Integer order) throws DaoException
    {
        Object params = new Object[] {order, item.getTitle(), item.getNotes(), item.getAction(), parentId, item.getItemId()};
        super.update("UPDATE meeting_agenda SET itemOrder = ?, title = ?, notes = ?, action = ?, parentId = ? WHERE itemId = ?", params);
    }

    public void deleteMeeting(String eventId) throws DaoException
    {
        super.update("DELETE FROM meeting_details WHERE eventId = ?", new Object[] {eventId});
        deleteMeetingAgenda(eventId);
    }

    public void deleteMeetingAgenda(String eventId) throws DaoException
    {
        super.update("DELETE FROM meeting_agenda WHERE eventId = ?", new Object[] {eventId});
    }

    public void deleteAgendaItems(String itemId) throws DaoException
    {
        super.update("DELETE FROM meeting_agenda WHERE itemId = ?", new Object[] {itemId});
    }

    public void deleteAgendaItemsChildren(String itemId) throws DaoException
    {
        super.update("DELETE FROM meeting_agenda WHERE parentId = ?", new Object[] {itemId});
    }

    private String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY " + sort;
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }

    public void moveUp(String itemId, String parentId, int order) throws DaoException, DataObjectNotFoundException {
        if(order<=1)
            return;
        int newOrder = order-1;
        AgendaItem agendaItem = selectAgendaItem(itemId);
        Collection col = super.select("SELECT itemId, eventId, parentId, taskId, title, notes, action, itemOrder FROM meeting_agenda WHERE parentId = ? AND itemOrder=? AND eventId=?", AgendaItem.class, new Object[] { parentId,new Integer(newOrder),agendaItem.getEventId()}, 0, -1);
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            AgendaItem temp_agendaItem = (AgendaItem) iterator.next();
            updateAgendaItems(temp_agendaItem,temp_agendaItem.getParentId(),new Integer(order));
        }
        updateAgendaItems(agendaItem,parentId,new Integer(newOrder));
    }

    public void moveDown(String itemId, String parentId, int order) throws DaoException, DataObjectNotFoundException {
        int newOrder = order+1;
        AgendaItem agendaItem = selectAgendaItem(itemId);
        Collection col = super.select("SELECT itemId, eventId, parentId, taskId, title, notes, action, itemOrder FROM meeting_agenda WHERE parentId = ? AND itemOrder=? AND eventId=?", AgendaItem.class, new Object[] { parentId,new Integer(newOrder),agendaItem.getEventId()}, 0, -1);
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            AgendaItem agendaItem2 = (AgendaItem) iterator.next();
            updateAgendaItems(agendaItem2,agendaItem2.getParentId(),new Integer(order));
        }
        updateAgendaItems(agendaItem,parentId,new Integer(newOrder));
    }


    /* Task Management Integration */
    public void assignTask(String itemId, String taskId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        try
        {
            con = getDataSource().getConnection();
/*
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "UPDATE meeting_agenda SET taskId = ? WHERE itemId = ?", new String[] {taskId, itemId});
*/
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "INSERT INTO meeting_task(itemId,taskId) VALUES(?,?)", new String[] {itemId,taskId});
            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
            }
        }
    }
}
