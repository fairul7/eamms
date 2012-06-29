package com.tms.collab.taskmanager.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.util.JdbcUtil;

public class TaskManagerDaoDB2 extends TaskManagerDao {
	
	public void init()throws DaoException{

        try {

            // add completedSetBy & completedDateSetBy
            super.update("ALTER TABLE tm_assignee ADD completedSetBy VARCHAR(255)",null);
        }
        catch(Exception e) {}
        
        try {
            super.update("ALTER TABLE tm_assignee ADD completedDateSetOn TIMESTAMP",null);
        }
        catch(Exception e) {}
        
        try {
        // add comments
            super.update("ALTER TABLE tm_task ADD comments CLOB(16M)",null);
        }catch(Exception e) {}
        
        try {
            	super.update("ALTER TABLE tm_assignee ADD updateBy VARCHAR(255)",null);
        }
        catch(Exception e) {}
        	
        try {
            super.update("ALTER TABLE tm_assignee ADD updateDate TIMESTAMP",null);
        }
        catch(Exception e) {}
        
        try {
            // add priority field
            super.update("ALTER TABLE tm_task ADD taskPriority CHAR(1)",null);
        }
        catch(Exception e) {}

        Collection col = super.select("SELECT COUNT(id) as total FROM tm_category WHERE id=?",HashMap.class,new Object[]{TaskManager.DEFAULT_CATEGORY_ID},0,-1);
        boolean existed = false;
        if(col!=null&&col.size()>0){
            HashMap map = (HashMap) col.iterator().next();
            int total = 0;
            total = Integer.parseInt(map.get("total").toString());
            if(total>0)
                existed = true;
        }
        if(!existed)
        {
            try{
            super.update("INSERT INTO tm_category(id,name,description,userId,general) " +
                    "VALUES('"+TaskManager.DEFAULT_CATEGORY_ID+"','General','General Category','administrator',1)",null);
            }catch(DaoException e){
            }
        }
        
    }
	
	public void updateTaskReassignment(String taskId, boolean reassigned) throws DaoException
    {
            String sql = "UPDATE tm_task set reassign=? WHERE id=?";
            super.update(sql, new Object[] { (reassigned ? "1":"0") , taskId });
    }
	
	public Collection selectUserCategories(String userId,String name, boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
        String sql = "SELECT id,name,description,userId,general FROM tm_category " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") + " AND UPPER(name) LIKE ?" + JdbcUtil.getSort(sort, desc);

        return super.select(sql,TaskCategory.class,new Object[]{userId, '%' + name.toUpperCase() + '%'},startIndex,rows);
    }
	
