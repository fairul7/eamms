package com.tms.collab.calendar.model;

import kacang.model.DaoException;

import java.util.*;

public class CalendarDaoMsSql extends CalendarDao {
    public CalendarEvent getNextRecurringEvent(Date from,String[] userIds,boolean next) throws DaoException{
        try {
            // formulate user clause
            String userClause = "";
            String userJoin = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " AND (cea.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause +=  ") OR (universal='1')) ";
                userJoin = " LEFT JOIN cal_event_attendee cea ON ce.eventId = cea.eventId ";
            }
            String  resourceJoin = " LEFT JOIN cal_event_resource cer ON ce.eventId = cer.eventId ";

            String sql =
                    "SELECT TOP 1 ce.eventId, ce.userId, ci.startDate, ci.endDate, allDay, ci.reminderDate, " +
                            "title, description, agenda, summary, location, status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, new, archived, deleted, lastModified, lastModifiedBy, reminderSent " +
                            "FROM cal_event ce,cal_instance ci " +
                            userJoin +
                            resourceJoin+
                            "WHERE ci.eventId = ce.eventId AND ci.startDate"+(next?">":"<" )+ "?" +
                            userClause +
                            "ORDER BY startDate "+(next?"" : "DESC") + " ";

            // construct argument array
            Object[] args =    new Object[] {
                    from
            };

            // execute query
            Collection col = super.select(sql, CalendarEvent.class, args, 0, -1);
            Iterator i = col.iterator();
            CalendarEvent e =  null;
            if(i.hasNext()){
                e = (CalendarEvent)i.next();
                e.setRecurrence(true);
            }
            return e;
        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public CalendarEvent getNextEvent(Date from,String[] userIds,boolean next) throws DaoException{
        try {
            // formulate user clause
            String userClause = "";
            String userJoin = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " AND (cea.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause +=  ") OR (universal='1')) ";
                userJoin = " LEFT JOIN cal_event_attendee cea ON ce.eventId = cea.eventId ";
            }
            String sql =
                    "SELECT TOP 1 ce.eventId, ce.userId, startDate, endDate, allDay, ce.reminderDate, " +
                            "title, description, agenda, summary, location, status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, new, archived, deleted, lastModified, lastModifiedBy, reminderSent " +
                            "FROM cal_event ce " +
                            userJoin +
                            "WHERE startDate"+(next?">":"<" )+ "?" +
                            userClause +
                            "ORDER BY startDate "+(next?"" : "DESC") + " ";

            // construct argument array
            Object[] args =    new Object[] {
                    from
            };

            // execute query
            Collection col = super.select(sql, CalendarEvent.class, args, 0, -1);
            Iterator i = col.iterator();
            CalendarEvent e =  null;
            if(i.hasNext())
                e = (CalendarEvent)i.next();
            return e;
            //return null;
        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public Collection selectRecurringCalendarEvents(String eventId) throws DaoException {
        try {
            Collection eventList = new ArrayList();

            // formulate user clause

            // formulate deleted flag clause
            String deletedClause = " AND deleted='0' ";

            // formulate SQL SELECT statement
            String sql =
                    "SELECT ce.eventId, ce.userId, ci.startDate, ci.endDate, allDay, ci.reminderDate, title, description, agenda, summary, " +
                            "location, status, categories, classification, lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, new, archived, deleted, lastModified, lastModifiedBy, reminderSent,creationDate " +
                            "FROM cal_event ce ,cal_instance ci " +
                            "WHERE ci.eventId=? AND ce.eventId=ci.eventId " +
                            //ci.lastRecurrenceDate //ci.startDate <= ? AND  >= ? //AND (recurrenceDates IS NOT NULL OR recurrenceRule IS NOT NULL)
                            deletedClause +
                            "ORDER BY ci.startDate";

            // construct argument array
            // execute query
            eventList = super.select(sql, CalendarEvent.class,new String[]{eventId}, 0, -1);
            for(Iterator i = eventList.iterator();i.hasNext();){
                ((CalendarEvent)i.next()).setRecurrence(true);
            }
            return eventList;
        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
}
