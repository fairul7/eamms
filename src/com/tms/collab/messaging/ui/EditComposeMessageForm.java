package com.tms.collab.messaging.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;

public class EditComposeMessageForm extends ComposeMessageForm {
    
    private static final Log log = Log.getLog(EditComposeMessageForm.class);
    
    // current message being edited
    // only valid after onRequest;
    private Message _message;
    
    // current messaging module
    // only valid after onRequest
    private boolean _hasError = false;
    private String _tmpBody;

    /**
     * @see com.tms.collab.messaging.ui.ComposeMessageForm#init()
     */
    public void init() {
        super.init();
    }
    
    
    public void onRequest(Event event) {
        super.onRequest(event);
        boolean firstTime = event.getParameter("validation") == null;

        try {
            MessagingModule _messagingModule = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

            _message = _messagingModule.getMessageByMessageId(event
                    .getRequest().getParameter("messageId"), true);

            if (firstTime) {
                markAsRead(event);
                tbTo.setValue(Util.convertListToCommaSeparatedString(Util
                        .mergeLists(new List[] { 
                                _message.getToIntranetList(),
                                _message.getToList() })));
                tbCc.setValue(Util.convertListToCommaSeparatedString(Util
                        .mergeLists(new List[] { 
                                _message.getCcIntranetList(),
                                _message.getCcList() })));
                tbBcc.setValue(Util.convertListToCommaSeparatedString(Util
                        .mergeLists(new List[] { 
                                _message.getBccIntranetList(),
                                _message.getBccList() })));

                tfSubject.setValue(_message.getSubject());
                
                Map attMap = new HashMap();
                if (_message.getStorageFileList() != null) {
                    for (Iterator i = _message.getStorageFileList().iterator(); i
                            .hasNext();) {
                        StorageFile sf = (StorageFile) i.next();
                        attMap.put(sf.getName(), "-data in storage service-");
                    }
                }

                event.getRequest().getSession().setAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE, attMap);
                //attachmentInfo.setAttachmentMap(attMap);

                tbBody.setValue(_message.getBody());
                cbCopyToSent.setChecked(_message.getCopyToSent());
            }
            else {
                // NOTE:
                // for some reason, text in rich text editor body is lost, so
                // i save a copy during failure (onValidationFailed) and retrive
                // it here, so upon validation failure, the body (modified or not)
                // will 'appear' not to be lost
                tbBody.setValue(_tmpBody);
            }
        } catch (MessagingException e) {
            log.error(e.toString(), e);
        }
        

