package com.tms.cms.core.model;

import kacang.model.DaoException;

public class ContentReporterDaoOracle extends ContentAuditorDaoOracle
{
	public void init() throws DaoException
	{
    }

    protected String getTableName()
	{
        return "cms_content_report";
    }
}
