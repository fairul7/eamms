package com.tms.cms.page;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;

public class PageModuleDaoOracle extends DefaultContentModuleDaoOracle
{
	protected String getTableName()
	{
        return PageModuleDao.TABLE_NAME;
    }

    protected Class getContentObjectClass()
	{
        return Page.class;
    }
}
