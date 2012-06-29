package com.tms.hr.leave.model;

import kacang.model.DefaultDataObject;

public class LeaveType extends DefaultDataObject {

    private String leaveType;
    private String name;
    private String gender;
    private boolean creditAllowed;
    private String fixedCalendar;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isCreditAllowed() {
        return creditAllowed;
    }

    public void setCreditAllowed(boolean creditAllowed) {
        this.creditAllowed = creditAllowed;
    }

    public String getFixedCalendar() {
        return fixedCalendar;
    }

    public void setFixedCalendar(String fixedCalendar) {
        this.fixedCalendar = fixedCalendar;
    }
}
