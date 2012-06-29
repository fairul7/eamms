package com.tms.cms.page;

import com.tms.cms.core.model.DefaultContentModuleDao;

/**
 * Page Module DAO.
 */
public class PageModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_page";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return Page.class;
    }
}
