<%@ page import="com.tms.cms.webdav.ContentWebdavServlet,
                 kacang.model.DataObjectNotFoundException,
                 com.tms.cms.core.ui.EditOfficeContentObjectPanel,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 com.tms.cms.core.model.ContentManager,
                 kacang.util.Log,
                 java.net.URLEncoder"%>
<%@ include file="/common/header.jsp" %>


<c:if test="${currentUser.id != 'anonymous'}">
<%
    // determine document path
    String fullPath = null;
    if (request.getParameter(EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH) != null) {
        try {
            String filename = request.getParameter(EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_FILE);
            // check for office extensions, if none append .doc
            int i = filename.lastIndexOf(".");
            if (i < 0) {
                filename += ".doc";
            }

            fullPath = request.getParameter(EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH) + "/" + filename;

            String documentId = ContentWebdavServlet.lookupContentObject(request, response, fullPath).toString();
            //session.setAttribute(EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH, documentId);
            pageContext.setAttribute(EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH, documentId);
        }
        catch(DataObjectNotFoundException e) {
            e.printStackTrace();
            Log.getLog(ContentWebdavServlet.class).debug("Error retrieving office document " + fullPath + ": " + e.toString());
            response.sendRedirect("documentNotFound.jsp");
            return;
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.getLog(ContentWebdavServlet.class).error("Error retrieving office document " + fullPath, e);
            response.sendRedirect("documentNotFound.jsp");
            return;
        }
    }
    else {
        response.sendRedirect("documentNotFound.jsp");
        return;
    }
%>
</c:if>

<c-rt:set var="sessionKey" value="<%= EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH %>"/>
<x:template type="TemplateProcessLoginForm" />
<c:if test="${currentUser.id != 'anonymous'}">
    <x:permission permission="<%= ContentManager.PERMISSION_MANAGE_CONTENT %>" module="<%= ContentManager.class.getName() %>" var="canEdit"/>
    <c:if test="${canEdit}">
        <script>
        location.href='<c:url value="contentEditOffice.jsp?id=${pageScope[sessionKey]}"/>';
        </script>
    </c:if>
</c:if>


<jsp:include page="/cmsadmin/blank.jsp" flush="true" />

<!-- Header -->
<HTML>
<HEAD>
<TITLE><fmt:message key='headermenu.label.mainTitle'/></TITLE>
<link rel="stylesheet" href="<c:url value="/cmsadmin/styles/style.css"/>">
<script>
<!--
    function loadFocus() {
        var obj = document.getElementById("loginUsername");
        if (obj != null && obj.value == '')
            obj.focus();
    }
//-->
</script>
</HEAD>
<BODY leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="#E2E2E2" onload="loadFocus()">

<TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="images/bannerbg.gif">
<TR>
	<TD width="407"><img src="<c:url value="/cmsadmin/images/banner.gif"/>"></TD>
	<TD valign="top" align="right">
	</TD>
	<TD bgcolor="#CCCCCC" width="1"><img src="<c:url value="/cmsadmin/images/images/clear.gif"/>" width="1"></TD>
	<TD align="right" valign="bottom">
	</TD>
</TR>
</TABLE>
<img src="images/clear.gif" height="1"><br>

<!-- End Header -->

<blockquote>
<table width="300">
  <tr>
    <td>
        <hr size="1">
        <fmt:message key='general.label.welcome'/>.<br>

<form method="post" name="loginForm">
    <table width="100%" class="loginTable" cellpadding="2" cellspacing="0">
        <tr>
            <td class="loginLabel" width="20%"><fmt:message key="security.label.username"/>: </td>
            <td class="loginError"><input type="text" id="loginUsername" name="loginUsername" size="15"> <c:out value="${widget.message.loginUsername}"/></td>
        </tr>
        <tr>
            <td class="loginLabel" width="20%"><fmt:message key="security.label.password"/>: </td>
            <td class="loginError"><input type="password" name="loginPassword" size="15"> <c:out value="${widget.message.loginPassword}"/></td>
        </tr>
        <c:if test="${!(empty widget.message.error)}">
            <tr>
                <td>&nbsp;</td>
                <td class="loginError"><c:out value="${widget.message.error}"/></td>
            </tr>
        </c:if>
        <tr>
            <td>&nbsp;</td>
            <td>
            <input type="hidden" name="action" value="Login">
            <input type="submit" value="<fmt:message key="security.label.login"/>">
            </td>
        </tr>
    </table>
    <input type="hidden" name="<%= EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH %>" value="<%= request.getParameter( EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_PATH ) %>">
    <input type="hidden" name="<%= EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_FILE %>" value="<%= request.getParameter( EditOfficeContentObjectPanel.SESSION_KEY_MSOFFICE_FILE ) %>">
</form>

    </td>
  </tr>
</table>
</blockquote>

</BODY>
</HTML>
