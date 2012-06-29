package com.tms.ekms.security.filter;

import com.tms.ekms.security.ui.LoginForm;
import com.tms.util.license.TmsLicense;
import com.tms.ekms.setup.model.SetupException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import kacang.Application;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;

public class MekmsLoginFilter implements Filter {
    public static final String EKMS_PREFIX = "/mekms";
    public static final String REDIRECT_LOGIN = "/mekms/login.jsp";

    private FilterConfig filterConfig;
    private Log log;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        log = Log.getLog(MekmsLoginFilter.class);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // filter don't apply to non EKMS JSP pages
        if (isEkmsPage(request)) {

            // check license
            boolean validLicense = false;
            try {
                validLicense = TmsLicense.checkLicense();
                if(!TmsLicense.PREFIX_EKP.equals(TmsLicense.getLicense().getPrefix())) {
                    // if not EKP license, return false also
                    validLicense = false;
                }
            } catch (SetupException e) {
            }
            if (!validLicense) {
                ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + TmsLicense.LICENSE_PAGE);
                return;
            }

            // check ekms permission
            try {
                SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user = ss.getCurrentUser(request);

                if (user == null || SecurityService.ANONYMOUS_USER_ID.equals(user.getId())) {
                    // redirect
                    ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + REDIRECT_LOGIN);

                } else {
                    // normal user, check for ekms permission
                    boolean hasPermission = ss.hasPermission(user.getId(), LoginForm.PROPERTY_EKMS_PERMISSION, null, null);
                    if (!hasPermission) {
                        // redirect
                        ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + REDIRECT_LOGIN);

                    } else {
                        // process request
                        filterChain.doFilter(request, servletResponse);
                    }
                }


            } catch (SecurityException e) {
                throw new ServletException(e.getMessage(), e);
            }

        } else {
            // process request
            filterChain.doFilter(request, servletResponse);
        }


    }

    private boolean isEkmsPage(HttpServletRequest request) {
        if(request.getServletPath().startsWith(EKMS_PREFIX) &&
                !request.getServletPath().endsWith(REDIRECT_LOGIN)) {
            return true;
        } else {
            return false;
        }
    }

    public void destroy() {
    }
}
