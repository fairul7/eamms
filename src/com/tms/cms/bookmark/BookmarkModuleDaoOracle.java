package com.tms.cms.bookmark;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;

public class BookmarkModuleDaoOracle extends DefaultContentModuleDaoOracle
{
	protected String getTableName()
	{
		return BookmarkModuleDao.TABLE_NAME;
	}

	protected Class getContentObjectClass()
	{
		return Bookmark.class;
	}
}
