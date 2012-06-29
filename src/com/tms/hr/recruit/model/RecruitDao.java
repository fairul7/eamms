package com.tms.hr.recruit.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.JdbcUtil;

public class RecruitDao extends DataSourceDao{    
	public void init() throws DaoException{
		try{
			super.update("CREATE TABLE rec_vacancy_template (\n" +
					"vacancyTempCode varchar(255) NOT NULL default '0',\n" +
					"positionId varchar(40) NOT NULL default '0',\n" +
					"countryId varchar(40) NOT NULL default '0',\n " +
					"departmentId varchar(40) NOT NULL default '0',\n" +
					"responsibilities text ,\n" +
					"requirements text ,\n" +
					"createdBy varchar(250) , \n" +
					"createdDate datetime ,\n" +
					"lastUpdatedBy varchar(250) ,\n" +
					"lastUpdatedDate datetime ,\n " +
					"PRIMARY KEY (vacancyTempCode) ,\n" +
					"UNIQUE KEY vacancyTempCode (vacancyTempCode)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_message_body (\n" +
					" messageBody text  \n)", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update(" ALTER TABLE rec_message_body ADD employeeOppor TEXT ", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_vacancy_detail (\n" +
					"vacancyCode varchar(255) NOT NULL default '0',\n" +
					"positionId varchar(40) NOT NULL default '0', \n" +
					"countryId varchar(40) NOT NULL default '0', \n" +
					"departmentId varchar(40) NOT NULL default '0', \n" +
					"noOfPosition int(2) ,\n" +
					"noOfPositionOffered int(2) ,\n" +
					"priority char(1) ,\n" +
					"responsibilities text ,\n" +
					"requirements text ,\n" +
					"startDate date ,\n" +
					"endDate date ,\n" +
					"createdBy varchar(250) ,\n" +
					"createdDate datetime ,\n" +
					"lastUpdatedBy varchar(250) ,\n" +
					"lastUpdatedDate datetime ,\n " +
					"PRIMARY KEY (vacancyCode) ,\n" +
					"UNIQUE KEY vacancyCode (vacancyCode)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_vacancy_total (\n" +
					"vacancyCode varchar(255) NOT NULL default '0' ,\n" +
					"totalApplied int(10) ,\n" +
					"totalShortlisted int(10) ,\n" +
					"totalScheduled int(10) ,\n" +
					"totalReScheduled int(10) ,\n" +
					"totalReScheduledRejected int(10) ,\n" +
					"totalAnotherInterview int(10) ,\n" +
					"totalJobOffered int(10) ,\n" +
					"totalInterviewUnsuccessful int(10) , \n" +
					"totalJobAccepted int(10) ,\n" +
					"totalJobRejected int(10) ,\n" +
					"totalBlackListed int(10) ,\n" +
					"totalViewed int(10) ,\n" +
					"UNIQUE KEY vacancyCode (vacancyCode)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_vacancy_audit (\n" +
					"auditId varchar(255) NOT NULL default '0' ,\n" +
					"vacancyCode varchar(255) ,\n" +
					"userId varchar(250) ,\n" +
					"applicantId varchar(250) ,\n" +
					"name varchar(255) ,\n" +
					"actionTaken varchar(255) ,\n" +
					"auditDate dateTime,\n" +
					"PRIMARY KEY (auditId) \n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_vacancy_hit (\n" +
					"vacancyCode varchar(255) ,\n" +
					"ipAddr varchar(250) \n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
	}
	
	//insert audit
	public void insertAudit(VacancyObj obj) throws DaoException{
    	super.update("INSERT INTO rec_vacancy_audit (auditId, vacancyCode, userId, applicantId, name, " +
    			" actionTaken, auditDate)" +
    			" VALUES(#auditId#, #vacancyCode#, #userId#, #applicantId#, #name#, " +
    			" #actionTaken#, #auditDate#)", obj);
    }
	
	//	find all audit
    public Collection findAllAudit(String sort, boolean desc, int start, int rows,  String auditMisc, String startDate, String endDate) throws DaoException{
    	
    	String sqlCause = findAllAuditSqlCause(auditMisc, startDate, endDate);
    	
    	/*Collection col= super.select("SELECT a.userId, d.username, a.vacancyCode, a.applicantId, b.name, a.actionTaken, a.auditDate " +
    			" FROM rec_vacancy_audit a, rec_applicant_detail b , rec_vacancy_detail c, security_user d " +
    			" WHERE a.userId=d.id AND a.applicantId=b.applicantId AND a.vacancyCode=c.vacancyCode " 
    			+ sqlCause +" ORDER BY a.auditDate desc " + JdbcUtil.getSort(sort, desc), VacancyObj.class, null, start, rows);*/ 	
    	
    	/*Collection col= super.select("SELECT a.userId,  a.vacancyCode, a.applicantId, b.name, a.actionTaken, a.auditDate " +
    			" FROM rec_vacancy_audit a, rec_applicant_detail b , rec_vacancy_detail c " +
    			" WHERE  a.applicantId=b.applicantId AND a.vacancyCode=c.vacancyCode " 
    			+ sqlCause +" ORDER BY a.auditDate desc " + JdbcUtil.getSort(sort, desc), VacancyObj.class, null, start, rows);*/
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		if("username".equals(sort)){
    			sort="a.userId";
    		}
    		
    	}else{
    		sqlSort=" ORDER BY a.auditDate desc ";
    	}
    	
    	Collection col= super.select("SELECT a.auditId, a.userId,  a.vacancyCode, a.applicantId, a.name, a.actionTaken, a.auditDate " +
    			" FROM rec_vacancy_audit a " +
    			" WHERE a.auditId!=' ' " 
    			+ sqlCause + sqlSort + JdbcUtil.getSort(sort, desc), VacancyObj.class, null, start, rows);
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		VacancyObj vObj = (VacancyObj)ite.next();
    		String getUserId ="";
    		
    		if(!vObj.getUserId().equals("-")){
    			getUserId=vObj.getUserId();
    			Collection colId = super.select("SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) as username FROM security_user WHERE id='"+getUserId+"' ", VacancyObj.class, null, 0, 1);
    			VacancyObj userObj = (VacancyObj)colId.iterator().next();
        		vObj.setUsername(userObj.getUsername());
    		}else
    			vObj.setUsername("-");
    		
    		/*if(!vObj.getApplicantId().equals("-")){
    			Collection applicantIdCol = super.select("SELECT name FROM rec_applicant_detail WHERE applicantId='"+vObj.getApplicantId()+"' ", 
    					VacancyObj.class, null, 0, 1);
    			VacancyObj applicantObj;
    			if(applicantIdCol.size() > 0){
    				applicantObj = (VacancyObj)applicantIdCol.iterator().next();
    				vObj.setName(applicantObj.getName());
    			}else{
    				vObj.setName(vObj.getApplicantId()); //applicantID is applicant name
    			}	
    			
    		}else
    			vObj.setName("-");*/
    		
    	}
    	
    	return col;
    }
	
    //  count total audit
    public int countAllAudit(String auditMisc, String startDate, String endDate) throws DaoException{
    	String sqlCause = findAllAuditSqlCause(auditMisc, startDate, endDate);

    	/*Collection col = super.select("SELECT COUNT(*) AS total " +
    			" FROM rec_vacancy_audit a, rec_applicant_detail b , rec_vacancy_detail c, security_user d " +
    			" WHERE a.userId=d.id AND a.applicantId=b.applicantId AND a.vacancyCode=c.vacancyCode " + sqlCause , HashMap.class, null, 0, 1);*/
    	
    	/*Collection col= super.select("SELECT COUNT(*) AS total " +
    			" FROM rec_vacancy_audit a, rec_applicant_detail b , rec_vacancy_detail c " +
    			" WHERE  a.applicantId=b.applicantId AND a.vacancyCode=c.vacancyCode " 
    			+ sqlCause , HashMap.class, null, 0, 1);*/
    	
    	Collection col= super.select("SELECT COUNT(*) AS total " +
    			" FROM rec_vacancy_audit a " +
    			" WHERE a.auditId!=' ' " +
    			sqlCause , HashMap.class, null, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //reverse getting the user ID-from username to userID
    public String getUSERID(String searchUserName, String TABLE_TYPE)  throws DaoException{
    	StringBuffer sb = new StringBuffer();
    	sb.append("(");
    	Collection col=new ArrayList();
    	if(TABLE_TYPE.equals("security_user"))
    		col = super.select("SELECT id FROM "+ TABLE_TYPE +" WHERE (firstName LIKE '%"+ searchUserName +"%' OR lastName LIKE '%"+searchUserName+"%' ) ", HashMap.class, null, 0, -1);
    		//col = super.select("SELECT id FROM "+ TABLE_TYPE +" WHERE username LIKE '%"+ searchUserName +"%' ", HashMap.class, null, 0, -1);
    	/*else if(TABLE_TYPE.equals("rec_applicant_detail"))
    		col = super.select("SELECT applicantId as id FROM "+TABLE_TYPE+" WHERE name LIKE '%"+ searchUserName +"%' ", HashMap.class, null, 0, -1);*/

    	
    	if(col.size() > 0){
	    	int i=0;
	    	for(Iterator ite = col.iterator(); ite.hasNext();){
	    		HashMap map = (HashMap)ite.next();
	    		
	    		if(i>0 && i<col.size())
	    			sb.append(", ");
	    		
	    		sb.append("'"+map.get("id").toString()+"'");
	    	i++;	
	    	}
    	}else
    		sb.append("' '");
    	
    	sb.append(")");
    	return sb.toString();
    }
    
    /*//reverse getting the applicant ID - from name to applicantID
    public String getAPPLICANTID(String searchName) throws DaoException{
    	StringBuffer sbf = new StringBuffer();
    	sb.
    	
    	return sbf.toString();
    }*/
    
    
    //  sql cause-audit
    public String findAllAuditSqlCause(String auditMisc, String startDate, String endDate) throws DaoException{
    	String sqlCause="";
    	
    	if(auditMisc!=null && !auditMisc.equals("")){
    		if(!auditMisc.equals("-")){
    			String strUserId = getUSERID(auditMisc, "security_user");
    			//String strApplicantId = getUSERID(auditMisc, "rec_applicant_detail");
    			/*sqlCause +=" AND (a.userId IN "+strUserId+" OR a.vacancyCode LIKE '%" + auditMisc + "%' OR " +
    				" b.name LIKE '%" + auditMisc + "%' OR a.actionTaken LIKE '%" + auditMisc + "%')";*/
    			sqlCause +=" AND (a.userId IN "+strUserId+" OR a.vacancyCode LIKE '%" + auditMisc + "%' OR " +
				" a.name LIKE '%"+auditMisc+"%' OR a.actionTaken LIKE '%" + auditMisc + "%')";
    		}else{
    			sqlCause +=" AND a.userId='"+auditMisc+"'";
				
    		}
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND a.auditDate BETWEEN " + "'" + startDate + " 00:00:00" + "'" + " AND '" +  endDate + " 23:59:00 '";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		//sqlCause +=" AND a.auditDate>='"+ startDate + " 00:00:00"+"' AND  a.auditDate<='"+ startDate + " 23:59:00 '";
    		sqlCause +=" AND a.auditDate BETWEEN '"+ startDate + " 00:00:00"+"' AND '"+ startDate + " 23:59:00 '";
    	}
    
    	return sqlCause;
    }
    
    //delete audit trail
    public void deleteAudit(String auditId) throws DaoException{
    	super.update("DELETE FROM rec_vacancy_audit WHERE auditId=? ", new Object[]{auditId});
    }
    
    //  insert vacancy hit
	public void insertVacancyHit(VacancyObj obj) throws DaoException{
    	super.update("INSERT INTO rec_vacancy_hit (vacancyCode, ipAddr)" +
    			" VALUES(#vacancyCode#, #ipAddr#)", obj);
    }
    
	//validate who is the HOD
    public boolean validateHod(String userId) throws DaoException {
    	   boolean hod = false;
    	   Collection col = super.select("SELECT hod FROM org_chart_hierachy WHERE userId=?", HashMap.class, new Object[]{userId}, 0, -1);
           
           if(col.size() > 0){
               for(Iterator comItr = col.iterator(); comItr.hasNext();){
                   HashMap map = (HashMap) comItr.next();
                   if(map.get("hod").toString().equals("1"))
                	   hod=true;
               }
           } 
           return hod;
    }
        
    //make sure vacancy template code is unique
    //codeExist(field value, database type)
    public boolean codeExist(String code, String type) throws DaoException{
    	String table="";
    	
    	if(type.equals("vacancyCode")){
    		table="rec_vacancy_detail";
    	}else if(type.equals("vacancyTempCode")){
    		table="rec_vacancy_template";
    	}
    	
    	Collection col = super.select("SELECT "+ type +" FROM  " + table +
    			" WHERE "+ type +"=? ", HashMap.class, new Object[]{code}, 0, -1);
    	
    	if(col.size() > 0){
    		return true;
    	}else{
    		return false;
    	}	
    	
    }
    
    //insert Vacancy Template
    public void insertVacancyTemp(VacancyObj obj) throws DaoException{
    	super.update("INSERT INTO rec_vacancy_template (vacancyTempCode, positionId, countryId, departmentId, " +
    			"responsibilities, requirements, createdBy, createdDate, lastUpdatedBy, lastUpdatedDate)" +
    			" VALUES(#vacancyTempCode#, #positionId#, #countryId#, #departmentId#, " +
    			"#responsibilities#, #requirements#, #createdBy#, #createdDate#, #lastUpdatedBy#, #lastUpdatedDate#)", obj);
    }
    
    
    //update Vacancy Template
    public void updateVacancyTemp(VacancyObj obj) throws DaoException{
    	super.update("UPDATE rec_vacancy_template " +
    			"SET positionId=#positionId#, " +
    			"countryId=#countryId#, " +
    			"departmentId=#departmentId#, " +
    			"responsibilities=#responsibilities#, " +
    			"requirements=#requirements#, " +
    			"lastUpdatedBy=#lastUpdatedBy#, " +
    			"lastUpdatedDate=#lastUpdatedDate# " +
    			"WHERE vacancyTempCode=#vacancyTempCode# ", obj);
    }
    
    //update Vacancy
    public void updateVacancy(VacancyObj obj) throws DaoException{
    	super.update("UPDATE rec_vacancy_detail " +
    			"SET positionId=#positionId#, " +
    			"countryId=#countryId#, " +
    			"departmentId=#departmentId#, " +
    			"noOfPosition=#noOfPosition#, " +
    			"priority=#priority#, " +
    			"responsibilities=#responsibilities#, " +
    			"requirements=#requirements#, " +
    			"startDate=#startDate#, " +
    			"endDate=#endDate#, " +
    			"lastUpdatedBy=#lastUpdatedBy#, " +
    			"lastUpdatedDate=#lastUpdatedDate# " +
    			"WHERE vacancyCode=#vacancyCode# ", obj);
    }
    
    //  update Vacancy noOfPositionOffered
    public void updateVacancyPositionOffered(VacancyObj obj) throws DaoException{
    	super.update("UPDATE rec_vacancy_detail " +
    			"SET noOfPositionOffered=#noOfPositionOffered# " +
    			"WHERE vacancyCode=#vacancyCode# ", obj);
    }
    
    //insert Vacancy
    public void insertVacancy(VacancyObj obj) throws DaoException{
    	super.update("INSERT INTO rec_vacancy_detail (vacancyCode,positionId, countryId, departmentId, " +
    			"noOfPosition, priority,responsibilities, requirements, startDate, endDate," +
    			"createdBy, createdDate, lastUpdatedBy, lastUpdatedDate) " +
    			" VALUES (#vacancyCode#,#positionId#, #countryId#, #departmentId#, " +
    			"#noOfPosition#, #priority#, #responsibilities#, #requirements#, #startDate#, #endDate#, " +
    			"#createdBy#, #createdDate#, #lastUpdatedBy#, #lastUpdatedDate#)", obj);
    	
    	super.update("INSERT INTO rec_vacancy_total (vacancyCode, totalApplied, totalShortlisted, totalScheduled, totalReScheduled, totalReScheduledRejected, " +
    			" totalAnotherInterview, totalJobOffered, " +
    			" totalInterviewUnsuccessful, totalJobAccepted, totalJobRejected, totalBlackListed, totalViewed) " +
    			" VALUES (#vacancyCode#, #totalApplied#, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 )", obj);

    }
    
    //find all vacancy template code
    public Collection findAllVacancyTempCodeFilter(String vacancyTempCode, String sort, boolean desc, int start, int rows, 
    		String countryCode, String deptCode, String titleCode) throws DaoException{
    	
    	String sqlCause = findAllVacancyTempSqlCause(vacancyTempCode, countryCode, deptCode, titleCode);
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		//nothing here
    	}else{
    		sqlSort=" ORDER BY a.lastUpdatedDate desc ";
    	}
    	
    	return super.select("SELECT a.vacancyTempCode, a.positionId, a.countryId, a.departmentId, a.createdDate, " +
    			" b.code, b.shortDesc as countryDesc, c.code, c.shortDesc as departmentDesc, " +
    			" d.code, d.shortDesc as positionDesc " +
    			" FROM rec_vacancy_template a , org_chart_country b, org_chart_department c, org_chart_title d " +
    			"WHERE a.countryId=b.code AND a.departmentId=c.code AND a.positionId=d.code " 
    			+ sqlCause + sqlSort + JdbcUtil.getSort(sort, desc), VacancyObj.class, null, start, rows); 	
    }
    
    //find all vacancy code
    public Collection findAllVacancyCodeFilter(String vacancyCode, String sort, boolean desc, int start, int rows, 
    		String priority, String startDate, String endDate) throws DaoException{
    	
    	String sqlCause = findAllVacancySqlCause(vacancyCode, priority, startDate, endDate);
    	
    	/*Collection col = super.select("SELECT a.vacancyCode as vacancyCodeName, a.positionId, a.noOfPosition, a.noOfPositionOffered, a.createdBy, " +
    			"a.priority as priority, b.code, b.shortDesc as positionDesc, " +
    			"c.vacancyCode, c.totalApplied as totalApplicant " +
    			"FROM rec_vacancy_detail a, org_chart_title b, rec_vacancy_total c " +
    			"WHERE a.positionId=b.code AND a.vacancyCode=c.vacancyCode " +
    			sqlCause + " ORDER BY a.lastUpdatedDate desc " + JdbcUtil.getSort(sort, desc), VacancyObj.class, null, start, rows);*/
    	
    	if(sort!=null){
    		if("priorityName".equals(sort))
    			sort="a.priority";
    		if("totalApplicant".equals(sort))
    			sort="c.totalApplied";
    		if("noOfPositionDetail".equals(sort)){
    			sort="a.noOfPositionOffered";
    		}
    	}
    		
    	Collection col = super.select("SELECT a.vacancyCode as vacancyCodeName, a.positionId, a.noOfPosition, a.noOfPositionOffered, a.createdBy, " +
		"a.priority as priority, a.startDate, a.noOfPositionOffered, b.code, b.shortDesc as positionDesc, " +
		"c.vacancyCode, c.totalApplied as totalApplicant, " +
		"(SELECT count(*) FROM rec_applicant_status WHERE a.vacancyCode=vacancyCode) as totalApplicantNo " +
		"FROM rec_vacancy_detail a, org_chart_title b, rec_vacancy_total c " +
		"WHERE a.positionId=b.code AND a.vacancyCode=c.vacancyCode " +
		sqlCause + JdbcUtil.getSort(sort, desc), VacancyObj.class, null, start, rows);
    	
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		VacancyObj vacancyObj = (VacancyObj) ite.next();
    		if(vacancyObj.isPriority()){
    			vacancyObj.setPriorityName("Urgent");
    		}else{
    			vacancyObj.setPriorityName("Normal");
    		}
    		
    		if(String.valueOf(vacancyObj.getNoOfPositionOffered())!=null)
    			vacancyObj.setNoOfPositionDetail(vacancyObj.getNoOfPositionOffered() +"/"+vacancyObj.getNoOfPosition());
    		else
    			vacancyObj.setNoOfPositionDetail("0/"+vacancyObj.getNoOfPosition());
    	}
    	
    	return col;
    }
    
    //list some vacancy details
    public Collection listAllVacancy() throws Exception{
    	Collection col = super.select("SELECT a.vacancyCode as vacancyCodeName, a.positionId, a.noOfPosition, a.noOfPositionOffered, a.priority as priority, b.code, b.shortDesc as positionDesc, " +
    			"c.vacancyCode, c.totalApplied as totalApplicant, c.totalViewed, a.startDate, a.endDate " +
    			"FROM rec_vacancy_detail a, org_chart_title b, rec_vacancy_total c " +
    			"WHERE a.positionId=b.code AND a.vacancyCode=c.vacancyCode " +
    			" ORDER BY a.lastUpdatedDate desc ", VacancyObj.class, null, 0, -1);
    	
    	Date getDate = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String nowDate = sdf.format(getDate);
    	
    
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		VacancyObj vacancyObj = (VacancyObj) ite.next();
    		 if(sdf.parse(nowDate).equals(vacancyObj.getEndDate()) || vacancyObj.getEndDate().after(sdf.parse(nowDate))){ //checking end date
    			if(sdf.parse(nowDate).equals(vacancyObj.getStartDate()) || vacancyObj.getStartDate().before(sdf.parse(nowDate))){	 //checking start date
		    		if(vacancyObj.isPriority()){
		    			vacancyObj.setPriorityName("Urgent");
		    		}else{
		    			vacancyObj.setPriorityName("Normal");
		    		}
		    	}else{
		    		ite.remove();
		    	}
    		 }else{
    				ite.remove();
    		}
    	}
    	
    	return col;
    }
   
    //find all position
    public Collection lookUpPosition(String vacancyCode) throws Exception{
    	String sqlQ="";
    	if(vacancyCode!=null && !vacancyCode.equals("")){
    		sqlQ=" SELECT vacancyCode, positionId, countryId, departmentId, startDate, endDate " +
    				 " FROM rec_vacancy_detail " +	
    				 " WHERE vacancyCode <> '" + vacancyCode + "' "+
    				 " ORDER BY positionId desc, endDate";
    	}else{
    		sqlQ=" SELECT vacancyCode, positionId, countryId, departmentId, startDate, endDate " +
			 " FROM rec_vacancy_detail " +	
			 " ORDER BY positionId desc, endDate";
    	}
    	
    	Collection lookUpPosCol=super.select(sqlQ, HashMap.class, null, 0, -1);

    	return lookUpPosCol;
    }
    
    
    //count total vacancy template
    public int countAllVacancyTemp(String vacancyTempCode, String countryCode, String deptCode, String titleCode) throws DaoException{
    	String sqlCause = findAllVacancyTempSqlCause(vacancyTempCode, countryCode, deptCode, titleCode);

    	Collection col = super.select("SELECT COUNT(*) AS total " +
    			"FROM rec_vacancy_template a , org_chart_country b, org_chart_department c, org_chart_title d " +
    			"WHERE a.countryId=b.code AND a.departmentId=c.code AND a.positionId=d.code " + sqlCause , HashMap.class, null, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //count total vacancy
    public int countAllVacancy(String vacancyCode, String priority, String startDate, String endDate) throws DaoException{
    	String sqlCause = findAllVacancySqlCause(vacancyCode, priority, startDate, endDate);
    	
    	Collection col = super.select("SELECT COUNT(*) AS total " +
    			"FROM rec_vacancy_detail a, org_chart_title b, rec_vacancy_total c " +
    			"WHERE a.positionId=b.code AND a.vacancyCode=c.vacancyCode " + sqlCause, HashMap.class, null, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //find vacancy template SQL cause
    public String findAllVacancyTempSqlCause(String vacancyTempCode, String countryCode, String deptCode, String titleCode){
		String sqlCause="";
	    
    	if(vacancyTempCode!=null && !vacancyTempCode.equals("")){
    		sqlCause +=" AND a.vacancyTempCode LIKE '%" + vacancyTempCode + "%'";
    	}
    	else{
    		sqlCause +=" AND a.vacancyTempCode LIKE '%' ";
    	}
    	
    	if(deptCode!=null && !deptCode.equals("")){
    		sqlCause +=" AND c.code='" + deptCode + "'";
         }
    	
    	if(countryCode!=null && !countryCode.equals("")){
    		sqlCause +=" AND b.code='"+ countryCode+ "'";
    	}
    		
    	if(titleCode!=null && !titleCode.equals("")){
    		sqlCause +=" AND d.code='"+ titleCode+ "'";	
    	}
		
		return sqlCause;
	}
    
    //find vacancy SQL cause
    public String findAllVacancySqlCause(String vacancyCode, String priority, String startDate, String endDate){
    	String sqlCause="";
    	if(vacancyCode!=null && !vacancyCode.equals("")){
    		sqlCause +=" AND (a.vacancyCode LIKE '%" + vacancyCode + "%' OR b.shortDesc LIKE '%" + vacancyCode + "%' ) ";
    	}
    	else 
    		sqlCause +=" AND a.vacancyCode LIKE '%' ";
    	
    	if(priority!=null && !priority.equals("")){
    		sqlCause +=" AND a.priority='"+ priority + "' ";
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND a.startDate BETWEEN " + "'" + startDate + "'" + " AND " + "'" +  endDate + "' ";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND a.startDate='"+ startDate +"' ";
    	}
    
    	return sqlCause;
    }
    
    
    //delete vacancy template
    public void deleteVacancyTemp(String vacancyTempCode) throws DaoException{
    	super.update("DELETE FROM rec_vacancy_template WHERE vacancyTempCode=? ", new Object[]{vacancyTempCode});
    }
  
    //delete vacancy 
    public void deleteVacancyAndApplicant(String vacancyCode) throws DaoException{
    	super.update("DELETE FROM rec_vacancy_detail WHERE vacancyCode=? ", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_vacancy_total WHERE vacancyCode=? ", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_vacancy_hit WHERE vacancyCode=? ", new Object[]{vacancyCode});
    	
    	//also to delete applicant
    	super.update("DELETE FROM rec_applicant_detail WHERE applicantId IN (SELECT applicantId FROM rec_applicant_status WHERE vacancyCode=?)", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_applicant_education WHERE applicantId IN (SELECT applicantId FROM rec_applicant_status WHERE vacancyCode=?)", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_applicant_employment WHERE applicantId IN (SELECT applicantId FROM rec_applicant_status WHERE vacancyCode=?)", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_applicant_language WHERE applicantId IN (SELECT applicantId FROM rec_applicant_status WHERE vacancyCode=?)", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_applicant_skill WHERE applicantId IN (SELECT applicantId FROM rec_applicant_status WHERE vacancyCode=?)", new Object[]{vacancyCode});
    	
    	super.update("DELETE FROM rec_interview_date WHERE applicantId IN (SELECT applicantId FROM rec_applicant_status WHERE vacancyCode=?)", new Object[]{vacancyCode});
    	super.update("DELETE FROM rec_interviewer_remark WHERE vacancyCode=? ", new Object[]{vacancyCode});
    	
    	super.update("DELETE FROM rec_applicant_status WHERE vacancyCode=? ", new Object[]{vacancyCode});
    }
    
	//get the particular vacancy template details
    public VacancyObj loadVacancyTemp(String vacancyTempCode) throws DaoException, DataObjectNotFoundException{
    	Object[] args = {vacancyTempCode};
    	Collection col = super.select("SELECT vacancyTempCode, positionId, countryId, departmentId, responsibilities, requirements " +
    			" FROM rec_vacancy_template " +
    			" WHERE vacancyTempCode=?", VacancyObj.class, args, 0, 1);
    	
    	if(col.size()==0){
    		throw new DataObjectNotFoundException();
    	}else{
    		return (VacancyObj) col.iterator().next();
    	}
    }
    
    //get the particular vacancy details
    public VacancyObj loadVacancy(String vacancyCode, String TYPE_USAGE) throws DaoException, DataObjectNotFoundException{
    	Object[] args = {vacancyCode};
    	Collection col=null;
    	if(TYPE_USAGE.equals("small")){	
    		col = super.select("SELECT vacancyCode, positionId, countryId, departmentId, noOfPosition, noOfPositionOffered, " +
    			"priority, responsibilities, requirements, startDate, endDate, createdBy, createdDate, lastUpdatedBy, lastUpdatedDate " +
    			"FROM rec_vacancy_detail WHERE vacancyCode=? ", VacancyObj.class, args, 0, 1); 
    	}else if(TYPE_USAGE.equals("big")){	    		
	    	col = super.select(" SELECT a.vacancyCode, a.noOfPosition, a.noOfPositionOffered, " +
	    		" a.priority, a.responsibilities, a.requirements, a.startDate, a.endDate, a.createdDate, " +
	    		" (SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) FROM security_user "+
	    		" WHERE a.createdBy=id) as createdBy, a.createdBy as hodId, " +
	    		" (SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) FROM security_user "+
	    		" WHERE a.lastUpdatedBy=id) as lastUpdatedBy, "+
	    		" a.lastUpdatedDate, b.shortDesc as positionId, c.shortDesc as departmentId, d.shortDesc as countryId, " +
	    		" e.totalApplied as totalApplied "+
	    		" FROM rec_vacancy_detail a, org_chart_title b, org_chart_department c, org_chart_country d, " +
	    		" rec_vacancy_total e "+
	    		" WHERE a.positionId=b.code AND a.departmentId=c.code AND a.countryId=d.code AND "+
	    		" a.vacancyCode=e.vacancyCode AND a.vacancyCode=? ", VacancyObj.class, args, 0, 1);
	    	
	    	for(Iterator ite = col.iterator(); ite.hasNext();){
	    		VacancyObj vacancyObj = (VacancyObj) ite.next();
	    		if(vacancyObj.isPriority()){
	    			vacancyObj.setPriorityName("Urgent");
	    		}else{
	    			vacancyObj.setPriorityName("Normal");
	    		}
	    		
	    		
	    	}
	    	
    	}
    	
    	if(col.size()==0){
    		throw new DataObjectNotFoundException();
    	}else{
    		return (VacancyObj) col.iterator().next();
    	}
    }
    
    //get the particular Message body details
    public VacancyObj loadMessagebody() throws DaoException{
    	Collection col = super.select("SELECT messageBody, employeeOppor " +
    			" FROM rec_message_body ", VacancyObj.class, null, 0, 1);
    	
    	if(col.iterator().hasNext())
    		return (VacancyObj) col.iterator().next();
    	else
    		return new VacancyObj();
    }
    
    //insert Setup message body
    public void insertSetupMessageBody(VacancyObj smbObj) throws DaoException{
    	super.update("INSERT INTO rec_message_body (messageBody,employeeOppor)" +
    			" VALUES(#messageBody#,#employeeOppor#)", smbObj);
    }
    
    //update Setup message body
    public void updateSetupMessageBody(VacancyObj smbObj) throws DaoException{
    	super.update("UPDATE rec_message_body " +
    			"SET messageBody=#messageBody# , employeeOppor=#employeeOppor# ", smbObj);
    }
    
    //get all vacancyTempCode value for selectbox
    public Collection findALLVacancyTempCode(String shortDesc, int start, int rows, String sort, boolean desc) throws DaoException{
    	return super.select("SELECT vacancyTempCode FROM rec_vacancy_template " + JdbcUtil.getSort(sort, desc), 
    			VacancyObj.class, null, start, rows);
    }
    
    //  get the particular vacancy detail
    public Collection findSpecificVacancy(String vacancyCode) throws DaoException {
    	Object[] args = {vacancyCode};
    	Collection col = super.select("SELECT vacancyCode, positionId, countryId, departmentId, noOfPosition, noOfPositionOffered , createdBy" +
    			" FROM rec_vacancy_detail " +
    			" WHERE vacancyCode=?", HashMap.class, args, 0, 1);
    	
    	return col;
    }
    
    //  update particular vacancy detail
    public void updateSpecificVacancy(VacancyObj obj) throws DaoException{
    	super.update("UPDATE rec_vacancy_detail SET noOfPositionOffered=#noOfPositionOffered# " +
    			"WHERE vacancyCode=#vacancyCode# " , obj);
    }
    
    //  lookup vacancy total report
    public Collection findAllVacancyTotal(String sort, boolean desc, int start, int rows, String strPosition, String strCountry, String strDepartment, 
			String vacancyMisc, String startDate, String endDate, String vacancyCode) throws DaoException{
    	String sqlCause = findAllVacancyTotalSqlCause(strPosition, strCountry, strDepartment, vacancyMisc, startDate, endDate, vacancyCode);
    	
    	String sqlSort="";
    	if(sort!=null){
    		
    	}else{
    		sqlSort=" ORDER BY a.lastUpdatedDate desc ";
    	}
    		
    	Collection col = super.select(" SELECT a.vacancyCode, a.startDate, a.endDate, a.priority, a.startDate, a.endDate, a.createdDate, a.lastUpdatedDate, " +
    	" a.responsibilities, a.requirements, a.noOfPosition, a.noOfPositionOffered, " +
    	" (SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) FROM security_user "+
	    " WHERE a.createdBy=id) as createdBy, " +
	    " (SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) FROM security_user "+
	    " WHERE a.lastUpdatedBy=id) as lastUpdatedBy, "+
    	" b.code, b.shortDesc as positionDesc, c.code, c.shortDesc as countryDesc, d.code, d.shortDesc as departmentDesc, "+
    	" e.totalApplied, e.totalShortlisted, e.totalScheduled, e.totalReScheduled, e.totalReScheduledRejected, e.totalAnotherInterview, "+
    	" e.totalJobOffered, e.totalInterviewUnsuccessful, e.totalJobAccepted, e.totalJobRejected, "+
    	" e.totalBlackListed, e.totalViewed "+
    	" FROM rec_vacancy_detail a, org_chart_title b, org_chart_country c, org_chart_department d, "+
    	" rec_vacancy_total e "+
    	" WHERE a.positionId=b.code AND a.countryId=c.code AND a.departmentId=d.code AND a.vacancyCode=e.vacancyCode"+ sqlCause +
    	sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		HashMap map = (HashMap) ite.next();
    		if(map.get("priority").equals("1")){
    			map.put("priorityName", "Urgent");
    		}else{
    			map.put("priorityName", "Normal");
    		}
    	}
    	
    	return col;
    }
    
    //  sql cause for vacancy total report
    public String findAllVacancyTotalSqlCause(String strPosition, String strCountry, String strDepartment, String vacancyMisc, String startDate, String endDate, String vacancyCode){
    	String sqlCause="";
    	
    	if(vacancyCode!=null && !vacancyCode.equals("")){
    		sqlCause +=" AND a.vacancyCode='" + vacancyCode + "'";
    	}
    		
    	if(strDepartment!=null && !strDepartment.equals("")){
    		sqlCause +=" AND d.code='" + strDepartment + "'";
         }
    	
    	if(strCountry!=null && !strCountry.equals("")){
    		sqlCause +=" AND c.code='"+ strCountry+ "'";
    	}
    		
    	if(strPosition!=null && !strPosition.equals("")){
    		sqlCause +=" AND b.code='"+ strPosition+ "'";	
    	}
    	
    	if(vacancyMisc!=null && !vacancyMisc.equals("")){
    		sqlCause +=" AND (a.vacancyCode LIKE '%" + vacancyMisc + "%' OR e.totalApplied LIKE '%" + vacancyMisc + "%' " +
    				" OR e.totalBlackListed LIKE '%" + vacancyMisc + "%' OR e.totalJobAccepted LIKE '%" + vacancyMisc + "%' " +
    				" OR e.totalJobRejected LIKE '%" + vacancyMisc + "%')";
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND a.startDate BETWEEN " + "'" + startDate + "'" + " AND " + "'" +  endDate + "'"; 
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND a.startDate='"+ startDate +"'";
    	}
    
    	return sqlCause;
    }
   
    //  count vacancy total report
    public int countAllVacancyTotal(String strPosition, String strCountry, String strDepartment, String vacancyMisc, String startDate, String endDate, String vacancyCode) throws DaoException{
    	String sqlCause = findAllVacancyTotalSqlCause(strPosition, strCountry, strDepartment, vacancyMisc, startDate, endDate, vacancyCode);
    	
    	Collection col = super.select("SELECT COUNT(*) AS total "+
    	    	" FROM rec_vacancy_detail a, org_chart_title b, org_chart_country c, org_chart_department d, "+
    	    	" rec_vacancy_total e "+
    	    	" WHERE a.positionId=b.code AND a.countryId=c.code AND a.departmentId=d.code AND a.vacancyCode=e.vacancyCode "+ sqlCause, HashMap.class, null, 0, 1);
  
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
}































