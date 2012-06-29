package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.List;
import java.util.Date;

public class ActivateForm extends Form {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";

    private TextField fromAddress;
    private SelectBox indicator;
    private TextField smtpServer;
    private TextField smtpPort;
    private CheckBox anonymous;
    private TextField username;
    private Password password;
    protected TextBox signature;

    private Button submit;

    public void init() {
        setColumns(2);
        setMethod("POST");

        addChild(new Label("1", Application.getInstance().getMessage("messaging.label.internetEmailAddress","Internet Email Address")+" *"));
        fromAddress = new TextField("fromAddress");
        fromAddress.addChild(new ValidatorNotEmpty("1"));
        addChild(fromAddress);

        addChild(new Label("2", Application.getInstance().getMessage("messaging.label.indicatorColour","Indicator Colour")));
        indicator = new SelectBox("indicator");
        indicator.setRows(1);
        indicator.setMultiple(false);
        indicator.addOption("1", Application.getInstance().getMessage("messaging.label.red","Red"));
        indicator.addOption("2", Application.getInstance().getMessage("messaging.label.green","Green"));
        indicator.addOption("3", Application.getInstance().getMessage("messaging.label.blue","Blue"));
        indicator.addOption("4", Application.getInstance().getMessage("messaging.label.purple","Purple"));
        indicator.addOption("5", Application.getInstance().getMessage("messaging.label.yellow","Yellow"));
        indicator.addOption("6", Application.getInstance().getMessage("messaging.label.cyan","Cyan"));
        indicator.addOption("7", Application.getInstance().getMessage("messaging.label.pink","Pink"));
        indicator.addOption("8", Application.getInstance().getMessage("messaging.label.gray","Gray"));
        addChild(indicator);

        addChild(new Label("3", Application.getInstance().getMessage("messaging.label.sMTPServerName","SMTP Server Name")+" *"));
        smtpServer = new TextField("smtpServer");
        smtpServer.addChild(new ValidatorNotEmpty("3"));
        addChild(smtpServer);

        addChild(new Label("4", Application.getInstance().getMessage("messaging.label.sMTPPort","SMTP Port")+" *"));
        smtpPort = new TextField("smtpPort");
        smtpPort.setValue("25");
        smtpPort.addChild(new ValidatorIsNumeric("4"));
        addChild(smtpPort);

        addChild(new Label("5", Application.getInstance().getMessage("messaging.label.authenticatedSMTPServer","Authenticated SMTP Server")));
        anonymous = new CheckBox("anonymous");
        anonymous.setChecked(true);
        addChild(anonymous);

        addChild(new Label("6", Application.getInstance().getMessage("messaging.label.sMTPUsername","SMTP Username")));
        username = new TextField("username");
        addChild(username);

        addChild(new Label("7", Application.getInstance().getMessage("messaging.label.sMTPServerPassword","SMTP Server Password")));
        password = new Password("password");
        addChild(password);

        addChild(new Label("8", Application.getInstance().getMessage("messaging.label.signature","Signature")));
        signature = new TextBox("signature");
        addChild(signature);

        addChild(new Label("9", "&nbsp;"));
        submit = new Button("submit", Application.getInstance().getMessage("messaging.label.submit","Submit"));
        addChild(submit);

    }

    public Forward onValidate(Event event) {
        User user = Util.getUser(event);

        if (Util.hasIntranetAccount(event)) {
            // account already exist
            String msg = "Cannot create intranet account. Already exist";
            Log.getLog(this.getClass()).error(msg);
            event.getRequest().getSession().setAttribute("error", msg);
            return new Forward(FORWARD_ERROR);
        }

        IntranetAccount intranetAccount;
        intranetAccount = new IntranetAccount();
        intranetAccount.setId(UuidGenerator.getInstance().getUuid());
        intranetAccount.setUserId(user.getId());
        intranetAccount.setIntranetUsername(user.getUsername());
        intranetAccount.setName("Intranet Messaging Account");
        intranetAccount.setFilterEnabled(false);
        intranetAccount.setFromAddress(getFromAddress().getValue().toString());
        int indi;
        try {
            List valueList = (List) getIndicator().getValue();
            indi = Integer.parseInt(valueList.get(0).toString());
        } catch (Exception e) {
            indi = 1;
        }
        intranetAccount.setIndicator(indi);
        intranetAccount.setSignature(getSignature().getValue().toString());

        SmtpAccount smtpAccount;
        smtpAccount = new SmtpAccount();
        smtpAccount.setSmtpAccountId(UuidGenerator.getInstance().getUuid());
        smtpAccount.setUserId(user.getId());
        smtpAccount.setName("SMTP account");
        smtpAccount.setServerName(getSmtpServer().getValue().toString());
        try {
            smtpAccount.setServerPort(Integer.parseInt(getSmtpPort().getValue().toString()));
        } catch (NumberFormatException e) {
            // default port number
            smtpAccount.setServerPort(25);
        }
        smtpAccount.setAnonymousAccess(!getAnonymous().isChecked());
        smtpAccount.setUsername(getUsername().getValue().toString());
        smtpAccount.setPassword(getPassword().getValue().toString());

        // create the intranet account
        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        try {
            mm.addIntranetAccount(intranetAccount, smtpAccount);
			Log.getLog(getClass()).write(new Date(), null, user.getId(), "kacang.services.log.messaging.ActivateAccount", "Messaging account for user "+ user.getName() + " activated", event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());
            return new Forward(FORWARD_SUCCESS);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }


    // === [ getters/setters ] =================================================
    public TextField getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(TextField fromAddress) {
        this.fromAddress = fromAddress;
    }

    public SelectBox getIndicator() {
        return indicator;
    }

    public void setIndicator(SelectBox indicator) {
        this.indicator = indicator;
    }

    public TextField getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(TextField smtpServer) {
        this.smtpServer = smtpServer;
    }

    public TextField getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(TextField smtpPort) {
        this.smtpPort = smtpPort;
    }

    public CheckBox getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(CheckBox anonymous) {
        this.anonymous = anonymous;
    }

    public TextField getUsername() {
        return username;
    }

    public void setUsername(TextField username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Button getSubmit() {
        return submit;
    }

    public void setSubmit(Button submit) {
        this.submit = submit;
    }

    public TextBox getSignature() {
        return signature;
    }

    public void setSignature(TextBox signature) {
        this.signature = signature;
    }

}
