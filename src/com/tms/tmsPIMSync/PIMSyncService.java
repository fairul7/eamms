package com.tms.tmsPIMSync;

import com.caucho.hessian.server.HessianServlet;
import com.funambol.foundation.pdi.contact.Contact;
import com.funambol.foundation.pdi.converter.ConverterException;
import com.funambol.foundation.pdi.utils.SourceUtils;
import com.funambol.foundation.pdi.event.Event;
import com.funambol.foundation.pdi.event.SIFE;
import com.funambol.foundation.pdi.task.SIFT;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.calendar.model.*;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.tmsPIMSync.exception.PIMSyncServiceException;
import com.tms.tmsPIMSync.model.SyncEvent;
import com.tms.tmsPIMSync.model.PIMSyncModule;
import com.tms.tmsPIMSync.model.TaskMasker;
import com.tms.tmsPIMSync.model.SyncTransactionModule;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Calendar;

/**
 * timezone is set to UTC time. All time coming in and going out should be in UTC time.
 */
public class PIMSyncService extends HessianServlet implements EKPSyncAPI, Serializable{
    String query, folderId, companyId, currentSyncUserId, sort, username, sessionId;
    int start, rows;
    Boolean approved, contactGroup;

    private boolean initialize(Properties syncContext){
        String username = (String) syncContext.get("username");
        String password = (String) syncContext.get("password");
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);

