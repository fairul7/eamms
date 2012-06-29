package com.tms.cms.maillist.model;

import kacang.model.DefaultDataObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class MailLog extends DefaultDataObject {
    private Date startDate;
    private Date endDate;
    private String message;

    private int mailListType;
    private boolean html;

    private String senderEmail;
    private List recipientList;
    private String subject;
    private String content;

    // === [ getters/setters ] =================================================
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public int getMailListType() {
        return mailListType;
    }

    public void setMailListType(int mailListType) {
        this.mailListType = mailListType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List recipientList) {
        this.recipientList = recipientList;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    // === [ for DbUtil ] ======================================================
    /**
     * Getter for DbUtil.
     * @return
     */
    public String getRecipientsEmail() {
        List list = getRecipientList();
        StringBuffer sb;

        if(list == null) {
            return "";
        } else {

            sb = new StringBuffer();
            for(int i = 0; i < list.size(); i++) {
                sb.append((String) list.get(i));
                if(i + 1 < list.size()) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
    }


    public String getRecipientsEmailForDisplay() {
        String s = getRecipientsEmail();

        if(s.length() > 250) {
            return s.substring(0, 250) + "...";
        } else {
            return s;
        }
    }

    /**
     * Setter for DbUtil.
     * @param s
     */
    public void setRecipientsEmail(String s) {
        StringTokenizer st;
        List list;

        list = getRecipientList();
        if(list == null) {
            list = new ArrayList();
            setRecipientList(list);
        } else {
            list.clear();
        }

        st = new StringTokenizer(s, ",");
        while(st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
    }

}
