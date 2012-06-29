package com.tms.mugshot.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jul 6, 2005
 * Time: 4:33:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mugshot implements Serializable {
    private String userId;
    private String filePath;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
