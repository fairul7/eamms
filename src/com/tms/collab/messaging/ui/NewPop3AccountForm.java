package com.tms.collab.messaging.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.messaging.model.*;

import java.util.List;

public class NewPop3AccountForm extends Form {
	
	private static final Log log = Log.getLog(NewPop3AccountForm.class);
	
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";

    private TextField tfName;
    private SelectBox sbIndicator;
    private SelectBox sbDeliveryFolderId;

    private TextField tfServerName;
    private TextField tfServerPort;
    private TextField tfUsername;
    private Password pwPassword;
    private Password pwConfirmPassword;
    private CheckBox cbLeaveMailOnServer;

    private Button btSubmit;

    
    public String getDefaultTemplate() {
    	return "messaging/newPop3Account";
    }
    
    public void init() {
        setColumns(2);
        setMethod("POST");

        addChild(new Label("1", Application.getInstance().getMessage("messaging.label.accountName","Account Name")+" *"));
        tfName = new TextField("tfName");
        tfName.addChild(new ValidatorNotEmpty("1"));
        addChild(tfName);

        addChild(new Label("2", Application.getInstance().getMessage("messaging.label.indicatorColour","Indicator Colour")));
        sbIndicator = new SelectBox("sbIndicator");
        sbIndicator.setRows(1);
        sbIndicator.setMultiple(false);
        sbIndicator.addOption("1", Application.getInstance().getMessage("messaging.label.red","Red"));
        sbIndicator.addOption("2", Application.getInstance().getMessage("messaging.label.green","Green"));
        sbIndicator.addOption("3", Application.getInstance().getMessage("messaging.label.blue","Blue"));
        sbIndicator.addOption("4", Application.getInstance().getMessage("messaging.label.purple","Purple"));
        sbIndicator.addOption("5", Application.getInstance().getMessage("messaging.label.yellow","Yellow"));
        sbIndicator.addOption("6", Application.getInstance().getMessage("messaging.label.cyan","Cyan"));
        sbIndicator.addOption("7", Application.getInstance().getMessage("messaging.label.pink","Pink"));
        sbIndicator.addOption("8", Application.getInstance().getMessage("messaging.label.gray","Gray"));
        addChild(sbIndicator);

        
        // NOTE: hide the 'deliver to folder' select field, default to INBOX, 
        //       cause we now have filters
        // NOTE: uncommented due to (enhancement) BUG #2308
        addChild(new Label("3", Application.getInstance().getMessage("messaging.label.deliverNewMessagesTo","Deliver New Messages To")));
        sbDeliveryFolderId = new FolderSelectBox("sbDeliveryFolderId");
        sbDeliveryFolderId.setRows(1);
        sbDeliveryFolderId.setMultiple(false);
        sbDeliveryFolderId.addChild(new ValidatorNotEmpty("3"));
        setDeliveryFolderIdOptions();
        addChild(sbDeliveryFolderId);
        

        addChild(new Label("4", Application.getInstance().getMessage("messaging.label.pOP3ServerName","POP3 Server Name")+" *"));
        tfServerName = new TextField("tfServerName");
        tfServerName.addChild(new ValidatorNotEmpty("4"));
        addChild(tfServerName);

        addChild(new Label("5", Application.getInstance().getMessage("messaging.label.pOP3ServerPort","POP3 Server Port")+" *"));
        tfServerPort = new TextField("tfServerPort");
        tfServerPort.setValue("110");
        tfServerPort.addChild(new ValidatorIsNumeric("5"));
        addChild(tfServerPort);

        addChild(new Label("6", Application.getInstance().getMessage("messaging.label.username","Username")+" *"));
        tfUsername = new TextField("tfUsername");
        tfUsername.addChild(new ValidatorNotEmpty("6"));
        addChild(tfUsername);

        addChild(new Label("7", Application.getInstance().getMessage("messaging.label.password","Password")));
        pwPassword = new Password("pwPassword");
        addChild(pwPassword);

        addChild(new Label("17", Application.getInstance().getMessage("messaging.label.confirmPassword","Confirm Password")));
        pwConfirmPassword = new Password("pwConfirmPassword");
        pwConfirmPassword.addChild(new ValidatorNotEmpty("17") {
            public boolean validate(FormField formField) {
                String pw = (pwPassword.getValue() != null) ? pwPassword.getValue().toString() : "";
                String pwc = (pwConfirmPassword.getValue() != null) ? pwConfirmPassword.getValue().toString() : "";
                boolean valid = (pw.trim().length() == 0 && pwc.trim().length() == 0) || pw.equals(pwc);
                if (!valid) {
                    pwPassword.setInvalid(true);
                }
                return valid;
            }
        });
        addChild(pwConfirmPassword);

        addChild(new Label("8", Application.getInstance().getMessage("messaging.label.leaveacopyofemailonserver","Leave a copy of email on server")));
        cbLeaveMailOnServer = new CheckBox("cbLeaveMailOnServer");
        addChild(cbLeaveMailOnServer);

        addChild(new Label("9", "&nbsp;"));
        btSubmit = new Button("10", Application.getInstance().getMessage("messaging.label.submit","Submit"));
        addChild(btSubmit);
    }

