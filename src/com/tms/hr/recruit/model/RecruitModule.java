package com.tms.hr.recruit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.util.Log;

public class RecruitModule extends DefaultModule{
    
	public void init(){
    }
	
	//	insert audit 
	public void insertAudit(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.insertAudit(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting audit", e);
		}
	}
	
	//	insert vacancy Hit 
	public void insertVacancyHit(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.insertVacancyHit(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting vacancy hit", e);
		}
	}
	
	//	find all audit 
	public Collection findAllAudit(String sort, boolean desc, int start, int rows, String auditMisc, String startDate, String endDate){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.findAllAudit(sort,desc,start,rows, auditMisc,startDate, endDate);
		}catch(DaoException e){
			 Log.getLog(getClass()).debug("error getting list of audit", e);
		}
		return new ArrayList();
	}
	
	//	count all audit
	public int countAllAudit(String auditMisc, String startDate, String endDate){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.countAllAudit(auditMisc, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting audit", e);
		}
		return 0;
	}
	
	//	delete selected audit
	public void deleteAudit(String auditId){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.deleteAudit(auditId);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting audit trail: "+ auditId, e);
		}
	}
	
	//validate who is the HOD
	public boolean validateHod(String userId) throws DaoException{
		RecruitDao dao = (RecruitDao) getDao();
        return dao.validateHod(userId);
	}
	
	//insert vacancy Template
	public void insertVacancyTemp(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.insertVacancyTemp(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting ", e);
		}
	}
	
	//update vacancy Template
	public void updateVacancyTemp(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.updateVacancyTemp(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating vacancy template", e);
		}
	}
	
	//update vacancy
	public void updateVacancy(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.updateVacancy(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating vacancy", e);
		}
	}
	
	//update Vacancy noOfPositionOffered
	public void updateVacancyPositionOffered(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.updateVacancyPositionOffered(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating vacancy", e);
		}
	}
	
	//insert vacancy Code
	public void insertVacancy(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.insertVacancy(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting vacancy", e);
		}
	}
	
	//make sure Code is unqiue
	public boolean codeExist(String code, String type) throws DaoException{
		RecruitDao dao = (RecruitDao) getDao();
		return dao.codeExist(code, type);
	}
	
	//find all vacancy template listing 
	public Collection findAllVacancyTempCodeFilter(String vacancyTempCode, String sort, boolean desc, int start, int rows, String strCountry, String strDept, String strTitle){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.findAllVacancyTempCodeFilter(vacancyTempCode,sort,desc,start,rows, strCountry,strDept, strTitle);
		}catch(DaoException e){
			 Log.getLog(getClass()).debug("error getting list of vacancy template", e);
		}
		return new ArrayList();
	}
	
	//find all vacancy listing
	public Collection findAllVacancyCodeFilter(String vacancyCode, String sort, boolean desc, int start, int rows, String priority, String startDate, String endDate){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.findAllVacancyCodeFilter(vacancyCode, sort, desc, start, rows, priority, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of vacancy", e);
		}
		return new ArrayList();
	}
	
	//list some vacancy details
	public Collection listAllVacancy(){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.listAllVacancy();
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of vacancy", e);
		}
		return new ArrayList();
	}
	
	//list all position inserted
	public Collection lookUpPosition(String vacancyCode){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.lookUpPosition(vacancyCode);
		}catch(Exception e){
			Log.getLog(getClass()).debug("error getting list of position", e);
		}
		return new ArrayList();
	}
	
	//count all vacancy template
	public int countAllVacancyTemp(String vacancyTempCode, String strCountry, String strDept, String strTitle){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.countAllVacancyTemp(vacancyTempCode, strCountry, strDept, strTitle);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting vacancy template", e);
		}
		return 0;
	}
	
	//count all vacancy
	public int countAllVacancy(String vacancyCode, String priority, String startDate, String endDate){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.countAllVacancy(vacancyCode, priority, startDate, endDate);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting vacancy ", e);
		}
		return 0;
	}
	
	//delete selected vacancy template
	public void deleteVacancyTemp(String vacancyTempCode){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.deleteVacancyTemp(vacancyTempCode);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting vacancy template: "+ vacancyTempCode, e);
		}
	}
	
	//delete selected vacancy
	public void deleteVacancyAndApplicant(String vacancyCode){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.deleteVacancyAndApplicant(vacancyCode);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error deleting vacancy: "+ vacancyCode, e);
		}
	}
	
	//get the particular vacancy template details
	public VacancyObj loadVacancyTemp(String vacancyTempCode) throws DataObjectNotFoundException{
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.loadVacancyTemp(vacancyTempCode);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error finding vacancy template " + vacancyTempCode, e);
			throw new VacancyTempFormException(e.toString());
		}
	}
	
	//get the particular vacancy details
	public VacancyObj loadVacancy(String vacancyCode, String TYPE_USAGE) throws DataObjectNotFoundException{
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.loadVacancy(vacancyCode, TYPE_USAGE);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error finding vacancy " + vacancyCode, e);
			throw new VacancyTempFormException(e.toString());
		}
	}
	
	//get the particular setup message body details
	public VacancyObj loadMessagebody() throws DataObjectNotFoundException{
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.loadMessagebody();
		}catch(DaoException e){
			Log.getLog(getClass()).error("Global Setup module not found" + e);
			throw new VacancyTempFormException(e.toString());
		}
	}
	
	public void insertSetupMessageBody(VacancyObj smbObj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.insertSetupMessageBody(smbObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error inserting Global Setup ", e);
		}
	}
	
	//update setup message body
	public void updateSetupMessageBody(VacancyObj smbObj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.updateSetupMessageBody(smbObj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating Global Setup", e);
		}
	}
	
	//get all vacancyTempCode value for selectbox
	public Collection findALLVacancyTempCode(String shortDesc, int start, int rows, String sort, boolean desc){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.findALLVacancyTempCode(shortDesc, start, rows, sort, desc);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error getting all" , e);
		}
		return null;
	}
	
	 //  get the particular vacancy detail
	public Collection findSpecificVacancy(String vacancyCode){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.findSpecificVacancy(vacancyCode);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getting vacancy detail" + e);
		}
		
		return new ArrayList();
	}
	
	//  update particular vacancy detail
	public void updateSpecificVacancy(VacancyObj obj){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			dao.updateSpecificVacancy(obj);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("Error updating vacancy details", e);
		}
	}
	
	// lookup vacancy total report
	public Collection findAllVacancyTotal(String sort, boolean desc, int start, int rows, String strPosition, String strCountry, String strDepartment, 
			String vacancyMisc, String startDate, String endDate, String vacancyCode){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.findAllVacancyTotal(sort, desc, start, rows, strPosition, strCountry, strDepartment, vacancyMisc, startDate, endDate, vacancyCode);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error getting list of vacancy total", e);
		}
		return new ArrayList();
	}
	
	//count vacancy total report
	public int countAllVacancyTotal(String strPosition, String strCountry, String strDepartment, String vacancyMisc, String startDate, String endDate, String vacancyCode){
		RecruitDao dao = (RecruitDao) getDao();
		try{
			return dao.countAllVacancyTotal(strPosition, strCountry, strDepartment, vacancyMisc, startDate, endDate, vacancyCode);
		}catch(DaoException e){
			Log.getLog(getClass()).debug("error counting vacancy total", e);
		}
		return 0;
	}
	
}




























