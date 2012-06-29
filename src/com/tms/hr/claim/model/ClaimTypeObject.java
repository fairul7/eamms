package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 8, 2005
 * Time: 12:30:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimTypeObject extends DefaultDataObject {
    String id;
    String typeName;
    String accountcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

}
