<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ page import="kacang.ui.WidgetManager, com.tms.fms.engineering.ui.DetailsRequest" %>
<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_requestDetailsPage">
    <com.tms.fms.engineering.ui.DetailsRequest name="details" width="100%"/>    
  </page>
</x:config>

<x:set name="fms_requestDetailsPage.details" property="mode" value="<%= DetailsRequest.ENGINEERING_STATUS%>"/>


<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_requestDetailsPage.details" property="requestId" value="${requestId}"/>


<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <title><fmt:message key='fms.tran.assignment'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
	<x:display name="fms_requestDetailsPage.details"/>
</body>
</html>
