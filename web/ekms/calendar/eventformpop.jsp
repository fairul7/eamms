<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentformpage">
        <calendarform name="AppointmentForm" prefix="com.tms.collab.calendar.model.CalendarEvent"/>
    </page>
</x:config>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='event added'}" >
    <script>
        alert("<fmt:message key='calendar.message.newEventAddedSuccessfully'/>");
        window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/ekms/calendar/conflictspop.jsp" ></c:redirect>
</c:if>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.addNewEvent'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
        <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key='calendar.label.addNewEvent'/>
        </font></b></td>

    </tr>     <Tr><td>
    <x:display name="appointmentformpage.AppointmentForm" >
</td></tr></table></x:display>
</body>
</html>