        try {
            Collection col = ss.getUsersByUsername(username);
            User user = (User) col.iterator().next();

            query = null;
            folderId = null;
            currentSyncUserId = user.getId();
            companyId = null;
            sort = null;
            start = 0;
            rows = -1;
            approved = null;
            sessionId = syncContext.getProperty("sessionId");
            contactGroup = null;
            this.username = username;
            System.setProperty("file.encoding", "UTF-8");
            if(user.getPassword().equals(password)){
                return true;
            }
        } catch (kacang.services.security.SecurityException e) {
            Log.getLog(getClass()).debug("Couldnt find user: " + username);
        }
        Log.getLog(getClass()).info(MessageFormat.format("User {0} authentication failed. with password {1}", new Object[]{username, password}));
        return false;
    }

    public String[] getAllContactKeys(Properties syncContext) {
        String[] keys = null;
        Log.getLog(getClass()).info("get all contacts: " + username );
        try {
            if(!initialize(syncContext)) return null;
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);

            Collection contacts = am.getContactList(query, folderId, companyId, currentSyncUserId, approved, contactGroup, sort, true, start, rows);

            keys = new String[contacts.size()];
            int i = 0;

            for(Iterator itr=contacts.iterator(); itr.hasNext();i++){
                com.tms.collab.directory.model.Contact contact  = (com.tms.collab.directory.model.Contact) itr.next();
                keys[i] = contact.getId();
            }

            return keys;
        } catch (AddressBookException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getUpdatedContactKeys(Properties syncContext, String since, String until) {
        String[] keys;
        try {
            if(!initialize(syncContext)) return null;
            Log.getLog(getClass()).info("get updated contacts: " + username );
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            Date sinceDate = null, untilDate = null;
            if(since != null){
                sinceDate = VCardUtils.format.parse(since);
            }
            if(until != null){
                untilDate = VCardUtils.format.parse(until);
            }

            Collection contacts = am.getUpdatedContactList(currentSyncUserId, sinceDate, untilDate);

            keys = new String[contacts.size()];
            int i = 0;

            for(Iterator itr=contacts.iterator(); itr.hasNext();i++){
                com.tms.collab.directory.model.Contact contact  = (com.tms.collab.directory.model.Contact) itr.next();
                keys[i] = contact.getId();
            }
            return keys;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getDeletedContactKeys(Properties syncContext, String since, String until) {
        try {
            if(!initialize(syncContext)) return null;
            Log.getLog(getClass()).info("get deleted contacts: " + username );
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            Date sinceDate = null, untilDate = null;
            if(since != null)
                sinceDate = VCardUtils.format.parse(since);
            if(until != null){
                untilDate = VCardUtils.format.parse(until);
            }

            Collection res = am.getDeletedContacts(currentSyncUserId, sinceDate, untilDate);
            if(res != null){
                if(res.size() > 0){
                    String[] deletedKeys = new String[res.size()];
                    int i = 0;
                    for(Iterator itr=res.iterator(); itr.hasNext();i++){
                        deletedKeys[i] = (String) itr.next();
                    }
                    return deletedKeys;
                }else return new String[0];
            }
            return null;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getNewContactKeys(Properties syncContext, String since, String until) {
        String[] keys = null;
        try {
            if(!initialize(syncContext)) return null;
            Log.getLog(getClass()).info("get new contacts: " + username );
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            Date sinceDate = null, untilDate = null;
            if(since != null){
                sinceDate = VCardUtils.format.parse(since);
            }
            if(until != null){
                untilDate = VCardUtils.format.parse(until);
            }

            Collection contacts = am.getNewContactList(currentSyncUserId, sinceDate, untilDate);

            keys = new String[contacts.size()];
            int i = 0;

            for(Iterator itr=contacts.iterator(); itr.hasNext();i++){
                com.tms.collab.directory.model.Contact contact  = (com.tms.collab.directory.model.Contact) itr.next();
                keys[i] = contact.getId();
            }

            return keys;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getContactByUID(Properties syncContext, String uid) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get contacts by uid: " + username + ":" + uid);
        try {
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            com.tms.collab.directory.model.Contact contact = am.getContact(uid);
            if(!contact.getOwnerId().equals(currentSyncUserId)){
                return null;
            }

            VCardUtils util = new VCardUtils();

            return util.ekpContactToXml(contact);
        } catch (PIMSyncServiceException e) {
            e.printStackTrace();
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();
        } catch (AddressBookException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeContact(Properties syncContext, String id) {
        try {
            if(!initialize(syncContext)) return;
            Log.getLog(getClass()).info("remove contact: " + username + ":" + id );
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            am.deleteContact(id, currentSyncUserId);
        } catch (AddressBookException e) {

        }
    }

    public String[] getContactsTwinKeys(Properties syncContext, String xmlContact) {
        try {
            if(!initialize(syncContext)) return null;
            Log.getLog(getClass()).info("find contact twin: " + username + ":" + xmlContact );
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            com.tms.collab.directory.model.Contact ekpCon = null;
            VCardUtils util = new VCardUtils();
            Contact foundationContact = util.getFoundationContactFromXML(xmlContact);
            ekpCon = util.foundationContactToEkpContact(foundationContact);
            ekpCon.setOwnerId(currentSyncUserId);
            Collection col = am.getContactsByCriteria(ekpCon);
            if(col.size() > 0){
                String contactId = ((com.tms.collab.directory.model.Contact) col.iterator().next()).getId();
                Log.getLog(getClass()).info("twin found: " + contactId);
                return new String[]{contactId};
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String insertContactCommit(String userId, String contactId, String contactToSet, Date modifiedTime) {
        try {
            VCardUtils util = new VCardUtils();
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            com.tms.collab.directory.model.Contact ekpCon = null;
            Contact foundationContact = util.getFoundationContactFromXML(contactToSet);
            ekpCon = util.foundationContactToEkpContact(foundationContact);
            ekpCon.setCreatedTime(modifiedTime);
            ekpCon.setModifiedTime(modifiedTime);
            ekpCon.setCreated(true);
            ekpCon.setApproved(true);
            ekpCon.setId(contactId);
            ekpCon = am.addContact(ekpCon, currentSyncUserId, modifiedTime);
            return util.ekpContactToXml(ekpCon);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String insertContact(Properties syncContext, String contactToSet) {
        try {
            if(!initialize(syncContext)) return null;
            Log.getLog(getClass()).info("insert contact: " + username + ":" + contactToSet );
            SyncTransactionModule stm = (SyncTransactionModule) Application.getInstance().getModule(SyncTransactionModule.class);

            VCardUtils util = new VCardUtils();
            SyncServiceCalHelper calUtil = new SyncServiceCalHelper();

            Contact foundationContact = util.getFoundationContactFromXML(contactToSet);
            Date modifiedTime = calUtil.dateParser(syncContext.getProperty("date_modified"), new Date());
            String id = UuidGenerator.getInstance().getUuid();
            foundationContact.setUid(id);
            contactToSet = util.foundationContactsToXml(foundationContact);
            stm.saveTransaction(id, sessionId, currentSyncUserId, "insertContact", contactToSet, modifiedTime);
            return contactToSet;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateContact(Properties syncContext, String contactToSet) {
        try {
            if(!initialize(syncContext)) return null;
            Log.getLog(getClass()).info("update contact: " + username + ":" + contactToSet);
            AddressBookModule am = (AddressBookModule) Application.getInstance().getModule(AddressBookModule.class);
            com.tms.collab.directory.model.Contact ekpCon = null;
            VCardUtils util = new VCardUtils();
            SyncServiceCalHelper calUtil = new SyncServiceCalHelper();
            Contact foundationContact = util.getFoundationContactFromXML(contactToSet);
            ekpCon = util.foundationContactToEkpContact(foundationContact);

            Date modifiedTime = calUtil.dateParser(syncContext.getProperty("date_modified"), new Date());

            com.tms.collab.directory.model.Contact ekpConServer = am.getContact(ekpCon.getId());


            ekpConServer.setTitle(ekpCon.getTitle());
            ekpConServer.setFirstName(ekpCon.getFirstName());
            ekpConServer.setMiddleName(ekpCon.getMiddleName());
            ekpConServer.setLastName(ekpCon.getLastName());
            ekpConServer.setNickName(ekpCon.getNickName());
            ekpConServer.setEmail(ekpCon.getEmail());
            ekpConServer.setMobile(ekpCon.getMobile());
            ekpConServer.setDesignation(ekpCon.getDesignation());
            ekpConServer.setCompany(ekpCon.getCompany());
            ekpConServer.setAddress(ekpCon.getAddress());
            ekpConServer.setCity(ekpCon.getCity());
            ekpConServer.setState(ekpCon.getState());
            ekpConServer.setPostcode(ekpCon.getPostcode());
            ekpConServer.setCountry(ekpCon.getCountry());
            ekpConServer.setPhone(ekpCon.getPhone());
            ekpConServer.setFax(ekpCon.getFax());
            ekpConServer.setComments(ekpCon.getComments());
            ekpConServer.setExtension(ekpCon.getExtension());

            ekpConServer = am.updateContact(ekpConServer, currentSyncUserId, modifiedTime);
            String returnXml = util.ekpContactToXml(ekpConServer);
            Log.getLog(getClass()).info(returnXml);
            return returnXml;
        } catch (IOException e) {
            Log.getLog(getClass()).error("IO Exception", e);
        } catch (ParserConfigurationException e) {
            Log.getLog(getClass()).error("Error parsing contact", e);
        } catch (AddressBookException e) {
            Log.getLog(getClass()).error("Error in addressbook", e);
        } catch (ParseException e) {
            Log.getLog(getClass()).error("Error parsing contact", e);
        } catch (ConverterException e) {
            Log.getLog(getClass()).error("Error converting contact", e);
        } catch (SAXException e) {
            Log.getLog(getClass()).error("SAXException", e);
        } catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Contact which should exist not found.", e);
        } catch (Exception e) {
            Log.getLog(getClass()).error(e);
        }
        return null;
    }

    public boolean isEKPUser(Properties syncAuth) {
        return initialize(syncAuth);
    }

    //-------------------------------calendar methods----------------------------------

    public String[] getUpdatedCalendarKeys(Properties syncContext, String since, String until) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get updated Calendars: " + username );
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        Date sinceDate = null, untilDate = null;

        try {
            if (since != null) {
                sinceDate = SyncServiceCalHelper.format.parse(since);

            }
            if (until != null) {
                untilDate = SyncServiceCalHelper.format.parse(until);

            }
            Collection keys = cm.getCalendarKeys(currentSyncUserId, new String[]{currentSyncUserId}, true, false, sinceDate, untilDate, "update");

            Collection filtered = new ArrayList();
            for(Iterator itr=keys.iterator(); itr.hasNext();){
                String key = (String) itr.next();
                if(key.startsWith(SyncEvent.EKP_APPOINTMENT) || key.startsWith(SyncEvent.EKP_EVENT)){
                    filtered.add(key);
                }
            }

            String[] filteredKeys = new String[filtered.size()];
            int i = 0;
            for(Iterator itr=filtered.iterator(); itr.hasNext(); i++){
                filteredKeys[i] = (String) itr.next();

            }

            return filteredKeys;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getDeletedCalendarKeys(Properties syncContext, String since, String until) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get deleted calendars: " + username );
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        Date sinceDate = null, untilDate = null;

        try {
            if (since != null) {
                sinceDate = SyncServiceCalHelper.format.parse(since);

            }
            if (until != null) {
                untilDate = SyncServiceCalHelper.format.parse(until);

            }
            Collection keys = cm.getCalendarKeys(currentSyncUserId, new String[]{currentSyncUserId}, true, true, sinceDate, untilDate, "deleted");

            Collection filtered = new ArrayList();
            for(Iterator itr=keys.iterator(); itr.hasNext();){
                String key = (String) itr.next();
                if(key.startsWith(SyncEvent.EKP_APPOINTMENT) || key.startsWith(SyncEvent.EKP_EVENT)){
                    filtered.add(key);
                }
            }

            String[] filteredKeys = new String[filtered.size()];
            int i = 0;
            for(Iterator itr=filtered.iterator(); itr.hasNext(); i++){
                filteredKeys[i] = (String) itr.next();
            }

            return filteredKeys;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getNewCalendarKeys(Properties syncContext, String since, String until) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get new calendars: " + username );
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        try {
            Date sinceDate = null, untilDate = null;
            if(since != null){
                sinceDate = SyncServiceCalHelper.format.parse(since);

            }
            if(until != null){
                untilDate = SyncServiceCalHelper.format.parse(until);

            }


            Collection keys = cm.getCalendarKeys(currentSyncUserId, new String[]{currentSyncUserId}, true, false, sinceDate, untilDate, "new");

            Collection filtered = new ArrayList();
            for(Iterator itr=keys.iterator(); itr.hasNext();){
                String key = (String) itr.next();
                if(key.startsWith(SyncEvent.EKP_APPOINTMENT) || key.startsWith(SyncEvent.EKP_EVENT)){
                    filtered.add(key);
                }
            }

            String[] filteredKeys = new String[filtered.size()];
            int i = 0;
            for(Iterator itr=filtered.iterator(); itr.hasNext(); i++){
                filteredKeys[i] = (String) itr.next();

            }


            return filteredKeys;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * EKP users normally don't delete an event after completion. Therefore, for performance reasons,
     * will only return all calendar since 6 months ago from NOW.
     * @param syncContext
     * @return String[] an array of keys
     */
    public String[] getAllCalendarKeys(Properties syncContext) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get all calendars: " + username );

        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        try {
            Calendar from = Calendar.getInstance();
            from.setLenient(true);
            from.set(Calendar.MONTH, from.get(Calendar.MONTH) - 6);

            Collection keys = cm.getCalendarKeys(currentSyncUserId, new String[]{currentSyncUserId}, true, false, from.getTime(), new Date(), "all");

            Collection filtered = new ArrayList();
            for(Iterator itr=keys.iterator(); itr.hasNext();){
                String key = (String) itr.next();
                if(key.startsWith(SyncEvent.EKP_APPOINTMENT) || key.startsWith(SyncEvent.EKP_EVENT)){
                    filtered.add(key);
                }
            }

            String[] filteredKeys = new String[filtered.size()];
            int i = 0;
            for(Iterator itr=filtered.iterator(); itr.hasNext(); i++){
                filteredKeys[i] = (String) itr.next();
            }
            return filteredKeys;
        }catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCalendarByUID(Properties syncContext, String uid) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get calendaar by id: " + username + " : " + uid );

        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        try {

            CalendarEvent ce = cm.getCalendarEvent(uid);
            SyncServiceCalHelper util = new SyncServiceCalHelper();
            // check if this person can view this calendar object or not

            // if is an EVENT, dont need to check
            if(uid.startsWith(CalendarEvent.class.getName())){
                return util.ekpCalendarEventToXml(ce);
            }
            boolean validView = currentSyncUserId.equals(ce.getUserId());
            Collection attendees = ce.getAttendees();
            for (Iterator i=attendees.iterator(); i.hasNext();) {
                Attendee attendee = (com.tms.collab.calendar.model.Attendee)i.next();
                if (currentSyncUserId.equals(attendee.getUserId())) {
                    validView = true;
                    break;
                }
            }

            if(validView){
                String xml = util.ekpCalendarEventToXml(ce);
                //Log.getLog(getClass()).info(xml);
                return xml;
            }

        } catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).debug("Calendar not found: " + uid);
            return null;
        } catch (CalendarException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * for finding twin, return only the Key
     * @param syncContext
     * @return String[] Key
     */
    public String[] getCalendarsTwinKeys(Properties syncContext, String xmlCalendar) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("search for calendar twin: " + username + " : " + xmlCalendar );
        SyncServiceCalHelper helper = new SyncServiceCalHelper();

        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        CalendarEvent cal = new CalendarEvent();
        try {
            com.funambol.foundation.pdi.event.Calendar calendar = helper.getFoundationCalendarFromXML(xmlCalendar);

            cal.setTitle(calendar.getEvent().getSummary().getPropertyValueAsString());
            if(calendar.getEvent().getLocation().getPropertyValue() != null){
                cal.setLocation(calendar.getEvent().getLocation().getPropertyValueAsString());
            }

            String dtStart = calendar.getEvent().getDtStart().getPropertyValueAsString();
            String dtEnd = calendar.getEvent().getDtEnd().getPropertyValueAsString();
            if(dtStart != null && dtStart.length() > 0){
                cal.setStartDate(helper.dateParser(dtStart, null));
            }

            if(dtEnd !=null && dtEnd.length() > 0){
                cal.setEndDate(helper.dateParser(dtEnd, null));
            }

            CalendarEvent ce = cm.getCalendarEventByCriteria(currentSyncUserId, cal);
            if(ce != null){
                Log.getLog(getClass()).info("twin found: " + ce.getEventId());
                return new String[]{ce.getEventId()};
            }
        } catch (DataObjectNotFoundException e){
            //no match found
            return new String[0];
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new String[0];
    }

    private void insertCalendarCommit(String userId, String eventId, String xmlCalendarToSet, Date modifiedDate){
        SyncServiceCalHelper calUtil = new SyncServiceCalHelper();
        try {
            com.funambol.foundation.pdi.event.Calendar funambolCalendar = calUtil.getFoundationCalendarFromXML(xmlCalendarToSet);
            Event event = funambolCalendar.getEvent();

            CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            CalendarEvent ce = new CalendarEvent();
            ce.setAllDay(event.getAllDay().booleanValue());
            if(event.getDtStamp().getPropertyValueAsString()!=null){
                ce.setCreationDate(calUtil.dateParser(event.getDtStamp().getPropertyValueAsString(), null));
            }

            if(event.getDtStart().getPropertyValueAsString()!=null) ce.setStartDate(calUtil.dateParser(event.getDtStart().getPropertyValueAsString(), null));

            Calendar defaultEndDate = Calendar.getInstance();
            defaultEndDate.setTime(ce.getStartDate());
            defaultEndDate.set(Calendar.HOUR, defaultEndDate.get(Calendar.HOUR) + 1);
            if(event.getDtEnd().getPropertyValueAsString()!=null) ce.setEndDate(calUtil.dateParser(event.getDtEnd().getPropertyValueAsString(), defaultEndDate.getTime()));

            if(event.getDescription().getPropertyValueAsString()!=null) ce.setDescription(event.getDescription().getPropertyValueAsString());
            if(event.getLocation().getPropertyValueAsString()!=null) ce.setLocation(event.getLocation().getPropertyValueAsString());
            if(event.getSummary().getPropertyValueAsString()!=null) ce.setTitle(event.getSummary().getPropertyValueAsString());
            if(event.getClassEvent().getPropertyValueAsString()!=null){
                if(event.getClassEvent().getPropertyValueAsString().equals("0")){
                    ce.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
                }else ce.setClassification(CalendarModule.CLASSIFICATION_PRIVATE);
            }

            ce.setUserId(currentSyncUserId);

            Collection attendeeList = new ArrayList();
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = ss.getUser(currentSyncUserId);
            Assignee att = new Assignee();
            att.setUserId(user.getId());
            att.setProperty("username", user.getUsername());
            att.setProperty("firstName", user.getProperty("firstName"));
            att.setProperty("lastName", user.getProperty("lastName"));
            att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
            attendeeList.add(att);

            ce.setAttendees(attendeeList);

            //generate recurring events
            //todo Have to disable recurrence as EKP does not support no end date.
            /* if(event.getRecurrencePattern()!=null){
               StringBuffer rrule = calUtil.composeFieldRrule(event.getRecurrencePattern());
               ce.setRecurrenceRule(rrule.toString());
           }
           Collection col = CalendarUtil.getRecurringEvents(ce, ce.getStartDate(), CalendarUtil.getLastRecurrenceDate(ce));
           CalendarEvent recurringEvent;*/

            //set reminder
            if(event.getReminder()!=null && event.getReminder().isActive()){
                int minutes = event.getReminder().getMinutes();
                Calendar reminderTime = Calendar.getInstance();
                reminderTime.setTime(ce.getStartDate());
                reminderTime.add(Calendar.MINUTE, -minutes);
                ce.setReminderDate(reminderTime.getTime());
                /*for (Iterator i = col.iterator(); i.hasNext();) {
                    recurringEvent = (CalendarEvent) i.next();
                    reminderTime.setTime(recurringEvent.getStartDate());
                    reminderTime.add(Calendar.MINUTE, -minutes);
                    recurringEvent.setReminderDate(reminderTime.getTime());
                }*/
            }else ce.setReminderDate(null);

            ce.setEventId(eventId);
            ce = cm.addCalendarEvent(Appointment.class.getName(), ce, currentSyncUserId, true, modifiedDate);

            //add recurring events
            //todo Have to disable recurrence as EKP does not support no end date.
            /*  for (Iterator i = col.iterator(); i.hasNext();) {
                            cm.addRecurringEvent((CalendarEvent) i.next(), currentSyncUserId, true);
                        }
            */
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error in xml to ekp calendar", e);
        }
    }

    public String insertCalendar(Properties syncContext, String xmlCalendarToSet) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("insert calendar: " + username + " : " + xmlCalendarToSet );
        SyncServiceCalHelper calUtil = new SyncServiceCalHelper();
        try {
            Date modifiedDate = calUtil.dateParser(syncContext.getProperty("date_modified"), new Date());

            SyncTransactionModule stm = (SyncTransactionModule) Application.getInstance().getModule(SyncTransactionModule.class);
            String eventId = Appointment.class.getName() + "_" + UuidGenerator.getInstance().getUuid();

            stm.saveTransaction(eventId, sessionId, currentSyncUserId, "insertCalendar", xmlCalendarToSet, modifiedDate);

            HashMap map = SourceUtils.xmlToHashMap(xmlCalendarToSet);
            map.remove(SIFE.UID);
            map.put(SIFE.UID, eventId);
            xmlCalendarToSet = SourceUtils.hashMapToXml(map);
            return xmlCalendarToSet;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error in xml to ekp calendar", e);
        }
        return null;
    }

    /**
     * Only owner can update.
     * @param syncContext
     * @param xmlCalendarToSet
     * @return String
     */
    public String updateCalendar(Properties syncContext, String xmlCalendarToSet){
        String uid = "";
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("update calendar: " + username + " : " + xmlCalendarToSet );
        SyncServiceCalHelper calUtil = new SyncServiceCalHelper();
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        com.funambol.foundation.pdi.event.Event event = null;
        try {
            event = calUtil.getFoundationCalendarFromXML(xmlCalendarToSet).getEvent();
            //uid = event.getUid().getPropertyValueAsString();
            uid = syncContext.getProperty("uid");
            Date modifiedDate = calUtil.dateParser(syncContext.getProperty("date_modified"), new Date());
            CalendarEvent ce = cm.getCalendarEvent(uid);
            if(!ce.getUserId().equals(currentSyncUserId)){
                Log.getLog(getClass()).debug("Does not have permission to update calendar from Sync");
                return calUtil.ekpCalendarEventToXml(ce);
            }

            ce.setAllDay(event.getAllDay().booleanValue());
            if(event.getDescription().getPropertyValueAsString()!=null) ce.setDescription(event.getDescription().getPropertyValueAsString());
            if(event.getDtEnd().getPropertyValueAsString()!=null) ce.setEndDate(calUtil.dateParser(event.getDtEnd().getPropertyValueAsString(), null));

            if(event.getLocation().getPropertyValueAsString()!=null) ce.setLocation(event.getLocation().getPropertyValueAsString());
            if(event.getDtStart().getPropertyValueAsString()!=null) ce.setStartDate(calUtil.dateParser(event.getDtStart().getPropertyValueAsString(), null));
            if(event.getSummary().getPropertyValueAsString()!=null) ce.setTitle(event.getSummary().getPropertyValueAsString());
            if(event.getClassEvent().getPropertyValueAsString()!=null){
                if(event.getClassEvent().getPropertyValueAsString().equals("0")){
                    ce.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
                }else ce.setClassification(CalendarModule.CLASSIFICATION_PRIVATE);
            }

            ce.setUserId(currentSyncUserId);

            //generate recurring events
            //todo Have to disable recurrence as EKP does not support no end date.
            /*if(event.getRecurrencePattern()!=null){
                StringBuffer rrule = calUtil.composeFieldRrule(event.getRecurrencePattern());
                ce.setRecurrenceRule(rrule.toString());
            }

            Collection col = CalendarUtil.getRecurringEvents(ce, ce.getStartDate(), CalendarUtil.getLastRecurrenceDate(ce));
            CalendarEvent recurringEvent;
*/
            //set reminder
            if(event.getReminder()!=null && event.getReminder().isActive()){
                int minutes = event.getReminder().getMinutes();
                Calendar reminderTime = Calendar.getInstance();
                reminderTime.setTime(ce.getStartDate());
                reminderTime.add(Calendar.MINUTE, -minutes);
                ce.setReminderDate(reminderTime.getTime());
                /*  for (Iterator i = col.iterator(); i.hasNext();) {
                    recurringEvent = (CalendarEvent) i.next();
                    reminderTime.setTime(recurringEvent.getStartDate());
                    reminderTime.add(Calendar.MINUTE, -minutes);
                    recurringEvent.setReminderDate(reminderTime.getTime());
                }*/
            }else ce.setReminderDate(null);

            //add recurring events
            /*  for (Iterator i = col.iterator(); i.hasNext();) {
                cm.addRecurringEvent((CalendarEvent) i.next(), currentSyncUserId, true);
            }*/
            cm.updateCalendarEvent(ce, currentSyncUserId, true, modifiedDate);
            xmlCalendarToSet = calUtil.ekpCalendarEventToXml(ce);

            return xmlCalendarToSet;

        } catch (Exception e) {
            Log.getLog(getClass()).error("Error converting to FunambolCalendar from XML for element " + uid, e);
        }
        return null;
    }

    /**
     * Only owner can delete
     * @param syncContext
     * @param id
     */
    public void removeCalendar(Properties syncContext, String id) {
        if(initialize(syncContext)){
            Log.getLog(getClass()).info("remove calendar: " + username + " : " + id );
            CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            try {
                CalendarEvent ce = cm.getCalendarEvent(id);
                // only the owner can delete
                if(ce.getUserId().equals(currentSyncUserId)){
                    cm.deleteCalendarEvent(id, currentSyncUserId);
                }

            } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).debug("Calendar doesnt not exist.");
            } catch (CalendarException e) {
                //
            }
        }//else do nothing
    }
    //----------------------- task methods---------------------------------------------------

    public String[] getAllTaskKeys(Properties syncContext) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get all task: " + username );
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);

        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-6);
            Collection col = tm.getCalendarTasks(cal.getTime(), new String[]{currentSyncUserId}, false);

            //BEGIN: Handling task masking
            TaskMasker masker = (TaskMasker) Application.getInstance().getModule(TaskMasker.class);
            Collection list = masker.getUserMasks(currentSyncUserId);

            String[] keys = new String[col.size() + list.size()];

            int i = 0;
            for(Iterator itr=col.iterator(); itr.hasNext();i++){
                Task task = (Task) itr.next();
                keys[i] = task.getId();
            }
            for (Iterator itr = list.iterator(); itr.hasNext();i++)
            {
                HashMap map = (HashMap) itr.next();
                keys[i] = (String) map.get("taskId");
            }

            return keys;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getUpdatedTaskKeys(Properties syncContext, String since, String until) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get updated task: " + username);
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-6);
            Collection col = tm.getCalendarTasks(cal.getTime(), new String[]{currentSyncUserId}, false);

            Date sinceDate = null, untilDate = null;
            if(since != null){
                sinceDate = SyncServiceCalHelper.format.parse(since);
            }
            if(until != null){
                untilDate = SyncServiceCalHelper.format.parse(until);
            }
            Collection filteredList = new ArrayList();
            for(Iterator itr=col.iterator(); itr.hasNext();){
                Task task = (Task) itr.next();
                Date modifiedDate = task.getLastModified();
                if(modifiedDate.after(sinceDate) && modifiedDate.before(untilDate) && task.isModified()){
                    filteredList.add(task);
                }
            }

            String[] keys = new String[filteredList.size()];
            int i = 0;
            for(Iterator itr=filteredList.iterator(); itr.hasNext(); i++){
                Task task = (Task) itr.next();
                keys[i] = task.getId();
            }

            return keys;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getDeletedTaskKeys(Properties syncContext, String since, String until) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get deleted tasks: " + username);
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        Date sinceDate = null, untilDate = null;

        try {
            if (since != null) {
                sinceDate = SyncServiceCalHelper.format.parse(since);
            }
            if (until != null) {
                untilDate = SyncServiceCalHelper.format.parse(until);
            }
            Collection keys = cm.getCalendarKeys(currentSyncUserId, new String[]{currentSyncUserId}, false, true, sinceDate, untilDate, "deleted");

            Collection filtered = new ArrayList();
            for(Iterator itr=keys.iterator(); itr.hasNext();){
                String key = (String) itr.next();
                if(key.startsWith(Task.class.getName())){
                    filtered.add(key);
                }
            }

            String[] filteredKeys = new String[filtered.size()];
            int i = 0;
            for(Iterator itr=filtered.iterator(); itr.hasNext(); i++){
                filteredKeys[i] = (String) itr.next();
            }
            return filteredKeys;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getNewTaskKeys(Properties syncContext, String since, String until) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get new tasks: " + username);
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-6);
            Collection col = tm.getCalendarTasks(cal.getTime(), new String[]{currentSyncUserId}, false);

            Date sinceDate = null, untilDate = null;
            if(since != null){
                sinceDate = SyncServiceCalHelper.format.parse(since);
            }
            if(until != null){
                untilDate = SyncServiceCalHelper.format.parse(until);
            }
            Collection filteredList = new ArrayList();
            for(Iterator itr=col.iterator(); itr.hasNext();){
                Task task = (Task) itr.next();
                if(task.isNew() && task.getCreationDate().after(sinceDate) && task.getCreationDate().before(untilDate)){
                    filteredList.add(task);
                }
            }

            String[] keys = new String[filteredList.size()];
            int i = 0;
            for(Iterator itr=filteredList.iterator(); itr.hasNext(); i++){
                Task task = (Task) itr.next();
                keys[i] = task.getId();
            }

            return keys;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTaskByUID(Properties syncContext, String uid) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("get task by id: " + username + " : " + uid );
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        String xml = null;
        try {
            TaskUtils tu = new TaskUtils();

            Task task = tm.getTask(uid, currentSyncUserId);
            xml = tu.ekpTaskToXml(task, currentSyncUserId);
            Log.getLog(getClass()).info(xml);

            return xml;
        } catch (DaoException e) {
            e.printStackTrace();
        } catch (DataObjectNotFoundException e) {
            //Handling task masking
            TaskMasker masker = (TaskMasker) Application.getInstance().getModule(TaskMasker.class);
            HashMap map = masker.getMask(currentSyncUserId, uid);
            if(map != null && map.keySet().size() > 0)
            {
                xml = (String) map.get("content");
                Log.getLog(getClass()).info(xml);
                return xml;
            }
            //Log.getLog(getClass()).debug("Requested task not found: " + uid);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeTask(Properties syncContext, String id) {
        if(!initialize(syncContext)) return;
        Log.getLog(getClass()).info("remove task: " + username + " : " + id );
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);


        try {
            CalendarEvent ce = cm.getCalendarEvent(id);
            if(ce.getUserId().equals(currentSyncUserId)){
                tm.deleteTask(id);
            }
        } catch (DataObjectNotFoundException e) {
            //Handling task masking
            TaskMasker masker = (TaskMasker) Application.getInstance().getModule(TaskMasker.class);
            masker.deleteMask(currentSyncUserId, id);
            //Log.getLog(getClass()).debug("Requested task for delete not found. " + id);
        } catch (CalendarException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    //TODO: Task masking is not executed during twin searches. This might lead to some redundant data in the masking tables. Might need to write a reconciliation method
    public String[] getTasksTwinKeys(Properties syncContext, String criteria) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("find twin for task: " + username + " : " + criteria );
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        SyncServiceCalHelper calUtil = new SyncServiceCalHelper();

        try {
            Map map = SourceUtils.xmlToHashMap(criteria);

            if(!isValidTask(map)){
                return new String[0];
            }

            Task task = new Task();
            task.setUserId(currentSyncUserId);
            task.setTitle((String) map.get(SIFT.SUBJECT));
            task.setStartDate(calUtil.dateParser(map.get(SIFT.START_DATE).toString(), new Date()));
            task.setDueDate(calUtil.dateParser(map.get(SIFT.DUEDATE).toString(), new Date()));

            // set start date and due date time
            Calendar calStartDate = Calendar.getInstance();
            calStartDate.setTime(task.getStartDate());
            // default start time to 8:30:00am
            calStartDate.set(Calendar.HOUR_OF_DAY, 8);
            calStartDate.set(Calendar.MINUTE, 30);
            task.setStartDate(calStartDate.getTime());

            //default to 5:30:00pm
            Calendar defaultDueDate = Calendar.getInstance();
            defaultDueDate.setTime(task.getDueDate());
            defaultDueDate.set(Calendar.HOUR_OF_DAY, 17);
            defaultDueDate.set(Calendar.MINUTE, 30);
            defaultDueDate.set(Calendar.SECOND, 00);
            task.setDueDate(defaultDueDate.getTime());


            String[] keys;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-6);
            Collection col = tm.getCalendarTasks(cal.getTime(), new String[]{currentSyncUserId}, false);
            for(Iterator itr=col.iterator(); itr.hasNext();){
                Task item = (Task) itr.next();
                if(item.getTitle().equals(task.getTitle())){
                    if(task.getDueDate().equals(item.getDueDate())){
                        if(task.getStartDate().equals(item.getStartDate())){
                            keys = new String[1];
                            keys[0] = item.getId();                            
                            return keys;
                        }
                    }
                }
            }
            return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private void insertTaskCommit(String userId, String taskId, String xmlTaskToSet, Date modifiedTime) {
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        SyncServiceCalHelper utils = new SyncServiceCalHelper();
        try {
            Map map = SourceUtils.xmlToHashMap(xmlTaskToSet);

            Task task = new Task();
            Assignee att = new Assignee();
            if(map.get(SIFT.BODY)!=null) task.setDescription((String) map.get(SIFT.BODY));

            task.setStartDate(utils.dateParser(map.get(SIFT.START_DATE).toString(), null));
            Calendar calStartDate = Calendar.getInstance();
            calStartDate.setTime(task.getStartDate());
            // default start time to 8:30am
            calStartDate.set(Calendar.HOUR_OF_DAY, 8);
            calStartDate.set(Calendar.MINUTE, 30);
            calStartDate.set(Calendar.SECOND, 00);
            task.setStartDate(calStartDate.getTime());

            if(map.get(SIFT.PERCENT_COMPLETE) !=null && !"".equals(map.get(SIFT.PERCENT_COMPLETE))){
                att.setProgress(Integer.getInteger(map.get(SIFT.PERCENT_COMPLETE).toString()));
                att.setStartDate(task.getStartDate());
                att.setTaskStatus(Assignee.TASK_STATUS_IN_PROGRESS);
            }

            task.setUserId(currentSyncUserId);
            if(map.get(SIFT.CATEGORIES)!=null && map.get(SIFT.CATEGORIES).toString().length()!=0){
                boolean found = false;
                String catName = (String) map.get(SIFT.CATEGORIES);
                Collection cats = tm.getCategories(currentSyncUserId);
                for(Iterator itr=cats.iterator(); itr.hasNext();){
                    TaskCategory cat = (TaskCategory) itr.next();
                    if(cat.getName().equals(catName)){
                        task.setCategoryId(cat.getId());
                        found = true;
                        break;
                    }
                }
                //if category not found, make new one
                if(!found){
                    TaskCategory cat = new TaskCategory(catName);
                    cat.setId(UuidGenerator.getInstance().getUuid());
                    cat.setUserId(currentSyncUserId);
                    cat.setGeneral(false);
                    cat.setDescription("Created from PIMSyncService");
                    tm.addCategory(cat);
                    task.setCategoryId(cat.getId());
                }
            }else task.setCategoryId("com.tms.collab.taskmanager.category.general");

            if(map.get(SIFT.REMINDER_SET)!=null && map.get(SIFT.REMINDER_SET).toString().equals("1")){
                task.setReminderDate(utils.dateParser((String) map.get(SIFT.REMINDER_TIME), null));
            }

            if(map.get(SIFT.IMPORTANCE)!=null && map.get(SIFT.IMPORTANCE).toString().length()!=0){
                String importance = map.get(SIFT.IMPORTANCE).toString();
                // if high
                if(importance.equals("2")) task.setTaskPriority("1");
                    // if normal
                else if(importance.equals("1")) task.setTaskPriority("3");
                    //if low
                else if(importance.equals("0")) task.setTaskPriority("5");
            }

            task.setReassign(false);
            if(map.get(SIFT.SENSITIVITY)!=null){
                String sensitivity = (String) map.get(SIFT.SENSITIVITY);
                if(sensitivity.equals("0")){
                    task.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
                }else task.setClassification(CalendarModule.CLASSIFICATION_PRIVATE);
            }

            task.setCreationDate(modifiedTime);

            Calendar defaultDueDate = Calendar.getInstance();
            defaultDueDate.setTime(utils.dateParser(map.get(SIFT.DUEDATE).toString(), null));
            defaultDueDate.set(Calendar.HOUR_OF_DAY, 17);
            defaultDueDate.set(Calendar.MINUTE, 30);
            defaultDueDate.set(Calendar.SECOND, 00);

            task.setDueDate(defaultDueDate.getTime());
            task.setEndDate(task.getDueDate());


            if(map.get(SIFT.SUBJECT)!=null){
                task.setTitle((String) map.get(SIFT.SUBJECT));
            }

            if(map.get(SIFT.COMPLETE).equals("1")){
                task.setCompleted(true);
                task.setCompleteDate(utils.dateParser((String) map.get(SIFT.DATE_COMPLETED), null));
                att.setTaskStatus(Assignee.TASK_STATUS_COMPLETED);
                att.setCompleteDate(task.getCompleteDate());
                att.setProgress(100);
            }else{
                task.setCompleted(false);
            }

            Collection attendeeList = new ArrayList();
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = ss.getUser(currentSyncUserId);
            att.setUserId(user.getId());
            att.setProperty("username", user.getUsername());
            att.setProperty("firstName", user.getProperty("firstName"));
            att.setProperty("lastName", user.getProperty("lastName"));
            att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
            attendeeList.add(att);

            task.setAssignerId(currentSyncUserId);
            task.setAssigner(user.getName());
            task.setAttendees(attendeeList);

            //calculate estimated effort
            long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
            long diff = task.getEndDate().getTime() - task.getStartDate().getTime();
            double days = (diff / MILLIS_PER_DAY);

            task.setEstimationType("Mandays");
            task.setEstimation(days+1);

            task.setEventId(taskId);
            task.setId(taskId);

            cm.addCalendarEvent(Task.class.getName(),task,task.getUserId(),true, modifiedTime);
            tm.addTask(task);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Map insertTask(Properties syncContext, String xmlTaskToSet){
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("insert task: " + username + " : " + xmlTaskToSet );
        SyncServiceCalHelper utils = new SyncServiceCalHelper();
        String taskId = syncContext.getProperty("taskId");
        if(taskId == null || (!taskId.startsWith(Task.class.getName())))
            taskId = Task.class.getName() + "_" + UuidGenerator.getInstance().getUuid();

        try {
            Map nMap = new HashMap();
            Map map = SourceUtils.xmlToHashMap(xmlTaskToSet);
            Date modifiedTime = utils.dateParser(syncContext.getProperty("date_modified"), new Date());

            if(!isValidTask(map)){
                TaskMasker taskMasker = (TaskMasker) Application.getInstance().getModule(TaskMasker.class);
                taskMasker.insertMask(currentSyncUserId, taskId, xmlTaskToSet);
                nMap.put("uid", taskId);
                nMap.put("content", xmlTaskToSet);
                return nMap;
            }

            SyncTransactionModule stm = (SyncTransactionModule) Application.getInstance().getModule(SyncTransactionModule.class);
            stm.saveTransaction(taskId, sessionId, currentSyncUserId, "insertTask", xmlTaskToSet, modifiedTime);

            nMap.put("uid", taskId);
            nMap.put("content", xmlTaskToSet);

            return nMap;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String updateTask(Properties syncContext, String xmlTaskToSet) {
        if(!initialize(syncContext)) return null;
        Log.getLog(getClass()).info("update task: " + username + " : " + xmlTaskToSet );
        TaskUtils util = new TaskUtils();
        SyncServiceCalHelper calUtil = new SyncServiceCalHelper();
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        String id = syncContext.getProperty("uid");
        Map map = new HashMap();
        try {
            Date modifiedDate = calUtil.dateParser(syncContext.getProperty("date_modified"), new Date());

            map = SourceUtils.xmlToHashMap(xmlTaskToSet);
            Task task = tm.getTask(id);

            if(!isValidTask(map)){
                TaskMasker taskMasker = (TaskMasker) Application.getInstance().getModule(TaskMasker.class);
                taskMasker.insertMask(currentSyncUserId, id, xmlTaskToSet);
                tm.destroyTask(id);
                return xmlTaskToSet;
            }
            Assignee att = tm.getAssignee(task.getEventId(), currentSyncUserId);

            if(!task.getUserId().equals(currentSyncUserId)){
                //not owner, update only assignee info.

                if(map.get(SIFT.COMPLETE).equals("1")){
                    att.setTaskStatus(Assignee.TASK_STATUS_COMPLETED);
                    att.setProgress(100);
                    att.setCompleteDate(calUtil.dateParser((String) map.get(SIFT.DATE_COMPLETED), null));
                }else{
                    // dont do anything, cannot set task back to incomplate status
                }

                if(map.get(SIFT.PERCENT_COMPLETE)!=null && map.get(SIFT.PERCENT_COMPLETE).toString().length()!=0){
                    att.setProgress(Integer.valueOf(map.get(SIFT.PERCENT_COMPLETE).toString()));
                    att.setTaskStatus(Assignee.TASK_STATUS_IN_PROGRESS);
                }

                cm.updateCalendarEvent(task,task.getUserId(),true, modifiedDate);
                tm.updateTask(task);
                return util.ekpTaskToXml(task, currentSyncUserId);
            }


            // if is owner
            if(map.get(SIFT.IMPORTANCE)!=null && map.get(SIFT.IMPORTANCE).toString().length()!=0){
                String importance = map.get(SIFT.IMPORTANCE).toString();
                // if high
                if(importance.equals("2")) task.setTaskPriority("1");
                    // if normal
                else if(importance.equals("1")) task.setTaskPriority("3");
                    //if low
                else if(importance.equals("0")) task.setTaskPriority("5");
            }


            if(map.get(SIFT.BODY)!=null) task.setDescription((String) map.get(SIFT.BODY));

            task.setStartDate(calUtil.dateParser(map.get(SIFT.START_DATE).toString(), null));
            Calendar calStartDate = Calendar.getInstance();
            calStartDate.setTime(task.getStartDate());
            // default start time to 8:30am
            calStartDate.set(Calendar.HOUR_OF_DAY, 8);
            calStartDate.set(Calendar.MINUTE, 30);
            calStartDate.set(Calendar.SECOND, 00);
            task.setStartDate(calStartDate.getTime());

            if(map.get(SIFT.CATEGORIES)!=null && map.get(SIFT.CATEGORIES).toString().length()!=0){
                boolean found = false;
                String catName = (String) map.get(SIFT.CATEGORIES);
                Collection cats = tm.getCategories(currentSyncUserId);
                for(Iterator itr=cats.iterator(); itr.hasNext();){
                    TaskCategory cat = (TaskCategory) itr.next();
                    if(cat.getName().equals(catName)){
                        task.setCategoryId(cat.getId());
                        found = true;
                        break;
                    }
                }
                //if category not found, make new one
                if(!found){
                    TaskCategory cat = new TaskCategory(catName);
                    cat.setId(UuidGenerator.getInstance().getUuid());
                    cat.setUserId(currentSyncUserId);
                    cat.setGeneral(false);
                    cat.setDescription("Created from PIMSyncService");
                    tm.addCategory(cat);
                    task.setCategoryId(cat.getId());
                }
            }

            if(map.get(SIFT.PERCENT_COMPLETE)!=null && map.get(SIFT.PERCENT_COMPLETE).toString().length()!=0){
                att.setProgress(Integer.valueOf(map.get(SIFT.PERCENT_COMPLETE).toString()));
            }

            if(map.get(SIFT.REMINDER_SET)!=null && map.get(SIFT.REMINDER_SET).toString().equals("1")){
                task.setReminderDate(calUtil.dateParser((String) map.get(SIFT.REMINDER_TIME), null));
            }

            if(map.get(SIFT.SENSITIVITY)!=null){
                String sensitivity = (String) map.get(SIFT.SENSITIVITY);
                if(sensitivity.equals("0")){
                    task.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
                }else task.setClassification(CalendarModule.CLASSIFICATION_PRIVATE);
            }

            Calendar defaultDueDate = Calendar.getInstance();
            defaultDueDate.setTime(calUtil.dateParser(map.get(SIFT.DUEDATE).toString(), null));
            defaultDueDate.set(Calendar.HOUR_OF_DAY, 17);
            defaultDueDate.set(Calendar.MINUTE, 30);
            defaultDueDate.set(Calendar.SECOND, 00);

            task.setDueDate(defaultDueDate.getTime());
            task.setEndDate(task.getDueDate());

            if(map.get(SIFT.SUBJECT)!=null){
                task.setTitle((String) map.get(SIFT.SUBJECT));
            }

            if(map.get(SIFT.COMPLETE).equals("1")){
                att.setTaskStatus(Assignee.TASK_STATUS_COMPLETED);
                att.setCompleteDate(calUtil.dateParser((String) map.get(SIFT.DATE_COMPLETED), null));
                att.setProgress(100);
            }else{
                // cannot set task back to incomplete status
            }

            cm.updateCalendarEvent(task,task.getUserId(),true, modifiedDate);
            tm.updateAssignee(att);
            tm.updateTask(task);
            //setting complete status
            if(map.get(SIFT.COMPLETE).equals("1"))
                tm.completeTask(task.getId(),att);

            String xml = util.ekpTaskToXml(task, currentSyncUserId);
            return xml;



        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DataObjectNotFoundException e) {
            TaskMasker taskMasker = (TaskMasker) Application.getInstance().getModule(TaskMasker.class);
            if(isValidTask(map)){
                taskMasker.deleteMask(currentSyncUserId, id);
                syncContext.put("taskId", id);
                insertTask(syncContext, xmlTaskToSet);
            }else{
                taskMasker.insertMask(currentSyncUserId, id, xmlTaskToSet);
            }
            return xmlTaskToSet;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    private boolean isValidTask(Map map)
    {
        if(map.get(SIFT.DUEDATE) == null || "".equals(map.get(SIFT.DUEDATE)))
            return false;
        if(map.get(SIFT.START_DATE) == null || "".equals(map.get(SIFT.START_DATE)))
            return false;
        if(map.get(SIFT.SUBJECT) == null || "".equals(map.get(SIFT.SUBJECT))){
            return false;
        }

        return true;
    }

    // commit transaction
    public void endSync(Properties syncContext, String sessionId) {
        Log.getLog(getClass()).info("endsync sessionId:" + sessionId);
        SyncTransactionModule stm = (SyncTransactionModule) Application.getInstance().getModule(SyncTransactionModule.class);
        Collection transactions = stm.getTransactionBySessionId(sessionId);

        for(Iterator itr=transactions.iterator(); itr.hasNext();){
            Map map = (Map) itr.next();
            String type = (String) map.get("type");
            String userId = (String) map.get("userId");
            String content = (String) map.get("content");
            Date modifiedTime = (Date) map.get("createdTime");
            String id = (String) map.get("id");

            if("insertContact".equals(type)){
                insertContactCommit(userId, id, content, modifiedTime);
            }else if("insertCalendar".equals(type)){
                insertCalendarCommit(userId, id, content, modifiedTime);
            }else if("insertTask".equals(type)){
                insertTaskCommit(userId, id, content, modifiedTime);
            }
        }
        stm.deleteTransactionBySessionId(sessionId);
    }
}
