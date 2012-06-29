package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.*;

public class ServerView extends Form {
    private SelectBox sbPop3Account;
    private Button btCheck;
    private Button btCheckAll;
    private CheckBox cbPreview;

    private String errorMessage;
    private transient MessagingUserStatus mus;
    

    public void init() {
        super.init();

        setMethod("post");
        sbPop3Account = new SelectBox("sbPop3Account");
        addChild(sbPop3Account);
        btCheck = new Button("btCheck", "Check");
        addChild(btCheck);
        btCheckAll = new Button("btCheckAll", "Check All");
        addChild(btCheckAll);

        cbPreview = new CheckBox("cbPreview", "Enable email preview", false);
        addChild(cbPreview);

        try {
            mus =  MessagingQueue.getInstance().getUserStatus(getWidgetManager().getUser().getId());
        } catch (MessagingException e) {
            Log.getLog(ServerView.class).error("Error getting server view tracker", e);
        }
    }

    public void onRequest(Event evt) {
        MessagingModule mm;
        Collection accounts;
        Pop3Account pop3Account;

        errorMessage = null;

        try {
            sbPop3Account.removeAllOptions();
            sbPop3Account.addOption("", "---Select POP3 Account---");
            mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
            accounts = mm.getAccounts(evt.getWidgetManager().getUser().getId());
            for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = (Account) iterator.next();
                if (Account.ACCOUNT_TYPE_POP3 == account.getAccountType()) {
                    // add POP3 account to list
                    pop3Account = (Pop3Account) account;
                    sbPop3Account.addOption(pop3Account.getAccountId(), pop3Account.getName() + " (" + pop3Account.getServerName() + ")");
                }
            }
        } catch (MessagingException e) {
            Log.getLog(ServerView.class).error("Error getting POP3 accounts", e);
            errorMessage = "Error getting POP3 accounts";
        }
    }

    public Forward onValidate(Event evt) {
        String accountId;
        String buttonClicked = findButtonClicked(evt);

        errorMessage = null;

        if(!mus.isServerViewBusy()) {
            if (buttonClicked != null && buttonClicked.endsWith("btCheck")) {
                accountId = (String) ((List) getSbPop3Account().getValue()).get(0);
                if (accountId == null || accountId.trim().length() == 0) {
                    errorMessage = "Select POP3 account to check for email";
                } else {
                    doPeek(Util.getUser(evt), new String[]{accountId});
                }

            } else if (buttonClicked != null && buttonClicked.endsWith("btCheckAll")) {
                Set keySet = getSbPop3Account().getOptionMap().keySet();
                List accountIdList = new ArrayList();

                for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                    accountId = (String) iterator.next();
                    if (accountId.trim().length() > 0) {
                        accountIdList.add(accountId);
                    }
                }
                doPeek(Util.getUser(evt), (String[]) accountIdList.toArray(new String[0]));
            }

            return new Forward("process");
        } else {
            return new Forward("idle");
        }
    }

    private void doPeek(User user, String[] accountIds) {
        MessagingModule mm;
        List accountIdList;

        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        accountIdList = Arrays.asList(accountIds);
        mm.downloadPop3MessageSummary(user.getId(), cbPreview.isChecked(), accountIdList);
    }

    public boolean isServerViewBusy() {
        return mus.isServerViewBusy();
    }


    // === [ getters/setters ] =================================================
    public SelectBox getSbPop3Account() {
        return sbPop3Account;
    }

    public void setSbPop3Account(SelectBox sbPop3Account) {
        this.sbPop3Account = sbPop3Account;
    }

    public Button getBtCheck() {
        return btCheck;
    }

    public void setBtCheck(Button btCheck) {
        this.btCheck = btCheck;
    }

    public Button getBtCheckAll() {
        return btCheckAll;
    }

    public void setBtCheckAll(Button btCheckAll) {
        this.btCheckAll = btCheckAll;
    }

    public Date getViewUpdate() {
        return mus.getServerViewUpdateTime();
    }

    public Map getDataMap() {
        return mus.getDataMap();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CheckBox getCbPreview() {
        return cbPreview;
    }

    public void setCbPreview(CheckBox cbPreview) {
        this.cbPreview = cbPreview;
    }
}
