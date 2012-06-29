<%@ page import="com.tms.cms.core.model.ContentManager,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 kacang.services.security.User,
                 java.util.Collection,
                 com.tms.cms.section.Section,
                 java.util.Iterator,
                 java.util.Map,
                 org.apache.commons.collections.SequencedHashMap,
                 com.tms.cms.core.model.ContentObject,
                 com.tms.cms.core.model.ContentAcl"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="contentSecurityPopup">
        <com.tms.cms.core.ui.AclContentTree name="tree" />
    </page>
</x:config>

<c:if test="${!empty param.principalId}">
    <c:set var="principalId" value="${param.principalId}"/>
    <c:set var="group" value="${param.group}"/>
    <x:set name="contentSecurityPopup.tree" property="principalId" value="${principalId}"/>
    <x:set name="contentSecurityPopup.tree" property="group" value="${group}"/>
</c:if>

<html>
<head>
    <title><fmt:message key="cms.label.contentPermissions"/></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">
</head>

<body onload="focus()">

<c:set var="principal" value="${widgets['contentSecurityPopup.tree'].principal}" />
<h3><fmt:message key="cms.label.contentPermissions"/></h3>
<c:choose>
    <c:when test="${principal.class.name=='kacang.services.security.User'}">
        <b><fmt:message key="security.label.user"/>: <c:out value="${principal.name}"/>
        [<c:out value="${principal.username}"/>]</b>
        <x:permission var="hasPermission" module="kacang.services.security.SecurityModule" permission="kacang.services.security.User.update"/>
    </c:when>
    <c:otherwise>
        <b><fmt:message key="security.label.group"/>:
        <c:out value="${principal.name}"/></b>
        <x:permission var="hasPermission" module="kacang.services.security.SecurityModule" permission="kacang.services.security.Group.update"/>
    </c:otherwise>
</c:choose>
<hr size="1">

<c:if test="${hasPermission}">
    <x:display name="contentSecurityPopup.tree" />
</c:if>

</body>
</html>