package com.tms.hr.leave.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.io.Serializable;

public class SetupDataObject extends DefaultDataObject implements Serializable {
    private String userID;
    private String name;
    private String firstName;
    private String lastName;
    private String deptCode;
    private String deptDesc;
    private String year;
    private String leaveCode;
    private String leaveDesc;
    private String entitlement;
    private int balance;
    private Date holidayDate;
    private String type;
    private String description;
    private boolean isCredited;
    private String reportToUserID;
    private String reportToName;
    private Date date;

    public Date getDate() { 
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getLeaveDesc() {
        return leaveDesc;
    }

    public void setLeaveDesc(String leaveDesc) {
        this.leaveDesc = leaveDesc;
    }

    public String getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(String entitlement) {
        this.entitlement = entitlement;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCredited() {
        return isCredited;
    }

    public void setCredited(boolean credited) {
        isCredited = credited;
    }

    public String getReportToUserID() {
        return reportToUserID;
    }

    public void setReportToUserID(String reportToUserID) {
        this.reportToUserID = reportToUserID;
    }

    public String getReportToName() {
        return reportToName;
    }

    public void setReportToName(String reportToName) {
        this.reportToName = reportToName;
    }

}
