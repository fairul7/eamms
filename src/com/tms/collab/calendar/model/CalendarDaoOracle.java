package com.tms.collab.calendar.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoQuery;
import kacang.util.Transaction;
import java.util.*;

public class CalendarDaoOracle extends CalendarDao
{
    public void init() throws DaoException {
        super.update("CREATE TABLE cal_event (eventId varchar(255) NOT NULL default '', " +
                "startDate datetime default NULL, endDate datetime default NULL, " +
                "reminderDate datetime default NULL, description text, " +
                "agenda text, summary varchar(255) default NULL, " +
                "location varchar(255) default NULL, " +
                "status varchar(255) default NULL, categories varchar(255) default NULL, " +
                "classification varchar(255) default NULL, recurrenceId varchar(255) default NULL, " +
                "recurrenceDates varchar(255) default NULL, recurrenceRule varchar(255) default NULL, " +
                "priority int(10) default '0', modified char(1) default '0', \"NEW\" char(1) default '0', " +
                "archived char(1) default '0', deleted char(1) NOT NULL default '0', " +
                "lastModified datetime default NULL, lastModifiedBy varchar(255) default '0', " +
                "lastRecurrenceDate datetime default NULL,creationDate datetime default NULL, allDay char(1) default '0', " +
                "title varchar(255) NOT NULL default '', reminderSent char(1) default '0', " +
                "userId varchar(255) default '0', universal char(1) default '0', " +
                "PRIMARY KEY  (eventId), KEY startDate (startDate), KEY endDate (endDate), " +
                "KEY deleted (deleted))", null);

        super.update("CREATE TABLE cal_instance (" +
                "eventId varchar(255) NOT NULL default ''," +
                "startDate datetime default NULL," +
                "endDate datetime default NULL," +
                "reminderDate datetime default NULL" +
                ")",null);

        super.update("CREATE TABLE cal_event_attendee (eventId varchar(255) NOT NULL default '', " +
                "userId varchar(255) default NULL, username varchar(255) default NULL, " +
                "firstName varchar(255) default NULL, lastName varchar(255) default NULL, " +
                "attendeeId varchar(255) NOT NULL default '', compulsory char(1) default '0', " +
                "KEY eventId (eventId), KEY userId (userId), KEY attendeeId (attendeeId))", null);

        super.update("CREATE TABLE cal_event_attendee_status (eventId varchar(255) NOT NULL default '', " +
                "instanceId varchar(255) default NULL, status varchar(255) default NULL, " +
                "attendeeId varchar(255) default NULL, comments varchar(255) default NULL, " +
                "KEY eventId (eventId), KEY instanceId (instanceId), " +
                "KEY attendeeId (attendeeId))", null);

        super.update("CREATE TABLE cal_event_resource (eventId varchar(255) default NULL, " +
                "resourceId varchar(255) default NULL, KEY eventId (eventId), " +
                "KEY resourceId (resourceId))", null);
    }

