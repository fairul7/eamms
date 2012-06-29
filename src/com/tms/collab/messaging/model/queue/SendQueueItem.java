package com.tms.collab.messaging.model.queue;

import com.tms.collab.messaging.model.SmtpAccount;

import javax.mail.internet.MimeMessage;

public class SendQueueItem extends QueueItem {
    private SmtpAccount smtpAccount;
    private MimeMessage mimeMessage;


    // === [ getters/setters ] =================================================
    public SmtpAccount getSmtpAccount() {
        return smtpAccount;
    }

    public void setSmtpAccount(SmtpAccount smtpAccount) {
        this.smtpAccount = smtpAccount;
    }

    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

}
