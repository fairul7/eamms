package com.tms.hr.leave.model;

import kacang.model.DefaultDataObject;
import kacang.util.Log;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.Application;

import java.util.Date;
import java.util.Calendar;

public class LeaveEntry extends DefaultDataObject {

    private String id;
    private String leaveType;
    private String leaveTypeId;
    private String leaveTypeName;
    private Date startDate;
    private Date endDate;
    private Float days;
    private boolean halfDay;
    private String status;
    private String reason;
    private String userId;
    private String applicantId;
    private Date applicationDate;
    private Date approvalDate;
    private String approvalUserId;
    private String approvalComments;
    private String eventId;
    private boolean credit;
    private boolean adjustment;
    private Date lastModifiedDate;
    private String lastModifiedUserId;
    private User user;
    private float balance;
    private transient LeaveCalculator calculator;
    private String fixedCalendar;

    public LeaveCalculator getCalculator() {
        if (calculator == null) {
            try {
                Date startDate = getStartDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int year = cal.get(Calendar.YEAR);
                this.calculator = LeaveModule.getLeaveCalculator(year);
            }
            catch (LeaveException e) {
                Log.getLog(getClass()).debug("Unable to retrieve leave calculator: " + e.toString());
            }
        }
        return calculator;
    }

    public void setCalculator(LeaveCalculator calculator) {
        this.calculator = calculator;
    }

    /**
     * Days for the leave, calculated on the fly if necessary.
     * @return
     */
    public Float getCalculatedDays() {
        if (isCredit()) {
            try {
                Date startDate = getStartDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int year = cal.get(Calendar.YEAR);
                LeaveCalculator calculator = getCalculator();
                float actual = calculator.calculateActualDaysForCreditLeave(year, this, false);
                return new Float(actual);
            }
            catch (LeaveException e) {
                Log.getLog(getClass()).debug("Unable to calculate actual days: " + e.toString());
                return getDays();
            }
        }
        else if (isAdjustment()) {
            return getDays();
        }
        else {
            try {
                Date startDate = getStartDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int year = cal.get(Calendar.YEAR);
                LeaveCalculator calculator = getCalculator();
                float actual = calculator.calculateActualDaysForLeave(year, this, false);
                return new Float(actual);
            }
            catch (LeaveException e) {
                Log.getLog(getClass()).debug("Unable to calculate actual days: " + e.toString());
                return getDays();
            }
        }
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getDays() {
        return days;
    }

    public void setDays(Float days) {
        this.days = days;
    }

    public boolean isHalfDay() {
        return halfDay;
    }

    public void setHalfDay(boolean halfDay) {
        this.halfDay = halfDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {


               SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try {
            setUser(security.getUser(userId));
        } catch (SecurityException e) {

        }

        this.userId = userId;




    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    public boolean isAdjustment() {
        return adjustment;
    }

    public void setAdjustment(boolean adjustment) {
        this.adjustment = adjustment;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public void setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getFixedCalendar() {
        return fixedCalendar;
    }

    public void setFixedCalendar(String fixedCalendar) {
        this.fixedCalendar = fixedCalendar;
    }

}
