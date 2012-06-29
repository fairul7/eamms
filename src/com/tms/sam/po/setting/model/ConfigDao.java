package com.tms.sam.po.setting.model;

import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.UuidGenerator;

public class ConfigDao extends DataSourceDao{
	public void init() throws DaoException
    {
		super.update("CREATE TABLE po_config_detail (" +
        		"configDetailId varchar(255), " +
        		"configDetailName varchar(255), " +
        		"configDetailDescription mediumtext, " +
        		"configDetailType varchar(255), " +
        		"configDetailOrder int(2) DEFAULT 1, " +
        		"dateCreated datetime, " +
        		"createdBy varchar(255), " +
        		"lastUpdatedDate datetime, " +
        		"lastUpdatedBy varchar(255), " +
                "PRIMARY KEY(configDetailId))", null);
    }
	
	public void deleteConfigDetailsByType(String configDetailType) throws DaoException {
		super.update("DELETE FROM po_config_detail " +
				"WHERE configDetailType = ?", new Object[] {configDetailType});
	}
	
	public void insertConfigDetail(ConfigObject configDetail) throws DaoException {
		 String configDetailSql = "INSERT INTO po_config_detail (" +
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
                "FROM po_config_detail " +
                "WHERE configDetailType = ? " + orderByClause, ConfigObject.class, new Object[] {configDetailType}, 0, -1);
		
		return priorities;
	}
}
