package com.tms.hr.leave.model;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.services.security.SecurityException;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.Transaction;
import kacang.util.UuidGenerator;
import org.apache.commons.beanutils.PropertyUtils;

import com.tms.collab.calendar.model.CalendarEvent;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LeaveDao extends DataSourceDao {

    public void init() throws DaoException {

        // create tables
        try{
            super.update("CREATE TABLE hr_leave_carry_forward (year int(10) unsigned default NULL,leaveType varchar(255) default NULL,userId varchar(255) default NULL,carryForwardDays float default NULL)", null);
        }
        catch(DaoException e){}

        try{
            super.update("CREATE TABLE hr_leave_entitlement (id varchar(255) default NULL,serviceClassId varchar(255) default NULL,leaveType varchar(255) default NULL,year int(10) unsigned default '0',serviceYears int(10) unsigned default NULL,entitlement int(10) unsigned default NULL)", null);
        }
        catch(DaoException e){}

        try{
            super.update("CREATE TABLE hr_leave_entry (id varchar(255) NOT NULL default '',leaveType varchar(255) default NULL,leaveTypeId varchar(255) default NULL,startDate datetime default NULL,endDate datetime default NULL,credit char(1) default '0',adjustment char(1) default '0',days float default NULL,halfDay char(1) default '0',status varchar(255) default NULL,reason varchar(255) default NULL,userId varchar(255) default NULL,applicantId varchar(255) default NULL,applicationDate datetime default NULL,approvalDate datetime default NULL,approvalUserId varchar(255) default NULL,approvalComments varchar(255) default NULL,eventId varchar(255) default NULL,lastModifiedDate datetime default NULL,lastModifiedUserId varchar(255) default NULL,PRIMARY KEY (id))", null);
        }
        catch(DaoException e){}

        try{
            super.update("CREATE TABLE hr_leave_settings (property varchar(255) NOT NULL default '',year int(11) NOT NULL default '0',value varchar(255) default NULL,PRIMARY KEY (property,year))", null);
            //super.update("CREATE TABLE hr_leave_type (id varchar(255) NOT NULL default '',leaveType varchar(255) default NULL,name varchar(255) default NULL,gender char(1) default NULL,fixedCalendar enum('0','1') NOT NULL default '0',creditAllowed char(1) NOT NULL default '0',PRIMARY KEY (id));", null);
        }
        catch(DaoException e){}

        try{
            super.update("CREATE TABLE hr_leave_type (id varchar(255) NOT NULL default '',leaveType varchar(255) default NULL,name varchar(255) default NULL,gender char(1) default NULL,fixedCalendar CHAR(1) NOT NULL default '0',creditAllowed char(1) NOT NULL default '0',PRIMARY KEY (id));", null);
        }
        catch(DaoException e ){}

        // default settings
        try{
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('ccAdmin', '0', '1');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('notifyEmail', '0', '1');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('notifyMemo', '0', '1');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('realTime', '0', '1');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('proRata', '0', '1');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('fridayWeekend', '0', '0');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('alternateWeekend', '0', '0');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('workingDays', '0', '5');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('carryForwardMaxDate', '0', NULL);", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('carryForwardMaxDays', '0', '5');", null);
            super.update("INSERT INTO hr_leave_settings (property, year, value) VALUES ('halfDay', '0', '0');", null);
        }

        catch(DaoException e){}
        try {
            //  super.update("ALTER TABLE hr_leave_type ADD fixedCalendar enum('0','1') NOT NULL default '0'",null);
            super.update("ALTER TABLE hr_leave_type ADD fixedCalendar CHAR(1) NOT NULL default '0'",null);
        }
        catch(Exception e) {}

        // default leave types
        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Annual', 'Annual', 'Annual Leave', '', '1','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Medical', 'Medical', 'Medical Leave', '', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Compassionate', 'Compassionate', 'Compassionate Leave', '', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Emergency', 'Annual', 'Emergency Leave', '', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Paternity', 'Paternity', 'Paternity Leave', 'M', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Maternity', 'Maternity', 'Maternity Leave', 'F', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Marriage', 'Marriage', 'Marriage Leave', '', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Unpaid', 'Unpaid', 'Unpaid Leave', '', '0','0');", null);
        }
        catch(DaoException e){}

        try{
            super.update("INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed, fixedCalendar) VALUES ('Education', 'Education', 'Education Leave', '', '0','0');", null);
        }
        catch(DaoException e){}
    }

    /**
     * Inserts a leave entry
     * @param entry
     */
    public int insertLeaveEntry(LeaveEntry entry) throws DaoException {
        String sql = "INSERT INTO hr_leave_entry (id, leaveType, leaveTypeId, startDate, endDate, days, halfDay, status, credit, adjustment, reason, userId, applicantId, applicationDate, approvalDate, approvalUserId, approvalComments, eventId, lastModifiedDate, lastModifiedUserId) " +
                "VALUES (#id#, #leaveType#, #leaveTypeId#, #startDate#, #endDate#, #days#, #halfDay#, #status#, #credit#, #adjustment#, #reason#, #userId#, #applicantId#, #applicationDate#, #approvalDate#, #approvalUserId#, #approvalComments#, #eventId#, #lastModifiedDate#, #lastModifiedUserId#)";
        return super.update(sql, entry);
    }

    /**
     * Updates a leave entry.
     * @param entry
     * @param propertiesToUpdate An array defining the properties to update. Null updates all.
     * @return
     * @throws DaoException
     */
    public int updateLeaveEntry(LeaveEntry entry, String[] propertiesToUpdate) throws DaoException {
        if (propertiesToUpdate == null) {
            propertiesToUpdate = new String[] { "leaveType", "leaveTypeId", "startDate", "endDate", "days", "halfDay", "status", "credit", "adjustment", "reason", "userId", "applicantId", "applicationDate", "approvalDate", "approvalUserId", "approvalComments", "eventId", "lastModifiedDate", "lastModifiedUserId" };
        }
        StringBuffer sql = new StringBuffer("UPDATE hr_leave_entry SET ");
        for (int i=0; i<propertiesToUpdate.length; i++) {
            String prop = propertiesToUpdate[i];
            if (i > 0) {
                sql.append(",");
            }
            sql.append(prop);
            sql.append("=#");
            sql.append(prop);
            sql.append("#");
        }
        sql.append(" WHERE id=#id#");
        return super.update(sql.toString(), entry);
    }

    /**
     * Retrieves a specific leave entry
     * @param id
     * @return
     * @throws DaoException
     * @throws DataObjectNotFoundException
     */
    public LeaveEntry selectLeaveEntry(String id) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT e.id, t.fixedCalendar AS fixedCalendar,e.leaveType, e.leaveTypeId, t.name AS leaveTypeName, startDate, endDate, days, halfDay, status, reason, userId, applicantId, applicationDate, approvalDate, credit, adjustment, approvalUserId, approvalComments, eventId, lastModifiedDate, lastModifiedUserId " +
                "FROM hr_leave_entry e " +
                "LEFT OUTER JOIN hr_leave_type t ON e.leaveTypeId=t.id " +
                "WHERE e.id=?";
        Object[] args = new Object[] { id };
        Collection results = super.select(sql.toString(), LeaveEntry.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            LeaveEntry entry  = (LeaveEntry)results.iterator().next();
            return entry;
        }
    }

    /**
     * Returns a Collection leave entries based on parameters
     * @param startDate
     * @param endDate
     * @param userId
     * @param applicantId
     * @param leaveType
     * @param status
     * @param credit
     * @param adjustment
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return A Collection of LeaveEntry objects.
     * @throws DaoException
     */
    public Collection selectLeaveEntryList(String leaveTypeName,Date startDate, Date endDate, String[] userId, String applicantId, String leaveType, String[] status, Boolean credit, Boolean adjustment, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT e.id,t.fixedCalendar AS fixedCalendar, e.leaveType, leaveTypeId, t.name AS leaveTypeName, startDate, endDate, days, halfDay, status, reason, userId, applicantId, applicationDate, approvalDate, credit, adjustment, approvalUserId, approvalComments, eventId, lastModifiedDate, lastModifiedUserId " +
                "FROM hr_leave_entry e " +
                "LEFT OUTER JOIN hr_leave_type t ON e.leaveTypeId=t.id " +
                "WHERE 1=1 ");

        sql.append(" AND ((startDate >= ? AND startDate <= ?) OR (startDate <= ? AND endDate >= ?)) ");
        args.add(startDate);
        args.add(endDate);
        args.add(startDate);
        args.add(startDate);

        if (userId != null) {
            if (userId.length == 0) {
                return new ArrayList();
            }
            sql.append(" AND e.userId IN (");
            for (int i=0; i<userId.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            args.addAll(Arrays.asList(userId));
        }
        if (applicantId != null) {
            sql.append(" AND applicantId=? ");
            args.add(applicantId);
        }
        if (leaveType != null) {
            sql.append(" AND e.leaveType=? ");
            args.add(leaveType);
        }
        if (status != null) {
            sql.append(" AND status IN (");
            for (int i=0; i<status.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            args.addAll(Arrays.asList(status));
        }
        if (credit != null) {
            sql.append(" AND credit=? ");
            args.add(credit);
        }
        if (adjustment != null) {
            sql.append(" AND adjustment=? ");
            args.add(adjustment);
        }
        
        /*if(leaveTypeName !=null && !("".equals(leaveTypeName)) ){

            sql.append(" AND leaveTypeId=? ");
            args.add(leaveTypeName);
        }
*/        
        if (sort != null) {
            if ("leaveType".equals(sort)) {
                sort = "e.leaveType";
            }
            sql.append(" ORDER BY ");
            sql.append(sort);
            if (desc) {
                sql.append(" DESC");
            }
        }
        Collection results = super.select(sql.toString(), LeaveEntry.class, args.toArray(), start, rows);
        return results;
    }

    /**
     * Returns a count of leave entries based on parameters
     * @param startDate Required
     * @param endDate Required
     * @param userId
     * @param applicantId
     * @param leaveType
     * @param status
     * @param credit
     * @param adjustment
     * @return
     * @throws DaoException
     */
    public int countLeaveEntryList(String leaveTypeName,Date startDate, Date endDate, String[] userId, String applicantId, String leaveType, String[] status, Boolean credit, Boolean adjustment) throws DaoException {
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total FROM hr_leave_entry e WHERE 1=1 ");

        sql.append(" AND ((startDate >= ? AND startDate <= ?) OR (startDate <= ? AND endDate >= ?)) ");
        args.add(startDate);
        args.add(endDate);
        args.add(startDate);
        args.add(startDate);

        if (userId != null) {
            if (userId.length == 0) {
                return 0;
            }
            sql.append(" AND e.userId IN (");
            for (int i=0; i<userId.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            args.addAll(Arrays.asList(userId));
        }
        if (applicantId != null) {
            sql.append(" AND applicantId=? ");
            args.add(applicantId);
        }
        if (leaveType != null) {
            sql.append(" AND e.leaveType=? ");
            args.add(leaveType);
        }
        if (status != null) {
            sql.append(" AND status IN (");
            for (int i=0; i<status.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            args.addAll(Arrays.asList(status));
        }
        if (credit != null) {
            sql.append(" AND credit=? ");
            args.add(credit);
        }
        if (adjustment != null) {
            sql.append(" AND adjustment=? ");
            args.add(adjustment);
        }
        if(leaveTypeName !=null && !("".equals(leaveTypeName))){

            sql.append(" AND leaveTypeId=? ");
            args.add(leaveTypeName);
        }
        Collection results = super.select(sql.toString(), HashMap.class, args.toArray(), 0, 1);
        if (results.size() > 0) {
            Map map = (Map)results.iterator().next();
            return Integer.parseInt(map.get("total").toString());
        }
        else {
            return 0;
        }
    }

    /**
     *
     * @param serviceClass
     * @param leaveType
     * @param year
     * @return Collection of LeaveEntitlement objects
     * @throws DaoException
     */
    public Collection selectLeaveEntitlement(String serviceClass, String leaveType, int year) throws DaoException {
        Collection args = new ArrayList();
        String sql = "SELECT DISTINCT e.id, serviceClassId, e.leaveType, entitlement, serviceYears, year, sc_class_desc AS serviceClassDescription " +
                "FROM hr_leave_entitlement e " +
                "LEFT OUTER JOIN service_classfication c ON e.serviceClassId=c.sc_class_code " +
                "WHERE 1=1 ";
        if (serviceClass != null) {
            sql += " AND serviceClassId=? ";
            args.add(serviceClass);
        }
        if (leaveType != null) {
            sql += " AND e.leaveType=? ";
            args.add(leaveType);
        }
        if (year >= 0) {
            sql += " AND year=? ";
            args.add(new Integer(year));
        }
        sql += " ORDER BY serviceClassId, serviceYears, year DESC";

        Collection results = super.select(sql, LeaveEntitlement.class, args.toArray(), 0, -1);
        return results;
    }

    public LeaveEntitlement selectLeaveEntitlementById(String id) throws DataObjectNotFoundException, DaoException {
        String sql = "SELECT DISTINCT e.id, serviceClassId, e.leaveType, entitlement, serviceYears, year, sc_class_desc AS serviceClassDescription " +
                "FROM hr_leave_entitlement e " +
                "LEFT OUTER JOIN service_classfication c ON e.serviceClassId=c.sc_class_code " +
                "WHERE e.id=? ";
        Object[] args = new Object[] { id };

        Collection results = super.select(sql, LeaveEntitlement.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            return (LeaveEntitlement)results.iterator().next();
        }
    }

    public int updateLeaveEntitlement(LeaveEntitlement[] le, int year) throws DaoException {
        String sql1 = "DELETE FROM hr_leave_entitlement WHERE serviceClassId=? AND leaveType=? AND year=? AND serviceYears=?";
        String sql2 = "INSERT INTO hr_leave_entitlement (id, serviceClassId, leaveType, year, serviceYears, entitlement) VALUES (?,?,?,?,?,?)";

        int rows = 0;
        for (int i=0; i<le.length; i++) {
            LeaveEntitlement entitlement = le[i];
            String uuid = UuidGenerator.getInstance().getUuid();
            entitlement.setId(uuid);
            Object[] args1 = new Object[] { entitlement.getServiceClassId(), entitlement.getLeaveType(), new Long(year), new Long(entitlement.getServiceYears()) };
            Object[] args2 = new Object[] { entitlement.getId(), entitlement.getServiceClassId(), entitlement.getLeaveType(), new Long(year), new Long(entitlement.getServiceYears()), new Long(entitlement.getEntitlement()) };
            super.update(sql1, args1);
            rows += super.update(sql2, args2);
        }
        return rows;
    }

    public int deleteLeaveEntitlement(String serviceClassId, String leaveType, int year, int serviceYears) throws DaoException {
        String sql = "DELETE FROM hr_leave_entitlement WHERE serviceClassId=? AND leaveType=? AND year=? AND serviceYears=?";
        Object[] args = new Object[] { serviceClassId, leaveType, new Integer(year), new Integer(serviceYears) };
        int rows = super.update(sql, args);
        return rows;
    }

    /**
     * Returns the number of carry forward days for the specified year
     * @param leaveType
     * @param userId
     * @param year
     * @return
     * @throws DaoException
     * @throws DataObjectNotFoundException
     */
    public float selectCarryForward(String leaveType, String userId, long year) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT carryForwardDays FROM hr_leave_carry_forward WHERE leaveType=? AND userId=? AND year=?";
        Object[] args = new Object[] { leaveType, userId, new Long(year) };
        Collection results = super.select(sql, HashMap.class, args, 0, 1);
        if (results.size() > 0) {
            Map map = (Map)results.iterator().next();
            return Float.parseFloat(map.get("carryForwardDays").toString());
        }
        else {
            return 0;
        }
    }

    /**
     * Inserts/updates a carry forward record
     * @param leaveType
     * @param userId
     * @param year
     * @param days
     * @return
     * @throws DaoException
     */
    public int updateCarryForward(String leaveType, String userId, long year, float days) throws DaoException {
        String sql1 = "DELETE FROM hr_leave_carry_forward WHERE leaveType=? AND userId=? AND year=?";
        String sql2 = "INSERT INTO hr_leave_carry_forward (leaveType, userId, year, carryForwardDays) VALUES (?,?,?,?)";
        Object[] args1 = new Object[] { leaveType, userId, new Long(year) };
        Object[] args2 = new Object[] { leaveType, userId, new Long(year), new Float(days) };
        super.update(sql1, args1);
        int rows = super.update(sql2, args2);
        return rows;
    }

    /**
     * Queries for a list of holidays within a date range
     * @param startDate
     * @param endDate
     * @param search
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return
     * @throws DaoException
     */
    public Collection selectHolidayList(Date startDate, Date endDate, String search, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT cm_date AS date, cm_desc AS description FROM calendar_main h WHERE 1=1 ");

        sql.append(" AND (cm_date >= ? AND cm_date <= ?) ");
        args.add(startDate);
        args.add(endDate);

        if (search != null && search.trim().length() > 0) {
            sql.append(" AND (cm_desc LIKE ?");
            args.add("%" + search + "%");
        }
        if (sort != null) {
            if ("date".equals(sort)) {
                sort = "cm_date";
            }
            else if ("description ".equals(sort)) {
                sort = "cm_desc";
            }
            sql.append(" ORDER BY ");
            sql.append(sort);
            if (desc) {
                sql.append(" DESC");
            }
        }
        Collection results = super.select(sql.toString(), LeaveHoliday.class, args.toArray(), start, rows);
        return results;
    }

    /**
     * Retrieves leave settings for a specified year.
     * @param year
     * @return
     */
    public LeaveSettings selectLeaveSettings(int year) throws DaoException {
        LeaveSettings settings = null;
        String sql = "SELECT property, year, value FROM hr_leave_settings WHERE year=?";
        Object[] args = new Object[] { new Integer(year) };
        Collection results = super.select(sql, HashMap.class, args, 0, -1);
        if (results.size() > 0) {
            settings = new LeaveSettings();
            settings.setYear(year);
            for (Iterator i=results.iterator(); i.hasNext();) {
                HashMap obj = (HashMap)i.next();
                String property = (String)obj.get("property");
                String value = (String)obj.get("value");
                try {
                    setBeanProperty(settings, property, value);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).debug("Error setting leave settings property " + property + ": " + e.toString());
                }
            }
        }
        return settings;
    }

    public int updateLeaveSettings(LeaveSettings settings, int year) throws DaoException {
        String sql1 = "DELETE FROM hr_leave_settings WHERE year=?";
        String sql2 = "INSERT INTO hr_leave_settings (property, year, value) VALUES (?,?,?)";

        int rows = 0;
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();
            // delete existing
            tx.update(sql1, new Object[] { new Long(year) });

            // insert new
            String[] props = settings.getProperties();
            Long lYear = new Long(year);
            for (int i=0; i<props.length; i++) {
                String propName = props[i];
                Object value = getBeanProperty(settings, propName);
                tx.update(sql2, new Object[] { propName, lYear, value });
                rows++;
            }            tx.commit();
            return rows;
        }
        catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw new DaoException("Error updating leave settings", e);
        }
    }

    public LeaveType selectLeaveType(String id) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT id, fixedCalendar,leaveType, name, gender, creditAllowed FROM hr_leave_type WHERE id=?";
        Object[] args = new Object[] { id };
        Collection results = super.select(sql.toString(), LeaveType.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            LeaveType entry = (LeaveType)results.iterator().next();
            return entry;
        }
    }

    /**
     *
     * @return A Collection of LeaveType objects.
     * @throws DaoException
     */
    public Collection selectLeaveTypeList() throws DaoException {
        Collection args = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT id, fixedCalendar, leaveType, name, gender, creditAllowed FROM hr_leave_type ");
        sql.append(" ORDER BY leaveType, name");

        Collection results = super.select(sql.toString(), LeaveType.class, args.toArray(), 0, -1);
        return results;
    }

    public int updateLeaveType(LeaveType leaveType) throws DaoException {
        String sql1 = "UPDATE hr_leave_type SET name=?, gender=?, creditAllowed=?, fixedCalendar=? WHERE id=?";
        String sql2 = "INSERT INTO hr_leave_type (id, leaveType, name, gender, creditAllowed,fixedCalendar) VALUES (?,?,?,?,?,?)";
        String fixedCalendarStr="";
        if(leaveType.getFixedCalendar()==null)
            fixedCalendarStr="0";
        else
            fixedCalendarStr=leaveType.getFixedCalendar();

        Object[] args1 = new Object[] { leaveType.getName(), leaveType.getGender(), new Boolean(leaveType.isCreditAllowed()), fixedCalendarStr,leaveType.getId() };
        Object[] args2 = new Object[] { leaveType.getId(), leaveType.getLeaveType(), leaveType.getName(), leaveType.getGender(), new Boolean(leaveType.isCreditAllowed()),fixedCalendarStr};
        int rows = super.update(sql1, args1);
        if (rows == 0) {
            rows = super.update(sql2, args2);
        }
        return rows;
    }

    public int deleteLeaveType(String leaveTypeId) throws DaoException {
        String sql = "DELETE FROM hr_leave_type WHERE id=?";
        Object[] args = new Object[] { leaveTypeId };
        int rows = super.update(sql, args);
        return rows;
    }

    /**
     * Utility method to set data object property.
     */
    protected void setBeanProperty(Object obj, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (obj instanceof Map) {
            ((Map) obj).put(name, value);
        }
        else {
            try {
                PropertyUtils.setSimpleProperty(obj, name, value);
            }
            catch (IllegalAccessException e) {
                throw e;
            }
            catch (IllegalArgumentException ie) {
                try {
                    PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(obj, name);
                    if (pd.getPropertyType().getName().equals("boolean") && !Boolean.getBoolean(value.toString())) {
                        // handle boolean value
                        value = ("1".equals(value)) ? Boolean.TRUE : Boolean.FALSE;
                        PropertyUtils.setSimpleProperty(obj, name, value);
                    }
                    else {
                        // handle as String
                        PropertyUtils.setSimpleProperty(obj, name, value.toString());
                    }
                }
                catch (Exception e) {
                    // try to invoke mapped property in DefaultDataObject
                    setMappedProperty(obj, name, value);
                }
            }
            catch (InvocationTargetException e) {
                // try to invoke mapped property in DefaultDataObject
                setMappedProperty(obj, name, value);
            }
            catch (NoSuchMethodException e) {
                // try to invoke mapped property in DefaultDataObject
                setMappedProperty(obj, name, value);
            }
        }
    }

    /**
     * Utility method to set data object mapped property.
     */
    protected void setMappedProperty(Object obj, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PropertyUtils.setMappedProperty(obj, "property", name, value);
    }

    /**
     * Utility method to set data object property.
     */
    protected Object getBeanProperty(Object obj, String varName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        try {
            Object value = PropertyUtils.getSimpleProperty(obj, varName);
            return value;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (InvocationTargetException e) {
            return getMappedProperty(obj, varName);
        } catch (NoSuchMethodException e) {
            return getMappedProperty(obj, varName);
        }
    }

    /**
     * Utility method to set data object mapped property.
     */
    private Object getMappedProperty(Object obj, String varName) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return PropertyUtils.getMappedProperty(obj, "property", varName);
    }
