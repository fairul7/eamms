package com.tms.collab.isr.model;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.UuidGenerator;

public class LogDao extends DataSourceDao {
	public void init() {
		try {
			super.update("CREATE TABLE isr_request_log (" +
					"logId varchar(255) NOT NULL," +
					"requestId varchar(255) NOT NULL," +
					"logAction varchar(255)," +
					"logDescription mediumtext DEFAULT ''," +
					"dateCreated datetime," +
					"createdBy varchar(255)," +
					"PRIMARY KEY(logId))", null);
		}
		catch(DaoException e) {
			// do nothing
		}
	}
	
	public void insertLog(LogObject log) throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO isr_request_log (" +
				"logId, requestId, logAction, logDescription, dateCreated, createdBy) VALUES (" +
				"#logId#, #requestId#, #logAction#, #logDescription#, " +
				"now(), #createdBy#)");
		
		User user = Application.getInstance().getCurrentUser();
        String userName = user.getName();
        if("".equals(log.getCreatedBy())) {
        	log.setCreatedBy(userName);
        }
        if("".equals(log.getLogId())) {
        	String newId = UuidGenerator.getInstance().getUuid();
        	log.setLogId(newId);
        }
        
        super.update(sql.toString(), log);
	}
}
