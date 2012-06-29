package com.tms.hr.employee.model;

import java.util.Date;

public class GlobalDataObject {
    private String serviceCode;
    private String serviceDesc;
    private int yearFrom;
    private int yearTo;
    private int entitlement;
    private int medicalMaxDays;
    private int maternityMaxDays;
    private int paternityMaxDays;
    private int compassionateMaxDays;
    private int carryForwardYear;
    private int carryForwardDays;
    private Date cfStartDate;
    private Date cfEndDate;

    private String memoAlertCode = "Memo Alert";
    private String emailAlertCode = "Email Alert";
    private String autoIncBalanceCode = "Auto Balance Inc";

    private String memoAlert = "0";
    private String emailAlert = "0";
    private String autoIncBalance = "0";
    private String year;
    private Date holidayDate;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }

    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }

    public int getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(int entitlement) {
        this.entitlement = entitlement;
    }

    public int getMedicalMaxDays() {
        return medicalMaxDays;
    }

    public void setMedicalMaxDays(int medicalMaxDays) {
        this.medicalMaxDays = medicalMaxDays;
    }

    public int getMaternityMaxDays() {
        return maternityMaxDays;
    }

    public void setMaternityMaxDays(int maternityMaxDays) {
        this.maternityMaxDays = maternityMaxDays;
    }

    public int getPaternityMaxDays() {
        return paternityMaxDays;
    }

    public void setPaternityMaxDays(int paternityMaxDays) {
        this.paternityMaxDays = paternityMaxDays;
    }

    public int getCompassionateMaxDays() {
        return compassionateMaxDays;
    }

    public void setCompassionateMaxDays(int compassionateMaxDays) {
        this.compassionateMaxDays = compassionateMaxDays;
    }

    public int getCarryForwardYear() {
        return carryForwardYear;
    }

    public void setCarryForwardYear(int carryForwardYear) {
        this.carryForwardYear = carryForwardYear;
    }

    public int getCarryForwardDays() {
        return carryForwardDays;
    }

    public void setCarryForwardDays(int carryForwardDays) {
        this.carryForwardDays = carryForwardDays;
    }

    public Date getCfStartDate() {
        return cfStartDate;
    }

    public void setCfStartDate(Date cfStartDate) {
        this.cfStartDate = cfStartDate;
    }

    public Date getCfEndDate() {
        return cfEndDate;
    }

    public void setCfEndDate(Date cfEndDate) {
        this.cfEndDate = cfEndDate;
    }

    public String getMemoAlertCode() {
        return memoAlertCode;
    }

    public void setMemoAlertCode(String memoAlertCode) {
        this.memoAlertCode = memoAlertCode;
    }

    public String getEmailAlertCode() {
        return emailAlertCode;
    }

    public void setEmailAlertCode(String emailAlertCode) {
        this.emailAlertCode = emailAlertCode;
    }

    public String getAutoIncBalanceCode() {
        return autoIncBalanceCode;
    }

    public void setAutoIncBalanceCode(String autoIncBalanceCode) {
        this.autoIncBalanceCode = autoIncBalanceCode;
    }

    public String getMemoAlert() {
        return memoAlert;
    }

    public void setMemoAlert(String memoAlert) {
        this.memoAlert = memoAlert;
    }

    public String getEmailAlert() {
        return emailAlert;
    }

    public void setEmailAlert(String emailAlert) {
        this.emailAlert = emailAlert;
    }

    public String getAutoIncBalance() {
        return autoIncBalance;
    }

    public void setAutoIncBalance(String autoIncBalance) {
        this.autoIncBalance = autoIncBalance;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }
}
