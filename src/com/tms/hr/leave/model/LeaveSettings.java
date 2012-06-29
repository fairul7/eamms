package com.tms.hr.leave.model;

import kacang.model.DefaultDataObject;

import java.util.Date;

public class LeaveSettings extends DefaultDataObject {

    private long year;
    private boolean halfDay;
    private String carryForwardMaxDays;
    private Date carryForwardMaxDate;
    private String workingDays;
    private boolean alternateWeekend;
    private boolean fridayWeekend;
    private boolean proRata;
    private boolean realTime;
    private boolean notifyMemo;
    private boolean notifyEmail;
    private boolean ccAdmin;
    private boolean allowShift;

    /**
     * Returns the properties to be saved by the DAO.
     * When adding a new property, remember to add an element here.
     * @return
     */
    public String[] getProperties() {
        return new String[] {
            "halfDay",
            "carryForwardMaxDays",
            "carryForwardMaxDate",
            "workingDays",
            "alternateWeekend",
            "fridayWeekend",
            "proRata",
            "realTime",
            "notifyMemo",
            "notifyEmail",
            "ccAdmin",
            "allowShift",
        };
    }

    public boolean isHalfDay() {
        return halfDay;
    }

    public void setHalfDay(boolean halfDay) {
        this.halfDay = halfDay;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getCarryForwardMaxDays() {
        return carryForwardMaxDays;
    }

    public void setCarryForwardMaxDays(String carryForwardMaxDays) {
        this.carryForwardMaxDays = carryForwardMaxDays;
    }

    public Date getCarryForwardMaxDate() {
        return carryForwardMaxDate;
    }

    public void setCarryForwardMaxDate(Date carryForwardMaxDate) {
        this.carryForwardMaxDate = carryForwardMaxDate;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public boolean isAlternateWeekend() {
        return alternateWeekend;
    }

    public void setAlternateWeekend(boolean alternateWeekend) {
        this.alternateWeekend = alternateWeekend;
    }

    public boolean isFridayWeekend() {
        return fridayWeekend;
    }

    public void setFridayWeekend(boolean fridayWeekend) {
        this.fridayWeekend = fridayWeekend;
    }

    public boolean isProRata() {
        return proRata;
    }

    public void setProRata(boolean proRata) {
        this.proRata = proRata;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public void setRealTime(boolean realTime) {
        this.realTime = realTime;
    }

    public boolean isNotifyMemo() {
        return notifyMemo;
    }

    public void setNotifyMemo(boolean notifyMemo) {
        this.notifyMemo = notifyMemo;
    }

    public boolean isNotifyEmail() {
        return notifyEmail;
    }

    public void setNotifyEmail(boolean notifyEmail) {
        this.notifyEmail = notifyEmail;
    }

    public boolean isCcAdmin() {
        return ccAdmin;
    }

    public void setCcAdmin(boolean ccAdmin) {
        this.ccAdmin = ccAdmin;
    }

    public boolean isAllowShift() {
        return allowShift;
    }

    public void setAllowShift(boolean allowShift) {
        this.allowShift = allowShift;
    }
}
