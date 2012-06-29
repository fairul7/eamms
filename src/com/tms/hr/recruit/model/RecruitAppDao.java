package com.tms.hr.recruit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.JdbcUtil;
import kacang.util.UuidGenerator;

public class RecruitAppDao extends DataSourceDao{
	public void init() throws DaoException{
		try{
			super.update("CREATE TABLE rec_applicant_detail (\n" +
					"applicantId varchar(250) NOT NULL default '0',\n" +
					"name varchar(255) ,\n" +
					"age varchar(2) ,\n " +
					"dob date ,\n" +
					"nirc varchar(35) NOT NULL default '0',\n" +
					"gender char(1) ,\n" +
					"maritalStatus varchar(10) ,\n" +
					"noOfChild int(2) ,\n" +
					"nationality varchar(5) ,\n" +
					"permanentResident int(2) ,\n" +
					"email varchar(255) NOT NULL default '0',\n" +
					"mobileNo varchar(15) ,\n" +
					"telephoneNo varchar(15) ,\n" +
					"address text ,\n" +
					"postalCode varchar(6) ,\n" +
					"country varchar(5) ,\n" +
					"state varchar(35) ,\n" +
					"city varchar(35) ,\n" +
					"yearOfWorkingExp varchar(35) ,\n" +
					"resumePath varchar(255) ,\n" +
					"expectedTypeSalary varchar(5) ,\n" +
					"expectedSalary float ,\n" +
					"negotiable char(1) ,\n" +
					"willingTravel varchar(10) ,\n" +
					"willingRelocate varchar(15) ,\n" +
					"ownTransport varchar(5) ,\n" +
					"createdDate datetime ,\n" +
					"applicantBlackListed char(1) ,\n" +
					"PRIMARY KEY (applicantId), \n" +
					"UNIQUE KEY (nirc) \n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_applicant_education (\n" +
					"eduId varchar(35) NOT NULL default '0' ,\n" +
					"applicantId varchar(250) ,\n" +
					"institute varchar(255) ,\n" +
					"highEduLevel varchar(5) ,\n" +
					"courseTitle varchar(255) ,\n" +
					"startDate date ,\n" +
					"endDate date ,\n" +
					"grade varchar(35) ,\n" +
					"PRIMARY KEY (eduId)\n" +
					")" , null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_applicant_employment (\n" +
					"empId varchar(35) NOT NULL default '0' ,\n" +
					"applicantId varchar(250) ,\n" +
					"companyName varchar(255) ,\n" +
					"positionName varchar(255) ,\n" +
					"positionLvl varchar(5) ,\n" +
					"startDate date ,\n" +
					"endDate date ,\n" +
					"reasonForLeave text ,\n" +
					"PRIMARY KEY (empId)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_applicant_skill (\n" +
					"skillId varchar(35) NOT NULL default '0' ,\n" +
					"applicantId varchar(250) ,\n" +
					"skill varchar(255) ,\n" +
					"yearOfExpSkill varchar(35) ,\n" +
					"proficiency varchar(2) ,\n" +
					"PRIMARY KEY (skillId)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_applicant_language (\n" +
					"languageId varchar(35) NOT NULL default '0' ,\n" +
					"applicantId varchar(250) ,\n" +
					"language varchar(5) ,\n" +
					"spoken int(2) ,\n" +
					"written int(2) ,\n" +
					"PRIMARY KEY (languageId)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_applicant_status (\n" +
					"applicantStatusId varchar(35) NOT NULL default '0' ,\n" +
					"vacancyCode varchar(35) ,\n" +
					"applicantId varchar(250) ,\n" +
					"dateApplied date ,\n" +
					"applicantStatus varchar(255) ,\n" +
					"jobOfferDate date ,\n" +
					"jobOfferLetterSent char(1) ,\n" +
					"jobOfferRemark text ,\n" +
					"jobOfferAdminRemark text ,\n" +
					"totalInterview int(2) ,\n" +
					"PRIMARY KEY (applicantStatusId) ,\n" +
					"UNIQUE KEY (applicantId) \n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_interview_date (\n" +
					"interviewDateId varchar(35) NOT NULL default '0' ,\n" +
					"applicantId varchar(35) ,\n" +
					"interviewDateTime datetime ,\n" +
					"interviewStageStatus varchar(2) ,\n" +
					"PRIMARY KEY (interviewDateId) \n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
		
		try{
			super.update("CREATE TABLE rec_interviewer_remark (\n" +
					"interviewerRemarkId varchar(250) NOT NULL default '0',\n" +
					"interviewDateId varchar(35) ,\n" +
					"vacancyCode varchar(35) ,\n" +
					"applicantId varchar(250) ,\n" +
					"interviewerId varchar(250) ,\n" +
					"remark text ,\n" +
					"createdBy varchar(250) ,\n" +
					"createdDate date ,\n" +
					"lastUpdatedBy varchar(250) ,\n" +
					"lastUpdatedDate date ,\n" +
					"PRIMARY KEY (interviewerRemarkId)\n" +
					")", null);
		}catch(Exception e){
			//ignore
		}
	}
	
	//	insert Job Application personal details
    public void insertJobAppPersonal(ApplicantObj obj) throws DaoException{
    	super.update("INSERT INTO rec_applicant_detail (applicantId, name, age, dob, " +
    			"nirc, gender, maritalStatus, noOfChild, nationality, email, mobileNo, telephoneNo, " +
    			"address, postalCode, country, state, city, yearOfWorkingExp, resumePath, " +
    			"expectedTypeSalary, expectedSalary, negotiable, willingTravel, willingRelocate, " +
    			"ownTransport, createdDate, applicantBlacklisted) " +
    			" VALUES(#applicantId#, #name#, #age#, #dob#, #nirc#, #gender#, #maritalStatus#, #noOfChild#, #nationality#, #email#, " +
    			"#mobileNo#, #telephoneNo#, #address#, #postalCode#, #country#, #state#, #city#, "+
    			"#yearOfWorkingExp#, #resumePath#, #expectedTypeSalary#, #expectedSalary#, #negotiable#, " +
    			"#willingTravel#, #willingRelocate#, #ownTransport#, #createdDate#, #applicantBlacklisted#)", obj);
    }
	
    //	insert Job Application employement/working experience details
    public void insertJobAppWorkingExp(ApplicantObj obj) throws DaoException{
    	super.update("INSERT INTO rec_applicant_employment (empId, applicantId, companyName, positionName, positionLvl, " +
    			"startDate, endDate, reasonForLeave) " +
    			" VALUES(#empId#, #applicantId#, #companyName#, #positionName#, #positionLvl#, #startDate#, " +
    			"#endDate#, #reasonForLeave#)", obj);
    }
    
    //insert Job Application education
    public void insertJobAppEdu(ApplicantObj obj) throws DaoException{
    	super.update("INSERT INTO rec_applicant_education (eduId, applicantId, institute, highEduLevel, courseTitle, " +
    			"startDate, endDate, grade) " +
    			" VALUES(#eduId#, #applicantId#, #institute#, #highEduLevel#, #courseTitle#, #startDate#, " +
    			"#endDate#, #grade#)", obj);
    }
    
    //find Job Application education 
    public Collection findJobAppEdu(String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	Collection findJobAppEduCol = super.select("SELECT eduId, applicantId, institute, highEduLevel, courseTitle, " +
    				" startDate, endDate, grade " +
    				" FROM rec_applicant_education " + 
    				" WHERE applicantId=? " 
    				, ApplicantObj.class, args, 0, -1);
    	
    	return findJobAppEduCol;
    }
    
    //find Job Application education particular
    public ApplicantObj findJobAppEduObj(String eduId) throws DaoException{
    	Object[] args = {eduId};
    	Collection applicantObjData = super.select("SELECT eduId, applicantId, institute, highEduLevel, courseTitle, " +
    				" startDate, endDate, grade " +
    				" FROM rec_applicant_education " + 
    				" WHERE eduId=? " 
    				, ApplicantObj.class, args, 0, 1);
    	
    	ApplicantObj aObj = (ApplicantObj)applicantObjData.iterator().next();
    	
    	return aObj;
    }
    
    //find Job Application education Key
    public Boolean findJobAppEduKey(String eduId) throws DaoException{
    	boolean flag=false;
    	Object[] args = {eduId};
    	Collection findJobAppEduCol = super.select("SELECT eduId, applicantId, institute, highEduLevel, courseTitle, " +
    				" startDate, endDate, grade " +
    				" FROM rec_applicant_education " + 
    				" WHERE eduId=? " 
    				, ApplicantObj.class, args, 0, 1);
    	if(findJobAppEduCol.size() > 0)
    		flag=true;
    	
    	return flag;
    }
    
    //delete the Job Application education
    public void deleteJobAppEdu(String eduId) throws DaoException{
    	super.update("DELETE FROM rec_applicant_education WHERE eduId=? ", new Object[]{eduId});
    }
    
    //insert Job Application skill details
    public void insertJobAppSkill(ApplicantObj obj) throws DaoException{
    	super.update("INSERT INTO rec_applicant_skill (skillId, applicantId, skill, yearOfExpSkill, proficiency) " +
    			" VALUES(#skillId#, #applicantId#, #skill#, #yearOfExpSkill#, #proficiency#)" , obj);
    }
    
    //insert Job Application language details
    public void insertJobAppLanguage(ApplicantObj obj) throws DaoException{
    	super.update("INSERT INTO rec_applicant_language (languageId, applicantId, language, spoken, written) " +
    			" VALUES(#languageId#, #applicantId#, #language#, #spoken#, #written#)" , obj);
    }
    
    //insert applicant Status
    public void insertApplicantStatus(ApplicantObj obj) throws DaoException{
    	super.update("INSERT INTO rec_applicant_status (applicantStatusId, vacancyCode, applicantId, dateApplied, applicantStatus, jobOfferDate, " +
    			"jobOfferLetterSent, jobOfferRemark, totalInterview) " +
    			" VALUES(#applicantStatusId#, #vacancyCode#, #applicantId#, #dateApplied#, #applicantStatus#, #jobOfferDate#, #jobOfferLetterSent# ," +
    			" #jobOfferRemark#, #totalInterview#)", obj);
    }
    
    //look up vacancy total
    public Collection lookUpVacancyTotal(String vacancyCode) throws Exception{
    	Object[] args = {vacancyCode};
    	Collection lookUpVacancyTotalCol=super.select("SELECT vacancyCode, totalApplied, totalShortlisted, totalScheduled, totalReScheduled, totalReScheduledRejected, " +
    			" totalAnotherInterview, totalJobOffered, totalInterviewUnsuccessful, totalJobAccepted, totalJobRejected, totalBlackListed, totalViewed FROM rec_vacancy_total WHERE" +
    			" vacancyCode=?" , HashMap.class, args, 0, 1);
    	
    	if(lookUpVacancyTotalCol.size()==0){
    		throw new DataObjectNotFoundException();
    	}else{
    		return lookUpVacancyTotalCol;
    	}
   
    }
    
    //lookup single applicant status
    public Collection lookUpApplicantStatus(String applicantId) throws Exception{
    	Object[] args = {applicantId};
    	Collection lookUpApplicantStatusCol=super.select("SELECT a.applicantStatusId, a.vacancyCode, a.applicantId, a.dateApplied, a.applicantStatus, a.jobOfferDate, " +
    			" a.jobOfferLetterSent, a.jobOfferRemark, a.totalInterview, b.name ,d.shortDesc as positionApplied " +
    			" FROM rec_applicant_status a, rec_applicant_detail b, rec_vacancy_detail c, org_chart_title d " +
    			" WHERE a.applicantId=b.applicantId AND a.vacancyCode=c.vacancyCode AND c.positionId=d.code" +
    			" AND a.applicantId=? " , ApplicantObj.class, args, 0, 1);
    	
    	if(lookUpApplicantStatusCol.size()==0){
    		throw new DataObjectNotFoundException();
    	}else{
    		return lookUpApplicantStatusCol;
    	}
   
    }
    
    //	look up email exist
    public boolean lookUpEmailExist(String strEmail) throws DaoException{
    	boolean flag=false;
    	Object[] args={strEmail};
    	Collection emailCol = super.select("SELECT email FROM rec_applicant_detail WHERE email=? ", HashMap.class, args, 0, 1);
    	
    	if(emailCol.size() > 0)
    		flag=true;
    		
    	return flag;
    }
    
    //	look up nirc exist
    public boolean lookUpNircExist(String strNirc) throws DaoException{
    	boolean flag=false;
    	Object[] args={strNirc};
    	Collection nircCol = super.select("SELECT nirc FROM rec_applicant_detail WHERE nirc=? ", HashMap.class, args, 0, 1);
    	
    	if(nircCol.size() > 0)
    		flag=true;
    		
    	return flag;
    }
    
    //lookup multiple applicant status-array
    public Collection lookUpApplicantStatus(String[] applicantId) throws Exception{
    	String sqlCause="";
    	for(int i=0; i< applicantId.length ; i++){
    		if(i>0 && i<applicantId.length)
    			sqlCause=sqlCause+" ,";
    		
    		sqlCause=sqlCause + "'"+ applicantId[i] +"'";
    	}
    	
    	Collection lookUpApplicantStatusCol=super.select("SELECT applicantStatusId, vacancyCode, applicantId, dateApplied, applicantStatus, jobOfferDate, " +
    			" jobOfferLetterSent, jobOfferRemark, totalInterview FROM rec_applicant_status WHERE applicantId IN ("+ sqlCause + ")" , ApplicantObj.class, null, 0, -1);
    	
    	if(lookUpApplicantStatusCol.size()==0){
    		throw new DataObjectNotFoundException();
    	}else{
    		return lookUpApplicantStatusCol;
    	}
   
    }
    
    //update vacancy total
    public void updateVacancyTotal(VacancyObj vacancyObj) throws DaoException{
    	super.update("UPDATE rec_vacancy_total SET totalApplied=#totalApplied#, totalShortlisted=#totalShortlisted#, totalScheduled=#totalScheduled#, " +
    			"totalReScheduled=#totalReScheduled#, totalReScheduledRejected=#totalReScheduledRejected#, totalAnotherInterview=#totalAnotherInterview#, " +
    			"totalJobOffered=#totalJobOffered#, totalInterviewUnsuccessful=#totalInterviewUnsuccessful#, " +
    			"totalJobAccepted=#totalJobAccepted#, totalJobRejected=#totalJobRejected#, totalBlackListed=#totalBlackListed#, " +
    			"totalViewed=#totalViewed# WHERE vacancyCode=#vacancyCode# ",  vacancyObj);
    }
    
    //update Applicant status
    public void updateApplicantStatus(ApplicantObj obj) throws DaoException{
    	super.update("UPDATE rec_applicant_status SET vacancyCode=#vacancyCode#, applicantId=#applicantId#, dateApplied=#dateApplied#, " +
    			"applicantStatus=#applicantStatus#, jobOfferDate=#jobOfferDate#, " +
    			"jobOfferLetterSent=#jobOfferLetterSent#, jobOfferRemark=#jobOfferRemark#, totalInterview=#totalInterview# " +
    			"WHERE applicantId=#applicantId# " , obj);
    }
    
    //get the particular vacancy details
    public Collection loadApplicantPersonal(String applicantId) throws DaoException, DaoException{
    	Object[] args = {applicantId};
    	
    	Collection col = super.select("SELECT a.applicantId, a.name, a.age, a.dob, a.nirc, a.gender, " +
    			"a.maritalStatus, a.noOfChild, a.nationality, a.email, a.mobileNo, a.telephoneNo, a.address, a.postalCode, a.country, a.state, a.city, " +
    			"a.yearOfWorkingExp, a.resumePath, a.expectedTypeSalary, a.expectedSalary, a.negotiable, a.willingTravel, a.willingRelocate, " +
    			"a.ownTransport, a.createdDate,  a.applicantBlacklisted, b.vacancyCode, b.applicantStatus, c.noOfPosition, c.noOfPositionOffered, " +
    			"d.shortDesc as positionDesc, e.shortDesc as countryDesc, f.shortDesc as departmentDesc " +
    			"FROM rec_applicant_detail a, rec_applicant_status b, rec_vacancy_detail c,  " +
    			"org_chart_title d, org_chart_country e, org_chart_department f " +
    			"WHERE a.applicantId=b.applicantId AND b.vacancyCode=c.vacancyCode AND c.positionId=d.code AND c.countryId=e.code AND c.departmentId=f.code " +
    			" AND a.applicantId=? ", HashMap.class, args, 0, 1); 
    	
    	return col;
    }
    
    public Collection findAllApplicantSpecial(String vacancyCode, String sort, boolean desc, int start, int rows, String applicantMisc,  String strApplicantStatus,
   		 String startDate, String endDate) throws DaoException{
    	
    	Object[] args = {vacancyCode};
    	HashMap 	mHighEduLevel = new LinkedHashMap();
    		
	    Application app = Application.getInstance();
	    mHighEduLevel.put("edu1", app.getMessage("recruit.general.label.primary") );
	    mHighEduLevel.put("edu2", app.getMessage("recruit.general.label.higher") );
	    mHighEduLevel.put("edu3", app.getMessage("recruit.general.label.professional") );
	    mHighEduLevel.put("edu4", app.getMessage("recruit.general.label.diploma") );
	    mHighEduLevel.put("edu5", app.getMessage("recruit.general.label.advanced") );
	    mHighEduLevel.put("edu6", app.getMessage("recruit.general.label.bachelor") );
	    mHighEduLevel.put("edu7", app.getMessage("recruit.general.label.postGraduate") );
	    mHighEduLevel.put("edu8", app.getMessage("recruit.general.label.professionalDegree") );
	    mHighEduLevel.put("edu9", app.getMessage("recruit.general.label.master") );
	    mHighEduLevel.put("edu10", app.getMessage("recruit.general.label.phd") );
	           	
    	String sqlCause = findAllApplicantSqlCause(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate);
    	
    	//sorting part-
    	String sqlSort="";
    	if(sort!=null){
    		if("paramCombine".equals(sort)){
    			sort="a.name";
	    	}
    	}else{
    		sqlSort=" ORDER BY c.dateApplied desc, a.name asc "; //default
    	}
    		
    	Collection col=null;
    	if(vacancyCode!=null && !vacancyCode.equals("")){
	    	col = super.select("SELECT a.applicantId as applicantId, a.name, a.yearOfWorkingExp, " +
	    			"c.dateApplied, c.applicantStatus, c.vacancyCode, a.applicantId " +
	    			"FROM rec_applicant_detail a, rec_applicant_status c " +
	    			"WHERE a.applicantId=c.applicantId AND a.applicantId IN" +
	    			"(Select applicantId from rec_applicant_status where vacancyCode=?) " + sqlCause +
	    			sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, args, start, rows);
    	}else{
    		col = super.select("SELECT a.applicantId as applicantId, a.name, a.yearOfWorkingExp, " +
	    			"c.dateApplied, c.applicantStatus, c.vacancyCode, a.applicantId " +
	    			"FROM rec_applicant_detail a, rec_applicant_status c " +
	    			"WHERE a.applicantId=c.applicantId AND c.applicantStatus IN('Short-Listed', 'Another Interview') " + sqlCause +
	    			sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);	
    	}
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		HashMap map = (HashMap)ite.next();
    		String applicantId=map.get("applicantId").toString();
    		Object[] datas = {applicantId};
    		
    		/*Collection educationCol = super.select("SELECT institute, highEduLevel, courseTitle, startDate, endDate, grade " +
    				"FROM rec_applicant_education " +
    				"WHERE applicantId=? " +
    				"order by highEduLevel desc ", HashMap.class, datas, 0, 1);*/
    		Collection educationCol = super.select("SELECT institute, highEduLevel, courseTitle, startDate, endDate, grade " +
    				"FROM rec_applicant_education " +
    				"WHERE applicantId=? " +
    				" ORDER BY highEduLevel desc ", HashMap.class, datas, 0, 1);
    		
    		HashMap eduMap = (HashMap)educationCol.iterator().next();
    		map.put("highEduLevelDesc", mHighEduLevel.get(eduMap.get("highEduLevel")));
    		map.put("courseTitle", eduMap.get("courseTitle"));
    		map.put("grade", eduMap.get("grade"));
    		map.put("paramCombine", map.get("applicantId") +"/"+ map.get("name"));
    	}
    	
    	/*for(Iterator ite = col.iterator(); ite.hasNext();){
    		ApplicantObj applicantObj = (ApplicantObj) ite.next();
    		applicantObj.setHighEduLevelDesc(mHighEduLevel.get(applicantObj.getHighEduLevel()).toString());
    	}*/
    	
    	return col;
    }
    
    
    //find all applicant details in a vacancy
    public Collection findAllApplicant(String vacancyCode, String sort, boolean desc, int start, int rows, String applicantMisc,  String strApplicantStatus,
    		 String startDate, String endDate) throws DaoException{
    	Object[] args = {vacancyCode};
    	HashMap 	mHighEduLevel = new LinkedHashMap();
    		
	    Application app = Application.getInstance();
	    mHighEduLevel.put("edu1", app.getMessage("recruit.general.label.primary") );
	    mHighEduLevel.put("edu2", app.getMessage("recruit.general.label.higher") );
	    mHighEduLevel.put("edu3", app.getMessage("recruit.general.label.professional") );
	    mHighEduLevel.put("edu4", app.getMessage("recruit.general.label.diploma") );
	    mHighEduLevel.put("edu5", app.getMessage("recruit.general.label.advanced") );
	    mHighEduLevel.put("edu6", app.getMessage("recruit.general.label.bachelor") );
	    mHighEduLevel.put("edu7", app.getMessage("recruit.general.label.postGraduate") );
	    mHighEduLevel.put("edu8", app.getMessage("recruit.general.label.professionalDegree") );
	    mHighEduLevel.put("edu9", app.getMessage("recruit.general.label.master") );
	    mHighEduLevel.put("edu10", app.getMessage("recruit.general.label.phd") );
	           	
    	String sqlCause = findAllApplicantSqlCause(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate);
    	Collection col=null;
    	if(vacancyCode!=null && !vacancyCode.equals("")){
	    	col = super.select("SELECT a.applicantId as applicantId, a.name, a.yearOfWorkingExp, b.courseTitle, b.grade, b.highEduLevel ," +
	    			"c.dateApplied, c.applicantStatus, c.vacancyCode, a.applicantId " +
	    			"FROM rec_applicant_detail a, rec_applicant_education b, rec_applicant_status c " +
	    			"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND a.applicantId IN" +
	    			"(Select applicantId from rec_applicant_status where vacancyCode=?) " + sqlCause +
	    			" ORDER BY c.dateApplied desc, a.name asc" + JdbcUtil.getSort(sort, desc), ApplicantObj.class, args, start, rows);
    	}else{
    		col = super.select("SELECT a.applicantId as applicantId, a.name, a.yearOfWorkingExp, b.courseTitle, b.grade, b.highEduLevel ," +
	    			"c.dateApplied, c.applicantStatus, c.vacancyCode, a.applicantId " +
	    			"FROM rec_applicant_detail a, rec_applicant_education b, rec_applicant_status c " +
	    			"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND c.applicantStatus IN('Short-Listed', 'Another Interview') " + sqlCause +
	    			" ORDER BY c.dateApplied desc, a.name asc" + JdbcUtil.getSort(sort, desc), ApplicantObj.class, null, start, rows);
    	}
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		ApplicantObj applicantObj = (ApplicantObj) ite.next();
    		applicantObj.setHighEduLevelDesc(mHighEduLevel.get(applicantObj.getHighEduLevel()).toString());
    	}
    	
    	return col;
    }
    
    //find applicant SQL cause
    public String findAllApplicantSqlCause(String vacancyCode, String applicantMisc, String strApplicantStatus, String startDate, String endDate){
    	Application app = Application.getInstance();
    	String sqlCause="";
    	/*if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (a.name LIKE '%" + applicantMisc + "%' OR b.courseTitle LIKE '%" + applicantMisc + "%' " +
    							" OR b.grade LIKE '%" + applicantMisc + "%'  OR a.yearOfWorkingExp LIKE '%" + applicantMisc + "%' ) ";
    	}*/
    	if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (a.name LIKE '%" + applicantMisc + "%' OR a.yearOfWorkingExp LIKE '%" + applicantMisc + "%')";
    	}
    	
	    if(strApplicantStatus!=null && !strApplicantStatus.equals("")){
	    	if(vacancyCode!=null && !vacancyCode.equals("")){
		    	String statusName="";
		    	if(strApplicantStatus.equals("0"))
		    		statusName=app.getMessage("recruit.general.label.new");
		    	else if(strApplicantStatus.equals("1"))
		    		statusName=app.getMessage("recruit.general.label.kiv");
		    	else if(strApplicantStatus.equals("2"))
		    		statusName=app.getMessage("recruit.general.label.short-listed");
		    	else if(strApplicantStatus.equals("3"))
		    		statusName=app.getMessage("recruit.general.label.shortlistedAndEmailSend");
		    	else if(strApplicantStatus.equals("4"))
		    		statusName=app.getMessage("recruit.general.label.scheduled");
		    	/*else if(strApplicantStatus.equals("4"))
		    		statusName=app.getMessage("recruit.general.label.scheduledAndInterviewerAdded");
		    	else if(strApplicantStatus.equals("4"))
		    		statusName=app.getMessage("recruit.general.label.rescheduledAndRejected");*/
		    	else if(strApplicantStatus.equals("5"))
		    		statusName=app.getMessage("recruit.general.label.offered");
		    	else if(strApplicantStatus.equals("6"))
		    		statusName=app.getMessage("recruit.general.label.interviewUnsuccessful");
		    	else if(strApplicantStatus.equals("7"))
		    		statusName=app.getMessage("recruit.general.label.anotherInterview");
		    	else if(strApplicantStatus.equals("8"))
		    		statusName=app.getMessage("recruit.general.label.anotherInterviewAndEmailSend");
		    	else if(strApplicantStatus.equals("9"))
		    		statusName=app.getMessage("recruit.general.label.jobAccepted");
		    	else if(strApplicantStatus.equals("10"))
		    		statusName=app.getMessage("recruit.general.label.jobRejected");
		    	else if(strApplicantStatus.equals("11"))
		    		statusName=app.getMessage("recruit.general.label.rejectedApplicant");
		    	else if(strApplicantStatus.equals("12"))
		    		statusName=app.getMessage("recruit.general.label.black-listed");
		
		    	sqlCause +=" AND c.applicantStatus='"+ statusName + "' ";
	    	}else{
	    		sqlCause +=" AND c.vacancyCode='"+ strApplicantStatus + "' ";
	    	}
	    }
 
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND c.dateApplied BETWEEN " + "'" + startDate + "'" + " AND " + "'" +  endDate + "' ";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND c.dateApplied='"+ startDate +"' ";
    	}
    
