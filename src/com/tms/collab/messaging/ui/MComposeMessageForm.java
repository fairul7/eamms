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
import java.util.Date;
import java.util.Map;
import java.util.Iterator;
import java.io.FileNotFoundException;

public class MComposeMessageForm extends Form {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_DRAFT_SAVED = "draftSaved";

    private TextBox tbTo;
    private TextBox tbCc;
    private TextBox tbBcc;

    private TextField tfSubject;
    private AttachmentInfo attachmentInfo;

    private TextBox tbBody;
    private CheckBox cbAllowHtml;
    private CheckBox cbCopyToSent;

    private Button btSend;
/*
    private Button btDraft;
*/
    private ResetButton btReset;

    public void init() {
        setColumns(2);
        setMethod("POST");

        addChild(new Label("1", "<a href=\"\" onclick=\"return selectRecipients()\">"+Application.getInstance().getMessage("messaging.label.to","To")+"</a>"));
        tbTo = new TextBox("to");
        tbTo.addChild(new ValidatorNotEmpty("1"));
        tbTo.setRows("3");
        tbTo.setCols("60");
        addChild(tbTo);

        addChild(new Label("2", "<a href=\"\" onclick=\"return selectRecipients()\">"+Application.getInstance().getMessage("messaging.label.cc","CC")+"</a>"));
        tbCc = new TextBox("cc");
        tbCc.setRows("2");
        tbCc.setCols("60");
        addChild(tbCc);

        addChild(new Label("3", "<a href=\"\" onclick=\"return selectRecipients()\">"+Application.getInstance().getMessage("messaging.label.bcc","BCC")+"</a>"));
        tbBcc = new TextBox("bcc");
        tbBcc.setRows("2");
        tbBcc.setCols("60");
        addChild(tbBcc);

        addChild(new Label("4", Application.getInstance().getMessage("messaging.label.subject","Subject")));
        tfSubject = new TextField("subject");
        tfSubject.addChild(new ValidatorNotEmpty("4"));
        addChild(tfSubject);

        addChild(new Label("6", Application.getInstance().getMessage("messaging.label.messageBody","Message Body")));
        tbBody = new RichTextBox("body");
        tbBody.setRows("40");
        tbBody.setCols("60");
        addChild(tbBody);

        addChild(new Label("5", Application.getInstance().getMessage("messaging.label.attachments","Attachments")));
        attachmentInfo = new AttachmentInfo("attachmentInfo");
        addChild(attachmentInfo);

        addChild(new Label("7", Application.getInstance().getMessage("messaging.label.allowHTML","Allow HTML")));
        cbAllowHtml = new CheckBox("allowHtml");
        cbAllowHtml.setOnClick("doAllowHtml()");
        addChild(cbAllowHtml);

        addChild(new Label("8", Application.getInstance().getMessage("messaging.label.copyToSent","Copy To Sent")));
        cbCopyToSent = new CheckBox("copyToSent");
        cbCopyToSent.setChecked(true);
        addChild(cbCopyToSent);

        Panel p = new Panel("panel");

        btSend = new Button("send", Application.getInstance().getMessage("messaging.label.send","Send"));
        p.addChild(btSend);
/*
        btDraft = new Button("draft", Application.getInstance().getMessage("messaging.label.saveAsDraft","Save As Draft"));
        p.addChild(btDraft);
*/
        btReset = new ResetButton("reset");
        btReset.setText(Application.getInstance().getMessage("messaging.label.reset","Reset"));
        p.addChild(btReset);

        addChild(new Label("9", "&nbsp;"));
        addChild(p);

    }

