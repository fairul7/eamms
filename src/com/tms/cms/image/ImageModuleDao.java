package com.tms.cms.image;

import com.tms.cms.document.DocumentModuleDao;

/**
 * Image Module DAO.
 */
public class ImageModuleDao extends DocumentModuleDao {
    public static final String TABLE_NAME = "cms_content_image";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected String getStorageRoot() {
        return "images";
    }

    protected Class getContentObjectClass() {
        return Image.class;
    }

}
