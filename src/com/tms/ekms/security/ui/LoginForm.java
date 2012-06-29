package com.tms.ekms.security.ui;

import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.Date;
import java.net.URLDecoder;

public abstract class LoginForm extends kacang.services.security.ui.LoginForm
{
    public static final String PROPERTY_EKMS_PERMISSION = "kacang.services.security.ekms.ekmsUser";

    private String errorUrl;

    public String getErrorUrl()
    {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl)
    {
        this.errorUrl = errorUrl;
    }

    public void processAction(HttpServletRequest request, HttpServletResponse response)
    {
        Application application = Application.getInstance();
        if (request.getParameter("loginUsername") == null || "".equals(request.getParameter("loginUsername")))
            setMessageEntry("loginUsername", application.getMessage("security.error.usernameRequired", "Please Enter Your Username"));
        if (request.getParameter("loginPassword") == null || "".equals(request.getParameter("loginPassword")))
            setMessageEntry("loginPassword", application.getMessage("security.error.passwordRequired", "Please Enter Your Password"));
        if (getMessage().size() <= 0)
        {
            SecurityService service = (SecurityService) application.getService(SecurityService.class);
            try
            {
                service.login(request, request.getParameter("loginUsername"), request.getParameter("loginPassword"));

                Collection list = service.getUsersByUsername(request.getParameter("loginUsername"));
                //Cookie Handling
                if (list.size() > 0)
                {
                    User user = (User) list.iterator().next();
                    //Checking for ekms permission
                    String ekmsPermission = Application.getInstance().getProperty(PROPERTY_EKMS_PERMISSION);
                    if(!(ekmsPermission == null || "".equals(ekmsPermission)))
                    {
                        if(!(service.hasPermission(user.getId(), ekmsPermission, null, null)))
                        {
                            service.logout(user, request.getSession());
                            request.getSession(true);
                            throw new SecurityException("Specified Principal Is Not An EKMS User: " + user.getUsername());
                        }
                    }
                    if (request.getParameter("rememberPassword") != null)
                    {
                        Cookie cookie = new Cookie(User.COOKIE_USER_KEY, user.getId());
                        cookie.setMaxAge(60 * 60 * 24 * 365);
                        response.addCookie(cookie);
                        cookie = new Cookie(User.COOKIE_PASSWORD_KEY, user.getPassword());
                        cookie.setMaxAge(60 * 60 * 24 * 365);
                        response.addCookie(cookie);
                    }
                    else
                    {
                        Cookie cookie = new Cookie(User.COOKIE_USER_KEY, null);
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        cookie = new Cookie(User.COOKIE_PASSWORD_KEY, null);
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
					Log.getLog(getClass()).write(new Date(), null, user.getId(), "kacang.services.log.security.Login", "User "+ user.getName() + " logged in successful", request.getRemoteAddr(), request.getSession().getId());

	                // added username in session for easier monitoring via session monitoring tools
	                request.getSession().setAttribute("currentUsername", user.getUsername());
					
                }
                // Redirect to forward URL
                try
                {
                    String forwardUrl = getForward();
                    if (forwardUrl == null || "".equals(forwardUrl))
                    {
                        forwardUrl = request.getRequestURL().toString();
                        if (request.getQueryString() != null)
                        {
                            forwardUrl += "?" + request.getQueryString();
                        }
                    }
                    response.sendRedirect(response.encodeRedirectURL(URLDecoder.decode(forwardUrl)));
                }
                catch (Exception e)
                {
                    ;
                }
            } catch (Exception e)
            {
                Log.getLog(LoginFormProcess.class).error(e);
                setMessageEntry("error", application.getMessage("security.message.loginInvalid", "Incorrect Login/Password. Unable To Log In."));
                try
                {
                    String errorUrl = getErrorUrl();
                    if (errorUrl == null || "".equals(errorUrl))
                    {
                        // do nothing
                    }
                    else
                    {
                        response.sendRedirect(response.encodeRedirectURL(errorUrl));
                    }
                }
                catch (Exception re)
                {
                    ;
                }
            }
        }
    }
}
