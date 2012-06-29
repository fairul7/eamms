package com.tms.hr.employee.model;

import kacang.model.DefaultDataObject;

public class DepartmentDataObject extends DefaultDataObject {
    private String employeeID;
    private String name;
    private String deptCode;
    private String deptDesc;
    private String serviceCode;
    private String serviceDesc;
    private boolean recommender = false;
    private String reportTo;
    private String reportToName;
    private String firstName;
    private String lastName;
    private String reportToFirstName;
    private String reportToLastName;
    private String recommenderCode;
    private String approverCode;
    private String headCode;


    public String getRecommenderCode() {
        return recommenderCode;
    }

    public void setRecommenderCode(String recommenderCode) {
        this.recommenderCode = recommenderCode;
    }

    public String getApproverCode() {
        return approverCode;
    }

    public void setApproverCode(String approverCode) {
        this.approverCode = approverCode;
    }

    public String getHeadCode() {
        return headCode;
    }

    public void setHeadCode(String headCode) {
        this.headCode = headCode;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isRecommender() {
        return recommender;
    }

    public void setRecommender(boolean recommender) {
        this.recommender = recommender;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    public String getReportToName() {
        return reportToName;
    }

    public void setReportToName(String reportToName) {
        this.reportToName = reportToName;
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

    public String getReportToFirstName() {
        return reportToFirstName;
    }

    public void setReportToFirstName(String reportToFirstName) {
        this.reportToFirstName = reportToFirstName;
    }

    public String getReportToLastName() {
        return reportToLastName;
    }

    public void setReportToLastName(String reportToLastName) {
        this.reportToLastName = reportToLastName;
    }

    public String getFullName() {
        String first = getFirstName();
        String last = getLastName();
        String fullName = (first != null) ? first : "";
        if (last != null && last.trim().length() > 0) {
            fullName += " " + last;
        }
        if (fullName.trim().length() == 0) {
            fullName = "[" + getName() + "]";
        }
        return fullName;
    }

    public String getReportToFullName() {
        String first = getReportToFirstName();
        String last = getReportToLastName();
        String fullName = (first != null) ? first : "";
        if (last != null && last.trim().length() > 0) {
            fullName += " " + last;
        }
        if (fullName.trim().length() == 0) {
            fullName = "[" + getReportToName() + "]";
        }
        return fullName;
    }
}