    /**
     * Inserts an event into the database.
     * @param event The event object to insert.
     * @throws kacang.model.DaoException when a database error occurs.
     */
    public void insertCalendarEvent(CalendarEvent event) throws DaoException {
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();
            // insert calendar event
            String sql =
                    "INSERT INTO cal_event (eventId, startDate, endDate, allDay, reminderDate, title, description, agenda, summary, " +
                            "location, status, categories, classification, recurrenceId, recurrenceDates, recurrenceRule, lastRecurrenceDate, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, userId, reminderSent,creationDate) " +
                            "VALUES (#eventId#, #startDate#, #endDate#, #allDay#, #reminderDate#, #title#, #description#, #agenda#, #summary#, " +
                            "#location#, #status#, #categories#, #classification#, #recurrenceId#, #recurrenceDates#, #recurrenceRule#, #lastRecurrenceDate#, " +
                            "#priority#, #universal#, #modified#, #new#, #archived#, #deleted#, #lastModified#, #lastModifiedBy#, #userId#, #reminderSent#,#creationDate#)";
            tx.update(sql, event);
            // insert calendar event attendees
            if (event.getAttendees() != null && event.getAttendees().size() > 0) {
                sql = "INSERT INTO cal_event_attendee (eventId, userId, username, firstName, lastName, attendeeId, compulsory) " +
                        "VALUES (#eventId#, #userId#, #username#, #firstName#, #lastName#, #attendeeId#, #compulsory#)";
                String sql2 = "INSERT INTO cal_event_attendee_status (eventId, instanceId, attendeeId, status, comments) VALUES (#eventId#, #instanceId#, #attendeeId#, #status#, #comments#)";
                for (Iterator i=event.getAttendees().iterator(); i.hasNext();) {
                    Attendee attendee = (Attendee)i.next();
                    tx.update(sql, attendee);
                    tx.update(sql2, attendee);
                }
            }
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null)
                tx.rollback();
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

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
                    "SELECT ce.eventId, ce.userId, ci.startDate, ci.endDate, allDay, ci.reminderDate, " +
                            "title, description, agenda, summary, location, status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent " +
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
            Collection col = super.select(sql, CalendarEvent.class, args, 0, 1);
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
                    "SELECT ce.eventId, ce.userId, startDate, endDate, allDay, ce.reminderDate, " +
                            "title, description, agenda, summary, location, status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent " +
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
            Collection col = super.select(sql, CalendarEvent.class, args, 0, 1);
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