    public void onRequest(Event event) {
        setDeliveryFolderIdOptions();
    }


    public Forward onValidate(Event event) {
        MessagingModule mm;
        Pop3Account pop3;

        mm = Util.getMessagingModule();
        pop3 = new Pop3Account();
        pop3.setId(UuidGenerator.getInstance().getUuid());
        pop3.setUserId(getWidgetManager().getUser().getId());

        setPop3AccountAttributes(pop3);

        try {
            mm.addPop3Account(pop3);
            init();
            return new Forward(FORWARD_SUCCESS);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
    }

    protected void setPop3AccountAttributes(Pop3Account pop3) {
        pop3.setFilterEnabled(false);

        pop3.setName(tfName.getValue().toString());
        int indi;
        try {
            List valueList = (List) getSbIndicator().getValue();
            indi = Integer.parseInt(valueList.get(0).toString());
        } catch (Exception e) {
            indi = 1;
        }
        pop3.setIndicator(indi);

        // NOTE: 
        // just deliver by default to INBOX folder instead of alowing user to
        // select
        // NOTE: uncommented due to (enhancement) BUG #2308
        List valueList = (List) getSbDeliveryFolderId().getValue();
        pop3.setDeliveryFolderId(valueList.get(0).toString());
        //MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        //pop3.setDeliveryFolderId(getInboxFolderId());
        
        pop3.setServerName(tfServerName.getValue().toString());
        try {
            pop3.setServerPort(Integer.parseInt(tfServerPort.getValue().toString()));
        } catch (NumberFormatException e) {
            pop3.setServerPort(110);
        }
        pop3.setUsername(tfUsername.getValue().toString());
        if (pwPassword.getValue() != null && pwPassword.getValue().toString().trim().length() > 0) {
            pop3.setPassword(pwPassword.getValue().toString());
        }
        pop3.setLeaveMailOnServer(cbLeaveMailOnServer.isChecked());
    }

    private void setDeliveryFolderIdOptions() {
/*
        sbDeliveryFolderId.removeAllOptions();

        try {
            MessagingModule mm = Util.getMessagingModule();
            Collection folders = mm.getFolders(getWidgetManager().getUser().getId());
            for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
                Folder f = (Folder) iterator.next();
                sbDeliveryFolderId.addOption(f.getFolderId(), f.getName());

                if (f.isSpecialFolder() && Folder.FOLDER_INBOX.equals(f.getName())) {
                    sbDeliveryFolderId.setSelectedOptions(new String[]{f.getId()});
                }
            }
        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
*/
    }

    // === [ getters/setters ] =================================================
    public TextField getTfName() {
        return tfName;
    }

    public void setTfName(TextField tfName) {
        this.tfName = tfName;
    }

    public SelectBox getSbIndicator() {
        return sbIndicator;
    }

    public void setSbIndicator(SelectBox sbIndicator) {
        this.sbIndicator = sbIndicator;
    }

    // NOTE: removed cause we no longer have a SbDeliveryFolder 
    //       select box
    // NOTE: uncommented due to (enhancement) BUG #2308
    public SelectBox getSbDeliveryFolderId() {
        return sbDeliveryFolderId;
    }
    public void setSbDeliveryFolderId(SelectBox sbDeliveryFolderId) {
        this.sbDeliveryFolderId = sbDeliveryFolderId;
    }

    public TextField getTfServerName() {
        return tfServerName;
    }

    public void setTfServerName(TextField tfServerName) {
        this.tfServerName = tfServerName;
    }

    public TextField getTfServerPort() {
        return tfServerPort;
    }

    public void setTfServerPort(TextField tfServerPort) {
        this.tfServerPort = tfServerPort;
    }

    public TextField getTfUsername() {
        return tfUsername;
    }

    public void setTfUsername(TextField tfUsername) {
        this.tfUsername = tfUsername;
    }

    public Password getPwPassword() {
        return pwPassword;
    }

    public void setPwPassword(Password pwPassword) {
        this.pwPassword = pwPassword;
    }

    public CheckBox getCbLeaveMailOnServer() {
        return cbLeaveMailOnServer;
    }

    public void setCbLeaveMailOnServer(CheckBox cbLeaveMailOnServer) {
        this.cbLeaveMailOnServer = cbLeaveMailOnServer;
    }

    public Button getBtSubmit() {
        return btSubmit;
    }

    public void setBtSubmit(Button btSubmit) {
        this.btSubmit = btSubmit;
    }

    
    // private ===
    private String getInboxFolderId() {
    	try {
    		Application application = Application.getInstance();
    		MessagingModule mm = (MessagingModule) application.getModule(MessagingModule.class);
    		String userId = getWidgetManager().getUser().getId();
    		Folder inbox = mm.getSpecialFolder(userId, Folder.FOLDER_INBOX);
    		return inbox.getId();
    	}
    	catch(MessagingException e) {
    		log.error(e);
    		throw new RuntimeException(e);
    	}
    }
}

