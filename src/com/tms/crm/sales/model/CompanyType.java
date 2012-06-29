package com.tms.crm.sales.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 3:09:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyType extends DefaultDataObject{
    private String type;
    private boolean archived = false;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
