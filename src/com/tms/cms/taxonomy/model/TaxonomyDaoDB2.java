package com.tms.cms.taxonomy.model;

import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.util.Log;

public class TaxonomyDaoDB2 extends TaxonomyDao{
	
	public void init() throws DaoException {
        
        try {
            // create taxonomy node table
            super.update("CREATE TABLE txy_node (" +
                    "taxonomyId VARCHAR(35)," +
                    "taxonomyName VARCHAR(255)," + 
                    "parentId VARCHAR(35)," +
                    "parent VARCHAR(1)," +
                    "description VARCHAR(255)," +
                    "shown INTEGER," +
                    "nodeSynonym VARCHAR(255)," +
                    "nodeOrder INTEGER)",null);
            
        }
        catch(Exception e) {
        	Log.getLog(getClass()).error("----------------------"+e);
        }
        try {
        	//create taxonomy mapping table
	        super.update("CREATE TABLE txy_mapping (" +
	                "taxonomyId VARCHAR(35)," +
	                "contentId VARCHAR(255)," +
	                "contentType VARCHAR(35)," +
	                "mapBy VARCHAR(255)," +
	                "mapDate TIMESTAMP )",null);
        }
        catch(Exception e) {
        	Log.getLog(getClass()).error("----------------------"+e);
        }
        
        try {
        	Collection col = super.select("SELECT COUNT(*) AS total FROM cms_content_role_permission WHERE role=? AND permission=?",HashMap.class,new Object[]{"manager","Taxonomy"},0,-1);
        	boolean hasUpdate=false;
        	if (col!=null && col.size()>0) {
        		HashMap map = (HashMap)col.iterator().next();
        		Number i = (Number)map.get("total");
        		if (i.intValue()>0) {
        			hasUpdate=true;
        		}
        	}
        	if (!hasUpdate)
        		super.update("INSERT INTO cms_content_role_permission (role,permission) VALUES (?,?) ",
        			new Object[]{"manager","Taxonomy"});
        	
        }
        catch(Exception e) {
        	
        }
    }

}
