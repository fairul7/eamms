package com.tms.collab.timesheet.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.util.Log;

import java.util.Collection;
import java.util.Date;

//import com.tms.collab.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 12:12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetModule extends DefaultModule {
    Log log = Log.getLog(getClass());

    public boolean add(TimeSheet obj) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            dao.insert(obj);
            return true;
        }
        catch(DaoException e) {
            log.error("error in add time sheet "+e.toString());
            return false;
        }
    }

    public boolean delete(String id){
    	TimeSheetDao dao = (TimeSheetDao) getDao();
    	try{
    		dao.delete(id);
    		return true;
    	}
    	catch(DaoException e){
    		log.error("error delete a timesheet"+e.toString());
    		return false;
    	}
    }

    public boolean update(TimeSheet obj) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            dao.update(obj);
            return true;
        }
        catch(DaoException e) {
            log.error("error in update time sheet "+e.toString());
            return false;
        }
    }

    public boolean updateAdjustment(TimeSheet obj) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            dao.updateAdjustment(obj);
            return true;
        }
        catch(DaoException e) {
            log.error("error in update adjustment "+e.toString());
            return false;
        }
    }

    public TimeSheet select(String id) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        TimeSheet obj = null;
        try {
            obj = dao.select(id);
        }
        catch(DaoException e) {
            log.error("error in select "+e.toString());
        }
        return obj;
    }

    public Collection list(String uid, String pid, String tid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.selectList(uid,pid,tid);
        }
        catch(DaoException e) {
            log.error ("error in list "+e.toString());
        }
        return col;
    }

    public int listCount(String uid, String pid, String tid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        int iRet = 0;
        try {
            iRet = dao.selectListCount(uid,pid,tid);
        }
        catch(DaoException e) {
            log.error("error in listCount "+e.toString());
        }
        return iRet;
    }

    public double getTotalHour(String uid, String pid, String tid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double dRet = 0.0;
        try {
            dRet = dao.getTotalHour(uid,pid,tid);
        }
        catch(DaoException e) {
            log.error ("error in getTotalHour "+e.toString());
        }
        return dRet;
    }
       

    public double getTotalHourReport(String uid, String pid, String tid, Date date, boolean adjust) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double dRet = 0.0;
        try {
            dRet = dao.getTotalHourReport(uid,pid,tid,date,adjust);
        }
        catch(DaoException e) {
            log.error ("error in getTotalHour "+e.toString());
        }
        return dRet;
    }
    
    public double getTotalHourReportAdjust(String uid, String pid, String tid, Date date, boolean adjust) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double dRet = 0.0;
        try {
            dRet = dao.getTotalHourReportAdjust(uid,pid,tid,date,adjust);
        }
        catch(DaoException e) {
            log.error ("error in getTotalHour "+e.toString());
        }
        return dRet;
    }
    
    /**
     * return total hour spent based on input
     * @param uid  userId of type String
     * @param pid  projectId of type String
     * @param tid  taskId of type String
     * @param startDate begining of the date range
     * @param endDate end of the date range
     * @param adjustment boolean flag if search is for adjusted entries or otherwise
     * @return totalHour of type double
     */
    public double getTotalHour(String uid, String pid, String tid, Date startDate, Date endDate, boolean adjustment)
    {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double dRet = 0.0;
        try
        {
            dRet = dao.getTotalHour(uid, pid, tid, startDate, endDate, adjustment);
        }
        catch(DaoException e)
        {
            log.error("error in getTotalHour " + e.toString(), e);
        }
        return dRet;
    }
    public double getTotalHourAdjust(String uid, String pid, String tid, Date startDate, Date endDate, boolean adjustment)
    {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double dRet = 0.0;
        try
        {
            dRet = dao.getTotalHourAdjust(uid, pid, tid, startDate, endDate, adjustment);
        }
        catch(DaoException e)
        {
            log.error("error in getTotalHour " + e.toString(), e);
        }
        return dRet;
    }

    public double getTotalAdjustedHour(String uid, String pid, String tid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double dRet = 0.0;
        try {
            dRet = dao.getTotalAdjustedHour(uid,pid,tid);
        }
        catch(DaoException e) {
            log.error("error in getTotalAdjustedHour "+e.toString());
        }
        return dRet;
    }

    public Collection getUserListByTask(String taskId) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.selectUserListByTask(taskId);
        }
        catch(DaoException e) {
            log.error("error in getUserListByTask "+e.toString());
        }
        return col;
    }

    public Collection getTaskListByProject(String projectId,String userid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.selectTaskListByProject(projectId,userid);
        }
        catch(DaoException e) {
            log.error("error in getTaskListByProject "+e.toString());
        }
        return col;
    }
    
    public Collection getProjectTaskList(String projectId,String userid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.getProjectTaskList(projectId,userid);
        }
        catch(DaoException e) {
            log.error("error in getProjectTaskList "+e.toString());
        }
        return col;
    }
    
    public Collection getNonProjectTaskList(String projectId,String userid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.getNonProjectTaskList(projectId,userid);
        }
        catch(DaoException e) {
            log.error("error in getNonProjectTaskList "+e.toString());
        }
        return col;
    }
    
    public Collection getTaskListByProjectReport(String projectId) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.getTaskListByProjectReport(projectId);
        }
        catch(DaoException e) {
            log.error("error in getTaskListByProject "+e.toString());
        }
        return col;
    }

    public Collection getNoTimeSheetTaskList(String projectId, String userid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.selectNoTimeSheetTaskList(projectId,userid);
        }
        catch(DaoException e) {
            log.error("error in getNoTimeSheetTaskList "+e.toString());
        }
        return col;
    }
    
    public Collection getNoTimeSheetNonProjectTaskList(String projectId, String userid) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.selectNoTimeSheetNonProjectTaskList(projectId,userid);
        }
        catch(DaoException e) {
            log.error("error in getNoTimeSheetNonProjectTaskList "+e.toString());
        }
        return col;
    }

    public Collection getTaskListByUser(String userId) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col = dao.selectTaskListByUser(userId);
        }
        catch(DaoException e) {
            log.error("error in getTaskListByUser "+e.toString());
        }
        return col;
    }

    public Collection getList(Date startDate, Date endDate, String userId, String sort, boolean desc, int start, int rows) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col=dao.list(startDate,endDate,userId,sort,desc, start, rows);
        }
        catch(DaoException e) {
            log.error("Error in getList "+e.toString());
        }
        return col;
    }

    public int getListCount(Date startDate, Date endDate, String userId) {
        TimeSheetDao dao = (TimeSheetDao) getDao();
        int iTotal = 0;
        try {
            iTotal = dao.listCount(startDate,endDate,userId);
        }
        catch(DaoException e) {
            log.error("Error in getListCount "+e.toString());
        }
        return iTotal;
    }

    public Collection getListByTimesheet(Date startDate, Date endDate, String userId, String sort, boolean desc, int start, int rows) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        Collection col = null;
        try {
            col=dao.listByTimesheet(startDate,endDate,userId,sort,desc, start, rows);
        }
        catch(DaoException e) {
            log.error("Error in getList "+e.toString());
        }
        return col;
    }

    public int getListCountByTimesheet(Date startDate, Date endDate, String userId) {
        TimeSheetDao dao = (TimeSheetDao) getDao();
        int iTotal = 0;
        try {
            iTotal = dao.listCountByTimesheet(startDate,endDate,userId);
        }
        catch(DaoException e) {
            log.error("Error in getListCount "+e.toString());
        }
        return iTotal;
    }

    public double getTotalHourSpentForProject(String projectId, String userId) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double total = 0;
        try {
            total = dao.getTotalHourSpentForProject(projectId, userId);
        }
        catch(DaoException e) {
            log.error("Error in getTotalHourSpentForProject "+e.toString());
        }

        return total;
    }
    
    public double getTotalHourSpentForNonProject(String projectId, String userId) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double total = 0;
        try {
            total = dao.getTotalHourSpentForNonProject(projectId, userId);
        }
        catch(DaoException e) {
            log.error("Error in getTotalHourSpentForNonProject "+e.toString());
        }

        return total;
    }
    public double getTotalHourSpentForProject(String projectId, Date date) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        double total = 0;
        try {
            total = dao.getTotalHourSpentForProject(projectId, date);
        }
        catch(DaoException e) {
            log.error("Error in getTotalHourSpentForProject "+e.toString());
        }

        return total;
    }

    public double getTotalHourSpentForProjectTillMonth(String projectId, int calendarMonth, int year) {
    	TimeSheetDao dao = (TimeSheetDao)getDao();
    	double total = 0;
        try {
            total = dao.getTotalHourSpentForProjectTillMonth(projectId, calendarMonth, year);
        }
        catch(DaoException e) {
            log.error(e, e);
        }

        return total;
    }

    /**
     * return list of user that had time sheet entry for an project in a time range
     */
    public Collection getUserListForProject(String projectId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getUserListForProject(projectId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getUserListForProject() : "+e.toString());
        }

        return null;
    }

    /**
     * get task list for user that had time sheet entry for a time range
     */
    public Collection getTaskListForUser(String userId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getTaskListForUser(userId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getTaskListForUser() : "+e.toString());
        }
        return null;
    }

    /**
     * get total hour spent by user for a project per day
     */
    public double getTotalHourSpentForProjectPerDay(String projectId, String userId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getTotalHourSpentForProjectPerDay(userId,projectId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getTotalHourSpentForProjectPerDay() : "+e.toString());
        }
        return 0.0;
    }

    /**
     * get total hour spent by user for a task per day
     */
    public double getTotalHourSpentForTaskPerDay(String userId, String taskId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getTotalHourSpentForTaskPerDay(userId,taskId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getTotalHourSpentForTaskPerDay() : "+e.toString());
        }
        return 0.0;
    }

    /**
     * get total hour spent by user for a project per day
     */
    public double getAdjustedHourSpentForProjectPerDay(String projectId, String userId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getAdjustedHourSpentForProjectPerDay(userId,projectId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getAdjustedHourSpentForProjectPerDay() : "+e.toString());
        }
        return 0.0;
    }

    /**
     * get total hour spent by user for a task per day
     */
    public double getAdjustedHourSpentForTaskPerDay(String userId, String taskId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getAdjustedHourSpentForTaskPerDay(userId,taskId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getAdjustedHourSpentForTaskPerDay() : "+e.toString());
        }
        return 0.0;
    }

    public void updateTaskCategory(String oldCategoryId, String newCategoryId) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            dao.updateTaskCategory(oldCategoryId,newCategoryId);
        }
        catch(Exception e) {
            log.error("error in updateTaskCategory() : "+e.toString());
        }
    }

    /**
     * get task list for user that had time sheet entry for a time range
     */
    public Collection getProjectListForUser(String userId, Date startDate, Date endDate) {
        TimeSheetDao dao = (TimeSheetDao)getDao();
        try {
            return dao.getTaskCategoryListForUser(userId,startDate,endDate);
        }
        catch(Exception e) {
            log.error("Error in getTaskCategoryListForUser() : "+e.toString());
        }
        return null;
    }

    public boolean insertMandayReportRemarks(String taskId, String assigneeUserId, String remarks) {
    	TimeSheetDao dao = (TimeSheetDao)getDao();
    	boolean isSuccess = true;

        try {
            dao.insertMandayReportRemarks(taskId, assigneeUserId, remarks);
        }
        catch(Exception e) {
        	isSuccess = false;
            log.error("Exception caught while inserting mandays report remarks: ", e);
        }

        return isSuccess;
    }

    public String getMandayReportRemarks(String taskId, String assigneeUserId) {
    	TimeSheetDao dao = (TimeSheetDao)getDao();

    	try {
            return dao.getMandayReportRemarks(taskId, assigneeUserId);
        }
        catch(Exception e) {
            log.error("Exception caught while retrieving mandays report remarks: ", e);
        	return null;
        }
    }
}
