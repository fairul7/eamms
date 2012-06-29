package com.tms.cms.vsection;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;

public class VSectionModuleDaoOracle extends DefaultContentModuleDaoOracle
{
	protected String getTableName()
	{
        return VSectionModuleDao.TABLE_NAME;
    }

    protected Class getContentObjectClass()
	{
        return VSection.class;
    }
}
