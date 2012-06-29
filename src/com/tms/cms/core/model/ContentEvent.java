package com.tms.cms.core.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 18, 2003
 * Time: 4:45:43 PM
 * To change this template use Options | File Templates.
 */
public class ContentEvent {

    private String id;
    private String event;
    private String userId;
    private String username;
    private Date date;
    private String host;
    private String param;
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
