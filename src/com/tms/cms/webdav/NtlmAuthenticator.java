package com.tms.cms.webdav;

import com.tms.util.security.NtlmHttpFilter;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * NTLM Authenticator
 */
public class NtlmAuthenticator extends DigestAuthenticator {

    private String defaultDomain;
    private String domainController;
    private boolean loadBalance;
    private boolean enableBasic;
    private boolean insecureBasic;
    private String realm;


    public NtlmAuthenticator() {
        /* Set jcifs properties we know we want; soTimeout and cachePolicy to 10min.
         */
        Config.setProperty("jcifs.smb.client.soTimeout", "30000");
        Config.setProperty("jcifs.netbios.cachePolicy", "600");

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

    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result = false;
        UniAddress dc;
        String msg;

        NtlmPasswordAuthentication ntlm = null;
        msg = request.getHeader("Authorization");
        boolean offerBasic = enableBasic && (insecureBasic || request.isSecure());

        try {
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
                    if ((ntlm = NtlmSsp.authenticate(request, response, challenge)) == null) {
                        return false;
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
                    HttpSession ssn = request.getSession(true);
                    String ntlmUsername = ntlm.getUsername();
                    ssn.setAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME, ntlmUsername);

                    try {

                        Application app = Application.getInstance();
                        SecurityService security = (SecurityService) app.getService(SecurityService.class);
                        // login to kacang
                        security.loginWithoutPassword(ssn, ntlmUsername);
                        result = true;
                    }
                    catch (Exception e) {
                        Log.getLog(getClass()).debug("Error logging in user " + request.getRemoteUser() + ": " + e.toString(), e);
                        result = false;
                    }
                    ssn.setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");

                }
                catch (SmbAuthException sae) {
                    // set flag (only authenticate once)
                    request.getSession(true).setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");
                }
            }
            else {
                HttpSession ssn = request.getSession(true);
                String ntlmUsername;
                if (ssn == null || (ntlmUsername = (String)
                        ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME)) == null) {
                    // send challenge
                    sendChallenge(request, response);
                }
                else {
                    // check flag (only authenticate once)
                    boolean ntlmOnce = Boolean.valueOf((String) ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE)).booleanValue();
                    if (!ntlmOnce) {
                        try {

                            Application app = Application.getInstance();
                            SecurityService security = (SecurityService) app.getService(SecurityService.class);
                            // login to kacang
                            security.loginWithoutPassword(ssn, ntlmUsername);
                            result = true;
                        }
                        catch (Exception e) {
                            Log.getLog(getClass()).debug("Error logging in user " + request.getRemoteUser() + ": " + e.toString(), e);
                            result = false;
                        }
                        ssn.setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");
                    }
                    else {
                        result = true;
                    }
                }
            }
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Error in NTLM Authentication: " + e.toString(), e);
            throw e;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error in NTLM Authentication: " + e.toString(), e);
            throw new IOException("Error in NTLM Authentication: " + e.toString());
        }
        return result;
    }

    public void sendChallenge(HttpServletRequest request, HttpServletResponse response) {
        boolean offerBasic = enableBasic && (insecureBasic || request.isSecure());
        response.setHeader("WWW-Authenticate", "NTLM");
        if (offerBasic) {
            response.addHeader("WWW-Authenticate", "Basic realm=\"" +
                    realm + "\"");
        }
        response.setHeader("Connection", "close");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.flushBuffer();
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Error flushing response", e);
        }
    }

}
