package com.tms.cms.vsection;

import com.tms.cms.core.model.DefaultContentModuleDao;


/**
 * VSection Module DAO.
 */
public class VSectionModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME="cms_content_vsection";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return VSection.class;
    }
}
