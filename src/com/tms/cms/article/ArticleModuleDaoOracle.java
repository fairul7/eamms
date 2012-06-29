package com.tms.cms.article;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;
import com.tms.cms.article.Article;
import com.tms.cms.article.ArticleModuleDao;

public class ArticleModuleDaoOracle extends DefaultContentModuleDaoOracle
{

	protected String getTableName()
	{
		return ArticleModuleDao.TABLE_NAME;
	}

	protected Class getContentObjectClass()
	{
		return Article.class;
	}
}
