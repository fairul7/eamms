<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="syndicationPreferencePage">
        <com.tms.cms.syndication.ui.SyndicationPreference name="syndicationPreference"/>
    </page>
</x:config>


<c:if test="${forward.name == 'cancel'}">
    <script>
    <!--
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${forward.name == 'added' || forward.name == 'updated' || forward.name == 'deleted' || forward.name == 'subscribed' }">
    <c:redirect url="synPreference.jsp"/>
</c:if>


<html>
<head>
    <title><fmt:message key='syndication.label.header'/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="bookmarkRow">
    <x:display name="syndicationPreferencePage.syndicationPreference"/>
</body>
</html>

