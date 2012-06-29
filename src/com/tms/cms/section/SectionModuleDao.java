package com.tms.cms.section;

import com.tms.cms.core.model.DefaultContentModuleDao;


/**
 * Section Module DAO.
 */
public class SectionModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_section";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return Section.class;
    }
}
