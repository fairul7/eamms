package com.tms.cms.spot;

import com.tms.cms.core.model.DefaultContentModuleDaoOracle;

public class SpotModuleDaoOracle extends DefaultContentModuleDaoOracle
{
	protected String getTableName()
	{
        return SpotModuleDao.TABLE_NAME;
    }

    protected Class getContentObjectClass()
	{
        return Spot.class;
    }
}
