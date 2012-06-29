package com.tms.hr.leave.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;

public class LeaveSummary extends DefaultDataObject {

    private String userId;
    private User user;
    private String leaveType;
    private long year;
    private float carryForward;
    private float entitlement;
    private float credited;
    private float taken;
    private float adjustments;
    private float balance;

    /**
     * Returns employee's name
     * @return
     */
    public String getEmployeeName() {
        User u = getUser();
        if (u != null) {
            return u.getName();
        }
        else {
            return getUserId();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public float getCarryForward() {
        return carryForward;
    }

    public void setCarryForward(float carryForward) {
        this.carryForward = carryForward;
    }

    public float getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(float entitlement) {
        this.entitlement = entitlement;
    }

    public float getCredited() {
        return credited;
    }

    public void setCredited(float credited) {
        this.credited = credited;
    }

    public float getTaken() {
        return taken;
    }

    public void setTaken(float taken) {
        this.taken = taken;
    }

    public float getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(float adjustments) {
        this.adjustments = adjustments;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

}
