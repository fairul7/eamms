package com.tms.hr.recruit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class RecruitAppModule extends DefaultModule{
	
	public void init(){
    }
	
	//insert Job Application Personal details
	public void insertJobAppPersonal(ApplicantObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertJobAppPersonal(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//insert Job Application Employement/working experience details
	public void insertJobAppWorkingExp(ApplicantObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertJobAppWorkingExp(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//insert Job Application Education details
	public void insertJobAppEdu(ApplicantObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertJobAppEdu(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//look up Job Application Education details
	public Collection findJobAppEdu(String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findJobAppEdu(applicantId);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of education detail", e);
		}
		return new ArrayList();
	}
	
	//	delete the Job Application education
	public void deleteJobAppEdu(String eduId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.deleteJobAppEdu(eduId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting edu details: "+ eduId, e);
		}
	}
	
	 //find Job Application education particular
	public ApplicantObj findJobAppEduObj(String eduId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findJobAppEduObj(eduId);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of education detail", e);
		}
		return null;
	}
	
	//	find Job Application education Key
	public boolean findJobAppEduKey(String eduId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findJobAppEduKey(eduId);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting eduId ", e);
		}
		return false;
	}
	
	//	insert Job Application Skill details
	public void insertJobAppSkill(ApplicantObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertJobAppSkill(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//	insert Job Application Language details
	public void insertJobAppLanguage(ApplicantObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertJobAppLanguage(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//insert applicant status
	public void insertApplicantStatus(ApplicantObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertApplicantStatus(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//	look up email exist
	public boolean lookUpEmailExist(String strEmail){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpEmailExist(strEmail);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of email ", e);
		}
		return false;
	}
	
	//	look up email exist
	public boolean lookUpNircExist(String strNirc){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpNircExist(strNirc);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of IC/Passport ", e);
		}
		return false;
	}
	
	//look up vacancy total
	public Collection lookUpVacancyTotal(String vacancyCode){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpVacancyTotal(vacancyCode);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of vacancy total", e);
		}
		return new ArrayList();
	}
	
	//look up applicant status
	public Collection lookUpApplicantStatus(String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpApplicantStatus(applicantId);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of applicant status", e);
		}
		return new ArrayList();
	}
	
	//	look up applicant status
	public Collection lookUpApplicantStatus(String[] applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpApplicantStatus(applicantId);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of applicant status", e);
		}
		return new ArrayList();
	}
	
	//update vacancy total
	public void updateVacancyTotal(VacancyObj vacancyObj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateVacancyTotal(vacancyObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating vacancy total ", e);
		}
	}
		
	//	get the particular applicant personal details
	public Collection loadApplicantPersonal(String applicantId) {
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.loadApplicantPersonal(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error finding applicant personal details " + applicantId, e);
			//throw new VacancyTempFormException(e.toString());
		}
		return new ArrayList();
	}
	
	//	find all vacancy listing
	public Collection findAllApplicantSpecial(String vacancyCode, String sort, boolean desc, int start, int rows, String applicantMisc,  String strApplicantStatus,
			String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findAllApplicantSpecial(vacancyCode, sort, desc, start, rows, applicantMisc, strApplicantStatus, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of applicant detail", e);
		}
		return new ArrayList();
	}
	
	//	find all vacancy listing
	public Collection findAllApplicant(String vacancyCode, String sort, boolean desc, int start, int rows, String applicantMisc,  String strApplicantStatus,
			String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findAllApplicant(vacancyCode, sort, desc, start, rows, applicantMisc, strApplicantStatus, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of applicant detail", e);
		}
		return new ArrayList();
	}
	
	//	count all applicant
	public int countAllApplicantSpecial(String vacancyCode,  String applicantMisc,  String strApplicantStatus, String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllApplicantSpecial(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting applicant ", e);
		}
		return 0;
	}
	
	//	count all applicant
	public int countAllApplicant(String vacancyCode,  String applicantMisc,  String strApplicantStatus, String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllApplicant(vacancyCode, applicantMisc, strApplicantStatus, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting applicant ", e);
		}
		return 0;
	}
	
	//	update vacancy total
	public void updateApplicantStatus(ApplicantObj applicantObj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateApplicantStatus(applicantObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating applicant status ", e);
		}
	}
	
	// delete all applicant details
	public void deleteAllApplicantDetail(String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.deleteAllApplicantDetail(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting applicant details: "+ applicantId, e);
		}
	}
	
	//load all shortlisted applicant id
	public Collection loadShortlisted(String[] applicantId, String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.loadShortlisted(applicantId, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of applicant id", e);
		}
		return new ArrayList();
	}
	
	//	insert applicant status
	public void insertInterviewDate(InterviewObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertInterviewDate(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting interview date", e);
		}
	}
	
	
//	find interview date
	public Collection findInterviewDateWithStatus(String[] applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findInterviewDateWithStatus(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of applicant interview Date and time ", e);
		}
		return new ArrayList();
	}
	
	//	find interview date
	public Collection findInterviewDate(String[] applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findInterviewDate(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of applicant status", e);
		}
		return new ArrayList();
	}
	
   //update interview date
	public void updateInterviewDate(InterviewObj interviewObj, String[] interviewStageStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateInterviewDate(interviewObj, interviewStageStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating interview stage status ", e);
		}
	}
	
	//Update interview date with interviewDateId - re-schedule interviewDateTime
	public void updateInterviewDateTime(InterviewObj interviewObj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateInterviewDateTime(interviewObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating interview Date and Time for re-schedule ", e);
		}
	}
	
	//lookup interviewDate
	public boolean validateHasInterviewDate(String[] applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.validateHasInterviewDate(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting applicant id", e);
		}
		return false;
	}
	
	//	find all applicant scheduled for interview
	public Collection findAllInterviewee(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findAllInterviewee(sort, desc, start, rows, applicantMisc, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail", e);
		}
		return new ArrayList();
	}
	
	//	count all interviewee
	public int countAllInterviewee(String applicantMisc, String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllInterviewee(applicantMisc, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail ", e);
		}
		return 0;
	}
	
	//	 delete all interview Date
	public void deleteInterviewDate(String selectedKey){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.deleteInterviewDate(selectedKey);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting interview date: "+ selectedKey, e);
		}
	}

    //delete interview remark details
	public void deleteInterviewRemark(String selectedKey){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.deleteInterviewRemark(selectedKey);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting interview remark detail: "+ selectedKey, e);
		}
	}	
	
	//find all selected interviewee details
	public Collection findSelectedInterviewee(String sort, boolean desc, int start, int rows, Collection col, String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findSelectedInterviewee(sort, desc, start, rows, col, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail", e);
		}
		return new ArrayList();
	}
	
	 //	find all re-schedule interviewee
	public Collection findReScheduleInterviewee(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate
			, Collection col, String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findReScheduleInterviewee(sort, desc, start, rows, applicantMisc, startDate, endDate, col, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of re-scheduled interviewee detail", e);
		}
		return new ArrayList();
	}
	
	//  count total re-schedule
	public int countAllReSchedule(String applicantMisc, String startDate, String endDate, Collection col, String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllReSchedule(applicantMisc, startDate, endDate, col, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of re-scheduled interviewee detail ", e);
		}
		return 0;
	}
	
	//	insert interview remark
	public void insertInterviewer(Collection intervieweeCol, InterviewObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertInterviewer(intervieweeCol, obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting interviewer ", e);
		}
	}
	
	//	insert interview remark date
	public void insertInterviewerRemark(InterviewObj obj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.insertInterviewerRemark(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting interviewer ", e);
		}
	}
	
	//	get the security user name (interviewer name) using id
    public Collection getSecurityUserNameSingle(String id){
    	RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.getSecurityUserNameSingle(id);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewer name", e);
		}
		return new ArrayList();
    }
	
	
	//	get the security user name (interviewer name) using interviewDateId
    public Collection getSecurityUserName(String interviewDateId){
    	RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.getSecurityUserName(interviewDateId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewer name", e);
		}
		return new ArrayList();
    }
	
    //update applicant status- applicant result
	public void updateApplicantStatusResult(ApplicantObj applicantObj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateApplicantStatusResult(applicantObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating applicant result ", e);
		}
	}
	
	 //update applicant Offer status- applicant result
	public void updateApplicantOfferStatusResult(ApplicantObj applicantObj, boolean updateStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateApplicantOfferStatusResult(applicantObj, updateStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating applicant result ", e);
		}
	}
	
	//	find all selected interviewee remark details- from rec_interviewer_remark
	public Collection findAllIntervieweeRemark(String sort, boolean desc, int start, int rows, String applicantId, String interviewDateId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findAllIntervieweeRemark(sort, desc, start, rows, applicantId, interviewDateId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee remark", e);
		}
		return new ArrayList();
	}
	
	//	find all Interviewer(s) to give remark
	public Collection findInterviewer(String sort, boolean desc, int start, int rows, String interviewerId,  String applicantMisc, String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findInterviewer(sort, desc, start, rows, interviewerId, applicantMisc, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewer detail", e);
		}
		return new ArrayList();
	}
	
	//	count all interviewer(s) to give remark
	public int countFindInterviewer(String interviewerId, String applicantMisc, String startDate, String endDate){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllInterviewee(interviewerId, applicantMisc, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail ", e);
		}
		return 0;
	}
	
	//  lookup specific Interviewee
	public Collection findSpecificInterviewee(String interviewerId ,String interviewDateId,  String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findSpecificInterviewee(interviewerId, interviewDateId, applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail", e);
		}
		return new ArrayList();
	}
	
	//	update interviewer Remark
	public void updateInterviewerRemark(String interviewerId, String interviewDateId, String applicantId, InterviewObj interviewObj){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			dao.updateInterviewerRemark(interviewerId, interviewDateId, applicantId, interviewObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating applicant result ", e);
		}
	}
	
	//	find job offered interviewee
	public Collection findAllJobOfferedInterviewee(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate, String statusType){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findAllJobOfferedInterviewee(sort, desc, start, rows, applicantMisc, startDate, endDate, statusType);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail for offered status", e);
		}
		return new ArrayList();
	}
	
	//	count all job offered interviewee
	public int countAllJobOfferedInterviewee(String applicantMisc, String startDate, String endDate, String statusType){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllJobOfferedInterviewee(applicantMisc, startDate, endDate, statusType);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interviewee detail for offered status", e);
		}
		return 0;
	}
	
	 //lookup single job Offered status
	public Collection lookUpApplicantOfferedStatus(String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpApplicantOfferedStatus(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting applicant status detail", e);
		}
		return new ArrayList();
	}
	
	 //lookup education details
	public Collection lookUpEducationDetail(String sort, boolean desc, int start, int rows, String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpEducationDetail(sort, desc, start, rows, applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list applicant education", e);
		}
		return new ArrayList();
	}
	
	 //lookup skill details
	public Collection lookUpSkillDetail(String sort, boolean desc, int start, int rows, String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpSkillDetail(sort, desc, start, rows, applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list applicant skill", e);
		}
		return new ArrayList();
	}
	
	 //lookup language details
	public Collection lookUpLanguageDetail(String sort, boolean desc, int start, int rows, String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpLanguageDetail(sort, desc, start, rows, applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list applicant language", e);
		}
		return new ArrayList();
	}
	
	 //lookup workingExp details
	public Collection lookUpWorkingExpDetail(String sort, boolean desc, int start, int rows, String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpWorkingExpDetail(sort, desc, start, rows, applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list applicant working experience", e);
		}
		return new ArrayList();
	}
	
	//lookup interviewer(s) based on interviewDateId-email usage
	public Collection lookUpInterviewer(String interviewDateId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpInterviewer(interviewDateId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting interview date id", e);
		}
		return new ArrayList();
	}
	
	//lookup interview date-particular
	public Collection lookUpInterviewDateId(String interviewDateId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpInterviewDateId(interviewDateId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting interview date ", e);
		}
		return new ArrayList();
	}
	
	//look up interviewee remark based on interviewdateId
	public Collection lookUpInterviewRemark(String interviewDateId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpInterviewRemark(interviewDateId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting interview date id", e);
		}
		return new ArrayList();
	}
	
	//	find interviewer remark details using interview remark id-primary key
	public Collection lookUpInterviewerRemarkId(String interviewerRemarkId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpInterviewerRemarkId(interviewerRemarkId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting interviewer details", e);
		}
		return new ArrayList();
	}
	
	
    //find interviewerName directly using interview remark id-primary key
	public String lookUpInterviewerIdToName(String interviewerRemarkId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.lookUpInterviewerIdToName(interviewerRemarkId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting interviewer name", e);
		}
		
		return "";
	}
	
    //load all shortlisted and another interview-setup interviewee date
	public Collection findShortListedAndAnother(String sort, boolean desc, int start, int rows, String applicantMisc, String startDate, String endDate
			, Collection col, Collection interviewDateColSs, String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findShortListedAndAnother(sort, desc, start, rows, applicantMisc, startDate, endDate, col, interviewDateColSs, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of selected applicant", e);
		}
		return new ArrayList();
	}
	

	//  count total re-schedule
	public int countAllShortListedAndAnother(String applicantMisc, String startDate, String endDate, Collection col, Collection interviewDateColSs, 
			String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countAllShortListedAndAnother(applicantMisc, startDate, endDate, col, interviewDateColSs, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of selected applicant ", e);
		}
		return 0;
	}
	
	//find interview date ONLY-with applicantid
	public HashMap findInterviewDateId(String applicantId){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.findInterviewDateId(applicantId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of interview date ", e);
		}
		return null;
	}
	
	//	count all applicant Status
	public int countByApplicantStatus(String vacancyCode, String applicantStatus){
		RecruitAppDao dao = (RecruitAppDao) getDao();
		try{
			return dao.countByApplicantStatus(vacancyCode, applicantStatus);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting applicant status ", e);
		}
		return 0;
	}
	
}

















