package com.tms.ekms.security.filter;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.tms.ekms.security.ui.LoginForm;
import com.tms.util.license.TmsLicense;
import com.tms.ekms.setup.model.SetupException;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.net.URLEncoder;

public class EkmsLoginFilter implements Filter {

    public static final String EKMS_PREFIX = "/ekms";
    public static final String MEKMS_PREFIX = "/mekms";
    public static final String REDIRECT_LOGIN = "/ekms/login.jsp";
    public static final String REDIRECT_LOGIN_M = "/mekms/login.jsp";
    public static final String EKMS_USER_KEY = "ekmsUser";
    public static final int EKMS_USER_CACHE_DURATION = 60;

    private static Cache ekmsUserCache;

    static {
        ekmsUserCache = new Cache(true, false, true);
    }


    private FilterConfig filterConfig;
    private static Log log = Log.getLog(EkmsLoginFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // filter don't apply to non EKMS JSP pages
        if (isEkmsPage(request)) {

            // check license
            boolean validLicense = false;
            try {
                validLicense = TmsLicense.checkLicense();
                if (!TmsLicense.PREFIX_EKP.equals(TmsLicense.getLicense().getPrefix())) {
                    // if not EKP license, return false also
                    validLicense = false;
                }
                if (validLicense) {
                    //Checking for number of users
                    try {
                        Integer userCount = null;
                        try {
                            userCount = (Integer)ekmsUserCache.getFromCache(EKMS_USER_KEY, EKMS_USER_CACHE_DURATION);
                        }
                        catch (NeedsRefreshException e) {
                            ;
                        }
                        if (userCount == null) {
                            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                            Collection users = service.getUsersByPermission(LoginForm.PROPERTY_EKMS_PERMISSION, Boolean.FALSE, null, false, 0, -1);
                            userCount = new Integer(users.size());
                            ekmsUserCache.putInCache(EKMS_USER_KEY, userCount);
                        }
                        int registeredFor = TmsLicense.getLicense().getMaxLicense();
                        if (registeredFor == 0 || registeredFor == -1) {
                            validLicense = true;
                        }
                        else if (userCount.intValue() <= registeredFor) {
                            validLicense = true;
                        }
                        else {
                            validLicense = false;
                            ekmsUserCache.flushEntry(EKMS_USER_KEY);
                        }
                    }
                    catch (Exception e) {
                        log.error("Error while checking for number of users", e);
                    }
                }
            }
            catch (SetupException e) {
            }
            if (!validLicense) {
                ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + TmsLicense.LICENSE_PAGE);
                return;
            }

            String msg = request.getHeader("Authorization");
            if (msg != null && msg.startsWith("NTLM ")) {
                // currently authenticating using NTLM, don't check for ekms permission yet
                filterChain.doFilter(request, servletResponse);
            }
            else {
                // check ekms permission
                try {
                    SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
                    User user = ss.getCurrentUser(request);

                    if (user == null || SecurityService.ANONYMOUS_USER_ID.equals(user.getId())) {
                        // redirect
                        if (request.getServletPath().startsWith(MEKMS_PREFIX))
                            ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + REDIRECT_LOGIN_M);
                        else
						{
							String absoluteUrl = URLEncoder.encode(request.getRequestURL().toString());
                            if (request.getQueryString() != null) {
                                absoluteUrl += URLEncoder.encode("?" + request.getQueryString());
                            }
							((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + REDIRECT_LOGIN + "?continue=" + absoluteUrl);
						}
                    }
                    else {
                        // normal user, check for ekms permission
                        boolean hasPermission = ss.hasPermission(user.getId(), LoginForm.PROPERTY_EKMS_PERMISSION, null, null);
                        if (!hasPermission) {
                            // redirect
                            if (request.getServletPath().startsWith(MEKMS_PREFIX))
                                ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + REDIRECT_LOGIN_M);
                            else
                                ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + REDIRECT_LOGIN);

                        }
                        else {
                            // process request
                            filterChain.doFilter(request, servletResponse);
                        }
                    }


                }
                catch (SecurityException e) {
                    throw new ServletException(e.getMessage(), e);
                }
            }
        }
        else {
            // process request
            filterChain.doFilter(request, servletResponse);
        }


    }

    private boolean isEkmsPage(HttpServletRequest request) {
        if (request.getServletPath().startsWith(EKMS_PREFIX) &&
                !request.getServletPath().endsWith(REDIRECT_LOGIN)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void destroy() {
    }
}
