package com.tms.elearning.coursecategory.model;

import kacang.model.DefaultDataObject;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Feb 25, 2005
 * Time: 3:04:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Category extends DefaultDataObject implements Serializable{


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    private String createdByUser;
    private String createdByUserId;
    private String createdDate;



    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String user) {
        this.createdByUser = user;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String userid) {
        this.createdByUserId = userid;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String date) {
        this.createdDate = date;
    }

}
