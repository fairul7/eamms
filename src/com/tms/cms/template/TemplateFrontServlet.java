package com.tms.cms.template;

import com.tms.cms.bookmark.Bookmark;
import com.tms.cms.core.model.*;
import com.tms.ekms.setup.model.SetupException;
import com.tms.util.license.TmsLicense;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Front-Controller Servlet that intercepts requests and redirects or dispatches to
 * the appropriate front-end template.
 */
public class TemplateFrontServlet extends HttpServlet {

    public static final String CONTENT_PATH = "/content.jsp";
    public static final String INIT_PARAMETER_KEY_SITE_TEMPLATE = "";
    public static final String PARAMETER_KEY_CONTENT_ID = "id";
    public static final String REQUEST_KEY_SITE_TEMPLATE = "TemplateFrontServlet.SiteTemplate";

    private Log log = Log.getLog(getClass());
    private String configuredTemplate;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        configuredTemplate = servletConfig.getInitParameter(ContentManager.APPLICATION_PROPERTY_SITE_TEMPLATE);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // get content manager
        Application application = Application.getInstance();
        ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
        if (cm == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ContentManager could not be located");
            return;
        }

        // determine type of request
        String requestedPath = request.getPathInfo();

        if (requestedPath == null || requestedPath.trim().length() == 0) {
            // get servlet path (handle jsp:include also)
            requestedPath = (String)request.getAttribute("javax.servlet.include.path_info");
        }

        if (requestedPath == null || requestedPath.trim().length() == 0 || "/".equals(requestedPath)) {
            // redirect to index page
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + request.getServletPath() + "/index.jsp"));
            return;
        }

        // check license
        if (requestedPath.endsWith(".jsp")) {
            boolean validLicense = false;
            try {
                validLicense = TmsLicense.checkLicense();
            } catch (SetupException e) {
            }
            if (!validLicense) {
                response.sendRedirect(request.getContextPath() + TmsLicense.LICENSE_PAGE);
                return;
            }
        }

        // determine client type
        String contentType = request.getContentType();
        String clientType = null;

        // get current site template
        String siteTemplate = configuredTemplate;
        if (siteTemplate == null)
            siteTemplate = (String)request.getAttribute(REQUEST_KEY_SITE_TEMPLATE);
        if (siteTemplate == null) {
            siteTemplate = getSiteTemplate(request, contentType, clientType);
            request.setAttribute(REQUEST_KEY_SITE_TEMPLATE, siteTemplate);
        }

        // get appropriate template
        String pageUrl = null;
        if (CONTENT_PATH.equals(requestedPath)) {
            // content template requested, get content id
            String id = request.getParameter(PARAMETER_KEY_CONTENT_ID);

            String pageTemplate = null;
            try {
                // HACK: check for bookmark/link
                try {
                    if (Bookmark.class.getName().equals(ContentUtil.getClassNameFromKey(id))) {
                        // get object and redirect to URL
                        User user = ((SecurityService)application.getService(SecurityService.class)).getCurrentUser(request);
                        ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                        Bookmark bookmark = (Bookmark)cp.view(id, user);
                        String url = bookmark.getUrl();
                        if (url != null && url.trim().length() > 0) {
                            // add hit statistic
                            boolean preview = ContentUtil.isPreviewRequest(request);
                            if (!preview) {
                                ContentReporter reporter = (ContentReporter)application.getModule(ContentReporter.class);
                                reporter.audit(bookmark.getId(), ContentManager.USE_CASE_VIEW, bookmark.getVersion(), user);
                            }

                            // redirect to url
                            response.sendRedirect(url);
                            return;
                        }
                    }
                }
                catch (InvalidKeyException e) {
                    ;
                }
                catch (DataObjectNotFoundException e) {
                    ;
                }

                // get content-specific template
                pageTemplate = cm.getContentPageTemplateById(id);
                if (pageTemplate == null) {
                    // check parent's template
                    try {
                        User user = ((SecurityService)application.getService(SecurityService.class)).getUser(SecurityService.ANONYMOUS_USER_ID);
                        ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                        ContentObject co = cp.view(id, user);
                        pageTemplate = cm.getContentPageTemplateById(co.getParentId());
                    }
                    catch (Exception e) {
                        ;
                    }
                }
                if (pageTemplate == null || pageTemplate.trim().length() == 0 || (pageTemplate.indexOf("..") >= 0)) {
                    // empty template name or illegal use of .., use default
                    pageTemplate = CONTENT_PATH;
                    pageUrl = ContentUtil.getPageTemplateUrl(siteTemplate, pageTemplate);
                }
                else {
                    // find url for specific template
                    pageUrl = ContentUtil.getPageTemplateUrl(siteTemplate, pageTemplate);
                    if (!exists(request, pageUrl)) {
                        // template doesn't exist, use default
                        pageTemplate = CONTENT_PATH;
                        pageUrl = ContentUtil.getPageTemplateUrl(siteTemplate, pageTemplate);
                    }
                }
            }
            catch (ContentException e) {
                log.error("Unable to retrieve page template for " + id + ": " + e.toString());
                pageTemplate = CONTENT_PATH;
                pageUrl = ContentUtil.getPageTemplateUrl(siteTemplate, pageTemplate);
            }
        }
        else {
            // handle other pages
            pageUrl = ContentUtil.getPageTemplateUrl(siteTemplate, requestedPath);
        }

        // include template
        request.getRequestDispatcher(pageUrl).forward(request, response);

    }

    protected String getSiteTemplate(HttpServletRequest request, String contentType, String clientType) {
        Application application = Application.getInstance();
        ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
        String siteTemplate = cm.getSiteTemplate(contentType, clientType);
        return siteTemplate;
    }

    protected boolean exists(HttpServletRequest request, String url) {
        String path = getServletContext().getRealPath(url);
        File file = new File(path);
        return file.exists() && file.isFile();
    }

}