//--- Employee stuff

    /**
     *
     * @param userId
     * @return Map with keys "gender", "dateJoined", "dateResigned" and "serviceClass"
     */
    public Map selectEmployeeServiceInfo(String userId) throws DaoException, DataObjectNotFoundException {
        String sql = "SELECT em_gender AS gender, em_join_date AS dateJoined, em_resign_date AS dateResigned, ud_service_class AS serviceClass " +
                "FROM employee_main e LEFT JOIN users_departments u ON e.em_employee_id=ud_employee_id " +
                "WHERE em_employee_id=?";
        Object[] args = new Object[] { userId };
        Collection results = super.select(sql, HashMap.class, args, 0, 1);
        if (results.size() > 0) {
            Map map = (Map)results.iterator().next();
            return map;
        }
        else {
            throw new DataObjectNotFoundException();
        }
    }

    /**
     * Retrieves employees who directly report to the specified approver
     * @return
     * @throws DaoException
     */
    public String[] selectEmployeeListForApprover(String approverId) throws DaoException, SecurityException {
        Collection userIdList = new ArrayList();

        // get employees as direct employees
        String sql = "SELECT eh.eh_employee_id AS userId FROM employee_hierarchy eh WHERE eh.eh_report_to=?";
        Object[] args = new Object[] { approverId };

        Collection tmp = super.select(sql.toString(), HashMap.class, args, 0, -1);
        for (Iterator i=tmp.iterator(); i.hasNext();) {
            HashMap m = (HashMap)i.next();
            userIdList.add(m.get("userId"));
        }
        return (String[])userIdList.toArray(new String[0]);
    }
    
    public Collection selectEmployeeListForApprover(String approverId, DaoQuery properties, int start, int maxResults, String sort, boolean descending) throws DaoException, SecurityException {
    	String strSort = "";
        Collection args = new ArrayList();
        args.add(approverId);
        args.addAll(Arrays.asList(properties.getArray()));
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (descending)
                strSort += " DESC";
        }
        String query = "SELECT u.id, u.username, u.password, u.weakpass, u.firstName, u.lastName, u.nickName, u.title, u.designation, u.email1, u.email2, u.email3, u.company, u.homepage, u.address, u.postcode, u.city, u.state, u.country, u.telOffice, u.telHome, u.telMobile, u.fax, u.notes, u.property1, u.property2, u.property3, u.property4, u.property5, u.active, u.locale FROM security_user u, employee_hierarchy eh WHERE u.id=eh.eh_employee_id AND eh.eh_report_to=?";
        return super.select(query + properties.getStatement() + strSort, User.class, args.toArray(), start, maxResults);
        
    }
