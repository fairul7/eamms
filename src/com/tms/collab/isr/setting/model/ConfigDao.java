package com.tms.collab.isr.setting.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.Transaction;
import kacang.util.UuidGenerator;

public class ConfigDao extends DataSourceDao {
	public void init() {
		try{
			super.update("CREATE TABLE isr_config_detail (" +
            		"configDetailId varchar(255), " +
            		"configDetailName varchar(255), " +
            		"configDetailDescription mediumtext , " +
            		"configDetailType varchar(255), " +
            		"configDetailOrder int(2) DEFAULT 1, " +
            		"dateCreated datetime, " +
            		"createdBy varchar(255), " +
            		"lastUpdatedDate datetime, " +
            		"lastUpdatedBy varchar(255), " +
                    "PRIMARY KEY(configDetailId))", null);
		} 
		catch(DaoException e){
			// do nothing
		}
		
		try{
			super.update("CREATE TABLE isr_status (" +
            		"statusId varchar(2), " +
            		"statusName varchar(50), " +
            		"statusOrder int(2) NOT NULL, " +
                    "PRIMARY KEY(statusId))", null);
		} 
		catch(DaoException e){
			// do nothing
		}
		
		try{
			Application app = Application.getInstance();
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'1', '" + app.getMessage("isr.label.statusNew", "New") + "', 1)", null);
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'2', '" + app.getMessage("isr.label.statusReopen", "Reopen") + "', 2)", null);
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'3', '" + app.getMessage("isr.label.statusInProgress", "In Progress") + "', 4)", null);
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'4', '" + app.getMessage("isr.label.statusResolved", "Resolved") + "', 5)", null);
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'5', '" + app.getMessage("isr.label.statusClosed", "Closed") + "', 6)", null);
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'6', '" + app.getMessage("isr.label.statusWithdrawn", "Withdrawn") + "', 7)", null);
			super.update("INSERT INTO isr_status (" +
					"statusId, statusName, statusOrder) VALUES (" +
					"'7', '" + app.getMessage("isr.label.statusClarification", "Clarification") + "', 3)", null);
		} 
		catch(DaoException e){
			// do nothing
		}
		
		try{
			super.update("CREATE TABLE isr_email_setting (" +
					"emailFor varchar(255) NOT NULL, " +
					"emailSubject varchar(255), " +
					"emailBody mediumtext)", null);
		} 
		catch(DaoException e){
			// do nothing
		}
		
		// add new column - notifyMethod into isr_emailSetting
		try {
			super.update("ALTER TABLE isr_email_setting ADD notifyMethod char(1) DEFAULT 'b'", null);
		} catch (DaoException e) {
		}
	}
	
	public void insertConfigDetail(ConfigDetailObject configDetail) throws DaoException {
    	String configDetailSql = "INSERT INTO isr_config_detail (" +
    			"configDetailId, " +
    			"configDetailName, " +
    			"configDetailDescription, " +
    			"configDetailType, " +
    			"configDetailOrder, " +
    			"dateCreated, " +
    			"createdBy, " +
    			"lastUpdatedDate, " +
    			"lastUpdatedBy) VALUES (" +
    			"#configDetailId#, #configDetailName#, #configDetailDescription#, #configDetailType#, #configDetailOrder#, " +
                "now(), #createdBy#, now(), #lastUpdatedBy#)";
    	
    	User user = Application.getInstance().getCurrentUser();
        String userId = user.getId();
        if("".equals(configDetail.getConfigDetailId())) {
    		String newId = UuidGenerator.getInstance().getUuid();
    		configDetail.setConfigDetailId(newId);
    	}
        if("".equals(configDetail.getCreatedBy())) {
        	configDetail.setCreatedBy(userId);
        }
        if("".equals(configDetail.getLastUpdatedBy())) {
        	configDetail.setLastUpdatedBy(userId);
        }
        
        super.update(configDetailSql, configDetail);
    }
	
	public void updateConfigDetail(ConfigDetailObject configDetail) throws DaoException {
    	User user = Application.getInstance().getCurrentUser();
        String userId = user.getId();
        if("".equals(configDetail.getLastUpdatedBy())) {
        	configDetail.setLastUpdatedBy(userId);
        }
        
        super.update("UPDATE isr_config_detail SET " +
    			"configDetailName=#configDetailName#, " +
    			"configDetailDescription=#configDetailDescription#, " +
    			"configDetailOrder=#configDetailOrder#, " +
    			"lastUpdatedDate=SYSDATE, " +
    			"lastUpdatedBy=#lastUpdatedBy# " +
    			"WHERE configDetailId=#configDetailId#", configDetail);
    }
	
	public ConfigDetailObject getConfigDetail(String configDetailId) throws DaoException {
        ConfigDetailObject configDetail = null;
        
        Collection col = super.select("SELECT configDetailId, " +
                "configDetailName, " +
                "configDetailDescription, " +
                "configDetailType, " +
                "configDetailOrder, " +
                "dateCreated, " +
                "createdBy, " +
                "lastUpdatedDate, " +
                "lastUpdatedBy " +
                "FROM isr_config_detail " +
                "WHERE configDetailId = ?", ConfigDetailObject.class, new Object[] {configDetailId}, 0, 1);
        
        if(col != null) {
            for(Iterator i=col.iterator(); i.hasNext();) {
                configDetail = (ConfigDetailObject) i.next();
            }
        }
        
        return configDetail;
    }
	
	public Collection getConfigDetailsByType(String configDetailType, String orderBy) throws DaoException {
		String orderByClause = " ORDER BY configDetailOrder ";
		
		if(orderBy != null && !"".equals(orderBy)) {
			orderByClause = " ORDER BY " + orderBy;
		}
		
		Collection priorities = super.select("SELECT configDetailId, " +
                "configDetailName, " +
                "configDetailDescription, " +
                "configDetailType, " +
                "configDetailOrder, " +
                "dateCreated, " +
                "createdBy, " +
                "lastUpdatedDate, " +
                "lastUpdatedBy " +
                "FROM isr_config_detail " +
                "WHERE configDetailType = ? " + orderByClause, ConfigDetailObject.class, new Object[] {configDetailType}, 0, -1);
		
		return priorities;
	}
	
	public String getConfigDetailName(String configDetailId) throws DaoException {
        String configDetailName = null;
        
        Collection col = super.select("SELECT configDetailName " +
                "FROM isr_config_detail " +
                "WHERE configDetailId = ?", HashMap.class, new Object[] {configDetailId}, 0, 1);
        
        if(col != null) {
            for(Iterator i=col.iterator(); i.hasNext();) {
                Map map = (HashMap) i.next();
                configDetailName = map.get("configDetailName").toString();
            }
        }
        
        return configDetailName;
    }
	
	public void deleteConfigDetail(String configDetailId) throws DaoException {
		super.update("DELETE FROM isr_config_detail " +
				"WHERE configDetailId = ?", new Object[] { configDetailId });
    }
	
	public void deleteConfigDetailsByType(String configDetailType) throws DaoException {
		super.update("DELETE FROM isr_config_detail " +
				"WHERE configDetailType = ?", new Object[] {configDetailType});
	}
	
	public int updateEmailSettings(ArrayList emailSettings) throws DaoException{
		int result = 0;
		StringBuffer updateSql = new StringBuffer("UPDATE isr_email_setting SET emailSubject = ?, emailBody = ?, notifyMethod = ? WHERE emailFor = ?");
		StringBuffer createSql = new StringBuffer("INSERT INTO isr_email_setting (emailFor, emailSubject, emailBody, notifyMethod) VALUES (?, ?, ?, ?)");
		
		Transaction tx = null;
		
		try {
			tx = getTransaction();
            tx.begin();
            
            for(int i=0; i<emailSettings.size(); i++){
            	EmailSetting es = new EmailSetting();
            	
            	es = (EmailSetting)emailSettings.get(i);
            	
            	result = tx.update(updateSql.toString(), new Object[]{es.getEmailSubject(), es.getEmailBody(), es.getNotifyMethod(), es.getEmailFor()});
            	if(result == 0){
            		result = tx.update(createSql.toString(), new Object[]{es.getEmailFor(), es.getEmailSubject(), es.getEmailBody(), es.getNotifyMethod()});
            	}
            		
            }
            
            tx.commit();
		}
		catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DaoException(e.toString());
        }
        return result;
		
	}
	
	public Collection selectEmailSettings(String[] emailFor) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT emailFor, emailSubject, emailBody, notifyMethod FROM isr_email_setting WHERE emailFor = ?");
		
		Collection results = new ArrayList();
		
		for(int i=0; i<emailFor.length; i++){
			Collection temp = null;
			temp = super.select(sql.toString(), EmailSetting.class, new Object[]{emailFor[i]}, 0, -1);
			results.addAll(temp);
		}
		
		return results;
	}
	
	public void recreateStatusTable() throws DaoException {
		super.update("DROP TABLE isr_status", null);
		
		super.update("CREATE TABLE isr_status (" +
        		"statusId varchar(2), " +
        		"statusName varchar(50), " +
        		"statusOrder int(2) NOT NULL, " +
                "PRIMARY KEY(statusId))", null);
		
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'1', 'New', 1)", null);
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'2', 'Reopen', 2)", null);
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'3', 'In Progress', 4)", null);
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'4', 'Completed', 5)", null);
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'5', 'Closed', 6)", null);
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'6', 'Withdrawn', 7)", null);
		super.update("INSERT INTO isr_status (" +
				"statusId, statusName, statusOrder) VALUES (" +
				"'7', 'Clarification', 3)", null);
	}
}