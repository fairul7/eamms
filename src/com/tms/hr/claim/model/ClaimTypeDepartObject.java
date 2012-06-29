package com.tms.hr.claim.model;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 9, 2005
 * Time: 8:49:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimTypeDepartObject {
    String id;
    String typeName;
    String dm_dept_desc;
    String typeid;
    String departmentid;

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

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

    public String getDm_dept_desc() {
        return dm_dept_desc;
    }

    public void setDm_dept_desc(String dm_dept_desc) {
        this.dm_dept_desc = dm_dept_desc;
    }
}
