package com.tms.elearning.testware.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.io.Serializable;

public class StudentStatistic extends DefaultDataObject implements Serializable{
    private Date dateTook;
    private Number total_questions;
    private Number wrong_questions;
    private String user_id;
    private String name; //assessment name
    private String module_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(String assessment_id) {
        this.assessment_id = assessment_id;
    }

    private String assessment_id;

	public Number getTotal_questions() {
		return total_questions;
	}

	public void setTotal_questions(Number total_questions) {
		this.total_questions = total_questions;
	}

	public Number getWrong_questions() {
		return wrong_questions;
	}

	public void setWrong_questions(Number wrong_questions) {
		this.wrong_questions = wrong_questions;
	}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public Date getDateTook() {
        return dateTook;
    }

    public void setDateTook(Date dateTook) {
        this.dateTook = dateTook;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }
}
