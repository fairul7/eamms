package com.tms.hr.leave.model;

import kacang.model.DefaultDataObject;

public class LeaveEntitlement extends DefaultDataObject {

    private String serviceClassId;
    private String leaveType;
    private long entitlement;
    private long year;
    private long serviceYears;
    private String serviceClassDescription;

    public String getServiceClassId() {
        return serviceClassId;
    }

    public void setServiceClassId(String serviceClassId) {
        this.serviceClassId = serviceClassId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public long getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(long entitlement) {
        this.entitlement = entitlement;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getServiceYears() {
        return serviceYears;
    }

    public void setServiceYears(long serviceYears) {
        this.serviceYears = serviceYears;
    }

    public String getServiceClassDescription() {
        return serviceClassDescription;
    }

    public void setServiceClassDescription(String serviceClassDescription) {
        this.serviceClassDescription = serviceClassDescription;
    }
}
