package com.tms.collab.messaging.model;

import java.io.Serializable;
import java.util.Date;

public class MessageSummary implements Serializable {
    private String messageId;

    private String subject;
    private String preview;
    private String from;
    private String to;
    private Date receivedDate;
    private int size;
    private boolean newMail;


    // === [ getters/setters ] =================================================
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isNewMail() {
        return newMail;
    }

    public void setNewMail(boolean newMail) {
        this.newMail = newMail;
    }
}
