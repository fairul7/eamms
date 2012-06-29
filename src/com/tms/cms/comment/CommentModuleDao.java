package com.tms.cms.comment;

import com.tms.cms.core.model.DefaultContentModuleDao;


/**
 * Comment Module DAO.
 */
public class CommentModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_comment";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return Comment.class;
    }
}
