<%@ page import="com.tms.portlet.theme.themes.DefaultTheme,
                 com.tms.portlet.Entity"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="themeManager" value="${widget}" scope="request"/>
<script language="javascript" src="<c:url value="/ekms/includes/portlet.js" />"></script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="24%" valign="top">
            <crt:set var="leftColumn" value="<%= DefaultTheme.COLUMN_LEFT_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[leftColumn]}" var="item" varStatus="status">
                <c:set var="columnStatus" value="${status}" scope="request"/>
                <c:set var="entity" value="${item}" scope="request"/>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                    <tr align="center" valign="top">
                        <td>
                            <crt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
                            <c:set var="path" value="${path}${item.portlet.portletClass}"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                            <jsp:include page="<%= (String) pageContext.getAttribute(\"path\") %>"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
                        </td>
                    </tr>
                </table>
                <c:set var="currentCount" value="${requestScope.currentCount+1}" scope="request" />
            </c:forEach>
        </td>
        <td width="52%" valign="TOP">
            <crt:set var="centerColumn" value="<%= DefaultTheme.COLUMN_CENTER_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[centerColumn]}" var="item" varStatus="status" >
                <c:set var="columnStatus" value="${status}" scope="request"/>
                <c:set var="entity" value="${item}" scope="request"/>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                    <tr valign="TOP">
                        <td>
                            <crt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
                            <c:set var="path" value="${path}${item.portlet.portletClass}"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                            <jsp:include page="<%= (String) pageContext.getAttribute(\"path\") %>"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
                        </td>
                    </tr>
                </table>
                <c:set var="currentCount" value="${requestScope.currentCount+1}" scope="request" />
            </c:forEach>
        </td>
        <td width="24%" valign="TOP">
            <crt:set var="rightColumn" value="<%= DefaultTheme.COLUMN_RIGHT_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[rightColumn]}" var="item" varStatus="status" >
                <c:set var="columnStatus" value="${status}" scope="request"/>
                <c:set var="entity" value="${item}" scope="request"/>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                    <tr valign="TOP">
                        <td>
                            <crt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
                            <c:set var="path" value="${path}${item.portlet.portletClass}"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                            <jsp:include page="<%= (String) pageContext.getAttribute(\"path\") %>"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
                        </td>
                    </tr>
                </table>
                <c:set var="currentCount" value="${requestScope.currentCount+1}" scope="request" />
            </c:forEach>
        </td>
    </tr>
</table>