<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>
<x:config>
	<page name="CheckAvailability">
		<com.tms.fms.engineering.ui.CheckAvailability name="form" width="100%"/>
	</page>
</x:config>

<c:if test="${! empty param.id}">
    <c:set var="requestId" value="${param.id}"></c:set>
	<x:set name="CheckAvailability.form" property="requestId" value="${requestId}"/>
</c:if>

<c:choose>
	<c:when test="${forward.name == 'cancel'}">
		<script>window.close();</script>
	</c:when>
	<c:when test="${forward.name == 'continue'}">
		<script>
			window.opener.location="requestListing.jsp";
			alert('<fmt:message key="fms.facility.msg.requestSubmitted"/>');
			window.close();
		</script>
	</c:when>
</c:choose>

<html>
<head>
    <title><fmt:message key='fms.request.label.facilitiesConflict'/> </title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
    <body onload="focus()">

		<x:display name="CheckAvailability.form" ></x:display>

	</body>
</html>