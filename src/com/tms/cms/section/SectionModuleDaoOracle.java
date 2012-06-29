package com.tms.cms.section;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;

public class SectionModuleDaoOracle extends DefaultContentModuleDaoOracle
{
	protected String getTableName()
	{
        return SectionModuleDao.TABLE_NAME;
    }

    protected Class getContentObjectClass()
	{
        return Section.class;
    }
}