    /**
     * Returns a specific event based on the event's unique identifier, including associated attendees and resources.
     * @param eventId The unique ID to identify the event.
     * @return A CalendarEvent object representing the desired event.
     * @throws kacang.model.DataObjectNotFoundException when the event does not exist.
     * @throws kacang.model.DaoException when a database error occurs.
     */
    public CalendarEvent selectCalendarEvent(String eventId,Class resultClass) throws DataObjectNotFoundException, DaoException {
        try {
            CalendarEvent event = null;
            // select event
            String sql =
                    "SELECT ce.eventId, ce.userId, startDate, endDate, allDay, ce.reminderDate, title, description, agenda, summary, " +
                            "location, status, categories, classification, lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent,creationDate " +
                            "FROM cal_event ce WHERE eventId=?";
            Object[] args = new Object[] {
                    eventId
            };
            Collection eventList = super.select(sql, resultClass, args, 0, -1);
            if (eventList.size() == 0)
                throw new DataObjectNotFoundException();
            event = (CalendarEvent)eventList.iterator().next();
            // select attendees
            sql = "SELECT eventId, userId, attendeeId, username, firstName, lastName, compulsory " +
                    "FROM cal_event_attendee " +
                    "WHERE eventId=?";
            args = new Object[] {
                    eventId
            };
            Collection attendeeList = super.select(sql, Attendee.class, args, 0, -1);
            event.setAttendees(attendeeList);
            return event;
        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    /**
     * Returns a Collection of CalendarEvent objects within a specified date range.
     * Only original events are returned.
     * Recurring instances are NOT returned.
     * This does NOT return attendees and resources for each event.
     * @param search A string to search for in the title or description. Use null to return all.
     * @param type The class name of event, e.g. com.tms.collab.calendar.model.Appointment. Use null to return all.
     * @param from The start of the required date range.
     * @param to The end of the required date range.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param onlyReminders Set true to search for reminders only.
     * @param includeUniversal Set true to include universal events.
     * @param includeDeleted Set true to include events with the deleted flag.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws kacang.model.DaoException when a database error occurs.
     */
    public Collection selectCalendarEvents(String search, String type, Date from, Date to,String userId, String[] userIds, String[] resourceIds, boolean onlyReminders, boolean includeUniversal, boolean includeDeleted, String sort, boolean desc, int start, int rows) throws DaoException {
        try {
            Collection eventList = new ArrayList();
            Collection argList = new ArrayList();

            // formulate search clause
            String searchClause = "";
            if (search != null && search.trim().length() > 0) {
                searchClause = " AND (ce.title LIKE ? OR ce.description LIKE ?)";
                search = "%" + search + "%";
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
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent " +
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

    public Collection selectCalendarEvents(DaoQuery properties, boolean includeDeleted,int startIndex,int maxResults,String sort, boolean descending)throws DaoException{
        try {
            Collection eventList = new ArrayList();
            String deletedClause = (includeDeleted) ? "" : " AND deleted='0' ";
            String strSort = "";
            if (sort != null) {
                strSort += " ORDER BY " + sort;
                if (descending)
                    strSort += " DESC";
            }
            String sql =
                    "SELECT ce.eventId, ce.userId, startDate, endDate, allDay, ce.reminderDate, " +
                            "title, description, agenda, summary, location, status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule,creationDate, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent " +
                            "FROM cal_event ce " + properties.getStatement() + deletedClause + strSort;
            eventList  =  super.select(sql,CalendarEvent.class,properties.getArray(),startIndex,maxResults);
            return eventList;
        }catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    /**
     * Returns a Collection of CalendarEvent objects that span a specified date range,
     * to check for conflicts.
     * Only original events are returned.
     * Recurring instances are NOT returned.
     * This does NOT return attendees and resources for each event.
     * @param from The start of the required date range.
     * @param to The end of the required date range.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeUniversal Set true to include universal events.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws kacang.model.DaoException when a database error occurs.
     */
    public Collection selectCalendarEventConflicts(Date from, Date to, String[] userIds, String[] resourceIds, boolean includeUniversal) throws DaoException {
        try {
            Collection eventList = new ArrayList();

            // formulate user clause
            String userClause = "";
            String userJoin = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " AND (cea.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += (includeUniversal) ? ") OR (universal='1')) " : ")) ";
                userJoin = " INNER JOIN cal_event_attendee cea ON ce.eventId = cea.eventId ";
                userJoin += " INNER JOIN cal_event_attendee_status ceas ON cea.attendeeId = ceas.attendeeId AND ceas.status <> '" + CalendarModule.ATTENDEE_STATUS_REJECT + "' ";
            }

            // formulate deleted flag clause
            String deletedClause = " AND deleted='0' ";

            // formulate SQL SELECT statement
            String sql =
                    "SELECT ce.eventId, ce.userId, startDate, endDate, allDay, ce.reminderDate, " +
                            "title, description, agenda, summary, location, ce.status, categories, classification, " +
                            "lastRecurrenceDate, recurrenceDates, recurrenceRule,creationDate, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent " +
                            "FROM cal_event ce " +
                            userJoin +
                            //   resourceJoin +
                            "WHERE (" +
                            "(startDate <= ? AND endDate > ?) OR " +
                            "(startDate > ? AND startDate < ?) OR" +
                            "(startDate = ? AND endDate = ?) " +
                            ") " +
                            deletedClause +
                            userClause +
                            //     resourceClause +
                            "ORDER BY startDate";

            // construct argument array
            Object[] args = new Object[] {
                    from,
                    from,
                    from,
                    to,
                    from,
                    to
            };

            // execute query
            eventList = super.select(sql, CalendarEvent.class, args, 0, -1);
            return eventList;
        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    /**
     * Returns a Collection of CalendarEvent objects that are recurring up to a specified date.
     * Only original events are returned.
     * Recurring instances are NOT returned.
     * This does NOT return attendees and resources for each event.
     * @param from The date up to start the search.
     * @param to The date up to which to search.
     * @param userIds An array of User IDs - indicating the required users. Use null to return events for all users.
     * @param resourceIds An array of Resource IDs - indicating the required resources. Use null to return events for all resources.
     * @param includeUniversal Set true to include universal events.
     * @return A Collection of CalendarEvent objects, never null.
     * @throws kacang.model.DaoException when a database error occurs.
     */
    public Collection selectRecurringCalendarEvents(String search, String type, Date from, Date to, String[] userIds, String[] resourceIds, boolean onlyReminders,boolean includeUniversal) throws DaoException {
        try {
            Collection eventList = new ArrayList();
            Collection argList = new ArrayList();

            // formulate search clause
            String searchClause = "";
            if (search != null && search.trim().length() > 0) {
                searchClause = " AND (ce.title LIKE ? OR ce.description LIKE ?)";
                search = "%" + search + "%";
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
            String userClause = "";
            String userJoin = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " AND (cea.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += (includeUniversal) ? ") OR (universal='1')) " : ")) ";
                userJoin = " LEFT JOIN cal_event_attendee cea ON ce.eventId = cea.eventId ";
            }

            String rangeClause = (!onlyReminders) ?
                    " AND ((ci.startDate >= ? AND ci.startDate <= ?) OR (ci.startDate <= ? AND ci.endDate >= ?)) " :
                    " AND (ci.reminderDate >= ? AND ci.reminderDate <= ?) ";

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

            // formulate deleted flag clause
            String deletedClause = " AND deleted='0' ";

            // formulate SQL SELECT statement
            String sql =
                    "SELECT ce.eventId, ce.userId, ci.startDate, ci.endDate, allDay, ci.reminderDate, title, description, agenda, summary, " +
                            "location, status, categories, classification, lastRecurrenceDate, recurrenceDates, recurrenceRule, " +
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent,creationDate " +
                            "FROM cal_event ce INNER JOIN cal_instance ci ON (ce.eventId=ci.eventId) " +
                            userJoin +
                            resourceJoin +
                            "WHERE 1=1 " +
                            searchClause +
                            typeClause +
                            rangeClause +
                            //ci.lastRecurrenceDate //ci.startDate <= ? AND  >= ? //AND (recurrenceDates IS NOT NULL OR recurrenceRule IS NOT NULL)
                            deletedClause +
                            userClause +
                            resourceClause +
                            "ORDER BY ci.startDate";

            // construct argument array
            Object[] args =(!onlyReminders)?
                    new Object[] {
                            from,
                            to,
                            from,
                            from,
                    } :
                    new Object[]{
                            from,to
                    };
            argList.addAll(Arrays.asList(args));
            args = argList.toArray();

            // execute query
            eventList = super.select(sql, CalendarEvent.class, args, 0, -1);
            for(Iterator i = eventList.iterator();i.hasNext();){
                if(onlyReminders)
                    ((CalendarEvent)i.next()).setReminder(true);
                else
                    ((CalendarEvent)i.next()).setRecurrence(true);
            }
            return eventList;
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
                            "priority, universal, modified, \"NEW\", archived, deleted, lastModified, lastModifiedBy, reminderSent,creationDate " +
                            "FROM cal_event ce ,cal_instance ci " +
                            "WHERE ci.eventId=? AND ce.eventId=ci.eventId " +
                            //ci.lastRecurrenceDate //ci.startDate <= ? AND  >= ? //AND (recurrenceDates IS NOT NULL OR recurrenceRule IS NOT NULL)
                            deletedClause +
                            "ORDER BY startDate";

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

    /**
     * Updates a calendar event's status and status flags.
     * @param event Event to update.
     * @throws kacang.model.DaoException
     */
    public void updateCalendarEventStatus(CalendarEvent event) throws DaoException {
        try {
            // insert calendar event
            String sql =
                    "UPDATE cal_event SET status=#status#, reminderSent=#reminderSent#, modified=#modified#, \"NEW\"=#new#, deleted=#deleted#, lastModified=#lastModified#, lastModifiedBy=#lastModifiedBy# " +
                            " WHERE eventId = #eventId#";
            super.update(sql, event);

        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }
}
