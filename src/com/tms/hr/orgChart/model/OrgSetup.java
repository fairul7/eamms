package com.tms.hr.orgChart.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 16, 2006
 * Time: 11:43:21 AM
 */
public class OrgSetup extends DefaultDataObject {
	
	protected String deptDesc;
	protected String deptSectionCode;
    protected String code;
    protected String shortDesc;
    protected String longDesc;
    protected boolean active;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

	public String getDeptSectionCode() {
		return deptSectionCode;
	}

	public void setDeptSectionCode(String deptSectionCode) {
		this.deptSectionCode = deptSectionCode;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

}

