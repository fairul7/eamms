package com.tms.cms.document;

import kacang.Application;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;

import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentManager;
import com.tms.util.security.NtlmHttpFilter;

/**
 * Servlet filter to check for permissions before allowing access to a document URL.
 */
public class DocumentStorageFilter implements Filter {

    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String path = null;
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        // check path
        path = getPathInfo(req, resp);

        if (path.startsWith("/documents/")) {

            try {
                // check ntlm logon
                doNtlmLogon(req, resp);

                // check for permission
                Application application = Application.getInstance();
                SecurityService security = (SecurityService)application.getService(SecurityService.class);
                User user = security.getCurrentUser(req);

                // locate content Id
                String id = null;
                StringTokenizer st = new StringTokenizer(path, "/");
                if (st.hasMoreTokens()) {
                    st.nextToken();
                }
                if (st.hasMoreTokens()) {
                	String tmp = st.nextToken();
                	if (!tmp.startsWith("com.")) {
                		id = st.nextToken();
                	}
                	else {
                		id = tmp;
                	}
                }

                if (id != null) {
                    // determine latest published version for document
                    ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
                    Document co = (Document)contentManager.view(id, user);

                    // check permission
                    if (!contentManager.hasPermission(co, user.getId(), ContentManager.USE_CASE_VIEW)) {
                        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error checking permission for " + path + ": " + e);
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }

    protected String getPathInfo(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        if (path == null) {
            path = request.getParameter("name");
        }
        return (path != null) ? path : "";
    }

    protected void doNtlmLogon(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession ssn = req.getSession(true);
        if (ssn != null && ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME) != null
            && !Boolean.valueOf((String) ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE)).booleanValue()) {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService) app.getService(SecurityService.class);

            String ntlmUsername = (String)ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME);
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
