package com.tms.tmsPIMSync.model;

import kacang.model.DefaultModule;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingService;
import kacang.services.scheduling.SchedulingException;
import kacang.Application;

import java.util.*;

import com.tms.tmsPIMSync.jobs.SyncCleanup;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jun 26, 2006
 * Time: 12:52:57 AM
 */
public class PIMSyncModule extends DefaultModule implements SecurityEventListener {
    public static final String FUNAMBOL_DEVICE_OUTLOOK = "sc-pim-outlook";
    public static final String FUNAMBOL_DEVICE_WINDOWSMOBILE = "windows-mobile";
    public static final String FUNAMBOL_DEVICE_SYMBIANMOBILE = "symbian-mobile";
    public static final String SCHEDULER_LABEL = "pim_sync_cleanup";
    Log log = Log.getLog(getClass());


    public void init() {
        super.init();
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        ss.addEventListener(this);

        //Initializing cleanup job
        JobSchedule schedule = new JobSchedule("Sync Cleanup", JobSchedule.MINUTELY);
        schedule.setGroup(SCHEDULER_LABEL);
        schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setRepeatInterval(2);
		schedule.setStartTime(new Date());

        JobTask task = new SyncCleanup();
        task.setName("Sync Cleanup");
        task.setGroup(SCHEDULER_LABEL);
        task.setDescription("PIM Sync Cleanup Cycle");

        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        try
        {
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
        }
        catch (SchedulingException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public Collection findAllUser(){
        PIMSyncDao dao = (PIMSyncDao) getDao();
        try {
            return dao.findAllUser();
        } catch (Exception e) {
            log.debug(e);
        }
        return null;
    }

    public SyncUser findUser(String username){
        PIMSyncDao dao = (PIMSyncDao) getDao();
        SyncUser su = null;
        try {
            su = dao.findUser(username);

        } catch (Exception e) {
            log.debug(e);
        }
        return su;
    }

    public void deleteUser(String username){
        PIMSyncDao dao = (PIMSyncDao) getDao();
        dao.deleteUser(username);
    }


    public Collection findAllUser(String filter, String value, int rows, String sort, boolean desc) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        try {
            return dao.findAllUser(filter, value, rows, sort, desc);
        } catch (Exception e) {
            Log.getLog(getClass()).debug(e);
        }
        return null;
    }

    public Integer countFindAllUser(String filter, String value) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        try {
            return dao.countFindAllUser(filter, value);
        } catch (Exception e) {
            Log.getLog(getClass()).debug(e);
        }

        return null;
    }

    public void saveUser(SyncUser su) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        try {
            if(!userExist(su.getUsername())){
                dao.addUser(su);

            }else dao.updateUser(su);
        } catch (Exception e) {
            Log.getLog(getClass()).debug(e);
        }
    }

    public boolean userExist(String username){
        PIMSyncDao dao = (PIMSyncDao) getDao();
        return dao.userExist(username);
    }

    public void addPrincipal(String username, String deviceId) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        dao.addPrincipal(username, deviceId);
    }


    public boolean principalExist(String username, String deviceId) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        return dao.principalExist(username, deviceId);
    }

    public void deletePrincipal(String username, String deviceId) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        dao.deletePrincipal(username, deviceId);
    }

    public void handleSecurityEvent(SecurityEvent securityEvent) {
        PIMSyncModule pm = (PIMSyncModule) Application.getInstance().getModule(PIMSyncModule.class);
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = null;
        Principal principal = securityEvent.getPrincipal();
        if(principal instanceof User){
            user = (User) securityEvent.getPrincipal();

            if(SecurityService.EVENT_USER_UPDATED.equals(securityEvent.getEventName()) || SecurityService.EVENT_USER_ADDED.equals(securityEvent.getEventName())){
                SyncUser su = new SyncUser();
                su.setUsername(user.getUsername());
                su.setPassword(user.getPassword());
                su.setFirst_name((String) user.getProperty("firstName"));
                su.setLast_name((String) user.getProperty("lastName"));
                su.setEmail((String) user.getProperty("email1"));
                pm.saveUser(su);

            }else if(SecurityService.EVENT_USER_REMOVED.equals(securityEvent.getEventName())){
                pm.deleteUser(user.getUsername());
            }
        }
    }

    public Collection getAllPrincipals(String username) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        return dao.getAllPrincipals(username);
    }

    public void saveDevice(PIMSyncDevice device){
        PIMSyncDao dao = (PIMSyncDao) getDao();
        dao.saveDevice(device);
    }

    public PIMSyncDevice getDevice(String id) {
        PIMSyncDao dao = (PIMSyncDao) getDao();
        return dao.getDevice(id);
    }

    public void cleanup()
    {
        try
        {
            PIMSyncDao dao = (PIMSyncDao) getDao();
            dao.cleanup();
        }
        catch (DaoException e)
        {
            Log.getLog(PIMSyncModule.class).error("Error while cleaning funambol database", e);
        }
    }
}
