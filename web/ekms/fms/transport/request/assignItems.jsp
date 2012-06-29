<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<x:config  reloadable="${param.reload}">
    <page name="assign">
        <com.tms.fms.transport.ui.AssignItemForm name="form" />

    </page>
</x:config>



<c:if test="${forward.name=='Ok'}" >
    <script>
    	window.opener.location.reload();
        window.close();
    </script>
</c:if>

<x:set name="assign.form" property="assgId" value="${param.id}" />   

<c:if test="${!empty param.vehicle_num}" >
    <x:set name="assign.form" property="vehicle_num" value="${param.vehicle_num}" />
</c:if>

<c:if test="${!empty param.type}" >
    <x:set name="assign.form" property="type" value="${param.type}" />
</c:if>

<c:if test="${!empty param.userId}" >
    <x:set name="assign.form" property="userId" value="${param.userId}" />   
</c:if>


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
<x:display name="assign.form" >
</x:display>
</body>
</html>


