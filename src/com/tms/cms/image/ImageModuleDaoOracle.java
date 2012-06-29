package com.tms.cms.image;

import com.tms.cms.document.DocumentModuleDaoOracle;

public class ImageModuleDaoOracle extends DocumentModuleDaoOracle
{
	protected String getTableName()
	{
        return ImageModuleDao.TABLE_NAME;
    }

    protected String getStorageRoot()
	{
        return "images";
    }

    protected Class getContentObjectClass()
	{
        return Image.class;
    }
}
