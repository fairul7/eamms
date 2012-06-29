package com.tms.cms.maillist.model;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;

public abstract class MailList extends DefaultDataObject {
    public static final int MAIL_LIST_TYPE_COMPOSED = 1;
    public static final int MAIL_LIST_TYPE_CONTENT = 2;
    public static final int MAIL_LIST_TYPE_SCHEDULED = 3;

    private String name;
    private String description;

    private boolean html;
    private String senderEmail;
    private String subject;
    private List recipientList;
    private String templateId;
    private int mailListType;

    /**
     * Subclasses to implement this
     * @return
     */
    public abstract String getBody();

    public boolean isReadyToSend() {

        // make sure not null
        if(senderEmail!=null && subject!=null && recipientList!=null) {

            // make sure not empty fields
            if(senderEmail.trim().length()>0 & subject.trim().length()>0 &&
            recipientList.size()>0) {

                // can send
                return true;

            }
        }

        // still cannot send
        return false;
    }

    /**
     * Returns the full content of this mailing list.
     * @return
     */
    public String getContent() throws MailListException {
        StringBuffer sb;

        sb = new StringBuffer();

        if(getTemplateId()!=null && getTemplateId().length()>0) {
            sb.append(getTemplate().getHeader());
        }

        sb.append(getBody());

        if(getTemplateId()!=null && getTemplateId().length()>0) {
            sb.append(getTemplate().getFooter());
        }

        return sb.toString();
    }

    public MailTemplate getTemplate() throws MailListException {
        MailListModule module;

        if(templateId==null) {
            return null;
        }

        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
        return module.getMailTemplate(templateId);
    }


    // === [ getters/setters ] =================================================
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public List getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List recipientList) {
        this.recipientList = recipientList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateId() {
        return templateId==null ? "" : templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getMailListType() {
        return mailListType;
    }

    public void setMailListType(int mailListType) {
        this.mailListType = mailListType;
    }


    // === [ for DbUtil ] ======================================================
    /**
     * Getter for DbUtil.
     * @return
     */
    public String getRecipientsEmail() {
        StringBuffer sb;

        if(getRecipientList() == null) {
            return "";
        } else {

            sb = new StringBuffer();
            for(int i = 0; i < getRecipientList().size(); i++) {
                sb.append((String) getRecipientList().get(i));
                if(i + 1 < getRecipientList().size()) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
    }

    public String getRecipientsEmailForDisplay() {
        String s = getAbsoluteRecipientsEmail();

        if(s.length()>250) {
            return s.substring(0, 250) + "...";
        } else {
            return s;
        }
    }

    public String getAbsoluteRecipientsEmail() {
        Collection emailSet = new HashSet();
        String value;
        StringBuffer sb;
        boolean unsubscribed;

        if(getRecipientList() == null) {
            return "";
        } else {

            for(int i = 0; i < getRecipientList().size(); i++) {
                unsubscribed = false;
                value = (String) getRecipientList().get(i);

                if((value.startsWith("{") && value.endsWith("}"))) {
                    // decode groupId
                    expandGroupIdToEmails(emailSet, value.substring(1, value.length()-1));
                } else {
                    // normal email
                    if(this instanceof ScheduledMailList) {
                        ScheduledMailList sml = (ScheduledMailList) this;
                        if(sml.getUnsubscribedEmailList().contains(value.trim())) {
                            unsubscribed = true;
                        }
                    }
                    if(!unsubscribed) {
                        emailSet.add(value.trim());
                    }
                }
            }

            sb = new StringBuffer();
            for(Iterator iter=emailSet.iterator(); iter.hasNext(); ) {
                sb.append((String) iter.next());
                if(iter.hasNext()) {
                    sb.append(",");
                }
            }
            return sb.toString();

        }
    }

    private void expandGroupIdToEmails(Collection collection, String id) {
        SecurityService ss;
        User user;
        boolean unsubscribed;
        ss = (SecurityService) Application.getInstance().getService(SecurityService.class);

        try {
            Collection users = ss.getGroupUsers(id);
            for(Iterator iter = users.iterator(); iter.hasNext();) {
                unsubscribed = false;
                user = (User) iter.next();
                if("1".equals(user.getProperty("active"))) {
                    String e = (String) user.getProperty("email1");
                    if(e != null && e.trim().length() > 0) {

                        if(this instanceof ScheduledMailList) {
                            ScheduledMailList sml = (ScheduledMailList) this;
                            if(sml.getUnsubscribedEmailList().contains(e.trim())) {
                                unsubscribed = true;
                            }
                        }

                        if(!unsubscribed) {
                            collection.add(e.trim());
                        }
                    }
                }
            }

        } catch(SecurityException e) {
            Log.getLog(MailList.class).error(e.getMessage(), e);
        }
    }

    public String getRecipientsEmailWithoutGroupId() {
        StringBuffer sb;
        String value;

        if(getRecipientList() == null) {
            return "";
        } else {

            sb = new StringBuffer();
            for(int i = 0; i < getRecipientList().size(); i++) {
                value = (String) getRecipientList().get(i);
                // only add if not a specially encoded groupId
                if(!(value.startsWith("{") && value.endsWith("}"))) {
                    sb.append(value);
                    sb.append(",");
                }
            }

            if(sb.length()>0 && sb.charAt(sb.length()-1)==',') {
                sb.deleteCharAt(sb.length()-1);
            }
            return sb.toString();
        }
    }

    public String[] getRecipientsEmailGroupIds() {
        String value;
        List groupIdList;

        groupIdList = new ArrayList();

        if(getRecipientList() == null) {
            return (String[]) groupIdList.toArray(new String[0]);
        } else {
            for(int i = 0; i < getRecipientList().size(); i++) {
                value = (String) getRecipientList().get(i);
                if(value.startsWith("{") && value.endsWith("}")) {
                    groupIdList.add(value.substring(1, value.length() - 1));
                }
            }
            return (String[]) groupIdList.toArray(new String[0]);
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
        if(list==null) {
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
