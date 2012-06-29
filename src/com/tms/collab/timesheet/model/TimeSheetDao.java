package com.tms.collab.timesheet.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.ui.WidgetManager;
import kacang.util.UuidGenerator;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

//import com.tms.collab.project.Project;
//import com.tms.collab.taskmanager.model.Task;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 22, 2005
 * Time: 11:14:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetDao extends DataSourceDao{

    /**
     *  create table if the table is not existed in init()
     */
    public void init() throws DaoException{
        try {
			super.update("ALTER TABLE timesheet ADD COLUMN taskTitle varchar(255) default ''",null);
		} catch (Exception e1) {
			// ignore
		}
        try {
			super.update("ALTER TABLE timesheet ADD COLUMN taskCategoryName varchar(255) default ''",null);
		} catch (DaoException e1) {
			// ignore
		}
        try {
        super.update("CREATE TABLE timesheet ("+
                "id VARCHAR(255),"+
                "createdDateTime DATETIME,"+
                "projectId VARCHAR(255),"+
                "taskId VARCHAR(255),"+
                "createdBy VARCHAR(255),"+
                "tsDate DATE,"+
                "duration FLOAT,"+
                "description TEXT,"+
                "adjustment VARCHAR(1) DEFAULT 'N',"+
                "adjustmentDateTime DATETIME,"+
                "adjustmentBy VARCHAR(255),"+
                "adjustmentById VARCHAR(255),"+
                "adjustmentDescription TEXT,"+
                "adjustedDuration FLOAT, " +
                "taskTitle VARCHAR(255) DEFAULT '', " +
                "taskCategoryName VARCHAR(255) DEFAULT '')",null);
        }
        catch(Exception e) {}
        try {
        	super.update("CREATE TABLE timesheet_manday_remarks (" +
        			"id VARCHAR(255), " +
        			"taskId VARCHAR(255), " +
        			"assigneeUserId VARCHAR(255), " +
        			"remarks VARCHAR(255), " +
        			"lastUpdatedBy VARCHAR(255), " +
        			"lastUpdatedDate DATETIME, " +
        			"PRIMARY KEY(id))", null);
        }
        catch(Exception e) {}
    }

    /**
     * insert new record
     * @param obj TimeSheet
     */
    public void insert(TimeSheet obj) throws DaoException {
        String id = UuidGenerator.getInstance().getUuid();
        obj.setId(id);
        super.update("INSERT INTO timesheet ("+
                "id,createdDateTime,projectId,taskId,createdBy,"+
                "tsDate,duration,description,adjustment,adjustmentDateTime,"+
                "adjustmentBy,adjustmentById,adjustmentDescription,adjustedDuration,taskCategoryName,"+
                "taskTitle) "+
                "VALUES "+
                "(#id#,NOW(),#projectId#,#taskId#,#createdBy#,"+
                "#tsDate#,#duration#,#description#,'N',curDate(),"+
                "#adjustmentBy#,#adjustmentById#,#adjustmentDescription#,0,#taskCategoryName#,"+
                "#taskTitle#)",obj);
    }


    public void delete(String id) throws DaoException {

    	super.update("DELETE  FROM timesheet where id=?", new Object[]{id});


    }

    /**
     * update record
     * @param obj TimeSheet
     */
    public void update(TimeSheet obj) throws DaoException {
        super.update("UPDATE timesheet SET "+
                "tsDate=#tsDate#,"+
                "duration=#duration#, "+
                "description=#description# WHERE "+
                "id=#id#",obj);
    }

    /**
     * update adjustment
     * @param obj TimeSheet
     */
    public void updateAdjustment(TimeSheet obj) throws DaoException {
        super.update("UPDATE timesheet SET "+
                "adjustment=#adjustment#,"+
                "adjustmentDateTime=curDate(),"+
                "adjustmentById=#adjustmentById#,"+
                "adjustmentBy=#adjustmentBy#,"+
                "adjustmentDescription=#adjustmentDescription#,"+
                "adjustedDuration=#adjustedDuration# "+
                "WHERE id=#id#",obj);
    }

    /**
     * select record by id
     * @param id String
     * @return tsObj TimeSheet
     */
    public TimeSheet select(String id) throws DaoException {
        Collection col = super.select("SELECT id,createdDateTime,projectId,taskId,createdBy,"+
                "tsDate,duration,description,adjustment,adjustmentDateTime,"+
                "adjustmentById,adjustmentBy,adjustmentDescription,adjustedDuration,taskCategoryName,"+
                "taskTitle FROM timesheet "+
                "WHERE id=?",TimeSheet.class,new String[] {id},0,1);
        TimeSheet tsObj=null;
        if (col.size()>0) {
            tsObj = (TimeSheet)col.iterator().next();
        }
        return tsObj;
    }

    /**
     * return list of record based on input
     * @param uid String
     * @param pid String
     * @param tid String
     * @return col Collection
     */
    public Collection selectList(String uid,String pid,String tid) throws DaoException {
        String sUid = (uid==null||uid.equals(""))?"%":"%"+uid+"%";
        String sPid = (pid==null||pid.equals(""))?"%":"%"+pid+"%";
        String sTid = (tid==null||tid.equals(""))?"%":"%"+tid+"%";

        Object obj = new String[] {sUid, sPid, sTid};

        String sql = "SELECT id,createdDateTime,projectId,taskId,createdBy,"+
                "tsDate,duration,description,adjustment,adjustmentDateTime,"+
                "adjustmentById, adjustmentBy,adjustmentDescription,adjustedDuration,taskCategoryName,"+
                "taskTitle FROM timesheet "+
                "WHERE createdBy like ? "+
                "AND projectId like ? "+
                "AND taskId like ? "+
                "ORDER BY tsDate DESC";

        Collection col = super.select(sql,TimeSheet.class,obj,0,-1);
        return col;
    }

    /**
     * return total count of record based on input
     * @param uid String
     * @param pid String
     * @param tid String
     * @return iRet int
     */
    public int selectListCount(String uid, String pid, String tid) throws DaoException {
        String sUid = (uid==null||uid.equals(""))?"%":"%"+uid+"%";
        String sPid = (pid==null||pid.equals(""))?"%":"%"+pid+"%";
        String sTid = (tid==null||tid.equals(""))?"%":"%"+tid+"%";

        Object obj = new String[] {sUid, sPid, sTid};

        String sql = "SELECT count(id) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                "AND projectId like ? "+
                "AND taskId like ? ";

        Collection col = super.select(sql,HashMap.class,obj,0,1);
        HashMap hashMap = (HashMap)col.iterator().next();

        int i = Integer.parseInt(hashMap.get("total").toString());
        return i;
    }

    /**
     * return total hour spent based on input
     * @param uid  userId of type String
     * @param pid  projectId of type String
     * @param tid  taskId of type String
     * @return totalHour of type double
     * @throws DaoException
     */
    public double getTotalHour(String uid, String pid, String tid) throws DaoException {
        String sUid = (uid==null)?"%":"%"+uid+"%";
        //String sPid = (pid==null)?"%":"%"+pid+"%";
        String sTid = (tid==null)?"%":"%"+tid+"%";

        Object obj = new String[] {sUid,sTid}; //{sUid, sPid, sTid};
        double d=0;

        String sql = "SELECT SUM(duration) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                //"AND projectId like ? "+
                "AND taskId like ? "+
                "AND adjustment='N'";

        try {
            Collection col = super.select(sql,HashMap.class,obj,0,1);
            if (col!=null && col.size()>0) {
                HashMap hashMap = (HashMap)col.iterator().next();
                d = Double.parseDouble(hashMap.get("total").toString());
            }
        }
        catch(Exception e) {

        }
        return d;
    }
    
    public double getTotalHourReport(String uid, String pid, String tid, Date date, boolean adjust) throws DaoException {
        String sUid = (uid==null)?"%":"%"+uid+"%";
        //String sPid = (pid==null)?"%":"%"+pid+"%";
        String sTid = (tid==null)?"%":"%"+tid+"%";
        Object obj = new Object[] {sUid,sTid,date}; //{sUid, sPid, sTid};
        double d=0;

        String sql = "SELECT SUM(duration) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                //"AND projectId like ? "+
                "AND taskId like ? "+
                "AND adjustment='N' " +
                "AND tsDate<=?";

        try {
            Collection col = super.select(sql,HashMap.class,obj,0,1);
            if (col!=null && col.size()>0) {
                HashMap hashMap = (HashMap)col.iterator().next();
                d = Double.parseDouble(hashMap.get("total").toString());
            }
        }
        catch(Exception e) {

        }
        return d;
    }
    
    public double getTotalHourReportAdjust(String uid, String pid, String tid, Date date, boolean adjust) throws DaoException {
        String sUid = (uid==null)?"%":"%"+uid+"%";
        //String sPid = (pid==null)?"%":"%"+pid+"%";
        String sTid = (tid==null)?"%":"%"+tid+"%";
        Object obj = new Object[] {sUid,sTid,date}; //{sUid, sPid, sTid};
        double d=0;

        String sql = "SELECT SUM(adjustedDuration) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                //"AND projectId like ? "+
                "AND taskId like ? "+
                "AND adjustment='Y' " +
                "AND tsDate<=?";

        try {
            Collection col = super.select(sql,HashMap.class,obj,0,1);
            if (col!=null && col.size()>0) {
                HashMap hashMap = (HashMap)col.iterator().next();
                d = Double.parseDouble(hashMap.get("total").toString());
            }
        }
        catch(Exception e) {

        }
        return d;
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
     * @throws DaoException
     */
    public double getTotalHour(String uid, String pid, String tid, Date startDate, Date endDate, boolean adjustment) throws DaoException
    {
        String sUid = (uid==null) ? "%" : "%" + uid + "%";
        String sTid = (tid==null) ? "%" : "%" + tid + "%";

        double d=0;
        String sql = "SELECT SUM(duration) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                "AND taskId like ? "+
                "AND (tsDate >= ? AND tsDate <= ?)" +
                "AND adjustment = 'N'";
        try
        {
            Collection col = super.select(sql, HashMap.class, new Object[] {sUid, sTid, startDate, endDate}, 0, 1);
            if (col!=null && col.size()>0)
            {
                HashMap hashMap = (HashMap)col.iterator().next();
                d = Double.parseDouble(hashMap.get("total").toString());
            }
        }
        catch(Exception e) {}
        return d;
    }
    
    public double getTotalHourAdjust(String uid, String pid, String tid, Date startDate, Date endDate, boolean adjustment) throws DaoException
    {
        String sUid = (uid==null) ? "%" : "%" + uid + "%";
        String sTid = (tid==null) ? "%" : "%" + tid + "%";
        double d=0;
        String sql = "SELECT SUM(adjustedDuration) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                "AND taskId like ? "+
                "AND (tsDate >= ? AND tsDate <= ?)" +
                "AND adjustment = 'Y'";
        try
        {
            Collection col = super.select(sql, HashMap.class, new Object[] {sUid, sTid, startDate, endDate}, 0, 1);
            if (col!=null && col.size()>0)
            {
                HashMap hashMap = (HashMap)col.iterator().next();
                d = Double.parseDouble(hashMap.get("total").toString());
            }
        }
        catch(Exception e) {}
        return d;
    }


    /**
     * return total adjusted hour
     * @param uid userId of type String
     * @param pid projectId of type String
     * @param tid taskId of type String
     * @return totalAdjustedHour of type double
     * @throws DaoException
     */
    public double getTotalAdjustedHour(String uid, String pid, String tid) throws DaoException  {
        String sUid = (uid==null)?"%":"%"+uid+"%";
        //String sPid = (pid==null)?"%":"%"+pid+"%";
        String sTid = (tid==null)?"%":"%"+tid+"%";

        Object obj = new String[] {sUid,sTid}; //{sUid, sPid, sTid};
        double d=0;

        String sql = "SELECT SUM(adjustedDuration) as total FROM timesheet "+
                "WHERE createdBy like ? "+
                //"AND projectId like ? "+
                "AND taskId like ? "+
                "AND adjustment='Y'";

        try {
            Collection col = super.select(sql,HashMap.class,obj,0,1);
            if (col!=null && col.size()>0) {
                HashMap hashMap = (HashMap)col.iterator().next();
                d = Double.parseDouble(hashMap.get("total").toString());
            }
        }
        catch(Exception e) {

        }
        return d;
    }

    /**
     * select user list based on taskId
     * @param taskId  of type String
     * @return collection of userId
     * @throws DaoException
     */
    public Collection selectUserListByTask(String taskId) throws DaoException {
        String sql = "SELECT DISTINCT(createdBy) as userid FROM timesheet WHERE taskId=?";

        Collection col = super.select(sql,HashMap.class,new String[]{taskId},0,-1);
        return col;
    }

    /**
     *
     * @param projectId
     * @param userid
     * @return
     * @throws DaoException
     */
    public Collection selectTaskListByProject(String projectId,String userid) throws DaoException {
        String sUser = (userid==null)?"":" and createdBy='"+userid+"'";
        String sql = "SELECT DISTINCT(taskId) as task FROM timesheet, tm_task WHERE projectId=? " +
        		"AND timesheet.taskId = tm_task.id "+sUser;
        Collection col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;
    }
    
    public Collection getProjectTaskList(String projectId,String userid) throws DaoException {
        String sUser = (userid==null)?"":" and (tm_task.assignerId='"+userid+"' OR worms_project.ownerId='"+userid+"')";
        String sql = "SELECT DISTINCT(timesheet.taskId) as task FROM timesheet INNER JOIN tm_task ON timesheet.taskId = tm_task.id " +
                "LEFT JOIN worms_project ON timesheet.projectId=worms_project.projectId WHERE timesheet.projectId=? " +
        		""+sUser;
        Collection col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;
    }
    
    public Collection getNonProjectTaskList(String projectId,String userid) throws DaoException {
        String sUser = (userid==null)?"":" and (tm_task.assignerId='"+userid+"')";
        String sql = "SELECT DISTINCT(timesheet.taskId) as task FROM timesheet " +
				"LEFT JOIN tm_category ON timesheet.projectId=tm_category.id, tm_task " +
				"WHERE timesheet.projectId=? " +
        		"AND timesheet.taskId = tm_task.id "+sUser;
        Collection col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;
    }

    /*public Collection getTaskListByProjectReport(String projectId) throws DaoException {

        String sql = "SELECT DISTINCT(worms_milestone_task.taskId) as task FROM tm_task, worms_milestone, worms_milestone_task " +
        		"LEFT JOIN timesheet ON tm_task.id=timesheet.taskId  WHERE timesheet.projectId=? " +
        		"AND timesheet.projectId=worms_milestone.projectId AND worms_milestone.milestoneId=worms_milestone_task.milestoneId " +
        		"AND worms_milestone_task.taskId=tm_task.id ";
        Collection col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;
    }*/
    
    
    public Collection getTaskListByProjectReport(String projectId) throws DaoException {

        String sql = "SELECT DISTINCT(worms_milestone_task.taskId) as task FROM worms_milestone_task,worms_milestone,tm_task INNER JOIN cal_event ce ON (ce.eventId = tm_task.id)   " +
        		"LEFT JOIN timesheet ON tm_task.id=timesheet.taskId AND timesheet.projectId=? WHERE worms_milestone.projectId=? " +
        		"AND worms_milestone.milestoneId=worms_milestone_task.milestoneId " +
        		"AND worms_milestone_task.taskId=tm_task.id ORDER BY worms_milestone.milestoneOrder, ce.startDate";
        Collection col = super.select(sql,HashMap.class,new Object[]{projectId, projectId},0,-1);
        return col;
    }
    
    /**
     *
     * @param projectId
     * @return
     * @throws DaoException
     */
    public Collection selectNoTimeSheetTaskList(String projectId, String userid) throws DaoException {
        Collection col = getProjectTaskList(projectId,userid);
        String s="";

        if (col!=null && col.size()>0){
            for (Iterator i=col.iterator();i.hasNext();) {
                HashMap map = (HashMap)i.next();
                String taskId = (String)map.get("task");
                if (taskId!=null && !taskId.equals("")) {
                    s += "AND tt.id<>'"+taskId+"' ";
                }
            }
        }
        String sUser = (userid==null)?"":" and (tt.assignerId='"+userid+"' OR wp.ownerId='"+userid+"')";
        String sql = "SELECT DISTINCT (tt.id) as taskId " +
                    "FROM tm_task tt LEFT JOIN tm_category tc ON "+
                    "tt.categoryId=tc.id LEFT JOIN " +
                    "worms_project wp ON wp.projectId=tc.id WHERE " +
                    "wp.projectId =? "+s+sUser;
        col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;

    }
    
    public Collection selectNoTimeSheetNonProjectTaskList(String projectId, String userid) throws DaoException {
        Collection col = getNonProjectTaskList(projectId,userid);
        String s="";

        if (col!=null && col.size()>0){
            for (Iterator i=col.iterator();i.hasNext();) {
                HashMap map = (HashMap)i.next();
                String taskId = (String)map.get("task");
                if (taskId!=null && !taskId.equals("")) {
                    s += "AND tt.id<>'"+taskId+"' ";
                }
            }
        }
        String sUser = (userid==null)?"":" and (tt.assignerId='"+userid+"')";
        String sql = "SELECT DISTINCT (tt.id) as taskId  " +
                    "FROM tm_task tt LEFT JOIN tm_category tc ON "+
                    "tt.categoryId=tc.id  WHERE " +
                    "tc.id =? "+s+sUser;
        col = super.select(sql,HashMap.class,new String[]{projectId},0,-1);
        return col;

    }

    public Collection selectTaskListByUser(String userId) throws DaoException {
        String sql = "SELECT DISTINCT(taskId) as task FROM timesheet WHERE createdBy=?";
        Collection col = super.select(sql,HashMap.class,new String[]{userId},0,-1);
        return col;
    }

    public Collection list(Date startDate, Date endDate, String userId, String sort, boolean desc, int start, int row)
            throws DaoException {
        String sEndDate = "";
        Object[] obj = new Object[]{userId,startDate};

        if (endDate!=null) {
            sEndDate = " AND ts.createdDateTime<=?";
            obj = new Object[]{userId,startDate,endDate};
        }

        String sDesc=desc?" ":" DESC";
        String sql = "SELECT ts.id,ts.createdDateTime,ts.projectId,ts.taskId,ts.createdBy,"+
                "ts.tsDate,ts.duration,ts.description,ts.adjustment,ts.adjustmentDateTime,"+
                "ts.adjustmentById,ts.adjustmentBy,ts.adjustmentDescription,ts.adjustedDuration,ts.taskCategoryName, "+
                "ce.title as taskName, tc.name as categoryName "+
                "FROM timesheet ts LEFT JOIN cal_event ce on ts.taskId=ce.eventId "+
                "LEFT JOIN tm_task tt on tt.id=ts.taskId "+
                "LEFT JOIN tm_category tc on tc.id=tt.categoryId "+
                "WHERE ts.createdBy=? AND ts.createdDateTime>=? " +  sEndDate +
                (sort==null?"":" ORDER BY "+sort+" "+sDesc);

        Collection col = super.select(sql,TimeSheet.class,obj,start,row);
        return col;
    }

    public int listCount(Date startDate, Date endDate, String userId) throws DaoException {
        String sEndDate = "";
        Object[] obj = new Object[]{userId,startDate};

        if (endDate!=null) {
            sEndDate = " AND createdDateTime<=?";
            obj = new Object[]{userId,startDate,endDate};
        }

        String sql = "SELECT COUNT(id) as total FROM timesheet "+
                "WHERE createdBy=? AND createdDateTime>=?";

        Collection col = super.select(sql+sEndDate,HashMap.class,obj,0,1);
        int iReturn = 0;

        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            iReturn = Integer.parseInt(map.get("total").toString());
        }
        return iReturn;
    }

    public Collection listByTimesheet(Date startDate, Date endDate, String userId, String sort, boolean desc, int start, int rows)
            throws DaoException {
        String sEndDate = "";
        Object[] obj = new Object[]{userId,startDate};

        if (endDate!=null) {
            sEndDate = " AND ts.tsDate<=?";
            obj = new Object[]{userId,startDate,endDate};
        }

        String sDesc=desc?" ":" DESC";
        String sql = "SELECT ts.id,ts.createdDateTime,ts.projectId,ts.taskId,ts.createdBy,"+
                "ts.tsDate,ts.duration,ts.description,ts.adjustment,ts.adjustmentDateTime,"+
                "ts.adjustmentById,ts.adjustmentBy,ts.adjustmentDescription,ts.adjustedDuration,ts.taskCategoryName, "+
                "ce.title as taskName, tc.name as categoryName "+
                "FROM timesheet ts LEFT JOIN cal_event ce on ts.taskId=ce.eventId "+
                "LEFT JOIN tm_task tt on tt.id=ts.taskId "+
                "LEFT JOIN tm_category tc on tc.id=tt.categoryId "+
                "WHERE ts.createdBy=? AND ts.tsDate>=? " +  sEndDate + " ORDER BY "+
                (sort==null?" ts.tsDate ":sort+" "+sDesc);

        Collection col = super.select(sql,TimeSheet.class,obj,start,rows);
        return col;
    }

    public int listCountByTimesheet(Date startDate, Date endDate, String userId) throws DaoException {
        String sEndDate = "";
        Object[] obj = new Object[]{userId,startDate};

        if (endDate!=null) {
            sEndDate = " AND tsDate<=?";
            obj = new Object[]{userId,startDate,endDate};
        }

        String sql = "SELECT COUNT(id) as total FROM timesheet "+
                "WHERE createdBy=? AND tsDate>=?";

        Collection col = super.select(sql+sEndDate,HashMap.class,obj,0,1);
        int iReturn = 0;

        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            iReturn = Integer.parseInt(map.get("total").toString());
        }
        return iReturn;
    }

    public double getTotalHourSpentForProject(String projectId, String userid) throws DaoException {
        double totalHourSpent=0;
        String sUser = (userid==null||"".equals(userid))?"":" and (tm_task.assignerId='"+userid+"' OR worms_project.ownerId='"+userid+"')";
        String sql = "SELECT SUM(timesheet.duration) as total FROM timesheet INNER JOIN tm_task " +
        		"ON timesheet.taskId=tm_task.id INNER JOIN worms_project ON " +
        		"timesheet.projectId=worms_project.projectId WHERE timesheet.projectId=? AND " +
        		"timesheet.adjustment='N'"+sUser;
        Collection col = super.select(sql,HashMap.class,new Object[]{projectId},0,-1);
        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                totalHourSpent = Double.parseDouble(map.get("total").toString());
        }

        sql = "SELECT SUM(timesheet.adjustedDuration) as total FROM timesheet INNER JOIN tm_task " +
        		"ON timesheet.taskId=tm_task.id INNER JOIN worms_project ON " +
        		"timesheet.projectId=worms_project.projectId WHERE timesheet.projectId=? AND " +
        		"timesheet.adjustment='Y'"+sUser;
        col = super.select(sql,HashMap.class,new Object[]{projectId},0,-1);
        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                totalHourSpent += Double.parseDouble(map.get("total").toString());
        }
        return totalHourSpent;
    }
    
    public double getTotalHourSpentForNonProject(String projectId, String userid) throws DaoException {
        double totalHourSpent=0;
        String sUser = (userid==null||"".equals(userid))?"":" and (tm_task.assignerId='"+userid+"')";
        String sql = "SELECT SUM(timesheet.duration) as total FROM timesheet INNER JOIN tm_task " +
        		"ON timesheet.taskId=tm_task.id INNER JOIN tm_category ON " +
        		"timesheet.projectId=tm_category.id WHERE timesheet.projectId=? AND " +
        		"timesheet.adjustment='N'"+sUser;
        Collection col = super.select(sql,HashMap.class,new Object[]{projectId},0,-1);
        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                totalHourSpent = Double.parseDouble(map.get("total").toString());
        }

        sql = "SELECT SUM(timesheet.adjustedDuration) as total FROM timesheet INNER JOIN tm_task " +
        		"ON timesheet.taskId=tm_task.id INNER JOIN tm_category ON " +
        		"timesheet.projectId=tm_category.id WHERE timesheet.projectId=? AND " +
        		"timesheet.adjustment='Y'"+sUser;
        col = super.select(sql,HashMap.class,new Object[]{projectId},0,-1);
        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                totalHourSpent += Double.parseDouble(map.get("total").toString());
        }
        return totalHourSpent;
    }
    
    public double getTotalHourSpentForProject(String projectId, Date date) throws DaoException {
        double totalHourSpent=0;
        String sql = "SELECT SUM(duration) as total FROM timesheet WHERE projectId=? AND adjustment='N' AND createdDateTime<=?";
        Collection col = super.select(sql,HashMap.class,new Object[]{projectId, date},0,-1);
        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                totalHourSpent = Double.parseDouble(map.get("total").toString());
        }

        sql = "SELECT SUM(adjustedDuration) as total FROM timesheet WHERE projectId=? AND adjustment='Y' AND createdDateTime<=?";
        col = super.select(sql,HashMap.class,new Object[]{projectId, date},0,-1);
        if (col!=null&&col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                totalHourSpent += Double.parseDouble(map.get("total").toString());
        }
        return totalHourSpent;
    }

    public double getTotalHourSpentForProjectTillMonth(String projectId, int calendarMonth, int year) throws DaoException {
    	double totalHourSpent=0;

    	if(calendarMonth >=0 && calendarMonth <= 11) {
	        String sql = "SELECT SUM(duration) as total " +
	        		"FROM timesheet WHERE projectId=? " +
	        		"AND adjustment='N' " +
	        		"AND adjustmentDateTime <= ? ";
	        Collection col = super.select(sql,HashMap.class,new Object[]{projectId, year + "-" + calendarMonth + "31"},0,-1);
	        if (col!=null&&col.size()>0) {
	            HashMap map = (HashMap)col.iterator().next();
	            if (map.get("total")!=null)
	                totalHourSpent = Double.parseDouble(map.get("total").toString());
	        }

	        sql = "SELECT SUM(adjustedDuration) as total " +
	        		"FROM timesheet " +
	        		"WHERE projectId=? AND adjustment='Y' " +
	        		"AND adjustmentDateTime <= ? ";
	        col = super.select(sql,HashMap.class,new Object[]{projectId, year + "-" + calendarMonth + "31"},0,-1);
	        if (col!=null&&col.size()>0) {
	            HashMap map = (HashMap)col.iterator().next();
	            if (map.get("total")!=null)
	                totalHourSpent += Double.parseDouble(map.get("total").toString());
	        }
	    }
    	else {
    		throw new DaoException("The month given, " + calendarMonth + ", is invalid. Please refer to the constant field value in java.util.Calendar.");
    	}

        return totalHourSpent;
    }

    /**
     * get user list for a project between a time range
     */
    public Collection getUserListForProject(String projectId, Date startDate, Date endDate)
        throws DaoException {
        String sql = "SELECT DISTINCT createdBy as userId FROM timesheet WHERE projectId=? AND tsDate>=? AND tsDate<=?";
        Collection col = super.select(sql,HashMap.class,new Object[]{projectId,startDate,endDate},0,-1);
        return col;
    }

    /**
     * get task list for user between a time range
     */
    public Collection getTaskListForUser(String userId,Date startDate, Date endDate)
    throws DaoException {
        String sql = "SELECT DISTINCT taskId as taskId FROM timesheet WHERE createdBy=? AND tsDate>=? AND tsDate<=?";
        Collection col = super.select(sql,HashMap.class,new Object[]{userId,startDate,endDate},0,-1);
        return col;
    }

    /**
     * return total hour spend per day for user under a project
     *
     */
    public double getTotalHourSpentForProjectPerDay(String userId, String projectId, Date startDate, Date endDate)
        throws DaoException {
        double dRet=0.0;
        String sql = "SELECT SUM(duration) as total FROM timesheet WHERE createdBy=? AND projectId=? AND "+
                    "tsDate>=? AND tsDate<=? AND adjustment='N'";
        Collection col = super.select(sql,HashMap.class,new Object[]{userId,projectId,startDate,endDate},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                dRet = Double.parseDouble(map.get("total").toString());
        }

        return dRet;
    }

    /**
     * return total hour spend per day by user for a task
     */
    public double getTotalHourSpentForTaskPerDay(String userId, String taskId, Date startDate, Date endDate)
        throws DaoException {
        double dRet = 0.0;
        String sql = "SELECT SUM(duration) as total FROM timesheet WHERE "+
                    "createdBy=? AND taskId=? AND tsDate>=? AND tsDate<=? AND adjustment='N'";
        Collection col = super.select(sql,HashMap.class,new Object[] {userId,taskId,startDate,endDate},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null) {
                dRet = Double.parseDouble(map.get("total").toString());
            }
        }
        return dRet;
    }

    /**
     * return total adjusted hour spend per day for user under a project
     *
     */
    public double getAdjustedHourSpentForProjectPerDay(String userId, String projectId, Date startDate, Date endDate)
        throws DaoException {
        double dRet=0.0;
        String sql = "SELECT SUM(adjustedDuration) as total FROM timesheet WHERE createdBy=? AND projectId=? AND "+
                    "tsDate>=? AND tsDate<=? AND adjustment='Y'";
        Collection col = super.select(sql,HashMap.class,new Object[]{userId,projectId,startDate,endDate},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null)
                dRet = Double.parseDouble(map.get("total").toString());
        }

        return dRet;
    }

    /**
     * return total adjusted hour spend per day by user for a task
     */
    public double getAdjustedHourSpentForTaskPerDay(String userId, String taskId, Date startDate, Date endDate)
        throws DaoException {
        double dRet = 0.0;
        String sql = "SELECT SUM(adjustedDuration) as total FROM timesheet WHERE "+
                    "createdBy=? AND taskId=? AND tsDate>=? AND tsDate<=? AND adjustment='Y'";
        Collection col = super.select(sql,HashMap.class,new Object[] {userId,taskId,startDate,endDate},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            if (map.get("total")!=null) {
                dRet = Double.parseDouble(map.get("total").toString());
            }
        }
        return dRet;
    }

    /**
     * update task category - when a category is deleted, the task will be under general by default
     */
    public void updateTaskCategory(String oldCategoryId, String newCategoryId) throws DaoException {
        String sql = "UPDATE timesheet SET taskId='"+newCategoryId+"' WHERE taskId='"+oldCategoryId+"'";
        super.update(sql,null);
    }

    /**
     * get task category list for user between a time range
     */
    public Collection getTaskCategoryListForUser(String userId,Date startDate, Date endDate)
    throws DaoException {
        String sql = "SELECT DISTINCT projectId AS projectId FROM timesheet WHERE createdBy=? AND tsDate>=? AND tsDate<=?";
        Collection col = super.select(sql,HashMap.class,new Object[]{userId,startDate,endDate},0,-1);
        return col;
    }

    public void insertMandayReportRemarks(String taskId, String assigneeUserId, String remarks) throws DaoException {
    	if(taskId != null && !"".equals(taskId)) {
    		if(assigneeUserId == null) {
    			assigneeUserId = "";
    		}

    		String sqlDelete = "DELETE FROM timesheet_manday_remarks " +
    				"WHERE taskId = ? and assigneeUserId = ?";

	    	String sqlInsert = "INSERT INTO timesheet_manday_remarks " +
	    			"(id, taskId, assigneeUserId, remarks, lastUpdatedBy, lastUpdatedDate) values (" +
	    			"?, ?, ?, ?, ?, now())";

	    	String newId = UuidGenerator.getInstance().getUuid();
	    	String userId = getCurrentUserFromDWR().getId();

	    	super.update(sqlDelete, new Object[] {taskId, assigneeUserId});
	    	super.update(sqlInsert, new Object[] {newId, taskId, assigneeUserId, remarks, userId});
    	}
    	else {
    		throw new DaoException("Parameter taskId shouldn't be null");
    	}
    }

    public String getMandayReportRemarks(String taskId, String assigneeUserId) throws DaoException {
    	String remarks = "";

    	if(taskId != null && !"".equals(taskId)) {
    		if(assigneeUserId == null) {
    			assigneeUserId = "";
    		}

	    	String sql = "SELECT remarks FROM timesheet_manday_remarks " +
	    			"WHERE taskId = ? " +
	    			"AND assigneeUserId = ?";

	    	Collection col = super.select(sql, HashMap.class, new Object[] {taskId, assigneeUserId}, 0, 1);
	    	if(col != null) {
	    		if(col.size() > 0) {
	    			HashMap map = (HashMap) col.iterator().next();
	    			remarks = map.get("remarks").toString();
	    		}
	    	}
    	}
    	else {
    		throw new DaoException("Parameter taskId shouldn't be null");
    	}

    	return remarks;
    }

    public User getCurrentUserFromDWR() {
    	User currentUser = null;

    	WebContext ctx = WebContextFactory.get();
    	HttpServletRequest request = ctx.getHttpServletRequest();

    	if (request != null) {
            WidgetManager wm = WidgetManager.getWidgetManager(request);
            currentUser = wm.getUser();
        }

    	return currentUser;
    }
}


