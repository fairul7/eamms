package com.tms.hr.employee.model;

import kacang.model.DefaultDataObject;

import java.util.Date;

public class EmployeeDataObject extends DefaultDataObject {

    private String employeeID;
    private String name;
    private String firstName;
    private String lastName;
    private String gender;
    private String deptCode;
    private String deptDesc;
    private String recommender;
    private Date joinDate;
    private Date resignDate;
    private String email;
    private boolean shiftWorker;
    private String workLocation;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getResignDate() {
        return resignDate;
    }

    public void setResignDate(Date resignDate) {
        this.resignDate = resignDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isShiftWorker() {
        return shiftWorker;
    }

    public void setShiftWorker(boolean shiftWorker) {
        this.shiftWorker = shiftWorker;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
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

}
