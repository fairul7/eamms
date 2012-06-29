<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.portlet.portlets.bookmark.BookmarkPreference"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletPreferenceBookmark">
        <com.tms.portlet.portlets.bookmark.BookmarkPreference name="preferenceForm"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= BookmarkPreference.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= BookmarkPreference.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_failed" value="<%= BookmarkPreference.FORWARD_FAILED %>"/>
<c:if test="${! empty param.entityId}">
    <x:set name="portletPreferenceBookmark.preferenceForm" property="entityId" value="${param.entityId}"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>alert("<fmt:message key='portlet.message.preference.bookmarkLinksSaved'/>");</script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<html>
<head>
    <title><fmt:message key='portlet.message.preference.managingBookmarkLinks'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="bookmarkRow">
    <x:display name="portletPreferenceBookmark.preferenceForm"/>
</body>
</html>
