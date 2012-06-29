package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.*;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.Application;

import java.util.ArrayList;
import java.util.Collection;

public class Pop3AccountTable extends Table {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_NEW_POP3_ACCOUNT = "newPop3Account";

    public void init() {
        super.init();

        setWidth("100%");
        setModel(new Pop3AccountTableModel());
    }

    class Pop3AccountTableModel extends TableModel {
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Log log = Log.getLog(getClass());
            log.debug("~~~ action = " + action);

            try {
                if (action.equalsIgnoreCase("delete")) {
                    Account account;
                    MessagingModule mm = Util.getMessagingModule();

                    for (int i = 0; i < selectedKeys.length; i++) {
                        account = mm.getAccount(selectedKeys[i]);

                        if(Account.ACCOUNT_TYPE_POP3 == account.getAccountType()) {
                            mm.deletePop3Account(account.getAccountId());
                        }
                    }

                } else if (action.equalsIgnoreCase("newPop3Account")) {
                    return new Forward(FORWARD_NEW_POP3_ACCOUNT);
                }

            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
                evt.getRequest().getSession().setAttribute("error", e);
                return new Forward(FORWARD_ERROR);
            }

            return new Forward(FORWARD_SUCCESS);
        }

        public Pop3AccountTableModel() {
            try {
                addAction(new TableAction("newPop3Account", Application.getInstance().getMessage("messaging.label.newPOP3Account","New POP3 Account")));
                addAction(new TableAction("delete", Application.getInstance().getMessage("messaging.label.delete","Delete"), Application.getInstance().getMessage("messaging.label.deleteselectedPOP3accounts","Delete selected POP3 account(s)?")));

                TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("messaging.label.accountName","Account Name"));
                tcName.setUrlParam("accountId");
                addColumn(tcName);

                TableColumn tcServerName = new TableColumn("serverName", Application.getInstance().getMessage("messaging.label.serverName","Server Name"));
                addColumn(tcServerName);

            } catch (Exception e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }

        public String getTableRowKey() {
            return "accountId";
        }

        public Collection getTableRows() {
            try {
                MessagingModule mm = Util.getMessagingModule();
                return mm.getAccounts(getWidgetManager().getUser().getId(), getStart(), getRows(), getSort(), isDesc(), "accountType", "" + Account.ACCOUNT_TYPE_POP3);

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            try {
                MessagingModule mm = Util.getMessagingModule();
                return mm.getNumOfAccount(getWidgetManager().getUser().getId(), "accountType", "" + Account.ACCOUNT_TYPE_POP3);

            } catch (MessagingException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
                return 0;
            }
        }
    }
}
