package com.tms.util.security;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.http.NtlmSsp;
import jcifs.netbios.NbtAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.util.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * This servlet Filter can be used to negotiate password hashes with
 * MSIE clients using NTLM SSP. This is similar to <tt>Authentication:
 * BASIC</tt> but weakly encrypted and without requiring the user to re-supply
 * authentication credentials.
 * <p>
 * Read <a href="../../../ntlmhttpauth.html">jCIFS NTLM HTTP Authentication and the Network Explorer Servlet</a> for complete details.
 */

public class NtlmHttpFilter implements Filter {

    public static final String NTLM_AUTH_ONCE = "NtlmHttpFilter_ntlmOnce";
    public static final String NTLM_AUTH_USERNAME = "NtlmHttpFilter_AuthUsername";

    private String defaultDomain;

    private String domainController;

    private boolean loadBalance;

    private boolean enableBasic;

    private boolean insecureBasic;

    private String realm;

    public void init(FilterConfig filterConfig) throws ServletException {
        String name;

        /* Set jcifs properties we know we want; soTimeout and cachePolicy to 10min.
         */
        Config.setProperty("jcifs.smb.client.soTimeout", "300000");
        Config.setProperty("jcifs.netbios.cachePolicy", "600");

        Enumeration e = filterConfig.getInitParameterNames();
        while (e.hasMoreElements()) {
            name = (String) e.nextElement();
            if (name.startsWith("jcifs.")) {
                Config.setProperty(name, filterConfig.getInitParameter(name));
            }
        }
        defaultDomain = Config.getProperty("jcifs.smb.client.domain");
        domainController = Config.getProperty("jcifs.http.domainController");
        if (domainController == null) {
            domainController = defaultDomain;
            loadBalance = Config.getBoolean("jcifs.http.loadBalance", true);
        }
        enableBasic = Boolean.valueOf(
                Config.getProperty("jcifs.http.enableBasic")).booleanValue();
        insecureBasic = Boolean.valueOf(
                Config.getProperty("jcifs.http.insecureBasic")).booleanValue();
        realm = Config.getProperty("jcifs.http.basicRealm");
        if (realm == null) realm = "jCIFS";
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req;
        HttpServletResponse resp;
        UniAddress dc;
        String msg;

        NtlmPasswordAuthentication ntlm = null;
        req = (HttpServletRequest) request;
        resp = (HttpServletResponse) response;
        msg = req.getHeader("Authorization");
        boolean offerBasic = enableBasic && (insecureBasic || req.isSecure());

        try {
            // HACK: Ignore single signon if user attempting to login
            if (!"Login".equals(req.getParameter("action"))) {

                if (msg != null && (msg.startsWith("NTLM ") ||
                        (offerBasic && msg.startsWith("Basic ")))) {
                    if (loadBalance) {
                        dc = new UniAddress(NbtAddress.getByName(domainController, 0x1C, null));
                    }
                    else {
                        dc = UniAddress.getByName(domainController, true);
                    }

                    // get NTML authentication
                    if (msg.startsWith("NTLM ")) {
                        byte[] challenge = SmbSession.getChallenge(dc);
                        if ((ntlm = NtlmSsp.authenticate(req, resp, challenge)) == null) {
                            return;
                        }
                    }
                    else {
                        String auth = new String(Base64.decode(msg.substring(6)),
                                "US-ASCII");
                        int index = auth.indexOf(':');
                        String user = (index != -1) ? auth.substring(0, index) : auth;
                        String password = (index != -1) ? auth.substring(index + 1) :
                                "";
                        index = user.indexOf('\\');
                        if (index == -1) index = user.indexOf('/');
                        String domain = (index != -1) ? user.substring(0, index) :
                                defaultDomain;
                        user = (index != -1) ? user.substring(index + 1) : user;
                        ntlm = new NtlmPasswordAuthentication(domain, user, password);
                    }

                    // SMB logon
                    try {
                        SmbSession.logon(dc, ntlm);
                        req.getSession().setAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME, ntlm.getUsername());

                        doLogon(req, resp, offerBasic);
                    }
                    catch (SmbAuthException sae) {
                        // set flag (only authenticate once)
                        req.getSession(true).setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");
                    }
                }
                else {
                    doLogon(req, resp, offerBasic);
                }
            }
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Error in NTLM Filter: " + e.toString(), e);
            throw e;
        }
        catch (ServletException e) {
            Log.getLog(getClass()).error("Error in NTLM Filter: " + e.toString(), e);
            throw e;
        }

        chain.doFilter(new NtlmHttpServletRequest(req, ntlm), response);
    }

    protected void doLogon(HttpServletRequest req, HttpServletResponse resp, boolean offerBasic) throws IOException {
        Application app = Application.getInstance();
        SecurityService security = (SecurityService) app.getService(SecurityService.class);

        HttpSession ssn = req.getSession(true);
        String ntlmUsername;
        if (ssn != null && !SecurityService.ANONYMOUS_USER_ID.equals(security.getCurrentUser(req).getId())) {
            ssn.setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");
        }
        else if (ssn == null || (ntlmUsername = (String)
                ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME)) == null) {
            // send challenge
            resp.setHeader("WWW-Authenticate", "NTLM");
            if (offerBasic) {
                resp.addHeader("WWW-Authenticate", "Basic realm=\"" +
                        realm + "\"");
            }
            resp.setHeader("Connection", "close");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }
        else {
            // check flag (only authenticate once)
            boolean ntlmOnce = Boolean.valueOf((String) ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE)).booleanValue();
            if (!ntlmOnce) {
                try {

                    // login to kacang
                    security.loginWithoutPassword(ssn, ntlmUsername);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).debug("Error logging in user " + req.getRemoteUser() + ": " + e.toString(), e);
                }
                ssn.setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");
            }
        }
    }

    // Added by cgross to work with weblogic 6.1.
    public void setFilterConfig(FilterConfig f) {
        try {
            init(f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FilterConfig getFilterConfig() {
        return null;
    }
}

