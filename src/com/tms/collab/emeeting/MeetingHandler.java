package com.tms.collab.emeeting;

import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.model.ConflictException;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.taskmanager.model.TaskManager;
import kacang.model.DefaultModule;
import kacang.model.DaoQuery;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.Application;

import java.util.*;

public class MeetingHandler extends DefaultModule
{

    public final static String MEETING_FILES_FOLDER ="meeting";

    public void init()
    {
    }

    public Meeting getMeeting(String eventId, boolean deepRetrieval) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            Meeting meeting = dao.selectMeeting(eventId);
            if(deepRetrieval)
            {
                CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
                meeting.setEvent(module.getCalendarEvent(eventId));
            }
            return meeting;
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public Collection getMeeting(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            Collection meetings = dao.selectMeeting(query, start, maxResults, sort, descending);
            if(deepRetrieval)
                setMeetingEvent(meetings);
            return meetings;
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public Collection getRelatedMeeting(String category, boolean deepRetrieval) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            Collection meetings = dao.selectRelatedMeetings(category);
            if(deepRetrieval)
                setMeetingEvent(meetings);
            return meetings;
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public Collection getCategory() throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            return dao.selectCategories();
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public Collection getAgendaItems(String eventId, String parentId) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            return dao.selectAgendaItems(eventId, parentId);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public Collection getAgendaItemChildren(String itemId) throws DaoException
    {
        MeetingDao dao = (MeetingDao) getDao();
        return dao.selectAgendaItemChildren(itemId);
    }

    public AgendaItem getAgendaItem(String itemId) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            return dao.selectAgendaItem(itemId);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.toString(), e);
        }
    }

    private void setMeetingEvent(Collection meetings) throws MeetingException
    {
        //TODO: ADD METHOD IN CALENDAR MODULE TO SUPPORT THE RETRIVAL OF LISTED ID
        try
        {
            CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            for(Iterator i = meetings.iterator(); i.hasNext();)
            {
                Meeting meeting = (Meeting) i.next();
                meeting.setEvent(module.getCalendarEvent(meeting.getEventId()));
            }
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void addMeeting(Meeting meeting, String userId) throws MeetingException, ConflictException
    {
        MeetingDao dao = (MeetingDao) getDao();
        CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        try
        {
            module.addCalendarEvent(Meeting.class.getName(), meeting.getEvent(), userId, false);
            dao.insertMeeting(meeting);
        }
        catch(DaoException e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
        catch(CalendarException ce)
        {
            throw new MeetingException(ce.getMessage(), ce);
        }
    }

    public void addMeeting(Meeting meeting, String userId,boolean ignoreConflict) throws MeetingException, ConflictException
    {
        MeetingDao dao = (MeetingDao) getDao();
        CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        try
        {
            module.addCalendarEvent(Meeting.class.getName(), meeting.getEvent(), userId, ignoreConflict);
            dao.insertMeeting(meeting);
        }
        catch(DaoException e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
        catch(CalendarException ce)
        {
            throw new MeetingException(ce.getMessage(), ce);
        }
    }


    public void addMeetingAgenda(Meeting meeting) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            dao.insertMeetingAgenda(meeting);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void addAgendaItem(AgendaItem item, String parentId, Integer order) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            dao.insertAgendaItems(item, parentId, order);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void editMeeting(Meeting meeting, String userId) throws MeetingException,ConflictException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            module.updateCalendarEvent(meeting.getEvent(), userId, false);
            dao.updateMeeting(meeting);
        }
        catch(ConflictException e){
            throw e;
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void editMeeting(Meeting meeting, String userId,boolean igonoreConflict) throws MeetingException,ConflictException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            module.updateCalendarEvent(meeting.getEvent(), userId, igonoreConflict);
            dao.updateMeeting(meeting);
        }
        catch(ConflictException e){
            throw e;
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void editMeetingAgenda(Meeting meeting) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            dao.updateMeetingAgenda(meeting);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void editAgendaItem(AgendaItem item, String parentId, Integer order) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            dao.updateAgendaItems(item, parentId, order);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void deleteMeeting(String eventId, String userId) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            Meeting meeting = getMeeting(eventId, true);
            Collection col = meeting.getMeetingAgenda();
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                AgendaItem agendaItem = (AgendaItem) iterator.next();
                Collection children = getAgendaItemChildren(agendaItem.getItemId());
                for (Iterator ci = children.iterator(); ci.hasNext();)
                {
                    AgendaItem child = (AgendaItem) ci.next();
                    if(!(child.getTaskId()==null || "".equals(child.getTaskId())))
                    {
                        tm.deleteTask(child.getTaskId());
                    }

                }
                if(!(agendaItem.getTaskId()==null||"".equals(agendaItem.getTaskId())))
                {
                    tm.deleteTask(agendaItem.getTaskId());
                }
            }
            module.deleteCalendarEvent(eventId, userId);
            dao.deleteMeeting(eventId);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void deleteMeetingAgenda(String eventId) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            dao.deleteMeetingAgenda(eventId);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void deleteAgendaItem(String itemId) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            String taskId = getAgendaItem(itemId).getTaskId();
            if(!(taskId==null||"".equals(taskId))){
                TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                tm.deleteTask(taskId);
            }
            deleteAgendaItemsChildren(itemId);
            dao.deleteAgendaItems(itemId);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void deleteAgendaItemsChildren(String itemId) throws DaoException, CalendarException
    {
        Collection children = getAgendaItemChildren(itemId);
        for (Iterator iterator = children.iterator(); iterator.hasNext();)
        {
            AgendaItem agendaItem = (AgendaItem) iterator.next();
            String taskId = agendaItem.getTaskId();
            if(!(taskId==null||"".equals(taskId))){
                TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                tm.deleteTask(taskId);
            }
        }
        MeetingDao dao = (MeetingDao) getDao();
        dao.deleteAgendaItemsChildren(itemId);
    }

    /* Task Management Integration */
    public void assignTask(String itemId, String taskId) throws MeetingException
    {
        try
        {
            MeetingDao dao = (MeetingDao) getDao();
            dao.assignTask(itemId, taskId);
        }
        catch(Exception e)
        {
            throw new MeetingException(e.getMessage(), e);
        }
    }

    public void removeTask(String itemId, String taskId) throws MeetingException
    {
        if(!(itemId == null || "".equals(itemId)))
        {
            try
            {
                TaskManager manager = (TaskManager) getDao();
                manager.deleteTask(taskId);
                assignTask(itemId, "");
            }
            catch(Exception e)
            {
                throw new MeetingException(e.getMessage(), e);
            }
        }
    }

    public void moveUp(String itemId) throws MeetingException {
        try {
            AgendaItem agendaItem = getAgendaItem(itemId);
            int order = agendaItem.getItemOrder();
            MeetingDao dao = (MeetingDao) getDao();
            dao.moveUp(itemId,agendaItem.getParentId(),order);
        } catch (MeetingException e) {
            throw new MeetingException(e.getMessage(),e);
        } catch (DaoException e) {
            throw new MeetingException(e.getMessage(),e);
        } catch (DataObjectNotFoundException e) {
            throw new MeetingException(e.getMessage(),e);
        }

    }

    public void moveDown(String itemId) throws MeetingException {
        try {
            AgendaItem agendaItem = getAgendaItem(itemId);
            int order = agendaItem.getItemOrder();
            MeetingDao dao = (MeetingDao) getDao();
            dao.moveDown(itemId,agendaItem.getParentId(),order);
        } catch (MeetingException e) {
            throw new MeetingException(e.getMessage(),e);
        } catch (DaoException e) {
            throw new MeetingException(e.getMessage(),e);
        } catch (DataObjectNotFoundException e) {
            throw new MeetingException(e.getMessage(),e);
        }

    }


}
