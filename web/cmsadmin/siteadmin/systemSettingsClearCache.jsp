<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.SystemSettings" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%
    if (Boolean.valueOf(request.getParameter("clearCache")).booleanValue()) {
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        ss.flushPermissionCache();
%>
        <x:flush />
<%
        response.sendRedirect(response.encodeRedirectURL("systemSettings.jsp"));
    }
%>