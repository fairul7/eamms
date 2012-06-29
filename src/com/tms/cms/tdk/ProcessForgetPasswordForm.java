package com.tms.cms.tdk;

import kacang.services.security.User;
import kacang.services.security.ui.ForgetPasswordFormProcess;

import javax.servlet.http.HttpServletRequest;

import com.tms.util.MailUtil;

public class ProcessForgetPasswordForm extends ForgetPasswordFormProcess {

    protected void sendEmail(HttpServletRequest request, User user, String password) {
        String mailMessage = "Your password has been reset to " + password + ". Please log in using the username " + user.getUsername() + " and the stated password and change it from your profile.";
        MailUtil.sendEmail(null, false, null, request.getParameter("forgetPasswordEmail"), "Your Password For " + request.getServerName(), mailMessage);
    }

}
