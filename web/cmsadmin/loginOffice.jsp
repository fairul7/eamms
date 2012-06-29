<%@ page import="com.tms.cms.webdav.ContentWebdavServlet,
                 kacang.model.DataObjectNotFoundException,
                 com.tms.cms.core.ui.EditContentObjectPanel,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 com.tms.cms.core.model.ContentManager"%>
<%@ include file="/common/header.jsp" %>

<%
    if (request.getParameter(EditContentObjectPanel.SESSION_KEY_MSOFFICE_PATH) != null) {
        String url = "office/loginOffice.jsp?"
                + EditContentObjectPanel.SESSION_KEY_MSOFFICE_PATH + "=" + request.getParameter(EditContentObjectPanel.SESSION_KEY_MSOFFICE_PATH)
                + "&" + EditContentObjectPanel.SESSION_KEY_MSOFFICE_FILE + "=" + request.getParameter(EditContentObjectPanel.SESSION_KEY_MSOFFICE_FILE);
        response.sendRedirect(response.encodeRedirectURL(url));
        return;
    }
    else {
        response.sendRedirect("office/documentNotFound.jsp");
    }
%>
