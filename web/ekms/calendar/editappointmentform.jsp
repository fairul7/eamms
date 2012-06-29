<%@ page import="com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.calendar.model.CalendarEvent"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentformpage">
        <calendarform name="AppointmentForm" prefix="com.tms.collab.calendar.model.Appointment" />
    </page>
</x:config>

<c:if test="${!empty param.eventId}" >
    <x:set name="appointmentformpage.AppointmentForm" property="eventId" value="${param.eventId}" ></x:set>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/ekms/calendar/calendar.jsp"/>
</c:if>

<c:if test="${forward.name=='event updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.eventUpdatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name=='appointment updated'}" >
    <script>
        alert("<fmt:message key='calendar.message.appointmentUpdatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%
        session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/ekms/calendar/conflictsedit.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />

<c:set var="mainBody"><x:display name="appointmentformpage.AppointmentForm"/></c:set>

<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.editAppointment'/> : <c:out value="${widgets['appointmentformpage.AppointmentForm'].title.value}"/> </td></tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td align="center"><c:out value="${mainBody}" escapeXml="false"/></td></tr>
</table>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

<%--

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title><fmt:message key='calendar.label.editAppointment'/></title>
    <link rel="stylesheet" href="<c:url value="/ekms/"/>images/style.css">
</head>
<body class="calendarRow">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <Tr>
   <td align="center" height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
       <fmt:message key='calendar.label.editAppointment'/>
      </font></b></td>

</tr>     <Tr><td>
 <x:display name="appointmentformpage.AppointmentForm" >
    </td></tr></table></x:display>
</body>
</html>
--%>

