<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<x:config  reloadable="${param.reload}">
    <page name="rejectformpage">
        <com.tms.fms.transport.ui.NotFulfilledForm name="cancelform" />

    </page>
</x:config>

<c:if test="${!empty param.id}" >
    <x:set name="rejectformpage.cancelform" property="id" value="${param.id}" />
</c:if>

<c:if test="${!empty param.vehicleNum}" >
    <x:set name="rejectformpage.cancelform" property="vehicleNum" value="${param.vehicleNum}" />
</c:if>

<c:if test="${forward.name=='error-unfulfill'}" >
    <script>
    	alert('<fmt:message key="fms.tran.msg.errorUnfulfill"/>');
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='rejected'}" >
    <script>
    	alert('<fmt:message key="fms.tran.msg.unfulfill"/>');
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <title><fmt:message key='fms.label.notFulfilled'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
<x:display name="rejectformpage.cancelform" >
</x:display>
</body>
</html>


