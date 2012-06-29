package com.tms.ekms.security.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFormDisplay extends LoginForm
{
    public String getDefaultTemplate()
    {
        return kacang.services.security.ui.LoginForm.DEFAULT_TEMPLATE;
    }

	public void processAction(HttpServletRequest request, HttpServletResponse response)
	{
		//-- Left empty to avoid duplicate logins
	}
}
