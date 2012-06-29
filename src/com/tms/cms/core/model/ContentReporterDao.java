package com.tms.cms.core.model;

import kacang.model.DaoException;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 21, 2003
 * Time: 4:48:19 PM
 * To change this template use Options | File Templates.
 */
public class ContentReporterDao extends ContentAuditorDao {

    public void init() throws DaoException {
    }

    protected String getTableName() {
        return "cms_content_report";
    }


}
