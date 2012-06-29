package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageException;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.Application;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.io.FileNotFoundException;

public class ComposeMessageForm extends Form {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_DRAFT_SAVED = "draftSaved";

    private int maxAutoCompleteMessages = 0;
    private int maxAutoCompleteAddresses = 0;

    protected ComposeMessageAutoCompleteBox tbTo;
    protected ComposeMessageAutoCompleteBox tbCc;
    protected ComposeMessageAutoCompleteBox tbBcc;

    protected TextField tfSubject;
    protected AttachmentInfo attachmentInfo;

    protected TextBox tbBody;
    protected CheckBox cbCopyToSent;

    protected Button btSend;
    protected Button btDraft;
    protected ResetButton btReset;

    private boolean htmlEmail = true;
    
    
    public String getDefaultTemplate() {
    	return "messaging/composeMessage";
    }
    

    public void init() {
        setColumns(2);
        setMethod("POST");

        addChild(new Label("1", "<a href=\"\" onclick=\"return selectRecipients()\">" + Application.getInstance().getMessage("messaging.label.to", "To")+"</a> *"));
        tbTo = new ComposeMessageAutoCompleteBox("to");
        tbTo.addChild(new ValidatorNotEmpty("1"));
        tbTo.setRows("3");
        tbTo.setCols("80");
        addChild(tbTo);

        addChild(new Label("2", "<a href=\"\" onclick=\"return selectRecipients()\">" + Application.getInstance().getMessage("messaging.label.cc", "CC") + "</a>"));
        tbCc = new ComposeMessageAutoCompleteBox("cc");
        tbCc.setRows("2");
        tbCc.setCols("80");
        tbCc.setSharedOptionsVarName(tbTo.getOptionsVarName());
        addChild(tbCc);

        addChild(new Label("3", "<a href=\"\" onclick=\"return selectRecipients()\">" + Application.getInstance().getMessage("messaging.label.bcc", "BCC") + "</a>"));
        tbBcc = new ComposeMessageAutoCompleteBox("bcc");
        tbBcc.setRows("2");
        tbBcc.setCols("80");
        tbBcc.setSharedOptionsVarName(tbTo.getOptionsVarName());
        addChild(tbBcc);

        addChild(new Label("4", Application.getInstance().getMessage("messaging.label.subject", "Subject")+" *"));
        tfSubject = new TextField("subject");
        tfSubject.addChild(new ValidatorNotEmpty("4"));
        addChild(tfSubject);

        addChild(new Label("6", Application.getInstance().getMessage("messaging.label.messageBody", "Message Body")));
        tbBody = new RichTextBox("body");
        tbBody.setRows("40");
        tbBody.setCols("60");
        addChild(tbBody);

        addChild(new Label("5", Application.getInstance().getMessage("messaging.label.attachments", "Attachments")));
        attachmentInfo = new AttachmentInfo("attachmentInfo");
        addChild(attachmentInfo);

        addChild(new Label("8", Application.getInstance().getMessage("messaging.label.copyToSent", "Copy To Sent")));
        cbCopyToSent = new CheckBox("copyToSent");
        cbCopyToSent.setChecked(true);
        addChild(cbCopyToSent);

        Panel p = new Panel("panel");

        btSend = new Button("send", Application.getInstance().getMessage("messaging.label.send", "Send"));
        p.addChild(btSend);
        btDraft = new Button("draft", Application.getInstance().getMessage("messaging.label.saveAsDraft", "Save As Draft"));
        p.addChild(btDraft);
        btReset = new ResetButton("reset");
        btReset.setText(Application.getInstance().getMessage("messaging.label.reset", "Reset"));
        p.addChild(btReset);

        addChild(new Label("9", "&nbsp;"));
        addChild(p);
    }
    
   
    public void onRequest(Event event) {
        String to, cc, bcc;

        if(Util.isRichTextCapable(event.getRequest())) {
            htmlEmail = true;
        } else {
            htmlEmail = false;
        }

        HttpSession session = event.getRequest().getSession();

        to = (String) session.getAttribute(MessagingModule.TO_ATTRIBUTE);
        cc = (String) session.getAttribute(MessagingModule.CC_ATTRIBUTE);
        bcc = (String) session.getAttribute(MessagingModule.BCC_ATTRIBUTE);

        if (to != null) {
            tbTo.setValue(to);
            session.removeAttribute(MessagingModule.TO_ATTRIBUTE);
        }

        if (cc != null) {
            tbCc.setValue(cc);
            session.removeAttribute(MessagingModule.CC_ATTRIBUTE);
        }

        if (bcc != null) {
            tbBcc.setValue(bcc);
            session.removeAttribute(MessagingModule.BCC_ATTRIBUTE);
        }

        try {
            Util.getMessagingModule().deleteUserTempFolder(Util.getUser(event).getId(), session);
        } catch (StorageException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

        // show signature
        String signature = "";
        try {
            IntranetAccount intranetAccount;
            User user;

            user = Util.getUser(event);
            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
            signature = intranetAccount.getSignature() == null ? "" : intranetAccount.getSignature().trim();

            if (signature.length() > 0) {
                if (htmlEmail) {
                    tbBody.setValue("<br><pre>" + signature + "</pre><br>");
                }
                else {
                    tbBody.setValue("\n\r\n\r\n\r\n\r" + signature);
                }
            }
            else {
                tbBody.setValue("");
            }
        }
        catch (MessagingException e) {
            Log.getLog(getClass()).error("Error retrieving signature", e);
        }



    }

    public Forward onSubmit(Event event) {
        Forward fwd = super.onSubmit(event);

        if (!isMessageAddressesValid()) {
            // not valid
            this.setInvalid(true);
            event.getRequest().setAttribute("invalidAddress", Boolean.TRUE);
        }

        return fwd;
    }

    public Forward onValidate(Event event) {
        Log log;
        log = Log.getLog(getClass());

        String button = findButtonClicked(event);
        button = button == null ? "" : button;
        try {
            if (button.endsWith("send")) {
                // process send
                sendMessage(event);
                init();
                return new Forward(FORWARD_SUCCESS);

            } else if (button.endsWith("draft")) {
                // process draft
                saveDraftMessage(event);
                init();
                return new Forward(FORWARD_DRAFT_SAVED);

            } else {
                String msg = "Unknown button '" + button + "' pressed!";
                log.error(msg);
                event.getRequest().getSession().setAttribute("error", msg);
                return new Forward(FORWARD_ERROR);
            }

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        } catch (AddressException e) {
            log.error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        } catch (StorageException e) {
            log.error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    protected void sendMessage(Event event) throws MessagingException, AddressException, FileNotFoundException, StorageException {
        MessagingModule mm;
        User user;
        SmtpAccount smtpAccount;
        Message message;
        HttpSession session = event.getRequest().getSession();

        mm = Util.getMessagingModule();
        user = Util.getUser(event);
        smtpAccount = mm.getSmtpAccountByUserId(user.getId());

        // construct the message to send
        message = new Message();
        String id = UuidGenerator.getInstance().getUuid();
        message.setMessageId(id);
        setMessageProperties(message, event);

        if (cbCopyToSent.isChecked()) {
            mm.sendMessage(smtpAccount, message, user.getId(), true, session);
        } else {
            mm.sendMessage(smtpAccount, message, user.getId(), false, session);
        }

        Log.getLog(getClass()).write(new Date(), id, user.getId(), "kacang.services.log.messaging.SendMessage", "Message sent from user " + user.getName(), event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());
    }

    private void saveDraftMessage(Event event) throws AddressException, MessagingException, FileNotFoundException, StorageException {
        MessagingModule mm;
        User user;
        Message message;

        mm = Util.getMessagingModule();
        user = Util.getUser(event);

        //construct message to save as draft
        message = new Message();
        message.setMessageId(UuidGenerator.getInstance().getUuid());
        setMessageProperties(message, event);

        // save draft message
        mm.saveDraftMessage(message, user.getId(), event.getRequest().getSession());
        mm.updateUserDiskQuota(user.getId());
    }

    protected void setMessageProperties(Message message, Event event) throws AddressException, MessagingException, FileNotFoundException, StorageException {
        IntranetAccount intranetAccount;
        User user;

        user = Util.getUser(event);
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
        detectAndSetMessageBody(event, message, signature);

        message.setDate(new Date());

        Map attachmentMap = attachmentInfo.getAttachmentMap();
        StorageFile sf;
        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);

        for (Iterator iterator = attachmentMap.keySet().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            sf = new StorageFile(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/" + name);
            sf = ss.get(sf);
            if (sf != null) {
                // if has attachment
                message.addStorageFile(sf);
            }
        }
    }

    protected static final String[] HTML_MESSAGE_SIGNATURES = {
        "</p>",
        "<br />",
        "<BLOCKQUOTE style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #1010ff 2px solid\">"
    };

    protected void detectAndSetMessageBody(Event event, Message message, String signature) {
        String body;

        body = tbBody.getValue().toString();

        if (!Util.isRichTextCapable(event.getRequest())) {
            // editor don't support RTE, send as text.

            // text format
            message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
            if (signature.length() > 0) {
                signature = "\n" + signature;
            }

        } else {
            // try to determine if contents of email is HTML
            boolean hasHtmlSign = false;
            for (int i = 0; i < HTML_MESSAGE_SIGNATURES.length; i++) {

                if (body.indexOf(HTML_MESSAGE_SIGNATURES[i]) != -1) {
                    hasHtmlSign = true;
                    break;
                }
            }

            if (hasHtmlSign) {
                // HTML format
                message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                if (signature.length() > 0) {
                    signature = "<br><pre>" + signature + "</pre>";
                }
            } else {
                // text format
                message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                if (signature.length() > 0) {
                    signature = "\n" + signature;
                }
            }

        }


        message.setBody(body + signature);
    }

    protected boolean isMessageAddressesValid() {
        try {
            Util.validateStringInternetAddress(tbTo.getValue().toString());
            Util.validateStringInternetAddress(tbCc.getValue().toString());
            Util.validateStringInternetAddress(tbBcc.getValue().toString());
            Util.convertStringToIntranetRecipientsList(tbTo.getValue().toString());
            Util.convertStringToIntranetRecipientsList(tbCc.getValue().toString());
            Util.convertStringToIntranetRecipientsList(tbBcc.getValue().toString());

        } catch (AddressException e) {
            return false;
        }

        return true;
    }

    // === [ getters/setters ] =================================================
    public ComposeMessageAutoCompleteBox getTbTo() {
        return tbTo;
    }

    public void setTbTo(ComposeMessageAutoCompleteBox tbTo) {
        this.tbTo = tbTo;
    }

    public ComposeMessageAutoCompleteBox getTbCc() {
        return tbCc;
    }

    public void setTbCc(ComposeMessageAutoCompleteBox tbCc) {
        this.tbCc = tbCc;
    }

    public ComposeMessageAutoCompleteBox getTbBcc() {
        return tbBcc;
    }

    public void setTbBcc(ComposeMessageAutoCompleteBox tbBcc) {
        this.tbBcc = tbBcc;
    }

    public TextField getTfSubject() {
        return tfSubject;
    }

    public void setTfSubject(TextField tfSubject) {
        this.tfSubject = tfSubject;
    }

    public TextBox getTbBody() {
        return tbBody;
    }

    public void setTbBody(TextBox tbBody) {
        this.tbBody = tbBody;
    }

    public CheckBox getCbCopyToSent() {
        return cbCopyToSent;
    }

    public void setCbCopyToSent(CheckBox cbCopyToSent) {
        this.cbCopyToSent = cbCopyToSent;
    }

    public Button getBtSend() {
        return btSend;
    }

    public void setBtSend(Button btSend) {
        this.btSend = btSend;
    }

    public Button getBtDraft() {
        return btDraft;
    }

    public void setBtDraft(Button btDraft) {
        this.btDraft = btDraft;
    }

    public AttachmentInfo getAttachmentInfo() {
        return attachmentInfo;
    }

    public void setAttachmentInfo(AttachmentInfo attachmentInfo) {
        this.attachmentInfo = attachmentInfo;
    }

    public ResetButton getBtReset() {
        return btReset;
    }

    public void setBtReset(ResetButton btReset) {
        this.btReset = btReset;
    }

    public int getMaxAutoCompleteMessages() {
        return maxAutoCompleteMessages;
    }

    public void setMaxAutoCompleteMessages(int maxAutoCompleteMessages) {
        this.maxAutoCompleteMessages = maxAutoCompleteMessages;
    }

    public int getMaxAutoCompleteAddresses() {
        return maxAutoCompleteAddresses;
    }

    public void setMaxAutoCompleteAddresses(int maxAutoCompleteAddresses) {
        this.maxAutoCompleteAddresses = maxAutoCompleteAddresses;
    }

    protected class ComposeMessageAutoCompleteBox extends AutoCompleteBox {

        private String userId;

        public ComposeMessageAutoCompleteBox() {
            super();
        }

        public ComposeMessageAutoCompleteBox(String name) {
            super(name);
        }

        public ComposeMessageAutoCompleteBox(String name, String value) {
            super(name, value);
        }

        public void onRequest(Event evt) {
            super.onRequest(evt);
            this.userId = evt.getWidgetManager().getUser().getId();
        }

        public Collection getOptions() {
            MessagingModule mm;
            Collection addresses = new HashSet();
            try {
                Application app = Application.getInstance();
                mm = Util.getMessagingModule();
                int maxMessages = getMaxAutoCompleteMessages();
                if (maxMessages == 0) {
                    try {
                        maxMessages = Integer.parseInt(app.getProperty(MessagingModule.APPLICATION_KEY_AUTOCOMPLETE_MAX_MESSAGES));
                    } catch (Exception e) {
                        // ignore
                    }
                }
                int maxAddresses = getMaxAutoCompleteAddresses();
                if (maxAddresses == 0) {
                    try {
                        maxAddresses = Integer.parseInt(app.getProperty(MessagingModule.APPLICATION_KEY_AUTOCOMPLETE_MAX_ADDRESSES));
                    } catch (Exception e) {
                        // ignore
                    }
                }

                // get most recent addresses
                if (maxMessages > 0 && maxAddresses > 0) {
                    addresses = mm.getAddressesForAutoComplete(userId, maxMessages, maxAddresses);
                }

            } catch (MessagingException e) {
                // ignore, messaging module already handles exception
            }

            return addresses;
        }

    }

    public boolean isHtmlEmail() {
        return htmlEmail;
    }

    public void setHtmlEmail(boolean htmlEmail) {
        this.htmlEmail = htmlEmail;
    }
}
