package com.tms.collab.taskmanager.model;

import java.util.Collection;
import java.util.Date;

import kacang.model.DaoException;

public class TaskManagerDaoMsSql extends TaskManagerDao {
    
	public void init() throws DaoException{
		try{
			super.init();
		}catch(Exception e){
			
		}
		
		try {
    		super.update("ALTER TABLE tm_task ADD estimation Decimal(11,2)",null);
    	}catch(Exception e) {
            //ignore
        }
		
	}
	
	public Collection selectUserCategories(String userId,boolean includeGeneral,String sort,boolean desc,int startIndex, int rows) throws DaoException
    {
        String sql = "SELECT id,name,description,userId,general FROM tm_category " +
                "WHERE userId='"+userId+"'" +
                (includeGeneral?" OR general='1' ":"") +
                (sort==null?"": " ORDER BY " + (sort.equals("general")?"":sort + ",") + "general" + (desc?" DESC":"" ));
        return super.select(sql,TaskCategory.class,null,startIndex,rows);
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

            String sql = "SELECT DISTINCT  ce.title,ce.startDate,tt.id,title,ce.eventId,ce.userId,endDate,dueDate,ce.reminderDate,categoryId,name as category,ce.description,assigner,assignerId,reassign,completed, modified, new, archived, deleted," +
                   /* "ca.firstName as assigneeFirst,ca.lastName as assigneeLast, ca.userId as assigneeId, " +*/
                    "ce.lastModified, ce.lastModifiedBy,ce.classification " +
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
