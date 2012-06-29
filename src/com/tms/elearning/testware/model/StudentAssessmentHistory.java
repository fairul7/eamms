package com.tms.elearning.testware.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Jun 7, 2005
 * Time: 10:30:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentAssessmentHistory extends DefaultDataObject implements Serializable{
    //for statistic
    private Date lastTakenDate;
    private String lastTakenDateStr;   //for mysql case
    private int numbTaken;
    private String numbTakenStr; // for mysql Case
    private Date startDate;
    private Date endDate;
    private String user_id;
    private String question;
    private String wrong_answer;
    private String right_answer;
    private String assessment_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getWrong_answer() {
        return wrong_answer;
    }

    public void setWrong_answer(String wrong_answer) {
        this.wrong_answer = wrong_answer;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public String getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(String assessment_id) {
        this.assessment_id = assessment_id;
    }

    public Date getLastTakenDate() {
        return lastTakenDate;
    }

    public void setLastTakenDate(Date lastTakenDate) {
        this.lastTakenDate = lastTakenDate;
    }

    public int getNumbTaken() {
        return numbTaken;
    }

    public void setNumbTaken(int numbTaken) {
        this.numbTaken = numbTaken;
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

    public String getLastTakenDateStr() {
        return lastTakenDateStr;
    }

    public void setLastTakenDateStr(String lastTakenDateStr) {
        this.lastTakenDateStr = lastTakenDateStr;
    }

    public String getNumbTakenStr() {
        return numbTakenStr;
    }

    public void setNumbTakenStr(String numbTakenStr) {
        this.numbTakenStr = numbTakenStr;
    }

}
