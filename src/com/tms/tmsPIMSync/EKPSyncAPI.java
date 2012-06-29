package com.tms.tmsPIMSync;

import java.util.Properties;
import java.util.Map;

public interface EKPSyncAPI {

    public String[] getAllContactKeys(Properties syncContext);

    /**
     * Get updated contacts
     * @param since
     * @param until
     * @return String[] an array of uids
     */
    public String[] getUpdatedContactKeys(Properties syncContext, String since, String until);

    /**
     * get deleted contacts
     * @param since
     * @param until
     * @return String[] an array of uids
     */
    public String[] getDeletedContactKeys(Properties syncContext, String since, String until);

    /**
     * Get new contact keys
     * @param syncContext
     * @param since
     * @param until
     * @return String[] an array of uids
     */
    public String[] getNewContactKeys(Properties syncContext, String since, String until);

    public String getContactByUID(Properties syncContext, String uid);

    public void removeContact(Properties syncContext, String id);

    public String[] getContactsTwinKeys(Properties syncContext, String xmlContact);

    public String insertContact(Properties syncContext, String contactToSet);

    public String updateContact(Properties syncContext, String contactToSet);

    public boolean isEKPUser(Properties syncAuth);

    public String[] getUpdatedCalendarKeys(Properties syncContext, String since, String until);

    public String[] getDeletedCalendarKeys(Properties syncContext, String since, String until);

    public String[] getNewCalendarKeys(Properties syncContext, String since, String until);

    public String[] getAllCalendarKeys(Properties syncContext);

    String getCalendarByUID(Properties syncContext, String uid);

    public void removeCalendar(Properties syncContext, String id);

    public String[] getCalendarsTwinKeys(Properties syncContext, String xmlCalendar);

    public String insertCalendar(Properties syncContext, String xmlCalendarToSet);

    public String updateCalendar(Properties syncContext, String xmlCalendarToSet);

    public String[] getAllTaskKeys(Properties syncContext);

    public String[] getUpdatedTaskKeys(Properties syncContext, String since, String until);

    public String[] getDeletedTaskKeys(Properties syncContext, String since, String until);


    public String[] getNewTaskKeys(Properties syncContext, String since, String until);

    public String getTaskByUID(Properties syncContext, String uid);

    public void removeTask(Properties syncContext, String id);

    public String[] getTasksTwinKeys(Properties syncContext, String criteria);

    public Map insertTask(Properties syncContext, String xmlTaskToSet);

    public String updateTask(Properties syncContext, String xmlTaskToSet);

    public void endSync(Properties syncContext, String sessionId);
}
