package com.tms.fms.engineering.model;

import java.util.Collection;

import kacang.model.DefaultModule;

public class AssignmentLogModule extends DefaultModule {
	public void addLog(AssignmentLog assign) {
		AssignmentLogDao dao = (AssignmentLogDao) getDao();
		dao.insertLog(assign);
	}
	
	public Collection getLog(String assignmentId) {
		AssignmentLogDao dao = (AssignmentLogDao) getDao();
		return dao.selectLog(assignmentId);
	}
	
	public boolean searchDriverLog(String assignmentId) {
		AssignmentLogDao dao = (AssignmentLogDao) getDao();
		return dao.searchDriverLog(assignmentId);
	}
	
	public void updateDriverLog(AssignmentLog log) {
		AssignmentLogDao dao = (AssignmentLogDao) getDao();
		dao.updateDriverLog(log);
	}
	
	public String getDriver(String assignmentId){
		AssignmentLogDao dao = (AssignmentLogDao) getDao();
		 return dao.selectDriver(assignmentId);
	}
}
