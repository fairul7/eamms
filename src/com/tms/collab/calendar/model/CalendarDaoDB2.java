package com.tms.collab.calendar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import kacang.model.DaoException;


public class CalendarDaoDB2 extends CalendarDao{

    public Collection selectCalendarEvents(String search, String type, Date from, Date to,String userId, String[] userIds, String[] resourceIds, boolean onlyReminders, boolean includeUniversal, boolean includeDeleted, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection eventList = new ArrayList();
            Collection argList = new ArrayList();

            // formulate search clause
            String searchClause = "";
            if (search != null && search.trim().length() > 0) {
                searchClause = " AND (UPPER(ce.title) LIKE ? OR UPPER(ce.description) LIKE ?)";
                search = "%" + search.toUpperCase() + "%";
                argList.add(search);
                argList.add(search);
            }

            // formulate type clause
            String typeClause = "";
            if (type != null && type.trim().length() > 0) {
                typeClause = " AND (ce.eventId LIKE ?)";
                type += "%";
                argList.add(type);
            }

            // formulate user clause
            boolean ownerIncluded = false;
            String userClause = "";
            String userJoin = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " AND ((cea.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += (includeUniversal) ? ") OR (universal='1')) " : "))";
                for (int i = 0; i < userIds.length; i++)
                {
                    String tempId = userIds[i];
                    if(tempId.equals(userId))        {
                        userClause +=" OR(ce.userId=?))";
                        ownerIncluded = true;
                        break;
                    }
                }
                if(!ownerIncluded) userClause += ") ";
                userJoin = " LEFT JOIN cal_event_attendee cea ON ce.eventId = cea.eventId ";
            }

            // formulate resource clause
            String resourceClause = "";
            String resourceJoin = "";
            if (resourceIds != null && resourceIds.length > 0) {
                resourceClause = " AND cer.resourceId IN (" + quote(resourceIds[0]);
                for (int i=1; i<resourceIds.length; i++) {
                    resourceClause += ", " + quote(resourceIds[i]);
                }
                resourceClause += ") ";
                resourceJoin = " LEFT JOIN cal_event_resource cer ON ce.eventId = cer.eventId ";
            }

            // formulate range clause
            String rangeClause = (!onlyReminders) ?
                    " AND ((startDate >= ? AND startDate <= ?) OR (startDate <= ? AND endDate >= ?)) " /*+
                    "OR (startDate IS NULL AND endDate <= ?))"*/ :
                    " AND (ce.reminderDate >= ? AND ce.reminderDate <= ?) ";

            // formulate deleted flag clause
            String deletedClause = (includeDeleted) ? "" : " AND deleted='0' ";

            // formulate SORT clause
            String sortClause = " ORDER BY ";
            if (sort != null && sort.trim().length() > 0) {
                sortClause += sort;
            }
            else {
                sortClause += "startDate";
            }
            if (desc) {
                sortClause += " DESC";
            }

            // formulate SQL SELECT statement
            String sql =
                    "SELECT DISTINCT ce.eventId, ce.userId, startDate, endDate, allDay, ce.reminderDate, " +
                            "title, description, agenda, summary, location, status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule,creationDate, " +
                            "priority, universal, modified, new, archived, deleted, lastModified, lastModifiedBy, reminderSent " +
                            "FROM cal_event ce " +
                            userJoin +
                            resourceJoin +
                            "WHERE 1=1" +
                            searchClause +
                            typeClause +
                            rangeClause +
                            deletedClause +
                            userClause +
                            resourceClause +
                            sortClause;

            // construct argument array
            Object[] args;
            if(ownerIncluded){
                args = (!onlyReminders) ?
                        new Object[] {
                                from,
                                to,
                                from,
                                from,
                                userId

                                /*,
                                to*/
                        } :
                        new Object[] {
                                from,
                                to,userId
                        } ;
            } else{
                args = (!onlyReminders) ?
                        new Object[] {
                                from,
                                to,
                                from,
                                from,
                        } :
                        new Object[] {
                                from,
                                to
                        } ;
            }
            argList.addAll(Arrays.asList(args));
            args = argList.toArray();

            // execute query
            eventList = super.select(sql, CalendarEvent.class, args, start, rows);
            return eventList;
        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
}
