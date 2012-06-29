package com.tms.cms.tdk;

import kacang.services.security.User;
import kacang.services.security.ui.SignupFormProcess;

import javax.servlet.http.HttpServletRequest;

import com.tms.util.MailUtil;

public class ProcessSignupForm extends SignupFormProcess {

    protected void sendEmail(HttpServletRequest request, User user) {
        //Send activation mail
        String path = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/cms/userActivate.jsp";
        String mailMessage = "Thank you for your registration to " + request.getServerName() + ". Please click <a href=\"" + path + "?uid=" + user.getId() + "&qualifier=" + user.getPassword() + "\">here</a> to activate your account.";
        MailUtil.sendEmail(null, true, null, (String) user.getProperty("email1"), "Registration Activation", mailMessage);
    }

}