    public void onRequest(Event event) {
        String to, cc, bcc;

        HttpSession session = event.getRequest().getSession();

        to = (String) session.getAttribute(MessagingModule.TO_ATTRIBUTE);
        cc = (String) session.getAttribute(MessagingModule.CC_ATTRIBUTE);
        bcc = (String) session.getAttribute(MessagingModule.BCC_ATTRIBUTE);

        if(to!=null) {
            tbTo.setValue(to);
            session.removeAttribute(MessagingModule.TO_ATTRIBUTE);
        }

        if(cc!=null) {
            tbCc.setValue(cc);
            session.removeAttribute(MessagingModule.CC_ATTRIBUTE);
        }

        if(bcc!=null) {
            tbBcc.setValue(bcc);
            session.removeAttribute(MessagingModule.BCC_ATTRIBUTE);
        }

        try {
            Util.getMessagingModule().deleteUserTempFolder(Util.getUser(event).getId(), session);
        } catch (StorageException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

    public Forward onSubmit(Event event) {
        Forward fwd = super.onSubmit(event);

        if(!isMessageAddressesValid()) {
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

    private void sendMessage(Event event) throws MessagingException, AddressException, FileNotFoundException, StorageException {
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

		Log.getLog(getClass()).write(new Date(), id, user.getId(), "kacang.services.log.messaging.SendMessage", "Message sent from user "+ user.getName(), event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());
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
    }

    private void setMessageProperties(Message message, Event event) throws AddressException, MessagingException, FileNotFoundException, StorageException {
        IntranetAccount intranetAccount;
        User user;

        user = Util.getUser(event);
        intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
        message.setFrom(intranetAccount.getFromAddress());

        message.setToList(Util.convertStringToInternetRecipientsList(
                tbTo.getValue().toString()
        ));
        message.setCcList(Util.convertStringToInternetRecipientsList(
                tbCc.getValue().toString()
        ));
        message.setBccList(Util.convertStringToInternetRecipientsList(
                tbBcc.getValue().toString()
        ));
        message.setToIntranetList(Util.convertStringToIntranetRecipientsList(
                tbTo.getValue().toString()
        ));
        message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(
                tbCc.getValue().toString()
        ));
        message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(
                tbBcc.getValue().toString()
        ));
        message.setSubject(tfSubject.getValue().toString());
        message.setBody(tbBody.getValue().toString());
        if (cbAllowHtml.isChecked()) {
            // HTML format
            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
        } else {
            // text format
            message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
        }
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

    private boolean isMessageAddressesValid() {
        try {
            Util.validateStringInternetAddress(
                    tbTo.getValue().toString());
            Util.validateStringInternetAddress(
                    tbCc.getValue().toString());
            Util.validateStringInternetAddress(
                    tbBcc.getValue().toString());
            Util.convertStringToIntranetRecipientsList(
                    tbTo.getValue().toString());
            Util.convertStringToIntranetRecipientsList(
                    tbCc.getValue().toString());
            Util.convertStringToIntranetRecipientsList(
                    tbBcc.getValue().toString());

        } catch (AddressException e) {
            return false;
        }

        return true;
    }

    // === [ getters/setters ] =================================================
    public TextField getTbTo() {
        return tbTo;
    }

    public void setTbTo(TextBox tbTo) {
        this.tbTo = tbTo;
    }

    public TextField getTbCc() {
        return tbCc;
    }

    public void setTbCc(TextBox tbCc) {
        this.tbCc = tbCc;
    }

    public TextField getTbBcc() {
        return tbBcc;
    }

    public void setTbBcc(TextBox tbBcc) {
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

    public CheckBox getCbAllowHtml() {
        return cbAllowHtml;
    }

    public void setCbAllowHtml(CheckBox cbAllowHtml) {
        this.cbAllowHtml = cbAllowHtml;
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

/*
    public Button getBtDraft() {
        return btDraft;
    }

    public void setBtDraft(Button btDraft) {
        this.btDraft = btDraft;
    }
*/

    public AttachmentInfo getAttachmentInfo() {
        return attachmentInfo;
    }

    public void setAttachmentInfo(AttachmentInfo attachmentInfo) {
        this.attachmentInfo = attachmentInfo;
    }

    public ResetButton getBtReset()
    {
        return btReset;
    }

    public void setBtReset(ResetButton btReset)
    {
        this.btReset = btReset;
    }
}
