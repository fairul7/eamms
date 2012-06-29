package com.tms.hr.claim.model;

import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;

import kacang.services.security.User;

import kacang.ui.Event;


public class Mailer {
    public static void notifyApproversOnSubmit(Event evt,
        ClaimFormIndex indexObj) throws Exception {
        /// getting all the needed objects and info
        String userId = evt.getWidgetManager().getUser().getId();
        MessagingModule mm = Util.getMessagingModule();
        User uSubmitter = UserUtil.getUser(userId);

        /// construct the message body
        StringBuffer sb = new StringBuffer();
        sb.append("<U>Online expenses form submitted by " +
            uSubmitter.getName() + " </U>");
        sb.append("<BR> ");
        sb.append("<BR> " + uSubmitter.getName() +
            " has submitted a expenses form.");
        sb.append("<br><br><a href=\"http://" +
            evt.getRequest().getServerName() + ":" +
            evt.getRequest().getServerPort() +
            evt.getRequest().getContextPath() +
            "/ekms/claim/view_claim.jsp?formId=" + indexObj.getFormId() +
            "\">Click here to view.</a>");
        sb.append("<BR><br> Thank you.");

        String to = "";

        User uApprover1 = null;

        try {
            if (UserUtil.getUser(indexObj.getUserApprover1()) != null) {
            	uApprover1 = UserUtil.getUser(indexObj.getUserApprover1());
            }

            

            if (uApprover1 != null) {
                to = uApprover1.getUsername() + "@" +
                    MessagingModule.INTRANET_EMAIL_DOMAIN;
                mm.sendStandardHtmlEmail(userId, to, "", "",
                    " Expenses: Online Form Submission - " +
                    indexObj.getRemarks(),
                    " Title: Online Expenses Submission - " +
                    indexObj.getRemarks(), sb.toString());
            }
        } catch (Exception e) {
            //this person doesnt need approver
        }

        User uApprover2 = null;

        try {
            if (indexObj.getUserApprover2() != null &&
            		!indexObj.getUserApprover2().equals("x") &&
            		UserUtil.getUser(indexObj.getUserApprover2()) != null) {
                uApprover2 = UserUtil.getUser(indexObj.getUserApprover2());
            }

            if (uApprover2 != null) {
                to = uApprover2.getUsername() + "@" +
                    MessagingModule.INTRANET_EMAIL_DOMAIN;
                mm.sendStandardHtmlEmail(userId, to, "", "",
                    " Expenses: Online Form Submission - " +
                    indexObj.getRemarks(),
                    " Title: Online Expenses Submission - " +
                    indexObj.getRemarks(), sb.toString());
            }
        } catch (Exception e) {
            //this person doesnt need approver2
        }
    }

    /// send the emails
    public static void notifyOwnerOnProcess(Event evt, ClaimFormIndex indexObj)
        throws Exception {
        /// getting all the needed objects and info
        String userId = evt.getWidgetManager().getUser().getId();
        MessagingModule mm = Util.getMessagingModule();
        User uAssessor = UserUtil.getUser(userId);
        User uOwner = UserUtil.getUser(indexObj.getUserOwner());
        User uOriginator = UserUtil.getUser(indexObj.getUserOriginator());

        /// construct the message body
        StringBuffer sb = new StringBuffer();

        //sb.append("<U>Your Expenses Has Been Approved</U>" );
        //sb.append("<BR><BR>" );
        sb.append("<br>Dear Claimant,");
        sb.append("<br>Your expenses has been approved.");
        sb.append("<br><br><a href=\"http://" +
            evt.getRequest().getServerName() + ":" +
            evt.getRequest().getServerPort() +
            evt.getRequest().getContextPath() +
            "/ekms/claim/view_claim.jsp?formId=" + indexObj.getFormId() +
            "\">Click here to view.</a>");
        sb.append("<BR><br>Thank you.");

        String to = "";

        if (uOwner != null) {
            to = uOwner.getUsername() + "@" +
                MessagingModule.INTRANET_EMAIL_DOMAIN;
            mm.sendStandardHtmlEmail(userId, to, "", "",
                " Your expenses has been approved for " + indexObj.getRemarks(),
                " Title: " + indexObj.getRemarks(), sb.toString());
        }

        if ((uOriginator != null) &&
                !uOriginator.getName().equals(uOwner.getName())) {
            to = uOriginator.getUsername() + "@" +
                MessagingModule.INTRANET_EMAIL_DOMAIN;
            mm.sendStandardHtmlEmail(userId, to, "", "",
                " Subject: " + indexObj.getRemarks(),
                " Title: " + indexObj.getRemarks(), sb.toString());
        }
    }
}
