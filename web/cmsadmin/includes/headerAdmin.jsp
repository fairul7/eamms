<%@ page pageEncoding="UTF-8" %>
<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 kacang.ui.WidgetManager,
                 com.tms.util.license.TmsLicense"%>
<%
    // check license
    if (!TmsLicense.checkLicense()) {
        if (!response.isCommitted()) {
            response.sendRedirect(request.getContextPath() + TmsLicense.LICENSE_PAGE);
        }
        return;
    }

    // check login
    SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
    User user = security.getCurrentUser(request);
    if (user == null || SecurityService.ANONYMOUS_USER_ID.equals(user.getId())) {
        if (!response.isCommitted()) {
            response.sendRedirect(request.getContextPath() + "/cmsadmin/login.jsp");
        }
        return;
    }
    if (request.getServletPath().startsWith("/cmsadmin/content")) {
        if (WidgetManager.getWidgetManager(request).getWidget("cms.contentTree") == null) {
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/cmsadmin/index.jsp");
            }
            return;
        }
    }
%>
<jsp:include page="../includes/headerMenu.jsp" flush="true" />