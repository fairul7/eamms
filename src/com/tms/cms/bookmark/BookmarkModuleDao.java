package com.tms.cms.bookmark;

import com.tms.cms.core.model.DefaultContentModuleDao;


/**
 * Bookmark Module DAO.
 */
public class BookmarkModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_bookmark";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return Bookmark.class;
    }
}