    	return sqlCause;
    }
    
    //  count total applicant
    public int countAllApplicantSpecial(String vacancyCode, String applicantMisc, String strApplicantStatus, String startDate, String endDate) throws DaoException{
    	Object[] args = {vacancyCode};
    	String sqlCause = findAllApplicantSqlCause(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate);
   
    	Collection col = super.select("SELECT COUNT(*) AS total " +
    			"FROM rec_applicant_detail a, rec_applicant_status c " +
    			"WHERE a.applicantId=c.applicantId AND a.applicantId IN" +
    			"(Select applicantId from rec_applicant_status where vacancyCode=?) " + sqlCause, 
    			HashMap.class, args, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //  count total applicant
    public int countAllApplicant(String vacancyCode, String applicantMisc, String strApplicantStatus, String startDate, String endDate) throws DaoException{
    	Object[] args = {vacancyCode};
    	String sqlCause = findAllApplicantSqlCause(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate);
   
    	Collection col = super.select("SELECT COUNT(*) AS total " +
    			"FROM rec_applicant_detail a, rec_applicant_education b, rec_applicant_status c " +
    			"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND a.applicantId IN" +
    			"(Select applicantId from rec_applicant_status where vacancyCode=?) " + sqlCause, 
    			HashMap.class, args, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
  
   //delete all applicant details 
    public void deleteAllApplicantDetail(String applicantId) throws DaoException{
    	super.update("DELETE FROM rec_applicant_detail WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_applicant_education WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_applicant_employment WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_applicant_language WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_applicant_skill WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_applicant_status WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_interview_date WHERE applicantId=? ", new Object[]{applicantId});
    	super.update("DELETE FROM rec_interviewer_remark WHERE applicantId=? ", new Object[]{applicantId});
    }
  
    //load all shortlisted applicant id - all applicant status named
    public Collection loadShortlisted(String[] applicantId, String applicantStatus) throws DaoException{
    	
    	String statusName="";
    	if(applicantStatus.contains(",")){
	    	String[] statusType=applicantStatus.split(",");
	    	statusName = "'"+statusType[0]+"'" +", '"+statusType[1]+"'";	
    	}else{
    		statusName="'"+applicantStatus+"'";
    	}
    	
    	String sqlCause="";
    	for(int i=0; i< applicantId.length ; i++){
    		
    		if(i>0 && i<applicantId.length)
    			sqlCause=sqlCause+" ,";
    		
    		sqlCause=sqlCause + "'"+ applicantId[i] +"'";
    	}
    	
    	/*Collection col = super.select("SELECT a.vacancyCode, a.applicantId, a.dateApplied, a.applicantStatus, b.name, b.applicantId " +
    			"FROM rec_applicant_status a, rec_applicant_detail b WHERE a.applicantId=b.applicantId AND " +
    			"a.applicantStatus='"+ applicantStatus +"' AND a.applicantId IN (" + sqlCause + ")", ApplicantObj.class, null, 0, -1);*/
    	
    	Collection col = super.select("SELECT a.vacancyCode, a.applicantId, a.dateApplied, a.applicantStatus, b.name, b.applicantId " +
    			"FROM rec_applicant_status a, rec_applicant_detail b WHERE a.applicantId=b.applicantId AND " +
    			"a.applicantStatus IN ("+ statusName +") AND a.applicantId IN (" + sqlCause + ")", ApplicantObj.class, null, 0, -1);
    	
    	return col;
    }
    
    //inserting interview date
    public void insertInterviewDate(InterviewObj obj) throws DaoException{
    	//for(int i=0; i<obj.getApplicantId().length; i++){
    		String interviewDateId = obj.getInterviewDateId()[0];
    		String applicantId = obj.getApplicantId()[0];
    		
    		super.update("INSERT INTO rec_interview_date (interviewDateId, applicantId, interviewDateTime, interviewStageStatus) " +
        			" VALUES(?, ?, ?, ?)"
    				, new Object[]{interviewDateId, applicantId, obj.getInterviewDateTime(), obj.getInterviewStageStatus()});
    	//}
    }
    
    //find interview date with the status - mail usage ONLY 
    public Collection findInterviewDateWithStatus(String[] applicantId) throws DaoException{
    	Collection interviewCol = new ArrayList();
    	
    	//only will always has 1 rows of data- temp
    	for(int i=0; i< applicantId.length; i++){
    		HashMap setMap = new HashMap();
	    	Collection col=super.select("SELECT a.interviewDateId, a.applicantId, a.interviewDateTime, a.interviewStageStatus, " +
	    	" b.applicantStatus, b.totalInterview, c.name  " +
	    	" FROM rec_interview_date a, rec_applicant_status b, rec_applicant_detail c " + 
	    	" WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId" +
	    	" AND a.applicantId='"+ applicantId[i] +"'" + 
			" order by a.interviewStageStatus desc  ", HashMap.class, null, 0, 1);
	    	
	    	HashMap map = (HashMap) col.iterator().next();
	    	setMap.put("interviewStageStatus", map.get("interviewStageStatus")); //add 26/12/06
	    	setMap.put("interviewDateId", map.get("interviewDateId")); //add 26/12/06
	    	setMap.put("interviewDateTime", map.get("interviewDateTime"));
	    	setMap.put("name", map.get("name"));
	    	interviewCol.add(setMap);
    	}
    	
    	return interviewCol;
    }
    
    //find interview date
    public Collection findInterviewDate(String[] applicantId) throws DaoException{
    	
    	String sqlCause="";
    	for(int i=0; i< applicantId.length ; i++){
    		if(i>0 && i<applicantId.length)
    			sqlCause+=" ,";
    		
    		sqlCause+= "'"+ applicantId[i] +"'";
    	}
    	
    	Collection col=super.select("SELECT a.interviewDateId, a.applicantId, a.interviewDateTime, a.interviewStageStatus, " +
    	"b.applicantStatus, b.totalInterview " +
    	"FROM rec_interview_date a, rec_applicant_status b " +
    	"WHERE a.applicantId=b.applicantId " + 
    	"AND a.applicantId IN ("+sqlCause+")" , HashMap.class, null, 0, -1);
    	
    	return col;
    }
    
    
    //Update interview date-**
    public void updateInterviewDate(InterviewObj obj, String[] interviewStageStatus) throws DaoException{
    	//ori
    	/*for(int i=0; i<obj.getApplicantId().length; i++){
    		super.update("UPDATE rec_interview_date SET interviewStageStatus=? WHERE applicantId=? " , new Object[]{obj.getInterviewStageStatus(), 
    				obj.getApplicantId()[i]});
    	}*/
    	for(int i=0; i<obj.getInterviewDateId().length; i++){
    		super.update("UPDATE rec_interview_date SET interviewStageStatus='"+interviewStageStatus[i]+"' WHERE interviewDateId=? " , new Object[]{
    				obj.getInterviewDateId()[i]});
    	}
    }
    
    //  Update interview date with interviewDateId - re-schedule interviewDateTime
    public void updateInterviewDateTime(InterviewObj obj) throws DaoException{
    	super.update("UPDATE rec_interview_date SET interviewDateTime=? WHERE interviewDateId=? " , new Object[]{obj.getInterviewDateTime(),
    			obj.getInterviewDateId()[0]});
    }
    
    
    //validate the interview date
    public boolean validateHasInterviewDate(String[] applicantId) throws DaoException{
    	boolean flag=false;
    	String sqlCause="";
    	for(int i=0; i< applicantId.length ; i++){
    		if(i>0 && i<applicantId.length)
    			sqlCause=sqlCause+" ,";
    		
    		sqlCause=sqlCause + "'"+ applicantId[i] +"'";
    	}
    	
    	Collection col=super.select("SELECT applicantId, interviewStageStatus FROM rec_interview_date WHERE interviewStageStatus='0' AND " +
    			"applicantId IN ("+ sqlCause + ")" , InterviewObj.class, null, 0, -1);
    	
    	if(col.size()>0)
    		flag=true; //mean got data and validation failed
    		
    	return flag;
    }
    
    //	find all applicant scheduled for interview
    public Collection findAllInterviewee(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate) throws DaoException{
    	
    	String sqlCause = findAllIntervieweeSqlCause(applicantMisc, startDate, endDate);
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		if("paramCombine".equals(sort)){
    			sort="c.name";
    		}
    		
    		if("vacancyCode".equals(sort)){
    			sort="a.vacancyCode";
    		}
    		
    		if("interviewStageName".equals(sort)){
    			sort="interviewStageStatus";
    		}
    	}else{
    		sqlSort=" ORDER BY b.interviewDateTime desc ";
    	}
    	
    	Collection col=super.select("SELECT a.totalInterview, a.applicantId, a.vacancyCode, a.applicantStatus, b.interviewDateId, b.interviewDateTime, b.interviewStageStatus, " +
    	"c.name, d.positionId, e.shortDesc as positionDesc " +
    	"FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, rec_vacancy_detail d, org_chart_title e " +
    	"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND d.positionId=e.code " +
    	//"AND a.vacancyCode=d.vacancyCode AND a.applicantStatus IN ('Scheduled', 'Scheduled & Interviewer(s) added') " + sqlCause + 
   	    "AND a.vacancyCode=d.vacancyCode AND a.applicantStatus IN ('Scheduled') " + sqlCause + 
    	sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		HashMap map = (HashMap) ite.next();
    		mapInterviewStageStatus(map);
    		
    		//remove the duplicated interviewee. example display the 1st round of interview and 2nd round of interview for the same interviewee name
    		int total = Integer.parseInt(map.get("totalInterview").toString());
    		total+=1;
    		
    		if(!String.valueOf(total).equals(map.get("interviewStageStatus")))
    			ite.remove();
    			
    		map.put("paramCombine", map.get("applicantId") +"/" + map.get("interviewDateId") +"/"+ map.get("name") +"/" + map.get("totalInterview"));
    	}
    	
    	return col;
    }
  
    //  count total interviewee
    public int countAllInterviewee(String applicantMisc, String startDate, String endDate) throws DaoException{
    	String sqlCause = findAllIntervieweeSqlCause(applicantMisc, startDate, endDate);
   
    	Collection col = super.select("SELECT COUNT(*) AS total " +
    			"FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, rec_vacancy_detail d " +
    	    	"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId " +
    	    	"AND a.vacancyCode=d.vacancyCode " + sqlCause,
    			HashMap.class, null, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //sql cause
    public String findAllIntervieweeSqlCause(String applicantMisc, String startDate, String endDate){
    	String sqlCause="";
    	
    	if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (c.name LIKE '%" + applicantMisc + "%' OR a.vacancyCode LIKE '%" + applicantMisc + "%' )";
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND b.interviewDateTime BETWEEN " + "'" + startDate + " 00:00:00 '" + " AND " + "'" +  endDate + " 23:59:00 ' ";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND b.interviewDateTime BETWEEN '"+ startDate +"  00:00:00 ' AND '" + startDate + " 23:59:00 '";
    	}
    	
    	return sqlCause;
    }
    
    //  delete interview date details 
    public void deleteInterviewDate(String selectedkey) throws DaoException{
    	super.update("DELETE FROM rec_interview_date WHERE interviewDateId=? ", new Object[]{selectedkey}); 	
    	super.update("DELETE FROM rec_interviewer_remark WHERE interviewDateId=? ", new Object[]{selectedkey}); //added 26/12/06
    }
    
    //delete interview remark details
    public void deleteInterviewRemark(String selectedkey) throws DaoException{
    	super.update("DELETE FROM rec_interviewer_remark WHERE interviewerRemarkId=? ", new Object[]{selectedkey}); 	
    }
    
    //	find all applicant scheduled for interview
    public Collection findSelectedInterviewee(String sort, boolean desc, int start, int rows, Collection colId, String applicantStatus) throws DaoException{
    	
    	String sqlCause="";
    	int i=0;
    	for(Iterator ite=colId.iterator(); ite.hasNext();){
    		if(i>0 && i<colId.size())
    			sqlCause+=" ,";
    		
    		sqlCause += "'"+ ite.next().toString() +"'";
    		i++;	
    	}
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		if("paramCombine".equals(sort)){
    			sort="c.name";
    		}
    		
    		if("interviewStageName".equals(sort)){
    			sort="b.interviewStageStatus";
    		}
    	}else{
    		sqlSort=" ORDER BY b.interviewDateTime desc ";
    	}
    	
    	/*Collection col=super.select("SELECT a.jobOfferRemark, a.applicantId, a.vacancyCode, a.applicantStatus, b.interviewDateId, b.interviewDateTime, b.interviewStageStatus, " +
    	"c.name, d.positionId, e.shortDesc as positionDesc, d.noOfPositionOffered, d.noOfPosition " +
    	"FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, rec_vacancy_detail d, org_chart_title e " +
    	"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND d.positionId=e.code " +
    	"AND a.vacancyCode=d.vacancyCode AND a.applicantStatus='"+applicantStatus+"' " +
    	"AND b.interviewDateId IN (" + sqlCause + ")" +
    	" ORDER BY b.interviewDateTime desc "+ JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);*/
    	Collection col=super.select("SELECT a.jobOfferRemark, a.applicantId, a.vacancyCode, a.applicantStatus, b.interviewDateId, b.interviewDateTime, b.interviewStageStatus, " +
    	    	"c.name, d.positionId, e.shortDesc as positionDesc, d.noOfPositionOffered, d.noOfPosition " +
    	    	"FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, rec_vacancy_detail d, org_chart_title e " +
    	    	"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND d.positionId=e.code " +
    	    	"AND a.vacancyCode=d.vacancyCode AND a.applicantStatus='"+applicantStatus+"' " +
    	    	"AND b.interviewDateId IN (" + sqlCause + ")" +
    	    	sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);
    	
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		HashMap map = (HashMap) ite.next();
    		
    		mapInterviewStageStatus(map);
    		
    		if(map.get("noOfPositionOffered")!=null && !map.get("noOfPositionOffered").equals(""))
    			map.put("totalPosition", map.get("noOfPositionOffered") + "/" + map.get("noOfPosition"));
    		else
    			map.put("totalPosition", "0" + "/" + map.get("noOfPosition"));
    		
    		map.put("paramCombine",  map.get("interviewDateId") +"/"+ map.get("applicantId") +"/" + map.get("name"));
    	}
    	
    	return col;
    }
  
    //	find all re-schedule interviewee
    public Collection findReScheduleInterviewee(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate, 
    		Collection colId, String applicantStatus) throws DaoException{
    	
    	String sqlCause="";
    	int i=0;
    	for(Iterator ite=colId.iterator(); ite.hasNext();){
    		if(i>0 && i<colId.size())
    			sqlCause+=" ,";
    		
    		sqlCause += "'"+ ite.next().toString() +"'";
    		i++;	
    	}
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		if("paramCombine".equals(sort)){
    			sort="c.name";
    		}
    		
    		if("interviewStageName".equals(sort)){
    			sort="b.interviewStageStatus";
    		}
    	}else{
    		sqlSort=" ORDER BY b.interviewDateTime desc ";
    	}
    	
    	String sqlFinder=findReScheduleCause(applicantMisc, startDate, endDate);
    	
    	Collection col=super.select("SELECT a.jobOfferRemark, a.applicantId, a.vacancyCode, a.applicantStatus, b.interviewDateId, b.interviewDateTime, b.interviewStageStatus, " +
    	    	"c.name, d.positionId, e.shortDesc as positionDesc, d.noOfPositionOffered, d.noOfPosition " +
    	    	"FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, rec_vacancy_detail d, org_chart_title e " +
    	    	"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND d.positionId=e.code " +
    	    	"AND a.vacancyCode=d.vacancyCode AND a.applicantStatus='"+applicantStatus+"' " +
    	    	"AND b.interviewDateId IN (" + sqlCause + ")" + sqlFinder +
    	    	sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);
    	
    	
    	for(Iterator ite = col.iterator(); ite.hasNext();){
    		HashMap map = (HashMap) ite.next();
    		
    		mapInterviewStageStatus(map);
    		
    		if(map.get("noOfPositionOffered")!=null && !map.get("noOfPositionOffered").equals(""))
    			map.put("totalPosition", map.get("noOfPositionOffered") + "/" + map.get("noOfPosition"));
    		else
    			map.put("totalPosition", "0" + "/" + map.get("noOfPosition"));
    		
    		map.put("paramCombine",  map.get("interviewDateId") +"/"+ map.get("applicantId") +"/" + map.get("name"));
    	}
    	
    	return col;
    }
    
    //  sql cause for interviewer remark
    public String findReScheduleCause(String applicantMisc, String startDate, String endDate){
    	String sqlCause="";
    	
    	if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (c.name LIKE '%" + applicantMisc + "%' OR a.vacancyCode LIKE '%" + applicantMisc + "%' " +
    				" OR e.shortDesc LIKE '%" + applicantMisc + "%' OR a.applicantStatus LIKE '%" + applicantMisc + "%')";
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND b.interviewDateTime BETWEEN " + "'" + startDate + " 00:00:00 '" + " AND " + "'" +  endDate + " 23:59:00 ' ";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND b.interviewDateTime BETWEEN '"+ startDate +"  00:00:00 ' AND '" + startDate + " 23:59:00 '";
    	}
    
    	return sqlCause;
    }
    
    //  count total re-schedule
    public int countAllReSchedule(String applicantMisc, String startDate, String endDate, Collection colId, String applicantStatus) throws DaoException{
    	String sqlFinder = findReScheduleCause(applicantMisc, startDate, endDate);
    	
    	String sqlCause="";
    	int i=0;
    	for(Iterator ite=colId.iterator(); ite.hasNext();){
    		if(i>0 && i<colId.size())
    			sqlCause+=" ,";
    		
    		sqlCause += "'"+ ite.next().toString() +"'";
    		i++;	
    	}
    	
    	Collection col=super.select("SELECT COUNT(*) AS total " +
    	    	"FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, rec_vacancy_detail d, org_chart_title e " +
    	    	"WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId AND d.positionId=e.code " +
    	    	"AND a.vacancyCode=d.vacancyCode AND a.applicantStatus='"+applicantStatus+"' " +
    	    	"AND b.interviewDateId IN (" + sqlCause + ")" + sqlFinder, HashMap.class, null, 0, 1);
    		
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //  inserting interview date
    public void insertInterviewer(Collection intervieweeCol, InterviewObj obj) throws DaoException{
    	//intervieweeCol-consist of 3 element(interviewDateId, applicantId, vacancyCode)
    	//obj-consist of important element(no of interviewers)
    	UuidGenerator uuid = UuidGenerator.getInstance();//interview remark id
    	//int j=0;
    	for(Iterator ite=intervieweeCol.iterator(); ite.hasNext();){
    		HashMap map = (HashMap) ite.next();
    		String interviewDateId = map.get("interviewDateId").toString();
    		String vacancyCode = map.get("vacancyCode").toString();
    		String applicantId = map.get("applicantId").toString();
    		//String interviewerRemarkId = obj.getInterviewerRemarkId()[j];
    		for(int i=0; i<obj.getInterviewerId().length;i++){
    			String interviewerId = obj.getInterviewerId()[i];
    			//String interviewerRemarkId = obj.getInterviewerRemarkId()[i];
    			String remark = "";
    			String createdBy = obj.getCreatedBy();
    			Date createdDate = obj.getCreatedDate();
    			String lastUpdatedBy = obj.getLastUpdatedBy();
    			Date lastUpdatedDate = obj.getLastUpdatedDate();
    			
    			/*super.update("INSERT INTO rec_interviewer_remark(interviewerRemarkId, interviewDateId, vacancyCode, applicantId, interviewerId, remark, " +
    					"createdBy, createdDate, lastUpdatedBy, lastUpdatedDate ) " +
            			" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
        				, new Object[]{interviewerRemarkId, interviewDateId, vacancyCode, applicantId, interviewerId, remark, 
    					createdBy, createdDate, lastUpdatedBy, lastUpdatedDate});*/
    			super.update("INSERT INTO rec_interviewer_remark(interviewerRemarkId, interviewDateId, vacancyCode, applicantId, interviewerId, remark, " +
    					"createdBy, createdDate, lastUpdatedBy, lastUpdatedDate ) " +
            			" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
        				, new Object[]{uuid.getUuid(), interviewDateId, vacancyCode, applicantId, interviewerId, remark, 
    					createdBy, createdDate, lastUpdatedBy, lastUpdatedDate});
    		}
    		//j++;
    	}
    }
    
    //  inserting interview remark-normal*particular applicant 
    public void insertInterviewerRemark(InterviewObj obj) throws DaoException{	
    	for(int i=0; i<obj.getInterviewerId().length;i++){
    		String interviewerId = obj.getInterviewerId()[i];
    		String interviewerRemarkId = obj.getInterviewerRemarkId()[i];
    		String remark = "";
			
			String interviewDateId = obj.getInterviewDateId()[0];
			String applicantId = obj.getApplicantId()[0];
			
			String vacancyCode = obj.getVacancyCode();
			String createdBy = obj.getCreatedBy();
			Date createdDate = obj.getCreatedDate();
			String lastUpdatedBy = obj.getLastUpdatedBy();
			Date lastUpdatedDate = obj.getLastUpdatedDate();
			
			super.update("INSERT INTO rec_interviewer_remark(interviewerRemarkId, interviewDateId, vacancyCode, applicantId, interviewerId, remark, " +
					"createdBy, createdDate, lastUpdatedBy, lastUpdatedDate ) " +
        			" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
    				, new Object[]{interviewerRemarkId, interviewDateId, vacancyCode, applicantId, interviewerId, remark, 
					createdBy, createdDate, lastUpdatedBy, lastUpdatedDate});
    	}
    }
    
    
    public Collection getSecurityUserNameSingle(String id)throws DaoException{
    	Object[] args = {id};
    	
    	Collection col = super.select("SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) as username FROM security_user WHERE id=? " , HashMap.class, args, 0, 1);
 	
    	return col;	
    }
    
    //get the security user name (interviewer name) using interviewDateId
    public Collection getSecurityUserName(String interviewDateId)throws DaoException{
    	Object[] args = {interviewDateId};
    	
    	Collection col = super.select("SELECT CONCAT(CAST(firstName AS CHAR),' ' , CAST(lastName AS CHAR)) as username FROM security_user WHERE id IN (SELECT interviewerId FROM rec_interviewer_remark " +
    			"WHERE interviewDateId=? )" , HashMap.class, args, 0, -1);
    		
    	return col;	
    }
    
    //update applicant status- applicant result
    public void updateApplicantStatusResult(ApplicantObj obj) throws DaoException{
    	super.update("UPDATE rec_applicant_status SET applicantStatus=#applicantStatus#, jobOfferDate=#jobOfferDate#, " +
    			"jobOfferRemark=#jobOfferRemark#, totalInterview=#totalInterview# " +
    			"WHERE applicantId=#applicantId# " , obj);
    }
    
    public void updateApplicantOfferStatusResult(ApplicantObj obj, boolean updateStatus) throws DaoException{
    	if(updateStatus){
    		super.update("UPDATE rec_applicant_status SET applicantStatus=#applicantStatus#, jobOfferLetterSent=#jobOfferLetterSent#, " +
    			" jobOfferAdminRemark=#jobOfferAdminRemark# " +
    			"WHERE applicantId=#applicantId# " , obj);
    	}else{
    		super.update("UPDATE rec_applicant_status SET jobOfferLetterSent=#jobOfferLetterSent#, jobOfferAdminRemark=#jobOfferAdminRemark# " +
        			" WHERE applicantId=#applicantId# " , obj);
    	}
    		
    }
    
    //lookup interviewee remark
    public Collection findAllIntervieweeRemark(String sort, boolean desc, int start, int rows, String applicantId, String interviewDateId) throws DaoException{
    	//Collection findAllRemarkCol;
    	
    	/*if(interviewDateId!=null && !interviewDateId.equals("")){
    	Object[] args = {interviewDateId};	
    	findAllRemarkCol=super.select("SELECT a.interviewDateId, a.vacancyCode, a.applicantId, a.interviewerId, a.remark, a.lastUpdatedDate, " +
    	    	"b.interviewDateTime, b.interviewStageStatus, CONCAT(CAST(c.firstName AS CHAR),' ' , CAST(c.lastName AS CHAR)) as username " +
    	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c " +
    	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id AND " +
    	    	"a.interviewDateId=? " +
    	    	"Order by b.interviewStageStatus asc ",  HashMap.class, args, 0, -1);
    	}else{*/
    	Object[] args = {applicantId};	
    	Collection findAllRemarkCol=super.select("SELECT a.interviewerRemarkId, a.interviewDateId, a.vacancyCode, a.applicantId, a.interviewerId, a.remark, a.lastUpdatedDate, " +
        	    	"b.interviewDateTime, b.interviewStageStatus, CONCAT(CAST(c.firstName AS CHAR),' ' , CAST(c.lastName AS CHAR)) as username " +
        	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c " +
        	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id AND " +
        	    	"a.applicantId=? " +
        	    	"Order by b.interviewStageStatus asc ",  HashMap.class, args, 0, -1);
    	
    	
    	for(Iterator ite=findAllRemarkCol.iterator(); ite.hasNext();){
			HashMap map = (HashMap) ite.next();
			mapInterviewStageStatus(map);
			
			if(map.get("remark").equals(""))
				map.put("remark", "-");
				//map.put("remark", "In progress");
		}
    	
    	return findAllRemarkCol;
    }
   
    
    //  lookup interviewee remark
    public Collection findInterviewer(String sort, boolean desc, int start, int rows, String interviewerId,  String applicantMisc, String startDate, String endDate) throws DaoException{
    	
    	String sqlCause=findInterviewerRemark(applicantMisc, startDate, endDate);
    	
    	Object[] args = {interviewerId};
    	//last date modified 26/12/06
    	/*Collection col=super.select("SELECT a.interviewDateId, a.vacancyCode, a.applicantId, a.interviewerId, a.remark, " +
    			"a.createdDate as dateAssigned, b.interviewDateTime, b.interviewStageStatus, d.name, f.shortDesc as positionDesc  " +
    	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c, rec_applicant_detail d, " +
				"rec_vacancy_detail e, org_chart_title f " +
    	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id " +
    	    	"AND a.applicantId=d.applicantId AND a.vacancyCode=e.vacancyCode AND e.positionId=f.code " +
    	    	"AND a.interviewerId=?" +sqlCause +
    	    	" Order by b.interviewStageStatus asc, a.createdDate desc", HashMap.class, args, 0, -1);*/
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		if("paramCombine".equals(sort)){
    			sort="d.name";
    		}
    		
    		if("interviewStageName".equals(sort)){
    			sort="interviewStageStatus";
    		}
    	}else{
    		sqlSort=" Order by b.interviewStageStatus asc, a.createdDate desc ";
    	}
    	
    	Collection col=super.select("SELECT a.interviewDateId, a.vacancyCode, a.applicantId, a.interviewerId, a.remark, " +
    			"a.createdDate as dateAssigned, b.interviewDateTime, b.interviewStageStatus, d.name, f.shortDesc as positionDesc, g.applicantStatus " +
    	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c, rec_applicant_detail d, " +
				"rec_vacancy_detail e, org_chart_title f, rec_applicant_status g " +
    	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id " +
    	    	"AND a.applicantId=d.applicantId AND a.vacancyCode=e.vacancyCode AND e.positionId=f.code " +
    	    	"AND a.applicantId = g.applicantId AND g.applicantStatus!='Black-Listed' " +
    	    	"AND a.interviewerId=?" +sqlCause +
    	    	sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, args, 0, -1);
    	
    	for(Iterator ite=col.iterator(); ite.hasNext();){
			HashMap map = (HashMap) ite.next();
			
			mapInterviewStageStatus(map);
		
			if(map.get("remark").equals(""))
				map.put("remark", "New");
    		
			map.put("paramCombine", map.get("applicantId") +"/" + map.get("interviewDateId") +"/"+ map.get("name") +"/"+map.get("vacancyCode")+"/"+"yes");
		}
    	
    	return col;
    }
    
    //  sql cause for interviewer remark
    public String findInterviewerRemark(String applicantMisc, String startDate, String endDate){
    	String sqlCause="";
    	
    	if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (d.name LIKE '%" + applicantMisc + "%' OR a.vacancyCode LIKE '%" + applicantMisc + "%' " +
    				" OR f.shortDesc LIKE '%" + applicantMisc + "%')";
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND (a.createdDate BETWEEN " + "'" + startDate + "'" + " AND " + "'" +  endDate + "' " +
    				"OR b.interviewDateTime BETWEEN " + "'" + startDate + " 00:00:00 '" + " AND " + "'" +  endDate + " 23:59:00 ' )";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND (a.createdDate='"+ startDate +"' " +
    				"OR b.interviewDateTime BETWEEN '"+ startDate +"  00:00:00 ' AND '" + startDate + " 23:59:00 ' )";
    	}

    	return sqlCause;
    }
    
    //put the interviewStageStatus Name type HashMap
    public void mapInterviewStageStatus(HashMap map){
    	if(map.get("interviewStageStatus").equals("1"))
			map.put("interviewStageName", "1st Round Interview");
		else if(map.get("interviewStageStatus").equals("2"))
			map.put("interviewStageName", "2nd Round Interview");
		else if(map.get("interviewStageStatus").equals("3"))
			map.put("interviewStageName", "3rd Round Interview");
		else
			map.put("interviewStageName", map.get("interviewStageStatus") +"th Round Interview");
    }
    
    //  count total interviewer -to give remark
    public int countAllInterviewee(String interviewerId, String applicantMisc, String startDate, String endDate) throws DaoException{
    	String sqlCause=findInterviewerRemark(applicantMisc, startDate, endDate);
    	
    	Object[] args = {interviewerId};
    	//last Date modified 26/12/06
    	/*Collection col=super.select("SELECT COUNT(*) AS total "+
    	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c, rec_applicant_detail d, " +
				"rec_vacancy_detail e, org_chart_title f  " +
    	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id " +
    	    	"AND a.applicantId=d.applicantId AND a.vacancyCode=e.vacancyCode AND e.positionId=f.code " +
    	    	"AND a.interviewerId=?"+ sqlCause , HashMap.class, args, 0, 1);*/
    	Collection col=super.select("SELECT COUNT(*) AS total "+
    	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c, rec_applicant_detail d, " +
				"rec_vacancy_detail e, org_chart_title f, rec_applicant_status g  " +
    	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id " +
    	    	"AND a.applicantId=d.applicantId AND a.vacancyCode=e.vacancyCode AND e.positionId=f.code " +
    	    	"AND a.applicantId = g.applicantId AND g.applicantStatus!='Black-Listed' " +
    	    	"AND a.interviewerId=?"+ sqlCause , HashMap.class, args, 0, 1);
    	
    	HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //  lookup specific Interviewee
    public Collection findSpecificInterviewee(String interviewerId ,String interviewDateId,  String applicantId) throws DaoException{
    
    	Object[] args = {interviewerId, interviewDateId, applicantId};
    	
    	Collection col=super.select("SELECT a.remark, a.interviewDateId, a.vacancyCode, a.applicantId, a.interviewerId, a.remark, " +
    			"a.createdDate as dateAssigned, b.interviewDateTime, b.interviewStageStatus, d.name, f.shortDesc as positionDesc, " +
    			"e.noOfPosition, e.noOfPositionOffered  " +
    	    	"FROM rec_interviewer_remark a, rec_interview_date b, security_user c, rec_applicant_detail d, " +
				"rec_vacancy_detail e, org_chart_title f " +
    	    	"WHERE a.interviewDateId=b.interviewDateId AND a.interviewerId=c.id " +
    	    	"AND a.applicantId=d.applicantId AND a.vacancyCode=e.vacancyCode AND e.positionId=f.code " +
    	    	"AND a.vacancyCode=e.vacancyCode " +
    	    	"AND a.interviewerId=? AND a.interviewDateId=? AND a.applicantId=? " , HashMap.class, args, 0, -1);
    	
    	for(Iterator ite=col.iterator(); ite.hasNext();){
			HashMap map = (HashMap) ite.next();
			mapInterviewStageStatus(map);
						
			if(map.get("noOfPositionOffered")!=null && !map.get("noOfPositionOffered").equals(""))
    			map.put("totalPosition", map.get("noOfPositionOffered") + "/" + map.get("noOfPosition"));
    		else
    			map.put("totalPosition", "0" + "/" + map.get("noOfPosition"));
		}
    	
    	return col;
    }
    
    //  Update interview date
    public void updateInterviewerRemark(String interviewerId, String interviewDateId, String applicantId, InterviewObj interviewObj) throws DaoException{
    	super.update("UPDATE rec_interviewer_remark SET remark=? , lastUpdatedBy=?, lastUpdatedDate=? " +
    			" WHERE interviewerId='"+interviewerId+"'"+" AND interviewDateId='"+interviewDateId+"'"+" AND applicantId='"+applicantId+"'"
    			, new Object[]{interviewObj.getRemark(), interviewObj.getLastUpdatedBy(), 
    			interviewObj.getLastUpdatedDate()});
    }
    
    //find job offered interviewee
    public Collection findAllJobOfferedInterviewee(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate, String statusType) throws DaoException{
    	
    	String sqlCause = findAllJobOfferedSqlCause(applicantMisc, startDate, endDate);
    	
    	/*Collection col=super.select("SELECT a.applicantId, a.vacancyCode, a.applicantStatus, a.jobOfferDate, a.jobOfferLetterSent, " +
    			" b.interviewDateId, b.interviewDateTime, b.interviewStageStatus, " +
    			" c.name, e.shortDesc as positionDesc, f.shortDesc as countryDesc, g.shortDesc as departmentDesc " +  
    			" FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, " + 
    			" rec_vacancy_detail d, org_chart_title e, org_chart_country f, org_chart_department g " + 
    			" WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId " +
    			" AND d.positionId=e.code AND d.countryId=f.code AND d.departmentId=g.code " +
    			" AND a.vacancyCode=d.vacancyCode " +
    			" AND a.applicantStatus='Offered' " + sqlCause +
    			" ORDER BY a.jobOfferDate desc " + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);*/
    	
    	//sorting
    	String sqlSort="";
    	if(sort!=null){
    		if("name".equals(sort)){
    			sort="c.name";
    		}
    		
    		if("jobOfferLetterSentStatus".equals(sort)){
    			sort="a.jobOfferLetterSent";
    		}
    			
    	}else{
    		sqlSort=" ORDER BY a.jobOfferDate desc ";
    	}
    	
    	Collection col=super.select("SELECT a.applicantId, a.vacancyCode, a.applicantStatus, a.jobOfferDate, a.jobOfferLetterSent, " +
    			" c.name, e.shortDesc as positionDesc, f.shortDesc as countryDesc, g.shortDesc as departmentDesc " +  
    			" FROM rec_applicant_status a, rec_applicant_detail c, " + 
    			" rec_vacancy_detail d, org_chart_title e, org_chart_country f, org_chart_department g " + 
    			" WHERE a.applicantId=c.applicantId " +
    			" AND d.positionId=e.code AND d.countryId=f.code AND d.departmentId=g.code " +
    			" AND a.vacancyCode=d.vacancyCode " +
    			" AND a.applicantStatus IN ("+statusType+")" + sqlCause +
    			sqlSort + JdbcUtil.getSort(sort, desc), HashMap.class, null, start, rows);
    	
    	for(Iterator ite=col.iterator(); ite.hasNext();){
			HashMap map = (HashMap) ite.next();
			if(map.get("jobOfferLetterSent").equals("0"))
				map.put("jobOfferLetterSentStatus", "No");
			else
				map.put("jobOfferLetterSentStatus", "Yes");
    	}	
    	
    	return col;
    }
    
    //  sql cause for job offered interviewee
    public String findAllJobOfferedSqlCause(String applicantMisc, String startDate, String endDate){
    	String sqlCause="";
    	
    	if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (c.name LIKE '%" + applicantMisc + "%' OR a.vacancyCode LIKE '%" + applicantMisc + "%' " +
    				" OR e.shortDesc LIKE '%" + applicantMisc + "%' OR f.shortDesc LIKE '%" + applicantMisc + "%' " +
    				" OR g.shortDesc LIKE '%" + applicantMisc + "%')";
    	}
    	
    	if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND a.jobOfferDate BETWEEN " + "'" + startDate + "'" + " AND " + "'" +  endDate + "'"; 
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND a.jobOfferDate='"+ startDate +"'";
    	}
    
    	return sqlCause;
    }
    
    //  count total job offered interviewee
    public int countAllJobOfferedInterviewee(String applicantMisc, String startDate, String endDate, String statusType) throws DaoException{
    	String sqlCause = findAllJobOfferedSqlCause(applicantMisc, startDate, endDate);
    	
    	Collection col=super.select("SELECT COUNT(*) AS total " +  
    			" FROM rec_applicant_status a, rec_interview_date b, rec_applicant_detail c, " + 
    			" rec_vacancy_detail d, org_chart_title e, org_chart_country f, org_chart_department g " + 
    			" WHERE a.applicantId=b.applicantId AND a.applicantId=c.applicantId " +
    			" AND d.positionId=e.code AND d.countryId=f.code AND d.departmentId=g.code " +
    			" AND a.vacancyCode=d.vacancyCode " +
    			" AND a.applicantStatus IN ("+statusType+")" + sqlCause , HashMap.class, null, 0, 1);
    	
    		HashMap map = (HashMap) col.iterator().next();
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //lookup single job Offered status
    public Collection lookUpApplicantOfferedStatus(String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	Collection lookUpApplicantStatusCol=super.select("SELECT a.applicantStatusId, a.vacancyCode, a.applicantId, a.dateApplied, a.applicantStatus, a.jobOfferDate, " +
    			" a.jobOfferLetterSent, a.jobOfferRemark, a.totalInterview, a.jobOfferAdminRemark, b.createdBy, " +
    			" c.shortDesc as positionDesc, d.shortDesc as countryDesc, " +
    			" e.shortDesc as departmentDesc, f.name " +
    			" FROM rec_applicant_status a, " +
    			" rec_vacancy_detail b, org_chart_title c, org_chart_country d, org_chart_department e, rec_applicant_detail f " +
    			" WHERE a.vacancyCode=b.vacancyCode AND b.positionId=c.code AND b.countryId=d.code AND b.departmentId=e.code " +
    			" AND a.applicantId=f.applicantId AND a.applicantId=? " , HashMap.class, args, 0, 1);
    	
    	return lookUpApplicantStatusCol;
    }
    
    //lookup education details
    public Collection lookUpEducationDetail(String sort, boolean desc, int start, int rows, String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	Collection lookUpEducationDetailCol=super.select("SELECT institute, highEduLevel, courseTitle, startDate , endDate, grade " +
    			"FROM rec_applicant_education " +
    			"WHERE applicantId=? ", ApplicantObj.class, args, start, rows);
    	
    	return lookUpEducationDetailCol;
    }
    
    //  lookup skill details
    public Collection lookUpSkillDetail(String sort, boolean desc, int start, int rows, String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	Collection lookUpSkillDetailCol=super.select("SELECT skill, yearOfExpSkill, proficiency " +
    			"FROM rec_applicant_skill " +
    			"WHERE applicantId=? ", ApplicantObj.class, args, start, rows);
    	
    	return lookUpSkillDetailCol;
    }
    
    //  lookup language details
    public Collection lookUpLanguageDetail(String sort, boolean desc, int start, int rows, String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	Collection lookUpLanguageDetailCol=super.select("SELECT language, spoken, written " +
    			"FROM rec_applicant_language " +
    			"WHERE applicantId=? ", ApplicantObj.class, args, start, rows);
    	
    	return lookUpLanguageDetailCol;
    }
    
    //  lookup working experience details
    public Collection lookUpWorkingExpDetail(String sort, boolean desc, int start, int rows, String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	Collection lookUpWorkingExpDetailCol=super.select("SELECT companyName, positionName, positionLvl, startDate, endDate, reasonForLeave " +
    			"FROM rec_applicant_employment " +
    			"WHERE applicantId=? ", ApplicantObj.class, args, start, rows);
    	
    	return lookUpWorkingExpDetailCol;
    }
    
    //lookup interviewer(s) based on interviewDateId-email usage
    public Collection lookUpInterviewer(String interviewDateId) throws DaoException{
    	Object[] args = {interviewDateId};
    	Collection lookUpInterviewerCol=super.select("SELECT interviewDateId, vacancyCode, applicantId, interviewerId, remark, createdBy, createdDate, " +
    			" lastUpdatedBy, lastUpdatedDate " +
    			" FROM rec_interviewer_remark " +
    			" WHERE interviewDateId=? " , HashMap.class, args, 0, -1);
    	
    	return lookUpInterviewerCol;
    }
    
    //find interview date ONLY-with interview Date id
    public Collection lookUpInterviewDateId(String interviewDateId) throws DaoException{
    	Object[] args = {interviewDateId};
    	Collection lookUpInterviewDateIdCol=super.select("SELECT interviewDateId, applicantId, interviewDateTime, interviewStageStatus " +
    			" FROM rec_interview_date " +
    			" WHERE interviewDateId=? " , HashMap.class, args, 0, 1);
    	
    	HashMap map = (HashMap)lookUpInterviewDateIdCol.iterator().next();
    	mapInterviewStageStatus(map);
    	
    	return lookUpInterviewDateIdCol;
    }
    
    //find interview date ONLY-with applicantid
    public HashMap findInterviewDateId(String applicantId) throws DaoException{
    	Object[] args = {applicantId};
    	HashMap map = null;
    	Collection lookUpInterviewDateIdCol=super.select("SELECT interviewDateId, applicantId, interviewDateTime, interviewStageStatus " +
    			" FROM rec_interview_date " +
    			" WHERE applicantId=? " , HashMap.class, args, 0, 1);
    	if(lookUpInterviewDateIdCol.size() > 0){
    		map = (HashMap)lookUpInterviewDateIdCol.iterator().next();
    		mapInterviewStageStatus(map);
    	}
  
    	return map;
    }
    
    //	look up interviewee remark based on interviewdateId	
    public Collection lookUpInterviewRemark(String interviewDateId) throws DaoException{
    	Object[] args = {interviewDateId};
    	/*StringBuffer sb=new StringBuffer();
    	for(int i=0; i<interviewDateId.length;i++){
    		if(i>0 && i<interviewDateId.length)
    			sb.append(" ,");
    		
    		sb.append("'"+interviewDateId[i]+"'");	
    	}*/
    
    	Collection lookUpInterviewRemarkCol=super.select("SELECT interviewDateId, vacancyCode, applicantId, interviewerId, remark, createdBy, createdDate, " +
    			" lastUpdatedBy, lastUpdatedDate " +
    			" FROM rec_interviewer_remark " +
    			" WHERE interviewDateId=? " , HashMap.class, args, 0, -1);
    	
    	return lookUpInterviewRemarkCol;
    }
  
    //find interviewer remark details using interview remark id-primary key
    public Collection lookUpInterviewerRemarkId(String interviewerRemarkId) throws DaoException{
    	Object[] args = {interviewerRemarkId};
    	Collection lookUpInterviewerRemarkIdCol=super.select("SELECT a.interviewDateId, a.vacancyCode, a.applicantId, a.interviewerId, a.remark, a.createdBy, a.createdDate, " +
    			" a.lastUpdatedBy, a.lastUpdatedDate, b.interviewDateTime, b.interviewStageStatus " +
    			" FROM rec_interviewer_remark a , rec_interview_date b " +
    			" WHERE a.interviewDateId=b.interviewDateId AND a.interviewerRemarkId=? " , HashMap.class, args, 0, 1);
    	
    	return lookUpInterviewerRemarkIdCol;
    }
    
    //find interviewerName directly using interview remark id-primary key
    public String lookUpInterviewerIdToName(String interviewerRemarkId) throws DaoException{
    	String interviewerName = "";
    	Application app = Application.getInstance();
    	Object[] args = {interviewerRemarkId};
    	Collection lookUpInterviewRemarkIdCol=super.select("SELECT interviewDateId, vacancyCode, applicantId, interviewerId, remark, createdBy, createdDate, " +
    			" lastUpdatedBy, lastUpdatedDate " +
    			" FROM rec_interviewer_remark " +
    			" WHERE interviewerRemarkId=? " , HashMap.class, args, 0, 1);
    	
    	HashMap map = (HashMap)lookUpInterviewRemarkIdCol.iterator().next();
    	String interviewerId = map.get("interviewerId").toString();
    	
    	
    	HashMap interviewerNameMap = (HashMap)getSecurityUserNameSingle(interviewerId).iterator().next();
    	interviewerName = interviewerNameMap.get("username").toString();
    	
    	return interviewerName;
    }
    
    //load all shortlisted and another interview-setup interviewee date
    public Collection findShortListedAndAnother(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate, 
    		Collection colId, Collection interviewDateColSs, String applicantStatus) throws DaoException{
    	
    	//make applicantStatus into xx,xx
    	String statusName="";
    	if(applicantStatus.contains(",")){
	    	String[] statusType=applicantStatus.split(",");
	    	statusName = "'"+statusType[0]+"'" +", '"+statusType[1]+"'";	
    	}else{
    		statusName="'"+applicantStatus+"'";
    	}
    	
    	//make colId into xx,xx
    	String sqlCause="";
    	int i=0;
    	for(Iterator ite=colId.iterator(); ite.hasNext();){
    		if(i>0 && i<colId.size())
    			sqlCause+=" ,";
    		
    		sqlCause += "'"+ ite.next().toString() +"'";
    		i++;	
    	}
    	
    	//sorting part
    	String sqlSort="";
    	if(sort!=null){
    		if("paramCombine".equals(sort)){
    			sort="b.name";
    		}
    		
    		if("interviewStageName".equals(sort)){
    			sort="b.interviewStageStatus";
    		}
    	}else{
    		sqlSort=" ORDER BY b.name desc ";
    	}
    	
    	Collection col = new ArrayList();
    	if(colId!=null && colId.size() > 0){
	    	String sqlFinder=findInterviewDateTimeCause(applicantMisc, startDate, endDate);
	    	
	    	col=super.select(" SELECT a.vacancyCode, a.applicantId, a.applicantStatus, b.name, " +
		    	" d.shortDesc as positionDesc " +
		    	" FROM rec_applicant_status a, rec_applicant_detail b, " +
		    	" rec_vacancy_detail c, org_chart_title d " + 
		    	" WHERE a.applicantId=b.applicantId " +
		    	" AND a.vacancyCode=c.vacancyCode AND c.positionId=d.code " +
		    	" AND a.applicantStatus IN ("+ statusName +") " + 
		    	" AND a.applicantId IN ("+ sqlCause +") "  + sqlFinder + sqlSort +  JdbcUtil.getSort(sort, desc)
		    	, HashMap.class, null, start, rows);
	    	
	    	for(Iterator ite = col.iterator(); ite.hasNext();){
	    		HashMap map = (HashMap) ite.next();
	    		String strApplicantId = map.get("applicantId").toString();
	    		
	    		//1st time put-dun care
	    		map.put("interviewDateTime", "-");
	    		
	    		if(interviewDateColSs!=null && interviewDateColSs.size() > 0){
		    		for(Iterator iter=interviewDateColSs.iterator();iter.hasNext();){
		    			InterviewObj iObj= (InterviewObj)iter.next();
		 
		    			if(iObj.getApplicantId()[0].equals(strApplicantId)){
		    				map.put("interviewDateTime", iObj.getInterviewDateTime());
		    			}
		    		}
	    		}
	    		
	    	/*	if(findInterviewDateId(strApplicantId)!=null && !findInterviewDateId(strApplicantId).equals(""))
	    			map.put("interviewDateTime", findInterviewDateId(strApplicantId).get("interviewDateTime"));
	    		else
	    			map.put("interviewDateTime", "-");*/
	    		
	    		map.put("paramCombine", map.get("applicantId") +"/" + map.get("name"));
	    	}
    	}
    	
    	return col;
    }
    
    //count all shortlisted and another interview-setup interviewee date
    //  count total re-schedule
    public int countAllShortListedAndAnother(String applicantMisc, String startDate, String endDate, Collection colId, 
    		Collection interviewDateColSs, String applicantStatus) throws DaoException{
    	
    	 String sqlFinder=findInterviewDateTimeCause(applicantMisc, startDate, endDate);
    	
    	//make applicantStatus into xx,xx
    	String statusName="";
    	if(applicantStatus.contains(",")){
	    	String[] statusType=applicantStatus.split(",");
	    	statusName = "'"+statusType[0]+"'" +", '"+statusType[1]+"'";	
    	}else{
    		statusName="'"+applicantStatus+"'";
    	}
    	
    	//sorting part
    	String sqlCause="";
    	int i=0;
    	for(Iterator ite=colId.iterator(); ite.hasNext();){
    		if(i>0 && i<colId.size())
    			sqlCause+=" ,";
    		
    		sqlCause += "'"+ ite.next().toString() +"'";
    		i++;	
    	}
    	
    	Collection col=new ArrayList();
    	HashMap map = new HashMap();
    	if(colId!=null && colId.size() > 0){
    		col=super.select(" SELECT  COUNT(*) AS total " +
    	    	" FROM rec_applicant_status a, rec_applicant_detail b, " +
    	    	" rec_vacancy_detail c, org_chart_title d " + 
    	    	" WHERE a.applicantId=b.applicantId " +
    	    	" AND a.vacancyCode=c.vacancyCode AND c.positionId=d.code " +
    	    	" AND a.applicantStatus IN ("+ statusName +") " + 
    	    	" AND a.applicantId IN ("+ sqlCause +") "  + sqlFinder 
    	    	, HashMap.class, null, 0, 1);
    		
    	 	map = (HashMap) col.iterator().next();
    	}else
    		map.put("total", 0);
    	
    	return Integer.parseInt(map.get("total").toString());
    }
    
    //sql cause for setup interviewee Date And Time
    public String findInterviewDateTimeCause(String applicantMisc, String startDate, String endDate){
    	String sqlCause="";
    	
    	if(applicantMisc!=null && !applicantMisc.equals("")){
    		sqlCause +=" AND (b.name LIKE '%" + applicantMisc + "%' OR a.vacancyCode LIKE '%" + applicantMisc + "%' " +
    				" OR d.shortDesc LIKE '%" + applicantMisc + "%' OR a.applicantStatus LIKE '%" + applicantMisc + "%')";
    	}
    	
    	/*if(startDate!=null && endDate!=null && !startDate.equals("") && !endDate.equals("")){
    		sqlCause +=" AND b.interviewDateTime BETWEEN " + "'" + startDate + " 00:00:00 '" + " AND " + "'" +  endDate + " 23:59:00 ' ";
    	}
    	else if(startDate!=null && !startDate.equals("")){
    		sqlCause +=" AND b.interviewDateTime BETWEEN '"+ startDate +"  00:00:00 ' AND '" + startDate + " 23:59:00 '";
    	}*/
    
    	return sqlCause;
    }
    
    public int countByApplicantStatus(String vacancyCode, String applicantStatus) throws DaoException{
    	Object[] args = {vacancyCode, applicantStatus};
    	Collection col=super.select(" SELECT COUNT(applicantStatusId) AS total FROM rec_applicant_status " +
		    	" WHERE vacancyCode=? AND applicantStatus=? ", HashMap.class, args, 0, 1);
    	
		HashMap map = (HashMap) col.iterator().next();
	    
		return Integer.parseInt(map.get("total").toString());
    }
}






















