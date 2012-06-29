package com.tms.fms.setup.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;

public class SetupDao extends DataSourceDao {
	public void init() throws DaoException
	{
		try{

			super.update("CREATE TABLE fms_prog_setup ("+
					"programId varchar(255) NOT NULL default '',"+
					"programName varchar(255) default NULL,"+
					"description varchar(255) default NULL," +
					"producer varchar(255),"+
					"pfeCode varchar(255),"+
					"startProductionDate datetime NULL,"+
					"endProductionDate datetime NULL,"+
					"department varchar(255),"+
					"status CHAR(1), " +
					"createdBy varchar(255),"+
					"createdDate datetime NULL,"+
					"updatedBy varchar(255) ,"+
					"updatedDate datetime NULL," +
					"PRIMARY KEY (programId))",null);      

		}catch(Exception er){
		}

		try {
			super.update("ALTER TABLE fms_prog_setup ADD producer VARCHAR(255)", null);
		} catch (Exception e){    			
		}

		try {
			super.update("ALTER TABLE fms_prog_setup ADD engManpowerBudget VARCHAR(255)", null);
		} catch (Exception e){    			
		}

		try {
			super.update("ALTER TABLE fms_prog_setup ADD facilitiesBudget VARCHAR(255)", null);
		} catch (Exception e){    			
		}

		try {
			super.update("ALTER TABLE fms_prog_setup ADD vtrBudget VARCHAR(255)", null);
		} catch (Exception e){    			
		}

		try {
			super.update("ALTER TABLE fms_prog_setup ADD transportBudget VARCHAR(255)", null);
		} catch (Exception e){    			
		}

		try{
			super.update("CREATE TABLE fms_status(" +
					"requestId varchar(255) default NULL," +
					"status varchar(255) default NULL," +
					"startDate datetime default '0000-00-00 00:00:00',  " +    			
					"userId varchar(255) default NULL " +	        
					")",null);
		}catch(Exception e){}

	}

	public void addProgram(ProgramObject p)throws DaoException{
		super.update("INSERT INTO fms_prog_setup(programId, programName, producer, description,pfeCode, " +
				"startProductionDate,endProductionDate,department,status,createdBy,createdDate,updatedBy,updatedDate," +
				"engManpowerBudget, facilitiesBudget, vtrBudget, transportBudget) " +
				"VALUES(#programId#,#programName#,#producer#,#description#,#pfeCode#," +
				"#startProductionDate#,#endProductionDate#,#department#,#status#,#createdBy#,#createdDate#,#updatedBy#,#updatedDate#," +
				"#engManpowerBudget#, #facilitiesBudget#, #vtrBudget#, #transportBudget#" +
				")", p);
	}


	public void updateProgram(ProgramObject p) throws DaoException {
		String sql = "UPDATE fms_prog_setup SET programName=#programName#, description=#description#," +
		"producer=#producer#, pfeCode=#pfeCode#, startProductionDate=#startProductionDate#, endProductionDate=#endProductionDate#, department=#department#, " + 
		"status=#status#, updatedBy=#updatedBy#, updatedDate=#updatedDate#," +
		"engManpowerBudget=#engManpowerBudget#, facilitiesBudget=#facilitiesBudget#, " +
		"vtrBudget=#vtrBudget#, transportBudget=#transportBudget# " +
		"WHERE programId=#programId#";
		super.update(sql, p);
	}


	public void deleteProgram(String programId) throws DaoException {
		super.update("DELETE FROM fms_prog_setup WHERE programId=?", new String[] {programId});
	}

	public Collection selectProgram(String programId) throws DaoException {
		return super.select("SELECT * from fms_prog_setup WHERE programId=?", ProgramObject.class, new String[] {programId}, 0, -1);
	}

	public Collection selectProgram(String search, String status, String sort, boolean desc, int startIndex,int maxRows) throws DaoException{

		String strSort= "";

		if (sort != null) {
			strSort += " ORDER BY " + sort;
			if (desc)
				strSort += " DESC";
		}else 
			strSort += " ORDER BY fps.programName ASC";

		String searchByName="";
		searchByName=" AND fps.programName LIKE '%"+ ((search==null || search.equals(""))?"":search) +"%'  ";
		String searchByStatus="";
		searchByStatus=" AND fps.status LIKE '%"+ ((status==null || status.equals("-1"))?"":status) +"%'  ";     	        
		
		/*	SQL changed and used left join for department. So for ABW can manually update the department
		 * 
		 * String sql = "SELECT fps.programId,fps.programName,fps.producer,fps.producer,fps.description AS description," +
		"fps.pfeCode,fps.startProductionDate,fd.name as departmentName,fps.status AS status " +
		"FROM fms_prog_setup fps, fms_department fd " +
		"WHERE 1=1 AND fps.department=fd.id";*/	
		

		String sql = "SELECT fps.programId,fps.programName,fps.producer,fps.producer,fps.description AS description, "+
					"fps.pfeCode,fps.startProductionDate,fd.name as departmentName,fps.status AS status "+ 
					"from fms_prog_setup fps "+
					"left join fms_department fd on fps.department=fd.id " +
					"WHERE 1=1";

		return super.select(sql + searchByName + searchByStatus + strSort,HashMap.class,null,startIndex,maxRows);	             	        	
	}