//--- Holidays

    public Collection selectPublicHolidays(String year, String sort, boolean desc, int start, int rows) throws DaoException {
        Collection holidays = new ArrayList();
        String sql = "SELECT cm_date AS holidayDate,cm_desc AS description,cm_iscredited AS isCredited FROM calendar_main " +
				"WHERE year(cm_date) = " + year;
        if (sort.equals("holidayDate"))
            sql = sql + " ORDER BY cm_date";
        else if (sort.equals("description"))
            sql = sql + " ORDER BY cm_desc";
        if (desc)
            sql = sql + " DESC";

        holidays = super.select(sql, SetupDataObject.class, null, start, rows);
        return holidays;
    }

    public int selectPublicHolidays(String year) throws DaoException {
        String sql = "SELECT count(*) AS total FROM calendar_main " +
                "WHERE year(cm_date) = " + year;
        Map row = (Map) super.select(sql, HashMap.class, null, 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    public Collection selectPublicHoliday(String date) throws DaoException {
        Collection holidays = new ArrayList();
        String sql = "SELECT cm_date as holidayDate,cm_type AS type,cm_desc AS description FROM calendar_main" +
                " WHERE cm_date='" + date + "'";
        holidays = super.select(sql, SetupDataObject.class, null, 0, -1);
        return holidays;
    }

    public int hasHoliday(SetupDataObject setup) throws DaoException {
        String sql = "SELECT count(*) AS total FROM calendar_main WHERE cm_date=?";
        Map row = (Map) super.select(sql, HashMap.class, new Object[] {setup.getHolidayDate()}, 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
    }

    public void updateSetupHolidays(SetupDataObject setup, String date) throws DaoException {
        String sql = "UPDATE calendar_main SET cm_date=#holidayDate#,cm_type=#type#,cm_desc=#description# " +
                " WHERE cm_date=date_format('" + date + "','%Y-%m-%d')";
        super.update(sql, setup);
    }

    public void updateSetupHolidays(SetupDataObject setup) throws DaoException {
        String sql = "UPDATE calendar_main SET cm_date=#holidayDate#,cm_type=#type#,cm_desc=#description# " +
                " WHERE cm_date=#date#";
        super.update(sql, setup);
    }

    public int deleteSetupHolidays(String date, Date startDate, Date endDate) throws DaoException {
        String sql = "DELETE FROM calendar_main WHERE cm_date ='" + date + "'";
        super.update(sql, null);
        //only return the cal_event table, because previously old programming routine missed this out
        //for new holiday entered, should not be problem
        sql = "DELETE FROM cal_event WHERE startDate = ? and endDate = ? ";
        return super.update(sql, new Object[]{startDate, endDate});
    }

    public void insertSetupHolidays(SetupDataObject setup) throws DaoException {

        String sql = "INSERT INTO calendar_main (cm_date,cm_type,cm_desc) " +
                " VALUES (#holidayDate#,#type#,#description#)";
        Transaction tx = null;
        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, setup);
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public Collection getLeave(String [] users, String startDate, String endDate) throws DaoException, DataObjectNotFoundException {

        //add 00:00:00 and 23:59:59
        startDate += " 00:00:00";
        endDate +=" 23:59:59";

        String getLeave ="select l.* from hr_leave_entry l, "+
                "security_user su where (l.status LIKE 'Approved' OR "+
                "l.status LIKE 'CancelSubmitted' OR "+
                "l.status LIKE 'CancelRejected') AND "+
                "( (l.startDate <= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDAte >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') ) OR "+
                "(l.startDate >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') ) OR "+
                "(l.startDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate >= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.startDate >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') ) OR "+
                "(l.startDate <= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.startDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S')  AND "+
                "l.endDate >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate >= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S')  )) AND ( ";

        String addUser ="";
        if(users.length >0)
        {
            for(int i=0 ; i< users.length ; i++)
            {addUser +="l.userId='"+users[i]+"'";
                //   addUser +="AND l.applicantId = su.id";

                if(i <= (users.length-2))
                    addUser +=" OR ";
            }
        }
        //String getLeave2 =") GROUP BY l.startDate ";
        String getLeave2 =") AND l.days < 0 ORDER BY l.startDate, l.endDate ";
        return super.select(getLeave+addUser+getLeave2,LeaveEntry.class,null,0,-1);

/*

         String sql = "SELECT e.id, t.fixedCalendar AS fixedCalendar,e.leaveType, e.leaveTypeId, t.name AS leaveTypeName, startDate, endDate, days, halfDay, status, reason, userId, applicantId, applicationDate, approvalDate, credit, adjustment, approvalUserId, approvalComments, eventId, lastModifiedDate, lastModifiedUserId " +
                "FROM hr_leave_entry e " +
                "LEFT OUTER JOIN hr_leave_type t ON e.leaveTypeId=t.id " +
                "WHERE e.id=?";
        Object[] args = new Object[] { users };
        Collection results = super.select(sql.toString(), LeaveEntry.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            LeaveEntry entry  = (LeaveEntry)results.iterator().next();
            return entry;
        }

*/
    }
    
    
    public Collection getLeaveDistinct(String [] users, String startDate, String endDate) throws DaoException, DataObjectNotFoundException {

        //add 00:00:00 and 23:59:59
        startDate += " 00:00:00";
        endDate +=" 23:59:59";

        String getLeave ="select distinct l.* from hr_leave_entry l, "+
                "security_user su where (l.status LIKE 'Approved' OR "+
                "l.status LIKE 'CancelSubmitted' OR "+
                "l.status LIKE 'CancelRejected') AND "+
                "( (l.startDate <= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDAte >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') ) OR "+
                "(l.startDate >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') ) OR "+
                "(l.startDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate >= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.startDate >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') ) OR "+
                "(l.startDate <= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.startDate <= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S')  AND "+
                "l.endDate >= date_format('"+startDate+"', '%Y-%m-%d %H:%i:%S') AND "+
                "l.endDate >= date_format('"+endDate+"', '%Y-%m-%d %H:%i:%S')  )) AND ( ";

        String addUser ="";
        if(users.length >0)
        {
            for(int i=0 ; i< users.length ; i++)
            {addUser +="l.userId='"+users[i]+"'";
                //   addUser +="AND l.applicantId = su.id";

                if(i <= (users.length-2))
                    addUser +=" OR ";
            }
        }
        //String getLeave2 =") GROUP BY l.startDate ";
        String getLeave2 =") AND l.days < 0 ORDER BY l.startDate, l.endDate ";
        return super.select(getLeave+addUser+getLeave2,LeaveEntry.class,null,0,-1);

/*

         String sql = "SELECT e.id, t.fixedCalendar AS fixedCalendar,e.leaveType, e.leaveTypeId, t.name AS leaveTypeName, startDate, endDate, days, halfDay, status, reason, userId, applicantId, applicationDate, approvalDate, credit, adjustment, approvalUserId, approvalComments, eventId, lastModifiedDate, lastModifiedUserId " +
                "FROM hr_leave_entry e " +
                "LEFT OUTER JOIN hr_leave_type t ON e.leaveTypeId=t.id " +
                "WHERE e.id=?";
        Object[] args = new Object[] { users };
        Collection results = super.select(sql.toString(), LeaveEntry.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        }
        else {
            LeaveEntry entry  = (LeaveEntry)results.iterator().next();
            return entry;
        }

*/
    }
    
    
    public String returnEventId(Date startDate, Date endDate, String description) throws DaoException {
    	
    	Collection result =super.select("select eventId from cal_event where startDate=? and endDate= ? and description =? ", HashMap.class,new Object[]{startDate,endDate, "Holiday:"+description},0,1);
    	
    	
    	 if (result.size() > 0) {
             Map map = (Map)result.iterator().next();
             return map.get("eventId").toString();
         }
    	
    	return null;
    }
    
}
