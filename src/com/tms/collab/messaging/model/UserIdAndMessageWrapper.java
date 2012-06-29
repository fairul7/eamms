package com.tms.collab.messaging.model;

/**
 * This is just a Value Object that wrapes up the Receiver's UserId and 
 * the Messages it is supposed to received. 
 * 
 * @see com.tms.collab.messaging.model.MessagingModule#sendIntranetMessage(String, Message, MimeMessage)
 * @see MessagingModule#sendIntranetAndSmtpMessagesNow()
 */
public class UserIdAndMessageWrapper {

    private String _userId;
    private Message _message;
    private javax.mail.Message _mimeMessage;
    
    public UserIdAndMessageWrapper(String userId, Message message, 
            javax.mail.Message mimeMessage) {
        _userId = userId;
        _message = message;
        _mimeMessage = mimeMessage;
    }
    
    public String getUserId() { return _userId; }
    public Message getMessage() { return _message; }
    public javax.mail.Message getMimeMessage() { return _mimeMessage; }
}

