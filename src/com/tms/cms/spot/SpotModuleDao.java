package com.tms.cms.spot;

import com.tms.cms.core.model.DefaultContentModuleDao;

/**
 * Spot Module DAO.
 */
public class SpotModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_spot";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return Spot.class;
    }
}
