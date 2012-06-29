package com.tms.cms.article;

import com.tms.cms.core.model.DefaultContentModuleDao;


/**
 * Article Module DAO.
 */
public class ArticleModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_article";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected Class getContentObjectClass() {
        return Article.class;
    }
}
