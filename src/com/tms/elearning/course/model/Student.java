package com.tms.elearning.course.model;

import kacang.services.security.DefaultPrincipal;
import kacang.model.DefaultDataObject;

import java.io.Serializable;
import java.util.Date;

import com.tms.elearning.core.model.LearningContentObject;

public class Student extends DefaultDataObject implements Serializable{


    private String id;
    private String student;
    private String name;     //course name registered


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    private Date createdDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }





    public Class getContentModuleClass() {

        return null;
    }
}