        if (Util.isRichTextCapable(event.getRequest())) {
            setHtmlEmail(true);
        } else {
            setHtmlEmail(false);
        }
    }
    
    

    /**
     * @see com.tms.collab.messaging.ui.ComposeMessageForm#onSubmit(kacang.ui.Event)
     */
    public Forward onSubmit(Event event) {
        return super.onSubmit(event);
    }


    /**
     * @see kacang.stdui.Form#onValidationFailed(kacang.ui.Event)
     */
    public Forward onValidationFailed(Event event) {
        
        _tmpBody = (String) tbBody.getValue();
        
        return new Forward("BACK_TO_COMPOSE", "composeMessage.jsp?messageId="+_message.getId()+"&validation=fail", true);
    }
    
    /**
     * @see com.tms.collab.messaging.ui.ComposeMessageForm#onValidate(kacang.ui.Event)
     */
    public Forward onValidate(Event event) {
        
        // NOTE:
        // oldMessageId is the message id to be deleted, once sendMessage(...) 
        // is invoke a new message id will be used for the message
        // Trace down sendMessage(...) code for details.
        String oldMessageId = _message.getId();
        super.onValidate(event);
        try {
            MessagingModule _messagingModule = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            _messagingModule.deleteMessage(oldMessageId);
        }
        catch(Exception e) {
            log.error(e.toString(), e);
        }
        
        // if ok button press go to forward
        return new Forward("BACK_TO_OUTBOX", "messageList.jsp?folder="+Folder.FOLDER_OUTBOX, true);
    }
    
    // === protected ==========================================================
    protected void sendMessage(Event event) throws MessagingException, AddressException, FileNotFoundException, StorageException {
        HttpSession session = event.getRequest().getSession();
        User user = Util.getUser(event);
        MessagingModule _messagingModule = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        SmtpAccount smtpAccount = _messagingModule.getSmtpAccountByUserId(user.getId());
        
        recreateAttachementsFromMimeMessage(event);
        setMessageProperties(_message, event);
        if (cbCopyToSent.isChecked()) {
            _messagingModule.sendMessage(smtpAccount, _message, user.getId(), true, session);
        } else {
            _messagingModule.sendMessage(smtpAccount, _message, user.getId(), false, session);
        }
        
        // Remove user's tmp directory (created in 
        // recreateAttachementsFromMimeMessage(...) method.
        _messagingModule.deleteUserTempFolder(user.getId(), event.getRequest().getSession());
    }
    
    protected void recreateAttachementsFromMimeMessage(Event event) {
        
        User user = Util.getUser(event);
        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
        
        //      NOTE:
        // recreate attachment files to user tmp directory (from .eml, the MimeMessage)
        // so subsequent code could recreate the email message with attachments 
        // with or without new attachments
        try {
            MessagingModule _messagingModule = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            Message tmp = _messagingModule.getMessageByMessageId(_message.getId(), true);
            List sfList = tmp.getStorageFileList();
            for (Iterator i = sfList.iterator(); i.hasNext(); ) {
                try {
                    StorageFile csf = (StorageFile) i.next();
            
                    StorageFile usrTmpFile = new StorageFile(
                            MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/" + csf.getName());
                    usrTmpFile.setInputStream(csf.getInputStream());
                    ss.store(usrTmpFile);
            
                }
                catch(Exception e) {
                    log.error(e.toString(), e);
                }
            }
        }
        catch(MessagingException e) {
            log.error(e.toString(), e);
        }
    }
    
    protected void setMessageProperties(Message message, Event event) throws AddressException, MessagingException, FileNotFoundException, StorageException {
        IntranetAccount intranetAccount;
        User user;

        user = Util.getUser(event);
        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);

        intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
        message.setFrom(intranetAccount.getFromAddress());

        message.setToList(Util.convertStringToInternetRecipientsList(tbTo.getValue().toString()));
        message.setCcList(Util.convertStringToInternetRecipientsList(tbCc.getValue().toString()));
        message.setBccList(Util.convertStringToInternetRecipientsList(tbBcc.getValue().toString()));
        message.setToIntranetList(Util.convertStringToIntranetRecipientsList(tbTo.getValue().toString()));
        message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(tbCc.getValue().toString()));
        message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(tbBcc.getValue().toString()));
        message.setSubject(tfSubject.getValue().toString());

        String signature;
        signature = ""; //intranetAccount.getSignature() == null ? "" : intranetAccount.getSignature().trim();

        // set message body with html or plain text detection
        super.detectAndSetMessageBody(event, message, signature);

        message.setDate(new Date());

        Map attachmentMap = attachmentInfo.getAttachmentMap();
        StorageFile sf;
        
        message.setStorageFileList(new ArrayList());
        
        
        
        
        // attach attachments to message as usual
        for (Iterator iterator = attachmentMap.keySet().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            try {
                sf = new StorageFile(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/" + name);
                sf = ss.get(sf);
                if (sf != null) {
                    // if has attachment
                    message.addStorageFile(sf);
                }
            }
            catch(Exception e) {
                log.error(e.toString(), e);
            }
        }
    }
    
    protected void markAsRead(Event event) {
        try {
            _message.setRead(true);
            MessagingModule _messagingModule = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            _messagingModule.updateMessage(_message);
        }
        catch(MessagingException e) {
            log.error(e.toString(), e);
        }
    }
}