	public int selectProgramCount(String search, String status) throws DaoException{
		int total=0;

		String searchByName="";
		searchByName=" AND fps.programName LIKE '%"+ ((search==null || search.equals(""))?"":search) +"%'  ";
		String searchByStatus="";
		searchByStatus=" AND fps.status LIKE '%"+ ((status==null || status.equals("-1"))?"":status) +"%'  ";     	        
		String sql = "SELECT COUNT(fps.programId) as total " +
		"FROM fms_prog_setup fps, fms_department fd " +
		"WHERE 1=1 AND fps.department=fd.id";

		Collection list=  super.select(sql + searchByName + searchByStatus,HashMap.class,null,0,1);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map object = (Map) iterator.next();
			total=Integer.parseInt(object.get("total").toString());
		}
		return total;	        	
	}

	public Collection getDept() throws DaoException {
		Collection list=new ArrayList();
		String sql="SELECT id, name, description, HOD, status FROM fms_department ";
		list=super.select(sql, HashMap.class , null, 0, -1);
		return list;
	}

	public boolean isExistPfeCode(String pfeCode) throws DaoException{
		Collection list=new ArrayList();
		boolean isExist=false;
		list=super.select("SELECT programId FROM fms_prog_setup where pfeCode like ?", HashMap.class, new String[]{pfeCode}, 0, -1);
		if(list.size()>0){
			isExist=true;
		}else{
			isExist=false;
		}
		return isExist;
	}

	public String getProgramName(String id)throws DaoException{

		String sql = "SELECT programName FROM fms_prog_setup WHERE programId = ?";		
		Collection col = super.select(sql,HashMap.class,new String[]{id},0,-1);
		if(col.size()>0){
			Map map = (Map) col.iterator().next();
			return (String) map.get("programName");		
		} else {
			return "-";
		}
	}

	public void addStatus(FMSStatus status)throws DaoException{
		super.update("INSERT INTO fms_status(requestId,status,startDate,userId) " +
				"VALUES(#requestId#,#status#,#startDate#,#userId#)", status);
	}

	public String getAssignmentIdByBookingIdUserId(String resourceId, String userId)throws DaoException{

		String sql = "SELECT M.assignmentId as assignmentId FROM fms_eng_assignment_manpower M	"+
		"INNER JOIN fms_eng_assignment A ON A.assignmentId=M.assignmentId	"+
		"INNER JOIN fms_eng_request R ON R.requestId=A.requestId  WHERE		"+
		"M.userId = ? AND R.requestId = ?";

		Collection col = super.select(sql,HashMap.class,new String[]{userId,resourceId},0,-1);
		if(col.size()>0){
			Map map = (Map) col.iterator().next();
			return (String) map.get("assignmentId");		
		} else {
			return "-";
		}
	}


	public Collection getEngineeringUserByRequestId(String requestId) throws DaoException {


		Collection list=new ArrayList();
		String sql="select DISTINCT m.userId as manpowerId from fms_eng_assignment a		" +
		"inner join fms_eng_assignment_manpower m ON 	" +
		"a.assignmentId=m.assignmentId	" +
		"where a.requestId=? ";
		list=super.select(sql, HashMap.class , new String[]{requestId}, 0, -1);
		return list;
	}

	protected Collection getDutyRosterAssignUser(String requestId, Date requiredFrom) throws DaoException {
		ArrayList args = new ArrayList();
		String sql="select DISTINCT m.userId as manpowerId from fms_eng_assignment a		" +
		"inner join fms_eng_assignment_manpower m ON 	" +
		"a.assignmentId=m.assignmentId	" +
		"where a.requestId=? AND requiredFrom = ?";
		args.add(requestId);
		args.add(requiredFrom);
		return super.select(sql, HashMap.class , args.toArray(), 0, -1);
	}
	
	public DefaultDataObject getAbwSchedulerTime(String abwSchedulerId) throws DaoException{
		String query = "SELECT id, taskId, scheduleTime1, scheduleTime2, scheduleTime3, createdBy, " +
				"createdDate, modifiedBy, modifiedDate " +
				"FROM fms_global_setup " +
				"WHERE taskId = ? ";
		
		Collection<DefaultDataObject> col = (Collection<DefaultDataObject>) 
			super.select(query, DefaultDataObject.class, new Object[]{abwSchedulerId}, 0, -1);
		
		return (col != null && col.size() == 1) ? col.iterator().next() : null;
	}
	
	public void updateAbwSchedulerTime(DefaultDataObject object) throws DaoException{
		ArrayList args = new ArrayList();
		
		String query = "SELECT taskId FROM fms_global_setup WHERE taskId = ?";
		Collection temp = super.select(query, HashMap.class, new Object[]{object.getProperty("taskId")}, 0, -1);
		if(temp != null && temp.size() == 1){
			query = "UPDATE fms_global_setup SET scheduleTime1 = ?, scheduleTime2 = ?, scheduleTime3 = ?, " +
					"modifiedBy = ?, modifiedDate = ? " +
					"WHERE taskId = ? ";
			args.add(object.getProperty("scheduleTime1"));
			args.add(object.getProperty("scheduleTime2"));
			args.add(object.getProperty("scheduleTime3"));
			args.add(object.getProperty("modifiedBy"));
			args.add(object.getProperty("modifiedDate"));
			args.add(object.getProperty("taskId"));
			
			super.update(query, args.toArray());
		}
		else{
			query = "INSERT INTO fms_global_setup (id, taskId, scheduleTime1, scheduleTime2, scheduleTime3, " +
					"createdBy, createdDate, modifiedBy, modifiedDate) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			args.add(object.getId());
			args.add(object.getProperty("taskId"));
			args.add(object.getProperty("scheduleTime1"));
			args.add(object.getProperty("scheduleTime2"));
			args.add(object.getProperty("scheduleTime3"));
			args.add(object.getProperty("createdBy"));
			args.add(object.getProperty("createdDate"));
			args.add(object.getProperty("modifiedBy"));
			args.add(object.getProperty("modifiedDate"));
			
			super.update(query, args.toArray());
		}
	}

}