	public int countUserCategories(String userId, String name, boolean includeGeneral, int startIndex, int rows) throws DaoException {
        String sql = "SELECT COUNT(id) catNum FROM tm_category " +
                "WHERE (userId=? "+ (includeGeneral ? "OR general='1')":")") + " AND UPPER(name) LIKE ?";

        Collection list = super.select(sql,HashMap.class,new Object[]{userId, '%' + name.toUpperCase() + '%'},startIndex,rows);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            int i = ((Number)map.get("catNum")).intValue();
            return i;
        }
        return 0;
    }
	
	public Collection selectTasks(String filter,String userId,String categoryId,boolean completed, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
            String nameFilter = " AND UPPER(ce.description) LIKE ? ";
            String sql = "SELECT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category," +
                    "ce.description,assigner,reassign,completed, modified, new, archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority  " +
                    "from tm_task tt LEFT JOIN cal_event ce ON " +
                    "tt.id=ce.eventId LEFT JOIN cal_event_attendee ca ON " +
                    "tt.id=ca.eventId LEFT JOIN tm_category tc ON " +
                    "tt.categoryId=tc.id WHERE " +
                    "(( tt.assignerId = ? AND ca.userId <> ? ) OR" +/* AND tt.id = ca.eventId AND ce.eventId = tt.id*/
                    "(ca.userId = ?)) "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    (categoryId==null?"":" AND tt.categoryId='"+categoryId+"'") + " "+ nameFilter +
                    (completed? " AND tt.completed='1'" : " AND tt.completed='0'") + (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            filter = (filter != null) ? "%" + filter.toUpperCase() + "%" : "%";
        Object [] args = {userId, userId,userId,filter};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }
	
	public Collection selectTasks(String filter,String userId,String categoryId, int sIndex,int maxRow,String sort,boolean desc) throws DaoException
    {
            String nameFilter = " AND UPPER(ce.description) LIKE ? ";

            String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,dueDate,categoryId,name as category,ce.description,assigner,reassign,completed, modified, new, archived, deleted," +
                    "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate,tt.taskPriority " +
                    "FROM tm_task tt INNER JOIN "+
                    "tm_category tc ON tt.categoryId=tc.id INNER JOIN "+
                    "cal_event ce ON ce.eventId=tt.id INNER JOIN "+
                    "cal_event_attendee ca ON ca.eventId=ce.eventId WHERE " +
                    "(( tt.assignerId = ? AND ca.userId <> ? ) OR" +/* AND tt.id = ca.eventId AND ce.eventId = tt.id*/
                    "(ca.userId = ?))AND ca.eventId = tt.id AND ce.eventId = tt.id "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    (categoryId==null?"":" AND tt.categoryId='"+categoryId+"'") + " AND tc.id=tt.categoryId "+ nameFilter+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            filter = (filter != null) ? "%" + filter.toUpperCase() + "%" : "%";
            Object [] args = {userId, userId,userId,filter};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }
	
	public Collection selectCalendarTasks(Date from,String[] userIds,boolean onlyIncomplete, int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
            String userClause = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " (ca.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " ) OR (tr.reassignedBy IN( "+ quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )) OR tt.assignerId IN(" +quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause+= "))";
            }

            String range = "((dueDate>=? )OR (completed='0'))";


            String sql = "SELECT DISTINCT ce.title,ce.startDate,tt.id,title,ce.eventId,endDate," +
                    "dueDate,categoryId,name as category,ce.description,assigner,assignerId," +
                    "reassign,completed, modified, new, archived, deleted,taskPriority," +
                    /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                    "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate " +
                    "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON (ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +/**/
                    "LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id WHERE " +
                    range + " AND 1=1 "/*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                    "AND "+ userClause + (onlyIncomplete?" AND completed='0'":"")+
                    (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            Object [] args = null ;
         //   if(onlyIncomplete)
                args =new Object[] {from};
         /*   else
                args =new Object[]{from};*/
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }
	
	public Collection selectCalendarTasks(Date from,Date to,String[] userIds,boolean onlyIncomplete/*, boolean onlyReminder*/,int sIndex,int maxRow,String sort, boolean desc) throws DaoException
    {
            String userClause = "";
            if (userIds != null && userIds.length > 0) {
                userClause = " (ca.userId IN (" + quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )OR (tr.reassignedBy IN( "+ quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause += " )AND tr.taskId=tt.id) OR tt.assignerId IN(" +quote(userIds[0]);
                for (int i=1; i<userIds.length; i++) {
                    userClause += ", " + quote(userIds[i]);
                }
                userClause+= "))";
            }

            String range = "((dueDate>=? AND dueDate<= ?)";
            if(onlyIncomplete){
                range += "OR (dueDate<?))";
            } else{
                range += ")" ;
            }

        String sql = "SELECT DISTINCT  ce.title,ce.startDate,tt.id,title,ce.eventId," +
                "ce.userId,endDate,dueDate,ce.reminderDate,categoryId,name as category," +
                "ce.description,assigner,assignerId,reassign,completed, modified, new, " +
                "archived, deleted,taskPriority, " +
               /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                "ce.lastModified, ce.lastModifiedBy,ce.classification,ce.creationDate " +
                "from tm_task tt INNER JOIN tm_category tc ON (tc.id=tt.categoryId) INNER JOIN cal_event ce ON (ce.eventId = tt.id) INNER JOIN cal_event_attendee ca ON (ca.eventId = tt.id) " +
                " LEFT JOIN tm_reassign tr  ON tr.taskId=tt.id  WHERE " +
                "("+range +"OR completed='0')" /*  AND ca.eventId = tt.id AND ce.eventId = tt.idAND tt.id = ce.eventId AND ca.eventId=tt.id AND tc.id=tt.id "*/ +
                "AND "+ userClause + (onlyIncomplete?" AND completed='0'":"")+
                (sort==null?"":" ORDER BY "+sort +(desc?" DESC":""));
            Object [] args = null ;
            if(onlyIncomplete)
                args =new Object[] {from,to,to};
            else
                args =new Object[]{from,to};
            return super.select(sql,Task.class,args,sIndex,maxRow);
    }
}
