package com.tms.cms.comment;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;

public class CommentModuleDaoOracle extends DefaultContentModuleDaoOracle
{
	protected String getTableName()
	{
		return CommentModuleDao.TABLE_NAME;
	}

	protected Class getContentObjectClass()
	{
		return Comment.class;
	}
}
